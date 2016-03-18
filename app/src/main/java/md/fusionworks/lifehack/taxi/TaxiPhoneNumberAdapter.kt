package md.fusionworks.lifehack.taxi

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.rx.RxBus
import org.jetbrains.anko.find

/**
 * Created by ungvas on 2/17/16.
 */
class TaxiPhoneNumberAdapter(private val taxiPhoneNumberList: List<TaxiPhoneNumberModel>) : RecyclerView.Adapter<TaxiPhoneNumberAdapter.TaxiPhoneNumberViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaxiPhoneNumberViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_taxi_phone_number, parent,
        false)
    return TaxiPhoneNumberViewHolder(view)
  }

  override fun onBindViewHolder(holder: TaxiPhoneNumberViewHolder, position: Int) {
    holder.phoneNumberField.text = taxiPhoneNumberList[position].phoneNumber.toString()
  }

  override fun getItemCount() = taxiPhoneNumberList.size

  inner class TaxiPhoneNumberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val phoneNumberField: TextView

    init {
      phoneNumberField = itemView.find(R.id.phoneNumberField)
      itemView.setOnClickListener {
        RxBus.postIfHasObservers(
            TaxiPhoneNumberClickEvent(taxiPhoneNumberList[adapterPosition]))
      }
    }
  }
}
