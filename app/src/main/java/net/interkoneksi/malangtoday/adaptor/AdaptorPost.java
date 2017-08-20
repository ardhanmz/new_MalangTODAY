package net.interkoneksi.malangtoday.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.interkoneksi.malangtoday.R;
import net.interkoneksi.malangtoday.model.ModelPost;

import java.util.List;

/**
 * Created by ardhanmz on 8/18/17.
 */

public class AdaptorPost extends RecyclerView.Adapter<AdaptorPost.DataObjectHolder> {



    List<ModelPost> mDataset;
    Context mContext;
    public AdaptorPost(Context context, List<ModelPost> mDataset){
        this.mDataset = mDataset;
        this.mContext=context;
    }
    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_post, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }
    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position){
        holder.title.setText(mDataset.get(position).title);
        if (mDataset.get(position).categories.size() > 0){
            holder.category.setVisibility(View.VISIBLE);
            holder.category.setText(mDataset.get(position).categories.get(0));
        }else {
            holder.category.setVisibility(View.GONE);
        }
        int count = mDataset.get(position).comment_count;
        if (!mDataset.get(position).comment_status.equals("open")&& !(count>0)){
            holder.comment.setVisibility(View.GONE);
            holder.comment_img.setVisibility(View.GONE);
        }
        else {
            holder.comment.setVisibility(View.VISIBLE);
            holder.comment_img.setVisibility(View.VISIBLE);
            holder.comment.setText(count+"");
        }

        Glide.with(mContext).
                load(mDataset.get(position).img)
                .placeholder(R.drawable.placeholder)
                .into(holder.image);
    }
    public void addItem(ModelPost obj, int index){
        mDataset.add(obj);
        notifyItemInserted(index);
    }
    public void update(List<ModelPost> list){
        mDataset = list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount(){
        return mDataset.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder{
        TextView title, comment, category;
        ImageView comment_img, image;

        public DataObjectHolder(View itemView){
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            comment = (TextView) itemView.findViewById(R.id.comment_count);
            category = (TextView) itemView.findViewById(R.id.category);
            image = (ImageView) itemView.findViewById(R.id.img);
            comment_img = (ImageView) itemView.findViewById(R.id.comment_count_img);

        }
    }


}
