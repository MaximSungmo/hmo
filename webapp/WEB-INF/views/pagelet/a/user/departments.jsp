<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>



<c:forEach items="${pagedResult.contents}" var="smallGroup" varStatus="status">
								
	<div class="row-triple ${status.first ? 'first' : '' } list-border-bottom" >		
		<div class="triple-first " style="border-right: 1px solid #eee;" >
			<div class="triple-cell" style="display: table-cell; vertical-align: middle; height: 50px; width: 120px; ">
				${smallGroup.name }
			</div>
		</div>
		<div class="triple-second">
			<div class="triple-cell position-relative" >
				<a href="/a/user/setsmallgroup"
					ajaxify="ajax_group_remove_user"
					rel="sync-post"
					class="hmo-button hmo-button-blue"
					data-request-map='{"smallGroupId":"${smallGroup.id }", "userId":"${user.id }", "join":"false"}'>
					탈퇴
					</a>
			</div>
		</div>
		
		<div class="triple-third" style="">
			<div class="triple-cell">
				<c:forEach items="${smallGroup.smallGroupPathIdNames }" var="smallGroupPathIdName" varStatus="status">
					<span >
						${smallGroupPathIdName.name }${not status.last ? "&nbsp;/&nbsp;" : '' }
					</span>
				</c:forEach>
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