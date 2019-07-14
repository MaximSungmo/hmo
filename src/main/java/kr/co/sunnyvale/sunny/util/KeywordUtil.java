package kr.co.sunnyvale.sunny.util;

import java.util.Arrays;
import java.util.HashSet;

public class KeywordUtil {
	
	public static final HashSet<String> blockUrlNames = new HashSet<String>(Arrays.asList(new String[]{
			"admin",
			"관리자",
			"basecamp",
			"home",
			"story",
			"comment",
			"news",
			"newsfeed",
			"user",
			"search",
			"plaza",
			"favicon",
			"test",
			"new",
			"notice",
			"event",
			"noti",
			"assets",
			"asset",
			"error",
			"evaluate",
			"1.0",
			"chat"
			
	}));
	
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