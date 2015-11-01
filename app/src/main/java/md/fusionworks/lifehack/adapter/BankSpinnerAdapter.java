package md.fusionworks.lifehack.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.model.Bank;
import md.fusionworks.lifehack.model.BankSpinnerItem;

/**
 * Created by ungvas on 10/30/15.
 */
public class BankSpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<BankSpinnerItem> items = new ArrayList<>();

    public BankSpinnerAdapter(Context context) {
        this.context = context;
    }

    public void clear() {
        items.clear();
    }

    public void addItem(String title, int bankId) {

        items.add(new BankSpinnerItem(false, title, bankId));
    }

    public void addHeader(String title) {

        items.add(new BankSpinnerItem(true, title, -1));
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private boolean isHeader(int position) {
        return position >= 0 && position < items.size()
                && items.get(position).isHeader();
    }

    private String getTitle(int position) {
        return position >= 0 && position < items.size() ? items.get(position).getTitle() : "";
    }


    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {

        view = ((Activity) context).getLayoutInflater().inflate(R.layout.bank_spinner_item_dropdown,
                parent, false);

        TextView headerTextView = (TextView) view.findViewById(R.id.header_text);
        View dividerView = view.findViewById(R.id.divider_view);
        TextView normalTextView = (TextView) view.findViewById(android.R.id.text1);

        if (isHeader(position)) {
            headerTextView.setText(getTitle(position));
            headerTextView.setVisibility(View.VISIBLE);
            normalTextView.setVisibility(View.GONE);
            dividerView.setVisibility(View.VISIBLE);
        } else {
            normalTextView.setText(getTitle(position));
            headerTextView.setVisibility(View.GONE);
            normalTextView.setVisibility(View.VISIBLE);
            dividerView.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        view = ((Activity) context).getLayoutInflater().inflate(R.layout.bank_spinner_item,
                parent, false);

        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));
        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        return !isHeader(position);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
}
