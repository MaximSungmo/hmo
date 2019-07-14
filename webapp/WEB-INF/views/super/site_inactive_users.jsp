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
						<c:param name="menuName">inactive_users</c:param>
					</c:import>			
					<!-- END:nav-list -->
				</div>					
				<!-- END:sidebar -->
				
				<!-- BEGIN:main-content -->		
				<div class="main-content z1">			
					<div class="page-content z1">
						<div class="rw-content-area-wrap">
							<div class="rw-content-area">
								<div class="rw-pagelet-blank"></div>
								<div class="rw-pagelet-wrap">
																	
									<c:forEach items="${siteInactiveUsers }" var="siteInactiveUser">
										<div style="border: 1px solid #eee; margin: 10px; min-height: 30px; ">
											<div style="float:left; ">
												${siteInactiveUser.id }-${siteInactiveUser.name }<c:if test="${not empty siteInactiveUser.site }"> - 대상 사이트  : ${siteInactiveUser.site.companyName }</c:if><br />
												${siteInactiveUser.email }${siteInactiveUser.createDate }
											</div>
											<form method="post" action="#" style="float:right; "  onsubmit="return inlineRemoveSubmit( this, event );">
												<input type="hidden" name="id" value="${siteInactiveUser.id }">
												<input type="hidden" name="sunny" value="sunnyvale">
												<input type="submit" value="삭제하기">
											</form>	
											<form method="post" action="#" style="float:right; "  onsubmit="return inlineConfirmSubmit( this, event );">
												<input type="hidden" name="id" value="${siteInactiveUser.id }">
												<input type="submit" value="인증하기">
											</form>		
											<div style=" clear:both;  "></div>
										</div>
									</c:forEach>
								</div>
							</div>
						</div>
						<div class="rw-right-col">
							<div class="rw-pagelet-blank"></div>
							<!-- BEGIN:rightcol -->
							<c:import url="/WEB-INF/views/common/right-col.jsp">
							</c:import>
							<!-- END:rightcol -->
						</div>				
					</div>
				</div>
			<!-- END:main-content -->		
			</div>
		</div>
	</div>
<script>	
function inlineRemoveSubmit( form, event ) {	
	var 
	$form = $(form);
	var isConfirmed = confirm("정말 삭제하시겠습니까?");

	if( isConfirmed == true ){
		$form.get(0).setAttribute("action", "/super/remove_inactive_user");
	}
	
	return isConfirmed;
}

function inlineConfirmSubmit( form, event ) {	
	var 
	$form = $(form);
	var isConfirmed = confirm("정말 승인하시겠습니까?");

	if( isConfirmed == true ){
		$form.get(0).setAttribute("action", "/super/confirm_inactive_user");
	}
	
	return isConfirmed;
}
</script>
</body>
</html>