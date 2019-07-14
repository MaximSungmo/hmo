<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script src="/assets/jqtree/tree.jquery.js"></script>

<div id="tree-wrap" style="position:relative; ">
	<div id="tree">
	
	
	</div>
</div>
<script type="text/javascript">

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
            dragAndDrop: false,
            selectable: false,
            closedIcon: '<i class="fa fa-plus-circle"></i>',
            openedIcon: '<i class="fa fa-minus-circle"></i>',
    		data:data.data,
    		onCreateLi: function( node, $li ){
    			$li.attr("data-id", node.id);
    			$li.find('.jqtree-element').wrapInner("<a href='/group/" + node.id +"'></a>" );
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
	window.location.href="/group/" + e.node.id ;
});

</script>

<!-- <div class="dd"> -->
<%-- 	<c:forEach items="${pagedResult.contents}" var="smallGroup"	varStatus="status"> --%>
<%-- 		<c:choose> --%>
<%-- 			<c:when test="${status.first }"> --%>
<!-- 				<ol class="dd-list"> -->
<%-- 			</c:when> --%>
<%-- 			<c:when test="${ makeList == true && prevParentId == smallGroup.parentSmallGroupId  }"> --%>
<!-- 				<ol class="dd-list"> -->
<%-- 				<c:set var="makeList" value="false" /> --%>
<%-- 			</c:when> --%>
<%-- 			<c:when test="${prevParentId != smallGroup.parentSmallGroupId }"> --%>
<!-- 				</ol> -->
<!-- 				</li> -->
<!-- 				<ol class="dd-list"> -->
<%-- 			</c:when> --%>
<%-- 		</c:choose> --%>
		
<!-- 			<li class="dd-item"> -->
<%-- 				<c:if test="${smallGroup.childrenDepartmentCount > 0 }"> --%>
<!-- 					<button data-action="collapse" type="button" style="display: block;">Collapse</button> -->
<!-- 					<button data-action="expand" type="button" style="display: none;">Expand</button> -->
<%-- 					<c:set var="prevParentId" value="${smallGroup.id }" /> --%>
<%-- 					<c:set var="makeList" value="true" /> --%>
<%-- 				</c:if> --%>
<!-- 				<div class="dd-handle"> -->
<%-- 					<a href="/group/${smallGroup.id}" class="block">${smallGroup.name }</a> --%>
<!-- 				</div> -->
<%-- 			<c:if test="${smallGroup.childrenDepartmentCount == 0 }"> --%>
<!-- 				</li> -->
<%-- 			</c:if> --%>
<%-- 	</c:forEach> --%>
<!-- </div> -->
