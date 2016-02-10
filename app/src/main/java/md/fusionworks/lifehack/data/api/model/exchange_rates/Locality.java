package md.fusionworks.lifehack.data.api.model.exchange_rates;

/**
 * Created by ungvas on 11/6/15.
 */
public class Locality {

    private int id;
    private String name;
    private String code;

    public Locality(int id, String name, String code) {
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
