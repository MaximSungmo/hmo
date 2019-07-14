<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize access="isAuthenticated()" var="isAuthenticated">
	<c:set value="true" var="isAuthenticated" scope="request"/>
	<sec:authentication var="authUserId" property="principal.userId" scope="request"/>
	<sec:authentication var="authUserName" property="principal.name" scope="request"/>
	<sec:authentication var="authUserProfilePic" property="principal.profilePic" scope="request"/>
</sec:authorize>



<div class="message-header clearfix">
	<div class="pull-left">
		<span class="blue bigger-125">${content.title }</span>

		<div class="space-4"></div>


		&nbsp;
		<img class="middle" alt="John's Avatar" src="${content.user.profilePic }" width="32" />
		&nbsp;
		<a href="#" class="sender">${content.user.name }</a>

		&nbsp;
		<i class="fa fa-clock-o bigger-110 orange2 middle"></i>
		<span class="time">${content.createDate }</span>
		
		
	</div>

	<div class="action-buttons pull-right">
		<a href="#" class="bookmark-${content.id }" onclick="return bookmark_this(this);" data-id="${content.id }" data-already-bookmarked="${alreadyBookmarked == true ? 'true' : 'false' }"><i class="fa fa-star${alreadyBookmarked == true ? '' : '-o' } bigger-170 orange2"></i></a>
	</div>
</div>

<div class="hr "></div>

<div class="message-body">
	${content.taggedTextPrev }
	${content.taggedTextNext }
</div>

<div class="hr "></div>

<div class="message-attachment clearfix">
	<div class="attachment-title">
		<span class="blue bolder bigger-110">파일들 </span>
		&nbsp;
		<span class="grey">(${content.mediaCount } 개의 파일이 있습니다.)</span>

		<div class="inline position-relative">
			<a href="#" data-toggle="dropdown" class="dropdown-toggle">
				&nbsp;
				<i class="fa fa-caret-down bigger-125 middle"></i>
			</a>

			<ul class="dropdown-menu dropdown-lighter">
<!-- 				<li> -->
<!-- 					<a href="#">Download all as zip</a> -->
<!-- 				</li> -->

<!-- 				<li> -->
<!-- 					<a href="#">Display in slideshow</a> -->
<!-- 				</li> -->
			</ul>
		</div>
	</div>

	&nbsp;
	<ul class="attachment-list pull-left unstyled">
		<c:forEach items="${content.mediaes }" var="media">
			<li> 
				<a href="/download?id=${media.id}" class="attached-file inline">
					<i class="fa fa-file-alt bigger-110 middle"></i>
					<span class="attached-name middle">${media.fileName }</span>
				</a>
	
				<div class="action-buttons inline">
					<a href="/download?id=${media.id}">
						<i class="fa fa-download bigger-125 blue"></i>
					</a>
				</div>
			</li>
		</c:forEach>
	</ul>
	
</div>

<div class="hr "></div>

<div class="widget-main _act">
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
					data-request-map="{&quot;contentId&quot;:&quot;${content.id }&quot;,&quot;feelId&quot;:&quot;1&quot;}">좋아요</a>
			</span>

			<span class="story-react">
				<span class="react-icon emo-cry <c:if test='${content.feeledId == 2}'>reacted</c:if>"></span>
				<a	href="/feel/okay" role="radio"
					class="feel-radio-react-sad-${content.id }"
					data-ft-class="feel-radio-react-sad-${content.id }"
					ajaxify="ajax_feel_toggle"
					aria-checked="${content.feeledId == 2 ? 'true':'false'}"
					aria-controls="feel-counter-${content.id }"
					rel="sync-get"
					data-ft="${content.id }"
					data-request-map="{&quot;contentId&quot;:&quot;${content.id }&quot;,&quot;feelId&quot;:&quot;2&quot;}">슬퍼요</a>
			</span>

			<span class="story-react">
				<span class="react-icon emo-cheer <c:if test='${content.feeledId == 3}'>reacted</c:if>"></span>
				<a	href="/feel/okay" role="radio"
					class="feel-radio-react-chr-${content.id }"
					data-ft-class="feel-radio-react-chr-${content.id }"
					ajaxify="ajax_feel_toggle"
					aria-checked="${content.feeledId == 3 ? 'true':'false'}"
					aria-controls="feel-counter-${content.id }"
					rel="sync-get"
					data-ft="${content.id }"
					data-request-map="{&quot;contentId&quot;:&quot;${content.id }&quot;,&quot;feelId&quot;:&quot;3&quot;}">힘내요</a>
				</span>

			<span class="story-react">
				<span class="react-icon emo-thumb <c:if test='${content.feeledId == 4}'>reacted</c:if>"></span>
				<a	href="/feel/okay" role="radio"
					class="feel-radio-react-bst-${content.id }"
					data-ft-class="feel-radio-react-bst-${content.id }"
					ajaxify="ajax_feel_toggle"
					aria-checked="${content.feeledId == 4 ? 'true':'false'}"
					aria-controls="feel-counter-${content.id }"
					rel="sync-get"
					data-ft="${content.id }"
					data-request-map="{&quot;contentId&quot;:&quot;${content.id }&quot;,&quot;feelId&quot;:&quot;4&quot;}">멋져요</a>
			</span>
		</c:if>
	</span>
</div>

<c:set value="${(empty content.dynamic.feelCount || content.dynamic.feelCount < 0) ? 0:content.dynamic.feelCount}" var="feelCount" />
<div class="widget-main ui-comment-container">
	<span class="head-line">&nbsp;</span>
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
								<a	role="hmo-dialog"
									aria-controls="insa-messagebox-yesno"
									data-label="댓글 삭제"
									data-message="삭제되면 복구할 수 없습니다.<br>이 댓글을 삭제하시겠습니까?"
									href="/reply/delete"
									rel="sync-get"
									ajaxify="ajax_hide_reply"
									data-request-map="{&quot;id&quot;:&quot;${reply.id }&quot;}"
									class="ui-comment-close-button"><i class="fa fa-times-circle fa-1g"></i></a>
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
