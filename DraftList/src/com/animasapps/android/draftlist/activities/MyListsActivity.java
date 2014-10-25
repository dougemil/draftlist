package com.animasapps.android.draftlist.activities;

import com.animasapps.android.draftlist.MyListsFragment;

import android.app.Fragment;

/*
 * This class is a host activity.
 * It adds a fragment to a container View by implementing abstract SingleFragmentActivity
 *  
 * Patterns:
 * 		Container View
 * 		Abstract Class/Method
 * 		Fragment Transaction		
 */
public class MyListsActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		
		return new MyListsFragment();
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.list, menu);
//		return true;
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

}
