package com.example.adrian.leagueplayers.activity;

/**
 * Created by sscsis on 1/10/15.
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.app.Activity;
import android.app.ListActivity;
import com.example.adrian.leagueplayers.R;
import com.example.adrian.leagueplayers.service.HttpService;
import com.example.adrian.leagueplayers.service.ServiceException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Async task class to get json by making HTTP call
 * */
public class GetPlayersTask extends AsyncTask<Void, Void, ArrayList<Map<String, String>>> {
    private ListActivity activity;
    private String url = "http://mi.nba.com/statsm2/league/playerlist.json";
    public GetPlayersTask(ListActivity activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Map<String, String>> doInBackground(Void... arg0) {
        ArrayList<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        HttpService http = new HttpService();

        try {
            String response = http.get(url);
            JSONObject json = new JSONObject(response);

            JSONObject payload = json.getJSONObject("payload");
            JSONArray playersJson = payload.getJSONArray("players");
            for(int i=0; i < playersJson.length(); i++){
                JSONObject player = playersJson.getJSONObject(i);
                JSONObject profile = player.getJSONObject("playerProfile");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", profile.getString("firstName") + " " + profile.getString("lastName"));
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
        /**
         * Updating parsed JSON data into ListView
         * */
        ListAdapter adapter = new SimpleAdapter(
                activity,
                dataList,
                R.layout.list_item,
                new String[] { "name"},
                new int[] { R.id.name});

        activity.setListAdapter(adapter);
    }

}
