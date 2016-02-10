package md.fusionworks.lifehack.data.api.model.exchange_rates;

/**
 * Created by ungvas on 11/6/15.
 */
public class District {

    private String id;
    private String name;
    private Locality locality;

    public District(String id, String name, Locality locality) {
        this.id = id;
        this.name = name;
        this.locality = locality;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }
}
