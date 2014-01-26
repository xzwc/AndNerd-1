package org.zhydevelop.andnerd;

import org.zhydevelop.andnerd.bean.Book;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class BookDetailActivity extends Activity {
	//
	public static String ACTION_ID = "id", ACTION_SERIALIZED = "serialized";	
	public static String TAG_BOOK = "book", TAD_ID = "id";
	
	private TextView textTitle, textISBN, textCode, textAuthor,
			textPublisher, textAvaliable;
	
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
		
		Intent intent = getIntent();
		String action = intent.getAction();
		if(action.equalsIgnoreCase(ACTION_ID)) {
			//通过ID调用
			book = new Book();
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
		}
		
		//绑定信息
		textTitle.setText(book.getTitle());
		textCode.setText(book.getCode());
		textAuthor.setText(book.getAuthor());
		textPublisher.setText(book.getPublisher());
		textISBN.setText(""); //TODO
		textAvaliable.setText(book.getLendable());
	}
}
