<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">결재 : ${content.title }</c:param>
	<c:param name="bsUsed">NO</c:param>
	<c:param name="aceUsed">NO</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>


<link type="text/css" rel="stylesheet" href="/assets/sunny/2.0/css/uncompressed/long-text.css" />
<!-- <link rel="stylesheet" href="/assets/sunny/2.0/css/uncompressed/story-writer.css"> -->
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
		<div class="main-container container-fluid">

			<!-- BEGIN:sidebar -->
			<div class="sidebar" id="snn-sidebar">

				<!-- BEGIN:welcome-box -->
				<c:import url="/WEB-INF/views/common/welcome-box.jsp">
				</c:import>
				<!-- END:welcome-box -->

				<!-- BEGIN:nav-list -->
				<c:import url="/WEB-INF/views/common/nav-list.jsp">
					<c:param name="menuName">전자결재</c:param>
				</c:import>
				<!-- END:nav-list -->

			</div>
			<!-- END:sidebar -->

			<!-- BEGIN:main-content -->
			<div class="main-content">

				<!-- BEGIN:breadcrumbs&search -->
				<c:set var="breadcrumbs" value="결재뷰" scope="request" />
				<c:set var="breadcrumbLinks" value="#" scope="request" />
				<div class="breadcrumbs" id="breadcrumbs">
					<c:import url="/WEB-INF/views/common/breadcrumbs.jsp">
						<c:param name="breadcrumbs">${breadcrumbs }</c:param>
						<c:param name="breadcrumbLinks">${breadcrumbLinks }</c:param>
					</c:import>
				</div>
				<!-- END:breadcrumbs&search -->

				<!-- BEGIN:page-content -->
				<div class="page-content">
					<div class="rw-content-area-wrap approval-rw-content-area-wrap">
						<div class="rw-content-area">
							
<%-- 							<c:if test="${not empty smallGroup }"> --%>
							
<!-- 								<div class="rw-pagelet-blank"></div> -->
<!-- 								<div class="rw-pagelet-wrap"> -->
<!-- 									<div id="group-cover-col"> -->
									
<%-- 											<c:import url="/WEB-INF/views/pagelet/group/cover.jsp"> --%>
<%-- 												<c:param name="tab">전자결재</c:param> --%>
<%-- 											</c:import> --%>
<!-- 									</div> -->
<!-- 								</div> -->
<%-- 							</c:if> --%>
							
							<div class="rw-pagelet-blank"></div>
							
							<div class="rw-pagelet-wrap approval-view-content" style="clear:both;">
								<div class="z1 approval-view-content-header">
									<h2>${content.title }</h2>
									<div class="approval-view-content-info">
										<c:choose>
											<c:when test="${content.status == 0 }">
												<span class="label-green">진행중</span>
											</c:when>
											<c:when test="${content.status == 1 }">
												<span class="label-red">반려됨</span>
											</c:when>
											<c:when test="${content.status == 2 }">
												<span class="label-blue">승인됨</span>
											</c:when>
											
										</c:choose>
										<c:if test="${content.inRequestReject == true }">
											<span class="label-red">반려 요청중</span>
										</c:if>
									</div>
								</div>
								<div class="row-fluid approval-view-content-write-info">
									<div class="span12">
										<div class="pull-left">
										<a href="/user/${content.user.id}" style="text-align: right; ">
											<img src="${content.user.profilePic }"/>
											<span class="approval-view-content-writer">${content.user.name}</span>
										</a>
										</div>
										<span class="bar">|</span>
										<span class="approval-view-content-creatdate">
											작성일 : <fmt:formatDate value='${content.createDate }' pattern="yyyy/MM/dd hh:mm:ss" /> 
										</span>
										<span class="bar">|</span>
										<span class="approval-view-content-hits">
											조회 ${content.contentReadCount.count }
										</span>
										
										<div class="rw-pagelet-wrap approval-view-header-right-menu">
								<div class="lighter blue">
									<div class="hmo-button-group-wrap">
										<c:choose>
											<c:when test="${ empty prevApproval }">
												<span class="hmo-button-group-text hmo-button-group-item hmo-button-group-middle-7 l-ft hmo-button-white hmo-button-group firstItem"><a href="#">이전글</a></span>	
											</c:when>
											<c:otherwise>
												<span class="hmo-button-group-text hmo-button-group-item hmo-button-group-middle-7 l-ft hmo-button-white hmo-button-group firstItem"><a href="${groupPath}/approval/${prevApproval.id}" class="">이전글</a></span>	
											</c:otherwise>
										</c:choose>
										<span class="hmo-button-group-text hmo-button-group-item hmo-button-group-middle-7 l-ft hmo-button-white hmo-button-group"><a href="${groupPath}/approval" class="">목록</a></span>
										<c:choose>
											<c:when test="${ empty nextApproval }">
												<span class="hmo-button-group-text hmo-button-group-item hmo-button-group-middle-7 hmo-button-white hmo-button-group"><a href="#">다음글</a></span>
											</c:when>
											<c:otherwise>
												<span class="hmo-button-group-text hmo-button-group-item hmo-button-group-middle-7 hmo-button-white hmo-button-group"><a href="${groupPath}/approval/${nextApproval.id}" class="content-navigation">다음글</a></span>	
											</c:otherwise>
										</c:choose>
										
									</div>	
								</div>
							</div>
									</div>
									
									
								</div>
								
								<div class="z1 long-text-view approval-view-content-body">
									${content.taggedTextPrev }
									${content.taggedTextNext }
								</div>
								
								<div class="z1">
									<div class="pull-left">
									<c:if test="${authUserId == content.user.id }">
									<div class="lighter blue">
<!-- 									<ul class="nav nav-pills"> -->
<%-- 												<li><a href="${groupPath}/approval/edit/${content.id}">수정</a> </li>	 --%>
<!-- 												<li><a href="/content/delete" -->
<!-- 										   ajaxify="ajax_delete_content" -->
<!-- 										   rel="sync-get" -->
<%-- 										   data-request-map='{"id":"${content.id }"}' >삭제</a></li> --%>
<!-- 									</ul> -->
									</div>
										
									</c:if>
									</div>
									
								</div>
								
								<c:if test="${content.mediaCount > 0 }">
								<div class="approval-view-content-media-wrap">
									<div class="approval-view-media-right">
										파일 <span class="mediacount">${content.mediaCount }</span>개
									</div>
									<div class="approval-view-media-left">
										<ul class="attachment-list pull-left unstyled">
											<c:forEach items="${mediaes }" var="media">
												<li> 
													<a href="/download?id=${media.id}" class="approval-view-media-name">
														<i class="fa fa-file-alt bigger-110 middle"></i>
														<span class="attached-name middle">${media.fileName }</span>
													</a>
														<a class="approval-view-media-download" href="/download?id=${media.id}">
															<i class="fa fa-download bigger-125 blue"></i>
														</a>
												</li>
											</c:forEach>
										</ul>
									</div>
								</div>
								</c:if>
								
								
								<div  class="approval-view-content-approver">
									<div class="z1" >
										<c:forEach items="${content.smallGroupApprovals }" var="smallGroupApproval" varStatus="status">
											<c:if test="${content.smallGroupApprovals[status.index - 1].type != smallGroupApproval.type || status.first }">
												<c:choose>
													<c:when test="${smallGroupApproval.type == 0 }">
														<div class="approval-view-content-approver-title">
														승인자 
														</div>	
													</c:when>
													<c:when test="${smallGroupApproval.type == 1 }">
														<div class="hr" style="clear:both;"></div>
															<div class="approval-view-content-approver-title2">
																협조처
															</div>
													</c:when>
													<c:when test="${smallGroupApproval.type == 2 }">
														<div class="hr" style="clear:both;"></div>
															<div class="approval-view-content-approver-title2">
																수신처
															</div>
													</c:when>
													<c:when test="${smallGroupApproval.type == 3 }">
														<div class="hr" style="clear:both;"></div>
															<div class="approval-view-content-approver-title2">
																회람
															</div>
													</c:when>
												</c:choose>
											</c:if>
											<div  class="approval-view-content-approver-list-wrap">
												<div class="approval-view-content-approver-list">
													
													<img src="${smallGroupApproval.smallGroup.onlyMineUser.profilePic }"/>
													<c:if test="${story.approval.checkOrdering }">
														<span class="label-yellow-b">${status.index + 1 }</span>
													</c:if>
												</div>
												${smallGroupApproval.smallGroup.onlyMineUser.name }
												<div class="approval-view-content-approver-list-action">
														<c:choose>
																<c:when test="${ smallGroupApproval.status == 0 }">
																	<span class="label-yellow-b">
																		<i class="fa fa-ellipsis-h"></i>
																		보기 전
																	</span>	
																</c:when>
																<c:when test="${ smallGroupApproval.status == 1 }">
																	<span class="label-green-b">
																		<i class="fa fa-spinner "></i>
																		진행중
																	</span>
																</c:when>
																<c:when test="${ smallGroupApproval.status == 2 }">
																	<span class="label-blue-b">
																		<i class="fa fa fa-circle-o"></i>
																		<c:choose>
																			<c:when test="${smallGroupApproval.type ==0 }">
																				승인함
																			</c:when>
																			<c:when test="${smallGroupApproval.type ==1 }">
																				협조함
																			</c:when>
																			<c:when test="${smallGroupApproval.type ==0 }">
																				확인함
																			</c:when>
																		</c:choose>
																		
																	</span>	
																</c:when>
																<c:when test="${ smallGroupApproval.status == 3 }">
																	<span class="label-red-b">
																		<i class="fa fa-times"></i>
																		반려
																	</span>
																</c:when>
															</c:choose>
													</div>
											</div>
										</c:forEach>
									</div>
								</div>
								
								
								<div class="approval-view-content-button-wrap" style="text-align:right; ">
									<c:choose>
										<c:when test="${content.user.id == authUserId}">
											<a href="/approval/cancel_request_reject" class="hmo-button hmo-button-blue hmo-button-small-10 request cancel ${content.inRequestReject == true && content.status != 1 ? '' : 'hidden-elem'}" rel="sync-get" ajaxify="ajax_approval_cancel_request_reject" data-request-map='{"aid":"${content.id }"}'>반려 요청 취소</a>
											<a href="/approval/request_reject" class="hmo-button hmo-button-blue hmo-button-small-10 request ${content.inRequestReject == false && content.status == 0 ? '' : 'hidden-elem'}" rel="sync-get" ajaxify="ajax_approval_request_reject" data-request-map='{"aid":"${content.id }"}'>반려 요청</a>
											
										</c:when>
										<c:when test="${smallGroupApproval.type == 0 }">
											
											<a href="/approval/reject" class="hmo-button hmo-button-small-10 hmo-button-white reject ${smallGroupApproval.status != 1 ? 'hidden-elem' : ''}" 
													rel="sync-get" 
													ajaxify-dialog-yes="ajax_approval_reject" 
													data-title="결재 반려"
													role="dialog" 
													data-style="messagebox-yesno"
													data-label="반려"
													data-message="반려하게 되면 취소할 수 없습니다.<br>이 결재를 반려하시겠습니까?"data-request-map='{"aid":"${content.id }", "sgaid":"${smallGroupApproval.id }"}'>반려</a>
											<a href="/approval/ok" 
													class="hmo-button hmo-button-blue hmo-button-small-10 approve ${smallGroupApproval.status != 1 ? 'hidden-elem' : ''}" 
													rel="sync-get" 
													data-title="결재 승인"
													ajaxify-dialog-yes="ajax_approval_ok" 
													data-request-map='{"aid":"${content.id }", "sgaid":"${smallGroupApproval.id }"}' 
													role="dialog" 
													data-style="messagebox-yesno"
													data-label="승인"
													data-message="승인하게 되면 취소할 수 없으며 승인라인이 존재하는 경우 다음 승인자에게 결재가 상신됩니다.<br>승인 하시겠습니까?">승인</a>
											<a href="#" class="hmo-button hmo-button-blue hmo-button-small-10 approved ${smallGroupApproval.status == 2 ? '' : 'hidden-elem'}">승인함</a>
											<a href="#" class="hmo-button hmo-button-blue hmo-button-small-10 rejected ${smallGroupApproval.status == 3 ? '' : 'hidden-elem'}">반려함</a>
										</c:when>
										<c:when test="${smallGroupApproval.type == 1 }">
											<a href="/approval/ok" class="hmo-button hmo-button-blue hmo-button-small-10 ${smallGroupApproval.status == 1 ? '' : 'hidden-elem'}" 
													rel="sync-get" 
													ajaxify-dialog-yes="ajax_approval_ok" 
													role="dialog" 
													data-style="hmo-messagebox-yesno"
													data-label="협조하기"
													data-message="협조하면 취소할 수 없습니다.<br>이 결재에 협조하시겠습니까?"data-request-map='{"aid":"${content.id }", "sgaid":"${smallGroupApproval.id }"}'>협조</a>
											<a href="#" class="hmo-button hmo-button-blue hmo-button-small-10 approved ${smallGroupApproval.status == 2 ? '' : 'hidden-elem'}">협조됨</a>
										</c:when>
										<c:when test="${smallGroupApproval.type == 2 }">
											<a href="/approval/ok" class="hmo-button hmo-button-blue hmo-button-small-10 ${smallGroupApproval.status == 1 ? '' : 'hidden-elem'}" 
													rel="sync-get" 
													ajaxify-dialog-yes="ajax_approval_ok" 
													role="dialog" 
													data-style="hmo-messagebox-yesno"
													data-label="수신확인"
													data-message="수신확인을 하면 취소할 수 없습니다.<br>이 결재를 수신확인 하시겠습니까?"
													data-request-map='{"aid":"${content.id }", "sgaid":"${smallGroupApproval.id }"}'>수신확인</a>
											<a href="#" class="hmo-button hmo-button-blue hmo-button-small-10 approved ${smallGroupApproval.status == 2 ? '' : 'hidden-elem'}">수신확인함</a>
										</c:when>
										<c:when test="${smallGroupApproval.type == 3 }">
										</c:when>
									</c:choose>
								</div>
								<div class="widget-main _act"">
									<span class="ui-action-links-bottom react-icons-${content.id }">
										<c:if test="${isAuthenticated==true  && ( empty smallGroup || not empty smallGroupUser )}">
											<span class="story-react">
												<span class="react-icon emo-grin <c:if test='${content.feeledId == 1}'>reacted</c:if>"></span>
												<a	href="/feel/okay" role="radio"
													class="feel-radio-react-del-${content.id }"
													data-ft-class="feel-radio-react-del-${content.id }"
													ajaxify="ajax_feel_toggle"
													aria-checked="${content.feeledId == 1 ? 'true':'false'}"
													aria-controls="feel-counter-${content.id }"
													rel="sync-get"
													data-ft="${content.id }"
													data-request-map="{&quot;contentId&quot;:&quot;${content.id }&quot;,&quot;feelId&quot;:&quot;1&quot;}">
													좋아요 <span class="react-icon <c:if test='${content.feeledId == 1}'>reacted</c:if>">취소</span></a>
											</span>
								
										</c:if>
									</span>
								</div>

								
								<c:set value="${(empty content.dynamic.feelCount || content.dynamic.feelCount < 0) ? 0:content.dynamic.feelCount}" var="feelCount" />
								<div class="ui-comment-container approval-ui-comment-container">
									<div class="cmnt-row-wrap" ${feelCount == 0 ? "style='display:none'" : "" }>
										<div class="timeline-feedback-actions _b cearfix">
											<span> 
												<a	class="pop-feeled-users feel-counter-${content.id }"
													rel="sync-get" href="/feel/users"
													data-request-map="{&quot;contentId&quot;:${content.id },&quot;feelId&quot;:null,&quot;top&quot;:true,&quot;size&quot;:20}"
													data-count="${feelCount }">
													<i class="fa fa-heart fa-1g"></i>
													<span class="count-text">${feelCount }명</span>
												</a><span>이 평가했습니다.</span>
											</span>
										</div>
									</div>
								
									<div class="rw-comment-container">
										<c:if test="${content.dynamic.replyCount > 3 }">
											<c:set value="10" var="requestReplyCount" />
											<c:set value="${content.dynamic.replyCount-3 }" var="remainReplyCount" />
											<div class="cmnt-row-wrap">
												<div class="timeline-feedback-actions _b cearfix">
													<span> 
														<a	rel="async-get"
															href="/reply/stream"
															ajaxify="ajax_more_reply"
															ajaxify-before="before_ajax_more_reply"
															data-reply-count="${remainReplyCount }"
															data-request-map="{ &quot;top&quot;:true, &quot;contentId&quot;:${content.id },&quot;replyId&quot;:${content.replys[0].id },&quot;size&quot;:${requestReplyCount } }">
															<i class="fa fa-comment fa-1g"></i>
															<i class="fa fa-spin fa-1g fa-spinner" style="display:none"></i>
															<span>
																<c:choose>
																	<c:when test="${remainReplyCount > requestReplyCount }">지난 댓글 더 보기</c:when>
																	<c:otherwise>댓글 ${remainReplyCount }개 더 보기 </c:otherwise>
																</c:choose>
															</span>
														</a>
													</span>
												</div>
											</div>							
										</c:if>
										<ul class="uc-container">
											<c:forEach items="${content.replys }" var="reply">
											<li class="ui-react-row">
												<div class="z1">
													<div class="cmnt-profile-box">
														<a	href="/user/${reply.user.id }"
															data-hovercard="">
															<img src="${reply.user.profilePic }" alt="" class="cmmnt-profile-pic"></a>
													</div>
													<div>
														<div class="image-block-content">
															<c:if test="${isAuthenticated==true && reply.user.id==authUserId }">
																<div class="ui-button-opa-block">
																				<a	href="/reply/delete"
																					role="dialog"
																					data-style="messagebox-yesno"
																					data-title="댓글 삭제"
																					data-message="삭제되면 복구할 수 없습니다.<br>이 댓글을 삭제하시겠습니까?"
																					ajaxify-dialog-yes="Reply.onAjaxReplyDelete" 
																					rel="sync-get"
																					data-request-map="{&quot;id&quot;:&quot;${reply.id }&quot;}"
																					class="ui-button-opa-a ui-comment-close-button">
																					<i class="fa fa-times fa-1g"></i>
																				</a>
																			</div>	
															</c:if>
															<div>
																<div>
																	<div class="ui-comment-content">
																		<a href="/user/${reply.user.id }"	data-hovercard="" class="ui-comment-user-name">${reply.user.name }</a>
																		<c:choose>
																			<c:when test="${not empty reply.taggedTextNext }">
																				<div id="reply-exposed-${reply.id }">
																					<div>${reply.taggedTextPrev}</div>
																					<span class="text-exposed-hide">...</span>
																					<span class="text-exposed-show">${reply.taggedTextNext}<br/><br/></span>
																					<span class="text-exposed-hide">
																						<span class="text-exposed-link">
																							<a href="#" onclick="css_add_class($(&quot;#reply-exposed-${reply.id }&quot;), &quot;text-exposed&quot;); return false;">더보기</a>
																						</span>
																					</span>
																				</div>
																			</c:when>
																			<c:otherwise>
																				<span>${reply.taggedTextPrev}</span>
																			</c:otherwise>
																		</c:choose>																	
																	</div>
																	<div class="ui-comment-actions">
																		<span>
																			<span> </span>
																			<a	href="#"
																				class="ui-link-subtle">
																				<abbr data-utime="${reply.createDate }" class="timestamp livetimestamp"  data-hover="tooltip" data-tooltip-alignh="left" aria-label=""></abbr>
																			</a>
																			<span> </span>
																			<!--
																			<a href="#" class="ui-link-subtle">모바일</a>
																			<span>에서</span>
																			-->
																		</span>
																		<span> · </span>
																		<c:choose>
																			<c:when test="${isAuthenticated==true  && ( empty group || not empty groupUser )}">
																				<span class="ui-reply-like-button ui-reply-like-button-${reply.id } ${reply.feeledId == 1 ? 'mt-1-like':'mt-0-like' }">
																					<a	href="/feel/okay"
																						rel="sync-get"
																						ajaxify="ajax_like_toggle"
																						class="ui-reply-like-icon"
																						data-control-class="ui-reply-like-button-${reply.id }"
																						role="checkbox"
																						aria-checked="${reply.feeledId == 1 ? 'true':'false' }"
																						data-request-map="{&quot;contentId&quot;:&quot;${reply.id}&quot;,&quot;feelId&quot;:&quot;1&quot; }"
																						title="${reply.feeledId == 1 ? '좋아요 취소':'좋아요' }">
																						<i class="fa fa-heart-c fa-1g"></i>
																					</a>
																					<span> : </span>
																					<a	class="pop-feeled-users ui-reply-feel-score"
																						rel="sync-get"
																						href="/feel/users"
																						data-request-map="{&quot;contentId&quot;:${reply.id },&quot;feeledId&quot;:null,&quot;top&quot;:true,&quot;size&quot;:20}"
																						data-feel-score="${reply.dynamic.feelScore }">${reply.dynamic.feelScore }명</a>
																				</span>
																			</c:when>
																			<c:otherwise>
																				<span class="ui-reply-like-button">
																					<i class="ui-reply-like-icon"></i>
																					<span> : </span>
																					<a	class="ui-reply-feel-score"
																						rel="sync-get"
																						href="/feel/names"
																						ajaxify="ajax_feel_names"
																						data-request-map="{&quot;contentId&quot;:${reply.id },&quot;feeledId&quot;:null,&quot;top&quot;:true,&quot;size&quot;:20}"
																						data-hover="tooltip"
																						data-tooltip-alignh="center"
																						data-fn-hovercard-show="hovercard_feel_names_show()"
																						data-fn-hovercard-hide="hovercard_feel_names_hide()"
																						data-feel-score="${reply.dynamic.feelScore }">${reply.dynamic.feelScore }명</a>
																				</span>
																			</c:otherwise>
																		</c:choose>																	
																	</div>
																</div>
															</div>		
														</div>
													</div>	
												</div>
											</li>
											</c:forEach>
											<c:if test="${isAuthenticated==true && ( empty group || not empty groupUser )}">
											<li class="ui-react-row _l">
												<div class="z1">
													<div class="cmnt-profile-box">
														<div>
															<img src="${authUserProfilePic }" alt="" class="cmmnt-profile-pic">
														</div>
													</div>
													<div>
														<div class="image-block-content">
															<div>
																<div class="ui-mentions-input">
																	<div class="highlighter">
																		<div>
																			<span class="highlighter-content hidden-elem"></span>
																		</div>
																	</div>
																	<div class="ui-type-head mentions-type-head">
																		<div class="wrap">
																			<div class="inner-wrap">
																				<textarea name="add-comment-text-${content.id }" title="댓글달기" placeholder="댓글달기" 
																						  autocomplete="off"
																						  spellcheck="false"  
																						  tabindex=""
																						  onfocus="on_ta_focus(this,true);" 
																						  onblur="on_ta_focus(this);"
																						  data-want-return="ta_want_return_handler"
																						  data-request-map="{&quot;contentId&quot;:&quot;${content.id }&quot;}"
																						  class="text-input mentions-textarea ui-textarea-auto-grow data-textarea-autogrow-set ui-textarea-no-resize ui-add-comment-input dom-control-placeholder"></textarea>
																				<span class="placeholder" onclick="document.forms[&quot;cmmnt-form-${content.id }&quot;][&quot;add-comment-text-${content.id }&quot;].focus();">댓글달기</span>
																			</div>
																		</div>
																	</div>
																</div>
															</div>
														</div>
													</div>
												</div>	
											</li>
											</c:if>
										</ul>
									</div>
								</div>
								
							</div>
						</div>
					</div>
					<div class="rw-right-col">
						
						<!-- BEGIN:rightcol -->
						<c:import url="/WEB-INF/views/common/right-col.jsp">
						</c:import>
						<!-- END:rightcol -->
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
			
<form>
	<textarea tabindex="-1" id="ta-cmmnt-mirroring" class="textarea-mirroring cmmnt-mirroring"></textarea>
</form>
<script>
function ajax_delete_content(data){
	if( data.result != "success"){
		alert(data.message);
		return false;
	}
	
	window.location.href = "${groupPath}/approval";
}

</script>
<script>

/* --mod-bookmark-- */
  
 
function bookmark_this(_this){
	//e.preventDefault();
	
	
	var isAlreadyBookmarked = $(_this).data("already-bookmarked");
	var cid = $(_this).data("id");
	var url = isAlreadyBookmarked == true ? "/bookmark/remove" : "/bookmark/add";
	$.ajax({
		url: url ,
		type: "GET",
		dataType: "json",
		data:{
			"cid" : cid
		},
		success: function(data){
			setContentBookmarked(cid, data.data);
		}
	});
	
	return false; 
}

function setContentBookmarked( cid, bookmarked ){
	
	var $bookmarks = $(".bookmark-" + cid);
	
	if( bookmarked == false){
		$bookmarks.html("<i class='fa fa-star-o bigger-170 orange2' />");
		$bookmarks.data("already-bookmarked", false);
	}else{
		$bookmarks.html("<i class='fa fa-star  bigger-170 orange2' />");
		$bookmarks.data("already-bookmarked", true);
	}
}

</script>

<script>
/* -- mod-reply-core -- */
var _ejs_story_stream_reply_template, wait_reply=0;

function on_ta_focus(ta,focused){
	var $ta = $(ta);
	if(focused){
		$ta.next().hide();			
	} else {
		($ta.val() == "") && $ta.next().show();
	}
}

function ta_want_return_handler(){
	if( wait_reply == 1 ){ return; }
	wait_reply = 1;
	var 
	$ta=$(this),
	reply=$ta.val();
	if( reply == "" ){
		wait_reply = 0;
		return;
	} 
	var data=$.parseJSON($ta.attr("data-request-map"));
	data["text"]=reply;
	$.ajax({
		url: "/reply/post",
		async: false,
		type: "post",
		dataType: "json",
		contentType: 'application/json',
		headers: {
			"Accept": "application/json",
			"Content-Type": "application/json"
		},
		data:JSON.stringify(data),
		success: function(data){
			wait_reply = 0;
			reply_ta_clear($ta);
			if(data.result != "success"){
				$.error("error:ta_want_return_handler:$.ajax-" + data.message);
				return;
			}
			var $li;
			($li = $( "<li class='ui-react-row'>" )).
			insertBefore($ta.parents(".ui-react-row")).
			html(_ejs_story_stream_reply_template.render(data.data));
			refresh_timesince.call($li.find(".livetimestamp"));
		},
		error: function( jqXHR, textStatus, errorThrown ){
			$.error("add replys:"+errorThrown);
		}		
	});
}

function ajax_like_toggle(data){
	if(data.result != "success"){
		$.error("ajax_like_toggle:$.ajax-" + data.message);
		return;
	}
	
	var 
	$trigger=$(this),
	checked=$trigger.attr("aria-checked") == "true";

	$("."+$trigger.attr("data-control-class")).each(function(){
		var 
		$parent=$(this),
		$anchor=$parent.find(".ui-reply-like-icon");
		$score=$parent.find(".ui-reply-feel-score"),
		score=parseInt($score.attr("data-feel-score"),10);
		
		if(checked){
			$parent.removeClass("mt-1-like");
			$parent.addClass("mt-0-like");
			$anchor.attr("title","좋아요");
			$anchor.attr("aria-checked","false");
			$score.attr("data-feel-score", --score);
		}else{
			$parent.removeClass("mt-0-like");
			$parent.addClass("mt-1-like");
			$anchor.attr("title","좋아요 취소");
			$anchor.attr("aria-checked","true");
			$score.attr("data-feel-score", ++score);
		}
		$score.text(score + "명");
	});
}

function ajax_hide_reply(data){
	if(data.result != "success") {
		$.error("error:ajax_hide_reply:$.ajax-" + data.message);
		return;
	}
	$(this).parents(".ui-react-row").remove();
}

function before_ajax_more_reply(){
	var $anchor = $(this);
	$anchor.find(".fa-comment").hide();
	$anchor.find(".fa-spin").show();
	$anchor.find("span").text("가져오는 중...");
}

function ajax_more_reply(data){
	if(data.result != "success"){
		$.error("error:ajax_more_reply:$.ajax-" + data.message);
		return;
	}
	//$.log( data );
	var 
	$anchor=$(this),
	$parent=$anchor.parents(".cmnt-row-wrap"),
	count=data.data.length,
	remains=parseInt($anchor.attr("data-reply-count"),10),
	requestCount=parseInt($anchor.attr("data-reply-count"),10),
	reuestData=$.parseJSON($anchor.attr("data-request-map"));

	for(var i=0;i<count;i++){
		var $li;
		($li = $("<li class='ui-react-row'>")).
		html(_ejs_story_stream_reply_template.render(data.data[i]));
		$parent.next().prepend($li);
		refresh_timesince.call($li.find(".livetimestamp"));
	}
	if(count>0){
		reuestData["replyId"]=data.data[count-1].id;
		$anchor.attr("data-request-map", JSON.stringify(reuestData));
	}
	remains-=count;
	if(remains<0){ remains=0; }
	$anchor.attr("data-reply-count",remains);

	$anchor.find(".fa-spin").hide();
	$anchor.find(".fa-comment").show();
	
	if(remains == 0){
		$parent.remove();
	} else if( remains < 10 ){
		$anchor.find("span").text( "댓글" + remains+"개 더 보기" );
	} else {
		$anchor.find("span").text( "지난 댓글 더보기" );
	} 
}
/* --  mod-reply-core -- */

/* -- mod-reply-textarea-autosize -- */
var _reply_ta_autoresize_adjust_active=false;
var _reply_ta_autoresize_input_offset=0;
var _$reply_ta_autoresize_mirror;

function reply_ta_clear($ta){
	$ta.val("");
	setTimeout(function(){reply_ta_adjust.call($ta.get(0))},10);
}

function reply_ta_test_enter(){
	var	
	ta=this,
	$ta=$(ta),
	text=$ta.val(),
	mirror=_$reply_ta_autoresize_mirror.get(0),
	original = $ta.height(),
	height;

	_$reply_ta_autoresize_mirror.val(text+"\n");
	mirror.scrollTop = 0;
	mirror.scrollTop = 9e4;
	height = mirror.scrollTop + _reply_ta_autoresize_input_offset;

	(original !== height) && $ta.css("height",height);
	_$reply_ta_autoresize_mirror.val(text);
}

function reply_ta_adjust() {
	if(_reply_ta_autoresize_adjust_active) { return; }
	var	
	ta=this,
	$ta=$(ta), 
	mirror=_$reply_ta_autoresize_mirror.get(0),
	original = $ta.height(),
	height;

	_reply_ta_autoresize_adjust_active=true;
	_$reply_ta_autoresize_mirror.val($ta.val());
		
	mirror.scrollTop = 0;
	mirror.scrollTop = 9e4;
	height = mirror.scrollTop + _reply_ta_autoresize_input_offset;

	(original !== height) && $ta.css("height",height);
	setTimeout(function (){ _reply_ta_autoresize_adjust_active = false; }, 1);
}

function reply_ta_keydown(event){
	var ta=this, $ta=$(ta);
	if(event.keyCode != 10 && event.keyCode != 13) {
		if(event.keyCode == 27){$ta.blur();}			
		return;
	}
	
	if(event.shiftKey === false) {
		event.preventDefault();		
		var handlerName = $ta.attr("data-want-return");
		setTimeout(function(){window[handlerName].call($ta);},10);		
		return;								
	}
	
	reply_ta_test_enter.call(ta);
}

function reply_ta_autoresize() {
	$(".data-textarea-autogrow-set").
	each(function(){
		var 
		ta=this, 
		$ta=$(this);

		_$reply_ta_autoresize_mirror.css("width",$ta.width()-10);
		if($ta.css('box-sizing')==="border-box"||$ta.css('-moz-box-sizing')==="border-box"||$ta.css('-webkit-box-sizing')==="border-box"){
			_reply_ta_autoresize_input_offset = $ta.outerHeight() - $ta.height();
		}
		
		$ta.keydown(reply_ta_keydown);
		
		if('onpropertychange' in ta) {
			if('oninput' in ta) {
		    	ta.oninput = reply_ta_adjust;
		    	$ta.keydown(function(){setTimeout(reply_ta_adjust.bind(ta),50);});
			} else {
				ta.onpropertychange=reply_ta_adjust;
		    }
		}else{
		    ta.oninput=reply_ta_adjust;
		}
		$ta.removeClass("data-textarea-autogrow-set");
	});
}
/* -- mod-reply-textarea-autosize -- */

$(function(){
	//
	_ejs_story_stream_reply_template = new EJS({ 
		url: "/assets/sunny/2.0/js/template/story-stream-reply.ejs?rnd=" + Math.floor( Math.random() * 999999999 ) 
	});

	//	
	_$reply_ta_autoresize_mirror = $("#ta-cmmnt-mirroring");
	reply_ta_autoresize();
});
</script>

<script>
/* -- mod-timesince -- */
var _interval_id_timesince,_timesince_refresh_millis=60000;
function refresh_timesince(){
	var $el=$(this),
		t=$el.attr("data-utime");
	if(!t){
	    var s=function(a,b){ return(1e15+a+"").slice(-b); },
	    	d=new Date(parseInt($el.attr("data-timestamp"), 10));
	    t=d.getFullYear() + '-' +
		  s(d.getMonth()+1,2) + '-' +
	      s(d.getDate(),2) + ' ' +
	      s(d.getHours(),2) + ':' +
	      s(d.getMinutes(),2) + ':' +
	      s(d.getSeconds(),2);
	    $el.attr("data-utime", t);
	}
	var dt,secs,itv,h,m,s=$.trim(t);
 	s=s.replace(/\.\d+/,"");
    s=s.replace(/-/,"/").replace(/-/,"/");
    s=s.replace(/T/," ").replace(/Z/," UTC");
    s=s.replace(/([\+\-]\d\d)\:?(\d\d)/," $1$2");
    dt=new Date(s);
    secs=Math.floor((new Date()-dt)/1000);
    if((itv=Math.floor(secs/31536000))>=1){
    	$el.text(itv+"년 전");
    }else if((itv=Math.floor(secs/2592000))>=1){
    	$el.text(itv+"개월 전");
    }else if((itv=Math.floor(secs/86400))>=1){
    	$el.text(itv+"일 전");
    }else if((itv=Math.floor(secs/3600))>=1){
    	$el.text(itv+"시간 전");
    }else if((itv=Math.floor(secs/60))>=1){
    	$el.text(itv+"분 전");
    }else{
    	$el.text("방금 전");
    }
    $el.attr("title", dt.getFullYear()+"년 "+(dt.getMonth()+1)+"월 "+dt.getDate()+"일 "+((h=dt.getHours())>12?("오후 "+(h-12)):("오전 "+h))+':'+((m=dt.getMinutes())<10?"0":"")+m);
}
function timesince(){
	_interval_id_timesince && clearInterval(_interval_id_timesince);
	$(".livetimestamp").each(refresh_timesince);
	_interval_id_timesince = setInterval(function(){$(".livetimestamp").each(refresh_timesince);},_timesince_refresh_millis);
}
/* -- mod-timesince -- */

/* -- mod-story     -- */
function ajax_feel_toggle(data) {
	if(data.result != "success"){
		$.error("ajax_feel_toggle:$.ajax-"+data.message);
		return;
	}
	
	var 
	$anchor=$(this),
	$span=$anchor.prev(),
	checked=$anchor.attr("aria-checked")=="true",
	feeled=false;
	
	$(".react-icons-"+$anchor.attr("data-ft")).each(function(){
		$(this).find(".story-react").each(function(){
			var 
			$r=$(this),
			$span=$r.find("span"),
			$anchor=$r.find("a");
			
			if($span.hasClass("reacted")){ feeled=true; }
			$span.removeClass("reacted");
			$anchor.attr("aria-checked","false");
		});	
	});

	if(!checked){
		//$span.addClass("reacted");
		//$anchor.attr("aria-checked","true");

		$( "." + $anchor.attr("data-ft-class") ).each( function(){
			$(this).prev().addClass("reacted");
			$(this).attr("aria-checked","true");
		});
		
		
		if(!feeled){
			var 
			$counter=$("."+$anchor.attr("aria-controls")),
			count=parseInt($counter.attr("data-count"),10);			
			$counter.attr("data-count",++count);
			$counter.find(".count-text").text(count+"명");
			(count==1) && $counter.parents(".cmnt-row-wrap").show();
		}
	}else{
		var 
		$counter=$("."+$anchor.attr("aria-controls")),
		count=parseInt($counter.attr("data-count"),10);
		$counter.attr("data-count",--count);
		$counter.find(".count-text").text(count+"명");
		(count==0) && $counter.parents(".cmnt-row-wrap").hide();
	}
}

function ajax_delete_story(data){
	if(data.result != "success"){
		$.error("error:ajax_delete_story:$.ajax-" + data.message);
		return;
	}
	var $si=$(this).parents(".ui-stream-story");
	if($si.prev().length == 0){
		var dataFWMap=$.parseJSON(_$sl.attr("data-fw-map")),
			$ni=$si.next();	
		if($ni.length>0){
			var dataMap=$.parseJSON($ni.attr("data-map"));
			dataFWMap["contentId"]=dataMap["id"];
		}else{
			dataFWMap["contentId"]="";
		}
		_$sl.attr("data-fw-map", JSON.stringify(dataFWMap));
	}
	if($si.next().length == 0){
		var dataBWMap=$.parseJSON(_$sl.attr("data-bw-map")),
			$pi=$si.prev();	
		if($pi.length>0){
			var dataMap=$.parseJSON($pi.attr("data-map"));
			dataBWMap["contentId"]=dataMap["id"];
		}else{
			dataBWMap["contentId"]="";
		} 
		_$sl.attr("data-bw-map", JSON.stringify(dataBWMap));
	}
	$si.fadeOut(600, "linear", function(){$(this).remove();});
}

$(function() {
	//
	$(".generated-mention").on( "click", function(e){
		e.preventDefault();
		location.href="/user/" + $(this).data("uid");
	});
	$(".generated-hash").on( "click", function(e){
		e.preventDefault();
		location.href="${param.currentUrl}?q%5B%5D=" + "%23" + $(this).text().substring(1);
	});
	//
	timesince();
});
/* -- mod-story    -- */
</script>


<script>
/* -- mod-story-dialogs    -- */
function on_feel_users_scroll(e, pos) {
	if (pos == "bottom" /* && end_of_chat_user_stream == false */) {
		//invite_friend_streaming(false);
	}
}

var _ejs_feel_users = null;
$(function(){
	$(document.body).on("click", ".pop-feeled-users", function(e){
		
		e.preventDefault();
		
		$this = $(this);
		map = $this.data("request-map");
		
		var channelId = $(this).attr("data-id");
		if (_ejs_feel_users == null) {
			_ejs_feel_users  = new EJS(
					{
						url : "/assets/sunny/2.0/js/template/feel-users.ejs?rnd=" + Math.floor(Math.random() * 999999999)
					});
		}
		
		var isFavorited = $.ajax({
			url: "/feel/users",
			type: "GET",
			dataType: "json",
			data:map
		});
		
		$bootboxContent = $("#res-dialog-pop-feeled-users-content");
		bootbox.dialog("res-dialog-pop-feeled-users", [{
			"label" : "취소",
			"class" : "hmo-hmo-button hmo-button-white hmo-button-small-10"
		}],{
			"header" : "평가한 사용자들",
			"embed" : true,
			"animate" : false,
			"onInit" : function() {
				//$("#res-dialog-show-profile-content").html( _$ejs_show_profile.render(map) );
				
				$.when(isFavorited)
				.done(function(data){
					if(data.result == "fail"){
						alert( data.message );
					}
					var html = "";
					$.each( data.data, function( idx, d ){
						html += _ejs_feel_users.render(d);
	
					});
					$bootboxContent.html( html  );
				})
				.fail(function(data){
					alert("오류발생");
				});
				
				$('.pop-feeled-users-scroll-wrap').slimscroll({
					height : "350px",
					railVisible : false,
					alwaysVisible : false,
					touchScrollStep : 70
				}).bind('slimscroll', on_feel_users_scroll);
			},
			"onFinalize" : function() {
				//alert( "call finalize" );	
				//window.location.reload();
			},
			"beforeShown" : function() {
				// 로딩으로 변경
				//$("#res-dialog-show-profile-content").html( "로딩중" );
				$bootboxContent.html( "<i class='fa fa-spinner fa-spin'></i>" );
			}
		});
		
	});
});
/* -- mod-story-dialogs    -- */
</script>


<script>

/* -- mod-approval-dialogs -- */

/* -- mod-approval-functions -- */
function ajax_approval_ok( data ){
	if( data.result == "fail" ){
		 alert( data.message );
		 return ;
	}
	
	$this = $(this);
	$this.addClass("hidden-elem");
	$this.siblings(".reject").addClass("hidden-elem");
	$this.siblings(".approved").removeClass("hidden-elem");
	
}

function ajax_approval_reject( data ){
	if( data.result == "fail" ){
		 alert( data.message );
		 return ;
	}
	
	$this = $(this);
	$this.addClass("hidden-elem");
	$this.siblings(".approve").addClass("hidden-elem");
	$this.siblings(".rejected").removeClass("hidden-elem");
	
}

function ajax_approval_request_reject( data ){
	 if( data.result == "fail" ){
		 alert( data.message );
		 return ;
	}
	
	$this = $(this);
	$this.addClass("hidden-elem");
	$this.siblings(".cancel").removeClass("hidden-elem");
	 
}

function ajax_approval_cancel_request_reject( data ){
	 if( data.result == "fail" ){
		 alert( data.message );
		 return ;
	}
	
	$this = $(this);
	$this.addClass("hidden-elem");
	$this.siblings(".request").removeClass("hidden-elem");
	 
}
/* --mod-approval-functions -- */
</script>
</body>
</html>