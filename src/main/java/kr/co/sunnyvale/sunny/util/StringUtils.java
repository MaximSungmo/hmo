package kr.co.sunnyvale.sunny.util;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.WordUtils;


public class StringUtils extends org.apache.commons.lang.StringUtils {
	

	public static String truncateHTML(String text, int length, String suffix) {
	    // if the plain text is shorter than the maximum length, return the whole text
	    if (text.replaceAll("<.*?>", "").length() <= length) {
	        return text;
	    }
	    String result = "";
	    boolean trimmed = false;
	    if (suffix == null) {
	        suffix = "...";
	    }

	    /*
	     * This pattern creates tokens, where each line starts with the tag.
	     * For example, "One, <b>Two</b>, Three" produces the following:
	     *     One,
	     *     <b>Two
	     *     </b>, Three
	     */
	    Pattern tagPattern = Pattern.compile("(<.+?>)?([^<>]*)");

	    /*
	     * Checks for an empty tag, for example img, br, etc.
	     */
	    Pattern emptyTagPattern = Pattern.compile("^<\\s*(img|br|input|hr|area|base|basefont|col|frame|isindex|link|meta|param).*>$");

	    /*
	     * Modified the pattern to also include H1-H6 tags
	     * Checks for closing tags, allowing leading and ending space inside the brackets
	     */
	    Pattern closingTagPattern = Pattern.compile("^<\\s*/\\s*([a-zA-Z]+[1-6]?)\\s*>$");

	    /*
	     * Modified the pattern to also include H1-H6 tags
	     * Checks for opening tags, allowing leading and ending space inside the brackets
	     */
	    Pattern openingTagPattern = Pattern.compile("^<\\s*([a-zA-Z]+[1-6]?).*?>$");

	    /*
	     * Find &nbsp; &gt; ...
	     */
	    Pattern entityPattern = Pattern.compile("(&[0-9a-z]{2,8};|&#[0-9]{1,7};|[0-9a-f]{1,6};)");

	    // splits all html-tags to scanable lines
	    Matcher tagMatcher =  tagPattern.matcher(text);
	    int numTags = tagMatcher.groupCount();

	    int totalLength = suffix.length();
	    List<String> openTags = new ArrayList<String>();

	    boolean proposingChop = false;
	    while (tagMatcher.find()) {
	        String tagText = tagMatcher.group(1);
	        String plainText = tagMatcher.group(2);

	        if (proposingChop &&
	                tagText != null && tagText.length() != 0 &&
	                plainText != null && plainText.length() != 0) {
	            trimmed = true;
	            break;
	        }

	        // if there is any html-tag in this line, handle it and add it (uncounted) to the output
	        if (tagText != null && tagText.length() > 0) {
	            boolean foundMatch = false;

	            // if it's an "empty element" with or without xhtml-conform closing slash
	            Matcher matcher = emptyTagPattern.matcher(tagText);
	            if (matcher.find()) {
	                foundMatch = true;
	                // do nothing
	            }

	            // closing tag?
	            if (!foundMatch) {
	                matcher = closingTagPattern.matcher(tagText);
	                if (matcher.find()) {
	                    foundMatch = true;
	                    // delete tag from openTags list
	                    String tagName = matcher.group(1);
	                    openTags.remove(tagName.toLowerCase());
	                }
	            }

	            // opening tag?
	            if (!foundMatch) {
	                matcher = openingTagPattern.matcher(tagText);
	                if (matcher.find()) {
	                    // add tag to the beginning of openTags list
	                    String tagName = matcher.group(1);
	                    openTags.add(0, tagName.toLowerCase());
	                }
	            }

	            // add html-tag to result
	            result += tagText;
	        }

	        // calculate the length of the plain text part of the line; handle entities (e.g. &nbsp;) as one character
	        int contentLength = plainText.replaceAll("&[0-9a-z]{2,8};|&#[0-9]{1,7};|[0-9a-f]{1,6};", " ").length();
	        if (totalLength + contentLength > length) {
	            // the number of characters which are left
	            int numCharsRemaining = length - totalLength;
	            int entitiesLength = 0;
	            Matcher entityMatcher = entityPattern.matcher(plainText);
	            while (entityMatcher.find()) {
	                String entity = entityMatcher.group(1);
	                if (numCharsRemaining > 0) {
	                    numCharsRemaining--;
	                    entitiesLength += entity.length();
	                } else {
	                    // no more characters left
	                    break;
	                }
	            }

	            // keep us from chopping words in half
	            int proposedChopPosition = numCharsRemaining + entitiesLength;
	            int endOfWordPosition = plainText.indexOf(" ", proposedChopPosition-1);
	            if (endOfWordPosition == -1) {
	                endOfWordPosition = plainText.length();
	            }
	            int endOfWordOffset = endOfWordPosition - proposedChopPosition;
	            if (endOfWordOffset > 6) { // chop the word if it's extra long
	                endOfWordOffset = 0;
	            }

	            proposedChopPosition = numCharsRemaining + entitiesLength + endOfWordOffset;
	            if (plainText.length() >= proposedChopPosition) {
	                result += plainText.substring(0, proposedChopPosition);
	                proposingChop = true;
	                if (proposedChopPosition < plainText.length()) {
	                    trimmed = true;
	                    break; // maximum length is reached, so get off the loop
	                }
	            } else {
	                result += plainText;
	            }
	        } else {
	            result += plainText;
	            totalLength += contentLength;
	        }
	        // if the maximum length is reached, get off the loop
	        if(totalLength >= length) {
	            trimmed = true;
	            break;
	        }
	    }

	    for (String openTag : openTags) {
	        result += "</" + openTag + ">";
	    }
	    if (trimmed) {
	        result += suffix;
	    }
	    return result;
	}

	public static String getRedisUserIds(Collection<String> collection) {
		StringBuffer receiveUserStringBuffer = new StringBuffer();
		if(collection.size() > 0){
			for( String userId : collection ){
				receiveUserStringBuffer.append(userId);
				receiveUserStringBuffer.append(",");
			}
			return receiveUserStringBuffer.substring(0, receiveUserStringBuffer.length() - 1 );
		}else{
			return "";
		}
	}
	public static String getRegdate(int year, int month, int day) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(year);
		
		if(month < 10){
			sb.append("0");
			sb.append(month);
		}else{
			sb.append(month);
		}
		if(day < 10){
			sb.append("0");
			sb.append(day);
		}else{
			sb.append(day);
		}
		return sb.toString();
	}
	
	public static String getRegtime(int hour, int minute, int second){
		StringBuffer sb = new StringBuffer();

		if(hour < 10){
			sb.append("0");
			sb.append(hour);
		}else{
			sb.append(hour);
		}
		if(minute < 10){
			sb.append("0");
			sb.append(minute);
		}else{
			sb.append(minute);
		}
		if(second < 10){
			sb.append("0");
			sb.append(second);
		}else{
			sb.append(second);
		}

		return sb.toString();
	}	
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean hasLength(String str) {
		return hasLength((CharSequence) str);
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean hasText(String str) {
		return hasText((CharSequence) str);
	}
	
	/**
	 * 
	 * @param length
	 * @return
	 */
	public static String genRandomString( int length )
	{
		final char[] possible = {'0','1','2','3','4','5','6','7','8','9','0', 'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
		
		StringBuffer rString = new StringBuffer( "" );
		int i = 0;
		int lenPossible = possible.length;
		
		Random rn = new Random();
		
		while( i < length ) {
            int pos = rn.nextInt() % lenPossible;
            if ( pos < 0 ) { pos = -pos; }
            if( rString.toString().indexOf( possible[ pos ] ) == -1 ) { 
            	rString.append( possible[ pos ] );
				i++;
    		}
    	}
		
		return rString.toString();
	}
	
	public static String getBaseDomain( String host ) {
//	    int startIndex = 0;
		if( host.startsWith("192.") ){
			return host;
		}
	    int nextIndex = host.indexOf( '.' );
	    int lastIndex = host.lastIndexOf( '.' );
	    if( nextIndex != lastIndex ){
	    	return host.substring(nextIndex + 1);
	    }
//	    while (nextIndex < lastIndex) {
//	        startIndex = nextIndex + 1;
//	        nextIndex = host.indexOf( '.', startIndex );
//	    }
//	    if (nextIndex > 0) {
//	        return host.substring(nextIndex);
//	    } else {
	        return host;
//	    }
	}
	
	public static String elapsed( String org, int upper, String end){
		if( org.length() < upper )
			return org;
		
		String tmp = org.substring(0, upper - end.length());
		return tmp + end;
	}
	
	/** WordUtils에 있는걸 가죠왔다.
	 * ex) WordUtils.abbreviate("123456789abcdefg", 0,맥스값,"..."같이 마지막에 들어갈 문자.) */
	public static String abbreviate(String org,int upper,String end){
		return WordUtils.abbreviate(org, 0,upper,"...");
	}
	
	/** org로 들어온 문자에 정해진 길이마다 separator를 추가한다.
	 * 보통 특정 길이마다 \n등을 넣을때 사용  */
	public static String splitByLength(String org,int length,String separator){
		int size = org.length();
		int root = size / length;
		if(root==0) return org;
		int in = 0;
		StringBuilder buff = new StringBuilder(); 
		for(int i=0;i<root;i++){
			buff.append(org.substring(in, in+length));
			buff.append(separator);
			in+= length;
		}
		buff.append(org.substring(in, size));
		return buff.toString();
	}
	
	/** 각각 길이별로 문자열을 잘라낸다. Text가 일정길이로 나뉘어진 형식일때 유효하다.
	 * org.length()+1 == nowLength;
	 * 마지막열은 길이값 무시하도록 추가. (trim()때문에..) */
	public static String[] splitEachLength(String org,int[] eachLength,int offset){
		String[] result = new String[eachLength.length];
		int nowLength = 0;
		for(int i=0;i<eachLength.length;i++){
			int length = eachLength[i];
			int subLength = nowLength + length;
			try {
				if(org.length() < subLength) result[i] = org.substring(nowLength, eachLength.length);
				else result[i] = org.substring(nowLength, subLength);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			nowLength += length + offset;
		}
		return result;
	}
	/** 각각 길이별로 문자열을 잘라낸다. Text가 일정길이로 나뉘어진 형식일때 유효하다.
	 * org.length()+1 == nowLength;
	 * 마지막열은 길이값 무시하도록 추가. (trim()때문에..) */
	public static String[] splitEachLength(String org,List<Integer> eachLength,int offset){
		String[] result = new String[eachLength.size()];
		int nowLength = 0;
		int size = eachLength.size();
		for(int i=0;i<size;i++){
			int length = eachLength.get(i);
			int subLength = nowLength + length;
			if(org.length() < subLength) result[i] = org.substring(nowLength, org.length());
			else result[i] = org.substring(nowLength, subLength);
			nowLength += length + offset;
		}
		//if(org.length() != (nowLength-1)) throw new RuntimeException(
				//"["+org.length()+"] / ["+(nowLength-1)+"] : Length is not match");
		return result;
	}
	
	public static void removeNullAndTrim(String[] strings){
		for(int i=0;i<strings.length;i++){
			String each = strings[i];
			if(each==null) strings[i] = "";
			else strings[i] = each.trim();
		}
	}

    /** size만큼 str과 seperator를 반복시킨다. */
    public static String iterateStr(String str,String seperator,int size) {
        StringBuilder b = new StringBuilder();
        for(int i=0;i<size;i++){
            if(i!=0) b.append(seperator);
            b.append(str);
        }
        return b.toString();
    }
    
    /**
     * strs들중 일부라도 매치가 되면 true를 리턴한다.
     */
    public static boolean isMatch(String body, String... strs) {
        for (String str : strs)
            if (StringUtils.contains(body, str)) return true;
        return false;
    }
    
    /**
     * prefix들중 일부라도 매치가 되면 true를 리턴한다.
     */
    public static boolean isStartsWithAny(String body, String... prefix) {
    	if(body==null) return false;
    	for (String each : prefix)
    		if (StringUtils.startsWith(body, each)) return true;
    	return false;
    }
    
    /**
     * strs들중 일부라도 매치가 되면 true를 리턴한다.
     */
    public static boolean isMatchIgnoreCase(String body, String... strs) {
        body = body.toUpperCase();
        for (String str : strs)
            if (StringUtils.contains(body, str.toUpperCase())) return true;
        return false;
    }

    /**
     * strs들중 정확히 매치가 되면 true를 리턴한다.
     */
    public static boolean isEquals(String body, String... strs) {
        if (body == null) return false;
        for (String str : strs)
            if (body.equals(str)) return true;
        return false;
    }

    /**
     * strs들중 정확히 매치가 되면 true를 리턴한다. 대소문자를 구분하지 않는다.
     */
    public static boolean isEqualsIgnoreCase(String body, String... strs) {
        if (body == null) return false;
        for (String str : strs)
            if (body.equalsIgnoreCase(str)) return true;
        return false;
    }

    /**
     * 첫번째 패턴까지의 문자열을 리턴한다. ex) getFirst("12345\qqq\asd","\") => 12345
     */
    public static String getFirst(String str, String pattern) {
    	int index = str.indexOf(pattern);
    	return index == -1 ? str : str.substring(0,index);
    }
    
    public static String[] getFirstOf(String str, String pattern) {
    	String[] temp = new String[2];
        int index = str.indexOf(pattern);
        if (index == -1){
        	temp[0] = str; 
        	temp[1] = str; 
        	return temp;
        }
        temp[0] = str.substring(0, index);
        temp[1] = str.substring(index + 1);
        return temp;
    }
    
    /**
     * 첫번째 패턴 이후로의 문자열을 리턴한다. 
     * ex) getFirstAfter("12345\qqq\asd","\") => qqq\asd
     * 매칭이 안되면 원본을 리턴한다.
     */
    public static String getFirstAfter(String str, String pattern) {
    	return str.substring(str.indexOf(pattern)+1,str.length() );
    }

    /**
     * 마지막 패턴 이후로의 문자열을 리턴한다. ex) getFirst("12345\qqq\asd","\") => asd
     * 매치 실패시 그대로 리턴
     */
    public static String getLast(String str, String pattern) {
    	int index = str.lastIndexOf(pattern);
    	if(index==-1) return str;
        return str.substring(index + 1);
    }

    /**
     * 확장자를 리턴한다. ex) getExtention("12345.qqq.asd") => "asd" ex)
     * getExtention("123asd") => ""
     */
    public static String getExtention(String str) {
        int index = str.lastIndexOf(".");
        if (index == -1) return "";
        return str.substring(index + 1);
    }
    
    /** 확장자를 리턴한다. 없으면 기본 문자를  그대로 리턴한다. */
    public static String getExtention2(String str) {
        int index = str.lastIndexOf(".");
        if (index == -1) return str;
        return str.substring(index + 1);
    }    
    
    /** .이 없으면 null을 리턴한다. */
    public static String[] getExtentions(String str) {
        return getLastOf(str,".");
    }
    
    /** 
     * 마자막 구분자를 기준으로 문자를 2개로 나눈다.
     * 파일의 확장자나 폴더/파일이름을 구분할때 사용된다.
     *  */
    public static String[] getLastOf(String str,String seperator) {
        String[] temp = new String[2];
        int index = str.lastIndexOf(seperator);
        if (index == -1) return null;
        temp[0] = str.substring(0, index);
        temp[1] = str.substring(index + 1);
        return temp;
    }

    /**
     * Url은 '/'를 포함하는 root부터 시작한다. ex) /D:/qwe.qwe.go => 'D:/qwe.qwe' and 'go'
     */
    public static String[] getUrlAndExtention(String url) {
        String[] str = new String[2];
        int index = url.lastIndexOf(".");
        if (index < 0) throw new RuntimeException(MessageFormat.format("[{0}] 확장자가 존재하지 않는 경로를 입력하셨습니다.", url));
        str[0] = url.substring(1, index);
        str[1] = url.substring(index + 1);
        return str;
    }

    /**
     * 다국어(한글등)이면 true를 리턴한다.
     */
    public static boolean isHan(char c) {
        if (Character.getType(c) == Character.OTHER_LETTER) { return true; }
        return false;
    }

    /**
     * 다국어(한글등)이면 true를 리턴한다.
     */
    public static boolean isHanAny(String str) {
        for (char c : str.toCharArray())
            if (Character.getType(c) == Character.OTHER_LETTER) { return true; }
        return false;
    }

    private static final String[] beanMethods = new String[] { "get", "set", "is" };

    /**
     * methodName(setter/geter/is)에서 fildName을 추출한다. 해당 조건이 아니면 null을 리턴한다.
     */
    public static String getFieldName(String name) {
        for (String type : beanMethods) {
            if (name.startsWith(type)) return escapeAndUncapitalize(name, type);
        }
        return null;
    }

    private static final String[] getterMethods = new String[] { "get", "is" };

    /**
     * methodName(geter/is)에서 fildName을 추출한다. 해당 조건이 아니면 null을 리턴한다.
     */
    public static String getterName(String name) {
        for (String type : getterMethods) {
            if (name.startsWith(type)) return escapeAndUncapitalize(name, type);
        }
        return null;
    }
    
    private static final String[] setterMethods = new String[] { "set"};
    
    /**
     * methodName(setter)에서 fildName을 추출한다. 해당 조건이 아니면 null을 리턴한다.
     */
    public static String setterName(String name) {
        for (String type : setterMethods) {
            if (name.startsWith(type)) return escapeAndUncapitalize(name, type);
        }
        return null;
    }

    /**
     * 사업자 등록번호인지 체크한다.
     */
    public static boolean isBusinessId(String str) {
        String[] strs = str.split(EMPTY);
        if (strs.length != 11) return false;
        int[] ints = new int[10];
        for (int i = 0; i < 10; i++)
            ints[i] = Integer.valueOf(strs[i + 1]);
        int sum = 0;
        int[] indexs = new int[] { 1, 3, 7, 1, 3, 7, 1, 3 };
        for (int i = 0; i < 8; i++) {
            sum += ints[i] * indexs[i];
        }
        int num = ints[8] * 5;
        sum += (num / 10) + (num % 10);
        sum = 10 - (sum % 10);
        return sum == ints[9] ? true : false;
    }


    /**
     * 주민등록번호인지 체크한다. 1. 주민등록번호의 앞 6자리의 수에 처음부터 차례대로 2,3,4,5,6,7 을 곱한다. 그 다음, 뒤
     * 7자리의 수에 마지막 자리만 제외하고 차례대로 8,9,2,3,4,5 를 곱한다. 2. 이렇게 곱한 각 자리의 수들을 모두 더한다.
     * 3. 모두 더한 수를 11로 나눈 나머지를 구한다. 4. 이 나머지를 11에서 뺀다. 5. 이렇게 해서 나온 최종 값을
     * 주민등록번호의 마지막 자리 수와 비교해서 같으면 유효한 번호이고 다르면 잘못된 값이다.
     */
    public static boolean isSid(String input) {
        input = getNumericStr(input);

        if (input.length() != 13) throw new RuntimeException("주민등록번호 자리수 13자리를 확인하기 바랍니다.");

        // 입력받은 주민번호 앞자리 유효성 검증============================
        String leftSid = input.substring(0, 6);
        String rightSid = input.substring(6, 13);

        int yy = Integer.parseInt(leftSid.substring(0, 2));
        int mm = Integer.parseInt(leftSid.substring(2, 4));
        int dd = Integer.parseInt(leftSid.substring(4, 6));

        if (yy < 1 || yy > 99 || mm > 12 || mm < 1 || dd < 1 || dd > 31) return false;

        int digit1 = Integer.parseInt(leftSid.substring(0, 1)) * 2;
        int digit2 = Integer.parseInt(leftSid.substring(1, 2)) * 3;
        int digit3 = Integer.parseInt(leftSid.substring(2, 3)) * 4;
        int digit4 = Integer.parseInt(leftSid.substring(3, 4)) * 5;
        int digit5 = Integer.parseInt(leftSid.substring(4, 5)) * 6;
        int digit6 = Integer.parseInt(leftSid.substring(5, 6)) * 7;

        int digit7 = Integer.parseInt(rightSid.substring(0, 1)) * 8;
        int digit8 = Integer.parseInt(rightSid.substring(1, 2)) * 9;
        int digit9 = Integer.parseInt(rightSid.substring(2, 3)) * 2;
        int digit10 = Integer.parseInt(rightSid.substring(3, 4)) * 3;
        int digit11 = Integer.parseInt(rightSid.substring(4, 5)) * 4;
        int digit12 = Integer.parseInt(rightSid.substring(5, 6)) * 5;

        int last_digit = Integer.parseInt(rightSid.substring(6, 7));

        int error_verify = (digit1 + digit2 + digit3 + digit4 + digit5 + digit6 + digit7 + digit8 + digit9 + digit10 + digit11 + digit12) % 11;

        int sum_digit = 0;
        if (error_verify == 0) {
            sum_digit = 1;
        } else if (error_verify == 1) {
            sum_digit = 0;
        } else {
            sum_digit = 11 - error_verify;
        }

        if (last_digit == sum_digit) return true;
        return false;
    }


    /**
     * 카멜 케이스를 "_" 형태로 연결한다. prototype의 underscore와는 달리 대문자 이다. ex) userName =>
     * USER_NAME
     */
    public static String getUnderscore(String str) {

        char[] chars = str.toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        for (char cha : chars) {
            if (cha >= 'A' && cha <= 'Z') stringBuffer.append('_');
            stringBuffer.append(cha);
        }
        return stringBuffer.toString().toUpperCase();
    }

    /** '_' or '-' 형태의 연결을 카멜 케이스로 변환한다. ex) USER_NAME => userName */
    public static String getCamelize(String str) {
        char[] chars = str.toCharArray();
        boolean nextCharIsUpper = false;
        StringBuffer stringBuffer = new StringBuffer();
        for (char cha : chars) {
            if (cha == '_' || cha == '-') {
                nextCharIsUpper = true;
                continue;
            }
            if (nextCharIsUpper) {
                stringBuffer.append(Character.toUpperCase(cha));
                nextCharIsUpper = false;
            } else stringBuffer.append(Character.toLowerCase(cha));
        }
        return stringBuffer.toString();
    }

    /** 두문자를 제거 후 첫 글자를 소문자로 바꾼다. ex) searchMapKey => mapKey */
    public static String escapeAndUncapitalize(String str, String header) {
        return StringUtils.uncapitalize(str.replaceFirst(header, EMPTY));
    }

    /**
     * 분자열을 바이트 배열로 변형한다. 일단은 Cryptor에서만 사용한다. String 기본의 getByte와는 특수만자 입력시
     * 다르다. 왜인지는.. 몰라.
     **/
    public static byte[] getByte(String str) {
        char[] chs = str.toCharArray();
        byte[] bytes = new byte[chs.length];
        for (int i = 0; i < chs.length; i++) {
            bytes[i] = (byte) chs[i];
        }
        return bytes;
    }

    /**
     * 바이트배열을 문자형으로 변형한다.
     **/
    public static String getStr(byte[] bytes) {
        char[] chars = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            chars[i] = (char) bytes[i];
        }
        return String.valueOf(chars);
    }

    /**
     * 입력 문자열을 ''로 묶은 후 ,로 구분한다. 영감&할멈 => '영감','할멈'
     */
    public static String getBundleStr(List<String> bundle, String defaultValue) {
        if (bundle == null || bundle.size() == 0) return defaultValue;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bundle.size(); i++) {
            stringBuffer.append((i == 0) ? EMPTY : ",");
            stringBuffer.append("'");
            stringBuffer.append(bundle.get(i));
            stringBuffer.append("'");
        }
        return stringBuffer.toString();
    }
    
    /** 임시 포매팅. MessageFormat이랑 비슷하다. null이면 ''를 입력한다. */
    public static String format(String str,Object ... args) {
    	return formatNullable(str,"",args);
    }
    
    /** 배열 문제 때문에 String으로 입력을 제한한다. */
    public static String formatStr(String str,String ... args) {
    	if(CollectionUtil.isEmpty(args)) return str;
    	for(int i=0;i<args.length;i++) str = str.replaceAll("\\{"+i+"\\}", args[i]==null ? "" : args[i].toString());
    	return str;
    }
    
    /** 임시 포매팅. null이면 0을 입력한다. */
    public static String formatNullable(String str,String nullString,Object ... args) {
    	if(CollectionUtil.isEmpty(args)) return str;
    	for(int i=0;i<args.length;i++) str = str.replaceAll("\\{"+i+"\\}", args[i]==null ? nullString : args[i].toString());
    	return str;
    }    

    /**
     * 문자열을 특정 문자의 개수를 구한다.
     */
    public static int getCharCount(String str, char ch) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ch) {
                count++;
            }
        }
        return count;
    }

    /**
     * 문자열을 구분하여 화면에 보여질 문자의 길이값을 리턴한다. 폰트 브라우저 등에 따라 가변적임으로 알아서 조정하자.
     */
    public static int getStrLength(String str) {
        int strLength = 0;
        int hangleHtmlWidth = 20;
        int elseHtmlWidth = 12;
        for (int i = 0; i < str.length(); i++) {
            if (Character.getType(str.charAt(i)) == 5) {
                strLength += hangleHtmlWidth;
            } else {
                strLength += elseHtmlWidth;
            }
        }
        return strLength;
    }

    /**
     * 두개의 String을 받아 산술연산(+) 후 String으로 리턴한다. ex) plus('08','-2') => 06
     */
    public static String plus(String str, String str2) {
        int len = str.length();
        String result = StringCalculator.Plus(str, str2, 0);
        return StringUtils.leftPad(result, len, "0");
    }
    
    /** 정수 덧셈을 한다. */
    public static Integer plusForInteger(String str, int str2) {
        return Integer.parseInt(str)+str2;
    }
    
    /** 배열을 더한다. Utils에 있을거 같은데 없네.. */
	public static String[] addArray(String[] org,String ... args){
		if(org==null) return args;
		String[] result = new String[org.length + args.length];
		for(int i=0;i<org.length;i++){
			result[i] = org[i];
		}
		for(int i=0;i<args.length;i++){
			result[org.length+i] = args[i];
		}
		return result;
	}    

    // ===========================================================================================
    //                                      숫자 치환            
    // ===========================================================================================    
    /**
     * String을 받아 숫자(절대값)형태만 추출해서 반환한다.
     */
    public static String getNumericStr(Object str) {
        if (str == null) return null;
        return getNumericStr(str.toString());
    }

    /**
     * String을 받아 숫자(절대값)형태만 추출해서 반환한다. 음수라면 따로 판별 컬럼을 나누자.
     * ==> 추후 정규식으로 바꾸자.
     * @return Parsing가능한 String값
     */
    public static String getNumericStr(String str) {
        if (str == null) return EMPTY;
        StringBuffer result = new StringBuffer();
        int dotCount = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((c >= '0' && c <= '9')) result.append(c);
            else if ((c == '.' && dotCount < 1)) { //소수점 1개만 허용
                result.append(c);
                dotCount++;
            }
        }
        return result.toString();
    }

    /**
     * String을 받아 숫자(절대값)형태만 추출해서 반환. <br> null safe하다. 음수라면 따로 판별 컬럼을 나누자.
     */
    public static BigDecimal getDecimal(String str) {
        if (str == null || str.equals(EMPTY)) return BigDecimal.ZERO;
        boolean minus = str.startsWith("-");
        String temp = StringUtils.getNumericStr(str);
        if (temp.equals(EMPTY)) return BigDecimal.ZERO;
        if(minus) temp = "-" + temp;
        return new BigDecimal(temp);
    }

    /**
     * String을 받아 숫자(절대값)형태만 추출해서 반환. 음수라면 따로 판별 컬럼을 나누자.
     */
    public static double getDoubleValue(String str) {
        return Double.parseDouble(StringUtils.getNumericStr(str));
    }

    /**
     * String을 받아 숫자(절대값)형태만 추출해서 반환. 음수라면 따로 판별 컬럼을 나누자.
     */
    public static int getIntValue(String str) {
        return Integer.parseInt(StringUtils.getNumericStr(str));
    }

    /**
     * join시 디폴드값으로 ","를 준다.
     */
    public static String join(List<?> list){ return join(list,","); }
    

    /**
     * 배열을 seperators로 연결해서 반환한다. Weblogic 10.0/10.3에서 join사용(2.3이후버전)시 오류발생으로
     * 이것으로 대체
     */
    public static String joinTemp(List<?> list, String seperator) {
        StringBuffer stringBuffer = new StringBuffer();
        boolean first = true;
        for (Object string : list) {
            if (!first) stringBuffer.append(seperator);
            else first = false;
            stringBuffer.append(string);
        }
        return stringBuffer.toString();
    }

    /**
     * 배열을 seperators로 연결해서 반환한다. Weblogic 10.0/10.3에서 join사용(2.3이후버전)시 오류발생으로
     * 이것으로 대체
     */
    public static <T> String joinTemp(T[] list, String seperator) {
        StringBuffer stringBuffer = new StringBuffer();
        boolean first = true;
        for (Object string : list) {
            if (!first) stringBuffer.append(seperator);
            else first = false;
            stringBuffer.append(string);
        }
        return stringBuffer.toString();
    }
   
    
    public static boolean isEmptyAny(String ... objs) {
    	for(String each : objs) if(isEmpty(each)) return true;
    	return false;
    }    
    
  
    // ===========================================================================================
    //                                      NVL            
    // ===========================================================================================

    /**
     * null 또는 공백문자사를 처리 stripToEmpty을 대신한다.
     */
    public static String nvl(String str) {
        return nvl(str, EMPTY);
    }
    public static String nvlObject(Object obj,String escape) {
    	if(obj==null) return escape;
    	return nvl(obj.toString(), escape);
    }

    /**
     * null 또는 공백문자사를 처리 stripToEmpty을 대신한다.
     */
    public static String nvl(String str, String defaultStr) {
        return StringUtils.isEmpty(str) ? defaultStr : str.trim();
    }

    public static Integer nvl(Integer integer) {
        return (integer == null) ? 0 : integer;
    }

    public static Integer nvl(String str, Integer defaultint) {
        return StringUtils.isEmpty(str) ? defaultint : Integer.parseInt(str.trim());
    }

    /**
     * null safe한 toString
     */
    public static String toString(Object str) {
        if (str == null) return EMPTY;
        return str.toString();
    }
	public static void main( String[] args ){
		System.out.println(StringUtils.truncateHTML("간만<a>에<img src='aadfasdfsdf'>택시 타임</a>~!매혹적인 오렌지빛 람보르기니를 타고혹적인 그녀, 류지혜와 함께트랙을 질주하다!그리고유경욱을 긴장하게 만든또다른 미녀는 누구인가?To be continued.INSITE TV 채널 '구독하기'를 누르시면매일매일 새로운 영상을 보실 수 있어요~!간만에 걸기어 택시 타임~!매혹적인 오렌지빛 람보르기니를 타고혹적인 그녀, 류지혜와 함께트랙을 질주하다!그리고유경욱을 긴장하게 만든또다른 미녀는 누구인가?To be continued.INSITE TV 채널 '구독하기'를 누르시면매일매일 새로운 영상을 보실 수 있어요~!간만에 걸기어 택시 타임~!매혹적인 오렌지빛 람보르기니를 타고혹적인 그녀, 류지혜와 함께트랙을 질주하다!그리고유경욱을 긴장하게 만든또다른 미녀는 누구인가?To be continued.INSITE TV 채널 '구독하기'를 누르시면매일매일 새로운 영상을 보실 수 있어요~!", 10, "..."));
	
	}


}