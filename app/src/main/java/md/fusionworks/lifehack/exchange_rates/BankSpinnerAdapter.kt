package md.fusionworks.lifehack.exchange_rates

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.exchange_rates.model.BankSpinnerItemModel
import md.fusionworks.lifehack.extension.toGone
import md.fusionworks.lifehack.extension.toVisible

/**
 * Created by ungvas on 10/30/15.
 */

class BankSpinnerAdapter(private val context: Context) : BaseAdapter() {
  private val items = arrayListOf<BankSpinnerItemModel>()

  fun clear() = items.clear()

  fun addItem(title: String, bankId: Int) {
    items.add(BankSpinnerItemModel(false, title, bankId))
  }

  fun addHeader(title: String) {
    items.add(BankSpinnerItemModel(true, title, -1))
  }

  val allItems: List<BankSpinnerItemModel>
    get() = items

  override fun getCount() = items.size

  override fun getItem(position: Int) = items[position]

  override fun getItemId(position: Int) = items[position].bankId.toLong()

  private fun isHeader(
      position: Int) = position >= 0 && position < items.size && items[position].isHeader

  private fun getTitle(
      position: Int) = if (position >= 0 && position < items.size) items[position].title ?: "" else ""

  override fun getDropDownView(position: Int, view: View?, parent: ViewGroup): View {
    var view = (context as Activity).layoutInflater.inflate(R.layout.bank_spinner_item_dropdown,
        parent,
        false)

    val headerTextView = view.findViewById(R.id.header_text) as TextView
    val dividerView = view.findViewById(R.id.divider_view)
    val normalTextView = view.findViewById(android.R.id.text1) as TextView

    if (isHeader(position)) {
      headerTextView.text = getTitle(position)
      headerTextView.toVisible()
      normalTextView.toGone()
      dividerView.toVisible()
    } else {
      normalTextView.text = getTitle(position)
      headerTextView.toGone()
      normalTextView.toVisible()
      dividerView.toGone()
    }

    return view
  }

  override fun getView(position: Int, view: View?, parent: ViewGroup): View {
    var view = (context as Activity).layoutInflater.inflate(R.layout.bank_spinner_item, parent,
        false)

    val textView = view.findViewById(android.R.id.text1) as TextView
    textView.text = getTitle(position)
    return view
  }

  override fun isEnabled(position: Int) = !isHeader(position)

  override fun getItemViewType(position: Int) = 0

  override fun getViewTypeCount() = 1

  override fun areAllItemsEnabled() = false
}
