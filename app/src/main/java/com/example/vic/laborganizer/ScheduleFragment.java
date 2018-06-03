package com.example.vic.laborganizer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private SharedPreferences prefs = null;
    public static  List<String> DAYS_OF_WEEK = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");

    Context _context;
    BroadcastReceiver br;
    PendingIntent pi;
    AlarmManager am;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    public static ScheduleFragment newInstance(String page, String title) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, page);
        args.putString(ARG_PARAM2, title);
        fragment.setArguments(args);
        return fragment;
    }

    private void setup() {
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent i)
            {
                Log.e("RECEIVED: ", "CONFIRMED");
                prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String thisWeek = prefs.getString("current_week", "odd");
                String nextWeek = prefs.getString("next_week", "even");
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("current_week", nextWeek);
                editor.putString("next_week", thisWeek);

                editor.apply();
            }
        };
        _context.registerReceiver(br, new IntentFilter("com.example.vic.laborganizer.setup"));
        pi = PendingIntent.getBroadcast(_context, 0, new Intent("com.example.vic.laborganizer.setup"), 0);
        am = (AlarmManager)(_context.getSystemService(Context.ALARM_SERVICE));

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final TextView tvSchedule = (TextView) view.findViewById(R.id.textViewSchedule);
        tvSchedule.setText("");

        Button getSchedule = (Button) view.findViewById(R.id.buttonWriteSchedule);
        getSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSchedule.setText("");
                prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String groupname = prefs.getString("group_name", "FAF-151");
                Log.e("CURRENT WEEK IS: ", prefs.getString("current_week", "odd"));

                JSONDecoder decoder = new JSONDecoder();
                String group = ScheduleFormatter.getGroup(decoder.decodeFromFile("schedule.json", getActivity()), groupname);
                String weekType = prefs.getString("current_week", "odd");
                String week = ScheduleFormatter.getWeek(group, weekType);

                for(int i = 0; i < 5; i++) {
                    String currentDay = DAYS_OF_WEEK.get(i);
                    tvSchedule.append(Html.fromHtml("<b>" + currentDay + "</b>"));
                    tvSchedule.append("\n\n");
                    String currentDayLessons = ScheduleFormatter.getDailyLessons(week, DAYS_OF_WEEK.get(i));
                    for(int  j = 0; j < 6; j++) {
                        String lesson = ScheduleFormatter.getLesson(currentDayLessons, String.valueOf(j + 1));

                        if (!lesson.equals("No Lesson") && !lesson.equals("null")) {
                            tvSchedule.append("lesson " + String.valueOf(j + 1) + ": "  + ScheduleFormatter.getLessonDetail(lesson, "name") + "\n");
                            tvSchedule.append("cabinet:  " + ScheduleFormatter.getLessonDetail(lesson, "cabinet") + "\n");
                            tvSchedule.append("teacher:  " + ScheduleFormatter.getLessonDetail(lesson, "teacher") + "\n");
                            tvSchedule.append("at:  " + ScheduleFormatter.getLessonDetail(lesson, "start") + "\n\n");
                        }
                        else {
                            tvSchedule.append("lesson " + String.valueOf(j + 1) + ":"  + "No Lesson\n\n");
                        }
                    }
                }

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setup();
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _context = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _context.unregisterReceiver(br);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
