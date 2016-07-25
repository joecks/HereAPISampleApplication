
package x0r.hereapisampleapplication.model;

import com.google.gson.annotations.SerializedName;

public class Context {

    @SerializedName("title")
    private String title;

    @SerializedName("location")
    private Location location;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
