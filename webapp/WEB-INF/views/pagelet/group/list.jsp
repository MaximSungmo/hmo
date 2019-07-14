<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<c:forEach items="${pagedResult.contents}" var="smallGroup"
									varStatus="status">
	<div class="group-list-content row-triple" style="padding: 5px; border-bottom: 1px solid lightgray; ">
		<div class="triple-first">
<%-- 			<a href="/group/${smallGroup.id }"> --%>
<%-- 				<img src="${smallGroup.coverPicThumb }" style="width:50px; height: 50px;"/> --%>
<!-- 			</a> -->
		</div>
		<div class="group-list-miscellaneous triple-second">
			<div class="user-count">
				<span style="color:gray;">
					회원수 : ${smallGroup.userCount }
					글 : ${smallGroup.eventCount } 
				</span><br />
				<span><abbr data-utime="${smallGroup.updateDate }" class="timestamp livetimestamp"  data-hover="tooltip" data-tooltip-alignh="left" aria-label=""></abbr> 업데이트</span>
			</div>
		</div>
		
		<a href="/group/${smallGroup.id }" class="block" style="text-decoration:none; color: black;">
			<div class="group-list-desc triple-third z1" >
				<div class="triple-cell" style="width:100%; ">
					
					<div class="group-list-title">
							<span>${smallGroup.name }</span>
							<c:if test="${not empty smallGroup.parent &&  smallGroup.parent.type != 1}">
								<i class="fa fa-angle-left"></i>
								${smallGroup.parent.name }
							</c:if>
							
							<br />
							<span style="color:gray;">${smallGroup.description }</span>
					</div>
	
				</div>
			</div>
		</a>
	</div>
</c:forEach>
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