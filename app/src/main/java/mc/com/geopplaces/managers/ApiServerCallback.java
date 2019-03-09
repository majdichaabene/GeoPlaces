package mc.com.geopplaces.managers;

import org.json.JSONArray;

public interface ApiServerCallback {

    boolean onSuccess(JSONArray result);
    boolean onFailure(String errorState);

}
