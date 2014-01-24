package org.zhydevelop.andnerd;

import java.util.ArrayList;
import java.util.List;

import org.zhydevelop.andnerd.adapter.BookListAdapter;
import org.zhydevelop.andnerd.bean.Book;
import org.zhydevelop.andnerd.parser.HuiwenParser;
import org.zhydevelop.andnerd.util.HuiwenURLBuilder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class SearchActivity extends Activity implements OnClickListener {
	public static String EXTRA_KEYWORD = "keyword";
	
	private enum Status {READY, LOADING_FIRST, LOADING_MORE, DONE};
	Status mStatus;
	
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
		mButtonSearch = (ImageButton)findViewById(R.id.button_search);
		mButtonSearch.setOnClickListener(this);
		
		LayoutInflater inflater = (LayoutInflater)getApplication()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View footer = inflater.inflate(R.layout.footer_load_more, null),
				header = inflater.inflate(R.layout.header_search_list, null);
		mLoadingIcon = footer.findViewById(R.id.progress_loading_list);
		mLoadingText = (TextView)footer.findViewById(R.id.text_load_more);
		mCountText = (TextView)header.findViewById(R.id.text_result_sum);
				
		listView = (ListView)findViewById(R.id.list_result);
		listView.setDivider(null);
		listView.addHeaderView(header);
		listView.addFooterView(footer);		
		
		mTextKeyword = (EditText)findViewById(R.id.edit_keyword);
		mTextKeyword.addTextChangedListener(new TextWatcher() {
	        public void afterTextChanged(Editable e) { 
	        	mKeyword = e.toString();
	        	mButtonClear.setVisibility(mKeyword.length() > 0 ? View.VISIBLE : View.GONE);
	        }
	        
		    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		    public void onTextChanged(CharSequence s, int start, int before, int count) {}
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
		
        mPage = 0;
        mLimit = 20;
        mBooks = new ArrayList<Book>(mLimit);
        
        asyncHttpClient = new AsyncHttpClient();
        //search books
        mKeyword = getIntent().getStringExtra(EXTRA_KEYWORD);
		mTextKeyword.setText(mKeyword);
        load();
	}
	
	/**
	 * 执行第一次搜索
	 */
	private void load() {	
		//第一次搜索
		if(mPage == 0) {			
			if(mKeyword == null || mKeyword.length() == 0) {
				Toast.makeText(getApplicationContext(), 
						getString(R.string.please_input_keyword), Toast.LENGTH_SHORT).show();
				return;
			}
	    	mResultAdapter = new BookListAdapter(getApplication(), mBooks);
	    	listView.setAdapter(mResultAdapter);
	    	
	    	mCountText.setVisibility(View.GONE);
		}
		
		//Prepare UI
		mLoadingIcon.setVisibility(View.VISIBLE);
		mLoadingText.setText(R.string.loading);
		mLoadingText.setVisibility(View.VISIBLE);		
		
		mPage++;
		String url = HuiwenURLBuilder.search(mKeyword, mPage, mLimit);		
		
		asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
		    @Override
		    public void onSuccess(String response) {		    	
		    	HuiwenParser parser = new HuiwenParser(response);
		    	List<Book> result;
		    	if(mPage == 1) {		    		
		    		mBooks.clear();
			    	mCount = parser.getCount();
					if(mCount == 0) {
						//TODO
					} else {
						mCountText.setVisibility(View.VISIBLE);
						result = parser.parseBooks();
				    	mBooks.addAll(result);
					}
					parser = null; //回收内存
			    	mCountText.setText(String.format(getString(R.string.result_sum), mCount));
		    	} else {
		    		result = parser.parseBooks();
			    	mBooks.addAll(result);
		    	}
		    	mButtonSearch.setImageResource(R.drawable.ic_search);
		    	mResultAdapter.notifyDataSetChanged();		    	
		    	mLoadingIcon.setVisibility(View.GONE);
		    	if(mLimit * mPage >= mCount) {
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
		    	if(mStatus == Status.LOADING_FIRST || mStatus == Status.LOADING_MORE) {
		    		//网络错误
		    		mLoadingText.setText(R.string.netword_error);
			    	Toast.makeText(getApplication(), getString(R.string.netword_error), 
			    			Toast.LENGTH_SHORT).show();	
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
	
	/*
	 * 取消请求
	 */
	private void abort() {
		//取消
		asyncHttpClient.cancelRequests(getApplicationContext(), true);
		mStatus = Status.READY;
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
			break;

		case R.id.button_search:
			switch(mStatus) {
			case READY:
			case DONE:
				//执行搜索
				mButtonSearch.setImageResource(R.drawable.ic_cancel);

				mPage = 0;
				mStatus = Status.LOADING_FIRST;
				load();
				break;
			case LOADING_FIRST:
				//TODO
				abort();
			case LOADING_MORE:
				abort();
				//TODO
				
			default:
				break;
			} 
			break;
		}
	}
}