package mc.com.geopplaces.models.repositories;


import android.content.Context;
import android.os.Handler;

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

    public void getDeliveryList(Context context,boolean hasNext, final OnDeliveryListLoadedCallback onDeliveryListLoadedCallback){
        try {
            RealmManager.open();
            if (Utils.isNetworkAvailable(context)){
                deliveryRepository.getDeliveryList(index, new OnDeliveryResponseCallBack() {
                    @Override
                    public void onSuccess(ArrayList<DeliveryEntity> deliveryEntitiesResult) {
                        deliveryDao.save(deliveryEntitiesResult);
                        index = deliveryEntitiesResult.get(deliveryEntitiesResult.size()-1).getId()+1;
                        onDeliveryListLoadedCallback.onSuccess(deliveryEntitiesResult);
                    }

                    @Override
                    public void onError(String errorState) {
                        onDeliveryListLoadedCallback.onError(errorState);
                    }
                });
            } else {
                if(hasNext){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onDeliveryListLoadedCallback.onError("error");
                        }
                    },1000);
                } else {
                    if (deliveryDao.loadAll().size() != 0){
                        ArrayList<DeliveryEntity> deliveryEntities = new ArrayList<>();
                        deliveryEntities.addAll(deliveryDao.loadAll());
                        index = deliveryEntities.get(deliveryEntities.size()-1).getId()+1;
                        onDeliveryListLoadedCallback.onSuccess(deliveryEntities);
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onDeliveryListLoadedCallback.onError("error");
                            }
                        },1000);
                    }
                }
            }
        } finally {
            RealmManager.close();
        }

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
