/**
 * 存取缓存
 */
package org.zhydevelop.andnerd.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

/**
 * @author ChiChou
 * 缓存工具类
 */
public class CacheUtil {
	private Context context;
	public CacheUtil(Context context) {
		this.context = context;
	}

	/**
	 * @return 返回缓存目录
	 */
	public String getCacheDir() {
		File file = context.getCacheDir();
		return file.getAbsolutePath();
	}
	
	/**
	 * @param url 远程地址
	 * @param content 写入内容
	 * 写入缓存
	 */
	public void put(String key, byte[] content) {
		try {
			File file = new File(concat(key));
			if(file.exists()) file.delete();
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(content);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param url 地址
	 * @return 缓存文件内容
	 */
	public byte[] get(String key) {
		byte[] content = null;
		try {
			File file = new File(concat(key));
			if(file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = -1;
				while((len = fis.read(buffer)) != -1){
					baos.write(buffer, 0, len);
				}
				content = baos.toByteArray();
				baos.close();
				fis.close();
			}
		} catch (IOException ex) {
			
		}
		return content;
	}

	private String concat(String file) {
		return new StringBuilder(getCacheDir()).append(File.separator)
				.append(file).toString();
	}
	
}
