<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="draft-list">
	<form>
		<table class="draft-list-top">
			<tbody>
				<tr>
					<td class="draft-list-top-check">삭제</td>	
					<td class="draft-list-top-title">제목</td>
					<td class="draft-list-top-content">내용</td>
					<td class="draft-list-top-date">수정일</td>
				</tr>
			</tbody>
		</table>
		<table class="draft-list-mid">
			<tbody>
				<c:forEach items="${pagedContents.contents}" var="draft" varStatus="status">
					<tr>
						<td class="draft-list-mid-check">
							<a class="draft-list-mid-del"
								role="hmo-dialog"
								aria-controls="insa-messagebox-yesno"
								data-label="임시저장 삭제"
								data-message="삭제되면 복구할 수 없습니다.<br>이 임시저장을 삭제하시겠습니까?"
								href="/draft/delete"
								data-request-map="{&quot;id&quot;:&quot;${draft.id }&quot;}"
								rel="sync-get"
								ajaxify="ajax_delete_draft">
							</a>
						</td>
						<td class="draft-list-mid-title"><a href="/${param.type }/write?id=${draft.id}">${not empty draft.rawTitle ? draft.rawTitle : '제목없음' }</a></td>
						<td class="draft-list-mid-content"><a href="/${param.type }/write?id=${draft.id}">${fn:substring(draft.snippetText, 0, 50) }</a>  </td>
						<td class="draft-list-mid-date"><span><abbr data-utime="${draft.updateDate}" class="timestamp livetimestamp"></abbr></span></td>
					</tr>
				</c:forEach>					
			</tbody>
		</table>
	</form>
	
		<div class="draft-list-button">
			<a class="qna-storage-button" href="${param.urlPrev }">목록보기</a>
			<a class="qna-storage-button" href="/draft/create/${param.type}<c:if test='${not empty group }'>?gid=${group.id }</c:if>">새로 작성하기</a></div>
		
		<div class="draft-list-bottom">
				<div class="draft-list-bottom-td-paging">
					
					<c:if test="${pagedContents.pageNumber > 1}">
						<a class="mr3" href="${param.prevUrl }?page=${pagedContents.pageNumber - 1 }">
							<img src="/assets/desktop/img/ins/bt_f1.gif" width="19" height="19" alt="이전페이지" />
						</a>
					</c:if>
					<c:forEach var="pageIndex" begin="${pagedContents.startPageNum }" end="${pagedContents.endPageNum }">
						<a href="${param.prevUrl }?page=${pageIndex }"><c:out value="${pageIndex }" /></a>
						<img src="/assets/desktop/img/ins/list_num_vline2.gif" width="17" height="6" alt="|" />
					</c:forEach>
					
					<a href="${param.prevUrl }?page=${pagedContents.totalPages}">${pagedContents.totalPages }</a>	
					<c:if test="${pagedContents.pageNumber != pagedContents.lastPageNum }">
						<a href="${param.prevUrl }?page=${pagedContents.pageNumber + 1 }" class="ml3">
							<img src="/assets/desktop/img/ins/bt_n1.gif" width="19" height="19" alt="다음페이지">
						</a>
					</c:if>
					<c:if test="${pagedContents.lastPageNum  > (pagedContents.endPageNum + 1) }">
						<c:set var="endSub" value="${ pagedContents.lastPageNum - ( pagedContents.endPageNum + 1) }" />
						<a href="${param.prevUrl }?page=${ endSub > 10 ? pagedContents.pageNumber + 10 : pagedContents.pageNumber + endSub }${pageParsedParams}" class="next10">
							<img src="/assets/desktop/img/ins/bt_n10.gif" width="19" height="19" alt="다음10개">
						</a>
					</c:if>																								
				</div>
		</div>
</div>

<%--
						
						<div class="pagelet-wrap mtl ">
							<h3 class="write-prev-navi">
								Qna
							</h3>
						</div>
						<div class="pagelet-blank"></div>
						<div class="pagelet-grid">
							<div class="ui-grid-wrap">
								<div class="ui-grid write-prev-grid">
									<div class="col50 dotted-right">
										<c:if test="${not empty pagedDrafts }">
										<table class="ui-grid prev-draft-tbl">
											<tbody>
												<c:forEach items="${pagedDrafts.contents}" var="draft" varStatus="status">
													<tr class="tr-${status.index % 2 == 1 ? 'odd' : 'even'}">
														<td style="width:30px;">
															<a class="item-anchor item-with-icon"
																role="hmo-dialog"
																aria-controls="insa-messagebox-yesno"
																data-label="임시저장 삭제"
																data-message="삭제되면 복구할 수 없습니다.<br>이 임시저장을 삭제하시겠습니까?"
																href="/draft/delete"
																data-request-map="{&quot;id&quot;:&quot;${draft.id }&quot;}"
																rel="sync-get"
																ajaxify="ajax_delete_draft">
																	<i class="mrs item-icon img icon-remove"></i>
															</a>
														</td>
														<td>
															<a href="/qna/write?id=${draft.id}">${not empty draft.rawTitle ? draft.rawTitle : '제목없음' }</a>
														</td>
														<td>
															<span>${fn:substring(draft.rawText, 0, 50) }</span>
														</td>
														<td>
															<span><abbr data-utime="${draft.updateDate}" class="timestamp livetimestamp"></abbr></span>
														</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
										</c:if>
										<c:if test="${empty pagedDrafts }">
											임시 저장중인 글이 없습니다
										</c:if>
									</div>
									<div class="col50">
										<a href="/draft/create/question" >새로 작성하러 가기</a>
									</div>
								</div>
							</div>						
						</div>
						
						--%>
						
													