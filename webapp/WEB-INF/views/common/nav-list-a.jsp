<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<ul class="nav nav-list">
<%-- 				<li class="${param.menuName == '관리자메인' ? 'active' : ''}"> --%>
<%-- 					<a href="/a"> --%>
<!-- 						<i class="fa fa-home fa-1g"></i> -->
<!-- 						<span class="menu-text">관리자메인</span> -->
<!-- 					</a> -->
<!-- 				</li> -->
				<li>
					<a href="/lobby">
						<i class="fa fa-group fa-1g"></i>
						<span class="menu-text"> 모두의 소식 </span>
					</a>
				</li>
				<li class="${param.menuName == '사이트관리' ? 'active' : ''}">
					<a href="/a/site">
						<i class="fa fa-gear fa-1g"></i>
						<span class="menu-text">사이트관리</span>
					</a>
				</li>
				<li class="${param.menuName == '메뉴관리' ? 'active' : ''}">
					<a href="/a/menu">
						<i class="fa fa-list-ul fa-1g"></i>
						<span class="menu-text">메뉴관리</span>
					</a>
				</li>
				
				<li class="${param.menuName == '사용자관리' ? 'active' : ''}">
					<a href="/a/user">
						<i class="fa fa-user fa-1g"></i>
						<span class="menu-text">사용자관리</span>
					</a>
				</li>
				<li class="${param.menuName == '가입신청관리' ? 'active' : ''}">
					<a href="/a/site/inactive_users">
						<i class="fa fa-list-alt fa-1g"></i>
						<span class="menu-text">가입신청관리</span>
					</a>
				</li>
				<li class="${param.menuName == '초대' ? 'active' : ''}">
					<a href="/a/site/invite_users">
						<i class="fa fa-group fa-1g"></i>
						<span class="menu-text">초대된사용자들</span>
					</a>
				</li>
				<li class="${param.menuName == '부서관리' ? 'active' : ''}">
					<a href="/a/department">
						<i class="fa fa-sitemap fa-1g"></i>
						<span class="menu-text">부서관리</span>
					</a>
				</li>
				<%--
				<li class="${param.menuName == '권한그룹관리' ? 'active' : ''}">
					<a href="/a/accessGroup">
						<i class="fa fa-lock fa-1g"></i>
						<span class="menu-text">권한그룹관리</span>
					</a>
				</li>
				<li class="${param.menuName == '템플릿' ? 'active' : ''}">
					<a href="/a/template">
						<i class="fa fa-lock fa-1g"></i>
						<span class="menu-text">결재템플릿</span>
					</a>
				</li>
				<li class="${param.menuName == '메뉴얼' ? 'active' : ''}">
					<a href="index.html">
						<i class="fa fa-book fa-1g"></i>
						<span class="menu-text">메뉴얼</span>
					</a>
				</li>
				 --%>				
</ul>	
