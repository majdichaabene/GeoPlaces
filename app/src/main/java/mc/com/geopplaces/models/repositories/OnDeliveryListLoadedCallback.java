package mc.com.geopplaces.models.repositories;

import java.util.ArrayList;

import mc.com.geopplaces.models.entities.DeliveryEntity;

public interface OnDeliveryListLoadedCallback {

    void onSuccess(ArrayList<DeliveryEntity> deliveryEntities);
    void onError(String errorState);

}
