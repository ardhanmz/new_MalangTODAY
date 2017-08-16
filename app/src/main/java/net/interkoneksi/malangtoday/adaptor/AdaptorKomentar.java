package net.interkoneksi.malangtoday.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.interkoneksi.malangtoday.R;
import net.interkoneksi.malangtoday.model.ModelKomentar;

import java.util.List;

/**
 * Created by Ardhan MZ on 16-Aug-17.
 */

public class AdaptorKomentar extends RecyclerView.Adapter<AdaptorKomentar.DataObjectHolder>{
    List<ModelKomentar> mDataset;
    Context context;

    //Membangun Konstruktor untuk Adaptor sebuah komentar dengan parameter Context dan List Komentar
    public AdaptorKomentar(Context context, List<ModelKomentar> myDataSet){
        this.mDataset=myDataSet;
        this.context= context;
    }

    @Override
    public AdaptorKomentar.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.baris_komen, parent,
                false);
        AdaptorKomentar.DataObjectHolder dataObjectHolder = new AdaptorKomentar.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(AdaptorKomentar.DataObjectHolder holder,int position){
        holder.title.setText(mDataset.get(position).name +"on:"+mDataset.get(position).date);
        holder.content.setText(Html.fromHtml(mDataset.get(position).content));
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder{
        TextView title, content;
        public DataObjectHolder(View itemView){
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
