package com.example.vic.laborganizer;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScheduleFormatter {

    public static String getGroup(String jsonSchedule, String groupFlag) {
        JSONObject group = null;
        String groupStr = null;
        try {
            JSONObject obj = new JSONObject(jsonSchedule);
            JSONArray groups = obj.getJSONArray("groups");

            for(int i = 0; i < groups.length(); i++) {
                JSONObject groupObj = groups.getJSONObject(i);
                String groupname = groupObj.keys().next();

                if(groupname.equals(groupFlag))
                {
                    group = groupObj;
                    groupStr = group.getJSONObject(groupFlag).toString();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return groupStr;
    }

    public static String getWeek(String jsonGroup, String weekFlag) {
        String week = null;

        try {
            JSONObject obj = new JSONObject(jsonGroup);
            week = obj.getJSONObject(weekFlag).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return week;
    }
}
