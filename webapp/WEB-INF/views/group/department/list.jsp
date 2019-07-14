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
	<c:param name="title">${empty smallGroup ? '부서' : smallGroup.name }</c:param>
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
		<div class="main-container container-fluid">

			<!-- BEGIN:sidebar -->
			<div class="sidebar" id="snn-sidebar">

				<!-- BEGIN:welcome-box -->
				<c:import url="/WEB-INF/views/common/welcome-box.jsp">
				</c:import>
				<!-- END:welcome-box -->

				<!-- BEGIN:nav-list -->
				<c:import url="/WEB-INF/views/common/nav-list.jsp">
					<c:param name="menuName">부서</c:param>
				</c:import>
				<!-- END:nav-list -->

			</div>
			<!-- END:sidebar -->

			<!-- BEGIN:main-content -->
			<div class="main-content">

				<!-- BEGIN:breadcrumbs&search -->
				<c:set var="breadcrumbs" value="부서 보기" scope="request" />
				<c:set var="breadcrumbLinks" value="/department" scope="request" />
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
						<div class="rw-content-area" style="background-color: white;">
							<div class="rw-pagelet-blank"></div>
							<div class="rw-pagelet-wrap">
								<div class="lighter blue">
									<ul class="nav nav-pills">
										<li class="${empty param.tab || param.tab == 'all' ? 'active' : ''}"><a href="/department">전체 부서</a></li>
										<li class="${param.tab == 'my' ? 'active' : ''}"><a href="/department?tab=my">나의 부서</a></li>
									</ul>
								</div>
								<c:if test="${param.tab == 'my' }">
									<form id="smallgroup-search-form" action="" method="GET" class="form-search">
										<div id="detail-search">
											<a href="#collapseOne" data-parent="#detail-searcch"
												data-toggle="collapse" class="accordion-toggle collapsed">
												상세검색 </a>
									
											<div class="accordion-body collapse" id="collapseOne"
												style="height: 0px;">
												<div class="accordion-inner">
													<div>
														<small style="display: inline-block; width: 60px;">정렬 : </small> 
														<label>
															<input type="radio" name="ordering" class="ace more-detail" data-name="ordering" data-value="" value="" checked>
															<span class="lbl">정렬없음</span>	
														</label>
														<label>
															<input type="radio" name="ordering" class="ace more-detail" data-name="ordering" data-value="${param.ordering != 'name' ? 'name' : ''}" value="name" >
															<span class="lbl">이름순</span>	
														</label>
													</div>
												</div>
											</div>
										</div>
										<div>
											<input type="text" class="search-query" style="width: 40%;">
											<button 
												class="hmo-button-search hmo-button hmo-button-blue">
												검색 
											</button>
										</div>
									</form>
								</c:if>
							</div>
							

							<c:choose>
								<c:when test="${param.tab == 'my' }">
									<div id="group-list-wrap" style="border-top: 1px solid lightgray; margin: 10px 10px 0 10px;">
										<c:import url="/WEB-INF/views/pagelet/group/list.jsp"></c:import>
									</div>
								</c:when>
								<c:otherwise>
									<div class="all-division-wrap">
										<c:import url="/WEB-INF/views/pagelet/group/tree.jsp"></c:import>
									</div>									
								</c:otherwise>
							</c:choose>
							
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
/* -- mod-timesince -- */
var _interval_id_timesince,_timesince_refresh_millis=60000;
function refresh_timesince(){
	var $el=$(this),
		t=$el.attr("data-utime");
	if(!t){
	    var s=function(a,b){ return(1e15+a+"").slice(-b); },
	    	d=new Date(parseInt($el.attr("data-timestamp"), 10));
	    t=d.getFullYear() + '-' +
		  s(d.getMonth()+1,2) + '-' +
	      s(d.getDate(),2) + ' ' +
	      s(d.getHours(),2) + ':' +
	      s(d.getMinutes(),2) + ':' +
	      s(d.getSeconds(),2);
	    $el.attr("data-utime", t);
	}
	var dt,secs,itv,h,m,s=$.trim(t);
 	s=s.replace(/\.\d+/,"");
    s=s.replace(/-/,"/").replace(/-/,"/");
    s=s.replace(/T/," ").replace(/Z/," UTC");
    s=s.replace(/([\+\-]\d\d)\:?(\d\d)/," $1$2");
    dt=new Date(s);
    secs=Math.floor((new Date()-dt)/1000);
    
    if( $el.hasClass( "_tf2" ) ) {
    	$el.text( dt.getHours()+":"+ ( (m=dt.getMinutes())<10?"0":"")+m );	
    } else {
	    if((itv=Math.floor(secs/31536000))>=1){
	    	$el.text(itv+"년 전");
	    }else if((itv=Math.floor(secs/2592000))>=1){
	    	$el.text(itv+"개월 전");
	    }else if((itv=Math.floor(secs/86400))>=1){
	    	$el.text(itv+"일 전");
	    }else if((itv=Math.floor(secs/3600))>=1){
	    	$el.text(itv+"시간 전");
	    }else if((itv=Math.floor(secs/60))>=1){
	    	$el.text(itv+"분 전");
	    }else{
	    	$el.text("방금 전");
	    }
    }
    
    $el.attr("title", dt.getFullYear()+"년 "+(dt.getMonth()+1)+"월 "+dt.getDate()+"일 "+((h=dt.getHours())>12?("오후 "+(h-12)):("오전 "+h))+':'+((m=dt.getMinutes())<10?"0":"")+m);
}
function timesince(){
	_interval_id_timesince && clearInterval(_interval_id_timesince);
	$(".livetimestamp").each(refresh_timesince);
	_interval_id_timesince = setInterval(function(){$(".livetimestamp").each(refresh_timesince);},_timesince_refresh_millis);
}

$(function(){
	timesince();
});
/* -- mod-timesince -- */
</script>
<script>

	var paramMap = new Array();

	function replaceList(parsedLink){
		$("#group-list-wrap").html('<div class="message-loading"><i class="fa fa-spin fa-spinner orange2 bigger-160"></i></div>');
		$.get("?" + parsedLink, function(data, status){
			$("#group-list-wrap").html(data);
			timesince();
		});
		
	}
	
	function appendList(parsedLink){
		
		//$("#next-stream-item").remove();
		$("#next-stream-btn").addClass("hidden-elem");
		$("#next-stream-loading").removeClass("hidden-elem");
		$.get("/group?" + parsedLink, function(data, status){
			$("#next-stream-item").remove();
			$("#group-list-wrap").append(data);
		});
	}
	<c:if test="${not empty param.ordering}">
	paramMap["ordering"] = "${param.ordering}";
	</c:if>

	paramMap["desc"] = true;
	<c:if test="${not empty param.desc}">
	paramMap["desc"] = "${param.desc}";
	</c:if>
	
	
	<c:if test="${not empty param.q}">
	paramMap["q"] = "${param.q}";
	</c:if>
	paramMap["tab"] = "all";
	<c:if test="${not empty param.tab}">
	paramMap["tab"] = "${param.tab}";
	</c:if>

	<c:if test="${not empty param.page}">
	paramMap["page"] = "${param.page}";
	</c:if>

	paramMap["pl"] = true;
	
	$(function() {
		
		$(document.body).onHMOClick("#next-stream-btn", function(e){
			e.preventDefault();
			var pageNum = paramMap["page"];
			
			if( typeof(pageNum) === 'undefined' ){
				pageNum = 1;
			}
			
			paramMap["page"] = parseInt(pageNum) + 1;
			
			appendList( getParsedLink(paramMap) );
			
		});
		
		$("#smallgroup-search-form").submit(function(e){
			e.preventDefault();
			var query = $(this).find("input[type=text]").val();
			
			paramMap["q"] = query;
			paramMap["page"] = 1;
			
			replaceList(getParsedLink(paramMap));
			
		});
		
		$(".more-detail").onHMOClick(null,  
			function(e) {
	
				var elemType = $(this).data("type");
	
				if( $(this).data("name") == "qt"){
					if( $("#smallgroup-search-form input[type=text]").val() == "" ){
						paramMap[$(this).data("name")] = $(this).data("value");
						return;
					}
				}
				
				if (elemType == "multiple") {
					var arr = $.map($($(this).data("selector")), function(
							elem, index) {
						return +$(elem).data("value");
					});
					paramMap[$(this).data("name")] = arr;
				} else {
					//e.preventDefault();
					paramMap[$(this).data("name")] = $(this).data("value");
				}
	
				paramMap["page"] = 1;
				
				replaceList( getParsedLink(paramMap) );
				
				
		});
	});
</script>

</body>
</html>