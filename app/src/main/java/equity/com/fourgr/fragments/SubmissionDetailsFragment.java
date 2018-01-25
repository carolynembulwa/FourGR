package equity.com.fourgr.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import equity.com.fourgr.R;
import equity.com.fourgr.helper.DateHelper;
import equity.com.fourgr.model.Submission;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubmissionDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubmissionDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubmissionDetailsFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SUBMISSION_DATA = "data";

    // TODO: Rename and change types of parameters
    private Submission submission;

    private OnFragmentInteractionListener mListener;

    public SubmissionDetailsFragment(){
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param submission Parameter 1.
     * @return A new instance of fragment SubmissionDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubmissionDetailsFragment newInstance(Submission submission) {
        SubmissionDetailsFragment fragment = new SubmissionDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SUBMISSION_DATA, submission);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            submission = getArguments().getParcelable(ARG_SUBMISSION_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_submission_details, container, false);
//        this.submission = getArguments().getParcelable(ARG_SUBMISSION_DATA);
        TextView txtSubmissionDate = (TextView) rootView.findViewById(R.id.submission_date);
        TextView txtCamp = (TextView) rootView.findViewById(R.id.camp);
        TextView txtTown = (TextView) rootView.findViewById(R.id.town);
        TextView txtRefugeeid= (TextView) rootView.findViewById(R.id.refugeeId);
        TextView txtFirstName = (TextView) rootView.findViewById(R.id.firstname);
        TextView txtLastname = (TextView) rootView.findViewById(R.id.lastname);
        Spinner txtSpinner = (Spinner) rootView.findViewById(R.id.spinner1);
        TextView txtComments = (TextView) rootView.findViewById(R.id.other_comments);

        txtSubmissionDate.setText(DateHelper.FormatDateString(submission.get_submission_date(), "yyyy-MM-dd", "dd-MM-yyyy"));
        txtCamp.setText(submission.get_camp());
        txtTown.setText(submission.get_town());
        txtFirstName.setText(submission.get_firstname());
        txtLastname.setText(submission.get_lastname());
        txtRefugeeid.setText(submission.get_refugee_id());
        //txtSpinner.setSelection(submission.get_spinner1());
        txtRefugeeid.setText(submission.get_refugee_id());
        txtComments.setText(submission.get_other_comments());

        return rootView;
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
