<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	
	<ul id="permission-list-ul"
		class="dropdown-menu  pull-${param.dropdownDirection } dropdown-lighter dropdown-caret dropdown-125 ui-toggle-flyout"
		data-popup-trigger="toggle-permission-detail"
		data-popup-group="global">
		
		<c:choose>
		
			<c:when test="${not empty basecampUser && basecampUser.id == authUserId }">
				<li class="active">
					<a href="#" class="default-candi-row " data-id="${authUserMySmallGroupId }" data-type="0"> 
						<i class=""></i> &nbsp; <span class="text">나만 보기</span>
						</a>
				</li>
			</c:when>
			<c:when test="${not empty basecampUser && basecampUser.id != authUserId }">
				<li class="active">
					<a href="#" class="default-candi-row " data-id="${basecampUser.mySmallGroup.id }" data-type="0"> 
						<i class=""></i> &nbsp; <span class="text">${basecampUser.name }과 나만 보기</span>
						</a>
				</li>
			
			</c:when>		
			<c:when test="${empty smallGroup }">
					
				<li class="active">
					<a href="#" class="default-candi-row " data-id="${sunny.site.lobbySmallGroup.id }" data-type="1"> 
							<span class="text"><i class="fa fa-globe"></i>&nbsp;&nbsp;&nbsp;전체공개</span>
						</a>
				</li>
				<li>
					<a href="#" class="default-candi-row " data-id="${authUserMySmallGroupId }" data-type="0"> 
						 <span class="text"><i class="fa fa-unlock-alt"></i>&nbsp;&nbsp;&nbsp;나만 보기</span>
					</a>
				</li>
			</c:when>
			<c:otherwise>
				<li class="active">
					<a href="#" class="default-candi-row " data-id="${smallGroup.id }" data-type="${smallGroup.type }"> 
						<i class=""></i> &nbsp; <span class="text">${smallGroup.name }</span>
						</a>
				</li>
				<li >
					<a href="#" class="default-candi-row " data-id="${authUserMySmallGroupId }" data-type="0"> 
						 <span class="text"><i class="fa fa-unlock-alt"></i>&nbsp;&nbsp;&nbsp;나만 보기</span>
					</a>
				</li>
			</c:otherwise>
		</c:choose>

		<li class="divider"></li>
		
		<li id="add_permission-li">
			<a href="#" id="pop-add-permission" onclick="return pop_add_permission();"> <i class="fa fa-plus blue"></i> &nbsp; 
			추가 공개
			</a>
		</li>
	</ul>

<script>

var permissionParamMap = new Array();

permissionParamMap["type"] = 3;

function replaceList(parsedLink){
	$("#permission-search-list").html("<table><tr><td class='center' colspan='100%'><i class='fa fa-spin fa-spinner' /></td></tr></table>");
	$.get("/pagelet/permission?" + parsedLink, function(data, status){
		$("#permission-search-list").html(data);
	});
}

function remove_candi_permission(_this){
	$this = $(_this);
	
	$this.parents(".candi-row").remove();
	return false; 
}

function add_candi_permission(_this){
	
	$this = $(_this);
	
	var map = $(_this).data("map");
	
	$tagsInput.tagsinput("add", {id:map.id, type:map.type, name:map.name});
	
}
var Permission = {
		
	getArrayData:function(){
		
		var _$defaultRow = $("#permission-list-ul li.active > .default-candi-row");
		

		var permissions = new Array();
		
		if( _$defaultRow.length > 0 ){
			var defaultData = {};
			var type = _$defaultRow.data("type");
			defaultData["id"] = _$defaultRow.data("id");
			
			if( type != "undefined" && type != null  ){
				defaultData["smallGroupType"] = type;
			}
			<%--
			_$defaultRow.find(".permission-rwd.active").each(function(){
				defaultData[$(this).data("name")] = true;		
			});
			--%>
			

			permissions.push( defaultData );
		}
		
		var _$permissionRows = $("#permission-list-ul .candi-row");
		
		if( _$permissionRows.length > 0 ){
			
			_$permissionRows.each(function(){
				var permEach = {};
				var _$eachRow = $(this);
				permEach["id"] = _$eachRow.data("id");
				
				var type = _$eachRow.data("type");
				
				if( type != "undefined" && type != null  ){
					permEach["smallGroupType"] = type;
				}
				
				<%--
				_$eachRow.find(".permission-rwd.active").each(function(){
					permEach[$(this).data("name")] = true;		
				});
				
				
				if( _$eachRow.find(".permission-chk-children:checked").length > 0 ){
					permEach["children"] = true;
				}
				--%>
				permissions.push( permEach );
			});
		}
		return permissions;
	}
		
}
function pop_add_permission(){
	
	$.log(permissionParamMap["type"]);
	
	if( typeof(alreadyShownPermBootbox) == "undefined" || alreadyShownPermBootbox == null ){
		
		var permissionAjax = $.ajax({
			url: "/pagelet/permission?" + getParsedLink(permissionParamMap),
			type: "GET"
		});
	}
	
	bootbox.dialog( "res-dialog-add-permission", [
		{
			"label" : "확인",
			"class" : "hmo-button hmo-button-blue hmo-button-small-10",
			"callback": function() {
				
				//$("#add_permission-li").before($("#permission-candi-list").html());
				
				$("#permission-list-ul li.candi-row").remove();
						
				
				var inputTags = $tagsInput.tagsinput('items');

				var parsedHtml = "";
				for( var i in inputTags ){
					parsedHtml += 	'<li class="candi-row" data-id="' + inputTags[i].id + '" data-type="' + inputTags[i].type +'" data-map=\'{"id":"' + inputTags[i].id + '", "name":"' + inputTags[i].name + '", "type":"'+ inputTags[i].type+'"}\'>' +
						'<a href="#" ><i class=""></i>&nbsp;<span class="candi-name" >' + inputTags[i].name + 
			 			'</span>' + 
			 			'<span class="btn btn-danger btn-minier btn-permission-remove">' +
			 				'<i class="fa fa-trash-o"></i>' +
			 			'</span></a>' +
			 		'</li>';
				}
				$("#add_permission-li").before( parsedHtml );
				return true;
			}
		},
		{
			"label" : "취소",
			"class" : "hmo-button hmo-button-white hmo-button-small-10"
		}
    ],{
	"embed" : true,
	"onInit" : function() {
		if( typeof(alreadyShownPermBootbox) == "undefined" || alreadyShownPermBootbox == null ){
		
			$.when(permissionAjax)
			.done(function(data){
				$("#permission-search-list").html( data );
				alreadyShownPermBootbox = true;
			})
			.fail(function(data){
				alert("오류발생");
			});
			
		}else{
		}
	},
	"onFinalize" : function() {
		// 다이럴로그가 올라왔을 때 뒤 body 가 같이 스크롤되는 현상이 있어서 바디의 스크롤을 없앰.
		// 추후 안이사님 방식으로 height 변경하는 것으로 변경할 것.
		$("html").css("overflow", "auto");
		$("html").css("position", "relative");
		$("body.scroll-y").css("overflow-y", "auto");
		//$("#permission-toggle-wrap").addClass("open");
		$("#toggle-permission-detail").click();
		
	},
	"beforeShown" : function() {
		if( typeof(alreadyShownPermBootbox) == "undefined" || alreadyShownPermBootbox == null ){
				$("#permission-search-list").html("<tr><td class='center' colspan='100%'><i class='fa fa-spin fa-spinner' /></td></tr>");	
		}
		// 다이럴로그가 올라왔을 때 뒤 body 가 같이 스크롤되는 현상이 있어서 바디의 스크롤을 없앰.
		// 추후 안이사님 방식으로 height 변경하는 것으로 변경할 것.
		$("html").css("position", "fixed");
		$("html").css("overflow-y", "scroll");
		$("body.scroll-y").css("overflow-y", "hidden");
// 		$("#permission-candi-list").empty();
// 		$("#permission-candi-list").append($("#permission-list-ul .candi-row"));
		$tagsInput.tagsinput('removeAll');
		$("#permission-list-ul").find(".candi-row").each(function(index){
			$this = $(this);
			var map = $(this).data("map");
			$tagsInput.tagsinput("add", map);
		});
	}
	});
	
}

$(function(){
	
	$(document.body).onHMOClick('.btn-permission, .default-candi-row', function(e){
		e.preventDefault();
		e.stopPropagation();
		
		if( $(e.target).hasClass("btn-permission") ){
			if( typeof($(this).data("toggle")) == "undefined" )
				return false;	
			
			$(this).toggleClass("active");
			return;
		}
		
		if( $(this).hasClass("active") ){
			return;
		}
	
		$(".default-candi-row").parent().removeClass("active");
		$(this).parent().addClass("active");
	
		$("#toggle-permission-detail .text").html($(this).find(".text").html());

	});
	
	$(document.body).onHMOClick(".btn-permission-remove", function(e){
		$(this).parents(".candi-row").remove();
		return false;
	});
	

	$("#permission-search-form").submit(function(e){
		e.preventDefault();
		var query = $(this).find("input[type=text]").val();
		
		permissionParamMap["q"] = query;
		permissionParamMap["page"] = 1;
		
		replaceList(getParsedLink(permissionParamMap));
		
	});
	
	$(document.body).onHMOClick(".permission-more-detail", function(e) {
		e.preventDefault();
		
		
		var $this = $(this);
		var elemType = $this.data("type");
		var name = $this.data("name");
		
		if( name == "type" ){
			$this.siblings("a").removeClass("active");
			$this.addClass("active");
		}
		
		if( name != "page"){
			permissionParamMap["page"] = 1;
		}
		
		if( name == "qt"){
			if( $("#permission-search-form input[type=text]").val() == "" ){
				permissionParamMap[name] = $this.data("value");
				return;
			}
		}
		
		if (elemType == "multiple") {
			var arr = $.map($($this.data("selector")), function(
					elem, index) {
				return +$(elem).data("value");
			});
			permissionParamMap[name] = arr;
		} else {
			permissionParamMap[name] = $this.data("value");
		}
		replaceList( getParsedLink(permissionParamMap) );
	});
	
	$tagsInput = $("#permission-tagsinput");
});

</script>
