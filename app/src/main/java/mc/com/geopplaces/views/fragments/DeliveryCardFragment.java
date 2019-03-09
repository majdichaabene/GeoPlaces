package mc.com.geopplaces.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import mc.com.geopplaces.R;
import mc.com.geopplaces.models.entities.DeliveryEntity;
import mc.com.geopplaces.models.repositories.DeliveryRepository;
import mc.com.geopplaces.models.repositories.OnDeliveryListLoadedCallback;
import mc.com.geopplaces.views.adapters.DeliveryAdapter;
import mc.com.geopplaces.views.components.CardOnClickListener;
import mc.com.geopplaces.views.components.CardOnLoadMoreListener;
import mc.com.geopplaces.views.components.CardOnLoadMoreScroll;


public class DeliveryCardFragment extends Fragment {

    private RecyclerView deliveryRecyclerView;
    private RelativeLayout loadingContainer,reloadContainer;
    private Button reloadButton;
    private DeliveryAdapter deliveryAdapter;
    private List<DeliveryEntity> deliveryEntities;
    private DeliveryRepository deliveryRepository;
    private CardOnLoadMoreScroll scrollListener;
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoadMore = false;
    private boolean isNetworkError = false;

    public DeliveryCardFragment() {
        deliveryEntities = new ArrayList<>();

    }

    public static DeliveryCardFragment newInstance() {
        DeliveryCardFragment fragment = new DeliveryCardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        deliveryRepository = new DeliveryRepository();
        loadFirstData();
    }

    private void initView(View view){
        deliveryRecyclerView =  view.findViewById(R.id.delivery_rv);
        loadingContainer = view.findViewById(R.id.loading_container);
        reloadContainer = view.findViewById(R.id.reload_container);
        reloadButton = view.findViewById(R.id.reload_btn);
        deliveryRecyclerView.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerView.setLayoutManager(linearLayoutManager);
    }
    private void setEvent(){
        deliveryAdapter = new DeliveryAdapter(deliveryEntities, new CardOnClickListener() {
            @Override
            public void onClick(DeliveryEntity deliveryEntity) {

            }
        });
        deliveryRecyclerView.setAdapter(deliveryAdapter);
        scrollListener=new CardOnLoadMoreScroll(linearLayoutManager);
        scrollListener.setOnLoadMoreListener(new CardOnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                isLoadMore = true;
                loadNextDelivery();
            }
        });
        deliveryRecyclerView.addOnScrollListener(scrollListener);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadContainer.setVisibility(View.GONE);
                loadFirstData();
            }
        });
    }

    private void loadFirstData(){
        loadingContainer.setVisibility(View.VISIBLE);
        deliveryRepository.getDeliveryList(0,  new OnDeliveryListLoadedCallback() {
            @Override
            public void onSuccess(ArrayList<DeliveryEntity> deliveryEntitiesResult) {
                deliveryEntities.addAll(deliveryEntitiesResult);
                loadingContainer.setVisibility(View.GONE);
                deliveryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorState) {
                loadingContainer.setVisibility(View.GONE);
                reloadContainer.setVisibility(View.VISIBLE);
                isNetworkError = true;
            }
        });
    }

    private void loadNextDelivery(){
        deliveryAdapter.addLoadingView();
        deliveryRepository.getDeliveryList(deliveryEntities.get(deliveryEntities.size()-1).getId(), new OnDeliveryListLoadedCallback() {
            @Override
            public void onSuccess(ArrayList<DeliveryEntity> deliveryEntitiesResult) {
                isLoadMore = false;
                deliveryAdapter.removeLoadingView();
                scrollListener.setLoaded();
                deliveryEntities.addAll(deliveryEntitiesResult);
                deliveryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorState) {
                deliveryAdapter.removeLoadingView();
                scrollListener.setLoaded();
                isNetworkError = true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery_list, container, false);
        initView(view);
        setEvent();
        return view;
    }

}
