package com.taimar198.weatherongooglemap.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.taimar198.weatherongooglemap.R;
import com.taimar198.weatherongooglemap.data.model.CurrentWeather;
import com.taimar198.weatherongooglemap.data.model.WeatherForecast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ScreenSlidePageFragment extends Fragment {

    private WeatherForecast mCurrentWeather;
    private ImageView mIconWeather;
    private ImageView mIconWeather1;
    private ImageView mIconWeather2;
    private ImageView mIconWeather3;
    private TextView textView1;
    private TextView mAddressText;
    private TextView mTempText;
    private TextView mDateText;
    public ScreenSlidePageFragment(WeatherForecast currentWeather) {
        mCurrentWeather = currentWeather;
    }
    public static ScreenSlidePageFragment  newInstance(WeatherForecast currentWeather) {
        return new ScreenSlidePageFragment(currentWeather);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.cardview_weather, container, false);
        mIconWeather = rootView.findViewById(R.id.icon_weather);
        mIconWeather1 = rootView.findViewById(R.id.img_next_day);
        mIconWeather2 = rootView.findViewById(R.id.img_next_two_day);
        mIconWeather3 = rootView.findViewById(R.id.img_next_three_day);
        mTempText = rootView.findViewById(R.id.text_temp);
        mAddressText = rootView.findViewById(R.id.address_weather);
        mDateText = rootView.findViewById(R.id.date_weather);
        mDateText.setText(formatDate());

        mIconWeather.setImageBitmap(mCurrentWeather.getCurrentWeather().getIcon());
        mIconWeather1.setImageBitmap(mCurrentWeather.getCurrentWeather().getIcon());
        mIconWeather2.setImageBitmap(mCurrentWeather.getCurrentWeather().getIcon());
        mIconWeather3.setImageBitmap(mCurrentWeather.getCurrentWeather().getIcon());
        mAddressText.setText(mCurrentWeather.getCurrentWeather().getWeather());
        mTempText.setText(String.valueOf( mCurrentWeather.getCurrentWeather().getTemp().getTemperature()));
//        mTempText.setText(1);
        return rootView;
    }

    private String formatDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        return df.format(c);
    }
}
