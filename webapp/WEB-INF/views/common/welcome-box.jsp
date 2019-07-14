<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
	
<div id="nav-search" class="rw-sidebar-search-box">
	<form class="form-search" action="">
			<div class="rw-sidebar-search-box-l"><input placeholder="검색" type="text" id="nav-search-input" name="q[]" autocomplete="off"></div>
			<div class="rw-sidebar-search-box-r"><a href="#"><i class="fa fa-search fa-1g sidebar-search-box-icon"></i></a></div>
	</form>
</div>
<div class="rw-sidebar-welcome-box" >
	<div class="rw-welcomebox z1">
		<a class="rw-welcomebox-block"href="/user/${authUserId}">
			<img class="welcomebox-profile" src="${authUserProfilePic }" alt="" id="">
		</a>
		<div class="rw-welcomebox-contents-wrap">
			<div class="rw-welcomebox-contents">
				<a class="rw-welcombox-user-name rw-welcomebox-name name-${authUserId }" href="/user/${authUserId}" >${authUserName }</a>
				<ul class="profile-deps">
				<c:forEach items="${authUserDepartments }" var="department">
					<li><a href="/group/${department.id}">${department.name }</a></li>
				</c:forEach>
				</ul>
			</div>
		</div>
	</div>
</div>
