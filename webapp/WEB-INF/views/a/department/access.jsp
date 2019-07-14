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
												<li><a href="/a/department/${smallGroup.id}/users">구성원</a></li>
												<li class="active"><a href="/a/department/${smallGroup.id}/access"">접근 허용</a></li>
												<li class="pull-right position-relative">
													<a href="#" data-toggle="dropdown" class="dropdown-toggle">
														정렬 &nbsp;
														<i class="fa fa-caret-down bigger-125"></i>
													</a>
									
													<ul class="dropdown-menu dropdown-lighter pull-right dropdown-100">
														<li>
															<a href="#" class="group-access-more-detail" data-name="ordering" data-value="createDate" data-desc="true">
																<i class="fa fa-caret-up green"></i>
																추가 순서 
															</a>
															<a href="#" class="group-access-more-detail" data-name="ordering" data-value="createDate" data-desc="false">
																<i class="fa fa-caret-down green"></i>
																추가 순서
															</a>
															<a href="#" class="group-access-more-detail" data-name="ordering" data-value="smallGroupAlias.type" data-desc="true">
																그룹 -> 사람 순서
															</a>
															<a href="#" class="group-access-more-detail" data-name="ordering" data-value="smallGroupAlias.type" data-desc="false">
																사람 -> 그룹 순서
															</a>
														</li>
													</ul>
												</li>
											</ul>
											<%-- 그룹에 속한 사용자이면서 그룹의 관리자일때만 보임 --%>
											<button onclick="return pop_add_permission();" class="btn btn-info hmo-button-khaki btn-minier pull-right" style="top: 5px;">추가하기</button>
										</div>
									</div>
									<div class="rw-pagelet-blank"></div>
									<div class="rw-pagelet-wrap" style="padding: 0; ">
										<div id="group-access-list" class="group-list-wrap" style="margin: 0; ">
											<c:import url="/WEB-INF/views/pagelet/a/department/access.jsp"></c:import>
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


<div id="res-dialog-add-permission" style="display:none">
	<div class="rw-dialog-wrap " id="res-dialog-add-permission-content ">
		<div class="hide loading">
			<i class="fa fa-spin fa-spinner"></i>
		</div>
		<div class="permission-scroll-wrap permission-wrap" >
			<div >
				<ul id="permission-candi-list" class="unstyled" >
				</ul>
			</div>
			<form id="permission-search-form" action="/pagelet/permission" method="GET" class="form-search clearfix" style="margin-bottom: 5px; clear:both;">
				<div>
					<input type="text" class="search-query" style="width: 40%;">
					<button 
						class="hmo-button-search hmo-button hmo-button-blue">
						검색 
					</button>
				</div>
			</form>
			
			<div data-toggle="buttons-radio" class="btn-group">
				<button data-name="type" data-value="3" class="permission-more-detail btn btn-info btn-small active" type="button">부서</button>
				<button data-name="type" data-value="4" class="permission-more-detail btn btn-info btn-small" type="button">프로젝트</button>
				<button data-name="type" data-value="5" class="permission-more-detail btn btn-info btn-small" type="button">그룹</button>
				<button data-name="type" data-value="" class="permission-more-detail btn btn-info btn-small" type="button">사람</button>
			</div>
			
			<div id="permission-search-list">
			</div>
		</div>
	</div>
</div>


<script>
/*
 * mod_misc
 */


function ajax_group_update_permission(data){
	if( data.result == "fail" ){
		alert(data.message);
		return false;
	}
	
	
	var siblingClass = $(this).data("sibling-class"); 
	
	if( typeof(siblingClass) != "undefined" ){
		$(this).siblings(siblingClass).removeClass("hidden-elem");	
	}
	
	$(this).addClass("hidden-elem");
	
	$($(this).data("toggle-dom")).toggleClass("hidden-elem");
	
}

function ajax_group_remove_access(data){
	if( data.result == "fail" ){
		alert(data.message);
		return false;
	}
	
	
	$(this).parents(".group-list-content").fadeOut( "slow" );
}
 
</script>


<script>
/*
 * mod_access_list
 */

 var groupAccessParamMap = new Array();
 
 
	<c:if test="${not empty param.ordering}">
	groupAccessParamMap["ordering"] = "${param.ordering}";
	</c:if>

	groupAccessParamMap["desc"] = true;
	<c:if test="${not empty param.desc}">
	groupAccessParamMap["desc"] = "${param.desc}";
	</c:if>
	
	<c:if test="${not empty param.q}">
	groupAccessParamMap["q"] = "${param.q}";
	</c:if>
	
 
	<c:if test="${not empty param.page}">
	groupAccessParamMap["page"] = "${param.page}";
	</c:if>
	
 groupAccessParamMap["pl"] = true;
 
 function replaceList(parsedLink){
		$("#group-access-list").html("<table><tr><td class='center' colspan='100%'><i class='fa fa-spin fa-spinner' /></td></tr></table>");
		$.get("?" + parsedLink, function(data, status){
			$("#group-access-list").html(data);
		});
	}
 
 
 $(function(){
	$(".main-search-form").submit(function(e){
		e.preventDefault();
		var query = $(this).find("input[type=text]").val();
		
		groupAccessParamMap["q"] = query;
		groupAccessParamMap["page"] = 1;
		
		replaceList(getParsedLink(groupAccessParamMap));
	});
	
	$(document.body).onHMOClick(null, ".group-access-more-detail", function(e) {
		e.preventDefault();

		var name = $(this).data("name");
		if( name != "page"){
			groupAccessParamMap["page"] = 1;
		}
		
		if( name == "ordering"){
			groupAccessParamMap["desc"] = $(this).data("desc");
		}
		
		groupAccessParamMap[name] = $(this).data("value");
		
		replaceList( getParsedLink(groupAccessParamMap) );
	});
	
	$(document.body).onHMOClick(null, "#next-stream-btn", function(e){
		e.preventDefault();
		var pageNum = groupAccessParamMap["page"];
		
		if( typeof(pageNum) === 'undefined' ){
			pageNum = 1;
		}
		
		groupAccessParamMap["page"] = parseInt(pageNum) + 1;
		
		appendList( getParsedLink(groupAccessParamMap) );
		
	});
	
});


/*
 * end of mod_user_list
 */
</script>


<script>
	/*
	
		mod_add_permission
	*/
		var permissionParamMap = new Array();
		
		permissionParamMap["type"] = 3;
		
		function permissionReplaceList(parsedLink){
			$("#permission-search-list").html("<table><tr><td class='center' colspan='100%'><i class='fa fa-spin fa-spinner' /></td></tr></table>");
			$.get("/pagelet/permission?" + parsedLink, function(data, status){
				$("#permission-search-list").html(data);
			});
		}
		
		function remove_candi_permission(_this){
			$this = $(_this);
			
			$this.parents(".candi-row").remove();
			return false; 
		}
		
		function add_candi_permission(_this){
			
			$this = $(_this);
			
			var map = $(_this).data("map");
			
			$candiList = $("#permission-candi-list");
			
			if( $candiList.find("[data-id=" + map.id + "][data-type=" + map.type + "]").length > 0 ){
				$.log("이미 존재함");
				return;
			}
			
			var chkChildrenHtml = "&nbsp;";
			if( map.type != null && map.type != "undefined" && map.type > 2  ){
				chkChildrenHtml = '' + 
				'<input id="permission-t-' + map.type + '-i-' + map.id + '" class="ace permission-chk-children" type="checkbox" data-name="children">' +
				'<label class="lbl permission-children-label" for="permission-t-' + map.type + '-i-' + map.id + '">' +
				'하위 포함</label>' +
				''; 
			}
			
			$("#permission-candi-list").append(
					'<li class="candi-row " data-id="' + map.id + '" data-type="' + map.type + '">' + 
					'<a href="#" ><i class=""></i>&nbsp;<span class="candi-name" >' + map.name + 
					'</span>' +
					'<span class="btn btn-danger btn-minier btn-permission-remove">' +
						'<i class="fa fa-trash-o"></i>' +
					'</span></a>' +
				'</li>'
			);
		}
		var Permission = {
				
			getArrayData:function(){
				
		
				var permissions = new Array();
				
				var _$permissionRows = $("#permission-candi-list .candi-row");
				
				if( _$permissionRows.length > 0 ){
					
					_$permissionRows.each(function(){
						var permEach = {};
						var _$eachRow = $(this);
						permEach["id"] = _$eachRow.data("id");
						
						var type = _$eachRow.data("type");
						
						if( type != "undefined" && type != null  ){
							permEach["smallGroupType"] = type;
						}
						
						permissions.push( permEach );
					});
				}
				return permissions;
			}
				
		}
		function pop_add_permission(){
			
			$.log(permissionParamMap["type"]);
			
			if( typeof(alreadyShownPermBootbox) == "undefined" || alreadyShownPermBootbox == null ){
				
				var permissionAjax = $.ajax({
					url: "/pagelet/permission?" + getParsedLink(permissionParamMap),
					type: "GET"
				});
			}
			
			bootbox.dialog( "res-dialog-add-permission", [
				{
					"label" : "확인",
					"class" : "hmo-button hmo-button-blue hmo-button-small-10",
					"callback": function() {
						
						var permissionArray = Permission.getArrayData();
						
						$.ajax({
							url:"/group/access?sgid=${smallGroup.id}",
							type:"POST",
							dataType:"json",
							contentType: 'application/json',
							headers: {
								"Accept": "application/json",
								"Content-Type": "application/json"
							},
						    data: JSON.stringify(permissionArray),
						    success:function(data){
						    	if( data.result == "fail" )
						    		alert(data.message);
						    	
// 						    	bootbox.hideAll();
// 								_$wrapper.show();
// 								_$loading.hide();

						    	//location.href = "/group/${smallGroup.id}/access";
						    	
						    	groupAccessParamMap["page"] = 1;
						    	replaceList(getParsedLink(groupAccessParamMap));
						    },
							error:function(jqXHR,textStatus,errorThrown){
								$.log("error:Event.__inlineSubmit:"+errorThrown);
							}
						});
						return true;
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
				if( typeof(alreadyShownPermBootbox) == "undefined" || alreadyShownPermBootbox == null ){
				
					$.when(permissionAjax)
					.done(function(data){
						$("#permission-search-list").html( data );
						alreadyShownPermBootbox = true;
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
				$("#permission-toggle-wrap").addClass("open");
				
			},
			"beforeShown" : function() {
				if( typeof(alreadyShownPermBootbox) == "undefined" || alreadyShownPermBootbox == null ){
						$("#permission-search-list").html("<tr><td class='center' colspan='100%'><i class='fa fa-spin fa-spinner' /></td></tr>");	
				}
				// 다이럴로그가 올라왔을 때 뒤 body 가 같이 스크롤되는 현상이 있어서 바디의 스크롤을 없앰.
				// 추후 안이사님 방식으로 height 변경하는 것으로 변경할 것.
				$("html").css("position", "fixed");
				$("html").css("overflow-y", "scroll");
				$("body.scroll-y").css("overflow-y", "hidden");
				$("#permission-candi-list").empty();
				$("#permission-candi-list").append($("#permission-list-ul .candi-row"));
			}
			});
			
		}
		
		$(function(){
			
			$(document.body).onHMOClick(null, '.btn-permission, .default-candi-row', function(e){
				e.preventDefault();
				e.stopPropagation();
				
				if( $(e.target).hasClass("btn-permission") ){
					if( typeof($(this).data("toggle")) == "undefined" )
						return false;	
					
					$(this).toggleClass("active");
					return;
				}
				
				if( $(this).hasClass("active") ){
					return;
				}
			
				$(".default-candi-row").parent().removeClass("active");
				$(this).parent().addClass("active");
			
				$("#toggle-permission-detail .text").text($(this).find(".text").text());
		
			});
			
			$(document.body).onHMOClick(null, ".btn-permission-remove", function(e){
				$(this).parents(".candi-row").remove();
				return false;
			});
			
			
			
			$(document.body).onHMOClick(null, ".permission-children-wrap", function(e){
				e.stopPropagation();
		// 		var _$checkbox = $(e.target).closest(".permission-chk-children");
		// 		var isChecked = _$checkbox.attr("checked");
		// 		$.log(isChecked);
		// 		_$checkbox.attr("checked", "checked");
			});
		
			$(document.body).on("change",".permission-chk-children:checkbox", function(e){
				e.preventDefault();
				e.stopPropagation();
				$.log("change");
				var isChecked = $(this).is(":checked");
				$(this).attr("checked", isChecked);
			});
			
			$("#permission-search-form").submit(function(e){
				e.preventDefault();
				var query = $(this).find("input[type=text]").val();
				
				permissionParamMap["q"] = query;
				permissionParamMap["page"] = 1;
				
				permissionReplaceList(getParsedLink(permissionParamMap));
				
			});
			
			$(document.body).onHMOClick(null, ".permission-more-detail", function(e) {
				e.preventDefault();
				
				var elemType = $(this).data("type");
				var name = $(this).data("name");
				
				if( name != "page"){
					permissionParamMap["page"] = 1;
				}
				
				if( name == "qt"){
					if( $("#permission-search-form input[type=text]").val() == "" ){
						permissionParamMap[name] = $(this).data("value");
						return;
					}
				}
				
				if (elemType == "multiple") {
					var arr = $.map($($(this).data("selector")), function(
							elem, index) {
						return +$(elem).data("value");
					});
					permissionParamMap[name] = arr;
				} else {
					//e.preventDefault();
					permissionParamMap[name] = $(this).data("value");
				}
		
				
				
				permissionReplaceList( getParsedLink(permissionParamMap) );
			});
		});
		
		</script>

</body>
</html>