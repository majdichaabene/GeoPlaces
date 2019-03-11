package mc.com.geopplaces.models.responses;

import java.util.ArrayList;

import mc.com.geopplaces.models.entities.DeliveryEntity;

public interface OnDeliveryResponseCallBack {
    void onSuccess(ArrayList<DeliveryEntity> deliveryEntities);
    void onError(String errorState);
}
