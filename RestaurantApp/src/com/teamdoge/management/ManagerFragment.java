package com.teamdoge.management;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.teamdoge.restaurantapp.R;
import com.teamdoge.schedules.DayShiftsManagementActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link ManagerFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link ManagerFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class ManagerFragment extends Fragment {


	@SuppressWarnings("unused")
	private OnFragmentInteractionListener mListener;
	private ArrayList<String> Days = new ArrayList<String>();
	private Button b;
	private String day;

	public static ManagerFragment newInstance() {
		ManagerFragment fragment = new ManagerFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public ManagerFragment() {
		// Required empty public constructor
	}

	// *******************************************************************************************************************//
	// 													Model 														      //
	// *******************************************************************************************************************//
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Days.add("Monday");
		Days.add("Tuesday");
		Days.add("Wednesday");
		Days.add("Thursday");
		Days.add("Friday");
		Days.add("Saturday");
		Days.add("Sunday");
	}
	
	// *******************************************************************************************************************//
	// 													End Model 														  //
	// *******************************************************************************************************************//

	// *******************************************************************************************************************//
	// 													  View 															  //
	// *******************************************************************************************************************//
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_manager, container, false);
		String weekDay;
		SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
		Calendar calendar = Calendar.getInstance();
		weekDay = dayFormat.format(calendar.getTime());
		int i = Days.indexOf(weekDay);
		for (int j = 0; j < Days.size(); j++) {
		  if (i == Days.size())
		    i = 0;
		  switch (j) {
		  case 0:
			  b = (Button) v.findViewById(R.id.monday);
			  b.setText(Days.get(i));
			  i++;
			  break;
		  case 1:
			  b = (Button) v.findViewById(R.id.tuesday);
			  b.setText(Days.get(i));
			  i++;
			  break;
		  case 2:
			  b = (Button) v.findViewById(R.id.wednesday);
			  b.setText(Days.get(i));
			  i++;
			  break;
		  case 3:
			  b = (Button) v.findViewById(R.id.thursday);
			  b.setText(Days.get(i));
			  i++;
			  break;
		  case 4:
			  b = (Button) v.findViewById(R.id.friday);
			  b.setText(Days.get(i));
			  i++;
			  break;
		  case 5:
			  b = (Button) v.findViewById(R.id.saturday);
			  b.setText(Days.get(i));
			  i++;
			  break;
		  case 6:
			  b = (Button) v.findViewById(R.id.sunday);
			  b.setText(Days.get(i));
			  i++;
			  break;
		}
		v.findViewById(R.id.monday).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						b = (Button) view;
						day = b.getText().toString();
						Intent intent = new Intent(getActivity(), DayShiftsManagementActivity.class);
						intent.putExtra("Day", day);
						Toast.makeText(getActivity().getApplicationContext(),day, Toast.LENGTH_SHORT).show();
						startActivity(intent);
					}
				});
		v.findViewById(R.id.tuesday).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						b = (Button) view;
						day = b.getText().toString();
						Intent intent = new Intent(getActivity(), DayShiftsManagementActivity.class);
						intent.putExtra("Day", day);
						Toast.makeText(getActivity().getApplicationContext(),day, Toast.LENGTH_SHORT).show();
						startActivity(intent);
					}
				});
		v.findViewById(R.id.wednesday).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						b = (Button) view;
						day = b.getText().toString();
						Intent intent = new Intent(getActivity(), DayShiftsManagementActivity.class);
						intent.putExtra("Day", day);
						Toast.makeText(getActivity().getApplicationContext(),day, Toast.LENGTH_SHORT).show();
						startActivity(intent);
					}
				});
		v.findViewById(R.id.thursday).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						b = (Button) view;
						day = b.getText().toString();
						Intent intent = new Intent(getActivity(), DayShiftsManagementActivity.class);
						intent.putExtra("Day", day);
						Toast.makeText(getActivity().getApplicationContext(),day, Toast.LENGTH_SHORT).show();
						startActivity(intent);
					}
				});
		v.findViewById(R.id.friday).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						b = (Button) view;
						day = b.getText().toString();
						Intent intent = new Intent(getActivity(), DayShiftsManagementActivity.class);
						intent.putExtra("Day", day);
						Toast.makeText(getActivity().getApplicationContext(),day, Toast.LENGTH_SHORT).show();
						startActivity(intent);
					}
				});
		v.findViewById(R.id.saturday).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						b = (Button) view;
						day = b.getText().toString();
						Intent intent = new Intent(getActivity(), DayShiftsManagementActivity.class);
						intent.putExtra("Day", day);
						Toast.makeText(getActivity().getApplicationContext(),day, Toast.LENGTH_SHORT).show();
						startActivity(intent);
					}
				});
		v.findViewById(R.id.sunday).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						b = (Button) view;
						day = b.getText().toString();
						Intent intent = new Intent(getActivity(), DayShiftsManagementActivity.class);
						intent.putExtra("Day", day);
						Toast.makeText(getActivity().getApplicationContext(),day, Toast.LENGTH_SHORT).show();
						startActivity(intent);
					}
				});
		// Inflate the layout for this fragment
		}
		return v;
	}
	
	// *******************************************************************************************************************//
	//                                                  End View                                                          //
	// *******************************************************************************************************************//

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		public void onFragmentInteraction();
	}
	
}

