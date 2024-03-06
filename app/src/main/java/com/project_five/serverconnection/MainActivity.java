package com.project_five.serverconnection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.project_five.serverconnection.adapter.CustomListViewArrayAdapter;
import com.project_five.serverconnection.model.Country;
import com.project_five.serverconnection.parser.JsonParser;
import com.project_five.serverconnection.utils.Helper;
import com.project_five.serverconnection.utils.HttpConnectionManager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<NetworkTask> tasks_List;
    ProgressBar pb;
    ListView lv;
    List<Country> countries;
    CustomListViewArrayAdapter<Country> countriesArrayAdapter;
    //room is wamp server folder
    public static final String main_url = "http://IP:PORT/room/";
    public static final String json_url = main_url+"countries.json";
    public static final String photos_url = main_url+"photo/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
        pb.setVisibility(View.INVISIBLE);
        tasks_List = new ArrayList<NetworkTask>();
        countries = new ArrayList<>();
    }

    private void setupViews() {
        pb = (ProgressBar) findViewById(R.id.pb);
        lv = (ListView) findViewById(R.id.lv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int Id = item.getItemId();
        if (Id == R.id.menu_get_data_from_server){
            NetworkTask networkTask = new NetworkTask();
            networkTask.execute(json_url);
        }
        return super.onOptionsItemSelected(item);
    }

    private class NetworkTask extends AsyncTask<String, String, InputStream>{
        @Override
        protected void onPreExecute() {
            Toast.makeText(MainActivity.this, "initializing...", Toast.LENGTH_SHORT).show();
            if (tasks_List.size() == 0){
                pb.setVisibility(View.VISIBLE);
            }
            tasks_List.add(this);
            super.onPreExecute();
        }

        @Override
        protected InputStream doInBackground(String... strings) {
            String uri = strings[0];
            InputStream inputStream = null, inputStream1 = null;
            Bitmap bitmap = null;
            publishProgress("(" + uri + ") " + "connecting to server ...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HttpConnectionManager manager = new HttpConnectionManager(uri);
            inputStream = manager.httpConnect();
            JsonParser jsonParser = new JsonParser();
            countries = jsonParser.jsonParser(inputStream);
            /*
                if we want download and set the bitmap of all photos at once
                and then display them in getView :
                try {
                    for (Country country : countries) {
                        URL url = new URL(photos_url + country.getName().replaceAll(" ", "_").toLowerCase() + ".jpg");
                        inputStream1 = (InputStream) url.getContent();
                        bitmap = BitmapFactory.decodeStream(inputStream1);
                        country.setBitmap(bitmap);
                        inputStream1.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
             */
            return inputStream;
        }
        //ui thread
        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(MainActivity.this, values[0], Toast.LENGTH_SHORT).show();
            super.onProgressUpdate(values);
        }
        //ui thread
        @Override
        protected void onPostExecute(InputStream inputStream) {
            updateUi();
            tasks_List.remove(this);
            if (tasks_List.size() == 0){
                pb.setVisibility(View.INVISIBLE);
            }
            super.onPostExecute(inputStream);
        }
    }
    private void updateUi(){
        if (countries != null){
            countriesArrayAdapter = new CustomListViewArrayAdapter(this,
                    R.layout.list_item, countries);
            lv.setAdapter(countriesArrayAdapter);
        }
    }
}