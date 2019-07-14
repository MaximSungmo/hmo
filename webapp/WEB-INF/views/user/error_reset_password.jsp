<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<head>
<c:import url="/WEB-INF/views/common/head-front.jsp">
	<c:param name="title">Hello My Office와 함께하세요</c:param>
	<c:param name="bsUsed">YES</c:param>
	<c:param name="aceUsed">YES</c:param>
	<c:param name="jsUsed">NO</c:param>
</c:import>
</head>
<html lang="ko">
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
							<h2>이용에 불편을 드려 죄송합니다.</h2>
							<p class="confirm_fail-p">
							<span class="exception-main-title-sp">
							<strong>다음과 같은 사유로 인증이 되지 않습니다.</strong><br>
							
							1. 인증기간이 만료되었습니다.<br>
							2. 이미 인증이 완료되었습니다.<br>
							3. 잘못된 접근입니다.<br><br>
							
							위의 3가지 경우를 확인해 주세요.<br></span>
							</p>
							<a href="/" class="hmo-button hmo-button-large-10 hmo-button-white">메인으로 가기</a>
							<a href="/user/find_password" class="hmo-button hmo-button-blue hmo-button-large-10">변경 안내메일 다시받기</a>
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
		<h1>인증 실패</h1>
		<p class="lead">인증이 실패했습니다.</p>
		<p>
			<a href="/" class="btn hmo-button-khaki btn-lg">메인으로 가기</a> 
		</p>
	</div>
</div>
<!-- /.container -->


--%>



<%--<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


인증 실패입니다. 토큰이 잘못됐남?

<p>
<a href="/">인트로로 가기</a>
 --%>