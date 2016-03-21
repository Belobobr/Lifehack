package md.fusionworks.lifehack.taxi

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import md.fusionworks.lifehack.R
import md.fusionworks.lifehack.di.PerActivity
import md.fusionworks.lifehack.rx.RxBusDagger
import javax.inject.Inject

/**
 * Created by ungvas on 2/17/16.
 */
@PerActivity
class LastUsedTaxiPhoneNumberAdapter
@Inject
constructor(private val rxBus: RxBusDagger) : RecyclerView.Adapter<LastUsedTaxiPhoneNumberAdapter.TaxiPhoneNumberViewHolder>() {

  private lateinit var taxiPhoneNumberList: List<TaxiPhoneNumberModel>

  fun addItems(taxiPhoneNumberList: List<TaxiPhoneNumberModel>) {
    this.taxiPhoneNumberList = taxiPhoneNumberList
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaxiPhoneNumberViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_taxi_phone_number, parent,
        false)
    return TaxiPhoneNumberViewHolder(view)
  }

  override fun onBindViewHolder(holder: TaxiPhoneNumberViewHolder, position: Int) {
    holder.bind(taxiPhoneNumberList[position], rxBus)
  }

  override fun getItemCount() = taxiPhoneNumberList.size

  class TaxiPhoneNumberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var phoneNumberField: TextView

    init {
      phoneNumberField = itemView.findViewById(R.id.phoneNumberField) as TextView
    }

    fun bind(taxiPhoneNumberModel: TaxiPhoneNumberModel, rxBus: RxBusDagger) {
      phoneNumberField.text = taxiPhoneNumberModel.phoneNumber.toString()
      itemView.setOnClickListener {
        rxBus.postIfHasObservers(
            TaxiPhoneNumberClickEvent(taxiPhoneNumberModel))
      }
    }
  }
}
