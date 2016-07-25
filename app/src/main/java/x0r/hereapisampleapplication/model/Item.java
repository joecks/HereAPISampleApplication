
package x0r.hereapisampleapplication.model;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("position")
    private double[] position = new double[2];

    @SerializedName("distance")
    private int distance;

    @SerializedName("title")
    private String title;

    @SerializedName("averageRating")
    private double averageRating;

    @SerializedName("category")
    private Category category;

    @SerializedName("icon")
    private String icon;

    @SerializedName("vicinity")
    private String vicinity;

    @SerializedName("href")
    private String href;

    @SerializedName("type")
    private String type;

    @SerializedName("sponsored")
    private boolean sponsored;

    @SerializedName("id")
    private String id;

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSponsored() {
        return sponsored;
    }

    public void setSponsored(boolean sponsored) {
        this.sponsored = sponsored;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
