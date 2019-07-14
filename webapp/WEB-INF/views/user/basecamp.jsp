<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>

<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">${basecampUser.name }</c:param>
	<c:param name="bsUsed">YES</c:param>
	<c:param name="aceUsed">YES</c:param>
	<c:param name="hmoUsed">YES</c:param>	
</c:import>

<style type="text/css">
.label-large.arrowed-in-right:after		{ display:none;}
</style>

<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/select2.css" />
<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/bootstrap-editable.css" />
<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/jquery.gritter.css" />

<!-- BEGIN:ace scripts -->
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/bootstrap.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-elements.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/select2.min.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/date-time/bootstrap-datepicker.min.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/fuelux/fuelux.spinner.min.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/x-editable/bootstrap-editable.min.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/x-editable/ace-editable.min.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/jquery.gritter.min.js"></script>
<!-- END:sunny extra libraries -->

<script>
var settingUrl = '/user/${basecampUser.id}/alter_settings';
document.domain = "${sunny.documentDomain}";
var cdnUrl = "";  

$(function() {
	//editables on first profile page
	$.fn.editable.defaults.mode = 'inline';
	$.fn.editableform.loading = "<div class='editableform-loading'><i class='light-blue icon-2x icon-spinner icon-spin'></i></div>";
    $.fn.editableform.buttons = '<button type="button" class="hmo-button-small-5 hmo-button hmo-button-white editable-cancel">취소</button>'+
  							    '<button type="submit" class="hmo-button-small-5 hmo-button hmo-button-blue editable-submit">저장</button>';  

	$(".change-status").onHMOClick(null, function(e){
		e.preventDefault();
		var _$this = $(this);
		var statNum = _$this.data("id");
		$.ajax({

			headers: {
			'Accept':'application/json',
			'Content-Type':'application/json'
			},
			url:settingUrl,
			type:"POST",
			data:JSON.stringify({"status":statNum}),
			dataType:"json",
			success:function(data){
				if( data.result == "fail"){
					alert(data.message);
					return false;
				}
				$("#basecamp-status").html(_$this.html());	
			},
			error:function(data){
			}
		});
	});

	<%--
	$(".request-friend").click(function(e){

		e.preventDefault();
		
		var _$this = $(this);
		var userId = _$this.data("uid");
		var type = _$this.data("type");
		$.ajax({
			headers: {
				'Accept':'application/json',
				'Content-Type':'application/json'
				},
			url:"/user/request_friend",
			type:"POST",
			data:JSON.stringify({
				"userId":userId,
				"requestType" : type
			}),
			success:function(data){
				//alert(JSON.stringify(data));
				if( data.result=="fail"){
					alert( data.message );
					return false;
				}
				
				_$this.addClass("hidden-elem");
				
				var nextType = 0;
				if( type == 0 ){
					nextType = 1;
				}else if( type == 1 ){
					nextType = 0;
				}else if( type == 2 ){
					nextType = 3;
				}else if( type == 3 ){
					nextType = 0;
				}
				
				$("#request-friend-${basecampUser.id}-t" + nextType).removeClass("hidden-elem");
				
				
			},
			error:function(data){
				alert(data);
			}
		})		
		
	});
	--%>
	$("#basecamp-message").onHMOClick(null, function(){
		
		bootbox.dialog( "res-dialog-start-chat", [
		{
			"label" : "취소",
			"class" : "hmo-button hmo-button-white hmo-button-small-10"
		}, {
			"label" : "확인",
			"class" : "hmo-button hmo-button-blue hmo-button-small-10",
			"callback" : function() {
				$.ajax({
					url : "/message/create",
					async : false,
					type : "post",
					dataType : "json",
					contentType : "application/json",
					data : JSON
							.stringify({
								"userIds" : ["${basecampUser.id}"]
							}),
					success : function(data) {
						location.href = "/message/"
								+ data.data.id;
					},
					error : function(jqXHR,	textStatus,	errorThrown) {
						$.error("confirm_alter_time:$.ajax:" + errorThrown);
					}
				});
				return true;
			}
		} ], {
			"header" : "${basecampUser.name} 님과 대화를 시작하겠습니까?",
			"embed" : true,
		});

	});
	
	
	$("#change-jobtitles").onHMOClick(null, function(){
		
		bootbox.dialog( "res-dialog-change-jobtitles", [
		{
			"label" : "취소",
			"class" : "hmo-button hmo-button-white hmo-button-small-10"
		}, {
			"label" : "확인",
			"class" : "hmo-button hmo-button-blue hmo-button-small-10",
			"callback" : function() {
				

				var data = {}, arr;
				
				arr = $('#form-change-jobtitles').serializeArrayAlt();
				$.each( arr, function() {
					data[this.name] = this.value;
				});
				
				$.ajax({
					url : settingUrl,
					async : false,
					type : "post",
					dataType : "json",
					contentType : "application/json",
					headers : {
						'Accept' : 'application/json',
						'Content-Type' : 'application/json'
					},
					data : JSON.stringify(data),
					success : function(data) {
						if( data.result == "fail" ){
				    		alert(data.message);
				    		return;
				    	}
				    	window.location.reload();
					},
					error : function(jqXHR,	textStatus,	errorThrown) {
						$.error("confirm_alter_time:$.ajax:" + errorThrown);
					}
				});
				return true;
			}
		} ], {
			"header" : "직책명을 변경해주세요",
			"embed" : true,
		});

	});
	
		
		//editables
		
		// *** editable avatar *** //
				try {//ie8 throws some harmless exception, so let's catch it
			
					//it seems that editable plugin calls appendChild, and as Image doesn't have it, it causes errors on IE at unpredicted points
					//so let's have a fake appendChild for it!
					if( /msie\s*(8|7|6)/.test(navigator.userAgent.toLowerCase()) ) Image.prototype.appendChild = function(el){}
			
					var last_gritter;
		$("#basecamp-profilePic").editable({
			type: 'image',
			name: 'profilePic',
			value: null,
			image: {
				//specify ace file input plugin's options here
				btn_choose: '프로필 변경',
				droppable: true,
				/**
				//this will override the default before_change that only accepts image files
				before_change: function(files, dropped) {
					return true;
				},
				*/

				//and a few extra ones here
				name: 'profilePic',//put the field name here as well, will be used inside the custom plugin
				max_size: 11000000,//~100Kb
				on_error : function(code) {//on_error function will be called when the selected file has a problem
					if(last_gritter) $.gritter.remove(last_gritter);
					if(code == 1) {//file format error
						last_gritter = $.gritter.add({
							title: 'File is not an image!',
							text: 'Please choose a jpg|gif|png image!',
							class_name: 'gritter-error gritter-center'
						});
					} else if(code == 2) {//file size rror
						last_gritter = $.gritter.add({
							title: 'File too big!',
							text: 'Image size should not exceed 100Kb!',
							class_name: 'gritter-error gritter-center'
						});
					}
					else {//other error
					}
				},
				on_success : function() {
					$.gritter.removeAll();
				}
				
			},
			url: function(params){
				//This is similar to the file-upload.html example
				//Replace the code inside profile page where it says ***UPDATE AVATAR HERE*** with the code below


				//please modify submit_url accordingly
				var submit_url = cdnUrl + '/upload/profile';
				var deferred;


				//if value is empty, means no valid files were selected
				//but it may still be submitted by the plugin, because "" (empty string) is different from previous non-empty value whatever it was
				//so we return just here to prevent problems
				var value = $('#basecamp-profilePic').next().find('input[type=hidden]:eq(0)').val();
				if(!value || value.length == 0) {
					deferred = new $.Deferred
					deferred.resolve();
					return deferred.promise();
				}

				var $form = $('#basecamp-profilePic').next().find('.editableform:eq(0)')
				var file_input = $form.find('input[type=file]:eq(0)');

				//user iframe for older browsers that don't support file upload via FormData & Ajax
// 				if( !("FormData" in window) ) {
					deferred = new $.Deferred
					var iframe_id = 'temporary-iframe-'+(new Date()).getTime()+'-'+(parseInt(Math.random()*1000));
					$form.after('<iframe id="'+iframe_id+'" name="'+iframe_id+'" frameborder="0" width="0" height="0" src="about:blank" style="position:absolute;z-index:-1;"></iframe>');
					$form.append('<input type="hidden" name="temporary-iframe-id" value="'+iframe_id+'" />');
					$form.next().data('deferrer' , deferred);//save the deferred object to the iframe
					$form.attr({'method' : 'POST', 'enctype' : 'multipart/form-data',
								'target':iframe_id, 'action':submit_url});

					$form.get(0).submit();

					//if we don't receive the response after 60 seconds, declare it as failed!
					setTimeout(function(){
						var iframe = document.getElementById(iframe_id);
						if(iframe != null) {
							iframe.src = "about:blank";
							$(iframe).remove();
							
							deferred.reject({'status':'fail','message':'Timeout!'});
						}
					} , 60000);
// 				}
// 				else {
// 					var fd = null;
// 					try {
// 						fd = new FormData($form.get(0));
// 					} catch(e) {
// 						//IE10 throws "SCRIPT5: Access is denied" exception,
// 						//so we need to add the key/value pairs one by one
// 						fd = new FormData();
// 						$.each($form.serializeArray(), function(index, item) {
// 							fd.append(item.name, item.value);
// 						});
// 						//and then add files because files are not included in serializeArray()'s result
// 						$form.find('input[type=file]').each(function(){
// 							if(this.files.length > 0) fd.append(this.getAttribute('name'), this.files[0]);
// 						});
// 					}
					
// 					//if file has been drag&dropped , append it to FormData
// 					if(file_input.data('ace_input_method') == 'drop') {
// 						var files = file_input.data('ace_input_files');
// 						if(files && files.length > 0) {
// 							fd.append(file_input.attr('name'), files[0]);
// 						}
// 					}

// 					deferred = $.ajax({
// 						url: submit_url,
// 						type: 'POST',
// 						processData: false,
// 						contentType: false,
// 						dataType: 'json',
// 						data: fd,
// 						xhr: function() {
// 							var req = $.ajaxSettings.xhr();
// 							/*if (req && req.upload) {
// 								req.upload.addEventListener('progress', function(e) {
// 									if(e.lengthComputable) {	
// 										var done = e.loaded || e.position, total = e.total || e.totalSize;
// 										var percent = parseInt((done/total)*100) + '%';
// 										//bar.css('width', percent).parent().attr('data-percent', percent);
// 									}
// 								}, false);
// 							}*/
// 							return req;
// 						},
// 						beforeSend : function() {
// 							//bar.css('width', '0%').parent().attr('data-percent', '0%');
// 						},
// 						success : function() {
// 							//bar.css('width', '100%').parent().attr('data-percent', '100%');
// 						}
// 					})
// 				}

				deferred.done(function(data){
					data = $.parseJSON(data);
					if(data.resultCode == 0){
						
						var postData = {
							"profilePicId" : data.data.id
						};
						
						$.ajax({
							url:settingUrl,
							type:"post",
							dataType:"json",
							contentType:"application/json",
							data:JSON.stringify(postData),
							success:function(recvData){
								if( recvData.result == "fail"){
									alert( recvData.message);
									return;
								}
								
								$('#basecamp-profilePic').get(0).src = cdnUrl + data.data.originalPath;
								
								if(last_gritter) $.gritter.remove(last_gritter);
								last_gritter = $.gritter.add({
									title: '프로필 사진 업데이트 성공',
									text: '프로필 사진이 업데이트 되었습니다.',
									class_name: 'gritter-info gritter-center'
								});
								
							},
							error: function(jqXHR,textStatus,errorThrown){
								$.error("profilePicUploader:$.ajax:"+errorThrown);
							}		
						});	
					}else alert(data);
				}).fail(function(data){
					alert("Failure");
				});


				return deferred.promise();
			},
			success: function(data, newValue){
			}
		})
				}catch(e) {}
		
		$('.basecamp-editable').editable({
			url : settingUrl,
			send : 'always',
			params : function(params) {
				
				var data = {};
				
				data[params.name] = params.value;
				params = JSON.stringify(data);
				return params;
			},
			ajaxOptions : {
				type : 'POST',
				dataType : 'json',
				headers : {
					'Accept' : 'application/json',
					'Content-Type' : 'application/json'
				}
			},
			success : function(data, newValue) {
				if( data.result == "fail" ){
					return data.message;
				}
				var className = $(this).data("class");
				$("." + className).text(newValue); 
				//alert( JSON.stringify(response) );
				//$("." + "a"+ "-${basecampUser.id}").text(newValue);
				//alert( response );
			}
		});
		
	});
</script>
<!-- END:basecamp script -->
</head>
<body class="scroll-y">
<div class="rw-snn-wrap" id="rw-snn-wrap">
	<div id="rw-snn-navi-wrap"></div>
	<div id="rw-snn-navir-wrap">
		<c:import url="/WEB-INF/views/common/nav-list-right.jsp">
		</c:import>
	</div>	
	<div id="rw-snn-main" class="rw-snn">

		<div id="rw-view-shield"></div>
			
		<!-- BEGIN:navbar -->
		<div class="navbar" id="navbar">
			<c:import url="/WEB-INF/views/common/navbar.jsp">
			</c:import>
		</div>
		<!-- END:navbar -->
		
		<!-- BEGIN:main-container -->
		<div class="main-container container-fluid z1">
			
			<!-- BEGIN:sidebar -->		
			<div class="sidebar" id="snn-sidebar">
			
				<!-- BEGIN:welcome-box -->		
				<c:import url="/WEB-INF/views/common/welcome-box.jsp">
				</c:import>
				<!-- END:welcome-box -->		
	
				<!-- BEGIN:nav-list -->
				<c:import url="/WEB-INF/views/common/nav-list.jsp">
					<c:param name="menuName">베이스캠프</c:param>
				</c:import>			
				<!-- END:nav-list -->
		
			</div>
			<!-- END:sidebar -->
			
			<!-- BEGIN:main-content -->		
			<div class="main-content z1">
			
				<!-- BEGIN:breadcrumbs&search -->	
				<c:set var="breadcrumbs" value="${basecampUser.name }" scope="request"/>
				<c:set var="breadcrumbLinks" value="#" scope="request"/>
				<div class="breadcrumbs" id="breadcrumbs">
					<c:import url="/WEB-INF/views/common/breadcrumbs.jsp">
						<c:param name="breadcrumbs">${breadcrumbs }</c:param>	
						<c:param name="breadcrumbLinks">${breadcrumbLinks }</c:param>	
					</c:import>			
				</div>
				<!-- END:breadcrumbs&search -->						
				
				<!-- BEGIN:page-content -->						
				<div class="page-content z1">
					<div class="rw-content-area-wrap">
						<div class="rw-content-area">
							<div class="rw-pagelet-wrap rw-mtl">
								<div class="row-fluid">
									<div class="span3 center">
										<span class="profile-picture">
											<img class="editable" alt="" id="basecamp-profilePic" src="${basecampUser.profilePic }" >
										</span>
										<div class="space space-4"></div>
										<h4 class="inline">
											<span  id="basecamp-name" data-name="name" data-class="name-${basecampUser.id }" class="${myBasecamp ? 'basecamp-editable' : ''} middle name-${basecampUser.id }">${basecampUser.name }</span>
										</h4>
										<div class="inline position-relative">
											<a href="#" id="basecamp-status" class="user-title-label dropdown-toggle" data-toggle="dropdown">
												<c:choose>
													<c:when test="${basecampUser.status == 0}">
														<span class="hmo-label hmo-label-green hmo-label-middle-10">근무중</span>
													</c:when>
													<c:when test="${basecampUser.status == 1}">
														<span class="hmo-label-orange hmo-label hmo-label-middle-10">휴가</span>
													</c:when>
													<c:when test="${basecampUser.status == 2}">
														<span class="hmo-label hmo-label-darkblue hmo-label-middle-10">퇴사</span>
													</c:when>
												</c:choose>
											</a>
	
											<c:if test="${ false && myBasecamp }">
												<ul class="align-left dropdown-menu dropdown-caret dropdown-lighter">
													<li class="nav-header center">근무여부 변경</li>
		
													<li class="center">
														<a href="#" class="change-status" data-id="0">
															<span class="hmo-label hmo-label-green hmo-label-middle-10">근무중</span>
														</a>
													</li>
		
													<li class="center">
														<a href="#" class="change-status" data-id="1">
															<span class="hmo-label-orange hmo-label hmo-label-middle-10">휴가</span>
														</a>
													</li>
		
													<li class="center" class="change-status" data-id="2">
														<a href="#" class="change-status" data-id="2">
															<span class="hmo-label hmo-label-darkblue hmo-label-middle-10">퇴사</span>
														</a>
													</li>
												</ul>
											</c:if>
										</div>
										
										<c:if test="${basecampUser.id != authUserId }">
											<a href="#" id="basecamp-message" class="basecamp-hmo-button hmo-button-white basecamp-hmo-button-request hmo-btn-block">
												
												메시지 보내기
											</a>
											
											<%--
												<a href="#" id="request-friend-${basecampUser.id }-t0" data-uid="${basecampUser.id }" data-type="0" class="request-friend basecamp-hmo-button hmo-button-blue basecamp-hmo-button-request hmo-btn-block ${basecampUser.relationFriendRequest != 0 ? 'hidden-elem' : ''}">
													
													친구요청
												</a>	
												<a href="#" id="request-friend-${basecampUser.id }-t1" data-uid="${basecampUser.id }" data-type="1" class="request-friend basecamp-hmo-button hmo-button-blue basecamp-hmo-button-request hmo-btn-block ${basecampUser.relationFriendRequest != 1 ? 'hidden-elem' : ''}">
												
													친구요청 취소
												</a>
												<a href="#" id="request-friend-${basecampUser.id }-t2" data-uid="${basecampUser.id }" data-type="2" class="request-friend basecamp-hmo-button hmo-button-blue basecamp-hmo-button-request hmo-btn-block ${basecampUser.relationFriendRequest != 2 ? 'hidden-elem' : ''}">
												
													친구요청 수락
												</a>
												<a href="#" id="request-friend-${basecampUser.id }-t3" data-uid="${basecampUser.id }" data-type="3" class="request-friend basecamp-hmo-button hmo-button-blue basecamp-hmo-button-request hmo-btn-block ${basecampUser.relationFriendRequest != 3 ? 'hidden-elem' : ''}">
													
													친구끊기
												</a>
											
											 --%>
										</c:if>	
										
									</div>
									<div class="span9">
										<div class="profile-user-info">
	
											<div class="profile-info-row">
												<div class="profile-info-name"> <span>직책</span> </div>
	
												<div class="profile-info-value">
													<c:if test="${not empty basecampUser.jobTitle1 }">
														<span>${basecampUser.jobTitle1 }</span>
													</c:if>
													<c:if test="${not empty basecampUser.jobTitle2 }">
														<span>${basecampUser.jobTitle2 }</span>
													</c:if>
													<c:if test="${not empty basecampUser.jobTitle3 }">
														<span>${basecampUser.jobTitle3 }</span>
													</c:if>
													<c:if test="${myBasecamp }">
														<a href="#" id="change-jobtitles" class="pull-right">변경</a>
													</c:if>
												</div>
											</div>
	
											<div class="profile-info-row">
												<div class="profile-info-name"> 부서 </div>
	
												<div class="profile-info-value">
													<c:forEach items="${basecampUser.departments }" var="department">
														<span>${department.name }</span>
													</c:forEach>
												</div>
											</div>
	
											<div class="profile-info-row">
												<div class="profile-info-name"> <span>이메일</span> </div>
	
												<div class="profile-info-value">
													<span data-name="email" data-type="email" class=" email-${basecampUser.id }" data-class="email-${basecampUser.id }" id="basecamp-email">${basecampUser.email }</span>
												</div>
											</div>
											<div class="profile-info-row">
												<div class="profile-info-name">  <span>전화번호(H.P)</span></div>
	
												<div class="profile-info-value">
													<span data-name="phone" class="${myBasecamp  ? 'basecamp-editable' : ''} phone-${basecampUser.id }" data-class="phone-${basecampUser.id }" id="basecamp-phone">${basecampUser.phone }</span>
												</div>
											</div>
	
											<div class="profile-info-row">
												<div class="profile-info-name"> 전화번호(회사)</div>
	
												<div class="profile-info-value">
													<span data-name="innercall" class="${myBasecamp  ? 'basecamp-editable' : ''} innercall-${basecampUser.id }" id="basecamp-innercall" data-class="innercall-${basecampUser.id }">${basecampUser.innercall }</span>
												</div>
											</div>
											
											<div class="profile-info-row">
												<div class="profile-info-name"> 상태 메시지 </div>
	
												<div class="profile-info-value">
													<span data-name="statusMessage" class="${myBasecamp  ? 'basecamp-editable' : ''} statusMessage-${basecampUser.id }" data-class="statusMessage-${basecampUser.id }" id="basecamp-statusMessage">${basecampUser.statusMessage }</span>
												</div>
											</div>
											
										</div>
									</div>
								</div>
							</div>
							
							<c:if test="${empty queries }">
								<div class="rw-pagelet-blank"></div>
								<div class="rw-pagelet-wrap">
																	
									<!-- BEGIN:story-writer -->
									<c:choose>
									
									<c:when test="${isAuthenticated && basecampUser.id == authUserId}">
										<c:import url="/WEB-INF/views/common/story-writer.jsp">
											<c:param name="placehoderMessage">지금,당신의 스토리를 들려주세요!</c:param>
										</c:import>
									</c:when>
									<c:otherwise>
										<c:import url="/WEB-INF/views/common/story-writer.jsp">
											<c:param name="placehoderMessage">${basecampUser.name }님에게 글 쓰기</c:param>
											<c:param name="receiverId">${basecampUser.id }</c:param>
										</c:import>
									
									</c:otherwise>
									</c:choose>
									
									<!-- END:story-writer -->
													
								</div>							
							</c:if>	
							<div class="rw-pagelet-blank"></div>
							<div class="rw-pagelet-wrap">
								<!-- BEGIN:composer -->
								<c:import url="/WEB-INF/views/common/story-stream.jsp">
									<c:param name="currentUrl">/user/${basecampUser.id }</c:param>
									<c:param name="streamUrl">/user/${basecampUser.id }</c:param>
									<c:param name="sizeFetchForward">10</c:param>
									<c:param name="sizeFetchBackward">10</c:param>
								</c:import>
								<!-- END:composer -->
							</div>
						</div>
					</div>
					<div class="rw-right-col">
						<div class="rw-pagelet-blank"></div>
						
						<!-- BEGIN:rightcol -->
						<c:import url="/WEB-INF/views/common/right-col.jsp">
						</c:import>
						<!-- END:rightcol -->
	
					</div>				
				</div>
			</div>	
		</div>
	</div>
</div>

<!-- BEGIN: dailog resource -->	
<div id="res-dialog-start-chat" style="display:none">
	<div class="rw-dialog-wrap rw-form-wrap">
		${basecampUser.name }님과 대화를 시작하겠습니까?<br>상대방과의 대화가 이미 존재하면 해당 채널로 이동되고 존재하지 않으면 새로 생성됩니다.
	</div>
</div>
<!-- END: dailog resource -->

<!-- BEGIN: dailog resource -->	
<div id="res-dialog-change-jobtitles" style="display:none">
	<div class="rw-dialog-wrap rw-form-wrap">
		<form class="form-vertical rw-form" id="form-change-jobtitles">
			<div class="control-group">
				<label class="control-label" for="job1">직책1</label>
				<div class="controls">
					<div class="row-fluid">
						<span class="span12 input-icon input-icon-right rw-input-wrap">
							<input class="span12" type="text" name="jobTitle1" id="job1" placeholder="직책1" value="${basecampUser.jobTitle1 }">
						</span>
					</div>
				</div>
			</div>				
			<div class="control-group">
				<label class="control-label" for="job2">직책2</label>
				<div class="controls">
					<div class="row-fluid">
						<span class="span12 input-icon input-icon-right rw-input-wrap">
							<input class="span12" type="text" name="jobTitle2" id="job2" placeholder="직책2" value="${basecampUser.jobTitle2 }">
						</span>
					</div>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="job3">직책3</label>
				<div class="controls">
					<div class="row-fluid">
						<span class="span12 input-icon input-icon-right rw-input-wrap">
							<input class="span12" type="text" name="jobTitle3" id="job3" placeholder="직책3" value="${basecampUser.jobTitle3 }">
						</span>
					</div>
				</div>
			</div>
		
		</form>
	</div>
</div>
<!-- END: dailog resource -->
<div id="res-dialog-pop-feeled-users" style="display: none">
	<div class="rw-dialog-wrap pop-feeled-users-scroll-wrap" id="res-dialog-pop-feeled-users-content"></div>
</div>
<div id="res-dialog-pop-approval-users" style="display: none">
	<div class="rw-dialog-wrap pop-approval-users-scroll-wrap" id="res-dialog-pop-approval-users-content"></div>
</div>
<form>
	<textarea tabindex="-1" id="ta-cmmnt-mirroring" class="textarea-mirroring cmmnt-mirroring"></textarea>
</form>
</body>
</html>