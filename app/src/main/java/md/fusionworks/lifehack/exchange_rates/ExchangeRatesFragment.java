package md.fusionworks.lifehack.exchange_rates;


import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.Marker;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.data.api.model.exchange_rates.Branch;
import md.fusionworks.lifehack.data.repository.ExchangeRatesRepository;
import md.fusionworks.lifehack.ui.fragment.BaseFragment;
import md.fusionworks.lifehack.ui.adapter.BankSpinnerAdapter;
import md.fusionworks.lifehack.ui.model.exchange_rates.BankModel;
import md.fusionworks.lifehack.ui.model.exchange_rates.BankSpinnerItemModel;
import md.fusionworks.lifehack.ui.model.exchange_rates.BranchModel;
import md.fusionworks.lifehack.ui.model.exchange_rates.CurrencyModel;
import md.fusionworks.lifehack.ui.widget.CurrenciesGroup;
import md.fusionworks.lifehack.ui.widget.DateView;
import md.fusionworks.lifehack.util.DateUtils;
import md.fusionworks.lifehack.util.MapUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeRatesFragment extends BaseFragment implements ExchangeRatesContract.View {

    public static final float CAMERA_ZOOM = 12f;
    @Bind(R.id.amountInField)
    EditText amountInField;
    @Bind(R.id.amountOutField)
    EditText amountOutField;
    @Bind(R.id.bankSpinner)
    Spinner bankSpinner;
    @Bind(R.id.bestExchangeBankField)
    TextView bestExchangeBankField;
    @Bind(R.id.ratesDateField)
    DateView ratesDateField;
    @Bind(R.id.currencyInRadioGroup)
    CurrenciesGroup currenciesInGroup;
    @Bind(R.id.currencyOutRadioGroup)
    CurrenciesGroup currenciesOutGroup;
    @Bind(R.id.exchangeRatesView)
    LinearLayout exchangeRatesView;
    @Bind(R.id.retryView)
    RelativeLayout retryView;
    @Bind(R.id.retryButton)
    Button retryButton;
    @Bind(R.id.whereToBuyButton)
    TextView whereToBuyButton;
    @Bind(R.id.onlyActiveNowCheckBox)
    CheckBox onlyActiveNowCheckBox;
    @Bind(R.id.branchesLayout)
    LinearLayout branchesLayout;
    @Bind(R.id.branchesListLayout)
    LinearLayout branchesListLayout;
    @Bind(R.id.map)
    MapView mapView;

    private ExchangeRatesContract.UserActionsListener userActionsListener;
    private BankSpinnerAdapter bankSpinnerAdapter;
    private MaterialDialog loadingInitialDataDialog;
    private GoogleMap map;
    private Map<Marker, BranchModel> branchMap = new HashMap<>();

    public static ExchangeRatesFragment newInstance() {

        return new ExchangeRatesFragment();
    }

    public ExchangeRatesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_exchange_rates, container, false);
        ButterKnife.bind(this, v);

        initializeMap(savedInstanceState);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userActionsListener = new ExchangeRatesPresenter(this, new ExchangeRatesRepository(getActivity()));
        userActionsListener.loadInitialData();
        bankSpinnerAdapter = new BankSpinnerAdapter(getActivity());
        retryButton.setOnClickListener(v -> {

            userActionsListener.onRetryAction();
        });
    }

    private void initializeMap(Bundle savedInstanceState) {

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(googleMap -> {
            map = googleMap;
            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.setMyLocationEnabled(true);

            MapsInitializer.initialize(getActivity());

            showMyLocationOnMap();

            map.setOnMarkerClickListener(marker -> {

                if (branchMap.get(marker) != null)
                    userActionsListener.onShowInfoWindowAction(branchMap.get(marker));
                return true;
            });
        });
    }

    private void showMyLocationOnMap() {

        Location location = map.getMyLocation();
        if (location != null) {

            pinHomeMarker(location);
        } else
            map.setOnMyLocationChangeListener(arg0 -> {

                pinHomeMarker(arg0);
                map.setOnMyLocationChangeListener(null);
            });
    }

    private void pinHomeMarker(Location location) {

        MapUtil.createMarker(map, location.getLatitude(), location.getLongitude(), R.drawable.home_pin_icon);
        MapUtil.goToPosition(map, location.getLatitude(), location.getLongitude(), false, CAMERA_ZOOM);
    }

    @Override
    public void showLoading(int resourceId) {

        String text = getString(resourceId);

        if (loadingInitialDataDialog == null) {
            loadingInitialDataDialog = new MaterialDialog.Builder(getActivity())
                    .content(text)
                    .progress(true, 0)
                    .cancelable(false)
                    .progressIndeterminateStyle(true)
                    .show();
        }

        if (!loadingInitialDataDialog.isShowing())
            loadingInitialDataDialog.show();
    }

    @Override
    public void hideLoading() {

        if (loadingInitialDataDialog != null)
            if (loadingInitialDataDialog.isShowing())
                loadingInitialDataDialog.hide();

        loadingInitialDataDialog = null;
    }

    @Override
    public void setAmountInText(String text) {

        amountInField.setText(text);
    }

    @Override
    public String getAmountInText() {

        return amountInField.getText().toString();
    }

    @Override
    public void setAmountOutText(String text) {

        amountOutField.setText(text);
    }

    @Override
    public void initializeViewListeners() {

        amountInField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                userActionsListener.onAmountInChanged(s.toString());
            }
        });

        bankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                userActionsListener.onBankSelected(position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ratesDateField.setOnDateChangedListener(date -> {

            userActionsListener.onRatesDateChanged(date);
        });

        currenciesInGroup.setOnCheckedChangeListener((group, checkedId) -> {

            userActionsListener.onCurrencyInChanged(checkedId);
        });


        currenciesOutGroup.setOnCheckedChangeListener((group, checkedId) -> {

            userActionsListener.onCurrencyOutChanged(checkedId);
        });

        whereToBuyButton.setOnClickListener(v -> {

            userActionsListener.onWhereToBuyAction();
        });
    }

    @Override
    public void populateBankSpinner(List<BankModel> bankList) {

        bankSpinnerAdapter.clear();
        bankSpinnerAdapter.addItem(getActivity().getString(R.string.spinner_option_best_exchange), 0);
        bankSpinnerAdapter.addHeader(getActivity().getString(R.string.spinner_option_bank_list));
        for (BankModel bank : bankList) {

            bankSpinnerAdapter.addItem(bank.getName(), bank.getId());
        }

        bankSpinner.setAdapter(bankSpinnerAdapter);
    }

    @Override
    public int getSelectedBankId() {

        BankSpinnerItemModel bankSpinnerItemModel = (BankSpinnerItemModel) bankSpinner.getSelectedItem();
        return bankSpinnerItemModel.getBankId();
    }

    @Override
    public void setBankSelection(int position) {

        bankSpinner.setSelection(position);
    }

    @Override
    public void setBankSelectionById(int bankId) {

        List<BankSpinnerItemModel> bankSpinnerItemModelList = bankSpinnerAdapter.getAllItems();
        int position = 0;
        for (BankSpinnerItemModel bankSpinnerItemModel : bankSpinnerItemModelList) {

            if (bankSpinnerItemModel.getBankId() == bankId)
                bankSpinner.setSelection(position);

            position++;

        }
    }

    @Override
    public void setBestExchangeBankText(String text) {

        bestExchangeBankField.setText(text);
    }

    @Override
    public void populateCurrencyInGroup(List<CurrencyModel> currencyList) {

        currenciesInGroup.addCurrencies(currencyList);
    }

    @Override
    public void populateCurrencyOutGroup(List<CurrencyModel> currencyList) {

        currenciesOutGroup.addCurrencies(currencyList);
    }

    @Override
    public int getCheckedCurrencyInId() {

        return currenciesInGroup.getCheckedRadioButtonId();
    }

    @Override
    public int getCheckedCurrencyOutId() {

        return currenciesOutGroup.getCheckedRadioButtonId();
    }

    @Override
    public void currencyInCheckNext(int checkedId) {

        currenciesInGroup.checkNextCurrency(checkedId);
    }

    @Override
    public void currencyOutCheckNext(int checkedId) {

        currenciesOutGroup.checkNextCurrency(checkedId);
    }

    @Override
    public void setCurrencyInCheckedById(int currencyId) {

        currenciesInGroup.setCurrencyCheckedById(currencyId);
    }

    @Override
    public void setCurrencyOutCheckedById(int currencyId) {

        currenciesOutGroup.setCurrencyCheckedById(currencyId);
    }

    @Override
    public void showNotificationToast(int type, int stringResId) {

        showNotificationToast(type, getString(stringResId));
//        ((ExchangeRatesActivity) getActivity()).showNotificationToast(type, stringResId);
    }

    @Override
    public void showExchangeRatesView() {

        exchangeRatesView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideExchangeRatesView() {

        exchangeRatesView.setVisibility(View.GONE);
    }

    @Override
    public void showRetryView() {

        retryView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRetryView() {

        retryView.setVisibility(View.GONE);
    }

    @Override
    public boolean onlyActiveNow() {
        return onlyActiveNowCheckBox.isChecked();
    }

    @Override
    public void showBranchesLayout() {

        branchesLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void populateBranchesMap(List<BranchModel> branchList) {

        map.clear();
        showMyLocationOnMap();

        for (BranchModel branch : branchList) {

            Marker marker = MapUtil.createMarker(map, branch.getAddress().getLocation().getLat(), branch.getAddress().getLocation().getLng(), R.drawable.exchange_pin_icon);
            branchMap.put(marker, branch);
        }
    }

    @Override
    public void populateBranchesList(List<BranchModel> branchList) {

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        branchesListLayout.removeAllViews();

        for (BranchModel branch : branchList) {

            View v = layoutInflater.inflate(R.layout.branches_list_item, null, false);
            TextView nameField = (TextView) v.findViewById(R.id.nameField);
            TextView addressField = (TextView) v.findViewById(R.id.addressField);
            TextView scheduleField = (TextView) v.findViewById(R.id.scheduleField);

            nameField.setText(branch.getName());
            addressField.setText(getBranchAddress(branch));
            scheduleField.setText(getBranchScheduleBreak(branch));

            nameField.setOnClickListener(v1 -> {

                userActionsListener.onShowInfoWindowAction(branch);
            });

            branchesListLayout.addView(v);
        }
    }

    private boolean isBranchDetailEmpty(String detail) {

        if (TextUtils.isEmpty(detail) || detail.equals("-"))
            return true;

        return false;
    }

    private String getBranchAddress(BranchModel branch) {

        String address = "";
        String locality = branch.getAddress().getDistrict().getLocality().getName();
        String district = branch.getAddress().getDistrict().getName();
        String street = branch.getAddress().getStreet();
        String house = branch.getAddress().getHouse();
        String phone = branch.getAddress().getPhone();

        if (!isBranchDetailEmpty(locality))
            address += locality;
        if (!isBranchDetailEmpty(district))
            address += " ," + district;
        if (!isBranchDetailEmpty(street) || !isBranchDetailEmpty(house))
            address += " ," + street + " " + house;
        if (!isBranchDetailEmpty(phone))
            address += "\n" + phone;

        return address;
    }

    private String getBranchSchedule(Branch branch) {

        String schedule = "";


        return schedule;
    }

    private String getBranchScheduleBreak(BranchModel branch) {

        String scheduleBreak = "";
        Date breakStart = null;
        Date breakEnd = null;

        if (branch.getSchedule().getMonday().getBreakStart() != null) {

            breakStart = branch.getSchedule().getMonday().getBreakStart();
            breakEnd = branch.getSchedule().getMonday().getBreakEnd();
        } else if (branch.getSchedule().getTuesday().getBreakStart() != null) {

            breakStart = branch.getSchedule().getTuesday().getBreakStart();
            breakEnd = branch.getSchedule().getTuesday().getBreakEnd();
        } else if (branch.getSchedule().getThursday().getBreakStart() != null) {

            breakStart = branch.getSchedule().getThursday().getBreakStart();
            breakEnd = branch.getSchedule().getThursday().getBreakEnd();
        } else if (branch.getSchedule().getWednesday().getBreakStart() != null) {

            breakStart = branch.getSchedule().getWednesday().getBreakStart();
            breakEnd = branch.getSchedule().getWednesday().getBreakEnd();
        } else if (branch.getSchedule().getFriday().getBreakStart() != null) {

            breakStart = branch.getSchedule().getFriday().getBreakStart();
            breakEnd = branch.getSchedule().getFriday().getBreakEnd();
        } else if (branch.getSchedule().getSaturday().getBreakStart() != null) {

            breakStart = branch.getSchedule().getSaturday().getBreakStart();
            breakEnd = branch.getSchedule().getSaturday().getBreakEnd();
        } else if (branch.getSchedule().getSunday().getBreakStart() != null) {

            breakStart = branch.getSchedule().getSunday().getBreakStart();
            breakEnd = branch.getSchedule().getSunday().getBreakEnd();
        }

        if (breakStart != null && breakEnd != null) {

            String start = DateUtils.getBranchScheduleBreakFormat().format(breakStart);
            String end = DateUtils.getBranchScheduleBreakFormat().format(breakEnd);
            scheduleBreak = String.format("(перерыв %s - %s)", start, end);
        }

        return scheduleBreak;
    }

    @Override
    public void showInfoWindow(BranchModel branch) {

        MapUtil.goToPosition(map, branch.getAddress().getLocation().getLat(), branch.getAddress().getLocation().getLng(), false);

        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title(branch.getName())
                .customView(R.layout.info_window_view, true)
                .positiveText("Закрыть")
                .build();

        TextView addressField = (TextView) dialog.getCustomView().findViewById(R.id.addressField);
        addressField.setText(getBranchAddress(branch));

        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
