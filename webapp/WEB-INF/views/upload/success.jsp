<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page session="false" %>
<html>
<head>
	<title>CDN</title>
</head>
<body>
<h3>업로드 성공</h3>


결과 JSON : ${cdnJsonResult}

<script type="text/javascript">

document.domain="${documentDomain }";

window.onload = function() {
	if( parent.FileUploader ){
	    parent.FileUploader.onUploadComplete${upid}( '${cdnJsonResult}' );
	}
};
</script>

</body>
</html>
