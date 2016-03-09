package md.fusionworks.lifehack.ui.exchange_rates

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.ui.exchange_rates.model.BankSpinnerItemModel

/**
 * Created by ungvas on 10/30/15.
 */

class BankSpinnerAdapter(private val context: Context) : BaseAdapter() {
  private val items = arrayListOf<BankSpinnerItemModel>()

  fun clear() {
    items.clear()
  }

  fun addItem(title: String, bankId: Int) {

    items.add(BankSpinnerItemModel(false, title, bankId))
  }

  fun addHeader(title: String) {

    items.add(BankSpinnerItemModel(true, title, -1))
  }

  val allItems: List<BankSpinnerItemModel>
    get() = items

  override fun getCount(): Int {
    return items.size
  }

  override fun getItem(position: Int): Any {
    return items[position]
  }

  override fun getItemId(position: Int): Long {
    return items[position].bankId.toLong()
  }

  private fun isHeader(position: Int): Boolean {
    return position >= 0 && position < items.size && items[position].isHeader
  }

  private fun getTitle(position: Int): String? {
    return if (position >= 0 && position < items.size) items[position].title else ""
  }

  override fun getDropDownView(position: Int, view: View?, parent: ViewGroup): View {
    var view = view

    view = (context as Activity).layoutInflater.inflate(R.layout.bank_spinner_item_dropdown, parent,
        false)

    val headerTextView = view.findViewById(R.id.header_text) as TextView
    val dividerView = view.findViewById(R.id.divider_view)
    val normalTextView = view.findViewById(android.R.id.text1) as TextView

    if (isHeader(position)) {
      headerTextView.text = getTitle(position)
      headerTextView.visibility = View.VISIBLE
      normalTextView.visibility = View.GONE
      dividerView.visibility = View.VISIBLE
    } else {
      normalTextView.text = getTitle(position)
      headerTextView.visibility = View.GONE
      normalTextView.visibility = View.VISIBLE
      dividerView.visibility = View.GONE
    }

    return view
  }

  override fun getView(position: Int, view: View?, parent: ViewGroup): View {
    var view = view

    view = (context as Activity).layoutInflater.inflate(R.layout.bank_spinner_item, parent, false)

    val textView = view.findViewById(android.R.id.text1) as TextView
    textView.text = getTitle(position)
    return view
  }

  override fun isEnabled(position: Int): Boolean {
    return !isHeader(position)
  }

  override fun getItemViewType(position: Int): Int {
    return 0
  }

  override fun getViewTypeCount(): Int {
    return 1
  }

  override fun areAllItemsEnabled(): Boolean {
    return false
  }
}
