<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">${smallGroup.name }</c:param>
	<c:param name="bsUsed">NO</c:param>
	<c:param name="aceUsed">NO</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>
<style type="text/css">
.rw-content-area-wrap	{background-color: white;}
</style>
</head>
<body class="scroll-y">

<c:choose>
	<c:when test="${smallGroup.type == 3}">
		<c:set var="groupTypeName" value="부서" />
	</c:when>
	<c:when test="${smallGroup.type == 4}">
		<c:set var="groupTypeName" value="프로젝트" />
	</c:when>
	<c:when test="${smallGroup.type == 5}">
		<c:set var="groupTypeName" value="소그룹" />
	</c:when>

</c:choose>

<div class="rw-snn-wrap" id="rw-snn-wrap">
	<div id="rw-snn-navi-wrap"></div>
	<div id="rw-snn-navir-wrap">
		<c:import url="/WEB-INF/views/common/nav-list-right.jsp">
		</c:import>
	</div>	
	<div id="rw-snn-main" class="rw-snn">
		<div id="rw-view-shield"></div>
		<!-- BEGIN:navbar -->
		<div class="navbar" id="navbar">
			<c:import url="/WEB-INF/views/common/navbar.jsp">
			</c:import>
		</div>
		<!-- END:navbar -->
		<!-- BEGIN:main-container -->
		<div class="main-container container-fluid z1" id="main-container">
			<!-- BEGIN:sidebar -->		
			<div class="sidebar" id="snn-sidebar">
				<!-- BEGIN:welcome-box -->		
				<c:import url="/WEB-INF/views/common/welcome-box.jsp">
				</c:import>
				<!-- END:welcome-box -->
				<!-- BEGIN:nav-list -->
				<c:import url="/WEB-INF/views/common/nav-list.jsp">
					<c:param name="menuName">${groupTypeName }</c:param>
				</c:import>			
				<!-- END:nav-list -->
			</div>
			<!-- END:sidebar -->
			<!-- BEGIN:main-content -->		
			<div class="main-content z1">
							
				<!-- BEGIN:page-content -->						
				<div class="page-content z1">
					<div class="rw-content-area-wrap">
						<!-- BEGIN:page-header -->
						<c:import url="/WEB-INF/views/common/page-header.jsp">
							<c:param name="pageName">group</c:param>
							<c:param name="tabName">about</c:param>
						</c:import>					
						<!-- END:page-header -->	
						<div class="rw-content-area rw-content-area-top-02">
						<div class="rw-pagelet-blank"></div>
							<div class="rw-pagelet-wrap">
							
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
							<small>
									<c:choose>
										<c:when test="${empty smallGroupUser}">
											미소속
										</c:when>
										<c:otherwise>
											소속됨
										</c:otherwise>
									</c:choose>			
							</small>
						</div>
						<div class="rw-pagelet-blank"></div>
							<div class="rw-pagelet-wrap">
								<div>
									소개 : ${smallGroup.description }
								</div>
								<div>
									생성자 : ${smallGroup.creator.name }
								</div>
								<div>
									생성일 : ${smallGroup.createDate}
								</div>
								<div>
									${smallGroup.userCount }명의 회원과 ${smallGroup.eventCount }개의 글이 있습니다.
								</div>
							
							</div>
						</div>
					</div>
					<div class="rw-right-col">
						<div class="rw-pagelet-blank"></div>
						
						<!-- BEGIN:rightcol -->
						<c:import url="/WEB-INF/views/common/right-col.jsp">
						</c:import>
						<!-- END:rightcol -->
	
					</div>				
				</div>
			</div>	
		</div>
	</div>
</div>
<script type="text/javascript">

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
</body>
</html>