package md.fusionworks.lifehack.ui.sales;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import butterknife.Bind;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.data.repository.SalesRepository;
import md.fusionworks.lifehack.ui.NavigationDrawerActivity;
import md.fusionworks.lifehack.ui.sales.model.ProductModel;
import md.fusionworks.lifehack.ui.sales.model.SaleCategoryModel;
import md.fusionworks.lifehack.ui.widget.RetryView;
import md.fusionworks.lifehack.util.AndroidUtil;
import md.fusionworks.lifehack.util.Constant;
import md.fusionworks.lifehack.util.DateUtil;
import md.fusionworks.lifehack.util.LocaleHelper;
import md.fusionworks.lifehack.util.rx.ObservableTransformation;
import md.fusionworks.lifehack.util.rx.ObserverAdapter;
import rx.Observable;

public class SalesActivity extends NavigationDrawerActivity {

  @Bind(R.id.saleCategorySpinner) AppCompatSpinner saleCategorySpinner;
  @Bind(R.id.productList) RecyclerView productList;
  @Bind(R.id.retryView) RetryView retryView;
  @Bind(R.id.todaySalesView) View todaySalesView;

  private SalesRepository salesRepository;
  private SaleCategorySpinnerAdapter saleCategorySpinnerAdapter;
  private SaleProductAdapter saleProductAdapter;

  private int productListOffset = 0;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sales);
    salesRepository = new SalesRepository();
    getSaleCategories();
  }

  @Override public void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    setTitle(getString(R.string.title_today_sales));
  }

  @Override protected void listenForEvents() {
    super.listenForEvents();
    getRxBus().event(NavigateToUrlEvent.class)
        .compose(bindToLifecycle())
        .subscribe(navigateToUrlEvent -> {
          Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
              navigateToUrlEvent.getUrl()));
          boolean isChromeInstalled =
              AndroidUtil.isPackageInstalled(Constant.APP_CHROME, SalesActivity.this);
          if (isChromeInstalled) {
            browserIntent.setPackage(Constant.APP_CHROME);
            startActivity(browserIntent);
          } else {
            Intent chooserIntent =
                Intent.createChooser(browserIntent, getString(R.string.choose_app));
            startActivity(chooserIntent);
          }
        });
  }

  @Override public int getSelfDrawerItem() {
    return Constant.DRAWER_ITEM_SALES;
  }

  private void initializeSaleCategorySpinner(List<SaleCategoryModel> saleCategoryModelList) {
    saleCategorySpinnerAdapter =
        new SaleCategorySpinnerAdapter(this, saleCategoryModelList, LocaleHelper.getLanguage(this));
    saleCategorySpinner.setAdapter(saleCategorySpinnerAdapter);
    saleCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (view != null) {
          productListOffset = 0;
          getSaleProducts(((SaleCategoryModel) saleCategorySpinner.getSelectedItem()).getId(),
              productListOffset, true);
        }
      }

      @Override public void onNothingSelected(AdapterView<?> parent) {

      }
    });
  }

  private void initializeProductList() {
    productList.setLayoutManager(new LinearLayoutManager(this));
    saleProductAdapter = new SaleProductAdapter(productList, LocaleHelper.getLanguage(this));
    productList.setAdapter(saleProductAdapter);

    saleProductAdapter.setOnLoadMoreItemsListener(() -> {
      getSaleProducts(((SaleCategoryModel) saleCategorySpinner.getSelectedItem()).getId(),
          productListOffset, false);
      productListOffset += Constant.LIMIT;
    });
  }

  private void getSaleCategories() {
    showLoadingDialog();
    loadingDialogCancelSubscription.add(salesRepository.getSaleCategories()
        .compose(ObservableTransformation.applyIOToMainThreadSchedulers())
        .compose(bindToLifecycle())
        .map(this::addAllCategoriesItem)
        .subscribe(new ObserverAdapter<List<SaleCategoryModel>>() {
          @Override public void onNext(List<SaleCategoryModel> categoryModelList) {
            hideLoadingDialog();
            hideRetryView();
            showTodaySalesView();
            initializeSaleCategorySpinner(categoryModelList);
            initializeProductList();
          }

          @Override public void onError(Throwable e) {
            hideLoadingDialog();
            hideTodaySalesView();
            showRetryView();
          }
        }));
  }

  private void getSaleProducts(int categoryId, int offset, boolean swap) {
    if (swap) showLoadingDialog();
    loadingDialogCancelSubscription.add(
        salesRepository.getSaleProducts(getTodayDateString(), Constant.MIN_RANGE,
            Constant.MAX_RANGE, Constant.LANG, Constant.SORT, Constant.LIMIT, offset, categoryId,
            Constant.PRICES_API_KEY)
            .compose(ObservableTransformation.applyIOToMainThreadSchedulers())
            .compose(bindToLifecycle())
            .flatMap(productModelList -> Observable.from(new ArrayList<>(productModelList)))
            .map(productModel -> {
              productModel.setCategoryId(categoryId);
              return productModel;
            })
            .toList()
            .subscribe(new ObserverAdapter<List<ProductModel>>() {
              @Override public void onNext(List<ProductModel> productModelList) {
                if (swap) {
                  hideLoadingDialog();
                  saleProductAdapter.swap(productModelList);
                } else {
                  saleProductAdapter.addItems(productModelList);
                }
              }

              @Override public void onError(Throwable e) {
                if (swap) hideLoadingDialog();
                showNotificationToast(Constant.NOTIFICATION_TOAST_ERROR,
                    getString(R.string.error_something_gone_wrong));
              }
            }));
  }

  private List<SaleCategoryModel> addAllCategoriesItem(List<SaleCategoryModel> categoryModelList) {
    categoryModelList.add(0, new SaleCategoryModel(0, getString(R.string.all_categories),
        getString(R.string.all_categories)));
    return categoryModelList;
  }

  private String getTodayDateString() {
    return DateUtil.getSaleDateToFormat().format(new Date());
  }

  private void showRetryView() {
    retryView.show();
    retryView.setOnRetryActionListener(() -> {
      retryView.hide();
      getSaleCategories();
    });
  }

  private void hideRetryView() {
    retryView.hide();
  }

  private void showTodaySalesView() {
    todaySalesView.setVisibility(View.VISIBLE);
  }

  private void hideTodaySalesView() {
    todaySalesView.setVisibility(View.GONE);
  }
}
