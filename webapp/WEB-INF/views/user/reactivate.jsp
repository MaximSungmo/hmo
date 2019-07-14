<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
<body style="overflow:auto">
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
							<h2 class="warning-title">인증이 완료되지 않았습니다.</h2>
							<p class="exception-main-title-p"><span class="exception-main-title-sp">제한된 기능을 사용하기 위해 이메일 인증이 반드시 필요합니다.<br>
							가입 시, 입력하신 이메일 주소로 보내드린 인증에 관한 내용을 참고 하세요.</span>
							</p>
							<form method="post" action="/user/reactivate" class="form-ss">
								<c:choose>
									<c:when test="${not empty param.username }">
										<input type="text" name="username" readonly value="${param.username }">
									</c:when>
									<c:otherwise>
										<input type="text" name="username" >
									</c:otherwise>
								</c:choose>
								<button type="submit" class="hmo-button-search hmo-button hmo-button-khaki" >확인</button>
							</form>
							
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




<%-- 

<div class="container">
	<div class="jumbotron">
		<h1>권한없음</h1>
		<p class="lead">해당 페이지에 대한 권한이 없습니다.</p>
		<p>
			<a href="/" class="btn hmo-button-khaki btn-lg">메인으로 가기</a> 
		</p>
	</div>
</div>
<!-- /.container -->


--%>