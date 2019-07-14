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
	<c:param name="title">폼</c:param>
	<c:param name="bsUsed">YES</c:param>
	<c:param name="aceUsed">YES</c:param>
	<c:param name="jsUsed">YES</c:param>
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

	<div class="main-container container-fluid site-list-container">
		<div class="main-content rw-signup-wizard-content">
			<div class="page-content login-page-content">
				<div id="site-accept-invite-form-wrap" class="row-fluid rw-signup-wizard-content-row-fluid" style="">
					<div class="span12">
						<div class="hmo-front-wrap">
							<div class="hmo-front-wrap-img">
								<img src="/assets/sunny/2.0/img/hellomyoffice_logo.png" alt="hellomyoffice-logo">
							</div>
						</div>
					<div class="site-list-signup-wrap">					
					<form id="form-site-signup" action="/site/${siteInactiveUser.site.id }/accept_invite" method="post" rel="sync" onsubmit="return window.Event&amp;&amp;Event.__inlineSubmit&amp;&amp;Event.__inlineSubmit(this,event);">
						<div class="row-fluid">
							<div class="span12">
								<div class="control-group">
									<label class="control-label" for="email">이메일</label>
									<div class="controls">
										<div class="">
											<span>${siteInactiveUser.email }</span>
										</div>
									</div>
								</div>
									
								<div class="control-group">
									<label class="control-label" for="name">이름</label>
									<div class="controls">
										<div class="row-fluid">
											<span class="span12 input-icon input-icon-right rw-input-wrap">
												<input class="span12" type="text" name="name" id="name" placeholder="이름을 넣어주세요" autocomplete="off" value="${siteInactiveUser.name }">
											</span>
										</div>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="password">비밀번호</label>
									<div class="controls">
										<div class="row-fluid">
											<span class="span12 input-icon input-icon-right rw-input-wrap">
												<input class="span12" type="password" name="password" id="password" placeholder="비밀번호를 넣어주세요">
											</span>
										</div>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="re-password">비밀번호 재입력</label>
									<div class="controls">
										<div class="row-fluid">
											<span class="span12 input-icon input-icon-right rw-input-wrap">
												<input class="span12" type="password" name="passwordConfirm" id="passwordConfirm" placeholder="비밀번호를 재입력해주세요">
											</span>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div style="border-top:1px solid #E5E5E5;">
						<div style="border-top:1px solid #fff;">
						<div style="padding-top:20px;">
						<a href="/site" class="hmo-button hmo-button-large-7 hmo-button-white">이전 화면</a>
						<input type="hidden" name="id" value="${siteInactiveUser.id }" />
						<button class="hmo-button hmo-button-blue hmo-button-large-7">확인</button>
						</div>
						</div>
						</div>
					</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>	
</div>
</body>
</html>
