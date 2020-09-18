package com.taimar198.weatherongooglemap.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentPagerAdapter;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.kml.KmlContainer;
import com.google.maps.android.data.kml.KmlPlacemark;
import com.google.maps.android.data.kml.KmlPolygon;
import com.google.maps.android.heatmaps.WeightedLatLng;
import com.taimar198.weatherongooglemap.BuildConfig;
import com.taimar198.weatherongooglemap.R;
import com.taimar198.weatherongooglemap.data.api.UtilsApi;
import com.taimar198.weatherongooglemap.data.api.WeatherApi;
import com.taimar198.weatherongooglemap.data.api.response.WeatherForecastResponse;
import com.taimar198.weatherongooglemap.data.model.PlaceMark;
import com.taimar198.weatherongooglemap.data.repository.CurrentWeatherRepository;
import com.taimar198.weatherongooglemap.ui.appwidget.WeatherAppWidget;
import com.taimar198.weatherongooglemap.ui.map.MapContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
 * https://www.google.com/search?q=reading+coordinates+in+kml+file+with+jsoup&oq=reading+coordinates+in+kml+file+with+jsoup&aqs=chrome..69i57.19692j0j4&sourceid=chrome&ie=UTF-8
 * */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        MapContract.View, WeatherAppWidget.OnDataReceived {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 123;
    private GoogleMap mMap;
    private CurrentWeatherRepository mCurrentWeatherRepository;
    TileOverlay tileOverlay ;
    private PlacesClient placesClient;
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private boolean locationPermissionGranted = false;
    private static final int DEFAULT_ZOOM = 15;
    private WeatherApi mWeatherApi;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;
    private WeatherForecastResponse mWeatherForecastResponse;
    private List<PlaceMark> mPlaceMarkList;
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentPagerAdapter pagerAdapter;
    private ArrayList<LatLng>    mLatLngList = new ArrayList<>();
    private WeatherAppWidget.OnDataReceived mOnDataReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        init();

    }

    private void init() {
        InputStream in_s = getResources().openRawResource(R.raw.diaphanhuyen);
        mPlaceMarkList = new ArrayList<>();
        Document doc = null;
        try {
            PlaceMark placeMark = new PlaceMark();
            doc = Jsoup.parse(in_s, "UTF-8", "");
            for(Element e : doc.select("Placemark")) {
                // the contents
               Elements es =  e.select("SimpleData");
               placeMark.setProvince(es.get(2).text());
                placeMark.setDistrict(es.get(3).text());
                placeMark.setPopulation(Integer.parseInt(es.get(4).text()));
//                placeMark.setDistrict(es.get(5).text());
               String [] coors =  e.select("coordinates").text().split(",");
               List<LatLng> latLngs = new ArrayList<>();
//                System.out.println(Arrays.toString(coors));
               for (int i = 0; i< coors.length -1; i++) {
                   String[] temp = coors[i].trim().split(" ");
                   if (i ==0 ) {
                       latLngs.add(new LatLng(Double.parseDouble(coors[0]),
                               Double.parseDouble(coors[coors.length-1])));
                   }else
                   latLngs.add(new LatLng(Double.parseDouble(temp[0]),
                           Double.parseDouble(temp[1])));
//                   System.out.println(latLngs.toString());
            }
               placeMark.setLatLngs(latLngs);
               mPlaceMarkList.add(placeMark);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        mWeatherForecastResponse = new WeatherForecastResponse();
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

        mPager = findViewById(R.id.weather_pager);
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
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mWeatherApi = UtilsApi.getAPIService();
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
                        System.out.println(weatherForecastResponses
                                .getCurrentWeather()
                                .getWeathers()
                                .get(0)
                                .getDescription());
                        mWeatherForecastResponse = weatherForecastResponses;
                        if (pagerAdapter != null) {
                            pagerAdapter.notifyDataSetChanged();
                            System.out.println("not null");
                        }else {
                            pagerAdapter = new CardAdapter(getSupportFragmentManager(),weatherForecastResponses,2);
                        }
                        mPager.setAdapter(pagerAdapter);
//                        mPager.getAdapter().notifyDataSetChanged();
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

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // Permission not yet granted. Use requestPermissions().
            // MY_PERMISSIONS_REQUEST_SEND_SMS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            // Permission already granted. Enable the SMS button.
            //new AsyncTaskTest().execute();
            if(mMap != null) {
                mMap.setMyLocationEnabled(true);
                setUpMap();
            }
        }
    }

    private void setUpMap() {

        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
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

    private void setOnClick() {
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
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
        checkPermission();
        setOnClick();
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng hanoi = new LatLng(21.027763, 105.834160);
        Marker marker = mMap.addMarker(new MarkerOptions()
        .position(hanoi)
                .title("Ha Noi")
                .snippet("Description"));
        marker.showInfoWindow();
//        mMap.addMarker(new MarkerOptions()
//                .position(hanoi)
//                .title("Ha Noi")
//                .snippet("Description")
//                .icon(BitmapDescriptorFactory
//                        .fromBitmap(createDrawableFromView(
//                                this,
//                                markerView))));
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(hanoi));
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
//        CameraUpdate zoom = CameraUpdateFactory.zoomTo(0.1f);
//        mMap.animateCamera(zoom);
//        try {
//            ArrayList<WeightedLatLng> result = generateHeatMapData();
////            new CrimeData().getWeightedPositions();
//            HeatmapTileProvider heatmapTileProvider = new HeatmapTileProvider.Builder()
//                    .weightedData(result) // load our weighted data
//                    .radius(50) // optional, in pixels, can be anything between 20 and 50
//                    .maxIntensity(1000.0) // set the maximum intensity
//                    .build();
//            mMap.addTileOverlay(new TileOverlayOptions().tileProvider(heatmapTileProvider));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .addAll(mPlaceMarkList.get(0).getLatLngs()));

        System.out.println(mPlaceMarkList.get(0).getDistrict());

    }

    public void accessContainers(Iterable<KmlContainer> containers) {
        for (KmlContainer container : containers) {
            if (container != null) {
                if (container.hasContainers()) {
                    accessContainers(container.getContainers());
                } else {
                    if (container.hasPlacemarks()) {
                        accessPlacemarks(container.getPlacemarks());
                    }
                }
            }
        }
    }

    public void accessPlacemarks(Iterable<KmlPlacemark> placemarks) {
        for (KmlPlacemark placemark : placemarks) {
            if (placemark != null) {
              Iterable<String> iterable =   placemark.getPropertyKeys();
//              for (String s : iterable) {
//                  System.out.println(s + "sout");
//              }
                System.out.println(placemark.toString());
                Geometry<LatLng> geometry = placemark.getGeometry();
                if (geometry instanceof KmlPolygon) {
                    KmlPolygon polygon = (KmlPolygon) geometry;
                    mLatLngList.addAll(polygon.getOuterBoundaryCoordinates());
                }


            }
        }
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

    public void updateTileOverlayTransparency() {
        if (tileOverlay != null) {
            // Switch between 0.0f and 0.5f transparency.
            tileOverlay.setTransparency(0.5f - tileOverlay.getTransparency());
        }
    }

    private ArrayList<WeightedLatLng> generateHeatMapData() throws JSONException {
         ArrayList<WeightedLatLng> result = new ArrayList<>();

        JSONArray jsonData = getJsonDataFromAsset("data.json");
        for (int i =0; i< jsonData.length(); i++){

            JSONObject jsonObject = jsonData.getJSONObject(i);
                double lat = jsonObject.getDouble("lat");
            double lon = jsonObject.getDouble("lon");
            double density = jsonObject.getDouble("density");

                if (density != 0.0) {
                    WeightedLatLng weightedLatLng = new WeightedLatLng(new LatLng(lat, lon), density);
                    result.add(weightedLatLng);
                }
            }
        return result;
    }


    private JSONArray getJsonDataFromAsset(String fileName) throws JSONException {
        String json = null;
        try {
            InputStream is = this.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return new JSONArray(json);
    }

    private Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        System.out.println("hello");
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        System.out.println("location" + location.getLatitude() +"---" + location.getLongitude());
    }

    @Override
    public void loadMap() {

    }

    @Override
    public void showLocationPermissionNeeded() {

    }

    @Override
    public void addMarkerToMap(MarkerOptions options, LatLng latLng) {

    }

    @Override
    public void setPresenter(MapContract.Presenter presenter) {

    }

    @Override
    public void receiveData(WeatherForecastResponse weatherForecastResponse) {

    }
}
