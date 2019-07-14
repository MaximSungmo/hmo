<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:forEach items="${pagedResult.contents }" var="smallGroupUser" varStatus="status">
<li class="_z6m pagelet-row group-list-content">
	<table class="ui-grid _k3x">		
		<tbody>
			<tr>
				<td class="v-top _z6o channel-col">
					<div>
						<div class="user-pic-wrap onetoone" style="width:60px;height:60px;">
							<div class="user-pic">
								<img src="${smallGroupUser.user.profilePic }" style="width:100%;height:100%;">
							</div>
						</div>
					</div>
				</td>
				<td class="v-top _z6p channel-col">
					<div class="_el2 fwn fcg">
						<span class="_sl1">
							<a style="color:#333;" href="#" id="group-list-user-name-${smallGroupUser.user.id }" >${smallGroupUser.user.name }<span class="iamadmin ${not inactive && smallGroupUser.admin ? '' : 'hidden-elem'}">(관리자)</span></a>
						</span>
					</div>
					<div class="_el2 fwn fcg fsm">
						<span>가입일 : ${smallGroupUser.createDate }</span>
					</div>
				</td>
				<td class="v-middle _z7q">
					<c:if test="${isSmallGroupAdmin }">
						<a href="#" aria-owns="user-${smallGroupUser.user.id }-popup-menus" aria-haspopup="true" rel="toggle" class="dropdown-toggle hmo-button hmo-button-khaki dropdown-toggle hmo-button-small-9">										
							<i class="white fa fa-gear bigger-130"></i> &nbsp;
							<i class="fa fa-caret-down bigger-125"></i>
						</a>				
						<ul id="user-${smallGroupUser.user.id }-popup-menus" data-popup-group="global" class="ui-toggle-flyout dropdown-menu pull-right dropdown-100">			
							<c:if test="${inactive }">
								<li>
									<a href="/group/${smallGroup.id}/accept_user"
									ajaxify="ajax_group_accept_user"
									rel="sync-get"
									data-request-map='{"uid":"${smallGroupUser.user.id }"}'>
									승인 
									</a>
								</li>
								<li>
									<a href="/group/${smallGroup.id}/remove_user"
									ajaxify="ajax_group_remove_user"
									rel="sync-get"
									data-request-map='{"uid":"${smallGroupUser.user.id }"}'>
									거부
									</a>
								</li>
							</c:if>
							<c:if test="${not inactive }">
								<li>
									<a class="group-remove-admin ${not smallGroupUser.admin ? 'hidden-elem' : ''}" href="/group/${smallGroup.id}/removeadmin"
									ajaxify="ajax_group_remove_admin"
									rel="sync-get"
									data-uid="${smallGroupUser.user.id }"
									data-request-map='{"uid":"${smallGroupUser.user.id }"}'>
									관리자 권한 빼기
									</a>
								</li>
								<li>
									<a class="group-join-admin ${ smallGroupUser.admin ? 'hidden-elem' : ''}" href="/group/${smallGroup.id}/joinadmin"
									ajaxify="ajax_group_join_admin"
									rel="sync-get"
									data-uid="${smallGroupUser.user.id }"
									data-request-map='{"uid":"${smallGroupUser.user.id }"}'>
									관리자 권한 부여
									</a>
								</li>
								<li>
									<a href="/group/${smallGroup.id}/remove_user"
									ajaxify="ajax_group_remove_user"
									rel="sync-get"
									data-request-map='{"uid":"${smallGroupUser.user.id }"}'>
									구성원에서 제외
									</a>
								</li>
							</c:if>
						</li>
					</ul>
					</c:if>
				</td>
			</tr>
		</tbody>
	</table>
</li>	
</c:forEach>
<div class="_f6m"></div>
<div id="next-stream-item" class="hmo-next-stream-item">
	<c:choose>
		<c:when test="${pagedResult.endPageNum > pagedResult.pageNumber }">
			<div class="row-fluid">
				<a href="#" class="btn span12" id="next-stream-btn">다음 리스트 가져오기</a>
			</div>
			<div id="next-stream-loading" class="center hidden-elem">로딩중</div>
		</c:when> 
		<c:otherwise>
			<div class="center">더이상 표시할 데이터가 없습니다.</div>
		</c:otherwise>
	</c:choose>
</div>