<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:forEach items="${pagedResult.contents}" var="media" varStatus="status">
	<div class="media-row row-triple" style="padding: 5px; border-bottom: 1px solid lightgray; ">
		<div class="triple-first" style="text-align:center; width:50px; overflow:hidden; text-overflow: ellipsis;">
			<img src="${media.user.profilePic}" style="width:35px; height: 35px;"/><br />
			<span>${ media.user.name }</span>
		</div>
		<div class="triple-second z1" >
			<div class="triple-cell " style="width:100%; height: 40px;">
			<a href="/download?id=${media.id}" class="attached-file inline">
				다운로드
			</a>
			</div>
		</div>
		<div class="triple-third">
			<div class="triple-cell " style="width:100%; height: 40px;">
				<div>
					파일명 : 
						<span style="font-size: 14px; ">${media.fileName }</span> 
						
				</div>
				<div>
				 	${media.formatSize }
					·
					<abbr data-utime="${media.createDate}" class="timestamp livetimestamp"  data-hover="tooltip" data-tooltip-alignh="left" aria-label=""></abbr>
				</div>
				<div>
					스토리 보기 <a href="/story/${media.content.id }"><span style="font-size: 12px; ">${media.content.strippedSnippetText}</span></a>
				</div>
			</div>
		</div>
	</div>
</c:forEach>
<div class="message-footer clearfix">
	<div class="pagination-wrap">
		<div class="inline middle"> <span style="color:#0088cc">${pagedResult.pageNumber}</span> / ${pagedResult.lastPageNum } </div>
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
						<a class="more-detail hmo-button hmo-button-white hmo-button-small-7-1" data-name="page" data-value="${pagedResult.pageNumber - 1 }" href="?page=${pagedResult.pageNumber - 1 }">
						<i class="fa fa-chevron-left"></i>
						이전
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
					<li class="pagination-right">
						<a class="more-detail hmo-button hmo-button-white hmo-button-small-7-1" data-name="page" data-value="${ pagedResult.pageNumber + 1 }"  href="?page=${ pagedResult.pageNumber + 1 }"> 
							다음
							<i class="fa fa-chevron-right"></i>
						</a>
					</li>
				</c:when>
				<c:otherwise>
					<li class="disabled pagination-right"><a href="#" class="hmo-button hmo-button-white hmo-button-small-7-1">다음<i
							class="fa fa-chevron-right"></i></a></li>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
</div>