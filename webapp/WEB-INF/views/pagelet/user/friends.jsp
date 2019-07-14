<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>



<c:forEach items="${pagedResult.contents}" var="friend"
									varStatus="status">
	<div style="padding: 15px;" class="row-triple list-border-bottom" >
		<div class="triple-first">
			<div class="triple-cell">
				<img src="${friend.follower.profilePic }" style="width: 50px;" alt="">
			</div>
		</div>
		<div class="triple-second">
			<div class="triple-cell">
				<c:choose>
					<c:when test="${not empty friend.myRelation && friend.myRelation.friend }">
						<span class="ui-button ui-button-overlay ui-button-large basecamp-fri-ui-button" role="button"><i class="mt3 mr3 img icon-check"></i><span class="ui-button-text basecamp-ui-button-text">친구</span></span>
					</c:when>
					<c:otherwise>
					
					
					<a class="ui-button ui-button-overlay ui-button-large basecamp-fri-ui-button ${ empty friend.myRelation || ( not friend.myRelation.friend  && friend.myRelation.sendFriendRequest == false ) ? '' : 'hidden-elem'}"
					   role="button"
					   href="/user/request_friend"
					   rel="sync-post"
					   data-follow-action="add"
					   data-request-map='{"userId":"${friend.follower.id}", "requestType":"0"}'
					   ajaxify="ajax_follow">
					   <span class="ui-button-text basecamp-ui-button-text">친구요청</span>
					</a>
					<a class="ui-button ui-button-overlay ui-button-large basecamp-fri-ui-button ${not friend.myRelation.friend && friend.myRelation.sendFriendRequest == true ? '' : 'hidden-elem'}"
					   role="button"
					   href="/user/request_friend"
					   rel="sync-post"
					   data-follow-action="delete"
				   	   data-request-map='{"userId":"${friend.follower.id}", "requestType":"1"}'
					   ajaxify="ajax_follow">
					   <span class="ui-button-text basecamp-ui-button-text">친구요청 취소</span>
					</a>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="triple-third">
			<div class="triple-cell">
				<a href="/user/${friend.follower.id}">
				${friend.follower.name } (${friend.follower.jobTitle1 }<c:if test="${not empty friend.follower.jobTitle2}">, ${friend.follower.jobTitle2 }</c:if><c:if test="${not empty friend.follower.jobTitle3}">, ${friend.follower.jobTitle3 }</c:if>)
				<div>
				<c:forEach items="${friend.follower.departments }"	var="department" varStatus="status">
					${department.name }${not status.last ? ',' : '' }
				</c:forEach>
				</div>
				</a>
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
<!-- <tr id="next-stream-item"> -->
<!-- 	<td colspan="100%"> -->
<%-- 	<c:choose> --%>
<%-- 		<c:when test="${pagedResult.endPageNum > pagedResult.pageNumber }"> --%>
<!-- 			<div class="row-fluid"> -->
<!-- 				<a href="#" class="btn span12" id="next-stream-btn">다음 리스트 가져오기</a> -->
<!-- 			</div> -->
<!-- 			<div id="next-stream-loading" class="center hidden-elem">로딩중</div> -->
<%-- 		</c:when>  --%>
<%-- 		<c:otherwise> --%>
<!-- 			<div class="center">더이상 표시할 데이터가 없습니다.</div> -->
<%-- 		</c:otherwise> --%>
<%-- 	</c:choose> --%>
<!-- 	</td> -->
<!-- </tr> -->
