package com.taimar198.weatherongooglemap.ui.main;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class HeatMapEx {

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

    private ArrayList<WeightedLatLng> generateHeatMapData(Context context) throws JSONException {
        ArrayList<WeightedLatLng> result = new ArrayList<>();

        JSONArray jsonData = getJsonDataFromAsset("data.json", context);
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


    private JSONArray getJsonDataFromAsset(String fileName, Context context) throws JSONException {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
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
}
