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

<!-- BEGIN:sunny extra libraries -->

<script src="/assets/sunny/2.0/js/uncompressed/ejs.js"></script>

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
				<c:set var="breadcrumbs" value="관리자콘솔,부서관리,'${smallGroup.name}'-구성원" />
				<c:set var="breadcrumbLinks" value="/a,/a/department,/a/department/${smallGroup.id}/users" />
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
											<li>
												<a data-toggle="" href="/a/department/${smallGroup.id }/info">정보</a>
											</li>
											<li class='active'>
												<a data-toggle="" href="/a/department/${smallGroup.id }/users">구성원</a>
											</li>
										</ul>
									</div>							
									<div class='rw-tab-content'>
										
										<div class="lighter blue z1">
											<ul class="nav nav-pills">
												<li class="${empty inactive ? 'active' : ''}"><a href="/a/department/${smallGroup.id}/users">구성원</a></li>
												<li><a href="/a/department/${smallGroup.id}/access"">접근 허용</a></li>
												<li class="pull-right position-relative">
												<a href="#" data-toggle="dropdown" class="dropdown-toggle">
														정렬 &nbsp;
														<i class="fa fa-caret-down bigger-125"></i>
													</a>
									
													<ul class="dropdown-menu dropdown-lighter pull-right dropdown-100">
														<li>
															<a href="#" class="group-users-more-detail" data-name="ordering" data-value="createDate" data-desc="true">
																<i class="fa fa-caret-up green"></i>
																가입일 
															</a>
															<a href="#" class="group-users-more-detail" data-name="ordering" data-value="createDate" data-desc="false">
																<i class="fa fa-caret-down green"></i>
																가입일 
															</a>
															<a href="#" class="group-users-more-detail" data-name="ordering" data-value="userAlias.name" data-desc="true">
																<i class="fa fa-caret-up green"></i>
																이름순
															</a>
															<a href="#" class="group-users-more-detail" data-name="ordering" data-value="userAlias.name" data-desc="false">
																<i class="fa fa-caret-down green"></i>
																이름순
															</a>
														</li>
													</ul>
												
												</li> 
											</ul>
											<button onclick="return pop_invite_users();" class="hmo-button hmo-button-blue hmo-button-small-10 pull-right" style="top: 0px;">초대하기</button>
										</div>
									</div>
									<div class="rw-pagelet-blank"></div>
									<div class="rw-pagelet-wrap" style="padding: 0; ">
										<div id="group-user-list" class="group-list-wrap" style="margin: 0; ">
											<c:import url="/WEB-INF/views/pagelet/a/department/user.jsp"></c:import>
										</div>
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
<!-- BEGIN:resource of dialog for creating user -->
<div id="res-dialog-new-user" style="display:none">
	<div class="rw-dialog-wrap rw-form-wrap" id="res-dialog-new-user-content">

	</div>
</div>
<!-- END:resource of dialog for creating user -->	

<div id="res-dialog-invite-users" style="display:none">
	<div class="rw-dialog-wrap " id="res-dialog-invite-users-content ">
		
		<div class="hide loading">
			<i class="fa fa-spin fa-spinner"></i>
		</div> 
		<div class="invite-users-scroll-wrap invite-users-wrap" >
			<div >
				<div id="invite-users-candi-list" class="unstyled" >
				</div>
			</div>
			<form id="invite-users-search-form" action="/pagelet/invite_users" method="GET" class="form-search clearfix" style="margin-bottom: 5px; clear:both;">
				<div>
					<input type="text" class="search-query" style="width: 40%;">
					<button 
						class="btn btn-purple btn-small">
						Search <i class="fa fa-search"></i>
					</button>
				</div>
			</form>
			<div id="invite-users-search-list">
			</div>
		</div>
	</div>
</div>
	<script>
/* mod_misc */
function ajax_group_remove_user(data){
	if( data.result == "fail")
		alert( data.message );

	$(this).parents(".group-list-content").fadeOut( "slow" );
}
function ajax_group_accept_user(data){
	if( data.result == "fail")
		alert( data.message );

	$(this).parents(".group-list-content").fadeOut( "slow" );
}
function ajax_group_remove_admin(data){
	if( data.result == "fail")
		alert( data.message );
	
	$(this).siblings(".group-join-admin").removeClass("hidden-elem");
	$(this).addClass("hidden-elem");

	$("#group-list-user-name-" + $(this).data("uid")).find(".iamadmin").addClass("hidden-elem");
	
}
function ajax_group_join_admin(data){
	if( data.result == "fail")
		alert( data.message );
	
	$(this).siblings(".group-remove-admin").removeClass("hidden-elem");
	$(this).addClass("hidden-elem");
	
	$("#group-list-user-name-" + $(this).data("uid")).find(".iamadmin").removeClass("hidden-elem");
}
</script>
<script>
/*
 * mod_user_list
 */
 var groupUsersParamMap = new Array();
	<c:if test="${not empty param.ordering}">
	groupUsersParamMap["ordering"] = "${param.ordering}";
	</c:if>
	groupUsersParamMap["desc"] = true;
	<c:if test="${not empty param.desc}">
	groupUsersParamMap["desc"] = "${param.desc}";
	</c:if>
	<c:if test="${not empty param.q}">
	groupUsersParamMap["q"] = "${param.q}";
	</c:if>
	<c:if test="${not empty param.page}">
	groupUsersParamMap["page"] = "${param.page}";
	</c:if>
 groupUsersParamMap["pl"] = true;

 function replaceList(parsedLink){
		$("#group-users-search-list").html("<table><tr><td class='center' colspan='100%'><i class='fa fa-spin fa-spinner' /></td></tr></table>");
		$.get("?" + parsedLink, function(data, status){
			$("#group-user-list").html(data);
		});
	}
 $(function(){
	$(".main-search-form").submit(function(e){
		e.preventDefault();
		var query = $(this).find("input[type=text]").val();
		
		groupUsersParamMap["q"] = query;
		groupUsersParamMap["page"] = 1;
		
		replaceList(getParsedLink(groupUsersParamMap));
	});
	
	$(document.body).onHMOClick(".group-users-more-detail", function(e) {
		e.preventDefault();

		var name = $(this).data("name");
		if( name != "page"){
			groupUsersParamMap["page"] = 1;
		}
		if( name == "ordering"){
			groupUsersParamMap["desc"] = $(this).data("desc");
		}
		groupUsersParamMap[name] = $(this).data("value");
		
		replaceList( getParsedLink(groupUsersParamMap) );
	});
	
});
/*
 * end of mod_user_list
 */
</script>
<script>
/*
 * mod_group_invite_users
 */
var inviteUsersParamMap = new Array();

inviteUsersParamMap["sgid"] = "${smallGroup.id}";

function inviteUserReplaceList(parsedLink){
	$("#invite-users-search-list").html("<table><tr><td class='center' colspan='100%'><i class='fa fa-spin fa-spinner' /></td></tr></table>");
	$.get("/pagelet/invite_users?" + parsedLink, function(data, status){
		$("#invite-users-search-list").html(data);
	});
}

function add_candi_invite_users(_this){

	$this = $(_this);
	
	var map = $(_this).data("map");
	
	$candiList = $("#invite-users-candi-list");
	
	if( $candiList.find("[data-id=" + map.id + "]").length > 0 ){
		$.log("이미 존재함");
		return;
	}
	
	$candiList.append(
		'<span class="invite-users-candi-row " data-id="' + map.id + '"' + 
			'<a href="#" ><i class=""></i>&nbsp;<span class="candi-name" >' + map.name + 
			'</span>&nbsp; '+
			'<span class="btn btn-danger btn-minier btn-candi-row-remove">' +
				'<i class="fa fa-trash-o"></i>' +
			'</span></a>' +
		'</span>'
	);
}
$(function(){
	$("#invite-users-search-form").submit(function(e){
		e.preventDefault();
		var query = $(this).find("input[type=text]").val();
		
		inviteUsersParamMap["q"] = query;
		inviteUsersParamMap["page"] = 1;
		
		inviteUserReplaceList(getParsedLink(inviteUsersParamMap));
	});
	
	$(document.body).onHMOClick(".invite-users-more-detail", function(e) {
		e.preventDefault();

		var name = $(this).data("name");
		if( name != "page"){
			inviteUsersParamMap["page"] = 1;
		}
		
		inviteUsersParamMap[name] = $(this).data("value");
		
		inviteUserReplaceList( getParsedLink(inviteUsersParamMap) );
	});
	
	$(document.body).onHMOClick("#invite-users-candi-list .btn-danger", function(e){
		$(this).parents(".invite-users-candi-row").remove();
		return false;
	});
});
function pop_invite_users(){
	
	$.log(inviteUsersParamMap["type"]);
	
	if( typeof(alreadyShownInviteBox) == "undefined" || alreadyShownInviteBox == null ){
		
		var inviteUsersAjax = $.ajax({
			url: "/pagelet/invite_users?" + getParsedLink(inviteUsersParamMap),
			type: "GET"
		});
	}
	
	bootbox.dialog( "res-dialog-invite-users", [
		{
			"label" : "확인",
			"class" : "hmo-button hmo-button-blue hmo-button-small-10",
			"callback": function() {
				
				var _$candiRows = $("#invite-users-candi-list .invite-users-candi-row");
				
				if( _$candiRows.length < 1 ){
					return true; 
				}
				
				//return true;
				
				var inviteUsers = new Array();
				
				_$candiRows.each(function(){
					inviteUsers.push( $(this).data("id"));					
				});
				
				$.ajax({
					url:"/group/invite_users?sgid=${smallGroup.id}",
					type:"POST",
					dataType:"json",
					contentType: 'application/json',
					headers: {
						"Accept": "application/json",
						"Content-Type": "application/json"
					},
				    data: JSON.stringify(inviteUsers),
				    success:function(data){
				    	if( data.result == "fail" )
				    		alert(data.message);
				    	

						groupUsersParamMap["page"] = 1;
						
						replaceList(getParsedLink(groupUsersParamMap));
				    },
					error:function(jqXHR,textStatus,errorThrown){
						$.log("error:Event.__inlineSubmit:"+errorThrown);
					}
				});
			}
		},
		{
			"label" : "취소",
			"class" : "hmo-button hmo-button-white hmo-button-small-10"
		}
    ],{
	"embed" : true,
	"onInit" : function() {
		$(this).find(".modal-body").css("overflow-y", "scroll");
		if( typeof(alreadyShownInviteBox) == "undefined" || alreadyShownInviteBox == null ){
		
			$.when(inviteUsersAjax)
			.done(function(data){
				$("#invite-users-search-list").html( data );
				alreadyShownInviteBox = true;
			})
			.fail(function(data){
				alert("오류발생");
			});
			
		}else{
		}
	},
	"onFinalize" : function() {
		// 다이럴로그가 올라왔을 때 뒤 body 가 같이 스크롤되는 현상이 있어서 바디의 스크롤을 없앰.
		// 추후 안이사님 방식으로 height 변경하는 것으로 변경할 것.
		$("html").css("overflow", "auto");
		$("html").css("position", "relative");
		$("body.scroll-y").css("overflow-y", "auto");
		
	},
	"beforeShown" : function() {
		if( typeof(alreadyShownInviteBox) == "undefined" || alreadyShownInviteBox == null ){
				$("#invite-users-search-list").html("<tr><td class='center' colspan='100%'><i class='fa fa-spin fa-spinner' /></td></tr>");	
		}
		// 다이럴로그가 올라왔을 때 뒤 body 가 같이 스크롤되는 현상이 있어서 바디의 스크롤을 없앰.
		// 추후 안이사님 방식으로 height 변경하는 것으로 변경할 것.
		$("html").css("position", "fixed");
		$("html").css("overflow-y", "scroll");
		$("body.scroll-y").css("overflow-y", "hidden");
		$("#invite-users-candi-list").empty();
	}
	});
	
}
</script>
</body>
</html>