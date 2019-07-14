<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:forEach items="${pagedResult.contents }" var="draft" varStatus="status">
	<div class="z1 draft-row temporary-approval-list">
		<div class="temporary-approval-list-title">
			<a href="${groupPath }/${param.typeName}/write?id=${draft.id }" class="block">${empty draft.rawTitle ? '제목없음' : draft.rawTitle}</a>
		</div>
		
		<div class="temporary-approval-list-substance">
			<a class="temporary-approval-list-substance-link" style="text-decoration:none;" href="${groupPath }/${param.typeName}/write?id=${draft.id }">${empty draft.snippetText ? '내용없음' : fn:substring(draft.snippetText, 0, 50) }</a></span>
			<a class="draft-list-mid-del hmo-button hmo-button-blue hmo-button-small-4 temporary-approval-list-button"
				role="hmo-dialog"
				aria-controls="hmo-messagebox-yesno"
				data-message="삭제되면 복구할 수 없습니다.<br />이 임시저장을 삭제하시겠습니까?"
				data-label="임시저장 삭제"
				href="/draft/delete"
				data-request-map="{&quot;id&quot;:&quot;${draft.id }&quot;}"
				rel="sync-get"
				ajaxify="ajax_delete_draft">
				삭제
			</a>
		</div>
		<div>
		<span class="temporary-approval-list-writer-info">
		마지막 수정 : 
			<abbr data-utime="${draft.updateDate}" class="timestamp livetimestamp"></abbr>
		</span>
		</div>
		
		
	</div>
</c:forEach>

<div class="pagination-wrap-container">
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
						<a href="#" class="more-detail hmo-button hmo-button-white hmo-button-small-7-1">
							<i class="fa fa-chevron-left"></i>
							이전
						</a>
					</li>
				</c:otherwise>
			</c:choose>

			<li>
				<form id="note-page-form" style="margin: 0;">
					<input  maxlength="3" type="text" value="${pagedResult.pageNumber }"/>
				</form>
			</li>
			
			<c:choose>
				<c:when test="${pagedResult.lastPageNum  > pagedResult.pageNumber }">
					<li class="pagination-right">
						<a class="more-detail hmo-button hmo-button-white hmo-button-small-7-1" data-name="page" data-value="${ pagedResult.pageNumber + 1 }"  href="?page=${ pagedResult.pageNumber + 1 }"> 
							다음 <i class="fa fa-chevron-right"></i>							
						</a>
					</li>
				</c:when>
				<c:otherwise>
					<li class="disabled"><a href="#" class="hmo-button hmo-button-white hmo-button-small-7-1">다음 <i
							class="fa fa-chevron-right"></i></a></li>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
</div>
<script>
$(function(){
	timesince();
});
</script>