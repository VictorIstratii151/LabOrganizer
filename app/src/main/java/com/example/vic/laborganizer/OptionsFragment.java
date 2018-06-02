package com.example.vic.laborganizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OptionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OptionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OptionsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int SUBGR_1_RADIO_ID = 0;
    private static final int SUBGR_2_RADIO_ID = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public static List<String> LAB_SUBGROUPS = Arrays.asList("subgr1", "subgr2");

    private OnFragmentInteractionListener mListener;
    private SharedPreferences prefs = null;
    private Spinner groupsSpinner;
    private RadioGroup subgrRadioGroup;
    private RadioButton radio_sub1;
    private RadioButton radio_sub2;
    private Button submitOptions;

    public OptionsFragment() {
        // Required empty public constructor
    }

    public static OptionsFragment newInstance(String page, String title) {
        OptionsFragment fragment = new OptionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, page);
        args.putString(ARG_PARAM2, title);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        groupsSpinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.group_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupsSpinner.setAdapter(adapter);
        groupsSpinner.setOnItemSelectedListener(this);


        subgrRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        radio_sub1 = (RadioButton) view.findViewById(R.id.radioSubgr1);
        radio_sub2 = (RadioButton) view.findViewById(R.id.radioSubgr2);
        radio_sub1.setId(SUBGR_1_RADIO_ID);
        radio_sub2.setId(SUBGR_2_RADIO_ID);
        int defaultRadioId = prefs.getInt("subgr_number", 1) - 1;
        ((RadioButton)subgrRadioGroup.getChildAt(defaultRadioId)).setChecked(true);

        submitOptions = (Button) view.findViewById(R.id.btnSubmitOptions);
        submitOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadio = subgrRadioGroup.getCheckedRadioButtonId();
                prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("group_name", groupsSpinner.getSelectedItem().toString());
                editor.putInt("subgr_number", selectedRadio + 1);

                editor.apply();

            }
        });



        Button btnPrefs = (Button) view.findViewById(R.id.buttonGetprefs);
        btnPrefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                Log.e("default group: ", prefs.getString("group_name", "FAF-151"));
                Log.e("default subgroup: ", String.valueOf(prefs.getInt("subgr_number", 1)));
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_options, container, false);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Toast.makeText(getContext(), "Selected item" + id,
                Toast.LENGTH_SHORT).show();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
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
