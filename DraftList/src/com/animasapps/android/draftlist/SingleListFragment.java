package com.animasapps.android.draftlist;

import java.util.ArrayList;
import java.util.UUID;

import android.app.ListFragment;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.animasapps.android.draftlist.model.Item;
import com.animasapps.android.draftlist.model.MyLists;
import com.animasapps.android.draftlist.model.SingleList;

/*
 * Simple ArrayAdapter pattern
 * newInstance() pattern for adding args to a frag bundle
 * 
 * 
 */
public class SingleListFragment extends ListFragment {

	public static final String EXTRA_LIST_ID = "com.animasapps.android.draftlist.singlelistfragment.list_id";
	private static final String TAG = "SingleListFragment";
	
	private SingleList mList;
	private ArrayList<Item> mItems;
	private String tempItemName;
	private int tempPosition;
	
	private EditText listTitleField;
	private TextView listDateField;
	private EditText listItemField;
	
	private ArrayList<String> justList;
	private ArrayAdapter<String> adapter;
//	private ListItemAdapter adapter;
	private boolean mWillDelete = false;
	private boolean mEdit = false;
	
	/*
	 * Alternative to constructor
	 * Creates new instance with an ID stashed in the Bundle
	 * Maintains Fragment encapsulation
	 * 
	 */
	public static SingleListFragment newInstance(UUID listId) {
		
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_LIST_ID, listId);	
		SingleListFragment fragment = new SingleListFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	/*
	 * Gets intent extra from host activity and uses to generate a new SingleList
	 */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		UUID listId = (UUID)getArguments().getSerializable(EXTRA_LIST_ID);
		mList = MyLists.get(getActivity()).getSingleList(listId);
		getActivity().setTitle(mList.getListTitle()); // displays list title in ActionBar
		
		// **** Adapter implementation for ListView
//		StringListAdapter adapter = new StringListAdapter(mList.getList());
		
	}
	/*
	 * Generic ListView/EditText Implementation
	 * Needs refactoring, add list items to top of list
	 * save list to MyLists: Return result to MyLists
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.view_list, container, false);
		 
		mItems = mList.getList();
		
		justList = new ArrayList<String>();
		for(Item li: mItems){
			justList.add(li.getListItem());
		}
		
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, justList);
//		adapter = new ListItemAdapter(mItems);
		setListAdapter(adapter);
		
		Button btn = (Button)v.findViewById(R.id.btnAdd);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				EditText edit = (EditText)getActivity().findViewById(R.id.txtItem);
                Log.i(TAG, "Text: " + edit.getText());
                justList.add(edit.getText().toString());	  // Add string to local (visible) list
                Item i = new Item(edit.getText().toString()); // Create new Item from String
                mList.addListItem(i);						  // Add Item to SingleList
                edit.setText("");
               
                adapter.notifyDataSetChanged();	
			}
		});
		
		
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_singlelist_actions, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case R.id.menu_item_new_list:
//			TODO
			return true;
		case R.id.menu_item_delete_list_item:
			Log.i(TAG, "Delete Item selected");
			mWillDelete = true;
			getListView().setBackgroundColor(Color.parseColor("#ff9800"));
			return true;
		case R.id.menu_item_edit:
			Log.i(TAG, "Edit Item selected");
			mEdit = true;
			getListView().setBackgroundColor(Color.parseColor("#ffeb3b"));
			Log.i(TAG, "Delete Item selected. Bground color set.");
			return true;
		case R.id.menu_item_undo:
			justList.add(tempPosition, tempItemName);
			resetList(justList);
			adapter.notifyDataSetChanged();
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (mWillDelete){								// Delete list
			
			tempItemName = justList.get(position);
			tempPosition = position;
			
			justList.remove(position);
			resetList(justList); 						// Updates mList
			adapter.notifyDataSetChanged();				// Update view
			getListView().setBackgroundColor(Color.parseColor("#ffffff"));
			mWillDelete = false;
		} else if (mEdit){
			
			TextView listItem = (TextView)l.getChildAt(position);
			listItem.setVisibility(View.INVISIBLE);
			adapter.notifyDataSetChanged();
			
//			EditText editListItem = new EditText(getActivity());
//			editListItem.setText(listItem.getText());
//			l.addView(editListItem);
			
//			l.addView(editListItem, position);
//			**** Throwing Exception: AdapterView does not support operation
		}
	}

	private void resetList(ArrayList<String> currentListStrings){
		
		ArrayList<Item> tempList = new ArrayList<Item>();
		for(String s: currentListStrings){
			Item i = new Item(s);
			tempList.add(i);
		}
		mList.setList(tempList);
	}
	
	@Override
	// Move save function to immediately follow add button click?
	public void onPause() {
		super.onPause();
		MyLists.get(getActivity()).saveLists();
	}
	
	
	
	
//	public class ListItemAdapter extends ArrayAdapter<Item>{
//		
//		public ListItemAdapter(ArrayList<Item> singleList){
//			super(getActivity(), 0, singleList);
//		}
//		
//		public View getView(int position, View convertView, ViewGroup parent){
//			
//			if(convertView==null){
//				convertView = getActivity().getLayoutInflater()
//										   .inflate(R.layout.view_list, null);
//			}
//			
//			Item li = getItem(position);
//			
//			TextView itemView = (TextView)convertView.findViewById(R.id.singlelist_itemView);
//			Log.i(TAG, "Item name: " + li.getListItem());
//			itemView.setText(li.getListItem()); // **ListItemAdapter.getView()
//			
//			return convertView;
//		}
//	}
//	private class ListItemsAdapter extends ArrayAdapter<Item>{
//		
//	}

//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View v = inflater.inflate(R.layout.fragment_singlelist, container, false);
//		
//		listTitleField = (EditText)v.findViewById(R.id.list_title_field);
//		listTitleField.setText(mList.getListTitle());
//		
//		listTitleField.addTextChangedListener(new TextWatcher(){
//			public void onTextChanged(CharSequence c, int start, int before, int count){
//				mList.setListTitle(c.toString());
//			}
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// Required for TextWatcher			
//			}
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//				// Required for TextWatcher
//				
//			}
//		});
//		
//		// Extract the items from a list
//		listItemField = (EditText)v.findViewById(R.id.list_item_field);
//		ArrayList<Item> items = mList.getList();
//		Item item = items.get(0);
//
//		listItemField.setText(item.getListItem());
////		listItemField.addTextChangedListener(new TextWatcher(){
////			public void onTextChanged(CharSequence c, int start, int before, int count){
////				mList.setListItems(c.toString());
////			}
////			@Override
////			public void afterTextChanged(Editable arg0) {
////				// Required for TextWatcher			
////			}
////			@Override
////			public void beforeTextChanged(CharSequence arg0, int arg1,
////					int arg2, int arg3) {
////				// Required for TextWatcher
////				
////			}
////		});
//		
//		listDateField = (TextView)v.findViewById(R.id.list_date_field);
//		listDateField.setText(mList.getFormattedDate());
//		
//		return v;
//		
//		
//	}

	
}
 