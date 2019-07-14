<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone=no">

<!-- BEGIN:basic bootstrap styles -->
<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/bootstrap.css" />
<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/bootstrap-responsive.css" />
<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/font-awesome.css" />
<!--[if IE 7]>
<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/font-awesome-ie7.min.css" />
<![endif]-->
<!-- END:basic bootstrap styles -->

<!-- fonts -->
<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/ace-fonts.css" />

<!-- BEGIN:ace styles -->
<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/uncompressed/ace.css" />
<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/uncompressed/ace-responsive.css" />
<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/uncompressed/ace-skins.css" />
<!--[if lte IE 8]>
<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/ace-ie.min.css" />
<![endif]-->
<!-- END:ace styles -->

<!-- BEGIN:sunny styles -->
<link rel="stylesheet" href="/assets/sunny/2.0/css/uncompressed/sunny.css" />
<!-- END:sunny styles -->

<!-- ace settings handler -->
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-extra.min.js"></script>

<!-- BEGIN:basic js scripts -->
	<!--[if !IE]> -->
	<script type="text/javascript">
		window.jQuery || document.write("<script src='/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/jquery-2.0.3.js'>"+"<"+"/script>");
	</script>
	<!-- <![endif]-->
	
	<!--[if IE]>
	<script type="text/javascript">
		window.jQuery || document.write("<script src='/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/jquery-1.10.2.js'>"+"<"+"/script>");
	</script>
	<![endif]-->
	
	<script type="text/javascript">
		if("ontouchend" in document) document.write("<script src='/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/jquery.mobile.custom.js'>"+"<"+"/script>");
	</script>
	
	<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/bootstrap.js"></script>
<!-- END:basic js scripts -->

<!-- BEGIN:ace scripts -->
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace-elements.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/ace.js"></script>
<!-- END:ace scripts -->

<!-- BEGIN:sunny scripts-->
<script src="/assets/sunny/2.0/js/uncompressed/sunny.js"></script>
<!-- END:sunny -->

</head>

<body class="scroll-y">
<div class="rw-snn">
	<div class="navbar" id="navbar">
		<div class="navbar-inner rw-fixed-element">
			<div class="container-fluid">
				<a href="#" class="brand">
					<small><i class="icon-sun"></i><span> 써니베일</span></small>
				</a>
				<ul class="nav ace-nav pull-right rw-nav">
					<li class="rw-mls">
						<a data-toggle="dropdown" href="#" class="dropdown-toggle">
							<img class="nav-user-photo" src="http://cdn.sunnyvale.co.kr/s2/53/45/1372779294553_1757_m.jpg" alt="안대혁" />
							<span class="user-info">가나다라마바사아자차카타파하</span>
						</a>
					</li>					
					<li>
						<a data-toggle="dropdown" class="dropdown-toggle" href="#">
							<i class="icon-group"></i>
							<span class="badge badge-important">4</span>
						</a>
					</li>
					<li>
						<a data-toggle="dropdown" class="dropdown-toggle" href="#">
							<i class="icon-bell-alt icon-animated-bell"></i>
							<span class="badge badge-important">8</span>
						</a>
						<ul class="pull-right dropdown-navbar dropdown-menu dropdown-caret dropdown-closer">
							<li class="nav-header">
								<i class="icon-envelope-alt"></i>
								<span>알림</span>
							</li>
							<li>
								<a href="#">
									<img src="http://cdn.sunnyvale.co.kr/s2/74/44/1381829574474_2659_m.jpg" class="msg-photo" alt="Alex's Avatar" />
									<span class="msg-body">
										<span class="msg-title"><strong>김성수</strong>님이 회원님의 스토리에 <strong>공감</strong> 했습니다.</span>
										<span class="msg-time"><i class="icon-time"></i> <span>방금전</span></span>
									</span>
								</a>
							</li>
							<li>
								<a href="#">
									<img src="http://cdn.sunnyvale.co.kr/s2/14/06/1381738410614_2656_m.jpg" class="msg-photo" alt="Bob's Avatar" />
									<span class="msg-body">
										<span class="msg-title"><strong>김종배</strong>님,<strong>김성박</strong>님이 회원님의 스토리에 댓글을 남겼습니다.</span>
										<span class="msg-time">
											<i class="icon-time"></i>
											<span>오후 3:15</span>
										</span>
									</span>
								</a>
							</li>
							<li>
								<a href="#">
									<img src="http://cdn.sunnyvale.co.kr/s2/53/45/1372779294553_1757_m.jpg" class="msg-photo" alt="Susan's Avatar" />
									<span class="msg-body">
										<span class="msg-title"><strong>이윤임</strong>님이 게시물에서 회원님을 언급했습니다.<span>:안녕하세요...</span></span>
										<span class="msg-time">
											<i class="icon-time"></i>
											<span>20 분전</span>
										</span>
									</span>
								</a>
							</li>							<li>
								<a href="#">
									<span>모두보기</span>
									<i class="icon-arrow-right"></i>
								</a>
							</li>
						</ul>						
					</li>
					<li>
						<a data-toggle="dropdown" class="dropdown-toggle" href="#">
							<i class="icon-comments icon-animated-vertical"></i>
							<span class="badge badge-success">5</span>
						</a>
						<ul class="pull-right dropdown-navbar dropdown-menu dropdown-caret dropdown-closer">
							<li class="nav-header">
								<i class="icon-envelope-alt"></i>
								<span>받은 메세지함</span>
							</li>
							<li>
								<a href="#">
									<img src="http://cdn.sunnyvale.co.kr/s2/12/28/1377864172812_2372_m.jpg" class="msg-photo" alt="Alex's Avatar" />
									<span class="msg-body">
										<span class="msg-title">
											<span class="blue">김성박</span>
											<span>배아퍼...머리아퍼...</span>
										</span>
										<span class="msg-time">
											<i class="icon-time"></i>
											<span>방금전</span>
										</span>
									</span>
								</a>
							</li>
							<li>
								<a href="#">
									<img src="http://cdn.sunnyvale.co.kr/s2/53/45/1372779294553_1757_m.jpg" class="msg-photo" alt="Susan's Avatar" />
									<span class="msg-body">
										<span class="msg-title">
											<span class="blue">안대혁</span>
											<span>가나다라마바사아자차카타파하가나다라마바사아자차카타파하</span>
										</span>
										<span class="msg-time">
											<i class="icon-time"></i>
											<span>20 분전</span>
										</span>
									</span>
								</a>
							</li>
							<li>
								<a href="#">
									<img src="http://cdn.sunnyvale.co.kr/s2/14/06/1381738410614_2656_m.jpg" class="msg-photo" alt="Bob's Avatar" />
									<span class="msg-body">
										<span class="msg-title">
											<span class="blue">김종배</span>
											<span>확산 마케팅. 그거 대박입니다.</span>
										</span>
										<span class="msg-time">
											<i class="icon-time"></i>
											<span>오후 3:15</span>
										</span>
									</span>
								</a>
							</li>
							<li>
								<a href="#">
									<span>모두보기</span>
									<i class="icon-arrow-right"></i>
								</a>
							</li>
						</ul>
					</li>
					<li class="rw-mls">
						<a data-toggle="dropdown" href="#" class="dropdown-toggle">
							<i class="icon-caret-down"></i>
						</a>
						<ul class="user-menu pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-closer">
							<li><a href="#"><i class="icon-cog"></i><span>설정</span></a></li>
							<li><a href="#"><i class="icon-user"></i><span>프로필 편집</span></a></li>
							<li class="divider"></li>
							<li><a href="#"><i class="icon-off"></i><span>로그아웃</span></a></li>
						</ul>
					</li>					
				</ul>
			</div>		
		</div>	
	</div>
	<!-- begin:MAIN CONTAINER -->
	<div class="main-container container-fluid">
		<!-- BEGIN:menu-toggler -->		
		<c:import url="/WEB-INF/views/common/menu-toggler.jsp">
		</c:import>
		<!-- END:menu-toggler -->
		<!-- BEGIN:LEFT SLIDEBAR -->		
		<div class="sidebar" id="snn-sidebar">
			<script type="text/javascript">
				try{ ace.settings.check('sidebar' ,'fixed') }catch(e){}
			</script>
			<div class="rw-sidebar-welcome-box" >
				<div class="clearfix rw-welcomebox">
					<a class="rw-welcomebox-block"href="#">
						<img class="rw-welcomebox-img" src="http://cdn.sunnyvale.co.kr/s2/53/45/1372779294553_1757_m.jpg" alt="" id="">
					</a>
					<div class="rw-welcomebox-contents-wrap">
						<div class="rw-welcomebox-contents">
							<a class="rw-welcomebox-name" href="">안대혁</a>
							<a href="">소셜컴퓨팅연구소</a><br>
							<a href="">프로필 편집</a>
						</div>
					</div>
				</div>
			</div>
			<div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
				<span class="btn hmo-button-khaki"></span>
				<span class="btn btn-info"></span>
				<span class="btn btn-warning"></span>
				<span class="btn btn-danger"></span>
			</div>
			<!-- begin:MAIN NAVIGATION LIST -->
			<ul class="nav nav-list">
				<li class="active">
					<a href="index.html">
						<i class="icon-group"></i>
						<span class="menu-text"> 모두의 소식 </span>
					</a>
				</li>
				<li>
					<a href="index.html">
						<i class="icon-user"></i>
						<span class="menu-text"> 관심 소식 </span>
					</a>
				</li>

				<li>
					<a href="index.html">
						<i class="icon-cloud"></i>
						<span class="menu-text"> 자료실 </span>
					</a>
				</li>

				<li>
					<a href="index.html">
						<i class="icon-cut"></i>
						<span class="menu-text"> 스크랩 </span>
					</a>
				</li>

				<li>
					<a href="index.html">
						<i class="icon-book"></i>
						<span class="menu-text"> 주소록 </span>
					</a>
				</li>

				<li>
					<a href="index.html">
						<i class="icon-tasks"></i>
						<span class="menu-text"> 작업 </span>
					</a>
				</li>

				<li>
					<a href="index.html">
						<i class="icon-folder-open"></i>
						<span class="menu-text"> 전자결제 </span>
					</a>
				</li>

				<li>
					<a href="index.html">
						<i class="icon-volume-up"></i>
						<span class="menu-text"> 공지사항 </span>
					</a>
				</li>
				
				<li>
					<a href="#" class="dropdown-toggle">
						<i class="icon-desktop"></i>
						<span class="menu-text">부서(팀)</span>
						<b class="arrow icon-angle-down"></b>
					</a>
					<ul class="submenu" style="display:block">
						<li>
							<a href="elements.html">
								<i class="icon-double-angle-right"></i>
								<span>소셜컴퓨터연구팀</span>
							</a>
						</li>
						<li>
							<a href="elements.html">
								<i class="icon-double-angle-right"></i>
								<span>시스템개발팀</span>
							</a>
						</li>
					</ul>										
				</li>

				<li>
					<a href="#" class="dropdown-toggle">
						<i class="icon-desktop"></i>
						<span class="menu-text">소그룹</span>
						<b class="arrow icon-angle-down"></b>
					</a>
					
					<ul class="submenu" style="display:block">
						<li>
							<a href="elements.html">
								<i class="icon-double-angle-right"></i>
								<span>주당클럽</span>
							</a>
						</li>
						<li>
							<a href="elements.html">
								<i class="icon-double-angle-right"></i>
								<span>골프클럽</span>
							</a>
						</li>
					</ul>										
				</li>				
		
				<li>
					<a href="index.html">
						<i class="icon-cut"></i>
						<span class="menu-text"> 관리자화면 </span>
					</a>
				</li>
				<li>
					<a href="index.html">
						<i class="icon-book"></i>
						<span class="menu-text"> 메뉴얼 </span>
					</a>
				</li>				
			</ul>	
		</div>
			
		<div class="main-content">
			<!-- BREADCRUMS -->						
			<div class="breadcrumbs" id="breadcrumbs">
				<script type="text/javascript">
					//try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
				</script>
				<ul class="breadcrumb">
					<li>
						<i class="icon-home home-icon"></i>
						<a href="#">홈</a>
						<span class="divider">
							<i class="icon-angle-right arrow-icon"></i>
						</span>
					</li>
					<li class="active">
						<a href="#">모두의 소식</a>
					</li>
				</ul>
				<div class="nav-search" id="nav-search">
					<form class="form-search">
						<span class="input-icon">
							<input type="text" placeholder="검색 ..." class="input-small nav-search-input" id="nav-search-input" autocomplete="off">
							<i class="icon-search nav-search-icon"></i>
						</span>
					</form>
				</div>
			</div>
			
			<!-- PAGE CONTENT -->						
			<div class="page-content">
				<div class="rw-content-area-wrap">
					<div class="rw-content-area">

						<div class="rw-pagelet-blank"></div>

								<div class="rw-pagelet-wrap">
								
									<div class="ui-composer-out">
									
									
										<div class="ui-mentions-input _11a">
											<div style="direction:ltr; text-align:left;" class="highlighter">
												<div><span class="highlighter-content hidden-elem mentions-highlighter"></span></div>
											</div>
											<div style="height:auto;" class="ui-type-head composer-type-ahead mentions-type-ahead">
												<div class="wrap">
													<div class="inner-wrap">
														<textarea  
															class="ui-textarea-auto-grow-uz input mentions-textarea text-input dom-control-placeholder"
															title=""
															placeholder=""
															role="textbox"
															aria-autocomplete="list"
															autocomplete="off"
															spellcheck="false"
															aria-expanded="false"
															aria-label=""
															onfocus="on_ta_focus(this,true);"
															onblur="on_ta_focus(this);"
															id="ta-message-text"></textarea>
														<span class="placeholder" onclick="document.getElementById(&quot;ta-message-text&quot;).focus();"></span>
													</div>
												</div>
											</div>
										</div>
										
										<div class="ui-composer-bottom">
											<div class="l-ft">
												<div class="l-ft composer-tool-button" id="composer-tool-button-photo">
													<a rel="ignore" role="button" class="composer-tool-button-photo">
														<div class="composer-tool-button-inner">
															<form enctype="multipart/form-data"
																action=""
																target="if-file-upload" method="post">
																<input multiple="" type="file" name="file" onchange="storyPicUploader &amp;&amp; storyPicUploader.onFileSelected(this);" accept="image/*">
																<input type="hidden" name="userId" value="">
																<input type="hidden" name="domain" value="">
															</form>
														</div>
													</a>
												</div>
												<!-- 
												<div class="l-ft composer-tool-button">
													<a href="" class="composer-tool-button-social"><span>SNS연동</span></a>
												</div>
												-->
											</div>
											<ul class="ui-list r-ft">
												<li>
													<div class="cls-fix mt4 mrm ui-input-submit">
														<input type="submit" class="btn btn-info" value="등록" onclick="$(window[&quot;story-w-form&quot;]).trigger(&quot;submit&quot;);">
													</div>
												</li>			
											</ul>										
										</div>
									</div>
									
										
								</div>								
								
								<div class="rw-pagelet-blank"></div>
								
								<div class="rw-pagelet-wrap rw-mtl">
									<div class="rw-pagelet-stream">
										<div class="rw-ui-stream-wrap">



											<ul id="story-stream-list" 
												class="rw-ui-stream-large-headline rw-ui-stream rw-timeline-stream" 
												data-fw-map="{'top':true,'contentId':'11491','size':'10'}" 
												data-bw-map="{'top':false,'contentId':'11461','size':'10'}" 
												data-stream-url="/newsfeed/stream" 
												data-async="async-get" role="list" >
												
												
												
												
												<li class="timeline-container">
													<div class="timeline-label">

														<span class="label label-primary arrowed-in-right label-large">
															<strong>2013.11.28 (오늘)</strong>
														</span>
													</div>
												</li>
			
			
												<li id="story-box-11491" class="timeline-container rw-ui-stream-story" data-map="{'id':'11491'}">
																							
													<div class="timeline-items">
													
														<div class="timeline-item clearfix">
															<div class="timeline-info">
																<img alt="김성박" src="http://cdn.sunnyvale.co.kr/s2/12/28/1377864172812_2372_m.jpg" />
																<span class="label label-info">16:22</span>
															</div>
															<div class="widget-box transparent">
																<div class="widget-header widget-header-small">
																	<h5 class="smaller">
																		<a href="#" class="blue">김성박</a>
																		<span class="grey"></span>
																	</h5>
																	<span class="widget-toolbar no-border">
																		<i class="icon-time bigger-110"></i>
																		16:22
																	</span>
																	<span class="widget-toolbar">
																		<a href="#" data-action="reload">
																			<i class="icon-refresh"></i>
																		</a>
		
																		<a href="#" data-action="collapse">
																			<i class="icon-chevron-up"></i>
																		</a>
																	</span>
																</div>
																<div class="widget-body">
																	<div class="widget-main">
																		<h5 class="rw-ui-stream-message">
																			<span class="rw-message-body">
																				<span id="message-exposed-11491" class="rw-message-body-wrap">
																					현재 ACE의 경우 안드로이드 폰으로 접속하니 기본으로 모바일에 맞게 보여지네요. 
																					
																					<br>
																					
																					노트3인데도.... 그런데.. 달력에서 드래그에서 날짜에 이벤트 놓은 이후에...... 
																					
																					<br>
																					
																					pc처럼 드래그에서 길어지지 않음. 모바일은 마우스처럼 드래그가 안됨.
																					
																					<br>
																					<br>
																					
																					모바일의 경우에는 터치하여 연다음에 기간을 정할 수 있어야 하네요.
																					
																				</span>	
																			</span>
																		</h5>
																		<div class="widget-toolbox clearfix">
																			<div class="pull-left">
																				<i class="icon-hand-right grey bigger-125"></i>
																				<a href="#" class="bigger-110">더보기  &hellip;</a>
																			</div>
																			<div class="pull-right action-buttons">
																				<a href="#">
																					<i class="icon-ok green bigger-130"></i>
																				</a>
																				<a href="#">
																					<i class="icon-pencil blue bigger-125"></i>
																				</a>
																				<a href="#">
																					<i class="icon-remove red bigger-125"></i>
																				</a>
																			</div>
																		</div>
																	</div>
																</div>																
															</div>
														</div>
														
														<div class="timeline-item clearfix">
															<div class="widget-box transparent">
																<div class="widget-body">
																	<div class="widget-main">
																		여기는 좋아요 아이콘들
																		<div class="pull-right">
																			<i class="icon-time bigger-110"></i>
																			192.168.0.200
																		</div>
																	</div>
																</div>
															</div>
														</div>
														
													
														<div class="timeline-item clearfix">
															<div class="timeline-info">
																<i class="timeline-indicator icon-star btn btn-warning no-hover green"></i>
															</div>
		
															<div class="widget-box transparent">
																<div class="widget-header widget-header-small">
																	<h5 class="smaller">파일</h5>
		
																	<span class="widget-toolbar no-border">
																		<i class="icon-time bigger-110"></i>
																		9:15
																	</span>
		
																	<span class="widget-toolbar">
																		<a href="#" data-action="reload">
																			<i class="icon-refresh"></i>
																		</a>
		
																		<a href="#" data-action="collapse">
																			<i class="icon-chevron-up"></i>
																		</a>
																	</span>
																</div>
		
																<div class="widget-body">
																	<div class="widget-main">
																		여기는 파일 박스
																		<div class="space-6"></div>
		
																		<div class="widget-toolbox clearfix">
																			<div class="pull-right action-buttons">
																				<div class="space-4"></div>
		
																				<div>
																					<a href="#">
																						<i class="icon-heart red bigger-125"></i>
																					</a>
		
																					<a href="#">
																						<i class="icon-facebook blue bigger-125"></i>
																					</a>
		
																					<a href="#">
																						<i class="icon-reply light-green bigger-130"></i>
																					</a>
																				</div>
																			</div>
																		</div>
																	</div>
																</div>
															</div>
														</div>
													</div>
												</li>
												
												
												<li id="story-box-11491" class="timeline-container rw-ui-stream-story"	data-map="{'id':'11491'}">
													
													<div class="timeline-items">
													
														<div class="timeline-item clearfix">
															<div class="timeline-info">
																<img alt="이윤임" src="http://cdn.sunnyvale.co.kr/s2/53/45/1372779294553_1757_m.jpg" />
																<span class="label label-info">16:22</span>
															</div>
															<div class="widget-box transparent">
																<div class="widget-header widget-header-small">
																	<h5 class="smaller">
																		<a href="#" class="blue">이윤임</a>
																		<span class="grey"></span>
																	</h5>
																	<span class="widget-toolbar no-border">
																		<i class="icon-time bigger-110"></i>
																		16:22
																	</span>
																	<span class="widget-toolbar">
																		<a href="#" data-action="reload">
																			<i class="icon-refresh"></i>
																		</a>
		
																		<a href="#" data-action="collapse">
																			<i class="icon-chevron-up"></i>
																		</a>
																	</span>
																</div>
																<div class="widget-body">
																	<div class="widget-main">
																		<h5 class="rw-ui-stream-message">
																			<span class="rw-message-body">
																				<span id="message-exposed-11491" class="rw-message-body-wrap">
																					1.&nbsp;Chat&nbsp;Notification&nbsp;메세지&nbsp;가져오는&nbsp;프로토콜&nbsp;필요.
																					<br>
																					&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;노티만&nbsp;주고&nbsp;메세지는&nbsp;api로&nbsp;가져오는&nbsp;것은&nbsp;맞으나.
																					<br>
																				</span>	
																			</span>
																		</h5>
																		<div class="widget-toolbox clearfix">
																			<div class="pull-right action-buttons">
																				<a href="#">
																					<i class="icon-ok green bigger-130"></i>
																				</a>
																				<a href="#">
																					<i class="icon-pencil blue bigger-125"></i>
																				</a>
																				<a href="#">
																					<i class="icon-remove red bigger-125"></i>
																				</a>
																			</div>
																		</div>
																	</div>
																</div>																
															</div>
														</div>
														
														<div class="timeline-item clearfix">
															<div class="widget-box transparent">
																<div class="widget-body">
																	<div class="widget-main">
																		여기는 좋아용 아이콘들
																		<div class="pull-right">
																			<i class="icon-time bigger-110"></i>
																			192.168.0.100
																		</div>
																	</div>
																</div>
															</div>
														</div>
													</div>
												</li>
												
												
												<li id="story-box-11491" class="timeline-container rw-ui-stream-story" data-map="{'id':'11491'}">
																							
													<div class="timeline-items">
													
														<div class="timeline-item clearfix">
															<div class="timeline-info">
																<img alt="김종배" src="http://cdn.sunnyvale.co.kr/s2/14/06/1381738410614_2656_m.jpg" />
																<span class="label label-info">16:22</span>
															</div>
															<div class="widget-box transparent">
																<div class="widget-header widget-header-small">
																	<h5 class="smaller">
																		<a href="#" class="blue">김종배</a>
																		<span class="grey"></span>
																	</h5>
																	<span class="widget-toolbar no-border">
																		<i class="icon-time bigger-110"></i>
																		16:22
																	</span>
																	<span class="widget-toolbar">
																		<a href="#" data-action="reload">
																			<i class="icon-refresh"></i>
																		</a>
		
																		<a href="#" data-action="collapse">
																			<i class="icon-chevron-up"></i>
																		</a>
																	</span>
																</div>
																<div class="widget-body">
																	<div class="widget-main">
																		<h5 class="rw-ui-stream-message">
																			<span class="rw-message-body">
																				<span id="message-exposed-11491" class="rw-message-body-wrap">
																					<br>1.&nbsp;Chat&nbsp;Notification&nbsp;메세지&nbsp;가져오는&nbsp;프로토콜&nbsp;필요.
																					<br>
																					<br>&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;노티만&nbsp;주고&nbsp;메세지는&nbsp;api로&nbsp;가져오는&nbsp;것은&nbsp;맞으나.
																					<br>
																					<br>&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;방에&nbsp;있다는&nbsp;가정에서&nbsp;가져오는&nbsp;프로토콜이다&nbsp;보니,&nbsp;앱에서&nbsp;맞지&nbsp;않음.
																					<br>
																					<br>&nbsp;&nbsp;&nbsp;&nbsp;-&nbsp;예를&nbsp;들면,&nbsp;앱을&nbsp;끄고&nbsp;노티발생시&nbsp;기준이&nbsp;되는&nbsp;메세지&nbsp;아이디를&nbsp;전달해&nbsp;줄수&nbsp;없어서
																					<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;채널의&nbsp;30개&nbsp;메세지를&nbsp;가져와서&nbsp;마지막&nbsp;것으로&nbsp;노티&nbsp;메세지&nbsp;매칭하는&nbsp;방식임
																					<br>
																				</span>	
																			</span>
																		</h5>
																		<div class="widget-toolbox clearfix">
																			<div class="pull-left">
																				<i class="icon-hand-right grey bigger-125"></i>
																				<a href="#" class="bigger-110">더보기  &hellip;</a>
																			</div>
																			<div class="pull-right action-buttons">
																				<a href="#">
																					<i class="icon-ok green bigger-130"></i>
																				</a>
																				<a href="#">
																					<i class="icon-pencil blue bigger-125"></i>
																				</a>
																				<a href="#">
																					<i class="icon-remove red bigger-125"></i>
																				</a>
																			</div>
																		</div>
																	</div>
																</div>																
															</div>
														</div>
														
														<div class="timeline-item clearfix">
															<div class="widget-box transparent">
																<div class="widget-body">
																	<div class="widget-main">
																		여기는 좋아요 아이콘들
																		<div class="pull-right">
																			<i class="icon-time bigger-110"></i>
																			192.168.0.200
																		</div>
																	</div>
																</div>
															</div>
														</div>
														
													
														<div class="timeline-item clearfix">
															<div class="timeline-info">
																<i class="timeline-indicator icon-food btn btn-warning no-hover green"></i>
															</div>
		
															<div class="widget-box transparent">
																<div class="widget-header widget-header-small">
																	<h5 class="smaller">사진</h5>
		
																	<span class="widget-toolbar no-border">
																		<i class="icon-time bigger-110"></i>
																		9:15
																	</span>
		
																	<span class="widget-toolbar">
																		<a href="#" data-action="reload">
																			<i class="icon-refresh"></i>
																		</a>
		
																		<a href="#" data-action="collapse">
																			<i class="icon-chevron-up"></i>
																		</a>
																	</span>
																</div>
		
																<div class="widget-body">
																	<div class="widget-main">
																		여기는 사진 박스
																		<div class="space-6"></div>
		
																		<div class="widget-toolbox clearfix">
																			<div class="pull-right action-buttons">
																				<div class="space-4"></div>
		
																				<div>
																					<a href="#">
																						<i class="icon-heart red bigger-125"></i>
																					</a>
		
																					<a href="#">
																						<i class="icon-facebook blue bigger-125"></i>
																					</a>
		
																					<a href="#">
																						<i class="icon-reply light-green bigger-130"></i>
																					</a>
																				</div>
																			</div>
																		</div>
																	</div>
																</div>
															</div>
														</div>
													</div>
												</li>												
												
												
												
												<li id="story-box-11491" class="timeline-container rw-ui-stream-story" data-map="{'id':'11491'}">
																							
													<div class="timeline-items">
													
														<div class="timeline-item clearfix">
															<div class="timeline-info">
																<img alt="안대혁" src="http://cdn.sunnyvale.co.kr/s1/80/66/1385687626680_3016_m.jpg" />
																<span class="label label-info">16:22</span>
															</div>
															<div class="widget-box transparent">
																<div class="widget-header widget-header-small">
																	<h5 class="smaller">
																		<a href="#" class="blue">안대혁</a>
																		<span class="grey"></span>
																	</h5>
																	<span class="widget-toolbar no-border">
																		<i class="icon-time bigger-110"></i>
																		16:22
																	</span>
																	<span class="widget-toolbar">
																		<a href="#" data-action="reload">
																			<i class="icon-refresh"></i>
																		</a>
		
																		<a href="#" data-action="collapse">
																			<i class="icon-chevron-up"></i>
																		</a>
																	</span>
																</div>
																<div class="widget-body">
																	<div class="widget-main">
																		<h5 class="rw-ui-stream-message">
																			<span class="rw-message-body">
																				<span id="message-exposed-11491" class="rw-message-body-wrap">
																					<br>
																					<br>어러 났어요!!!!!!
																				</span>	
																			</span>
																		</h5>
																		<div class="widget-toolbox clearfix">
																			<div class="pull-left">
																				<i class="icon-hand-right grey bigger-125"></i>
																				<a href="#" class="bigger-110">더보기  &hellip;</a>
																			</div>
																			<div class="pull-right action-buttons">
																				<a href="#">
																					<i class="icon-ok green bigger-130"></i>
																				</a>
																				<a href="#">
																					<i class="icon-pencil blue bigger-125"></i>
																				</a>
																				<a href="#">
																					<i class="icon-remove red bigger-125"></i>
																				</a>
																			</div>
																		</div>
																	</div>
																</div>																
															</div>
														</div>
														
														<div class="timeline-item clearfix">
															<div class="widget-box transparent">
																<div class="widget-body">
																	<div class="widget-main">
																		여기는 좋아요 아이콘들
																		<div class="pull-right">
																			<i class="icon-time bigger-110"></i>
																			192.168.0.200
																		</div>
																	</div>
																</div>
															</div>
														</div>
														
													
														<div class="timeline-item clearfix">
															<div class="timeline-info">
																<i class="timeline-indicator icon-bug btn btn-danger no-hover red"></i>
															</div>
		
															<div class="widget-box transparent">
																<div class="widget-header header-color-red2 widget-header-small">
																	<h5 class="smaller">버그 리포팅</h5>
		
																	<span class="widget-toolbar no-border">
																		<i class="icon-time bigger-110"></i>
																		9:15
																	</span>
		
																	<span class="widget-toolbar">
																		<a href="#" data-action="reload">
																			<i class="icon-refresh"></i>
																		</a>
		
																		<a href="#" data-action="collapse">
																			<i class="icon-chevron-up"></i>
																		</a>
																	</span>
																</div>
		
																<div class="widget-body">
																	<div class="widget-main">
																		여기는 사진 박스
																		<div class="space-6"></div>
		
																		<div class="widget-toolbox clearfix">
																			<div class="pull-right action-buttons">
																				<div class="space-4"></div>
		
																				<div>
																					<a href="#">
																						<i class="icon-heart red bigger-125"></i>
																					</a>
		
																					<a href="#">
																						<i class="icon-facebook blue bigger-125"></i>
																					</a>
		
																					<a href="#">
																						<i class="icon-reply light-green bigger-130"></i>
																					</a>
																				</div>
																			</div>
																		</div>
																	</div>
																</div>
															</div>
														</div>
													</div>
												</li>												
												
												
												
												
												
											</ul>





										</div>
									</div>
								</div>


						
						
						
					</div>
				</div>
				
				
				<div class="rw-right-col">
					<div class="rw-pagelet-blank"></div>
					<div class="widget-box transparent">
						<div class="widget-header widget-header-small  header-color-grey">
							<h4 class="blue smaller">
								<i class="icon-volume-up grey"></i>
								<span class="rw-title">공지사항</span>
							</h4>
							<div class="widget-toolbar action-buttons">
								<a href="#" data-action="reload">
									<i class="icon-refresh blue"></i>
								</a>
								&nbsp;
								<a href="#" class="pink">
									<i class="icon-trash"></i>
								</a>
							</div>
						</div>
						
						<div class="widget-body">
								<div class="widget-main padding-8">
													<div class="slimScrollDiv" style="position: relative; overflow: hidden; width: auto; height: 250px;"><div id="profile-feed-1" class="profile-feed" style="overflow: hidden; width: auto; height: 250px;">
														<div class="profile-activity clearfix">
															<div>
																<img class="pull-left" alt="Alex Doe's avatar" src="http://cdn.sunnyvale.co.kr/s2/12/28/1377864172812_2372_m.jpg">
																<a class="user" href="#"> Alex Doe </a>
																changed his profile photo.
																<a href="#">Take a look</a>

																<div class="time">
																	<i class="icon-time bigger-110"></i>
																	an hour ago
																</div>
															</div>

															<div class="tools action-buttons">
																<a href="#" class="blue">
																	<i class="icon-pencil bigger-125"></i>
																</a>

																<a href="#" class="red">
																	<i class="icon-remove bigger-125"></i>
																</a>
															</div>
														</div>

														<div class="profile-activity clearfix">
															<div>
																<img class="pull-left" alt="Susan Smith's avatar" src="http://cdn.sunnyvale.co.kr/s2/12/28/1377864172812_2372_m.jpg">
																<a class="user" href="#"> Susan Smith </a>

																is now friends with Alex Doe.
																<div class="time">
																	<i class="icon-time bigger-110"></i>
																	2 hours ago
																</div>
															</div>

															<div class="tools action-buttons">
																<a href="#" class="blue">
																	<i class="icon-pencil bigger-125"></i>
																</a>

																<a href="#" class="red">
																	<i class="icon-remove bigger-125"></i>
																</a>
															</div>
														</div>

														<div class="profile-activity clearfix">
															<div>
																<i class="pull-left thumbicon icon-ok hmo-button-khaki no-hover"></i>
																<a class="user" href="#"> Alex Doe </a>
																joined
																<a href="#">Country Music</a>

																group.
																<div class="time">
																	<i class="icon-time bigger-110"></i>
																	5 hours ago
																</div>
															</div>

															<div class="tools action-buttons">
																<a href="#" class="blue">
																	<i class="icon-pencil bigger-125"></i>
																</a>

																<a href="#" class="red">
																	<i class="icon-remove bigger-125"></i>
																</a>
															</div>
														</div>

														<div class="profile-activity clearfix">
															<div>
																<i class="pull-left thumbicon icon-picture btn-info no-hover"></i>
																<a class="user" href="#"> Alex Doe </a>
																uploaded a new photo.
																<a href="#">Take a look</a>

																<div class="time">
																	<i class="icon-time bigger-110"></i>
																	5 hours ago
																</div>
															</div>

															<div class="tools action-buttons">
																<a href="#" class="blue">
																	<i class="icon-pencil bigger-125"></i>
																</a>

																<a href="#" class="red">
																	<i class="icon-remove bigger-125"></i>
																</a>
															</div>
														</div>

														<div class="profile-activity clearfix">
															<div>
																<img class="pull-left" alt="David Palms's avatar" src="assets/avatars/avatar4.png">
																<a class="user" href="#"> David Palms </a>

																left a comment on Alex's wall.
																<div class="time">
																	<i class="icon-time bigger-110"></i>
																	8 hours ago
																</div>
															</div>

															<div class="tools action-buttons">
																<a href="#" class="blue">
																	<i class="icon-pencil bigger-125"></i>
																</a>

																<a href="#" class="red">
																	<i class="icon-remove bigger-125"></i>
																</a>
															</div>
														</div>

														<div class="profile-activity clearfix">
															<div>
																<i class="pull-left thumbicon icon-edit btn-pink no-hover"></i>
																<a class="user" href="#"> Alex Doe </a>
																published a new blog post.
																<a href="#">Read now</a>

																<div class="time">
																	<i class="icon-time bigger-110"></i>
																	11 hours ago
																</div>
															</div>

															<div class="tools action-buttons">
																<a href="#" class="blue">
																	<i class="icon-pencil bigger-125"></i>
																</a>

																<a href="#" class="red">
																	<i class="icon-remove bigger-125"></i>
																</a>
															</div>
														</div>

														<div class="profile-activity clearfix">
															<div>
																<img class="pull-left" alt="Alex Doe's avatar" src="assets/avatars/avatar5.png">
																<a class="user" href="#"> Alex Doe </a>

																upgraded his skills.
																<div class="time">
																	<i class="icon-time bigger-110"></i>
																	12 hours ago
																</div>
															</div>

															<div class="tools action-buttons">
																<a href="#" class="blue">
																	<i class="icon-pencil bigger-125"></i>
																</a>

																<a href="#" class="red">
																	<i class="icon-remove bigger-125"></i>
																</a>
															</div>
														</div>

														<div class="profile-activity clearfix">
															<div>
																<i class="pull-left thumbicon icon-key btn-info no-hover"></i>
																<a class="user" href="#"> Alex Doe </a>

																logged in.
																<div class="time">
																	<i class="icon-time bigger-110"></i>
																	12 hours ago
																</div>
															</div>

															<div class="tools action-buttons">
																<a href="#" class="blue">
																	<i class="icon-pencil bigger-125"></i>
																</a>

																<a href="#" class="red">
																	<i class="icon-remove bigger-125"></i>
																</a>
															</div>
														</div>

														<div class="profile-activity clearfix">
															<div>
																<i class="pull-left thumbicon icon-off btn-inverse no-hover"></i>
																<a class="user" href="#"> Alex Doe </a>

																logged out.
																<div class="time">
																	<i class="icon-time bigger-110"></i>
																	16 hours ago
																</div>
															</div>

															<div class="tools action-buttons">
																<a href="#" class="blue">
																	<i class="icon-pencil bigger-125"></i>
																</a>

																<a href="#" class="red">
																	<i class="icon-remove bigger-125"></i>
																</a>
															</div>
														</div>

														<div class="profile-activity clearfix">
															<div>
																<i class="pull-left thumbicon icon-key btn-info no-hover"></i>
																<a class="user" href="#"> Alex Doe </a>

																logged in.
																<div class="time">
																	<i class="icon-time bigger-110"></i>
																	16 hours ago
																</div>
															</div>

															<div class="tools action-buttons">
																<a href="#" class="blue">
																	<i class="icon-pencil bigger-125"></i>
																</a>

																<a href="#" class="red">
																	<i class="icon-remove bigger-125"></i>
																</a>
															</div>
														</div>
													</div><div class="slimScrollBar ui-draggable" style="background-color: rgb(0, 0, 0); width: 7px; position: absolute; top: 0px; opacity: 0.4; display: block; border-top-left-radius: 7px; border-top-right-radius: 7px; border-bottom-right-radius: 7px; border-bottom-left-radius: 7px; z-index: 99; right: 1px; height: 96.00614439324116px; background-position: initial initial; background-repeat: initial initial;"></div><div class="slimScrollRail" style="width: 7px; height: 100%; position: absolute; top: 0px; display: none; border-top-left-radius: 7px; border-top-right-radius: 7px; border-bottom-right-radius: 7px; border-bottom-left-radius: 7px; background-color: rgb(51, 51, 51); opacity: 0.2; z-index: 90; right: 1px; background-position: initial initial; background-repeat: initial initial;"></div></div>
												</div>
											</div>
										</div>



				</div>				
				
				
			</div>
		</div>	
	</div>
</div>	
</body>
</html>