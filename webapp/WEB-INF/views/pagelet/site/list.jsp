<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<c:choose>
	<c:when test="${empty pagedResult.contents }">
		<div class="site-list-search-notfound">
		<p>
		요청하신 검색어에 대한 <span>검색결과가 없습니다.</span>	<br>
		다시 시도해 주시기 바랍니다.
		</p>
		</div>
	</c:when>
	<c:otherwise>
	<div class="site-list-wrap">
	<c:forEach items="${pagedResult.contents}" var="site"
									varStatus="status">
		<div class="site-list-form">
			<div class="site-list-form-top-wrap">
				<div class="site-list-form-top">
					<div class="site-list-form-top-name">${site.companyName }</div>
					<c:if test="${not empty site.homepage }">
						<a href="${site.homepage }" target="_blank" class="site-list-form-a">${site.homepage }</a>
					</c:if>
				</div>
			</div>
			<div class="site-list-form-middle-wrap">
				<div class="site-list-form-middle">
					${site.companyIntroduce }
				</div>
			</div>
			<div class="site-list-form-bottom-wrap">
				<div class="site-list-form-bottom">
					<c:choose>
						<c:when test="${not empty site.joinedUser && site.joinedUser }">
							<span>가입됨</span>
						</c:when>
						<c:when test="${not empty site.alreadyRequestUser && site.alreadyRequestUser }">
						<a href="#" class="site-list-form-button hmo-button hmo-button-blue hmo-button-small-6">
							요청취소
						</a>
						</c:when>
						<c:otherwise>
							<a href="/site/${site.id }/signup"  class="site-list-form-button hmo-button hmo-button-blue hmo-button-small-6">가입요청</a>	
						</c:otherwise>
					</c:choose>			
				</div>
			</div>
		</div>
		</c:forEach>
	</div>		
	
	
	<div class="z1 site-list-pagelet">
		<div class="pull-right">
			<div class="inline middle"> ${pagedResult.pageNumber} of ${pagedResult.endPageNum } </div>
			&nbsp; &nbsp;
			<ul class="pagination inline unstyled middle" >
	
				<c:choose>
				<c:when test="${pagedResult.pageNumber % 10 > 1 }">
					<li>
						<a class="more-detail" data-name="page" data-value="${pagedResult.pageNumber - 1 }" href="?page=${pagedResult.pageNumber - 1 }">
							<i class="fa fa-arrow-left"></i>
						</a>
					</li>
					</c:when>
					<c:otherwise>
						<li class="disabled">
							<a href="#">
								<i class="fa fa-arrow-left"></i>
							</a>
						</li>
					</c:otherwise>
				</c:choose>
				<li>
					<form id="note-page-form" style="margin: 0;">
						<input  maxlength="3" type="text" value="${pagedResult.pageNumber }" style="margin-bottom: 0px;width: 25px;text-align: center;"/>
					</form>
				</li>
				
				<c:choose>
					<c:when test="${pagedResult.lastPageNum  > pagedResult.pageNumber }">
						<li>
							<a class="more-detail" data-name="page" data-value="${ pagedResult.pageNumber + 1 }"  href="?page=${ pagedResult.pageNumber + 1 }"> 
								<i class="fa fa-arrow-right"></i>
							</a>
						</li>
					</c:when>
					<c:otherwise>
						<li class="disabled"><a href="#"><i
								class="fa fa-arrow-right"></i></a></li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
		<div class="site-list-pagelet-button">
			<a href="/a/signup" class="hmo-button hmo-button-white hmo-button-small-6"><span>오피스 생성하기</span></a>
		</div>
	</div>
		
	
	</c:otherwise>

</c:choose>
