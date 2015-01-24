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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Async task class to get json by making HTTP call
 * */
public class GetScheduleTask extends AsyncTask<Void, Void, ArrayList<Map<String, String>>>{
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
    protected ArrayList<Map<String, String>> doInBackground(Void... arg0) {
        ArrayList<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        String todayUtc  = null;
        HttpService http = new HttpService();

        try {
            String response = http.get(url);
            JSONObject json = new JSONObject(response);

            JSONObject payload = json.getJSONObject("payload");
            JSONObject today = payload.getJSONObject("today");
            JSONArray games = today.getJSONArray("games");
            todayUtc = today.getString("utcMillis");

            Calendar utcCal= Calendar.getInstance();
            utcCal.setTimeInMillis(Long.valueOf(todayUtc));

            SimpleDateFormat sdf= new SimpleDateFormat("EEE, MMM dd yyyy");
            Date utcDate= utcCal.getTime();
            String formattedDate = sdf.format(utcDate);


            for(int i=0; i < games.length(); i++){
                JSONObject homeTeam = games.getJSONObject(i).getJSONObject("homeTeam");
                JSONObject homeProfile = homeTeam.getJSONObject("profile");

                JSONObject awayTeam = games.getJSONObject(i).getJSONObject("awayTeam");
                JSONObject awayProfile = awayTeam.getJSONObject("profile");

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("date", formattedDate);


                String homeTeamName = homeProfile.getString("city") + " " + homeProfile.getString("name");
                String awayTeamName = awayProfile.getString("city") + " " + awayProfile.getString("name");
                map.put("homeTeamName", homeTeamName);
                map.put("awayTeamName", awayTeamName);
                map.put("matchup", awayTeamName + " @ " + homeTeamName);
                dataList.add(map);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    @Override
    protected void onPostExecute(ArrayList<Map<String, String>> dataList) {
        super.onPostExecute(dataList);

        TextView scheduleDate = (TextView)activity.findViewById(R.id.scheduleDate);
        if(dataList.size() > 0) {
            scheduleDate.setText(dataList.get(0).get("date"));
        }
        ListAdapter adapter = new SimpleAdapter(
                activity,
                dataList,
                R.layout.list_item,
                new String[] {"matchup"},
                new int[] { R.id.name});

        activity.setListAdapter(adapter);
    }

}
