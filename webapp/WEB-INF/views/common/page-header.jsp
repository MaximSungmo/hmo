<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:choose>
	<c:when test="${param.pageName == 'chat' }">
		<div class="page-header">
			<div class="page-header-inner">
				<div class="page-header-content">
					<div class="header-content">
						<div class="header-section no-tool-buttons">
							<div class="cover-wrap">
								<div class="cover no-headline">
									<div class="cover-image">
										<a href="" class="cover-image-container no-image">
											<img class="cover-photo-img photo img" style="width: auto; height: auto; left: 0px; top: 0px; display: none;" alt="커버 사진" src="">
										</a>
										<div class="cover-border"></div>
									</div>
									<div class="page-name">
										<h2>메세지</h2>
									</div>					
									<div class="header-button-wrap">
										<a	class="hmo-button hmo-button-blue"
											role="dialog"
											aria-control="dialog-chat-user"
											ajaxify-dialog-init="DialogChatUser.onAjaxUserStreamOrder"
											href="/user/stream/order"
											rel="async-get"
											data-request-map='{"status[]":"0,1"}'
											data-title="대화상대 초대"
											data-custom-nm="시작"
											data-custom-fn="DialogChatUser.onStartChat"											
											data-close-fn="DialogChatUser.onClose">
											<i class="fa fa-plus fa-1g"></i>
											<span>새 대화</span>
										</a>									   
									</div>
								</div>
								<div class="header-image">
									<div class="header-image-container">
										<div class="header-image-thumb-icon" id="header-pic-thumb-message">
											<i class="fa fa-comments fa-1g"></i>
										</div>
									</div>
								</div>
							</div>
						</div>	
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
	</c:when>
	<c:when test="${param.pageName == 'inchat' }">
		<div class="page-header">
			<div class="page-header-inner">
				<div class="page-header-content">
					<div class="header-content">
						<div class="header-section no-tool-buttons">
							<div class="cover-wrap">
								<div class="cover no-headline">
									<div class="cover-image">
										<a href="" class="cover-image-container no-image">
											<img class="cover-photo-img photo img" style="width: auto; height: auto; left: 0px; top: 0px; display: none;" alt="커버 사진" src="">
										</a>
										<div class="cover-border"></div>
									</div>
									<div class="page-name _users">
										<h2>
											<c:set value="${ sunny.device.mobile ? 3 : 6 }" var="maxUserCount"/>
											<c:set value="${ channel.userCount > maxUserCount ? maxUserCount : channel.userCount }" var="userCount" />											
											<c:forEach begin="1" end="${userCount }" step="1" var="index">
												${channel.userRelation[index-1].user.name }${ index < userCount ? ", " : ' ' }
											</c:forEach>
											<c:if test="${channel.userCount > maxUserCount }">
												+ ${channel.userCount-maxUserCount  }
											</c:if>	
										</h2>
									</div>					
									<div class="header-button-wrap">
										<a	href="/channel/leave"
											class="hmo-button hmo-button-khaki"
											role="dialog"
											data-style="messagebox-yesno"
											data-title="대화방 나가기"
											data-message="대화방을 나가면 상대방에게 메시지가 전달되며, 앞으로 대화방의 알림을 받을 수 없습니다.<br>나가시겠습니까?"
											ajaxify-dialog-yes="Messenger.onAjaxExitChannel" 
											rel="sync-get"
											data-request-map="{&quot;id&quot;:&quot;${channel.id }&quot;}">
											<i class="fa fa-sign-out fa-1g"></i>
											<span>나가기</span>
										</a>									   
									
										<a	class="hmo-button hmo-button-blue"
										  	role="dialog"
											aria-control="dialog-chat-user"
											ajaxify-dialog-init="DialogChatUser.onAjaxUserStreamOrder"
											href="/user/stream/order"
											rel="async-get"
											data-request-map='{"status[]":"0,1"}'
											data-title="대화상대 초대"
											data-custom-nm="초대하기"
											data-custom-fn="DialogChatUser.onInviteUser"											
											data-close-fn="DialogChatUser.onClose"
											data-channel-id="${channel.id }">
											<i class="fa fa-plus fa-1g"></i>
											<span>초대</span>
										</a>									   
									</div>
								</div>
								<div class="header-image">
									<div class="header-image-container">
										<a href="/message/channels" class="header-image-thumb-icon _invrt" id="header-pic-thumb-message">
											<i class="fa fa-chevron-left fa-1g"></i>
										</a>
									</div>
								</div>
							</div>
						</div>	
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
	</c:when>	
	<c:when test="${param.pageName == 'group' }">
		<c:if test="${ smallGroup.type == 3 }">
		<c:set var="isDepartment" value='true' scope="request"></c:set> 
		</c:if>
		<c:if test="${ smallGroup.type != 3 }">
		<c:set var="isDepartment" value='false' scope="request"></c:set> 
		</c:if>
		<c:if test="${ not authUserIsAdmin && ( empty smallGroupUser || smallGroupUser.admin == false)}">
			<c:set var="isSmallGroupAdmin" value='false' scope="request"></c:set>	
		</c:if>
		<c:if test="${ authUserIsAdmin || ( not empty smallGroupUser && smallGroupUser.admin )}">
			<c:set var="isSmallGroupAdmin" value='true' scope="request"></c:set>	
		</c:if>
		<div class="page-header group-page-header">
			<div class="page-header-inner">
				<div class="page-header-content">
					<div class="header-content">
						<div class="header-section">
							<div class="cover-wrap">
								<div class="cover group-cover">
									<div class="cover-image">
										<a href="" class="cover-image-container">
											<img class="cover-photo-img photo img" style="width: auto; height: auto; left: 0px; top: 0px; display: none;" alt="커버 사진" src="">
										</a>
									</div>
									<div class="u-name">
										<h2>
											<a href="/group/${smallGroup.id}">${smallGroup.name }<span class="alt-name"></span></a>
										</h2>
									</div>					
									<div class="u-status">
										<h2>${smallGroup.description }</h2>
									</div>				
								</div>
								<div class="cover-headline group-headline">
									<div class="cover-headline-wrap">
										<div class="z1 cover-headline-menus setting-navi cover-menu-select">
											<c:if test="${isSmallGroupAdmin==true }">
												<a class="cover-setting-border cv-hl-m-item _f ${param.tabName == 'setting' ? 'selected' : '' }" href="/group/${smallGroup.id}/setting">
													설정
													<%--<c:if test="${param.tabName == 'setting' }">
														<span class="_513x"></span>
													</c:if>--%>
												</a>
											</c:if>
											<a class="cv-hl-m-item _f ${param.tabName == 'story' ? 'selected' : '' }" href="/group/${smallGroup.id}">
												타임라인
												<%--<c:if test="${param.tabName == 'story' }">
													<span class="_513x"></span>
												</c:if>--%>
											</a>
											<a class="cv-hl-m-item _f ${param.tabName == 'user' ? 'selected' : '' }" href="/group/${smallGroup.id}/joined">
												구성원
												<%--<c:if test="${param.tabName == 'user' }">
													<span class="_513x"></span>
												</c:if>--%>
											</a>
											<a class="cv-hl-m-item _l  ${param.tabName == 'about' ? 'selected' : '' }" href="/group/${smallGroup.id}/about" role="button">
												정보
												<%-- <c:if test="${param.tabName == 'about' }">
													<span class="_513x"></span>
												</c:if>--%>
											</a>
											
											
											
										</div>
									</div>				
								</div>
								
								<%-- <div class="profile-pic-container">
									<div class="photo-box">
										<div class="profile-pic-thumb">
											<img class="profile-photo img" src="/assets/sunny/2.0/img/profile-pic-division-default.png">
											<div class="profile-pic-border">
												<div class="photo-uploading-indicator"></div>
											</div>	
										</div>
									</div>	
								</div>	
								--%>
							</div>
						</div>	
					</div>
				</div>
			</div>
		</div>	
	</c:when>
	<c:when test="${param.pageName == 'bug' }">
		<div class="page-header bug-wrap">
			<div class="page-header-inner">
				<div class="page-header-content">
					<div class="header-content">
						<div class="header-section no-tool-buttons">
							<div class="cover-wrap">
								<div class="cover no-headline">
									<div class="cover-image">
										<a href="" class="cover-image-container no-image">
											<img class="cover-photo-img photo img" style="width: auto; height: auto; left: 0px; top: 0px; display: none;" alt="커버 사진" src="">
										</a>
										<div class="cover-border"></div>
									</div>
									<div class="page-name" style="line-height:16px">
										<span>다음은 현재 서비스에서 흔히 발생하는 문제입니다. 문제들은 해결 중에 있습니다. 문제 해결에 도움이 되도록 자세한 내용을 보내주시려면  <a href="/feedback">버그신고하기</a>를 이용해 주시기 바랍니다.</span>
									</div>					
								</div>
								<div class="header-image">
									<div class="header-image-container">
										<div class="header-image-thumb-icon" id="header-pic-thumb-message">
											<i class="fa fa-bug fa-1g"></i>
										</div>
									</div>
								</div>
							</div>
						</div>	
					</div>
				</div>
			</div>
		</div>	
	</c:when>
</c:choose>
