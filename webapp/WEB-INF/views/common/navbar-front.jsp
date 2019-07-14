<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div class="navbar-inner">
	<div class="container-fluid navbar-inner-ss">
		<span class="brand">
			<a href="/" class="white"><small><span class="brand-slogan" style="font-size:78%;">for your smart business,</span> <span class="brand-slogan">"</span>Hello my office<span class="brand-slogan">"</span></small></a>
		</span>
		<span class="page-tool">
			<ul class="page-tool-ul">
				<c:if test="${empty sunny.site }">
					<li>
						<a href="/a/signup" class="tool-link"><span>설정</span></a>
					</li>
					<li class="page-tool-ul-li">
					·
					</li>
				</c:if>
				<c:choose>
					<c:when test="${empty isAuthenticated }">
						<li>
							<a href="/login" class="tool-link"><span>로그인</span>	</a>
						</li>
					</c:when>
					<c:otherwise>
						<li class="navi-profile-name">
							<img class="rw-nav-user-photo" src="${authUserProfilePic }" alt="${authUserName }">
							<span class="user-info name-${authUserId }">${authUserName }</span>
						</li>					
					</c:otherwise>
				</c:choose>
			</ul>
		</span>	
	</div>	
</div>