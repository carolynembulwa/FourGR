package equity.com.fourgr.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import equity.com.fourgr.R;
import equity.com.fourgr.db.DatabaseHandler;
import equity.com.fourgr.model.SubmissionPhoto;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReviewPhotoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReviewPhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewPhotoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SUBMISSION_ID = "submission_id";

    ArrayList<SubmissionPhoto> submissionPhotoList = new ArrayList<SubmissionPhoto>();
    DatabaseHandler db;

    // TODO: Rename and change types of parameters
    private int mSubmissionId;

    private OnFragmentInteractionListener mListener;

    public ReviewPhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param submission_id Parameter 1.
     * @return A new instance of fragment ReviewPhotoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewPhotoFragment newInstance(int submission_id) {
        ReviewPhotoFragment fragment = new ReviewPhotoFragment();
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
        View view = inflater.inflate(R.layout.fragment_review_photo, container, false);

        db = new DatabaseHandler(getContext());

        submissionPhotoList = db.getSubmissionPhotos(mSubmissionId);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.photos_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        ImageGalleryAdapter adapter = new ImageGalleryAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        return view;
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

    public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>{
        Activity activity;
        public ImageGalleryAdapter(Activity a){
            activity = a;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View photoView = inflater.inflate(R.layout.review_photo_row, parent, false);
            ImageGalleryAdapter.MyViewHolder viewHolder = new ImageGalleryAdapter.MyViewHolder(photoView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            SubmissionPhoto photo = submissionPhotoList.get(position);
            ImageView imageView = holder.mPhotoImageView;

            File file = new File(photo.getThumb());
            Picasso.with(getContext())
                    .load(file)
                    .into(imageView);
        }

        @Override
        public int getItemCount() {
            return submissionPhotoList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView mPhotoImageView;
            public MyViewHolder(View itemView) {
                super(itemView);
                mPhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo);
            }
        }
    }
}
