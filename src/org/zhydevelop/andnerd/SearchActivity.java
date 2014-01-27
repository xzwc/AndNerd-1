package org.zhydevelop.andnerd;

import java.util.ArrayList;
import java.util.List;

import org.zhydevelop.andnerd.adapter.BookListAdapter;
import org.zhydevelop.andnerd.bean.Book;
import org.zhydevelop.andnerd.parser.HuiwenParser;
import org.zhydevelop.andnerd.util.HuiwenURLBuilder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class SearchActivity extends Activity implements OnClickListener {
	//Intent相关常量
	public static String EXTRA_KEYWORD = "keyword";
	
	//同时进行的HTTP请求数
	public static int MAX_CONNECTIONS = 3;

	//运行状态
	private enum Status {READY, LOADING_FIRST, LOADING_MORE, DONE};
	Status mStatus = Status.READY;

	//界面元素
	private ImageButton mButtonSearch;
	private ImageView mButtonClear;
	private EditText mTextKeyword;
	private ListView listView;
	private View mLoadingIcon;
	private TextView mLoadingText, mCountText; 

	//当前翻页
	private int mPage, mLimit, mCount;
	private String mKeyword;
	//搜素结果
	private ArrayList<Book> mBooks;
	private BookListAdapter mResultAdapter;

	//异步请求
	AsyncHttpClient asyncHttpClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		mButtonClear = (ImageView)findViewById(R.id.button_clear_search);
		mButtonClear.setOnClickListener(this);	
		mButtonSearch = (ImageButton)findViewById(R.id.button_detail_menu);
		mButtonSearch.setOnClickListener(this);

		findViewById(R.id.button_search_return).setOnClickListener(this);

		LayoutInflater inflater = (LayoutInflater)getApplication()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		//列表的FOOTER
		View footer = inflater.inflate(R.layout.footer_load_more, null),
				header = inflater.inflate(R.layout.header_search_list, null);
		mLoadingIcon = footer.findViewById(R.id.progress_loading_list);
		mLoadingText = (TextView)footer.findViewById(R.id.text_load_more);
		mCountText = (TextView)header.findViewById(R.id.text_result_sum);

		//结果列表
		listView = (ListView)findViewById(R.id.list_result);
		listView.addHeaderView(header);
		listView.addFooterView(footer);
		listView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				hideKeyboard();
				return false;
			}
		});

		//关键字文本框
		mTextKeyword = (EditText)findViewById(R.id.edit_keyword);
		mTextKeyword.setImeActionLabel(getString(R.string.search), KeyEvent.KEYCODE_ENTER);
		mTextKeyword.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable e) { 
				mKeyword = e.toString();
				mButtonClear.setVisibility(mKeyword.length() > 0 ? View.VISIBLE : View.GONE);
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
		mTextKeyword.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
						(keyCode == KeyEvent.KEYCODE_ENTER)) {
					//TODO:优化LOAD的API
					mPage = 0;
					mStatus = Status.LOADING_FIRST;
					load();
					return true;
				}
				return false;
			}
		});

		listView.setOnScrollListener(new OnScrollListener(){
			@Override
			public void onScroll(AbsListView paramAbsListView, int firstVisibleItem, 
					int visibleItemCount, int totalItemCount) {
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(mStatus == Status.READY &&
						scrollState == OnScrollListener.SCROLL_STATE_IDLE &&
						view.getLastVisiblePosition() == view.getCount() - 1) {
					load();
					mStatus = Status.LOADING_MORE;                	
				}
			}
		});

		//初始化列表
		mPage = 0;
		mLimit = 20;
		mBooks = new ArrayList<Book>(mLimit);
		mResultAdapter = new BookListAdapter(getApplication(), mBooks);
		listView.setAdapter(mResultAdapter);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> l, View v, int position, long id) {
				Book book = (Book)l.getItemAtPosition(position);
				if(book == null) return;
				
				//打开页面
				Intent intent = new Intent(SearchActivity.this, BookDetailActivity.class);
				Bundle bundle = new Bundle();
				//以object方式传递参数
				intent.setAction(BookDetailActivity.ACTION_SERIALIZED);
				bundle.putSerializable(BookDetailActivity.TAG_BOOK, book);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		asyncHttpClient = new AsyncHttpClient();        
		asyncHttpClient.setMaxConnections(MAX_CONNECTIONS);
		//search books
		mKeyword = getIntent().getStringExtra(EXTRA_KEYWORD);
		mTextKeyword.setText(mKeyword);
		if(mKeyword != null && mKeyword.length() > 0)
			load();
	}

	/**
	 * 执行第一次搜索
	 */
	private void load() {
		hideKeyboard();

		//第一次搜索
		if(mPage == 0) {		
			if(mKeyword == null || mKeyword.length() == 0) {
				Toast.makeText(getApplicationContext(), 
						getString(R.string.please_input_keyword), Toast.LENGTH_SHORT).show();
				return;
			}
			mButtonSearch.setImageResource(R.drawable.ic_cancel);

			//清空结果
			mBooks.clear();
			mResultAdapter.notifyDataSetChanged();
			//设置状态
			mStatus = Status.LOADING_FIRST;
			mCountText.setVisibility(View.GONE);
		} else {
			mButtonSearch.setImageResource(R.drawable.ic_cancel);
			mStatus = Status.LOADING_MORE;
		}

		//Prepare UI
		mLoadingIcon.setVisibility(View.VISIBLE);
		mLoadingText.setText(R.string.loading);
		mLoadingText.setVisibility(View.VISIBLE);		

		mPage++;
		String url = HuiwenURLBuilder.search(mKeyword, mPage, mLimit);		

		asyncHttpClient.get(SearchActivity.this, url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {		    	
				HuiwenParser parser = new HuiwenParser(response);
				List<Book> result;
				if(mPage == 1) {
					mCount = parser.getCount();
					if(mCount == 0) {
						//TODO
						mLoadingText.setText(R.string.not_found);
					} else {
						mCountText.setVisibility(View.VISIBLE);
						result = parser.parseBooks();
						mBooks.addAll(result);
					}					
					mCountText.setText(String.format(getString(R.string.result_sum), mCount));
				} else {
					result = parser.parseBooks();
					mBooks.addAll(result);
				}
				mButtonSearch.setImageResource(R.drawable.ic_search);
				mResultAdapter.notifyDataSetChanged();		    	
				mLoadingIcon.setVisibility(View.GONE);

				//TODO:优化此处逻辑
				if(mLimit * mPage >= mCount) {
					if(mCount > 0)
						mLoadingText.setText(R.string.load_finished);
					mStatus = Status.DONE;
				} else {
					mStatus = Status.READY;
				}
			}

			@Override
			public void onFailure(int statusCode, org.apache.http.Header[] headers, 
					byte[] responseBody, Throwable error)
			{
				//TODO
				mLoadingIcon.setVisibility(View.GONE);
				mButtonSearch.setImageResource(R.drawable.ic_search);
				if(mStatus == Status.LOADING_FIRST ) {
					//网络错误
					mLoadingText.setText(R.string.not_found);
					Toast.makeText(getApplication(), getString(R.string.netword_error), 
							Toast.LENGTH_SHORT).show();
					mStatus = Status.READY;
				} else if(mStatus == Status.LOADING_MORE){
					//网络错误
					mLoadingText.setText(R.string.load_finished);
					Toast.makeText(getApplication(), getString(R.string.netword_error), 
							Toast.LENGTH_SHORT).show();
					mStatus = Status.DONE;
				} else {
					//用户取消
					mStatus = Status.READY;
				}				
			}
		});
	}

	@Override  
	protected void onDestroy() {  
		super.onDestroy();
		//TODO
		if(mStatus == Status.LOADING_FIRST || mStatus == Status.LOADING_MORE)
			abort();
	}

	/**
	 * 隐藏键盘
	 */
	private void hideKeyboard() {
		try{
			((InputMethodManager)this.getSystemService(
					Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
							this.getCurrentFocus().getWindowToken(),      
							InputMethodManager.HIDE_NOT_ALWAYS);
		} catch(Exception e) {

		}
	}
	/*
	 * 取消请求
	 */
	private void abort() {
		//取消
		asyncHttpClient.cancelRequests(SearchActivity.this, true);
		mStatus = Status.READY;
		mLoadingIcon.setVisibility(View.GONE);
		mLoadingText.setText(R.string.user_aborted);
		mButtonSearch.setImageResource(R.drawable.ic_search);		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		//清空文本框
		case R.id.button_clear_search:
			//清空关键字
			mTextKeyword.setText(mKeyword = "");
			mBooks.clear();
			mResultAdapter.notifyDataSetChanged();
			mLoadingText.setVisibility(View.GONE);
			mCountText.setVisibility(View.GONE);			
			break;

		case R.id.button_detail_menu:
			mTextKeyword.clearFocus();
			switch(mStatus) {
			case READY:
			case DONE:
				//执行搜索
				mPage = 0;
				mStatus = Status.LOADING_FIRST;
				load();
				break;
			case LOADING_FIRST:
			case LOADING_MORE:
				//取消搜索
				abort();
				break;
			default:
			}
			break;
		case R.id.button_search_return:
			//退出Activity
			finish();
		default:
			break;
		}
	}
}