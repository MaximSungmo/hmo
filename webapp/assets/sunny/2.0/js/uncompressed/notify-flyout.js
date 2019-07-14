/*
 * noti-flyout.js
 * 
 * depend on iscroll.js
 * 
 */
var NotifyFlyout = {
	noti: {
		$flyout: null,
		$toolButtonWrap: null,		/* _$ncvw */	
		$counter: null,				/* _$ncv */	
		$scroller: null,
		isDirty: true,
		dimensions: {
			leftPosition: -157,
			topNubLeft: 170,
			itemMaxWidth: 260
		},
		$list: null,
		__ejsTemplate: null,
		__isStreaming: false,
		__iscroll: null,
		init: function( deafultWidth, windowWidth ) {
			this.__ejsTemplate = new EJS( { url:"/assets/sunny/2.0/js/template/navbar-noti-list.ejs?v=1.0"  } );
			
			this.$flyout 		 = $( "#user-noti-flyout" );
			this.$toolButtonWrap = $( "#nav-noti-list" );
			this.$counter        = $( "#noti-count-value" );
			this.$scroller       = $( "#t-noti-s-area" );
			this.$list			 = $( "#t-noti-l" );
			
			var 
			w = deafultWidth,
			$topNub = $( "#t-noti-top-nub" ),
			$header = $( "#t-noti-h-inner" );
			
			if( __mobile__[ "is" ] ) {
				var
				parentLeft = this.$flyout.parent().offset().left;
				
				w = windowWidth - 2;
				
				this.dimensions[ "leftPosition" ] = -1 * parentLeft;
				this.dimensions[ "topNubLeft" ] = parentLeft + 12;
				this.dimensions[ "itemMaxWidth" ] = w - 60; 
			}
			
			this.$scroller.width( w );
			this.$flyout.css( "left", this.dimensions[ "leftPosition" ] );
			$header.width( w );
			$topNub.css( "left", this.dimensions[ "topNubLeft" ] );
			
			//
			if( this.$scroller.get(0).addEventListener ) {
				this.__iscroll = new iScroll( "t-noti-s-area", {
//					bounce:false,
//					momentum:false,
					onScrollEnd: this.onScrollEnd.bind( this )
				});
			} else {
				/* iscroll not used */
			}
		},
		clear: function(){
			this.$list.empty();

			var dataMap=$.parseJSON( this.$list.attr( "data-fw-map" ) );
			delete dataMap["cid"] ;
			this.$list.attr( "data-fw-map", JSON.stringify( dataMap ) );
//			
			dataMap=$.parseJSON( this.$list.attr( "data-bw-map" ) );
			delete dataMap["cid"] ;
			this.$list.attr( "data-bw-map", JSON.stringify( dataMap ) );
			
		},
		streaming: function( direction ) {
			
			if( this.__isStreaming ) {
				return;
			}
			
			this.__isStreaming = true;
			
			if( direction ){
			}
			
			var
			url   = this.$list.attr( "data-stream-url" ),
			mode  = this.$list.attr( "data-async" ) || "async-get",
			modes = mode.split("-"),
			async = ( modes[0] == "async" ),
			type  = ( modes[1] == "post" ) ? "post" : "get",
			data  = $.parseJSON( this.$list.attr( direction ? "data-fw-map" : "data-bw-map" ) );

			$.ajax({
				url: url  + "?rnd=" + Math.floor(Math.random() * 999999999),
				async: async,
				type: type,
				dataType: "json",
				contentType: "application/json",
				data: data,
				success: ( direction ? this.onFWStreaming : this.onBWStreaming ).bind( this ),
				error:function( jqXHR, textStatus, errorThrown ) {
					alert( "mod-streaming[" + this.$list.identify() + "]:" + errorThrown );
					$.error( "mod-streaming[" + this.$list.identify() + "]:" + errorThrown );
				},
				statusCode: {
					"401": function() {
						alert( "Unauthorized" );
					},
					"404": function() {
						alert( "Unkown" );
					}
				}	
			});			
		},
		onScrollEnd: function() {
			if( this.__iscroll.y == this.__iscroll.maxScrollY ) {
				this.streaming( false );
			}
		},
		onFWStreaming: function( result ) {
		},
		onBWStreaming: function( result ) {
			var data = result.data;
			if( !data || data.length == 0 ) {
				this.__isStreaming = false;
				return;
			}

			var
			l=data.length,
			html="",
			top = ( this.$list.children().length == 0 );

			for( var i=0; i< l; i++ ){
				html += this.__ejsTemplate.render( data[i] );
			}
			this.$list.append( html );
			
			setTimeout( function() {
				if( l > 0 ) {
					var dataMap;
					if( this.$list.children().length == 0 ) {
						dataMap=$.parseJSON( this.$list.attr( "data-fw-map" ) );
						dataMap["cid"] = data[0]["id"];
						this.$list.attr( "data-fw-map", JSON.stringify( dataMap ) );
					}
					dataMap=$.parseJSON( this.$list.attr( "data-bw-map" ) );
					dataMap["cid"]=data[l-1]["id"];
					
					this.$list.attr( "data-bw-map", JSON.stringify( dataMap ) );
				}
				
				this.__iscroll && this.__iscroll.refresh();
				timesince();
				this.__isStreaming = false;
				
			}.bind( this ), 1);			
		}
	},
	
	notice: {
		$flyout: null,
		$toolButtonWrap: null,		/* _$rcvw */	
		$counter: null,				/* _$rcv  */
		$scroller: null,		
		isDirty: true,
		dimensions: {
			leftPosition: -204,
			topNubLeft: 217,
			itemMaxWidth: 260
		},
		$list: null,		
		__ejsTemplate: null,
		__isStreaming: false,
		__iscroll: null,		
		init: function( deafultWidth, windowWidth ) {
			this.__ejsTemplate = new EJS( { url:"/assets/sunny/2.0/js/template/navbar-notice-list.ejs?v=1.0"  } );
			
			this.$flyout 		 = $( "#user-notice-flyout" );
			this.$toolButtonWrap = $( "#nav-notice-list" );
			this.$counter        = $( "#notice-count-value" );
			this.$scroller       = $( "#t-notice-s-area" );
			this.$list			 = $( "#t-notice-l" );
			
			var 
			w = deafultWidth,
			$topNub = $( "#t-notice-top-nub" ),
			$header = $( "#t-notice-h-inner" );
			
			if( __mobile__[ "is" ] ) {
				var
				parentLeft = this.$flyout.parent().offset().left;
				
				w = windowWidth - 2;
				
				this.dimensions[ "leftPosition" ] = -1 * parentLeft;
				this.dimensions[ "topNubLeft" ] = parentLeft + 12;
				this.dimensions[ "itemMaxWidth" ] = w - 60; 
			}
			
			this.$scroller.width( w );
			this.$flyout.css( "left", this.dimensions[ "leftPosition" ] );
			$header.width( w );
			$topNub.css( "left", this.dimensions[ "topNubLeft" ] );
			
			//
			if( this.$scroller.get(0).addEventListener ) {
				this.__iscroll = new iScroll( "t-notice-s-area", {
//					bounce:false,
//					momentum:false,
					onScrollEnd: this.onScrollEnd.bind( this )
				});
			} else {
				/* iscroll not used */
			}
		},
		clear: function(){
			this.$list.empty();

			var dataMap=$.parseJSON( this.$list.attr( "data-fw-map" ) );
			delete dataMap["cid"] ;
			this.$list.attr( "data-fw-map", JSON.stringify( dataMap ) );
//			
			dataMap=$.parseJSON( this.$list.attr( "data-bw-map" ) );
			delete dataMap["cid"] ;
			this.$list.attr( "data-bw-map", JSON.stringify( dataMap ) );
			
		},
		streaming: function( direction ) {
			
			if( this.__isStreaming ) {
				return;
			}
			
			this.__isStreaming = true;
			
			if( direction ){
				//_$tnnm.hide();
				//_$tnld.show();
			}
			
			var
			url   = this.$list.attr( "data-stream-url" ),
			mode  = this.$list.attr( "data-async" ) || "async-get",
			modes = mode.split("-"),
			async = ( modes[0] == "async" ),
			type  = ( modes[1] == "post" ) ? "post" : "get",
			data  = $.parseJSON( this.$list.attr( direction ? "data-fw-map" : "data-bw-map" ) );

			$.ajax({
				url: url  + "?rnd=" + Math.floor(Math.random() * 999999999),
				async: async,
				type: type,
				dataType: "json",
				contentType: "application/json",
				data: data,
				success: ( direction ? this.onFWStreaming : this.onBWStreaming ).bind( this ),
				error:function( jqXHR, textStatus, errorThrown ) {
					alert( "mod-streaming[" + this.$list.identify() + "]:" + errorThrown );
					$.error( "mod-streaming[" + this.$list.identify() + "]:" + errorThrown );
				},
				statusCode: {
					"401": function() {
						alert( "Unauthorized" );
					},
					"404": function() {
						alert( "Unkown" );
					}
				}	
			});			
		},
		onScrollEnd: function() {
			if( this.__iscroll.y == this.__iscroll.maxScrollY ) {
				this.streaming( false );
			}
		},
		onFWStreaming: function( result ) {
		},
		onBWStreaming: function( result ) {
			var data = result.data;
			if( !data || data.length == 0 ) {
				this.__isStreaming = false;
				return;
			}

			var
			l=data.length,
			html="",
			top = ( this.$list.children().length == 0 );

			for( var i=0; i< l; i++ ){
				html += this.__ejsTemplate.render( data[i] );
			}
			this.$list.append( html );
			
			setTimeout( function() {
				if( l > 0 ) {
					var dataMap;
					if( this.$list.children().length == 0 ) {
						dataMap=$.parseJSON( this.$list.attr( "data-fw-map" ) );
						dataMap["cid"] = data[0]["id"];
						this.$list.attr( "data-fw-map", JSON.stringify( dataMap ) );
					}
					dataMap=$.parseJSON( this.$list.attr( "data-bw-map" ) );
					dataMap["cid"]=data[l-1]["id"];
					
					this.$list.attr( "data-bw-map", JSON.stringify( dataMap ) );
				}
				
				this.__iscroll && this.__iscroll.refresh();
				timesince();
				this.__isStreaming = false;
				
			}.bind( this ), 1);			
		}
	},
	
	message: {
		$flyout: null,
		$toolButtonWrap: null,		/* _$rcvw */	
		$counter: null,				/* _$rcv  */
		$scroller: null,		
		isDirty: true,
		dimensions: {
			leftPosition: -251,
			topNubLeft: 264,
			itemMaxWidth: 260
		},
		$list: null,		
		__ejsTemplate: null,
		__isStreaming: false,
		__iscroll: null,		
		init: function( deafultWidth, windowWidth ) {
			this.__ejsTemplate = new EJS( { url:"/assets/sunny/2.0/js/template/navbar-message-list.ejs?v=1.0"  } );
			
			this.$flyout 		 = $( "#user-message-flyout" );
			this.$toolButtonWrap = $( "#nav-message-list" );
			this.$counter        = $( "#message-count-value" );
			this.$scroller       = $( "#t-message-s-area" );
			this.$list			 = $( "#t-message-l" );
			
			var 
			w = deafultWidth,
			$topNub = $( "#t-message-top-nub" ),
			$header = $( "#t-message-h-inner" );
			
			if( __mobile__[ "is" ] ) {
				var
				parentLeft = this.$flyout.parent().offset().left;
				
				w = windowWidth - 2;
				
				this.dimensions[ "leftPosition" ] = -1 * parentLeft;
				this.dimensions[ "topNubLeft" ] = parentLeft + 12;
				this.dimensions[ "itemMaxWidth" ] = w - 60; 
			}
			
			this.$scroller.width( w );
			this.$flyout.css( "left", this.dimensions[ "leftPosition" ] );
			$header.width( w );
			$topNub.css( "left", this.dimensions[ "topNubLeft" ] );
			
			//
			if( this.$scroller.get(0).addEventListener ) {
				this.__iscroll = new iScroll( "t-message-s-area", {
//					bounce:false,
//					momentum:false,
					onScrollEnd: this.onScrollEnd.bind( this )
				});
			} else {
				/* iscroll not used */
			}
		},
		clear: function(){
			this.$list.empty();

			var dataMap=$.parseJSON( this.$list.attr( "data-fw-map" ) );
			delete dataMap["cid"] ;
			this.$list.attr( "data-fw-map", JSON.stringify( dataMap ) );
//			
			dataMap=$.parseJSON( this.$list.attr( "data-bw-map" ) );
			delete dataMap["cid"] ;
			this.$list.attr( "data-bw-map", JSON.stringify( dataMap ) );
			
		},
		streaming: function( direction ) {
			
			if( this.__isStreaming ) {
				return;
			}
			
			this.__isStreaming = true;
			
			if( direction ){
				//_$tnnm.hide();
				//_$tnld.show();
			}
			
			var
			url   = this.$list.attr( "data-stream-url" ),
			mode  = this.$list.attr( "data-async" ) || "async-get",
			modes = mode.split("-"),
			async = ( modes[0] == "async" ),
			type  = ( modes[1] == "post" ) ? "post" : "get",
			data  = $.parseJSON( this.$list.attr( direction ? "data-fw-map" : "data-bw-map" ) );

			$.ajax({
				url: url  + "?rnd=" + Math.floor(Math.random() * 999999999),
				async: async,
				type: type,
				dataType: "json",
				contentType: "application/json",
				data: data,
				success: ( direction ? this.onFWStreaming : this.onBWStreaming ).bind( this ),
				error:function( jqXHR, textStatus, errorThrown ) {
					alert( "mod-streaming[" + this.$list.identify() + "]:" + errorThrown );
					$.error( "mod-streaming[" + this.$list.identify() + "]:" + errorThrown );
				},
				statusCode: {
					"401": function() {
						alert( "Unauthorized" );
					},
					"404": function() {
						alert( "Unkown" );
					}
				}	
			});			
		},
		onScrollEnd: function() {
			if( this.__iscroll.y == this.__iscroll.maxScrollY ) {
				this.streaming( false );
			}
		},
		onFWStreaming: function( result ) {
		},
		onBWStreaming: function( result ) {
			var data = result.data;
			if( !data || data.length == 0 ) {
				this.__isStreaming = false;
				return;
			}

			var
			l=data.length,
			html="",
			top=( this.$list.children().length == 0);

			for( var i=0; i< l; i++ ){
				html += this.__ejsTemplate.render( data[i] );
			}
			this.$list.append( html );
			
			setTimeout( function() {
				if( l > 0 ) {
					var dataMap;
					if( this.$list.children().length == 0 ) {
						dataMap=$.parseJSON( this.$list.attr( "data-fw-map" ) );
						dataMap["cid"] = data[0]["id"];
						this.$list.attr( "data-fw-map", JSON.stringify( dataMap ) );
					}
					dataMap=$.parseJSON( this.$list.attr( "data-bw-map" ) );
					dataMap["cid"]=data[l-1]["id"];
					
					this.$list.attr( "data-bw-map", JSON.stringify( dataMap ) );
				}
				
				this.__iscroll && this.__iscroll.refresh();
				timesince();
				this.__isStreaming = false;
				
			}.bind( this ), 1);			
		}
	},
	
	// common
	isFlyout: false,
	__$w: null,
	__$contentArea: null,
	__$appMain: null,
	__scrollTop: 0,
	__defaultScrollableAreaWidth: 330,
	__scrollableAreaHeight: 300,
	__enableWheel: true,
	init: function(){
		this.__$w = $( window );
		this.__$appMain = $( "#rw-snn-main" );
		this.__$contentArea = $( ".page-content" );
		
		var
		ww = this.__$w.width();
		
		this.noti.init( this.__defaultScrollableAreaWidth, ww );
		this.notice.init( this.__defaultScrollableAreaWidth, ww );
		this.message.init( this.__defaultScrollableAreaWidth, ww );
		//
		
		$(".ui-popup-close").onHMOClick( null, this.onCloseButtonClicked );
	},
	onCloseButtonClicked: function( $event ) {
		var $button = $(this);
		popup.hide( $( "#" + $button.attr( "aria-owns" ) ) );
	},
	onHide: function( which ) {
		var
		__this = NotifyFlyout,
		flyout = __this[ this.attr( "data-which" ) ]; 

		if( __mobile__[ "is" ] ) {
			// support Jelly Bean
			if( __mobile__[ "android" ] && __mobile__[ "version" ] > 4.0 ){
				__this.__$appMain.css({ "height": "auto", "overflow": "auto" });
				__this.__$contentArea.css( "top", 0 );
				__this.__$w.scrollTop( __this.__scrollTop );
			} 
		}
		
		//
		__this.isFlyout = false;
	},
	onShow: function() {
		var 
		__this = NotifyFlyout,
		flyout = __this[ this.attr( "data-which" ) ]; 

		__this.isFlyout = true;

		if( __mobile__[ "is" ] ) {
			// support Jelly Bean
			if( __mobile__[ "android" ] && __mobile__[ "version" ] > 4.0 ){
				__this.__scrollTop = __this.__$w.scrollTop();
				//__this.__$contentArea.css( "top", -1*this.__scrollTop );
				__this.__$appMain.css( "overflow", "hidden" );
				__this.__$appMain.css( "height", __this.__$w.height() );
				//this.__$w.scrollTop( 0 );
			} 
		}
		
		flyout.$toolButtonWrap.removeClass( "_new" );
		flyout.$counter.text( 0 );
		document.title = __orgTitle ;
		if( !flyout.isDirty ) {
			return;
		}

		//flyout.$list.empty();
		flyout.clear();
		flyout.isDirty = false;
		__this.recalcLayout( flyout.$scroller );

		flyout.streaming( false );
	},
	recalcLayout: function( $element ) {
		if( __mobile__[ "is" ] ) {
			var wh = this.__$w.height();
			this.__scrollableAreaHeight = wh - ( 32 + 33 + 45 - 3 );
		}
		$element.height( this.__scrollableAreaHeight );		
	},
	onAjaxRead: function( data ) {
		$.log( data );
		
		if( data.result == "fail"){
			MessageBox( "알림", "알림 내용을 읽는 중 다음과 같은 에러가 발생했습니다.<br>" +  data.message, MB_ERROR );
			return;
		}
		var $anchor = this;
		$.log( $anchor.attr( "href" ) );
		$anchor.closest( ".notification" ).removeClass( "unread" );	
	},
	doRead: function( anchor ) {
		if( $(anchor).closest( ".notification" ).hasClass("unread") == false ){
			window.location.href = $( anchor ).attr( "href" );	
			return false; 
		}
		var $anchor = $( anchor );
		$.ajax({
			url : "/notification/make_read" + "?rnd=" + Math.floor(Math.random() * 999999999),
			type: "get",
			dataType: "json",
			contentType: 'application/json',
			data: $anchor.data( "request-map" ),
			success: NotifyFlyout.onAjaxRead.bind( $anchor )
		});		
		window.location.href = $anchor.attr( "href" );	
	}
};