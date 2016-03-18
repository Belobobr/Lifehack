package md.fusionworks.lifehack.widget

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.util.DateUtil
import java.util.*

/**
 * Created by ungvas on 10/30/15.
 */
class DateView : AppCompatTextView, View.OnClickListener, DatePickerDialog.OnDateSetListener {

  private var onDateChangedListener: OnDateChangedListener? = null

  fun setOnDateChangedListener(onDateChangedListener: OnDateChangedListener) {
    this.onDateChangedListener = onDateChangedListener
  }

  constructor(context: Context) : super(context, null, R.attr.dateViewStyle) {
    initialize()
  }

  constructor(context: Context, attrs: AttributeSet) :
  super(context, attrs, R.attr.dateViewStyle) {
    initialize()
  }

  private fun initialize() {
    val typeface = Typeface.createFromAsset(resources.assets, "font/Roboto-Regular.ttf")
    setTypeface(typeface)
    setDateText(Date())
    setOnClickListener(this)
  }

  private fun setDateText(date: Date) {

    text = DateUtil.getDateViewFormat().format(date)
    tag = date
  }

  override fun onClick(v: View) {
    val currentSelectedDate = tag as Date
    val currentSelectedCalendar = Calendar.getInstance()
    currentSelectedCalendar.time = currentSelectedDate
    showDatePickerDialog(currentSelectedCalendar.get(Calendar.YEAR),
        currentSelectedCalendar.get(Calendar.MONTH),
        currentSelectedCalendar.get(Calendar.DAY_OF_MONTH))
  }

  override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {

    val selectedDate = DateUtil.createDate(year, monthOfYear, dayOfMonth)
    setDateText(selectedDate)

    if (onDateChangedListener != null) onDateChangedListener!!.onDateChanged(selectedDate)
  }

  private fun showDatePickerDialog(year: Int, monthOfYear: Int, dayOfMonth: Int) {

    val calendar = Calendar.getInstance()
    calendar.time = Date()

    val dpd = DatePickerDialog.newInstance(this, year, monthOfYear, dayOfMonth)
    dpd.maxDate = calendar
    dpd.show((context as Activity).fragmentManager, "DatePickerDialog")
  }

  interface OnDateChangedListener {

    fun onDateChanged(date: Date)
  }
}
