package x0r.hereapisampleapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * Created by kirillrozhenkov on 21.07.16.
 *
 */
public class SuggestionsResult {

    @SerializedName("results")
    private List<Suggestion> results;

    public List<Suggestion> getResults() {
        return results;
    }

    public void setResults(List<Suggestion> results) {
        this.results = results;
    }
}
