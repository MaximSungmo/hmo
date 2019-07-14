/**
 *	StoryWriter
 * 
 *	@update 2013.08.30 [1.0]
 *	@update 2013.12.10 [new for sunny2.0 ]
 *  @update 2014.02.28 [editable version ]
 *   
 *	@author Dae H. Ahn ( kickscar@sunnyvale.co.kr )
 *
 *	StoryWriter Launcher Plugin Function ( Based on jQuery Extension )  
 * 
 */

//
// 
//
$( function() {
	$.fn.storywriter = function( config ) {
		var count = this.length;
		for( var i = 0; i < count; i++ ) {
			StoryWriter[ "__core__" ].init( this[ i ] );
		}
	};
});

var StoryWriter = {
	/* CORE */
	__core__: {
		__POSTFIX_MIRROR_ID__: "-writer-mirror",
		init: function( element ) {
			// for mirroring
			$textarea = $( element );
			$( "<textarea/>", {
				"id": $textarea.identify() + this.__POSTFIX_MIRROR_ID__,
				"class": "textarea-mirroring story-mirroring",
				"tabindex": -1	
			}).
			appendTo( $("body") ).
			css( "width", $textarea.width() );
			
			if( $textarea.css('box-sizing')==="border-box" || 
				$textarea.css('-moz-box-sizing')==="border-box" || 
				$textarea.css('-webkit-box-sizing')==="border-box" ) {
				StoryWriter["__ui__"].__inputOffset = $textarea.outerHeight() - $textarea.height();
			}
			
			$textarea.
			attr( "aria-mirror", $textarea.identify() + this.__POSTFIX_MIRROR_ID__ ).
			keydown(  StoryWriter[ "__event__" ].onKeydown ).
			focus( StoryWriter[ "__event__" ].onFocus ).
			blur( StoryWriter[ "__event__" ].onBlur );
			
			var ta = $textarea.get(0);
			if( "onpropertychange" in ta ) {
				if( "oninput" in ta) {
			    	ta.oninput = StoryWriter[ "__ui__" ].adjust;
			    	$textarea.keydown( function(){ 
			    		setTimeout( StoryWriter[ "__ui__" ].adjust.bind( $textarea ), 50 ); 
			    	} );
				} else {
					ta.onpropertychange =  StoryWriter[ "__ui__" ].adjust;
			    }
			} else {
			    ta.oninput =  StoryWriter[ "__ui__" ].adjust;
			}
			
//			Init Listener
//			var lengthListner = StoryWriter[ "__event__" ].__inputListeners.length;
//			for( var i = 0; i < length; i++ ) {
//				var inputListener = StoryWriter[ "__event__" ].__inputListeners[ i ];
//				inputListener.init && inputListener.init( $textarea );
//			}
		}
	},
	/* UI */
	__ui__: {
		UNFOLDING_HEIGHT: 74,	
		INPUT_HEIGHT_OFFSET: 10,
		__inputMinHeight: 0,	
		__inputOffset: 0,
		__adjust_active: false,
		testEnter: function() {
			var
			$ta = $(this),
			$mirror = $( "#" + $ta.attr( "aria-mirror" ) ),
			text = $ta.val(),
			mirror = $mirror.get(0),
			original = $ta.height(),
			height;

			$mirror.val( text + "\n" );
			mirror.scrollTop = 0;
			mirror.scrollTop = 9e4;
			height = mirror.scrollTop + StoryWriter["__ui__"].__inputOffset;
			
			if( original !== height ) {
				if( height < StoryWriter[ "__ui__" ].__inputMinHeight ) {
					height = StoryWriter[ "__ui__" ].__inputMinHeight;			
				}
				$ta.css( "height", height );
			}
			
			$mirror.val( text );
		},
		adjust: function() {
			if( StoryWriter["__ui__"].__adjust_active ) { 
				return; 
			}
			
			var
			$ta = $( this ),
			$mirror = $( "#" + $ta.attr( "aria-mirror" ) ),
			mirror = $mirror.get(0),
			original = $ta.height(),
			height;

			StoryWriter["__ui__"].__adjust_active = true;
			$mirror.val( $ta.val() );
			
			mirror.scrollTop = 0;
			mirror.scrollTop = 9e4;
			height = mirror.scrollTop + StoryWriter[ "__ui__" ].__inputOffset;
			if( original !== height ) {
				if( height < StoryWriter[ "__ui__" ].__inputMinHeight ) {
					height = StoryWriter[ "__ui__" ].__inputMinHeight;			
				}
				$ta.css( "height", height );
			}
			
			setTimeout( function() { 
				StoryWriter["__ui__"].__adjust_active = false; 
			}, 10);
		},
		rect: function(){
			$ta = $( this );
			return { 
				left: 0, 
				top: 0, 
				right: $ta.width(), 
				bottom: $ta.height() 
			};
		}
	},
	/* EVENT */
	__event__: {
		__keyContextLookupInterval: null,
		__inputListeners: [],
		attachInputListener: function( inputListener ) {
			var index = this.__inputListeners.length;
			if( inputListener && inputListener.onContext ) {
				this.__inputListeners[ index ] = inputListener;
				this.__inputListeners[ index ].onAttached && this.__inputListeners[ index ].onAttached();
			}
		},
		onFocus: function( event ) {
			var $ta = $( this );
			if( $ta.val() == "" ) {
				$ta.css( "height", StoryWriter["__ui__"].UNFOLDING_HEIGHT );
				StoryWriter[ "__ui__" ].__inputMinHeight = $ta.height() + StoryWriter[ "__ui__" ].INPUT_HEIGHT_OFFSET;
			}
			
			StoryWriter[ "__event__" ].__keyContextLookupInterval || StoryWriter[ "__event__" ].onContext.call( $ta );
		},
		onBlur: function( event ) {
			var $ta = $( this );
			if( StoryWriter[ "__event__" ].__keyContextLookupInterval ) {
	            window.clearInterval( StoryWriter[ "__event__" ].__keyContextLookupInterval );
	            StoryWriter[ "__event__" ].__keyContextLookupInterval = null;
			}
		},
		onKeydown: function( event ) {
			var $ta = $(this);
			
			if( event.keyCode == 13 ) { /* enter */
				StoryWriter[ "__ui__" ].testEnter.call( $ta );
				return;
			}
			
			if(event.keyCode == 27) { /* esc */ 
				$ta.blur(); 
				return; 
			}
			
			if( event.ctrlKey && event.keyCode == 86 ) { /* ctrl + v */
				setTimeout( function(){
					StoryWriter[ "__event__" ].onPaste.call( $ta );
				}, 100);
			}			
		},
		onPaste: function() {
			var $ta = $( this ),
				ta = $ta.get(0),
				offset = 0;

			if( ta.selectionStart ) {
				offset = ta.selectionStart;
			} else if( ta.ownerDocument.selection ) {
				var range = ta.ownerDocument.selection.createRange();
				if( range ) {
					var textrange = ta.createTextRange();
					var textrange2 = textrange.duplicate();
					textrange.moveToBookmark( range.getBookmark() );
					textrange2.setEndPoint( 'EndToStart', textrange );
					offset = textrange2.text.length;
				}
			}
			
			//$.log( "++++++++++++ :" + offset );
			//
			var countListeners = StoryWriter[ "__event__" ].__inputListeners.length;
			for( var i = 0; i < countListeners; i++ ) {
				StoryWriter[ "__event__" ].__inputListeners[ i ].onPaste && StoryWriter[ "__event__" ].__inputListeners[ i ].onPaste( $ta, $ta.val(), offset );
			}			
		},
		onContext: function() {
			//
			StoryWriter[ "__event__" ].__keyContextLookupInterval && window.clearInterval( StoryWriter[ "__event__" ].__keyContextLookupInterval );
			
			//
			var $ta = $( this ),
				ta = $ta.get(0),
				offset = 0;
			
			if( ta.selectionStart ) {
				offset = ta.selectionStart;
			} else if( ta.ownerDocument.selection ) {
				var range = ta.ownerDocument.selection.createRange();
				if( range ) {
					var textrange = ta.createTextRange();
					var textrange2 = textrange.duplicate();

					textrange.moveToBookmark( range.getBookmark() );
					textrange2.setEndPoint( 'EndToStart', textrange );
					offset = textrange2.text.length;
				}
			}
			
			//$.log( "++++++++++++ :" + offset );
			//
			var countListeners = StoryWriter[ "__event__" ].__inputListeners.length;
			for( var i = 0; i < countListeners; i++ ) {
				StoryWriter[ "__event__" ].__inputListeners[ i ].onContext && StoryWriter[ "__event__" ].__inputListeners[ i ].onContext( $ta, $ta.val(), offset );
			}			
			
		    //
			StoryWriter[ "__event__" ].__keyContextLookupInterval = window.setInterval( StoryWriter[ "__event__" ].onContext.bind( $ta ), 5 );
		}
	}
}


//
//Export Functions of StoryWriter's Internel Objects ( external access explicitly )
//
$.extend( StoryWriter, {
	adjust: StoryWriter[ "__ui__" ].adjust,
	attachInputListener: StoryWriter[ "__event__" ].attachInputListener.bind( StoryWriter[ "__event__" ] ),
	rect: StoryWriter[ "__ui__" ].rect
} );