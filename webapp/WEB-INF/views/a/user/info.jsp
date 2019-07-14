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


.rw-a-header						{ font-size:12px; font-weight:none; margin:0 }
.rw-a-header i						{ font-size:18px; }
.rw-a-header strong					{ font-size:15px; font-weight:bold; }
.rw-a-content						{ margin-top:20px }

.rw-sidebar-welcome-box						{ background-color: #fafafa; border-bottom: 1px solid #ddd; heig!ht:55px; margin-bottom: 0; padding:4px }

.rw-a-profilebox-block						{ position: relative; float: left; margin-right: 8px; display: block; }
.rw-a-profilebox-block img					{ height:50px !important; width:50px !important; display: block; border:0 }

.rw-a-profilebox											{ background-color: #fafafa; border: 1px solid #ddd; height:55px; margin-bottom: 0; padding:4px 0 0 4px }
.rw-a-profilebox-contents-wrap								{ overflow: hidden; display:block }
.rw-a-profilebox-contents-wrap .rw-a-profilebox-contents	{ padding-right: 5px; display: inline-block; padding-top:5px; line-height:120% }
.rw-a-profilebox-name										{ padding-bottom: 1px; display: block; font-weight: bold; width: 112px; word-wrap: break-word; }

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
	
	$( "#form-update-user" ).validate({
		errorElement: 'span',
		errorClass: 'help-inline',
		focusInvalid: false,
		onfocusout:function (element) {
			$(element).valid();
	    },
		rules: {
			"userEmail": {
				required: true,
				email: true,
			}			
		},
		messages: {
			"userName": {
				required: " "
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
			    	MessageBox("성공", "사용자 정보가 변경되었습니다.",   MB_INFORMATION );
// 			    	window.location.reload();
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
					<c:param name="menuName">사용자관리</c:param>
				</c:import>			
				<!-- END:nav-list -->
		
			</div>
			<!-- END:sidebar -->		
	
			<!-- BEGIN:main-content -->			
			<div class="main-content">
			
				<!-- BEGIN:breadcrumbs&search -->	
				<c:set var="breadcrumbs" value="관리자콘솔,사용자관리" />
				<c:set var="breadcrumbLinks" value="/a,/a/user" />
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
									<a class="rw-a-profilebox-block" href="#">
										<img src="${user.profilePic }" alt="">
									</a>
									<div class="rw-a-profilebox-contents-wrap">
										<div class="rw-a-profilebox-contents">
											<a class="rw-a-profilebox-name" href="">${user.name }</a>
											<a href="">${user.email }</a><br>
										</div>
									</div>
								</div>							
								
								<div class="rw-a-content">
									<div class='tabbable'>
										<ul class='nav nav-tabs padding-12 tab-color-blue' id=''>
											<li class='active'>
												<a data-toggle="" href="/a/user/${user.id }/info">정보</a>
											</li>
											<li >
												<a data-toggle="" href="/a/user/${user.id }/department">부서</a>
											</li>
											<li>
												<a data-toggle="" href="/a/user/${user.id }/group?sgtype=5">소그룹</a>
											</li>
											
										</ul>
									</div>							
									<div class='rw-tab-content'>
										<div class="rw-form-wrap">
											<form class="form-vertical rw-form" id="form-update-user">
												<div class="row-fluid">
													<div class="span12">
														<div class="control-group">
															<label class="control-label" for="job1">직책1</label>
															<div class="controls">
																<div class="row-fluid">
																	<span class="span12 input-icon input-icon-right rw-input-wrap">
																		<input class="span12" type="text" name="jobTitle1" id="job1" placeholder="직책1" value="${user.jobTitle1 }">
																	</span>
																</div>
															</div>
														</div>				
														<div class="control-group">
															<label class="control-label" for="job2">직책2</label>
															<div class="controls">
																<div class="row-fluid">
																	<span class="span12 input-icon input-icon-right rw-input-wrap">
																		<input class="span12" type="text" name="jobTitle2" id="job2" placeholder="직책2" value="${user.jobTitle2 }">
																	</span>
																</div>
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="job3">직책3</label>
															<div class="controls">
																<div class="row-fluid">
																	<span class="span12 input-icon input-icon-right rw-input-wrap">
																		<input class="span12" type="text" name="jobTitle3" id="job3" placeholder="직책3" value="${user.jobTitle3 }">
																	</span>
																</div>
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="desc">설명</label>
															<div class="controls">
																<div class="row-fluid">
																	<span class="span12 input-icon input-icon-right rw-input-wrap">
																		<input class="span12" type="text" name="adminComment" id="desc" placeholder="설명" value="${user.adminComment }">
																	</span>
																</div>
															</div>
														</div>
														<div class="control-group">
															<label class="control-label" for="desc">상태</label>
															<div class="controls">
																<select name="status" id="userStatus" style="height: 30px; ">
																	<option value="0" ${user.status == 0 ? 'selected' : ''}>근무</option>
																	<option value="1" ${user.status == 1 ? 'selected' : ''}>휴가</option>
																	<option value="2" ${user.status == 2 ? 'selected' : ''}>퇴사</option>
																</select>
															</div>
														</div>													
													</div>
												</div>
												<div class="row-fluid">
													<div class="span12">
														<div class="control-group site-admin-checkbox">
															<label class="rw-chckbx">
																<input name="admin" type="checkbox" class="ace" value="true" <c:if test="${user.admin }">checked</c:if>>
																<span class="lbl">
																	<span>사이트 관리자 입니다.</span>
																</span>
															</label>
														</div>				
														<div class="control-group rw-btn-info-save">
															<button class="hmo-button hmo-button-blue hmo-button-small-10">저장</button>
															<a class="pull-right hmo-button hmo-button-blue hmo-button-small-10"
																href="/a/user/${user.id }"
																role="dialog"
																data-style="messagebox-yesno" 
																data-title="정말 삭제하시겠습니까?"
																data-message="사용자가 삭제되면 해당 사용자가 작성한 모든 데이터가 삭제되며 복구할 수 없습니다.<br>이 사용자를 영구적으로 삭제하시겠습니까?"
																ajaxify-dialog-yes="ajax_remove_user" 
																rel="sync-DELETE"
																data-request-map='{"id":"${user.id }"}'
																 >삭제</a>
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

<script>
function ajax_remove_user(data){
	if( data.result == "fail" ){
		MessageBox("오류", data.message, MB_ERROR );
		return false;
	}
	MessageBox("삭제 성공", "삭제되었습니다. 이전 화면으로 이동합니다.", MB_INFORMATION);
	window.location.href="/a/user";
	return false; 
}

</script>
</body>
</html>