<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone=no">

<!-- BEGIN:basic bootstrap styles -->
<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/bootstrap.css" />
<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/bootstrap-responsive.css" />
<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/font-awesome.css" />
<!--[if IE 7]>
<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/font-awesome-ie7.min.css" />
<![endif]-->
<!-- END:basic bootstrap styles -->

<!-- fonts -->
<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/ace-fonts.css" />

<!-- BEGIN:ace styles -->
<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/uncompressed/ace.css" />
<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/uncompressed/ace-responsive.css" />
<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/uncompressed/ace-skins.css" />
<!--[if lte IE 8]>
<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/ace-ie.min.css" />
<![endif]-->
<!-- END:ace styles -->

<!-- BEGIN:sunny styles -->
<link rel="stylesheet" href="/assets/sunny/2.0/css/uncompressed/sunny.css" />
<!-- END:sunny styles -->

<!-- ace settings handler -->
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-extra.min.js"></script>

<!-- BEGIN:basic js scripts -->
	<!--[if !IE]> -->
	<script type="text/javascript">
		window.jQuery || document.write("<script src='/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/jquery-2.0.3.js'>"+"<"+"/script>");
	</script>
	<!-- <![endif]-->
	
	<!--[if IE]>
	<script type="text/javascript">
		window.jQuery || document.write("<script src='/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/jquery-1.10.2.js'>"+"<"+"/script>");
	</script>
	<![endif]-->
	
	<script type="text/javascript">
		if("ontouchend" in document) document.write("<script src='/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/jquery.mobile.custom.js'>"+"<"+"/script>");
	</script>
	
	<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/bootstrap.js"></script>
<!-- END:basic js scripts -->

<!-- BEGIN:ace scripts -->
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-elements.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace.js"></script>
<!-- END:ace scripts -->

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
				</c:import>			
				<!-- END:nav-list -->
		
			</div>
			<!-- END:sidebar -->
			
			<!-- BEGIN:main-content -->		
			<div class="main-content">
			
				<!-- BEGIN:breadcrumbs&search -->	
				<c:set var="breadcrumbs" value="모두의 소식" scope="request"/>
				<c:set var="breadcrumbLinks" value="/" scope="request"/>
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
									
								<!-- BEGIN:composer -->
								<c:import url="/WEB-INF/views/common/composer.jsp">
								</c:import>
								<!-- END:composer -->
												
							</div>								
							<div class="rw-pagelet-blank"></div>
							<div class="rw-pagelet-wrap rw-mtl">
								<div class="rw-pagelet-stream">
									<div class="rw-ui-stream-wrap">
	
										<!-- BEGIN:composer -->
										<c:import url="/WEB-INF/views/common/story-stream.jsp">
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