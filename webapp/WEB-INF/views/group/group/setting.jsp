<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">소그룹 세팅</c:param>
	<c:param name="bsUsed">NO</c:param>
	<c:param name="aceUsed">NO</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>
<style type="text/css">
.rw-content-area-wrap	{background-color: white;}
</style>

</head>
<body class="scroll-y">

<c:choose>
	<c:when test="${smallGroup.type == 3}">
		<c:set var="groupTypeName" value="부서" />
	</c:when>
	<c:when test="${smallGroup.type == 4}">
		<c:set var="groupTypeName" value="프로젝트" />
	</c:when>
	<c:when test="${smallGroup.type == 5}">
		<c:set var="groupTypeName" value="소그룹" />
	</c:when>

</c:choose>

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
		<div class="main-container container-fluid z1" id="main-container">
			
			<!-- BEGIN:sidebar -->		
			<div class="sidebar" id="snn-sidebar">
			
				<!-- BEGIN:welcome-box -->		
				<c:import url="/WEB-INF/views/common/welcome-box.jsp">
				</c:import>
				<!-- END:welcome-box -->
	
				<!-- BEGIN:nav-list -->
				<c:import url="/WEB-INF/views/common/nav-list.jsp">
					<c:param name="menuName">${groupTypeName }</c:param>
				</c:import>			
				<!-- END:nav-list -->
			</div>
			<!-- END:sidebar -->
			
			<!-- BEGIN:main-content -->		
			<div class="main-content z1">
			
				
								
				<!-- BEGIN:page-content -->						
				<div class="page-content z1">
					<div class="rw-content-area-wrap">
						<!-- BEGIN:page-header -->
						<c:import url="/WEB-INF/views/common/page-header.jsp">
							<c:param name="pageName">group</c:param>
							<c:param name="tabName">setting</c:param>
						</c:import>					
						<!-- END:page-header -->	
						<div class="rw-content-area rw-content-area-top-02">
							<div class="rw-pagelet-wrap rw-mtl rw-mobile-pagelet">
								<div class="ui-rw-pagelet-wrap rw-mobile-pgelet-wrap group-setting-rw-mobile-pgelet-wrap">
									<form id="form-group-setting" action="/group/${smallGroup.id }/setting" method="post" rel="sync" onsubmit="return window.Event&amp;&amp;Event.__inlineSubmit&amp;&amp;Event.__inlineSubmit(this,event);">
										<div class="_h6m"></div>
										<ul class="ui-rw-pagelet-list">
											<li class="_z6m pagelet-row">
												<table class="ui-grid _k3x">
													<tbody>
														<tr class="_3stt">
															<th class="_3sts"><label class="_sl1"  for="groupName">이름</label></td>
															<td class="_480u"><input class="rw-mobile-pgelet-wrap-input uiInlineTokenizer fbGroupEditTopic" type="text" name="name" id="groupName" placeholder="소그룹 이름을 넣어주세요" value="${smallGroup.name }"></td>													
														</tr>
													</tbody>
												</table>
											</li>
											<li class="_z6m pagelet-row">
												<table class="ui-grid _k3x">
													<tbody>
														<tr class="_3stt">
															<th class="_3sts"><label class="_sl1" for="groupDescription">설명</label></td>
															<td class="_480u"><textarea class="rw-mobile-pgelet-wrap-textarea fbGroupDescriptionEditor uiInlineTokenizer fbGroupEditTopic" name="description" id="groupDescription" placeholder="소그룹 설명을 넣어주세요.">${smallGroup.description }</textarea></td>	
														</tr>
													</tbody>
												</table>
											</li>	
											<li class="_z6m pagelet-row rw-mobile-pgelet-wrap-button">
												<div style="clear:both;display:inline-block;">
												<div class="_3sts l-ft"></div>
												<ul class="_4ki rw-mobile-pgelet-wrap-button-ul">
													<li>
														<a href="/group/${smallGroup.id}/remove" 
														role="dialog"
														data-style="messagebox-yesno"
														data-title="그룹 삭제"
														data-message="확인을 누르시면 그룹의 모든 글이 삭제되며 복구할 수 없습니다.<br/>이 그룹을 삭제하시겠습니까?"
														ajaxify-dialog-yes="ajax_remove_smallgroup"
														rel="sync-get"
														data-request-map='{}'
														class="hmo-button hmo-button-white hmo-button-small-10">삭제하기</a>	
													</li>
													<li>
														<button class="hmo-button hmo-button-blue hmo-button-small-10">변경하기</button>											
													</li>
												</ul>
												</div>
											</li>	
										</ul>	
										<div class="_f6m"></div>									
										<input type="hidden" name="id" value="${smallGroup.id }" />
									</form>
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
<script>
function ajax_remove_smallgroup(data){
	if( data.result != "success"){
		MessageBox( "그룹", "그룹 삭제중 다음과 같은 오류가 발생 했습니다.<br>" + data.message, MB_ERROR );
		return false;
	}	
	MessageBox( "그룹", "그룹이 삭제되었습니다.<br /> 확인 버튼을 누르시면 그룹 리스트로 이동합니다.", MB_INFORMATION);
	window.location.href="/group";
}
</script>


<script>

Event.__inlineSubmit = function( form, event ){
	
	$.Event( event ).preventDefault();

	if( !$("#form-group-setting").valid() ){
		return false; 
	}
	var 
	$form=$(form),
	rel=$form.attr("rel"),
	url=$form.attr("action"),
	type=$form.attr("method");
	
	var data = $form.serializeObject();
	
	$.ajax({
		url:url,
		type:"POST",
		dataType:"json",
		contentType: 'application/json',
		headers: {
			"Accept": "application/json",
			"Content-Type": "application/json"
		},
	    data: JSON.stringify(deepen( data ) ),
	    success:function(data){
	    	if( data.result == "fail" ){
	    		alert( data.message );
	    		return false;
	    	}
	    	
	    	MessageBox("그룹 변경", "그룹 정보가 변경되었습니다.", MB_INFORMATION);
	    },
		error:function(jqXHR,textStatus,errorThrown){
			$.log("error:Event.__inlineSubmit:"+errorThrown);
		}
	});

	return false;
}


</script>	
</body>
</html>