package com.animasapps.android.draftlist.model;

import java.io.Serializable;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * Ready for JSON serialization
 * 
 */
public class Item implements Serializable{

	private UUID mId;
	private String mListItem;
	
	private static final String JSON_ITEM_ID= "itemId";
	private static final String JSON_LIST_ITEM = "listItem";
	
	public Item(){
		mId = UUID.randomUUID();
	}
	
	public Item(String s){
		mId = UUID.randomUUID();
		mListItem = s;
	}
	
	public Item(JSONObject json) throws JSONException{
		mId = UUID.fromString(json.getString(JSON_ITEM_ID));
		mListItem = json.getString(JSON_LIST_ITEM);
	}
	
	public UUID getId() {
		return mId;
	}

	public void setUUID(){
		mId = UUID.randomUUID();
	}
	public String getListItem() {
		return mListItem;
	}
	public void setListItem(String itemName) {
		mListItem = itemName;
	}
	
	public JSONObject toJSON() throws JSONException{
		JSONObject json = new JSONObject();
		json.put(JSON_ITEM_ID, mId.toString());
		json.put(JSON_LIST_ITEM, mListItem);
		return json;
	}

}
