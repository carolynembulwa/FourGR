package equity.com.fourgr.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import equity.com.fourgr.R;
import equity.com.fourgr.db.DatabaseHandler;
import equity.com.fourgr.model.Submission;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReviewDataFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReviewDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewDataFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SUBMISSION_ID = "submission_id";

    List<ReviewData> reviewDataList = new ArrayList<ReviewData>();
    DatabaseHandler db;
    ListView listView;

    // TODO: Rename and change types of parameters
    private int mSubmissionId;

    private OnFragmentInteractionListener mListener;

    public ReviewDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param submission_id Parameter 1.
     * @return A new instance of fragment ReviewDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewDataFragment newInstance(int submission_id) {
        ReviewDataFragment fragment = new ReviewDataFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SUBMISSION_ID, submission_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSubmissionId = getArguments().getInt(ARG_SUBMISSION_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_data, container, false);
        db = new DatabaseHandler(getContext());
        this.reviewDataList = this.getReviewDataList();

        listView = (ListView) view.findViewById(R.id.submission_data_list);
        ReviewDataListAdapter adapter = new ReviewDataListAdapter(getActivity());

        listView.setAdapter(adapter);
        return view;
    }

    public List<ReviewData> getReviewDataList(){
        List<ReviewData> list = new ArrayList<ReviewData>();
        Submission submission = db.draftSubmission(mSubmissionId);

        try {
            ReviewData data = new ReviewData();
            data.setTitle(getContext().getString(R.string.prompt_date));
            data.setData(submission.get_submission_date());
            list.add(data);

            data = new ReviewData();
            data.setTitle("First Name");
            data.setData(submission.get_firstname());
            list.add(data);

            data = new ReviewData();
            data.setTitle("Last Name");
            data.setData(submission.get_lastname());
            list.add(data);

            data = new ReviewData();
            data.setTitle("Camp");
            data.setData(submission.get_camp());
            list.add(data);

            data = new ReviewData();
            data.setTitle(getContext().getString(R.string.prompt_town));
            data.setData(submission.get_town());
            list.add(data);

            data = new ReviewData();
            data.setTitle("Refugee ID");
            data.setData(submission.get_refugee_id());
            list.add(data);

            data = new ReviewData();
            data.setTitle("Are you going to school?");
            data.setData(submission.get_spinner1());
            list.add(data);



            data = new ReviewData();
            data.setTitle(getContext().getString(R.string.prompt_other_comments));
            data.setData(submission.get_other_comments());

            list.add(data);
        }catch (Exception ex){
        }
        return list;
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

    class ReviewData{
        private String title, data;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    class ReviewDataListAdapter extends BaseAdapter{
        Activity activity;
        LayoutInflater inflater;
        public ReviewDataListAdapter(Activity a){
            activity = a;
        }
        @Override
        public int getCount() {
            return reviewDataList.size();
        }

        @Override
        public Object getItem(int i) {
            return reviewDataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (inflater == null)
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (view == null)
                view = inflater.inflate(R.layout.data_item_row, null);

            TextView txtData = (TextView) view.findViewById(R.id.data_item);
            TextView txtTitle = (TextView) view.findViewById(R.id.title_item);

            ReviewData reviewData = reviewDataList.get(i);
            txtData.setText(reviewData.getData());
            txtTitle.setText(reviewData.getTitle());

            return view;
        }
    }
}
