<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>

<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">전자결재</c:param>
	<c:param name="bsUsed">NO</c:param>
	<c:param name="aceUsed">NO</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>

<!-- ace settings handler -->
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-extra.min.js"></script>

<!-- BEGIN:ace scripts -->
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-elements.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace.js"></script>
<!-- END:ace scripts -->

<script	src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/bootbox.js"></script>

<!-- BEGIN:sunny extra libraries -->

<script src="/assets/sunny/2.0/js/uncompressed/ejs.js"></script>

<!-- END:sunny extra libraries -->
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
		<div class="main-container container-fluid" id="main-container">
			
			<!-- BEGIN:sidebar -->		
			<div class="sidebar" id="snn-sidebar">
			
				<!-- BEGIN:welcome-box -->		
				<c:import url="/WEB-INF/views/common/welcome-box.jsp">
				</c:import>
				<!-- END:welcome-box -->
	
				<!-- BEGIN:nav-list -->
				<c:import url="/WEB-INF/views/common/nav-list.jsp">
					<c:param name="menuName">전자결재</c:param>
				</c:import>			
				<!-- END:nav-list -->
			</div>
			<!-- END:sidebar -->
			
			<!-- BEGIN:main-content -->		
			<div class="main-content z1">
			
				<!-- BEGIN:breadcrumbs&search -->	
				<c:set var="breadcrumbs" value="전자결재" scope="request"/>
				<c:set var="breadcrumbLinks" value="/approval" scope="request"/>
				<div class="breadcrumbs" id="breadcrumbs">
					<c:import url="/WEB-INF/views/common/breadcrumbs.jsp">
						<c:param name="breadcrumbs">${breadcrumbs }</c:param>	
						<c:param name="breadcrumbLinks">${breadcrumbLinks }</c:param>	
					</c:import>			
				</div>
				<!-- END:breadcrumbs&search -->						
				
				<!-- BEGIN:page-content -->						
				<div class="page-content">
				
					<div class="rw-content-area-wrap approval-rw-content-area-wrap">
					
						<div class="rw-content-area">
<!-- 							<div class="rw-pagelet-blank"></div> -->
<!-- 							<div class="rw-pagelet-wrap"> -->
<%-- 								<c:import url="/WEB-INF/views/common/approval-cover.jsp"> --%>
<%-- 									<c:param name="menu">index</c:param> --%>
<%-- 								</c:import> --%>
<!-- 							</div> -->
							<div class="rw-pagelet-blank"></div>
							<div class="rw-pagelet-wrap approval-drafts-rw-pagelet-wrap">
								<div id="draft-list-changeable">
									<c:import url="/WEB-INF/views/pagelet/drafts.jsp">
										<c:param name="typeName">approval</c:param>
									</c:import>
								</div>
								<div class="pull-right approval-drafts-buttonwrap">
									<a href="/draft/create/approval" class="hmo-button hmo-button-blue hmo-button-small-10"><span>새로운 결재 작성</span></a>
								</div>
							</div>
							<div class="rw-pagelet-blank"></div>
							
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

<script>

function ajax_delete_draft(data){
	if(data.result != "success"){
		$.error("error:ajax_delete_draft:$.ajax-" + data.message);
		return;
	}
	
	$(this).closest(".draft-row").remove();
	//window.location.href = document.referrer;
//	window.location.href = "${groupPath}/note/drafts";
// 	if(IE){ //IE, bool var, has to be defined
// 	    var newlocation = document.createElement('a');
// 	    newlocation.href = URLtoCall;
// 	    document.body.appendChild(newlocation);
// 	    newlocation.click();
// 	}
}

</script>

<script type="text/javascript">
	
/*
 * mod_
 */

	var paramMap = new Array();

	
	function draftListReplaceList(parsedLink){
		
		$('.message-container').append('<div class="message-loading"><i class="fa fa-spin fa-spinner orange2 bigger-160"></i></div>');
		$.get("?" + parsedLink, function(data, status){
			$('.draft-container').find('.message-loading').remove();
			$("#draft-list-changeable").html(data);
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


	<c:if test="${not empty param.uid }">
	paramMap["uid"] = "${param.uid}";
	</c:if>

	
	paramMap["pl"] = true;
	
	
	$(function() {
		
		$(document.body).on("submit", "#draft-page-form", function(e){
			e.preventDefault();
			var page = $(this).find("input[type=text]").val();
			
			paramMap["page"] = page;
			
			draftListReplaceList(getParsedLink(paramMap));
		});
		$(document.body).on("click", ".more-detail", function(e){
				
				var elemType = $(this).data("type");
	
				var name = $(this).data("name"); 
				var value =  $(this).data("value");
				
				if(  name == "qt"){
					if( $("#draft-search-form input[type=text]").val() == "" ){
						paramMap[name] = value;
						return;
					}
				}
				
				if (elemType == "multiple") {
					var arr = $.map($($(this).data("selector")), function(
							elem, index) {
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
				draftListReplaceList( getParsedLink(paramMap) );
				
		});
	});
</script>
</body>
</html>