<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<c:forEach items="${pagedResult.contents }" var="smallGroupSmallGroup" varStatus="status">
	<c:choose>
	<%-- 사용자일때 --%>
	<c:when test="${smallGroupSmallGroup.accessSmallGroup.type == 0 }">
		<div >
			<div class="group-list-content row-triple" >
				<div class="triple-first"  >
					<div class="triple-cell list-profilePic">
						<img alt="" src="${smallGroupSmallGroup.accessSmallGroup.creator.profilePic }">
					</div>
				</div>
				<div class="triple-second">
					<div class="triple-cell position-relative" >
						<a href="#" data-toggle="dropdown" class="hmo-button hmo-button-blue hmo-button-small-10 dropdown-toggle">
							
							<i class="white fa fa-gear bigger-130"></i> &nbsp;
							<i class="fa fa-caret-down bigger-125"></i>
						</a>
			
						<ul class="dropdown-menu dropdown-lighter pull-right dropdown-100">
							<li>
								<a href="/group/${smallGroup.id}/remove_access"
									ajaxify="ajax_group_remove_access"
									rel="sync-get"
									data-request-map='{"sgsgid":"${smallGroupSmallGroup.id }"}'>
									<i class="fa fa-ban red"></i>
									허용 그룹에서 제외
								</a>
							</li>
						</ul>
					</div>
				</div>
				<div class="triple-third" >
				
					<div class="triple-cell">
						<div>
							<a href="#">${smallGroupSmallGroup.accessSmallGroup.creator.name }</a>
						</div>
						<div style="color: gray"> 
							${smallGroupSmallGroup.accessSmallGroup.creator.statusMessage }
						</div >
						<div style="color: gray">
							가입일 : ${smallGroupSmallGroup.createDate }
						</div>
					</div>
				</div>
			</div>
		
		</div>
	</c:when>
	<c:when test="${smallGroupSmallGroup.accessSmallGroup.id == smallGroup.id }">
	
	</c:when>
	<c:otherwise>
		<div class="small-group-data">
			<div class="group-list-content row-triple" >
				<div class="triple-first hover-pointer" onclick="return show_profile(this);" >
					<div class="triple-cell list-profilePic">
					</div>
				</div>
				<div class="triple-second">
					<div class="triple-cell position-relative" >
						<a href="#" data-toggle="dropdown" class="hmo-button hmo-button-blue hmo-button-small-10 dropdown-toggle">
							
							<i class="white fa fa-gear bigger-130"></i> &nbsp;
							<i class="fa fa-caret-down bigger-125"></i>
						</a>
		
						<ul class="dropdown-menu dropdown-lighter pull-right dropdown-100">
							<li>
									<a href="/group/${smallGroup.id}/remove_access"
										ajaxify="ajax_group_remove_access"
										rel="sync-get"
										data-request-map='{"sgsgid":"${smallGroupSmallGroup.id }"}'>
										<i class="fa fa-ban red"></i>
										허용 그룹에서 제외
									</a>
							</li>
						</ul>
					</div>
				</div>
				<div class="triple-third hover-pointer" onclick="return show_profile(this);" >
				
					<div class="triple-cell">
						<div>
							<a href="#">${smallGroupSmallGroup.accessSmallGroup.name }</a>
						</div>
						<div style="color: gray"> 
							${smallGroupSmallGroup.accessSmallGroup.description }
						</div >
						<div style="color: gray">
							가입일 : ${smallGroupSmallGroup.createDate }
						</div>
					</div>
				</div>
			</div>
		
		</div>
	
	</c:otherwise>
	</c:choose>

</c:forEach>

<div class="z1">
	<div class="pull-right">
		<div class="inline middle"> ${pagedResult.pageNumber} of ${pagedResult.endPageNum } </div>
		&nbsp; &nbsp;
		<ul class="pagination inline unstyled middle" >

			<c:choose>
			<c:when test="${pagedResult.pageNumber % 10 > 1 }">
				<li>
					<a class="group-access-more-detail" data-name="page" data-value="${pagedResult.pageNumber - 1 }" href="?page=${pagedResult.pageNumber - 1 }">
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
						<a class="group-access-more-detail" data-name="page" data-value="${ pagedResult.pageNumber + 1 }"  href="?page=${ pagedResult.pageNumber + 1 }"> 
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