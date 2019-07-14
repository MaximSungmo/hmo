<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>



<table style="text-align:center; width:100%; border-collapse:collapse; ">
	<tr>
		<th>
			이름
		</th>
		<th style="">
			사이트
		</th>
		<th style="">
			이메일
		</th>
		<th>
			마지막 로그인
		</th>
		<th>
			가입날짜
		</th>
		<th>
			삭제
		</th>
	</tr>
	<c:forEach items="${pagedResult.contents}" var="user" varStatus="status">
	<tr style="height: 30px; border-top: 1px solid #eee;">
		<td>
			<a href="/super/user/${user.id }" target="__blank">${user.name}</a>	
		</td>
		<td>
			<a href="/super/site/${user.site.id }" target="__blank">${user.site.companyName }</a>	
		</td>
		<td>
			${user.email }
		</td>
		<td>
 			<fmt:formatDate value='${user.lastLogin}' pattern="yyyy.MM.dd" />일 
		</td>
		<td>
			<fmt:formatDate value='${user.createDate}' pattern="yyyy.MM.dd" />일 
		</td>
		<td>
			<form method="post" action="#" style=""  onsubmit="return inlineSubmit( this, event );">
				<input type="hidden" name="id" value="${user.id }">
				<input type="hidden" name="sunny" value="sunnyvale">
				<input type="submit" value="삭제하기">	
			</form>
		</td>
	</tr>
</c:forEach>
</table>
<div class="message-footer clearfix">
	<div class="pull-left"> </div>

	<div class="pull-right">
		<div class="inline middle"> ${pagedResult.pageNumber} of ${pagedResult.lastPageNum } </div>
		&nbsp; &nbsp;
		<ul class="pagination inline unstyled middle" >

			<c:choose>
			<c:when test="${pagedResult.pageNumber % 10 > 1 }">
				<li>
					<a class="more-detail" data-name="page" data-value="${pagedResult.pageNumber - 1 }" href="?page=${pagedResult.pageNumber - 1 }">
						<i class="fa fa-arrow-left"></i>
					</a>
				</li>
				</c:when>
				<c:otherwise>
					<li class="disabled">
						<a href="#">
							<i class="fa fa-arrow-left"></i>
						</a>
					</li>
				</c:otherwise>
			</c:choose>

			<li>
				<form id="main-page-form" style="margin: 0;">
					<input  maxlength="3" type="text" value="${pagedResult.pageNumber }"/>
				</form>
			</li>
			
			<c:choose>
				<c:when test="${pagedResult.lastPageNum  > pagedResult.pageNumber }">
					<li>
						<a class="more-detail" data-name="page" data-value="${ pagedResult.pageNumber + 1 }"  href="?page=${ pagedResult.pageNumber + 1 }"> 
							<i class="fa fa-arrow-right"></i>
						</a>
					</li>
				</c:when>
				<c:otherwise>
					<li class="disabled"><a href="#"><i
							class="fa fa-arrow-right"></i></a></li>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
</div>