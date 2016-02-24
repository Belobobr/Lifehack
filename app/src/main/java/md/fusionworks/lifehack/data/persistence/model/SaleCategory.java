package md.fusionworks.lifehack.data.persistence.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ungvas on 2/24/16.
 */
public class SaleCategory extends RealmObject {

  @PrimaryKey private int id;
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
