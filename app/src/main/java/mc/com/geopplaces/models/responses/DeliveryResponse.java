package mc.com.geopplaces.models.responses;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import mc.com.geopplaces.managers.ApiManager;
import mc.com.geopplaces.managers.ApiServerCallback;
import mc.com.geopplaces.managers.ConfigManager;
import mc.com.geopplaces.models.entities.DeliveryEntity;

public class DeliveryResponse {
    private static final String END_POINT = "/deliveries";
    private static final int LIMIT_LOAD_DELIVERY = 20;

    public DeliveryResponse() {

    }

    private String getDeliveriesUrl(int offset){
        return ConfigManager.getInstance().getWebApiRoot() + END_POINT + "?offset=" + offset + "&limit=" + LIMIT_LOAD_DELIVERY;
    }

    public void getDeliveryList(int offset ,final OnDeliveryResponseCallBack onDeliveryResponseCallBack){
        Log.e("vm","url = "+getDeliveriesUrl(offset));
        ApiManager.getsInstance().GET(getDeliveriesUrl(offset), new ApiServerCallback() {
            @Override
            public boolean onSuccess(JSONArray result) {
                onDeliveryResponseCallBack.onSuccess(parseJsonToDeliveryList(result));
                return false;
            }

            @Override
            public boolean onFailure(String errorState) {
                onDeliveryResponseCallBack.onError(errorState);
                return false;
            }

        });
    }

    private ArrayList<DeliveryEntity> parseJsonToDeliveryList(JSONArray result) {
        ArrayList<DeliveryEntity> deliveryEntities = new ArrayList<>();
        try {
            for (int i = 0; i < result.length(); i++) {
                DeliveryEntity deliveryEntity = new DeliveryEntity();
                deliveryEntity.setId(result.getJSONObject(i).getInt("id"));
                deliveryEntity.setDescription(result.getJSONObject(i).getString("description"));
                deliveryEntity.setImageUrl(result.getJSONObject(i).getString("imageUrl"));
                deliveryEntity.setLat(result.getJSONObject(i).getJSONObject("location").getDouble("lat"));
                deliveryEntity.setLng(result.getJSONObject(i).getJSONObject("location").getDouble("lng"));
                deliveryEntity.setAddress(result.getJSONObject(i).getJSONObject("location").getString("address"));
                deliveryEntities.add(deliveryEntity);
            }
            return deliveryEntities;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }
}
