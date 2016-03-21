package md.fusionworks.lifehack.sales.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import md.fusionworks.lifehack.sales.model.SaleCategoryModel
import md.fusionworks.lifehack.util.Constant

/**
 * Created by ungvas on 10/30/15.
 */

class SaleCategorySpinnerAdapter(private val context: Context, private val itemList: List<SaleCategoryModel>,
    private val language: String) : BaseAdapter() {

  override fun getCount() = itemList.size

  override fun getItem(position: Int) = itemList[position]

  override fun getItemId(position: Int) = itemList[position].id.toLong()

  private fun getTitle(
      position: Int) = if (position >= 0 && position < itemList.size) if (language == Constant.LANG_RU) itemList[position].nameRu
  else itemList[position].nameRo
  else ""

  override fun getDropDownView(position: Int, view: View?, parent: ViewGroup): View {
    val view = (context as Activity).layoutInflater.inflate(
        android.R.layout.simple_spinner_dropdown_item, parent, false)

    val normalTextView = view.findViewById(android.R.id.text1) as TextView
    normalTextView.text = getTitle(position)

    return view
  }

  override fun getView(position: Int, view: View?, parent: ViewGroup): View {
    val view = (context as Activity).layoutInflater.inflate(
        android.R.layout.simple_spinner_item,
        parent, false)

    val textView = view.findViewById(android.R.id.text1) as TextView
    textView.text = getTitle(position)
    return view
  }
}
