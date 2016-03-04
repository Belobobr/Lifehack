package md.fusionworks.lifehack.ui.taxi;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.Bind;
import io.realm.Realm;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.data.persistence.taxi.TaxiDataStore;
import md.fusionworks.lifehack.data.repository.TaxiRepository;
import md.fusionworks.lifehack.ui.NavigationDrawerActivity;
import md.fusionworks.lifehack.util.Constant;
import md.fusionworks.lifehack.util.rx.ObservableTransformation;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class TaxiActivity extends NavigationDrawerActivity {

  @Bind(R.id.taxiPhoneNumberList) RecyclerView taxiPhoneNumberList;
  @Bind(R.id.lastUsedTaxiPhoneNumberList) RecyclerView lastUsedTaxiPhoneNumberList;

  private TaxiRepository taxiRepository;
  private TaxiPhoneNumberAdapter taxiPhoneNumberAdapter;
  private TaxiPhoneNumberAdapter lastUsedTaxiPhoneNumberAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_taxi);
    taxiRepository = new TaxiRepository(Realm.getDefaultInstance(),
        new TaxiDataStore(Realm.getDefaultInstance()));
  }

  @Override protected void onResume() {
    super.onResume();
    getAllPhoneNumbers();
  }

  @Override protected void listenForEvents() {
    super.listenForEvents();
    getRxBus().event(TaxiPhoneNumberClickEvent.class)
        .compose(bindToLifecycle())
        .subscribe(taxiPhoneNumberClickEvent -> {
          onPhoneNumberClick(taxiPhoneNumberClickEvent.getTaxiPhoneNumberModel().getPhoneNumber());
        });
  }

  @Override public void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    setTitle(getString(R.string.title_taxi));
  }

  @Override public int getSelfDrawerItem() {
    return Constant.DRAWER_ITEM_TAXI;
  }

  private void initializeTaxiPhoneNumberList(List<TaxiPhoneNumberModel> taxiPhoneNumberModelList) {
    taxiPhoneNumberList.setLayoutManager(new GridLayoutManager(this, 4));
    taxiPhoneNumberAdapter = new TaxiPhoneNumberAdapter(taxiPhoneNumberModelList);
    taxiPhoneNumberList.setAdapter(taxiPhoneNumberAdapter);

    getLastUsedPhoneNumbers(Observable.just(taxiPhoneNumberModelList));
  }

  private void initializeLastUsedTaxiPhoneNumberList(
      List<TaxiPhoneNumberModel> taxiPhoneNumberModelList) {
    lastUsedTaxiPhoneNumberList.setLayoutManager(new GridLayoutManager(this, 4));
    lastUsedTaxiPhoneNumberAdapter = new TaxiPhoneNumberAdapter(taxiPhoneNumberModelList);
    lastUsedTaxiPhoneNumberList.setAdapter(lastUsedTaxiPhoneNumberAdapter);
  }

  private void getAllPhoneNumbers() {
    taxiRepository.getAllPhoneNumbers()
        .observeOn(AndroidSchedulers.mainThread())
        .compose(this.bindToLifecycle())
        .map(this::sortPhoneNumberListByPhoneNUmber)
        .subscribe(this::initializeTaxiPhoneNumberList);
  }

  private void getLastUsedPhoneNumbers(Observable<List<TaxiPhoneNumberModel>> observable) {
    observable.compose(ObservableTransformation.applyIOToMainThreadSchedulers())
        .compose(this.bindToLifecycle())
        .flatMap(taxiPhoneNumberModels -> Observable.from(new ArrayList<>(taxiPhoneNumberModels)))
        .filter(taxiPhoneNumberModel -> taxiPhoneNumberModel.getLastCallDate() != null)
        .toList()
        .map(this::sortLastUsedPhoneNumberListByDateDesc)
        .flatMap(taxiPhoneNumberModels -> Observable.from(new ArrayList<>(taxiPhoneNumberModels)))
        .take(4)
        .toList()
        .subscribe(this::initializeLastUsedTaxiPhoneNumberList);
  }

  private List<TaxiPhoneNumberModel> sortLastUsedPhoneNumberListByDateDesc(
      List<TaxiPhoneNumberModel> taxiPhoneNumberModelList) {
    Collections.sort(taxiPhoneNumberModelList,
        (lhs, rhs) -> rhs.getLastCallDate().compareTo(lhs.getLastCallDate()));
    return taxiPhoneNumberModelList;
  }

  private List<TaxiPhoneNumberModel> sortPhoneNumberListByPhoneNUmber(
      List<TaxiPhoneNumberModel> taxiPhoneNumberModelList) {
    Collections.sort(taxiPhoneNumberModelList,
        (lhs, rhs) -> lhs.getPhoneNumber() - rhs.getPhoneNumber());
    return taxiPhoneNumberModelList;
  }

  private void onPhoneNumberClick(int phoneNumber) {
    taxiRepository.updateLastUsedDate(phoneNumber);
    getNavigator().navigateToCallActivity(this, phoneNumber);
  }
}
