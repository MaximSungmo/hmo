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
		<div class="main-container container-fluid" id="main-container">
			
			<!-- BEGIN:sidebar -->		
			<div class="sidebar" id="snn-sidebar">
			
				<!-- BEGIN:welcome-box -->		
				<c:import url="/WEB-INF/views/common/welcome-box.jsp">
				</c:import>
				<!-- END:welcome-box -->
	
				<!-- BEGIN:nav-list -->
				<c:import url="/WEB-INF/views/common/nav-list.jsp">
					<c:param name="menuName">${not empty smallGroup && smallGroup.type == 3 ? '부서' : '노트' }</c:param>
				</c:import>			
				<!-- END:nav-list -->
			</div>
			<!-- END:sidebar -->
			
			<!-- BEGIN:main-content -->		
			<div class="main-content">
			
				<!-- BEGIN:breadcrumbs&search -->	
				<c:set var="breadcrumbs" value="작성중인 노트" scope="request"/>
				<c:set var="breadcrumbLinks" value="/note/drafts" scope="request"/>
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
					
						<div class="rw-content-area">
							<c:if test="${not empty smallGroup }">
							<div class="rw-pagelet-blank"></div>
								<div class="rw-pagelet-wrap">
									<div id="group-cover-col">
									
											<c:import url="/WEB-INF/views/pagelet/group/cover.jsp">
												<c:param name="tab">note</c:param>
											</c:import>
									</div>
							</div>
							</c:if>
							<div class="rw-pagelet-blank"></div>
							<div class="rw-pagelet-wrap">
								<div id="draft-list-changeable">
									<c:import url="/WEB-INF/views/pagelet/drafts.jsp">
										<c:param name="typeName">note</c:param>
									</c:import>
								</div>
								<div class="pull-right">
									<a href="/draft/create/note?gid=${smallGroup.id}" class="hmo-button hmo-button-blue hmo-button-small-10"><span>노트쓰기</span></a>
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