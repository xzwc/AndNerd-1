package org.zhydevelop.andnerd;

import java.util.List;

import org.zhydevelop.andnerd.parser.HuiwenParser;
import org.zhydevelop.andnerd.util.HuiwenURLBuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * @author: ChiChou
 */
public class HomeActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);	

		for(int id : new int[] {R.id.button_home_scan, R.id.button_home_search})
			findViewById(id).setOnClickListener(this);

		//热门关键词
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(HuiwenURLBuilder.top10(), new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				//添加热门关键词
				loadKeywords(response);
			}

			@Override
			public void onFailure(int statusCode, org.apache.http.Header[] headers, 
					byte[] responseBody, Throwable error)
			{
				loadKeywords(null);
			}
		});

	}

	private void loadKeywords(String content) {
		//添加热门关键词
		if(content == null) return;

		LinearLayout hotKeywordLayout = (LinearLayout)findViewById(
				R.id.layout_home_hot_keywords);

		//监听点击事件
		OnClickListener textViewClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				search(((TextView)v).getText().toString());
			}		
		};
		//解析内容
		HuiwenParser parser = new HuiwenParser(content);
		List<String> list = parser.parseKeywords();
		for(String hotKeyword : list) {
			//创建一个新的布局
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			layoutParams.setMargins(10, 10, 10, 10);

			//生成一个新链接
			TextView tv = new TextView(this);
			tv.setOnClickListener(textViewClickListener);
			tv.setText(hotKeyword);
			tv.setSingleLine(true);
			tv.setTextAppearance(this, R.style.HotKeyword);
			//设置布局
			tv.setLayoutParams(layoutParams);
			hotKeywordLayout.addView(tv);
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
