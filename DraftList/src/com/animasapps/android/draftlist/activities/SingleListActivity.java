package com.animasapps.android.draftlist.activities;

import java.util.UUID;

import android.app.Fragment;

import com.animasapps.android.draftlist.EditListFragment;

public class SingleListActivity extends SingleFragmentActivity {

	/*
	 * Intent Extra generates this Activity.
	 * createFragment():
	 * 		Retrieves ID from Extra
	 * 		Uses ID to call static newInstance constructor > returns new frag
	 * 
	 */
	@Override
	protected Fragment createFragment() {

		UUID listId = (UUID)getIntent().getSerializableExtra(EditListFragment.EXTRA_LIST_ID);
		return EditListFragment.newInstance(listId);
		
	}

}
