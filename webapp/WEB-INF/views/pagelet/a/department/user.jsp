<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:forEach items="${pagedResult.contents }" var="smallGroupUser" varStatus="status">
	<div class="group-list-content user-data">
		<div class=" row-triple" >
			<div class="triple-first ">
				<div class="triple-cell list-profilePic">
					<img alt="" src="${smallGroupUser.user.profilePic }">
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
								<c:if test="${inactive }">
									
									<a href="/group/${smallGroup.id}/accept_user"
										ajaxify="ajax_group_accept_user"
										rel="sync-get"
										data-request-map='{"uid":"${smallGroupUser.user.id }"}'>
										승인 
									</a>
									<a href="/group/${smallGroup.id}/remove_user"
										ajaxify="ajax_group_remove_user"
										rel="sync-get"
										data-request-map='{"uid":"${smallGroupUser.user.id }"}'>
										거부
									</a>
								</c:if>
								<c:if test="${not inactive }">
									<a class="group-remove-admin ${not smallGroupUser.admin ? 'hidden-elem' : ''}" href="/group/${smallGroup.id}/removeadmin"
										ajaxify="ajax_group_remove_admin"
										rel="sync-get"
										data-uid="${smallGroupUser.user.id }"
										data-request-map='{"uid":"${smallGroupUser.user.id }"}'>
										관리자 권한 빼기
									</a>
									<a class="group-join-admin ${ smallGroupUser.admin ? 'hidden-elem' : ''}" href="/group/${smallGroup.id}/joinadmin"
										ajaxify="ajax_group_join_admin"
										rel="sync-get"
										data-uid="${smallGroupUser.user.id }"
										data-request-map='{"uid":"${smallGroupUser.user.id }"}'>
										관리자 권한 부여
									</a>
									<a href="/group/${smallGroup.id}/remove_user"
										ajaxify="ajax_group_remove_user"
										rel="sync-get"
										data-request-map='{"uid":"${smallGroupUser.user.id }"}'>
										구성원에서 제외
									</a>
								</c:if>
							</li>
						</ul>
					</div>
			</div>
			<div class="triple-third" >
			
				<div class="triple-cell">
					<div>
						<a href="#" id="group-list-user-name-${smallGroupUser.user.id }" >${smallGroupUser.user.name }<span class="iamadmin ${not inactive && smallGroupUser.admin ? '' : 'hidden-elem'}">(관리자)</span></a>
					</div>
					<div style="color: gray"> 
					</div >
					<div style="color: gray">
						가입일 : ${smallGroupUser.createDate }
					</div>
				</div>
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
					<a class="group-users-more-detail" data-name="page" data-value="${pagedResult.pageNumber - 1 }" href="?page=${pagedResult.pageNumber - 1 }">
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
						<a class="group-users-more-detail" data-name="page" data-value="${ pagedResult.pageNumber + 1 }"  href="?page=${ pagedResult.pageNumber + 1 }"> 
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