
package x0r.hereapisampleapplication.model;

import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("city")
    private String city;

    @SerializedName("country")

    private String country;

    @SerializedName("text")
    private String text;


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
