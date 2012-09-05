package com.morcinek.activities;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.ViewPager;

public class SpecificActivity extends Activity {

	private ViewPager pager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.specific);

		pager = (ViewPager) findViewById(R.id.pager);
	}

}
