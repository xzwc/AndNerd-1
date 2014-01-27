package org.zhydevelop.andnerd;

import org.zhydevelop.andnerd.bean.AmazonItem;
import org.zhydevelop.andnerd.bean.Book;
import org.zhydevelop.andnerd.parser.AmazonParser;
import org.zhydevelop.andnerd.parser.HuiwenParser;
import org.zhydevelop.andnerd.util.AmazonURLBuilder;
import org.zhydevelop.andnerd.util.CacheUtil;
import org.zhydevelop.andnerd.util.HuiwenURLBuilder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;

/**
 * @author ChiChou
 *
 */
public class BookDetailActivity extends Activity {
	//
	public static String ACTION_ID = "id", ACTION_SERIALIZED = "serialized";	
	public static String TAG_BOOK = "book", TAD_ID = "id", ACTION_ISBN = "isbn";

	//界面元素
	private TextView textTitle, textISBN, textCode, textAuthor,
	textPublisher, textAvaliable;
	private View progressLoading;
	private ImageView imageCover;

	//HTTP请求类
	AsyncHttpClient asyncHttpClient;

	//
	Book book;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_detail);

		//绑定控件
		textTitle = (TextView)findViewById(R.id.text_detail_book_title);
		textAuthor = (TextView)findViewById(R.id.text_detail_book_author);
		textISBN = (TextView)findViewById(R.id.text_detail_book_isbn);
		textCode = (TextView)findViewById(R.id.text_detail_book_code);		
		textPublisher = (TextView)findViewById(R.id.text_detail_book_publisher);		
		textAvaliable = (TextView)findViewById(R.id.text_detail_book_avaliable);
		progressLoading = findViewById(R.id.progress_detail_cover_loading);
		imageCover = (ImageView)findViewById(R.id.img_detail_cover);

		//获得启动参数
		Intent intent = getIntent();
		String action = intent.getAction();
		if(action.equalsIgnoreCase(ACTION_ID)) {
			//通过ID调用
			book = new Book();

		} else if(action.equalsIgnoreCase(ACTION_ISBN)) {
			//ISBN

		} else if(action.equalsIgnoreCase(ACTION_SERIALIZED)) {
			//通过序列化数据调用
			try {
				book = (Book)(intent.getExtras().getSerializable(TAG_BOOK));
			} catch(Exception ex) {
			} finally {
				if(book == null) {
					Toast.makeText(this, R.string.error_invalid_call, Toast.LENGTH_SHORT).show();
					finish();
					return;
				}
			}
			updateForm();

			//初始化请求客户端
			asyncHttpClient = new AsyncHttpClient();			

			//获取更详细的信息
			String url = HuiwenURLBuilder.item(book.getId());
			asyncHttpClient.get(BookDetailActivity.this, url, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					HuiwenParser parser = new HuiwenParser(response);
					book = parser.detail();
					loadCover();
					updateForm();
				}
			});
		}
	}


	/**
	 * 加载封面
	 */
	private void loadCover() {
		if(book == null) return;
		
		//ISBN
		String ISBN = book.getISBN();
		//无效的ISBN
		if(ISBN == null || ISBN.length() == 0) {
			imageCover.setImageResource(R.drawable.blank_cover);
			return;
		}
		
		final String cacheName = book.getISBN() + ".jpg";
		//
		progressLoading.setVisibility(View.VISIBLE);
		
		Context context = getApplicationContext();
		//载入缓存
		final CacheUtil cache = new CacheUtil(context);
		byte[] data = cache.get(cacheName);
		try {
			Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
			imageCover.setImageBitmap(bm);
		} catch(Exception ex) {
			data = null;
		}
		
		if(data != null) {
			progressLoading.setVisibility(View.GONE);
			return;
		}

		String url = AmazonURLBuilder.search(ISBN);
		//请求
		asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				String[] allowedContentTypes = new String[] { "image/jpeg" };
				
				AmazonItem item = AmazonParser.parse(response);
				if(item == null) {
					imageCover.setImageResource(R.drawable.blank_cover);
					return;
				}
				final String urlImage = item.getImage();				
				
				asyncHttpClient.get(urlImage, new BinaryHttpResponseHandler(allowedContentTypes) {
					@Override
					public void onSuccess(byte[] data) {
						//载入图片
						Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
						imageCover.setImageBitmap(bm);
						//放进缓存
						cache.put(cacheName, data);						
						progressLoading.setVisibility(View.GONE);
					}
				});
			}
		});

	}

	/**
	 * 更新界面
	 */
	private void updateForm() {
		//绑定信息
		textTitle.setText(book.getTitle());
		textCode.setText(book.getCode());
		textAuthor.setText(book.getAuthor());
		textPublisher.setText(book.getPublisher());
		textISBN.setText(book.getISBN()); //TODO
		textAvaliable.setText(book.getLendable());
	}
}
