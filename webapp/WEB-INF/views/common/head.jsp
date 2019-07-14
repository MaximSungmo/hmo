<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
<c:choose>
	<c:when test="${sunny.device.mobile || sunny.device.tablet }">
	<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="format-detection" content="telephone=no">
	</c:when>
	<c:otherwise>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	</c:otherwise>
</c:choose>
<title>${param.title }</title>
<c:if test="${sunny.device.mobile || sunny.device.tablet }">
	<%-- 이 부분은 모바일에서만 존재함 --%>
</c:if>
<c:if test="${sunny.device.normal }">
	
	<%-- 이 부분은 데스크탑에서만 보임 --%>
</c:if>
<!-- BEGIN:basic bootstrap-ace styles -->
<c:if test="${ empty param.bsUsed || param.bsUsed != 'NO' }">
<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/bootstrap.css" />
<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/bootstrap-responsive.css" />
</c:if>

<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/font-awesome.css" />
<!--[if IE 7]>
<link rel="stylesheet" href="/assets/bootstrap/v2.3.2/css/font-awesome-ie7.min.css" />
<![endif]-->

<c:if test="${ empty param.aceUsed || param.aceUsed != 'NO' }">
	<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/uncompressed/ace.css" />
	<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/uncompressed/ace-responsive.css" />
	<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/uncompressed/ace-skins.css" />
</c:if>
	
<c:if test="${ not empty param.hmoUsed && param.hmoUsed == 'YES' }">
	<link rel="stylesheet" href="/assets/sunny/2.0/css/uncompressed/hmo.css?v=0.6" />
</c:if>
<!-- END:basic bootstrap-ace styles -->

<!-- Fav and touch icons -->
<link rel="apple-touch-icon-precomposed" sizes="144x144" href="/assets/sunny/2.0/ico/touch-icon-144-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="/assets/sunny/2.0/ico/touch-icon-114-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="72x72" href="/assets/sunny/2.0/ico/touch-icon-72-precomposed.png">
<link rel="apple-touch-icon-precomposed" href="/assets/sunny/2.0/ico/touch-icon-57-precomposed.png">
<link rel="shortcut icon" href="/assets/sunny/2.0/ico/favicon.png?v=1">


<!-- BEGIN:basic js-libs -->
<script src="/assets/sunny/2.0/js/uncompressed/jquery-1.9.1.js"></script>
<script src="/assets/sunny/2.0/js/uncompressed/ejs.js"></script>
<script src="/assets/sunny/2.0/js/uncompressed/iscroll.js"></script>
<script src="/assets/sunny/2.0/js/uncompressed/_iscroll.js"></script>
<script src="/assets/sunny/2.0/js/uncompressed/bootbox.js"></script>
<script src="/assets/sunny/2.0/js/uncompressed/dialog.js"></script>
<script src="/assets/sunny/2.0/js/uncompressed/moment-with-langs.js"></script>
<!-- END:basic js-libs   -->

<c:if test="${ empty param.bsUsed || param.bsUsed != 'NO' }">
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/bootstrap.js"></script>
</c:if>

<script>

/*-- ie 구버전 체크 --*/
 

/*-- global js variables --*/
window.__a__=null;
window.__g__={"documentDomain":"${sunny.documentDomain}",  "lobbyId":"${sunny.site.lobbySmallGroup.id}", "serverName":"${sunny.serverName}", "serverPort":"${sunny.serverPort}", "serverProtocol":"${sunny.serverProtocol}" };
window.__cdn="${sunny.site.cdn}";
window.__usize="${not empty sunny.site.uploadMaxSize ? sunny.site.uploadMaxSize : 5000000}";

<c:choose>
	<c:when test="${empty param.title}">
	window.__orgTitle=document.getElementsByTagName("title")[0].innerHTML;
	</c:when>
	<c:otherwise>
	window.__orgTitle="${param.title}";
	</c:otherwise>
</c:choose>

<sec:authorize access="isAuthenticated()" var="isAuthenticated">
	<c:set value="true" var="isAuthenticated" scope="request"/>
	<sec:authentication var="authUserId" property="principal.userId" scope="request"/>
	<sec:authentication var="authUserName" property="principal.name" scope="request"/>
	<sec:authentication var="authUserProfilePic" property="principal.profilePic" scope="request"/>
	<sec:authentication var="authUserType" property="principal.type" scope="request"/>
	<sec:authentication var="authUserIsAdmin" property="principal.admin" scope="request"/>
	<sec:authentication var="authUserIsSuperAdmin" property="principal.superAdmin" scope="request"/>
	<sec:authentication var="authUserFriendSmallGroupId" property="principal.friendSmallGroupId" scope="request"/>
	<sec:authentication var="authUserMySmallGroupId" property="principal.mySmallGroupId" scope="request"/>
	<sec:authentication var="authUserDepartments" property="principal.departments" scope="request"/>
	<sec:authentication var="authUserUploadMaxSize" property="principal.uploadMaxSize" scope="request"/>
	window.__a__={ "is":true, "id":"${authUserId }", "profilePic":"${authUserProfilePic}" };
	window.__orgEventCount = ${currentInfo.notificationCount } + ${currentInfo.friendRequestCount } + ${currentInfo.messageCount };
	<c:if test="${not empty  authUserUploadMaxSize && authUserUploadMaxSize > 0 }">
	window.__usize=${authUserUploadMaxSize };
	</c:if>
</sec:authorize>

/*-- detect device & browser --*/
(function() {
	
	//detect mobile os
	var
	ua = navigator.userAgent.toLowerCase(),
	matchAndroid=ua.match(/android\s([0-9\.]*)/),
	matchIPhone=ua.match(/iphone|ipod/i),
	matchIPad=ua.match(/ipad/i),
	matchEtcMobile=ua.match(/blackberry|opera mini|iemobile/i),
	androidVersion = ( matchAndroid ) ? parseFloat( matchAndroid[1] ) : null;
	window.__mobile__= { "android": matchAndroid!=null, "version": androidVersion, "ipad": matchIPad!=null, "is": matchAndroid!=null || matchIPhone!=null || matchEtcMobile!=null };
	
	// detect protocol
	window.__protocol_delimeter__ = ( window.__protocol__ = window.location.protocol ) + "//"; 
		
	// detect browser
	$.browser={};
	var match = /(webkit)[ \/]([\w.]+)/.exec(ua)											|| /* webkit	*/
				/(opera)(?:.*version)?[ \/]([\w.]+)/.exec(ua)								|| /* opera		*/
				/(msie) ([\w.]+)/.exec(ua)													|| /* msie		*/		
				ua.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([\w.]+))?/.exec(ua)	|| /* mozilla	*/
				[];
	
	$.browser[ match[1] || "unkown" ] = true;
	$.browser.version = match[2] || "0";
})();


/* -- mod-sunny-js-extentions -- */
(function( $ ) {
	
	// log, info, warn, error, assert
	$.each([ "log", "info", "warn", "error", "assert" ], function(i,fn){
	    $[ fn ] = function() {
	        if ( !window.console ) {
	        	return;
	        }
	        ( window.console[ fn ] )( arguments[ 0 ] );
	    };
	});
	
	// escapeHTML
	$.escapeHTML = function( html ) {
		var entityMap = {
			"&": "&amp;",
			"<": "&lt;",
			">": "&gt;",
			'"': '&quot;',
			"'": '&#39;',
			"/": '&#x2F;'
		}; 
		return String( html ).replace( /[&<>"'\/]/g, function( s ) {
			return entityMap[ s ];
		});
	};
	
	// form serialize 
	$.fn.serializeArrayAlt = function(){
		var rselectTextarea = /^(?:select|textarea)/i,
			rinput = /^(?:color|date|datetime|datetime-local|email|hidden|month|number|password|range|search|tel|text|time|url|week)$/i,
			rCRLF = /\r?\n/g;
		
		return this.map(function(){
			return this.elements ? jQuery.makeArray( this.elements ) : this;
		}).
		filter(function(){
			return	this.name && !this.disabled && ( $(this).attr( "data-serializable" ) != "false" ) && 
					( this.checked || rselectTextarea.test( this.nodeName ) || rinput.test( this.type ) );
		}).
		map(function( i, elem ){
			var val = jQuery( this ).val();
			return val == null ?
				null :
				jQuery.isArray( val ) ?
					jQuery.map( val, function( val, i ){
						return { name: elem.name, value: val.replace( rCRLF, "\r\n" ) };
					}) :
					{ name: elem.name, value: val.replace( rCRLF, "\r\n" ) };
		}).
		get();
	};
	
	$.fn.serializeJSON = function(){
		var 
		obj = {},
		arr = $(this).serializeArrayAlt();
		
		$.each( arr, function() {
			obj[this.name] = this.value;
		});
		
		return obj;
	}
	
	// identify
	window.idCounter=0;
	$.fn.identify = function() {
		if( this.length != 1 ) {
			$.warn( "1 More Element Selected. Not Applied" );
			return null;
		}		
		if( !this[0].tagName ) {
			$.warn( "Only Support HTMLElement(HTML TAG), Not Apply" );
			return null;
		}
		var $e = $( this[0] );
		var id = $e.attr( "id" ); 
		if( id ) {
			return id;
		}
        do { 
        	id = "anonymous_element_" + window.idCounter++;
        } while ( $( "#" + id ).length > 0 );
        $e.attr( "id", id );
        return id;
	};
	
	// basic extends (array short cut)
	window.$A = Array.from = function( iterable ) {
		if ( !iterable )
			return [];
		if ( iterable.toArray ) {
			return iterable.toArray();
		} else {
			var results = [];
			for ( var i = 0, length = iterable.length; i < length; i++)
				results.push( iterable[i] );
			return results;
		}
	}
	
	// add Function.prototype.bind
	!Function.prototype.bind && 
	$.extend( Function.prototype, {
		bind: function() {
			if (arguments.length < 2 && arguments[0] === undefined) {
				return this;
			}
			var __method = this, args = $A(arguments), object = args.shift();
			return function() {
				return __method.apply(object, args.concat($A(arguments)));
			}
		}
	} );
	
	// object serialize
	$.fn.serializeObject = function(){
	    var o = {};
	    var a = this.serializeArray();
	    $.each(a, function() {
	        if (o[this.name] !== undefined) {
	            if (!o[this.name].push) {
	                o[this.name] = [o[this.name]];
	            }
	            o[this.name].push(this.value || '');
	        } else {
	            o[this.name] = this.value || '';
	        }
	    });
	    return o;
	};
	
	// onHMOClick
	window.HMOClick = {
		__SENSITIVITY: 10,	
		__start: null,
		__delta: { x:0, y:0 },
		$fn: function( selector, fn ) {
			if( "ontouchstart" in document.documentElement == false ) {
				this.on( "click", selector, fn );
				return;
			}
			
			this.on( "HMOClick", selector, fn );
			this.on( "click", selector, HMOClick.onDefaultClicked );
			this.on( "touchstart", selector, HMOClick.onTouchStart  );
		},
		onDefaultClicked: function( $event ) {
			$event.preventDefault();
		},
		onDocumentClicked: function( $event ) {
			$event.preventDefault();
			$event.stopPropagation();
			return false;
		},
		onTouchStart: function( $event ) {
			$event.preventDefault();
			$event.stopPropagation();

			var 
			$this = $(this),
			event = $event.originalEvent,
			touches = event.touches[0];
			
			$this.on( "touchmove", HMOClick.onTouchMove );
			$this.on( "touchend", HMOClick.onTouchEnd );
			
			HMOClick.__start = {
				x: touches.pageX,
				y: touches.pageY,
				time: +new Date
			};			
		},
		onTouchMove: function( $event ) {
			var 
			event = $event.originalEvent,
			touches = event.touches[0];
			
			// measure change in x and y
			HMOClick.__delta = {
				x: touches.pageX - HMOClick.__start.x,
				y: touches.pageY - HMOClick.__start.y
			};
		},
		onTouchEnd: function( $event ) {
			$event.preventDefault();
			$event.stopPropagation();
			
			var	$this = $(this);
			$this.off("touchmove", HMOClick.onTouchMove);
			$this.off("touchend", HMOClick.onTouchEnd);

			if( Math.abs( HMOClick.__delta.x) <= HMOClick.__SENSITIVITY &&  Math.abs( HMOClick.__delta.y) <= HMOClick.__SENSITIVITY ) {
				$(document.body).on( "click", null, HMOClick.onDocumentClicked );
				setTimeout( function() {
					$(document.body).off( "click", null, HMOClick.onDocumentClicked );
				}, 500 );
				$this.trigger( "HMOClick" );
			}

			HMOClick.__delta = { x:0, y:0 };
		},
		$fnOff: function( selector, fn ) {
			if( "ontouchstart" in document.documentElement == false ) {
				this.off( "click", fn );
				return;
			}
			this.off( "HMOClick", selector, fn );
			this.off( "click", selector, HMOClick.onDefaultClicked );
			this.off( "touchstart", selector, HMOClick.onTouchStart );
		}
	};
	
	$.fn.onHMOClick = HMOClick.$fn;
	$.fn.offHMOClick = HMOClick.$fnOff;
	
	
})(jQuery);
/* -- mod-sunny-js-extentions -- */

/* -- mod-timesince -- */
var _interval_id_timesince,_timesince_refresh_millis=60000;

function refresh_timesince(){
	var
	$el=$(this),
	t=$el.attr("data-utime");
	
	if( !t ){
	    var 
	    s=function(a,b){ return(1e15+a+"").slice(-b); },
	    d=new Date(parseInt($el.attr("data-timestamp"), 10)),
	    t=d.getFullYear() + '-' +
		  s(d.getMonth()+1,2) + '-' +
	      s(d.getDate(),2) + ' ' +
	      s(d.getHours(),2) + ':' +
	      s(d.getMinutes(),2) + ':' +
	      s(d.getSeconds(),2);
	      
	    $el.attr("data-utime", t);
	}
	
	var
	dt,
	secs,
	itv,
	h,
	m,
	s=$.trim(t);
    
	s=s.replace(/\.\d+/,"");
    s=s.replace(/-/,"/").replace(/-/,"/");
    s=s.replace(/T/," ").replace(/Z/," UTC");
    s=s.replace(/([\+\-]\d\d)\:?(\d\d)/," $1$2");
    dt=new Date(s);
	now = new Date();
    
	if( now.getFullYear() != dt.getFullYear() ){
    	$el.text( dt.getFullYear()+"년 "+(dt.getMonth()+1)+"월 "+dt.getDate()+"일 "+((h=dt.getHours())>12?("오후 "+(h-12)):("오전 "+h))+':'+((m=dt.getMinutes())<10?"0":"")+m );
    }else if( now.getMonth() != dt.getMonth() ){
    	$el.text( (dt.getMonth()+1)+"월 "+dt.getDate()+"일 "+((h=dt.getHours())>12?("오후 "+(h-12)):("오전 "+h))+':'+((m=dt.getMinutes())<10?"0":"")+m );
    }else if( now.getDate() != dt.getDate() ){
    	$el.text( (dt.getMonth()+1)+"월 "+dt.getDate()+"일 "+((h=dt.getHours())>12?("오후 "+(h-12)):("오전 "+h))+':'+((m=dt.getMinutes())<10?"0":"")+m );
    }else{
    	secs=Math.floor((now-dt)/1000);
    	
    	if((itv=Math.floor(secs/3600))>=1){
        	$el.text(itv+"시간 전");
        }else if((itv=Math.floor(secs/60))>=1){
        	$el.text(itv+"분 전");
        }else{
        	$el.text("방금 전");
        }
    }
    
    $el.attr("title", dt.getFullYear()+"년 "+(dt.getMonth()+1)+"월 "+dt.getDate()+"일 "+((h=dt.getHours())>12?("오후 "+(h-12)):("오전 "+h))+':'+((m=dt.getMinutes())<10?"0":"")+m);
}

function timesince(){
	_interval_id_timesince && clearInterval(_interval_id_timesince);
	$(".livetimestamp").each(refresh_timesince);
	_interval_id_timesince = setInterval(function(){$(".livetimestamp").each(refresh_timesince);},_timesince_refresh_millis);
}
/* -- mod-timesince    -- */


/*-- mod-aias-haspopup --*/
var popup={
	__zIndex:1000,
	show:function($popup){
		var $trigger=$("#"+$popup.attr("data-popup-trigger")), 
			toggle=($trigger.attr("rel")=="toggle"), 
			topMost=true,				//($popup.attr("data-top-most")=="true"), 
			zIndex=popup.__zIndex++,	//(topMost ? popup.__zindex++:-1),	
			popupGroup = $popup.attr("data-popup-group"),
			fn = $popup.attr("data-fn-show") || "fn_empty";
		

		if(popupGroup){
			$("[data-popup-group='" + popupGroup + "']").each(function(){
				var $p = $(this);
				$p.is(":visible") && popup.hide($p);
			});
		}
		if( zIndex>0 && $popup.hasClass( "ui-toggle-flyout" ) == false ){
			$popup.css("z-index",zIndex);
		}

		toggle && $trigger.addClass("toggle-on");
		$popup.show();
		
		if( window[ fn ] ){
			window[ fn ].call( $popup );
		} else {
			eval( fn + ".call( $popup )" );
		}	

		$(document).bind("mousedown", popup.fnDocumentMousedown = function(e){ popup.hide($popup); });
		(__mobile__["is"] || __mobile__["ipad"]) && $popup.bind( "click", popup.fnPopupMousedown = function(e){ popup.hide($popup); } );
	},
	hide:function(popup){
		var
		$popup = $(popup),
		$trigger = $( "#"+$popup.attr( "data-popup-trigger" ) ),
		toggle=( $trigger.attr("rel") == "toggle" ),
		fn = $popup.attr("data-fn-hide") || "fn_empty";
		
		toggle && $trigger.removeClass("toggle-on");
		$popup.hide();

		if( window[ fn ] ){
			window[ fn ].call( $popup );
		} else {
			eval( fn + ".call( $popup )" );
		}		
		
		$(document).unbind("mousedown", this.fnDocumentMousedown);
		(__mobile__["is"] || __mobile__["ipad"]) && $popup.unbind( "click", this.fnPopupMousedown );
	},
	fn:function(e){
		e.preventDefault();
		
		var
		$this=$(this), 
		$trigger=$this,
		$popup=$("#"+$trigger.attr("aria-owns"));

		var visible=$popup.is(':visible');
		if(!($trigger.attr("rel")=="toggle")){
			popup.show($popup);
			return;
		}
		
		if(visible){
			popup.hide($popup);
		}else{
			popup.show($popup);
		}		
	},
	stopPropagation:function(e){
		e.stopPropagation();		
	}
}
/*-- mod-aias-haspopup --*/ 


/* -- mod-ajaxify -- */
var Ajaxify = {
	callback: null,
	onFired: function( e ) {
		e && e.stopPropagation();
		e && e.preventDefault() ;
		
		var
		$anchor = $(this),
		url = $anchor.attr("href");
		relation = $anchor.attr("rel");
		
		if( !url ) {
			$.error("Ajaxify.onFired:empty url");
			return;
		}
		
		if( !relation ) {
			relation="async-get";
		}
		
		var rels=relation.split("-"),
			async=(rels[0]=="async"),
			//type=((rels[1]=="post")?"post":"get"),
			type=rels[1],
			cb = $anchor.attr("ajaxify") || 
				 $anchor.attr("ajaxify-dialog-init") ||
				 $anchor.attr("ajaxify-dialog-custom") ||
				 $anchor.attr("ajaxify-dialog-ok") ||
				 $anchor.attr("ajaxify-dialog-yes") ||
				 $anchor.attr("ajaxify-dialog-cancel") ||
				 $anchor.attr("ajaxify-dialog-no") ||
				 "fn_empty",
			cbBefore =  $anchor.attr("ajaxify-before") || "fn_empty", 	 
			data = (type=="post") ?  $anchor.attr("data-request-map") : $.parseJSON($anchor.attr("data-request-map"));
		
		// before job
		// window["before_"+cb] && window["before_"+cb].call($anchor);
		if( window[ cbBefore ] ){
			window[ cbBefore ].call( $anchor );
		} else {
			eval( cbBefore + ".call($anchor)" );
		}
		
		// set ajax callback
		if( window[ cb ] ){
			Ajaxify.callback = window[ cb ];
		} else {
			eval( "Ajaxify.callback=" + cb );
		}
		
		$.ajax({
			url:url+"?rnd=" + Math.floor(Math.random() * 999999999),
			async:async,
			type:type,
			dataType:"json",
			contentType:"application/json", 
			data:data,
			success:  Ajaxify.callback.bind($anchor),
			error: function(jqXHR,textStatus,errorThrown){
				$.error("mod-ajaxify["+$anchor.identify()+"]:"+errorThrown);
			}		
		});
	}
};
/* -- mod-ajaxify -- */



$(function(){
	//
	$(document.body).on( "mousedown", ".ui-toggle-flyout", popup.stopPropagation );
	$(document.body).on( "mousedown", "[aria-haspopup='true']", popup.stopPropagation );
	$(document.body).onHMOClick( "[aria-haspopup='true']", popup.fn );
	//
	$(document.body).onHMOClick( "[ajaxify]", Ajaxify.onFired );
});


/* -- mod-utils -- */
var fn_empty=function(){};
var fn_preventDefault=function(e){ e.preventDefault(); }
var fn_stopPropagation=function(e){ e.stopPropagation(); }
var logger = {
	level: function( level ) {
		var fns = [ "error", "warn", "info", "log"  ],
			threshold = ( level == "none" ) ? -1 : $.inArray( level, fns );
		$.each( fns, function( i, fn ) {
		    this[ fn ] = ( i > threshold ) ? fn_empty : $[ fn ];
		}.bind( this ) );
	}
};
function css_add_class($o, c) {
	$o.addClass(c);
}
function css_remove_class($o, c) {
	$o.addClass(c);
}
function css_toggle_class($o, c) {
	$o.addClass(c);
}
$(function() {
	//
	// patch android
	//
	__mobile__.android && (function() {
		var
		ua = navigator.userAgent,
	    match = ua.match(/Android\s([0-9\.]*)/);
		if( !match ) {
			return;
	    }
		// only <= 4.0
		if( parseFloat( match[1] ) > 4.0 ) {
			return;
		}
		$("#navbar .navbar-inner").addClass( "patch-fixed" );
		$(".breadcrumbs .breadcrumbs-inner").addClass( "patch-fixed" );
	})();
});
/* -- mod-utils -- */

/* -- mod-mook-utils -- */
//admin.user.email 과 같은 것을 {admin:{user:email} 어쩌구와 같이 변경해줌
//JSON.stringify(deepen(data)); 하면 됨
function deepen(o) {
  var oo = {}, t, parts, part;
  for (var k in o) {
    t = oo;
    parts = k.split('.');
    var key = parts.pop();
    while (parts.length) {
      part = parts.shift();
      t = t[part] = t[part] || {};
    }
    t[key] = o[k]
  }
  return oo;
}

//stackoverflow 에 있는 답을 참고했음
//기존의 링크에 key-value 를 더해준다.
function insertParam(parsedLink, key, value)
{
    key = encodeURI(key); 
    value = encodeURI(value);

    if( key == null || value == null || key == "" || value == "")
    	return parsedLink;
    
    var kvp = parsedLink.split('&');

    var i=kvp.length; 
    var x; 
    while(i--) 
    {
        x = kvp[i].split('=');
        if (x[0]==key)
        {
            x[1] = value;
            kvp[i] = x.join('=');
            break;
        }
    }

    if(i<0) 
    {
    	kvp[kvp.length] = [key,value].join('=');
    }

    //this will reload the page, it's likely better to store this until finished
    parsedLink = kvp.join('&');
    return parsedLink;
}

// 위의 insertParam 을 이용해서 map 에 있는 key-value 들을 넣어준다. 
function getParsedLink(_map){
	var parsedLink = "";
	
	for ( var name in _map) {
		parsedLink = insertParam(parsedLink, name, _map[name]);
	}
	
	if( parsedLink.indexOf('&') == 0 ){
		parsedLink = parsedLink.substring(1);
	}
	
	return parsedLink;
}
/* -- mod-mook-utils -- */
</script>



<script>
/* -- mod-dialog -- */ 
//deprecated!!!
var OldDialog = {
	__core__: {
		_$trigger:null,
		_$control: null,
		_buttons: [],
		_option: {},
		_message: "",
		init: function(){},
		doModal: function(){
			
			var
			controlId,
			title,
			ctext="",
			message="";
			
			if( this == window ) {
				controlId = arguments[2];
				if( controlId == null ) {
					$.error( "OldDialog:doModal[ Control not Assigned ]" );
					return;
				}
				title = arguments[0];
				message = arguments[1];
			} else {
				OldDialog["__core__"]._$trigger = $(this);
				controlId = OldDialog["__core__"]._$trigger.attr("aria-controls");
				title = OldDialog["__core__"]._$trigger.attr( "data-label" );
				message = OldDialog["__core__"]._$trigger.attr( "data-message" );
				ctext = OldDialog["__core__"]._$trigger.attr( "data-custom-nm" );
			}

			OldDialog["__core__"]._$control = $( "#" + controlId );
// 			if( OldDialog["__core__"]._$control.length == 0 ) {
// 				$.error( "OldDialog:doModal[ Control not Assigned ]" );
// 				return;
// 			}
			$trigger = OldDialog["__core__"]._$trigger;
			OldDialog["__core__"]._message = message;
// 			OldDialog["__core__"]._option["embed"] = true ;
			OldDialog["__core__"]._option["header"] = title ;
			OldDialog["__core__"]._option["animate"] = false;

			OldDialog["__core__"]._buttons.length = 0;
			if( controlId === MB_CONFIRMATION ){
				OldDialog["__core__"]._$control.attr( "data-style", "MB_CONFIRMATION" );
				OldDialog["__core__"]._$control.attr( "data-message", message );
				OldDialog["__core__"]._$control.attr( "data-ok-fn",    ( OldDialog["__core__"]._$trigger ? OldDialog["__core__"]._$trigger.attr("data-ok-fn") : "" ) );
				OldDialog["__core__"]._$control.attr( "data-cacel-fn", ( OldDialog["__core__"]._$trigger ? OldDialog["__core__"]._$trigger.attr("data-cancel-fn") : "" ) );
			} else if( controlId === MB_ERROR ){
				OldDialog["__core__"]._$control.attr( "data-style", "MB_ERROR" );
				OldDialog["__core__"]._$control.attr( "data-message", message );
				OldDialog["__core__"]._$control.attr( "data-ok-fn", ( OldDialog["__core__"]._$trigger ? OldDialog["__core__"]._$trigger.attr("data-ok-fn") : "" ) );
			} else if( controlId === MB_INFORMATION ){
				OldDialog["__core__"]._$control.attr( "data-style", "MB_INFORMATION" );
				OldDialog["__core__"]._$control.attr( "data-message", message );
				OldDialog["__core__"]._$control.attr( "data-ok-fn", ( OldDialog["__core__"]._$trigger ? OldDialog["__core__"]._$trigger.attr("data-ok-fn") : "" ) );
			} else if( controlId === MB_EXCLAIMATION ){
				OldDialog["__core__"]._$control.attr( "data-style", "MB_EXCLAIMATION" );
				OldDialog["__core__"]._$control.attr( "data-message", message );
				OldDialog["__core__"]._$control.attr( "data-ok-fn", ( OldDialog["__core__"]._$trigger ? OldDialog["__core__"]._$trigger.attr("data-ok-fn") : "" ) );
			} else if( controlId === MB_YESNO ){
				
				OldDialog["__core__"]._buttons.push({"label":"취소", "class":"hmo-button hmo-button-small-10 hmo-button-white"}	);
				OldDialog["__core__"]._buttons.push({"label":"확인", "class":"hmo-button hmo-button-blue hmo-button-small-10", "callback": $trigger.attr("ajaxify") ? Ajxify.onFired.bind( $trigger ) : "fn_empty"});
				
				
// 				OldDialog["__core__"]._$control.attr( "data-style", "MB_YESNO" );
// 				OldDialog["__core__"]._$control.attr( "data-message", message );
// 				OldDialog["__core__"]._$control.attr( "data-yes-fn", ( OldDialog["__core__"]._$trigger ? OldDialog["__core__"]._$trigger.attr("data-yes-fn") : "" ) );
// 				OldDialog["__core__"]._$control.attr( "data-no-fn",  ( OldDialog["__core__"]._$trigger ? OldDialog["__core__"]._$trigger.attr("data-no-fn") : "" ) );
			} else {
				OldDialog["__core__"]._$control.attr( "data-style", "MB_CUSTOM" );
				OldDialog["__core__"]._$control.attr( "data-custom-nm", ctext );
				OldDialog["__core__"]._$control.attr( "data-custom-fn", ( OldDialog["__core__"]._$trigger ? OldDialog["__core__"]._$trigger.attr("data-custom-fn") : "" ) );
				OldDialog["__core__"]._$control.attr( "data-cancel-fn", ( OldDialog["__core__"]._$trigger ? OldDialog["__core__"]._$trigger.attr("data-cancel-fn") : "" ) );
				OldDialog["__core__"]._$control.attr( "data-close-fn",  ( OldDialog["__core__"]._$trigger ? OldDialog["__core__"]._$trigger.attr("data-close-fn") : "" ) );
			} 
			
			OldDialog["__ui__"].activate();
		},
		endOldDialog: function(){
			OldDialog["__ui__"].deactivate();
			this._$control = null;
			this._$trigger=null;
		}
	},
	__ui__: {
		_$dialogWrap: null,
		_$title: null,
		_$content: null,
		_$footer: null,
		_$buttons: null,
		init: function(){
			this._$dialogWrap = $(".dialog-wrap");
			this._$title = $(".dialog-title");
			this._$content = $(".dialog-content");
			this._$content.html("");
			this._$footer = $(".dialog-footer");
			this._$buttons=$(".dialog-buttons");
		},
		enableModalLayer: function( enabled ){
// 			if( enabled ) {
// 				if( !$(".hmo").hasClass("_theater") ) {	
// 					var
// 					$win = $( window ),
// 					st = $(window).scrollTop();
// 					$("#content-area").css({"top": -1*st});
// 				}
// 				$(".hmo").addClass("_theater");
// 				$(".dialog-modal-layer").addClass("modal");
// 			} else {
// 				var
// 				st = parseInt( $("#content-area").css("top"), 10 );
				
// 				$(".hmo").removeClass("_theater");
// 				$("#content-area").css({ "top":0 });
// 				$(window).scrollTop( -1*st );
// 				$(".dialog-modal-layer").removeClass( "modal" );
// 			}
			
		},
		activate: function(){
			//
			this.enableModalLayer( true );
			//
			this._render();
			//
			//$(".dialog").show();
 			bootbox.dialog( OldDialog["__core__"]._message, OldDialog["__core__"]._buttons, OldDialog["__core__"]._option);
			
		},
		deactivate: function(){
			//			
			OldDialog[ "__core__" ]._$control.appendTo($(document.body));
			OldDialog[ "__core__" ]._$control.attr("data-ok-fn", "");
			OldDialog[ "__core__" ]._$control.attr("data-cancel-fn", "");
			OldDialog[ "__core__" ]._$control.attr("data-yes-fn", "");
			OldDialog[ "__core__" ]._$control.attr("data-no-fn", "");
			OldDialog[ "__core__" ]._$control.attr("data-custom-fn", "");
			OldDialog[ "__core__" ]._$control.attr("data-close-fn", "");
			//
			this.enableModalLayer( false );
			//
			var
			style = OldDialog[ "__core__" ]._$control.attr( "data-style" ),
			$activeButton = this._$buttons.find(".active"),
			$cancelButton = this._$buttons.find(".cancel");
			
			$activeButton.text("");
			$activeButton.removeClass("btn-ok btn-yes btn-custom");
			$activeButton.hide();
			$cancelButton.text("");
			$cancelButton.removeClass("btn-cencel btn-no");
			$cancelButton.hide();
			//
			$(".dialog").hide();
		},
		_render: function(){
			//
			var title = OldDialog[ "__core__" ]._$control.attr( "data-title" );
			this._$title.text( title );
			//
			var width = OldDialog[ "__core__" ]._$control.width();
			OldDialog[ "__core__" ]._$control.appendTo( this._$content );
			this._$dialogWrap.css( "width", width + 20 );
			//
			var
			style = OldDialog[ "__core__" ]._$control.attr( "data-style" ),
			$activeButton = this._$buttons.find(".active"),
			$cancelButton = this._$buttons.find(".cancel"); 
			//			
			if( style === "MB_CONFIRMATION" ){
				
				this._$content.find("p").html(OldDialog[ "__core__" ]._$control.attr("data-message"));
				$activeButton.text("확인");
				$activeButton.addClass("btn-ok");
				$activeButton.show();
				
				$cancelButton.text("취소");
				$cancelButton.addClass("btn-cancel");
				$cancelButton.show();
				
			} else if( style === "MB_ERROR" ){
				
				this._$content.find("p").html(OldDialog[ "__core__" ]._$control.attr("data-message"));
				$activeButton.text("확인");
				$activeButton.addClass("btn-ok");
				$activeButton.show();
				
				$cancelButton.text("");
				$cancelButton.hide();
			} else if( style === "MB_INFORMATION" ){
				this._$content.find("p").html(OldDialog[ "__core__" ]._$control.attr("data-message"));
				$activeButton.text("확인");
				$activeButton.addClass("btn-ok");
				$activeButton.show();
				
				$cancelButton.text("");
				$cancelButton.hide();
			} else if( style === "MB_EXCLAIMATION" ){
				this._$content.find("p").html(OldDialog[ "__core__" ]._$control.attr("data-message"));
				$activeButton.text("확인");
				$activeButton.addClass("btn-ok");
				$activeButton.show();
				
				$cancelButton.text("");
				$cancelButton.hide();
			} else if( style === "MB_YESNO" ){
				this._$content.find("p").html(OldDialog[ "__core__" ]._$control.attr("data-message"));
				$activeButton.text("예");
				$activeButton.addClass("btn-yes");
				$activeButton.show();
				
				$cancelButton.text("아니오");
				$cancelButton.addClass("btn-no");
				$cancelButton.show();
			} else if( style === "MB_CUSTOM" ){
				var ctext = OldDialog[ "__core__" ]._$control.attr( "data-custom-nm" );
				if( ctext != null && ctext !== "" ) {
					$activeButton.text(ctext);
					$activeButton.addClass("btn-custom");
					$activeButton.show();
					
					$cancelButton.text("취소");
					$cancelButton.addClass("btn-cancel");
					$cancelButton.show();
					
					// init content
					var fn_init = OldDialog[ "__core__" ]._$control.attr( "data-init-fn" );
					fn_init && window[fn_init].call( OldDialog[ "__core__" ]._$trigger.get(0) );  
						
				} else {
					$activeButton.text("닫기");
					$cancelButton.addClass("btn-close");
					$activeButton.show();
					$cancelButton.text("");
					$cancelButton.hide();
				}
			} else {
			}
		}
	}
};

//Export Functions of OldDialog' Internel Objects ( external access explicitly )
$.extend( OldDialog, {
	init: function() {
		OldDialog[ "__ui__" ].init();
	},
	doModal: OldDialog["__core__"].doModal,
	enableModalLayer: OldDialog["__ui__"].enableModalLayer,
	showMessage: function( title, message, style ){
		OldDialog["__core__"].doModal.call( null, title, message, style );
	},
	onOldDialogTriggerFired: function(e) {
		//
		e.preventDefault();
		//
		e.stopPropagation();
		// 떠있는 팝업 없앰
		//
		OldDialog.doModal.call(this);
	}
});
// alias
// var MessageBox = OldDialog.showMessage;
// init
$(function(){
	OldDialog.init();
	$(document.body).on( "click", "[role='hmo-dialog']", OldDialog.onOldDialogTriggerFired );
});
/* -- mod-dialog -- */ 
</script>


<%--cordova 에서 들어왔을때만 실행된다.  --%>
<c:if test="${ sunny.cordova }">
	<script src="/assets/sunny/2.0/js/uncompressed/cordova.js"></script>
	
	<script>
	
	var fileAttemptCount = 0;
	
	$(function(){
		document.addEventListener("deviceready",onDeviceReady,false);
	});
	function onDeviceReady(){
		<%-- 로그인에 성공했을때 한번만 실행되는 부분 --%>
		<c:if test="${not empty loginNow && loginNow == true }">
			cordova.exec(onSuccessLogin, onFailLogin, "SunnyCordovaPlugin", "onLogin", ["${authUserId}", "${authUserName}", "${authUserProfilePic}", "${sunny.site.id}"]);
		</c:if>
		cordova.exec(empty, empty, "SplashScreen", "hide", [""]);

// 		   navigator.notification.activityStop();
		cordova.exec(empty, empty, "SunnyPreloaderPlugin", "hide", [""]);

		window.onbeforeunload = function(event) {
// 			navigator.notification.activityStart("", "로딩중 ...");
			cordova.exec(empty, empty, "SunnyPreloaderPlugin", "show", [""]);
		}

		<%--
		코르도바 삽질부분
		--%>
		$(document.body).onHMOClick("a.download-media", startCordovaDownload);
		
	}
	function getDirEntry(rootFS, dirName){
		var dataDir = rootFS.getDirectory(dirName, {create: true, exclusive: false});		
		var dirEntry = null;
		window.resolveLocalFileSystemURI(
			rootFS.fullPath + "/" + dirName + "/",
			function(entry){
	    		if(entry.isFile){
	    			entry.remove( 
    					function(){
    						entry = getDirEntry( rootFS, dirName );
    					},
    					function(){
    						alert("HelloMyOffice 란 파일이 이미 존재합니다. 삭제하신 뒤 다시 작업해주세요");
    						return;
    					}
	    			)
	    		}
	    		dirEntry = entry;
	    	},
	    	function(evt){
	    		console.log(JSON.stringify(evt));
	    	}
		)
		return dirEntry;		
		
	}
	
	function parseToAvailableFilename( dirEntry, filename ){
		
		if( fileAttemptCount == 0 ){
		}else if( fileAttemptCount == 1 ){
			filename = fileAttemptCount + "_" + filename;
		}else{
			filename = fileAttemptCount + filename.substring(filename.indexOf("_"));
		}
		
		var availableFilename = filename;
		var exists = false; 		
		dirEntry.getFile(
			filename,
			{create:false},
			function(entry){
				exists = true;
	    		// 파일이 이미 존재하면 다시 시도
	    	},
	    	function(evt){
	    		// 에러 핸들러는 비동기로 처리되는듯.. 여기서 뭐 하면 안됨
	    		console.log( JSON.stringify(evt) );
	    	}
		);
		
		if( exists == true ){
			fileAttemptCount++;
			return parseToAvailableFilename( dirEntry, filename );
		}else{
			return availableFilename;
		}
				
	}
	
	function startCordovaDownload(e){
		e.preventDefault();
	
		var href = $(this).attr("href");
		
		var filename = href.substring( href.lastIndexOf("/") + 1 );
		
		var rootFS = null;
		
		window.requestFileSystem  = window.requestFileSystem || window.webkitRequestFileSystem;
	    window.requestFileSystem(
	    		LocalFileSystem.PERSISTENT, 
	    		0,
	    		function(fileSystem){
	    			rootFS = fileSystem.root;
	    		}, 
	    		function(){
	        		console.log("error getting LocalFileSystem");
	    		}
	    );

	    // Directory 를 읽어온다.
		var dirEntry = getDirEntry( rootFS, "HelloMyOffice");	   
	
	    var availableFilename = parseToAvailableFilename( dirEntry, filename );
		fileAttemptCount = 0;
	    
		var fileTransfer = new FileTransfer();
		
		var uri = encodeURI(href);
		
		
// 		fileTransfer.onprogress =  function(progressEvent) {
// 		    if (progressEvent.lengthComputable) {
// 		    	loadingStatus.setPercentage(progressEvent.loaded / progressEvent.total);
// 		    } else {
// 		      loadingStatus.increment();
// 		    }
// 		}
		
		var localPath = dirEntry.fullPath + "/" + availableFilename;

		fileTransfer.abort( win, fail );
		fileTransfer.download(
		    uri,
		    localPath,
		    win,
		    fail,
		    true,
		    {}
		);
	}
	var win = function(entry) {
		alert("Success!!" + entry.fullPath);
		window.open(encodeURI(entry.fullPath),"_system","location=yes");
        console.log("download complete: " + entry.fullPath);
	}

	var fail = function(error) {
	    alert("An error has occurred: Code = " + error.code);
	    console.log("upload error source " + error.source);
	    console.log("upload error target " + error.target);
	}
	
	function empty(){};
	
	function onSuccessLogin(result){
		console.log("쿠키 success", result);
	}
	
	function onFailLogin(result){
		console.log("쿠키 fail", result);
	}

		</script>
</c:if>
<c:if test="${not empty loginNow && loginNow == true }">
	<script>
		<%-- 로그인에 성공했을때 한번만 실행되는 부분 --%>
		
	</script>
	<c:remove var="loginNow" scope="session"></c:remove>
</c:if>



<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  $(function(){
	  ga('create', 'UA-41965595-2', '${sunny.documentDomain}');
	  ga('send', 'pageview');
  });
</script>