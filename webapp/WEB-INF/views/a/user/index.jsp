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
			<div class="main-content z1">
			
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
				<div class="page-content z1">
					<div class="rw-content-area-wrap user-admin-manage-wrap admin-content-area-wrap">
							<div class="rw-pagelet-wrap rw-mtl admin-full-size">
							
								<div class="lighter blue">
									<div class="nav">
										<span class="hmo-button-group-wrap">											
											<span class="hmo-button-group-text hmo-button-group-item hmo-button-group-middle-9 l-ft hmo-button-white hmo-button-group firstItem">
											<a href="#collapseOne" data-parent="#detail-searcch"
												data-toggle="collapse" class="accordion-toggle collapsed">
												상세검색 </a>
											</span>
											<span class="hmo-button-group-text hmo-button-group-item hmo-button-group-middle-9 hmo-button-white hmo-button-group"><a class="" id="admin-btn-new-user">새사용자</a></span>
										</span>
									</div>
								</div>
								<div id="detail-select">
									<div class="accordion-body collapse" id="collapseOne"
										style="height: 0px;">
										<div class="accordion-inner">
											<div class="z1">
												<small class="pull-left admin-manage-title">검색기준 : </small><br />
												<label class="pull-left">
													<input type="radio" data-type="radio" name="qt" class="ace more-detail" value="0" checked data-name="qt" data-value="0">
													<span class="lbl">이름</span>	
												</label>
												<label class="pull-left">
													<input type="radio" data-type="radio" name="qt" class="ace more-detail" value="1" data-name="qt" data-value="1">
													<span class="lbl">이메일</span>	
												</label>
												<label class="pull-left">
													<input type="radio" data-type="radio" name="qt" class="ace more-detail" value="2" data-name="qt" data-value="2">
													<span class="lbl">직책</span>	
												</label>
											</div>
											<div class="z1">
												<small  class="pull-left admin-manage-title">필터 적용 :</small> <br />
												<label class="pull-left"> <input type="checkbox"
													class="ace chk-status more-detail" data-type="multiple"
													data-selector=".chk-status:checked" data-name="status[]"
													data-value="0"
													${not empty STATUS_WORKING ? 'checked=checked' : ''}><span
													class="lbl">근무중</span>
												</label> 
												<label class="pull-left"> <input type="checkbox"
													class="ace chk-status more-detail" data-type="multiple"
													data-selector=".chk-status:checked" data-name="status[]"
													data-value="1"
													${not empty STATUS_VACATION ? 'checked=checked' : ''}><span
													class="lbl">휴가중</span>
												</label > 
												<label class="pull-left"> <input type="checkbox"
													class="ace checked chk-status more-detail"
													data-type="multiple" data-selector=".chk-status:checked"
													data-name="status[]" data-value="2"
													${not empty STATUS_LEAVE ? 'checked' : ''}><span
													class="lbl">퇴사</span>
												</label>
											</div>
											<div class="z1">
												<small class="pull-left admin-manage-title">정렬 : </small><br />
												<label class="pull-left">
													<input type="radio" data-type="radio" name="ordering" class="ace more-detail" data-name="ordering" data-value="" value="" checked>
													<span class="lbl">정렬없음</span>	
												</label>
												<label class="pull-left">
													<input type="radio" data-type="radio" name="ordering" class="ace more-detail" data-name="ordering" data-value="${param.ordering != 'name' ? 'name' : ''}" value="name" >
													<span class="lbl">이름순</span>	
												</label>
											</div>
											<div class="z1">
												<small class="pull-left admin-manage-title">관리자 : </small><br />
												<label class="pull-left">
													<input type="radio" data-type="radio" name="onlyAdmin" class="ace more-detail" data-name="onlyAdmin" data-value="false" value="false" ${empty param.onlyAdmin || param.onlyAdmin == false ? 'checked' : ''}>
													<span class="lbl">모든 사용자</span>	
												</label>
												<label class="pull-left">
													<input type="radio" data-type="radio" name="onlyAdmin" class="ace more-detail" data-name="onlyAdmin" data-value="true" value="true" >
													<span class="lbl">관리자</span>	
												</label>
											</div>
										</div>
									</div>
								</div>
								
								<div id="pagelet-changeable-list">
									<c:import url="/WEB-INF/views/pagelet/a/user/users.jsp"></c:import>
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

<%--
<!-- BEGIN:resource of dialog for creating user -->
<div id="res-dialog-new-user" style="display:none">
	<div class="rw-dialog-wrap rw-form-wrap  pop-new-user-wrap">
		<form class="form-vertical rw-form" id="form-new-user">
			<div class="row-fluid">
				<div class="span12">
				
					<div class="control-group">
						<label class="control-label" for="name">이름</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon input-icon-right rw-input-wrap">
									<input class="span12" type="text" name="name" id="name" placeholder="이름" autocomplete=off>
									<i class="icon-remove-sign"></i>
									<i class="icon-ok-sign"></i>
								</span>
							</div>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="email">이메일 주소</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon input-icon-right rw-input-wrap">
									<input class="span12" type="email" name="email" id="email" placeholder="초대할 이메일@emailaddress.com" autocomplete=off>
									<i class="icon-remove-sign"></i>
									<i class="icon-ok-sign"></i>
								</span>
							</div>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="job1">직책1</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon input-icon-right rw-input-wrap">
									<input class="span12" type="text" name="jobTitle1" id="job1" placeholder="직책1">
								</span>
							</div>
						</div>
					</div>				
					<div class="control-group">
						<label class="control-label" for="job2">직책2</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon input-icon-right rw-input-wrap">
									<input class="span12" type="text" name="jobTitle2" id="job2" placeholder="직책2">
								</span>
							</div>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="job3">직책3</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon input-icon-right rw-input-wrap">
									<input class="span12" type="text" name="jobTitle3" id="job3" placeholder="직책3">
								</span>
							</div>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="inviteMessage">초대 메시지</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon input-icon-right rw-input-wrap">
									<input class="span12" type="text" name="inviteMessage" id="inviteMessage" placeholder="설명">
								</span>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
<!-- END:resource of dialog for creating user -->
 --%>
<!-- BEGIN:resource of dialog for updating user -->
<div id='res-dialog-setting-division' style='display:none'>
	<div class='rw-dialog-wrap pop-setting-division-wrap'>
		<div class='row-fluid'>
			<div class='span12'>
				<div class='tabbable'>
					<ul class='nav nav-tabs padding-12 tab-color-blue background-blue' id=''>
						<li class='active'>
							<a data-toggle='tab' href='#tab-info'>정보</a>
						</li>
						<li>
							<a data-toggle='tab' href='#tab-division'>부서</a>
						</li>
						<li>
							<a data-toggle='tab' href='#tab-group'>소그룹</a>
						</li>
						<li>
							<a data-toggle='tab' href='#tab-ugroup'>사용자그룹</a>
						</li>
					</ul>
					<div class='tab-content'>
						<div id='tab-info' class='tab-pane in active'>
							<p>기본정보 관리</p>
						</div>

						<div id='tab-division' class='tab-pane'>
							<p>포함된 부서 관리</p>
						</div>
						<div id='tab-project' class='tab-pane'>
							<p>포함된 과제 관리</p>
						</div>
						<div id='tab-group' class='tab-pane'>
							<p>포함된 소그룹 관리</p>
						</div>
						<div id='tab-ugroup' class='tab-pane'>
							<p>사용자 그룹 관리</p>
						</div>
					</div>
				</div>
			</div>						
		</div>							
	</div>
</div>
<!-- END:resource of dialog for updating user -->
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/jquery.validate.js"></script>

<script>

$( function() {
	// 
// 	$('table th input:checkbox').on('click' , function(){
// 		var that = this;
// 		$(this).
// 		closest('table').
// 		find('tr > td:first-child input:checkbox').
// 		each(function(){
// 			this.checked = that.checked;
// 			$(this).closest('tr').toggleClass('selected');
// 		});
// 	});
	
	//
	$("#admin-btn-new-user").onHMOClick(null, function() {
		bootbox.dialog( "res-dialog-new-user", [{
			"label" : "취소",
			"class" : "hmo-button hmo-button-white hmo-button-small-10"
		},{
			"label" : "확인",
			"class" : "hmo-button hmo-button-blue hmo-button-small-10",
			"callback": function() {
				var result = $( "#form-new-user" ).valid();
				if( !result ) {
					return false;
				}
				//
				var data = {}, arr;
				
				arr = $('#form-new-user').serializeArrayAlt();
				$.each( arr, function() {
					data[this.name] = this.value;
				});
				
				$.ajax({
					url:"/a/user/invite",
					type:"post",
					dataType:"json",
					headers: {
						'Accept':'application/json',
						'Content-Type':'application/json'
						},
				    data: JSON.stringify( deepen( data ) ),
				    success: function( data ) {
				    	if( data.result == "fail" ){
				    		//Dialog.showMessage( "오류가 발생되었습니다.", data.message, MB_INFORMATION);
				    		alert( data.message );
				    		return;
				    	}
						MessageBox( "초대되었습니다.", "초대되었습니다. 사용자가 이메일을 클릭하면 가입이 완료됩니다.", MB_INFORMATION );

				    	//window.location.reload();
				    },
					error:function( jqXHR,textStatus,errorThrown ) {
						$.log( "error:Event.__inlineSubmit:" + errorThrown );
					}
				});
				return true;
			}
		}],{
			"header" : "사용자 초대",	
			"embed" : true,
			"onInit" : function(){
// 				new iScroll(".pop-new-user-wrap").slimscroll({
// 					height : "320px",
// 					railVisible : false,
// 					alwaysVisible : false
// 				});
			}
		});
	});
	//	
// 	$("#btn-delete-user").onHMOClick(null, function() {
// 		bootbox.confirm("<strong>정말로 삭제 하시겠습니까?</strong>", function( result ) {
// 			if( !result ) {
// 				return;
// 			}
			
// 			var chks = $("#a-table-user-list input[type='checkbox']:checked:enabled")
// 			var ids = [];
// 			chks.each( function( index, chk ) {
// 				var id = $( chk ).attr( "data-userid" );
// 				id && ids.push( id );
// 			});
			
// 			$.log ( ids );
// 		});		
// 	});
	
// 	//validation for new user
// 	$( "#form-new-user" ).validate({
// 		errorElement: 'span',
// 		errorClass: 'help-inline',
// 		focusInvalid: false,
// 		onfocusout:function (element) {
// 			$(element).valid();
// 	    },
// 		rules: {
// 			"email": {
// 				required: true,
// 				email: true
// 				,
// 				remote: {
// 					url: "/user/valid/email",
// 					type: "get",
// 					dataType:"json",
// 			    	data: {
// 			            email: function() { return $("#email").val(); }
// 			        },
// 			        dataFilter: function( response ) {
// 		            	var data = jQuery.parseJSON( response );
// 						return ( data.result == "success" );
// 			        }
// 				}				
// 			}		
// 		},
// 		messages: {
// 			"email": {
// 				required: "필수입력 항목입니다.",
// 				email: "올바르지 않은 형식의 이메일입니다.",
// 				remote: "이미 가입된 이메일입니다."
// 			}
// 		},
// 		invalidHandler: function (event, validator) {	
// 		},
// 		highlight: function (e) {
// 			$(e).closest('.control-group').removeClass('info').addClass('error');
// 		},
// 		success: function (e) {
// 			$(e).closest('.control-group').removeClass('error').addClass('info');
// 			$(e).remove();
// 		},
// 		errorPlacement: function ( error, element ) {
// 			if(element.is(':checkbox') || element.is(':radio')) {
// 				var controls = element.closest('.controls');
// 				if(controls.find(':checkbox,:radio').length > 1) {
// 					controls.append(error);
// 				} else {
// 					error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
// 				}
// 			} else if(element.is('.select2')) {
// 				error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
// 			} else if(element.is('.chosen-select')) {
// 				error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
// 			} else {
// 				error.insertAfter( element.parent().eq(0) );
// 			}
// 		},
// 		submitHandler: function (form) {
// 		}
// 	});		
});

</script>



<script>

	var paramMap = new Array();

	function getParsedLink(map){
		var parsedLink = "";
		
		for ( var name in paramMap) {
			parsedLink = insertParam(parsedLink, name, paramMap[name]);
		}
		
		return parsedLink;
	}
	
	function replaceList(parsedLink){
		$("#pagelet-changeable-list").html("<tr><td class='center' colspan='100%'><i class='icon-spin icon-spinner' /></td></tr>");
		$.get("/a/user?" + parsedLink, function(data, status){
			$("#pagelet-changeable-list").html(data);
		});
		
	}
	
	<c:if test="${not empty param.range}">
	paramMap["range"] = "${param.range}";
	</c:if>

	<c:if test="${not empty param.ordering}">
	paramMap["ordering"] = "${param.ordering}";
	</c:if>

	<c:if test="${not empty param.status}">
	paramMap["status"] = "${param.status}";
	</c:if>

	<c:if test="${not empty param.q}">
	paramMap["q"] = "${param.q}";
	</c:if>

	<c:if test="${not empty param.qt}">
	paramMap["qt"] = "${param.qt}";
	</c:if>

	<c:if test="${not empty param.page}">
	paramMap["page"] = "${param.page}";
	</c:if>

	<c:if test="${not empty param.onlyAdmin}">
	paramMap["onlyAdmin"] = "${param.onlyAdmin}";
	</c:if>
	

	paramMap["pl"] = true;
	$(function() {
		
		
		$(".main-search-form").submit(function(e){
			e.preventDefault();
			var query = $(this).find("input[type=text]").val();
			
			paramMap["q"] = query;
			paramMap["page"] = 1;
			
			replaceList(getParsedLink(paramMap));
			
		});
		
		$(document.body).on("click", ".more-detail", function(e){
			var elemType = $(this).data("type");
			var name = $(this).data("name"); 
			var value =  $(this).data("value");
			if(  name == "qt"){
				if( $(".main-search-form input[type=text]").val() == "" ){
					paramMap[name] = value;
					return;
				}
			}
			
			if (elemType == "multiple") {
				var arr = $.map($($(this).data("selector")), function(elem, index) {
					return +$(elem).data("value");
				});
				paramMap[name] = arr;
			} else if( elemType == "radio"){
				paramMap[name] = value;
			}else{
				e.preventDefault();
				paramMap[name] = value;
			}
				
			if( name != "page" ){
				paramMap["page"] = 1;
			}
			
			replaceList( getParsedLink(paramMap) );
				
				
		});
	});
</script>
</body>
</html>