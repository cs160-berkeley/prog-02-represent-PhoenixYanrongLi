package com.example.yanrongli.cs260a_hw2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SwipeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SwipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SwipeFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView textViewName;
    TextView textViewParty;
    TextView textViewTitle;
    TextView textViewLocation;
    TextView textViewVS;
    TextView textViewVoteObama;
    TextView textViewVoteRomney;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SwipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SwipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SwipeFragment newInstance(String param1, String param2) {
        SwipeFragment fragment = new SwipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_swipe, container, false);

        Bundle bundle = getArguments();
        int length = bundle.getInt("Length");
        int position = bundle.getInt("Position");

        if(position < length) {
            final String name = bundle.getString("Name");
            final String party = bundle.getString("Party");
            textViewName = (TextView) view.findViewById(R.id.frag_textView_name);
            textViewParty = (TextView) view.findViewById(R.id.frag_textView_party);
            textViewName.setText(name);
            textViewParty.setText(party);
            Button button = (Button) view.findViewById(R.id.button);
            if(party.equals("Republican")) {
                button.setBackgroundResource(R.drawable.border_red);
            }
            else if(party.equals("Democrat")) {
                button.setBackgroundResource(R.drawable.border_blue);
            }
            else {
                button.setBackgroundResource(R.drawable.border_white);
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //send message to ...
                    Toast.makeText(getActivity(), "Click!", Toast.LENGTH_SHORT).show();
                    String sendName = name;
                    Intent sendMessageRandom = new Intent(getActivity(), WatchToPhoneService.class);
                    sendMessageRandom.putExtra("string",sendName);
                    getActivity().startService(sendMessageRandom);
                }
            });

        }
        else if(position == length) {
            String county = bundle.getString("County");
            String state = bundle.getString("State");
            String obama_vote = bundle.getString("ObamaVote");
            String romney_vote = bundle.getString("RomneyVote");
            textViewTitle = (TextView) view.findViewById(R.id.frag_textView_name);
            textViewLocation = (TextView) view.findViewById(R.id.frag_textView_party);
            textViewVS = (TextView) view.findViewById(R.id.frag_textView_vs);
            textViewVoteObama = (TextView) view.findViewById(R.id.frag_textView_obamavote);
            textViewVoteRomney = (TextView) view.findViewById(R.id.frag_textView_romneyvote);
            Button button = (Button) view.findViewById(R.id.button);

            textViewTitle.setText("2012 Presidential Vote");
            textViewLocation.setText(county + ", " + state);
            textViewVS.setVisibility(View.VISIBLE);
            textViewVoteObama.setVisibility(View.VISIBLE);
            textViewVoteRomney.setVisibility(View.VISIBLE);
            button.setVisibility(View.INVISIBLE);
            textViewVoteObama.setText(obama_vote + "%");
            textViewVoteRomney.setText(romney_vote + "%");

        }

        return view;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

