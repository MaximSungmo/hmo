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
	<c:param name="title">전자결재</c:param>
	<c:param name="bsUsed">YES</c:param>
	<c:param name="aceUsed">NO</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>


<!-- BEGIN:ace scripts -->
<script
	src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-elements.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace.js"></script>
<script
	src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-extra.js"></script>


<link rel="stylesheet"
	href="/assets/ace-theme-v1.2/bs-v2.3.x/css/font-awesome.min.css" />

<!-- END:ace scripts -->

<!-- BEGIN:sunny extra libraries -->


<!-- END:sunny extra libraries -->




<!-- BEGIN:additional plugin -->
<script
	src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/bootbox.js"></script>
<script
	src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/jquery.validate.js"></script>
	
<!-- END:additional plugin -->

<script src="/assets/sunny/2.0/js/uncompressed/ejs.js"></script>

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
					<c:param name="menuName">전자결재</c:param>
				</c:import>
				<!-- END:nav-list -->

			</div>
			<!-- END:sidebar -->

			<!-- BEGIN:main-content -->
			<div class="main-content">

				<!-- BEGIN:breadcrumbs&search -->
				<c:set var="breadcrumbs" value="전자결재" scope="request" />
				<c:set var="breadcrumbLinks" value="/approval" scope="request" />
				<div class="breadcrumbs" id="breadcrumbs">
					<c:import url="/WEB-INF/views/common/breadcrumbs.jsp">
						<c:param name="breadcrumbs">${breadcrumbs }</c:param>
						<c:param name="breadcrumbLinks">${breadcrumbLinks }</c:param>
					</c:import>
				</div>
				<!-- END:breadcrumbs&search -->

				<!-- BEGIN:page-content -->
				<div class="page-content">
					<div class="rw-content-area-wrap approval-rw-content-area-wrap">
						<div class="rw-content-area">
							
<!-- 							<div class="rw-pagelet-blank"></div> -->
<!-- 							<div class="rw-pagelet-wrap"> -->
							
<%-- 								<c:import url="/WEB-INF/views/common/approval-cover.jsp"> --%>
<%-- 									<c:param name="menu">write</c:param> --%>
<%-- 								</c:import> --%>
<!-- 							</div> -->
						
							<div class="rw-pagelet-blank"></div>
							<div class="rw-pagelet-wrap approval-wrtie-rw-pagelet-wrap">
							
								<c:import url="/WEB-INF/views/common/draft-publish.jsp">
									<c:param name="typeName">approval</c:param>
									<c:param name="contentType">8</c:param>
								</c:import>
							
							</div>
						</div>
					</div>
					<div class="rw-right-col">
						
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
			
<!-- BEGIN:resource of dialog for showing profile -->
<div id="res-dialog-show-profile" style="display:none">
	<div class="rw-dialog-wrap rw-form-wrap" id="res-dialog-show-profile-content">
	</div>
</div>
<!-- END:resource of dialog for showing profile -->	

</body>
</html>