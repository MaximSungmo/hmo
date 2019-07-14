<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="rw-nav-list-wrap">
	<ul class="nav nav-list">
		<c:forEach items="${currentInfo.leftMenus }" var="menu" varStatus="status">
		
			<c:choose>
				<c:when test="${menu.type == 0 }">
					<li class="${param.menuName == menu.name ? 'active' : '' } ${status.first? 'first-nav-list' : ''}">
						<a href="${menu.relativeHref}">
							${menu.iconHtml }
							<span class="menu-text">${menu.name }</span>
							<c:if test="${not empty menu.extraHtml }">
								<span class="menu-extra">${menu.extraHtml}</span>
							</c:if>
						</a>
					</li>	
				</c:when>
				<c:when test="${menu.type == 1 }">
					<li class="${param.menuName == '부서' ? 'active' : ''}">
						<a href="/department" class="dropdown-toggle">
							<i class="fa fa-sitemap fa-1g mb-mgr"></i>
							<span class="menu-text">부서(팀)</span>
							<b class="arrow fa fa-angle-down"></b>
						</a>
						<ul class="submenu" style="display:block">
							<c:forEach items="${currentInfo.leftDepartments}" var="department" varStatus="status">
								<li>
									<a href="/group/${department.id}">
										<span>${department.name }</span>
									</a>
								</li>
							</c:forEach>
						</ul>										
					</li>
				</c:when>
				<c:when test="${menu.type == 2 }">
					<li class="${param.menuName == '소그룹' ? 'active' : ''}">
						<a href="/group" class="dropdown-toggle">
							<i class="fa fa-sitemap fa-1g mb-mgr"></i>
							<span class="menu-text">소그룹</span>
							<b class="arrow fa fa-angle-down"></b>
						</a>
						<ul class="submenu" style="display:block">
							<c:forEach items="${currentInfo.leftGroups}" var="group" varStatus="status">
								<li>
									<a href="/group/${group.id}">
										<span>${group.name }</span>
									</a>
								</li>
							</c:forEach>
						</ul>										
					</li>
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<c:if test="${authUserIsAdmin }">
		<li class="left-col-admin-menu">
			<a href="/a">
				<i class="fa fa-laptop fa-1g mb-mgr admin-menu-title-main-icon"></i>
				<span class="menu-text admin-menu-title">관리자 도구<span><i class="admin-menu-title-icon fa fa-wrench fa-1g mb-mgr"></i></span></span>
			</a>
		</li>
		</c:if>
		
		<%--
		<li>
			<a href="#">
				<i class="fa fa-book fa-1g mb-mgr"></i>
				<span class="menu-text">메뉴얼</span>
			</a>
		</li>
		 --%>
		<sec:authorize access="hasRole('ROLE_SUPER_ADMIN')">
			<li class="super-admin">
			<a href="/super">
				<i class="fa fa-smile-o mb-mgr"></i>
				<span class="menu-text">슈퍼관리자</span>
			</a>
		</li>
		</sec:authorize>
	</ul>
</div>


