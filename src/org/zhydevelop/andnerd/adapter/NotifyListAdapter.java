package org.zhydevelop.andnerd.adapter;

import java.util.List;

import org.zhydevelop.andnerd.R;
import org.zhydevelop.andnerd.bean.Book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NotifyListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<Book> mBooks;
//	private int[] mTextColors;
//	private int[] mBackgroundColors;
	
	private final class ViewHolder {
		public TextView title, number, code, publisher, author;
//		public View view;
	}
	
	public NotifyListAdapter(Context context, List<Book> books){
		mInflater = LayoutInflater.from(context);
		mBooks = books;
		
//		Resources res = context.getResources();
//		mTextColors = res.getIntArray(R.array.list_text_colors);
//		mBackgroundColors = res.getIntArray(R.array.list_background_colors);
	}
	
	@Override
	public int getCount() {
		return mBooks.size();
	}

	@Override
	public Object getItem(int position) {
		return mBooks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		
		if (convertView == null) {			 
			viewHolder = new ViewHolder();
			/*viewHolder.view = */convertView = mInflater.inflate(
					R.layout.listitem_book_search, null);		
			viewHolder.title = (TextView)convertView.findViewById(R.id.text_title);
			viewHolder.number = (TextView)convertView.findViewById(R.id.text_number);
			viewHolder.code = (TextView)convertView.findViewById(R.id.text_code);
			viewHolder.publisher = (TextView)convertView.findViewById(R.id.text_publisher);
			viewHolder.author = (TextView)convertView.findViewById(R.id.text_author);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder)convertView.getTag();
		}

		//显示界面内容
		viewHolder.title.setText(mBooks.get(position).getTitle());		
		viewHolder.number.setText(mBooks.get(position).getLendable());
		viewHolder.code.setText(mBooks.get(position).getCode());
		viewHolder.author.setText(mBooks.get(position).getAuthor());
		viewHolder.publisher.setText(mBooks.get(position).getPublisher());
		
//		viewHolder.title.setTextColor(
//				mTextColors[position % mTextColors.length]);
//		viewHolder.view.setBackgroundColor(
//				mBackgroundColors[position % mBackgroundColors.length]);
		
		return convertView;
	}

}
