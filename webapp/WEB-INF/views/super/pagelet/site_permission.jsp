<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<table class="table table-striped ">
	
	<c:choose>
		<c:when test="${pagedResult.totalCountOfElements > 0}">
			<c:forEach items="${pagedResult.contents}" var="site"
				varStatus="status">
				<tr>
					<td>${site.companyName}</td>
					<td><button class="btn btn-small hmo-button-khaki" data-map='{"id":"${site.lobbySmallGroup.id }", "name":"${site.companyName }"}' onclick="return add_candi_permission(this);">추가</button>
				</tr>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<tr>
				<td colspan="100%">
					해당 데이터가 존재하지 않습니다.
				</td>
			</tr>
		</c:otherwise>
	</c:choose>
</table>


<c:if test="${pagedResult.totalCountOfElements > 0}">
	<div class="pagination center">
		<ul>
			<c:choose>
				<c:when test="${pagedResult.pageNumber % 10 > 1 }">
					<li><a class="permission-more-detail" data-name="page" data-value="${pagedResult.pageNumber - 1 }" href="?page=${pagedResult.pageNumber - 1 }"><i
							class="fa fa-arrow-left"></i></a></li>
				</c:when>
				<c:otherwise>
					<li class="disabled"><a href="#"><i
							class="fa fa-arrow-left"></i></a></li>
				</c:otherwise>
			</c:choose>
			<c:forEach var="pageIndex" begin="${pagedResult.startPageNum }"
				end="${pagedResult.endPageNum }">
				<li class="${pageIndex == pagedResult.pageNumber ? 'active' : '' }">
					<a class="permission-more-detail" data-name="page" data-value="${pageIndex }" href="?page=${pageIndex }"><c:out value="${pageIndex }" /></a>
				</li>
			</c:forEach>
	
			<c:if test="${pagedResult.endPageNum < pagedResult.lastPageNum }">
				<li><a class="permission-more-detail" data-name="page" data-value="${pagedResult.lastPageNum}" href="?page=${pagedResult.lastPageNum }">${ pagedResult.lastPageNum}</a>
				</li>
			</c:if>
	
			<c:choose>
				<c:when test="${pagedResult.lastPageNum  > pagedResult.pageNumber }">
					<li><a class="permission-more-detail" data-name="page" data-value="${ pagedResult.pageNumber + 1 }"  href="?page=${ pagedResult.pageNumber + 1 }"> <i
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
</c:if>