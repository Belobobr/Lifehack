package md.fusionworks.lifehack.ui.sales;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import com.bumptech.glide.Glide;
import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.ui.BaseViewHolder;
import md.fusionworks.lifehack.ui.LoadMoreAdapter;
import md.fusionworks.lifehack.ui.sales.model.ProductModel;
import md.fusionworks.lifehack.util.Constant;
import md.fusionworks.lifehack.util.rx.RxBus;

/**
 * Created by ungvas on 12/30/15.
 */
public class SaleProductAdapter extends LoadMoreAdapter<ProductModel> {

  private final int VIEW_TYPE_ITEM = 0;
  private final int VIEW_TYPE_LOADING = 1;

  private RxBus rxBus;

  public SaleProductAdapter(RecyclerView recyclerView) {
    super(recyclerView);
    rxBus = RxBus.getInstance();
  }

  @Override public int getItemViewType(int position) {
    return getItemList().get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == VIEW_TYPE_ITEM) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_sale_product, parent, false);
      return new SaleProductItemViewHolder(view);
    } else if (viewType == VIEW_TYPE_LOADING) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_sale_product_load_more, parent, false);
      return new LoadingMoreItemsViewHolder(view);
    }
    return null;
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
      SaleProductItemViewHolder saleProductItemViewHolder = ((SaleProductItemViewHolder) holder);
      ProductModel productModel = getItemList().get(position);

      initializeThumbnailImage(saleProductItemViewHolder.thumbnail, productModel.productImage);
      if (productModel.categoryId == 0) {
        saleProductItemViewHolder.categoryField.setVisibility(View.VISIBLE);
        saleProductItemViewHolder.categoryField.setText(productModel.categoryNameRu);
      } else {
        saleProductItemViewHolder.categoryField.setVisibility(View.GONE);
      }
      saleProductItemViewHolder.nameField.setText(productModel.productName);
      initializePrevPriceField(saleProductItemViewHolder.prevPriceField,
          productModel.productPrevPriceForGraph);
      saleProductItemViewHolder.minPriceField.setText(
          String.format("%d MDL", Math.round(productModel.productMinPriceForGraph)));
      saleProductItemViewHolder.percentSaleField.setText(
          "-" + Math.round(productModel.percent) + "%");
    }
  }

  private void initializeThumbnailImage(ImageView thumbnailImage, String url) {
    Drawable noPhotoDrawable =
        ContextCompat.getDrawable(thumbnailImage.getContext(), R.drawable.ic_no);
    DrawableCompat.setTint(noPhotoDrawable,
        ContextCompat.getColor(thumbnailImage.getContext(), R.color.text_disabled));

    Glide.with(thumbnailImage.getContext())
        .load(String.format("%s_%d.jpg", url, Constant.SALE_PRODUCT_THUMBNAIL_IMAGE_SIZE))
        .crossFade()
        .error(noPhotoDrawable)
        .into(thumbnailImage);
  }

  private void initializePrevPriceField(TextView prevPriceField, double value) {
    prevPriceField.setText(String.format("%d MDL", Math.round(value)));
    prevPriceField.setPaintFlags(prevPriceField.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
  }

  public class SaleProductItemViewHolder extends BaseViewHolder {

    @Bind(R.id.thumbnailImage) ImageView thumbnail;
    @Bind(R.id.nameField) TextView nameField;
    @Bind(R.id.categoryField) TextView categoryField;
    @Bind(R.id.prevPriceField) TextView prevPriceField;
    @Bind(R.id.minPriceField) TextView minPriceField;
    @Bind(R.id.salePercentField) TextView percentSaleField;
    @Bind(R.id.buyButton) Button buyButton;
    @Bind(R.id.allPricesButton) Button allPricesButton;

    public SaleProductItemViewHolder(@NonNull View view) {
      super(view);
      buyButton.setOnClickListener(v -> rxBus.postIfHasObservers(
          new NavigateToUrlEvent(getItemList().get(getAdapterPosition()).storeProductUrl)));
      allPricesButton.setOnClickListener(v -> rxBus.postIfHasObservers(
          new NavigateToUrlEvent(getItemList().get(getAdapterPosition()).jbUrl)));
    }
  }

  public class LoadingMoreItemsViewHolder extends BaseViewHolder {

    public LoadingMoreItemsViewHolder(@NonNull View view) {
      super(view);
    }
  }
}

