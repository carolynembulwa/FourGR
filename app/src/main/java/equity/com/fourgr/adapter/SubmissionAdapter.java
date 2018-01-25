package equity.com.fourgr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.List;

import equity.com.fourgr.R;
import equity.com.fourgr.db.DatabaseHandler;
import equity.com.fourgr.helper.DateHelper;
import equity.com.fourgr.model.Submission;


public class SubmissionAdapter extends RecyclerView.Adapter<SubmissionAdapter.MyViewHolder> implements Filterable{
    List<Submission> submissionList;
    List<Submission> filteredList;
    Context context;
    private CustomFilter mFilter;

    public SubmissionAdapter(Context c, List<Submission> submissions){
        this.submissionList = submissions;
        this.filteredList = submissions;
        this.context = c;
        mFilter = new CustomFilter(SubmissionAdapter.this, submissions);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.submission_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Submission submission = this.submissionList.get(position);

        String submission_date = DateHelper.FormatDateString(submission.get_submission_date(), "yyyy-MM-dd", "dd.MM.Y");
        String submission_time = DateHelper.FormatDateString(submission.get_created_at(), "yyyy-MM-dd H:m:s", "H:mm");
//        String submission_date = submission.get_submission_date();
        holder.txtSubmissionDate.setText(submission_date);
        holder.txtBrandMediaOwner.setText(submission.get_firstname() + " " + submission.get_lastname());
        holder.txtStructureSize.setText(submission.get_camp() + " " + submission.get_town());
        holder.txtSubmissionTime.setText(submission_time);

    }

    @Override
    public int getItemCount() {
        return submissionList.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txtBrandMediaOwner, txtStructureSize, txtSubmissionDate, txtUser, txtSubmissionTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtBrandMediaOwner = (TextView) itemView.findViewById(R.id.name);
            txtStructureSize = (TextView) itemView.findViewById(R.id.camp);
            txtSubmissionDate = (TextView) itemView.findViewById(R.id.submission_date);
            txtSubmissionTime = (TextView) itemView.findViewById(R.id.submission_time);
        }
    }

    public class CustomFilter extends Filter {

        private SubmissionAdapter mAdapter;
        List<Submission> subs;
        private CustomFilter(SubmissionAdapter adapter, List<Submission> subs){
            super();
            mAdapter = adapter;
            this.subs = subs;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();
            DatabaseHandler db = new DatabaseHandler(context);
            List<Submission> submissions = db.allSubmissions();
            if (constraint.length() == 0) {
                filteredList.addAll(submissions);
            }else{
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (Submission submission : submissions){
                    if (submission.get_camp().toLowerCase().contains(filterPattern) || submission.get_town().toLowerCase().contains(filterPattern)){
                        filteredList.add(submission);
                    }
                }
            }

            System.out.println("Count Number " + filteredList.size());
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            this.mAdapter.notifyDataSetChanged();
        }
    }
}
