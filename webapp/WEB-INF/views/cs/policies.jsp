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
							<div class="ins min-ht-m">
								<div id="global-container">
									<div id="cs-wrap">
										<div id="cs-container" class="_cs">
											<div id="cs-main-navi">
												<div class="cs-main-navi-bottom">
													<div class="cs-navigation">
														<ul class="cs-navigation-ul">
															<li class="privacy cs-left-menu"><a href="${sitePath }/cs/privacy">개인정보취급방침</a></li>
															<li class="terms select"><a href="${sitePath }/cs/policies">이용약관</a></li>
														</ul>
													</div>
												</div>
											</div>
											<div id="cs-visual">
													<span class="cs-service-top">이용약관</span>
											</div>
											<div id="cs-content">
												<div class="policies-content-top">
													<a class="print" onclick="window.print()" href="#print">
														<img width="71" height="22" alt="인쇄하기" src="/assets/sunny/2.0/img/btn-print.gif">
													</a>
													<div class="dl policies-content-top-list">
														<dl class="dl1">
															<dt>제  1조</dt>
															<dd>
																<a href="#a1">목적</a>
															</dd>
															<dt>제  2조</dt>
															<dd>
																<a href="#a2">정의</a>
															</dd>
															<dt>제 3조</dt>
															<dd>
																<a href="#a3">약관의 게시와 개정</a>
															</dd>	
															<dt>제 4조</dt>
															<dd>
																<a href="#a4">약관의 해석</a>
															</dd>
															<dt>제 5조</dt>
															<dd>
																<a href="#a5">이용계약 체결</a>
															</dd>
															<dt>제 6조</dt>
															<dd>
																<a href="#a6">회원정보의 변경</a>
															</dd>
															<dt>제 7조</dt>
															<dd>
																<a href="#a7">개인정보보호 의무</a>
															</dd>
															<dt>제 8조</dt>
															<dd>
																<a href="#a8">"회원"에 대한 통지</a>
															</dd>
															<dt>제 9조</dt>
															<dd>
																<a href="#a9">"회사"의 의무</a>
															</dd>
															<dt>제 10조</dt>
															<dd>
																<a href="#a10">"회원"의 의무</a>
															</dd>						
														</dl>
														<dl class="dl2">
															<dt>제 11조</dt>
															<dd>
																<a href="#a11">"서비스"의 제공 등</a>
															</dd>
															<dt>제 12조</dt>
															<dd>
																<a href="#a12">"서비스"의 변경</a>
															</dd>
															<dt>제 13조</dt>
															<dd>
																<a href="#a13">정보의 제공 및 광고의 게재</a>
															</dd>	
															<dt>제 14조</dt>
															<dd>
																<a href="#a14">"게시물"의 저작권</a>
															</dd>
															<dt>제 15조</dt>
															<dd>
																<a href="#a15">"게시물"의 관리</a>
															</dd>
															<dt>제 16조</dt>
															<dd>
																<a href="#a16">권리의 귀속</a>
															</dd>
															<dt>제 17조</dt>
															<dd>
																<a href="#a17">계약해제, 해지 등</a>
															</dd>
															<dt>제 18조</dt>
															<dd>
																<a href="#a18">이용제한 등</a>
															</dd>
															<dt>제 19조</dt>
															<dd>
																<a href="#a19">책임제한</a>
															</dd>
															<dt>제 20조</dt>
															<dd>
																<a href="#a20">준거법 및 재판관할</a>
															</dd>	
															<dt></dt>
															<dd>
																<a href="#a21">부칙</a>
															</dd>
														</dl>
													</div>
												</div>
												<div class="policies-content-bottom">
													<h3 class="first">
														<a name="a1"> 제 1 조 (목적)</a>
													</h3>
													<p>
														이 약관은 (주)써니베일(이하 "회사")가 제공하는 Hello My Office서비스의 이용과 관련하여 회사와 회원과의 권리, 의무 및 책임사항, 기타 필요한 사항을 규정함을 목적으로 합니다. 
													</p>
													<h3 >
														<a name="a2">제 2 조 (정의)</a>
													</h3>
													<p>
														이 약관에서 사용하는 용어의 정의는 다음과 같습니다.
													</p>
														<ol>
															<li>
																<span class="spacing">1.</span>"서비스"라 함은 설치되어 있는 단말기(PC, TV, 휴대형단말기 등의 각종 유무선 장치를 포함)와 상관없이 "회원"이 이용할 수 있는 Hello My Office 및 Hello My Office 관련 제반 서비스를 의미합니다.
															</li>
															<li>
																<span class="spacing">2.</span>"회원"이라 함은 회사의 "서비스"에 접속하여 이 약관에 따라 "회사"와 이용계약을 체결하고 "회사"가 제공하는 "서비스"를 이용하는 고객을 말합니다.
															</li>
															<li>
																<span class="spacing">3.</span>"게시물"이라 함은 "회원"이 "서비스"를 이용함에 있어 "서비스상"에 게시한 부호ㆍ문자ㆍ음성ㆍ음향ㆍ화상ㆍ동영상 등의 정보 형태의 글, 사진, 동영상 및 각종 파일과 링크 등을 의미합니다.														
															</li>
														</ol>
													<h3>
														<a name="a3">제 3 조 (약관의 게시와 개정)</a>
													</h3>
														<ol>
															<li>
																<span class="spacing">1.</span>"회사"는 이 약관의 내용을 "회원"이 쉽게 알 수 있도록 서비스 초기 화면에 게시합니다.
															</li>
															<li>
																<span class="spacing">2.</span>"회사"는 관련법령을 위배하지 않는 범위에서 이 약관을 개정할 수 있습니다. "회사"가 약관을 개정할 경우에는 적용일자 및 개정사유를 명시하여 현행약관과 함께 제1항의 방식에 따라 그 개정약관의 적용일자 30일 전부터 공지합니다. 다만, “회원”에게 불리한 약관의 개정의 경우에는 일정기간 서비스 내 공지하고, SMS, 로그인시 동의창 등의 전자적 수단을 통해 따로 명확히 통지하도록 합니다.
															</li>
															<li>
																<span class="spacing">3.</span>"회사"가 전항에 따라 개정약관을 공지 또는 통지하면서 “회원”에게 30일 기간 내에 의사표시를 하지 않으면 의사표시가 표명된 것으로 본다는 뜻을 명확하게 공지 또는 통지하였음에도 “회원”이 명시적으로 거부의 의사표시를 하지 아니한 경우 “회원”이 개정약관에 동의한 것으로 봅니다.													
															</li>
															<li>
																<span class="spacing">4.</span>"회사"이 개정약관의 적용에 동의하지 않는 경우 “회사”는 개정 약관의 내용을 적용할 수 없으며, 이 경우 “회원”은 이용계약을 해지할 수 있습니다. 다만, 기존 약관을 적용할 수 없는 특별한 사정이 있는 경우에는 “회사”는 이용계약을 해지할 수 있습니다.														
															</li>
															<li>
																<span class="spacing">5.</span>이 약관의 일부가 집행 불능으로 판단되더라도 나머지 부분은 계속해서 효력을 갖습니다.														
															</li>
														</ol>
													<h3 >
														<a name="a4">제 4 조 (약관의 해석)</a>
													</h3>
														<ol>
															<li>
																<span class="spacing">1.</span>본 약관에서 사용하는 용어의 정의는 다음과 같습니다.
																<ol>
																	<li>
																		<span class="spacing">①.</span>"회사"는 개별 서비스에 대해서는 별도의 이용약관 및 운영정책(이하 "운영정책 등")을 둘 수 있으며, 해당 내용이 이 약관과 상충할 경우에는 "운영정책 등"이 우선하여 적용됩니다.
																	</li>
																	<li>
																		<span class="spacing">②.</span>이 약관에서 정하지 아니한 사항이나 해석에 대해서는 "운영정책 등" 및 관계법령 또는 상관례에 따릅니다.
																	</li>								
																</ol>
															</li>
														</ol>
													<h3 >
														<a name="a5">제 5 조 (이용계약 체결)</a>
													</h3>
														<ol>
															<li>
															<span class="spacing">1.</span>이용계약은 "회원"이 되고자 하는 자(이하 "가입신청자")가 약관의 내용에 대하여 동의를 한 다음 회원가입신청을 하고 "회사"가 이러한 신청에 대하여 승낙함으로써 체결됩니다.
															</li>
															<li>
															<span class="spacing">2.</span>"회사"는 "가입신청자"의 신청에 대하여 "서비스" 이용을 승낙함을 원칙으로 합니다. 다만, "회사"는 다음 각 호에 해당하는 신청에 대하여는 승낙을 하지 않거나 사후에 이용계약을 해지할 수 있습니다.
																<ol>
																	<li>
																		<span class="spacing">①.</span>가입신청자가 이 약관에 의하여 이전에 회원자격을 상실한 적이 있는 경우, 단 "회사"의 회원 재가입 승낙을 얻은 경우에는 예외로 함.
																	</li>
																	<li>
																		<span class="spacing">②.</span>허위의 정보를 기재하거나, "회사"가 제시하는 내용을 기재하지 않은 경우
																	</li>
																	<li>
																		<span class="spacing">③.</span>"회원"의 귀책사유로 인하여 승인이 불가능하거나 기타 규정한 제반 사항을 위반하며 신청하는 경우
																	</li>
																</ol>
															</li>
															<li>
															<span class="spacing">3.</span>"회사"는 서비스관련설비의 여유가 없거나, 기술상 또는 업무상 문제가 있는 경우에는 승낙을 유보할 수 있습니다.
															</li>
															<li>
															<span class="spacing">4.</span>제2항과 제3항에 따라 회원가입신청의 승낙을 하지 아니하거나 유보한 경우, "회사"는 원칙적으로 이를 가입신청자에게 알리도록 합니다.
															</li>
															<li>
															<span class="spacing">5.</span>이용계약의 성립 시기는 "회사"가 가입완료를 신청절차 상에서 표시한 시점으로 합니다.
															</li>
															<li>
															<span class="spacing">6.</span>"회사"는 "회원"에 대해 회사정책에 따라 등급별로 구분하여 이용시간, 이용횟수, 서비스 메뉴 등을 세분하여 이용에 차등을 둘 수 있습니다.
															</li>
														</ol>
													<h3 >
														<a name="a6">제 6 조 (회원정보의 변경)</a>
													</h3>
														<ol>
															<li>														
																<ol>
																	<li>
																		<span class="spacing">1.</span>"회원"은 개인정보관리화면을 통하여 언제든지 본인의 개인정보를 열람하고 수정할 수 있습니다.
																	</li>
																	<li>
																		<span class="spacing">2.</span>"회원"은 회원가입신청 시 기재한 사항이 변경되었을 경우 온라인으로 수정을 하여 "회사"에 대하여 그 변경사항을 알려야 합니다.
																	</li>
																	<li>
																		<span class="spacing">3.</span>제2항의 변경사항을 "회사"에 알리지 않아 발생한 불이익에 대하여 "회사"는 책임지지 않습니다.
																	</li>																	
																</ol>
															</li>															
														</ol>
													<h3 >
														<a name="a7">제 7 조 (개인정보보호 의무)</a>
													</h3>
													<p>
														"회사"는 관계 법령이 정하는 바에 따라 "회원"의 개인정보를 보호하기 위해 노력합니다. 개인정보의 보호 및 사용에 대해서는 관련법 및 "회사"의 개인정보취급방침이 적용됩니다. 다만, "회사"의 공식 사이트 이외의 링크된 사이트에서는 "회사"의 개인정보취급방침이 적용되지 않습니다.
													</p>
													<h3 >
														<a name="a8">제 8 조 ("회원"에 대한 통지)</a>
													</h3>
														<ol>
															<li>
																<span class="spacing">1.</span>"회사"가 "회원"에 대한 통지를 하는 경우 이 약관에 별도 규정이 없는 한 SMS, 기타 “회사”가 적당하다고 판단하는 방법으로 할 수 있습니다.
															</li>
															<li>
																<span class="spacing">2.</span>"회사"는 "회원" 전체에 대한 통지의 경우 7일 이상 "회사"의 게시판에 게시함으로써 제1항의 통지에 갈음할 수 있습니다.
															</li>
														</ol>
													<h3 >
														<a name="a9">제 9 조 ("회사"의 의무)</a>
													</h3>
														<ol>
															<li>
																<span class="spacing">1.</span>"회사"는 관련법과 이 약관이 금지하거나 미풍양속에 반하는 행위를 하지 않으며, 계속적이고 안정적으로 "서비스"를 제공하기 위하여 최선을 다하여 노력합니다.
															</li>
															<li>
																<span class="spacing">2.</span>"회사"는 "회원"이 안전하게 "서비스"를 이용할 수 있도록 개인정보(신용정보 포함)보호를 위해 보안시스템을 갖추어야 하며 개인정보취급방침을 공시하고 준수합니다.
															</li>
															<li>
																<span class="spacing">3.</span>"회사"는 서비스이용과 관련하여 "회원"으로부터 제기된 의견이나 불만이 정당하다고 인정할 경우에는 이를 처리하여야 합니다. "회원"이 제기한 의견이나 불만사항에 대해서는 게시판을 활용하거나 기타 “회사”가 적당하다고 판단하는 방법 등을 통하여 "회원"에게 처리과정 및 결과를 전달합니다.
															</li>
														</ol>
													<h3 >
														<a name="a10">제 10 조 ("회원"의 의무)</a>
													</h3>
														<ol>
															<li>
																<span class="spacing">1.</span>"회원"은 다음 행위를 하여서는 안 됩니다.
																<ol>
																	<li>
																		<span class="spacing">①.</span>신청 또는 변경 시 허위내용의 등록<br>
																		<span class="spacing">②.</span>타인의 정보도용<br>
																		<span class="spacing">③.</span>"회사"가 게시한 정보의 변경<br>
																		<span class="spacing">④.</span>"회사"가 정한 정보 이외의 정보(컴퓨터 프로그램 등) 등의 송신 또는 게시<br>
																		<span class="spacing">⑤.</span>"회사"와 기타 제3자의 저작권 등 지적재산권에 대한 침해<br>
																		<span class="spacing">⑥.</span>"회사" 및 기타 제3자의 명예를 손상시키거나 업무를 방해하는 행위<br>
																		<span class="spacing">⑦.</span>외설 또는 폭력적인 메시지, 화상, 음성, 기타 공서양속에 반하는 정보를 "서비스"에 공개 또는 게시하는 행위<br>
																		<span class="spacing">⑧.</span>“회사” 나 제3자를 사칭하거나 또는 타인의 명의를 도용하는 행위<br>
																		<span class="spacing">⑨.</span>자기 또는 타인에게 재산상의 이익을 주거나 타인에게 손해를 가할 목적으로 허위의 정보를 유통시키는 행위<br>
																		<span class="spacing">⑩.</span>본 “서비스”를 불건전 교제를 조장•매개하기 위한 목적으로 이용하거나, 본 “서비스”를 이용하여 개인이나 단체를 비방하고 명예를 훼손하는 등 등 기타 본 “서비스”를 본 목적에 어긋나게 이용하는 행위<br>
																		<span class="spacing">⑪.</span>다른 “회원”의 개인정보, 등록 정보, 이용 이력 정보 등을 무단으로 수집, 공개 또는 제공하는 행위<br>
																		<span class="spacing">⑫.</span>본인의 계정에 타인이 접속하도록 하는 등 “회원”의 계정 보안을 위협할 수 있는 행위<br>
																		<span class="spacing">⑬.</span>“회사”의 동의 없이 영리를 목적으로 "서비스"를 사용하는 행위<br>
																		<span class="spacing">⑭.</span>상기에 해당되는 행위를 방조 또는 조장하는 행위<br>
																		<span class="spacing">⑮.</span>기타 불법적이거나 부당한 행위<br>
																	</li>
																</ol>
															</li>
															<li>
																<span class="spacing">2.</span>"회원"은 관계법, 이 약관의 규정, 이용안내 및 "서비스"와 관련하여 공지한 주의사항, "회사"가 통지하는 사항 등을 준수하여야 하며, 기타 "회사"의 업무에 방해되는 행위를 하여서는 안 됩니다.
															</li>
														</ol>
													<h3 >
														<a name="a11">제 11 조 ("서비스"의 제공 등)</a>
													</h3>
														<ol>
															<li>
																<span class="spacing">1.</span>회사는 회원에게 아래와 같은 서비스를 제공합니다.
																	<ol>
																	<li>
																		<span class="spacing">①.</span>Hello My Office:유무선 연동 비공개 모임 서비스
																	</li>
																	<li>
																		<span class="spacing">②.</span>기타 "회사"가 추가 개발하거나 다른 회사와의 제휴계약 등을 통해 "회원"에게 제공하는 일체의 서비스
																	</li>
																	</ol>
															</li>
															<li>
																<span class="spacing">2.</span>회사는 "서비스"를 일정범위로 분할하여 각 범위 별로 이용가능시간을 별도로 지정할 수 있습니다. 다만, 이러한 경우에는 그 내용을 사전에 공지합니다.
															</li>
															<li>
																<span class="spacing">3.</span>"서비스"는 연중무휴, 1일 24시간 제공함을 원칙으로 합니다.
															</li>
															<li>
																<span class="spacing">4.</span>"회사"는 컴퓨터 등 정보통신설비의 보수점검, 교체 및 고장, 통신두절 또는 운영상 상당한 이유가 있는 경우 "서비스"의 제공을 일시적으로 중단할 수 있습니다. 이 경우 "회사"는 제8조["회원"에 대한 통지]에 정한 방법으로 "회원"에게 통지합니다. 다만, "회사"가 사전에 통지할 수 없는 부득이한 사유가 있는 경우 사후에 통지할 수 있습니다.
															</li>
															<li>
																<span class="spacing">5.</span>"회사"는 서비스의 제공에 필요한 경우 정기점검을 실시할 수 있으며, 정기점검시간은 서비스제공화면에 공지한 바에 따릅니다.
															</li>
															<li>
																<span class="spacing">6.</span>"회사"는 본 "서비스"의 전부 또는 일부를 본인 확인 여부, 등록정보 유무, 기타 "회사"에서 정한 조건에 따라 "회원"에게 차등적으로 제공할 수 있습니다.
															</li>
														</ol>										
													<h3 >
														<a name="a12">제 12 조 ("서비스"의 변경)</a>
													</h3>
														<ol>
															<li>
																<span class="spacing">1.</span>"회사"는 상당한 이유가 있는 경우에 운영상, 기술상의 필요에 따라 제공하고 있는 전부 또는 일부 "서비스"를 변경할 수 있습니다.
															</li>
															<li>
																<span class="spacing">2.</span>"서비스"의 내용, 이용방법, 이용시간에 대하여 변경이 있는 경우에는 변경사유, 변경될 서비스의 내용 및 제공일자 등은 그 변경 전에 해당 서비스 내 “공지사항” 화면 등 회원이 충분히 인지할 수 있는 방법으로 게시하여야 합니다.
															</li>
															<li>
																<span class="spacing">3.</span>"회사"는 무료로 제공되는 서비스의 일부 또는 전부를 회사의 정책 및 운영의 필요상 수정, 중단, 변경할 수 있으며, 이에 대하여 관련법에 특별한 규정이 없는 한 "회원"에게 별도의 보상을 하지 않습니다.
															</li>
														</ol>
													<h3 >
														<a name="a13">제 13 조 (정보의 제공 및 광고의 게재)</a>
													</h3>
														<ol>
															<li>
																<span class="spacing">1.</span>"회사"는 "회원"이 "서비스" 이용 중 필요하다고 인정되는 다양한 정보를 공지사항이나 SMS등의 방법으로 "회원"에게 제공할 수 있습니다. 다만, "회원"에게 영리목적의 광고성 정보를 SMS등 전화 및 모사전송기기에 의하여 전송하려고 하는 경우에는 "회원"의 사전 동의를 받아서 전송합니다.
															</li>
															<li>
																<span class="spacing">2.</span>"회원"은 관련법에 따른 거래관련 정보 및 고객문의 등에 대한 답변 등을 제외하고는 언제든지 수신 거절을 할 수 있습니다.
															</li>
															<li>
																<span class="spacing">3.</span>"회사"는 "서비스"의 운영과 관련하여 “서비스” 화면, 홈페이지, 등에 광고를 게재할 수 있습니다.
															</li>
															<li>
																<span class="spacing">4.</span>"회원"은 "회사"가 제공하는 "서비스"와 관련하여 게시물 또는 기타 정보를 변경, 수정, 제한하는 등의 조치를 취하지 않습니다.
															</li>
														</ol>
													<h3 >
														<a name="a14">제 14 조  ("게시물"의 저작권)</a>
													</h3>
														<ol>
															<li>
																<span class="spacing">1.</span>"회원"이 "서비스" 내에 게시한 "게시물"의 저작권은 해당 게시물의 저작자에게 귀속됩니다.
															</li>
														</ol>
													<h3 >
														<a name="a15">제 15 조 ("게시물"의 관리)</a>
													</h3>
														<ol>
															<li>
																<span class="spacing">1.</span>"회원"의 "게시물"이 관련법에 위반되는 내용을 포함하는 경우, 권리자는 관련법이 정한 절차에 따라 해당 "게시물"의 게시중단 및 삭제 등을 요청할 수 있으며, "회사"는 관련법에 따라 조치를 취하여야 합니다.
															</li>
															<li>
																<span class="spacing">2.</span>"회사"는 전항에 따른 권리자의 요청이 없는 경우라도 권리침해가 인정될 만한 사유가 있거나 기타 회사 정책 및 관련법에 위반되는 경우에는 관련법에 따라 해당 "게시물"에 대해 임시조치 등을 취할 수 있습니다.
															</li>
															<li>
																<span class="spacing">3.</span>"회사"는 관계 법령 또는 이 약관의 준수 여부 등을 확인할 필요가 있을 때, "게시물"의 내용을 확인할 수 있습니다.
															</li>
															<li>
																<span class="spacing">4.</span>"게시물" 일부는 일정 기간 이후에는 "서비스" 상에서 삭제될 수 있으며, "게시물"에 대한 관리 책임은 "회원"에게 있습니다.
															</li>
															<li>
																<span class="spacing">5.</span>"회사"는 모임의 대표가 "회원"이 비공개 모임 내에 게시하여 타인과 공유하고 있는 "게시물"을 삭제할 수 있는 편집기능을 제공하고 있습니다.
															</li>
														</ol>
													<h3 >
														<a name="a16">제 16 조 (권리의 귀속)</a>
													</h3>
														<ol>
															<li>
																<span class="spacing">1.</span>"서비스"에 대한 저작권 및 지적재산권은 "회사"에 귀속됩니다. 단, "회원"의 "게시물" 및 제휴계약에 따라 제공된 저작물 등은 제외합니다. "회사"가 제공하는 서비스의 디자인, "회사"가 만든 텍스트, 스크립트(script), 그래픽, "회원" 상호간 전송 기능 등 "회사"가 제공하는 서비스에 관련된 모든 상표, 서비스 마크, 로고 등에 관한 저작권 기타 지적재산권은 관련법령에 기하여 "회사"가 보유하고 있거나 "회사"에게 소유권 또는 사용권이 있습니다.
															</li>
															<li>
																<span class="spacing">2.</span>"회사"는 서비스와 관련하여 "회원"에게 "회사"가 정한 이용조건에 따라 “서비스" 등을 이용할 수 있는 이용권만을 부여하며, "회원"은 이를 양도, 판매, 담보제공 등의 처분행위를 할 수 없습니다. "회원"은 본 이용약관으로 인하여 "서비스"를 소유하거나 "서비스"에 관한 저작권을 보유하게 되는 것이 아니라, "회사"로부터 "서비스"의 이용을 허락 받게 되는바, "서비스"는 정보취득 또는 개인용도로만 제공되는 형태로 "회원"이 이용할 수 있습니다.
															</li>
															<li>
																<span class="spacing">3.</span>"회원"은 명시적으로 허락된 내용을 제외하고는 "서비스"를 통해 얻어지는 "회원" 상태정보를 영리 목적으로 사용, 복사, 유통하는 것을 포함하여 "회사"가 만든 텍스트, 스크립트, 그래픽의 "회원" 상호간 전송기능 등을 복사하거나 유통할 수 없습니다.
															</li>
															<li>
																<span class="spacing">4.</span>"회원"은 "회사"의 명백한 서면 허가를 받은 경우를 제외하고는 "서비스" 또는 이에 포함된 소프트웨어와 관련된 파생물 제작, 역파일, 소스 코드의 추출을 시도할 수 없습니다
															</li>
														</ol>
													<h3 >
														<a name="a17">제 17 조 (계약해제, 해지 등)</a>
													</h3>
														<ol>
															<li>
																<span class="spacing">1.</span>"회원"은 언제든지 서비스 내 계정삭제 화면을 통하여 이용계약 해지 신청을 할 수 있으며, "회사"는 관련법령 등이 정하는 바에 따라 이를 즉시 처리하여야 합니다.
															</li>
															<li>
																<span class="spacing">2.</span>"회원"이 계약을 해지할 경우, 관련법 및 개인정보취급방침에 따라 "회사"가 회원정보를 보유하는 경우를 제외하고는 해지 즉시 "회원"의 모든 데이터는 소멸되며 이는 복구할 수 없습니다.
															</li>
															<li>
																<span class="spacing">3.</span>"회원"이 계약을 해지하는 경우, "회원"을 식별할 수 있는 프로필 사진 등과 같이 본인계정에 등록된 내용 일체는 삭제됩니다. 다만, 타인에 의해 담기, 스크랩 등이 되어 재게시 되거나, 공용게시판에 등록된 "게시물" 등은 삭제되지 않으니 사전에 삭제 후 탈퇴하시기 바랍니다.
															</li>
														</ol>
													<h3 >
														<a name="a18">제 18 조 (이용제한 등)</a>
													</h3>
														<ol>
															<li>
																<span class="spacing">1.</span>"회사"는 "회원"이 이 약관의 의무를 위반하거나 "서비스"의 정상적인 운영을 방해한 경우, 경고, 일시정지, 영구이용정지 등으로 "서비스" 이용을 단계적으로 제한할 수 있습니다.
															</li>
															<li>
																<span class="spacing">2.</span>"회사"는 전항에도 불구하고, 불법프로그램의 제공 및 운영방해, 불법통신 및 해킹, 악성프로그램의 배포, 접속권한 초과행위 등과 같이 관련법을 위반한 경우에는 즉시 영구이용정지를 할 수 있습니다. 본 항에 따른 영구이용정지 시 "서비스" 이용을 통해 획득한 혜택 등도 모두 소멸되며, "회사"는 이에 대해 별도로 보상하지 않습니다.
															</li>
															<li>
																<span class="spacing">3.</span>"회사"는 "회원"이 계속해서 1년 이상 로그인하지 않는 경우, 회원정보의 보호 및 운영의 효율성을 위해 이용을 제한할 수 있습니다.
															</li>
															<li>
																<span class="spacing">4.</span>"회사"는 본 조의 이용제한 범위 내에서 제한의 조건 및 세부내용은 이용제한정책 및 개별 서비스상의 운영정책에서 정하는 바에 의합니다.
															</li>
															<li>
																<span class="spacing">5.</span>본 조에 따라 "서비스" 이용을 제한하거나 계약을 해지하는 경우에는 "회사"는 제8조["회원"에 대한 통지]에 따라 통지합니다.
															</li>
															<li>
																<span class="spacing">6.</span>"회원"은 본 조에 따른 이용제한 등에 대해 "회사"가 정한 절차에 따라 이의신청을 할 수 있습니다. 이 때 이의가 정당하다고 "회사"가 인정하는 경우 "회사"는 즉시 "서비스"의 이용을 재개합니다
															</li>
														</ol>
													<h3 >
														<a name="a19">제 19 조 (책임제한)</a>
													</h3>
														<ol>
															<li>
																<span class="spacing">1.</span>"회사"는 천재지변 또는 이에 준하는 불가항력으로 인하여 "서비스"를 제공할 수 없는 경우에는 "서비스" 제공에 관한 책임이 면제됩니다.
															</li>
															<li>
																<span class="spacing">2.</span>"회사"는 단말기 등 접근 매체의 분실로 인한 서비스 이용 장애 등 "회원"의 귀책사유로 인한 "서비스" 이용의 장애 및 통신 장애 등으로 인한 서비스 이용 장애 등에 대하여는 책임을 지지 않습니다.
															</li>
															<li>
																<span class="spacing">3.</span>"회사"는 "회원"이 "서비스"와 관련하여 게재한 정보, 자료, 사실의 신뢰도, 정확성 등의 내용에 관하여는 책임을 지지 않습니다.
															</li>
															<li>
																<span class="spacing">4.</span>"회사"는 "회원" 간 또는 "회원"과 제3자 상호간에 "서비스"를 매개로 하여 거래 등을 한 경우에는 책임이 면제됩니다.
															</li>
															<li>
																<span class="spacing">5.</span>"회사"는 무료로 제공되는 서비스 이용과 관련하여 관련법에 특별한 규정이 없는 한 책임을 지지 않습니다.
															</li>
														</ol>
													<h3 >
														<a name="a20">제 20 조 (준거법 및 재판관할)</a>
													</h3>
														<ol>
															<li>
																<span class="spacing">1.</span>"회사"와 "회원" 간 제기된 소송은 대한민국법을 준거법으로 합니다.
															</li>
															<li>
																<span class="spacing">2.</span>"회사"와 "회원" 간 발생한 분쟁에 대하여는 서울중앙지방법원을 제1심 관할 법원으로 한다.
															</li>							
														</ol>
													<h3 >
														<a name="a21">[부칙] (재판권 및 준거법)</a>
													</h3>
														<ol>
															<li>
																<span class="spacing">본 약관은 2014년 3월 13일부터 적용됩니다.</span>
															</li>												
														</ol>
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
	
</body>
</html>















