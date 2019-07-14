<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">파일목록</c:param>
	<c:param name="bsUsed">NO</c:param>
	<c:param name="aceUsed">NO</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>

<style type="text/css">
.rw-content-area-wrap	{background-color: white;}
/* .scroll-y 				{ padding: 0;} */
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
		<div class="main-container container-fluid">

			<!-- BEGIN:sidebar -->
			<div class="sidebar" id="snn-sidebar">

				<!-- BEGIN:welcome-box -->
				<c:import url="/WEB-INF/views/common/welcome-box.jsp">
				</c:import>
				<!-- END:welcome-box -->

				<!-- BEGIN:nav-list -->
				<c:import url="/WEB-INF/views/common/nav-list.jsp">
					<c:param name="menuName">파일 목록</c:param>
				</c:import>
				<!-- END:nav-list -->

			</div>
			<!-- END:sidebar -->

			<!-- BEGIN:main-content -->
			<div class="main-content">

				<!-- BEGIN:breadcrumbs&search -->
				<c:set var="breadcrumbs" value="파일목록" scope="request" />
				<c:set var="breadcrumbLinks" value="/media" scope="request" />
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
							<div class="rw-pagelet-blank"></div>
							<div class="rw-pagelet-wrap">
								<div class="z1">
										<small  class="pull-left" style="display: inline-block; width: 120px; font-size:11px;">검색단어(파일명 검색) </small><br />
										<form class="main-search-form" action="">
										<label class="pull-left" style="width:250px;">
											<input type="text" placeholder="파일명..." class="input-small nav-search-input" id="nav-search-input" name="q[]" autocomplete="off" style="width:230px;">
										</label>
										<button class="pull-left hmo-button hmo-button-blue">검색</button>
										</form>
								</div>
								<div class="z1" style="margin-bottom:3px;">
									
									<small  class="pull-left" style="display: inline-block; width: 60px;font-size:11px;">검색기준 : </small><br />
									<label class="pull-left">
										<input type="radio" name="my" class=" more-detail" value="0" checked data-type="radio" data-name="my" data-value="0">
										<span class="lbl vaml">전체파일</span>	
									</label>
									<label class="pull-left">
										<input type="radio" name="my" class=" more-detail" value="1" data-type="radio" data-name="my" data-value="1">
										<span class="lbl vaml">내파일</span>	
									</label>
								</div>
								<div class="z1">
									<small  class="pull-left" style="display: inline-block; width: 60px;font-size:11px;">필터 적용 :</small> <br />
									 
									<label class="pull-left"> 
									<input type="checkbox"
										class="chk-types  more-detail" 
										data-selector=".chk-types:checked"
										data-type="multiple" data-name="types[]"
										data-value="IMAGE"
										${not empty TYPE_IMAGE ? 'checked=checked' : ''}><span
										class="lbl vams">이미지 파일</span>
									</label> 
									<label class="pull-left"> 
									<input type="checkbox"
										class="chk-types  more-detail" data-type="multiple"
										data-selector=".chk-types:checked" data-name="types[]"
										data-value="WORD"
										${not empty TYPE_WORD ? 'checked=checked' : ''}><span
										class="lbl vams">워드(doc, docx)</span>
									</label >
									<label class="pull-left"> 
									<input type="checkbox"
										class="chk-types  more-detail" data-type="multiple"
										data-selector=".chk-types:checked" data-name="types[]"
										data-value="POWERPOINT"
										${not empty TYPE_POWERPOINT? 'checked=checked' : ''}><span
										class="lbl vams">파워포인트(ppt, pptx)</span>
									</label >
									<label class="pull-left"> 
									<input type="checkbox"
										class="chk-types  more-detail" data-type="multiple"
										data-selector=".chk-types:checked" data-name="types[]"
										data-value="EXCEL"
										${not empty TYPE_EXCEL ? 'checked=checked' : ''}><span
										class="lbl vams">엑셀(xls, xlsx)</span>
									</label >
									<label class="pull-left"> 
									<input type="checkbox"
										class="chk-types  more-detail" data-type="multiple"
										data-selector=".chk-types:checked" data-name="types[]"
										data-value="HWP"
										${not empty TYPE_HWP ? 'checked=checked' : ''}><span
										class="lbl vams">한글(hwp)</span>
									</label >
									<label class="pull-left"> 
									<input type="checkbox"
										class="chk-types  more-detail" data-type="multiple"
										data-selector=".chk-types:checked" data-name="types[]"
										data-value="PDF"
										${not empty TYPE_PDF ? 'checked=checked' : ''}><span
										class="lbl vams">PDF</span>
									</label >
									<label class="pull-left"> 
									<input type="checkbox"
										class="chk-types  more-detail" data-type="multiple"
										data-selector=".chk-types:checked" data-name="types[]"
										data-value="OTHER"
										${not empty TYPE_OTHER ? 'checked=checked' : ''}><span
										class="lbl vams">기타</span>
									</label >
								</div>
							</div>
							<div id="main-list-wrap" style="border-top: 1px solid lightgray; margin: 10px 10px 0 10px;">
								<c:import url="/WEB-INF/views/pagelet/media/list.jsp"></c:import>
							</div>
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

	$(function() {
		timesince();
		
	});
	
	
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
							return $(elem).data("value");
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