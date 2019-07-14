/* -- mod-dialog -- */ 
var Dialog = {
	__core__: {
		_$trigger:null,
		_$control: null,
		doModal: function(){
			var
			_this = Dialog,
			__this = _this[ "__core__" ];
			
			if( this == window ) {
				var
				controlId = arguments[2];
				
				if( controlId == null ) {
					$.error( "Dialog:doModal[ Control not Assigned ]" );
					return;
				}
				__this._$trigger = $(window);
				__this._$control = $( "#" + controlId );
				__this._$control.attr( "data-style", arguments[2]  );
				__this._$control.attr( "data-title",  arguments[0] );
				__this._$control.find(".msg-text").html( arguments[1] );
			} else {
				__this._$trigger = $(this);
				__this._$control = $( "#" + ( __this._$trigger.attr("data-style") || __this._$trigger.attr("aria-control") ) );
			}
			
			if( __this._$control.length == 0 ) {
				$.error( "Dialog:doModal[ Control not Assigned ]" );
				return;
			}
			
			_this["__ui__"].activate();
		},
		endDialog: function(){
			Dialog["__ui__"].deactivate();
			this._$control = null;
			this._$trigger = null;
		},
		init: function(){
			$(document.body).append("<div class='ui-layer _l3 _4qx _3qw dialog-modal-layer' role='region' id='dialog-modal-layer'><div class='_l2'><div class='_l1'><div class='dialog-wrap'><div class='hmo-dialog' id='hmo-dialog' tabindex='-1' aria-hidden='false'><div class='hmo-dialog-header'><a href='javascript:;' class='hmo-dialog-button header-close close'>×</a><h3 id='hmo-dialog-title'></h3></div><div class='hmo-dialog-body'  id='hmo-dialog-body'></div><div class='hmo-dialog-footer' id='hmo-dialog-footer'><a data-handler='0' class='hmo-dialog-button hmo-button hmo-button-blue hmo-button-small-9 default' href='javascript:;'></a><a data-handler='0' class='hmo-dialog-button hmo-button hmo-button-white hmo-button-small-9 alt' href='javascript:;'></a></div></div></div></div></div></div><div id='messagebox-confirm' class='hmo-dialog-res'><div class='messagebox-content'><h3 class='msg-text'></h3></div></div><div id='messagebox-error' class='hmo-dialog-res'><div class='messagebox-content'><h3 class='msg-text'></h3></div></div><div id='messagebox-exclaim' class='hmo-dialog-res'><div class='messagebox-content'><h3 class='msg-text'></h3></div></div><div id='messagebox-inform' class='hmo-dialog-res'><div class='messagebox-content'><h3 class='msg-text'></h3></div></div><div id='messagebox-yesno' class='hmo-dialog-res'><div class='messagebox-content'><h3 class='msg-text'></h3></div></div>");
		},
	},
	__ui__: {
		_$win: null,
		_$globalWrapper: null,
		_$contentArea: null,
		_$dialogModalLayer: null,
		
		_$dialog: null,
		_$title: null,
		_$body: null,
		_$footer: null,
		
		_iscroll: null,		
		_theater: false,
		init: function(){
			// UI 관련된 엘리멘트 $객체로 셀렉트해서 미리 저장 
			this._$win = $( window );
			this._$globalWrapper = $( "#rw-snn-wrap" );
			this._$contentArea = $( "#main-container" );			
			this._$dialogModalLayer = $( "#dialog-modal-layer" );
			
			this._$dialog = $( "#hmo-dialog" );
			this._$title = $("#hmo-dialog-title");
			this._$body = $("#hmo-dialog-body");
			this._$footer = $("#hmo-dialog-footer");
		},
		activate: function(){
			this.enableModalLayer( true );
			this._render();
			this._$dialog.show();
		},
		deactivate: function(){
			Dialog[ "__core__" ]._$control.appendTo( $( document.body ) );
			Dialog[ "__core__" ]._$control.attr("data-title", "");
			Dialog[ "__core__" ]._$control.attr("data-style", "");
			Dialog[ "__core__" ]._$control.attr("data-init-fn", "");
			Dialog[ "__core__" ]._$control.attr("data-ok-fn", "");
			Dialog[ "__core__" ]._$control.attr("data-cancel-fn", "");
			Dialog[ "__core__" ]._$control.attr("data-yes-fn", "");
			Dialog[ "__core__" ]._$control.attr("data-no-fn", "");
			Dialog[ "__core__" ]._$control.attr("data-custom-fn", "");
			Dialog[ "__core__" ]._$control.attr("data-close-fn", "");

			this.enableModalLayer( false );

			var
			$defaultButton = this._$footer.find(".default"),
			$altButton = this._$footer.find(".alt");
			
			$defaultButton.text("");
			$defaultButton.removeClass("ok yes custom");
			$defaultButton.hide();
			$altButton.text("");
			$altButton.removeClass("cancel no");
			$altButton.hide();

			this._$dialog.hide();
		},
		enableModalLayer: function( enabled ){
			if( enabled ) {
				
				this._theater = false;
				
				if( !this._$globalWrapper.hasClass( "_theater" ) ) {	
					var st = this._$win.scrollTop();
					this._theater = true;
					this._$globalWrapper.addClass( "_theater" );
					this._$contentArea.css( { "top": -1*st } );
				}
				
				this._$dialogModalLayer.addClass( "_modal" );
				
			} else {
				
				if( this._theater ) {	
					this._$globalWrapper.removeClass("_theater");
					
					var st = parseInt( this._$contentArea.css( "top" ), 10 );
					this._$contentArea.css( { "top":0 } );
					this._$win.scrollTop( -1*st );
				}
				
				this._$dialogModalLayer.removeClass( "_modal" );
			}
			
		},
		_render: function(){
			var
			$control = Dialog["__core__"]._$control,
			$trigger = Dialog["__core__"]._$trigger;
			
			this._$title.text( $control.attr( "data-title" ) ||  $trigger.attr( "data-title" ) || "" );
			$control.appendTo( this._$body );
			
			var
			style = ( $control.attr( "data-style" ) || $trigger.attr( "data-style" ) || MB_CUSTOM ),
			$defaultButton = this._$footer.find(".default"),
			$altButton = this._$footer.find(".alt"); 

			if( style === MB_CONFIRMATION ){
				
				this._$body.find(".msg-text").html( $control.attr("data-message") || $trigger.attr("data-message") );
				$defaultButton.text("확인");
				$defaultButton.addClass("ok");
				$defaultButton.show();
				
				$altButton.text("취소");
				$altButton.addClass("cancel");
				$altButton.show();
				
			} else if( style === MB_ERROR ){
				var
				message = $control.attr("data-message") || $trigger.attr("data-message");
				message && this._$body.find(".msg-text").html( message );
				
				$defaultButton.text("확인");
				$defaultButton.addClass("ok");
				$defaultButton.show();
				
				$altButton.text("");
				$altButton.hide();
				
			} else if( style === MB_INFORMATION ){
				var
				message = $control.attr("data-message") || $trigger.attr("data-message");
				message && this._$body.find(".msg-text").html( message );
				
				$defaultButton.text("확인");
				$defaultButton.addClass("ok");
				$defaultButton.show();
				
				$altButton.text("");
				$altButton.hide();
				
			} else if( style === MB_EXCLAIMATION ){
				var
				message = $control.attr("data-message") || $trigger.attr("data-message");
				message && this._$body.find(".msg-text").html( message );
				
				this._$body.find(".msg-text").html( $control.attr("data-message") || $trigger.attr("data-message") );
				$defaultButton.text("확인");
				$defaultButton.addClass("ok");
				$defaultButton.show();
				
				$altButton.text("");
				$altButton.hide();
				
			} else if( style === MB_YESNO ){
				var
				message = $control.attr("data-message") || $trigger.attr("data-message");
				message && this._$body.find(".msg-text").html( message );
				
				this._$body.find(".msg-text").html( $control.attr("data-message") || $trigger.attr("data-message") );
				$defaultButton.text("예");
				$defaultButton.addClass("yes");
				$defaultButton.show();
				
				$altButton.text("아니오");
				$altButton.addClass("no");
				$altButton.show();
				
			} else if( style === MB_CUSTOM ){
				
				var
				fn,
				$caller,
				ctext = $control.attr( "data-custom-nm" ) || $trigger.attr( "data-custom-nm" );
				
				if( ctext != null && ctext !== "" ) {
					$defaultButton.text(ctext);
					$defaultButton.addClass("custom");
					$defaultButton.show();

					$altButton.text("취소");
					$altButton.addClass("cancel");
					$altButton.show();
				} else {
					$defaultButton.text("닫기");
					$defaultButton.addClass("close");
					$defaultButton.show();
					
					$altButton.text("");
					$altButton.hide();
				}
				
				// init content
				if( $control && $control.attr( "ajaxify-dialog-init" ) ){
					fn = "Ajaxify.onFired";
					$caller = $control;
				} else if( $trigger && $trigger.attr( "ajaxify-dialog-init" ) ) {
					fn = "Ajaxify.onFired";
					$caller = $trigger;
				} else if( $control && ( fn = $control.attr( "data-init-fn" ) ) ){
					$caller = $control.get(0);
				} else if( $control && ( fn = $trigger.attr( "data-init-fn" ) ) ){
					$caller = $trigger.get(0);
				}
				
				Dialog[ "__utils__"].evalFn( fn, $caller );

			} else {
			}
		}
	},
	__event__: {
		init: function(){
			$(".hmo-dialog-button").onHMOClick( null, Dialog["__event__"].onButtonClick );
		},
		onButtonClick: function( e ){
			var
			buttonNames = ["ok", "cancel", "yes", "no", "custom", "close" ],
			buttonNamesCount = buttonNames.length;
			$button = $(this),
			$control = Dialog["__core__"]._$control,
			$trigger = Dialog["__core__"]._$trigger;

			for( var i = 0; i < buttonNamesCount; i++ ) {
				if( !$button.hasClass( buttonNames[ i ] ) ) {
					continue;
				}
				
				var 
				fn,
				$caller,
				dataName = "data-" + buttonNames[ i ] + "-fn",
				ajaxifyName = "ajaxify-dialog-" + buttonNames[ i ];
				
				if( $trigger &&  ( fn = $trigger.attr( dataName ) ) ) {
					$caller = $trigger;
				} else if( $control && ( fn = $control.attr( dataName ) ) ) {
					$caller = $control;
				} else if( $control && $control.attr( ajaxifyName ) ){
					fn = "Ajaxify.onFired";
					$caller = $control;
				} else if( $trigger && $trigger.attr( ajaxifyName ) ) {
					fn = "Ajaxify.onFired";
					$caller = $trigger;
				}
				
				var result = Dialog[ "__utils__"].evalFn( fn, $caller );
				if( !result ) {
					Dialog[ "__core__" ].endDialog();
				}
				
				return;
			}
		}
	},
	__utils__: {
		evalFn: function( fn, caller ) {
			if( !fn ) {
				return;
			}
			if( window[ fn ] ){
				return window[ fn ].call( caller );
			} 
			
			return eval( fn + ".call( caller );" );
		}
	}
};

//Export Functions of Dialog' Internel Objects ( external access explicitly )
$.extend( Dialog, {
	init: function() {
		Dialog[ "__core__" ].init();
		Dialog[ "__ui__" ].init();
		Dialog[ "__event__" ].init();
	},
	doModal: Dialog["__core__"].doModal,
	enableModalLayer: Dialog["__ui__"].enableModalLayer,
	showMessage: function( title, message, style ){
		Dialog["__core__"].doModal.call( null, title, message, style );
	},
	onDialogTriggerFired: function( $e ) {
		$e.preventDefault();
		
		// 떠있는 팝업 없앰
		$("[data-popup-group]").each(function(){
			var $p = $(this);
			$p.is(":visible") && popup.hide($p);
		});
		//
		Dialog.doModal.call(this);
	}
});

//
var MB_CONFIRMATION = "messagebox-confirm";
var MB_ERROR = "messagebox-error";
var MB_INFORMATION = "messagebox-inform";
var MB_EXCLAIMATION = "messagebox-exclaim";
var MB_YESNO = "messagebox-yesno";
var MB_CUSTOM = "messagebox-custom";

// alias
var MessageBox = Dialog.showMessage;

// init
$(function(){
	Dialog.init();
	$(document.body).onHMOClick( "[role='dialog']", Dialog.onDialogTriggerFired );
});
/* -- mod-dialog -- */ 