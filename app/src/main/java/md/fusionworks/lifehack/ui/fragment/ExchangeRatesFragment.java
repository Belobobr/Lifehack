package md.fusionworks.lifehack.ui.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.Bind;
import com.afollestad.materialdialogs.MaterialDialog;
import com.badoo.mobile.util.WeakHandler;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jakewharton.rxbinding.widget.RxAdapterView;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.data.repository.ExchangeRatesRepository;
import md.fusionworks.lifehack.ui.adapter.BankSpinnerAdapter;
import md.fusionworks.lifehack.ui.event.ScrollToEvent;
import md.fusionworks.lifehack.ui.model.exchange_rates.BankModel;
import md.fusionworks.lifehack.ui.model.exchange_rates.BankSpinnerItemModel;
import md.fusionworks.lifehack.ui.model.exchange_rates.BestExchangeModel;
import md.fusionworks.lifehack.ui.model.exchange_rates.BranchModel;
import md.fusionworks.lifehack.ui.model.exchange_rates.CurrencyModel;
import md.fusionworks.lifehack.ui.model.exchange_rates.InitialDataModel;
import md.fusionworks.lifehack.ui.model.exchange_rates.RateModel;
import md.fusionworks.lifehack.ui.widget.CurrenciesGroup;
import md.fusionworks.lifehack.ui.widget.DateView;
import md.fusionworks.lifehack.util.BranchUtil;
import md.fusionworks.lifehack.util.Constant;
import md.fusionworks.lifehack.util.Converter;
import md.fusionworks.lifehack.util.DateUtil;
import md.fusionworks.lifehack.util.ExchangeRatesUtil;
import md.fusionworks.lifehack.util.MapUtil;
import md.fusionworks.lifehack.util.rx.ObservableTransformation;
import md.fusionworks.lifehack.util.rx.ObserverAdapter;
import rx.Observable;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeRatesFragment extends BaseFragment {

  @Bind(R.id.amountInField) EditText amountInField;
  @Bind(R.id.amountOutField) EditText amountOutField;
  @Bind(R.id.bankSpinner) Spinner bankSpinner;
  @Bind(R.id.bestExchangeBankField) TextView bestExchangeBankField;
  @Bind(R.id.ratesDateField) DateView ratesDateField;
  @Bind(R.id.currencyInRadioGroup) CurrenciesGroup currenciesInGroup;
  @Bind(R.id.currencyOutRadioGroup) CurrenciesGroup currenciesOutGroup;
  @Bind(R.id.exchangeRatesView) LinearLayout exchangeRatesView;
  @Bind(R.id.retryView) RelativeLayout retryView;
  @Bind(R.id.retryButton) Button retryButton;
  @Bind(R.id.whereToBuyButton) TextView whereToBuyButton;
  @Bind(R.id.onlyActiveNowCheckBox) CheckBox onlyActiveNowCheckBox;
  @Bind(R.id.branchesLayout) LinearLayout branchesLayout;
  @Bind(R.id.branchesListLayout) LinearLayout branchesListLayout;
  @Bind(R.id.map) MapView mapView;
  @Bind(R.id.imageOverMap) ImageView imageOverMap;

  private List<RateModel> rateList;
  private List<BankModel> bankList;
  private List<CurrencyModel> currencyList;

  private ExchangeRatesRepository exchangeRatesRepository;
  private BankSpinnerAdapter bankSpinnerAdapter;
  private GoogleMap map;
  private Map<Marker, BranchModel> branchMap = new HashMap<>();
  private GoogleApiClient googleApiClient;
  private Location myLastLocation;
  private ArrayList<Polyline> routePolylines;
  private WeakHandler weakHandler;

  public ExchangeRatesFragment() {
  }

  public static ExchangeRatesFragment newInstance() {
    return new ExchangeRatesFragment();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    exchangeRatesRepository = new ExchangeRatesRepository();
    bankSpinnerAdapter = new BankSpinnerAdapter(getActivity());
    routePolylines = new ArrayList<>();
    weakHandler = new WeakHandler();
    buildGoogleApiClient();
  }

  @Override public void onStart() {
    super.onStart();
    googleApiClient.connect();
  }

  @Override public void onStop() {
    super.onStop();
    if (googleApiClient.isConnected()) {
      googleApiClient.disconnect();
    }
  }

  @Override public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override public void onDestroy() {
    weakHandler.removeCallbacksAndMessages(null);
    mapView.onDestroy();
    super.onDestroy();
  }

  @Override public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflateAndBindViews(inflater, R.layout.fragment_exchange_rates, container);
    initializeMap(savedInstanceState);
    return view;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    loadInitialData();
  }

  private void initializeMap(Bundle savedInstanceState) {
    mapView.onCreate(savedInstanceState);
    enableMapGesturesInScrollView();
    mapView.getMapAsync(googleMap -> {
      map = googleMap;
      map.getUiSettings().setMyLocationButtonEnabled(true);
      map.getUiSettings().setZoomControlsEnabled(true);
      map.getUiSettings().setCompassEnabled(true);
      map.setMyLocationEnabled(true);
      MapsInitializer.initialize(getActivity());

      map.setOnMarkerClickListener(marker -> {
        if (branchMap.get(marker) != null) {
          smoothShowInfoWindow(branchMap.get(marker));
        }
        return true;
      });
    });
  }

  private void pinHomeMarker(Location location) {
    if (location != null) {
      MapUtil.createMarker(map, location.getLatitude(), location.getLongitude(),
          R.drawable.home_pin_icon);
      MapUtil.goToPosition(map, location.getLatitude(), location.getLongitude(), false,
          Constant.CAMERA_ZOOM);
    }
  }

  private void showInfoWindow(BranchModel branch) {
    MaterialDialog dialog = new MaterialDialog.Builder(getActivity()).title(branch.getName())
        .customView(R.layout.info_window_view, true)
        .positiveText("Закрыть")
        .build();

    TextView addressField = (TextView) dialog.getCustomView().findViewById(R.id.addressField);
    addressField.setText(BranchUtil.getBranchAddress(branch));

    dialog.show();
  }

  private void enableMapGesturesInScrollView() {
    imageOverMap.setOnTouchListener((v, event) -> {
      int action = event.getAction();
      switch (action) {
        case MotionEvent.ACTION_DOWN:
          exchangeRatesView.requestDisallowInterceptTouchEvent(true);
          return false;
        case MotionEvent.ACTION_UP:
          exchangeRatesView.requestDisallowInterceptTouchEvent(false);
          return true;
        case MotionEvent.ACTION_MOVE:
          exchangeRatesView.requestDisallowInterceptTouchEvent(true);
          return false;
        default:
          return true;
      }
    });
  }

  private void populateBranchesMap(List<BranchModel> branchList) {
    map.clear();
    pinHomeMarker(myLastLocation);
    for (BranchModel branch : branchList) {
      Marker marker = MapUtil.createMarker(map, branch.getAddress().getLocation().getLat(),
          branch.getAddress().getLocation().getLng(), R.drawable.exchange_pin_icon);
      branchMap.put(marker, branch);
    }
  }

  private void loadInitialData() {
    showLoadingDialog();
    Observable<List<BankModel>> bankObservable = exchangeRatesRepository.getBanks()
        .compose(ObservableTransformation.applyIOToMainThreadSchedulers());

    Observable<List<CurrencyModel>> currencyObservable = exchangeRatesRepository.getCurrencies()
        .compose(ObservableTransformation.applyIOToMainThreadSchedulers());

    String today = DateUtil.getRateDateFormat().format(new Date());
    Observable<List<RateModel>> rateObservable = exchangeRatesRepository.getRates(today)
        .compose(ObservableTransformation.applyIOToMainThreadSchedulers());

    Observable.zip(bankObservable, currencyObservable, rateObservable,
        (bankModels, currencyModels, rateModels) -> new InitialDataModel(bankModels, currencyModels,
            rateModels))
        .compose(ObservableTransformation.applyIOToMainThreadSchedulers())
        .compose(this.bindToLifecycle())
        .subscribe(new ObserverAdapter<InitialDataModel>() {
          @Override public void onNext(InitialDataModel initialDataModel) {
            bankList = initialDataModel.getBankModelList();
            currencyList = initialDataModel.getCurrencyModelList();
            rateList = initialDataModel.getRateModelList();
            onInitialDataLoadedSuccess();
          }

          @Override public void onError(Throwable e) {
            onInitialDataLoadedError();
          }
        });
  }

  private void loadRates(String date) {
    showLoadingDialog();
    exchangeRatesRepository.getRates(date)
        .compose(ObservableTransformation.applyIOToMainThreadSchedulers())
        .compose(this.bindToLifecycle())
        .subscribe(new ObserverAdapter<List<RateModel>>() {
          @Override public void onNext(List<RateModel> rateModels) {
            rateList = rateModels;
            hideLoadingDialog();
            applyConversion();
          }

          @Override public void onError(Throwable e) {
            hideLoadingDialog();
            showNotificationToast(Constant.NOTIFICATION_TOAST_ERROR,
                getString(R.string.field_something_gone_wrong));
          }
        });
  }

  private void loadBankBranches(int bankId, boolean active) {
    showLoadingDialog();
    exchangeRatesRepository.getBranches(bankId, active)
        .compose(ObservableTransformation.applyIOToMainThreadSchedulers())
        .compose(this.bindToLifecycle())
        .subscribe(new ObserverAdapter<List<BranchModel>>() {
          @Override public void onNext(List<BranchModel> branchModels) {
            hideLoadingDialog();
            showBranchesLayout();
            populateBranchesMap(branchModels);
            populateBranchesList(branchModels);
            weakHandler.postDelayed(() -> scrollToMap(), 500);
          }

          @Override public void onError(Throwable e) {
            hideLoadingDialog();
            showNotificationToast(Constant.NOTIFICATION_TOAST_ERROR,
                getString(R.string.field_something_gone_wrong));
          }
        });
  }

  private void onInitialDataLoadedSuccess() {
    hideLoadingDialog();
    hideRetryView();
    showExchangeRatesView();
    populateCurrencyInGroup(currencyList);
    populateCurrencyOutGroup(currencyList);
    populateBankSpinner(bankList);
    setupUIWithDefaultValues();
    initializeViewListeners();
  }

  private void onInitialDataLoadedError() {
    hideLoadingDialog();
    hideExchangeRatesView();
    showRetryView();
  }

  private void showExchangeRatesView() {
    exchangeRatesView.setVisibility(View.VISIBLE);
  }

  private void hideExchangeRatesView() {
    exchangeRatesView.setVisibility(View.GONE);
  }

  private void showRetryView() {
    retryView.setVisibility(View.VISIBLE);
    retryButton.setOnClickListener(v -> loadInitialData());
  }

  private void hideRetryView() {
    retryView.setVisibility(View.GONE);
  }

  private void populateCurrencyInGroup(List<CurrencyModel> currencyList) {
    currenciesInGroup.addCurrencies(currencyList);
  }

  private void populateCurrencyOutGroup(List<CurrencyModel> currencyList) {
    currenciesOutGroup.addCurrencies(currencyList);
  }

  private void populateBankSpinner(List<BankModel> bankList) {
    bankSpinnerAdapter.clear();
    bankSpinnerAdapter.addItem(getActivity().getString(R.string.spinner_option_best_exchange), 0);
    bankSpinnerAdapter.addHeader(getActivity().getString(R.string.spinner_option_bank_list));
    for (BankModel bank : bankList) {
      bankSpinnerAdapter.addItem(bank.getName(), bank.getId());
    }
    bankSpinner.setAdapter(bankSpinnerAdapter);
  }

  private void setupUIWithDefaultValues() {
    setAmountInText(String.valueOf(Constant.DEFAULT_AMOUNT_IN_VALUE));
    setCurrencyInCheckedById(Constant.DEFAULT_CURRENCY_IN_ID);
    setCurrencyOutCheckedById(Constant.DEFAULT_CURRENCY_OUT_ID);
    setBankSelection(Constant.DEFAULT_BANK_ID);
    initializeViewListeners();
  }

  private void setAmountInText(String text) {
    amountInField.setText(text);
  }

  private String getAmountInText() {
    return amountInField.getText().toString();
  }

  private void setAmountOutText(String text) {
    amountOutField.setText(text);
  }

  private void setCurrencyInCheckedById(int currencyId) {
    currenciesInGroup.setCurrencyCheckedById(currencyId);
  }

  private void setCurrencyOutCheckedById(int currencyId) {
    currenciesOutGroup.setCurrencyCheckedById(currencyId);
  }

  private void setBankSelection(int position) {
    bankSpinner.setSelection(position);
  }

  private void initializeViewListeners() {
    getCompositeSubscription().add(RxTextView.textChanges(amountInField).subscribe(charSequence -> {
      applyConversion();
    }));

    getCompositeSubscription().add(RxAdapterView.itemSelections(bankSpinner).subscribe(position -> {
      if (position != Constant.BEST_EXCHANGE_BANK_ID) setBestExchangeBankText("");
      applyConversion();
    }));

    ratesDateField.setOnDateChangedListener(date -> {
      String dateText = DateUtil.getRateDateFormat().format(date);
      loadRates(dateText);
    });

    currenciesInGroup.setOnCheckedChangeListener(
        (group, checkedId) -> onCurrencyInChanged(checkedId));

    currenciesOutGroup.setOnCheckedChangeListener(
        (group, checkedId) -> onCurrencyOutChanged(checkedId));

    whereToBuyButton.setOnClickListener(v -> onWhereToBuyAction());
  }

  private void onCurrencyInChanged(int checkedId) {
    int currencyInId = checkedId;
    int currencyOutId = getCheckedCurrencyOutId();
    if (currencyInId == currencyOutId) {
      currencyOutCheckNext(checkedId);
    }
    applyConversion();
  }

  private void onCurrencyOutChanged(int checkedId) {
    int currencyOutId = checkedId;
    int currencyInId = getCheckedCurrencyInId();
    if (currencyInId == currencyOutId) {
      currencyInCheckNext(checkedId);
    }
    applyConversion();
  }

  private int getCheckedCurrencyInId() {
    return currenciesInGroup.getCheckedRadioButtonId();
  }

  private int getCheckedCurrencyOutId() {
    return currenciesOutGroup.getCheckedRadioButtonId();
  }

  private void currencyInCheckNext(int checkedId) {
    currenciesInGroup.checkNextCurrency(checkedId);
  }

  private void currencyOutCheckNext(int checkedId) {
    currenciesOutGroup.checkNextCurrency(checkedId);
  }

  private void notifyNoExchangeRates() {
    showNotificationToast(Constant.NOTIFICATION_TOAST_ERROR, getString(R.string.field_no_rate));
    setAmountOutText(Constant.NO_EXCHANGE_RATES_OUT_VALUE);
  }

  private boolean validateConversionParams(List<RateModel> bankRateList, double currencyInRateValue,
      double currencyOutRateValue) {
    if (bankRateList.size() == 0 || currencyInRateValue == 0 || currencyOutRateValue == 0) {
      notifyNoExchangeRates();
      return false;
    }
    return true;
  }

  private BestExchangeModel convertBestExchange() {
    List<RateModel> bankRateList;
    double amountInValue;
    double currencyInRateValue;
    double currencyOutRateValue;
    double amountOutValue = 0;

    BestExchangeModel bestExchangeModel = new BestExchangeModel();
    for (BankModel bank : bankList) {
      if (bank.getId() != 1) {
        bankRateList = ExchangeRatesUtil.getBankRates(rateList, bank.getId());
        amountInValue = Converter.toDouble(getAmountInText());
        currencyInRateValue =
            ExchangeRatesUtil.getCurrencyRateValue(bankRateList, getCheckedCurrencyInId());
        currencyOutRateValue =
            ExchangeRatesUtil.getCurrencyRateValue(bankRateList, getCheckedCurrencyOutId());
        double bankAmountOutValue =
            ExchangeRatesUtil.convert(amountInValue, currencyInRateValue, currencyOutRateValue);
        if (bankAmountOutValue > amountOutValue) {
          amountOutValue = bankAmountOutValue;
          bestExchangeModel = new BestExchangeModel(bank, amountOutValue);
        }
      }

      setAmountOutText(String.format("%.2f", bestExchangeModel.getAmountOutvalue()));

      String bestExchangeBankText =
          (bestExchangeModel.getBank() != null) ? String.format("Используется курс банка %s",
              bestExchangeModel.getBank().getName()) : "Не найден подходящий банк";
      setBestExchangeBankText(bestExchangeBankText);
    }

    return bestExchangeModel;
  }

  private void applyConversion() {
    int bankId = getSelectedBankId();
    if (bankId == Constant.BEST_EXCHANGE_BANK_ID) {
      convertBestExchange();
    } else {
      convertBank(bankId);
    }
  }

  private void convertBank(int bankId) {

    List<RateModel> bankRateList;
    double amountInValue;
    double currencyInRateValue;
    double currencyOutRateValue;

    bankRateList = ExchangeRatesUtil.getBankRates(rateList, bankId);
    amountInValue = Converter.toDouble(getAmountInText());
    currencyInRateValue =
        ExchangeRatesUtil.getCurrencyRateValue(bankRateList, getCheckedCurrencyInId());
    currencyOutRateValue =
        ExchangeRatesUtil.getCurrencyRateValue(bankRateList, getCheckedCurrencyOutId());

    if (validateConversionParams(bankRateList, currencyInRateValue, currencyOutRateValue)) {
      double amountOutValue =
          ExchangeRatesUtil.convert(amountInValue, currencyInRateValue, currencyOutRateValue);
      setAmountOutText(String.format("%.2f", amountOutValue));
    }
  }

  private void setBestExchangeBankText(String text) {
    bestExchangeBankField.setVisibility((text.length() > 0) ? View.VISIBLE : View.GONE);
    bestExchangeBankField.setText(text);
  }

  private int getSelectedBankId() {
    BankSpinnerItemModel bankSpinnerItemModel =
        (BankSpinnerItemModel) bankSpinner.getSelectedItem();
    return bankSpinnerItemModel.getBankId();
  }

  private void onWhereToBuyAction() {
    showLoadingDialog();
    int bankId = getSelectedBankId();
    if (bankId == Constant.BNM_BANK_ID) {
      setBankSelection(Constant.BEST_EXCHANGE_BANK_ID);
      bankId = Constant.BEST_EXCHANGE_BANK_ID;
    }
    if (bankId == Constant.BEST_EXCHANGE_BANK_ID) {
      BestExchangeModel bestExchangeModel = convertBestExchange();
      if (bestExchangeModel.getBank() == null) {
        hideLoadingDialog();
        return;
      }
      bankId = bestExchangeModel.getBank().getId();
    }

    boolean onlyActive = onlyActiveNow();
    loadBankBranches(bankId, onlyActive);
  }

  private boolean onlyActiveNow() {
    return onlyActiveNowCheckBox.isChecked();
  }

  private void showBranchesLayout() {
    branchesLayout.setVisibility(View.VISIBLE);
  }

  private void populateBranchesList(List<BranchModel> branchList) {
    LayoutInflater layoutInflater = getActivity().getLayoutInflater();
    branchesListLayout.removeAllViews();

    for (BranchModel branch : branchList) {
      View v = layoutInflater.inflate(R.layout.branches_list_item, null, false);
      TextView nameField = (TextView) v.findViewById(R.id.nameField);
      TextView addressField = (TextView) v.findViewById(R.id.addressField);
      TextView scheduleField = (TextView) v.findViewById(R.id.scheduleField);
      TextView workField = (TextView) v.findViewById(R.id.workField);

      nameField.setText(branch.getName());
      addressField.setText(BranchUtil.getBranchAddress(branch));
      String sheduleText = BranchUtil.getBranchMondayFridayHours(branch)
          .concat(BranchUtil.getBranchMondayFridayScheduleBreak(branch))
          .concat(BranchUtil.getBranchSaturdayHours(branch))
          .concat(BranchUtil.getBranchSaturdayScheduleBreak(branch));
      scheduleField.setText(sheduleText);
      workField.setText(BranchUtil.getClosingTime(branch));

      nameField.setOnClickListener(v1 -> {
        scrollToMap();
        smoothShowInfoWindow(branch);
      });
      workField.setOnClickListener(v1 -> {
        scrollToMap();
        showRouteOnMap(branch);
      });
      branchesListLayout.addView(v);
    }
  }

  protected synchronized void buildGoogleApiClient() {
    googleApiClient = new GoogleApiClient.Builder(getActivity()).addConnectionCallbacks(
        new GoogleApiClient.ConnectionCallbacks() {
          @Override public void onConnected(@Nullable Bundle bundle) {
            myLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            pinHomeMarker(myLastLocation);
          }

          @Override public void onConnectionSuspended(int i) {
            googleApiClient.connect();
          }
        }).addOnConnectionFailedListener(connectionResult -> {

    }).addApi(LocationServices.API).build();
  }

  private void showRouteOnMap(BranchModel branch) {
    if (myLastLocation != null) {
      showLoadingDialog();
      Routing routing = new Routing.Builder().travelMode(AbstractRouting.TravelMode.DRIVING)
          .withListener(new RoutingListener() {
            @Override public void onRoutingFailure(RouteException e) {
              hideLoadingDialog();
              showNotificationToast(Constant.NOTIFICATION_TOAST_ERROR,
                  getString(R.string.field_something_gone_wrong));
            }

            @Override public void onRoutingStart() {

            }

            @Override public void onRoutingSuccess(ArrayList<Route> arrayList, int j) {
              hideLoadingDialog();
              MapUtil.goToPosition(map, branch.getAddress().getLocation().getLat(),
                  branch.getAddress().getLocation().getLng(), false);

              if (routePolylines.size() > 0) {
                for (Polyline poly : routePolylines) {
                  poly.remove();
                }
              }
              routePolylines = new ArrayList<>(arrayList.size());

              for (int i = 0; i < arrayList.size(); i++) {
                PolylineOptions polyOptions = new PolylineOptions();
                polyOptions.color(ContextCompat.getColor(getActivity(), R.color.mainColorBlue));
                polyOptions.width(10 + i * 3);
                polyOptions.addAll(arrayList.get(i).getPoints());
                Polyline polyline = map.addPolyline(polyOptions);
                routePolylines.add(polyline);
              }
            }

            @Override public void onRoutingCancelled() {

            }
          })
          .alternativeRoutes(false)
          .waypoints(new LatLng(myLastLocation.getLatitude(), myLastLocation.getLongitude()),
              new LatLng(branch.getAddress().getLocation().getLat(),
                  branch.getAddress().getLocation().getLng()))
          .build();
      routing.execute();
    } else {
      showNotificationToast(Constant.NOTIFICATION_TOAST_ERROR,
          getString(R.string.field_something_gone_wrong));
    }
  }

  private void scrollToMap() {
    getRxBus().post(new ScrollToEvent(0, mapView.getBottom()));
  }

  private void smoothShowInfoWindow(BranchModel branch) {
    weakHandler.postDelayed(() -> showInfoWindow(branch), 100);
    weakHandler.postDelayed(
        () -> MapUtil.goToPosition(map, branch.getAddress().getLocation().getLat(),
            branch.getAddress().getLocation().getLng(), false), 200);
  }
}
