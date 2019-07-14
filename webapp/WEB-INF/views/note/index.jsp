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
		<div class="main-container container-fluid z1" id="main-container">
			<!-- BEGIN:sidebar -->		
			<div class="sidebar" id="snn-sidebar">
				<!-- BEGIN:welcome-box -->		
				<c:import url="/WEB-INF/views/common/welcome-box.jsp">
				</c:import>
				<!-- END:welcome-box -->
				<!-- BEGIN:nav-list -->
				<c:import url="/WEB-INF/views/common/nav-list.jsp">
					<c:param name="menuName">
					<c:if test="${empty  smallGroup}">
						노트
					</c:if>
					<c:if test="${not empty smallGroup }">
					<c:choose>	
						<c:when test="${ smallGroup.type == 3 }">
							부서														
						</c:when>
						 <c:when test="${ smallGroup.type == 4 }">
						 	프로젝트
						 </c:when>
						 <c:when test="${ smallGroup.type == 5 }">
						 	소그룹
						 </c:when>
					</c:choose>
					</c:if>
					</c:param>
				</c:import>			
				<!-- END:nav-list -->
			</div>
			<!-- END:sidebar -->
			<!-- BEGIN:main-content -->		
			<div class="main-content z1">
				<!-- BEGIN:breadcrumbs&search -->	
				<c:set var="breadcrumbs" value="노트" scope="request"/>
				<c:set var="breadcrumbLinks" value="/" scope="request"/>
				<div class="breadcrumbs" id="breadcrumbs">
					<c:import url="/WEB-INF/views/common/breadcrumbs.jsp">
						<c:param name="breadcrumbs">${breadcrumbs }</c:param>	
						<c:param name="breadcrumbLinks">${breadcrumbLinks }</c:param>	
					</c:import>			
				</div>
				<!-- END:breadcrumbs&search -->						
				<!-- BEGIN:page-content -->						
				<div class="page-content z1">
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
							
								<div class="lighter blue">
									<ul class="nav nav-pills">
										<li class="${empty param.tab || param.tab == 'all' ? 'active' : ''}"><a href="${groupPath }/note">전체노트</a></li>
										<li class="${param.tab == 'my' ? 'active' : ''}"><a href="${groupPath }/note?tab=my">내가쓴 노트</a></li>
										<li class="pull-right" style="position:relative;">
											<a href="#" data-toggle="dropdown" class="dropdown-toggle">
													<span>정렬</span>
													<i class="fa fa-caret-down bigger-125"></i>
												</a>
												<ul class="dropdown-menu dropdown-lighter pull-right dropdown-100">
													<li>
														<a href="#">
															<i class="icon-ok green"></i>
															생성날짜
														</a>
													</li>
												</ul>
										</li>
									</ul>
								</div>
							</div>	
							<div class="rw-pagelet-wrap">
								<div id="note-list-changeable" style="border-top: 1px solid lightgray;">
									<c:import url="/WEB-INF/views/pagelet/note/list.jsp"></c:import>
								</div>
							</div>
							<div class="pull-right">
								<a href="${groupPath}/note/drafts" class="hmo-button hmo-button-white hmo-button-small-10"><span>작성중인 노트목록</span></a>
								<a href="/draft/create/note?gid=${smallGroup.id}" class="hmo-button hmo-button-blue hmo-button-small-10"><span>노트쓰기</span></a>								
							</div>
							
							<div class="rw-pagelet-blank"></div>
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
<script type="text/javascript">
/*
 * mod_
 */
	var paramMap = new Array();
	function noteListReplaceList(parsedLink){
		$('.message-container').append('<div class="message-loading"><i class="fa fa-spin fa-spinner orange2 bigger-160"></i></div>');
		$.get("?" + parsedLink, function(data, status){
			$('.note-container').find('.message-loading').remove();
			$("#note-list-changeable").html(data);
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
		
		
		$(document.body).onHMOClick(".content-navigation", function(e){
			e.preventDefault();
			paramMap["pl"] = false;
			var parsedLink =  getParsedLink(paramMap);
			window.history.pushState(null, null, "${groupPath}/note?" + parsedLink);
			window.location.href=$(this).attr("href") + "?" + parsedLink;
		});
		
		$(".main-search-form").submit(function(e){
			e.preventDefault();
			var query = $(this).find("input[type=text]").val();
			paramMap["q"] = query;
			paramMap["page"] = 1;
			noteListReplaceList(getParsedLink(paramMap));
		});
		$(document.body).on("submit", "#note-page-form", function(e){
			e.preventDefault();
			var page = $(this).find("input[type=text]").val();
			paramMap["page"] = page;
			noteListReplaceList(getParsedLink(paramMap));
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
			noteListReplaceList( getParsedLink(paramMap) );
		});
		
		
	});
</script>
</body>
</html>