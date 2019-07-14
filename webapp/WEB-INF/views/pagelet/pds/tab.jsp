<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- <ul id="pds-tabs" class="inbox-tabs nav nav-tabs padding-16 tab-size-bigger tab-space-1" style="margin:0px;"> -->
<%-- 	<li class="${tab == 'all' ? 'active' : '' }"> --%>
<%-- 		<a class="pds-tabs" href="/pds"> --%>
<!-- 			<i class="blue fa fa-download bigger-130"></i> -->
<!-- 			<span class="bigger-110">전체보기</span> -->
<!-- 		</a> -->
<!-- 	</li> -->

<%-- 	<li class="${tab == 'my' ? 'active' : '' }"> --%>
<%-- 		<a class="pds-tabs" href="/pds/my"> --%>
<!-- 			<i class="orange fa fa-user bigger-130 "></i> -->
<!-- 			<span class="bigger-110">내가 올린</span> -->
<!-- 		</a> -->
<!-- 	</li> -->
<!-- </ul> -->

<div class="lighter blue">
	<ul class="nav nav-pills">
		<li class="${tab == 'all' ? 'active' : '' }"><a href="${groupPath }/pds">전체보기</a></li>
		<li class="${tab == 'my' ? 'active' : '' }"><a href="${groupPath }/pds/my">내가 올린</a></li>
		<li class="pull-right" style="position:relative;">
			<a href="#" data-toggle="dropdown" class="dropdown-toggle">
					<span>정렬</span>
					<i class="fa fa-caret-down bigger-125"></i>
				</a>
				<ul class="dropdown-menu dropdown-lighter pull-right dropdown-100">
					<li>
						<a href="#">
							<i class="icon-ok green"></i>
							생성날짜
						</a>
					</li>
				</ul>
		</li>
	</ul>
</div>
								