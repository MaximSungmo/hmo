<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="ko">
<head>

<c:import url="/WEB-INF/views/common/head-front.jsp">
	<c:param name="title">오피스 목록</c:param>
	<c:param name="bsUsed">YES</c:param>
	<c:param name="aceUsed">YES</c:param>
	<c:param name="jsUsed">YES</c:param>
</c:import>
</head>
<body style="overflow-y:scroll">
<div class="rw-snn-fs">

	<!-- BEGIN:navbar -->
	<div class="navbar" id="navbar">
		<c:import url="/WEB-INF/views/common/navbar-front.jsp">
		</c:import>
	</div>
	<!-- END:navbar -->		

	<div class="main-container container-fluid site-list-container">
		<div class="main-content rw-signup-wizard-content">
			<div class="page-content login-page-content">
				<div class="row-fluid rw-signup-wizard-content-row-fluid">
					<div class="span12">
						<div class="site-list-wrap">
							<div class="exception-img">
								<img src="/assets/sunny/2.0/img/hellomyoffice_logo.png" alt="hellomyoffice-logo">
							</div>
						</div>
						
						<div class="rw-signup-wizard-content-row-fluid hmo-site-list">
							<div class="site-list-content">
							<p class="site-list-content-p"><strong>소속된 회사가 있으신가요 ?</strong><br>소속된 회사를 찾아
									저희 'Hello my office'를 이용해 보세요.</p>
							</div>
						</div>
						<div class="site-list-search-wrap">
							<form id="form-search" action="">
								<span class="input-icon">
									<input type="text" placeholder="회사 이름, 도메인, 전화번호로 검색하세요" class="input-small nav-search-input site-search-input span12" id="nav-search-input" name="q" autocomplete="off">
									<i class="fa fa-search fa-1g nav-search-icon site-search-icon"></i>
									<button type="submit" class="hmo-button-search hmo-button hmo-button-khaki">확인</button>
								</span>
							</form>
						</div>
						<div class="rw-signup-wizard-content-row-fluid hmo-site-list">
								<div id="changeable-pagelet" class="site-list-content">
								</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>	



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
		$("#changeable-pagelet").html("<tr><td class='center' colspan='100%'><i class='icon-spin icon-spinner' /></td></tr>");
		$.get("/site?" + parsedLink, function(data, status){
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

	$(function() {
		
		$("#form-search").submit(function(e){
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
			
			e.preventDefault();
			paramMap[name] = value;
				
			if( name != "page" ){
				paramMap["page"] = 1;
			}
				
			replaceList( getParsedLink(paramMap) );
				
				
		});
	});
</script>

</body>
</html>




<%-- 

<div class="container">
	<div class="jumbotron">
		<h1>권한없음</h1>
		<p class="lead">해당 페이지에 대한 권한이 없습니다.</p>
		<p>
			<a href="/" class="btn btn-success btn-lg">메인으로 가기</a> 
		</p>
	</div>
</div>
<!-- /.container -->


--%>