/**
 *	YAEditor 0.6.1
 * 
 *	@update 2013.03.30
 *	@author Dae H. Ahn ( kickscar@sunnyvale.co.kr )
 * 
 */


//
// YAEditor Launcher Plugin Function ( Based on jQuery Extension )
//
( function( $ ) {
	$.fn.yaEditor = function( config ) {
		
		/* check rangy library */
		if( !rangy ){  
			$.error( "Rangy Library Not Imported" );
			return;
		}
		
		/* singleton */
		if( this.length != 1 ) {
			$.error( "1 More Element or None Selected. Not Applied" );
			return;
		}

		/* set logging level */	
		logger.level( config[ "loggingLevel" ] );

		/* configuaration */
		YAEditor.configuration( 
			config[ "inputAreaDoc" ] /*input area document url*/ );
		
		/* plugins */
		var countPlugins = config.plugins.length;
		for( var i = 0; i < countPlugins; i++ ) {
			var name = config.plugins[ i ];
			var plugin = window[ name ];
			
			if( !plugin ){
				continue;
			}
			
			if( plugin.type == "command" ) {
				YAEditor.attachCommandPlugin( plugin );
			} else if( plugin.type == "listener" ) {
				YAEditor.attachInputListener( plugin );
			} else {
				$.warn( "unknown type of plugin id:" + plugin.id );
			} 
		}

		/* initialize */
		YAEditor[ "__core__" ].init( this[ 0 ] );
	};
})( jQuery );


//
// YAEditor Singleton Object
//
var YAEditor = {
	///////////////////////////////////////////////////////////////////////////
	/* CORE */
	__core__: {
		isDirty: false,
		inputAreaDoc: "",
		init: function( element ) {
			var $element = $( element ),
				$toolsElement = $element.children( ".tools" );
				$editorElement = $element.children( ".editor" );
				editorElement = $editorElement.get( 0 );
			
			editorElement || $.error( "YAEditor.init :: Editor Element that has class editor Can Not Found" );

			// css set
			$element.addClass( "ya-editor" );
			
			// create iframe
			$iframe = $( "<iframe/>", {
				id:  YAEditor["__util__"].PREFIX + "_" + YAEditor["__util__"].ID_IFRAME,
				src: this.inputAreaDoc
			} ).attr( "frameBorder", "0" ).appendTo( editorElement );
			
			// create textarea for storage
			/*
			$( "<textarea/>", {
				id:  YAEditor["__util__"].PREFIX + "_" + YAEditor["__util__"].ID_TEXTAREA
			} ).appendTo( editorElement );
			*/
			
			// dimentsion set
			var height = $element.height(),
				heightTools = $toolsElement.height(),
				width = $element.width(),
				paddingTop = parseInt( $editorElement.css( "padding-top" ), 10 ),
				paddingBottom = parseInt( $editorElement.css( "padding-bottom" ), 10 ),
				paddingRight = parseInt( $editorElement.css( "padding-right" ), 10 ),
				paddingLeft = parseInt( $editorElement.css( "padding-left" ), 10 );
			
			$editorElement.css({
				width: ( width - paddingLeft - paddingRight ) + "px",
				height: ( height - heightTools - paddingTop - paddingBottom ) + "px"
			});
			
			// tool's button event handler
			if( $toolsElement.length > 0 ) {
				var $buttons = $toolsElement.find( "button" );
				$buttons.click( YAEditor[ "__event__" ].onButtonClick );
				$buttons.mousedown( YAEditor[ "__event__" ].onButtonMousedown );
				$buttons.mouseup( YAEditor[ "__event__" ].onButtonMouseup );
				$buttons.mouseover( YAEditor[ "__event__" ].onButtonMouseover );
				$buttons.mouseout( YAEditor[ "__event__" ].onButtonMouseout );
			}
			
			// onload event mapping 
			var iframe = $iframe.get( 0 );
			if ( iframe.attachEvent ){
			    iframe.attachEvent( "onload", YAEditor[ "__event__" ].onIframeLoaded.bind( YAEditor["__event__"] ) );
			} else {
			    iframe.onload = YAEditor[ "__event__" ].onIframeLoaded.bind( YAEditor["__event__"] );
			}
		},
		loadData: function( htmls ) {
			YAEditor[ "__util__" ].$textarea().val( htmls );
			this.updateData( false );
		},
		updateData: function( mode ) {
			var html = "",
				$iframe = YAEditor[ "__util__" ].$iframe(),
				$textarea = YAEditor[ "__util__" ].$textarea(),
				$body = $iframe.contents().find( "body" );
			if( mode == true ) { /* save */
				$textarea.html( $body.html() );
			} else { /* load */
				$body.html( $textarea.val() );
			}

			this.isDirty = mode;
		},
		html: function( html, directive, ch ) {
			var iframe = YAEditor[ "__util__" ].$iframe().get( 0 ), 
				doc = iframe.contentWindow.document,
				selection, 
				range,
				container;
			
			if( directive == "after" ) {
				
				if( !ch || ch == "" ) {
					logger.error( "YAEdit.command.html : marker character not assigned" );
					return;
				}
				
				selection = rangy.getIframeSelection( iframe ); 
				range = selection.getRangeAt( 0 );
				container = range.commonAncestorContainer;
	
				var startOffset = -1,
			    	endOffset = range.startOffset,
			    	innerText = $( container ).text();
			    
				for( var i = endOffset - 1; i >= 0 ; i-- ) {
					var charAt = innerText.charAt( i );
					if( charAt == ch ) {
						startOffset = i;
						break;
					}
				}
				
				if( startOffset == -1 ) {
					logger.error( "YAEdit.command.html : Can Not Set Start Offset ["  + ch + "]" );
					return;
				}
				
		        //
				range.setStart( container, startOffset );
				range.setEnd( container, endOffset );
				selection.removeAllRanges();
				selection.addRange(range);
			
			} else if( directive == "before" ) {
				if( !ch || ch == "" ) {
					logger.error( "YAEdit.command.html : marker character not assigned" );
					return;
				}
			} else if( !directive || directive == "current" ) {
				/* do nothing */
			} else {
				logger.warn( "YAEdit.command.html : directive not support " );
				return;
			}
			
			// insert or repalce
			selection = rangy.getIframeSelection( iframe ), 
			range  = selection.getRangeAt( 0 );
			
        	range.deleteContents();
	        	
            var el = doc.createElement( "div" ),
            	frag = doc.createDocumentFragment(), 
            	node = null, 
            	lastNode = null;
            
            el.innerHTML = html;
            while ( ( node = el.firstChild ) ) {
                lastNode = frag.appendChild( node );
            }
	            
            range.insertNode( frag );

            // Preserve the selection
            if ( lastNode ) {
                range = range.cloneRange();
                range.setStartAfter( lastNode );
                range.collapse( true );
                selection.removeAllRanges();
                selection.addRange( range );
            }
		}		
	},
	
	
	
	///////////////////////////////////////////////////////////////////////////
	/* UI */
	__ui__: {
		focus: function() {
			var $iframe = YAEditor[ "__util__" ].$iframe(),
				iframe = $iframe.get( 0 ),
				w = iframe.contentWindow;
			
			// with ff, wiondow's focusing must be set before
			if( $.browser.mozilla ) {
				w.focus();
				return;
			}
			
			var doc = w.document,
				body = doc.body;
			if( !body ) {
				return;
			}

			// 포커싱 on almost browser
			body.focus();

			//
//			var selection = rangy.getIframeSelection( iframe ),
//				range = selection.getRangeAt( 0 ),
//				container = range.commonAncestorContainer;
//			
//			range.setStart( container, 0 );
//			range.setEnd( container, 0 );		
//			selection.removeAllRanges();
//			selection.addRange( range );			
		},
		rectCaret: function( directive, ch ) {
			
			var $iframe = YAEditor[ "__util__" ].$iframe(),
				iframe = $iframe.get( 0 ),
				selection = rangy.getIframeSelection( iframe ),
				range = selection.getRangeAt( 0 ),
				container = range.commonAncestorContainer,
				startOffset = -1,
				endOffset = range.startOffset,
				rect;
			
			if( directive == "after" ) {

				if( !ch || ch == "" ) {
					logger.error( "YAEdit.__ui__.rectCaret : marker character not assigned" );
					return;
				}
				
				var text = $( container ).text();
				for( var i = endOffset - 1; i >= 0 ; i-- ) {
					var charAt = text.charAt( i );
					if( charAt == ch ) {
						startOffset = i;
						break;
					}
				}
				
				if( startOffset == -1 ) {
					logger.error( "YAEdit.__ui__.rectCaret : Can Not Set Start Offset ["  + ch + "]" );
					return;
				}
				
		        //
				range.setStart( container, startOffset );
				range.setEnd( container, startOffset );
			
			} else if( directive == "before" ) {
				if( !ch || ch == "" ) {
					logger.error( "YAEdit.command.html : marker character not assigned" );
					return;
				}
			} else if( !directive || directive == "current" ) {
				/* do nothing */
			} else {
				logger.warn( "YAEdit.command.html : directive not support " );
				return;
			}			
			
			// get rect
			try {
				if( $.browser.webkit ) {
					var startPos = selection.getStartDocumentPos(),
						endPos = selection.getEndDocumentPos();
					rect = {
						left: startPos.x,
						top: startPos.y,
						width: endPos.x - startPos.x,
						height: endPos.y - startPos.y
					};
				} else {
					rect = selection.getBoundingDocumentRect();
				}
			} catch( e ) {
				
				$.log( "rectCaret:" + e );
				/* neglect */
			}
	
			range.setStart( container, endOffset );
			range.setEnd( container, endOffset );
			
			//
			var offset = $iframe.offset();
			rect.top += offset.top;
			rect.left += offset.left;
			
			return rect;
		}
	},

	
	
	///////////////////////////////////////////////////////////////////////////
	/* EVENT */
	__event__: {
		cache: {},
		inputListeners: [],
		commandPlugins: [],
		attachCommandPlugin: function( plugin ){
			var index = this.commandPlugins.length;
			this.commandPlugins[ index ] = plugin;
		},
		attachInputlistener: function( inputListener ) {
			var index = this.inputListeners.length;
			if( inputListener && inputListener.onContext ) {
				this.inputListeners[ index ] = inputListener;
				this.inputListeners[ index ].onInit && this.inputListeners[ index ].onInit();
			}
		},
		routeCommand: function( elementId, eventName ) {
			// capitalize
			var handlerName = "on" + eventName.replace( /^(.)|\s(.)/g, function( $1 ){
				return $1.toUpperCase( );
			});
			
			//
			var countPlugins = this.commandPlugins.length;
			for( var i = 0; i < countPlugins; i++ ) {
				var plugin = this.commandPlugins[ i ];
				if( !plugin[ handlerName ] ) {
					continue;
				}
				
				var result = plugin[ handlerName ].call( plugin, $( "#" + elementId ) );
				if( result ) {
					return;
				}
			}			
		},
		onIframeLoaded: function() {
			try {
				logger.info( "input area document loaded successfully" );
	
				var $iframe = YAEditor["__util__"].$iframe(),
					$txarea = YAEditor["__util__"].$textarea(),
					$doc  = $iframe.contents(),
					$body = $doc.find( "body" );
	
				$doc.attr( "designMode", "on" );						// Almost Browser
//				$doc.find( "body" ).attr( "contentEditable", true );	// Only for HTML5
				
				// focus, blur event handler
				if( $.browser.mozilla ) {
					( $doc || $iframe ).focus( this.onFocus.bind( this ) );
					( $doc || $iframe ).blur( this.onBlur.bind( this ) );
				} else {
					$body.focus( this.onFocus.bind( this ) );
					$body.blur( this.onBlur.bind( this ) );
					$doc.mousedown( this.onDocumentMousedown.bind( this ) );
				}
				
				// keypress event handler
				// $doc.contents().keypress( YAEditor[ "__event__" ].onKeypress );
	
				// Load Data
				if( $txarea.html() !== "" ) {
					YAEditor[ "__core__" ].updateData( false );
				} 
				
				/* First Focusing ( ff에서느는 딜레이 타임이 필요 ) */
				// window.setTimeout( function() {
				//	YAEditor[ "__ui__" ].focus();
				// }, 1);
				
			} catch( e ) {
			}
		},
		onFocus: function() {
			logger.log( "focus!!!" );
			this.cache[ "keyEventCheck" ] || this.onContext();		
		},
		onBlur: function() {
			logger.log( "blur!!!" );
			if( this.cache[ "keyEventCheck" ] ) {
	            window.clearInterval( this.cache[ "keyEventCheck" ] );
	            this.cache[ "keyEventCheck" ] = null;
			}
		},
		onDocumentMousedown: function( e ) {
			logger.log( "onDocumentMousedown" );
			e.preventDefault();
			YAEditor["__ui__"].focus();
		},
		onButtonClick: function( e ) {
			YAEditor[ "__event__" ].routeCommand( $( this ).parent().attr( "id" ), "command" );
		}, 
		onButtonMousedown: function( e ) {
			logger.log( "onButtonMousedown" );
			YAEditor[ "__event__" ].routeCommand( $( this ).parent().attr( "id" ), "mousedown" );
		}, 
		onButtonMouseup: function( e ) {
			YAEditor[ "__event__" ].routeCommand( $( this ).parent().attr( "id" ), "mouseup" );
		}, 
		onButtonMouseover: function( e ) {
			YAEditor[ "__event__" ].routeCommand( $( this ).parent().attr( "id" ), "mouseover" );
		}, 
		onButtonMouseout: function( e ) {
			YAEditor[ "__event__" ].routeCommand( $( this ).parent().attr( "id" ), "mouseout" );
		}, 
		onContext: function() {
			//
			this.cache[ "keyEventCheck" ] && window.clearInterval( this.cache[ "keyEventCheck" ] );

			//
			var $iframe = YAEditor[ "__util__" ].$iframe(),
				$body = $iframe.contents().find("body"),
				iframe = $iframe.get( 0 ),
			    selection = rangy.getIframeSelection( iframe ),
				range = selection.getRangeAt( 0 ),
				container = range.commonAncestorContainer;
				parentNode = ( container.nodeType == 3 ) ? container.parentNode : container;
			
			//logger.log( ">>>>>" + $body.html() + "<<<<<<<" );

			var text = $( container ).text();
			if( this.cache[ "text" ] !=  text ) {
				$body.trigger( "keyup" );
			}
				
			//cache text
			this.cache[ "text" ] = text;
			
			// init body inner
			if( parentNode.tagName.toUpperCase() == "BODY" && ( this.cache[ "text" ] == "\n" ||  this.cache[ "text" ] == "" ) ) {
	        	var
	        	doc = $iframe.contents().get( 0 ),
	        	p  = doc.createElement( "p" ),
	        	br = doc.createElement( "br" );

	        	range.deleteContents();
	        	$body.html( "" );

	        	p.align = "left";
	        	p.appendChild( br );
	        	parentNode.appendChild( p );
	        	
	        	range.setStartBefore( br );
	        	range.collapse( true );
	            selection.removeAllRanges();
	            selection.addRange( range );
			}
			
			//
			var countListeners = this.inputListeners.length;
			for( var i = 0; i < countListeners; i++ ) {
				this.inputListeners[ i ].onContext( this.cache[ "text" ], range.startOffset );
			}
			
			//
			var countPlugins = this.commandPlugins.length;
			for( var i = 0; i < countPlugins; i++ ) {
				if( !this.commandPlugins[ i ].onContext ){
					continue;
				}
				this.commandPlugins[ i ].onContext( this.cache[ "text" ], range.startOffset );
			}			
			
		    //
			this.cache[ "keyEventCheck" ] = window.setInterval( this.onContext.bind( this ), 100 );
		}
//		,
//		onKeypress : function(e) {
//			logger.log( "onKeypress" );
//			YAEditor["__core__"].updateData( true );
//		}		
	},
	
	
	///////////////////////////////////////////////////////////////////////////
	/* UTILITY */
	__util__: {
		PREFIX: "YaEdItOr",
		ID_IFRAME: "IfRaMe",
		ID_TEXTAREA: "TeXtArEa",
		cache: { iframe: null, textarea: null  },
		$iframe: function() {
			return this.cache[ "iframe" ] || ( this.cache[ "iframe" ] = $( "#" + this.PREFIX + "_" + this.ID_IFRAME ) ); 
		},
		$textarea: function() {
			return this.cache[ "textarea" ] || ( this.cache[ "textarea" ] = $( "#" + this.PREFIX + "_" + this.ID_TEXTAREA ) ); 
		}
	}	
};


//
// Export Functions of YAEditor's Internel Objects ( external access explicitly )
//
$.extend( YAEditor, { 

	configuration: function( inputAreaDoc ) {
		this[ "__core__" ].inputAreaDoc = inputAreaDoc;
	},
	html: function(){
		if( arguments.length == 0 ) {
			this["__core__"].updateData( true );
			return this["__util__"].$textarea().val();
		} else if( arguments.length == 3 ) {
			this[ "__core__" ].html( arguments[ 0 ],  arguments[ 1 ], arguments[ 2 ] );
			return;
		}
		
		logger.error( "YaEditor.httml : insufficient parameters" );
	},
	attachInputListener: YAEditor[ "__event__" ].attachInputlistener.bind( YAEditor[ "__event__" ] ),	
	attachCommandPlugin: YAEditor[ "__event__" ].attachCommandPlugin.bind( YAEditor[ "__event__" ] ),	
	$iframe: YAEditor[ "__util__" ].$iframe.bind( YAEditor[ "__util__" ] ),
	$textarea: YAEditor[ "__util__" ].$textarea.bind( YAEditor[ "__util__" ] ),
	rectCaret: YAEditor[ "__ui__" ].rectCaret.bind( YAEditor[ "command" ] ),
	focus: YAEditor[ "__ui__" ].focus
} );




/**
 *	simple logger ( yaJS's $[ "log", "info", "warn", "error" ] wrapped )
 * 
 *	@update 2013.04.02
 *	@author Dae H. Ahn ( kickscar@sunnyvale.co.kr )
 * 
 */
var logger = {
	level: function( level ) {
		var fns = [ "error", "warn", "info", "log"  ],
			threshold = ( level == "none" ) ? -1 : $.inArray( level, fns );
		$.each( fns, function( i, fn ) {
		    this[ fn ] = ( i > threshold ) ? yaJS.emptyFunction : $[ fn ];
		}.bind( this ) );
	}
};