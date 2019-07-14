<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">채팅</c:param>
	<c:param name="bsUsed">YES</c:param>
	<c:param name="aceUsed">YES</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>

<style type="text/css">
.rw-content-area-wrap	{background-color: white;}
.scroll-y 				{ padding: 0;}
.nav-search-icon		{ top: 2px; left: 3px;}
</style>


<script	src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-elements.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace.js"></script>
<script	src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-extra.js"></script>

<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/jquery.validate.js"></script>
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
					<c:param name="menuName">채팅</c:param>
				</c:import>
				<!-- END:nav-list -->

			</div>
			<!-- END:sidebar -->

			<!-- BEGIN:main-content -->
			<div class="main-content">

				<!-- BEGIN:breadcrumbs&search -->
				<c:set var="breadcrumbs" value="채팅" scope="request" />
				<c:set var="breadcrumbLinks" value="/" scope="request" />
				<div class="breadcrumbs" id="breadcrumbs">
					<c:import url="/WEB-INF/views/common/breadcrumbs.jsp">
						<c:param name="breadcrumbs">${breadcrumbs }</c:param>
						<c:param name="breadcrumbLinks">${breadcrumbLinks }</c:param>
					</c:import>
				</div>
				<!-- END:breadcrumbs&search -->

				<!-- BEGIN:page-content -->
				<div class="page-content">
					<div class="rw-content-area-wrap">
						<div class="rw-content-area" >
							
							<div class="rw-pagelet-wrap" >
								<div class="z1" style="margin-bottom: 5px; border-bottom: 1px solid lightgrey; ">
									<h6 class="pull-left">
									총 (${ channel.userCount })명
									<c:forEach items="${channel.userRelation}" var="channelUser"
											varStatus="status">
										${channelUser.user.name }${ status.last != true ? ", " : ' ' }
									</c:forEach>
									</h6> 
									<div class="pull-right">
										<a href="#" class="hmo-button hmo-button-white hmo-button-small-10" data-id="${channel.id }">나가기</a>
										<a class="hmo-button hmo-button-khaki hmo-button-small-9" id="btn-invite-chat">초대하기</a>
									</div>
								</div>
								<div class="message-inside-content"  style="overflow: hidden; position: relative; direction: ltr; ">
									<div id="message-list-scrollable" style="height: 100%; outline:none; overflow-x:hidden; overflow-y:auto; position:relative; ">
										<div class="dialogs" id="message-list" class="ui-stream"
											role="list" data-async="async-get"
											data-stream-url="/message/${channel.id }"
											data-bw-map='{"top":false,"ur":false,"msgId":"","size":"10"}'
											data-fw-map='{"top":true,"ur":false,"msgId":"","size":"10"}'>
		
										</div>
									</div>
								</div>
								<div id="inside-channel-status" style="height: 20px; width: 200px; ">
									<span class="read_all" style="display:none; ">모두 읽음</span>
									<a href="#" id="pop-message-readers" data-request-map='{"channelId":"${channel.id }"}'><span class="read_some" style="display:none; "><em class="count"></em>명이 읽음</span></a>
								</div>
								<div style="padding: 10px;">
									<div class="row-fluid">
										<form rel="async" name="send-message-form" method="post" action="" data-live="{}" onsubmit="return window.Event &amp;&amp; Event.__inlineSubmit &amp;&amp; Event.__inlineSubmit(this,event)">
<!-- 										<textarea class="span12"  -->
<!-- 										id="message-input" name="message-input" title="메시지 입력" -->
<!-- 											placeholder="메시지 입력" autocomplete="off" -->
<%-- 											data-request-map='{"channel":{"id":"${channel.id }"}}'></textarea> --%>
<%-- 											<input class="span12" id="message-input" name="message-input" title="메시지 입력" type="text" placeholder="메시지 입력" autocomplete="off" data-request-map='{"channel":{"id":"${channel.id }"}}'> --%>
											
											<div class="ui-mentions-input">
												<div class="highlighter">
													<div>
														<span class="highlighter-content hidden-elem"></span>
													</div>
												</div>
												<div class="ui-type-head mentions-type-head">
													<div class="wrap">
														<div class="inner-wrap">
															<textarea name="send-message-text" title="메시지 보내기" placeholder="메시지 보내기" 
																	  autocomplete="off"
																	  spellcheck="false"  
																	  tabindex=""
																	  onfocus="on_ta_focus(this,true);" 
																	  onblur="on_ta_focus(this);"
																	  data-want-return="ta_want_return_handler"
																	  data-request-map="{&quot;channelId&quot;:&quot;${channel.id }&quot;}"
																	  class="text-input mentions-textarea ui-textarea-auto-grow data-textarea-autogrow-set ui-textarea-no-resize ui-add-comment-input dom-control-placeholder"></textarea>
															<span class="placeholder" onclick="document.forms[&quot;send-message-form&quot;][&quot;send-message-text&quot;].focus();">메시지 보내기</span>
														</div>
													</div>
												</div>
											</div>
										</form>
									</div>
								</div>
								
								
								
<!-- 								<button class="hmo-button hmo-button-blue hmo-button-small-10" -->
<!-- 									onclick="return send_message();" -->
<!-- 									style="float: right; margin-right: 10px;"> -->
									
<!-- 									<span>메시지 입력</span> -->
<!-- 								</button> -->
								
								<label class="text-right" style="float:right; margin-top: 5px; ">
<!-- 									<input type="checkbox">엔터로 메시지 보내기 -->
								</label>
								
							</div>
						</div>
						
									<c:forEach items="${channel.userRelation}" var="channelUser"
											varStatus="status">
										${channelUser.user.name } - (${channelUser.lastReadMessageInfoId })까지 읽음 <br />
									</c:forEach>
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
			
<!-- BEGIN:resource of dialog for invite users -->
<div id='res-dialog-invite-chat' style='display:none'>
	<div class='rw-dialog-wrap chat-scroll-wrap'>
		<div class='row-fluid'>
			<div class='span12'>
				<div class='tabbable'>
					<ul class='nav nav-tabs padding-12 tab-color-blue background-blue' id=''>
						<li class='active'>
							<a data-toggle='tab' href='#tab-all'>모든 사용자들</a>
						</li>
						<li>
							<a data-toggle='tab' href='#tab-my-departs'>내가 속한 부서의 사용자들</a>
						</li>
					</ul>
					<div class='tab-content'>
						<div id='tab-all' class='tab-pane in active'>
							<div id="invite-chat-wrap" class="">
								<div id="invite-friend-list" class="uiUserList" role="list"
									data-async="async-get" data-stream-url="/user/stream/order"
									data-bw-map="{&quot;top&quot;:false,&quot;name&quot;:&quot;&quot;,&quot;size&quot;:&quot;10&quot;}"
									data-fw-map="{&quot;top&quot;:true,&quot;name&quot;:&quot;&quot;,&quot;size&quot;:&quot;10&quot;}">
									
								</div>
								<div class="ui-stream-pagelet stream-pager">
									<div class="z1 mts ui-more-pager">
										<div>
											<div class="ui-box ui-more-pager-primary" id="invite-friend-no-more">로딩이 끝났습니다.</div>
											<div class="ui-more-pager-loader ui-box" id="invite-friend-loading"><img class="img" src="/assets/desktop/img/ins/yacamp-loader-m.gif" alt="" height="11px" width="16px"></div>
										</div>
									</div>
								</div>
							</div>						
						</div>

						<div id='tab-my-departs' class='tab-pane'>
							<p>내가 속한 부서의 사용자들</p>
						</div>
						
					</div>
				</div>
			</div>						
		</div>							
	</div>
</div>
<!-- END:resource of dialog for invite users -->

<!-- BEGIN:resource of dialog -->
<div id="res-dialog-leave-channel" style="display:none">
	<div class="rw-dialog-wrap rw-form-wrap" id="res-dialog-delete-user-content">
		대화방을 나가면 상대방에게 메시지가 전달되며<br>앞으로 대화방의 알림을 받아볼 수 없습니다. 나가시겠습니까?
	</div>
</div>
<!-- END:resource of dialog -->


<script>
var _is_invite_friend_streaming=false, _ejs_invite_friend_stream_template=null;
var end_of_chat_user_stream=false;
var alreadyOpened=false;
function on_backward_invite_friend_streaming(result){
	var data = result.data;
	if( !data || data.length==0){
		_$ifld.hide();
		_$ifnm.show();
		_is_invite_friend_streaming=false;
		end_of_chat_user_stream=true;
		return;
	}
	for( var i=0; i<data.length; i++){
			_$ifl.append(_ejs_invite_friend_stream_template.render(data[i]));
			if(i+1==data.length){
				var dataMap=$.parseJSON(_$ifl.attr("data-bw-map"));
				dataMap["name"]=data[i]["userName"];
				_$ifl.attr("data-bw-map", JSON.stringify(dataMap));
			}
			if(_$ifl.children().length==1){
				var dataMap=$.parseJSON(_$ifl.attr("data-fw-map"));
				dataMap["name"]=data[i]["userName"];
				_$ifl.attr("data-fw-map", JSON.stringify(dataMap));
			}
			
	}
	
	_$ifld.hide();
	_is_invite_friend_streaming = false;
}


$(function(){
	_$ifl=$("#invite-friend-list");
	_$ifnm=$("#invite-friend-no-more");
	_$ifld=$("#invite-friend-loading");	
});


function invite_friend_streaming(direction){
	
	alreadyOpened=true;
	
	if( _is_invite_friend_streaming ){
		return;
	}
	_is_invite_friend_streaming = true;
	
	if( direction ){
		_$ifnm.hide();
		_$ifld.show();
	}
	var url=_$ifl.attr("data-stream-url");
	var mode=_$ifl.attr("data-async");

	if(!mode){ mode="async-get"; }
	
	var modes=mode.split("-"),
	async=(modes[0]=="async"),
	type=(modes[1]=="post")?"post":"get",
	data=$.parseJSON(_$ifl.attr(direction?"data-fw-map":"data-bw-map"));

	$.ajax({
		url:url  + "?rnd=" + Math.floor(Math.random() * 999999999),
		async:async,
		type:type,
		dataType:"json",
		contentType:"application/json",
		data:data,
		success:(direction?on_forward_invite_friend_streaming:on_backward_invite_friend_streaming),
		error:function(jqXHR,textStatus,errorThrown){
			alert("mod-streaming["+_$ifl.identify()+"]:"+errorThrown);
			$.error("mod-streaming["+_$ifl.identify()+"]:"+errorThrown);
		}	
	});
}



function on_invite_friends_scroll(e, pos){
	if( pos == "bottom" && end_of_chat_user_stream==false){
		invite_friend_streaming(false);
	}
}



var _ejs_message_readers = null;

function on_message_readers_scroll(e, pos){
	if (pos == "bottom" /* && end_of_chat_user_stream == false */) {
		//invite_friend_streaming(false);
	}
}


$( function() {
// Initialize Dialog
	
	$("#pop-message-readers").onHMOClick(null, function(e){
		e.preventDefault();
		
		$this = $(this);
		map = $this.data("request-map");
		
		if (_ejs_message_readers == null) {
			_ejs_message_readers = new EJS(
				{
					url : "/assets/sunny/2.0/js/template/message-readers.ejs?v=1.0"
				});
		}
		var ajaxMessageReaders = $.ajax({
			url: "/message/readers",
			type: "GET",
			dataType: "json",
			data:map
		});
		
		$bootboxContent = $("#res-dialog-pop-message-readers-content");
		bootbox.dialog( "res-dialog-pop-message-readers", [{
			"label" : "확인",
			"class" : "hmo-button hmo-button-blue hmo-button-small-10"
		}],{
			"header" : "읽은 사용자",
			"embed" : true,
			"animate" : false,
			"onInit" : function() {
				
				$.when(ajaxMessageReaders)
				.done(function(data){
					if(data.result == "fail"){
						alert( data.message );
					}
					var html = "";
					$.each( data.data, function( idx, d ){
						html += _ejs_message_readers.render(d);

					});
					$bootboxContent.html( html );
				})
				.fail(function(data){
					alert("오류발생");
				});
				
				$('.pop-message-readers-scroll-wrap').slimscroll({
					height : "350px",
					railVisible : false,
					alwaysVisible : false,
					touchScrollStep : 70
				}).bind('slimscroll', on_message_readers_scroll);
			},
			"onFinalize" : function() {
				//alert( "call finalize" );	
				//window.location.reload();
			},
			"beforeShown" : function() {
				// 로딩으로 변경
				//$("#res-dialog-show-profile-content").html( "로딩중" );
				$bootboxContent.html( "<i class='fa fa-spinner fa-spin'></i>" );
			}
		});
		
		
		
		
	});
	
	$(".btn-leave-channel").onHMOClick(null, function(){
		var channelId = $(this).attr("data-id");
		
		bootbox.dialog( "res-dialog-leave-channel", [
		{
			"label" : "취소",
			"class" : "hmo-button hmo-button-white hmo-button-small-10"
		},{
			"label" : "확인",
			"class" : "hmo-button hmo-button-blue hmo-button-small-10",
			"callback" : function(){
				$.ajax({
					url:"/channel/leave?id=" + channelId ,
					type:"get",
					dataType:"json",
					headers: {
						'Accept':'application/json',
						'Content-Type':'application/json'
					},
					success: ajax_leave_channel,
					error:function( jqXHR,textStatus,errorThrown ) {
						$.log( "error:Event.__inlineSubmit:" + errorThrown );
					}
				});
				
				return true;
			}
		}                                            
		], {
			"header" : "대화방을 나가시겠습니까?",	
			"embed" : true,
		});
	});
	

	$("#btn-invite-chat").onHMOClick(null, function() {
		bootbox.dialog( "res-dialog-invite-chat", [{
			"label" : "취소",
			"class" : "hmo-button hmo-button-white hmo-button-small-10"
		},{
			"label" : "확인",
			"class" : "hmo-button hmo-button-blue hmo-button-small-10",
			"callback": function() {
// 				var result = $( "#form-new-user" ).valid();
// 				if( !result ) {
// 					return false;
// 				}
				//
// 				var data = {}, arr;
				
// 				arr = $('#form-new-user').serializeArrayAlt();
// 				$.each( arr, function() {
// 					data[this.name] = this.value;
// 				});
				var data = new Array();
		
				$("input[name='invite-checked']:checked").each(function(index){
					data[index] = $(this).val();
				})
				if( data == null || data.length < 1 ){
					alert( "사용자를 선택하지 않았습니다.");
					return false; 
				}
				$.ajax({
					url:"/channel/invite?id=${channel.id}",
					async:false,
					type:"post",
					dataType:"json",
					contentType:"application/json",
					data:JSON.stringify(
							data
							),
					success:function(data){
						//alert("성공 : " + data.data.id);
						if( data.data ){
							location.href="/message/" + data.data.id;
						}else{
							alert("모두 방에 존재합니다");
						}
					},
					error: function(jqXHR,textStatus,errorThrown){
						$.error("confirm_alter_time:$.ajax:"+errorThrown);
					}
				});	 
				return true;
			}
		}],{
			"header" : "새로운 사용자 추가",	
			"embed" : true
		});
		
		if( _ejs_invite_friend_stream_template == null ){
			_ejs_invite_friend_stream_template = new EJS( {
				url: "/assets/sunny/2.0/js/template/invite-friend-stream.ejs?v=1.0"
			});
		}
		$('.chat-scroll-wrap').slimscroll({ height:"390px", railVisible:false,  alwaysVisible: false }).bind('slimscroll', on_invite_friends_scroll);
		if( alreadyOpened == false ){
			invite_friend_streaming(false);
		}
	});
});
</script>


<script>

/* -- mod-reply-textarea-autosize -- */
var _message_ta_autoresize_adjust_active=false;
var _message_ta_autoresize_input_offset=0;
var _$message_ta_autoresize_mirror;

function message_ta_clear($ta){
	$ta.val("");
	setTimeout(function(){message_ta_adjust.call($ta.get(0))},10);
}

function message_ta_test_enter(){
	var	
	ta=this,
	$ta=$(ta),
	text=$ta.val(),
	mirror=_$message_ta_autoresize_mirror.get(0),
	original = $ta.height(),
	height;

	_$message_ta_autoresize_mirror.val(text+"\n");
	mirror.scrollTop = 0;
	mirror.scrollTop = 9e4;
	height = mirror.scrollTop + _message_ta_autoresize_input_offset;

	(original !== height) && $ta.css("height",height);
	_$message_ta_autoresize_mirror.val(text);
}

function message_ta_adjust() {
	if(_message_ta_autoresize_adjust_active) { return; }
	var	
	ta=this,
	$ta=$(ta), 
	mirror=_$message_ta_autoresize_mirror.get(0),
	original = $ta.height(),
	height;

	_message_ta_autoresize_adjust_active=true;
	_$message_ta_autoresize_mirror.val($ta.val());
		
	mirror.scrollTop = 0;
	mirror.scrollTop = 9e4;
	height = mirror.scrollTop + _message_ta_autoresize_input_offset;

	(original !== height) && $ta.css("height",height);
	setTimeout(function (){ _message_ta_autoresize_adjust_active = false; }, 1);
}

function message_ta_keydown(event){
	
	var ta=this, $ta=$(ta);
	
	if(event.keyCode != 10 && event.keyCode != 13) {
		if(event.keyCode == 27){$ta.blur();}			
		return;
	}
	
	
//	if( !__mobile__["is"] && !__mobile__[ "ipad" ]  && event.shiftKey === false ) {
	if( event.shiftKey === false ) {
		event.preventDefault();		
		var handlerName = $ta.attr("data-want-return");
		setTimeout(function(){window[handlerName].call($ta);},10);		
		return;								
	}
	
	message_ta_test_enter.call(ta);
}

function message_ta_autoresize() {
	$(".data-textarea-autogrow-set").
	each(function(){
		var 
		ta=this, 
		$ta=$(this);

		_$message_ta_autoresize_mirror.css("width",$ta.width()-10);
		if($ta.css('box-sizing')==="border-box"||$ta.css('-moz-box-sizing')==="border-box"||$ta.css('-webkit-box-sizing')==="border-box"){
			_message_ta_autoresize_input_offset = $ta.outerHeight() - $ta.height();
		}
		
		$ta.keydown( message_ta_keydown );
		
		if('onpropertychange' in ta) {
			if('oninput' in ta) {
		    	ta.oninput = message_ta_adjust;
		    	$ta.keydown(function(){setTimeout(message_ta_adjust.bind(ta),50);});
			} else {
				ta.onpropertychange=message_ta_adjust;
		    }
		}else{
		    ta.oninput=message_ta_adjust;
		}
		$ta.removeClass("data-textarea-autogrow-set");
	});
}
/* -- mod-message-textarea-autosize -- */

$(function(){

	//	
	_$message_ta_autoresize_mirror = $("#ta-cmmnt-mirroring");
	message_ta_autoresize();
});


</script>
<script>
	
	var _pre_scroll_gap=200, _$pl,_$pmn,_$ml,_is_msg_streaming=false,_ejs_message_stream_template; 
	
	function on_message_scroll(event){
		if( $("#message-list-scrollable").scrollTop() < 21 ){
			
			message_inner_streaming( true );
		}
	}
	
	
	var wait_message=0;
	function on_ta_focus(ta,focused){
		var $ta = $(ta);
		if(focused){
			$ta.next().hide();			
		} else {
			($ta.val() == "") && $ta.next().show();
		}
	}

	function ta_want_return_handler(){
		if( wait_message == 1 ){ return; }
		wait_message = 1;
		var 
		$ta=$(this),
		message=$ta.val();
		if( message == "" ){
			wait_message = 0;
			return;
		} 
		var data=$.parseJSON($ta.attr("data-request-map"));
		data["text"]=message;
		$.ajax({
			url: "/message/send",
			async: false,
			type: "post",
			dataType: "json",
			contentType: 'application/json',
			headers: {
				"Accept": "application/json",
				"Content-Type": "application/json"
			},
			data:JSON.stringify(data),
			success: function(data){
				wait_message = 0;
				message_ta_clear($ta);
				if(data.result != "success"){
					$.error("error:ta_want_return_handler:$.ajax-" + data.message);
					return;
				}
				$("#inside-channel-status .read_some").hide();
				$("#inside-channel-status .read_all").hide();
				
				html = _ejs_message_stream_template.render(data.data);

				_$ml.append( html );
// 				refresh_timesince.call($li.find(".livetimestamp"));
				timesince();
				$("#message-list-scrollable").scrollTop($("#message-list-scrollable").prop("scrollHeight"));
			},
			error: function( jqXHR, textStatus, errorThrown ){
				$.error("add replys:"+errorThrown);
			}		
		});
	}

	
// 	function send_message(){
// 		var $ta=$(this);
// 		var message = $ta.val();
// 		if( message == "" ){
// 			return;
// 		}
		
// 		var data = {
// 				"channelId" : "${channel.id}",
// 				"text" : message
// 		}
// 		$.ajax( {
// 	        url : "/message/send",
// 	        type: "post",
// 	        dataType: "json",
// 	        contentType: 'application/json',
// 	        data: JSON.stringify(data),
// 	        success: function(data){
// 	        	$ta.val("");
// 				if(data.result != "success"){
// 					$.error("error:ta_want_return_handler:$.ajax-" + data.message);
// 					return;
// 				}
				
				
				
// 	        },
// 	        error: function( jqXHR, textStatus, errorThrown ){
// 	            alert( textStatus + " : " + errorThrown );
// 	        },
// 	        statusCode: {
// 	            "401" : function() {
// 	                alert( "Unauthorized" );
// 	            },
// 	            "404" : function() {
// 	                alert( "Unkown" );
// 	            }
// 	        }        
// 	    } );
		
// 	}

	
	function read_messages( unreadCount ){
		// 스트리밍이 진행중일 때는 readMessage 를 진행하지 않는다.
// 		if( _is_msg_streaming ){
// 			setTimeout( function(){ read_messages( startInfoId, lastInfoId ) }, 1000);
// 			return;
// 		}
		
		if( unreadCount == 0 ){
			$("#inside-channel-status .read_some").hide();
			$("#inside-channel-status .read_all").show();
		}else if( ${channel.userCount} - unreadCount == 1 ){
			$("#inside-channel-status .read_some").hide();
			$("#inside-channel-status .read_all").hide();
		}else if( unreadCount > 0){
			$("#inside-channel-status .read_some em").html(${channel.userCount} - unreadCount - 1);
			$("#inside-channel-status .read_some").show();
			$("#inside-channel-status .read_all").hide();
		}
		
		// 처음 들어온 사용자
// 		if( startInfoId == null ){
// 		}
		
// 		var infoId = "";
// 		var $count;
// 		$($(".message-list-each").get().reverse()).each(function(index){
			
// 			infoId = $(this).data("id");
			
// 			if( lastInfoId != null && infoId > lastInfoId ){
// 				return true; 
// 			}
			
// 			if( startInfoId != null && infoId <= startInfoId ){
// 				return false;
// 			}
// 			$count = $(this).find(".message-list-count");
			
// 			var cntVar = parseInt($count.text());
// 			if(cntVar > 0)
// 				$count.text( cntVar - 1);
			
// 		});
	}
	
	function message_inner_streaming(direction){
		if(_is_msg_streaming){ 
			return; 
		}
		_is_msg_streaming=true;
		if(direction){
			_$pmn.hide();
			_$pl.show();
		} else {
		}
		
		var url=_$ml.attr("data-stream-url");
		var mode=_$ml.attr("data-async");
		
		if(!mode){ mode="async-get"; }
		
		var modes=mode.split("-"),
			async=(modes[0]=="async"),
			type=(modes[1]=="post")?"post":"get",
			data=$.parseJSON(_$ml.attr(direction?"data-fw-map":"data-bw-map"));

		$.ajax({
			url:url,
			async:async,
			type:type,
			dataType:"json",
			contentType:"application/json",
			data:data,
			success:(direction?on_forward_streaming:on_backward_streaming),
			error:function(jqXHR,textStatus,errorThrown){
				alert("mod-streaming["+_$ml.identify()+"]:"+errorThrown);
				$.error("mod-streaming["+_$ml.identify()+"]:"+errorThrown);
			}	
		});
	}
	
	function on_forward_streaming(result){
		var data=result.data;
		if( !data || data.length==0){
			_is_msg_streaming=false;
			return;
		}
		for(var i=0;i<data.length;i++){
			try{
				_$ml.prepend(_ejs_message_stream_template.render(data[i]));
				if(i+1==data.length){
					var dataMap=$.parseJSON(_$ml.attr("data-fw-map"));
					dataMap["msgId"]=data[i].info.id;
					_$ml.attr("data-fw-map", JSON.stringify(dataMap));
				}
				if(_$ml.children().length==1){
					var dataMap=$.parseJSON(_$ml.attr("data-bw-map"));
					dataMap["msgId"]=data[i].info.id;
					_$ml.attr("data-bw-map", JSON.stringify(dataMap));
				}
			}catch(e){
				$.error(e);
			}
		}
		//comment_ta_autoresize();
		_is_msg_streaming=false;
		timesince();
		$("#message-list-scrollable").scrollTop(50);
	}

	function on_backward_streaming(result){
		$("#inside-channel-status .read_some").hide();
		$("#inside-channel-status .read_all").hide();
		var data=result.data;
		if( !data || data.length==0){
			_$pl.hide();
			_$pmn.show();
			_is_msg_streaming=false;
			return;
		}
		
		
		
		var
		l=data.length;
		html="";
		if( data[0].info.unreadCount == 0){
			$("#inside-channel-status .read_some").hide();
			$("#inside-channel-status .read_all").show();
		}else if( ( ${channel.userCount} - data[0].info.unreadCount ) == 1 ){
			$("#inside-channel-status .read_some").hide();
			$("#inside-channel-status .read_all").hide();
		}else{
			$("#inside-channel-status .read_some em").html(${channel.userCount}- data[0].info.unreadCount - 1);
			$("#inside-channel-status .read_some").show();
			$("#inside-channel-status .read_all").hide();
		}
		try{
			for(var i = l - 1; i >= 0; i--){
				html += _ejs_message_stream_template.render(data[i]);
			}
		}catch(e){
			alert( "서버통신 에러 : 다시 시도해 주세요.-" + e);
			$.error(e);
			return;
		}

		_$ml.append( html );
		
		<%-- 만약 메시지 보내고 새로운 메시지 가져오는 도중에 상대방이 읽었는지 여부 판단 후 unreadCount 줄여줌 --%>
	
		
		if(l > 0 ){
			var dataMap;
			dataMap=$.parseJSON(_$ml.attr("data-fw-map"));
			dataMap["msgId"]=data[l-1].info.id;
			_$ml.attr("data-fw-map", JSON.stringify(dataMap));
			dataMap=$.parseJSON(_$ml.attr("data-bw-map"));
			dataMap["msgId"]=data[0].info.id;

			_$ml.attr("data-bw-map", JSON.stringify(dataMap));
		}
		_$pl.hide();
		_is_msg_streaming=false;
		timesince();
		$("#message-list-scrollable").scrollTop($("#message-list-scrollable").prop("scrollHeight"));
	}

	
	$(function(){
		_$ml=$("#message-list");
		_$pmn=$("#pager-no-more");
		_$pl=$("#pager-loading");
		message_inner_streaming( false );
		$("#message-list-scrollable").scroll(on_message_scroll);
		_$ml.scrollTop(_$ml.prop("scrollHeight"));
	});
	
	_ejs_message_stream_template = new EJS( {
		url: "/assets/sunny/2.0/js/template/message-stream.ejs?v=1.0"
	});
	
	</script>

<script>
	/* Channel Controls */
	
	function ajax_leave_channel(data){
	if(data.result != "success"){
		$.error("error:ajax_leave_channel:$.ajax-" + data.message);
		return;
	}
	//$(this).parents(".ui-react-row").remove();
		$(".message-list-form").html("<div class='mtm'>대화방을 퇴장하셨습니다.<br />앞으로 대화방에서 오는 모든 소식과 내용은 볼 수 없습니다.</div>");
		
		
		location.href="/message/channels";
		
	}
	
	function autoResizeDiv(){
		$(".message-inside-content").height( ($(window).height() - $("#top-bar").height() - 270) );		
	}
	$(function(){
		window.onresize = autoResizeDiv;
		autoResizeDiv();
	});
	</script>
	
	<div id="res-dialog-pop-message-readers" style="display: none">
		<div class="rw-dialog-wrap pop-message-readers-scroll-wrap" id="res-dialog-pop-message-readers-content">
		</div>
	</div>
	
<form>
	<textarea tabindex="-1" id="ta-reply-mirroring" class="textarea-mirroring reply-mirroring"></textarea>
</form>
	
</body>
</html>