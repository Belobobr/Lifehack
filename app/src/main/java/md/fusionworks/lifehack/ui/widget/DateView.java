package md.fusionworks.lifehack.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;

import md.fusionworks.lifehack.R;
import md.fusionworks.lifehack.util.DateUtil;

/**
 * Created by ungvas on 10/30/15.
 */
public class DateView extends TextView
    implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

  private OnDateChangedListener onDateChangedListener;

  public void setOnDateChangedListener(OnDateChangedListener onDateChangedListener) {
    this.onDateChangedListener = onDateChangedListener;
  }

  public DateView(Context context) {
    super(context, null, R.attr.dateViewStyle);

    initialize();
  }

  public DateView(Context context, AttributeSet attrs) {

    super(context, attrs, R.attr.dateViewStyle);

    initialize();
  }

  private void initialize() {

    Typeface typeface =
        Typeface.createFromAsset(getResources().getAssets(), "font/Roboto-Regular.ttf");
    setTypeface(typeface);
    setDateText(new Date());
    setOnClickListener(this);
  }

  private void setDateText(Date date) {

    setText(DateUtil.getDateViewFormat().format(date));
    setTag(date);
  }

  @Override public void onClick(View v) {

    Date currentSelectedDate = (Date) getTag();
    Calendar currentSelectedCalendar = Calendar.getInstance();
    currentSelectedCalendar.setTime(currentSelectedDate);
    showDatePickerDialog(currentSelectedCalendar.get(Calendar.YEAR),
        currentSelectedCalendar.get(Calendar.MONTH),
        currentSelectedCalendar.get(Calendar.DAY_OF_MONTH));
  }

  @Override
  public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    Date selectedDate = DateUtil.createDate(year, monthOfYear, dayOfMonth);
    setDateText(selectedDate);

    if (onDateChangedListener != null) onDateChangedListener.onDateChanged(selectedDate);
  }

  private void showDatePickerDialog(int year, int monthOfYear, int dayOfMonth) {

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());

    DatePickerDialog dpd = DatePickerDialog.newInstance(this, year, monthOfYear, dayOfMonth);
    dpd.setMaxDate(calendar);
    dpd.show(((Activity) getContext()).getFragmentManager(), "DatePickerDialog");
  }

  public interface OnDateChangedListener {

    void onDateChanged(Date date);
  }
}
