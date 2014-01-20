package org.zhydevelop.andnerd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);	
		startActivity(new Intent(
				this, SearchActivity.class).putExtra(
						SearchActivity.EXTRA_KEYWORD, "oracle"));
	}
}
