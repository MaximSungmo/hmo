<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<title>Hello my office</title>
<!-- BEGIN:common head contents -->
<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/bootstrap.css" />
<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/bootstrap-responsive.css" />
<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/font-awesome.css" />
<!--[if IE 7]>
<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/font-awesome-ie7.min.css" />
<![endif]-->
<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/uncompressed/ace.css" />
<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/uncompressed/ace-responsive.css" />
<!-- END:common head contents -->
<link rel="stylesheet" href="/assets/sunny/2.0/css/uncompressed/hmo-front.css" />		
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
							<h2>이용에 불편을 드려 죄송합니다.</h2>
							<p class="exception-main-title-p"><span class="exception-main-title-sp">현재 요청하신 페이지로의 이동이 원활하지 않습니다.<br>
							본 메시지가 반복되는 경우 관리자에게 문의해 주시기 바랍니다.</span>
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




<%-- 
<div class="container">
	<div class="jumbotron">
		<h1>오류발생</h1>
		<p class="lead">오류가 발생했습니다. 관리자에게 문의해주세요.</p>
		<p>
			<a href="/" class="btn hmo-button-khaki btn-lg">메인으로 가기</a> 
		</p>
	</div>
</div>
<!-- /.container -->

--%>
