<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<div class="ui-message-channel-stream-wrap">
	<div class="ui-message-channel-stream"
		 id="channel-stream-list"
		 role="list"
		 data-bw-map='{"page":2 }'
		 data-stream-url="/message/channels"
		 data-async="async-get">
		<div class="_h6m"></div>
		<ul class="ui-message-channel-list" id="channel-list">
			<c:forEach items="${pagedResult.contents}" var="channel" varStatus="status">
				<li class="_z6m channel-row">
					<table class="ui-grid _k3x" cellspacing="0" cellpadding="0">
						<tbody>
							<tr data-channel-id="${channel.id }">
								<td class="v-top _z6o channel-col" onclick="Channel.onChannelRowClicked(this);">
									<div>
										<c:if test="${channel.type == 0 }">
											<div class="user-pic-wrap onetoone" style="width: 60px; height: 60px;">
												<div class="user-pic">
													<c:forEach items="${channel.tmpJoinedUsers }"
														var="joinUser" varStatus="status">
														<img src="${joinUser.profilePic}" style="width:100%; height:100%;">
													</c:forEach>
												</div>
											</div>
										</c:if> 
										<c:if test="${channel.type == 1 }">
											<div class="user-pic-wrap group group-${ channel.userCount > 3 ? '3' : channel.userCount - 1}" style="width:60px; height:60px">
												<c:forEach items="${channel.tmpJoinedUsers }" var="joinUser" varStatus="status">
													<c:if test="${status.index < 3 }">
														<div class="user-pic user-pic-${ status.index }">
															<img src="${joinUser.profilePic}" style="margin: 0 auto">
														</div>
													</c:if>
												</c:forEach>
											</div>
										</c:if>								
									</div>
								</td>
								<td class="v-top _z6p channel-col" onclick="Channel.onChannelRowClicked(this);">
									<div class="_el2 fwn fcg">
										<span class="_sl1">
											<c:set value="${sunny.device.mobile ? 3 : 4 }" var="maxUserCount"/>
											<c:set value="${fn:length(channel.tmpJoinedUsers) }" var="count" />
											<c:set value="${ count >= maxUserCount ? maxUserCount : count }" var="loopCount" />
											<c:forEach begin="1" end="${loopCount }" step="1" var="index">
												${channel.tmpJoinedUsers[index-1].name } ${index < loopCount ? ',' : '' }
											</c:forEach>
											<c:if test="${channel.type == 0 }"></c:if>
											<c:if test="${channel.type == 1 }">
													<c:if test="${channel.userCount > maxUserCount }">
														+${channel.userCount - maxUserCount }
													</c:if>
											</c:if>
										</span>
										<abbr data-utime="${channel.updateDate }" class="_el0 timestamp livetimestamp"  data-hover="tooltip" data-tooltip-alignh="left" aria-label=""></abbr>
									</div>
									<div class="_el2 fwn fcg fsm">
										<span>
											${channel.lastUserName }: ${channel.lastEscapeHTMLSnippet }
										</span>
									</div>
								</td>
								<td class="v-middle _z6q">
									<div class="_41a2 stat_elem">
										<a	href="/channel/leave"
											role="dialog"
											data-style="messagebox-yesno"
											data-title="대화방 나가기"
											data-message="대화방을 나가면 상대방에게 메시지가 전달되며, 앞으로 대화방의 알림을 받을 수 없습니다.<br>나가시겠습니까?"
											ajaxify-dialog-yes="Channel.onAjaxExitChannel" 
											rel="sync-get"
											data-request-map="{&quot;id&quot;:&quot;${channel.id }&quot;}"										
											class="hmo-button hmo-button-white hmo-button-small-9 btn-leave-channel">나가기</a>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</li>
			</c:forEach>
		</ul>
		<div class="_f6m"></div>
	</div>	
</div>