package com.apc.ups_selector.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.apc.ups_selector.app.R;

public class SearchResultsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//customize title
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.results);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		
	}
	
}
