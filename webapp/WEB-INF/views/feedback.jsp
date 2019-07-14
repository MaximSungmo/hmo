<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">피드백</c:param>
	<c:param name="bsUsed">NO</c:param>
	<c:param name="aceUsed">NO</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>
</head>
<body class="scroll-y">
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
					<c:param name="menuName">피드백</c:param>
				</c:import>			
				<!-- END:nav-list -->
			</div>					
			<!-- END:sidebar -->
			
			<!-- BEGIN:main-content -->		
			<div class="main-content z1">
			
				<!-- BEGIN:breadcrumbs&search -->
				<c:set var="breadcrumbs" value="피드백" scope="request"/>
				<c:set var="breadcrumbLinks" value="/" scope="request"/>
				<div class="breadcrumbs" id="breadcrumbs">
					<c:import url="/WEB-INF/views/common/breadcrumbs.jsp">
						<c:param name="breadcrumbs">${breadcrumbs }</c:param>	
						<c:param name="breadcrumbLinks">${breadcrumbLinks }</c:param>	
					</c:import>			
				</div>					
				<!-- END:breadcrumbs&search -->						
				
				<!-- BEGIN:page-content -->						
				<div class="page-content z1">
				
					<div class="rw-content-area-wrap">
					
						<div class="rw-content-area">
							<c:if test="${empty queries }">
								<div class="rw-pagelet-blank"></div>
								<div class="rw-pagelet-wrap">
									<h1>버그 신고 및 기능 요청은 HelloMyOffice를 이용하는 모든 사람이 함께 사용하는 공간입니다.</h1>
								</div>
								<div class="rw-pagelet-blank"></div>
								<div class="rw-pagelet-wrap">
									<!-- BEGIN:story-writer -->
									<c:import url="/WEB-INF/views/common/story-writer-feedback.jsp">
										<c:param name="placehoderMessage">피드백을 올려주세요</c:param>
									</c:import>
									<!-- END:story-writer -->
								</div>
							</c:if>
															
							<div class="rw-pagelet-blank"></div>
							<div class="rw-pagelet-wrap">
								<!-- BEGIN:story-stream -->
								<c:import url="/WEB-INF/views/common/story-stream.jsp">
									<c:param name="currentUrl">/feedback</c:param>
									<c:param name="streamUrl">/feedback</c:param>
									<c:param name="sizeFetchForward">10</c:param>
									<c:param name="sizeFetchBackward">10</c:param>
									<c:param name="feedback">1</c:param>
								</c:import>
								<!-- END:story-stream -->
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
			<!-- END:main-content -->		
		</div>
	</div>
</div>
<c:import url="/WEB-INF/views/common/file-viewer.jsp"/>
<form>
	<textarea tabindex="-1" id="ta-cmmnt-mirroring" class="textarea-mirroring cmmnt-mirroring"></textarea>
</form>
</body>
</html>