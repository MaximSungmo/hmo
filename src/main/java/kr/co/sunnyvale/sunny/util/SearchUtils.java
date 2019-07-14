package kr.co.sunnyvale.sunny.util;

import java.util.Arrays;

public class SearchUtils {
	
	/**
	 * 
	 * @param queries - 기존의 쿼리 목록
	 * @param newQuery - 기존 쿼리 날리고 난 후 새로 쿼리 날린거
	 * @param isRecursive - 결과내 재검색 여부
	 * @return
	 * 결과내 재검색 여부가 true 면 기존 queries 리스트에 newQuery 가 포함돼서 리턴되고
	 * 결과내 재검색 여부가 false 면 기존 queries 리스트는 지워지고 newQuery 만 새로 생성되어 리턴된다.
	 */
	public static String[] checkAndFixQuery( String[] queries, String newQuery, Boolean isRecursive ){
		if( isRecursive != null && isRecursive == true ){
			int tmpOriginalLength = queries.length;
	        queries = Arrays.copyOf(queries, tmpOriginalLength + 1);	// 이전의 검색어들이 들어있는 queries 에 새로운 검색어 등록
	        queries[tmpOriginalLength] = newQuery;
	    }else{
			// 검색 결과에서 결과내 재검색을 체크하지 않고 검색하는 경우
			if( newQuery != null ){
				queries = new String[1];
				queries[0] = newQuery;
			}
		}
		
		return queries; 
	}
	
}