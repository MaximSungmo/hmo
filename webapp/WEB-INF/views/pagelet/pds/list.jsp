<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="message-list-container">
	<div class="message-list" id="message-list">

	<c:forEach items="${pagedResult.contents}" var="content" varStatus="status">
		<div class="message-item " style="border:none; border-bottom: 1px solid #eee; ">
			<a href="${groupPath }/pds/${content.id}" data-id="${content.id }" data-uid="${content.user.id }" data-view="view" class="pathchange">
<!-- 							<i class="message-star fa fa-star orange2"></i> -->
				
				<img src="${content.user.profilePic }" style="width:30px; height: 30px; ">
				<span class="sender" title="${content.user.name }" style="width: 50px; ">${content.user.name }</span>
				
				<span class="time" style="width: 113px; ">
					<fmt:formatDate value='${content.createDate }' pattern="yyyy/MM/dd hh:mm:ss" />
				</span>
				<span class="attachment">
					<i class="fa fa-file"></i>
					<span class="media-count">(${content.mediaCount })</span>
				</span>
				<span class="summary">
					<span class="text">
						${content.title }
					</span>
				</span>
			</a>
		</div>
		
	</c:forEach>
	</div>
	<div class="hide message-content" id="id-message-content"></div>
</div>

<div class="message-footer clearfix">
	<div class="pull-left"> </div>

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
				<form id="pds-page-form" style="margin: 0;">
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