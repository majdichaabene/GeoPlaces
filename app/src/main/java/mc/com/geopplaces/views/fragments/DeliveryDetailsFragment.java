package mc.com.geopplaces.views.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import mc.com.geopplaces.R;
import mc.com.geopplaces.models.entities.DeliveryEntity;
import mc.com.geopplaces.models.repositories.DeliveryRepository;


public class DeliveryDetailsFragment extends Fragment implements OnMapReadyCallback {


    private MapView mapView;
    private GoogleMap googleMap;
    private int deliveryId;
    private DeliveryRepository deliveryRepository;
    private DeliveryEntity deliveryEntity;
    private TextView descriptionTextView, addressTextView;
    private ImageView deliveryItemImageView;

    public DeliveryDetailsFragment() {
    }

    public static DeliveryDetailsFragment newInstance(int id) {
        DeliveryDetailsFragment fragment = new DeliveryDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("delivery_id", id);
        fragment.setArguments(args);
        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            deliveryId = bundle.getInt("delivery_id");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        readBundle(getArguments());
        deliveryRepository = new DeliveryRepository();
        deliveryEntity = new DeliveryEntity();
        deliveryEntity = deliveryRepository.getDeliveryById(deliveryId);
        addressTextView.setText(deliveryEntity.getAddress());
        descriptionTextView.setText(deliveryEntity.getDescription());
        Picasso.get()
                .load(deliveryEntity.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(deliveryItemImageView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_delivery_details, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_delivery_details_tb);
        descriptionTextView = view.findViewById(R.id.description_tv);
        addressTextView = view.findViewById(R.id.address_tv);
        deliveryItemImageView = view.findViewById(R.id.delivery_item_iv);
        mapView =  view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng location = new LatLng(deliveryEntity.getLat(), deliveryEntity.getLng());
        this.googleMap .addMarker(new MarkerOptions().position(location).title(deliveryEntity.getAddress()).snippet(deliveryEntity.getDescription()));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(14).build();
        this.googleMap .animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
