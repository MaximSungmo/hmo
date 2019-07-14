<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<ul class="nav nav-list">
	<li>
		<a href="/">
			<i class="fa fa-globe fa-1g"></i>
			<span class="menu-text">회사로 돌아가기</span>
		</a>
	</li>
	<li class="${param.menuName == 'main' ? 'active' : ''}">
		<a href="/super">
			<i class="fa fa-globe fa-1g"></i>
			<span class="menu-text">메인</span>
		</a>
	</li>
	<li class="${param.menuName == 'menu' ? 'active' : ''}">
		<a href="/super/menu">
			<i class="fa fa-globe fa-1g"></i>
			<span class="menu-text">메뉴관리</span>
		</a>
	</li>
	<li class="${param.menuName == 'user' ? 'active' : ''}">
		<a href="/super/users">
			<i class="fa fa-group fa-1g"></i>
			<span class="menu-text">전체사용자</span>
		</a>
	</li>
	<li class="${param.menuName == 'site' ? 'active' : ''}">
		<a href="/super/sites">
			<i class="fa fa-group fa-1g"></i>
			<span class="menu-text">사이트관리</span>
		</a>
	</li>
	<li class="${param.menuName == 'inactive_users' ? 'active' : ''}">
		<a href="/super/site_inactive_users">
			<i class="fa fa-group fa-1g"></i>
			<span class="menu-text">미인증 사용자들</span>
		</a>
	</li>
	<li class="${param.menuName == 'email' ? 'active' : ''}">
		<a href="/super/send_email">
			<i class="fa fa-inbox fa-1g"></i>
			<span class="menu-text">전체 이메일 보내기</span>
		</a>
	</li>
	<li class="${param.menuName == 'story' ? 'active' : ''}">
		<a href="/super/story">
			<i class="fa fa-inbox fa-1g"></i>
			<span class="menu-text">시스템 스토리</span>
		</a>
	</li>

	<li class="${param.menuName == 'tag' ? 'active' : ''}">
		<a href="/super/tags">
			<i class="fa fa-inbox fa-1g"></i>
			<span class="menu-text">기본 태그들</span>
		</a>
	</li>
	
</ul>	
