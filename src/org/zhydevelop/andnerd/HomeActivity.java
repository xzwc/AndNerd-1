package org.zhydevelop.andnerd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * @author: ChiChou
 */
public class HomeActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);	

		for(int id : new int[]{R.id.button_home_scan, R.id.button_home_search})
			findViewById(id).setOnClickListener(this);

		LinearLayout hotKeywordLayout = (LinearLayout)findViewById(
				R.id.layout_home_hot_keywords);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		layoutParams.setMargins(10,10,10,10);
		for(String hotKeyword : new String[]{"Ruby", "PHP", "Android", "信息安全", "渗透"}) {
			TextView tv = new TextView(this);
			tv.setOnClickListener(new TextViewClickHandler());
			tv.setText(hotKeyword);
			tv.setSingleLine(true);
			tv.setTextAppearance(getApplicationContext(), R.style.HotKeyword);
			tv.setLayoutParams(layoutParams);
			hotKeywordLayout.addView(tv);
		}
	}

	class TextViewClickHandler implements OnClickListener{
		@Override
		public void onClick(View v) {
			search(((TextView)v).getText().toString());
		}		
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.button_home_scan:
			//TODO: 条形码扫描
			break;
		case R.id.button_home_search:
			search();
			break;
		}
	}

	/**
	 * @param keyword
	 */
	private void search(String keyword) {
		startActivity(new Intent(
				this, SearchActivity.class).putExtra(
						SearchActivity.EXTRA_KEYWORD, keyword));
	}

	/**
	 * @param keyword
	 */
	private void search(){
		search(null);
	}
}
