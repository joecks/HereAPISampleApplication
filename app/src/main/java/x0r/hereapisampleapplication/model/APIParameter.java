package x0r.hereapisampleapplication.model;

/**
 *
 * Holds basic API parameters and values
 *
 */
public final class APIParameter {
    private APIParameter() {}

    public static final String BASE_URL = "https://places.cit.api.here.com";
    public static final String HARDCODED_GEOLOCATION = "geo:52.531,13.3843";

    public static final class Header {
        public static final String GEOLOCATION = "Geolocation";
    }

    public static final class Request {
        public static final String APP_ID = "app_id";
        public static final String APP_CODE = "app_code";
        public static final String QUERY = "q";
    }
}
