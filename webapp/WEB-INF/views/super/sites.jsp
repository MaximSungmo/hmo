<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title"></c:param>
	<c:param name="bsUsed">NO</c:param>
	<c:param name="aceUsed">NO</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>
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
					<!-- BEGIN:nav-list -->
					<c:import url="/WEB-INF/views/common/nav-list-super.jsp">
						<c:param name="menuName">site</c:param>
					</c:import>			
					<!-- END:nav-list -->
				</div>					
				<!-- END:sidebar -->
				
				<!-- BEGIN:main-content -->		
				<div class="main-content z1">			
					<div class="page-content z1">
						<div class="rw-content-area-wrap">
							<div class="rw-content-area" style="margin-right:0;">
								<div class="rw-pagelet-blank"></div>
								<div class="rw-pagelet-wrap">
									사이트 총 개수 : ${pagedResult.totalCountOfElements }개
								</div>
								<div class="rw-pagelet-blank"></div>
								<div class="rw-pagelet-wrap">
									
									<div id="main-list-wrap" style="margin: 10px 10px 0 10px;">
										<c:import url="/WEB-INF/views/super/pagelet/sites.jsp"></c:import>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			<!-- END:main-content -->		
			</div>
		</div>
	</div>
	
<script>

function inlineSubmit( form, event ) {	
	var 
	$form = $(form);
	var isConfirmed = confirm("정말 삭제하시겠습니까?");

	if( isConfirmed == true ){
		$form.get(0).setAttribute("action", "/super/remove_site");
	}
	
	return isConfirmed;
}
</script>


<script type="text/javascript">
	
	/*
	 * mod_
	 */
	
		var paramMap = new Array();
	
		
		function replaceList(parsedLink){
			
			$('#main-list-wrap').append('<div class="message-loading"><i class="fa fa-spin fa-spinner orange2 bigger-160"></i></div>');
			$.get("?" + parsedLink, function(data, status){
				$('#main-list-wrap').find('.message-loading').remove();
				$("#main-list-wrap").html(data);
				timesince();
			});
			
		}
		
		
		<c:if test="${not empty tab}">
		paramMap["tab"] = "${tab}";
		</c:if>
	
		<c:if test="${not empty param.ordering}">
		paramMap["ordering"] = "${param.ordering}";
		</c:if>
	
		<c:if test="${not empty param.types}">
		paramMap["types"] = "${param.types}";
		</c:if>
	
		<c:if test="${not empty param.q}">
		paramMap["q"] = "${param.q}";
		</c:if>
	
		<c:if test="${not empty param.my}">
		paramMap["my"] = "${param.my}";
		</c:if>
	
		<c:if test="${not empty param.page}">
		paramMap["page"] = "${param.page}";
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
			
			$(document.body).on("submit", "#main-page-form", function(e){
				e.preventDefault();
				var page = $(this).find("input[type=text]").val();
				
				paramMap["page"] = page;
				
				replaceList(getParsedLink(paramMap));
			});
			$(document.body).on("click", ".more-detail", function(e){
					
					var elemType = $(this).data("type");
		
					var name = $(this).data("name"); 
					var value =  $(this).data("value");
					
					
					
					
					if (elemType == "multiple") {
						var arr = $.map($($(this).data("selector")), function(
								elem, index) {
							return +$(elem).data("value");
						});
						paramMap[name] = arr;
					} else if( elemType == "radio"){
						paramMap[name] = value;
					}else {
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