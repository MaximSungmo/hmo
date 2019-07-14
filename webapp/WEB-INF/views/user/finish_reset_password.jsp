<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/WEB-INF/views/common/head-front.jsp">
	<c:param name="title">Hello My Office와 함께하세요</c:param>
	<c:param name="bsUsed">YES</c:param>
	<c:param name="aceUsed">YES</c:param>
	<c:param name="jsUsed">NO</c:param>
</c:import>
</head>
<body style="overflow:auto;">
<div class="rw-snn-fs">

	<!-- BEGIN:navbar -->
	<div class="navbar" id="navbar">
		<c:import url="/WEB-INF/views/common/navbar-front.jsp">
		</c:import>
	</div>
	<!-- END:navbar -->		

	<div class="main-container container-fluid">
		<div class="main-content rw-signup-wizard-content">
			<div class="page-content login-page-content">
				<div class="row-fluid rw-signup-wizard-content-row-fluid">
					<div class="span12">
						<div class="exception-wrap">
							<div class="exception-img">
								<img src="/assets/sunny/2.0/img/hellomyoffice_logo.png" alt="hellomyoffice-logo">
							</div>
							<div class="exception-main-title">
							<p class="exception-main-title-p ss-bold"><span class="exception-main-title-sp">
							비밀번호가 변경되었습니다 !<br>
							로그인 해주세요 :D</span>
							</p>
							<a href="/" class="hmo-button hmo-button-large-10 hmo-button-white">이전 화면</a>
							<a href="/" class="hmo-button hmo-button-blue hmo-button-large-10">메인으로 가기</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>	
</body>
</html>
