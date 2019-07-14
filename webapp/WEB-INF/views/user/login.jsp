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
							<div style="clear:both;"></div>
							<div class="exception-main-title hmo-signin login-box">
								<div class="hmo-signin-wrap">
									<h2>로그인</h2>
									<c:choose>
										<c:when test="${not empty param.username }">
										<div class="control-group error">
											<span class="help-inline">로그인 이메일 또는 비밀번호가 일치하지 않습니다.</span>
										</div>
										</c:when>
									</c:choose>
									<form method="post" action="/user/auth">
										<fieldset>												
											<label>
												<div class="login-form-place-holder">이메일</div>
												<span class="block input-icon input-icon-right">
													<input type="email" class="span12"name="username" value="${param.username}"/>
													<i class="fa fa-user fa-1g"></i>
												</span>
											</label>													
											<label>
												<div class="login-form-place-holder">비밀번호</div>
												<span class="block input-icon input-icon-right">
													<input type="password" class="span12" name="password"/>
													<i class="fa fa-lock fa-1g"></i>
												</span>
											</label>
											<%-- <div class="landing-toolbar-wrap landing-find-password">--%>
		
										<%--</div>--%>
											<div style="clear:both;"></div>
											<div class="clearfix landing-loginbox">
												<label class="inline hmo-inline">
													<input name="_sunny_remember_me" value="1" type="checkbox" class="ace" checked/>
													<span class="l-ft lbl hmo_remember_me"> 로그인상태 유지</span>
													<span class="l-ft landing-dot">·</span>
													<a href="/user/find_password" onclick="show_box('forgot-box'); return false;" class="l-ft landing-find-password-mobile forgot-password-link landing-forgot-password-link">
													<span>비밀번호 찾기</span>
													</a>
												</label>
												<button class="landing-width-34 pull-right hmo-button hmo-button-blue landing-button hmo-button-small-9">
													<span class="fwb">로그인</span>
												</button>
											</div>
										</fieldset>
									</form>
								</div>
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