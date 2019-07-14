<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:import url="/WEB-INF/views/desktop/_common/draft-script.jsp">
<c:param name="contentType">${param.contentType }</c:param>
</c:import>

<form action="" id="draft-form" onsubmit="return window.Event &amp;&amp; (Event.__submit || Event.__inlineSubmit || fn_empty)(this, event);" rel="sync" name="draft-w-form" method="post">
	<ul class="ui-list mtl _4kg _6-h _6-j _6-i">
	<c:if test="${param.typeName == 'note' }">
		<li style="padding-top: 10px; ">
			<div class="edit-draft-wrapper-input">
				<input	id="title"
						class="inpu-text edit-draft-title-input dom-control-placeholder" 
						type="text" 
						aria-label="제목" 
						placeholder="제목" 
						maxlength="128" 
						autocomplete="off" 
						<%--  value="${${param.typeName}.title }" --%>
						name="title">
			</div>
		</li>
	</c:if>
		<li  class="draft-editor-wrap">		
				<%--	
				<c:import url="/WEB-INF/views/desktop/_common/ya-editor.jsp">
					<c:param name="textareaName">rawText</c:param>
				</c:import>		
				--%>
				 <c:import url="/WEB-INF/views/desktop/_common/redwood-editor.jsp" />		
		</li>
		<li class="mtl mbl">
			<div id="draft_photos" class="z1 _6a">
				<c:if test="${ empty draft.photos }">
					<div class="no_photos">사진 없음</div>
				</c:if>
				<ul id="draftPhotoList">
				<c:if test="${ not empty draft.photos }">
					<c:forEach items="${ draft.photos }" var="photo">
						<li class="draftPhotoBoxWrap l-ft" data-pid="${photo.id }" >
							<label class="closeButtonLabel ui-close-button" for="u_0_7">
								<input type="button" title="삭제">
							</label>
							<div class="draftPhotoBox">
								<img class="img" src="${photo.smallSrc }" />
							</div>
							<div class="draftPhotoControl">
								<div class="draftSizeInputs">
									<div class="draftSizeWidth l-ft">
										<span>width</span><br />
										<input type="text" value="${photo.width }"/>
									</div>
									<div class="draftSizeHeight l-ft">
										<span>height</span><br />
										<input type="text" value="${photo.height }"/>
									</div>
								</div>
								<div class="draftButtons">
									<a href="#" class="ui-button ui-button-overlay photo-inject" data-hugesrc="${photo.originalSrc }">현재 커서에 넣기</a>
								</div>
							</div>
						</li>
					</c:forEach>
				</c:if>
				</ul>
				
				<%--
				<c:if test="${ not empty draft.photos }">
					<c:forEach items="${ draft.photos }" var="photo">
						<div class="draftPhotoBoxWrap" data-pid="${photo.id }">
							<div class="draftPhotoBox">
								<img class="img" src="${photo.smallSrc }" />
								<label class="closeButton ui-close-button" for="u_0_7">
									<input type="button" title="삭제">
								</label>
							</div>
							<div class="draftPhotoControl">
								<a href="#" class="photo-inject" data-hugesrc="${photo.hugeSrc }">위 커서에 넣기</a>
							</div>
						</div>
					</c:forEach>
				</c:if>
				 --%>
			</div>
			
		</li>
		<c:if test="${param.typeName == 'note' }">
			<li>
				<label for="tag-input">태그</label>
							<input id="tag-input" />
				
			</li>
		</c:if>
									
	</ul>
</form>
			<div>
<form id="proxy-file-form" style="position:absolute; left: -10000px; top: -10000px;" enctype="multipart/form-data"
	action="/upload/${param.typeName}"
	target="if-file-upload" method="post">
	<input multiple="" type="file" name="file" onchange="${param.typeName}PicUploader &amp;&amp; ${param.typeName}PicUploader.onFileSelected(this);" accept="image/*">
	<input type="hidden" name="userId" value="${authUserId }">
	<input type="hidden" name="domain" value="${site.domain }">
</form>
<iframe name="if-file-upload" src="/upload/${param.typeName}" style="display:none;"></iframe>
		</div>					
		
			<div class="mtm">
				<label id="u_0_2" class="text-edit-ui-button-bk r-ft" for="u_0_3">
						<a href="#" id="u_0_3" data-type="publish" onclick="Event.__submit(this);">답변등록</a>
				</label>										
<!-- 				<label id="u_0_6" class="text-edit-ui-button r-ft mrs" for="u_0_7"> -->
<!-- 						<a href="#" id="u_0_7" data-type="draft" onclick="Event.__submit(this);">임시저장</a> -->
<!-- 				</label> -->
<!-- 				<label id="u_0_4" class="text-edit-ui-button r-ft mrs" for="u_0_5"> -->
<!-- 						<a href="#" id="u_0_5" data-type="preview" onclick="Event.__submit(this);">미리보기</a> -->
<!-- 				</label> -->
				<%--
				<label id="u_0_10" class="text-edit-ui-button ui-button-large r-ft" for="u_0_11">
					<input id="u_0_7" type="submit" name="cancel" value="취소하기" onclick="this.focus();">
				</label>										
				 --%>
<!-- 				 <label id="#" class="text-edit-ui-button l-ft" for=""> -->
<!-- 						<input id="" type="submit" name="" value="돌아가기" onclick="this.focus();"> -->
<!-- 				</label> -->
			</div>
						
