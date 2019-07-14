<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">사용자 관리</c:param>
	<c:param name="bsUsed">YES</c:param>
	<c:param name="aceUsed">YES</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>
<style type="text/css">
.rw-content-area-wrap	{background-color: white;}
.scroll-y 				{ padding: 0;}
.nav-search-icon		{ top: 2px; left: 3px;}
</style>
<!-- BEGIN:ace scripts -->
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-elements.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-extra.js"></script>
<!-- END:ace scripts -->

<!-- BEGIN:additional plugin -->
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/bootbox.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/jquery.validate.js"></script>
<!-- END:additional plugin -->

<script src="/assets/sunny/2.0/js/uncompressed/jquery.tokeninput.js"></script>
<link href="/assets/sunny/2.0/css/uncompressed/jquery.tokeninput.css" rel="stylesheet"  />


<!--  jqTree -->
<script src="/assets/jqtree/tree.jquery.js"></script>
<link rel="stylesheet" href="/assets/jqtree/jqtree.css" />


<style type="text/css">
.jqtree-element:hover				{ background-color: lightgrey; }
.jqtree-element						{ padding: 5px; border-bottom: 1px solid #eee; }
</style>
 
<%--
<script src="/assets/jstree/jstree.min.js"></script>
 <link rel="stylesheet" href="/assets/jstree/themes/default/style.min.css" />
 
 --%>
<script>

$(function(){
	var $tree = $('#tree');
	$.ajax( {
		url:"/a/department/tree",
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
	    		data:data.data,
	    		onCreateLi: function( node, $li ){
	    			$li.attr("data-id", node.id);
	    			 $li.find('.jqtree-element').append(
   		                '<a href="#node-' + node.id + '" class="delete2" data-id="'+
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
	

	$tree.on( "click", ".delete2", function(e){
		
		alert("hello");
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
</head>
<body class="scroll-y">
<div class="rw-snn-wrap" id="rw-snn-wrap">
	<div id="rw-snn-navi-wrap"></div>
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
				<c:set var="breadcrumbLinks" value="#,/a/user" />
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
								<div class="lighter blue">
									<ul class="nav nav-pills">
										<li >
											<a href="/a/department">부서보기</a>
										</li>
										<li class="active">
											<a href="/a/department/tree">부서관리</a>
										</li>
									</ul>
								</div>
							</div>
							<div class="rw-pagelet-blank"></div>
							<div class="rw-pagelet-wrap rw-mtl">
								- 드래그 & 드로그로 이동할 수 있습니다. (모바일 지원)
								- 하위 부서가 존재하는 부서는 이동할 수 없습니다.(하위 부서를 삭제하거나 하나씩 이동한 뒤 이동해야합니다)
								<div id="tree">
														
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
</body>
</html>