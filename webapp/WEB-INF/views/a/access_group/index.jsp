<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>

<!-- BEGIN:common head contents -->
<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title"></c:param>
</c:import>
<!-- END:common head contents -->

<!-- BEGIN:ace scripts -->
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-elements.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-extra.js"></script>
<!-- END:ace scripts -->

<!-- BEGIN:additional plugin -->
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/bootbox.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/jquery.validate.js"></script>
<!-- END:additional plugin -->

<script>

$( function() {
	// 
	$('table th input:checkbox').onHMOClick(null , function(){
		var that = this;
		$(this).
		closest('table').
		find('tr > td:first-child input:checkbox').
		each(function(){
			this.checked = that.checked;
			$(this).closest('tr').toggleClass('selected');
		});
	});
	
	// Initialize Dialog
	bootbox.setLocale( "kr" );
	//
	$("#btn-new-user").onHMOClick(null, function() {
		bootbox.dialog( "res-dialog-new-user", [{
			"label" : "취소",
			"class" : "hmo-button hmo-button-white hmo-button-small-10"
		},{
			"label" : "확인",
			"class" : "hmo-button hmo-button-blue hmo-button-small-10",
			"callback": function() {
				var result = $( "#form-new-user" ).valid();
				if( !result ) {
					return false;
				}
				//
				var data = {}, arr;
				
				arr = $('#form-new-user').serializeArrayAlt();
				$.each( arr, function() {
					data[this.name] = this.value;
				});
				
				$.ajax({
					url:"",
					type:"post",
					dataType:"json",
					headers: {
						'Accept':'application/json',
						'Content-Type':'application/json'
						},
				    data: JSON.stringify( deepen( data ) ),
				    success: function( data ) {
				    	window.location.reload();
				    },
					error:function( jqXHR,textStatus,errorThrown ) {
						$.log( "error:Event.__inlineSubmit:" + errorThrown );
					}
				});
				return true;
			}
		}],{
			"header" : "새사용자 생성",	
			"embed" : true	
		});
	});
	//	
	$("#btn-delete-user").onHMOClick(null, function() {
		bootbox.confirm("<strong>정말로 삭제 하시겠습니까?</strong>", function( result ) {
			if( !result ) {
				return;
			}
			
			var chks = $("#a-table-user-list input[type='checkbox']:checked:enabled")
			var ids = [];
			chks.each( function( index, chk ) {
				var id = $( chk ).attr( "data-userid" );
				id && ids.push( id );
			});
			
			$.log ( ids );
		});		
	});
	//
// 	$(".rw-modi-userinfo").on(ace.click_event, function() {
// 		bootbox.dialog( "res-dialog-setting-division", [{
// 			"label" : "취소",
// 			"class" : "hmo-button hmo-button-white hmo-button-small-10"
// 		},{
// 			"label" : "확인",
// 			"class" : "hmo-button hmo-button-blue hmo-button-small-10",
// 			"callback": function() {
// 			}
// 		}],{
// 			"header" : "사용자정보 수정",	
// 			"embed" : true,
// 			"onInit" : function() {
// 				//alert( "call init" );	
// 			},
// 			"onFinalize" : function() {
// 				//alert( "call finalize" );	
// 			}
// 		});
// 	});
	
	
	//validation for new user
	$( "#form-new-user" ).validate({
		errorElement: 'span',
		errorClass: 'help-inline',
		focusInvalid: false,
		onfocusout:function (element) {
			$(element).valid();
	    },
		rules: {
			"userName" : {
				required: true
			},
			"userEmail": {
				required: true,
				email: true,
			},
			"userPassword": {
				required: true
			},
			"userPasswordConfirm": {
				required: true,
				equalTo: "#password"
			}			
		},
		messages: {
			"userName": {
				required: " "
			},
			"userEmail": {
				required: " ",
				email: " "
			},
			"userPassword": {
				required: " "
			},
			"userPasswordConfirm": {
				required: " ",
				equalTo: " "
			}
		},
		invalidHandler: function (event, validator) {	
		},
		highlight: function (e) {
			$(e).closest('.control-group').removeClass('info').addClass('error');
		},
		success: function (e) {
			$(e).closest('.control-group').removeClass('error').addClass('info');
			$(e).remove();
		},
		errorPlacement: function ( error, element ) {
			if(element.is(':checkbox') || element.is(':radio')) {
				var controls = element.closest('.controls');
				if(controls.find(':checkbox,:radio').length > 1) {
					controls.append(error);
				} else {
					error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
				}
			} else if(element.is('.select2')) {
				error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
			} else if(element.is('.chosen-select')) {
				error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
			} else {
				error.insertAfter( element.parent().eq(0) );
			}
		},
		submitHandler: function (form) {
		}
	});		
});

</script>
</head>
<body class="scroll-y">
<div class="rw-snn-wrap" id="rw-snn-wrap">
	<div id="rw-snn-navi-wrap"></div>
	<div id="rw-snn-main" class="rw-snn">

		<div id="rw-view-shield"></div>

		<!-- BEGIN:navbar -->
		<div class="navbar" id="navbar">
			<c:import url="/WEB-INF/views/common/navbar.jsp">
			</c:import>
		</div>
		<!-- END:navbar -->
		
		<!-- BEGIN:main-container -->
		<div class="main-container container-fluid">
	
			<!-- BEGIN:sidebar -->		
			<div class="sidebar" id="snn-sidebar">
				<!-- BEGIN:welcome-box -->		
				<c:import url="/WEB-INF/views/common/welcome-box.jsp">
				</c:import>
				<!-- END:welcome-box -->		
	
				<!-- BEGIN:nav-list -->
				<c:import url="/WEB-INF/views/common/nav-list-a.jsp">
					<c:param name="menuName">권한그룹관리</c:param>
				</c:import>			
				<!-- END:nav-list -->
			</div>
			<!-- END:sidebar -->		
	
			<!-- BEGIN:main-content -->			
			<div class="main-content">
			
				<!-- BEGIN:breadcrumbs&search -->	
				<c:set var="breadcrumbs" value="관리자콘솔,권한그룹관리" />
				<c:set var="breadcrumbLinks" value="/a,/a/accessGroup" />
				<div class="breadcrumbs" id="breadcrumbs">
					<c:import url="/WEB-INF/views/common/breadcrumbs.jsp">
						<c:param name="breadcrumbs">${breadcrumbs }</c:param>	
						<c:param name="breadcrumbLinks">${breadcrumbLinks }</c:param>	
					</c:import>			
				</div>
				<!-- END:breadcrumbs&search -->	
				
				<!-- BEGIN:page-content -->						
				<div class="page-content">
					<div class="rw-content-area-wrap">
						<div class="rw-content-area rw-content-fs">
							<div class="rw-pagelet-blank"></div>
							<div class="rw-pagelet-wrap rw-mtl">
								<table id="a-table-user-list" class="table table-striped table-bordered table-hover rw-tbl-cell-vertical">
									<thead>
										<tr>
											<th class="center">
												<label>
													<input type="checkbox" class="ace">
													<span class="lbl"></span>
												</label>
											</th>
											<th class="center">번호</th>
											<th>이름</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${pagedResult.contents }" var="smallGroup" varStatus="status">
											<tr>
												<td class="center middle">
													<label>
														<input type="checkbox" class="ace" data-userid="${smallGroup.id }">
														<span class="lbl"></span>
													</label>
												</td>
												<td class="center"><a href="/a/department/${smallGroup.id}/info" class="rw-modi-userinfo" data-userid="${smallGroup.id }">${status.index + 1}</a></td>
												<td>
													${smallGroup.name }
												</td>
											</tr>
										</c:forEach>
	<!-- 									<tr> -->
	<!-- 										<td class="center"> -->
	<!-- 											<label> -->
	<!-- 												<input type="checkbox" class="ace" data-userid="0000002"> -->
	<!-- 												<span class="lbl"></span> -->
	<!-- 											</label> -->
	<!-- 										</td> -->
	<!-- 										<td class="center">2</td> -->
	<!-- 										<td class="hidden-767"> -->
	<!-- 											<a class="rw-profilebox-block" href="#"> -->
	<!-- 												<img src="http://cdn.sunnyvale.co.kr/s0/12/59/1378345085912_2450_m.jpg" alt=""> -->
	<!-- 											</a> -->
	<!-- 										</td> -->
	<!-- 										<td> -->
	<!-- 											<a href="#" class="rw-modi-userinfo" data-userid="0000002">임성묵</a> -->
	<!-- 										</td> -->
	<!-- 										<td> -->
	<!-- 											선임연구원 -->
	<!-- 										</td> -->
	<!-- 										<td class="hidden-480">kickscar@gmail.com</td> -->
	<!-- 										<td> -->
	<!-- 											소셜컴퓨팅연구팀<br> -->
	<!-- 											전산실 -->
	<!-- 										</td> -->
	<!-- 										<td> -->
	<!-- 											<span class="hmo-label hmo-label-green hmo-label-middle-10">근무중</span> -->
	<!-- 										</td> -->
	<!-- 										<td class="hidden-979"> -->
	<!-- 											우리회사에서 제일 잘 생긴 놈. -->
	<!-- 										</td> -->
	<!-- 									</tr> -->
	<!-- 									<tr> -->
	<!-- 										<td class="center"> -->
	<!-- 											<label> -->
	<!-- 												<input type="checkbox" class="ace" data-userid="0000003"> -->
	<!-- 												<span class="lbl"></span> -->
	<!-- 											</label> -->
	<!-- 										</td> -->
	<!-- 										<td class="center">3</td> -->
	<!-- 										<td class="hidden-767"> -->
	<!-- 											<a class="rw-profilebox-block" href="#"> -->
	<!-- 												<img src="http://cdn.sunnyvale.co.kr/s2/74/44/1381829574474_2659_m.jpg" alt=""> -->
	<!-- 											</a> -->
	<!-- 										</td> -->
	<!-- 										<td> -->
	<!-- 											<a href="#" class="rw-modi-userinfo" data-userid="0000003">김성수</a> -->
	<!-- 										</td> -->
	<!-- 										<td> -->
	<!-- 											선임연구원 -->
	<!-- 										</td> -->
	<!-- 										<td class="hidden-480">kickscar@gmail.com</td> -->
	<!-- 										<td> -->
	<!-- 											소셜컴퓨팅연구팀<br> -->
	<!-- 											전산실 -->
	<!-- 										</td> -->
	<!-- 										<td > -->
	<!-- 											<span class="hmo-label-orange hmo-label hmo-label-middle-10">휴가</span> -->
	<!-- 										</td> -->
	<!-- 										<td class="hidden-979"> -->
	<!-- 											우리회사에서 제일 잘 생긴 놈. -->
	<!-- 										</td> -->
	<!-- 									</tr> -->
									</tbody>
								</table>
								<div class="row-fluid">
									<div class="span12 rw-a-user-list-buttons ta-right">
										<a class="hmo-button hmo-button-white hmo-button-small-10" id="btn-delete-user">선택삭제</a>
										<a class="hmo-button hmo-button-blue hmo-button-small-10" id="btn-new-user">새부서</a>
									</div>	
									<div class="span6">
										<div class="pagination" id="a-table-user-list-pagination">
											<ul>
												<c:choose>
												<c:when test="${pagedResult.pageNumber > 10 }">
												<li>
													<a href="?page=${pagedResult.pageNumber - 1 }"><i class="icon-double-angle-left"></i></a>
												</li>
												</c:when>
												<c:otherwise>
												<li class="disabled">
													<a href="#"><i class="icon-double-angle-left"></i></a>
												</li>
												</c:otherwise>
												</c:choose>
												<c:forEach var="pageIndex" begin="${pagedResult.startPageNum }" end="${pagedResult.endPageNum }">
													<li class="${pageIndex == pagedResult.pageNumber ? 'active' : '' }">
														<a href="?page=${pageIndex }"><c:out value="${pageIndex }" /></a>
													</li>
												</c:forEach>
												<c:if test="${pagedResult.endPageNum < pagedResult.lastPageNum }">
													<li>
														<a href="?page=${pagedResult.lastPageNum }">${ pagedResult.lastPageNum}</a>
														</li>
												</c:if>
												
												
												<c:choose>
												<c:when test="${pagedResult.lastPageNum  > (pagedResult.endPageNum + 1) }">											
													<li>
													<c:set var="endSub" value="${ pagedResult.lastPageNum - ( pagedResult.endPageNum + 1) }" />
														<a href="?page=${ endSub > 10 ? pagedResult.pageNumber + 10 : pagedResult.pageNumber + endSub }">
															<i class="icon-double-angle-right"></i>
														</a>
													</li>
												</c:when>
												<c:otherwise>
													<li class="disabled">
														<a href="#"><i class="icon-double-angle-right"></i></a>
													</li>
												</c:otherwise>
												</c:choose>				
											</ul>
										</div>
									</div>	
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- END:page-content -->						
			</div>
			<!-- END:main-content -->	
		</div>
		<!-- EDN:main-container -->
	</div>
</div>	
	
<!-- BEGIN:resource of dialog for creating user -->
<div id="res-dialog-new-user" style="display:none">
	<div class="rw-dialog-wrap rw-form-wrap">
		<form class="form-vertical rw-form" id="form-new-user">
			<div class="row-fluid">
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="name">이름</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon input-icon-right rw-input-wrap">
									<input class="span12" type="text" name="userName" id="name" placeholder="이름">
									<i class="icon-remove-sign"></i>
									<i class="icon-ok-sign"></i>
								</span>
							</div>
						</div>
					</div>				
					<div class="control-group">
						<label class="control-label" for="companyEmail">이메일 주소</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon input-icon-right rw-input-wrap">
									<input class="span12" type="email" name="userEmail" id="companyEmail" placeholder="you@emailaddress.com">
									<i class="icon-remove-sign"></i>
									<i class="icon-ok-sign"></i>
								</span>
							</div>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="password">비밀번호 만들기</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon input-icon-right rw-input-wrap">
									<input class="span12" type="password" name="userPassword" id="password" placeholder="비밀번호">
									<i class="icon-remove-sign"></i>
									<i class="icon-ok-sign"></i>
								</span>
							</div>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="passwordConfirm">비밀번호  확인</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon input-icon-right rw-input-wrap">
									<input class="span12" type="password" name="userPasswordConfirm" id="passwordConfirm" placeholder="비밀번호 확인">
									<i class="icon-remove-sign"></i>
									<i class="icon-ok-sign"></i>
								</span>
							</div>
						</div>
					</div>				
				</div>
				<div class="span6">
					<div class="control-group">
						<label class="control-label" for="job1">직책1</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon input-icon-right rw-input-wrap">
									<input class="span12" type="text" name="jobTitlesString1" id="job1" placeholder="직책1">
								</span>
							</div>
						</div>
					</div>				
					<div class="control-group">
						<label class="control-label" for="job2">직책2</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon input-icon-right rw-input-wrap">
									<input class="span12" type="text" name="jobTitlesString2" id="job2" placeholder="직책2">
								</span>
							</div>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="job3">직책3</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon input-icon-right rw-input-wrap">
									<input class="span12" type="text" name="jobTitlesString3" id="job3" placeholder="직책3">
								</span>
							</div>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="desc">설명</label>
						<div class="controls">
							<div class="row-fluid">
								<span class="span12 input-icon input-icon-right rw-input-wrap">
									<input class="span12" type="text" name="userDescription" id="desc" placeholder="설명">
								</span>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span12">
					<div class="control-group site-admin-checkbox">
						<label class="rw-chckbx">
							<input name="admin" id="isAdmin" type="checkbox" class="ace" value="true">
							<span class="lbl">
								<span>사이트 관리자 입니다.</span>
							</span>
						</label>
					</div>				
					<div class="control-group">
						<label class="rw-chckbx">
							<input name="sendConfirmMail" id="sendMail" type="checkbox" class="ace" value="true">
							<span class="lbl">
								<span>새로 생성됭 사용자에게 알림 이메일을 보냅니다.</span>
							</span>
						</label>
					</div>				
				</div>
			</div>
		</form>
	</div>
</div>
<!-- END:resource of dialog for creating user -->
	
<!-- BEGIN:resource of dialog for updating user -->
<div id='res-dialog-setting-division' style='display:none'>
	<div class='rw-dialog-wrap'>
		<div class='row-fluid'>
			<div class='span12'>
				<div class='tabbable'>
					<ul class='nav nav-tabs padding-12 tab-color-blue background-blue' id=''>
						<li class='active'>
							<a data-toggle='tab' href='#tab-info'>정보</a>
						</li>
						<li>
							<a data-toggle='tab' href='#tab-division'>부서</a>
						</li>
						<li>
							<a data-toggle='tab' href='#tab-project'>과제</a>
						</li>
						<li>
							<a data-toggle='tab' href='#tab-group'>소그룹</a>
						</li>
						<li>
							<a data-toggle='tab' href='#tab-ugroup'>사용자그룹</a>
						</li>
					</ul>
					<div class='tab-content'>
						<div id='tab-info' class='tab-pane in active'>
							<p>기본정보 관리</p>
						</div>

						<div id='tab-division' class='tab-pane'>
							<p>포함된 부서 관리</p>
						</div>
						<div id='tab-project' class='tab-pane'>
							<p>포함된 과제 관리</p>
						</div>
						<div id='tab-group' class='tab-pane'>
							<p>포함된 소그룹 관리</p>
						</div>
						<div id='tab-ugroup' class='tab-pane'>
							<p>사용자 그룹 관리</p>
						</div>
					</div>
				</div>
			</div>						
		</div>							
	</div>
</div>
<!-- END:resource of dialog for updating user -->

</body>
</html>