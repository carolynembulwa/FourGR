package equity.com.fourgr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import equity.com.fourgr.R;
import equity.com.fourgr.db.DatabaseHandler;
import equity.com.fourgr.model.Submission;

public class OfflineSubmissionsAdapter extends RecyclerView.Adapter<OfflineSubmissionsAdapter.MyViewHolder> {
    Context context;
    List<Submission> offlineList;
    public OfflineSubmissionsAdapter(Context context, List<Submission> offlineList){
        this.context = context;
        this.offlineList = offlineList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offline_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            Submission submission = this.offlineList.get(position);

            DatabaseHandler db = new DatabaseHandler(context);

            holder.txtSubmissionDate.setText(submission.get_submission_date());
            holder.txtName.setText(submission.get_firstname() + submission.get_lastname());
            holder.txtCamp.setText(submission.get_camp());
            holder.txtCounty.setText(submission.get_town());
        }catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return this.offlineList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtName, txtCamp, txtCounty, txtSubmissionDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.name);
            txtCamp = (TextView) itemView.findViewById(R.id.camp);
            txtSubmissionDate = (TextView) itemView.findViewById(R.id.submission_date);
        }
    }
}
