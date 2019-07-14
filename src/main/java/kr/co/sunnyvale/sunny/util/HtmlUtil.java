package kr.co.sunnyvale.sunny.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.co.sunnyvale.sunny.domain.Content;


public class HtmlUtil {
	
	
	public static final int HREF_STATE_NONE = 0;
	public static final int HREF_STATE_START = 1;
	public static final int HREF_STATE_MEET_FIRST_A = 2;
	public static final int HREF_STATE_SECOND_START = 3;
	public static final int HREF_STATE_MEET_SLASH = 4;
	
	
	/*
	 * 허용되는 태그 목록
	 */
//	public static final String hydeAllowTags[] = { "a", "b", "br", "u", "i", "s", "span", "p", "div", "img", "font", "embed", "center", "sup", "sub", "ol", "li", "ul", "table", "tr", "th", "td", "tbody", "blockquote", "object"} ;
	
	/**
	 * nl2br 과 replace 를 수정해서 합쳤습니다. 
	 * 기능은
	 * 1. \n 을 <br /> 로 바꾸기
	 * 2. \n 카운트 세기
	 * 3. 긴글 짧은글 기준으로 prev, next 에 넣기 
	 * 줄바꿈문자(n)을 &lt;br&gt;n으로 바꿈
	 * 
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static ParsedText parseRawText(String strSrc, int contentType){
		if( strSrc == null ){
			return null;
		}
		ParsedText parsedText = new ParsedText();
		
		int lengthStandard = 500;
		
		if (contentType == Content.TYPE_QUESTION
				|| contentType == Content.TYPE_ANSWER
				|| contentType == Content.TYPE_NOTE
				|| contentType == Content.TYPE_APPROVAL) {
			// strSrc = filterHtmlTag(strSrc, redwoodAllowTags);
			parsedText.setTaggedTextPrev(strSrc);
			parsedText.setReturnCount(0);
			return parsedText;
		}
		
		/*
		 * 짧은 글
		 * 1. <, > 을 escape 시킨다.
		 * 2. http://www.naver.com 과 같은 것을 <a href="http://www.naver.com">http://www.naver.com</a> 와 같이 autolink 한다.
		 * 3. Custom Markup을 태깅한다. [code] 와 같은 것들
		 * 4. 엔터와 공백을 escape 하면서 returnCount 도 세고 이것저것 처리한다.
		 */
		
		strSrc = strSrc.replaceAll("\\$","\\\\\\$");
		
		/*
		 * 1. escape
		 */
		strSrc = StringEscapeUtils.escapeHtml(strSrc);
		
		/*
		 * 2. autoLink 
		 */
		strSrc = autoLinkURL(strSrc);
		
		/*
		 * 3. Custom Markup 은 아직 구현되지 않음
		 * 
		 */
		strSrc = autoLinkMention( parsedText, strSrc );
		
		strSrc = autoBold( parsedText, strSrc );
		
		strSrc = autoLinkHashTag( parsedText, strSrc );
		/*
		 * 4. 엔터와 공백을 escape 하면서 returnCount 도 세고 이것저것 처리한다.
		 */
		int orgLength = strSrc.length();
		StringEscapeResult retEscape = escapeHTML(strSrc, lengthStandard);
		strSrc = retEscape.getContent();
		
		parsedText.setReturnCount( retEscape.getReturnCount() );
		
		
		int prevContentLength = 0;
		int prevContentOriginalLength = 0;
//		if( contentType == ContentType.COMMENT ){
//			prevContentLength = retEscape.getPrevCommentLength();
//			prevContentOriginalLength = retEscape.getPrevCommentOriginalLength();
//		}else{
			prevContentLength = retEscape.getPrevStoryLength();
			prevContentOriginalLength = retEscape.getPrevStoryOriginalLength();
//		}
		
		/*
		 * 리턴 개수도 기준 미만이고, 길이도 짧을 때 잘라줄 필요도 없이 걍 앞에다 넣어준다. 
		 */
		if( prevContentLength == 0  ){
			parsedText.setTaggedTextPrev( retEscape.getContent() );
			return parsedText;
		}
		
		/*
		 * 자르고 남는게 적을 때 부분. 리턴 개수 기준이나 길이가 너무 길어서 잘랐는데 남은게 50개정도로 애매하게 남아있으면 걍 앞에 붙여준다. 
		 */
		if( ( orgLength - prevContentOriginalLength ) < 50 ){
			parsedText.setTaggedTextPrev( retEscape.getContent() );
			return parsedText;
		}
		
		
		parsedText.setTaggedTextPrev( strSrc.substring(0, prevContentLength) );
		parsedText.setTaggedTextNext( strSrc.substring(prevContentLength) );
		return parsedText;
		

	}
	

	
	
	
	
	/**
	 * nl2br 과 replace 를 수정해서 합쳤습니다. 
	 * 기능은
	 * 1. \n 을 <br /> 로 바꾸기
	 * 2. \n 카운트 세기
	 * 3. 긴글 짧은글 기준으로 prev, next 에 넣기 
	 * 줄바꿈문자(n)을 &lt;br&gt;n으로 바꿈
	 * 
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static ParsedText parseMessageRawText(String strSrc){
		if( strSrc == null ){
			return null;
		}
		ParsedText parsedText = new ParsedText();
		
		
		/*
		 * 짧은 글
		 * 1. <, > 을 escape 시킨다.
		 * 2. http://www.naver.com 과 같은 것을 <a href="http://www.naver.com">http://www.naver.com</a> 와 같이 autolink 한다.
		 * 3. Custom Markup을 태깅한다. [code] 와 같은 것들
		 * 4. 엔터와 공백을 escape 하면서 returnCount 도 세고 이것저것 처리한다.
		 */
		
		strSrc = strSrc.replaceAll("\\$","\\\\\\$");
		
		/*
		 * 1. escape
		 */
		strSrc = StringEscapeUtils.escapeHtml(strSrc);
		
		/*
		 * 2. autoLink 
		 */
		strSrc = autoLinkURL(strSrc);
		
		
		StringEscapeResult retEscape = escapeHTML(strSrc, 1000000000);
		strSrc = retEscape.getContent();
		
		parsedText.setReturnCount( retEscape.getReturnCount() );
		parsedText.setTaggedTextPrev(strSrc);
		
		return parsedText;
		

	}
	
	private static String autoBold(ParsedText parsedText, String strSrc) {
		if( strSrc == null || strSrc.length() < 1 ){
			return strSrc;
		}
		String pattern = "\\[B\\](.*?)\\[/B\\]+?";
		
	    StringBuffer sb = new StringBuffer();
	    try {
	        Pattern compiledPattern = Pattern.compile(pattern, Pattern.DOTALL);
	        Matcher matcher = compiledPattern.matcher(strSrc);
	        
	        while (matcher.find()) {
	            String boldString = matcher.group(1).trim();
	            matcher.appendReplacement(sb, "<strong>" + boldString + "</strong>");
	        }
	        matcher.appendTail(sb);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	throw new RuntimeException("autoBold 에서 실패");
	    }
	    
	    return sb.toString();
	    
	}


	public static void main(String args[]) throws Exception {
//		String htmlString = "hello world hello hi hello !!!";
//		String targetString = "hello";
//		String highlightOpenTag = "<span style=\"background-color:blue;\">";
//		String highlightCloseTag = "</span>";
//		String msg = highlighting(htmlString, targetString, highlightOpenTag,
//				highlightCloseTag);
//		System.out.println(msg);
		
	//	String rawString = "http://hellowlr.com";
			//String rawString = "http://identi.ca/?page=2 11111     2222  \nhello <javascript>s</javascript>ydney sdfsd  <a      href='kkk'>f</a>s  http://hellowlr.com <br /> dfdfd";
		//String rawString = "http://identi.ca/?page=2 ********** www.yacamp.com *****	http://hellowlr.com ********* img src='http://cdn.yacamp.com/s2/57/53//1369155465357_965_h.jpg'/>";
		String rawString = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15\n ";
		//ParsedText parsedText = new ParsedText();
		//System.out.println( filterHtmlTag(rawString , redwoodAllowTags) );
		//System.out.println(parseRawText( rawString, 1 ));
		
//		String content = "안녕하세요. http://youtu.be/cJgQAk2tvzc ㅋㅋㅋㅋ 주소를 뽑아요. http://www.youtube.com/watch?v=PhxI8Fd5a1o 랄라. http://youtu.be/e902jNKIcmA zz";
//		String[] list = HtmlUtil.getYoutubeLinkList(content);
//		for (String link : list) {
//			System.out.println(link);
//			System.out.println(HtmlUtil.convertYoutubeEmbedHtmlTagFromYoutubeLink(link, false));
//			System.out.println("-------------------------------------------------------");
//			System.out.println("<img src=" + HtmlUtil.getYoutubeThumbnailImage(HtmlUtil.getYoutubeVideoId(link), 0) + ">\n\n");
//		}
		
//		String content = "안녕 &amp; & < > &gt;";
//		String str = StringEscapeUtils.escapeHtml(content);
//		String content2 = StringEscapeUtils.unescapeHtml(str);
//		System.out.println(content);
//		System.out.println(str);
//		System.out.println(content2);
		
		
//		System.out.println( filterHtmlTag( "하이루<span style=\"font-size: 8pt\" ", null) );
		
//		System.out.println("남는 길이가 적을 경우 테스트");
//		String escapeContent = "안녕하세요? \n\n 저는 임성묵이라고 합니다.\n\n\n\n\n\n\n\n\n\n                      ㅋㅋ";
//		//System.out.println(HtmlUtil.escapeHTML(escapeContent, 500));
//		
//		ParsedText text = HtmlUtil.parseRawText(escapeContent, ContentType.STORY);
//		
//		System.out.println("결과 : " + text.getTaggedTextPrev());
//		System.out.println("남는 길이가 길 경우 테스트");
//		escapeContent = "안녕하세요? \n\n 저는 임성묵이라고 합니다.\n\n\n\n\n\n\n\n\n\n ㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋㅋ";
//		text = HtmlUtil.parseRawText(escapeContent, ContentType.STORY);
//		System.out.println("결과 : " + text.getTaggedTextPrev());
//		
//		escapeContent = "엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11엔터없이고고11";
//		text = HtmlUtil.parseRawText(escapeContent, ContentType.STORY);
//		System.out.println("결과 : " + text.getTaggedTextPrev());
//		
	    String content = "안녕 #javaworld #java #java2 ㅎㅎ #java2worldgood  끝";
//	    System.out.println(HtmlUtil.setAutoLinkHashTag("/search", content));
	    
	    String mentionContent = "@[1222234201:임성묵]님이 헬로우 했습니다. asd@[1234567890:임성묵]asdfasdfasdf";
	    String[] retMentionStrings = HtmlUtil.getMentionTagList(mentionContent);
	    for( String mentionEach : retMentionStrings ){
		    System.out.println(mentionEach);
	    }
	    
	    
	    
	    String retString = HtmlUtil.autoLinkMention(new ParsedText(), mentionContent);
	    System.out.println(retString);
//	    Replacable replacable = new Replacable() {
//	        public String replace(String findToken) {
//	            try {
//	                return " <a href='http://localhost/search/plaza?q%5B%5D="
//	                        + URLEncoder.encode(findToken, "UTF-8") + "'>"
//	                        + findToken + "</a>";
//	            } catch (UnsupportedEncodingException e) {
//	                throw new IllegalArgumentException();
//	            }
//	        }
//
//	        public String regexp() {
//	            return "(?:^|\\s|[\\p{Punct}&&[^/]])(#[\\p{L}0-9-_]+)";
//	        }
//	    };
//
//	    System.out.println(HtmlUtil.replaceHashTagToUrl(content,
//	            replacable));
//		System.out.println(HtmlUtil.autoLinkHashTag("#임성묵 #안드로이드 ㅋㅋ #iphone"));
	}
	
	public static String autoLinkHashTag( ParsedText parsedText, String content ){
		
		if (content == null || content.length() < 1) {
			return content;
		}

		String pattern = "(?:^|\\s|[\\p{Punct}&&[^/]])(#[\\p{L}0-9-_]+)";
		// String pattern = "\\s+(#[\\p{L}0-9-_]+)|(^#[\\p{L}0-9-_]+)";

		StringBuffer sb = new StringBuffer();
		try {
			Pattern compiledPattern = Pattern.compile(pattern,
					Pattern.CASE_INSENSITIVE);
			Matcher matcher = compiledPattern.matcher(content);

			Set<String> tagTitles = new HashSet<String>();

			while (matcher.find()) {
				String hashTag = matcher.group(0);
				
	            matcher.appendReplacement(sb, "<a href='#' class='generated-hash'>" + hashTag + "</a>");
				tagTitles.add(hashTag.substring(hashTag.indexOf('#') + 1));
			}
			matcher.appendTail(sb);
			if (parsedText != null) {
				parsedText.setTagTitles(tagTitles);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
//	public static String setAutoLinkHashTag(String url, String content) {
//		if( content == null || content.length() < 1 ){
//			return null;
//		}
//	    String pattern = "(?:^|\\s|[\\p{Punct}&&[^/]])(#[\\p{L}0-9-_]+)";
//
//	    StringBuffer sb = new StringBuffer();
//	    try {
//	        Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
//	        Matcher matcher = compiledPattern.matcher(content);
//	        
//	        while (matcher.find()) {
//	            String hashTag = matcher.group(1).trim();
//	            matcher.appendReplacement(sb, createTargetUrl(url, hashTag));
//	        }
//	        matcher.appendTail(sb);
//	    } catch (Exception e) {
//
//	    }
//	    return sb.toString();
//	}

	private static String createTargetUrl(String url, String hashTag)
	        throws UnsupportedEncodingException {
	    return " <a href='" + url + "?q%5B%5D="
	            + URLEncoder.encode(hashTag, "UTF-8") + "'>" + hashTag + "</a>";
	}

	
	public static String replaceHashTagToUrl(String contents, Replacable replacable) {
		if( contents == null || contents.length() < 1 ){
			return null;
		}
	    StringBuffer sb = new StringBuffer();
	    try {
	        Pattern compiledPattern = Pattern.compile( replacable.regexp(), Pattern.CASE_INSENSITIVE);
	        Matcher matcher = compiledPattern.matcher(contents);
	        
	        while (matcher.find()) {
	            String replacedToken = replacable.replace(matcher.group(1).trim());
	            matcher.appendReplacement(sb, replacedToken);
	        }
	        matcher.appendTail(sb);
	    } catch (Exception e) {

	    }
	    return sb.toString();
	}
	
	public static String[] getMentionTagList(String content){
		if( content == null || content.length() < 1 ){
			return null;
		}
		List<String> list = new ArrayList<String>();
		String pattern = "@\\[([0-9]{10}):(.+?)]\\]?";	// @[1234:임성묵] 방식일 때
//		String pattern = "@\\[([0-9]*?)\\]"; 			// @[1234] 방식일 때	

		Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = compiledPattern.matcher(content);
		while(matcher.find()) {
		   list.add(matcher.group(0).trim());
		   list.add(matcher.group(1).trim());
		   list.add(matcher.group(2).trim());
		}
		return list.toArray(new String[list.size()]);
	}

	public static String autoLinkMention(ParsedText parsedText, String strSrc) {
		if( strSrc == null || strSrc.length() < 1 ){
			return strSrc;
		}
		String pattern = "@\\[([0-9]{1,}):(.+?)\\]+?";	// @[1234:임성묵] 방식일 때
		
	    StringBuffer sb = new StringBuffer();
	    try {
	        Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
	        Matcher matcher = compiledPattern.matcher(strSrc);
	        Set<Long> mentionReceivers = new HashSet<Long>();
	        
	        while (matcher.find()) {
	            String userIdString = matcher.group(1).trim();
	            String name = matcher.group(2).trim();
	            matcher.appendReplacement(sb, "<a href='#' data-uid='" + userIdString + "' class='generated-mention'>" + name + "</a>");
	            
	            mentionReceivers.add(Long.valueOf( userIdString ) );
	        }
	        matcher.appendTail(sb);
	        if( parsedText != null ){
	        	parsedText.setMentionReceivers(mentionReceivers);	
	        }
	    } catch (Exception e) {
	    	throw new RuntimeException("autoLinkMention 에서 실패");
	    }
	    
	    return sb.toString();
	}
	public static String stripMention(String strSrc) {
		if( strSrc == null || strSrc.length() < 1 ){
			return null;
		}
		String pattern = "@\\[[0-9]{1,}:(.+?)\\]+?";	// @[1234:임성묵] 방식일 때
		
	    StringBuffer sb = new StringBuffer();
	    try {
	        Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
	        Matcher matcher = compiledPattern.matcher(strSrc);
	        
	        while (matcher.find()) {
	            String name = matcher.group(1).trim();
	            matcher.appendReplacement(sb,  name );
	        }
	        matcher.appendTail(sb);
	    } catch (Exception e) {
	    	throw new RuntimeException("autoLinkMention 에서 실패");
	    }
	    
	    return sb.toString();
	}
	
	public static String stripBold(String strSrc) {
		if( strSrc == null || strSrc.length() < 1 ){
			return null;
		}
		

		
	    StringBuffer sb = new StringBuffer();
		String pattern = "\\[B\\](.*?)\\[/B\\]+?";
		
	    try {
	        Pattern compiledPattern = Pattern.compile(pattern, Pattern.DOTALL);
	        Matcher matcher = compiledPattern.matcher(strSrc);
	        
	        while (matcher.find()) {
	            String name = matcher.group(1).trim();
	            matcher.appendReplacement(sb,  name );
	        }
	        matcher.appendTail(sb);
	    } catch (Exception e) {
	    	throw new RuntimeException("stripBold 에서 실패");
	    }
	    
	    return sb.toString();
	}
	
	public static String[] getHashTagList(String content){
		if( content == null || content.length() < 1 ){
			return null;
		}
		List<String> list = new ArrayList<String>();
		String pattern = "(?:^|\\s|[\\p{Punct}&&[^/]])(#[\\p{L}0-9-_]+)";

		Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = compiledPattern.matcher(content);
		while(matcher.find()) {
		   list.add(matcher.group().trim());
		}
		return list.toArray(new String[list.size()]);		
	}
	
//	public static String setAutoLinkHashTag(String url, String content){
//
//		String strRet = content;
//		try{
//			String[] list = getHashTagList(content);
//			Arrays.sort(list);
//			if(list.length > 0){
//				for(int k = list.length -1; k >= 0; k--){
//					strRet = strRet.replaceAll(list[k], "<a href='" + url + "?q%5B%5D=" + URLEncoder.encode(list[k], "UTF-8") + "'>" + list[k] + "</a>"); 
//				}
//			}
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
//
//		return strRet;		
//	}	
	
	public static String[] getYoutubeLinkList(String content){
		if( content == null || content.length() < 1 ){
			return null;
		}
		List<String> list = new ArrayList<String>();
		String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";

		Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = compiledPattern.matcher(content);
		while(matcher.find()) {
		   list.add(matcher.group());
		}
		return list.toArray(new String[list.size()]);
	}
	
	public static String convertYoutubeEmbedHtmlTagFromYoutubeLink(String link, boolean oldStyle){
		return convertYoutubeEmbedHtmlTagFromYoutubeLink(link, oldStyle, 640, 380);
	}
	
	public static String getYoutubeVideoId(String link){
		if( link == null || link.length() < 1 ){
			return null;
		}
		int index = link.lastIndexOf("/");
		if(index < 0)
			throw new IllegalArgumentException("잘못된 Youtube URL형식입니다.");
		String code = link.substring(index + 1);
		
		int index2 = link.lastIndexOf("?v="); 
		if( index2 > 0){
			code = link.substring(index2 + 3);
		}
		return code;
	}
	
	/**
	 
	 	@param youtubeId 유튜브아이디
	 	@param type 이미지 type (0~3)
		Default Thumbnail Image, Full-Size (480x360)
		http://img.youtube.com/vi/DVbfTjeIeIs/0.jpg

		1st Thumbnail Image, Small (120x90)
		http://img.youtube.com/vi/DVbfTjeIeIs/1.jpg

		2nd | Default Thumbnail Image, Small (120x90)
		http://img.youtube.com/vi/DVbfTjeIeIs/2.jpg

		3rd Thumbnail Image, Small (120x90)
		http://img.youtube.com/vi/DVbfTjeIeIs/3.jpg
    */
	public static String getYoutubeThumbnailImage(String youtubeId, int type){
		StringBuffer sb = new StringBuffer();
		sb.append("http://img.youtube.com/vi/");
		sb.append(youtubeId);
		sb.append("/");
		sb.append(type);
		sb.append(".jpg");
		return sb.toString();
	}
	
	public static String convertYoutubeEmbedHtmlTagFromYoutubeLink(String link, boolean oldStyle, int width, int height){
		StringBuffer sb = new StringBuffer();
		if(!oldStyle){
			sb.append("<iframe width=\"");
			sb.append(width);
			sb.append("\" height=\"");
			sb.append(height);
			sb.append("\" src=\"");
			sb.append(link); 
			sb.append("\" frameborder=\"0\" allowfullscreen></iframe>\n");
		}else{
			String code = getYoutubeVideoId(link);
			sb.append("<object width=\"");
			sb.append(width);
			sb.append("\" height=\"");
			sb.append(height);
			sb.append("\">\n");
			sb.append("<param name=\"movie\" value=\"//www.youtube.com/v/");
			sb.append(code);
			sb.append("?version=3&amp;hl=ko_KR\"></param>\n");
			sb.append("<param name=\"allowFullScreen\" value=\"true\"></param>\n");
			sb.append("<param name=\"allowscriptaccess\" value=\"always\"></param>\n");
			sb.append("<embed src=\"//www.youtube.com/v/");
			sb.append(code);
			sb.append("?version=3&amp;hl=ko_KR\""); 
			sb.append("type=\"application/x-shockwave-flash\" width=\"");
			sb.append(width);
			sb.append("\" height=\"");
			sb.append(height);
			sb.append("\" ");
			sb.append("allowscriptaccess=\"always\" allowfullscreen=\"true\"></embed>\n");
			sb.append("</object>\n");
		}
		
		return sb.toString();
	}
	
	public static String filterHtmlTag(String content, String[] allowTags) {
		if( content == null || content.length() < 1 ){
			return content;
		}
		String pattern = "<(\\/?)(?!\\/####)([^<|>]+)?>";
		/*
		 *   - / 가 하나라도 있거나 $0
		 *   - 
		 */
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; allowTags != null && i < allowTags.length; i++)
		{
			buffer.append("|" + allowTags[i].trim() + "(?!\\w)");
		}
		pattern = pattern.replace("####",buffer.toString());
		 
		String msg = content.replaceAll(pattern,"&lt;$1$2&gt;");
		return msg;
//		String pattern = "<(\\/?)(?!\\/####)([^<|>]+)?>";
//		StringBuffer buffer = new StringBuffer();
//		 for (int i = 0; i < allowTags.length; i++)
//		 {
//			 buffer.append("|" + allowTags[i].trim() + "(?!\\w)");
//		 }
//		 
//		pattern = pattern.replace("####",buffer.toString());
//		 
//		System.out.println("원본 : " +  content + "변경된 패턴 : " + pattern);
//		 
//		
//		String msg = content.replaceAll(pattern,"&lt;$1$2&gt;");
//		return msg;		
	}
	
	public static String removeHtmlTag(String content, String[] allowTags) {
		
		if( content == null )
			return "";
		
		String pattern = "<(\\/?)(?!\\/####)([^<|>]+)?>";
		/*
		 *   - / 가 하나라도 있거나 $0
		 *   - 
		 */
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; allowTags != null && i < allowTags.length; i++)
		{
			buffer.append("|" + allowTags[i].trim() + "(?!\\w)");
		}
		 
		pattern = pattern.replace("####",buffer.toString());
		 
		String msg = content.replaceAll(pattern,"");
		return msg;
//		String pattern = "<(\\/?)(?!\\/####)([^<|>]+)?>";
//		StringBuffer buffer = new StringBuffer();
//		 for (int i = 0; i < allowTags.length; i++)
//		 {
//			 buffer.append("|" + allowTags[i].trim() + "(?!\\w)");
//		 }
//		 
//		pattern = pattern.replace("####",buffer.toString());
//		 
//		System.out.println("원본 : " +  content + "변경된 패턴 : " + pattern);
//		 
//		
//		String msg = content.replaceAll(pattern,"&lt;$1$2&gt;");
//		return msg;		
	}
	
	
	public static StringEscapeResult escapeHTML(String s, int lengthStandard ) {
		if (null == s) {
			return null;
		}
		int count = s.length();

		char[] value = new char[count];
		s.getChars(0, count, value, 0);

		char result[] = new char[count];
		char nbsp[] = { '&', 'n', 'b', 's', 'p', ';' };
		char br[] = { '<', 'b', 'r', ' ', '/', '>' };

		
		/**
		 * start -> 0
		 * <     -> 1
		 * a	-> 2, 딴거 -> 0
		 * < 	-> 3
		 * /    -> 4, 딴거 -> 2
		 * a    -> 0, 딴거 -> 2
		 */
		int hrefAutomataState = 0;
		
		//TODO: 공백의 경우에 autolink 와 맞지 않는 문제가 있어 좀더 연구 후 적용
		boolean check = true;		// <> 가 있으면 체크하지 않는다.
		int len, newCount, retCount = 0;
		int prevCommentStandard = 0, prevStoryStandard = 0, prevCommentOriginalLength = 0, prevStoryOriginalLength = 0;;

		for (int i = 0, j = 0; j < count; j++) {
			char c = value[j];
			/*
			if (c == '\'') {
			} else if (c == '\"') {
			} 
			*/
			
			if (c == '<') {
				check = false;
				
				switch( hrefAutomataState ){
				case HREF_STATE_NONE:
					hrefAutomataState = HREF_STATE_START;
					break;
					
				case HREF_STATE_START:
					hrefAutomataState = HREF_STATE_START;
					break;
					
				case HREF_STATE_MEET_FIRST_A:
					hrefAutomataState = HREF_STATE_SECOND_START;
					break;
					
				case HREF_STATE_SECOND_START:
					hrefAutomataState = HREF_STATE_SECOND_START;
					break;
					
				case HREF_STATE_MEET_SLASH:
					hrefAutomataState = HREF_STATE_SECOND_START;
					break;
				}
				
				result[i++] = value[j];
			} else if (c == '>') {
				check = true;
				
				
				switch( hrefAutomataState ){
				case HREF_STATE_NONE:
					hrefAutomataState = HREF_STATE_NONE;
					break;
					
				case HREF_STATE_START:
					hrefAutomataState = HREF_STATE_NONE;
					break;
					
				case HREF_STATE_MEET_FIRST_A:
					hrefAutomataState = HREF_STATE_MEET_FIRST_A;
					break;
					
				case HREF_STATE_SECOND_START:
					hrefAutomataState = HREF_STATE_MEET_FIRST_A;
					break;
					
				case HREF_STATE_MEET_SLASH:
					hrefAutomataState = HREF_STATE_MEET_FIRST_A;
					break;
				}
				
				
				result[i++] = value[j];
			} 
			
			else if (c == ' ') {
				
				
				switch( hrefAutomataState ){
				case HREF_STATE_NONE:
					hrefAutomataState = HREF_STATE_NONE;
					break;
					
				case HREF_STATE_START:
					hrefAutomataState = HREF_STATE_NONE;
					break;
					
				case HREF_STATE_MEET_FIRST_A:
					hrefAutomataState = HREF_STATE_MEET_FIRST_A;
					break;
					
				case HREF_STATE_SECOND_START:
					hrefAutomataState = HREF_STATE_MEET_FIRST_A;
					break;
					
				case HREF_STATE_MEET_SLASH:
					hrefAutomataState = HREF_STATE_MEET_FIRST_A;
					break;
				}
				
				if( check == false ){
					result[i++] = value[j];
					continue;
				}
				len = nbsp.length;
				newCount = result.length + len - 1;
				char newValue[] = new char[newCount];
				System.arraycopy(result, 0, newValue, 0, result.length);
				result = newValue;
				System.arraycopy(nbsp, 0, result, i, len);
				i += len;
			}
			else if (c == '\n') {
				
				switch( hrefAutomataState ){
				case HREF_STATE_NONE:
					hrefAutomataState = HREF_STATE_NONE;
					break;
					
				case HREF_STATE_START:
					hrefAutomataState = HREF_STATE_NONE;
					break;
					
				case HREF_STATE_MEET_FIRST_A:
					hrefAutomataState = HREF_STATE_MEET_FIRST_A;
					break;
					
				case HREF_STATE_SECOND_START:
					hrefAutomataState = HREF_STATE_MEET_FIRST_A;
					break;
					
				case HREF_STATE_MEET_SLASH:
					hrefAutomataState = HREF_STATE_MEET_FIRST_A;
					break;
				}
				
				if( check == false ){
					result[i++] = value[j];
					continue;
				}
				len = br.length;
				newCount = result.length + len - 1;
				char newValue[] = new char[newCount];
				System.arraycopy(result, 0, newValue, 0, result.length);
				result = newValue;
				System.arraycopy(br, 0, result, i, len);
				i += len;
				retCount++;
				if( retCount == 5 && prevCommentStandard == 0 ){
					prevCommentStandard =  i ;
					prevCommentOriginalLength = j;
				}else if( retCount == 10 && prevStoryStandard == 0 ){
					prevStoryStandard =  i ;
					prevStoryOriginalLength = j;
				}
			}
			else {
				
				if (c == 'a') {
					switch( hrefAutomataState ){
					case HREF_STATE_NONE:
						hrefAutomataState = HREF_STATE_NONE;
						break;
						
					case HREF_STATE_START:
						hrefAutomataState = HREF_STATE_MEET_FIRST_A;
						break;
						
					case HREF_STATE_MEET_FIRST_A:
						hrefAutomataState = HREF_STATE_MEET_FIRST_A;
						break;
						
					case HREF_STATE_SECOND_START:
						hrefAutomataState = HREF_STATE_MEET_FIRST_A;
						break;
						
					case HREF_STATE_MEET_SLASH:
						hrefAutomataState = HREF_STATE_NONE;
						break;
					}
				}else if( c == '/'){
					switch( hrefAutomataState ){
					case HREF_STATE_NONE:
						hrefAutomataState = HREF_STATE_NONE;
						break;
						
					case HREF_STATE_START:
						hrefAutomataState = HREF_STATE_NONE;
						break;
						
					case HREF_STATE_MEET_FIRST_A:
						hrefAutomataState = HREF_STATE_MEET_FIRST_A;
						break;
						
					case HREF_STATE_SECOND_START:
						hrefAutomataState = HREF_STATE_MEET_SLASH;
						break;
						
					case HREF_STATE_MEET_SLASH:
						hrefAutomataState = HREF_STATE_MEET_FIRST_A;
						break;
					}
				}
				
				
				result[i++] = value[j];
				// 리턴을 5개, 10개 이상 만나지 못했는데 길이가 500자 정도를 넘어섰을 경우엔 잘라준다. 
				if( j > lengthStandard ){
					if( hrefAutomataState == HREF_STATE_NONE && prevCommentStandard == 0 && check == true ){
						prevCommentStandard = i ;
						prevCommentOriginalLength = j;
					}
					
					if( hrefAutomataState == HREF_STATE_NONE && prevStoryStandard == 0 && check == true ){
						prevStoryStandard = i ;
						prevStoryOriginalLength = j;
					}
				}
			}
		}
		
		return new StringEscapeResult( retCount, new String(result), prevCommentStandard, prevStoryStandard, prevCommentOriginalLength, prevStoryOriginalLength);
	}
//
//	 public final static String replaceTagMark(String s){
//        if (s == null)return null;
//        StringBuffer buf = new StringBuffer();
//        char[] c = s.toCharArray();
//        int len = c.length;
//        for (int i = 0; i < len; i++) {
//            if (c[i] == '&') buf.append("&amp;");
//            else if (c[i] == '<') buf.append("&lt;");
//            else if (c[i] == '>') buf.append("&gt;");
//            else if (c[i] == '"') buf.append("&quot;");
//            else if (c[i] == '\'') buf.append("&#039;");
//            else if (c[i] == ' ') buf.append("&nbsp;");
//            else buf.append(c[i]);
//        }
//        return buf.toString();
//    }
//	
	
	
	/**
	 * 줄바꿈문자(n)을 &lt;br&gt;n으로 바꿈
	 * 
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static String nl2br(String src) throws Exception {
		String strRet = "";
		try {
			// 정규식을 사용하는것 보다 직접 하나하나 잘라주는것이 더 빠르네... ㅡ.ㅡ;
			strRet = HtmlUtil.replace(src, "\n", "<br />");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}

		return strRet;
	}

	/**
	 * 일반적인 URL(http|https|ftp|telnet|news|mms) 자동링크 한글주소 포함.
	 * 
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static String autoLinkURL(String src) {
		if( src == null || src.length() < 1 ){
			return src;
		}
		String strRet = "";

		final String httpLink = "(((http|https|ftp|telnet|news|mms):\\/\\/)(([\\uAC00-\\uD7A3a-z0-9:_\\-]+\\.[\\uAC00-\\uD7A3a-z0-9:;&#=_~%\\[\\]\\?\\/\\.\\,\\+\\-]+)([\\.]*[\\uAC00-\\uD7A3\\/a-z0-9\\[\\]]|=[\\uAC00-\\uD7A3a-z0-9:_\\-]+)))";
		try {
			// 이메일을 자동링크시키는건 java.lang.String.replaceAll()을 써도 잘 되는데 URL링크는 왜
			// 안될까?
			// 서버까지만 나오고 그 뒤 URI내용은 안나오네... ㅡ.ㅡ;
			Pattern pattern_ = Pattern.compile(httpLink,
					Pattern.CASE_INSENSITIVE);
			Matcher match = pattern_.matcher(src);

			strRet = match.replaceAll("<a href='$1' target='__blank'>$1</a>");
			// strRet = src.replaceAll(httpLink, "<a href='$0'>$0</a>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}

		return strRet;
	}

	/**
	 * 메일주소 자동링크
	 * 
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static String autoLinkEmail(String src) throws Exception {
		if( src == null || src.length() < 1 ){
			return null;
		}
		String strRet = "";
		final String mailLink = "([uAC00-uD7A3A-Za-z0-9_-]{1,})@([uAC00-uD7A3A-Za-z0-9_-]+.[uAC00-uD7A3A-Za-z0-9_-]{1,})";
		try {
			// 이메일을 자동링크시키는건 java.lang.String.replaceAll()을 써도 잘 되는데 URL링크는 왜
			// 안될까?
			// Pattern pattern_ = Pattern.compile(mailLink,
			// Pattern.CASE_INSENSITIVE);
			// Matcher match = pattern_.matcher(src);
			//
			// strRet = match.replaceAll("<a href='$0'>$0</a>");

			strRet = src.replaceAll(mailLink, "<a href='mailto:$0'>$0</a>");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}

		return strRet;
	}

	/**
	 * 입력 String중에서 원하는 부분을 바꿔준다.
	 * 
	 * @return String
	 * 
	 * @param String
	 * @param String
	 * @param String
	 * 
	 */
	public static String replace(String strSrc, String strOrg, String strReplace)
			throws Exception {

		int iNewIdx, iOldIdx = 0;
		StringBuffer sbNewStr = null;
		String strReturn = null;
		
		
		try {
			if (strSrc != null && strOrg != null && strReplace != null) {
				iNewIdx = strSrc.indexOf(strOrg);

				sbNewStr = new StringBuffer();

				while (iNewIdx > -1) {
					sbNewStr.append(strSrc.substring(iOldIdx, iNewIdx)
							+ strReplace);

					iOldIdx = iNewIdx + strOrg.length();
					iNewIdx = strSrc.indexOf(strOrg, iOldIdx);
				}

				sbNewStr.append(strSrc.substring(iOldIdx, strSrc.length()));

				strReturn = sbNewStr.toString();

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}

		return strReturn;
	}

	public static String highlighting(String htmlString, String targetString,
			String highlightOpenTag, String highlightCloseTag) {

		Pattern htmlPattern = Pattern
				.compile("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>");
		Pattern charPattern = Pattern.compile("&[^;]+;");
		Pattern textPattern = Pattern.compile(targetString,
				Pattern.CASE_INSENSITIVE);
		Matcher htmlMatcher = htmlPattern.matcher(htmlString);
		Matcher charMatcher = charPattern.matcher(htmlString);
		Matcher textMatcher = textPattern.matcher(htmlString);
		int contentsLength = htmlString.length();
		StringBuffer sb = new StringBuffer();
		int htmlIndex = 0, charIndex = 0, textIndex = 0;
		for (int startIndex = 0, endIndex = 0; startIndex < contentsLength; startIndex = endIndex) {
			if (htmlIndex != -1 && htmlIndex <= startIndex) {
				htmlIndex = (htmlMatcher.find(startIndex)) ? htmlMatcher
						.start() : -1;
			}
			if (charIndex != -1 && charIndex <= startIndex) {
				charIndex = (charMatcher.find(startIndex)) ? charMatcher
						.start() : -1;
			}
			if (textIndex != -1 && textIndex <= startIndex) {
				textIndex = (textMatcher.find(startIndex)) ? textMatcher
						.start() : -1;
			}
			if (textIndex == -1) {
				sb.append(htmlString.substring(endIndex));
				break;
			} else if (htmlIndex == -1 && charIndex == -1) {
				sb.append(htmlString.substring(startIndex, textIndex));
				sb.append(highlightOpenTag).append(textMatcher.group())
						.append(highlightCloseTag);
				endIndex = textMatcher.end();
			} else if (htmlIndex == -1) {
				if (charIndex < textIndex) {
					sb.append(htmlString.substring(startIndex, charIndex));
					endIndex = charMatcher.end();
					sb.append(htmlString.substring(charIndex, endIndex));
				} else {
					sb.append(htmlString.substring(startIndex, textIndex));
					sb.append(highlightOpenTag).append(textMatcher.group())
							.append(highlightCloseTag);
					endIndex = textMatcher.end();
				}
			} else if (charIndex == -1) {
				if (htmlIndex < textIndex) {
					sb.append(htmlString.substring(startIndex, htmlIndex));
					endIndex = htmlMatcher.end();
					sb.append(htmlString.substring(htmlIndex, endIndex));
				} else {
					sb.append(htmlString.substring(startIndex, textIndex));
					sb.append(highlightOpenTag).append(textMatcher.group())
							.append(highlightCloseTag);
					endIndex = textMatcher.end();
				}
			} else if (htmlIndex < charIndex) {
				if (htmlIndex < textIndex) {
					sb.append(htmlString.substring(startIndex, htmlIndex));
					endIndex = htmlMatcher.end();
					sb.append(htmlString.substring(htmlIndex, endIndex));
				} else {
					sb.append(htmlString.substring(startIndex, textIndex));
					sb.append(highlightOpenTag).append(textMatcher.group())
							.append(highlightCloseTag);
					endIndex = textMatcher.end();
				}
			} else // htmlIndex > charIndex
			{
				if (charIndex < textIndex) {
					sb.append(htmlString.substring(startIndex, charIndex));
					endIndex = charMatcher.end();
					sb.append(htmlString.substring(charIndex, endIndex));
				} else {
					sb.append(htmlString.substring(startIndex, textIndex));
					sb.append(highlightOpenTag).append(textMatcher.group())
							.append(highlightCloseTag);
					endIndex = textMatcher.end();
				}
			}
		}
		return sb.toString();
	}
	public static String stripHashTag(String content) {
		if (content == null || content.length() < 1) {
			return content;
		}

		String pattern = "((?:^|\\s|[\\p{Punct}&&[^/]])(#[\\p{L}0-9-_]+))";
		content = content.replaceAll(pattern, "");

		return content;

	}


	
}
