<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>



<table style="border: 1px solid #eee; text-align: center; width: 100%;">
	<tr>
		<th>보내는 이메일</th>
		<th>보낸사람</th>
		<th>타이틀</th>
		<th>보낸날짜</th>
		<th>상태</th>
		<th>관리자만</th>
		<th>사이트</th>
		<th>삭제</th>
	</tr>
	<c:forEach items="${pagedResult.contents}" var="mq">
		<tr style="border-top: 1px solid #eee">
			<td>${mq.sendEmail }</td>
			<td>${mq.user.name }</td>
			<td><a href="/super/email/${mq.id}" target="__blank">${mq.title }</a></td>
			<td>${mq.createDate }</td>
			<td><c:choose>
					<c:when test="${mq.status == 0 }">
														대기중
														</c:when>
					<c:when test="${mq.status == 1 }">
														전송중
														</c:when>
					<c:when test="${mq.status == 2 }">
														전송됨
														</c:when>
				</c:choose></td>
			<td>${mq.onlyAdmin}</td>
			<td>${ empty mq.siteId ? "모두" : mq.siteId}</td>
			<td>
				<form action="/super/delete_email" method="POST">
					<input type="hidden" name="id" value="${mq.id }">
					<button>삭제하기</button>
				</form>
			</td>
		</tr>
	</c:forEach>
</table>

<div class="message-footer clearfix">
	<div class="pull-left"></div>

	<div class="pull-right">
		<div class="inline middle">${pagedResult.pageNumber} of
			${pagedResult.lastPageNum }</div>
		&nbsp; &nbsp;
		<ul class="pagination inline unstyled middle">

			<c:choose>
				<c:when test="${pagedResult.pageNumber % 10 > 1 }">
					<li><a class="more-detail" data-name="page"
						data-value="${pagedResult.pageNumber - 1 }"
						href="?page=${pagedResult.pageNumber - 1 }"> <i
							class="fa fa-arrow-left"></i>
					</a></li>
				</c:when>
				<c:otherwise>
					<li class="disabled"><a href="#"> <i
							class="fa fa-arrow-left"></i>
					</a></li>
				</c:otherwise>
			</c:choose>

			<li>
				<form id="main-page-form" style="margin: 0;">
					<input maxlength="3" type="text" value="${pagedResult.pageNumber }" />
				</form>
			</li>

			<c:choose>
				<c:when test="${pagedResult.lastPageNum  > pagedResult.pageNumber }">
					<li><a class="more-detail" data-name="page"
						data-value="${ pagedResult.pageNumber + 1 }"
						href="?page=${ pagedResult.pageNumber + 1 }"> <i
							class="fa fa-arrow-right"></i>
					</a></li>
				</c:when>
				<c:otherwise>
					<li class="disabled"><a href="#"><i
							class="fa fa-arrow-right"></i></a></li>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
</div>