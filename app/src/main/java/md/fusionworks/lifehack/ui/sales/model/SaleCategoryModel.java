package md.fusionworks.lifehack.ui.sales.model;

/**
 * Created by ungvas on 2/24/16.
 */
public class SaleCategoryModel {

  public int id;
  public String nameRu;
  public String nameRo;
  public boolean isHeader;
  public String header;

  public SaleCategoryModel(int id, String nameRu, String nameRo) {
    this.id = id;
    this.nameRu = nameRu;
    this.nameRo = nameRo;
  }

  public SaleCategoryModel(int id, String nameRu, String nameRo, boolean isHeader, String header) {
    this.id = id;
    this.nameRu = nameRu;
    this.nameRo = nameRo;
    this.isHeader = isHeader;
    this.header = header;
  }
}
