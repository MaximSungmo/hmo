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
							<c:param name="tabName">story</c:param>
						</c:import>					
						<!-- END:page-header -->
						<div class="rw-content-area rw-content-area-top-01">
							<c:if test="${not empty smallGroupUser && empty queries }">								
								<div class="rw-pagelet-blank"></div>
								<div class="rw-pagelet-wrap">
									<!-- BEGIN:story-writer -->
									<c:import url="/WEB-INF/views/common/story-writer.jsp">
										<c:param name="placehoderMessage">지금,당신의 스토리를 들려주세요!</c:param>
									</c:import>
									<!-- END:story-writer -->
								</div>
							</c:if>
							<div class="rw-pagelet-blank"></div>
							<div class="rw-pagelet-wrap rw-stream-pagelet-wrap">
								<div class="rw-pagelet-stream">
									<div class="rw-ui-stream-wrap">
										<!-- BEGIN:composer -->
										<c:import url="/WEB-INF/views/common/story-stream.jsp">
											<c:param name="currentUrl">/group/${smallGroup.id }</c:param>
											<c:param name="streamUrl">/group/${smallGroup.id }</c:param>
											<c:param name="sizeFetchForward">10</c:param>
											<c:param name="sizeFetchBackward">10</c:param>
										</c:import>
										<!-- END:composer -->
									</div>
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
</body>
</html>