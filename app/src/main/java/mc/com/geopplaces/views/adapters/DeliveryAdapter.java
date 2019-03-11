package mc.com.geopplaces.views.adapters;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import mc.com.geopplaces.R;
import mc.com.geopplaces.models.entities.DeliveryEntity;
import mc.com.geopplaces.views.components.CardOnClickListener;

public class DeliveryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_ITEM = 0;
    private static final int VIEW_LOADING = 1;

    private List<DeliveryEntity> deliveryEntities;
    private CardOnClickListener cardOnClickListener;

    public DeliveryAdapter(List<DeliveryEntity> deliveryEntities, CardOnClickListener cardOnClickListener) {
        this.deliveryEntities = deliveryEntities;
        this.cardOnClickListener = cardOnClickListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM)
            return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_delivery, parent, false));
        else if (viewType == VIEW_LOADING)
            return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false));
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        DeliveryEntity deliveryEntity = deliveryEntities.get(position);

        if (deliveryEntity != null)
            ((ItemViewHolder) holder).bind(deliveryEntity,cardOnClickListener);
    }

    public void addLoadingView() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                deliveryEntities.add(null);
                notifyItemInserted(deliveryEntities.size() - 1);
            }
        });
    }

    public void removeLoadingView() {
        deliveryEntities.removeAll(Collections.singleton(null));
        notifyItemRemoved(deliveryEntities.size());
    }


    @Override
    public int getItemCount() {
        return deliveryEntities == null ? 0 : deliveryEntities.size();
    }

    @Override
    public int getItemViewType(int position) {
        return deliveryEntities.get(position) == null ? VIEW_LOADING : VIEW_ITEM;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView descriptionTextView, addressTextView;
        private ImageView deliveryItemImageView;

        private ItemViewHolder(View view) {
            super(view);
            descriptionTextView = view.findViewById(R.id.description_tv);
            addressTextView = view.findViewById(R.id.address_tv);
            deliveryItemImageView = view.findViewById(R.id.delivery_item_iv);
        }

        private void bind(final DeliveryEntity deliveryEntity, final CardOnClickListener listener){
            descriptionTextView.setText(deliveryEntity.getId()+"xx");
            addressTextView.setText(deliveryEntity.getAddress());
            Picasso.get()
                    .load(deliveryEntity.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(deliveryItemImageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(deliveryEntity);
                }
            });
        }
    }
}