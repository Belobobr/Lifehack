package md.fusionworks.lifehack.ui.sales;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import md.fusionworks.lifehack.ui.sales.model.SaleCategoryModel;
import md.fusionworks.lifehack.util.Constant;

/**
 * Created by ungvas on 10/30/15.
 */

public class SaleCategorySpinnerAdapter extends BaseAdapter {

  private Context context;
  private List<SaleCategoryModel> items = new ArrayList<>();
  private String language;

  public SaleCategorySpinnerAdapter(Context context, List<SaleCategoryModel> items,
      String language) {
    this.context = context;
    this.items = items;
    this.language = language;
  }

  @Override public int getCount() {
    return items.size();
  }

  @Override public Object getItem(int position) {
    return items.get(position);
  }

  @Override public long getItemId(int position) {
    return items.get(position).id;
  }

  private String getTitle(int position) {
    return position >= 0 && position < items.size() ? (language.equals(Constant.LANG_RU))
        ? items.get(position).nameRu : items.get(position).nameRo : "";
  }

  @Override public View getDropDownView(int position, View view, ViewGroup parent) {
    view = ((Activity) context).getLayoutInflater()
        .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);

    TextView normalTextView = (TextView) view.findViewById(android.R.id.text1);
    normalTextView.setText(getTitle(position));

    return view;
  }

  @Override public View getView(int position, View view, ViewGroup parent) {
    view = ((Activity) context).getLayoutInflater()
        .inflate(android.R.layout.simple_spinner_item, parent, false);

    TextView textView = (TextView) view.findViewById(android.R.id.text1);
    textView.setText(getTitle(position));
    return view;
  }
}
