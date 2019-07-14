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
							<c:choose>
								<c:when test="${not empty notAdmin }">
										<div class="exception-main-title">
										<h2 class="warning-title">관리자 계정이 아닙니다.</h2>
										<p class="exception-main-title-p"><span class="exception-main-title-sp">입력하신 계정에는 비밀번호 변경에 관한 권한이 없습니다.<br>
										변경권한은 귀 회사의 회사관리시스템 관리자가 부여할 수 있습니다.<br>
										권한을 부여받으시려면 시스템 관리자에게 요청 하세요.</span>
										</p>
										<a href="/user/find_password" class="hmo-button hmo-button-large-10 hmo-button-white">이전 화면</a>
										<a href="/" class="hmo-button hmo-button-blue hmo-button-large-10">메인으로 가기</a>
										</div>
								</c:when>
								<c:when test="${not empty noExists }">
									<div class="exception-main-title">
									<h2 class="warning-title">존재하지 않는 사용자 입니다.</h2>
									<p class="exception-main-title-p"><span class="exception-main-title-sp">가입 이메일로 비밀번호 변경 안내메일을 발송해 드립니다<br>
									가입시 입력했던 이메일을 정확하게 입력해 주세요.</span>
									</p>
									<form method="post" action="">
										<input type="text" name="email" value="${email }">
										<button type="submit" class="hmo-button-search hmo-button hmo-button-khaki">확인</button>
									</form>
									
									<a href="/" class="hmo-button hmo-button-large-10 hmo-button-white">이전 화면</a>
									<a href="/" class="hmo-button hmo-button-blue hmo-button-large-10">메인으로 가기</a>
									</div>
								</c:when>
								<c:otherwise>
									<div class="exception-main-title">
									<h2>비밀번호 찾기</h2>
									<p class="exception-main-title-p"><span class="exception-main-title-sp">가입 이메일로 비밀번호 변경 안내메일을 발송해 드립니다<br>
									가입시 입력했던 이메일을 정확하게 입력해 주세요.</span>
									</p>
									<form method="post" action="" class="form-ss">
										<input type="text" name="email" value="${email }">
										<button type="submit" class="hmo-button-search hmo-button hmo-button-khaki">확인</button>
									</form>
									
									<a href="/" class="hmo-button hmo-button-large-10 hmo-button-white">이전 화면</a>
									<a href="/" class="hmo-button hmo-button-blue hmo-button-large-10">메인으로 가기</a>
									</div>
								</c:otherwise>
							</c:choose>
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