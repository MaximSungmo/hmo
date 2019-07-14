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
	<c:param name="title">알림</c:param>
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
					<c:param name="menuName">뉴스피드</c:param>
				</c:import>
				<!-- END:nav-list -->

			</div>
			<!-- END:sidebar -->

			<!-- BEGIN:main-content -->
			<div class="main-content">

				<!-- BEGIN:breadcrumbs&search -->
				<c:set var="breadcrumbs" value="알림" scope="request" />
				<c:set var="breadcrumbLinks" value="/notifications" scope="request" />
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
								<%--
								<form id="bookmark-search-form" action="" method="GET" class="form-search bookmark-search-form-wrap">
									<div id="detail-search" style="text-align:right;">
											<label>
											<input type="checkbox"
												class="ace chk-status more-detail" data-type="multiple"
												data-selector=".chk-status:checked" data-name="types[]"
												data-value="1"
												${not empty TYPE_STORY ? 'checked=checked' : ''}><span
												class="lbl">스토리</span>
											</label> 
											<label> 
											<input type="checkbox"
												class="ace chk-status more-detail" data-type="multiple"
												data-selector=".chk-status:checked" data-name="types[]"
												data-value="5"
												${not empty TYPE_NOTE ? 'checked=checked' : ''}><span
												class="lbl">노트</span>
											</label> 
											<label> 
											<input type="checkbox"
												class="ace checked chk-status more-detail"
												data-type="multiple" data-selector=".chk-status:checked"
												data-name="types[]" data-value="6"
												${not empty TYPE_PDS ? 'checked' : ''}><span
												class="lbl">자료실</span>
											</label>
											
									</div>
								</form>
								 --%>
							</div>
							<div id="bookmark-list-wrap" style="border-top: 1px solid lightgray; margin: 10px 10px 0 10px;">
								<c:import url="/WEB-INF/views/pagelet/notifications.jsp"></c:import>
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
	$(function(){
	timesince();
});

function ajax_delete_bookmark(data){
	if(data.result != "success"){
		$.error("error:ajax_delete_bookmark:$.ajax-" + data.message);
		return;
	}
	var $row=$(this).closest(".bookmark-row");
		
	$row.fadeOut(600, "linear", function(){$row.remove();});
}
</script>
	
	
<script type="text/javascript">
	function mainSearchSubmit( form ){
		return false; 
	}
	/*
	 * mod_
	 */
	
		var paramMap = new Array();
	
		
		function bookmarkListReplaceList(parsedLink){
			
			$('#bookmark-list-wrap').append('<div class="message-loading"><i class="fa fa-spin fa-spinner orange2 bigger-160"></i></div>');
			$.get("?" + parsedLink, function(data, status){
				$('#bookmark-list-wrap').find('.message-loading').remove();
				$("#bookmark-list-wrap").html(data);
				timesince();
			});
			
		}
		
		
		<c:if test="${not empty tab}">
		paramMap["tab"] = "${tab}";
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
	
		paramMap["pl"] = true;
		
		function setProfileFavorite( uid, favorited ){
			
			var $favorites = $(".favorite-" + uid);
			
			if( favorited == false){
				$favorites.html("<i class='icon-star-empty' />");
				$favorites.data("already-favorited", false);
			}else{
				$favorites.html("<i class='icon-star' />");
				$favorites.data("already-favorited", true);
			}
		}
		
		$(function() {
			
			$(".main-search-form").submit(function(e){
				e.preventDefault();
				var query = $(this).find("input[type=text]").val();
				
				paramMap["q"] = query;
				paramMap["page"] = 1;
				
				bookmarkListReplaceList(getParsedLink(paramMap));
			});
			
			$(document.body).on("submit", "#bookmark-page-form", function(e){
				e.preventDefault();
				var page = $(this).find("input[type=text]").val();
				
				paramMap["page"] = page;
				
				bookmarkListReplaceList(getParsedLink(paramMap));
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
					bookmarkListReplaceList( getParsedLink(paramMap) );
					
			});
		});
	</script>
</body>
</html>