package com.animasapps.android.draftlist.activities;

import com.animasapps.android.draftlist.R;
import com.animasapps.android.draftlist.R.id;
import com.animasapps.android.draftlist.R.layout;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
/*
 * Abstract class to elimate duplicate code when creating activities that host a single fragment
 * All activities in this app will be subclasses of this activity to define a common container view
 * Subclasses must override createFragment()
 * 
 * Patterns:
 * 		Container View, Fragment Manager
 * 
 * Classes:
 * 		FragmentManager, Fragment
 * 
 * Methods:
 *  	onCreate(..)
 *  		setContentView(..)
 * 			getSupportFragmentManager()
 * 			findFragmentById(..)
 * 		abstract createFragment()
 * 		getLayoutResourceId()
 * 	 
 * Views:
 * 		FrameLayout	
 */



/*
 * This class encapsulates a Container View pattern in an Abstract Class
 * Subclass implements createFragment() to return a Frag for the container
 * 
 */
public abstract class SingleFragmentActivity extends ActionBarActivity {
	
	
	// Abstract method implemented by subclass
	protected abstract Fragment createFragment();
		
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_generic_container_view_layout);
		FragmentManager fm = getFragmentManager();
		// Check for existing frag
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer); 
		
		if (fragment == null){
			// Create new frag
			fragment = createFragment(); 
			fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
		}
	}
}
