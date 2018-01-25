package equity.com.fourgr.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import equity.com.fourgr.DataSubmission;
import equity.com.fourgr.DraftSubmissionsActivity;
import equity.com.fourgr.MainActivity;
import equity.com.fourgr.OfflineDataActivity;
import equity.com.fourgr.R;
import equity.com.fourgr.model.Menu;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MenuViewHolder> {
    List<Menu> menuList;
    Context context;
    Activity activity;
    String[] colors = {"#AB47BC", "#2196F3", "#FF9800", "#009688", "#795548"};

    public RecyclerViewAdapter(List<Menu> menuList, Context context, Activity activity){
        this.menuList = menuList;
        this.context = context;
        this.activity = activity;
    }
    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_card_item, parent, false);
        MenuViewHolder menuViewHolder = new MenuViewHolder(v);
        return menuViewHolder;
    }

    @Override
    public void onBindViewHolder(final MenuViewHolder holder, int position) {
        Drawable colorBackground = new ColorDrawable(Color.parseColor("#ffffff"));
        Drawable image = context.getResources().getDrawable(menuList.get(position).get_menu_icon());

        LayerDrawable ld = new LayerDrawable(new Drawable[]{colorBackground, image});
        holder.imgIcon.setImageDrawable(ld);

        holder.txtMenuTitle.setText(menuList.get(position).get_menu_title());
        holder.txtMenuSlug.setText(menuList.get(position).get_menu_slug());
        holder.cv.setCardBackgroundColor(Color.parseColor(colors[new Random().nextInt(colors.length)]));

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtSlug = (TextView) v.findViewById(R.id.menu_slug);
                String slug = txtSlug.getText().toString();
                Class newActivity = null;
                switch (slug){
                    case "enter_data_action":
                        newActivity = DataSubmission.class;
                        break;

                    case "offline_data_action":
                        newActivity = OfflineDataActivity.class;
                        break;

                    case "draft_data_action":
                        newActivity = DraftSubmissionsActivity.class;
                        break;

                    default:
                        newActivity = MainActivity.class;
                        break;
                }
                Intent intent = new Intent(context, newActivity);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.menuList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder{

        CardView cv;
        TextView txtMenuTitle, txtMenuSlug;
        CircleImageView imgIcon;

        public MenuViewHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cv);
            txtMenuTitle = (TextView) itemView.findViewById(R.id.menu_text);
            txtMenuSlug = (TextView) itemView.findViewById(R.id.menu_slug);
            imgIcon = (CircleImageView) itemView.findViewById(R.id.menu_icon);
        }
    }
}
