package com.teamdoge.restaurantapp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.os.Build;

public class InventoryList extends Activity {
	
	/***************ExpandableView for Inventory***********************/
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> InventoryList;
    ExpandableListView expListView;
    /*****************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inventory_list);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		 /***************ExpandableView for Inventory***********************/
        createGroupList();
        
        createCollection();
        
        expListView = (ExpandableListView) findViewById(R.id.categoryList);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
                this, groupList, InventoryList);
        expListView.setAdapter(expListAdapter);
        expListView.setOnChildClickListener(ExpandList_ItemClicked);
        
     /*****************************************************************/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inventory_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	 /***************ExpandableView for Inventory***********************/
    private OnChildClickListener ExpandList_ItemClicked =  new OnChildClickListener() {

		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			Intent mIntent = new Intent(InventoryList.this, Add_item.class);
			startActivity(mIntent);
			return false;
		}
		
	};
    /***************ExpandableView for Inventory***********************/
    private void createGroupList() {
        groupList = new ArrayList<String>();
        groupList.add(getString(R.string.category1));
        groupList.add(getString(R.string.category2));
    }
    
    private void createCollection() {
        // preparing items in category(child)
        String[] category1 = { getString(R.string.item), getString(R.string.item),
        		getString(R.string.item) };
        String[] category2 = { getString(R.string.item), getString(R.string.item),
        		getString(R.string.item) };
 
        InventoryList = new LinkedHashMap<String, List<String>>();
 
        for (String category : groupList) {
            if (category.equals("Category One")) {
                loadChild(category1);
            } else if (category.equals("Category Two"))
                loadChild(category2);
            
 
            InventoryList.put(category, childList);
        }
    }
    private void loadChild(String[] itemList) {
        childList = new ArrayList<String>();
        for (String Citem : itemList)
            childList.add(Citem);
    }

    /*****************************************************************/  
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_inventory_list,
					container, false);
			return rootView;
		}
	}

}
