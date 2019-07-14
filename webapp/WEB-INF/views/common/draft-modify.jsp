<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" href="/assets/sunny/2.0/css/uncompressed/story-writer.css">

<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/bootstrap-tagsinput.css">

<script src="/assets/bootstrap/v2.3.2/js/bootstrap-tagsinput.js"></script>

<script src="/assets/sunny/2.0/js/uncompressed/file-uploader.js"></script>


<c:import url="/WEB-INF/views/common/draft-script.jsp">
	<c:param name="contentType">${param.contentType }</c:param>
</c:import>


<c:if test="${param.contentType == 8 }">
	<a href="#" onclick="return add_approbator();">개인 혹은 단체에게 결재 요청 </a><br />
	<label id="approbator-checkbox" class="checkbox ${draft.checkOrdering == false ? 'hidden-elem' : ''}">
      <input type="checkbox" ${draft.checkOrdering == false ? '' : 'checked'}>순서를 적용합니다.(이전 승인자가 승인을 해야 다음 승인자에게 보여집니다)
    </label>
    
	<ol id="approbator-list-wrap" class=" unstyled">
		<c:forEach items="${draft.draftSmallGroupApprovals }" var="draftSmallGroupApproval">
			<c:if test="${draftSmallGroupApproval.type == 0 }">
				<li class="candi-row" data-map='{"id":"${draftSmallGroupApproval.smallGroupId }", "name":"${draftSmallGroupApproval.name }"}'>
				<div class="candi-info"><span class="candi-name" >${draftSmallGroupApproval.name }
				</span><span class="toolbox">
				<span class="btn btn-primary btn-minier  btn-userAndGroup-up"><i class="fa fa-caret-up"></i></span>
				<span class="btn btn-primary btn-minier  btn-userAndGroup-down"><i class="fa fa-caret-down"></i></span>
				<span class="btn btn-danger btn-minier btn-userAndGroup-remove">
					<i class="fa fa-trash-o"></i>
				</span></span></div><div class="${draft.checkOrdering == false ? 'hidden-elem' : ''} candi-arrow"><i class="fa fa-long-arrow-down"></i></div></li>
			</c:if>
		</c:forEach>
	</ol>
	
	<hr />
	
	<a href="#" onclick="return add_cooperation();">개인 혹은 단체에게 협조 요청 </a>
	<ul id="cooperation-list-wrap" class=" unstyled">
	
		<c:forEach items="${draft.draftSmallGroupApprovals }" var="draftSmallGroupApproval">
			<c:if test="${draftSmallGroupApproval.type == 1 }">
				<li class="candi-row" data-map='{"id":"${draftSmallGroupApproval.smallGroupId }", "name":"${draftSmallGroupApproval.name }"}'>
				<div class="candi-info"><span class="candi-name" >${draftSmallGroupApproval.name }
				</span><span class="toolbox">
				<span class="btn btn-primary btn-minier  btn-userAndGroup-up"><i class="fa fa-caret-up"></i></span>
				<span class="btn btn-primary btn-minier  btn-userAndGroup-down"><i class="fa fa-caret-down"></i></span>
				<span class="btn btn-danger btn-minier btn-userAndGroup-remove">
					<i class="fa fa-trash-o"></i>
				</span></span></div></li>
			</c:if>
		</c:forEach>
	</ul>
	<hr />
	<a href="#" onclick="return add_receiver();">결재를 봐야할 개인 혹은 단체</a>
	<ul id="receiver-list-wrap" class=" unstyled">
		<c:forEach items="${draft.draftSmallGroupApprovals }" var="draftSmallGroupApproval">
			<c:if test="${draftSmallGroupApproval.type == 2 }">
				<li class="candi-row" data-map='{"id":"${draftSmallGroupApproval.smallGroupId }", "name":"${draftSmallGroupApproval.name }"}'>
				<div class="candi-info"><span class="candi-name" >${draftSmallGroupApproval.name }
				</span><span class="toolbox">
				<span class="btn btn-primary btn-minier  btn-userAndGroup-up"><i class="fa fa-caret-up"></i></span>
				<span class="btn btn-primary btn-minier  btn-userAndGroup-down"><i class="fa fa-caret-down"></i></span>
				<span class="btn btn-danger btn-minier btn-userAndGroup-remove">
					<i class="fa fa-trash-o"></i>
				</span></span></div></li>
			</c:if>
		</c:forEach>
	</ul>
	<hr />
	<a href="#" onclick="return add_circulation();">결재 내용을 볼 수는 있는 개인 혹은 단체</a>
	<ul id="circulation-list-wrap" class=" unstyled">
		<c:forEach items="${draft.draftSmallGroupApprovals }" var="draftSmallGroupApproval">
			<c:if test="${draftSmallGroupApproval.type == 3 }">
				<li class="candi-row" data-map='{"id":"${draftSmallGroupApproval.smallGroupId }", "name":"${draftSmallGroupApproval.name }"}'>
				<div class="candi-info"><span class="candi-name" >${draftSmallGroupApproval.name }
				</span><span class="toolbox">
				<span class="hmo-button hmo-button-white hmo-button-small-10  btn-userAndGroup-up"><i class="fa fa-caret-up"></i></span>
				<span class="hmo-button hmo-button-white hmo-button-small-10  btn-userAndGroup-down"><i class="fa fa-caret-down"></i></span>
				<span class="btn btn-danger btn-minier btn-userAndGroup-remove">
					<i class="fa fa-trash-o"></i>
				</span></span></div></li>
			</c:if>
		</c:forEach>
	</ul>

</c:if>


<form action="" id="draft-form" onsubmit="return window.Event &amp;&amp; (Event.__submit || Event.__inlineSubmit || fn_empty)(this, event);" rel="sync" name="draft-w-form" method="post">
	
	<input type="hidden" name="id" value="${draft.id }" />
	<div class="row-fluid">
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
	<div class="draft-editor-wrap">
		 <c:import url="/WEB-INF/views/common/sunny-editor.jsp" />
	</div>
	
	<div id="draft_mediaes" class="z1">
	
	</div>
</form>
<hr />
<div class="z1">
	<label>첨부되어 있는 파일들(삭제되면 복구할 수 없습니다)</label>
	<ul class="attachment-list pull-left unstyled ">
		<c:forEach items="${mediaes }" var="media">
			<li> 
				<a href="/download?id=${media.id}" class="attached-file inline">
					<i class="fa fa-file-alt bigger-110 middle"></i>
					<span class="attached-name middle">${media.fileName }</span>
				</a>
		
				<div class="action-buttons inline">
					<a href="/media/delfromcontent"
						data-request-map='{"mid":"${media.id }"}'
						rel="sync-get"
						ajaxify="ajax_delete_media">
						<i class="fa fa-trash-o bigger-125 blue"></i>
					</a>
				</div>
			</li>
		</c:forEach>
	</ul>
</div>
<hr />
<div class="control-group no-margin-bottom">
	<label class="control-label">새로운 첨부파일:</label>

	<div class="controls">
		<div class="composer-files" style="display:block;">
			<div class="composer-file-tiles">
				<c:forEach items="${draft.mediaes }" var="media" varStatus="status">
					<c:if test="${media.mediaType != 2 }">
						<div class="file-grid-item upload-image-item" id="item-Up${status.index }-0"><div class="ui-scaled-image-container"><span class="uploaded-files file" data-up-file="636"><span>${media.fileName }</span></span></div><input name="" value="" type="hidden"><label class="file-grid-item-remove-button ui-close-button-opa" data-tooltip-alignh="left" aria-label="이 사진의 업로드를 취소합니다." data-hover="tooltip" for=""><input id="" type="button"></label></div>
					</c:if>
					<c:if test="${media.mediaType == 2 }">
						<div class="file-grid-item upload-image-item" id="item-Up${status.index }-0"><div class="ui-scaled-image-container"><img class="uploaded-files img scaled-image-fit-width" src="${media.smallPath }"  width="96px" data-up-file="${media.id }"></div><input name="" value="" type="hidden"><label class="file-grid-item-remove-button ui-close-button-opa" data-tooltip-alignh="left" aria-label="이 사진의 업로드를 취소합니다." data-hover="tooltip" for=""><input id="" type="button"></label></div>
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

<hr />
<div class="pull-right" style="margin-bottom: 30px; ">
	<a class="hmo-button hmo-button-blue hmo-button-small-10" href="#" data-type="push" onclick="Event.__submit(this);">반영하기</a>
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
		<form id="userAndGroup-search-form" action="/pagelet/userAndGroup" method="GET" class="form-search clearfix" style="margin-bottom: 5px; clear:both;">
			<div>
				<input type="text" id="userAndGroup-search-form-input" class="search-query" style="width: 40%;">
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
