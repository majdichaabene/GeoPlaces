package mc.com.geopplaces.views.activies;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;

import mc.com.geopplaces.R;
import mc.com.geopplaces.models.entities.DeliveryEntity;
import mc.com.geopplaces.models.repositories.DeliveryRepository;
import mc.com.geopplaces.models.repositories.OnDeliveryListLoadedCallback;
import mc.com.geopplaces.views.fragments.DeliveryCardFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.main_tb);
        toolbar.setTitle(getString(R.string.title_tb));
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.colorWhite));
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_ll, DeliveryCardFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        }

    }
    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container_ll);
        if (!(fragment instanceof DeliveryCardFragment)) {
            super.onBackPressed();
        } else {
            System.exit(0);
        }
    }
}
