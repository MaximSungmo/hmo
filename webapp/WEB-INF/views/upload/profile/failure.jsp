<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
	<h1>업로드가 실패했습니다.</h1>
	<span>jsonString : ${cdnJsonResult }</span>

<script type="text/javascript">

document.domain="${documentDomain}";

window.onload = function() {
	alert( '${cdnJsonResult}' );
};

</script>

</body>
</html>
