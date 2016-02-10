package md.fusionworks.lifehack.ui.model.exchange_rates;

/**
 * Created by ungvas on 11/6/15.
 */
public class LocalityModel {

    private int id;
    private String name;
    private String code;

    public LocalityModel(int id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
