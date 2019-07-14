<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">부서 관리</c:param>
	<c:param name="bsUsed">YES</c:param>
	<c:param name="aceUsed">YES</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>

<style type="text/css">
.rw-content-area-wrap	{background-color: white;}
.scroll-y 				{ padding: 0;}
.nav-search-icon		{ top: 2px; left: 3px;}
</style>
<!-- END:common head contents -->

<!-- BEGIN:ace scripts -->
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-elements.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-extra.js"></script>
<!-- END:ace scripts -->

<!-- BEGIN:additional plugin -->
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/bootbox.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/jquery.validate.js"></script>
<!-- END:additional plugin -->

<%-- BEGIN:부서이름 검색을 위한 typemanager --%>
<!-- <script src="/assets/sunny/2.0/js/uncompressed/bootstrap-tagsinput.js"></script> -->
<!-- <script src="/assets/sunny/2.0/js/uncompressed/typeahead.js"></script> -->
<!-- <script src="/assets/sunny/2.0/js/uncompressed/tagmanager.js"></script> -->
<!-- <script src="http://twitter.github.com/hogan.js/builds/2.0.0/hogan-2.0.0.js"></script> -->
<!-- <link href="/assets/sunny/2.0/css/uncompressed/typeahead.css" rel="stylesheet"  /> -->

<script src="/assets/sunny/2.0/js/uncompressed/jquery.tokeninput.js"></script>
<link href="/assets/sunny/2.0/css/uncompressed/jquery.tokeninput.css" rel="stylesheet"  />

<!--  jqTree -->
<script src="/assets/jqtree/tree.jquery.js"></script>


<style type="text/css">
</style>
 
<script>

$(function(){
	var $tree = $('#tree');
	$.ajax( {
		url:"/group/tree",
		type:"GET",
		dataType:"json",
		headers: {
			'Accept':'application/json',
			'Content-Type':'application/json'
		},
	    success: function(data){
	    	if( data.result =="fail" ){
	    		alert(data.message);
	    		return false; 
	    	}
	    	<%--
	    	$tree.jstree({
	    		'core' : {
	    		    "themes" : { "stripes" : true },
    				"multiple" : false,
    				"animation":0,
	    			'data': data.data,
	    		    "check_callback" : true
	    		},
	    		"plugins" : [ "wholerow","dnd" ]
	    	});
	    	$tree.jstree('open_all');
	    	--%>
	    	$tree.tree({
	            autoOpen: 1,
	            dragAndDrop: true,
	            selectable: false,
	            closedIcon: '<i class="fa fa-plus-circle"></i>',
	            openedIcon: '<i class="fa fa-minus-circle"></i>',
	    		data:data.data,
	    		onCreateLi: function( node, $li ){
	    			$li.attr("data-id", node.id);
	    			$li.find('.jqtree-element').wrapInner("<a href='/a/department/" + node.id + "/info'></a>");
	    			$li.find('.jqtree-element').append(
   		                '<a href="#" class="delete department-delete hmo-button hmo-button-white hmo-button-small-3	" data-id="'+
   		                node.id +'">삭제하기</a>'
   		            );
	    		},
	    		onCanMove: function(node) {
	    	        if ( node.children.length > 0) {
	    	            return false;
	    	        }
	    	        return true;
	    	    }
	    	});
	    }
	});
	
	$tree.bind( "tree.click", function(e){
		window.location.href="/a/department/" + e.node.id + "/info";
	});
	
	$tree.onHMOClick(".delete", function(e){
		
		e.preventDefault();
        e.stopPropagation(); 
		// Get the id from the 'node-id' data property
        var node_id = $(e.target).data('id');

        // Get the node from the tree
        var node = $tree.tree('getNodeById', node_id);

        if (node == null) {
           return;
        }
        
        if( node.children.length > 0 ){
        	alert("하위 부서가 존재하면 삭제할 수 없습니다. 하위 부서를 먼저 삭제해주세요. ");
        	return;
        }
        
        // Display the node name
        if( !confirm("정말 삭제하시겠습니까? 삭제하면 해당 부서의 글들을 복구할 수 없습니다.") ){
        	return;
        }
        
        
        $.ajax({
    		url:"/a/department/delete",
    		type : 'GET',
    		dataType : 'json',
    		headers : {
    			'Accept' : 'application/json',
    			'Content-Type' : 'application/json'
    		},
    		data:{
    			"id" : node.id
    		},
    		success:function(data){
    			if( data.result == "fail" ){
    				alert(data.message);
    				return false; 
    			}
    	        $tree.tree("removeNode", node);
    		}
    	})
        
        
		
	});
	
	
	$tree.bind("tree.move", function(e){
        e.preventDefault();
        
        if( confirm('정말 옮기시겠습니까?') ){
        	e.move_info.do_move();
        	var data = {
        			"siteId" : "${sunny.site.id}",
        			"id" : e.move_info.moved_node.id,
        			"targetId" : e.move_info.target_node.id,
        			"position" : e.move_info.position
        	}
        	
        	$.ajax({
        		url:"/a/department/tree",
        		type:"PUT",
        		dataType:"json",
        		headers: {
        			'Accept':'application/json',
        			'Content-Type':'application/json'
        		},
        		data: JSON.stringify( data ),
        		success:function(data){
        			if( data.result == "fail" ){
        				alert(data.message);
        				return false; 
        			}
        		}
        	})
        }
	});
});

</script>
<script>

$( function() {
	
	// Initialize Dialog
	bootbox.setLocale( "kr" );
	
	//
	$("#btn-new-department").onHMOClick(null, function() {
		bootbox.dialog( "res-dialog-new-department", [{
			"label" : "취소",
			"class" : "hmo-button hmo-button-white hmo-button-small-10"
		},{
			"label" : "확인",
			"class" : "hmo-button hmo-button-blue hmo-button-small-10",
			"callback": function() {
				var result = $( "#form-new-department" ).valid();
				if( !result ) {
					return false;
				}
				
				
				if( $("#parentDepartmentRadio1:checked").length == 0 &&  $("input[name=departmentParentId]").length == 0  ){
					var isConfirmed = confirm("상위 부서가 선택되지 않았습니다. 최상위 부서로 만드시겠습니까?");
					
					if( isConfirmed == false )
						return false;
					
					$("#parentDepartmentRadio1").attr("checked", true);
				}
				
				//
				var data = {}, arr;
				
				arr = $('#form-new-department').serializeArrayAlt();
				$.each( arr, function() {
					data[this.name] = this.value;
				});
				
				$.ajax({
					url:"",
					type:"post",
					dataType:"json",
					headers: {
						'Accept':'application/json',
						'Content-Type':'application/json'
						},
				    data: JSON.stringify( deepen( data ) ),
				    success: function( data ) {
				    	window.location.reload();
				    },
					error:function( jqXHR,textStatus,errorThrown ) {
						$.log( "error:Event.__inlineSubmit:" + errorThrown );
					}
				});
				return true;
			}
		}],{
			"header" : "새 부서 생성",	
			"embed" : true	
		});
	});
	
});


<%-- 부서 이름 API 로 가져와서 Hint 주고, 거기에서 선택한 것을 Token 에 넣는 tokeninput.js 사용 부분 --%>

$(function(){
	
	$("#departmentParentName").tokenInput("/match/department", {
		hintText: "상위 부서 이름을 입력해주세요.",
		noResultsText: "부서가 없습니다",
		searchingText: "검색중",
		queryParam: "key",
		method: "GET",
		tokenLimit: "1",
		onReady: function(e){
			<%-- tokeninput.js 는 placeholder 를 지원하지 않기 때문에 placeholder 를 직접 넣어줘야함.
			departmentParentName 앞에 붙어있는 token-input 은 tokeninput.js 에서 자동으로 생성하는 엘리먼트로써, 적용하는 엘리먼트 이름마다 다르니 소스 확인 후 붙여줘야한다. --%>
			$("#token-input-departmentParentName").attr("placeholder", $("#departmentParentName").attr("placeholder"));
		},
		onResult: function (data) {
			var newData = new Array();
			var totalCount = 0;
			if( data == null ){
				newData[totalCount] = {
						value:"없습니다",
					}
			}else{
				$.each(data, function( index,data ) {
					newData[index] = {
							id:data.id,
							name:data.value
					}
					totalCount++;
				});
			}

			return newData;
		},
		onAdd: function( data ){
			$("#departmentTitle").focus();
		},
		resultsFormatter: function(result){
			return "<li>" + result.name + "</li>"
		},
        tokenFormatter: function(result) { 
        	return "<li class='post-tags'>" 
        	+ result.name 
        	+ "<input type='hidden' name='departmentParentId' value='" + result.id + "'></li>" 
        }
	});
	
	$("#parentDepartmentRadio1").onHMOClick(null, function(e){
		$("#departmentParentName").tokenInput("clear");
		$("#departmentTitle").focus();
	});
});

$(function(){
	$(".main-search-form").submit(function(e){
		e.preventDefault();
		var query = $(this).find("input[type=text]").val();
		paramMap["q"] = query;
		paramMap["page"] = 1;
		replaceList(getParsedLink(paramMap));
	});
	
	$(document.body).on("submit", "#page-form", function(e){
		e.preventDefault();
		var page = $(this).find("input[type=text]").val();
		paramMap["page"] = page;
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
				<c:import url="/WEB-INF/views/common/nav-list-a.jsp">
					<c:param name="menuName">부서관리</c:param>
				</c:import>			
				<!-- END:nav-list -->
		
			</div>
			<!-- END:sidebar -->		
	
			<!-- BEGIN:main-content -->			
			<div class="main-content">
			
				<!-- BEGIN:breadcrumbs&search -->	
				<c:set var="breadcrumbs" value="관리자콘솔,부서관리" />
				<c:set var="breadcrumbLinks" value="/a,/a/department" />
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
						<div class="rw-content-area rw-content-fs">
							<div class="rw-pagelet-blank"></div>
							<div class="rw-pagelet-wrap rw-mtl">
								- 드래그 & 드로그로 이동할 수 있습니다. (모바일 지원) <br />
								- 하위 부서가 존재하는 부서는 이동할 수 없습니다.(하위 부서를 삭제하거나 하나씩 이동한 뒤 이동해야합니다)
								<div id="tree">
														
								</div>
							</div>
							<div class="rw-pagelet-blank"></div>
							<div class="rw-pagelet-wrap rw-mtl">
								<div class="row-fluid">
									<div class="span12 rw-a-user-list-buttons ta-right">
										<a class="hmo-button hmo-button-blue hmo-button-small-10" id="btn-new-department">새부서</a>
									</div>	
									<div class="span6">
	
									</div>	
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- END:page-content -->						
			</div>
			<!-- END:main-content -->	
		</div>
		<!-- EDN:main-container -->
	</div>
</div>
	
<!-- BEGIN:resource of dialog for creating 부서 -->
<div id="res-dialog-new-department" style="display:none">
	<div class="rw-dialog-wrap rw-form-wrap">
		<form class="form-vertical rw-form" id="form-new-department">
			<div class="row-fluid">
				<div class="span12">
					<div class="control-group">
						<h3 class="black rw-fieldset-title">상위부서</h3>
						<div class="controls">
							<div class="row-fluid">
								<label>
									<input type="radio" class="ace" id="parentDepartmentRadio1" name="parentDepartmentRadio" value="1">
									<span class="lbl">&nbsp;최상위 부서</span>
								</label>
							</div>
							<div class="row-fluid">
								<label style="position:absolute;">
									<input type="radio" class="ace" name="parentDepartmentRadio" value="2" checked>
									<span class="lbl">&nbsp;</span>
								</label>
								<div style="margin-left: 20px; position: relative; " >
									<input type="text" id="departmentParentName" placeholder="상위부서 이름을 검색하여 주세요.">
								</div>
							</div>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="companyEmail">부서 이름</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon input-icon-right rw-input-wrap">
									<input class="span12" type="text" name="departmentTitle" id="departmentTitle" placeholder="부서 이름을 넣어주세요.">
									<i class="icon-remove-sign"></i>
									<i class="icon-ok-sign"></i>
								</span>
							</div>
						</div>
					</div>		
					<div class="control-group">
						<label class="control-label" for="companyEmail">부서 설명</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon input-icon-right rw-input-wrap">
									<input class="span12" type="text" name="departmentDescription" id="departmentDescription" placeholder="부서 설명을 넣어주세요.">
									<i class="icon-remove-sign"></i>
									<i class="icon-ok-sign"></i>
								</span>
							</div>
						</div>
					</div>			
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<div class="control-group">
						<label class="rw-chckbx">
							<span class="lbl">
								<span>새로운 부서를 생성합니다.</span>
							</span>
						</label>
					</div>				
				</div>
			</div>
		</form>
	</div>
</div>
<!-- END:resource of dialog for creating user -->

</body>
</html>