package com.animasapps.android.draftlist.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateFormat;
import android.util.Log;

/*
 * Model Object
 * JSON compatible
 * 
 */
public class SingleList implements Serializable {

	private static final String TAG = "Item";
	
	private UUID mId;
	private String mListTitle;
	private Date mDate;
	private ArrayList<Item> mList;
	
	private static final String JSON_LIST_ID = "listId";
	private static final String JSON_LIST_TITLE = "listTitle";
	private static final String JSON_DATE = "date";
	//private static final String JSON_LIST = "list";
	private static final String JSON_ITEMS_ARRAY = "itemsArray";
	
	
	public SingleList() {
		mId = UUID.randomUUID();
		mList = new ArrayList<Item>();
		mDate = new Date();
	}

	public SingleList(JSONObject json) throws JSONException{
		mId = UUID.fromString(json.getString(JSON_LIST_ID));
		mListTitle = new String(json.getString(JSON_LIST_TITLE));
		
		// ***parse error
		mDate = new Date(json.getLong(JSON_DATE));
		
		mList = new ArrayList<Item>();
		JSONArray array = json.getJSONArray(JSON_ITEMS_ARRAY);
		for (int i = 0; i < array.length(); i++){
			// get JSONObject at index(i), instantiate as Item, add to mList
			mList.add(new Item(array.getJSONObject(i)));
		}	
	}
	
	public ArrayList<Item> getList() {
		return mList;
	}

	public void setList(ArrayList<Item> list){
		mList = list;
	}
	public Item getListItem(UUID id){
		for (Item item: mList){
			if (item.getId().equals(id))
				return item;	
		}
		Log.e(TAG, "Item not found in list");
		return null;
	}
	
	public Item getListItem(String itemName){
		for (Item i: mList){
			if (i.getListItem().equals(itemName))
				return i;	
		}
		Log.e(TAG, "Item not found in list");
		return null;
	}
	
	public void addListItem(Item item) {
		mList.add(item);
	}
	
	public void removeListItem(UUID id){	
		for (Item item: mList){
			if (item.getId().equals(id))
				mList.remove(item);
		}
	}

	public String getListTitle() {
		return mListTitle;
	}

	public void setListTitle(String listTitle) {
		mListTitle = listTitle;
	}
	
	public UUID getId() {
		return mId;
	}

	@Override
	public String toString() {
		return mListTitle;
	}

	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public String getFormattedDate(){
		
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE', 'MMM dd', 'hh':'mm' 'aa");
		String formattedDate = formatter.format(mDate);
		return formattedDate;
		
	}

	public JSONObject toJSON() throws JSONException{
		
		JSONArray jsonArray = new JSONArray(); // array for items from mList
		for(Item i : mList){			  	   //  populate array from mList using Item.toJSON()
			jsonArray.put(i.toJSON());
		}
		
		JSONObject json = new JSONObject(); 		// object for a SingleList
		json.put(JSON_LIST_ID, mId.toString());		// populate
		json.put(JSON_LIST_TITLE, mListTitle);
		json.put(JSON_DATE, mDate.getTime());       // saves Date as millisecond value
		json.put(JSON_ITEMS_ARRAY, jsonArray);		// contents of mList
		return json;
	}

}
