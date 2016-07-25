package x0r.hereapisampleapplication.domain;


import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;
import x0r.hereapisampleapplication.model.Search;
import x0r.hereapisampleapplication.model.SuggestionsResult;

import static x0r.hereapisampleapplication.model.APIParameter.Header.GEOLOCATION;
import static x0r.hereapisampleapplication.model.APIParameter.Request.QUERY;

/**
 *
 * Describes actions to work with Here API
 *
 */
public interface HereAPI {

    /**
     * Get autocomplete suggestions for partial search queries
     * @param location User's location
     * @param str String to search
     * @return
     *
     * @see <a href="https://developer.here.com/rest-apis/documentation/places/topics_api/resource-autosuggest.html">Autosuggest Entrypoint</a> for more information
     *
     */
    @GET("/places/v1/autosuggest")
    Observable<SuggestionsResult> suggest(@Header(GEOLOCATION) String location, @Query(QUERY) String str);

    /**
     * Find places using a completed text query
     * @param location User's location
     * @param str String to search
     * @return
     *
     * @see <a href="https://developer.here.com/rest-apis/documentation/places/topics_api/resource-search.html">Autosuggest Entrypoint</a> for more information
     *
     */
    @GET("/places/v1/discover/search")
    Observable<Search> search(@Header(GEOLOCATION) String location, @Query(QUERY) String str);
}
