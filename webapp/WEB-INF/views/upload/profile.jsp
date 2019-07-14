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
	 프로필 업로드 ( 리사이징 / 크롭 기능 포함  )<br/>
	<form id="form-upload" action="<c:url value='/upload/profile'/>" enctype="multipart/form-data" method="post" target="upload_target">
		<div class="file-uploader">
			<input type="button" value="프로필 업로드" />
			<input type="file" id="file" name="file"/>
		</div>	
	</form>
	<iframe id="upload_target" name="upload_target" src="#" style="width: 500px; height: 5000px; border: 1px solid #111;">
	</iframe>	 
	 담당: 임성묵
	 
	 
	 <span id="data">${data }</span>
</p>

<P>  The time on the server is ${serverTime}. </P>

<script type="text/javascript" src="<c:url value='/assets/js/libs/jquery-1.7.2.js'/>" ></script>
<script type="text/javascript" src="<c:url value='/assets/js/libs/yajs.js'/>" ></script>
<script type="text/javascript" src="<c:url value='/assets/js/apps/app_upload.js'/>" ></script>

</script>

</body>

</html>
