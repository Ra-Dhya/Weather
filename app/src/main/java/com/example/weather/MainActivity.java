package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Calendar;

//7a9a53eab325276a3cba58c649e19faf
public class MainActivity extends AppCompatActivity {

    EditText search;
    ImageFilterButton but;
    TextView city, temper, maxi, mini, humdity, condito, wind, cond, sunr, suns, sea, din, tharik;

    String url, bhaviour;

    ConstraintLayout cl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = findViewById(R.id.searchView);
        city = findViewById(R.id.city);
        temper = findViewById(R.id.temp);
        maxi = findViewById(R.id.max);
        mini = findViewById(R.id.min);
        humdity = findViewById(R.id.humdity);
        condito = findViewById(R.id.conditon);
        wind = findViewById(R.id.wind);
        cond = findViewById(R.id.cond);
        sunr = findViewById(R.id.sunrise);
        suns = findViewById(R.id.sunset);
        sea = findViewById(R.id.sea);
        but = findViewById(R.id.button);
        cl = findViewById(R.id.bhav);
        din = findViewById(R.id.day);
        tharik = findViewById(R.id.date);


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yy");
        String date = DateFormat.getDateInstance(dateFormat.MONTH_FIELD).format(calendar.getTime());
        tharik.setText(date);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        String day = simpleDateFormat.format(calendar.getTime());
        din.setText(day);
        url = "https://api.openweathermap.org/data/2.5/weather?q=ajmer&appid=7a9a53eab325276a3cba58c649e19faf";
        resuse(url);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cityn = search.getText().toString();
                if (cityn != null) {
                    url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityn + "&appid=7a9a53eab325276a3cba58c649e19faf";
                    resuse(url);
                    search.setText("");
                }

            }
        });


    }


    public void resuse(String url) {
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String name = response.getString("name");
                            city.setText(name);
                            JSONObject jsonObject = response.getJSONObject("main");
                            int tempe = jsonObject.getInt("temp");
                            temper.setText(String.valueOf(tempe - 273) + "°C");
                            int max = jsonObject.getInt("temp_max");
                            maxi.setText("MAX:" + String.valueOf(max - 273) + "°C");
                            int min = jsonObject.getInt("temp_min");
                            mini.setText("MIN:" + String.valueOf(min - 273) + "°C");
                            String humdi = jsonObject.getString("humidity");
                            humdity.setText(humdi + "%");
                            sea.setText(jsonObject.getString("pressure"));

                            JSONArray jsonArray = response.getJSONArray("weather");
                            JSONObject json = jsonArray.getJSONObject(0);
                            bhaviour = json.getString("main").toUpperCase();
                            Log.d(bhaviour, "onResponse: ");
                            condito.setText(bhaviour);
                            JSONObject win = response.getJSONObject("wind");
                            wind.setText(win.getString("speed") + "km/h");
                            JSONObject cloud = response.getJSONObject("clouds");
                            cond.setText(cloud.getString("all") + "%");
                            JSONObject sys = response.getJSONObject("sys");
                            long sr = sys.getLong("sunrise");
                            sunr.setText(String.valueOf(sr / (1000 * 60 * 60 * 60)) + "AM");
                            long sra = sys.getLong("sunset");
                            suns.setText(String.valueOf(sra / (1000 * 60 * 60 * 60)) + "PM");

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });


    }

}