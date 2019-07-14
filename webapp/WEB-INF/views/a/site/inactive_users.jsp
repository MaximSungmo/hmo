<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">

<head>
<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">${empty smallGroup ? '노트' : smallGroup.name }</c:param>
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
					<c:param name="menuName">가입신청관리</c:param>
				</c:import>			
				<!-- END:nav-list -->
		
			</div>
			<!-- END:sidebar -->		
	
			<!-- BEGIN:main-content -->			
			<div class="main-content z1">
			
				<!-- BEGIN:breadcrumbs&search -->	
				<c:set var="breadcrumbs" value="관리자콘솔,가입신청관리" />
				<c:set var="breadcrumbLinks" value="/a,/a/site/inactive_users" />
				<div class="breadcrumbs" id="breadcrumbs">
					<c:import url="/WEB-INF/views/common/breadcrumbs.jsp">
						<c:param name="breadcrumbs">${breadcrumbs }</c:param>	
						<c:param name="breadcrumbLinks">${breadcrumbLinks }</c:param>	
					</c:import>			
				</div>
				<!-- END:breadcrumbs&search -->	
				
				<!-- BEGIN:page-content -->						
				<div class="page-content z1">
					<div class="rw-content-area-wrap invite-admin-manage-wrap">
						<div class="rw-pagelet-blank"></div>
						<div class="rw-pagelet-wrap rw-mtl">
							<div class="lighter blue">
								<ul class="nav nav-tabs">
									<li class="active"><a href="/a/site/inactive_users" >미승인목록</a></li>
									<li class=""><a href="/a/site/sent_users">사용자미인증목록</a></li>
								</ul>
							</div>
						</div>
						<div class="rw-pagelet-wrap">
							<div id="pagelet-changeable-list">
								<c:import url="/WEB-INF/views/pagelet/a/site/inactive_users.jsp"></c:import>
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

<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/jquery.validate.js"></script>

<script>

function deny_inactive_user(data){
	if( data.result == "fail"){
		MessageBox(data.message, data.message, MB_ERROR);
		return false;
	}
	window.location.href="/a/site/inactive_users";
}


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
	$("#btn-delete-user").onHMOClick(null, function() {
		bootbox.confirm("<strong>정말로 삭제 하시겠습니까?</strong>", function( result ) {
			if( !result ) {
				return;
			}
			
			var chks = $("#a-table-user-list input[type='checkbox']:checked:enabled")
			var ids = [];
			chks.each( function( index, chk ) {
				var id = $( chk ).attr( "data-userid" );
				id && ids.push( id );
			});
			
			$.log ( ids );
		});		
	});
	
	//validation for new user
	$( "#form-new-user" ).validate({
		errorElement: 'span',
		errorClass: 'help-inline',
		focusInvalid: false,
		onfocusout:function (element) {
			$(element).valid();
	    },
		rules: {
			"userName" : {
	            minlength: 2,
				required: true
			},
			"userEmail": {
				required: true,
				email: true,
				remote: {
					url: "/user/valid/email",
					type: "get",
					dataType:"json",
			    	data: {
			            email: function() { return $("#companyEmail").val(); }
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
			}			
		},
		messages: {
			"userName": {
	            minlength: "2자 이상 입력해주세요",
				required: " "
			},
			"userEmail": {
				required: " ",
				email: " ",
				remote: " "
			},
			"userPassword": {
				required: " "
			},
			"userPasswordConfirm": {
				required: " ",
				equalTo: " "
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
		}
	});		
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