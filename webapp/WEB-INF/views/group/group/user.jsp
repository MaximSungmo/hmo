<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">${smallGroup.name }</c:param>
	<c:param name="bsUsed">NO</c:param>
	<c:param name="aceUsed">NO</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>
<style type="text/css">
.rw-content-area-wrap	{background-color: white;}
</style>

</head>
<body class="scroll-y">

<c:choose>
	<c:when test="${smallGroup.type == 3}">
		<c:set var="groupTypeName" value="부서" />
	</c:when>
	<c:when test="${smallGroup.type == 4}">
		<c:set var="groupTypeName" value="프로젝트" />
	</c:when>
	<c:when test="${smallGroup.type == 5}">
		<c:set var="groupTypeName" value="소그룹" />
	</c:when>

</c:choose>

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
				<c:import url="/WEB-INF/views/common/nav-list.jsp">
					<c:param name="menuName">${groupTypeName }</c:param>
				</c:import>			
				<!-- END:nav-list -->
			</div>
			<!-- END:sidebar -->
			<!-- BEGIN:main-content -->		
			<div class="main-content z1">
			
						
			
				<!-- BEGIN:page-content -->						
				<div class="page-content z1">
					<div class="rw-content-area-wrap">
							<!-- BEGIN:page-header -->
							<c:import url="/WEB-INF/views/common/page-header.jsp">
								<c:param name="pageName">group</c:param>
								<c:param name="tabName">user</c:param>
							</c:import>					
							<!-- END:page-header -->	
							<div class="rw-content-area rw-content-area-top-02">
								<div class="rw-pagelet-wrap rw-mtl rw-mobile-pagelet">
										<div class="ui-rw-pagelet-wrap rw-mobile-pgelet-wrap">
												<div class="_h6m"></div>
													<ul class="ui-rw-pagelet-list">
														<li class="_z6m pagelet-row">
															<table class="ui-grid _k3x">
																<tbody>
																	<tr class="ui-rw-pagelet-list-tr-button"><td style="padding-right:10px;"><span class="ui-rw-pagelet-list-button"><button onclick="return pop_invite_users();" class="hmo-button hmo-button-blue" style="float:right;">초대하기</button></span></td></tr>
																</tbody>
															</table>
														</li>
														<li class="_z6m pagelet-row" style="border-top:none;">
															<table class="ui-grid _k3x">
																<tbody>		
																	<tr>
																		<td class="pagelet-row-menu-wrap" style="padding-left:10px;">
																			<ul>
																				<li class="${empty inactive ? 'active' : ''}"><a href="/group/${smallGroup.id}/joined">구성원</a></li>
																				<li><a href="/group/${smallGroup.id}/access"">접근 허용</a></li>
																				<li class="${not empty inactive ? 'active' : '' }"><a href="/group/${smallGroup.id}/inactive">미승인 사용자</a></li>										
																			</ul>																		
																		</td>
																		<td style="padding-right:12px;">
																			<li class="pull-right position-relative">
																				<a href="#" aria-owns="ordering-popup-menus" aria-haspopup="true" rel="toggle" class="dropdown-toggle">
																					정렬 &nbsp;
																					<i class="fa fa-caret-down bigger-125"></i>
																				</a>
																					<ul id="ordering-popup-menus" data-popup-group="global" class="ui-toggle-flyout dropdown-menu dropdown-lighter pull-right dropdown-100">
																						<li>
																							<a href="#" class="group-users-more-detail" data-name="ordering" data-value="createDate" data-desc="true">
																								<i class="fa fa-caret-up green"></i>
																								가입일 
																							</a>
																						</li>
																						<li>
																							<a href="#" class="group-users-more-detail" data-name="ordering" data-value="createDate" data-desc="false">
																								<i class="fa fa-caret-down green"></i>
																								가입일 
																							</a>
																						</li>
																						<li>
																							<a href="#" class="group-users-more-detail" data-name="ordering" data-value="userAlias.name" data-desc="true">
																								<i class="fa fa-caret-up green"></i>
																								이름순
																							</a>
																						</li>
																						<li>
																							<a href="#" class="group-users-more-detail" data-name="ordering" data-value="userAlias.name" data-desc="false">
																								<i class="fa fa-caret-down green"></i>
																								이름순
																							</a>
																						</li>
																					</ul>
																				</li> 
																		</td>
																	</tr>
																</tbody>
															</table>
														</li>
														<div id="group-user-list">
																<c:import url="/WEB-INF/views/pagelet/group/user.jsp"></c:import>
														</div>
													</ul>
										</div>
								</div>
							</div>
								<div class="rw-pagelet-blank"></div>
								<div class="rw-pagelet-blank"></div>
								<div class="rw-pagelet-blank"></div>
								<div class="rw-pagelet-blank"></div>
								<div class="rw-pagelet-blank"></div>
								<div class="rw-pagelet-blank"></div>
								<div class="rw-pagelet-blank"></div>
								<div class="rw-pagelet-blank"></div>
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
<div id="res-dialog-invite-users" style="display:none">
	<div class="rw-dialog-wrap " id="res-dialog-invite-users-content ">
		
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
	
	$("#user-" + $(this).data("uid") + "-popup-menus").find(".group-join-admin").removeClass("hidden-elem");
	$(this).addClass("hidden-elem");

	$("#group-list-user-name-" + $(this).data("uid")).find(".iamadmin").addClass("hidden-elem");
	
}
function ajax_group_join_admin(data){
	if( data.result == "fail")
		alert( data.message );
	
	$("#user-" + $(this).data("uid") + "-popup-menus").find(".group-remove-admin").removeClass("hidden-elem");
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
 function appendList(parsedLink){
		//$("#next-stream-item").remove();
		$("#next-stream-btn").addClass("hidden-elem");
		$("#next-stream-loading").removeClass("hidden-elem");
		$.get("?" + parsedLink, function(data, status){
			$("#next-stream-item").remove();
			$("#group-user-list").append(data);
		});
	}
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
	
	$(document.body).onHMOClick( "#next-stream-btn", function(e){
		e.preventDefault();
		var pageNum = groupUsersParamMap["page"];
		
		if( typeof(pageNum) === 'undefined' ){
			pageNum = 1;
		}
		
		groupUsersParamMap["page"] = parseInt(pageNum) + 1;
		appendList( getParsedLink(groupUsersParamMap) );
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
	
	$(document.body).onHMOClick( "#invite-users-candi-list .btn-danger", function(e){
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
				
				_$wrapper = $(".invite-users-wrap");
				_$loading = _$wrapper.siblings(".loading");
				
				_$wrapper.hide();
				_$loading.show();
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

						location.href = "/group/${smallGroup.id}/inactive";
				    },
					error:function(jqXHR,textStatus,errorThrown){
						$.log("error:Event.__inlineSubmit:"+errorThrown);
					}
				});
					
				return false; 
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