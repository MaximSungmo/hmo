package kr.co.sunnyvale.sunny.util;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class CriteriaUtils {
	
	
	public static Criterion returnRestrictionAfterCheckInitial( String column, String queryName ){
		
		if( queryName.length() != 1 ){
			return Restrictions.like(column, queryName, MatchMode.ANYWHERE);
		}
		String start = null;
		String end = null;
		
		switch(queryName){
		case "ㄱ":
			start = "가";
			end = "나";
			break;
		case "ㄴ":
			start = "나";
			end = "다";
			break;
		case "ㄷ":
			start = "다";
			end = "라";
			break;
		case "ㄹ":
			start = "라";
			end = "마";
			break;
		case "ㅁ":
			start = "마";
			end = "바";
			break;
		case "ㅂ":
			start = "바";
			end = "사";
			break;
		case "ㅅ":
			start = "사";
			end = "아";
			break;
		case "ㅇ":
			start = "아";
			end = "자";
			break;
		case "ㅈ":
			start = "자";
			end = "차";
			break;
		case "ㅊ":
			start = "차";
			end = "카";
			break;
		case "ㅋ":
			start = "카";
			end = "타";
			break;
		case "ㅌ":
			start = "타";
			end = "파";
			break;
		case "ㅍ":
			start = "파";
			end = "하";
			break;
		case "ㅎ":
			start = "하";
			end = "힣";
			break;
		default:
			break;
		}
		
		
		if( start != null && end != null ){
			return Restrictions.and(
					Restrictions.ge(column, start),
					Restrictions.lt(column, end)
					);
		}else{
			return Restrictions.like(column, queryName, MatchMode.ANYWHERE);
		}
		
	}
	
}