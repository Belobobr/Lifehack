package md.fusionworks.lifehack.taxi.persistence

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Created by ungvas on 2/18/16.
 */
open class TaxiPhoneNumber : RealmObject() {

  @PrimaryKey open var phoneNumber: Int = 0
  open var lastCallDate: Date? = null
}
