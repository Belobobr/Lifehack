package md.fusionworks.lifehack.ui.sales

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.util.ArrayList
import md.fusionworks.lifehack.ui.sales.model.SaleCategoryModel
import md.fusionworks.lifehack.util.Constant

/**
 * Created by ungvas on 10/30/15.
 */

class SaleCategorySpinnerAdapter(val context: Context, val items: List<SaleCategoryModel>,
    private val language: String) : BaseAdapter() {

  override fun getCount(): Int {
    return items.size
  }

  override fun getItem(position: Int): Any {
    return items[position]
  }

  override fun getItemId(position: Int): Long {
    return items[position].id.toLong()
  }

  private fun getTitle(position: Int): String {
    return if (position >= 0 && position < items.size) if (language == Constant.LANG_RU) items[position].nameRu
    else items[position].nameRo
    else ""
  }

  override fun getDropDownView(position: Int, view: View, parent: ViewGroup): View {
    var view = view
    view = (context as Activity).layoutInflater.inflate(
        android.R.layout.simple_spinner_dropdown_item, parent, false)

    val normalTextView = view.findViewById(android.R.id.text1) as TextView
    normalTextView.text = getTitle(position)

    return view
  }

  override fun getView(position: Int, view: View, parent: ViewGroup): View {
    var view = view
    view = (context as Activity).layoutInflater.inflate(android.R.layout.simple_spinner_item,
        parent, false)

    val textView = view.findViewById(android.R.id.text1) as TextView
    textView.text = getTitle(position)
    return view
  }
}
