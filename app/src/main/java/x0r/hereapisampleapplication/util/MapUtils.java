package x0r.hereapisampleapplication.util;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.mapping.MapMarker;

import java.io.IOException;

public final class MapUtils {

    private MapUtils() {}

    @Nullable
    public static MapMarker getMapMarketFrom(double latitude, double longitude, @DrawableRes int imageRes) {

        Image mapMarketImage = new Image();

        try {
            mapMarketImage.setImageResource(imageRes);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return new MapMarker(new GeoCoordinate(latitude, longitude), mapMarketImage);
    }
}
