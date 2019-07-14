<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title"></c:param>
	<c:param name="bsUsed">NO</c:param>
	<c:param name="aceUsed">NO</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>
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
			<div class="main-container container-fluid z1" id="main-container">
				
				<!-- BEGIN:sidebar -->
				<div class="sidebar" id="snn-sidebar">
					<!-- BEGIN:nav-list -->
					<c:import url="/WEB-INF/views/common/nav-list-super.jsp">
						<c:param name="menuName">email</c:param>
					</c:import>			
					<!-- END:nav-list -->
				</div>					
				<!-- END:sidebar -->
				
				<!-- BEGIN:main-content -->		
				<div class="main-content z1">			
					<div class="page-content z1">
						<div class="rw-content-area-wrap">
							<div class="rw-content-area" style="margin-right:0px; ">
								<div class="rw-pagelet-blank"></div>
								<div class="rw-pagelet-wrap">
									<div id="main-list-wrap" style="margin: 10px 10px 0 10px;">
										<c:import url="/WEB-INF/views/super/pagelet/emails.jsp"></c:import>
									</div>
									
								</div>
								
								<div class="rw-pagelet-blank"></div>
								<div class="rw-pagelet-wrap">
								
									<form id="myForm" action="#" method="POST" onsubmit="return inlineSubmit( this, event );">
										<div>
											<label for="sendEmail">
												보내는 이메일 : 					
											</label>
											<input type="email" id="sendEmail" name="sendEmail" class="required">
										</div>
										<div>
											<label for="siteId">
												사이트 아이디
											</label>
											<select id="siteId" name="siteId" style="height: 30px; ">
												<option value="-1">사이트를 선택해주세요</option>
												<option value="">전체사이트</option>
												<c:forEach items="${sites }" var="site">
													<option value="${site.id }">${site.id } - ${site.companyName }</option>
												</c:forEach>
											</select>
										</div>
										<div>
											<input type="checkbox" id="onlyAdmin" name="onlyAdmin">
											<label for="onlyAdmin">
												관리자에게만 보내기(체크되지 않으면 퇴사중이 아닌, 이메일 받기 체크한 모두에게 보냅니다.)
											</label>
										</div>
										<div>
											<label for="title">
												제목
											</label>
											<input type="text" id="title" name="title" style="width:80%;" class="required">
										</div>
										<div>
											<label for="text">
												내용을 입력해주세요
											</label>
											<textarea class="required" id="text" name="text" style="width:100%; min-height: 300px;"></textarea>
										</div>
										<div>
											<input type="submit" class=" hmo-button hmo-button-blue" value="전송">
										</div>
									</form>
								</div>
								
							</div>
						</div>
					</div>
				</div>
			<!-- END:main-content -->		
			</div>
		</div>
	</div>
	
	<script src="/assets/tinymce/tinymce.min.js"></script>
	<script>
	function inlineSubmit( form, event ) {	
		var 
		$form = $(form);

		if( $("#siteId").val() == -1 ){
			alert("사이트를 선택해주세요");
			return false; 
		}
		var isValid=$form.valid();
		
		if( isValid == false ){
			return false;;
		}

		var isConfirmed = confirm("정말 전송하시겠습니까??");
		
		if( isConfirmed == true ){
			$form.get(0).setAttribute("action", "/super/send_email");
		}
		
		return isConfirmed;
	}
	$(document).ready(function(){
// 		$("#myForm").validate();
		
		tinymce.init({
			selector: '#text',
			language : 'ko_KR',
			plugins: "autolink lists charmap preview hr anchor pagebreak visualblocks visualchars nonbreaking contextmenu directionality fullscreen table template paste textcolor code",
			toolbar1: "undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent ",
			toolbar2: "fullscreen preview | forecolor backcolor | template code",
		    tools: "inserttable"
		    ,
		    template_cdate_classes: "cdate creationdate",
		    template_mdate_classes: "mdate modifieddate",
		    template_selected_content_classes: "selcontent",
		    template_cdate_format: "%m/%d/%Y : %H:%M:%S",
		    template_mdate_format: "%m/%d/%Y : %H:%M:%S"

		});
		
	});
	</script>
</body>
</html>