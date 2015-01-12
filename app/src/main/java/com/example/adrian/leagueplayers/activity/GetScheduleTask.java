package com.example.adrian.leagueplayers.activity;

/**
 * Created by sscsis on 1/10/15.
 */

import android.app.ListActivity;
import android.os.AsyncTask;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.adrian.leagueplayers.R;
import com.example.adrian.leagueplayers.service.HttpService;
import com.example.adrian.leagueplayers.service.ServiceException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

/**
 * Async task class to get json by making HTTP call
 * */
public class GetScheduleTask extends AsyncTask<Void, Void, String>{
    private ListActivity activity;
    private String url = "http://mi.nba.com/statsm2/scores/miniscoreboard.json";
    public GetScheduleTask(ListActivity activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... arg0) {
        String todayUtc  = null;
        HttpService http = new HttpService();

        try {
            String response = http.get(url);
            JSONObject json = new JSONObject(response);

            JSONObject payload = json.getJSONObject("payload");
            JSONObject today = payload.getJSONObject("today");
            todayUtc = today.getString("utcMillis");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return todayUtc;
    }

    @Override
    protected void onPostExecute(String todayUtc) {
        super.onPostExecute(todayUtc);
        TextView scheduleDate = (TextView)activity.findViewById(R.id.scheduleDate);
        Calendar utcCal= Calendar.getInstance();
        utcCal.setTimeInMillis(Long.valueOf(todayUtc));

        SimpleDateFormat sdf= new SimpleDateFormat("EEE, MMM dd yyyy");
        Date utcDate= utcCal.getTime();
        String formattedDate = sdf.format(utcDate);

        scheduleDate.setText(formattedDate);
    }

}
