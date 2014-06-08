package com.teamdoge.restaurantapp;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> categoryList;
    private List<String> items;
    
    public ExpandableListAdapter(Activity context, List<String> items,
            Map<String, List<String>> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
        this.items = items;
    }
 
    public Object getChild(int groupPosition, int childPosition) {
        return categoryList.get(items.get(groupPosition)).get(childPosition);
    }
 
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    public int getChildrenCount(int groupPosition) {
        return categoryList.get(items.get(groupPosition)).size();
    }
 
    public Object getGroup(int groupPosition) {
        return items.get(groupPosition);
    }
 
    public int getGroupCount() {
        return items.size();
    }
 
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
	// *******************************************************************************************************************//
	// 													  View 															  //
	// *******************************************************************************************************************//
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        String Name = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.fragment_inventory_list,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.categoryList);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(Name);
        return convertView;
    }
    
    
    
   public View getChildView(final int groupPosition, final int childPosition,
           boolean isLastChild, View convertView, ViewGroup parent) {
       final String Citem = (String) getChild(groupPosition, childPosition);
       LayoutInflater inflater = context.getLayoutInflater();
        
       if (convertView == null) {
           convertView = inflater.inflate(R.layout.fragment_inventory_item, null);
       }
        
       TextView item = (TextView) convertView.findViewById(R.id.categoryList);
       item.setText(Citem);

       return convertView;
   }
    
	// *******************************************************************************************************************//
	//                                                  End View                                                          //
	// *******************************************************************************************************************//
 
    public boolean hasStableIds() {
        return true;
    }
 
    public boolean isChildSelectable(int groupPosition, int childPosition) { 
        return true;
    }
    
}