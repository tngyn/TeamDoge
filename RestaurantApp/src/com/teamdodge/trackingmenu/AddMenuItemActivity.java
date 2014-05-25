package com.teamdodge.trackingmenu;

import com.teamdoge.restaurantapp.R;
import com.teamdoge.restaurantapp.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class AddMenuItemActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		setContentView(R.layout.activity_add_menu_item);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

	    switch (item.getItemId()) {
	        case android.R.id.home:	
	        	onBackPressed();
	            this.finish();

	            return true;
	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }

	}
}
