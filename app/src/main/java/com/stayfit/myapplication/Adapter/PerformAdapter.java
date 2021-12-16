package com.stayfit.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.stayfit.myapplication.Model.PerformModel;
import com.stayfit.myapplication.R;
import com.stayfit.myapplication.RecylerviewClickInterface;

import java.util.List;

public class PerformAdapter  extends RecyclerView.Adapter<PerformAdapter.PerformAdapter_Holder> {
    private Context mContext;
    private List<PerformModel> mData;
    private RecylerviewClickInterface recylerviewClickInterface;
    public PerformAdapter (android.content.Context mContext, List<PerformModel> mData, RecylerviewClickInterface recylerviewClickInterface) {
        this.mContext = mContext;
        this.mData = mData;
        this.recylerviewClickInterface = recylerviewClickInterface;
    }

    @NonNull
    @Override
    public PerformAdapter.PerformAdapter_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.card_perform_item,parent,false); //connecting to cardview
        return new PerformAdapter.PerformAdapter_Holder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull PerformAdapter.PerformAdapter_Holder holder, int position) {
        //String dPhotoURL = mData.get(position).getDayPhotoUrl();
        //Picasso.get().load(dPhotoURL).fit().centerCrop().into(holder.mItemImageView);

        String dsPhotoURL = mData.get(position).getExercisePhotoUrl();
        String dsName = mData.get(position).getExerciseName();
        long dlExcerciseDuration = mData.get(position).getExerciseiDuration();

        Picasso.get().load(dsPhotoURL).fit().centerCrop().into(holder.mItemImage);
        holder.mItemTittleText.setText(dsName);
        holder.mItemBioText.setText("00:"+dlExcerciseDuration);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class PerformAdapter_Holder extends RecyclerView.ViewHolder {


        ImageView mItemImage;
        TextView mItemTittleText;
        TextView mItemBioText;

        public PerformAdapter_Holder(@NonNull View itemView) {
            super(itemView);
            mItemImage = (ImageView)itemView.findViewById(R.id.perform_image) ;
            mItemTittleText = (TextView)itemView.findViewById(R.id.perform_title_id);;
            mItemBioText = (TextView)itemView.findViewById(R.id.perform_bio_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recylerviewClickInterface .onItemClick(getAdapterPosition());
                }
            });

        }
    }



}
