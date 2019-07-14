<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:import url="/WEB-INF/views/pagelet/pds/navbars.jsp">
</c:import>



<c:choose>
<c:when test="${current == 'list' }">
	<div id="message-list-changeable">
			<c:import url="/WEB-INF/views/pagelet/pds/list.jsp"></c:import>
	</div>
	<div id="id-message-list-button" class="clearfix " style="margin-top: 10px; ">
		<div class="pull-right">
			<a class="pds-tabs pathchange" href="${groupPath }/pds/write" data-view="write">
				<span class="hmo-button hmo-button-blue hmo-button-small-10">
					<span>자료 올리기</span>
				</span>
			</a>
		</div>
	</div>
	
	<div id="id-message-form-button" class="clearfix hide">
		<div class="pull-right">
			<a href="#" onclick="return pds_post(this);"class="hmo-button hmo-button-blue hmo-button-small-10">
				<span class="bigger-110">저장하기</span>
			</a>
		</div>
	</div>
				
	<div class="message-content hide" id="id-message-content">
		<c:import url="/WEB-INF/views/pagelet/pds/write.jsp"></c:import>	
	</div><!-- /.message-content -->

	<script src="/assets/sunny/2.0/js/uncompressed/jquery.pathchange.js"></script>
	
</c:when>
<c:when test="${ current == 'write' }">

	<div id="message-list-changeable">
		<div class="message-list-container">
			<c:import url="/WEB-INF/views/pagelet/pds/write.jsp"></c:import>	
		</div>
	</div>
	<div id="id-message-form-button" class="clearfix ">
		<div class="pull-right">
			<a href="#" onclick="return pds_post(this);"class="hmo-button hmo-button-blue hmo-button-small-10">
				<span class="bigger-110">저장하기</span>
			</a>
		</div>
	</div>
	
</c:when>
<c:otherwise>
	
	<div id="message-list-changeable">
		<div class="message-list-container">
			<div class="message-content" id="id-message-content">
				<c:import url="/WEB-INF/views/pagelet/pds/view.jsp"></c:import>
			</div>
		</div>
	</div>
	
	<div class="hide" >
		<c:import url="/WEB-INF/views/pagelet/pds/write.jsp"></c:import>	
	</div><!-- /.message-content -->
</c:otherwise>		

</c:choose>
<c:if test="${current != 'write' }">

	<script>
	
	function ajax_remove_pds(data){
		if( data.result == "fail" ){
			alert(data.message);
			return false;
		}
		window.location.href="${groupPath }/pds"
	}
	
	
	</script>
	<script>
	/* --mod-bookmark-- */
	  
	 
	function bookmark_this(_this){
		//e.preventDefault();
		
		
		var isAlreadyBookmarked = $(_this).data("already-bookmarked");
		var cid = $(_this).data("id");
		var url = isAlreadyBookmarked == true ? "/bookmark/remove" : "/bookmark/add";
		$.ajax({
			url: url ,
			type: "GET",
			dataType: "json",
			data:{
				"cid" : cid
			},
			success: function(data){
				setContentBookmarked(cid, data.data);
			}
		});
		
		return false; 
	}
	
	function setContentBookmarked( cid, bookmarked ){
		
		var $bookmarks = $(".bookmark-" + cid);
		
		if( bookmarked == false){
			$bookmarks.html("<i class='fa fa-star-o bigger-170 orange2' />");
			$bookmarks.data("already-bookmarked", false);
		}else{
			$bookmarks.html("<i class='fa fa-star  bigger-170 orange2' />");
			$bookmarks.data("already-bookmarked", true);
		}
	}
	
	</script>
	
	<script>
	/* -- mod-reply-core -- */
	var _ejs_story_stream_reply_template, wait_reply=0;
	
	function on_ta_focus(ta,focused){
		var $ta = $(ta);
		if(focused){
			$ta.next().hide();			
		} else {
			($ta.val() == "") && $ta.next().show();
		}
	}
	
	function ta_want_return_handler(){
		if( wait_reply == 1 ){ return; }
		wait_reply = 1;
		var 
		$ta=$(this),
		reply=$ta.val();
		if( reply == "" ){
			wait_reply = 0;
			return;
		} 
		var data=$.parseJSON($ta.attr("data-request-map"));
		data["text"]=reply;
		$.ajax({
			url: "/reply/post",
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
				wait_reply = 0;
				reply_ta_clear($ta);
				if(data.result != "success"){
					$.error("error:ta_want_return_handler:$.ajax-" + data.message);
					return;
				}
				var $li;
				($li = $( "<li class='ui-react-row'>" )).
				insertBefore($ta.parents(".ui-react-row")).
				html(_ejs_story_stream_reply_template.render(data.data));
				refresh_timesince.call($li.find(".livetimestamp"));
			},
			error: function( jqXHR, textStatus, errorThrown ){
				$.error("add replys:"+errorThrown);
			}		
		});
	}
	
	function ajax_like_toggle(data){
		if(data.result != "success"){
			$.error("ajax_like_toggle:$.ajax-" + data.message);
			return;
		}
		
		var 
		$trigger=$(this),
		checked=$trigger.attr("aria-checked") == "true";
	
		$("."+$trigger.attr("data-control-class")).each(function(){
			var 
			$parent=$(this),
			$anchor=$parent.find(".ui-reply-like-icon");
			$score=$parent.find(".ui-reply-feel-score"),
			score=parseInt($score.attr("data-feel-score"),10);
			
			if(checked){
				$parent.removeClass("mt-1-like");
				$parent.addClass("mt-0-like");
				$anchor.attr("title","좋아요");
				$anchor.attr("aria-checked","false");
				$score.attr("data-feel-score", --score);
			}else{
				$parent.removeClass("mt-0-like");
				$parent.addClass("mt-1-like");
				$anchor.attr("title","좋아요 취소");
				$anchor.attr("aria-checked","true");
				$score.attr("data-feel-score", ++score);
			}
			$score.text(score + "명");
		});
	}
	
	function ajax_hide_reply(data){
		if(data.result != "success") {
			$.error("error:ajax_hide_reply:$.ajax-" + data.message);
			return;
		}
		$(this).parents(".ui-react-row").remove();
	}
	
	function before_ajax_more_reply(){
		var $anchor = $(this);
		$anchor.find(".fa-comment").hide();
		$anchor.find(".fa-spin").show();
		$anchor.find("span").text("가져오는 중...");
	}
	
	function ajax_more_reply(data){
		if(data.result != "success"){
			$.error("error:ajax_more_reply:$.ajax-" + data.message);
			return;
		}
		//$.log( data );
		var 
		$anchor=$(this),
		$parent=$anchor.parents(".cmnt-row-wrap"),
		count=data.data.length,
		remains=parseInt($anchor.attr("data-reply-count"),10),
		requestCount=parseInt($anchor.attr("data-reply-count"),10),
		reuestData=$.parseJSON($anchor.attr("data-request-map"));
	
		for(var i=0;i<count;i++){
			var $li;
			($li = $("<li class='ui-react-row'>")).
			html(_ejs_story_stream_reply_template.render(data.data[i]));
			$parent.next().prepend($li);
			refresh_timesince.call($li.find(".livetimestamp"));
		}
		if(count>0){
			reuestData["replyId"]=data.data[count-1].id;
			$anchor.attr("data-request-map", JSON.stringify(reuestData));
		}
		remains-=count;
		if(remains<0){ remains=0; }
		$anchor.attr("data-reply-count",remains);
	
		$anchor.find(".fa-spin").hide();
		$anchor.find(".fa-comment").show();
		
		if(remains == 0){
			$parent.remove();
		} else if( remains < 10 ){
			$anchor.find("span").text( "댓글" + remains+"개 더 보기" );
		} else {
			$anchor.find("span").text( "지난 댓글 더보기" );
		} 
	}
	/* --  mod-reply-core -- */
	
	/* -- mod-reply-textarea-autosize -- */
	var _reply_ta_autoresize_adjust_active=false;
	var _reply_ta_autoresize_input_offset=0;
	var _$reply_ta_autoresize_mirror;
	
	function reply_ta_clear($ta){
		$ta.val("");
		setTimeout(function(){reply_ta_adjust.call($ta.get(0))},10);
	}
	
	function reply_ta_test_enter(){
		var	
		ta=this,
		$ta=$(ta),
		text=$ta.val(),
		mirror=_$reply_ta_autoresize_mirror.get(0),
		original = $ta.height(),
		height;
	
		_$reply_ta_autoresize_mirror.val(text+"\n");
		mirror.scrollTop = 0;
		mirror.scrollTop = 9e4;
		height = mirror.scrollTop + _reply_ta_autoresize_input_offset;
	
		(original !== height) && $ta.css("height",height);
		_$reply_ta_autoresize_mirror.val(text);
	}
	
	function reply_ta_adjust() {
		if(_reply_ta_autoresize_adjust_active) { return; }
		var	
		ta=this,
		$ta=$(ta), 
		mirror=_$reply_ta_autoresize_mirror.get(0),
		original = $ta.height(),
		height;
	
		_reply_ta_autoresize_adjust_active=true;
		_$reply_ta_autoresize_mirror.val($ta.val());
			
		mirror.scrollTop = 0;
		mirror.scrollTop = 9e4;
		height = mirror.scrollTop + _reply_ta_autoresize_input_offset;
	
		(original !== height) && $ta.css("height",height);
		setTimeout(function (){ _reply_ta_autoresize_adjust_active = false; }, 1);
	}
	
	function reply_ta_keydown(event){
		var ta=this, $ta=$(ta);
		if(event.keyCode != 10 && event.keyCode != 13) {
			if(event.keyCode == 27){$ta.blur();}			
			return;
		}
		
		if(event.shiftKey === false) {
			event.preventDefault();		
			var handlerName = $ta.attr("data-want-return");
			setTimeout(function(){window[handlerName].call($ta);},10);		
			return;								
		}
		
		reply_ta_test_enter.call(ta);
	}
	
	function reply_ta_autoresize() {
		$(".data-textarea-autogrow-set").
		each(function(){
			var 
			ta=this, 
			$ta=$(this);
	
			_$reply_ta_autoresize_mirror.css("width",$ta.width()-10);
			if($ta.css('box-sizing')==="border-box"||$ta.css('-moz-box-sizing')==="border-box"||$ta.css('-webkit-box-sizing')==="border-box"){
				_reply_ta_autoresize_input_offset = $ta.outerHeight() - $ta.height();
			}
			
			$ta.keydown(reply_ta_keydown);
			
			if('onpropertychange' in ta) {
				if('oninput' in ta) {
			    	ta.oninput = reply_ta_adjust;
			    	$ta.keydown(function(){setTimeout(reply_ta_adjust.bind(ta),50);});
				} else {
					ta.onpropertychange=reply_ta_adjust;
			    }
			}else{
			    ta.oninput=reply_ta_adjust;
			}
			$ta.removeClass("data-textarea-autogrow-set");
		});
	}
	/* -- mod-reply-textarea-autosize -- */
	
	$(function(){
		//
		_ejs_story_stream_reply_template = new EJS({ 
			url: "/assets/sunny/2.0/js/template/story-stream-reply.ejs?rnd=" + Math.floor( Math.random() * 999999999 ) 
		});
	
		//	
		_$reply_ta_autoresize_mirror = $("#ta-cmmnt-mirroring");
		reply_ta_autoresize();
	});
	</script>
	
	<script>
	/* -- mod-timesince -- */
	var _interval_id_timesince,_timesince_refresh_millis=60000;
	function refresh_timesince(){
		var $el=$(this),
			t=$el.attr("data-utime");
		if(!t){
		    var s=function(a,b){ return(1e15+a+"").slice(-b); },
		    	d=new Date(parseInt($el.attr("data-timestamp"), 10));
		    t=d.getFullYear() + '-' +
			  s(d.getMonth()+1,2) + '-' +
		      s(d.getDate(),2) + ' ' +
		      s(d.getHours(),2) + ':' +
		      s(d.getMinutes(),2) + ':' +
		      s(d.getSeconds(),2);
		    $el.attr("data-utime", t);
		}
		var dt,secs,itv,h,m,s=$.trim(t);
	 	s=s.replace(/\.\d+/,"");
	    s=s.replace(/-/,"/").replace(/-/,"/");
	    s=s.replace(/T/," ").replace(/Z/," UTC");
	    s=s.replace(/([\+\-]\d\d)\:?(\d\d)/," $1$2");
	    dt=new Date(s);
	    secs=Math.floor((new Date()-dt)/1000);
	    if((itv=Math.floor(secs/31536000))>=1){
	    	$el.text(itv+"년 전");
	    }else if((itv=Math.floor(secs/2592000))>=1){
	    	$el.text(itv+"개월 전");
	    }else if((itv=Math.floor(secs/86400))>=1){
	    	$el.text(itv+"일 전");
	    }else if((itv=Math.floor(secs/3600))>=1){
	    	$el.text(itv+"시간 전");
	    }else if((itv=Math.floor(secs/60))>=1){
	    	$el.text(itv+"분 전");
	    }else{
	    	$el.text("방금 전");
	    }
	    $el.attr("title", dt.getFullYear()+"년 "+(dt.getMonth()+1)+"월 "+dt.getDate()+"일 "+((h=dt.getHours())>12?("오후 "+(h-12)):("오전 "+h))+':'+((m=dt.getMinutes())<10?"0":"")+m);
	}
	function timesince(){
		_interval_id_timesince && clearInterval(_interval_id_timesince);
		$(".livetimestamp").each(refresh_timesince);
		_interval_id_timesince = setInterval(function(){$(".livetimestamp").each(refresh_timesince);},_timesince_refresh_millis);
	}
	/* -- mod-timesince -- */
	
	/* -- mod-story     -- */
	function ajax_feel_toggle(data) {
		if(data.result != "success"){
			$.error("ajax_feel_toggle:$.ajax-"+data.message);
			return;
		}
		
		var 
		$anchor=$(this),
		$span=$anchor.prev(),
		checked=$anchor.attr("aria-checked")=="true",
		feeled=false;
		
		$(".react-icons-"+$anchor.attr("data-ft")).each(function(){
			$(this).find(".story-react").each(function(){
				var 
				$r=$(this),
				$span=$r.find("span"),
				$anchor=$r.find("a");
				
				if($span.hasClass("reacted")){ feeled=true; }
				$span.removeClass("reacted");
				$anchor.attr("aria-checked","false");
			});	
		});
	
		if(!checked){
			//$span.addClass("reacted");
			//$anchor.attr("aria-checked","true");
	
			$( "." + $anchor.attr("data-ft-class") ).each( function(){
				$(this).prev().addClass("reacted");
				$(this).attr("aria-checked","true");
			});
			
			
			if(!feeled){
				var 
				$counter=$("."+$anchor.attr("aria-controls")),
				count=parseInt($counter.attr("data-count"),10);			
				$counter.attr("data-count",++count);
				$counter.find(".count-text").text(count+"명");
				(count==1) && $counter.parents(".cmnt-row-wrap").show();
			}
		}else{
			var 
			$counter=$("."+$anchor.attr("aria-controls")),
			count=parseInt($counter.attr("data-count"),10);
			$counter.attr("data-count",--count);
			$counter.find(".count-text").text(count+"명");
			(count==0) && $counter.parents(".cmnt-row-wrap").hide();
		}
	}
	
	function ajax_delete_story(data){
		if(data.result != "success"){
			$.error("error:ajax_delete_story:$.ajax-" + data.message);
			return;
		}
		var $si=$(this).parents(".ui-stream-story");
		if($si.prev().length == 0){
			var dataFWMap=$.parseJSON(_$sl.attr("data-fw-map")),
				$ni=$si.next();	
			if($ni.length>0){
				var dataMap=$.parseJSON($ni.attr("data-map"));
				dataFWMap["contentId"]=dataMap["id"];
			}else{
				dataFWMap["contentId"]="";
			}
			_$sl.attr("data-fw-map", JSON.stringify(dataFWMap));
		}
		if($si.next().length == 0){
			var dataBWMap=$.parseJSON(_$sl.attr("data-bw-map")),
				$pi=$si.prev();	
			if($pi.length>0){
				var dataMap=$.parseJSON($pi.attr("data-map"));
				dataBWMap["contentId"]=dataMap["id"];
			}else{
				dataBWMap["contentId"]="";
			} 
			_$sl.attr("data-bw-map", JSON.stringify(dataBWMap));
		}
		$si.fadeOut(600, "linear", function(){$(this).remove();});
	}
	
	$(function() {
		//
		$(".generated-mention").onHMOClick(null, function(e){
			e.preventDefault();
			location.href="/user/" + $(this).data("uid");
		});
		$(".generated-hash").onHMOClick(null, function(e){
			e.preventDefault();
			location.href="${param.currentUrl}?q%5B%5D=" + "%23" + $(this).text().substring(1);
		});
		//
		timesince();
	});
	/* -- mod-story    -- */
	</script>
	
	
	<script>
	/* -- mod-story-dialogs    -- */
	function on_feel_users_scroll(e, pos) {
		if (pos == "bottom" /* && end_of_chat_user_stream == false */) {
			//invite_friend_streaming(false);
		}
	}
	
	var _ejs_feel_users = null;
	$(function(){
		$(document.body).onHMOClick(".pop-feeled-users", function(e){
			
			e.preventDefault();
			
			$this = $(this);
			map = $this.data("request-map");
			
			var channelId = $(this).attr("data-id");
			if (_ejs_feel_users == null) {
				_ejs_feel_users  = new EJS(
						{
							url : "/assets/sunny/2.0/js/template/feel-users.ejs?rnd=" + Math.floor(Math.random() * 999999999)
						});
			}
			
			var isFavorited = $.ajax({
				url: "/feel/users",
				type: "GET",
				dataType: "json",
				data:map
			});
			
			$bootboxContent = $("#res-dialog-pop-feeled-users-content");
			bootbox.dialog("res-dialog-pop-feeled-users", [{
				"label" : "취소",
				"class" : "hmo-button hmo-button-white hmo-button-small-10"
			}],{
				"header" : "평가한 사용자들",
				"embed" : true,
				"animate" : false,
				"onInit" : function() {
					//$("#res-dialog-show-profile-content").html( _$ejs_show_profile.render(map) );
					
					$.when(isFavorited)
					.done(function(data){
						if(data.result == "fail"){
							alert( data.message );
						}
						var html = "";
						$.each( data.data, function( idx, d ){
							html += _ejs_feel_users.render(d);
		
						});
						$bootboxContent.html( html  );
					})
					.fail(function(data){
						alert("오류발생");
					});
					
					$('.pop-feeled-users-scroll-wrap').slimscroll({
						height : "350px",
						railVisible : false,
						alwaysVisible : false,
						touchScrollStep : 70
					}).bind('slimscroll', on_feel_users_scroll);
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
	});
	/* -- mod-story-dialogs    -- */
	</script>
</c:if>


<c:if test="${current == 'list' }">
	<script type="text/javascript">
	
	/*
	 * mod_
	 */
	
		var paramMap = new Array();
	
		
		function pdsListReplaceList(parsedLink){
			
			$('.message-container').append('<div class="message-loading"><i class="fa fa-spin fa-spinner orange2 bigger-160"></i></div>');
			$.get("?" + parsedLink, function(data, status){
				$('.message-container').find('.message-loading').remove();
				$("#message-list-changeable").html(data);
			});
			
		}
		
		
		<c:if test="${not empty tab}">
		paramMap["tab"] = "${tab}";
		</c:if>
	
		<c:if test="${not empty param.ordering}">
		paramMap["ordering"] = "${param.ordering}";
		</c:if>
	
		<c:if test="${not empty param.status}">
		paramMap["status"] = "${param.status}";
		</c:if>
	
		<c:if test="${not empty param.q}">
		paramMap["q"] = "${param.q}";
		</c:if>
	
		<c:if test="${not empty param.qt}">
		paramMap["qt"] = "${param.qt}";
		</c:if>
	
		<c:if test="${not empty param.page}">
		paramMap["page"] = "${param.page}";
		</c:if>
	
		paramMap["pl"] = true;
		
		function setProfileFavorite( uid, favorited ){
			
			var $favorites = $(".favorite-" + uid);
			
			if( favorited == false){
				$favorites.html("<i class='icon-star-empty' />");
				$favorites.data("already-favorited", false);
			}else{
				$favorites.html("<i class='icon-star' />");
				$favorites.data("already-favorited", true);
			}
		}
		
		$(function() {
			
			$(".main-search-form").submit(function(e){
				e.preventDefault();
				var query = $(this).find("input[type=text]").val();
				
				paramMap["q"] = query;
				paramMap["page"] = 1;
				
				pdsListReplaceList(getParsedLink(paramMap));
			});
			
			$(document.body).on("submit", "#pds-page-form", function(e){
				e.preventDefault();
				var page = $(this).find("input[type=text]").val();
				
				paramMap["page"] = page;
				
				pdsListReplaceList(getParsedLink(paramMap));
			});
			$(document.body).on("click", ".more-detail", function(e){
					
					var elemType = $(this).data("type");
		
					var name = $(this).data("name"); 
					var value =  $(this).data("value");
					
					
					
					if(  name == "qt"){
						if( $(".main-search-form input[type=text]").val() == "" ){
							paramMap[name] = value;
							return;
						}
					}
					
					if (elemType == "multiple") {
						var arr = $.map($($(this).data("selector")), function(
								elem, index) {
							return +$(elem).data("value");
						});
						paramMap[name] = arr;
					} else {
						e.preventDefault();
						paramMap[name] = value;
					}
					
					
					if( name != "page" ){
						paramMap["page"] = 1;
					}
					pdsListReplaceList( getParsedLink(paramMap) );
					
			});
		});
	</script>

</c:if>

<c:if test="${current == 'list' }">
	<script type="text/javascript">
	
	$(function(){
		$.pathchange.init({
			selector : ".pathchange"	
		});
		changeFlag = 0;
		$(window).bind("pathchange", function(e, _this) {
			if( $(_this).data("view") == null || typeof($(_this).data("view")) == "undefined" ){
				Pds.show_list();
			}else{
				if( $(_this).data("view") == "write" ){
					Pds.show_form();
				}else if( $(_this).data("view") == "view" ){
					// 뷰 나와라
					Pds.show_view(_this);
				} 
			}
			
		});
		
		$('.btn-back-message-list').onHMOClick(null, function(e) {
			e.preventDefault();
			Pds.show_list();
		});
		
		
		
		
		var Pds = {};
		
			//show message list (back from writing mail or reading a message)
		Pds.show_list = function() {
				$('.message-navbar').hide();
				$('#id-message-list-navbar').show();
		
				$('.message-footer').hide();
				$('.message-footer:not(.message-footer-style2)').show();
				$('#message-list').show(); //.next().hide();
				$("#id-message-form").hide();
				$("#id-message-content").hide();
				//hide the message item / new message window and go back to list
				
	
				$("#id-message-list-button").show();
				$("#id-message-form-button").hide();
				
			}
		
		//show write mail form
		Pds.show_form = function() {
			if($('.message-form').is(':visible')) return;
			
			var message = $('#message-list');
			$('.message-container').append('<div class="message-loading"><i class="fa fa-spin fa-spinner orange2 bigger-160"></i></div>');
			
	//			setTimeout(function() {
				message.next().hide();
				
				$('.message-container').find('.message-loading').remove();
				
				$('#message-list').hide();
				$('.message-footer').hide();
				$('.message-form').show().insertAfter('#message-list');
				
				$('.message-navbar').hide();
				$('#id-message-new-navbar').show();
				
				$("#id-message-list-button").hide();
				$("#id-message-form-button").show();
				
				//reset form??
				
	//				$('.message-form').get(0).reset();
	//			}, 300 + parseInt(Math.random() * 300));
		}
		
		Pds.show_view = function(elem){
			$('.message-container').append('<div class="message-loading"><i class="fa fa-spin fa-spinner orange2 bigger-160"></i></div>');
			
			//$('#id-message-content').remove();
			$("#id-message-list-button").hide();
	
			// id 로 해도 될듯
			var message_list = $('#message-list');
			
			
			$.get("${groupPath}/pds/" +  $(elem).data("id") + "?pl=true", function(data, status){
				// settimeout
				message_list.next().hide();
				$('.message-container').find('.message-loading').remove();
				
				//close and remove the inline opened message if any!
				
				//hide all navbars
				$('.message-navbar').hide();
				//now show the navbar for single message item
				
				if( __a__["id"] == $(elem).data("uid") ){
					var $removeTrigger = $("#id-message-item-navbar").find(".remove-trigger");
					$removeTrigger.closest("li").removeClass("hidden-elem");
					$removeTrigger.attr("data-request-map",'{"id" : "' + $(elem).data("id") + '"}');
				}else{
					var $removeTrigger = $("#id-message-item-navbar").find(".remove-trigger");
					$removeTrigger.closest("li").addClass("hidden-elem");
				}
				
				$("#id-message-item-navbar").show();
				//hide all footers
				$('.message-footer').hide();
				
				message_list.hide();
				
				$("#id-message-content").html(data);
				
				reply_ta_autoresize();
				timesince();
				
				$("#id-message-content").show();
				//move .message-content next to .message-list and hide .message-list
				//message_list.hide().after($('#id-message-content')).next().show();
	
				//add scrollbars to .message-body
	//				$('#id-message-content .message-body').slimScroll({
	//					height: 300,
	//					railVisible:true
	//				});
	
			// timeout end
			});
		}
		
	});
	
	</script>
</c:if>

<form>
	<textarea tabindex="-1" id="ta-cmmnt-mirroring" class="textarea-mirroring cmmnt-mirroring"></textarea>
</form>
	
<div id="res-dialog-pop-feeled-users" style="display: none">
	<div class="rw-dialog-wrap pop-feeled-users-scroll-wrap" id="res-dialog-pop-feeled-users-content"></div>
</div>

