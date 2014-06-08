package com.teamdoge.inventory;

import com.teamdoge.restaurantapp.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link InvAddPageDropdownFrag.OnFragmentInteractionListener} interface to
 * handle interaction events. Use the {@link InvAddPageDropdownFrag#newInstance}
 * factory method to create an instance of this fragment.
 * 
 */
public class InvAddPageDropdownFrag extends DialogFragment {

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
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}

	private OnFragmentInteractionListener mListener;

	public static InvAddPageDropdownFrag newInstance(String param1,
			String param2) {
		InvAddPageDropdownFrag fragment = new InvAddPageDropdownFrag();
		Bundle args = new Bundle();

		fragment.setArguments(args);
		return fragment;
	}
	
	// *******************************************************************************************************************//
	// 													  View 															  //
	// *******************************************************************************************************************//
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		LayoutInflater inflater = getActivity().getLayoutInflater();		
		
		builder.setView(inflater.inflate(R.layout.fragment_inv_add_page_dropdown, null))
		    .setPositiveButton(R.string.Submit, new DialogInterface.OnClickListener() {
		    	@Override
		    	public void onClick(DialogInterface dialog, int id) {
		    		mListener.onDialogPositiveClick(InvAddPageDropdownFrag.this);
		    	}
		    })
		    .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mListener.onDialogNegativeClick(InvAddPageDropdownFrag.this);					
				}
			});
		return builder.create();
	}
	
	// *******************************************************************************************************************//
	//                                                  End View                                                          //
	// *******************************************************************************************************************//

	public InvAddPageDropdownFrag() {
		// Required empty public constructor
	}
	
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
}
