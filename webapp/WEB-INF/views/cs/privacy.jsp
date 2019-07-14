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
							<div class="row-fluid">	
								<div class="span12">
									<div class="ins min-ht-m">
										<div id="global-container">
											<div id="cs-wrap">
												<div id="cs-container" class="_cs">
													<div id="cs-main-navi">
														<div class="cs-main-navi-bottom">
															<div class="cs-navigation">
																<ul class="cs-navigation-ul">
																	<li class="privacy select cs-left-menu"><a href="/cs/privacy">개인정보취급방침</a></li>
																	<li class="terms"><a href="/cs/policies">이용약관</a></li>
																</ul>
															</div>
														</div>
													</div>
													<div id="cs-visual">
														<span class="cs-service-top">개인정보 취급방침</span>
													</div>
													<div id="cs-content">
														<div class="privacy-content-top">
															<p>(주)써니베일 (이하 "회사" 또는 "Hello My Office"라 함)은 정보통신망 이용촉진 및 정보보호 등에 관한 법률, 개인정보보호법, 통신비밀보호법, 전기통신사업법, 등 정보통신서비스제공자가 준수하여야 할 관련 법령상의 개인정보보호 규정을 준수하며, 관련 법령에 의거한 개인정보취급방침을 정하여 이용자 권익 보호에 최선을 다하고 있습니다. 
															본 개인정보취급방침은 써니베일에서 제공하는 서비스에 적용되며 다음과 같은 내용을 담고 있습니다.</p>
															<a class="print" onclick="window.print()" href="#print">
																<img width="71" height="22" alt="인쇄하기" src="/assets/sunny/2.0/img/btn-print.gif">
															</a>
															<div class="dl">
																<dl class="dl1">
																	<dt>1.</dt>
																	<dd>
																		<a href="#a1">수집하는 개인정보의 항목 및 수집방법 </a>
																	</dd>
																	<dt>2.</dt>
																	<dd>
																		<a href="#a2">개인정보의 수집 및 이용목적</a>
																	</dd>
																	<dt>3.</dt>
																	<dd>
																		<a href="#a3">개인정보 공유 및 제공</a>
																	</dd>	
																	<dt>4.</dt>
																	<dd>
																		<a href="#a4">개인정보의 취급위탁</a>
																	</dd>
																	<dt>5.</dt>
																	<dd>
																		<a href="#a5">개인정보의 보유 및 이용기간</a>
																	</dd>
																	<dt>6.</dt>
																	<dd>
																		<a href="#a6">개인정보 파기절차 및 방법 </a>
																	</dd>												
																</dl>
																<dl class="dl2">
																	<dt>7.</dt>
																	<dd>
																		<a href="#a7">이용자 및 법정대리인의 권리와 그 행사방법</a>
																	</dd>
																	<dt>8.</dt>
																	<dd>
																		<a href="#a8">개인정보 자동 수집 장치의 설치/운영 및 거부에 관한 사항 </a>
																	</dd>
																	<dt>9.</dt>
																	<dd>
																		<a href="#a9">개인정보의 기술적/관리적 보호 대책 </a>
																	</dd>	
																	<dt class="pull1">10.</dt>
																	<dd>
																		<a href="#a10">개인정보관리책임자 및 담당자의 연락처</a>
																	</dd>
																	<dt class="pull2">11.</dt>
																	<dd>
																		<a href="#a11">기타 </a>
																	</dd>
																	<dt class="pull3">12.</dt>
																	<dd>
																		<a href="#a12">고지의 의무 </a>
																	</dd>							
																</dl>
															</div>
														</div>
														<div style="clear:both;"></div>
														<div class="privacy-content-bottom">
															<h4 class="first">
																<a name="a1">1. 수집하는 개인정보의 항목 및 수집방법 </a>
															</h4>
															<h5>가. 수집하는 개인정보의 항목</h5>
															<p class="depth2">회사는 회원가입(또는 서비스 가입), 원활한 고객상담, 각종 서비스의 제공을 위해 Hello My Office 서비스 최초 이용시 다음과 같은 개인정보 수집하고 있습니다.
															</p>
																<ul class="depth2 ex">
																	<li>
																		<span class="spacing">-</span>
																		필수: 이름, 생일, 휴대폰 번호, 단말기 정보
																	</li>
																	<li>
																		<span class="spacing">-</span>
																		선택: 프로필 사진, 멤버로 초대한 개별 지인의 이름 및 휴대폰 번호
																	</li>
																</ul>
															<p class="depth2">서비스 이용과정이나 사업처리 과정에서 아래와 같은 정보들이 자동으로 생성되어 수집될 수 있습니다.</p>
																<ul class="depth2">
																	<li>- IP Address, 쿠키, 방문 일시, 서비스 이용 기록, 불량 이용 기록</li>
																</ul>															
															<h5>나. 개인정보 수집방법</h5>
															<p class="depth2">회사는 다음과 같은 방법으로 개인정보를 수집합니다.</p>
																<ul class="depth2">
																	<li>- Hello My Office 프로그램의 실행 또는 사용 과정에서 수집</li>
																	<li>- 협력회사로부터의 제공</li>
																	<li>- 생성정보 수집 툴을 통한 수집</li>
																</ul>
															<h4>
																<a name="a2">2. 개인정보의 수집 및 이용목적 </a>
															</h4>
															<p class="depth2">회사는 수집한 개인정보를 다음과 같은 목적으로 이용합니다</p>
															<h5>가. Hello My Office 서비스 제공</h5>
															<p class="depth2">이용자가 가입한 Hello My Office 내 상호 식별, 이용자가 설정한 연결수단으로 계정연결 및 사용자초대</p>
															<h5>나. 회원관리</h5>
															<p class="depth2">회원제 서비스 제공, 회원식별, 불량회원(Hello My Office 이용약관 제18조 제1항 및 제2항 따라 이용제한된 회원)의 부정 이용방지와 비인가 사용방지, 가입의사 확인, 분쟁 조정을 위한 기록보존, 불만처리 등 민원처리, 고지사항 전달</p>
															<h5>다. 신규 서비스 개발 및 <strong class="emp">마케팅·광고에의 활용</strong></h5>
															<p class="depth2">신규 서비스 개발 및 맞춤 서비스 제공, 통계학적 특성에 따른 서비스 제공 및 광고 게재, 서비스의 유효성 확인, 이벤트 참여기회 제공 또는 <strong class="emp">광고성 정보 제공</strong>, 접속빈도 파악, 회원의 서비스이용에 대한 통계</p>
															<h4>
																<a name="a3">3. 개인정보의 공유 및 제공 </a>
															</h4>
															<h5>회사는 이용자들의 개인정보를 원칙적으로 "2. 개인정보의 수집목적 및 이용목적"에서 고지한 범위 내에서 사용하며, 이에 따라 서비스 이용과정에서 다음의 정보가 공개될 수 있습니다.</h5>
																<ul class="depth2">
																	<li>- Hello My Office 서비스 이용 도중 상호간 식별을 위해 이름, 프로필 사진이 Hello My Office 내 이용자간에 공개됩니다. 또한 이용자 설정에 따라 생일 및 휴대폰 번호가 공개될 수 있습니다.</li>
																	<li>- 이용자가 사용자를 초대할 경우 초대자 식별을 위해 ‘이름, 프로필 사진’이 공개됩니다.</li>																	
																</ul>
															<h5>상기 목적 이외에는 이용자의 사전 동의 없이 "2. 개인정보의 수집목적 및 이용목적"에서 고지한 범위를 넘어서 이용하거나 원칙적으로 이용자의 개인정보를 외부에 공개하지 않습니다. 단, 아래의 경우에는 예외로 합니다.</h5>	
																<ul class="depth2">
																	<li>- 이용자가 사전에 동의한 경우</li>
																	<li>- 제휴한 제 3자의 서비스 이용을 위해 이용자가 사전에 동의한 경우</li>	
																	<li>제휴한 서비스에 제공하는 개인정보는 이용자 이름 및 프로필 사진, 서비스를 제공하기 위해 반드시 필요한 정보로 한정합니다. 동의 시점에 서비스마다 제공되는 개인정보의 내용을 안내해 드립니다. 제공되는 개인정보의 내용은 서비스를 제공하면서 추가/변경 될 수 있으며, 제휴 서비스를 사용하기 위해 필요한 개인정보가 변경되면 서비스 이용시 추가로 동의를 받습니다.</li>	
																	<li>- 법령의 규정에 의거하거나, 수사 목적으로 법령에 정해진 절차와 방법에 따라 수사기관의 요구가 있는 경우</li>																	
																</ul>
															<h4>
																<a name="a4">4. 개인정보의 취급위탁</a>
															</h4>
															<p class="depth1 mt20">
																회사는 서비스 향상을 위해서 아래와 같이 개인정보를 위탁하고 있으며, 관계 법령에 따라 위탁계약 시 개인정보가 안전하게 관리될 수 있도록 필요한 사항을 규정하고 있습니다.
															</p>
															<h4>
																<a name="a5">5. 개인정보의 보유 및 이용기간</a>
															</h4>
															<p class="depth1 mt20">
																이용자의 개인정보는 원칙적으로 계정삭제시 또는 개인정보의 수집 및 이용목적 달성시 지체없이 파기합니다. 단, 관계법령의 규정에 의하여 보존할 필요가 있는 경우 회사는 관계법령에서 정한 일정한 기간 동안 회원정보를 보관합니다.
															</p>
															<h4>
																<a name="a6">6. 개인정보 파기절차 및 방법</a>
															</h4>
															<p class="depth1 mt20">
																이용자의 개인정보는 원칙적으로 개인정보의 수집 및 이용목적이 달성되면 지체 없이 파기합니다. 회사의 개인정보 파기절차 및 방법은 다음과 같습니다.
															</p>
															<h5>
																가. 파기절차
															</h5>
																<ul class="depth2 mt10 ex">
																	<li>
																		<span class="spacing">-</span> 이용자가 회원가입 등을 위해 입력한 정보는 목적이 달성된 후 별도의 DB로 옮겨져(종이의 경우 별도의 서류함) 내부 방침 및 기타 관련 법령에 의한 정보보호 사유에 따라(보유 및 이용기간 참조)일정 기간 저장된 후 파기됩니다.
																	</li>
																	<li>
																		<span class="spacing">-</span> 동 개인정보는 법률에 의한 경우가 아니고서는 보유되는 이외의 다른 목적으로 이용되지 않습니다.
																	</li>
																</ul>
															<h5>
																나. 파기방법
															</h5>
																<ul class="depth2 mt10">
																	<li>종이에 출력된 개인정보는 분쇄기로 분쇄하거나 소각을 통하여 파기합니다.</li>
																	<li>전자적 파일 형태로 저장된 개인정보는 기록을 재생할 수 없는 기술적 방법을 사용하여 삭제합니다.</li>
																</ul>
															<h4>
																<a name="a7">7. 이용자의 권리와 그 행사방법</a>
															</h4>
																<ul class="depth1 mt20 ex">
																	<li>
																		<span class="spacing">-</span> 이용자는 언제든지 등록되어 있는 자신의 개인정보를 조회하거나 수정할 수 있으며, 회사의 개인정보의 처리에 동의하지 않는 경우 동의를 거부하거나 가입해지(회원탈퇴)를 요청하실 수 있습니다. 다만, 그러한 경우 서비스의 일부 또는 전부 이용이 어려울 수 있습니다.
																	</li>
																	<li>
																		<span class="spacing">-</span> 이용자가 자신의 개인정보를 조회하거나, 수정하기 위해서는 애플리케이션 내 '설정> 회원정보수정'을, 가입해지(동의철회)를 위해서는 "설정> 계정삭제"를 클릭하여 직접 열람, 정정 또는 탈퇴가 가능합니다. 혹은 개인정보관리책임자에게 서면, 전화 또는 이메일로 연락하시면 지체 없이 조치하겠습니다.
																	</li>
																	<li>
																		<span class="spacing">-</span> 이용자가 개인정보의 오류에 대한 정정을 요청하신 경우에는 정정을 완료하기 전까지 당해 개인정보를 이용 또는 제공하지 않습니다. 또한 잘못된 개인정보를 제3자에게 이미 제공한 경우에는 정정 처리결과를 제3자에게 지체 없이 통지하여 정정이 이루어지도록 하겠습니다.
																	</li>
																	<li>
																		<span class="spacing">-</span> 회사는 이용자의 요청에 의해 해지 또는 삭제된 개인정보는 "5. 개인정보의 보유 및 이용기간"에 명시된 바에 따라 처리하고 그 외의 용도로 열람 또는 이용할 수 없도록 처리하고 있습니다.  
																	</li>
																</ul>
															<h4>
																<a name="a8">8. 개인정보 자동 수집 장치의 설치/운영 및 그 거부에 관한 사항</a>
															</h4>
															<h5>가. 쿠키란?</h5>
																<ul class="depth2 mt10 ex">
																	<li>
																		<span class="spacing">-</span> 회사는 개인화되고 맞춤화된서비스를 제공하기 위해서 이용자의 정보를 저장하고 수시로 불러오는 '쿠키(cookie)'를 사용합니다.
																	</li>
																	<li>
																		<span class="spacing">-</span> 쿠키는 웹사이트를 운영하는데 이용되는 서버가 이용자의 브라우저에게 보내는 아주 작은 텍스트 파일로 이용자 컴퓨터의 하드디스크에 저장됩니다. 이후 이용자가 웹 사이트에 방문할 경우 웹 사이트 서버는 이용자의 하드 디스크에 저장되어 있는 쿠키의 내용을 읽어 이용자의 환경설정을 유지하고 맞춤화된 서비스를 제공하기 위해 이용됩니다.
																	</li>
																	<li>
																		<span class="spacing">-</span> 쿠키는 개인을 식별하는 정보를 자동적/능동적으로 수집하지 않으며, 이용자는 언제든지 이러한 쿠키의 저장을 거부하거나 삭제할 수 있습니다
																	</li>
																</ul>
															<h5>나. 회사의 쿠키 사용 목적</h5>
															<p class="depth2 mt10">
																이용자들이 Hello My Office의 PC 또는 모바일 웹 사이트를 방문할 때 쿠키의 정보를 활용하여 웹 사이트 사용을 설정한대로 접속하고 편리하게 사용할 수 있도록 돕습니다. 또 이용자의 웹사이트 방문 기록, 이용 형태를 통해서 최적화된 광고 등 맞춤형 정보를 제공하기 위해 사용합니다.
															</p>
															<h5>다. 쿠키의 설치/운영 및 거부</h5>
																<ul class="depth2 mt10 ex">
																	<li>
																		<span class="spacing">-</span>이용자는 쿠키 설치에 대한 선택권을 가지고 있습니다. 따라서 이용자는 웹브라우저에서 옵션을 설정함으로써 모든 쿠키를 허용하거나, 쿠키가 저장될 때마다 확인을 거치거나, 아니면 모든 쿠키의 저장을 거부할 수도 있습니다.
																	</li>
																	<li>
																		<span class="spacing">-</span>다만, 쿠키의 저장을 거부할 경우에는 로그인이 필요한 부분 등 서비스의 일부의 이용에 어려움이 있을 수 있습니다.
																	</li>											
																</ul>
															<h4>
																<a name="a9">9. 개인정보의 기술적/관리적 보호 대책 </a>
															</h4>
															<p class="depth1 mt20">
																회사는 이용자들의 개인정보를 취급함에 있어 개인정보가 분실, 도난, 누출, 변조 또는 훼손되지 않도록 안전성 확보를 위하여 다음과 같은 기술적/관리적 대책을 강구하고 있습니다.
															</p>
															<h5>가. 개인정보의 암호화</h5>
															<p class="depth2">
																회사는 관련 법률규정 또는 내부정책에 따라 개인정보를 암호화하여 안전하게 저장 및 관리하고 있습니다.
															</p>
															<h5>나. 해킹 등에 대비한 대책</h5>
															<p class="depth2">
																회사는 해킹이나 컴퓨터 바이러스 등에 의해 회원의 개인정보가 유출되거나 훼손되는 것을 막기 위해 최선을 다하고 있습니다.<br />
																개인정보의 훼손에 대비해서 자료를 수시로 백업하고 있고, 최신 백신프로그램을 이용하여 이용자들의 개인정보나 자료가 누출되거나 손상되지 않도록 방지하고 있으며, 암호화통신 등을 통하여 네트워크상에서 개인정보를 안전하게 전송할 수 있도록 하고 있습니다.<br />
																그리고 침입차단시스템을 이용하여 외부로부터의 무단 접근을 통제하고 있으며, 기타 시스템적으로 보안성을 확보하기 위한 가능한 모든 기술적 장치를 갖추려 노력하고 있습니다.
															</p>
															<h5>다. 취급 직원의 최소화 및 교육</h5>
															<p class="depth2">
																회사의 개인정보관련 취급 직원은 담당자에 한정시키고 있고 이를 위한 별도의 비밀번호를 부여하여 정기적으로 갱신하고 있으며, 담당자에 대한 수시 교육을 통하여 써니베일 모바일 개인정보취급방침의 준수를 항상 강조하고 있습니다.
															</p>
															<h5>라. 개인정보보호전담기구의 운영</h5>
															<p class="depth2">
																그리고 사내 개인정보보호전담기구 등을 통하여 Hello My Office 개인정보취급방침의 이행사항 및 담당자의 준수여부를 확인하여 문제가 발견될 경우 즉시 수정하고 바로 잡을 수 있도록 노력하고 있습니다.<br />
																단, 이용자 본인의 부주의나 인터넷상의 문제로 ID, 비밀번호 등 개인정보가 유출되어 발생한 문제에 대해 회사는 일체의 책임을 지지 않습니다.
															</p>
															<h4>
																<a name="a10">10. 개인정보관리책임자 및 담당자의 연락처</a>
															</h4>
															<p class="depth1 mt20">
																귀하께서는 회사의 서비스를 이용하시며 발생하는 모든 개인정보보호 관련 민원을 개인정보관리책임자 혹은 담당부서로 신고하실 수 있습니다.<br />
																회사는 이용자들의 신고사항에 대해 신속하게 충분한 답변을 드릴 것입니다.
															</p>
															<div class="contact">
																<table>
																	<thead>
																		<tr>
																			<th colspan="2" scope="col">개인정보 관리책임자</th>
																		</tr>
																	</thead>
																	<tbody>
																		<tr>
																			<td class="col1">이 름 : 안대혁</td>	
																			<td class="col2">소 속 : 개발팀</td>	
																		</tr>
																		<tr>
																			<td class="col1">전 화 : 02-2038-7772</td>	
																			<td class="col2">직 위 : 이사</td>			
																		</tr>
																		<tr>
																			<td class="col1">
																				메 일 : <a href="mailto:support@sunnyvale.co.kr">support@sunnyvale.co.kr</a>        
																			</td>
																			<td class="col2">&nbsp;</td>
																			<td class="col4">&nbsp;</td>
																		</tr>
																	</tbody>
																</table>
															</div>
															<p class="depth1">기타 개인정보침해에 대한 신고나 상담이 필요하신 경우에는 아래 기관에 문의하시기 바랍니다.</p>
																<ul class="depth1">
																	<li>- 개인정보침해신고센터 (<a href="http://www.118.or.kr" target="_blank">www.118.or.kr</a>/ 국번없이 118)</li>
																	<li>- 대검찰청 사이버범죄수사단 (<a href="http://www.spo.go.kr" target="_blank">www.spo.go.kr</a>/ 02-3480-2000) </li>
																	<li>- 경찰청 사이버테러대응센터 (<a href="http://www.ctrc.go.kr" target="_blank">www.ctrc.go.kr</a>/ 1566-0112) </li>
																</ul>
															<h4>
																<a name="a11">11. 기타</a>
															</h4>
															<p class="depth1 mt20">
																Hello My Office 서비스 내에 링크되어 있는 웹사이트들이 개인정보를 수집하는 행위에 대해서는 본 "Hello My Office 개인정보취급방침"이 적용되지 않음을 알려 드립니다.
															</p>
															<h4>
																<a name="a12">12. 고지의 의무</a>
															</h4>
															<p class="depth1 mt20">
																현 개인정보취급방침 내용 추가, 삭제 및 수정이 있을 시에는 개정 최소 7일전부터 회사 홈페이지, 서비스 내의 '공지사항' 또는 기타 알기쉬운 방법을 통해 고지할 것입니다. 다만, 개인정보의 수집 및 활용, 제3자 제공 등과 같이 이용자 권리의 중요한 변경이 있을 경우에는 최소 30일 전에 고지합니다. 
															</p>
															<br />
																<ul class="depth1">
																	<li>- 공고일자 : 2014년 3월 13일</li>
																	<li>- 시행일자 : 2014년 3월 21일</li>
																</ul>
														</div>
													</div>
													<div id="gotop" class="gotop">
														<a href="#cs-container">top</a>
													</div>		
												</div>
											</div>
											<div id="footer">
											</div>
										</div>
									</div>
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






















