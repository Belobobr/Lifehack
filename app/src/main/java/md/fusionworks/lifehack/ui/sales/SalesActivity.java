package md.fusionworks.lifehack.ui.sales;

import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import butterknife.Bind;
import java.util.List;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.data.repository.SalesRepository;
import md.fusionworks.lifehack.ui.NavigationDrawerActivity;
import md.fusionworks.lifehack.ui.sales.model.ProductModel;
import md.fusionworks.lifehack.ui.sales.model.SaleCategoryModel;
import md.fusionworks.lifehack.util.Constant;
import md.fusionworks.lifehack.util.rx.ObservableTransformation;
import md.fusionworks.lifehack.util.rx.ObserverAdapter;

public class SalesActivity extends NavigationDrawerActivity {

  @Bind(R.id.saleCategorySpinner) AppCompatSpinner saleCategorySpinner;

  private SalesRepository salesRepository;
  private SaleCategorySpinnerAdapter saleCategorySpinnerAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sales);
    salesRepository = new SalesRepository();

    getAllSaleCategories();
  }

  @Override public void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    setTitle("Скидки");
  }

  @Override public int getSelfDrawerItem() {
    return Constant.DRAWER_ITEM_SALES;
  }

  private void initializeSaleCategorySpinner(List<SaleCategoryModel> saleCategoryModelList) {
    saleCategorySpinnerAdapter = new SaleCategorySpinnerAdapter(this, saleCategoryModelList);
    saleCategorySpinner.setAdapter(saleCategorySpinnerAdapter);
  }

  private void getAllSaleCategories() {
    showLoadingDialog();
    salesRepository.getCategories()
        .compose(ObservableTransformation.applyIOToMainThreadSchedulers())
        .compose(this.bindToLifecycle())
        .map(this::addAllCategoriesItem)
        .subscribe(new ObserverAdapter<List<SaleCategoryModel>>() {
          @Override public void onNext(List<SaleCategoryModel> categoryModelList) {
            hideLoadingDialog();
            initializeSaleCategorySpinner(categoryModelList);
            getProducts();
          }

          @Override public void onError(Throwable e) {
            hideLoadingDialog();
            showNotificationToast(Constant.NOTIFICATION_TOAST_ERROR,
                getString(R.string.field_something_gone_wrong));
          }
        });
  }

  private void getProducts() {
    showLoadingDialog();
    salesRepository.getProducts()
        .compose(ObservableTransformation.applyIOToMainThreadSchedulers())
        .compose(this.bindToLifecycle())
        .subscribe(new ObserverAdapter<List<ProductModel>>() {
          @Override public void onNext(List<ProductModel> categoryModelList) {
            hideLoadingDialog();
          }

          @Override public void onError(Throwable e) {
            hideLoadingDialog();
            showNotificationToast(Constant.NOTIFICATION_TOAST_ERROR,
                getString(R.string.field_something_gone_wrong));
          }
        });
  }

  private List<SaleCategoryModel> addAllCategoriesItem(List<SaleCategoryModel> categoryModelList) {
    categoryModelList.add(0, new SaleCategoryModel(0, "Все категории", "Toate categoriile"));
    return categoryModelList;
  }
}
