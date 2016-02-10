package md.fusionworks.lifehack.ui.model;

/**
 * Created by ungvas on 11/6/15.
 */
public class DistrictModel {

    private String id;
    private String name;
    private LocalityModel locality;

    public DistrictModel(String id, String name, LocalityModel locality) {
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

    public LocalityModel getLocality() {
        return locality;
    }

    public void setLocality(LocalityModel locality) {
        this.locality = locality;
    }
}
