package com.mycompany.foo;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Jose on 5/29/2015.
 */
public class CategoryAdapter extends BaseExpandableListAdapter{
    private Context ctx;
    private HashMap<String,List<String>> Category;
    private List<String> List;
    public CategoryAdapter(Context ctx,HashMap<String,List<String>> Category,List<String> List){
        this.ctx = ctx;
        this.Category=Category;
        this.List=List;
    }

    @Override
    public int getGroupCount() {
        return List.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Category.get(List.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return List.get(groupPosition);
    }

    @Override
    public Object getChild(int parent, int child) {
        return Category.get(List.get(parent)).get(child);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int parent, int child) {
        return child;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int parent, boolean isExpanded, View convertView, ViewGroup parentView) {
        String group_title = (String)getGroup(parent);
        //check for convert view equal to null
        if(convertView == null){
            LayoutInflater inflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //create convert view
            convertView = inflator.inflate(R.layout.parent_layout,parentView,false);
        }
        //get parent layout on XML file
        TextView parent_textview = (TextView)convertView.findViewById(R.id.parent_text);

        parent_textview.setTypeface(null, Typeface.BOLD);
        parent_textview.setText(group_title);
        return convertView;
    }

    @Override
    public View getChildView(int parent, int child, boolean lastChild, View convertView, ViewGroup parentView) {
        //get title for the child
        String child_title =(String) getChild(parent,child);

        //create convert view
        //if convert view is null
        if (convertView == null){
            LayoutInflater inflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //make a view
            convertView = inflator.inflate(R.layout.child_layout,parentView,false);
        }
        TextView child_textview = (TextView)convertView.findViewById(R.id.child_txt);
        try {
            child_textview.setText(child_title);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
