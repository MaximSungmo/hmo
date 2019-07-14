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
<script src="/assets/sunny/2.0/js/uncompressed/net.js?v=1.2"></script>
<c:if test="${sunny.device.mobile }">
<script src="/assets/sunny/2.0/js/uncompressed/socket.io.js"></script>
</c:if>
<script>
$( function() {
	
	Messenger.init();
	
	<c:if test="${sunny.device.mobile && not empty channel }">
		<sec:authorize access="isAuthenticated()" var="isAuthenticated">
		<sec:authentication var="authUserId" property="principal.userId" scope="request"/>
		var
		socket=io.connect('https://push.sunnyvale.co.kr?serviceId=${sunny.serviceId}&userId=${authUserId }',{secure: true, port:19999, 'flash policy port':19999, 'reconnect':true, 'resourse':'socket.io'});
		socket.emit('connect', { userId :'${authUserId}', serviceId :'${sunny.serviceId}' } );
		$( window ).on( "beforeunload", function() {
			if( socket ) {
				socket.disconnect();
			}
			return;
		});
		socket.on( "chat-notify", Messenger.onNotify.bind( Messenger ) );
		</sec:authorize>
	</c:if>
})
</script>
</head>
<body class="scroll-y">
<div class="rw-snn-wrap" id="rw-snn-wrap">
	<c:if test="${ not sunny.device.mobile }">
		<div id="rw-snn-navi-wrap"></div>
		<div id="rw-snn-navir-wrap">
			<c:import url="/WEB-INF/views/common/nav-list-right.jsp">
			</c:import>
		</div>
	</c:if>	
	<div id="rw-snn-main" class="rw-snn">
		<c:if test="${ not sunny.device.mobile }">
		<div id="rw-view-shield"></div>
		<!-- BEGIN:navbar -->
		<div class="navbar" id="navbar">
			<c:import url="/WEB-INF/views/common/navbar.jsp">
			</c:import>
		</div>
		<!-- END:navbar -->
		</c:if>	
		
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
					<c:param name="menuName">채팅</c:param>
				</c:import>
				<!-- END:nav-list -->

			</div>
			<!-- END:sidebar -->

			<!-- BEGIN:main-content -->
			<div class="main-content">
			
				<!-- BEGIN:page-header -->
				<c:import url="/WEB-INF/views/common/page-header.jsp">
					<c:param name="pageName">inchat</c:param>
				</c:import>					
				<!-- END:page-header -->
				
				<!-- BEGIN:page-content -->
				<div class="page-content">
					<div class="rw-content-area-wrap in-chat-rw-content-are-wrap">
						<div class="rw-content-area">
							<div class="rw-pagelet-wrap rw-mtl rw-mobile-pagelet">
								<div class="ui-msg-v">
									<div class="ui-scrollable-area" id="msgr-message-v">
										<div class="ui-scrollable-area-body" id="msgr-scrollable-area-body">
											<div class="ui-scrollable-area-contents" id="msgr-scrollable-area-content">
												<ul role="list"
													class="ui-list" id="msgr-stream-list"
													data-async="async-get"
													data-stream-url="/message/${channel.id }"
													data-bw-map='{"top":false,"ur":false,"msgId":"","size":"10"}'
													data-fw-map='{"top":true,"ur":false,"msgId":"","size":"10"}'>
												</ul>
											</div>	
										</div>
									</div>
									<div class="ui-editable-area" id="msgr-edit">
										<div class="_2k">
											<div class="_1rs">
												<div class="_2pt no-tools">
													<div class="_1rt">
														<form	rel="sync"
																method="post"
																action="/message/send"
																onsubmit="return false;">
															<textarea	role="textbox"
																		class="ui-textarea-message ui-textarea-no-resize ui-textarea-auto-grow"
																		autocomplete="off"
																		spellcheck="false"
																		placeholder="메세지 쓰기..."
																		name="text"
																		id="ta-message-text"
																		rows="3"
																		style="background-color:#fff; height:48px"></textarea>
															<input type="hidden" name="channelId" value="${channel.id }">			
														</form>
														<c:if test="${sunny.device.mobile }">
															<div class="btn-add-sndm-wrap" >	
																<a href="#" id="btn-send-message" class="hmo-button hmo-button-blue hmo-button-middle-10">보내기</a>
															</div>
														</c:if>																	
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
			<!-- END:main-content -->
		</div>
	</div>
</div>
<div class="hmo-dialog-res" id="dialog-chat-user">
	<div class="hmo-dialog-scrollable-area" id="dialog-start-chat-scrollable-area">
		<div class="d-fuc-l" id="dialog-start-chat-list"></div>
	</div>
</div>
<c:if test="${not empty channel}">
<c:set value="${fn:length(channel.userRelation ) }" var="count" />
<script>
Messenger.channel={"id":${channel.id },"type":${channel.type},"usrs":[<c:forEach  begin="1" end="${count }" step="1" var="index" >{"id":${channel.userRelation[index-1].user.id }, "name":"${channel.userRelation[index-1].user.name }", "lMsgId":${channel.userRelation[index-1].lastReadMessageInfoId == null ? -1 : channel.userRelation[index-1].lastReadMessageInfoId }}${ ( index < count ) ? ',' : '' }</c:forEach>]};	 	
</script>
</c:if>
</body>
</html>