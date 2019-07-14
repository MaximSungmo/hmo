<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<script src="/assets/sunny/2.0/js/uncompressed/socket.io.js"></script>
<script src="/assets/sunny/2.0/js/uncompressed/notify-flyout.js"></script>
<script>
/* SideMenuOpener Object  */
var SideMenuOpener = {
	__$mainWrap: null,
	__$naviWrap: null,
	__$main: null,
	__$sidebar: null,
	__$shield: null,
	__$contentArea: null,
	__$mainContainer: null,
	__$w: null,
	__sidebarHeight: 0,
	__scrollTop: 0,
	init: function(){
		this.__$mainWrap = $( "#rw-snn-wrap" );
		this.__$naviWrap = $( "#rw-snn-navi-wrap" );
		this.__$main = $( "#rw-snn-main" );
		this.__$sidebar = $( "#snn-sidebar" );
		this.__$shield = $( "#rw-view-shield" );
		this.__$contentArea = $( ".rw-content-area-wrap" );
		this.__$mainContainer = $( "#main-container" );
		this.__$w = $( window );

		this.__sidebarHeight = this.__$sidebar.height();

		this.__$shield.onHMOClick( null, this.onShieldClicked.bind( this ) );
		$( "#rw-left-bar-button" ).onHMOClick( null, this.onLeftBarButtonClicked.bind( this ) );
		$( "#rw-right-bar-button" ).onHMOClick( null, this.onRightBarButtonClicked.bind( this ) );
	},
	onLeftBarButtonClicked: function( $event ) {
		//
		$event.preventDefault();
		
		// store scroll top
		this.__scrollTop = this.__$w.scrollTop();	
		this.__$contentArea.css({ "top": -1*this.__scrollTop  });
		
		// recalc. window height
		this.__$naviWrap.css( "height", this.__sidebarHeight );
		this.__$main.css( "height", this.__sidebarHeight );
		
		// open menu!
		this.__$naviWrap.append( this.__$sidebar );
		this.__$mainWrap.addClass( "open-menu" );
		
		// shielding
		setTimeout( function() {
			SideMenuOpener.__$shield.addClass( "visible" );
		}, 0 );
		
		//
		this.__$w.scrollTop( 0 );
	},
	onRightBarButtonClicked: function( $event ) {
		// store scroll top
		this.__scrollTop = this.__$w.scrollTop();	
		this.__$contentArea.css({ "top": -1*this.__scrollTop  });
		
		// recalc. window height
		this.__$naviWrap.css( "height", this.__sidebarHeight );
		this.__$main.css( "height", this.__sidebarHeight );
		
		// open menu!
		this.__$mainWrap.addClass( "open-menu-r" );
		
		// shielding
		setTimeout( function() {
			SideMenuOpener.__$shield.addClass( "visible" );
		}, 0 );
		
		//
		this.__$w.scrollTop( 0 );
	},
	onShieldClicked: function( event ) {
		//
		event.stopPropagation();
		//
		this.__$shield.removeClass( "visible" );
		// this.__$main.removeClass( "rw-close" );
		// this.__$main.addClass( "rw-transition rw-open" );

		//	SideMenuOpener.__$main.removeClass( "rw-transition rw-open" );
		SideMenuOpener.__$mainWrap.removeClass( "open-menu" );
		SideMenuOpener.__$mainWrap.removeClass( "open-menu-r" );
		
		//
		SideMenuOpener.__$naviWrap.css( "height", "auto" );
		SideMenuOpener.__$main.css( "height", "auto" );
		SideMenuOpener.__$mainContainer.prepend( SideMenuOpener.__$sidebar );

		setTimeout( function() { 
			SideMenuOpener.__$contentArea.css({ "top":0 });
			SideMenuOpener.__$w.scrollTop( SideMenuOpener.__scrollTop );
		}, 0 );
	}
};

function play_ring(){
	<c:if test="${not sunny.cordova }">
		var filename =  "/assets/sunny/2.0/audio/bell-ringing-04";
		$("#sound").html('<audio autoplay="autoplay"><source src="' + filename + '.mp3" type="audio/mpeg" /><source src="' + filename + '.ogg" type="audio/ogg" /><embed hidden="true" autostart="true" loop="false" src="' + filename +'.mp3" /></audio>');
	</c:if>
}

function load_top_noti_count(){
	$.ajax({
		url :  "/bar/noti_count?rnd=" + Math.floor(Math.random() * 999999999),
		type: "get",
// 		dataType: "json",
		contentType: 'application/json',
		success: function(data){
			var
			notiCount = Number(data);

			if( notiCount < 1 ){
				return;
			}
			
			_$ncvw.addClass("_new");
			_$ncv.text(notiCount);
			NotifyFlyout.noti.isDirty = true;
			
			
			newEventCount = __orgEventCount + notiCount ;
			document.title = "(" + ( newEventCount ) + ")" + __orgTitle ;
			play_ring();
// 			}else{
// 				document.title = __orgTitle ;
// 			}
		},
		error: function( jqXHR, textStatus, errorThrown ){
			//alert( "상단 카운트 패치 에러 textStatus " + " : " + errorThrown );
		},
		statusCode: {
			"401" : function() {
				alert( "Unauthorized" );
			},
			"404" : function() {
				//alert( "Unkown" );
			}
		}		
	} );
}

function load_top_notice_count(){
	$.ajax({
		url :  "/bar/notice_count?rnd=" + Math.floor(Math.random() * 999999999),
		type: "get",
// 		dataType: "json",
		contentType: 'application/json',
		success: function(data){
			var
			noticeCount = Number(data);

			if( noticeCount < 1 ){
				return; 	
			}
			_$rcvw.addClass("_new");
			_$rcv.text(noticeCount);
			NotifyFlyout.notice.isDirty = true;
			
			newEventCount = __orgEventCount + noticeCount ;
			document.title = "(" + ( newEventCount ) + ")" + __orgTitle ;
			play_ring();
// 			}else{
// 				document.title = __orgTitle ;
// 			}
		},
		error: function( jqXHR, textStatus, errorThrown ){
			//alert( "상단 카운트 패치 에러 textStatus " + " : " + errorThrown );
		},
		statusCode: {
			"401" : function() {
				alert( "Unauthorized" );
			},
			"404" : function() {
				//alert( "Unkown" );
			}
		}		
	} );
}

function load_top_message_count(){
	$.ajax({
		url :  "/bar/message_count?rnd=" + Math.floor(Math.random() * 999999999),
		type: "get",
		contentType: 'application/json',
		success: function(data){
			var
			messageCount = Number(data);
			if( messageCount < 1 ){
				return ;	
			}
			
			_$mcvw.addClass("_new");
			_$mcv.text(messageCount);
			NotifyFlyout.message.isDirty = true;
		
			newEventCount = __orgEventCount + messageCount ;
			document.title = "(" + ( newEventCount ) + ")" + __orgTitle ;
			play_ring();
// 			}else{
// 				document.title = __orgTitle ;
// 			}
		},
		error: function( jqXHR, textStatus, errorThrown ){
			//alert( "상단 카운트 패치 에러 textStatus " + " : " + errorThrown );
		},
		statusCode: {
			"401" : function() {
				alert( "Unauthorized" );
			},
			"404" : function() {
				//alert( "Unkown" );
			}
		}		
	});
}

var _$ncvw, _$ncv, _$rcvw, _$rcv, _$mcvw, _$mcv;

$(function() {
	//
	SideMenuOpener.init();
	
	//
	_$rcv = $("#notice-count-value");
	_$ncv = $("#noti-count-value");
	_$mcv = $("#message-count-value");
	_$ncvw = $("#nav-noti-list");
	_$rcvw = $("#nav-notice-list");
	_$mcvw = $("#nav-message-list");
	
	NotifyFlyout.init();
	
	// connection	
	<sec:authorize access="isAuthenticated()" var="isAuthenticated">
	<sec:authentication var="authUserId" property="principal.userId" scope="request"/>
		var
<%-- 		socket=io.connect('https://push.sunnyvale.co.kr?serviceId=${sunny.serviceId}&userId=${authUserId }',{port:19999, 'reconnect':true, 'resourse':'socket.io'});--%>
		socket=io.connect('https://push.sunnyvale.co.kr?serviceId=${sunny.serviceId}&userId=${authUserId }',{secure: true, port:19999, 'flash policy port':19999, 'reconnect':true, 'resourse':'socket.io'});
		socket.emit('connect', { userId :'${authUserId}', serviceId :'${sunny.serviceId}' } );
		//
		$(window).on('beforeunload', function() {
			if( socket ) {
				//console.log("prev emit");
				//socket.emit('connectend',{userId : '${authUserId}', serviceId: '${sunny.serviceId}' }, function() {
					socket.disconnect();
				//});
			}
			return;
		});
		//
		socket.on( 'noti-notify', function( jsonObj ) {
			load_top_noti_count();
		});
		//
		socket.on( 'notice-notify', function( jsonObj ) {
			load_top_notice_count();
		});
		//
		<c:choose> 	
			<c:when test="${empty channel}">
				socket.on( 'chat-notify', function( jsonObj ) {
					load_top_message_count();
				});
			</c:when>
			<c:otherwise>
				socket.on( 'chat-notify', function( jsonObj ) {
					var
					channelId = jsonObj.cid;
					if( channelId == ${channel.id} ) {
						Messenger.onNotify( jsonObj );
					} else {
						load_top_message_count();
					}
				});
			</c:otherwise>	
		</c:choose>
	</sec:authorize>
});
</script>
<div id="sound" class="hidden"></div>
<div class="navbar-inner fixed">
			<div class="container-fluid">
				<a class="rw-left-bar-button" id="rw-left-bar-button">
					<i class="fa fa-bars fa-2x"></i>
				</a>
				<a href="/" class="brand">${sunny.site.companyName }</a>
				<div class="pull-right rw-nav-wrap z1">
					<ul class="nav ace-nav rw-nav">
						<li class="navi-profile-name z1">
							<a href="/user/${authUserId }">
								<img class="rw-nav-user-photo" src="${authUserProfilePic }" alt="${authUserName }">
								<span class="user-info name-${authUserId }">${authUserName }</span>
							</a>
						</li>
						<li id="nav-noti-list" class="rw-tool-button <c:if test="${currentInfo.notificationCount > 0 }">_new</c:if>">
							<a  class="dropdown-toggle"
								id="user-noti-flyout-trigger"
								aria-owns="user-noti-flyout"
								aria-haspopup="true"
								rel="toggle">
								<i class="fa fa-bell fa-1g"></i>
								<span class="noti-count">
									<span id="noti-count-value">${currentInfo.notificationCount }</span>
								</span>
							</a>
							<div id="user-noti-flyout"
								 class="noti-flyout ui-toggle-flyout"
								 data-which="noti"
								 data-top-most="true"
								 data-popup-group="global"
								 data-popup-trigger="user-noti-flyout-trigger"
								 data-fn-show="NotifyFlyout.onShow"
								 data-fn-hide="NotifyFlyout.onHide">
								<div class="noti-flyout-top-header">
									<div class="top-nub-wrapper">
										<div class="top-nub" id="t-noti-top-nub"></div>
									</div>
								</div>
								<div class="noti-flyout-item-list">
									<div>
										<div class="psr"></div>
										
										<div class="ui-scrollable-area" id="t-noti-s-area">

														
															<div class="notice-list hot-unit">
																<!-- begin:리스트  -->
																<ul class="ui-list"
																	id="t-noti-l" 
																	role="list" 
																	data-async="async-get" 
																	data-stream-url="/bar/noties"
																	data-bw-map="{&quot;top&quot;:false,&quot;nid&quot;:&quot;0&quot;,&quot;size&quot;:&quot;10&quot;}"
																	data-fw-map="{&quot;top&quot;:true,&quot;nid&quot;:&quot;0&quot;,&quot;size&quot;:&quot;10&quot;}">
																	<li class="no-data empty hidden-elem">
																		<table class="ui-grid no-activity-grid" cellspacing="0" cellpadding="0">
																			<tbody>
																				<tr>
																					<td class="v-mid h-cent">새로운 알람이 없습니다.</td>
																				</tr>
																			</tbody>
																		</table>
																	</li>
																	<li class="next-loading empty hidden-elem">
																		<table class="ui-grid no-requests-grid" cellspacing="0" cellpadding="0">
																			<tbody>
																				<tr>
																					<td class="v-mid h-cent"><img src="/assets/sunny/2.0/img/sunny-loader-big.gif"/></td>
																				</tr>
																			</tbody>
																		</table>
																	</li>
																</ul>
																<!-- end:리스트  -->																		
															</div>
											
											
										</div>
										
										
										<div class="ui-contextual-layer-positioner ui-layer" style="left:0px; top:0;">
											<div class="ui-contextual-layer" style="bottom:0px;">
												<div id="t-noti-h-inner" class="_ptt">
													<div class="ui-header noti-flyout-header">
														<div class="ui-header-top z1">
															<div class="l-ft">
																<h3 class="ui-header-title" aria-hidden="true">알림</h3>
															</div>
															<div class="r-ft  ui-popup-close" aria-owns="user-noti-flyout">
																<div class="ui-header-actions fsm fwn fcg">
																	<i class="fa fa-times fa-1g"></i>
																</div>
															</div>
														</div>
													</div>
												</div>
											</div>
										</div>
										<div class="noti-flyout-footer">
											<a class="see-more" href="/notifications"><span>전체보기</span></a>
										</div>										
									</div>
								</div>
							</div>
						</li>
						<li id="nav-notice-list" class="rw-tool-button <c:if test="${currentInfo.noticeCount > 0 }">_new</c:if>">
							<a	class="dropdown-toggle"
								id="user-notice-flyout-trigger"
								aria-owns="user-notice-flyout"
								aria-haspopup="true"
								rel="toggle">
								<i class="fa fa-bullhorn fa-1g"></i>
								<span class="noti-count">
									<span id="notice-count-value">${currentInfo.noticeCount }</span>
								</span>
							</a>
							<div id="user-notice-flyout"
								 class="noti-flyout ui-toggle-flyout"
								 data-which="notice"
								 data-top-most="true"
								 data-popup-group="global"
								 data-popup-trigger="user-notice-flyout-trigger"
								 data-fn-show="NotifyFlyout.onShow"
								 data-fn-hide="NotifyFlyout.onHide">
								<div class="noti-flyout-top-header">
									<div class="top-nub-wrapper">
										<div class="top-nub" id="t-notice-top-nub"></div>
									</div>
								</div>
								<div class="noti-flyout-item-list">
									<div>
										<div class="psr"></div>
										<div class="ui-scrollable-area" id="t-notice-s-area">
														
															<div class="notice-list hot-unit">
																<!-- begin:리스트  -->
																<ul class="ui-list"
																	id="t-notice-l" 
																	role="list" 
																	data-async="async-get" 
																	data-stream-url="/bar/notice"
																	data-bw-map="{&quot;top&quot;:false,&quot;cid&quot;:&quot;0&quot;,&quot;size&quot;:&quot;10&quot;}"
																	data-fw-map="{&quot;top&quot;:true,&quot;cid&quot;:&quot;0&quot;,&quot;size&quot;:&quot;10&quot;}">
																	<li class="no-data empty hidden-elem">
																		<table class="ui-grid no-activity-grid" cellspacing="0" cellpadding="0">
																			<tbody>
																				<tr>
																					<td class="v-mid h-cent">새로운 공지사항이 없습니다.</td>
																				</tr>
																			</tbody>
																		</table>
																	</li>
																	<li class="next-loading empty hidden-elem">
																		<table class="ui-grid no-requests-grid" cellspacing="0" cellpadding="0">
																			<tbody>
																				<tr>
																					<td class="v-mid h-cent"><img src="/assets/sunny/2.0/img/sunny-loader-big.gif"/></td>
																				</tr>
																			</tbody>
																		</table>
																	</li>
																</ul>
																<!-- end:리스트  -->																		
															</div>
															
											
										</div>
										<div class="ui-contextual-layer-positioner ui-layer" style="left:0px; top:0;">
											<div class="ui-contextual-layer" style="bottom:0px;">
												<div id="t-notice-h-inner" class="_ptt">
													<div class="ui-header noti-flyout-header">
														<div class="ui-header-top z1">
															<div class="l-ft">
																<h3 class="ui-header-title" aria-hidden="true">공지사항</h3>
															</div>
															<div class="r-ft  ui-popup-close" aria-owns="user-notice-flyout">
																<div class="ui-header-actions fsm fwn fcg">
																	<i class="fa fa-times fa-1g"></i>
																</div>
															</div>
														</div>
													</div>
												</div>
											</div>
										</div>
										<div class="noti-flyout-footer">
											<a class="see-more" href="/notice"><span>전체보기</span></a>
										</div>										
									</div>
								</div>
							</div>							
						</li>
						
						
						<li id="nav-message-list" class="rw-tool-button <c:if test="${currentInfo.messageCount > 0 }">_new</c:if>">
							<a	class="dropdown-toggle"
								id="user-message-flyout-trigger"
								aria-owns="user-message-flyout"
								aria-haspopup="true"
								rel="toggle">
								<i class="fa fa-comments fa-1g"></i>
								<span class="noti-count">
									<span id="message-count-value">${currentInfo.messageCount }</span>
								</span>								
							</a>
							<div id="user-message-flyout"
								class="noti-flyout ui-toggle-flyout"
								 data-which="message"
								 data-top-most="true"
								 data-popup-group="global"
								 data-popup-trigger="user-message-flyout-trigger"
								 data-fn-show="NotifyFlyout.onShow"
								 data-fn-hide="NotifyFlyout.onHide">
								<div class="noti-flyout-top-header">
									<div class="top-nub-wrapper">
										<div class="top-nub" id="t-message-top-nub"></div>
									</div>
								</div>
								<div class="noti-flyout-item-list">
									<div>
										<div class="psr"></div>
										<div class="ui-scrollable-area" id="t-message-s-area">
															<div class="message-list hot-unit">
																<!-- begin:리스트  -->
																<ul class="ui-list" id="t-message-l" 
																	role="list" 
																	data-async="async-get" 
																	data-stream-url="/bar/messages"
																	data-bw-map="{&quot;top&quot;:false,&quot;uid&quot;:&quot;0&quot;,&quot;size&quot;:&quot;10&quot;}"
																	data-fw-map="{&quot;top&quot;:true,&quot;uid&quot;:&quot;0&quot;,&quot;size&quot;:&quot;10&quot;}">
																	<li class="no-data empty hidden-elem">
																		<table class="ui-grid no-activity-grid" cellspacing="0" cellpadding="0">
																			<tbody>
																				<tr>
																					<td class="v-mid h-cent">새로운 메세지가 없습니다.</td>
																				</tr>
																			</tbody>
																		</table>
																	</li>
																	<li class="next-loading empty hidden-elem">
																		<table class="ui-grid no-requests-grid" cellspacing="0" cellpadding="0">
																			<tbody>
																				<tr>
																					<td class="v-mid h-cent"><img src="/assets/sunny/2.0/img/sunny-loader-big.gif"/></td>
																				</tr>
																			</tbody>
																		</table>
																	</li>
																</ul>
																<!-- end:리스트  -->																		
															</div>
										</div>
										<div class="ui-contextual-layer-positioner ui-layer" style="left:0px; top:0;">
											<div class="ui-contextual-layer" style="bottom:0px;">
												<div id="t-message-h-inner" class="_ptt">
													<div class="ui-header noti-flyout-header">
														<div class="ui-header-top z1">
															<div class="l-ft">
																<h3 class="ui-header-title" aria-hidden="true">받은 메세지함</h3>
															</div>
															<div class="r-ft  ui-popup-close" aria-owns="user-message-flyout">
																<div class="ui-header-actions fsm fwn fcg">
																	<i class="fa fa-times fa-1g"></i>
																</div>
															</div>
														</div>
													</div>
												</div>
											</div>
										</div>
										<div class="noti-flyout-footer">
											<a class="see-more" href="/message/channels"><span>전체보기</span></a>
										</div>										
									</div>
								</div>
							</div>
						</li>
					</ul>
					<div class="rw-user-menu-more">
						<a	class="dropdown-toggle" 
							id="user-menu-more-trigger"
							aria-owns="user-menu-more-trigger-popup-menus"
							aria-haspopup="true"
							rel="toggle">
							<i class="fa fa-caret-down fa-1g"></i>
						</a>
						<ul	class="ui-toggle-flyout dropdown-menu pull-right dropdown-caret dropdown-closer navbar-dropdown-menu"
							id="user-menu-more-trigger-popup-menus"
							data-popup-trigger="user-menu-more-trigger"
							data-popup-group="global">
							<c:if test="${authUserId != '203'}">
							<li><a href="/user/${authUserId}/setting" class="h-icon"><i class="fa fa-cog fa-1g"></i><span class="menu-text">설정</span></a></li>
							</c:if>
							<li><a href="/user/${authUserId}" class="h-icon"><i class="fa fa-user fa-1g"></i><span class="menu-text">프로필 편집</span></a></li>
							
							<c:if test="${authUserIsAdmin}">
								<li class="divider"></li>
								<li><a class="btn-new-user" href="#"><i class="fa fa-plus-square fa-1g"></i><span class="menu-text">구성원 초대하기</span></a></li>
							</c:if>
							<li class="divider"></li>
							<li class="${param.menuName == '알려진 버그' ? 'active' : '' }">
								<a href="/issue">
									<i class="fa fa-bug mb-mgr"></i>
									<span class="menu-text">알려진 버그</span>
								</a>
							</li>
							<li class="${param.menuName == '피드백' ? 'active' : '' }">
								<a href="/feedback">
									<i class="fa fa-smile-o mb-mgr"></i>
									<span class="menu-text">버그 신고 및 기능 요청</span>
								</a>
							</li>
							<li class="divider"></li>
							<li><a href="/user/logout"><i class="fa fa-power-off fa-1g"></i><span class="menu-text">로그아웃</span></a></li>
						</ul>
					</div>
				</div>
				<div id="rw-right-bar-button"></div>				
			</div>		
</div>	


<c:if test="${authUserIsAdmin}">
<%--새로운 사용자 추가하기. 관리자일때만 import 한다.  --%>
<script>
 $(function(){
 $(".btn-new-user").onHMOClick(null, function() {
		bootbox.dialog( "res-dialog-new-user", [{
			"label" : "취소",
			"class" : "hmo-button hmo-button-white hmo-button-small-10"
		},{
			"label" : "확인",
			"class" : "hmo-button hmo-button-blue hmo-button-small-10",
			"callback": function() {
				var result = $( "#form-new-user" ).valid();
				if( !result ) {
					return false;
				}
				//
				var data = {}, arr;
				
				arr = $('#form-new-user').serializeArrayAlt();
				$.each( arr, function() {
					data[this.name] = this.value;
				});
				
				$.ajax({
					url:"/a/user/invite",
					type:"post",
					dataType:"json",
					headers: {
						'Accept':'application/json',
						'Content-Type':'application/json'
						},
				    data: JSON.stringify( deepen( data ) ),
				    success: function( data ) {
				    	if( data.result == "fail" ){
				    		//Dialog.showMessage( "오류가 발생되었습니다.", data.message, MB_INFORMATION);
				    		alert( data.message );
				    		return;
				    	}
						MessageBox( "초대되었습니다.", "초대되었습니다. 사용자가 이메일을 클릭하면 가입이 완료됩니다.", MB_INFORMATION );
				    	//window.location.reload();
				    },
					error:function( jqXHR,textStatus,errorThrown ) {
						$.log( "error:Event.__inlineSubmit:" + errorThrown );
					}
				});
				return true;
			}
		}],{
			"header" : "사용자 초대",	
			"embed" : true,
			"onInit" : function(){
			}
		});
	});
 
//validation for new user
	$( "#form-new-user" ).validate({
		errorElement: 'span',
		errorClass: 'help-inline',
		focusInvalid: false,
		onfocusout:function (element) {
			$(element).valid();
	    },
		rules: {
			"email": {
				required: true,
				email: true
				,
				remote: {
					url: "/user/valid/email",
					type: "get",
					dataType:"json",
			    	data: {
			            email: function() { return $("#email").val(); }
			        },
			        dataFilter: function( response ) {
		            	var data = jQuery.parseJSON( response );
						return ( data.result == "success" );
			        }
				}				
			}		
		},
		messages: {
			"email": {
				required: "필수입력 항목입니다.",
				email: "올바르지 않은 형식의 이메일입니다.",
				remote: "이미 가입된 이메일입니다."
			}
		},
		invalidHandler: function (event, validator) {	
		},
		highlight: function (e) {
			$(e).closest('.control-group').removeClass('info').addClass('error');
		},
		success: function (e) {
			$(e).closest('.control-group').removeClass('error').addClass('info');
			$(e).remove();
		},
		errorPlacement: function ( error, element ) {
			if(element.is(':checkbox') || element.is(':radio')) {
				var controls = element.closest('.controls');
				if(controls.find(':checkbox,:radio').length > 1) {
					controls.append(error);
				} else {
					error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
				}
			} else if(element.is('.select2')) {
				error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
			} else if(element.is('.chosen-select')) {
				error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
			} else {
				error.insertAfter( element.parent().eq(0) );
			}
		},
		submitHandler: function (form) {
		}
	});		
 });
 </script>
 <!-- BEGIN:resource of dialog for creating user -->
<div id="res-dialog-new-user" style="display:none">
	<div class="rw-dialog-wrap rw-form-wrap  pop-new-user-wrap">
		<form class="form-vertical rw-form" id="form-new-user">
			<div class="row-fluid">
				<div class="span12">
				
					<div class="control-group">
						<label class="control-label" for="name">이름</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon-right rw-input-wrap">
									<input class="span12" type="text" name="name" id="name" placeholder="이름" autocomplete=off>
									<i class="icon-remove-sign"></i>
									<i class="icon-ok-sign"></i>
								</span>
							</div>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="email">이메일 주소</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon-right rw-input-wrap">
									<input class="span12" type="email" name="email" id="email" placeholder="초대할 이메일@emailaddress.com" autocomplete=off>
									<i class="icon-remove-sign"></i>
									<i class="icon-ok-sign"></i>
								</span>
							</div>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="job1">직책1</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon-right rw-input-wrap">
									<input class="span12" type="text" name="jobTitle1" id="job1" placeholder="직책1">
								</span>
							</div>
						</div>
					</div>				
					<div class="control-group">
						<label class="control-label" for="job2">직책2</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon-right rw-input-wrap">
									<input class="span12" type="text" name="jobTitle2" id="job2" placeholder="직책2">
								</span>
							</div>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="job3">직책3</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon-right rw-input-wrap">
									<input class="span12" type="text" name="jobTitle3" id="job3" placeholder="직책3">
								</span>
							</div>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="inviteMessage">초대 메시지</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon-right rw-input-wrap">
									<input class="span12" type="text" name="inviteMessage" id="inviteMessage" placeholder="설명">
								</span>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
<!-- END:resource of dialog for creating user -->

<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/jquery.validate.js"></script>
</c:if>
<%-- 새로운 사용자 추가 끝 --%>