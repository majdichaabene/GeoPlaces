package mc.com.geopplaces.models.repositories;


import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import io.realm.Realm;
import mc.com.geopplaces.managers.RealmManager;
import mc.com.geopplaces.models.dao.DeliveryDao;
import mc.com.geopplaces.models.entities.DeliveryEntity;
import mc.com.geopplaces.models.responses.DeliveryResponse;
import mc.com.geopplaces.models.responses.OnDeliveryResponseCallBack;
import mc.com.geopplaces.utils.Utils;

public class DeliveryRepository {
    private DeliveryDao deliveryDao;
    private DeliveryResponse deliveryRepository;
    private int index = 0;

    public DeliveryRepository() {
        deliveryDao = new DeliveryDao(Realm.getDefaultInstance());
        deliveryRepository = new DeliveryResponse();
    }

    public void getDeliveryList(Context context, final OnDeliveryListLoadedCallback onDeliveryListLoadedCallback){
        Log.e("getDeliveryList","index = "+index);
        RealmManager.open();
        if (Utils.isNetworkAvailable(context)){
            deliveryRepository.getDeliveryList(0, new OnDeliveryResponseCallBack() {
                @Override
                public void onSuccess(ArrayList<DeliveryEntity> deliveryEntitiesResult) {
                    deliveryDao.save(deliveryEntitiesResult);
                    index = deliveryEntitiesResult.get(deliveryEntitiesResult.size()-1).getId()+1;
                    RealmManager.close();
                    onDeliveryListLoadedCallback.onSuccess(deliveryEntitiesResult);
                }

                @Override
                public void onError(String errorState) {
                    onDeliveryListLoadedCallback.onError(errorState);
                }
            });
        } else {
            ArrayList<DeliveryEntity> deliveryEntities = new ArrayList<>();
            deliveryEntities.addAll(deliveryDao.loadAll());
            RealmManager.close();
            index = deliveryEntities.get(deliveryEntities.size()-1).getId()+1;
            onDeliveryListLoadedCallback.onSuccess(deliveryEntities);
        }
    }
    public void getNextDeliveryList(final OnDeliveryListLoadedCallback onDeliveryListLoadedCallback){
        Log.e("getNextDeliveryList","index = "+index);
        RealmManager.open();
        deliveryRepository.getDeliveryList(index, new OnDeliveryResponseCallBack() {
            @Override
            public void onSuccess(ArrayList<DeliveryEntity> deliveryEntitiesResult) {
                deliveryDao.save(deliveryEntitiesResult);
                index = deliveryEntitiesResult.get(deliveryEntitiesResult.size()-1).getId()+1;
                RealmManager.close();
                Log.e("vm","getNextDeliveryList : "+deliveryEntitiesResult.size()+"xxx");
                onDeliveryListLoadedCallback.onSuccess(deliveryEntitiesResult);
            }

            @Override
            public void onError(String errorState) {
                onDeliveryListLoadedCallback.onError(errorState);
            }
        });
    }

    public DeliveryEntity getDeliveryById(int id){
        try {
            RealmManager.open();
            return deliveryDao.loadById(id);

        } finally {
            RealmManager.close();
        }
    }
}
