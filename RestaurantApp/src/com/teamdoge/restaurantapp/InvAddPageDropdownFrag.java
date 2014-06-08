package com.teamdoge.restaurantapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
	
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment InvAddPageDropdownFrag.
	 */
	// TODO: Rename and change types and number of parameters
	public static InvAddPageDropdownFrag newInstance(String param1,
			String param2) {
		InvAddPageDropdownFrag fragment = new InvAddPageDropdownFrag();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}
	
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

//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		if (getArguments() != null) {
//			mParam1 = getArguments().getString(ARG_PARAM1);
//			mParam2 = getArguments().getString(ARG_PARAM2);
//		}
//		
//		//newItemBox = (EditText) getView().findViewById(R.id.new_item_box);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		// Inflate the layout for this fragment
//		return inflater.inflate(R.layout.fragment_inv_add_page_dropdown,
//				container, false);
//	}
//
//	// TODO: Rename method, update argument and hook method into UI event
//	public void onButtonPressed(Uri uri) {
//		if (mListener != null) {
//			mListener.onFragmentInteraction(uri);
//		}
//	}
//

//
//	
//	public void launchDialog(adding_inventory_item_page item){
//		final Dialog dialog = new Dialog(item);
//		
//		dialog.setContentView(R.layout.fragment_inv_add_page_dropdown);
//		dialog.setTitle("Title...");
//		
//		EditText text = (EditText) dialog.findViewById(R.id.new_item_box);
//		text.setText("Android custom dialog example!");
//
//		Button submitButton = (Button) dialog.findViewById(R.id.fragSubmit);
//		Button cancelButton = (Button) dialog.findViewById(R.id.fragCancel);
//		// if button is clicked, close the custom dialog
//		submitButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.cancel();
//			}
//		});
//		
//		cancelButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.cancel();
//			}
//		});
//
//		dialog.show();
//	}

	
//	@Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        // Use the Builder class for convenient dialog construction
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        
//        builder.setView(inflater.inflate(R.layout.fragment_inv_add_page_dropdown, null))
//        	.setPositiveButton(R.string.button_submit, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    // FIRE ZE MISSILES!
//                }
//            })
//            .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    // User cancelled the dialog
//                }
//            });
//        
////        builder.setMessage("Fire the Missiles")
////        .setPositiveButton(R.string.button_submit, new DialogInterface.OnClickListener() {
////            public void onClick(DialogInterface dialog, int id) {
////                // FIRE ZE MISSILES!
////            }
////        })
////        .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
////            public void onClick(DialogInterface dialog, int id) {
////                // User cancelled the dialog
////            }
////        });
//        
//        return builder.create();
//	}
	
//	public void onDismiss(){
//		//what we do when we dismiss the dialog (add new item to the dropdown box)
//	}
//	
//	public void launchFrag(){
//		DialogFragment newFragment = new InvAddPageDropdownFrag();
//        newFragment.show(getFragmentManager(), "Hi BRANDON");
//	}

}
