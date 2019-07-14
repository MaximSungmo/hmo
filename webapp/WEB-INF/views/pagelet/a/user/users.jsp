<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:forEach items="${pagedResult.contents}" var="user"
									varStatus="status">
	<div class="row-triple list-border-bottom" >
		<div class="triple-first">
			<div class="triple-cell">
				<img src="${user.profilePic }" class="img i-prof-pic" alt="">
			</div>
		</div>
		<div class="triple-second">
			<div class="triple-cell">
			
				<div>
					<a href="/a/user/${user.id}/info" class="hmo-button hmo-button-blue hmo-button-small-8">설정</a>
				</div>
			</div>
		</div>
		<div class="triple-third">
			<div class="triple-cell item-c-col">
				<h3 class="n-prof">
				${user.name } 
				</h3>
				<c:choose>
					<c:when test="${user.status == 0}">
						<span class="hmo-label">근무중</span>
					</c:when>
					<c:when test="${user.status == 1}">
						<span class="hmo-label">휴가</span>
					</c:when>
					<c:when test="${user.status == 2}">
						<span class="hmo-label">퇴사</span>
					</c:when>
				</c:choose>
				<div>
				${user.jobTitle1 }<c:if test="${not empty user.jobTitle2}">, ${user.jobTitle2 }</c:if><c:if test="${not empty user.jobTitle3}">, ${user.jobTitle3 }</c:if>
				</div>
				<div>
				<c:forEach items="${user.departments }"	var="department" varStatus="status">
					${department.name }${not status.last ? ',' : '' }
				</c:forEach>
				</div>
			</div>
		</div>
	</div>
</c:forEach>

<div class="z1">
	<div class="pagination-wrap">
		<div class="inline middle"> <span style="color:#0088cc">${pagedResult.pageNumber}</span> / ${pagedResult.endPageNum } </div>
		&nbsp; &nbsp;
		<ul class="pagination inline unstyled middle" >

			<c:choose>
			<c:when test="${pagedResult.pageNumber % 10 > 1 }">
				<li class="pagination-left">
					<a class="more-detail hmo-button hmo-button-white hmo-button-small-7-1" data-name="page" data-value="${pagedResult.pageNumber - 1 }" href="?page=${pagedResult.pageNumber - 1 }">
						<i class="fa fa-chevron-left"></i>
						이전
					</a>
				</li>
				</c:when>
				<c:otherwise>
					<li class="disabled pagination-left">
						<a href="#" class="hmo-button hmo-button-white hmo-button-small-7-1">
							<i class="fa fa-chevron-left"></i>
						이전
						</a>
					</li>
				</c:otherwise>
			</c:choose>

			<li>
				<form id="note-page-form" style="margin: 0;">
					<input  maxlength="3" type="text" value="${pagedResult.pageNumber }" style="margin-bottom: 0px;width: 25px;text-align: center;"/>
				</form>
			</li>
			
			<c:choose>
				<c:when test="${pagedResult.lastPageNum  > pagedResult.pageNumber }">
					<li class="pagination-right">
						<a class="more-detail hmo-button hmo-button-white hmo-button-small-7-1" data-name="page" data-value="${ pagedResult.pageNumber + 1 }"  href="?page=${ pagedResult.pageNumber + 1 }"> 
							다음
							<i class="fa fa-chevron-right"></i>
						</a>
					</li>
				</c:when>
				<c:otherwise>
					<li class="disabled pagination-right"><a href="#" class="hmo-button hmo-button-white hmo-button-small-7-1">다음
							<i class="fa fa-chevron-right"></i></a></li>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
</div>
<!-- <tr id="next-stream-item"> -->
<!-- 	<td colspan="100%"> -->
<%-- 	<c:choose> --%>
<%-- 		<c:when test="${pagedResult.endPageNum > pagedResult.pageNumber }"> --%>
<!-- 			<div class="row-fluid"> -->
<!-- 				<a href="#" class="btn span12" id="next-stream-btn">다음 리스트 가져오기</a> -->
<!-- 			</div> -->
<!-- 			<div id="next-stream-loading" class="center hidden-elem">로딩중</div> -->
<%-- 		</c:when>  --%>
<%-- 		<c:otherwise> --%>
<!-- 			<div class="center">더이상 표시할 데이터가 없습니다.</div> -->
<%-- 		</c:otherwise> --%>
<%-- 	</c:choose> --%>
<!-- 	</td> -->
<!-- </tr> -->
