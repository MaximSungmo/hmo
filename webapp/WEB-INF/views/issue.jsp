<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">채팅</c:param>
	<c:param name="bsUsed">NO</c:param>
	<c:param name="aceUsed">NO</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>
</head>
<body class="scroll-y">
<div class="rw-snn-wrap" id="rw-snn-wrap">
	<div id="rw-snn-navi-wrap"></div>
	<div id="rw-snn-navir-wrap">
		<c:import url="/WEB-INF/views/common/nav-list-right.jsp">
		</c:import>
	</div>	
	<div id="rw-snn-main" class="rw-snn">
		<div id="rw-view-shield"></div>
		<!-- BEGIN:navbar -->
		<div class="navbar" id="navbar">
			<c:import url="/WEB-INF/views/common/navbar.jsp">
			</c:import>
		</div>
		<!-- END:navbar -->
		<!-- BEGIN:main-container -->
		<div class="main-container container-fluid">

			<!-- BEGIN:sidebar -->
			<div class="sidebar" id="snn-sidebar">

				<!-- BEGIN:welcome-box -->
				<c:import url="/WEB-INF/views/common/welcome-box.jsp">
				</c:import>
				<!-- END:welcome-box -->

				<!-- BEGIN:nav-list -->
				<c:import url="/WEB-INF/views/common/nav-list.jsp">
					<c:param name="menuName">알려진 버그</c:param>
				</c:import>
				<!-- END:nav-list -->

			</div>
			<!-- END:sidebar -->

			<!-- BEGIN:main-content -->
			<div class="main-content z1">
			
				<!-- BEGIN:page-header -->
				<c:import url="/WEB-INF/views/common/page-header.jsp">
					<c:param name="pageName">bug</c:param>
				</c:import>					
				<!-- END:page-header -->
				
				<!-- BEGIN:page-content -->
				<div class="page-content z1">
					<div class="rw-content-area-wrap">
						<div class="rw-content-area">
							<div class="rw-pagelet-wrap rw-mtl rw-mobile-pagelet">
								<div class="ui-message-channel-stream-wrap">
									<div class="ui-message-channel-stream ui-bug-stream">
										<div class="_h6m"></div>
										<ul class="ui-message-channel-list" id="channel-list">
											<li class="_z6m channel-row">
												<table class="ui-grid _k3x" cellspacing="0" cellpadding="0">
													<tbody>
														<tr>
															<td class="v-top _z6p channel-col plm" onclick="Channel.onChannelRowClicked(this);">
																<div class="_el2 fwn fcg">
																	<span class="_sl1">IE8 이하는 아직 지원하지 않습니다.</span>
																</div>
																<div class="_el2 fwn fcg fsm">
																	<span>
																		IE8이하 버젼에서는 서비스가 정상적으로 동작하지 않을 수 있습니다.계속 수정 중이나 혹시라도 사용중에 불편한 점이 있으면  <a href="/feedback">신고를 제출해주십시오</a> 
																	</span>
																</div>
															</td>
															<td class="v-middle _z6q">
															</td>
														</tr>
													</tbody>
												</table>
											</li>
											<li class="_z6m channel-row">
												<table class="ui-grid _k3x" cellspacing="0" cellpadding="0">
													<tbody>
														<tr>
															<td class="v-top _z6p channel-col plm" onclick="Channel.onChannelRowClicked(this);">
																<div class="_el2 fwn fcg">
																	<span class="_sl1">공지사항 알림이 실시간으로 뜨지 않습니다.</span>
																</div>
																<div class="_el2 fwn fcg fsm">
																	<span>
																		공지사항이 발생했을 때, 상단 알림 아이콘의 뱃지가 업데이트 되지 않습니다. 그 밖에 알림에 대해 문제점이 발견되면  <a href="/feedback">신고를 제출해주십시오</a>
																	</span>
																</div>
															</td>
															<td class="v-middle _z6q">
															</td>
														</tr>
													</tbody>
												</table>
											</li>										
											<li class="_z6m channel-row">
												<table class="ui-grid _k3x" cellspacing="0" cellpadding="0">
													<tbody>
														<tr>
															<td class="v-top _z6p channel-col plm" onclick="Channel.onChannelRowClicked(this);">
																<div class="_el2 fwn fcg">
																	<span class="_sl1">파일을 하나 올리고, 그 파일을 지운 후 다시 파일을 올리려고 하면 안 올라가집니다.</span>
																</div>
																<div class="_el2 fwn fcg fsm">
																	<span>
																		스토리 작성 시 파일 올리는 중 최초 한 개 파일을 올리고 삭제 후, 파일 올리기 를 하면 올라가지 않습니다. 그 밖에 스토리 작성은 모두 정상적으로 작동합니다. 혹시라도 스토리 작성에 문제가 발생하면  <a href="/feedback">신고를 제출해주십시오</a> 
																	</span>
																</div>
															</td>
															<td class="v-middle _z6q">
																<div style="text-align:center">
																	<i class="fa fa-check fa-1g"></i><br>
																	<span>수정됨</span>
																</div>
															</td>
														</tr>
													</tbody>
												</table>
											</li>
											<li class="_z6m channel-row">
												<table class="ui-grid _k3x" cellspacing="0" cellpadding="0">
													<tbody>
														<tr>
															<td class="v-top _z6p channel-col plm" onclick="Channel.onChannelRowClicked(this);">
																<div class="_el2 fwn fcg">
																	<span class="_sl1">로그인 시 가끔 화면이 깨집니다.</span>
																</div>
																<div class="_el2 fwn fcg fsm">
																	<span>
																		자주 발생하지는 않지만, 브라우저에 따라 캐시문제로 인한 최초 화면이 깨질 수 있습니다. 최초 화면이 아닌 다른 페이지에서 화면 깨짐이 발생하면   <a href="/feedback">신고를 제출해주십시오</a>
																	</span>
																</div>
															</td>
															<td class="v-middle _z6q">
															</td>
														</tr>
													</tbody>
												</table>
											</li>
											<li class="_z6m channel-row">
												<table class="ui-grid _k3x" cellspacing="0" cellpadding="0">
													<tbody>
														<tr>
															<td class="v-top _z6p channel-col plm" onclick="Channel.onChannelRowClicked(this);">
																<div class="_el2 fwn fcg">
																	<span class="_sl1">파일만 올린 댓글에 대한 알림 메세지가  null로 표시됩니다.</span>
																</div>
																<div class="_el2 fwn fcg fsm">
																	<span>
																		스토리에서는 별다른 내용 없이 파일만으로도 게시가 가능하지만 그에 대한 알림메세지가 내용이 없는 이유로 null 문자가 나타납니다. 이 외에 알림 메세지가 이상하게 보여지면  <a href="/feedback">신고를 제출해주십시오</a>
																	</span>
																</div>
															</td>
															<td class="v-middle _z6q">
															</td>
														</tr>
													</tbody>
												</table>
											</li>
											<li class="_z6m channel-row">
												<table class="ui-grid _k3x" cellspacing="0" cellpadding="0">
													<tbody>
														<tr>
															<td class="v-top _z6p channel-col plm" onclick="Channel.onChannelRowClicked(this);">
																<div class="_el2 fwn fcg">
																	<span class="_sl1">노티 메세지 전체보기 지원이 되지 않습니다.</span>
																</div>
																<div class="_el2 fwn fcg fsm">
																	<span>
																		알림 메세지의 팝업 리스트창 하단의 전체보기 링크가 작동하지 않는 버그가 있습니다. 이 외의 알림의 팝업리스트창 링크에 오류가 있다면  <a href="/feedback">신고를 제출해주십시오</a>
																	</span>
																</div>
															</td>
															<td class="v-middle _z6q">
															</td>
														</tr>
													</tbody>
												</table>
											</li>
											<li class="_z6m channel-row">
												<table class="ui-grid _k3x" cellspacing="0" cellpadding="0">
													<tbody>
														<tr>
															<td class="v-top _z6p channel-col plm" onclick="Channel.onChannelRowClicked(this);">
																<div class="_el2 fwn fcg">
																	<span class="_sl1">소그룹에서 관리자가 초대할 경우 승인을 거치지 않고 바로 가입되게 변경할 예정입니다.</span>
																</div>
																<div class="_el2 fwn fcg fsm">
																	<span>
																		그룹에서의 관리자의 초대가  가입신청하는 경우와 마찬가지로 관리자가 꼭 승인해야 하는 번거로움이 아직 남아 있습니다. 
																	</span>
																</div>
															</td>
															<td class="v-middle _z6q">
															</td>
														</tr>
													</tbody>
												</table>
											</li>
											<li class="_z6m channel-row">
												<table class="ui-grid _k3x" cellspacing="0" cellpadding="0">
													<tbody>
														<tr>
															<td class="v-top _z6p channel-col plm" onclick="Channel.onChannelRowClicked(this);">
																<div class="_el2 fwn fcg">
																	<span class="_sl1">사용자가 중간에 서버와 세션이 끊어졌을 경우 상단 알림을 받게 될 경우 오류가 발생합니다.</span>
																</div>
																<div class="_el2 fwn fcg fsm">
																	<span>
																		자동으로 로그인 옵션을 켜고 로그인하면 이 문제를 막을 수 있지만, 그렇게 사용하지 않는 사용자를 위해서 상단 알림이 오지 못하도록 수정 중에 있습니다. 인증에 대한 문제가 발생하면  <a href="/feedback">신고를 제출해주십시오</a> 
																	</span>
																</div>
															</td>
															<td class="v-middle _z6q">
															</td>
														</tr>
													</tbody>
												</table>
											</li>
											<li class="_z6m channel-row">
												<table class="ui-grid _k3x" cellspacing="0" cellpadding="0">
													<tbody>
														<tr>
															<td class="v-top _z6p channel-col plm" onclick="Channel.onChannelRowClicked(this);">
																<div class="_el2 fwn fcg">
																	<span class="_sl1">주소록에 즐겨찾기 기능이 되지 않습니다.</span>
																</div>
																<div class="_el2 fwn fcg fsm">
																	<span>
																		즐겨찾기 링크가 작동하지 않습니다. 그 밖의 주소록에 대한 오류는  <a href="/feedback">신고를 제출해주십시오</a>
																	</span>
																</div>
															</td>
															<td class="v-middle _z6q">
															</td>
														</tr>
													</tbody>
												</table>
											</li>																																																																																							
										</ul>
										<div class="_f6m"></div>
									</div>
								</div>				
							</div>
						</div>
					</div>
					<div class="rw-right-col">
						<div class="rw-pagelet-blank"></div>
						<!-- BEGIN:rightcol -->
						<c:import url="/WEB-INF/views/common/right-col.jsp">
						</c:import>
						<!-- END:rightcol -->
					</div>
				</div>
			</div>
		</div>
	</div>
</div>	
<div class="hmo-dialog-res" id="dialog-chat-user">
	<div class="hmo-dialog-scrollable-area" id="dialog-start-chat-scrollable-area">
		<div class="d-fuc-l" id="dialog-start-chat-list"></div>
	</div>
</div>
</body>
</html>