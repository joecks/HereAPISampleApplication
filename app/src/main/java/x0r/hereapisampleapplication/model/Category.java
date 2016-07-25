
package x0r.hereapisampleapplication.model;

import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("title")
    private String title;

    @SerializedName("type")
    private String type;

    @SerializedName("href")
    private String href;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
