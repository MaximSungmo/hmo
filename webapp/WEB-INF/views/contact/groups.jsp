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
<script src="/assets/sunny/2.0/js/uncompressed/jquery.pathchange.js"></script>
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
					<c:param name="menuName">주소록</c:param>
				</c:import>
				<!-- END:nav-list -->

			</div>
			<!-- END:sidebar -->

			<!-- BEGIN:main-content -->
			<div class="main-content">

				<!-- BEGIN:breadcrumbs&search -->
				<c:set var="breadcrumbs" value="주소록,그룹" scope="request" />
				<c:set var="breadcrumbLinks" value="/contact,/contact/groups" scope="request" />
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
								<div class="lighter blue">
									<ul class="nav nav-pills">
										<li><a href="/contact">전체보기</a></li>
										<li class="active"><a href="/contact/groups">그룹</a></li>
										<li ><a href="/contact/favorites">즐겨찾기</a></li>
									</ul>
								</div>
							</div>
							<div id="contact-user-list" class="hidden-elem">
							
								<div style="padding-left: 10px; padding-bottom: 6px; ">
									<a href="#" onclick="javascript:history.back();" style="font-size:20px; ">
										<i class="fa fa-chevron-left"></i>
										<span id="contact-current-group"><c:if test="${not empty contactSmallGroup}">${contactSmallGroup.name }</c:if></span>
									</a>
								</div>
							
								<div id="contact-list">
								
								
								</div>
								
							</div>
							<div id="contact-sg-list">
								<c:forEach items="${pagedResult.contents}" var="smallGroup"
									varStatus="status">
									
								
									<div class="row-triple list-border-top">
									<a 
									class="pathchange" 
									href="/contact/groups/${smallGroup.id}"
									data-href="/pagelet/contact_group_users/${smallGroup.id}"
									data-sgname="${smallGroup.name }"
									style="color:black;">
										<div class="triple-first">
										
										</div>
										<div class="triple-second">
											<div class="triple-cell">
												${smallGroup.userCount }명
											</div>
										</div>
										<div class="triple-third">
											<div class="triple-cell">
												${smallGroup.name }
											
											</div>
										</div>
								</a>
									</div>
								</c:forEach>
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
		
<!-- BEGIN:resource of dialog for showing profile -->
<div id="res-dialog-show-profile" style="display:none">
	<div class="rw-dialog-wrap rw-form-wrap" id="res-dialog-show-profile-content">
	</div>
</div>
<!-- END:resource of dialog for showing profile -->

<script>
$(function(){
	

	$.pathchange.init({
		selector : ".pathchange"	
	});
	
	$(window).bind("pathchange", function(e, _this) {
		
		// 페이지 로딩 시 
		if( typeof(changeFlag) == "undefined" ){
			changeFlag = 0;
		}else{
			changeFlag ^= 1; 
		}
		
		if( changeFlag == 1 ){
			$("#contact-user-list").removeClass("hidden-elem");
			$("#contact-sg-list").addClass("hidden-elem");
			$("#contact-current-group").text($(_this).data("sgname"));
			
			$("#contact-list").html("<tr><td class='center' colspan='100%'><i class='icon-spin icon-spinner' /></td></tr>");
			$.get($(_this).data("href"), function(data, status){
				$("#contact-list").html(data);
				
				
				//$("#contact-user-list").removeClass("hidden-elem");
			});
		}else{
			$("#contact-user-list").addClass("hidden-elem");
			$("#contact-sg-list").removeClass("hidden-elem");
		}
	    //respondToUrl();
	});
});
</script>



<script>

	var paramMap = new Array();

	function getParsedLink(map){
		var parsedLink = "";
		
		for ( var name in paramMap) {
			parsedLink = insertParam(parsedLink, name, paramMap[name]);
		}
		
		return parsedLink;
	}
	
	function replaceList(parsedLink){
		$("#contact-list").html("<tr><td class='center' colspan='100%'><i class='icon-spin icon-spinner' /></td></tr>");
		$.get("/contact/pagelet/list?" + parsedLink, function(data, status){
			$("#contact-list").html(data);
		});
		
	}
	
// 	function appendList(parsedLink){
		
// 		//$("#next-stream-item").remove();
// 		$("#next-stream-btn").addClass("hidden-elem");
// 		$("#next-stream-loading").removeClass("hidden-elem");
// 		$.get("/contact/pagelet/list?" + parsedLink, function(data, status){
// 			$("#next-stream-item").remove();
// 			$("#contact-list").append(data);
// 		});
		
// 	}
	
	function show_profile(elem){
		
		
		if( typeof(_$ejs_show_profile) == "undefined" || _$ejs_show_profile == null ){
		_$ejs_show_profile = new EJS( {
			url: "/assets/sunny/2.0/js/template/show-profile.ejs?v=1.0"
		});
		}
		
		var map =  $(elem).data("map");
		
		var isFavorited = $.ajax({
			url: "/contact/check_favorite",
			type: "GET",
			dataType: "json",
			data:{
				"uid" : map.id
			}
		});
		bootbox.dialog( "res-dialog-show-profile", [
			{
				"label" : "확인",
				"class" : "hmo-button hmo-button-blue hmo-button-small-10",
				"callback": function() {
					return true;
				}
			}		                                           		                                           
	    ],{
		"header" : map.name + "<a href='#' data-id='" + map.id + "' class='favorite-in-profile favorite-" + map.id + "'><i class='icon-spinner icon-spin'></i></a>",	
		"embed" : true,
		"onInit" : function() {
			//$("#res-dialog-show-profile-content").html( _$ejs_show_profile.render(map) );
			
			$.when(isFavorited)
			.done(function(data){
				if(data.result == "fail"){
					alert( data.message );
				}
				setProfileFavorite( map.id, data.data );
				
			})
			.fail(function(data){
				alert("오류발생");
			});
		},
		"onFinalize" : function() {
			//alert( "call finalize" );	
			//window.location.reload();
		},
		"beforeShown" : function() {
			// 로딩으로 변경
			//$("#res-dialog-show-profile-content").html( "로딩중" );
			$("#res-dialog-show-profile-content").html( _$ejs_show_profile.render(map) );
		}
		});
				
	}
	
	
	
	
	<c:if test="${not empty param.range}">
	paramMap["range"] = "${param.range}";
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
		
		$(document.body).onHMOClick(".favorite-in-profile", function(e){
			e.preventDefault();
			
			var isAlreadyFavorited = $(this).data("already-favorited");
			var uid = $(this).data("id");
			var url = isAlreadyFavorited == true ? "/contact/remove_favorite" : "/contact/add_favorite";
			$.ajax({
				url: url ,
				type: "GET",
				dataType: "json",
				data:{
					"uid" : uid
				},
				success: function(data){
					setProfileFavorite(uid, data.data);	
				}
			});
			
		});
		
		$(document.body).onHMOClick("#next-stream-btn", function(e){
			e.preventDefault();
			var pageNum = paramMap["page"];
			
			if( typeof(pageNum) === 'undefined' ){
				pageNum = 1;
			}
			
			paramMap["page"] = parseInt(pageNum) + 1;
			
			appendList( getParsedLink(paramMap) );
			
		});
		
		$(".main-search-form").submit(function(e){
			e.preventDefault();
			var query = $(this).find("input[type=text]").val();
			
			paramMap["q"] = query;
			paramMap["page"] = 1;
			
			replaceList(getParsedLink(paramMap));
			
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
</body>
</html>