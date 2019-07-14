<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">채팅</c:param>
	<c:param name="bsUsed">NO</c:param>
	<c:param name="aceUsed">NO</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>
<script src="/assets/sunny/2.0/js/uncompressed/net.js?v=1.2"></script>
<script>
$(function(){
	Channel.init();
});
</script>
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
					<c:param name="menuName">채팅</c:param>
				</c:import>
				<!-- END:nav-list -->

			</div>
			<!-- END:sidebar -->

			<!-- BEGIN:main-content -->
			<div class="main-content z1">
			
				<!-- BEGIN:page-header -->
				<c:import url="/WEB-INF/views/common/page-header.jsp">
					<c:param name="pageName">chat</c:param>
				</c:import>					
				<!-- END:page-header -->
				
				<!-- BEGIN:page-content -->
				<div class="page-content z1">
					<div class="rw-content-area-wrap chatting-rw-content-area-wrap">
						<div class="rw-content-area">
							<div class="rw-pagelet-wrap rw-mtl rw-mobile-pagelet">
								<c:import url="/WEB-INF/views/pagelet/message/channels.jsp"></c:import>
							</div>
						</div>
					</div>
					
				</div>
			</div>
		</div>
	</div>
</div>	
<div class="hmo-dialog-res" id="dialog-chat-user">
	<div class="hmo-dialog-scrollable-area" id="dialog-start-chat-scrollable-area">
		<div class="d-fuc-l" id="dialog-start-chat-list"></div>
	</div>
</div>
</body>
</html>