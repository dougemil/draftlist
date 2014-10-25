package com.animasapps.android.draftlist;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.animasapps.android.draftlist.activities.CreateListActivity;
import com.animasapps.android.draftlist.activities.SingleListActivity;
import com.animasapps.android.draftlist.model.MyLists;
import com.animasapps.android.draftlist.model.SingleList;
/*
 * This class holds a group of lists
 * 
 * Patterns:
 * 		Singleton
 * 		ListFragment, Interface Adapter
 * 		List Listener
 * 		Custom ListAdapter Subclass, Inner Class
 * 		Date Formatting
 * 		Intent Extras
 * 
 */
public class MyListsFragment extends ListFragment {
// Default implementation of ListFragment inflates a full-screen ListView layout
	
	private static final String TAG = "MyListsFragment";
	private static final int REQ_NEW_LIST = 1;
	
	private ArrayList<SingleList> mLists;
	private SingleList tempList;
	private int tempListPosition;
	private boolean mWillDelete = false;
	private boolean mEdit = false;
	
	/*
	 * Tells frag mgr to create options menu
	 * Sets title
	 * Instantiates list and adpater. Puts adapter on list.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
		getActivity().setTitle(R.string.my_lists_label);
		
		mLists = MyLists.get(getActivity()).getLists(); 			// Instantiates MyLists instance w/ singleton pattern
		SingleListAdapter adapter = new SingleListAdapter(mLists);  // Instantiates custom adapter
		setListAdapter(adapter);
		
		if (mLists.isEmpty()){
			Log.i(TAG, "Empty ListGroup > Starting CreateList");
			Intent i = new Intent(getActivity(), CreateListActivity.class);
			startActivityForResult(i, REQ_NEW_LIST);
		}
	}

	/*
	 * Creates a SingleList object corresponding to the list item clicked 
	 */
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		
		if (mWillDelete){				
			tempList = mLists.get(position);  // Save to tmep location for undo
			tempListPosition = position;
			
		mLists.remove(position);			 // Delete from current view
			mWillDelete = false;
			((SingleListAdapter)getListAdapter()).notifyDataSetChanged();
			
		}else if (mEdit){
			
//			TODO
//			TextView list = (TextView)lv.getChildAt(position);
//			list.setVisibility(View.INVISIBLE);
			
			
			((SingleListAdapter)getListAdapter()).notifyDataSetChanged();
		}else{  						// Open list	
			SingleList sl = ((SingleListAdapter)getListAdapter()).getItem(position); // Use adapter to get a list		
			Intent i = new Intent(getActivity(), SingleListActivity.class);
			i.putExtra(SingleListFragment.EXTRA_LIST_ID, sl.getId());
			startActivity(i);
			Log.d(TAG, sl.getListTitle() + " was clicked");
		}
	}

	// Callback for Action Bar Options Menu
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_mylists_actions, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case R.id.menu_item_new_list:
			Intent i = new Intent(getActivity(), CreateListActivity.class);
			startActivityForResult(i, REQ_NEW_LIST);
			// notify data set changed
			((SingleListAdapter)getListAdapter()).notifyDataSetChanged();
			return true;
		case R.id.menu_item_delete_list:
			mWillDelete = true;
			((SingleListAdapter)getListAdapter()).notifyDataSetChanged();
			return true;
		case R.id.menu_item_edit:
			mEdit = true;
			((SingleListAdapter)getListAdapter()).notifyDataSetChanged();
		case R.id.menu_item_undo:
			if(tempList != null){
				mLists.add(tempListPosition, tempList);
				((SingleListAdapter)getListAdapter()).notifyDataSetChanged();
			}
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	
	// *** Custom Adapter subclass that knows how to work with SingleList ***
	private class SingleListAdapter extends ArrayAdapter<SingleList>{
		
		// Requires explicit constructor
		public SingleListAdapter(ArrayList<SingleList> myLists) {
			super(getActivity(), 0, myLists);
		}
		
		// Create and return a custom list item
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			// Inflate a View if one was not received
			if (convertView == null) {
				convertView = getActivity().
							  getLayoutInflater().
							  inflate(R.layout.list_item_mylists, null);		
			}
			
			// Configure view for a list item
			SingleList sl = getItem(position);
			
			TextView titleTextView = (TextView)convertView.findViewById(R.id.mylists_item_titleTextView);
			titleTextView.setText(sl.getListTitle());
			TextView dateTextView = (TextView)convertView.findViewById(R.id.mylists_item_dateTextView);
			dateTextView.setText(sl.getFormattedDate());
			
			// Color conifguration
			if(mWillDelete){
				convertView.setBackgroundColor(Color.parseColor("#ff9800"));  // Orange
			}else if(mEdit){
				convertView.setBackgroundColor(Color.parseColor("#ffeb3b"));  // Yellow
			}else{
				convertView.setBackgroundColor(Color.parseColor("#ffffff"));  // White
			}
			return convertView;
		}
		
	} //**** End Adapter Subclass ****


	
	@Override
	public void onResume() {
		super.onResume();
		// Update MyLists instance?
		((SingleListAdapter)getListAdapter()).notifyDataSetChanged();
	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Sucessfully retrieves new list from CreateListFragment,
		// following  > startActivityForResult(.)
		// Does not error check
		if ((requestCode == REQ_NEW_LIST) && (resultCode == Activity.RESULT_OK)){
			SingleList list = (SingleList)data.getSerializableExtra(CreateListFragment.NEW_LIST);
			mLists.add(list);	
			Log.i(TAG, "Added list: " + list.getListTitle());
			
			Intent i = new Intent(getActivity(), SingleListActivity.class);
			i.putExtra(SingleListFragment.EXTRA_LIST_ID, list.getId());
			startActivity(i);
			
		}else{
			Log.i(TAG, "Bad request code or result");
		}
		
	}

	
	@Override
	public void onPause() {
		super.onPause();
		MyLists.get(getActivity()).saveLists();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	
	

	
	
}
