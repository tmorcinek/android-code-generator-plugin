package com.morcinek.activities;

import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.view.View.OnClickListener;

public class LayoutActivity extends Activity implements OnClickListener{

	private Button button;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout);

		button = (Button) findViewById(R.id.button);
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {

		if(view.getId() == R.id.button){

		}
	}

}
