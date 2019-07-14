<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
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
							<a href="/a/reactivate" class="hmo-button hmo-button-blue hmo-button-large-10">재인증 받기</a>
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