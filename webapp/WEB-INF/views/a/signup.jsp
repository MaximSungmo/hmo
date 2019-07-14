<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/WEB-INF/views/common/head-front.jsp">
	<c:param name="title">Hello My Office와 함께하세요</c:param>
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
				<div class="row-fluid rw-signup-wizard-content-row-fluid">
					<div class="span12">
						<div class="widget-box rw-no-border">

							<div class="widget-body">
								<div class="widget-main widget-main-wrap">
									<div class="row-fluid">
										<div id="fuelux-wizard" class="row-fluid" data-target="#step-container">
											<ul class="wizard-steps">
												<li data-target="#step1" class="active">
													<strong class="step">1</strong>
												</li>
												<li data-target="#step2">
													<strong class="step">2</strong>
												</li>
												<li class="landing-site-signup-admin-step-3" data-target="#step3">
													<strong class="step">3</strong>
												</li>
											</ul>
										</div>
										<div class="rw-step-container-wrap position-relative" >											
											<div class="step-content" id="step-container">
												<div class="step-pane active" id="step1">
													<div class="row-fluid">
														<div class="span7 login-02-wrap">
															<div class="rw-step-title step-left-col">
																<h3 class="step-left-col-title login-step-left-col-title"><span class="mobile-hiede-text">포춘지가 선정하는 100대 그룹중 이미 80%가 넘는 기업들이 그들만의 SNS를 앞다투어 도입하고 있습니다.<br>우리나라 실정에 맞는 기업의 현실을 생각하며 만들었습니다.<br>이제 <strong></span>'Hello my office'</strong>를 통해 활기찬 기업 문화를 만들어 보세요.</h3>
																<p class="step-left-col-img">
																	
															<img src="/assets/sunny/2.0/img/login-01.png" alt="communication"> 
																</p>
															</div>
														</div>
														<div class="span5 rw-row-signup-form step-1-rw-row-signup-form">
														
															<div class="rw-form-wrap step-1-rw-form-wrap">	
																<form class="form-vertical rw-form" id="form-signup-basic">
																	
																	<h3 class="black rw-fieldset-title rw-fieldset-title-02">회사정보</h3>
																	<div class="control-group">
																		<label class="control-label" for="companyName">회사 또는 조직명</label>
																		<div class="controls">
																			<div class="row-fluid">
																				<span class="span12 input-icon input-icon-right rw-input-wrap">
																					<input class="span12" type="text" name="siteCompanyName" id="companyName" placeholder="이름" <c:if test="${not empty param.cname }">value="${param.cname }"</c:if> autocomplete="off">
																					<i class="icon-remove-sign"></i>
																					<i class="icon-ok-sign"></i>																				
																				</span>	
																			</div>
																		</div>
																	</div>
																	<div class="control-group">
																		<label class="control-label" for="companyDomain">홈페이지</label>
																		<div class="controls">
																			<div class="row-fluid">
																				<span class="span12 input-icon input-icon-right rw-input-wrap">
																					<input class="span12" type="text" name="siteHomepage" id="companyHomepage" placeholder="http://www.mycompany.com">
																				</span>
																			</div>	
																		</div>
																	</div>
																	<div class="control-group">
																		<label class="control-label" for="companyPhone">전화</label>
																		<div class="controls">
																			<div class="input-prepend">
																				<span class="add-on">
																					<i class="fa fa-phone"></i>
																				</span>																			
																				<span class="input-icon input-icon-right rw-input-wrap">
																					<input class="input-medium input-mask-phone" type="text" name="siteCompanyPhone" id="companyPhone" placeholder="">
																				</span>
																			</div>	
																		</div>
																	</div>
																	<div class="control-group">
																		<label class="control-label" for="employeeSize">직원수</label>
																		<div class="controls">
																			<div class="rw-input-wrap clearfix">
																				<select name="siteEmployeeSize" id="employeeSize" >
																					<option value="">선택해 주세요</option>
																					<option value="1">직원 1명</option>
																					<option value="5">직원 2-5명</option>
																					<option value="10">직원 6-10명</option>
																					<option value="20">직원 11-20명</option>
																					<option value="50">직원 21-50명</option>
																					<option value="100">직원 51-100명</option>
																					<option value="1000">직원 101-1000명</option>
																					<option value="5000">직원 1001-5000명</option>
																					<option value="10000">직원 5000명 이상</option>
																				</select>
																			</div>
																		</div>
																	</div>
																	<div class="control-group">
																		<label class="control-label" for="companyRegion">지역</label>
																		<div class="controls">
																			<div class="rw-input-wrap clearfix">
																				<select name="siteCompanyRegion" id="companyRegion" >
																					<option value="">선택해 주세요</option>
																					<option value="1">서울</option>
																					<option value="2">경기</option>
																					<option value="3">강원</option>
																					<option value="4">경남</option>
																					<option value="5">경북</option>
																					<option value="6">광주</option>
																					<option value="7">대구</option>
																					<option value="8">대전</option>
																					<option value="9">부산</option>
																					<option value="10">세종</option>
																					<option value="11">울산</option>
																					<option value="12">인천</option>
																					<option value="13">전남</option>
																					<option value="14">전북</option>
																					<option value="15">제주</option>
																					<option value="16">충남</option>
																					<option value="17">충북</option>
																					<option value="18">해외</option>
																				</select>
																			</div>
																		</div>
																	</div>																														
																</form>
															</div>													
														</div>
													</div>
												</div>												
												<div class="step-pane" id="step2">
													<div class="row-fluid">
													<div class="span7 login-02-wrap">
														<div class="rw-step-title login-02-rw-step-title">
															<h3 class="login-step-left-col-title"><strong>'Hello my office'</strong>를 이용하시면 회사의 업무 효율이 극대화 됩니다.<br><br>
															
															<span class="mobile-hiede-text"><strong>주요특징</strong>:막강한 권한관리, 실시간 타임라인, 동영상 메시징, 푸시형 공지사항, CCTV 모니터링, 갱차트에 의한 프로세스 관리, 정부 과제 관리, 간단장부관리, 모바일 웹 & 앱 완벽 지원 등</span>
															
															
															</h3>
															<p class="rw-step-content">
																	
															<img src="/assets/sunny/2.0/img/login-02.png" alt="communication">
															</p>
														</div>
													</div>
													<div class="span5 rw-row-signup-form manager-form">
														<div class="rw-form-wrap">	
															<h3 class="rw-fieldset-main-title login-rw-fieldset-main-title">Hello my office" 관리자 계정 생성<br><br></h3>
															<h3 class="black rw-fieldset-title">관리자 계정 만들기</h3>
															<form class="form-vertical" id="form-signup-account">
															
																<div class="control-group">
																	<label class="control-label" for="name">이름</label>
																	<div class="controls">
																		<div class="row-fluid">
																			<span class="span12 input-icon input-icon-right rw-input-wrap">
																				<input class="span12" type="text" name="userName" id="name" placeholder="이름">
																				<i class="icon-remove-sign"></i>
																				<i class="icon-ok-sign"></i>
																			</span>
																		</div>
																	</div>
																</div>

																<div class="control-group">
																	<label class="control-label" for="email">로그인 이메일</label>
																	<div class="controls">
																		<div class="row-fluid">
																			<span class="span12 input-icon input-icon-right rw-input-wrap">
																				<input class="span12" type="email" name="userEmail" id="email" placeholder="you@emailaddress.com">
																				<i class="icon-remove-sign"></i>
																				<i class="icon-ok-sign"></i>
																			</span>
																		</div>		
																	</div>
																</div>
																<div class="control-group">
																	<label class="control-label" for="password">비밀번호 만들기</label>
																	<div class="controls">
																		<div class="row-fluid">
																			<span class="span12 input-icon input-icon-right rw-input-wrap">
																				<input class="span12" type="password" name="userPassword" id="password">
																				<i class="icon-remove-sign"></i>
																				<i class="icon-ok-sign"></i>
																			</span>
																		</div>
																	</div>
																</div>
																<div class="control-group">
																	<label class="control-label" for="passwordConfirm">비밀번호 재입력</label>
																	<div class="controls">
																		<div class="row-fluid">
																			<span class="span12 input-icon input-icon-right rw-input-wrap">
																				<input class="span12" type="password" name="userPasswordConfirm" id="passwordConfirm" >
																				<i class="icon-remove-sign"></i>
																				<i class="icon-ok-sign"></i>
																			</span>
																		</div>
																	</div>
																</div>
																<!--h3 class="black rw-fieldset-title">보안문자를 입력하세요.</h3-->
																<div class="control-group">
																	<label>
																		<input name="userAgreeEmail" type="checkbox" class="ace" checked="checked" value="true">
																		<span class="lbl">
																			<span>업데이트, 공지, 특별 행사 및 시장 조사와 관련된 이메일을 수신합니다.</span>
																		</span>
																	</label>
																</div>
																<div class="control-group">
																	<label class="rw-chckbx">
																		<input name="tosAgree" id="tosAgree" type="checkbox" class="ace" data-serializable="false">
																		<span class="lbl">
																			<span><a href="/cs/policies" target="__blank">서비스 약관</a> 및 <a href="/cs/privacy" target="__blank">개인정보 취급방침</a>을 읽었으며, 이에 동의합니다.</span>
																		</span>
																	</label>
																</div>															
															</form>
														</div>													
													</div>
													</div>
												</div>
																											
												<div class="step-pane" id="step3">
													<div class="center rw-progressing" id="step3-status">
														<div class="rw-step-status rw-status-progressing">
															<p class="step3-img-wrap">																	
															<img src="/assets/sunny/2.0/img/login-03.png" alt="communication"> 
															</p>

															<%-- <h3>서비스 관리 계정을 생성하고 있습니다.</h3>
															<i class="icon-spinner icon-spin orange bigger-125"></i>--%>
															<i class="fa fa-spinner fa-spin orange bigger-125"></i>
														
														</div>
														<div class="rw-step-status rw-status-finished">
															<h3>서비스 관리 계정을 생성 했습니다.</h3>
															<p>
																이메일 주소 <strong id="email-confirm">you@emailaddress.com</strong> 으로 <br>
																서비스 및 관리자 계정 활성화에 필요한 인증 절차를 확인해 주십시오. 
															</p>	
															<a class="hmo-button hmo-button-blue hmo-button-large-10" href="/">
																<%-- <i class="icon-ok bigger-150"></i>--%>
																<span>확인</span>
															</a>
														</div>
													</div>
												</div>
											</div>
											<div class="rw-form-buttons login-rw-form-buttons">													
												<div class="wizard-actions">
													<button class="hmo-button hmo-button-white hmo-button-large-10 btn-prev" disabled="disabled" id="btn-prev">
														<%-- <i class="icon-arrow-left"></i>--%>
														<span class="landing-button">이전</span>
													</button>
													<button class="hmo-button hmo-button-blue hmo-button-large-10 btn-next" id="btn-next">
														<span class="landing-button">확인</span>
														
													</button>
												</div>
											</div>
										</div>
									</div>
								</div>	
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>	



<!-- BEGIN:basic js-libs -->
<script src="/assets/sunny/2.0/js/uncompressed/ejs.js"></script>
<script src="/assets/sunny/2.0/js/uncompressed/moment-with-langs.js"></script>
<!-- END:basic js-libs   -->

<!-- BEGIN:basic bs script -->
<script src="/assets/sunny/2.0/js/uncompressed/bootbox.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/bootstrap.js"></script>
<!-- END:bs script   -->

<!-- BEGIN:ace scripts -->
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-elements.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace.js"></script>
<!-- END:ace scripts -->

<!-- BEGIN:additional plugin -->
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/fuelux/fuelux.wizard.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/jquery.validate.js"></script>
<script src="/assets/sunny/2.0/js/uncompressed/jquery.mask.js"></script>
<!-- END:additional plugin -->

<script>

$( function(){
	$( "#fuelux-wizard" ).
	ace_wizard().
	on( "change" , function( e, info ) {
		if( info.direction == "previous" ) {
			return true;
		}
		
		var resultValid = false;
		if( info.step == 1 ) {
			resultValid = $('#form-signup-basic').valid();
// 			resultValid = true;
		} else if( info.step == 2 ) {
			resultValid = $('#form-signup-account').valid();
// 			resultValid = true;
			if( resultValid ) {
				var data = {}, arr;
				
				arr = $('#form-signup-basic').serializeArrayAlt();
				$.each( arr, function() {
                    data[this.name] = this.value;
                });

				arr = $('#form-signup-account').serializeArrayAlt();
				$.each( arr, function() {
					data[this.name] = this.value;
				});
				
				
				$("#btn-prev").hide();
				$("#btn-next").hide();					
				
				setTimeout( function(){
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
					    	
					    	$.log( JSON.stringify( data ) );
					    	
					    	if( data.result == "fail" ){
					    		alert(data.message);
								$("#btn-prev").show();
								$("#btn-next").show();
					    	}else{
								$("#fuelux-wizard").find( "[data-target='#step3']" ).removeClass( "active" ).addClass( "complete" );
								//$("#step3-status").removeClass( "rw-progressing" ).addClass( "rw-finished" );
								//$("#email-confirm").text(data.data.email);
								window.location.href = "/a/activate?id=" + data.data.id + "&key=" + data.data.value;
					    	}
					    },
						error:function( jqXHR,textStatus,errorThrown ) {
							$.log( "error:Event.__inlineSubmit:" + errorThrown );
						}
					});
					
// 					$("#fuelux-wizard").find( "[data-target='#step3']" ).removeClass( "active" ).addClass( "complete" );
// 					$("#step3-status").removeClass( "rw-progressing" ).addClass( "rw-finished" );
					
				}, 1000);
			}
		}

		return resultValid;
	}).
	on( "finished", function( e ) {
	}).
	on( "stepclick", function( e ){
	});
	
// 	jQuery.validator.addMethod( "companyPhone", function( value, element ) {
// 		console.log( "***" + value.replace(/_/g,'') + "***" );
// 		return this.optional(element) || /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$/.test( value.replace(/_/g,'') );
// 	}, "전화번호가 비어 있거나 올바르지 않은 전화번호입니다." );
	
	$('#form-signup-basic').validate({
		errorElement: 'span',
		errorClass: 'help-inline',
		focusInvalid: false,
		onfocusout:function (element) {
			$(element).valid();
	    },
		rules: {
			"userName": {
				required: true
			},
			"siteCompanyName": {
				required: true
			},
			"siteCompanyPhone": {
				required: true/*,
				companyPhone: 'required' */
			},
			"siteEmployeeSize": {
				required: true
			},
			"siteCompanyRegion": {
				required: true
			}
		},
		messages: {
			"userName": {
				required: "필수입력 항목입니다."
			},
			"siteCompanyName": {
				required: "필수입력 항목입니다."
			},
			"siteCompanyPhone": {
				required: "필수입력 항목입니다."
			},
			"siteEmployeeSize": {
				required: "필수입력 항목입니다."
			},
			"siteCompanyRegion": {
				required: "필수입력 항목입니다."
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
	
	$( "#form-signup-account" ).validate({
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
				remote: {
					url: "/user/valid/email",
					type: "get",
					dataType:"json",
			    	data: {
			            email: function() { return $("#email").val(); }
			        },
			        dataFilter: function( response ) {
		            	var data = jQuery.parseJSON( response );
						return ( data.result == "success" );
			        }
				}
			},
			"userPassword": {
				required: true
			},
			"userPasswordConfirm": {
				required: true,
				equalTo: "#password"
			},
			tosAgree: "required"			
		},
		messages: {
			"userEmail": {
				required: "필수입력 항목입니다.",
				email: "올바르지 않은 형식의 이메일입니다.",
				remote: "이미 사용중인 이메일입니다."
			},
			"userPassword": {
				required: "필수입력 항목입니다."
			},
			"userPasswordConfirm": {
				required: "필수입력 항목입니다.",
				equalTo: "비밀번호가 일치하지 않습니다."
			},
			tosAgree: "서비스 약관 및 개인정보 취급방침을 읽고 동의해야 합니다."
			
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
	
	// masked
// 	$('.input-mask-phone').mask('099-0999-0999', {translation: {'Z': {pattern: /[0-9]/, optional: true}}});
	//$('.input-mask-phone').mask('092-9999-9999');
});
</script>
</body>
</html>