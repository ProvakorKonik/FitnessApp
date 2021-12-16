package com.stayfit.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stayfit.myapplication.Model.DayModel;
import com.stayfit.myapplication.R;
import com.stayfit.myapplication.RecylerviewClickInterface;

import java.util.List;

public class DayAdapter   extends RecyclerView.Adapter<DayAdapter.DayAdapter_Holder> {
    private Context mContext;
    private List<DayModel> mData;
    private RecylerviewClickInterface recylerviewClickInterface;
    public DayAdapter (android.content.Context mContext, List<DayModel> mData, RecylerviewClickInterface recylerviewClickInterface) {
        this.mContext = mContext;
        this.mData = mData;
        this.recylerviewClickInterface = recylerviewClickInterface;
    }

    @NonNull
    @Override
    public DayAdapter.DayAdapter_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_day_item,parent,false); //connecting to cardview
        return new DayAdapter.DayAdapter_Holder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull DayAdapter.DayAdapter_Holder holder, int position) {
        //String dPhotoURL = mData.get(position).getDayPhotoUrl();
        //Picasso.get().load(dPhotoURL).fit().centerCrop().into(holder.mItemImageView);

        String dsTitle = mData.get(position).getDayName();
        long dlExcercise = mData.get(position).getDayiTotalExercise();

        holder.mItemTittleText.setText(dsTitle);
        if(dlExcercise == 0)
            holder.mItemBioText.setText("Rest Day");
        else
            holder.mItemBioText.setText(dlExcercise+" Exercises");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class DayAdapter_Holder extends RecyclerView.ViewHolder {


        TextView mItemTittleText;
        TextView mItemBioText;

        public DayAdapter_Holder(@NonNull View itemView) {
            super(itemView);

            mItemTittleText = (TextView)itemView.findViewById(R.id.level_d_title_id);
            mItemBioText = (TextView)itemView.findViewById(R.id.level_d_bio_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recylerviewClickInterface .onItemClick(getAdapterPosition());
                }
            });

        }
    }



}
