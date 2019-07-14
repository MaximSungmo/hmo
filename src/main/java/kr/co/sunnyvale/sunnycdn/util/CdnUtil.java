package kr.co.sunnyvale.sunnycdn.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class CdnUtil {
	
	public static final HashSet<String> noConvertList = new HashSet<String>(Arrays.asList(new String[]{
			"gif"
			
	}));
	
	
	public static final HashMap<String, String> extToContentType = new HashMap<String, String>();
	static {
		extToContentType.put("doc", "application/msword");
		extToContentType.put("docx", "application/msword");
		extToContentType.put("pdf", "application/pdf");
		extToContentType.put("xls", "application/vnd.ms-excel");
		extToContentType.put("ppt", "application/vnd-ms-powerpoint");
		extToContentType.put("pptx", "application/vnd-ms-powerpoint");
		extToContentType.put("zip", "application/zip");
		extToContentType.put("mpeg", "video/mpeg");
		extToContentType.put("mpg", "video/mpeg");
		extToContentType.put("mpe", "video/mpeg");
		extToContentType.put("avi", "video/x-msvideo");
		extToContentType.put("mp4", "video/mp4");
	}
	
	public CdnUtil(){
		
	}
	
	public static HashSet<String> getBlockNicknames() {
		HashSet<String> blockNicknames = new HashSet<String>();
		
		blockNicknames.add("admin");
		blockNicknames.add("관리자");
		blockNicknames.add("basecamp");
		blockNicknames.add("story");
		blockNicknames.add("comment");
		blockNicknames.add("news");
		blockNicknames.add("newsfeed");
		
		return blockNicknames; 
	}
	
}