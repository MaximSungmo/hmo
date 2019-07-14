<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
/* -- mod-data-hover -- */
_$hovercard_anchor = null;

function hide_hovercard(){
	//
	$(window).unbind("scroll", on_window_scroll_hovercard);
	$(window.document).unbind( "mousemove", on_doc_mousemove_hovercard );
	//
	eval(_$hovercard_anchor.attr("data-fn-hovercard-hide")||"fn_empty()");
	//
	var $hovercard = $("#global-hover-card");
	$hovercard.find(".content").html("");
	$hovercard.removeClass("_t _b _r1 _r2 _r3 _t4 _r5 _ar _ac _al n-r");
	$hovercard.hide();
	//
	_$hovercard_anchor = null;
}

function on_window_scroll_hovercard(){
	hide_hovercard();
}

function on_doc_mousemove_hovercard(e){
	var
	$hovercard = $("#global-hover-card"),
	$hovercardContent = $hovercard.find(".content"),
	offsetA = _$hovercard_anchor.offset(),
	widthA = _$hovercard_anchor.width(),
	heightA = _$hovercard_anchor.height(),
	offsetH = $hovercard.offset(),
	widthH = $hovercardContent.width(),
	heightH = $hovercard.height();
	
	if( heightH == 0 ) {
		heightH = $hovercardContent.height() + 4;
	}
	
	// test pt
	var
	testPtInAnchor = ( offsetA.left <= e.pageX && e.pageX <= offsetA.left + widthA ) && ( offsetA.top <= e.pageY && e.pageY <= offsetA.top + heightA ),
	testPtInHovercard = ( offsetH.left <= e.pageX && e.pageX <= offsetH.left + widthH ) && ( offsetH.top <= e.pageY && e.pageY <= offsetH.top + heightH );
	
	if( !testPtInAnchor && !testPtInHovercard ){
		hide_hovercard();
	}
}

function anchor_mouseover(e){
	var 
	$hovercard=$("#global-hover-card"),
	$content = $hovercard.find(".content"); 
	if( $hovercard.is(":visible") ){
		return;
	}
	
	_$hovercard_anchor = $(this);

	// alignment
	var
	align = _$hovercard_anchor.attr("data-tooltip-alignh"),
	aclass = ( align == "right") ? "_ar" : (  ( align == "center") ? "_ac" : "_al" );
	$hovercard.addClass( aclass );
	
	//
	var
	label = _$hovercard_anchor.attr("aria-label");
	if( label != null ) {
		$content.append( "<p>" + label + "</p>" );
		$hovercard.addClass("_t _r3");
	} else {
		eval(_$hovercard_anchor.attr("data-fn-hovercard-show")||"fn_empty()");
	}

	// location
	var 
	$pt = $hovercard.find(".pt"),
	offset = _$hovercard_anchor.offset(),
	l, 
	t = offset.top + _$hovercard_anchor.height() + ( label != null ? 5 : 0 );
	
//	$.log( offset.top + ":" + _$hovercard_anchor.height() );
	
	if( aclass == "_al" ) {
		l = offset.left + _$hovercard_anchor.width()/2 - ( parseInt( $pt.css("left"), 10 ) +  $pt.find("span").width()/2 );
	} else if( aclass == "_ar" ) {
		l = offset.left + _$hovercard_anchor.width()/2 - $hovercard.width() + ( parseInt( $pt.css("right"), 10 ) +  $pt.find("span").width()/2 );
	} else if( aclass == "_ac" ) {
		l = offset.left + _$hovercard_anchor.width()/2 - ( parseInt( $pt.css("left"), 10 ) +  $pt.find("span").width() );
	}

	// show !
	$.log( t + ":" + l );
	$hovercard.css( {"left":l, "top":t } ).show();
	
	//
	var
	cb=_$hovercard_anchor.attr("ajaxify");
	if( cb != null && cb !== "" ){
		Ajaxify.onFired.call( this, e );
	}
	
	//
	$(window.document).bind( "mousemove", on_doc_mousemove_hovercard );
	$(window).bind( "scroll", on_window_scroll_hovercard );
}

$(function(){
	$(document).onHMOClick("[data-hover]", hide_hovercard );
	$(document).on( "mouseover", "[data-hover]", anchor_mouseover );
})
/* -- mod-data-hover -- */
</script>
<div class="tooltip" id="global-hover-card">
	<div class="pt"><span></span></div>
	<div class="content">
	</div>
</div>