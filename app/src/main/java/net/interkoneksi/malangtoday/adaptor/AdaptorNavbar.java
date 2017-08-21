package net.interkoneksi.malangtoday.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.interkoneksi.malangtoday.JSONParser.ModelKategori;
import net.interkoneksi.malangtoday.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Ardhan MZ on 16-Aug-17.
 */

public class AdaptorNavbar extends BaseExpandableListAdapter {
    private Context mContext;
    private List<ExpandMenu> mListData;

    private HashMap<ExpandMenu,List<ModelKategori>> mListDataC;

    public AdaptorNavbar(Context context, List<ExpandMenu> listData, HashMap<ExpandMenu,
            List<ModelKategori>> listDataC){
        this.mContext=context;
        this.mListData = listData;
        this.mListDataC=listDataC;
    }
    @Override
    public int getGroupCount(){
        int i = mListData.size();
        return this.mListData.size();
    }
    @Override
    public int getChildrenCount(int groupPosition){
        List<ModelKategori> list = this.mListDataC.get(this.mListData.get(groupPosition));
        if (list == null){
            return 0;
        }else {
            return list.size();
        }
    }
    @Override
    public Object getGroup(int groupPosition){
        return this.mListData.get(groupPosition);
    }
    @Override
    public Object getChild(int groupPosition, int childPosition){
        return this.mListDataC.get(this.mListData.get(groupPosition)).get(childPosition).title;
    }
    @Override
    public long getGroupId(int groupPosition){
        return groupPosition;
    }
    @Override
    public long getChildId(int groupPostition, int childPosition){
        return childPosition;
    }
    @Override
    public boolean hasStableIds(){
        return false;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpand, View convertView, ViewGroup parent){
        ExpandMenu headerTitle = (ExpandMenu) getGroup(groupPosition);
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)
                    this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.navigation_list_header,null);
        }
        TextView listHeader = (TextView) convertView.findViewById(R.id.submenu);
        ImageView indicator = (ImageView) convertView.findViewById(R.id.indicator);
        listHeader.setText(headerTitle.iconName);
        if (getChildrenCount(groupPosition)>0){
            indicator.setVisibility(View.VISIBLE);
            if (mListData.get(groupPosition).isExpand){
                indicator.setImageResource(R.drawable.ic_expand_yes);
            }else {
                indicator.setImageResource(R.drawable.ic_expand_no);
            }
        }else {
            indicator.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent){
        final String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE
            );
            convertView = inflater.inflate(R.layout.navigation_list_menu, null);

        }TextView ListChild = (TextView) convertView.findViewById(R.id.submenu);
        ListChild.setText(childText);
        if (mListData.get(groupPosition).isExpand){

        }return convertView;
    }
    @Override
    public boolean isChildSelectable(int groupPostion, int childPosition){
        return true;
    }

    public static class ExpandMenu{
        public String iconName="";
        public int iconImg = -1;
        public boolean isExpand = false;

        public void toggle(){
            isExpand =  !isExpand;
        }
    }
    public void updated(){
        this.notifyDataSetChanged();
    }
}
