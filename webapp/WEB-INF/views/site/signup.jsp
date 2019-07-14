<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="ko">
<head>

<c:import url="/WEB-INF/views/common/head-front.jsp">
	<c:param name="title">오피스 목록</c:param>
	<c:param name="bsUsed">YES</c:param>
	<c:param name="aceUsed">YES</c:param>
	<c:param name="jsUsed">YES</c:param>
</c:import>
</head>
<body style="overflow:auto">
<div class="rw-snn-fs">

	<!-- BEGIN:navbar -->
	<div class="navbar" id="navbar">
		<c:import url="/WEB-INF/views/common/navbar-front.jsp">
		</c:import>
	</div>
	<!-- END:navbar -->

	<div class="main-container container-fluid">
		<div class="main-content rw-signup-wizard-content">
			<div class="page-content login-page-content">
				<div id="site-signup-complete" style="display:none; ">
					<div class="row-fluid rw-signup-wizard-content-row-fluid" >
					<div class="span12">
						<div class="site-list-wrap">
							<div class="exception-img">
								<img src="/assets/sunny/2.0/img/hellomyoffice_logo.png" alt="hellomyoffice-logo">
							</div>
							<div class="site-list-signup-complete">					
							<h2>가입 요청이 완료되었습니다.</h2>
									<p>관리자의 승인 후, 가입시 입력하신 이메일에서<br>
									본인 인증을 마치고 <strong>'Hello my office'</strong>를 이용해 주세요.<br>
									감사합니다.							
							</p>
							<a href="/site" class="hmo-button hmo-button-large-10 hmo-button-white">이전 화면</a>
							<a href="/" class="hmo-button hmo-button-blue hmo-button-large-10">메인으로 가기</a>
							</div>
						</div>
						</div>
					</div>
				</div>
				<div id="site-signup-form-wrap" class="row-fluid rw-signup-wizard-content-row-fluid" >
					<div class="span12">
						<div>
							<div class="hmo-front-wrap-img">
								<img src="/assets/sunny/2.0/img/hellomyoffice_logo.png" alt="hellomyoffice-logo">
							</div>
						</div>
						<div class="site-list-signup-wrap">
						<form id="form-site-signup" action="/site/${site.id }/signup" method="post" rel="sync" onsubmit="return window.Event&amp;&amp;Event.__inlineSubmit&amp;&amp;Event.__inlineSubmit(this,event);">
							<div class="row-fluid">
								<div class="span12">							
											<div class="control-group">
												<label class="control-label" for="email">이메일</label>
												<div class="controls">
													<div class="">
														<label class="input-icon input-icon-right rw-input-wrap" for="email-text">
															<input class="span12" type="email" id="email-text" name="email" placeholder="새로운 이메일을 넣어주세요">
														</label>
													</div>
												</div>
											</div>
										
										<div class="control-group">
											<label class="control-label" for="name">이름</label>
											<div class="controls">
												<div class="row-fluid">
													<span class="span12 input-icon input-icon-right rw-input-wrap">
														<input class="span12" type="text" name="name" id="name" placeholder="이름을 넣어주세요" autocomplete="off">
													</span>
												</div>
											</div>
										</div>
										<%--
										<div class="control-group">
											<label class="control-label" for="password">비밀번호</label>
											<div class="controls">
												<div class="row-fluid">
													<span class="span12 input-icon input-icon-right rw-input-wrap">
														<input class="span12" type="password" name="password" id="password" placeholder="비밀번호를 넣어주세요">
													</span>
												</div>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label" for="re-password">비밀번호 재입력</label>
											<div class="controls">
												<div class="row-fluid">
													<span class="span12 input-icon input-icon-right rw-input-wrap">
														<input class="span12" type="password" name="passwordConfirm" id="passwordConfirm" placeholder="비밀번호를 재입력해주세요">
													</span>
												</div>
											</div>
										</div>
										 --%>
									<div class="control-group">
										<label class="control-label" for="requestMessage">가입요청 메시지</label>
										<div class="controls">
											<div class="row-fluid">
												<span class="span12 input-icon input-icon-right rw-input-wrap">
													<textarea class="span12" name="requestMessage" id="requestMessage" placeholder="가입요청 메시지"></textarea>
												</span>
											</div>
										</div>
									</div>
									
								</div>
							</div>
							<div style="border-top:1px solid #E5E5E5;">
							<div style="border-top:1px solid #fff;">
							<div style="padding-top:20px;">
							<a href="/site" class="hmo-button hmo-button-large-7 hmo-button-white">이전 화면</a>
							<input type="hidden" name="site.id" value="${site.id }" />
							<button class="hmo-button hmo-button-blue hmo-button-large-7">가입 요청</button>
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



<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/jquery.validate.js"></script>

<script>
$("#form-site-signup").validate({
	errorElement: 'span',
	errorClass: 'help-inline',
	focusInvalid: false,
	onfocusout:function (element) {
		$(element).valid();
    },
	rules: {
		"email": {
			required: false,
			email: true,
			remote: {
				url: "/user/valid/email",
				type: "get",
				dataType:"json",
		    	data: {
		            email: function() { return $("#email-text").val(); }
		        },
		        dataFilter: function( response ) {
	            	var data = jQuery.parseJSON( response );
					return ( data.result == "success" );
		        }
			}
		},
		"password": {
			required: true
		},
		"passwordConfirm": {
			required: true,
			equalTo: "#password"
		}		
	},
	messages: {
		"email": {
			email: "올바르지 않은 형식의 이메일입니다.",
			remote: "이미 사용중인 이메일입니다."
		},
		"password": {
			required: "필수입력 항목입니다."
		},
		"passwordConfirm": {
			required: "필수입력 항목입니다.",
			equalTo: "비밀번호가 일치하지 않습니다."
		}
		
	},
	invalidHandler: function (event, validator) {	
		$('.alert-error', $('.login-form')).show();
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
			if(controls.find(':checkbox,:radio').length > 1) controls.append(error);
			else error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
		} else if(element.is('.select2')) {
			error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
		} else if(element.is('.chosen-select')) {
			error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
		} else {
			error.insertAfter( element.parent().eq(0) );
		}
	},
	submitHandler: function (form) {
	}
});	


</script>

<script>


Event.__inlineSubmit = function( form, event ){
	
	$.Event( event ).preventDefault();

	if( !$("#form-site-signup").valid() ){
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
	    	
	    	$("#site-signup-form-wrap").hide();
	    	$("#site-signup-complete").show();
	    },
		error:function(jqXHR,textStatus,errorThrown){
			$.log("error:Event.__inlineSubmit:"+errorThrown);
		}
	});

	return false;
}


</script>	
<%--
<script>

Event.__inlineLogin = function( form, event ){
	$.Event( event ).preventDefault();
	
	var 
	$form=$(form),
	rel=$form.attr("rel"),
	url=$form.attr("action"),
	type=$form.attr("method");
	
	var data = $form.serializeObject();
	
	$.ajax({
		url:url,
		type:"POST",
		headers: {
			"Accept": "application/json"
		},
	    data: data,
	    success:function(data){
	    	if( data.result == "fail"){
	    		alert( data.message );
	    		return false;
	    	}
	    	window.location.href="/site/${site.id}/signup";
	    },
		error:function(jqXHR,textStatus,errorThrown){
			$.log("error:Event.__inlineSubmit:"+errorThrown);
		}
	});

	return false;
}




function show_login(){
	$("#site-prev-choice").hide();
	$("#site-login-form-wrap").show();
	return false; 
}
function show_signup(){
	$("#site-prev-choice").hide();
	$("#site-signup-form-wrap").show();
	return false; 
}

</script>
 --%>
</body>
</html>




<%-- 

<div class="container">
	<div class="jumbotron">
		<h1>권한없음</h1>
		<p class="lead">해당 페이지에 대한 권한이 없습니다.</p>
		<p>
			<a href="/" class="btn btn-success btn-lg">메인으로 가기</a> 
		</p>
	</div>
</div>
<!-- /.container -->


--%>