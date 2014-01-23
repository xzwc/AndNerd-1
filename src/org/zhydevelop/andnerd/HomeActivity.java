package org.zhydevelop.andnerd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.zhydevelop.andnerd.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class HomeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);	
		startActivity(new Intent(
				this, SearchActivity.class).putExtra(
						SearchActivity.EXTRA_KEYWORD, "ruby"));
	}

}
