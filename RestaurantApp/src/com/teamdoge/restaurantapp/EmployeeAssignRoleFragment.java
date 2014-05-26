package com.teamdoge.restaurantapp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class EmployeeAssignRoleFragment extends Fragment {
    ArrayAdapter<String> adapter;
    OnItemSelectedListener listener;
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_assign_role, container, false);
        
        // Setup handles to view objects here
        
        // SPINNER
        Spinner sp = (Spinner) view.findViewById(R.id.spinner);
    	ArrayList<String> spinnerOptionsList = new ArrayList<String>();
    	spinnerOptionsList.add("Waiter");
    	spinnerOptionsList.add("Host");
    	spinnerOptionsList.add("Chef");
    	adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, spinnerOptionsList);
    	
    	// Specify the layout to use when the list of choices appears
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	
    	sp.setAdapter(adapter);
    	
    	//NAME FIELD
    	TextView t = (TextView) view.findViewById(R.id.name);
    	t.setText(this.getTag());
    	
        return view;
      }
 
    public void onAttach(Activity activity) {
        super.onAttach(activity);
          if (activity instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) activity;
          } else {
            throw new ClassCastException(activity.toString()
                + " must implement MyListFragment.OnItemSelectedListener");
          }
      }
}
