<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script>

var draftId;
<c:if test="${not empty draft}">
	draftId = ${draft.id};
</c:if>

function insert_to_focus(_this){
	
	_$this = $(_this);
	
	var injectTag = "<img src='" + _$this.data("hugesrc") + "' alt='' />";
	
	setContentToTextarea(injectTag);
	
	return false; 
}

function ajax_delete_media(data){
	if(data.result != "success"){
		$.error("error:ajax_delete_media:$.ajax-" + data.message);
		return;
	}
	
	$(this).closest("li").remove();
}
function ajax_delete_draft(data){
	if(data.result != "success"){
		$.error("error:ajax_delete_draft:$.ajax-" + data.message);
		return;
	}
	window.location.href = document.referrer;
	if(IE){ //IE, bool var, has to be defined
	    var newlocation = document.createElement('a');
	    newlocation.href = URLtoCall;
	    document.body.appendChild(newlocation);
	    newlocation.click();
	}
}


<c:if test="${param.contentType == 8}">
function parsingApprovalData(data){
	var isChecked = $("#approbator-checkbox input[type=checkbox]").is(":checked");
	
	data["checkOrdering"] = isChecked;

	var draftSmallGroupApprovals = new Array();
	
	if( isChecked == true ){
		$("#approbator-list-wrap").find(".candi-row").each(function( index ){
			var eachItem = {};
			var map = $(this).data("map");
			
			eachItem["smallGroupId"] = map.id;
			eachItem["draft"] = {"id":draftId};
			eachItem["type"]=0;
			eachItem["ordering"]=index;
			eachItem["name"]=map.name;
			draftSmallGroupApprovals.push(eachItem);
		});
	}else{
		$("#approbator-list-wrap").find(".candi-row").each(function( ){
			var eachItem = {};
			var map = $(this).data("map");
			eachItem["smallGroupId"] = map.id;
			eachItem["draft"] = {"id":draftId};
			eachItem["type"]=0;
			eachItem["name"]=map.name;
			draftSmallGroupApprovals.push(eachItem);
		});	
	}
	
	
	$("#cooperation-list-wrap").find(".candi-row").each(function(){
		var eachItem = {};
		var map = $(this).data("map");
		eachItem["smallGroupId"] =map.id;
		eachItem["draft"] = {"id":draftId};
		eachItem["type"]=1;
		eachItem["name"]=map.name;
		draftSmallGroupApprovals.push(eachItem);
	});
	
	$("#receiver-list-wrap").find(".candi-row").each(function(){
		var eachItem = {};
		var map = $(this).data("map");
		eachItem["smallGroupId"] = map.id;
		eachItem["draft"] = {"id":draftId};
		eachItem["type"]=2;
		eachItem["name"]=map.name;
		draftSmallGroupApprovals.push(eachItem);
	});
	
	$("#circulation-list-wrap").find(".candi-row").each(function(){
		var eachItem = {};
		var map = $(this).data("map");
		eachItem["smallGroupId"] = map.id;
		eachItem["draft"] = {"id":draftId};
		eachItem["type"]=3;
		eachItem["name"]=map.name;
		draftSmallGroupApprovals.push(eachItem);
	});
	data["draftSmallGroupApprovals"] = draftSmallGroupApprovals;
}

</c:if>
function save_draft(url){
	
	if( donotsave == 1 ){
		return;
	}
	
	var data = {
			rawTitle:$("#note-title").val(),
			rawText: getContentFromTextarea(),
// 	    	permissions: Permission.getArrayData(),
			id: draftId
	};

	
	
	<c:if test="${param.contentType == 8}">
		parsingApprovalData(data);
	</c:if>
	
	
	$.ajax( {
		url: url,
		//async: false, /* Cross-domain or Jsonp Async Option not support */
		type: "POST",
		async: false,
		dataType: "json",
		contentType: 'application/json',
		data: JSON.stringify(data),
		success: function(data){
		},
		error: function( jqXHR, textStatus, errorThrown ){
		},
		statusCode: {
			"401" : function() {
				alert( "Unauthorized" );
			},
			"404" : function() {
				alert( "Unkown" );
			}
		}		
	} );
    return ;
}

/* -- mode-note-write -- */
var donotsave=0;
$(function(){

	var tmpPhotoCount = 0;
// 	$(document).on("click",".draftPhotoBoxWrap .closeButtonLabel input[type=button]",${param.typeName}PicUploader.onClickClose);
// 	$(document).on("click",".photo-inject", ${param.typeName}PicUploader.onClickInject);

	$(window).on('beforeunload', function() {
		save_draft("/draft/update");
		return; 
	});
	
});
 
</script>

<script>
// form submit
Event.__submit = function( e ) {
	
	$.Event( e ).preventDefault();
	var
	$form = $("#draft-form"),
	submitName = $(e).attr("data-type");
	if( submitName=="publish" ) {
		var data = {
				rawTitle:$("#note-title").val(),
				rawText: getContentFromTextarea(),
// 		    	permissions: Permission.getArrayData(),
				id: draftId
		};
		<c:if test="${param.contentType == 8}">
			parsingApprovalData(data);
		</c:if>

		
		$.ajax( {
			url: "/draft/publish",
			type: "POST",
			async: false,
			dataType: "json",
			contentType: 'application/json',
			data: JSON.stringify(data),
			success: function(data){
				if( data.result == "fail"){
					alert(data.message);
					return;
				}
				var groupPath = '';
				<c:if test="${not empty smallGroup}">
					groupPath = "/group/${smallGroup.id}";
				</c:if>
				donotsave = 1;
				window.location.href = groupPath + "/${param.typeName}";
			},
			error: function( jqXHR, textStatus, errorThrown ){
			},
			statusCode: {
				"401" : function() {
					alert( "Unauthorized" );
				},
				"404" : function() {
					alert( "Unkown" );
				}
			}		
		} );
		
	} 
	
	<c:if test="${not empty draft.pushContent}">
	else if( submitName=="push" ) {
		var data = {
				rawTitle:$("#note-title").val(),
				rawText: getContentFromTextarea(),
// 		    	permissions: Permission.getArrayData(),
				id: draftId,
				pushContent: {
					id: ${draft.pushContent.id}
				}
		};

		<c:if test="${param.contentType == 8}">
			parsingApprovalData(data);
		</c:if>

		
		$.ajax( {
			url: "/draft/push",
			type: "POST",
			async: false,
			dataType: "json",
			contentType: 'application/json',
			data: JSON.stringify(data),
			success: function(data){
				if( data.result == "fail")
					alert(data.result);	
				var groupPath = '';
				<c:if test="${not empty smallGroup}">
					groupPath = "/group/${smallGroup.id}";
				</c:if>
				donotsave = 1;
				window.location.href = groupPath + "/${param.typeName}/" + data.data;
				},
			error: function( jqXHR, textStatus, errorThrown ){
			},
			statusCode: {
				"401" : function() {
					alert( "Unauthorized" );
				},
				"404" : function() {
					alert( "Unkown" );
				}
			}		
		} );
		
	} 
	</c:if>
	else if( submitName=="preview" ) {
		$form.attr( "action", "/draft/update");
		// draft가 ajax submit 이면,
		Event.__inlineSubmit( form, event );
		document.location.reload();
		window.location.href = "/${param.typeName}/preview/" + draftId;
	} else if( submitName=="draft" ) {
		//$form.attr( "action", "/draft/update");
		// draft가 ajax submit 이면,
		//Event.__inlineSubmit( form, event );
		save_draft( "/draft/update" );
	} else if( submitName=="cancel" ) {
		donotsave=1;
		window.location.href = "/${param.typeName}/drafts";
	} 

	return false;
}


// ajax submit 이 되게 할려면 이걸 구현하고 
Event.__inlineSubmit = function(form,event){
	var 
	$form=$(form),
	rel=$form.attr("rel"),
	url=$form.attr("action"),
	type=$form.attr("method");
	
	if( rel.toLowerCase()=="sync" || rel.toLowerCase()=="async") {
		if( event.preventDefault ) {
			event.preventDefault();
		} else {
			event.returnValue = false;
		}	
	}
	save_draft( url );
    return ( event.preventDefault ) ? true : false;

}

</script>





<c:if test="${param.contentType == 8 }">
<script>

$(function(){
	$tagsInput = $("#userAndGroup-search-form-input");
});
  
function show_userAndGroupBootbox(callback, $wrap){
	var userAndGroupAjax = $.ajax({
		url: "/pagelet/user_and_group?" + getParsedLink(userAndGroupParamMap),
		type: "GET"
	});
	
	var $userAndGroupContent = $("#res-dialog-add-user-and-group-content");
	
	var $userAndGroupChangeable = $userAndGroupContent.find(".changeable-pagelet");
	bootbox.dialog( "res-dialog-add-user-and-group", [{
		"label" : "확인",
		"class" : "hmo-button hmo-button-blue hmo-button-small-10",
		"callback": callback
	}],{
		"embed" : true,
		"animate" : false,
		"onInit" : function() {
			
// 			userAndGroupCounter = $wrap.find(".candi-row").length;
// 			$(".modal-footer").prepend("<span class='add-count'>" + userAndGroupCounter + "개 선택됨</span>");			
			$.when(userAndGroupAjax)
			.done(function(data){
				if(data.result == "fail"){
					alert( data.message );
				}
				$userAndGroupChangeable.html( data );
			})
			.fail(function(data){
				alert("오류발생");
			});
			$wrap.find(".candi-row").each(function(index){
				//add_user_and_group(this);

				$this = $(this);
				var map = $this.data("map");
				$tagsInput.tagsinput('add', {id:map.id, name:map.name});
				
				
// 				$tagsInput.tagsinput('add', {id:$(this).data("id"), name:$(this).data("name")});
			});

		},
		"onFinalize" : function() {
			$("html").css("overflow", "auto");
			$("html").css("position", "relative");
			$("body.scroll-y").css("overflow-y", "auto");
		},
		"beforeShown" : function() {
			// 로딩으로 변경
			$userAndGroupChangeable.html( "<i class='fa fa-spinner fa-spin'></i>" );
			$("html").css("position", "fixed");
			$("html").css("overflow-y", "scroll");
			$("body.scroll-y").css("overflow-y", "hidden");
			//$("#userAndGroup-candi-list").html($wrap.html());
			$tagsInput.tagsinput('removeAll');
			
		}
	});
}
  
function add_approbator(){
	$("#approbator-list-wrap").find(".candi-arrow").addClass("hidden-elem");	
	var $wrap = $("#approbator-list-wrap");
	var callback = function(){
		
		var parsedHtml = "";
		
		var inputTags = $tagsInput.tagsinput('items');
		
		for( var i in inputTags ){
			parsedHtml += 	'<li class="candi-row" data-sgid="' + inputTags[i].id + '" data-map=\'{"id":"' + inputTags[i].id + '", "name":"' + inputTags[i].name + '"}\'>' +
			'<div class="candi-info"><span class="candi-name" >' + inputTags[i].name +
			'</span><span class="toolbox">' +
			'<span class="hmo-button hmo-button-white hmo-button-small-4"><i class="fa fa-caret-up"></i></span>' +
			'<span class="hmo-button hmo-button-white hmo-button-small-4"><i class="fa fa-caret-down"></i></span>' +
			'<span class="hmo-button hmo-button-white hmo-button-small-4 btn-userAndGroup-remove">' +
				'<i class="fa fa-trash-o"></i>' +
			'</span></span></div><div class="hidden-elem candi-arrow"><i class="fa fa-long-arrow-down"></i></div></li>';
		}
		
		$wrap.html( parsedHtml );
		//$wrap.html($("#userAndGroup-candi-list").html());
		
		if( $wrap.find(".candi-row").length > 0 ){
			$("#approbator-checkbox").removeClass("hidden-elem");
			
			
			var isChecked = $("#approbator-checkbox input[type=checkbox]").is(":checked");
			$.log(isChecked);
			if( isChecked){
				$("#approbator-list-wrap").find(".candi-arrow").removeClass("hidden-elem");
			}else{
				$("#approbator-list-wrap").find(".candi-arrow").addClass("hidden-elem");	
			}
			
			$("#approbator-wrap").removeClass("hidden-elem");
			$("#misc-wrap").removeClass("hidden-elem");
			
		}else{
			$("#approbator-checkbox").addClass("hidden-elem");
			$("#approbator-wrap").addClass("hidden-elem");
		}
		return true;
	}
	
	show_userAndGroupBootbox( callback, $wrap );
	
}
function add_cooperation(){
	var $wrap = $("#cooperation-list-wrap");
	var callback = function(){
		var parsedHtml = "";
		
		var inputTags = $tagsInput.tagsinput('items');
		
		for( var i in inputTags ){
			parsedHtml += 	'<li class="candi-row"  data-sgid="' + inputTags[i].id + '" data-map=\'{"id":"' + inputTags[i].id + '", "name":"' + inputTags[i].name + '"}\'>' +
			'<div class="candi-info"><span class="candi-name" >' + inputTags[i].name +
			'</span><span class="toolbox">' +
			'<span class="hmo-button hmo-button-white hmo-button-small-4"><i class="fa fa-caret-up"></i></span>' +
			'<span class="hmo-button hmo-button-white hmo-button-small-4"><i class="fa fa-caret-down"></i></span>' +
			'<span class="hmo-button hmo-button-white hmo-button-small-4 btn-userAndGroup-remove">' +
				'<i class="fa fa-trash-o"></i>' +
			'</span></span></div></li>';
		}
		
		$wrap.html( parsedHtml );
		
		if( $wrap.find(".candi-row").length > 0 ){
			$("#cooperation-wrap").removeClass("hidden-elem");			
		}else{
			$("#cooperation-wrap").addClass("hidden-elem");
		}
		return true;
	}
	show_userAndGroupBootbox( callback, $wrap );
}
function add_receiver(){
	var $wrap = $("#receiver-list-wrap");
	var callback = function(){
	var parsedHtml = "";
		
		var inputTags = $tagsInput.tagsinput('items');
		
		for( var i in inputTags ){
			parsedHtml += 	'<li class="candi-row"  data-sgid="' + inputTags[i].id + '" data-map=\'{"id":"' + inputTags[i].id + '", "name":"' + inputTags[i].name + '"}\'>' +
			'<div class="candi-info"><span class="candi-name" >' + inputTags[i].name +
			'</span><span class="toolbox">' +
			'<span class="hmo-button hmo-button-white hmo-button-small-4"><i class="fa fa-caret-up"></i></span>' +
			'<span class="hmo-button hmo-button-white hmo-button-small-4"><i class="fa fa-caret-down"></i></span>' +
			'<span class="hmo-button hmo-button-white hmo-button-small-4 btn-userAndGroup-remove">' +
				'<i class="fa fa-trash-o"></i>' +
			'</span></span></div></li>';
		}
		
		$wrap.html( parsedHtml );
		
		if( $wrap.find(".candi-row").length > 0 ){
			$("#receiver-wrap").removeClass("hidden-elem");			
		}else{
			$("#receiver-wrap").addClass("hidden-elem");
		}
		
		return true;
	}

	show_userAndGroupBootbox( callback, $wrap );
}
function add_circulation(){
	

	
	var $wrap = $("#circulation-list-wrap");
	var callback = function(){
	var parsedHtml = "";
		
		var inputTags = $tagsInput.tagsinput('items');
		
		for( var i in inputTags ){
			parsedHtml += 	'<li class="candi-row"  data-sgid="' + inputTags[i].id + '" data-map=\'{"id":"' + inputTags[i].id + '", "name":"' + inputTags[i].name + '"}\'>' +
			'<div class="candi-info"><span class="candi-name" >' + inputTags[i].name +
			'</span><span class="toolbox">' +
			'<span class="hmo-button hmo-button-white hmo-button-small-4"><i class="fa fa-caret-up"></i></span>' +
			'<span class="hmo-button hmo-button-white hmo-button-small-4"><i class="fa fa-caret-down"></i></span>' +
			'<span class="hmo-button hmo-button-white hmo-button-small-4 btn-userAndGroup-remove">' +
				'<i class="fa fa-trash-o"></i>' +
			'</span></span></div></li>';
		}
		
		$wrap.html( parsedHtml );
		
		if( $wrap.find(".candi-row").length > 0 ){
			$("#circulation-wrap").removeClass("hidden-elem");			
		}else{
			$("#circulation-wrap").addClass("hidden-elem");
		}
		
		return true;
	}

	show_userAndGroupBootbox( callback, $wrap );
}


function add_user_and_group(_this){
	
	$.log("class : " + $(_this).attr("class"));
	$this = $(_this);
	var map = $this.data("map");
	$.log(map.id);
	
	$existCandirow = $("[data-sgid=" + map.id + "]");
	if( $existCandirow.length > 0 ){
		alert( "승인자, 협조처, 수신자, 회람자는 중복될 수 없습니다.");
		return false; 
	}
	
	$tagsInput.tagsinput('add', {id:map.id, name:map.name});
	
	

// 	$candiList = $("#userAndGroup-candi-list");
	
// 	if( $candiList.find("[data-id=" + map.id + "]").length > 0 ){
// 		$.log("이미 존재함");
// 		return;
// 	}
	
// 	$modalCountBar = $(".modal-footer").find(".add-count");
// 	if( $modalCountBar.length == 0 ){
// 		userAndGroupCounter = 1;
// 		$(".modal-footer").prepend("<span class='add-count'>1개 선택됨</span>");
// 	}else{
// 		$modalCountBar.html(++userAndGroupCounter + "개 선택됨");
// 	}
// 	$candiList.append(
// 		'<li class="candi-row" data-id="' + map.id + '" data-name="' + map.name + '">' +
// 		'<div class="candi-info"><span class="candi-name" >' + map.name +
// 		'</span><span class="toolbox">' +
// 		'<span class="btn btn-primary btn-minier  btn-userAndGroup-up"><i class="fa fa-caret-up"></i></span>' +
// 		'<span class="btn btn-primary btn-minier  btn-userAndGroup-down"><i class="fa fa-caret-down"></i></span>' +
// 		'<span class="btn btn-danger btn-minier btn-userAndGroup-remove">' +
// 			'<i class="fa fa-trash-o"></i>' +
// 		'</span></span></div><div class="hidden-elem candi-arrow"><i class="fa fa-long-arrow-down"></i></div></li>'
// 	);
	
	
}


var userAndGroupParamMap = new Array();

userAndGroupParamMap["type"] = 0;


function replaceList(parsedLink){
	var $changeable = $("#res-dialog-add-user-and-group-content .changeable-pagelet");
	$changeable.html("<table><tr><td class='center' colspan='100%'><i class='fa fa-spin fa-spinner' /></td></tr></table>");
	$.get("/pagelet/user_and_group?" + parsedLink, function(data, status){
		$changeable.html(data);
	});
}

$(function(){
	
	$("#userAndGroup-search-form").submit(function(e){
		e.preventDefault();
		var query = $(this).find("input[type=text]").val();
		//var query = $("#userAndGroup-search-form-input").tagsinput('input').val();
		userAndGroupParamMap["q"] = query;
		userAndGroupParamMap["page"] = 1;
		
		replaceList(getParsedLink(userAndGroupParamMap));
		
	});
	
	$(document.body).onHMOClick(".userAndGroup-more-detail", function(e) {
		e.preventDefault();
		
		var elemType = $(this).data("type");
		var name = $(this).data("name");
		
		if( name != "page"){
			userAndGroupParamMap["page"] = 1;
		}
		
		if( name == "qt"){
			if( $("#userAndGroup-search-form input[type=text]").val() == "" ){
				userAndGroupParamMap[name] = $(this).data("value");
				return;
			}
		}
		
		if (elemType == "multiple") {
			var arr = $.map($($(this).data("selector")), function(
					elem, index) {
				return +$(elem).data("value");
			});
			userAndGroupParamMap[name] = arr;
		} else {
			//e.preventDefault();
			userAndGroupParamMap[name] = $(this).data("value");
		}
		replaceList( getParsedLink(userAndGroupParamMap) );
	});
	
	$(document.body).onHMOClick(".btn-userAndGroup-remove", function(e){
		$(this).closest(".candi-row").remove();
		return false;
	});
	$(document.body).onHMOClick(".btn-userAndGroup-up", function(e) {
		e.preventDefault();	
		
		var $thisRow = $(this).closest(".candi-row");
		var $prevRow = $thisRow.prev();
		if( $prevRow.length > 0 ){
			$thisRow.slideUp(function(){
				$thisRow.insertBefore($prevRow).slideDown();	
			});
		}
	});
	$(document.body).onHMOClick(".btn-userAndGroup-down", function(e) {
		e.preventDefault();	
		
		var $thisRow = $(this).closest(".candi-row");
		var $nextRow = $thisRow.next();
		if( $nextRow.length > 0 ){
			$thisRow.slideUp(function(){
				$thisRow.insertAfter($nextRow).slideDown();	
			});
		}
	});
	
	$("#approbator-checkbox").change(function(e){

		var isChecked = $(e.target).is(":checked");
		if( isChecked){
			$("#approbator-list-wrap").find(".candi-arrow").removeClass("hidden-elem");
		}else{
			$("#approbator-list-wrap").find(".candi-arrow").addClass("hidden-elem");	
		}
				
	});
	
});

</script>
</c:if>