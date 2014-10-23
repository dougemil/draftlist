package com.animasapps.android.draftlist;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Log;

import com.animasapps.android.draftlist.model.SingleList;

/*
 * Manages JSON serialization for MyListsFragment
 * JSONArray, Writer, OutputStream, OutputStreamWriter
 *  
 */
public class DraftListJSONSerializer {

	private static final String TAG = "DraftListJSONSerializer";
	private Context mContext; // Enables file access
	private String mFilename;
	
	public DraftListJSONSerializer(Context c, String f){
		mContext = c;
		mFilename = f;
	}
	
	// accepts a group of lists > writes to disk
	public void saveLists(ArrayList<SingleList> lists) throws JSONException, IOException{ 
		
		JSONArray array = new JSONArray();
		// Load a group of lists into a JSONArray
		for (SingleList list : lists){
			array.put(list.toJSON());
		}
		// Write file to disk
		Writer writer = null;
		try {
			OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(array.toString());
		}finally {
			if (writer != null)
				writer.close();
		}
	}
	
	public ArrayList<SingleList> loadLists() throws IOException, JSONException {
		ArrayList<SingleList> lists = new ArrayList<SingleList>();
		BufferedReader reader = null;
		try{
			// Open and read file into a StringBuilder
			Log.i(TAG, "Opening/reading file: " + mFilename);
			InputStream in = mContext.openFileInput(mFilename);
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null){
				// line breaks omitted and irrelevant
				jsonString.append(line);
			}
			// Parse the string with JSONTokener
			Log.i(TAG, "Parsing JSON String");
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
			// Build an array of lists from the  array of JSONObjects
			for (int i=0; i<array.length(); i++){
				lists.add(new SingleList(array.getJSONObject(i)));
			}
		}catch(FileNotFoundException e){
			// Ignore
			Log.i(TAG, "File not found: " + mFilename);
		}finally{
			if (reader != null)
				reader.close();
		}
		return lists;
		
	}
}
