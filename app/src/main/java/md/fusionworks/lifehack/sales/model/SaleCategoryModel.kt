package md.fusionworks.lifehack.sales.model

/**
 * Created by ungvas on 2/24/16.
 */
class SaleCategoryModel {

  var id: Int = 0
  var nameRu: String
  var nameRo: String
  var isHeader: Boolean = false
  var header: String = ""

  constructor(id: Int, nameRu: String, nameRo: String) {
    this.id = id
    this.nameRu = nameRu
    this.nameRo = nameRo
  }

  constructor(id: Int, nameRu: String, nameRo: String, isHeader: Boolean, header: String) {
    this.id = id
    this.nameRu = nameRu
    this.nameRo = nameRo
    this.isHeader = isHeader
    this.header = header
  }
}
