package com.jakfromspace.kotoandroid;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;


public class DataFetch extends AsyncTask<String, String, String> {


    String data = "";
    String details = "";
    String amnt = "";
    String dateTaken = "";
    String IsExpense;


    @Override
    protected String doInBackground(String ... Strings) {

        try {
            URL url = new URL("http://192.168.43.118:8000/transactions/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }
            try {
                JSONArray JA = new JSONArray(data);
                for (int i = 0; i < JA.length(); i++) {
                    JSONObject JO = (JSONObject) JA.get(i);

                    //DEBUG
                    details =  JO.get("Details").toString();
                    IsExpense = JO.get("isExpence").toString();
                    amnt = JO.get("amount").toString();
                    dateTaken =  JO.get("date").toString();
                    amnt = JO.get("amount").toString();
                    Log.d("Async",details+amnt+dateTaken);

                    KotoItem item = new KotoItem(details,Float.parseFloat(amnt));
                    item.setDateTaken(dateTaken);
                    item.isExpense(Boolean.parseBoolean(IsExpense));
                    MainActivity.kotoItems.add(item); //DEBUG

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(String aVoid){
        super.onPostExecute(aVoid);


        MainActivity.adapter.notifyDataSetChanged();
    }



}
