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
						<c:param name="menuName">menu</c:param>
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
											<th>이름</th>
											<th>설명</th>
											<th>아이콘</th>
											<th>Extra 아이콘</th>
											<th>상대경로</th>
											<th>순서</th>
											<th>타입</th>
											<th>절대이름</th>
											<th>
												변경하기
											</th>
										</tr>
									<c:forEach items="${menus }" var="menu">
												<tr>
													<form method="post" action="/super/update_menu" style="float:left; ">
													<td>
														<input style="width:60px;"type="text" name="name" value="${menu.name }" />		
													</td>
													<td>
														<input type="text" name="description" value="${menu.description }" />
													</td>
													<td>
														<input type="text" name="iconHtml" value="${menu.iconHtml }" />
													</td>
													<td>
														<input type="text" name="extraHtml" value="${menu.extraHtml }" />
													</td>
													<td>
														<input type="text" name="relativeHref" value="${menu.relativeHref }" />
													</td>
													<td>
														<input style="width:15px;" type="text" name="ordering" value="${menu.ordering }" />
													</td>
													<td>
														<select name="type">
															<option value="0" ${menu.type == 0 ? 'selected' : '' }>일반메뉴</option>
															<option value="1" ${menu.type == 1 ? 'selected' : '' }>부서</option>
															<option value="2" ${menu.type == 2 ? 'selected' : '' }>소그룹</option>
														</select>
													</td>
													<td>
														<input type="text" name="absoluteName" value="${menu.absoluteName }" />
													</td>
													<td>
														<input type="submit" value="수정">
														<input type="hidden" name="id" value="${menu.id }">
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
									<form method="post" action="/super/create_menu" >
										<table>
											<tr>
												<th>이름</th>
												<th>설명</th>
												<th>아이콘</th>
												<th>Extra 아이콘</th>
												<th>상대경로</th>
												<th>순서</th>
												<th>타입</th>
												<th>절대이름</th>
												<th>
													생성
												</th>
											</tr>
											<tr>
												<td>
													<input style="width:60px;"type="text" name="name" value="${menu.name }" />		
												</td>
												<td>
													<input type="text" name="description" value="${menu.description }" />
												</td>
												<td>
													<input type="text" name="iconHtml" value="${menu.iconHtml }" />
												</td>
												<td>
													<input type="text" name="extraHtml" value="${menu.extraHtml }" />
												</td>
												<td>
													<input type="text" name="relativeHref" value="${menu.relativeHref }" />
												</td>
												<td>
													<input style="width:15px;" type="text" name="ordering" value="${menu.ordering }" />
												</td>
												<td>
													<select name="type">
														<option value="0" ${menu.type == 0 ? 'selected' : '' }>일반메뉴</option>
														<option value="1" ${menu.type == 1 ? 'selected' : '' }>부서</option>
														<option value="2" ${menu.type == 2 ? 'selected' : '' }>소그룹</option>
													</select>
												</td>
												<td>
													<input type="text" name="absoluteName" value="${menu.absoluteName }" />
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
									<a href="/super/sync_menu">각 사이트의 메뉴와 싱크 맞추기</a>
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
		$form.get(0).setAttribute("action", "/super/remove_user");
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