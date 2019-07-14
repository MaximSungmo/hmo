<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="now" class="java.util.Date" />
<c:set value="${fn:length(stories) }" var="countStory" />
<c:set var="currentDate">
	<fmt:formatDate value="${now }" pattern="yyyy.MM.dd" />
</c:set>

<c:if test="${countStory > 0 }">
	<c:set var="lastStoryCDate">
		<fmt:formatDate value='${stories[0].createDate }' pattern="yyyy.MM.dd" />
	</c:set>	
</c:if>

<!DOCTYPE html>
<html lang="ko">
<head>
</head>
<body >
	
									사이트 이름 : ${site.companyName }<br/>
									번호 : ${site.companyPhone }<br />
									소개 : ${site.companyIntroduce }<br />
									주소 : <a href="${site.homepage }">${site.homepage }</a><br />
									규모 : ${site.employeeSize }명<br />
									지역 : ${site.companyRegion }<br />
									사용자 수 : ${userCount }명<br />
									글 수 : ${countStory }<br />
								
									
									<c:forEach items="${stories }" var="story">
									<c:set var="storyCreateDate">
										<fmt:formatDate value='${story.createDate }' pattern="yyyy.MM.dd" />
									</c:set>
				
										<div style="clear:both; border:1px solid black; margin-bottom: 3px; ">
											<strong>ID : ${story.id } name : ${story.userName } 생성일 ${storyCreateDate } 댓글개수 : ${story.replyCount }, 평가 개수 : ${story.feelCount }, 미디어 개수 : ${story.mediaCount }</strong>
											<br />
											${story.taggedTextPrev }
											<div style="display:none">${story.taggedTextNext }</div>
											<br />
										</div>
									</c:forEach>
								
</body>
</html>