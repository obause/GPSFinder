package obause.example.mapstest2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    TextView timeTextView;
    TextView latTextView;
    TextView longTextView;
    TextView heightTextView;
    TextView speedTextView;
    TextView addressTextView;
    TextView accuracyTextView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    public void updateLocationInfo (Location location) {
        //timeTextView.setText(String.format("%d", location.getTime()));
        latTextView.setText(String.format("%.5f°", location.getLatitude()));
        longTextView.setText(String.format("%.5f°", location.getLongitude()));
        heightTextView.setText(String.format("%.2f m", location.getAltitude()));
        speedTextView.setText(String.format("%.2f km/h", location.getSpeed()));
        accuracyTextView.setText(String.format("%.2f m", location.getAccuracy()));

        String address = "";
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> listAdresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (listAdresses != null && listAdresses.size() > 0) {

                if (listAdresses.get(0).getCountryName() != null) {
                    address += listAdresses.get(0).getCountryName() + "\n";
                }
                if(listAdresses.get(0).getPostalCode() != null) {
                    address += listAdresses.get(0).getPostalCode() + " ";
                }
                if(listAdresses.get(0).getLocality() != null) {
                    address += listAdresses.get(0).getLocality() + "\n";
                }
                if(listAdresses.get(0).getThoroughfare() != null) {
                    address += listAdresses.get(0).getThoroughfare() + " ";
                }
                if(listAdresses.get(0).getSubThoroughfare() != null) {
                    address += listAdresses.get(0).getSubThoroughfare() + " ";
                }
            } else {
                address = "Keine Adresse gefunden";
            }

            addressTextView.setText(address);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeTextView = findViewById(R.id.timeTextView);
        latTextView = findViewById(R.id.latTextView);
        longTextView = findViewById(R.id.longTextView);
        heightTextView = findViewById(R.id.heightTextView);
        speedTextView = findViewById(R.id.speedTextView);
        addressTextView = findViewById(R.id.addressTextView);
        accuracyTextView = findViewById(R.id.accuracyTextView);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);
            }
        };

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            updateLocationInfo(lastLocation);
        }
    }
}