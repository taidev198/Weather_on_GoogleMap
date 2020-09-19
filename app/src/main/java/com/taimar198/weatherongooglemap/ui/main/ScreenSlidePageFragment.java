package com.taimar198.weatherongooglemap.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.taimar198.weatherongooglemap.R;
import com.taimar198.weatherongooglemap.data.api.response.WeatherForecastResponse;
import com.taimar198.weatherongooglemap.utls.Methods;

import static com.taimar198.weatherongooglemap.utls.Methods.formatDate;

public class ScreenSlidePageFragment extends Fragment {

    private WeatherForecastResponse mCurrentWeather;
    private ImageView mIconWeather;
    private ImageView mIconWeather1;
    private ImageView mIconWeather2;
    private ImageView mIconWeather3;
    private TextView textView1;
    private TextView mAddressText;
    private TextView mTempText;
    private TextView mDateText;
    public ScreenSlidePageFragment(WeatherForecastResponse currentWeather) {
        mCurrentWeather = currentWeather;
    }
    public static ScreenSlidePageFragment  newInstance(WeatherForecastResponse currentWeather) {
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
        mDateText.setText(Methods.formatDate());

        mIconWeather.setImageResource(Methods.getDrawable(mCurrentWeather.getCurrentWeather().getWeathers().get(0).getIcon(),
                                                            this.getContext()));
//        mIconWeather1.setImageBitmap((mCurrentWeather.getCurrentWeather().getWeathers().get(0).getIcon()));
//        mIconWeather2.setImageBitmap((mCurrentWeather.getCurrentWeather().getWeathers().get(0).getIcon()));
//        mIconWeather3.setImageBitmap((mCurrentWeather.getCurrentWeather().getWeathers().get(0).getIcon()));
        mAddressText.setText(mCurrentWeather.getCurrentWeather().getWeathers().get(0).getDescription());
        mTempText.setText(String.valueOf( mCurrentWeather.getCurrentWeather().getTemp()));
//        mTempText.setText(1);
        return rootView;
    }


}
