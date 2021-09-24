package com.example.mygooglemap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mygooglemap.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        OnMyLocationButtonClickListener,
        GoogleMap.OnMapClickListener,
        OnMapReadyCallback, GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private int MY_LOCATION_REQUEST_CODE = 1;

    // csv파일 읽어오기
    private final String LANDMARK = "../assets/final_landmark.csv";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1);
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

        // 마커 표시(예제)
        LatLng tour = new LatLng(35.790228, 129.332082);
        mMap.addMarker(new MarkerOptions().position(tour).title("불국사"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tour, 12));

        // 저장소 경로
        String filePath = getFilesDir().toString();
        JavaCsvHelper csvHelper = new JavaCsvHelper(filePath);

        List<String[]> dataList = csvHelper.readAllCsvData(LANDMARK);
        for (String[] data : dataList){
            Log.d("mygooglemap", "data: " + Arrays.deepToString(data));
        }




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[]
            permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                mMap.setMyLocationEnabled(true);
            }
        }
    }


    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current Location:\n" + location, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }
    public static Location addToPoint(Context context){
        Location location = new Location("");
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = null;

        try{
            addresses = geocoder.getFromLocationName("포천시청", 3);
        } catch (IOException e){
            e.printStackTrace();
        }
        if(addresses != null){
            for(int i=0; i<addresses.size(); i++){
                Address lating = addresses.get(i);
                location.setLatitude(lating.getLatitude());
                location.setLongitude(lating.getLongitude());
            }
        }
        return location;
    }


}

