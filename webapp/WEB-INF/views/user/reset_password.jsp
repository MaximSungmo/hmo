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
							<h2>새로운 비밀번호를 넣어주세요</h2><br>
							<form method="post" action="/user/alter_password"" method="post" >
								<input id="user_id" name="user_id" type="hidden" value="${user.id }" />
								<input id="key_value" name="key_value" type="hidden" value="${key.value }" />
								<div >
										<input type="password" 
											maxlength="32" 
											name="password"
											tabindex="1" 
											autocomplete="off"
											spellcheck="false"  
											tabindex="2" 
											placeholder="비밀번호" 
											value="">
								</div>
								<div>
										<input type="password" 
											maxlength="32" 
											name="password_confirm"
											tabindex="2" 
											autocomplete="off"
											spellcheck="false"  
											tabindex="2" 
											placeholder="비밀번호 재입력" 
											value=""
											id="password-confirm">
								</div>										
								<input class="hmo-button hmo-button-khaki hmo-button-large-10" type="submit" tabindex="3" value="비밀번호 변경" >
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