<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">사용자 관리</c:param>
	<c:param name="bsUsed">YES</c:param>
	<c:param name="aceUsed">YES</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>
<style type="text/css">
.rw-content-area-wrap	{background-color: white;}
.scroll-y 				{ padding: 0;}
.nav-search-icon		{ top: 2px; left: 3px;}
</style>
<!-- BEGIN:ace scripts -->
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-elements.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-extra.js"></script>
<!-- END:ace scripts -->

<!-- BEGIN:additional plugin -->
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/bootbox.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/jquery.validate.js"></script>
<!-- END:additional plugin -->

<script>

$( function() {
	//validation for new user
	
	$( "#form-update-smallgroup" ).validate({
		errorElement: 'span',
		errorClass: 'help-inline',
		focusInvalid: false,
		onfocusout:function (element) {
			$(element).valid();
	    },
		rules: {
			"name" : {
				required: true
			},
			"description": {
				required: true
			}
		},
		messages: {
			"name": {
				required: "이름은 필수요소입니다."
			},
			"description": {
				required: "부서 설명을 넣어주세요 ",
				email: " "
			}
		},
		invalidHandler: function (event, validator) {	
		},
		highlight: function (e) {
			$(e).closest('.control-group').removeClass('info').addClass('error');
		},
		success: function (e) {
			$(e).closest('.control-group').removeClass('error').addClass('info');
			$(e).remove();
		},
		errorPlacement: function ( error, element ) {
			if(element.is(':checkbox') || element.is(':radio')) {
				var controls = element.closest('.controls');
				if(controls.find(':checkbox,:radio').length > 1) {
					controls.append(error);
				} else {
					error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
				}
			} else if(element.is('.select2')) {
				error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
			} else if(element.is('.chosen-select')) {
				error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
			} else {
				error.insertAfter( element.parent().eq(0) );
			}
		},
		submitHandler: function (form) {
			var data = {}, arr;
			
			arr = $(form).serializeArrayAlt();
			$.each( arr, function() {
				if( this.value != null && this.value != ""){
					data[this.name] = this.value;
				}
			});
			$.ajax({
				url:"",
				type:"post",
				dataType:"json",
				headers: {
					'Accept':'application/json',
					'Content-Type':'application/json'
					},
			    data: JSON.stringify( deepen( data ) ),
			    success: function( data ) {
			    	if( data.result == "fail" ){
			    		alert(data.message);
			    		return;
			    	}
			    	
			    	alert("부서 정보가 변경되었습니다.");
			    },
				error:function( jqXHR,textStatus,errorThrown ) {
					$.log( "error:Event.__inlineSubmit:" + errorThrown );
				}
			});
			return true;
			
			
		}
	});		
});

</script>
</head>
<body class="scroll-y">
<div class="rw-snn-wrap" id="rw-snn-wrap">
	<div id="rw-snn-navi-wrap"></div>
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
				<c:import url="/WEB-INF/views/common/nav-list-a.jsp">
					<c:param name="menuName">부서관리</c:param>
				</c:import>			
				<!-- END:nav-list -->
		
			</div>
			<!-- END:sidebar -->		
	
			<!-- BEGIN:main-content -->			
			<div class="main-content">
			
				<!-- BEGIN:breadcrumbs&search -->	
				<c:set var="breadcrumbs" value="관리자콘솔,부서관리,'${smallGroup.name}'-정보" />
				<c:set var="breadcrumbLinks" value="/a,/a/department,/a/department/${smallGroup.id}/info" />
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
						<div class="rw-content-area rw-content-fs">
							<div class="rw-pagelet-wrap rw-mtl">
													
								<div class="rw-a-profilebox">
									<div class="rw-a-profilebox-contents-wrap">
										<div class="rw-a-profilebox-contents">
											<%--
											${not empty smallGroup.parent ? smallGroup.parent.name : ''} -
											${smallGroup.name } 에 대한 정보입니다.
											 --%>
										</div>
									</div>
								</div>		
															
								<div class="rw-a-content">
									<div class='tabbable'>
										<ul class='nav nav-tabs padding-12 tab-color-blue' id=''>
											<li class='active'>
												<a href="/a/department/${smallGroup.id }/info">정보</a>
											</li>
											<li>
												<a href="/a/department/${smallGroup.id }/users">구성원</a>
											</li>
										</ul>
									</div>							
									<div class='rw-tab-content'>
										<div class="rw-form-wrap">
											<form class="form-vertical rw-form" id="form-update-smallgroup">
												<div class="row-fluid">
													<div class="span9">
														<div class="control-group">
															<label class="control-label" for="name">부서 이름</label>
															<div class="controls">
																<div class="row-fluid">
																	<span class="span12 input-icon input-icon-right rw-input-wrap">
																		<input class="span12" type="text" name="name" id="name"  value="${smallGroup.name }" placeholder="이름">
																		<i class="icon-remove-sign"></i>
																		<i class="icon-ok-sign"></i>
																	</span>
																</div>
															</div>
														</div>	
	
														<div class="control-group">
															<label class="control-label" for="name">부서 설명</label>
															<div class="controls">
																<div class="row-fluid">
																	<span class="span12 input-icon input-icon-right rw-input-wrap">
																		<input class="span12" type="text" name="description" id="description"  value="${smallGroup.description }" placeholder="이름">
																		<i class="icon-remove-sign"></i>
																		<i class="icon-ok-sign"></i>
																	</span>
																</div>
															</div>
														</div>	
	
														<%--
														<div class="control-group">
															<label class="control-label" for="desc">상태</label>
															<div class="controls">
																<select name="status" id="status" style="height: 30px; ">
																	<option value="0" ${smallGroup.status == 0 ? 'selected' : ''}>운영중</option>
																	<option value="1" ${smallGroup.status == 1 ? 'selected' : ''}>폐쇄</option>
																	<option value="2" ${smallGroup.status == 2 ? 'selected' : ''}>삭제</option>
																</select>
															</div>
														</div>		
														 --%>											
													</div>
	
												</div>
												<div class="row-fluid">
													<div class="span12">
			
														<div class="control-group rw-btn-info-save">
															<button class="hmo-button hmo-button-blue hmo-button-small-10">저장</button>
														</div>
													</div>
												</div>
											</form>
										</div>
									</div>
								</div>	
							</div>
						</div>
					</div>
				</div>
				<!-- END:page-content -->						
			</div>
			<!-- END:main-content -->	
		</div>
		<!-- EDN:main-container -->
	</div>	
</div>
</body>
</html>