<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div id="reply-photo-viewer-full-screen" class="z1 ui-layer _l3 _3qx _3qw _abs _3qz full-screen-photo-viewer" style="display:none" onclick="Reply.closePhotoFullScreen(); return false;"">
	<div class="_l2">
		<div class="_l1">
			<c:choose>
				<c:when test="${sunny.device.mobile || sunny.device.tablet }">
					<img style="width:100%">
				</c:when>
				<c:otherwise>
					<img >
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>
<div id="photo-viewer-full-screen" class="z1 ui-layer _l3 _3qx _3qw _abs _3qz full-screen-photo-viewer" style="display:none"  onclick="FileViewer.closeFullScreen(); return false;">
	<div class="_l2">
		<div class="_l1">
			<img style="display:none">
		</div>
	</div>
</div>
<div id="photos-viewer" class="ui-layer _l3 _3qx _3qw  photos-viewer full-screen-available">
	<div class="_l2">
		<div class="_l1">
			<div id="photo-viewer-container" class="photo-viewer-container ui-contextual-layer-parent">
				<div class="z1 photo-viewer-popup">
					<c:choose>
						<c:when test="${not sunny.device.mobile &&  not sunny.device.tablet }">
							<div class="stage-wrapper l-ft">
								<div id="full-screen-switch" class="photo-viewer-full-screen full-screen-switch">
									<a	id="photo-viewer-full-screen-switch" 
										role="button"
										href="#"
										data-tooltip-alignh="left"
										data-tooltip-position="below"
										aria-label="사진 원본 보기"
										onclick="FileViewer.openFullScreen(); return false;"
										data-hover="tooltip"
										style="display:;">
										<i class="fa fa-expand fa-1g"></i></a>
											
									<a	id="file-download-switch" 
										role="button"
										href="#"
										data-tooltip-alignh="left"
										data-tooltip-position="below"
										aria-label="파일 다운로드"
										data-hover="tooltip"
										style="display:none">
										<i class="fa fa-download fa-1g"></i></a>
								</div>
								<div class="stage photo-stage" onclick="FileViewer.changeImage(); return false;" >
									<img class="spotlight" alt="" style="display:none" >
									<div class="stage-outter"></div>
								</div>
								<div class="stage file-stage" onclick="FileViewer.changeImage(); return false;" style="display:none">
									<div class="file-spot">
										<img src="/assets/sunny/2.0/img/file-icon.png">
										<div class="file-desc">
											<span class="file-desc-item" id="file-stage-name"></span>
											<span class="file-desc-item _l" id="file-stage-size"></span>																			
										</div>
									</div>
								</div>
								<a class="snow-lift-pager prev" title="이전"><i class="fa fa-arrow-left fa-1g" onclick="FileViewer.prevImage(); return false;"></i></a>
								<a class="snow-lift-pager next" title="다음"><i class="fa fa-arrow-right fa-1g" onclick="FileViewer.nextImage(); return false;"></i></a>
								<div id="bottom-stage" class="bottom-stage"></div>
							</div>
							<div class="story-ufi-container">
								<div class="detail-content-header">
									<div class="photos-viewer-controls">
										<a	class="close-theater" 
											role="button"
											aria-label="Esc 키를 누르면 창이 닫힙니다."
											data-hover="tooltip"
											data-tooltip-alignh="right"
											data-tooltip-position="below">
											<i class="fa fa-times fa-1g"></i></a>
									</div>
								</div>
								<div id="detail-content-body"></div>
							</div>
						</c:when>
						<c:otherwise>
							<div class="stage-wrapper" style="display:table; overflow:hidden">
								<div class="stage-swipe-list swipe-stage" style="position:relative;">
									<div class="stage-swipe-wrap" style="position:relative; width:20000px" id="stage-swipe_list">
									</div>
								</div>																
							</div>							
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
		<div class="_l4" id="closer-theater" style="position:absolute; top:10px; right:10px; z-index:10">
			<a	class="close-theater-button hmo-button"
				role="button"
				href="#">
				<span>닫기</span></a>
		</div>
		<div id="mention-select-box-wrap" style="position:absolute; height:0; width:271px;">
			<div class="mention-select-box" style="display:none">
				<ul></ul>	
			</div>
		</div>									
	</div>
	<c:if test="${sunny.device.mobile  ||  sunny.device.tablet }">
		<div class="story-ufh-container" id="story-ufh-container"></div>
	</c:if>
</div>
<form>
<textarea tabindex="-1" id="fv-ta-cmmnt-mirroring" class="textarea-mirroring cmmnt-mirroring"></textarea>
</form>
<c:choose>
<c:when test="${sunny.device.mobile || sunny.device.tablet }">
	<script src="/assets/sunny/2.0/js/uncompressed/file-viewer-m.js"></script>
</c:when>
<c:otherwise>
	<script src="/assets/sunny/2.0/js/uncompressed/file-viewer.js"></script>
</c:otherwise>
</c:choose>

											