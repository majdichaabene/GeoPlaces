package mc.com.geopplaces.models.repositories;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import mc.com.geopplaces.managers.ApiManager;
import mc.com.geopplaces.managers.ApiServerCallback;
import mc.com.geopplaces.managers.ConfigManager;
import mc.com.geopplaces.models.entities.DeliveryEntity;
import mc.com.geopplaces.models.entities.LocationEntity;

public class DeliveryRepository {
    private String endPoint = "/deliveries";
    public DeliveryRepository() {

    }

    private String getDeliveriesUrl(int offset ,int limit){
        return ConfigManager.getInstance().getWebApiRoot() + endPoint + "?offset=" + offset + "&limit=" +limit;
    }

    public void getDeliveryList(int offset ,int limit,final OnDeliveryListLoadedCallback onDeliveryListLoadedCallback){
        ApiManager.getsInstance().GET(getDeliveriesUrl(offset,limit), new ApiServerCallback() {
            @Override
            public boolean onSuccess(JSONArray result) {
                onDeliveryListLoadedCallback.onSuccess(parseJsonToDeliveryList(result));
                return false;
            }

            @Override
            public boolean onFailure(String errorState) {
                onDeliveryListLoadedCallback.onError(errorState);
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
                LocationEntity locationEntity = new LocationEntity();
                locationEntity.setLat(result.getJSONObject(i).getJSONObject("location").getDouble("lat"));
                locationEntity.setLng(result.getJSONObject(i).getJSONObject("location").getDouble("lng"));
                locationEntity.setAddress(result.getJSONObject(i).getJSONObject("location").getString("address"));
                deliveryEntity.setLocation(locationEntity);
                deliveryEntities.add(deliveryEntity);
            }
            return deliveryEntities;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }
}
