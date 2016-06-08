package mg.yvan.truth.event;

/**
 * Created by Yvan on 08/06/16.
 */
public class OnSearchQueryChange {

    private String mQuery;

    public OnSearchQueryChange(String query) {
        mQuery = query;
    }

    public String getQuery() {
        return mQuery;
    }

    public void setQuery(String query) {
        mQuery = query;
    }

}
