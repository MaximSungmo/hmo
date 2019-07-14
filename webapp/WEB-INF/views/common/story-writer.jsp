<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/bootstrap-tagsinput.css">
<script src="/assets/sunny/2.0/js/composer/story-writer/core.js"></script>
<!-- <script src="/assets/bootstrap/v2.3.2/js/bootstrap-tagsinput.js"></script> -->
<script src="/assets/bootstrap/v3.0.2/bootstrap-tagsinput/bootstrap-tagsinput.js"></script>
<script src="/assets/sunny/2.0/js/uncompressed/jquery.tokeninput.js"></script>
<!-- <link href="/assets/sunny/2.0/css/uncompressed/jquery.tokeninput.css" rel="stylesheet"  /> -->

<script>
document.domain=window.__g__.documentDomain;
var StoryComposer =	{
	waitStoryPost: 0,
	__hide_tags: false,
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
		data["permissionType"] = $("#permission-list-ul li.active > .default-candi-row").data("type");
		data["notice"] = $("#composer-button-notice").data("checked");

		
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
			if( StoryComposer.__hide_tags == false ){
				$("#composer-tags").show();
			}
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


<script>
<c:if test="${authUserIsAdmin || isSmallGroupAdmin }">
var DialogComposerTag = {
	onAjaxInit: function( data ){
		$.log( data );
		if(data.result == "fail"){
			MessageBox( "태그 가져오기", "태그를 가져오는 중 다음과 같은 오류가 발생했습니다.<br>" + data.message, MB_ERROR );
			return;
		}
		
		if( $("#dialog-setting-composer-tags .token-input-list-hmo").length == 0 ){
			$("#composer-tagsinput").tokenInput(data.data, {
				<c:if test="${not empty composerTags}">
				prePopulate:[<c:forEach items="${composerTags }" var="tag" varStatus="status">{id:${tag.id }, title:"${tag.title}"}${not status.last ? "," : ""}</c:forEach>],</c:if>
				hintText: "원하는 태그를 입력해주세요",
				propertyToSearch: "title",
				noResultsText: "태그가 없습니다. 생성하시겠습니까?",
				searchingText: "검색중",
				tokenLimit: "5",
				theme: "hmo",
				preventDuplicates:true,
				onResult: function(result){
					var newData = new Array();
					var totalCount = 0;
					var existInList = false;
					var orgText = $("#token-input-composer-tagsinput").val().trim();
					if( result == null ){
						newData[0] = { id:-1, title:orgText, referenceCount:0};
					}else{
						$.each(result, function( index,value ) {
							newData[index] = {
									id:value.id,
									title:value.title,
									referenceCount:value.referenceCount
							}
							totalCount++;
							if( value.title == orgText ){
								existInList = true;
							}
						});
						if( existInList == false ){
							newData[totalCount] = {
								title:orgText,
								referenceCount:-1
							}
						}
					}
					return newData;
				},
				onReady: function(e){
				},
				onAdd: function( data ){
				},
				resultsFormatter: function(result){
					if( result.referenceCount == -1 ){
						return "<li>" + result.title + " 생성하기</li>";
					}
					return "<li>" + result.title  + "(" + result.referenceCount + "번 사용)"+ "</li>"
				},
		        tokenFormatter: function(result) { 
		        	return "<li class='post-tags'>" 
		        	+ result.title 
		        	+ "<input type='hidden' name='' value='" + result.id + "'></li>" 
		        }
			});
		}
		
	},
	onApplyClicked: function(){
		var data = $("#composer-tagsinput").tokenInput("get");
		$.ajax({
			url:"/group/${empty smallGroup ? sunny.site.lobbySmallGroup.id : smallGroup.id }/composer_tags",
			type:"POST",
			async: true,
			dataType:"json",
			contentType: 'application/json',
			headers: {
				"Accept": "application/json",
				"Content-Type": "application/json"
			},
		    data: JSON.stringify(data),
		    success: function(resultData){
		    	if( resultData.result != "success" ){
		    		MessageBox("태그 변경 실패", resultData.message, MB_ERROR);
		    		return false;
		    	}
		    	MessageBox("변경됨", "변경되었습니다", MB_INFORMATION);
		    	$("#composer-tag-changeable").html("");
		    	if( data == null || Object.keys(data).length == 0 ){
		    		$("#composer-tag-changeable").html("&nbsp;설정된 태그가 없습니다. 관리자만 자주 쓰이는 태그를 설정할 수 있습니다.");
		    	}else{
		    		$("#composer-tag-changeable").append("&nbsp;원하는 태그를 선택해주세요");
		    		$.each(data, function( index, value ){
		    			$("#composer-tag-changeable").append('<a href="#" class="composer-tag" onclick="return putTagToTextarea(this);">' + value.title + '</a>');
		    		});
		    	}
		    	
		    },
			error:function(jqXHR,textStatus,errorThrown){
				$.log("error:Event.__inlineSubmit:"+errorThrown);
			}
		});
	},
	onClose: function() {
		$("#composer-tagsinput").tokenInput("clear");
	}	
}
</c:if>

function insertAtCaret(areaId,text) {
    var txtarea = document.getElementById(areaId);
    var scrollPos = txtarea.scrollTop;
    var strPos = 0;
    var br = ((txtarea.selectionStart || txtarea.selectionStart == '0') ? 
        "ff" : (document.selection ? "ie" : false ) );
    if (br == "ie") { 
        txtarea.focus();
        var range = document.selection.createRange();
        range.moveStart ('character', -txtarea.value.length);
        strPos = range.text.length;
    }
    else if (br == "ff") strPos = txtarea.selectionStart;

    var front = (txtarea.value).substring(0,strPos);  
    var back = (txtarea.value).substring(strPos,txtarea.value.length); 
    txtarea.value=front+text+back;
    strPos = strPos + text.length;
    if (br == "ie") { 
        txtarea.focus();
        var range = document.selection.createRange();
        range.moveStart ('character', -txtarea.value.length);
        range.moveStart ('character', strPos);
        range.moveEnd ('character', 0);
        range.select();
    }
    else if (br == "ff") {
        txtarea.selectionStart = strPos;
        txtarea.selectionEnd = strPos;
        txtarea.focus();
    }
    txtarea.scrollTop = scrollPos;
}
function putTagToTextarea(dom){
	$this = $(dom);
	$("#ta-message-text").next().hide();
	
	insertAtCaret("ta-message-text", "#" + $this.text() + " ");
	
	return false; 
}


function hide_composer_tag(){
	$("#composer-tags").hide();
	StoryComposer.__hide_tags = true;
	return false; 
}
</script>
<div class="ui-composer-out">
	<form
		rel="sync"
		name="story-w-form"
		method="post"
		action="/story/post"
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
	
	
	<c:if test="${empty basecampUser}">
	<div id="composer-tags">
		<div id="composer-tag-titles">
			<div id="composer-tag-changeable" style="float:left; line-height:220%;">
				<c:choose>
					<c:when test="${not empty composerTags }">
						 &nbsp;원하는 태그를 선택해주세요 
						<c:forEach items="${composerTags }" var="tag">
							<a href="#" class="composer-tag" onclick="return putTagToTextarea(this);">${tag.title }</a>		
						</c:forEach>
					</c:when>
					<c:otherwise>
						&nbsp;설정된 태그가 없습니다. 관리자만 자주 쓰이는 태그를 설정할 수 있습니다. 
					</c:otherwise>
				</c:choose>
				
			</div>
			<div class="composer-tag-tools pull-right">
					<a class="composer-tag-hide"
						href="#"
						onclick="return hide_composer_tag();"
					>숨기기
					</a>
					<c:if test="${authUserIsAdmin || isSmallGroupAdmin }">
					 | 
						<a class="composer-tag-setting"
						href="/group/${empty smallGroup ? sunny.site.lobbySmallGroup.id : smallGroup.id }/tags" 
						role="dialog"
						aria-control="dialog-setting-composer-tags"
						ajaxify-dialog-init="DialogComposerTag.onAjaxInit"
						rel="async-get"
						data-request-map="{}"
						data-title="태그 설정"
						data-custom-nm="적용하기"
						data-custom-fn="DialogComposerTag.onApplyClicked"
						data-close-fn="DialogComposerTag.onClose">설정</a>
					</c:if>
			</div>
		</div>
	</div>
	</c:if>
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
		<c:if test="${authUserIsAdmin }">
				<div class="composer-tool-button ico-btn composer-button-notice-ss">
					<a rel="" role="button" id="composer-button-notice" title="공지사항을 올립니다." data-checked="false" class="composer-tool-button-notice">
						<i class="composer-tool-button-notice"></i> 
					</a>
				</div>
		</c:if>		
		<ul class="ui-list">
			<li>
				<c:choose>
					<c:when test="${not empty basecampUser && basecampUser.id == authUserId }">
						<div id="permission-toggle-wrap" class="controls">
							<a	class="btn-message hmo-button btn-small dropdown-toggle"
								id="toggle-permission-detail"
								aria-owns="permission-list-ul"
								aria-haspopup="true"
								rel="toggle"> 
								<span class="text">나만 보기</span>
								<i class="fa fa-caret-down fa-on-right"></i>
							</a>
							<c:import url="/WEB-INF/views/pagelet/permission/dropdown.jsp">
								<c:param name="dropdownDirection" value="right" />
							</c:import>
						</div>
					</c:when>
					<c:when test="${not empty basecampUser && basecampUser.id != authUserId }">
						<div id="permission-toggle-wrap" class="controls">
							<a	class="btn-message hmo-button btn-small dropdown-toggle"
								id="toggle-permission-detail"
								aria-owns="permission-list-ul"
								aria-haspopup="true"
								rel="toggle"> 
								<span class="text">${basecampUser.name }과 나만 보기</span>
								<i class="fa fa-caret-down fa-on-right"></i>
							</a>
							<c:import url="/WEB-INF/views/pagelet/permission/dropdown.jsp">
								<c:param name="dropdownDirection" value="right" />
							</c:import>
						</div>	
					</c:when>
					<c:otherwise>
						<div id="permission-toggle-wrap" class="controls">
							<a	class="btn-message hmo-button btn-small dropdown-toggle"
								id="toggle-permission-detail"
								aria-owns="permission-list-ul"
								aria-haspopup="true"
								rel="toggle"> 
								<span class="text">${empty smallGroup ? '<i class="fa fa-globe"></i>&nbsp;&nbsp;&nbsp;전체공개' : smallGroup.name }</span>
								<i class="fa fa-caret-down fa-on-right"></i>
							</a>
							<c:import url="/WEB-INF/views/pagelet/permission/dropdown.jsp">
								<c:param name="dropdownDirection" value="right" />
							</c:import>
						</div>	
					</c:otherwise>
				
				</c:choose>
				
			</li>		
			<li>
				<div class="ui-input-submit">
					<input type="submit" class="hmo-button hmo-button-blue" value="등록" onclick="$(window[&quot;story-w-form&quot;]).trigger(&quot;submit&quot;);">
				</div>
			</li>			
		</ul>
	</div>
</div>


<!-- BEGIN:resource of dialog for tag Settings -->
<c:if test="${authUserIsAdmin || isSmallGroupAdmin }">
	<div id="dialog-setting-composer-tags" class="hmo-dialog-res">
		<div class="hmo-dialog-body-inner" >
			<input type="text" id="composer-tagsinput" 
			/>
		</div>
	</div>
</c:if>
<!-- END:resource of dialog for tag Settings -->	

<!-- BEGIN:resource of dialog for showing profile -->

<div id="res-dialog-add-permission" style="display:none">
	<div class="rw-dialog-wrap " id="res-dialog-add-permission-content ">
	
<!-- 		<ul id="permission-candi-list" class="unstyled" style="max-height: 100px; overflow-y:auto;"> -->
<!-- 		</ul> -->
		<input type="text" id="permission-tagsinput" />

		<form id="permission-search-form" action="/pagelet/permission" method="GET" class="form-search clearfix" style="margin-bottom: 5px; clear:both;">
			<div>
				<input type="text" class="search-query" style="width: 40%;">
				<button class="hmo-button-search hmo-button hmo-button-blue">
					<span>검색</span>
				</button>
			</div>
		</form>
		<div class=" permission-wrap" >			
			<div class="permission-tabs z1">
				<a data-name="type" data-value="3" class=" permission-more-detail active" >부서</a>
				<a data-name="type" data-value="4" class=" permission-more-detail " >프로젝트</a>
				<a data-name="type" data-value="5" class=" permission-more-detail " >그룹</a>
				<a data-name="type" data-value="" class=" permission-more-detail " >사람</a>
			</div>
			<div class="permission-scroll-wrap scrollable">
				<div id="permission-search-list">
				</div>
			</div>
			
		</div>
	</div>
</div>
<!-- END:resource of dialog for showing profile -->	