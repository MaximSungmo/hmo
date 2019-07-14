<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page session="false" %>
<html>
<head>
	<title>CDN</title>
</head>
<body>
<h3> hellomyoffice CDN 서비스 </h3>

	<form id="form-upload" action="/upload/profile"
		enctype="multipart/form-data" method="post" target="upload_target">
		<div class="file-uploader">
			<button>업로드</button>
			<input type="file" id="file" name="profilePic" />
		</div>
	</form>
	<iframe id="upload_target" name="upload_target" src="#" style="width: 500px; height: 5000px; border: 1px solid #111;">
	</iframe>

<script type="text/javascript" src="/assets/js/libs/jquery-1.7.2.js" ></script>
<script type="text/javascript" src="/assets/js/libs/yajs.js" ></script>
<script type="text/javascript" src="/assets/js/apps/app_upload.js" ></script>
</body>
</html>
