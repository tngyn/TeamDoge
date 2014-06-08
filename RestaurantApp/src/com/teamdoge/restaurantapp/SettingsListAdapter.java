package com.teamdoge.restaurantapp;

import java.util.ArrayList;

import com.teamdoge.schedules.Shifts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;


public class SettingsListAdapter extends BaseExpandableListAdapter {
 
 
    private LayoutInflater inflater;
    private ArrayList<Shifts> mParent;
    private ExpandableListView accordion;
    public int lastExpandedGroupPosition;    
    
 
    public SettingsListAdapter(Context context, ArrayList<Shifts> parent, ExpandableListView accordion) {
        mParent = parent;        
        inflater = LayoutInflater.from(context);
        this.accordion = accordion;       
        
	}
 
 
    @Override
    //counts the number of group/parent items so the list knows how many times calls getGroupView() method
    public int getGroupCount() {
        return mParent.size();
    }
 
    @Override
    //counts the number of children items so the list knows how many times calls getChildView() method
    public int getChildrenCount(int i) {
        return mParent.get(i).children.size();
    }
 
    @Override
    //gets the title of each parent/group
    public Object getGroup(int i) {
        return mParent.get(i).name;
    }
 
    @Override
    //gets the name of each item
    public Object getChild(int i, int i1) {
        return mParent.get(i).children.get(i1);
    }
 
    @Override
    public long getGroupId(int i) {
        return i;
    }
 
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }
 
    @Override
    public boolean hasStableIds() {
        return true;
    }
    
	// *******************************************************************************************************************//
	// 													  View 															  //
	// *******************************************************************************************************************//
 
    @Override
    //in this method you must set the text to see the parent/group on the list
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
    	
        if (view == null) {
            view = inflater.inflate(R.layout.settings_list_item_parent, viewGroup,false);
        }
        // set category name as tag so view can be found view later
        view.setTag(getGroup(i).toString());
        
        TextView textView = (TextView) view.findViewById(R.id.list_item_text_view);
        
        //"i" is the position of the parent/group in the list
        textView.setText(getGroup(i).toString());    
  
        
        //return the entire view
        return view;
    }
    
 
    @Override
    //in this method you must set the text to see the children on the list
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.settings_list_item_child, viewGroup,false);
        }
 
        
        CheckedTextView textView = (CheckedTextView) view.findViewById(R.id.list_item_text_child);
        TextView textView1 = (TextView) view.findViewById(R.id.list_item_text_child_position);
        
        //"i" is the position of the parent/group in the list and 
        //"i1" is the position of the child
        textView.setText(mParent.get(i).children.get(i1).name);
        textView1.setText(mParent.get(i).children.get(i1).position);
        textView.setChecked(mParent.get(i).children.get(i1).checked);
 
        
        //return the entire view
        return view;
    }
    
	// *******************************************************************************************************************//
	//                                                  End View                                                          //
	// *******************************************************************************************************************//
 
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    
    @Override
    /**
     * automatically collapse last expanded group
     * @see http://stackoverflow.com/questions/4314777/programmatically-collapse-a-group-in-expandablelistview
     */    
    public void onGroupExpanded(int groupPosition) {
    	
    	if(groupPosition != lastExpandedGroupPosition){
            accordion.collapseGroup(lastExpandedGroupPosition);
        }
    	
        super.onGroupExpanded(groupPosition);
     
        lastExpandedGroupPosition = groupPosition;
        
    }
    
    
    
}

