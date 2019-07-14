<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h3> 야캠프 CDN 서비스 </h3>
<p>
	<h1>업로드가 실패했습니다.</h1>
	에러 내용 : <span id="failureMessage">${failureMessage}</span><br /><br />	 
	 담당: 임성묵
</p>

<P>  The time on the server is ${serverTime}. </P>

<script type="text/javascript">

document.domain="${documentDomain }";


window.onload = function() {
	
	var failureMessage = document.getElementById('failureMessage').innerHTML;
	if( parent.PicUploader ){
	    parent.PicUploader.onUploadFailure( failureMessage );
	}
	else if( parent.PicUploader ){
	    parent.PicUploader.onUploadFailure( failureMessage );
	}	
	else if( parent.profilePicUploader ){
	    parent.profilePicUploader.onUploadFailure( failureMessage );
	}

};
</script>

</body>
</html>
