package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText field;
    private Button button;
    private TextView res;
    private TextView res2;
    private TextView res3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        field = findViewById(R.id.field);
        button = findViewById(R.id.button);
        res = findViewById(R.id.temperature);
        res2 = findViewById(R.id.weather);
        res3 = findViewById(R.id.feels);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(field.getText().toString().trim().equals(""))
                    Toast.makeText(MainActivity.this, R.string.nothing, Toast.LENGTH_LONG).show();
                else{
                    String city = field.getText().toString();
                    String key = "3be6ca9028ae5a05f83a4bfb4d71c5c2";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric";


                    new URLData().execute(url);
                }
            }
        });
    }

    private class URLData extends AsyncTask<String, String, String> {

        protected void onPreExecute(){
            super.onPreExecute();
            res.setText("Waiting");
            res2.setText("");
            res3.setText("");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection con = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                con = (HttpURLConnection) url.openConnection();
                con.connect();

                InputStream stream = con.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buf = new StringBuffer();
                String  line = "";

                while((line = reader.readLine()) != null)
                    buf.append(line).append("\n");

                return buf.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(con != null)
                    con.disconnect();

                try {
                if(reader != null)
                        reader.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }


            }

            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            try {
                JSONObject obj = new JSONObject(result);

                res.setText("Temperature: " + obj.getJSONObject("main").getDouble("temp"));
                res3.setText("Feels like: " + obj.getJSONObject("main").getDouble("feels_like"));
                res2.setText("Weather: " + obj.getJSONArray("weather").getJSONObject(Integer.parseInt("0")).getString("main"));

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}