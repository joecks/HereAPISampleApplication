
package x0r.hereapisampleapplication.model;

import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("position")
    private double[] position = new double[2];

    @SerializedName("address")
    private Address address;

    @SerializedName("type")
    private String type;

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
