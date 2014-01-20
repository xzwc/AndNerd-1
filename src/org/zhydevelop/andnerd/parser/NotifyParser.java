package org.zhydevelop.andnerd.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.zhydevelop.andnerd.bean.FeedItem;

import android.util.Xml;

public class NotifyParser implements FeedParser {
	private String ENCODING = "UTF-8";
	
	private String
			TAG_ITEM = "item",
			TAG_TITLE = "title",
			TAG_LINK = "link",
			TAG_DESCRIPTION = "description";
	
    @Override  
    public List<FeedItem> parse(InputStream is) throws Exception {  
        List<FeedItem> feeds = null;  
        FeedItem feed = null;
        
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, ENCODING);  
  
        int eventType = parser.getEventType();  
        while (eventType != XmlPullParser.END_DOCUMENT) { 
            switch (eventType) {  
            case XmlPullParser.START_DOCUMENT:  
                feeds = new ArrayList<FeedItem>();  
                break;  
            case XmlPullParser.START_TAG:  
            	String name = parser.getName();
                if (name.equals(TAG_ITEM)) {                	
                    feed = new FeedItem();  
                } else if (name.equals(TAG_TITLE)) {
                	if(feed == null) {
                		//文档的标题
                	} else {
                		//项目的标题
                        feed.setTitle(parser.getText());
                	}         	
                } else if(name.equals(TAG_LINK) && feed != null) {
                	//链接
                    feed.setLink(parser.getText());            	
                } else if(name.equals(TAG_DESCRIPTION) && feed != null) {
                	//说明
                	feed.setDescription(parser.getText());                	
                }
                break;
            case XmlPullParser.END_TAG:  
                if (parser.getName().equals(TAG_ITEM)) {  
                    feeds.add(feed);
                    feed = null;
                }
                break;  
            }  
            eventType = parser.next();  
        }  
        return feeds;  
    }  
      
    @Override  
    public String serialize(List<FeedItem> feeds) throws Exception {
    	return null;
    	//@TODO:
//        XmlSerializer serializer = Xml.newSerializer();  
//        StringWriter writer = new StringWriter();  
//        serializer.setOutput(writer);   //设置输出方向为writer  
//        serializer.startDocument(ENCODING, true);  
//        serializer.startTag("", "books");  
//        for (FeedItem feed : feeds) {  
//            serializer.startTag("", "book");
//            //serializer.attribute("", "id", feed.getId() + "");  
//              
//            serializer.startTag("", "name");  
//            serializer.text(feed.getTitle());  
//            serializer.endTag("", "name");  
//              
//            serializer.endTag("", "book");  
//        }  
//        serializer.endTag("", "books");  
//        serializer.endDocument();  
//
//        return writer.toString();
    }
}
