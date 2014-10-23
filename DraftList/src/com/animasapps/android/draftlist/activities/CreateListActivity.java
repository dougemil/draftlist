package com.animasapps.android.draftlist.activities;

import com.animasapps.android.draftlist.CreateListFragment;

import android.app.Fragment;

public class CreateListActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new CreateListFragment();
	}

}
