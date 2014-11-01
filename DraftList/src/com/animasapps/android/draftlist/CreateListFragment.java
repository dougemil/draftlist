package com.animasapps.android.draftlist;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.animasapps.android.draftlist.model.SingleList;

/*  Git Test ********  one two *****
 * 
 * This class is instantiated by MyListsFragment ActionBar to create a new list
 * New list is loaded in an extra and returned
 * 
 * Modify to start Add List Item use case?
 * 
 * Patterns:
 * 		Return from startActivityForResult(.)
 * 		TextWatcher
 * 		OnClickListener
 * 
 */
public class CreateListFragment extends Fragment {

	private static final String TAG = "CreateListFragment";
	public static final String NEW_LIST = "com.animasapps.android.newList";
	
	private SingleList mList;
	private EditText mTitle;
	private ImageButton mSaveButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mList = new SingleList();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_createlist, container, false);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Manifest activity element updated to display keyboard when activity starts
		mTitle = (EditText)v.findViewById(R.id.list_title_field);
		mTitle.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable title) {
				
				mList.setListTitle(title.toString());
				Log.i(TAG, "Title updated: " + mList.getListTitle());
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}		
		});
		
		mSaveButton = (ImageButton)v.findViewById(R.id.createListFragment_saveButton);
		mSaveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				// Sets result intent /w new list for MyListsFragment
				Intent i = new Intent();
				i.putExtra(NEW_LIST, mList);
				getActivity().setResult(Activity.RESULT_OK, i);
				Log.i(TAG, "Leaving CreateListFragment");
				getActivity().finish();
				
			}
		});
		return v;
	}
	
	

}
