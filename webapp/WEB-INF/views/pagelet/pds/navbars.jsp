<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<div id="id-message-list-navbar" class="${current != 'list' ? 'hide' : '' } clearfix" style="border-bottom: 1px solid #eee">
<!-- 	<div style="height:29px;"> -->
<!-- 		<div class="messagebar-item-right"> -->
<!-- 			<div class="inline position-relative"> -->
<!-- 				<a href="#" data-toggle="dropdown" class="dropdown-toggle"> -->
<!-- 					정렬 &nbsp; -->
<!-- 					<i class="fa fa-caret-down bigger-125"></i> -->
<!-- 				</a> -->

<!-- 				<ul class="dropdown-menu dropdown-lighter pull-right dropdown-100"> -->
<!-- 					<li> -->
<!-- 						<a href="#"> -->
<!-- 							<i class="icon-ok green"></i> -->
<!-- 							생성날짜 -->
<!-- 						</a> -->
<!-- 					</li> -->
<!-- 				</ul> -->
<!-- 			</div> -->
<!-- 		</div> -->

<!-- 		<div class="nav-search minimized"> -->
<%-- 			<form id="pds-search-form" action="/pagelet/pds" method="GET" class="form-search" class="form-search"> --%>
<!-- 				<span class="input-icon"> -->
<!-- 					<input type="text" autocomplete="off" class="input-small nav-search-input" placeholder="이름 및 제목입력" /> -->
<!-- 					<i class="fa fa-search nav-search-icon" style="top:1px; left: 3px; "></i> -->
<!-- 				</span> -->
<!-- 			</form> -->
<!-- 		</div> -->
<!-- 	</div> -->

</div>
<div id="id-message-item-navbar" class="${current != 'view' ? 'hide' : '' }  message-navbar align-center clearfix">

	<div style="height:29px;">
		<div class="messagebar-item-left">
			<a href="${groupPath}/pds" class="btn-back-message-list pathchange" data-view="list">
				<i class="fa fa-arrow-left blue bigger-110 middle"></i>
				<b class="bigger-110 middle">Back</b>
			</a>
		</div>

		<div class="messagebar-item-right" style="top:7px; ">
			<div class="inline position-relative align-left">
				<a href="#" class="btn-message btn btn-mini dropdown-toggle" data-toggle="dropdown">
					<span class="bigger-110"></span>

					<i class="fa fa-caret-down"></i>
				</a>

				<ul class="dropdown-menu dropdown-lighter dropdown-caret dropdown-125 pull-right">
					<li>
						<a href="/content/delete" class="remove-trigger" role="hmo-dialog" aria-controls="hmo-messagebox-yesno" data-label="자료삭제" data-message="삭제되면 복구할 수 없습니다.<br />이 자료를 삭제하시겠습니까?" rel="sync-get" ajaxify="ajax_remove_pds" <c:if test="${not empty content }">data-request-map='{"id":"${content.id }"}' </c:if>>
							<i class="fa fa-trash-o blue"></i>
							&nbsp; 삭제
						</a>
					</li>
				</ul>
			</div>
		</div>
	</div>
</div>
<div id="id-message-new-navbar" class="${current != 'write' ? 'hide' : '' }  message-navbar align-center clearfix">

	<div class="message-item-bar" style="height:29px;">
		<div class="messagebar-item-left">
			<a href="${groupPath}/pds" class="btn-back-message-list no-hover-underline pathchange" data-view="list">
				<i class="icon-arrow-left blue bigger-110 middle"></i>
				<b class="middle bigger-110">Back</b>
			</a>
		</div>

		<div class="messagebar-item-right">
		</div>
	</div>
</div>