<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">

<head>

<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">사이트</c:param>
	<c:param name="bsUsed">YES</c:param>
	<c:param name="aceUsed">YES</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>

 <script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
 <script>
 
function change_ordering( siteMenuId, beforePrevId, targetPrevId ){
	
	var data = {
			"siteId" : "${sunny.site.id}",
			"id" : siteMenuId,
			"beforePrevId" : beforePrevId,
			"targetPrevId" : targetPrevId
	}
	
	
	$.ajax({
		url:"/a/menu",
		type:"PUT",
		dataType:"json",
		headers: {
			'Accept':'application/json',
			'Content-Type':'application/json'
		},
		data: JSON.stringify( data ),
		success:function(data){
			if( data.result == "fail" ){
				alert(data.message);
				return false; 
			}
		}
	})
} 


function apply_checkbox(_this){
	var data = {};
	var $this = $(_this);
	var isChecked = $this.is(":checked"); 
	data["smid"] = $this.closest("li").data("smid");
	if( isChecked ){
		data["visible"] = true;
	}else{
		data["visible"] = false;
	}
	params = data;
	
	$.ajax({
		url:"/a/menu/visible",
		type : 'GET',
		dataType : 'json',
		headers : {
			'Accept' : 'application/json',
			'Content-Type' : 'application/json'
		},
		data:params,
		success:function(data){
			if( data.result == "fail" ){
				alert(data.message);
				return false; 
			}
				
		}
	})
}

$(function(){
	 
	 var beforePrevId = null;
	 $("#sortable").sortable({
		start:function( event, ui ){
			if( ui.item.prev().length ){
				beforePrevId = ui.item.prev().data("smid");
			}else{
				beforePrevId  = null;
			}
		},
		update:function( event, ui ){
			var targetPrevId = null;

			if( ui.item.prev().length ){
				targetPrevId = ui.item.prev().data("smid"); 
			}
			change_ordering( ui.item.data("smid"), beforePrevId, targetPrevId );
		}		 
	 });
	 
	 $( "#sortable" ).disableSelection();
	 
	 $(".btn-up").onHMOClick(null, function(e){
		e.preventDefault();

		beforePrevId = null;
		
		
		var $thisRow = $(this).closest("li");
		var $prevRow = $thisRow.prev();
		if( $prevRow.length > 0 ){
			$thisRow.slideUp(function(){
				$thisRow.insertBefore($prevRow).slideDown();	
			});
			beforePrevId = $prevRow.data("smid");	
		}else{
			return false; 
		}
		
		var targetPrevId = null;
		
		if( $prevRow.prev().length ){
			targetPrevId = $prevRow.prev().data("smid"); 
		}
		
		change_ordering( $thisRow.data("smid"), beforePrevId, targetPrevId );
	 });
	 
	 $(".btn-down").onHMOClick(null, function(e){
			
	 	e.preventDefault();

		beforePrevId = null;

		var targetPrevId = null;
		
		
		var $thisRow = $(this).closest("li");
		
		
		var $prevRow = $thisRow.prev();
		if( $prevRow.length > 0 ){

			beforePrevId = $prevRow.data("smid");	
		}
		
		var $nextRow = $thisRow.next();
		if( $nextRow.length > 0 ){
			$thisRow.slideUp(function(){
				$thisRow.insertAfter($nextRow).slideDown();	
			});
			targetPrevId = $nextRow.data("smid"); 
		}else{
			return false; 
		}
		
		
		change_ordering( $thisRow.data("smid"), beforePrevId, targetPrevId );
	 });
	 
	 
 })
 </script>
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
				<c:import url="/WEB-INF/views/common/nav-list-a.jsp">
					<c:param name="menuName">메뉴관리</c:param>
				</c:import>			
				<!-- END:nav-list -->
		
			</div>
			<!-- END:sidebar -->		
	
			<!-- BEGIN:main-content -->			
			<div class="main-content">
			
				<!-- BEGIN:breadcrumbs&search -->	
				<c:set var="breadcrumbs" value="관리자콘솔,메뉴관리" />
				<c:set var="breadcrumbLinks" value="/a,/a/site" />
				<div class="breadcrumbs" id="breadcrumbs">
					<c:import url="/WEB-INF/views/common/breadcrumbs.jsp">
						<c:param name="breadcrumbs">${breadcrumbs }</c:param>	
						<c:param name="breadcrumbLinks">${breadcrumbLinks }</c:param>	
					</c:import>			
				</div>
				<!-- END:breadcrumbs&search -->	
				
				<!-- BEGIN:page-content -->						
				<div class="page-content">
					<div class="rw-content-area-wrap admin-content-area-wrap">
						<div class="rw-content-fs rw-mtl-wrap">
							<div class="rw-pagelet-blank"></div>
							<div class="rw-pagelet-wrap rw-mtl menu-admin-manage-wrap admin-full-size">
								<div class="row-fluid">
									<div class="span12">
										<ul id="sortable">
										<c:forEach items="${siteMenus }" var="siteMenu"  varStatus="status">
											<li class="ui-state-default z1" data-smid="${siteMenu.id }">
												<div class="site-admin-manage-title">${siteMenu.menu.name }${siteMenu.menu.extraHtml }</div>
												<div class="admin-site-menu">
													<div class="site-admin-manage-push">
														<label style="display:inline;">
															<input  onclick="return apply_checkbox(this);" name="switch-field-1" class="ace ace-switch ace-switch-3" type="checkbox" ${siteMenu.visible ? 'checked' : ''}>
															<span class="lbl"></span>
														</label>
													</div>
													<div class="menu-admin-manage-button-wrap">
														<span class="hmo-button hmo-button-white hmo-button-small-4 btn-up"><i class="fa fa-arrow-up fa-1g"></i></span>
														<span class="hmo-button hmo-button-white hmo-button-small-4 btn-down"><i class="fa fa-arrow-down fa-1g"></i></span>
													</div>											 
												</div>
												<div class="site-admin-manage-content">										
													<div class="admin-manage-help-text-01">${siteMenu.menu.description }</div>
												</div>	
												
											</li>
										</c:forEach>
										</ul>
									</div>
								</div>
							</div>
							<div class="rw-pagelet-blank"></div>
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