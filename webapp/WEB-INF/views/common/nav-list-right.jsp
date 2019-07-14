<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div class="sidebar" style="background-color:#eee">
	<div class="rw-nav-list-wrap right-sidebar-wrap">
		<ul class="nav nav-list">
			<h3>설정</h3>
			
			<c:if test="${authUserId != '203'}">
			<li class="">
				<a href="/user/${authUserId}/setting">
					<i class="fa fa-cog fa-1g mb-mgr"></i>
					<span class="menu-text">설정</span>
				</a>
			</li>
			</c:if>
				
			<li class="">
				<a href="/user/${authUserId}">
					<i class="fa fa-user fa-1g mb-mgr"></i>
					<span class="menu-text">프로필 편집</span>
				</a>
			</li>	
			
			<c:if test="${authUserIsAdmin}">
			<li class="">
				<a class="btn-new-user" href="#">
					<i class="fa fa-plus-square fa-1g mb-mgr"></i>
					<span class="menu-text">구성원 초대하기</span>
				</a>
			</li>	
			</c:if>
			<li class="${param.menuName == '알려진 버그' ? 'active' : '' }">
				<a href="/issue">
					<i class="fa fa-bug mb-mgr"></i>
					<span class="menu-text">알려진 버그</span>
				</a>
			</li>
			<li class="${param.menuName == '피드백' ? 'active' : '' }">
				<a href="/feedback">
					<i class="fa fa-smile-o mb-mgr"></i>
					<span class="menu-text">버그 신고 및 기능 요청</span>
				</a>
			</li>
			<li class="">
				<a href="/user/logout">
					<i class="fa fa-power-off fa-1g mb-mgr"></i>
					<span class="menu-text">로그아웃</span>
				</a>
			</li>	
		</ul>
	</div>
</div>

