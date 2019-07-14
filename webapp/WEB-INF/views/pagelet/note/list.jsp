<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:forEach items="${pagedResult.contents }" var="content" varStatus="status">
	<div class="note-list-content " style="padding: 5px; border-bottom: 1px solid lightgray; position:relative; " >
		<div class="z1" style="padding-right: 115px; width: 100%; -moz-box-sizing:border-box; box-sizing: border-box;">
			<div style="font-size: 16px; font-weight:bold; margin-bottom: 5px; ">
				<a href="${groupPath }/note/${content.id }" class="block content-navigation">${content.title}</a>
			</div>
			<div style="float:left">
				<img src="${content.user.profilePic }" style="width: 26px; height: 26px;">
				<span class="list-name no-att"><a class="" style="text-decoration:none;"href="/user/${content.user.id}">${content.user.name }</a></span>
			</div>
			<div style="float:right; color:darkgray;">
				<fmt:formatDate value='${content.createDate }' pattern="yyyy/MM/dd hh:mm:ss" />
			</div>
			
		</div>
		<div class="" style="position:absolute; top:5px; right: 0; width: 120px; ">
			<div class="pull-left" style="width: 40px; text-align: center;">
				<em >${content.dynamic.replyCount}</em><br/>
				댓글
			</div>
			<div class="pull-left" style="width: 40px; text-align: center;">
				<em >${content.dynamic.feelScore }</em><br />
				공감
			</div>
			<div class="pull-left" style="width: 40px; text-align: center;">
				<em >${empty content.contentReadCount.count ? '0' : content.contentReadCount.count}</em><br />
				조회수
			</div>
					
		</div>
	</div>
</c:forEach>

<div class="z1">
	<div class="pull-right">
		<div class="inline middle"> ${pagedResult.pageNumber} of ${pagedResult.endPageNum } </div>
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
				<form id="note-page-form" style="margin: 0;">
					<input  maxlength="3" type="text" value="${pagedResult.pageNumber }" style="margin-bottom: 0px;width: 25px;text-align: center;"/>
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