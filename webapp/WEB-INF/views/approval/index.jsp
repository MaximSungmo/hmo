<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>

<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">전자결재</c:param>
	<c:param name="bsUsed">NO</c:param>
	<c:param name="aceUsed">NO</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>

<!-- <link rel="stylesheet" href="/assets/sunny/2.0/css/uncompressed/story-writer.css"> -->

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
					<c:param name="menuName">전자결재</c:param>
				</c:import>			
				<!-- END:nav-list -->
			</div>					
			<!-- END:sidebar -->
			
			<!-- BEGIN:main-content -->		
			<div class="main-content z1">
			
				<!-- BEGIN:breadcrumbs&search -->
				<c:set var="breadcrumbs" value="전자결재" scope="request"/>
				<c:set var="breadcrumbLinks" value="/approval" scope="request"/>
				<div class="breadcrumbs" id="breadcrumbs">
					<c:import url="/WEB-INF/views/common/breadcrumbs.jsp">
						<c:param name="breadcrumbs">${breadcrumbs }</c:param>	
						<c:param name="breadcrumbLinks">${breadcrumbLinks }</c:param>	
					</c:import>			
				</div>					
				<!-- END:breadcrumbs&search -->						
				
				<!-- BEGIN:page-content -->						
				<div class="page-content z1 approbal-page-content">
				
					<div class="rw-content-area-wrap">
					
						<div class="rw-content-area">
						
							<div class="rw-pagelet-blank"></div>
							<div class="approval-cover-wrap">
								<div class="rw-pagelet-wrap approval-rw-pagelet-menu">
									<c:import url="/WEB-INF/views/common/approval-cover.jsp">
										<c:param name="nav">
										<c:choose>
											<c:when test="${empty param.menu || param.menu == 'index'}">
												
											</c:when>
											<c:when test="${param.menu == 'process' || param.menu == 'sent_process' || param.menu == 'receive_process'}">
												process										
											</c:when>
											<c:when test="${param.menu == 'approved' || param.menu == 'sent_approved' || param.menu == 'receive_approved' }">
												approved										
											</c:when>
											<c:when test="${param.menu == 'sent_reject' || param.menu == 'receive_reject' || param.menu == 'receive' || param.menu == 'circulation' }">
												misc
											</c:when>
										</c:choose>
										</c:param>								
									</c:import>
								</div>
								<div class="rw-pagelet-wrap approval-rw-pagelet-write-button-wrap">
									<div>
										<a href="/approval/drafts" class="hmo-button hmo-button-small-9 hmo-button-white">작성중인 결재 목록</a>
										<a href="/draft/create/approval" class="hmo-button hmo-button-blue hmo-button-small-9">새로운 결재 작성</a>
									</div>
								</div>
							</div>
							<div class="rw-pagelet-blank" style="clear:both;"></div>
							<div class="rw-pagelet-wrap">
								<!-- BEGIN:story-stream -->
								<c:import url="/WEB-INF/views/common/story-stream.jsp">
									<c:param name="currentUrl">/approval<c:if test="${not empty param.menu }">?menu=${param.menu }</c:if></c:param>
									<c:param name="streamUrl">/approval<c:if test="${not empty param.menu }">?menu=${param.menu }</c:if></c:param>
									<c:param name="sizeFetchForward">10</c:param>
									<c:param name="sizeFetchBackward">10</c:param>
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
<div id="res-dialog-pop-feeled-users" style="display: none">
	<div class="rw-dialog-wrap pop-feeled-users-scroll-wrap" id="res-dialog-pop-feeled-users-content"></div>
</div>
<div id="res-dialog-pop-approval-users" style="display: none">
	<div class="rw-dialog-wrap pop-approval-users-scroll-wrap" id="res-dialog-pop-approval-users-content"></div>
</div>
<form>
	<textarea tabindex="-1" id="ta-cmmnt-mirroring" class="textarea-mirroring cmmnt-mirroring"></textarea>
</form>
</body>
</html>