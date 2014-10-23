package com.animasapps.android.draftlist.model;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

import com.animasapps.android.draftlist.DraftListJSONSerializer;

/*
 * This class encapsulates a List of Lists
 * 
 * Patterns:
 * 		Singleton
 * 		Context Member Variable
 * 		Persistence Method
 * 
 */
public class MyLists {
	
	private static final String TAG = "MyLists";
	private static final String FILENAME = "basicListGroup.json";
	
	private ArrayList<SingleList> mListGroup;
	private DraftListJSONSerializer mSerializer;
	
	private static MyLists sMyLists;
	private Context mAppContext; // Context parameter allows object to start activities, access resource, etc.
	
	// Singleton private contructor/public getter replaces public constructor
	private MyLists(Context appContext){
		mAppContext = appContext;
		mSerializer = new DraftListJSONSerializer(mAppContext, FILENAME);
		// mListGroup = new ArrayList<SingleList>();
		try{
			mListGroup = mSerializer.loadLists();
			Log.i(TAG, "Loading saved list");
		}catch (Exception e){
			// If no lists exist?
			mListGroup = new ArrayList<SingleList>();
			Log.e(TAG, "Error loading lists: ", e);
		}
		
		// Populate array for test
//		for (int i=0; i < 5; i++){
//			SingleList sl = new SingleList();
//			sl.setListTitle("List #" + i);
//
//			Item item = new Item();
//			item.setListItem("cheese and crackers");
//			sl.addListItem(item);
//			
//			Item item2 = new Item();
//			item2.setListItem("milk and coffee");
//			sl.addListItem(item2);
//			
//			Item item3 = new Item();
//			item3.setListItem("nuts and olives");
//			sl.addListItem(item3);
//			
//			mListGroup.add(sl);
//		}
	}
	
	// Singleton private contructor/public getter replaces public constructor
	public static MyLists get(Context c){ 
		if (sMyLists == null) {
			sMyLists = new MyLists(c.getApplicationContext()); // Context param may not be the application context
		}
		return sMyLists;
	}

	public ArrayList<SingleList> getLists(){
		return mListGroup;
	}
	
	// Search Lists for a specific SingleList
	public SingleList getSingleList(UUID id){
		for (SingleList sl : mListGroup) {
			if (sl.getId().equals(id))
				return sl;
		}
		return null;
	}
	
	public void addList(SingleList list){
		mListGroup.add(list);
	}

	public boolean saveLists(){
		mSerializer = new DraftListJSONSerializer(mAppContext, FILENAME);
		try{
			mSerializer.saveLists(mListGroup);
			Log.d(TAG, "List saved to file " + FILENAME);
			return true;
		}catch(Exception e){
			Log.e(TAG, "Error saving lists to: " + FILENAME, e);
			return false;
		}
	}

}
