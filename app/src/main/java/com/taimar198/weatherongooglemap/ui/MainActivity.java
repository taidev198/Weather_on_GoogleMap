package com.taimar198.weatherongooglemap.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;
import com.taimar198.weatherongooglemap.R;
import com.taimar198.weatherongooglemap.data.model.CurrentWeather;
import com.taimar198.weatherongooglemap.data.repository.CurrentWeatherRepository;
import com.taimar198.weatherongooglemap.data.source.CurrentWeatherDataSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        CurrentWeatherDataSource.OnFetchDataListener {

    private GoogleMap mMap;
    private CurrentWeatherRepository mCurrentWeatherRepository;
    private static String OWM_TILE_URL = "https://tile.openweathermap.org/map/%s/%d/%d/%d.png";
    private Spinner spinner;
    private String tileType = "clouds";
    private TileOverlay tileOver;
    private ImageView mIconWeather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        init();
    }

    private void init() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mIconWeather = findViewById(R.id.icon_wether);
        mCurrentWeatherRepository = CurrentWeatherRepository.getInstance();
        mCurrentWeatherRepository.getCurrentWeather(this, "21.027763", "105.834160");
    }


    private TileProvider createTransparentTileProvider() {
        return new TransparentTileOWM(tileType);
    }

    private TileProvider createTilePovider() {
        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public URL getTileUrl(int x, int y, int zoom) {
                String fUrl = "https://openweathermap.org/img/wn/10d@2x.png";
                URL url = null;
                try {
                    url = new URL(fUrl);
                }
                catch(MalformedURLException mfe) {
                    mfe.printStackTrace();
                }

                return url;
            }
        } ;

        return tileProvider;
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
        View markerView = ((LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.activity_main2, null);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng hanoi = new LatLng(21, 105);
               // .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_background));
        mMap.addMarker(new MarkerOptions()
                .position(hanoi)
                .title("Ha Noi")
                .snippet("Description")
                .icon(BitmapDescriptorFactory
                        .fromBitmap(createDrawableFromView(
                                this,
                                markerView))));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hanoi));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);


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
    public void onFetchDataSuccess(CurrentWeather data) {
//        if (mIconWeather == null) {
//            mIconWeather = findViewById(R.id.icon_wether);
//            mIconWeather.setImageResource(R.drawable.custom_marker);
//        }

    }

    @Override
    public void onFetchDataFailure(Exception e) {

    }
}