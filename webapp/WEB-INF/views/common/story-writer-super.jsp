<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/bootstrap-tagsinput.css">
<script src="/assets/sunny/2.0/js/composer/story-writer/core.js"></script>
<script src="/assets/bootstrap/v2.3.2/js/bootstrap-tagsinput.js"></script>
<script>
document.domain=window.__g__.documentDomain;
var StoryComposer =	{
	waitStoryPost: 0,
	init: function() {
		
		StoryWriter.attachInputListener( MentionInputListener );
		FileUploader.init();
		
		// buttons event-mappings 
		$( document.body ).onHMOClick( ".composer-button-text-bold", this.onButtonTextBoldClicked );
		$( "#composer-button-notice" ).onHMOClick( null, this.onButtonNoticeClicked );
		
		//
		$( "#ta-message-text" ).storywriter();
		
		//
		$("#permission-tagsinput").tagsinput( {
			confirmKeys:[],
			itemValue: function( item ) {
				return item.id;
			},
			itemText: function( item ) {
				return item.name;
			},
		    freeInput: false
		} );
	},
	inlinePostSubmit: function( form, event ) {
		$.Event( event ).preventDefault();
		
		if( this.waitStoryPost == 1 ){
			return;
		}
		
		this.waitStoryPost = 1;
		
		var 
		$form = $(form),
		$textarea = $form.find( "textarea" );
		$uploadedFiles = $form.parent().find(".upload-image-item .uploaded-files"),
		rel = $form.attr("rel"),
		url = $form.attr("action"),
		type = $form.attr("method"),
		data = $form.serializeObject(),
		fileAttached = false;
		
		if( $uploadedFiles.length > 0 ){
			data["mediaIds"] = [];
			$uploadedFiles.each( function() {
				data["mediaIds"].push( $( this ).data( "up-file" ) );
			});
			fileAttached = true;
		}
		
		if( fileAttached == false && $.trim(data["text"]) == "" ) {
			$textarea.focus();
			this.waitStoryPost = 0;
			return false;
		}
		
		var permissions = Permission.getArrayData();
		if( typeof(permissions) != "undefined" && permissions != null && permissions.length > 0 ){
			data["permissions"] = permissions;
		}

		
		$.ajax({
			url:url,
			type:type,
			async: rel == "async",
			dataType:"json",
			contentType: 'application/json',
			headers: {
				"Accept": "application/json",
				"Content-Type": "application/json"
			},
		    data: JSON.stringify(data),
		    success:this.onAjaxStoryPost,
			error:function(jqXHR,textStatus,errorThrown){
				$.log("error:Event.__inlineSubmit:"+errorThrown);
			}
		});

		return false;
	},
	onAjaxStoryPost: function( data ) {
		
		StoryComposer.waitStoryPost = 0;
		
		if( data.result != "success" ) {
			MessageBox( "스토리", "스토리 작성중 다음과 같은 오류가 발생 했습니다.<br>" + data.message, MB_ERROR );
			return;
		}
		
		if( $("#composer-button-notice").data("checked") ){
			//refresh_right_notice();
		}
		
		$("#ta-message-text").val("").focus();
		$("#story-permission-list").empty();
		$("#composer-button-notice").data("checked", false);
		$("#composer-button-notice i").removeClass("blue");
		$("#composer-permission").addClass("hidden-elem");
		
		FileUploader && FileUploader.uploadIdCounter > 0 && FileUploader.clear();
		
		Stream && ( Stream.streaming || fn_empty ).call( Stream, true );
	},
	onTextareaFocus: function( ta, focused ){
		var $ta = $( ta );
		if( focused ){
			$ta.next().hide();			
		} else {
			( $ta.val() == "" ) && $ta.next().show();
		}
	},
	onButtonNoticeClicked: function( $event ) {
		var
		$button = $( this ),
		checked = $button.data( "checked" );
		
		if( checked == true ) {
			$button.data( "checked", false );
			$button.find( "i" ).removeClass( "blue" );
		} else {
			$button.data( "checked", true );
			$button.find( "i" ).addClass( "blue" );
		}		
	},
	onButtonTextBoldClicked: function( $event ) {
		MentionInputListener.bold();
	}
};

$(function(){
	StoryComposer.init();	
});
</script>

<div class="ui-composer-out">
	<form
		rel="sync"
		name="story-w-form"
		method="post"
		action="/super/post_story"
		onsubmit="return StoryComposer.inlinePostSubmit( this, event );">
		<div class="ui-mentions-input">
			<div class="highlighter">
				<div><span class="highlighter-content hidden-elem mentions-highlighter"></span></div>
			</div>
			<div style="height:auto;" class="composer-type-ahead">
				<div class="wrap">
					<div class="inner-wrap">
						<textarea  
							role="textbox"
							class="input mentions-textarea"
							title="${param.placehoderMessage }"
							placeholder="${param.placehoderMessage }"
							autocomplete="off"
							spellcheck="false"
							onfocus="StoryComposer.onTextareaFocus(this,true);"
							onblur="StoryComposer.onTextareaFocus(this);"
							id="ta-message-text"></textarea>
						<span class="placeholder" onclick="document.getElementById(&quot;ta-message-text&quot;).focus();">${param.placehoderMessage }</span>
					</div>
				</div>
			</div>
			<c:if test="${not empty smallGroup }">
				<input name="smallGroupId" autocomplete="off" class="hidden-input" type="hidden" value="${smallGroup.id }">
			</c:if>
			<input name="text" autocomplete="off" class="mentions-hidden mentions-text" type="hidden" value="">
			<input name="receiverId" autocomplete="off" class="hidden-input" type="hidden" value="${param.receiverId }">
		</div>
	</form>
	<div style="position:relative; height:0">
		<div class="mention-select-box" style="display:none">
			<ul></ul>	
		</div>
	</div>
	<div class="composer-files">
		<div class="composer-file-tiles">
			<div class="file-grid-item next-set-item">
				<div class="ui-scaled-image-container">
					<a rel="ignore" role="button">
						<div class="next-item-inner">
							<form enctype="multipart/form-data"
								  action="/upload"
								  method="post">
								<input type="file" name="file" onchange="FileUploader &amp;&amp; FileUploader.onFileSelected(this);" multiple="multiple" accept="*/*">
								<input type="hidden" name="upid" value="">
							</form>
						</div>
					</a>
				</div>
			</div>
		</div>
	</div>
	<div class="ui-composer-bottom">
		<div class="composer-tool-button ico-btn composer-button-text-bold">
			<a rel="ignore" role="button">
				<i class="fa fa-bold fa-1g"></i>
			</a>
		</div>
		<div class="composer-tool-button">
			<div class="composer-tool-button">
				<a rel="ignore" role="button" class="composer-tool-button-file">
					<div class="composer-tool-button-inner">
						<form enctype="multipart/form-data"
							  action="/upload"
							  method="post">
							<input type="file" name="file" onchange="FileUploader &amp;&amp; FileUploader.onFileSelected(this);" multiple="multiple"  accept="*/*">
							<input type="hidden" name="upid" value="">
						</form>
					</div>
				</a>
			</div>
		</div>
		<ul class="ui-list">
			<li>
				<div id="permission-toggle-wrap" class="controls">
					<a	class="btn-message hmo-button btn-small dropdown-toggle"
						id="toggle-permission-detail"
						aria-owns="permission-list-ul"
						aria-haspopup="true"
						rel="toggle"> 
						<span class="text"><i class="fa fa-globe"></i>&nbsp;&nbsp;&nbsp;모든 사이트에 공개</span>
						<i class="fa fa-caret-down fa-on-right"></i>
					</a>
					<c:import url="/WEB-INF/views/super/permission_dropdown.jsp">
						<c:param name="dropdownDirection" value="right" />
					</c:import>
				</div>	
				
			</li>		
			<li>
				<div class="ui-input-submit">
					<input type="submit" class="hmo-button hmo-button-blue" value="등록" onclick="$(window[&quot;story-w-form&quot;]).trigger(&quot;submit&quot;);">
				</div>
			</li>			
		</ul>
	</div>
</div>
<!-- BEGIN:resource of dialog for showing profile -->

<div id="res-dialog-add-permission" style="display:none">
	<div class="rw-dialog-wrap " id="res-dialog-add-permission-content ">
	
<!-- 		<ul id="permission-candi-list" class="unstyled" style="max-height: 100px; overflow-y:auto;"> -->
<!-- 		</ul> -->
		<input type="text" id="permission-tagsinput" />

		<form id="permission-search-form" action="/pagelet/super/site_permission" method="GET" class="form-search clearfix" style="margin-bottom: 5px; clear:both;">
			<div>
				<input type="text" class="search-query" style="width: 40%;">
				<button class="hmo-button-search hmo-button hmo-button-blue">
					<span>검색</span>
				</button>
			</div>
		</form>
		<div class=" permission-wrap" >			
			<div class="permission-scroll-wrap scrollable">
				<div id="permission-search-list">
				</div>
			</div>
			
		</div>
	</div>
</div>
<!-- END:resource of dialog for showing profile -->	