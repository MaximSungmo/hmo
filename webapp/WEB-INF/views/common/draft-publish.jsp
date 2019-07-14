<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- <link rel="stylesheet" href="/assets/sunny/2.0/css/uncompressed/story-writer.css"> -->

<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/bootstrap-tagsinput.css">


<script src="/assets/bootstrap/v2.3.2/js/bootstrap-tagsinput.js"></script>

<c:import url="/WEB-INF/views/common/draft-script.jsp">
	<c:param name="contentType">${param.contentType }</c:param>
</c:import>

<script src="/assets/sunny/2.0/js/uncompressed/file-uploader.js"></script>

<!-- <div> -->
<!-- 	<div class="control-group"> -->
<!-- 		<label class="control-label" for="form-field-recipient">권한설정:</label> -->
<!-- 		<div id="userAndGroup-toggle-wrap" class="controls" style="position: relative;"> -->
<!-- 			<a href="#" id="toggle-userAndGroup-detail" class="btn-message btn btn-mini dropdown-toggle" data-toggle="dropdown">  -->
<%-- 				<span class="bigger-120 text">${empty smallGroup ? '전체공개' : smallGroup.name } --%>
<!-- 			</span> <i class="fa fa-caret-down fa-on-right"></i> -->
<!-- 			</a> -->
<%-- 			<c:import url="/WEB-INF/views/pagelet/userAndGroup/dropdown.jsp"> --%>
<%-- 				<c:param name="dropdownDirection" value="left" /> --%>
<%-- 			</c:import> --%>
<!-- 		</div> -->
<!-- 	</div> -->
<!-- </div> -->


<c:if test="${param.contentType == 8 }">
	<div class="approval-write-header-top">
		<a href="#" onclick="return add_approbator();" class="approval-write-header-menu1"><i class="fa fa-angle-right"></i>결재를 요청할 승인처 추가 </a>
	</div>
    <div id="approbator-wrap" class="widget-box ${empty draft.draftSmallGroupApprovals ? 'hidden-elem' : '' } approval-write-header-top-02" style="border-bottom:none;" >
    	<%-- <div class="widget-header">
    		<h3>승인처</h3>
    	</div>--%>
		<div class="widget-body" >
			<ol id="approbator-list-wrap" class="unstyled" style="margin-bottom:0px;">
				<c:forEach items="${draft.draftSmallGroupApprovals }" var="draftSmallGroupApproval">
					<c:if test="${draftSmallGroupApproval.type == 0 }">
						<li class="candi-row" data-sgid="${draftSmallGroupApproval.smallGroupId}" data-map='{"id":"${draftSmallGroupApproval.smallGroupId }", "name":"${draftSmallGroupApproval.name }"}'>
						<div class="candi-info"><span class="candi-name" >${draftSmallGroupApproval.name }
						</span><span class="toolbox">
						<span class="hmo-button hmo-button-white hmo-button-small-4"><i class="fa fa-caret-up"></i></span>
						<span class="hmo-button hmo-button-white hmo-button-small-4"><i class="fa fa-caret-down"></i></span>
						<span class="hmo-button hmo-button-white hmo-button-small-4 btn-userAndGroup-remove">
							<i class="fa fa-trash-o"></i>
						</span></span></div><div class="${draft.checkOrdering == false ? 'hidden-elem' : ''} candi-arrow"><i class="fa fa-long-arrow-down"></i></div></li>
					</c:if>
				</c:forEach>
			</ol>
		</div>
    </div>
	
	<label id="approbator-checkbox" class="approval-write-header-menu1-label checkbox ${empty draft.draftSmallGroupApprovals && draft.checkOrdering == false ? 'hidden-elem' : ''}">
      <input class="ace" type="checkbox" ${draft.checkOrdering == false ? '' : 'checked'}><span class="lbl">순서를 적용합니다.(이전 승인자가 승인을 해야 다음 승인자에게 보여집니다)</span>
    </label>
	
		
	<div id="misc-wrap" class="approval-write-header-bottom">
	
		
	
		<a href="#" onclick="return add_cooperation();" class="approval-write-header-menu2"><i class="fa fa-angle-right"></i>협조요청 추가</a>
		
	    <div id="cooperation-wrap" class="widget-box ${ draft.cooperationCount == 0 ? 'hidden-elem' : '' } approval-write-header-menu2-label">
	    	<%-- <div class="widget-header">
	    		<h3>협조요청</h3>
	    	</div>--%>
			<div class="widget-body" >
				<ul id="cooperation-list-wrap" class=" unstyled" style="margin-bottom:0px;">
					<c:forEach items="${draft.draftSmallGroupApprovals }" var="draftSmallGroupApproval">
						<c:if test="${draftSmallGroupApproval.type == 1 }">
							<li class="candi-row" data-sgid="${draftSmallGroupApproval.smallGroupId}"  data-map='{"id":"${draftSmallGroupApproval.smallGroupId }", "name":"${draftSmallGroupApproval.name }"}'>
							<div class="candi-info"><span class="candi-name" >${draftSmallGroupApproval.name }
							</span><span class="toolbox">
							<span class="hmo-button hmo-button-white hmo-button-small-4"><i class="fa fa-caret-up"></i></span>
							<span class="hmo-button hmo-button-white hmo-button-small-4"><i class="fa fa-caret-down"></i></span>
							<span class="hmo-button hmo-button-white hmo-button-small-4 btn-userAndGroup-remove">
								<i class="fa fa-trash-o"></i>
							</span></span></div></li>
						</c:if>
					</c:forEach>
				</ul>
			</div>
		</div>
		<a href="#" onclick="return add_receiver();" class="approval-write-header-menu3"><i class="fa fa-angle-right"></i>수신자 추가</a>
		<div id="receiver-wrap" class="widget-box ${ draft.receiverCount == 0 ? 'hidden-elem' : '' } approval-write-header-menu3-label">
	    	<%--<div class="widget-header">
	    		<h3>수신자 목록</h3>
	    	</div>--%>
			<div class="widget-body" >
				<ul id="receiver-list-wrap" class=" unstyled" style="margin-bottom:0px;">
					<c:forEach items="${draft.draftSmallGroupApprovals }" var="draftSmallGroupApproval">
						<c:if test="${draftSmallGroupApproval.type == 2 }">
							<li class="candi-row" data-sgid="${draftSmallGroupApproval.smallGroupId}" data-map='{"id":"${draftSmallGroupApproval.smallGroupId }", "name":"${draftSmallGroupApproval.name }"}'>
							<div class="candi-info"><span class="candi-name" >${draftSmallGroupApproval.name }
							</span><span class="toolbox">
							<span class="hmo-button hmo-button-white hmo-button-small-4"><i class="fa fa-caret-up"></i></span>
							<span class="hmo-button hmo-button-white hmo-button-small-4"><i class="fa fa-caret-down"></i></span>
							<span class="hmo-button hmo-button-white hmo-button-small-4 btn-userAndGroup-remove">
								<i class="fa fa-trash-o"></i>
							</span></span></div></li>
						</c:if>
					</c:forEach>
				</ul>
			</div>
		</div>
		<a href="#" onclick="return add_circulation();" class="approval-write-header-menu4"><i class="fa fa-angle-right"></i>회람 추가</a>
		<div id="circulation-wrap" class="widget-box ${ draft.circulationCount == 0 ? 'hidden-elem' : '' } approval-write-header-menu4-label">
	    	<%-- <div class="widget-header">
	    		<h3>수신자 목록</h3>
	    	</div>--%>
			<div class="widget-body" >
				<ul id="circulation-list-wrap" class=" unstyled" style="margin-bottom:0px;">
					<c:forEach items="${draft.draftSmallGroupApprovals }" var="draftSmallGroupApproval">
						<c:if test="${draftSmallGroupApproval.type == 3 }">
							<li class="candi-row" data-sgid="${draftSmallGroupApproval.smallGroupId}" data-map='{"id":"${draftSmallGroupApproval.smallGroupId }", "name":"${draftSmallGroupApproval.name }"}'>
							<div class="candi-info"><span class="candi-name" >${draftSmallGroupApproval.name }
							</span><span class="toolbox">
							<span class="hmo-button hmo-button-white hmo-button-small-4"><i class="fa fa-caret-up"></i></span>
							<span class="hmo-button hmo-button-white hmo-button-small-4"><i class="fa fa-caret-down"></i></span>
							<span class="hmo-button hmo-button-white hmo-button-small-4 btn-userAndGroup-remove">
								<i class="fa fa-trash-o"></i>
							</span></span></div></li>
						</c:if>
					</c:forEach>
				</ul>
			</div>
		</div>
		<div class="approval-write-header-menu-description">
		<span>협조처, 수신자 그리고 회람은 승인자가 모두 승인을 한 뒤에 결재가 보여집니다.</span>
		</div>
	</div>
</c:if>

<form action="" id="draft-form" onsubmit="return window.Event &amp;&amp; (Event.__submit || Event.__inlineSubmit || fn_empty)(this, event);" rel="sync" name="draft-w-form" method="post">
	
	<input type="hidden" name="id" value="${draft.id }" />
	
	<div class="row-fluid approval-write-form-header">
		<div class="span12">
			<div class="control-group">
				<label class="control-label" for="note-title">제목</label>
				<div class="controls">
					<div class="row-fluid">
						<input	id="note-title"
							class="span12 inpu-text edit-draft-title-input dom-control-placeholder" 
							type="text" 
							placeholder="제목을 입력해주세요" 
							maxlength="100" 
							autocomplete="off" 
							name="title"
							value="${draft.rawTitle }">
					</div>
				</div>
			</div>
		</div>
	</div>
	<div  class="draft-editor-wrap">
		 <c:import url="/WEB-INF/views/common/sunny-editor.jsp" />
	</div>
	<div id="draft_mediaes" class="z1">
	
	</div>
</form>
<div class="control-group no-margin-bottom approval-write-body-bottom">
	<label class="control-label">첨부파일</label>

	<div class="controls">
		<div class="composer-files" style="display:block;">
			<div class="composer-file-tiles">
				<c:forEach items="${draft.mediaes }" var="media" varStatus="status">
					<c:if test="${media.mediaType != 2 }">
						<div class="file-grid-item upload-image-item" id="item-Up${status.index }-0">
							<div class="ui-scaled-image-container">
								<span class="uploaded-files file" data-up-file="636">
									<span>${media.fileName }</span>
								</span>
							</div>
							<input name="" value="" type="hidden">
							<label class="file-grid-item-remove-button ui-close-button-opa" data-tooltip-alignh="left" aria-label="이 사진의 업로드를 취소합니다." data-hover="tooltip" for="">
								<a href="/media/delfromdraft" rel="sync-get" data-request-map='{"did":"${draft.id }", "mid":"${media.id }"}' ajaxify="FileUploaderOnRemoveClick"><i class="fa fa-trash-o bigger-200 red"></i></a>
<!-- 								<input id="" type="button"> -->
							</label>
						</div>
					</c:if>
					<c:if test="${media.mediaType != 2 }">
						<div class="file-grid-item upload-image-item"
							id="item-Up${status.index }-0">
							<div class="ui-scaled-image-container">
								<img class="uploaded-files img scaled-image-fit-width"
									src="${media.smallPath }" width="96px"
									data-up-file="${media.id }">
							</div>
							<input name="" value="" type="hidden">
							<label class="file-grid-item-insert-button"
								data-tooltip-alignh="left" aria-label="이 사진을 커서에 넣습니다."
								data-hover="tooltip" for="">
								<a href="#" data-hugesrc="${media.hugePath }" onclick="return insert_to_focus(this);"><i class="fa fa-arrow-circle-o-up bigger-250"></i></a>
							</label>
							<label class="file-grid-item-remove-button ui-close-button-opa"
								data-tooltip-alignh="left" aria-label="사진을 삭제합니다."
								data-hover="tooltip" for="">
								<a href="/media/delfromdraft" rel="sync-get" data-request-map='{"did":"${draft.id }", "mid":"${media.id }"}' ajaxify="FileUploaderOnRemoveClick"><i class="fa fa-trash-o bigger-200 red"></i></a>
<!-- 								<input id="" type="button" class="fa fa-trash-o"> -->
							</label>
						</div>
					</c:if>
					<script>
						FileUploader.uploadIdCounter++;
					</script>
				</c:forEach>
				<div class="file-grid-item next-set-item">
					<div class="ui-scaled-image-container">
						<a rel="ignore" role="button">
							<div class="next-item-inner">
								<form enctype="multipart/form-data"
									  action="/upload"
									  method="post">
									<input type="file" name="file" onchange="FileUploader &amp;&amp; FileUploader.onFileSelected(this);" multiple="multiple" accept="*/*">
									<input type="hidden" name="upid" value="">
									<c:if test="${not empty draft }">
										<input type="hidden" name="did" value="${draft.id }">
									</c:if>
								</form>
							</div>
						</a>
					</div>
				</div>
				
			</div>
		</div>
	</div>
</div>

<div class="approval-write-button-wrap">
	<div class="pull-right">
		<a class="hmo-button hmo-button-blue hmo-button-small-10 approval-write-button" href="#" id="" data-type="publish" onclick="Event.__submit(this);">게시하기</a>
	</div>	
</div>

	
<script>

document.domain=window.__g__.documentDomain;


$(function(){
	FileUploader.init();
	$("#userAndGroup-search-form-input").tagsinput({
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

</script>




<c:if test="${param.contentType == 8 }">
<div id="res-dialog-add-user-and-group" style="display:none">
	<div class="rw-dialog-wrap " id="res-dialog-add-user-and-group-content">
		<input type="text" id="userAndGroup-search-form-input">
		<form id="userAndGroup-search-form" action="/pagelet/userAndGroup" method="GET" class="form-search clearfix" style="margin-bottom: 5px; clear:both;">
			<div>
				<input type="text"  class="search-query" style="width: 40%;">
				<button class="hmo-button-search hmo-button hmo-button-blue">
					<span>검색</span>
				</button>
			</div>
		</form>
		<div data-toggle="buttons-radio" class="btn-group">
			<button data-name="type" data-value="0" class="userAndGroup-more-detail btn btn-info btn-small active" type="button">사람</button>
			<button data-name="type" data-value="3" class="userAndGroup-more-detail btn btn-info btn-small " type="button">부서</button>
			<button data-name="type" data-value="4" class="userAndGroup-more-detail btn btn-info btn-small" type="button">프로젝트</button>
			<button data-name="type" data-value="5" class="userAndGroup-more-detail btn btn-info btn-small" type="button">그룹</button>
		</div>
		<div class="userAndGroup-scroll-wrap userAndGroup-wrap scrollable" >
			<div class="changeable-pagelet">
			</div>
		</div>
	</div>
</div>
</c:if>
<%-- End of 결재일때 --%>
