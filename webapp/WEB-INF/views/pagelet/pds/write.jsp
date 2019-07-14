<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" href="/assets/sunny/2.0/css/uncompressed/story-writer.css">
	
<script src="/assets/sunny/2.0/js/composer/story-writer/core.js"></script>
<script src="/assets/sunny/2.0/js/composer/story-writer/plugins/mention-input-listener.js"></script>
<script src="/assets/sunny/2.0/js/composer/story-writer/plugins/url-input-listener.js"></script>
	
<script src="/assets/sunny/2.0/js/uncompressed/file-uploader.js"></script>

<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/bootstrap-tagsinput.css">
<script src="/assets/bootstrap/v2.3.2/js/bootstrap-tagsinput.js"></script>


<div id="id-message-form" class="form-horizontal message-form">

	<div class="control-group">
		<label class="control-label" for="form-field-recipient">권한설정:</label>
		<div id="permission-toggle-wrap" class="controls" style="position: relative;">
			<a href="#" id="toggle-permission-detail" class="btn-message btn btn-mini dropdown-toggle" data-toggle="dropdown"> 
				<span class="bigger-120 text">${empty smallGroup ? '전체공개' : smallGroup.name }
			</span> <i class="fa fa-caret-down fa-on-right"></i>
			</a>
			<c:import url="/WEB-INF/views/pagelet/permission/dropdown.jsp">
				<c:param name="dropdownDirection" value="left" />
			</c:import>
		</div>
	</div>

	<div class="hr hr-16 dotted"></div>

	<div class="control-group">
		<label class="control-label" for="form-field-subject">제목:</label>

		<div class="controls">
			<div class="row-fluid">
				<span class="span12"> <input maxlength="100"
					type="text" class="span12" name="subject" id="pds-post-title"
					placeholder="제목" /> 
				</span>
			</div>
		</div>
	</div>

	<div class="hr hr-16 dotted"></div>

	<div class="control-group">
		<label class="control-label"> <span
			class="inline space-16 hidden-480"></span> 내용:
		</label>

		<div class="controls">
<!-- 			<textarea id="pds-post-text"></textarea> -->
			<!-- 													<textarea class="input mentions-textarea" style="border-width: 0px; height: 74px;" title="지금,당신의 스토리를 들려주세요!" placeholder="지금,당신의 스토리를 들려주세요!" role="textbox" aria-autocomplete="list" autocomplete="off" spellcheck="false" aria-expanded="false" aria-label="" onfocus="on_ta_focus(this,true);" onblur="on_ta_focus(this);" id="ta-message-text"></textarea> -->
			
			<div class="ui-mentions-input">
				<div style="direction:ltr; text-align:left;" class="highlighter">
					<div><span class="highlighter-content hidden-elem mentions-highlighter"></span></div>
				</div>
				<div style="height:auto;" class="composer-type-ahead">
					<div class="wrap">
						<div class="inner-wrap">
							<textarea
								class="input mentions-textarea"
								style="border:1px solid #D5D5D5; min-height: 76px; "						
								title="${param.placehoderMessage }"
								placeholder="${param.placehoderMessage }"
								role="textbox"
								aria-autocomplete="list"
								autocomplete="off"
								spellcheck="false"
								aria-expanded="false"
								aria-label=""
								onfocus="on_ta_focus(this,true);"
								onblur="on_ta_focus(this);"
								id="ta-message-text"></textarea>
							<span class="placeholder" onclick="document.getElementById(&quot;ta-message-text&quot;).focus();">${param.placehoderMessage }</span>
						</div>
					</div>
				</div>
				<c:if test="${not empty smallGroup }">
					<input id="pds-smallGroupId" name="smallGroupId" autocomplete="off" class="hidden-input" type="hidden" value="${smallGroup.id }">
				</c:if>
				<input name="text" autocomplete="off" class="mentions-hidden mentions-text" type="hidden" value="">
				<input name="receiverId" autocomplete="off" class="hidden-input" type="hidden" value="">
			</div>
			<div style="position:relative; height:0">
				<div class="mention-select-box" style="display:none">
					<ul></ul>	
				</div>
			</div>
		</div>
		
	</div>

	<div class="hr hr-16 dotted"></div>

	<div class="control-group no-margin-bottom">
		<label class="control-label">Attachments:</label>

		<div class="controls">
			<div class="composer-files" style="display:block;">
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
		</div>
	</div>

</div>

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

<script>

document.domain=window.__g__.documentDomain;

function on_ta_focus( ta, focused ){
	var $ta = $( ta );
	if( focused ){
		$ta.next().hide();			
	} else {
		( $ta.val() == "" ) && $ta.next().show();
	}
}

$(function(){
	FileUploader.init();
	$( "#ta-message-text" ).storywriter({
		plugins: [ "MentionInputListener" ]
	});
	$("#permission-tagsinput").tagsinput({
		confirmKeys:[],
		itemValue: function(item){
			return item.id;
		},
		itemText: function(item){
			return item.name;
		},
	    freeInput: false
	});
	
})

function pds_post(){
	
	
	var data = {
	    	title:$("#pds-post-title").val(),
	    	text:$("#ta-message-text").parents(".ui-mentions-input").find('.mentions-text').val() ,
	    	permissions: Permission.getArrayData()
	};
	
	if( $("#pds-smallGroupId").length > 0 ){
		data["smallGroupId"] = $("#pds-smallGroupId").val();
	}
	
	var fileAttached = false;
	
	var _$uploadedFiles = $(".upload-image-item .uploaded-files"); 
	if( _$uploadedFiles.length > 0 ){
		data["mediaIds"]=[];
		
		_$uploadedFiles.each(function(){
			data["mediaIds"].push($(this).data("up-file"));
		});
		fileAttached = true;
	}else{
		alert("최소 1개 이상의 파일을 등록해주셔야합니다.");
		return false; 
	}
	
	
	$.ajax({
		url:"/pds/post",
		type:"POST",
		dataType:"json",
		contentType: 'application/json',
		headers: {
			"Accept": "application/json",
			"Content-Type": "application/json"
		},
	    data: JSON.stringify(data),
	    success:function(data){
	    	
	    	if( data.result != "success"){
	    		alert("실패.");
	    	}
	    	
	    	window.location.href = "<c:if test='${not empty smallGroup }'>/group/${smallGroup.id }</c:if>/pds/" + data.data.id;
	    	
	    },
		error:function(jqXHR,textStatus,errorThrown){
			$.log("error:Event.__inlineSubmit:"+errorThrown);
		}
	});
	
	return false;
}


</script>
