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
			<div class="landing-navbar">
				<div class="landing-navbar-inner">
					<div class="navbar-logo-wrap">
						<h1>
							<a href="/">Hello My Office</a>
						</h1>
					</div>
					<div class="navbar-menu-wrap">
						<ul>
							<li class="landing-navbar-login">
								<a href="/login">로그인</a>
								<span class="navbar-menu-wrap-line"></span>
							</li>
							<li class="landing-navbar-signup">
								<a href="/a/signup">설정</a>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<%-- New landing page --%>
			<div class="landing-page-content-wrap"> 
				<div class="landing-page-content-b">
					<div class="landing-page-content-inner-b">
						<div class="lading-page-slogan">
							<h2>업무를 위한 커뮤니티 서비스<br>헬로마이오피스를 설치해 주셔서 감사합니다.</h2>
						</div>
						<%-- pc, 태블릿 화면 --%>
						<div class="lading-page-content-b">
							<div class="lading-page-experience">
								<h3>관리자 및 기본정보 설정</h3>
								<p>설치를 완료하기 위해  기본설정이 필요합니다.</p>
								<a class="lading-page-experience-button guest-login" href="/a/signup" >설정하기</a>
							</div>
						</div>	
						<%-- mobile 화면 --%>	
						<div class="lading-page-content-b-mobile">
							<div class="lading-page-experience">
								<h3>관리자 및 기본정보 설정</h3>
								<p>설치를 완료하기 위해  기본설정이 필요합니다.</p>
								<a class="lading-page-experience-button guest-login" href="/a/signup">설정하기</a>
							</div>
						</div>	
					</div>
					<div class="app-link-wrap">
						<a class="landing-android-link" href="https://play.google.com/store/apps/details?id=kr.co.sunnyvale.android.hmo.nc" target="_blank"></a>
						<a class="landing-ios-link" href="https://itunes.apple.com/us/app/hellomaiopiseu-hello-my-office/id882662778?mt=8" target="_blank"></a>
					</div>
					<form method="post" action="/user/auth" id="form-guest-login">
						<input type="hidden" name="username" value="guest1@sunny.com"/>
						<input type="hidden" name="password" value="1234"/>
					</form>				
				</div>
			</div>		
				
			<%-- 기존 landing page 주석처리 --%> 
			<%--
			<div class="landing-page-content-wrap" style="background-color: rgba(0,0,0,0.3);"> 
			<div class="page-content landing-page-content">
				<div class="landing-page-content-inner">
					<div class="landing-main-form">
						<div class="row-fluid landing-main-description-wrap">
							<div class="landing-span-wrap"> 
								<div class="lading-main-description-top">처음 방문하셨나요?</div>
								<div class="lading-main-description-bottom">헬로우 마이 오피스에서 여러분만의 오피스 공간을 만들어 효율적인 협업/소통을 하세요.</div>	
								<div class="landing-generate-company">
									<form action="/a/signup" method="get">						
										<input class="company-name" type="text" name="cname" placeholder="회사 이름">							
										<input type="submit" value="만들기">
									</form>
								</div>						
							</div>
						</div>
					</div>
				</div>
			</div>
			</div>			
			<div class="sunny-sns-content hmo-content-mobile-wrap">			
				<div class="sunny-content-01" style="margin:0 auto;">
					<div class="hmo-content-01-wrap">
						<img src="/assets/sunny/2.0/img/hmo-timeline-img.png" alt="다양한 콘텐츠를 담을 수 있는 스토리 작성과 효율적 공유를 위한 타임라인">
					</div>
					<div class="hmo-content-mobile-01">
						<h2>다양한 콘텐츠를 담을 수 있는 스토리 작성과 효율적 공유를 위한 타임라인</h2>
						<div class="hmo-content-mobile-01-wrap">
							<div class="hmo-content-mobile-img-wrap">
								<img src="/assets/sunny/2.0/img/hmo-mobile-timeline-img.png" alt="다양한 콘텐츠를 담을 수 있는 스토리 작성과 효율적 공유를 위한 타임라인">
							</div>
						</div>
					</div>
				</div>			
				<div class="sunny-content-02" style="margin:0 auto; clear:both;">
					<div class="hmo-content-02-wrap">
						<img src="/assets/sunny/2.0/img/hmo-channel-img.png" alt="회사 구성원들끼리의 협업을 위해 꼭 필요한 기능들">
					</div>
					<div class="hmo-content-mobile-02">
						<h2>회사 구성원들끼리의 협업을 위해 꼭 필요한 기능들</h2>
						<div class="hmo-content-mobile-02-wrap">
							<div class="hmo-content-mobile-img-wrap">
								<img src="/assets/sunny/2.0/img/hmo-mobile-menu-img.png" alt="회사 구성원들끼리의 협업을 위해 꼭 필요한 기능들">
							</div>
						</div>
					</div>
				</div>
				<div class="sunny-content-03" style="margin:0 auto; clear:both;">
					<div class="hmo-content-03-wrap">
						<img src="/assets/sunny/2.0/img/hmo-responsive-img.png" alt="다양한 디바이스에서 최적의 화면 제공">
					</div>
					<div class="hmo-content-mobile-03">
						<h2>다양한 디바이스에서 최적의 화면 제공</h2>
						<div class="hmo-content-mobile-03-wrap">
							<div class="hmo-content-mobile-img-wrap">
								<img src="/assets/sunny/2.0/img/hmo-mobile-responsive-img.png" alt="다양한 디바이스에서 최적의 화면 제공">
							</div>
						</div>
					</div>
				</div>				
				<div class="sunny-content-04" style="margin:0 auto; clear:both;">
					<div class="hmo-content-04-wrap">
						<img src="/assets/sunny/2.0/img/hmo-admin-img.png" alt="보다 간단하고 편리한 관리자 도구">
					</div>
					<div class="hmo-content-mobile-04">
						<h2>보다 간단하고 편리한 관리자 도구 </h2>
						<div class="hmo-content-mobile-04-wrap">			
							<div class="hmo-content-mobile-img-wrap">
								<img src="/assets/sunny/2.0/img/hmo-mobile-admin-img.png" alt="보다 간단하고 편리한 관리자 도구">
							</div>
						</div>
					</div>
				</div>						 
			</div>
			--%>
			
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