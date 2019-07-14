<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">

<head>

<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">사이트</c:param>
	<c:param name="bsUsed">YES</c:param>
	<c:param name="aceUsed">NO</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>


<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/select2.css" />
<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/bootstrap-editable.css" />
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/bootstrap.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/select2.min.js"></script>
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/x-editable/bootstrap-editable.min.js"></script>

<script>
$(function(){
	$.fn.editable.defaults.mode = 'inline';
	$.fn.editableform.loading = "<div class='editableform-loading'><i class='light-blue icon-2x icon-spinner icon-spin'></i></div>";
    $.fn.editableform.buttons = '<button type="submit" class="btn btn-info editable-submit"><i class="fa fa-check fa-1g icon-white"></i></button>'+
                                '<button type="button" class="btn editable-cancel"><i class="fa fa-times fa-1g"></i></button>';    
    $.fn.editable.defaults.url = '/a/site';                     
    $.fn.editable.defaults.ajaxOptions =  {
			type : 'POST',
			dataType : 'json',
			headers : {
				'Accept' : 'application/json',
				'Content-Type' : 'application/json'
			}
	}
    $.fn.editable.defaults.params = function(params) {
		
		var data = {};
		
		data[params.name] = params.value;
		params = JSON.stringify(data);
		return params;
	}
    
    
    var countries = [];
    $.each({"1": "1일", "2": "2일", "3": "3일", "5": "5일", "10": "10일", "20": "20일", "30": "한달", "100000": "무기한"}, function(k, v) {
        countries.push({id: k, text: v});
    }); 
    $('#noticeDuration').editable({
        source: countries,
        select2: {
            width: 200,
            placeholder: '기간을 정하세요',
            allowClear: true
        } 
    });      
	$('.editable').editable({
		send : 'always',
		success : function(data, newValue) {
			if( data.result == "fail" ){
				return data.message;
			}
		}
	});

                                
});


function apply_checkbox(_this){
	var data = {};
	var $this = $(_this);
	var isChecked = $this.is(":checked"); 
	if( isChecked ){
		data[$this.data("name")] = $this.data("checked");
		if( $this.data("name") == "privacy" ){
			$("#site-introduce-wrap").removeClass("hidden-elem");
		}
	}else{
		data[$this.data("name")] = $this.data("unchecked");
		if( $this.data("name") == "privacy" ){
			$("#site-introduce-wrap").addClass("hidden-elem");
		}
	}
	params = JSON.stringify(data);
	
	$.ajax({
		url:"/a/site",
		type : 'POST',
		dataType : 'json',
		headers : {
			'Accept' : 'application/json',
			'Content-Type' : 'application/json'
		},
		data:params,
		success:function(data){
			if( data.result == "fail" ){
				alert(data.message);
				return false; 
			}
				
		}
	})
}

</script>
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
		<div class="main-container container-fluid">
	
			<!-- BEGIN:sidebar -->		
			<div class="sidebar" id="snn-sidebar">
			
				<!-- BEGIN:welcome-box -->		
				<c:import url="/WEB-INF/views/common/welcome-box.jsp">
				</c:import>
				<!-- END:welcome-box -->		
	
				<!-- BEGIN:nav-list -->
				<c:import url="/WEB-INF/views/common/nav-list-a.jsp">
					<c:param name="menuName">사이트관리</c:param>
				</c:import>			
				<!-- END:nav-list -->
		
			</div>
			<!-- END:sidebar -->		
	
			<!-- BEGIN:main-content -->			
			<div class="main-content">
			
				<!-- BEGIN:breadcrumbs&search -->	
				<c:set var="breadcrumbs" value="관리자콘솔,사이트관리" />
				<c:set var="breadcrumbLinks" value="/a,/a/site" />
				<div class="breadcrumbs" id="breadcrumbs">
					<c:import url="/WEB-INF/views/common/breadcrumbs.jsp">
						<c:param name="breadcrumbs">${breadcrumbs }</c:param>	
						<c:param name="breadcrumbLinks">${breadcrumbLinks }</c:param>	
					</c:import>			
				</div>
				<!-- END:breadcrumbs&search -->	
				
				<!-- BEGIN:page-content -->						
				<div class="page-content">
					<div class="rw-content-area-wrap admin-content-area-wrap">
						<div class="rw-content-fs site-admin-manage-conte-area">
							<div class="rw-pagelet-blank"></div>
							<div class="rw-pagelet-wrap rw-mtl site-admin-manage-wrap">
								<div class="row-fluid">
									<div class="span12">
										<form class="form-horizontal">
											<div class="control-group control-group-border-tp">
												<label class="control-label site-admin-manage-title site-admin-manage-title-first">사이트 이름</label>
												<div class="controls">
													<span class="editable" data-name="companyName" >${site.companyName }</span>
												</div>
											</div>
											
											<div class="control-group">
												<label class="control-label site-admin-manage-title site-admin-manage-title-first" >홈페이지</label>
												<div class="controls">
													<span class="editable" data-name="homepage" >${site.homepage }</span>
												</div>
											</div>
											
											<div class="control-group">
												<label class="control-label site-admin-manage-title site-admin-manage-title-first" >회사 전화번호</label>
												<div class="controls">
													<span class="editable" data-name="companyPhone" >${site.companyPhone }</span>
												</div>
											</div>
											
											<%--
											<div class="control-group">
												<label class="control-label">자료실 허용 IP : <br /> 앞부분만 입력합니다. 예(192.168.0)</label>
												<div class="controls">
													<span class="editable" data-name="accessIpPds" >${site.accessIpPds }</span>
												</div>
											</div>
											 --%>
											<%--
											<div class="control-group">
												<label class="control-label" >공지사항 메인 노출 기간 : </label>
												<div class="controls">
													<span id="noticeDuration" class="editable editable-click" data-type="select2" data-pk="${site.noticeDuration }" data-value="${site.noticeDuration }" data-title="공지 기간을 정하세요" data-name="noticeDuration" ></span>
												</div>
											</div>
											 --%>
											<div class="control-group">
												<label class="control-label site-admin-manage-title site-admin-manage-title-first">공개여부</label>
												<div class="controls admin-manage-controls">
													<label class="checkbox">
												      <input type="checkbox" ${site.privacy == 1 ? 'checked' : '' } data-name="privacy" data-checked="1" data-unchecked="2" onclick="return apply_checkbox(this);"> 리스트에 공개합니다.
												    </label>
												    <div>												    	 
												    </div>
												</div>
											</div>
											<div id="site-introduce-wrap" class="control-group ${site.privacy == 1 ? '' : 'hidden-elem'} " >
												<label class="control-label site-admin-manage-title site-admin-manage-title-first" >사이트 소개글</label>
												<div class="controls">
													<span data-name="companyIntroduce" data-value="${site.companyIntroduce }" data-type="textarea" data-pk="1" data-placeholder="사이트 소개를 넣어주세요." data-title="사이트 소개" class="editable editable-pre-wrapped editable-click mb8"></span>
													<span class="admin-manage-help-text">공개시 사이트 리스트에 함께 노출됩니다.</span>
												</div>
											</div>
											<div class="control-group">
												<label class="control-label site-admin-manage-title site-admin-manage-title-first" for="form-field-1">하나의 파일 최대용량</label>
												<div class="controls admin-manage-controls">
<%-- 													<span class="admin-manage-editable">${site.uploadMaxSize }</span> --%>
														<span class="admin-manage-editable">20MB</span>
												</div>
											</div>
<!-- 											<div class="control-group control-group-border-bd"> -->
<!-- 												<label class="control-label site-admin-manage-title site-admin-manage-title-first" for="form-field-1">사용중인 용량</label> -->
<!-- 												<div class="controls"> -->
<!-- 												<div class="admin-manage-controls"> -->
<!-- 													<div class="progress progress-striped " style="width:60%"> -->
<!-- 														<div class="bar" style="width: 40%; padding-top:4px;">40%</div> -->
<!-- 													</div> -->
<!-- 												</div> -->
<!-- 													<span class="admin-manage-help-text">할당된 용량 : 100 mb 중 40mb 사용중입니다.</span><span class="admin-manage-help-text-02"> (베타엔 미적용)</span> -->
<!-- 												</div> -->
<!-- 											</div> -->
											<%--
											<hr />
											<div>
												<a class="btn btn-info">사이트 삭제 요청</a>
											</div>
											 --%>
										</form>
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


</body>
</html>