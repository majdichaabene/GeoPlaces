package mc.com.geopplaces.models.entities;

public class DeliveryEntity {
    private int id;
    private String description;
    private String imageUrl;
    private LocationEntity location;

    public DeliveryEntity(int id, String description, String imageUrl, LocationEntity location) {
        this.id = id;
        this.description = description;
        this.imageUrl = imageUrl;
        this.location = location;
    }

    public DeliveryEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocationEntity getLocation() {
        return location;
    }

    public void setLocation(LocationEntity location) {
        this.location = location;
    }
}
