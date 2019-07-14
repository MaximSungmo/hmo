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
<style type="text/css">

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

.group-select { height: 25px; width: 120px;  }
.group-select-wrap	{ float:left; }
.remove-group-select	{ display:inline-block; font-size: 24px; }
</style>
<script>
var paramMap = new Array();
function replaceList(parsedLink){
	$('#changeable-pagelet').append('<div class="message-loading"><i class="fa fa-spin fa-spinner orange2 bigger-160"></i></div>');
	$.get("?" + parsedLink, function(data, status){
		$('#changeable-pagelet').find('.message-loading').remove();
		$("#changeable-pagelet").html(data);
	});
}
<c:if test="${not empty param.ordering}">
paramMap["ordering"] = "${param.ordering}";
</c:if>
<c:if test="${not empty param.q}">
paramMap["q"] = "${param.q}";
</c:if>
<c:if test="${not empty param.page}">
paramMap["page"] = "${param.page}";
</c:if>
paramMap["pl"] = true;
function ajax_group_remove_user(data){
	
	if( data.result == "fail" ){
		alert(data.message);
		return false; 
	}
	
	paramMap["page"] = 1;
	
	replaceList( getParsedLink(paramMap) );
}
$(function(){
	$("#join-user-to-group").onHMOClick(null, function(e){
		e.preventDefault();
		$last = $(".group-select").last();
		if( $last.val() == null || $last.val() == "" ){
			if( $last.data("sequence") == 0 ){
				alert("그룹이 선택되지 않았습니다.");
				return false; 
			}else{
				$last = $(".group-select[data-sequence='" + ( $last.data('sequence') - 1 ) + "']");
			}
		}
		var smallGroupId = $last.val(); 
		var data={
			"userId":"${user.id}",
			"smallGroupId":smallGroupId,
			"join":true
		};
		$.ajax({
			url:"/a/user/setsmallgroup",
			type:"post",
			dataType:"json",
			headers: {
				'Accept':'application/json',
				'Content-Type':'application/json'
				},
		    data: JSON.stringify(  data  ),
		    success: function( data ) {
		    	if( data.result == "fail" ){
		    		alert(data.message);
		    		return false;
		    	}
		    	paramMap["page"] = 1;
		    	
		    	replaceList( getParsedLink(paramMap) );
		    }
		});
	});
	$(document.body).onHMOClick(".remove-group-select", function(e){
		e.preventDefault();
		$(this).closest(".group-select-wrap").nextAll(".group-select-wrap").andSelf().remove();
	});
	$(document.body).on("change", ".group-select", function(e){
		$this = $(this);
		$this.closest(".group-select-wrap").nextAll(".group-select-wrap").remove();
		if($this.val() == null || $this.val() == ""){
			return false; 
		}
		$.ajax({
			url:"/a/get_group_children_idname",
			async: false,
			type: "GET",
			dataType: "json",
			contentType: 'application/json',
			headers: {
				"Accept": "application/json",
				"Content-Type": "application/json"
			},
			data:{
				"type":$this.data("type"),
				"parentId":$this.val()
			},
			success: function(data){
				if( data.result == "fail" ){
					alert(data.message);
					return false; 
				}
				if( data.data == null || data.data.length == 0 ){
					return;
				}
				var idNameList = data.data;
				var html = '<div class="group-select-wrap"><select class="group-select " data-type="3" data-sequence="' + ($this.data("sequence") + 1 ) + '" class="group-select">';
				html += "<option value=''></option>";
				for( var i = 0; i < idNameList.length; i++ ){
					html = html + '<option value="' + idNameList[i].id + '">' + idNameList[i].name + '</option>';
				}
				html = html + "</select>" + "<a href='#' class='remove-group-select'><i class='fa fa-times'></i></a></div>";
				$this.closest(".group-select-wrap").after(html);
			}
		})
		
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
		} else {
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
								
								<div class="rw-a-content z1" style="background-color: #FAFAFA; border: 1px solid #DDD; padding: 10px; ">
									<div class="group-select-wrap">
										<select id="select-group-0" data-sequence="0" data-type="3" class="group-select" style="">
											<option value=""></option>
											<c:forEach items="${firstSmallGroups }" var="smallGroup" varStatus="status">
												<option value="${smallGroup.id }">${smallGroup.name }</option>
											</c:forEach>
										</select>
									</div>
									
									<div class="rw-a-content" style="clear:both; ">
										<a href="#" id="join-user-to-group" class="hmo-button hmo-button-blue">${user.name } 님을 선택된 부서에 추가합니다.</a>							
									</div>
								</div>
								<div class="rw-a-content">
									<div class='tabbable'>
										<ul class='nav nav-tabs padding-12 tab-color-blue' id=''>
											<li>
												<a data-toggle="" href="/a/user/${user.id }/info">정보</a>
											</li>
											<li class='active'>
												<a data-toggle="" href="/a/user/${user.id }/department">부서</a>
											</li>
											<li>
												<a data-toggle="" href="/a/user/${user.id }/group?sgtype=5">소그룹</a>
											</li>
										</ul>
									</div>
									<div id="changeable-pagelet">
										<c:import url="/WEB-INF/views/pagelet/a/user/departments.jsp">
										</c:import>
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