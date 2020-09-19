package com.taimar198.weatherongooglemap.ui.addressspinner;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.taimar198.weatherongooglemap.R;

import java.util.List;
import java.util.Map;

public class SpinnerProvinceListener implements AdapterView.OnItemSelectedListener {
    private Map<String, List<String>> mProvince;
    private Activity mContext;
    private Spinner mSpinnerDistrict;

    public SpinnerProvinceListener(Activity context, Map<String, List<String>> province) {
        mProvince = province;
        mContext = context;
        mSpinnerDistrict = context.findViewById(R.id.spinner_district);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
    //create spinner district here?
        ArrayAdapter aa = new ArrayAdapter(mContext,android.R.layout.simple_spinner_item,
                mProvince.get(adapterView.getItemAtPosition(pos))
                        .toArray(new String[0]));
        mSpinnerDistrict.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) mContext);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        mSpinnerDistrict.setAdapter(aa);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
