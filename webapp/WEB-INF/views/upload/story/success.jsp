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
	업로드가 성공했습니다<br />
	
	이미지 아이디 : <span id="imageFront">${image.id}</span><br />
	
	 담당: 임성묵
</p>

<P>  The time on the server is ${serverTime}. </P>

<script type="text/javascript">

document.domain="${documentDomain }";

window.onload = function() {
	
//	var imageFront = document.getElementById('imageFront').innerHTML;
	if( parent.PicUploader ){
	    parent.PicUploader.onUploadComplete( '${image.jsonString}' );
	}
	else if( parent.storyPicUploader ){
	    parent.storyPicUploader.onUploadComplete( '${image.jsonString}' );
	}	
	else if( parent.profilePicUploader ){
	    parent.profilePicUploader.onUploadComplete( '${image.jsonString}' );
	}
};
</script>

</body>
</html>
