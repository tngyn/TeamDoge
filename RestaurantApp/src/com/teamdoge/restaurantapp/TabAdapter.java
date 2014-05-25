package com.teamdoge.restaurantapp;

import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;





public class TabAdapter extends FragmentStatePagerAdapter{
	
	public TabAdapter(FragmentManager fm) {
		super(fm);

}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new FragTab();
		Bundle args = new Bundle();
		args.putInt(FragTab.ARG_OBJECT, i+1);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return 3;
	}
	public CharSequence getPageTitle(int position) {
		return "OBJECT " + (position + 1);
	}
	
	public static class FragTab extends Fragment {
		public static final String ARG_OBJECT = "object";
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			final View view = inflater.inflate(R.layout.tab, container, false);
			ParseUser.logInInBackground("my name", "my pass", new LogInCallback() {
				  public void done(ParseUser user, ParseException e) {
				    if (user != null) {
				    } else {
				    	
				    }
				  }
				});
		/**	ParseUser user = ParseUser.getCurrentUser();
			ParseObject o = new ParseObject("Schedule");
			o.addAllUnique("AvailableTimes", Arrays.asList("4-5","5-6","6-7"));
			user.put("Work", o);
			user.saveInBackground();*/
			getSpinner1(view);
		    getSpinner2(view);
			Button b = (Button) view.findViewById(R.id.trade);
		    b.setOnClickListener(new OnClickListener() {
		    	public void onClick(View v) {
		    		Spinner s1 = (Spinner) view.findViewById(R.id.spinner1);
					Spinner s2 = (Spinner) view.findViewById(R.id.spinner2);
					final String time_1 = (String) s1.getSelectedItem();
					final String time_2 = (String) s2.getSelectedItem();
					ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
					  query.findInBackground(new FindCallback<ParseObject>() {
					    public void done(List<ParseObject> object, ParseException e) {
					    		if (e == null) {
					    			ParseObject o = object.get(0);
					    			o.add("AvailableTimes", time_1);
					    			o.saveInBackground();
					    			o.removeAll("AvailableTimes", Arrays.asList(time_2));
					    			o.saveInBackground();
					    			getSpinner2(view);
					    		}
					    	}
					    });
					  ParseUser user = ParseUser.getCurrentUser();
					  user.add("StartTime", time_2);
					  user.saveInBackground();
					  user.removeAll("StartTime", Arrays.asList(time_1));
					  user.saveInBackground();
					  getSpinner1(view);
		    	}
		    });
			/**String spinner1[];
			spinner1 = new String[3];
		    spinner1[0] = "1:00-2:00";
		    spinner1[1] = "2:00-3:00";
		    spinner1[2] = "3:00-4:00";
		    Spinner s1 = (Spinner) view.findViewById(R.id.spinner1);
		    ArrayAdapter<String> adapt1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinner1);
		    s1.setAdapter(adapt1);
		    String spinner2[];
			spinner2 = new String[4];
		    spinner2[0] = "5:00-6:00";
		    spinner2[1] = "6:00-7:00";
		    spinner2[2] = "7:00-8:00";
		    spinner2[3] = "";
		    Spinner s2 = (Spinner) view.findViewById(R.id.spinner2);
		    ArrayAdapter<String> adapt2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinner2);
		    s2.setAdapter(adapt2);*/
		    return view;
		} 
		
		public void getSpinner1(View view) {
			ParseUser user = ParseUser.getCurrentUser();
			List<String> time = (List<String>) user.get("StartTime");
			if(time == null) {
				return;
			}
		    Spinner spin = (Spinner) view.findViewById(R.id.spinner1);
		    ArrayAdapter<String> adapt1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, time);
		    spin.setAdapter(adapt1);
		    spin.setOnItemSelectedListener(new OnItemSelectedListener() {
		    	@Override
		    	public void onItemSelected(AdapterView<?> adapt, View view,
		    		int i, long l) {
		    		String t = adapt.getItemAtPosition(i).toString();
		    	}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
		    });
		}
	/**	public void getSpinner2(final View view) {
		    ParseQuery<ParseUser> query = ParseQuery.getQuery("Schedule");
		    query.findInBackground(new FindCallback<ParseObject>() {
		    	public void done(List<ParseObject> object, ParseException e) {
		    		if (e == null) {
		    			ParseObject o = object.get(object.size()-1);
		    			List<String> time = (List<String>) o.get("AvailableTimes");
		    		    Spinner spin = (Spinner) view.findViewById(R.id.spinner2);
		    		    ArrayAdapter<String> adapt2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, time);
		    		    spin.setAdapter(adapt2);
		    		    spin.setOnItemSelectedListener(new OnItemSelectedListener() {
		    		    	@Override
		    		    	public void onItemSelected(AdapterView<?> adapt, View view,
		    		    		int i, long l) {
		    		    		String t = adapt.getItemAtPosition(i).toString();
		    		    	}

		    				@Override
		    				public void onNothingSelected(AdapterView<?> arg0) {
		    					// TODO Auto-generated method stub
		    					
		    				}
		    		    });
		    		}
		    	}
		    });
		}*/
		public void getSpinner2(final View view) {
			ParseUser user = ParseUser.getCurrentUser();
			ParseObject obj = user.getParseObject("Work");
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Schedule");
			query.getInBackground(obj.getObjectId(), new GetCallback<ParseObject>() {
				public void done(ParseObject o, ParseException e) {
					if (e == null) {
		    			List<String> time = (List<String>) o.get("AvailableTimes");
		    		    Spinner spin = (Spinner) view.findViewById(R.id.spinner2);
		    		    ArrayAdapter<String> adapt2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, time);
		    		    spin.setAdapter(adapt2);
		    		    spin.setOnItemSelectedListener(new OnItemSelectedListener() {
		    		    	@Override
		    		    	public void onItemSelected(AdapterView<?> adapt, View view,
		    		    		int i, long l) {
		    		    		String t = adapt.getItemAtPosition(i).toString();
		    		    	}

		    				@Override
		    				public void onNothingSelected(AdapterView<?> arg0) {
		    					// TODO Auto-generated method stub
		    					
		    				}
		    		    });
					}
				}
			});
		}
		
	}
	/**public void tradeShift(View view) {
		FragTab.tradeShift(view);	
	}*/

}
		
		

	