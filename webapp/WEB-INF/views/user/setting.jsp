<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
	<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="ko">
<head>

<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">설정</c:param>
	<c:param name="bsUsed">YES</c:param>
	<c:param name="aceUsed">YES</c:param>
	<c:param name="hmoUsed">YES</c:param>	
</c:import>

<!-- ace settings handler -->
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-extra.min.js"></script>

<!-- BEGIN:ace scripts -->
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-elements.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace.js"></script>



<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/select2.min.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/fuelux/fuelux.spinner.min.js"></script>
<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/jquery.gritter.css" />



<!-- END:sunny extra libraries -->
<script
	src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/bootbox.js"></script>

<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/jquery.gritter.min.js"></script>
<!-- BEGIN:basecamp script -->

<script>

var settingUrl = '/user/${basecampUser.id}/alter_settings';
document.domain = "${sunny.documentDomain}";
var cdnUrl = "";  

$(function() {

    bootbox.setLocale("kr");
    
	$("#change-jobtitles").onHMOClick(null, function(){
		
		bootbox.dialog( "res-dialog-change-jobtitles", [
		{
			"label" : "취소",
			"class" : "hmo-button hmo-button-white hmo-button-small-10"
		}, {
			"label" : "확인",
			"class" : "hmo-button hmo-button-blue hmo-button-small-10",
			"callback" : function() {
				

				var data = {}, arr;
				
				arr = $('#form-change-jobtitles').serializeArrayAlt();
				$.each( arr, function() {
					data[this.name] = this.value;
				});
				
				$.ajax({
					url : settingUrl,
					async : false,
					type : "post",
					dataType : "json",
					contentType : "application/json",
					headers : {
						'Accept' : 'application/json',
						'Content-Type' : 'application/json'
					},
					data : JSON.stringify(data),
					success : function(data) {
						if( data.result == "fail" ){
				    		alert(data.message);
				    		return;
				    	}
				    	window.location.reload();
					},
					error : function(jqXHR,	textStatus,	errorThrown) {
						$.error("confirm_alter_time:$.ajax:" + errorThrown);
					}
				});
				return true;
			}
		} ], {
			"header" : "직책명을 변경해주세요",
			"embed" : true,
		});

	});
	
});
</script>
<!-- END:basecamp script -->
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
		<div class="main-container container-fluid">
			
			<!-- BEGIN:sidebar -->		
			<div class="sidebar" id="snn-sidebar">
			
				<!-- BEGIN:welcome-box -->		
				<c:import url="/WEB-INF/views/common/welcome-box.jsp">
				</c:import>
				<!-- END:welcome-box -->		
	
				<!-- BEGIN:nav-list -->
				<c:import url="/WEB-INF/views/common/nav-list.jsp">
					<c:param name="menuName">모두의 소식</c:param>
				</c:import>			
				<!-- END:nav-list -->
		
			</div>
			<!-- END:sidebar -->
			
			<!-- BEGIN:main-content -->		
			<div class="main-content">
			
				<!-- BEGIN:breadcrumbs&search -->	
				<c:set var="breadcrumbs" value="모두의 소식" scope="request"/>
				<c:set var="breadcrumbLinks" value="/" scope="request"/>
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
						<div class="rw-content-area">
							<div class="rw-pagelet-wrap rw-mtl">
								<div class="center">
								<form class="form-password form-normal" action="/user/alter_setting_password" method="POST">
	
									<%-- 비밀번호 필드 --%>
									<c:if test="${not empty param.scp }">
										<p class="text-success">비밀번호가 변경되었습니다.</p>
									</c:if>
									
									<spring:hasBindErrors name="passwordPost">
										<c:if test="${errors.hasFieldErrors('currentPassword') }">
											<c:set var="pwdErr" value="e" />
											<p class="text-warning">${errors.getFieldError('currentPassword').defaultMessage }</p>
										</c:if>
									</spring:hasBindErrors>
									<div class="form-group ${not empty pwdErr ? 'has-error' : '' }">
										<label class="control-label" for="form-password">현재 비밀번호를 입력하시오</label>
										<input id="form-password" type="password" name="currentPassword" class="form-control" placeholder="Current Password" />
									</div>
									
									
									<%-- 비밀번호 필드 --%>
									<spring:hasBindErrors name="passwordPost">
										<c:if test="${errors.hasFieldErrors('newPassword') }">
											<c:set var="pwdErr" value="e" />
											<p class="text-warning">${errors.getFieldError('newPassword').defaultMessage }</p>
										</c:if>
									</spring:hasBindErrors>
									<div class="form-group ${not empty pwdErr ? 'has-error' : '' }">
										<label class="control-label" for="form-password">새로운 비밀번호를 입력하시오</label>
										<input id="form-password" type="password" name="newPassword" class="form-control" placeholder="New Password" />
									</div>
									
									
									<%-- 비밀번호 재입력 필드 --%>
									<spring:hasBindErrors name="passwordPost">
										<c:if test="${errors.hasFieldErrors('confirmPassword') }">
											<c:set var="rePwdErr" value="e" />
											<p class="text-warning">${errors.getFieldError('confirmPassword').defaultMessage }</p>
										</c:if>
									</spring:hasBindErrors>
									<div class="form-group ${not empty rePwdErr ? 'has-error' : '' }">
										<label class="control-label" for="form-password-retype">비밀번호를 재 입력하시오</label>
										<input id="form-password-retype" type="password" name="confirmPassword" class="form-control" placeholder="Re-type Password" />
									</div>
									
									<hr />
									<button type="submit" class="hmo-button hmo-button-blue hmo-button-small-10 btn-block">비밀번호만 변경하기</button>
									
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

</body>
</html>