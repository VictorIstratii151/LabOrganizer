package com.example.vic.laborganizer;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScheduleFormatter {

    public static String getGroup(String jsonSchedule, String groupFlag) {
        JSONObject group = null;
        try {
            JSONObject obj = new JSONObject(jsonSchedule);
            JSONArray groups = obj.getJSONArray("groups");

            for(int i = 0; i < groups.length(); i++) {
                JSONObject groupObj = groups.getJSONObject(i);
                String groupname = groupObj.keys().next();

                if(groupname.equals(groupFlag))
                {
                    group = groupObj;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("group-->", group.toString());

        return group.toString();
    }

    public String getWeek(String jsonSchedule, String weekFlag) {
        try {
            JSONObject obj = new JSONObject(jsonSchedule);
//            JSONArray m_jArry = obj.getJSONArray("groups");
//
//            for(int i = 0; i < m_jArry.length(); i++) {
//                JSONObject groupObj = m_jArry.getJSONObject(i);
//                String groupname = groupObj.keys().next();
//                JSONObject group = groupObj.getJSONObject(groupname);
//                JSONObject odd = group.getJSONObject("odd");
//                JSONObject monday = odd.getJSONObject("Monday");
//                JSONObject firstONe = monday.getJSONObject("1");
//                Log.d("prof name-->", firstONe.getString("teacher"));
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "sas";
    }
}
