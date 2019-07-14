<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div class="breadcrumbs-inner">
	<ul class="breadcrumb">
		<li>
			<i class="fa fa-home fa-1g"></i>
			<a href="/">홈</a>
		</li>
		<c:forTokens items="${param.breadcrumbs }" delims="," var="breadcrumb" varStatus="status">
			<c:forTokens items="${param.breadcrumbLinks }" delims="," var="breadcrumbLink" begin="${status.index}" end="${status.index}">
				<li class="active">
					<span class="divider">
						<i class="fa fa-angle-right fa-1g"></i>
					</span>
					<c:choose>	
						<c:when test="${breadcrumbLink == '#' }">
							<span>${breadcrumb }</span>
						</c:when>
						<c:otherwise>
							<a href="${breadcrumbLink }">${breadcrumb }</a>
						</c:otherwise>
					</c:choose>
				</li>
			</c:forTokens>
		</c:forTokens>
	</ul>
	<div class="nav-search" >
		<form class="main-search-form" action="">
			<span class="input-icon">
				<input type="text" placeholder="검색 ..." class="input-small nav-search-input" id="nav-search-input" name="q[]" autocomplete="off">
				<i class="fa fa-search fa-1g nav-search-icon"></i>
			</span>
		</form>
	</div>
</div>