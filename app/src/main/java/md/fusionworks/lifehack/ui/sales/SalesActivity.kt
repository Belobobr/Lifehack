package md.fusionworks.lifehack.ui.sales

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_sales.*
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.data.repository.SalesRepository
import md.fusionworks.lifehack.ui.NavigationDrawerActivity
import md.fusionworks.lifehack.ui.base.view.LoadMoreAdapter
import md.fusionworks.lifehack.ui.sales.model.ProductModel
import md.fusionworks.lifehack.ui.sales.model.SaleCategoryModel
import md.fusionworks.lifehack.ui.widget.RetryView
import md.fusionworks.lifehack.util.AndroidUtil
import md.fusionworks.lifehack.util.Constant
import md.fusionworks.lifehack.util.DateUtil
import md.fusionworks.lifehack.util.LocaleHelper
import md.fusionworks.lifehack.util.rx.ObservableTransformation
import md.fusionworks.lifehack.util.rx.ObserverAdapter
import md.fusionworks.lifehack.util.rx.RxBus
import rx.Observable
import java.util.*

class SalesActivity : NavigationDrawerActivity() {

  private lateinit var salesRepository: SalesRepository
  private lateinit var saleCategorySpinnerAdapter: SaleCategorySpinnerAdapter
  private lateinit var saleProductAdapter: SaleProductAdapter

  private var productListOffset = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sales)
    salesRepository = SalesRepository()
    getSaleCategories()
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    setTitle(getString(R.string.title_today_sales))
  }

  override fun listenForEvents() {
    super.listenForEvents()
    RxBus.event(NavigateToUrlEvent::class.java).compose(
        bindToLifecycle<NavigateToUrlEvent>()).subscribe { navigateToUrlEvent ->
      val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(
          navigateToUrlEvent.url))
      val isChromeInstalled = AndroidUtil.isPackageInstalled(Constant.APP_CHROME,
          saleCategorySpinner.context)
      if (isChromeInstalled) {
        browserIntent.`package` = Constant.APP_CHROME
        startActivity(browserIntent)
      } else {
        val chooserIntent = Intent.createChooser(browserIntent, getString(R.string.choose_app))
        startActivity(chooserIntent)
      }
    }
  }

  override fun getSelfDrawerItem() = Constant.DRAWER_ITEM_SALES

  private fun initializeSaleCategorySpinner(saleCategoryModelList: List<SaleCategoryModel>) {
    saleCategorySpinnerAdapter = SaleCategorySpinnerAdapter(this, saleCategoryModelList,
        LocaleHelper.getLanguage(this))
    saleCategorySpinner.adapter = saleCategorySpinnerAdapter
    saleCategorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

        if (view != null) {
          productListOffset = 0
          getSaleProducts((saleCategorySpinner.selectedItem as SaleCategoryModel).id,
              productListOffset, true)
        }
      }

      override fun onNothingSelected(parent: AdapterView<*>) {

      }
    }
  }

  private fun initializeProductList() {
    productList.layoutManager = LinearLayoutManager(this)
    saleProductAdapter = SaleProductAdapter(productList, LocaleHelper.getLanguage(this))
    productList.adapter = saleProductAdapter

    saleProductAdapter.setOnLoadMoreItemsListener(
        object : LoadMoreAdapter.OnLoadMoreItemsListener {
          override fun onLoadMoreItems() {
            getSaleProducts((saleCategorySpinner.selectedItem as SaleCategoryModel).id,
                productListOffset, false)
            productListOffset += Constant.LIMIT
          }
        })
  }

  private fun getSaleCategories() {
    showLoadingDialog()
    loadingDialogCancelSubscription.add(salesRepository.saleCategories
        .compose(
            ObservableTransformation.applyIOToMainThreadSchedulers<List<SaleCategoryModel>>())
        .compose(bindToLifecycle<List<SaleCategoryModel>>())
        .map { addAllCategoriesItem(it.toMutableList()) }
        .subscribe(object : ObserverAdapter<List<SaleCategoryModel>>() {
          override fun onNext(categoryModelList: List<SaleCategoryModel>) {
            hideLoadingDialog()
            hideRetryView()
            showTodaySalesView()
            initializeSaleCategorySpinner(categoryModelList)
            initializeProductList()
          }

          override fun onError(e: Throwable) {
            hideLoadingDialog()
            hideTodaySalesView()
            showRetryView()
          }
        }))
  }

  private fun getSaleProducts(categoryId: Int, offset: Int, swap: Boolean) {
    if (swap) showLoadingDialog()
    loadingDialogCancelSubscription.add(
        salesRepository.getSaleProducts(todayDateString, Constant.MIN_RANGE,
            Constant.MAX_RANGE, Constant.LANG, Constant.SORT, Constant.LIMIT, offset, categoryId,
            Constant.PRICES_API_KEY)
            .compose(ObservableTransformation
                .applyIOToMainThreadSchedulers<List<ProductModel>>())
            .compose(bindToLifecycle<List<ProductModel>>())
            .flatMap { Observable.from(it) }
            .map {
              it.categoryId = categoryId
              it
            }
            .toList()
            .subscribe(object : ObserverAdapter<List<ProductModel>>() {
              override fun onNext(productModelList: List<ProductModel>) {
                if (swap) {
                  hideLoadingDialog()
                  saleProductAdapter.swap(productModelList)
                } else {
                  saleProductAdapter.addItems(productModelList)
                }
              }

              override fun onError(e: Throwable) {
                if (swap) hideLoadingDialog()
                showNotificationToast(Constant.NOTIFICATION_TOAST_ERROR,
                    getString(R.string.error_something_gone_wrong))
              }
            }))
  }

  private fun addAllCategoriesItem(
      categoryModelList: MutableList<SaleCategoryModel>): List<SaleCategoryModel> {
    categoryModelList.add(0, SaleCategoryModel(0, getString(R.string.all_categories),
        getString(R.string.all_categories)))
    return categoryModelList
  }

  private val todayDateString: String
    get() = DateUtil.getSaleDateToFormat().format(Date())

  private fun showRetryView() {
    retryView.show()
    retryView.setOnRetryActionListener(object : RetryView.OnRetryActionListener {
      override fun onRetryAction() {
        retryView.hide()
        getSaleCategories()
      }
    })
  }

  private fun hideRetryView() = retryView.hide()

  private fun showTodaySalesView() {
    todaySalesView.visibility = View.VISIBLE
  }

  private fun hideTodaySalesView() {
    todaySalesView.visibility = View.GONE
  }
}
