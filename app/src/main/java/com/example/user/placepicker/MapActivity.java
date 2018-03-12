package com.example.user.placepicker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class MapActivity extends AppCompatActivity {

    int PLACE_PICKER_REQUEST = 1;
    private PlacePicker.IntentBuilder builder;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        isLocationEnabled();
        if (!isLocationEnabled()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
            builder.setTitle("Title")
                    .setMessage("setting")
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            builder = new PlacePicker.IntentBuilder();
            try {
                ///builder.setLatLngBounds(new LatLngBounds(new LatLng(23.6850,23.6850),new LatLng(90.3563,90.3563)));
                startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    protected boolean isLocationEnabled() {
        String le = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(le);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return false;
        } else {
            return true;
        }
    }
}
