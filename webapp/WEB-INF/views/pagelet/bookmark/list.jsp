<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:forEach items="${pagedResult.contents}" var="bookMark" varStatus="status">
	<div class="bookmark-row row-triple" style="padding: 5px; border-bottom: 1px solid lightgray; ">
		<div class="triple-first" style="text-align:center; width:50px; overflow:hidden; text-overflow: ellipsis;">
			<img src="${bookMark.content.user.profilePic}" style="width:50px; height: 50px;"/><br />
			<span>${ bookMark.content.user.name }</span>
		</div>
		<div class="triple-second z1"  >
			<div class="triple-cell" >
				<a href="/bookmark/remove"
					data-request-map="{&quot;cid&quot;:&quot;${bookMark.content.id }&quot;}"
					role="dialog"
					data-style="messagebox-yesno"
					data-title="스크랩 삭제"
					data-message="삭제되면 복구할 수 없습니다.<br />이 스크랩을 삭제하시겠습니까?"
					ajaxify-dialog-yes="ajax_delete_bookmark"
					rel="sync-get" 
					>삭제</a>
			</div>
		</div>
		<a href="/story/${bookMark.content.id }" class="block" style="text-decoration:none; color: black;">
		<div class="triple-third">
			<div class="triple-cell " style="width:100%; height: 70px;">
				 <c:if test="${not empty bookMark.title }">
					<div>
						<span style="font-size: 14px; ">이름 : ${bookMark.title }</span>
					</div>
				</c:if>
				
				<c:choose>
					<c:when test="${bookMark.content.type == 1}">
						<div>
							<c:set var="rawText" value="${ bookMark.content.strippedRawText  }" />
							<c:set var="length" value="${fn:length(rawText)}"/>
							<c:if test="${length > 150 }">
								<c:set var="rawText" value="${fn:substring(rawText, 0, 150 ) } ··· " />
							</c:if>
							<span style="color:grey; font-size: 12px; ">원본 : ${rawText }</span>
						</div>
						<div>
							<span class="label label-info">스토리</span><span>&nbsp; <abbr data-utime="${bookMark.createDate}" class="timestamp livetimestamp"  data-hover="tooltip" data-tooltip-alignh="left" aria-label=""></abbr> 에 스크랩</span>
						</div>
					</c:when>
					<c:otherwise>
						<div>
							<span style="color:grey; font-size: 12px; ">원본 : ${bookMark.content.title }</span>
						</div>
						<div>
							<c:choose>
								<c:when test="${bookMark.content.type == 5 }">
									<span class="label label-grey">노트</span>								
								</c:when>
								<c:when test="${bookMark.content.type == 6 }">
									<span class="label label-success">자료실</span>								
								</c:when>
							</c:choose>
							<span>&nbsp; <abbr data-utime="${bookMark.createDate}" class="timestamp livetimestamp"  data-hover="tooltip" data-tooltip-alignh="left" aria-label=""></abbr> 에 스크랩</span>
						</div>
					
					</c:otherwise>
				</c:choose>
			</div>
		</div>
			</a>
	</div>
</c:forEach>
<div class="message-footer clearfix">
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
				<form id="bookmark-page-form" style="margin: 0;">
					<input  maxlength="3" type="text" value="${pagedResult.pageNumber }"/>
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