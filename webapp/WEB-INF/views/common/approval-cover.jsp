<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<div >
	<%-- <div class="page-header position-relative">
		<h1>
			<a href="/approval"><i class="fa fa-folder-open fa-1g mb-mgr"></i><span>전자결재</span></a>
		</h1>
		
	</div>--%>
	<div class="z1">	
		<div class="nav-pills-wrap">
			<ul class="nav nav-pills">
				<li class="${param.nav == 'process' ? 'active' : '' } approval-rw-pagelet-menu-li">
					<a	class="dropdown-toggle"
					id="approval-progress-context-trigger"
					aria-owns="approval-progress-context-popup-menus"
					aria-haspopup="true"
					rel="toggle">
						진행중&nbsp;<i class="fa fa-caret-down fa-1g"></i>
					</a>
					<ul id="approval-progress-context-popup-menus"
					class="dropdown-menu dropdown-info  dropdown-caret ui-toggle-flyout approval-rw-pagelet-popupmenu-ul"
					data-popup-trigger="approval-progress-context-trigger"
					data-popup-group="global">
						<li class="active">
							<a href="/approval?menu=process"><c:if test="${param.menu == 'index' }"><i class="fa fa-check"></i></c:if>모두보기</a>
						</li>
						<li>
							<a href="/approval?menu=sent_process"><c:if test="${param.menu == 'sent_process' }"></c:if>상신 진행 문서함</a>
						</li>
						<li>
							<a	href="/approval?menu=receive_process"><c:if test="${param.menu == 'receive_process' }"></c:if>결재 수신 문서함</a>
						</li>
					</ul>
				</li>
				
				<li  class="${param.nav == 'approved' ? 'active' : '' } approval-rw-pagelet-menu-li">
					<a	class="dropdown-toggle"
						id="approval-complete-context-trigger"
						aria-owns="approval-complete-context-popup-menus"
						aria-haspopup="true"
						rel="toggle">
						완료문서함&nbsp;<i class="fa fa-caret-down fa-1g"></i>
					</a>
					<ul id="approval-complete-context-popup-menus"
						class="dropdown-menu dropdown-info  dropdown-caret ui-toggle-flyout approval-rw-pagelet-popupmenu-ul"
						data-popup-trigger="approval-complete-context-trigger"
						data-popup-group="global">
						<li>
							<a href="/approval?menu=approved"><c:if test="${param.menu == 'approved' }"></c:if>모두보기</a>
						</li>
						<li>
							<a href="/approval?menu=sent_approved"><c:if test="${param.menu == 'sent_approved' }"></c:if>상신 완료 문서함</a>
						</li>
						<li>
							<a	href="/approval?menu=receive_approved"><c:if test="${param.menu == 'receive_approved' }"></c:if>결재 완료 문서함</a>
						</li>
					</ul>
				</li>
				
				<li  class="${param.nav == 'misc' ? 'active' : '' } approval-rw-pagelet-menu-li approval-rw-pagelet-menu-li-f">
					 <a	class="dropdown-toggle"
						id="approval-misc-context-trigger"
						aria-owns="approval-misc-context-popup-menus"
						aria-haspopup="true"
						rel="toggle">
						기타 문서함&nbsp;<i class="fa fa-caret-down fa-1g"></i>
					</a>
					<ul id="approval-misc-context-popup-menus"
						class="dropdown-menu dropdown-info  dropdown-caret ui-toggle-flyout approval-rw-pagelet-popupmenu-ul"
						data-popup-trigger="approval-misc-context-trigger"
						data-popup-group="global">
						<li>
							<a href="/approval?menu=sent_reject"><c:if test="${param.menu == 'sent_reject' }"></c:if>반려된 문서함</a>
						</li>
						<li>
							<a href="/approval?menu=receive_reject"><c:if test="${param.menu == 'receive_reject' }"></c:if>반려한 문서함</a>
						</li>
						<li>
							<a	href="/approval?menu=receive"><c:if test="${param.menu == 'receive' }"></c:if>수신 문서함</a>
						</li>
						<li>
							<a	href="/approval?menu=circulation"><c:if test="${param.menu == 'circulation' }"></c:if>회람 문서함</a>
						</li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
</div>