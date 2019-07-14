<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone=no">

<!-- BEGIN:basic bootstrap styles -->
<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/bootstrap.css" />
<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/bootstrap-responsive.css" />


	<script src="/assets/sunny/2.0/js/uncompressed/jquery-1.10.2.min.js"></script>
	<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/bootstrap.js"></script>
<%-- BEGIN:부서이름 검색을 위한 typemanager --%>
<!-- <script src="/assets/sunny/2.0/js/uncompressed/bootstrap-tagsinput.js"></script> -->
<script src="/assets/sunny/2.0/js/uncompressed/typeahead.js"></script>
<!-- <script src="/assets/sunny/2.0/js/uncompressed/tagmanager.js"></script> -->
<script src="http://twitter.github.com/hogan.js/builds/2.0.0/hogan-2.0.0.js"></script>
<link href="/assets/sunny/2.0/css/uncompressed/typeahead.css" rel="stylesheet"  />

<script src="/assets/sunny/2.0/js/uncompressed/jquery.tokeninput.js"></script>

<%--END:부서이름 검색을 위한 typemanager --%>


<script>

$(function(){
	
$("#departmentParentName").tokenInput("/match/department", {
	hintText: "부서 이름을 입력해주세요.",
	noResultsText: "부서가 없습니다",
	searchingText: "검색중",
	queryParam: "key",
	method: "GET",
	tokenLimit: "1",
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
//				newData[totalCount] = {
//					name:$("#token-input-tag-input").val(),
//					referenceCount:-1
//				}
		}

	return newData;
//			return data;
	},
	resultsFormatter: function(result){
		return "<li>" + result.name + "</li>"
	},
    tokenFormatter: function(result) { 
    	return "<li class='post-tags'><label class='title'>" + result.name + "</label></li>" 
    }
});


});
</script>

</head>

<body>
<input class="typeahead" style="display:block;float:left" type="text" name="departmentParentName" id="departmentParentName" placeholder="상위부서 이름을 검색하여 주세요.">

</body>
</html>
