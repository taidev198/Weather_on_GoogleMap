package com.taimar198.weatherongooglemap.ui.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.taimar198.weatherongooglemap.BuildConfig;
import com.taimar198.weatherongooglemap.R;
import com.taimar198.weatherongooglemap.data.api.UtilsApi;
import com.taimar198.weatherongooglemap.data.api.WeatherApi;
import com.taimar198.weatherongooglemap.data.api.response.WeatherForecastResponse;
import com.taimar198.weatherongooglemap.data.model.PlaceMarkList;
import com.taimar198.weatherongooglemap.data.service.ParserCoorFromKML;
import com.taimar198.weatherongooglemap.ui.addressspinner.SpinnerProvinceListener;
import com.taimar198.weatherongooglemap.ui.appwidget.WeatherAppWidget;
import com.taimar198.weatherongooglemap.utls.Methods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**https://guides.codepath.com/android/Google-Maps-API-v2-Usage
 * https://stackoverflow.com/questions/15636303/extract-coordinates-from-kml-file-in-java
 * https://stackoverflow.com/questions/42516114/geoxml3-accessing-kml-attribute-datas
 * https://stackoverflow.com/questions/46346531/parsing-kml-in-java
 * https://stackoverflow.com/questions/1140144/read-and-parse-kml-in-java
 * https://developers.google.com/maps/documentation/geocoding/overview
 * https://stackoverflow.com/questions/3109158/how-to-draw-a-path-on-a-map-using-kml-file
 * https://www.tutlane.com/tutorial/android/android-xml-parsing-using-sax-parser
 * https://github.com/openstreetmap/splitter/blob/master/src/uk/me/parabola/splitter/kml/KmlParser.java
 * tutlane.com/tutorial/android/android-xml-parsing-using-sax-parser
 * //spinner
 * https://mkyong.com/android/android-spinner-drop-down-list-example/
 * //get current location
 * https://www.google.com/search?q=reading+coordinates+in+kml+file+with+jsoup&oq=reading+coordinates+in+kml+file+with+jsoup&aqs=chrome..69i57.19692j0j4&sourceid=chrome&ie=UTF-8
 * */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        ParserCoorFromKML.OnParsingData, AdapterView.OnItemSelectedListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 123;
    private GoogleMap mMap;
    private PlacesClient placesClient;
    private boolean locationPermissionGranted = false;
    private static final int DEFAULT_ZOOM = 15;
    private static final int DISTRICT_ZOOM = 12;
    private WeatherApi mWeatherApi;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;
    private WeatherForecastResponse mWeatherForecastResponse;
    private PlaceMarkList mPlaceMarkList;
    private ViewPager mPager;
    private Spinner mSpinnerProvince;
    private Spinner mSpinnerDistrict;


    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStatePagerAdapter pagerAdapter;
    private ArrayList<LatLng>  mLatLngList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        init();
    }

    private void init() {
        mSpinnerProvince = findViewById(R.id.spinner_province);
        mSpinnerDistrict = findViewById(R.id.spinner_district);
        new ParserCoorFromKML(getApplicationContext(), this).execute();
        mWeatherForecastResponse = new WeatherForecastResponse();
        mPager = findViewById(R.id.weather_pager);
        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mWeatherApi = UtilsApi.getAPIService();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //init autocomplete
        initAutocomplete();
    }

    private void initAutocomplete() {
        Places.initialize(getApplicationContext(), BuildConfig.CONSUMER_GMAP_KEY);
        // Create a new Places client instance.
        placesClient = Places.createClient(this);
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
//        autocompleteFragment.setTypeFilter(TypeFilter.CITIES);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS));
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(place.getLatLng().latitude,
                                place.getLatLng().longitude), DEFAULT_ZOOM));
                mMap.addMarker(new MarkerOptions().position(new LatLng(place.getLatLng().latitude,
                        place.getLatLng().longitude)).
                        title(place.getAddress()));
                System.out.println(place.getAddress());
                fetchingWeatherForecast(Double.toString(place.getLatLng().latitude),
                        Double.toString(place.getLatLng().longitude), place.getAddress());
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }


    private void fetchingWeatherForecast(String lat, String lon, String address) {
        mWeatherApi.requestRepos(lat,
                lon,
                "hourly,daily",
                "vi",
                "metric",
                "e370756ec8af6d31ce5f25668bf0bee8").subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WeatherForecastResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(WeatherForecastResponse weatherForecastResponses) {
                        mWeatherForecastResponse = weatherForecastResponses;
                        pagerAdapter = new CardAdapter(getSupportFragmentManager(),weatherForecastResponses,2);
                        pagerAdapter.notifyDataSetChanged();
                        mPager.setAdapter(pagerAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setUpMap() {

        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        //change position of my location
        View locationButton = ((View) findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);rlp.setMargins(0,0,30,30);
        mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(Circle circle) {
                // Flip the r, g and b components of the circle's
                // stroke color.
                int strokeColor = circle.getStrokeColor() ^ 0x00ffffff;
                circle.setStrokeColor(strokeColor);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }

    }
    updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (!locationPermissionGranted) {
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            mMap.setMyLocationEnabled(true);
            setUpMap();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }
    }
//https://stackoverflow.com/questions/9409195/how-to-get-complete-address-from-latitude-and-longitude
    //https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            lastKnownLocation = task.getResult();
                            //get address from lat and long
                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                            try {
                                addresses = geocoder.getFromLocation(lastKnownLocation.getLatitude(),
                                        lastKnownLocation.getLongitude(),
                                        1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = addresses.get(0).getLocality();
                                System.out.println(address + "---" + city);
                                if (lastKnownLocation != null) {
//                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                            new LatLng(lastKnownLocation.getLatitude(),
//                                                    lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                    fetchingWeatherForecast(Double.toString(lastKnownLocation.getLatitude()),
                                            Double.toString(lastKnownLocation.getLongitude()), address);
                                    Intent intent = new Intent(WeatherAppWidget.ACTION_TEXT_CHANGED);
                                    intent.putExtra("NewString", "tai");
                                    getApplicationContext().sendBroadcast(intent);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // Set the map's camera position to the current location of the device.
                        } else {

//                            mMap.moveCamera(CameraUpdateFactory
//                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    @Override
    public void onSuccess(PlaceMarkList placeMarkList) {
        mPlaceMarkList = placeMarkList;
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,
                placeMarkList.getLocation().keySet().toArray(new String[0]));
        mSpinnerProvince.setOnItemSelectedListener(new SpinnerProvinceListener(this, placeMarkList.getLocation()));
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        mSpinnerProvince.setAdapter(aa);
    }

    @Override
    public void onFailure(Exception e) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
        System.out.println(mSpinnerProvince.getSelectedItem().toString());
        System.out.println(adapterView.getItemAtPosition(pos));
        String province = mSpinnerProvince.getSelectedItem().toString();
        String district = adapterView.getItemAtPosition(pos).toString();
        mMap.clear();
        List<LatLng> latLngsFromAddress = new ArrayList<>();
        if (!province.equals("TỈNH") && !district.equals("HUYỆN") && !district.equals("TẤT CẢ")) {
            List<LatLng> latLngs = mPlaceMarkList.getLocation()
                    .get(province)
                    .get(district);
            Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .addAll(latLngs));
          Methods.getCoorsFromAddress(mWeatherApi, province, district, latLngsFromAddress);
            mMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(latLngs.get(0), DISTRICT_ZOOM));
        }else if (district.equals("TẤT CẢ")) {
            Map<String, List<LatLng>> districtList = mPlaceMarkList.getLocation()
                    .get(province);
            String[] disString = districtList.keySet().toArray(new String[0]);
            for (String s : disString) {
                if (!s.equals("HUYỆN") && !s.equals("TẤT CẢ")) {
                    Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                            .clickable(true)
                            .addAll(districtList.get(s)));
                    Methods.getCoorsFromAddress(mWeatherApi, province, s, latLngsFromAddress);
                    System.out.println(latLngsFromAddress.get(2));
                }
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
