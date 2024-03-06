package com.project_five.serverconnection.parser;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.project_five.serverconnection.R;
import com.project_five.serverconnection.model.Country;
import com.project_five.serverconnection.model.CountryForGSONLibrary;
import com.project_five.serverconnection.utils.Helper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    List<Country> countries;
    public List<Country> jsonParser(InputStream json_inputStream){
//        String json_string = Helper.inputStreamToString(json_inputStream);
        String json_string = Helper.inputStreamToStringV2(json_inputStream);
        return jsonParser(json_string);
    }
    public List<Country> jsonParser(String json_string){
        Log.d("json_string", json_string + "nnn");
        countries = new ArrayList<Country>();
        try {
            JSONObject root_object = new JSONObject(json_string);
            JSONArray jsonArray = root_object.getJSONArray("countries");
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Country country = new Country();
                country.setName(jsonObject.getString("name"));
                country.setCode(jsonObject.getString("code"));
                country.setRank(jsonObject.getInt("rank"));
                country.setId(jsonObject.getInt("id"));
                countries.add(country);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return countries;
    }

    public CountryForGSONLibrary jsonParserUsingGSON(InputStream is){
        CountryForGSONLibrary countryForGSONLibrary = null;
        InputStream inputStream = is;
        String json_string = null;
        try {
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
            json_string = new String(data, "UTF-8");
            Gson gson = new Gson();
            countryForGSONLibrary = gson.fromJson(json_string, CountryForGSONLibrary.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return countryForGSONLibrary;
    }
}
