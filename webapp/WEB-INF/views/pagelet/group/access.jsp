<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>



<c:forEach items="${pagedResult.contents }" var="smallGroupSmallGroup" varStatus="status">

	<c:choose>
	<%-- 사용자일때 --%>
		<c:when test="${smallGroupSmallGroup.accessSmallGroup.type == 0 }">
		<li class="_z6m pagelet-row group-list-content">
				<table class="ui-grid _k3x">
					<tbody>
						<tr>
							<td class="v-top _z6o channel-col">
									<div class="user-pic-wrap onetoone" style="width:60px;height:60px;">
										<div class="user-pic">
											<img src="${smallGroupSmallGroup.accessSmallGroup.creator.profilePic }" style="width:100%;height:100%;">
										</div>
									</div>
							</td>
							<td class="v-top _z6p channel-col">			
									<div class="_el2 fwn fcg">													
										<span class="_sl1">
											<a href="#" style="color:#333;" >${smallGroupSmallGroup.accessSmallGroup.creator.name }</a>
										</span>
									</div>
									<div class="_el2 fwn fcg fsm"> 
										${smallGroupSmallGroup.accessSmallGroup.creator.statusMessage }
									</div >
									<div class="_el2 fwn fcg fsm">
										가입일 : ${smallGroupSmallGroup.createDate }
									</div>
							</td>
							<td class="v-top _z126o channel-col">
									<c:if test="${isSmallGroupAdmin }">
										<a href="/group/${smallGroup.id}/remove_access"
										ajaxify="ajax_group_remove_access"
										rel="sync-get"
										data-request-map='{"sgsgid":"${smallGroupSmallGroup.id }"}'>
										<i class="fa fa-ban red"></i>
										허용 그룹에서 제외
										</a>
									</c:if>
							</td>
						</tr>
					</tbody>
				</table>	
			</li>															
			</c:when>
			
			<c:when test="${smallGroupSmallGroup.accessSmallGroup.id == smallGroup.id }">
														
			</c:when>
			<%-- 리스트 중에 현재 칼럼이 사람이 아닌 그룹인 경우 --%>
			<c:otherwise>
				<li class="_z6m pagelet-row group-list-content">
					<table class="ui-grid _k3x">
						<tbody>
							<tr>
								<td class="v-top channel-col" style="padding-left:12px;">																	
										<div>
											<a href="#">${smallGroupSmallGroup.accessSmallGroup.name }</a>
										</div>
										<div style="color: gray"> 
											${smallGroupSmallGroup.accessSmallGroup.description }
										</div >
										<div style="color: gray">
											가입일 : ${smallGroupSmallGroup.createDate }
										</div>
								</td>
								<td class="v-top _z126o channel-col">	
									<c:if test="${isSmallGroupAdmin }">
										<a href="/group/${smallGroup.id}/remove_access"
										ajaxify="ajax_group_remove_access"
										rel="sync-get"
										data-request-map='{"sgsgid":"${smallGroupSmallGroup.id }"}'>
										<i class="fa fa-ban red"></i>
										허용 그룹에서 제외
										</a>
									</c:if>
								</td>
							</tr>
						</tbody>
					</table>
				</li>
			</c:otherwise>
		</c:choose>

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