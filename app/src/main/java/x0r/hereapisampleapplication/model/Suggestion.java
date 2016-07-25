package x0r.hereapisampleapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by kirillrozhenkov on 21.07.16.
 *
 */
public class Suggestion implements Parcelable {

    @SerializedName("position")
    private double[] position = new double[2];

    @SerializedName("title")
    private String title;

    @SerializedName("highlightedTitle")
    private String highlightedTitle;

    @SerializedName("vicinity")
    private String vicinity;

    @SerializedName("highlightedVicinity")
    private String highlightedVicinity;

    @SerializedName("category")
    private String category;

    @SerializedName("href")
    private String href;

    @SerializedName("type")
    private String type;

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHighlightedTitle() {
        return highlightedTitle;
    }

    public void setHighlightedTitle(String highlightedTitle) {
        this.highlightedTitle = highlightedTitle;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getHighlightedVicinity() {
        return highlightedVicinity;
    }

    public void setHighlightedVicinity(String highlightedVicinity) {
        this.highlightedVicinity = highlightedVicinity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public double getLatCoordinate() {
        return position[0];
    }

    public double getLngCoordinate() {
        return position[1];
    }

    public boolean hasValidPosition() {
        return position != null && position.length == 2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDoubleArray(this.position);
        dest.writeString(this.title);
        dest.writeString(this.highlightedTitle);
        dest.writeString(this.vicinity);
        dest.writeString(this.highlightedVicinity);
        dest.writeString(this.category);
        dest.writeString(this.href);
        dest.writeString(this.type);
    }

    public Suggestion() {
    }

    protected Suggestion(Parcel in) {
        this.position = in.createDoubleArray();
        this.title = in.readString();
        this.highlightedTitle = in.readString();
        this.vicinity = in.readString();
        this.highlightedVicinity = in.readString();
        this.category = in.readString();
        this.href = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<Suggestion> CREATOR = new Parcelable.Creator<Suggestion>() {
        @Override
        public Suggestion createFromParcel(Parcel source) {
            return new Suggestion(source);
        }

        @Override
        public Suggestion[] newArray(int size) {
            return new Suggestion[size];
        }
    };
}
