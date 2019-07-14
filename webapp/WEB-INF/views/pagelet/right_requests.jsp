<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>



<c:choose>
		<c:when test="${pagedResult.totalCountOfElements > 0}">
			<c:forEach items="${pagedResult.contents}" var="siteInactiveUser" varStatus="status">
			<div class="z1 user-list">
				<div style="float:left;">
				<h3 class="n-prof">${siteInactiveUser.name }</h3>		
				<div class="time">
					<abbr data-utime="${siteInactiveUser.createDate }" class="timestamp livetimestamp"  data-hover="tooltip" data-tooltip-alignh="left" aria-label=""></abbr>
				</div>
				</div>
				<div style="float:right;">
					<a href="/a/site/deny" 
						role="dialog"
						data-style="messagebox-yesno"
						data-title="요청한 사용자 거절"
						data-message="가입 요청한 사용자를 삭제하시겠습니까?"
						ajaxify-dialog-yes="deny_inactive_user" 
						rel="async-get"
						data-request-map='{"id":"${siteInactiveUser.id}"}' 
						class="hmo-button hmo-button-white hmo-button-small-6">거부</a>
					<a href="/a/site/accept_after_confirm" 
						role="dialog"
						data-style="messagebox-yesno"
						data-title="가입 요청 수락"
						data-message="수락하면 사용자에게 인증 이메일이 전송됩니다.<br />수락하시겠습니까?"
						ajaxify-dialog-yes="accept_inactive_user" 
						rel="async-get"
						data-request-map='{"id":"${siteInactiveUser.id}"}'
						class="hmo-button hmo-button-blue hmo-button-small-6">승인</a>
				</div>
			</div>
			</c:forEach>
		</c:when>
		<c:otherwise>
			미승인된 구성원이 없습니다.
		</c:otherwise>
</c:choose>
