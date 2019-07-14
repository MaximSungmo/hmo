<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<title>${param.title }</title>

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
<%-- 	<link rel="stylesheet" href="/assets/ace-theme-v1.2/bs-v2.3.x/css/uncompressed/ace-skins.css" />  --%>
</c:if>
	
<link rel="stylesheet" href="/assets/sunny/2.0/css/uncompressed/hmo-front.css" />


<!-- BEGIN:basic js-libs -->
<!-- <script src="http://jsconsole.com/remote.js?hellomyoffice"></script> -->
<script src="/assets/sunny/2.0/js/uncompressed/jquery-1.9.1.js"></script>



<c:if test="${ empty param.bsUsed || param.bsUsed != 'NO' }">
<script src="/assets/ace-theme-v1.2/bs-v2.3.x/js/uncompressed/bootstrap.js"></script>
</c:if>



<c:if test="${ empty param.jsUsed || param.jsUsed != 'NO' }">


<%--<script src="/assets/sunny/2.0/js/uncompressed/ejs.js"></script>
<script src="/assets/sunny/2.0/js/uncompressed/bootbox.js"></script>
<script src="/assets/sunny/2.0/js/uncompressed/moment-with-langs.js"></script>
 --%>
<!-- END:basic js-libs   -->

<script>

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
			this.on( "touchstart", selector, HMOClick.onTouchStart  );
		},
		onDocumentBodyClicked: function( $event ) {
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
				$(document.body).on( "click", null, HMOClick.onDocumentBodyClicked );
				setTimeout( function() {
					$(document.body).off( "click", null, HMOClick.onDocumentBodyClicked );
				}, 500 );
				$this.trigger( "HMOClick" );
			}

			HMOClick.__delta = { x:0, y:0 };
		},
		$fnUnbind: function( fn ) {
			this.off( "click", null, fn );
			this.off( "touchstart", null, HMOClick.onTouchStart );
		}
	};
	
	$.fn.onHMOClick = HMOClick.$fn;
	$.fn.unbindHMOClick = HMOClick.$fnUnbind;
	
})(jQuery);
/* -- mod-sunny-js-extentions -- */


/* -- mod-ajaxify -- */
var Ajaxify = {
	callback: null,
	onFired: function( e ) {
		
		e && e.preventDefault();
		var $anchor=$(this);
		var url=$anchor.attr("href");
		if(!url){
			$.error("mod-ajaxify["+$anchor.identify()+"]:empty url");
			return;
		}
		var relation=$anchor.attr("rel");
		if(!relation){
			relation="async-get";
		}
		
		var rels=relation.split("-"),
			async=(rels[0]=="async"),
			type=((rels[1]=="post")?"post":"get"),
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
			eval( window[ cbBefore ] + ".call($anchor)" );
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
$(function(){
	$(document.body).onHMOClick( "a[ajaxify], input[ajaxify]", Ajaxify.onFired );
});
/* -- mod-ajaxify -- */

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
	<%--
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
	--%>
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
</c:if>

<script>
if(typeof Event == "undefined"){ Event = {}; } //<ie8

/*-- global js variables --*/
window.__a__=null;
window.__g__={"documentDomain":"${sunny.documentDomain}", "lobbyId":"${sunny.site.lobbySmallGroup.id}" };
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
	<sec:authentication var="authUserFriendSmallGroupId" property="principal.friendSmallGroupId" scope="request"/>
	<sec:authentication var="authUserMySmallGroupId" property="principal.mySmallGroupId" scope="request"/>
	<sec:authentication var="authUserDepartments" property="principal.departments" scope="request"/>
	<sec:authentication var="authUserUploadMaxSize" property="principal.uploadMaxSize" scope="request"/>
	window.__a__={ "is":true, "id":"${authUserId }", "profilePic":"${authUserProfilePic}" };
	<%-- window.__orgEventCount = ${currentInfo.notificationCount } + ${currentInfo.friendRequestCount } + ${currentInfo.messageCount }; --%>
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

</script>




<%--cordova 에서 들어왔을때만 실행된다.  --%>
<c:if test="${ sunny.cordova }">
	<script src="/assets/sunny/2.0/js/uncompressed/cordova.js"></script>
	
	<script>
	$(function(){
		document.addEventListener("deviceready",onDeviceReady,false);
	});
	function onDeviceReady(){
		<%-- 로그인에 성공했을때 한번만 실행되는 부분 --%>
		<c:if test="${not empty loginNow && loginNow == true }">
			cordova.exec(onSuccessLogin, onFailLogin, "SunnyCordovaPlugin", "onLogin", [""]);
		</c:if>
		
		
		
		cordova.exec(empty, empty, "SplashScreen", "hide", [""]);
		cordova.exec(empty, empty, "SunnyPreloaderPlugin", "hide", [""]);
		window.onbeforeunload = function(event) {
// 			navigator.notification.activityStart("", "로딩중 ...");
			cordova.exec(empty, empty, "SunnyPreloaderPlugin", "show", [""]);
		}
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
	  ga('create', 'UA-41965595-2', '${sunny.documentDomain }');
	  ga('send', 'pageview');
  });
</script>

<!-- Facebook Conversion Code for 헬로마이오피스랜딩 -->
<script>(function() {
  var _fbq = window._fbq || (window._fbq = []);
  if (!_fbq.loaded) {
    var fbds = document.createElement('script');
    fbds.async = true;
    fbds.src = '//connect.facebook.net/en_US/fbds.js';
    var s = document.getElementsByTagName('script')[0];
    s.parentNode.insertBefore(fbds, s);
    _fbq.loaded = true;
  }
})();
window._fbq = window._fbq || [];
window._fbq.push(['track', '6017424630886', {'value':'0.00','currency':'KRW'}]);
</script>
<noscript><img height="1" width="1" alt="" style="display:none" src="https://www.facebook.com/tr?ev=6017424630886&amp;cd[value]=0.00&amp;cd[currency]=KRW&amp;noscript=1" /></noscript>
