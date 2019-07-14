<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<c:if test="${not empty smallGroup }">
	<c:set var="groupPath" value="/group/${smallGroup.id }" scope="request"></c:set>
</c:if>
<c:choose>
	<c:when test="${smallGroup.type == 3 }" >
		<c:set var="isDepartment" value="true" scope="request"></c:set>
	</c:when>
	<c:otherwise>
		<c:set var="isDepartment" value="false" scope="request"></c:set>
	</c:otherwise>
</c:choose>

<c:if test="${not empty smallGroupUser}">
	<c:set var="isAccessible" value='true' scope="request"></c:set>
</c:if>

<c:if test="${ not empty smallGroupUser && smallGroupUser.admin }">
	<c:set var="isSmallGroupAdmin" value='true' scope="request"></c:set>	
</c:if>



<c:if test="${not isDepartment }">
	<script>
	function ajax_small_group_add_user(data){
		if( data.result == "fail" ){
			alert( data.message );
			return;
		}
	
		$(this).addClass("hidden-elem");
		$(this).siblings("a").removeClass("hidden-elem");
	}
	
	function ajax_small_group_remove_user(data){
		if( data.result == "fail" ){
			alert( data.message );
			return;
		}
		$(this).addClass("hidden-elem");
		$(this).siblings("a").removeClass("hidden-elem");
	}
	
	</script>
</c:if>
<div>

	<div class="page-header position-relative">
		<h1>
			<span>${smallGroup.name }
			
			<c:if test="${not isDepartment }">
				<c:choose>
					<c:when test="${empty smallGroupUser && empty smallGroupInactiveUser}">
						<a	href="/group/${smallGroup.id }/add_user"
							class="hmo-button hmo-button-khaki hmo-button-small-3 btn-small-group-request-${smallGroup.id }"
							ajaxify="ajax_small_group_add_user"
							rel="sync-get"
							data-request-map="{&quot;uid&quot;:&quot;${authUserId}&quot;}">가입하기</a>
						<a class="hmo-button hmo-button-khaki hmo-button-small-3 btn-small-group-request-${smallGroup.id } hidden-elem"
							href="/group/${smallGroup.id }/remove_user" 
							class="btn hmo-button-khaki btn-minier btn-small-group-request-${smallGroup.id }"
							ajaxify="ajax_small_group_remove_user"
							rel="sync-get"
							data-request-map="{&quot;uid&quot;:&quot;${authUserId}&quot;}">가입신청 취소</a>
					</c:when>
					<c:when test="${empty smallGroupUser && not empty smallGroupInactiveUser }">
						<a	href="/group/${smallGroup.id }/add_user"
							class="hmo-button hmo-button-khaki hmo-button-small-3 btn-small-group-request-${smallGroup.id } hidden-elem"
							ajaxify="ajax_small_group_add_user"
							rel="sync-get"
							data-request-map="{&quot;uid&quot;:&quot;${authUserId}&quot;}">가입하기</a>
						<a class="hmo-button hmo-button-khaki hmo-button-small-3 btn-small-group-request-${smallGroup.id } "
							href="/group/${smallGroup.id }/remove_user" 
							class="btn hmo-button-khaki btn-minier btn-small-group-request-${smallGroup.id }"
							ajaxify="ajax_small_group_remove_user"
							rel="sync-get"
							data-request-map="{&quot;uid&quot;:&quot;${authUserId}&quot;}">가입신청 취소</a>
					</c:when>
				</c:choose>
			</c:if>
			
			</span>
			<small>
				<c:if test="${isDepartment }">
					<c:choose>
						<c:when test="${empty smallGroupUser}">
							미소속
						</c:when>
						<c:otherwise>
							소속됨
						</c:otherwise>
					</c:choose>			
				</c:if>
				<i class="fa fa-hand-o-right"></i>
					<a href="#group-detail" data-toggle="collapse" class="accordion-toggle collapsed">
						설명보기</a>
			</small>
		</h1>
		<div class="accordion-body collapse" id="group-detail" style="height: 0px;">
			<div class="accordion-inner" style="background-color: #F5FAFF">
				
				<div>
					소개 : ${smallGroup.description }
				</div>
				<c:if test="${not isDepartment }">
					<div>
						초대나 가입신청 모두 관리자의 승인을 받아야만 가입이 완료됩니다.
					</div>
					<div>
						${smallGroup.userCount }명의 회원과 ${smallGroup.eventCount }개의 글이 있습니다.
					</div>
				</c:if>					
			</div>
		</div>
	</div>
	
	<c:if test="${isAccessible}">
	
		<ul class="nav nav-tabs padding-18 tab-size-bigger z1" id="myTab">
			<li class="${param.tab == 'story' ? 'active' : ''}">
				<a href="/group/${smallGroup.id }" >
					<i class="blue fa fa-users bigger-120"></i>
				</a>
			</li>
			
			<li class="dropdown ${param.tab == 'misc' ? 'active' : ''}">
				<a data-toggle="dropdown" class="dropdown-toggle" href="#">
					<i class="purple fa fa-gear bigger-120"></i>
					기타
					<i class="fa fa-caret-down"></i>
				</a>
		
				<ul class="dropdown-menu dropdown-lighter dropdown-125">
					<li class="">
						<a href="/group/${smallGroup.id}/about"> 정보보기 </a>
					</li>
					<li class="">
						<a href="/group/${smallGroup.id}/joined"> 회원목록 </a>
					</li>
					<c:if test="${not empty smallGroupUser && smallGroupUser.admin == true }">
						<li class="">
							<a href="/group/${smallGroup.id}/setting"> 설정 </a>
						</li>
					</c:if>
				</ul>
			</li><!-- /.dropdown -->
		</ul>
	</c:if>
</div>