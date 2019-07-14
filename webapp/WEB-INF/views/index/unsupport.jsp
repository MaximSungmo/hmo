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
<body  style="overflow-y:scroll">

<div class="rw-snn-fs">

	<div class="main-container container-fluid">

		<div class="main-content rw-signup-wizard-content langding-main-content">
			<div class="browser-information-navbar">
				<div class="browser-information-navbar-inner">
					<h1><a href="/"><span class="title-001">for your smart business,</span> <span class="title-002">"Hello my office"</span></a></h1>
				</div>
			</div>			
			<div class="browser-information">
				<div class="browser-information-logo-wrap">
					<img src="/assets/sunny/2.0/img/hellomyoffice_logo.png" alt="hellomyoffice-logo">
				</div>
				<div class="browser-information-content-container">
					<div class="browser-information-content">
						<div class="information-message">
							<div class="information-message-title">
								브라우저를 업그레이드 하세요!
							</div>
							<div class="information-message-description">
								아래에서 Hello My Office 지원 최신버전을 설치하시면<br> Hello My Office의 다양한 기능을 사용하실 수 있습니다.
							</div>
						</div>
						<div class="browser-information-link">
								<a class="ie-information" target="_blank" href="http://windows.microsoft.com/ko-kr/internet-explorer/download-ie">
									<i class="ie-image"></i>
									<div class="browser-image-text">Internet Explorer</div>
								</a>
								<a class="chrome-information" target="_blank" href="http://www.google.co.kr/intl/ko/chrome/browser/">
									<i class="chrome-image"></i>
									<div class="browser-image-text">Google Chrome</div>
								</a>
								<a class="firefox-information" target="_blank" href="http://www.mozilla.org/ko/firefox/new/">
									<i class="firefox-image"></i>
									<div class="browser-image-text">Mozila Firefox</div>
								</a>
						</div>
					</div>
				</div>
			</div>
			<div style="clear:both;"></div>			
			<div class="landing-footer">
				<div class="landing-footer-copy">
					<span class="landing-footer-subtitle">Copyright © 2014 <a href="http://www.sunnyvale.co.kr" target="_blank">Sunnyvale Corp.</a> All right reserved</span>
					<span class="landing-footer-Affiliates">제휴문의: <a href="http://www.sunnyvale.co.kr" target="_blank">(주)써니베일</a></span>
				</div>
			</div>
			
		</div>
		
	</div>

</div>	
</body>
</html>