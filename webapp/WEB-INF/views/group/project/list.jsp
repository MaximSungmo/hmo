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
<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">모든 프로젝트 보기</c:param>
	<c:param name="bsUsed">YES</c:param>
	<c:param name="aceUsed">YES</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>
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
					<c:param name="menuName">프로젝트</c:param>
				</c:import>
				<!-- END:nav-list -->

			</div>
			<!-- END:sidebar -->

			<!-- BEGIN:main-content -->
			<div class="main-content">

				<!-- BEGIN:breadcrumbs&search -->
				<c:set var="breadcrumbs" value="프로젝트 보기" scope="request" />
				<c:set var="breadcrumbLinks" value="/project" scope="request" />
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
										<li class="${empty param.tab || param.tab == 'all' ? 'active' : ''}"><a href="/project">전체 프로젝트</a></li>
										<li class="${param.tab == 'my' ? 'active' : ''}"><a href="/project?tab=my">나의 프로젝트</a></li>
										<li class="pull-right"><a href="#" onclick="return create_group();">프로젝트 만들기</a></li>
									</ul>
								</div>
								
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
							</div>
							<div id="group-list-wrap" style="border-top: 1px solid lightgray; margin: 10px 10px 0 10px;">
								<c:import url="/WEB-INF/views/pagelet/group/list.jsp"></c:import>
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
				
<!-- BEGIN:resource of dialog for creating 부서 -->
<div id="res-dialog-new-group" style="display:none">
	<div class="rw-dialog-wrap rw-form-wrap">
		<form class="form-horizontal  rw-form" id="form-new-group">
			<div class="row-fluid">
				<div class="span12">
					<div class="control-group">
						<label class="control-label" for="parentSmallGroupId">상위 그룹</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon input-icon-right rw-input-wrap">
									<input readonly class="span12 " type="text" value="없음" >
									<i class="icon-remove-sign"></i>
									<i class="icon-ok-sign"></i>
								</span>
								<span class="lbl lbl-detail">프로젝트의 상위 그룹은 부서와 프로젝트만 가능합니다.</span>
							</div>
						</div>
					</div>		
				
					<div class="control-group">
						<label class="control-label" for="groupName">프로젝트 이름</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon input-icon-right rw-input-wrap">
									<input class="span12" type="text" name="name" id="groupName" placeholder="프로젝트 이름을 넣어주세요">
									<i class="icon-remove-sign"></i>
									<i class="icon-ok-sign"></i>
								</span>
							</div>
						</div>
					</div>		
					<div class="control-group">
						<label class="control-label" for="groupDescription">프로젝트 설명</label>
						<div class="controls">
							<div class="row-fluid">
								<textarea class="span12" name="description" id="groupDescription" placeholder="프로젝트 설명을 넣어주세요."></textarea>
							</div>
						</div>
					</div>		
				</div>
			</div>
			<input type="hidden" name="type" value="4" />
		</form>
	</div>
</div>
<!-- END:resource of dialog for creating user -->

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
		
		$(".more-detail").onHMOClick( null,  
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


<script>
/** mod_create_group **/


function create_group(){

	// Initialize Dialog
	bootbox.setLocale( "kr" );
	
	//
	bootbox.dialog( "res-dialog-new-group", [{
		"label" : "취소",
		"class" : "hmo-button hmo-button-white hmo-button-small-10"
	},{
		"label" : "확인",
		"class" : "hmo-button hmo-button-blue hmo-button-small-10",
		"callback": function() {
			var result = $( "#form-new-group" ).valid();
			if( !result ) {
				return false;
			}
			//
			var data = {}, arr;
			
			arr = $('#form-new-group').serializeArrayAlt();
			$.each( arr, function() {
				data[this.name] = this.value;
			});
			
			$.ajax({
				url:"/group",
				type:"post",
				dataType:"json",
				headers: {
					'Accept':'application/json',
					'Content-Type':'application/json'
					},
			    data: JSON.stringify( deepen( data ) ),
			    success: function( data ) {
			    	if( data.result == "fail" ){
			    		alert( data.message );
			    		return false; 
			    	}
			    		
					location.href ="/group/"
					+ data.data.id;

			    	//window.location.reload();
			    },
				error:function( jqXHR,textStatus,errorThrown ) {
					$.log( "error:Event.__inlineSubmit:" + errorThrown );
				}
			});
			return true;
		}
	}],{
		"header" : "프로젝트 만들기",	
		"embed" : true	
	});
}


$(function(){
	
	//validation for new user
	$( "#form-new-group" ).validate({
		errorElement: 'span',
		errorClass: 'help-inline',
		focusInvalid: false,
		onfocusout:function (element) {
			$(element).valid();
	    },
		rules: {
			"name" : {
				required: true
			},
			"description": {
				required: true
			}			
		},
		messages: {
			"name": {
				required: "이름을 넣어주세요"
			},
			"description": {
				required: "설명을 넣어주세요"
			}
		},
		invalidHandler: function (event, validator) {	
		},
		highlight: function (e) {
			$(e).closest('.control-group').removeClass('info').addClass('error');
		},
		success: function (e) {
			$(e).closest('.control-group').removeClass('error').addClass('info');
			$(e).remove();
		},
		errorPlacement: function ( error, element ) {
			if(element.is(':checkbox') || element.is(':radio')) {
				var controls = element.closest('.controls');
				if(controls.find(':checkbox,:radio').length > 1) {
					controls.append(error);
				} else {
					error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
				}
			} else if(element.is('.select2')) {
				error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
			} else if(element.is('.chosen-select')) {
				error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
			} else {
				error.insertAfter( element.parent().eq(0) );
			}
		},
		submitHandler: function (form) {
		}
	});		
	
});
/** mod_create_group end */

</script>
</body>
</html>