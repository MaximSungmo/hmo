<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title"></c:param>
	<c:param name="bsUsed">NO</c:param>
	<c:param name="aceUsed">NO</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>
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
			<div class="main-container container-fluid z1" id="main-container">
				
				<!-- BEGIN:sidebar -->
				<div class="sidebar" id="snn-sidebar">
					<!-- BEGIN:nav-list -->
					<c:import url="/WEB-INF/views/common/nav-list-super.jsp">
						<c:param name="menuName">tag</c:param>
					</c:import>			
					<!-- END:nav-list -->
				</div>					
				<!-- END:sidebar -->
				
				<!-- BEGIN:main-content -->		
				<div class="main-content z1">			
					<div class="page-content z1">
						<div class="rw-content-area-wrap">
							<div class="rw-content-area" style="margin-right:0;">
								<div class="rw-pagelet-blank"></div>
								<div class="rw-pagelet-wrap">
								
								
								
								<%-- BEGIN:내부 컨텐츠 --%>
									<table>
										<tr>
											<th>타이틀</th>
											<th>순서</th>
											<th>타입</th>
											<th>
												변경하기
											</th>
										</tr>
									<c:forEach items="${tags }" var="tag">
												<tr>
													<form method="post" action="/super/update_tag" style="float:left; ">
													<td>
														<input style="width:60px;"type="text" name="title" value="${tag.title }" />		
													</td>
													<td>
														<input type="text" name="adminOrdering" value="${tag.adminOrdering }" />
													</td>
													<td>
														<select name="contentType">
															<option value="1" ${tag.contentType == 1 ? 'selected' : '' }>스토리</option>
														</select>
													</td>
													<td>
														<input type="submit" value="수정">
														<input type="hidden" name="id" value="${tag.id }">
														<input type="hidden" name="sunny" value="sunnyvale">
													</td>
													</form>	
												</tr>
										<div style="clear:both;"></div>
										
									</c:forEach>
									</table>
									
								<%-- END:내부 컨텐츠 --%>
								
								</div>
								
								<div class="rw-pagelet-blank"></div>
								<div class="rw-pagelet-wrap">
									<form method="post" action="/super/create_tag" >
										<table>
											<tr>
												<th>타이틀</th>
												<th>순서</th>
												<th>타입</th>
												<th>
													생성
												</th>
											</tr>
											<tr>
												<td>
													<input style="width:60px;"type="text" name="title" value="${tag.title }" />		
												</td>
												<td>
													<input type="text" name="adminOrdering" value="${tag.adminOrdering}" />
												</td>
												
												<td>
													<select name="contentType">
														<option value="1" selected>스토리</option>
													</select>
												</td>
												<td>
													<input type="submit" value="생성">
												</td>
											</tr>
										</table>
									</form>	
								</div>
								<div class="rw-pagelet-blank"></div>
								<div class="rw-pagelet-wrap">
									<a href="/super/sync_tag">각 사이트의 태그들과 싱크 맞추기</a>
								</div>
							</div>
						</div>
					</div>
				</div>
			<!-- END:main-content -->		
			</div>
		</div>
	</div>
<script>
function inlineSubmit( form, event ) {	
	var 
	$form = $(form);
	var isConfirmed = confirm("정말 삭제하시겠습니까?");

	if( isConfirmed == true ){
		$form.get(0).setAttribute("action", "/super/remove_tag");
	}
	
	return isConfirmed;
}
// function inlineSubmit( form, event ) {
// 	$.Event( event ).preventDefault();
	
// 	var 
// 	$form = $(form),
// 	$textarea = $form.find( "textarea" );
// 	$uploadedFiles = $form.parent().find(".upload-image-item .uploaded-files"),
// 	rel = $form.attr("rel"),
// 	url = $form.attr("action"),
// 	type = $form.attr("method"),
// 	data = $form.serializeObject(),
// 	fileAttached = false;
	
// 	if( $uploadedFiles.length > 0 ){
// 		data["mediaIds"] = [];
// 		$uploadedFiles.each( function() {
// 			data["mediaIds"].push( $( this ).data( "up-file" ) );
// 		});
// 		fileAttached = true;
// 	}
	
// 	if( fileAttached == false && $.trim(data["text"]) == "" ) {
// 		$textarea.focus();
// 		this.waitStoryPost = 0;
// 		return false;
// 	}
	
// 	var permissions = Permission.getArrayData();
// 	if( typeof(permissions) != "undefined" && permissions != null && permissions.length > 0 ){
// 		data["permissions"] = permissions;
// 	}
// 	data["permissionType"] = $("#permission-list-ul li.active > .default-candi-row").data("type");
// 	data["notice"] = $("#composer-button-notice").data("checked");

	
// 	$.ajax({
// 		url:url,
// 		type:type,
// 		async: rel == "async",
// 		dataType:"json",
// 		contentType: 'application/json',
// 		headers: {
// 			"Accept": "application/json",
// 			"Content-Type": "application/json"
// 		},
// 	    data: JSON.stringify(data),
// 	    success:this.onAjaxStoryPost,
// 		error:function(jqXHR,textStatus,errorThrown){
// 			$.log("error:Event.__inlineSubmit:"+errorThrown);
// 		}
// 	});

// 	return false;
// }
</script>
</body>
</html>