<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

					<%--
					<div class="widget-box transparent">
						<div class="widget-header widget-header-small  header-color-grey">
							<h4 class="blue smaller">
								<i class="fa fa-volume-up fa-1g grey"></i>
								<span class="rw-title">공지사항</span>
							</h4>
							<div class="widget-toolbar action-buttons">
								<a href="#" onclick="return refresh_right_notice(this);">
									<i class="fa fa-refresh fa-1g blue"></i>
								</a>
							</div>
						</div>
						<div class="widget-body">
							<div class="widget-main padding-8">
								<div class="slimScrollDiv" style="position: relative; overflow: hidden; width: auto; height: 330px;">
									<div id="right-notice-changeable" class="profile-feed" style="overflow: hidden; width: auto; height: 330px;">
										<c:import url="/WEB-INF/views/pagelet/right_notice.jsp">
										</c:import>
									</div>
								</div>
							</div>
						</div>
					</div>
					<script>
					function refresh_right_notice(){
						$("#right-notice-changeable").html('<i class="fa fa-spin fa-spinner"></i>');
						$.get("/pagelet/${sunny.site.lobbySmallGroup.id}/right_notice", function(data, status){
							$("#right-notice-changeable").html(data);
						});
						return false; 
						
					}
					</script>
					 --%>
					 
					 

<c:if test="${authUserIsAdmin == true}">
<div class="user-management">
	<div class="user-management-title">
		mini 관리 메뉴
	</div>
	<div class="user-management-content">
		<div class="overall-management">
			<div class="user-management-invite">
				<a href="/a/site">전체관리 바로가기</a>
			</div>
		</div>
		<div class="department-management">
			<div class="user-management-invite">
				<a href="/a/department">부서관리 바로가기</a>
			</div>
		</div>
		<div class="user-management-content-top">
			<div class="user-management-invite">
				<div>
				<a class="btn-new-user" id="">구성원 초대하기</a>
				</div>
				<div>
				<a class="invite-user-link" href="/a/site/invite_users">초대된 사용자보기</a>	
				</div>
			</div>
		</div>
		<div class="user-management-content-bottom">
			<div class="user-management-signup">
				<div class="user-management-signup-title">가입신청</div>
				<a class="user-management-refresh" href="#" onclick="return refresh_right_requests(this);">
					<i class="fa fa-refresh fa-1g blue"></i>
				</a>
			</div>
			<div class="slimScrollDiv user-management-signup-list-wrap">
				<div id="right-requests-changeable" class="profile-feed user-management-signup-list">
<%-- 					<c:import url="/WEB-INF/views/pagelet/right_requests.jsp"> --%>
<%-- 					</c:import> --%>
				</div>
		</div>
		</div>
		
	</div>
</div>

</c:if>

<div class="tag-list-wrap">
	<div class="tag-list-wrap-title">
		자주 사용되는 태그
	</div>
	<div class="tag-list-content">
		<c:choose>
			<c:when test="${not empty currentInfo.topTags }">
				<div class="hmo-tag-wrap">
				<c:forEach items="${currentInfo.topTags }" var="tag" varStatus="status">
				
				<%-- 짝수일때 --%>
				<c:if test="${status.index % 2 == 0}">
					
				</c:if>
					<div style="display:inline-block;width:96px;margin-bottom:11px;">
						<a href="#" class="hmo-tag-blue hmo-tag generated-hash">${tag.title}</a> <span class="tag-count">x ${tag.referenceCount }</span>
					</div>
				<%-- 홀수일때 --%>
				<c:if test="${status.index % 2 == 1 || status.last }">
					
				</c:if>
				</c:forEach>
				</div>	
			</c:when>
				<c:otherwise>
					태그가 없습니다.
				</c:otherwise>
		</c:choose>
	</div>
</div>

<div class="widget-box transparent">
<div class="app-download-wrap">
			<a class="app-download" href="https://play.google.com/store/apps/details?id=kr.co.sunnyvale.android.hmo.nc" target="_blank"></a>
			<span class="app-download-new"></span>
		</div>
</div>
<div class="widget-box transparent">
<div class="ios-app-download-wrap">
			<a class="ios-app-download" href="https://itunes.apple.com/us/app/hellomaiopiseu-hello-my-office/id882662778?mt=8" target="_blank"></a>
		</div>
</div>
<div class="widget-box transparent">
	<div class="mobile-copyright">
		<div class="copyright-2014">
			<span class="menu-text">Hello my office &copy; 2014 <span class="copyright-beta"></span><br></span>제휴문의: <a href="http://www.sunnyvale.co.kr" target="_blank">(주)써니베일</a></span>
		</div>
	</div>
</div>
<c:if test="${authUserIsAdmin == true}">
<script>
	function refresh_right_requests(){
		$("#right-requests-changeable").html('<i class="fa fa-spin fa-spinner"></i>');
		$.get("/pagelet/${sunny.site.lobbySmallGroup.id}/right_requests", function(data, status){
			$("#right-requests-changeable").html(data);
			$("#right-requests-changeable .livetimestamp").each(refresh_timesince);
		});
		return false; 
	}
	$(function(){
		refresh_right_requests();
	});
	function deny_inactive_user(data){
		if( data.result == "fail"){
			MessageBox(data.message, data.message, MB_ERROR);
			return false;
		}
		$(this).closest(".user-list").fadeOut('slow');
	}

	function accept_inactive_user(data){
		if( data.result == "fail"){
			MessageBox(data.message, data.message, MB_ERROR);
			return false;
		}
		$(this).closest(".user-list").fadeOut('slow');
	}

</script>
</c:if>					
					 
					 
					 