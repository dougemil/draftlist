package com.animasapps.android.draftlist;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.animasapps.android.draftlist.activities.SingleListActivity;
import com.animasapps.android.draftlist.model.Item;
import com.animasapps.android.draftlist.model.MyLists;
import com.animasapps.android.draftlist.model.SingleList;

public class ViewList extends ListFragment {

	private static final String TAG = "EditListFragment";
	public static final String EXTRA_LIST_ID = "com.animasapps.android.draftlist.singlelistfragment.list_id";
	private static final int ADD_LIST_ITEM = 1;
	
	private SingleList mList;
	private ArrayList<Item> mItems;
	private ArrayList<String> itemNames;
	private ArrayAdapter<String> adapter;
	
	private String tempItemName;
	private int tempPosition;
	private boolean mWillDelete = false;
	
	 
//	Alternative constructor maintains fragment encapsulation.  
	public static ViewList newInstance(UUID listId) {
		
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_LIST_ID, listId); 
		ViewList fragment = new ViewList();
		fragment.setArguments(args); // Stashes id in the Bundle
		
		return fragment;
	}
	
//	Gets intent extra from host activity and uses to generate a new SingleList
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		UUID listId = (UUID)getArguments().getSerializable(EXTRA_LIST_ID);
		mList = MyLists.get(getActivity()).getSingleList(listId);
		getActivity().setTitle(mList.getListTitle()); // displays list title in ActionBar
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.list_view_layout, container, false);
		 
		mItems = mList.getList(); // Create array of strings with list item names
		itemNames = new ArrayList<String>();
		for(Item li: mItems)
			itemNames.add(li.getListItem());
		
		adapter = new BasicArrayAdapter(itemNames);
		setListAdapter(adapter);
		return v;
	}
	
//	Wire click action post ActionBar option selected
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (mWillDelete){										// Delete list item
					
					tempItemName = itemNames.get(position);
					tempPosition = position;
					
					itemNames.remove(position);
					resetList(itemNames); 						// Updates mList
					adapter.notifyDataSetChanged();				// Update view
					getListView().setBackgroundColor(Color.parseColor("#ffffff"));
					mWillDelete = false;
				} 
	}
	
//	Inflate ActionBar options
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_viewlist_actions, menu);
	}
	
//	Wire ActionBar options
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case R.id.menu_item_new_list_item:
			Intent i = new Intent(getActivity(), SingleListActivity.class);
			i.putExtra(ViewList.EXTRA_LIST_ID, mList.getId());
			startActivityForResult(i, ADD_LIST_ITEM);
			adapter.notifyDataSetChanged();
			return true;
		case R.id.menu_item_delete_list_item:
			Log.i(TAG, "Delete Item selected");
			mWillDelete = true;
			getListView().setBackgroundColor(Color.parseColor("#ff9800"));
			return true;
		case R.id.menu_item_undo:
			itemNames.add(tempPosition, tempItemName);
			resetList(itemNames);
			adapter.notifyDataSetChanged();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

//	Simple implementation of custom ArrayAdapter. Dependencies: list_row_layout.xml, list_view_layout.xml
	public class BasicArrayAdapter extends ArrayAdapter<String>{
		
		public BasicArrayAdapter(ArrayList<String> items){
			super(getActivity(), R.layout.list_row_layout, items);			
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (convertView == null) {
				convertView = getActivity().
							  getLayoutInflater().
							  inflate(R.layout.list_row_layout, null);
			}
			
			String item = getItem(position);
			TextView itemView = (TextView)convertView.findViewById(R.id.list_item);
			itemView.setText(item);
			
			// Color conifguration
			if(mWillDelete){
				convertView.setBackgroundColor(Color.parseColor("#ff9800"));  // Orange
				itemView.setPadding(4, 20, 4, 20);
				getListView().setDividerHeight(20);
			}else{
				convertView.setBackgroundColor(Color.parseColor("#ffffff"));  // White
				itemView.setPadding(4, 4, 4, 4);
			}
			
			return convertView;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if ((requestCode == ADD_LIST_ITEM) && (resultCode == Activity.RESULT_OK)){
			//mList = (SingleList)data.getSerializableExtra(id for modified list);
			
		}else{
			Log.i(TAG, "Bad request code or result");
		}
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		// Move save function to immediately follow add button click?
		MyLists.get(getActivity()).saveLists();
	}
	
//	Utility method
	private void resetList(ArrayList<String> currentListStrings){
			
			ArrayList<Item> tempList = new ArrayList<Item>();
			for(String s: currentListStrings){
				Item i = new Item(s);
				tempList.add(i);
			}
			mList.setList(tempList);
		}
}
