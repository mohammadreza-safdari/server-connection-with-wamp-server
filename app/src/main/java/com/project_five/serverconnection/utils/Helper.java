package com.project_five.serverconnection.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Helper {
    public static String inputStreamToString(InputStream inputStream){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            for (String line; (line = bufferedReader.readLine()) != null; )
                stringBuilder.append(line);
            bufferedReader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static String inputStreamToStringV2(InputStream inputStream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null){
                stringBuilder.append(line);
            }
            reader.close();
            return stringBuilder.toString();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
