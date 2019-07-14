<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>

<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">자료실</c:param>
	<c:param name="bsUsed">YES</c:param>
	<c:param name="aceUsed">YES</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>

<style type="text/css">
.rw-content-area-wrap	{background-color: white;}
.scroll-y 				{ padding: 0;}
.nav-search-icon		{ top: 2px; left: 3px;}
</style>

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
		<div class="main-container container-fluid">

			<!-- BEGIN:sidebar -->
			<div class="sidebar" id="snn-sidebar">

				<!-- BEGIN:welcome-box -->
				<c:import url="/WEB-INF/views/common/welcome-box.jsp">
				</c:import>
				<!-- END:welcome-box -->

				<!-- BEGIN:nav-list -->
				<c:import url="/WEB-INF/views/common/nav-list.jsp">
					<c:param name="menuName">자료실</c:param>
				</c:import>
				<!-- END:nav-list -->

			</div>
			<!-- END:sidebar -->

			<!-- BEGIN:main-content -->
			<div class="main-content">

				<!-- BEGIN:breadcrumbs&search -->
				<c:set var="breadcrumbs" value="자료실" scope="request" />
				<c:set var="breadcrumbLinks" value="/pds" scope="request" />
				<div class="breadcrumbs" id="breadcrumbs">
					<c:import url="/WEB-INF/views/common/breadcrumbs.jsp">
						<c:param name="breadcrumbs">${breadcrumbs }</c:param>
						<c:param name="breadcrumbLinks">${breadcrumbLinks }</c:param>
					</c:import>
				</div>
				<!-- END:breadcrumbs&search -->

				<!-- BEGIN:page-content -->
				<div class="page-content">
					<div class="rw-content-area-wrap">
						<div class="rw-content-area">
							<div class="rw-pagelet-blank"></div>
							<div class="rw-pagelet-wrap">
								<div class="row-fluid">
									<div class="span12">
										<!-- PAGE CONTENT BEGINS -->
			
										<div class="row-fluid">
											<div class="span12">
												<div class="tabbable">
													<c:import url="/WEB-INF/views/pagelet/pds/tab.jsp">
													</c:import>
			
													<div class="tab-content no-border no-padding">
														<div class="tab-pane in active">
															<div class="message-container">

																<c:import url="/WEB-INF/views/common/pds.jsp"></c:import>
																
															</div><!-- /.message-container -->
														</div><!-- /.tab-pane -->
													</div><!-- /.tab-content -->
												</div><!-- /.tabbable -->
												
											</div><!-- /.span -->
										</div><!-- /.row-fluid -->
										
										
			
										<!-- PAGE CONTENT ENDS -->
										
									</div><!-- /.span -->
								</div><!-- /.row-fluid -->
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