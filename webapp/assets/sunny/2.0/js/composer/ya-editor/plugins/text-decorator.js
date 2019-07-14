//
//	Text Decorator 0.5.0
//

var TextDecorator = {
	__event__: {
		CLASSNAME_ON: "on",
		PREFIX_COMMAND_ID: "edt-mn",
		commands: [ "bold", "italic", "underline", "strikethrough", "justifyleft", "justifycenter", "justifyright", "justifyfull", "insertorderedlist", "insertunorderedlist" ],
		onCommand: function( $e ) {
			var
			commandId = $e.attr( "id" ),
			command = commandId.replace( this.PREFIX_COMMAND_ID + "-", "" );
			if(	TextDecorator[ "__command__" ][ command ] ){
				TextDecorator[ "__command__" ][ command ].call( null );
				return true;
			}
			return false;
		},
		onContext: function( text, offset ){
			var
			iframe = YAEditor.$iframe().get( 0 ), 
			doc = iframe.contentWindow.document;

			$.each( this.commands, function(index, cmd){
				// query
				var
				$button = $( "#" + TextDecorator["__event__"].PREFIX_COMMAND_ID + "-" + cmd );
				if( doc.queryCommandState( cmd ) ) {
					$button.addClass( TextDecorator["__event__"].CLASSNAME_ON );
				} else {
					$button.removeClass( TextDecorator["__event__"].CLASSNAME_ON );
				}
			});
		}
	},
	__command__: {
		bold: function() {
			var
			iframe = YAEditor.$iframe().get( 0 ), 
			doc = iframe.contentWindow.document;
			doc.execCommand( "bold", false, "Bold" );
			YAEditor.focus();
		},
		italic: function() {
			var
			iframe = YAEditor.$iframe().get( 0 ), 
			doc = iframe.contentWindow.document;
			
			doc.execCommand( "italic", false, "Italic" );
			YAEditor.focus();
		},
		underline: function() {
			var
			iframe = YAEditor.$iframe().get( 0 ), 
			doc = iframe.contentWindow.document;
			doc.execCommand( "underline", false, "Underline" );
			YAEditor.focus();
		},
		strikethrough: function(){
			var
			iframe = YAEditor.$iframe().get( 0 ), 
			doc = iframe.contentWindow.document;
			doc.execCommand( "strikethrough", false, "Strikethrough" );
			YAEditor.focus();
		},
		justifyleft: function(){
			var
			iframe = YAEditor.$iframe().get( 0 ), 
			doc = iframe.contentWindow.document,
			ex = doc;
			
			if(typeof document.body.createTextRange != "undefined"){ //ie
				var
				selection = rangy.getIframeSelection( iframe ),
				range = selection.getRangeAt( 0 ),
				container = range.commonAncestorContainer,
				parentNode = ( container.nodeType == 3 ) ? container.parentNode : container;
				ex = doc.body.createTextRange();
		        ex.moveToElementText(parentNode);
			}
			
			ex.execCommand( "justifyleft", false, null );
			YAEditor.focus();
		},
		justifycenter: function(){
			var
			iframe = YAEditor.$iframe().get( 0 ), 
			doc = iframe.contentWindow.document,
			ex = doc;
			
			if(typeof document.body.createTextRange != "undefined"){ //ie
				var
				selection = rangy.getIframeSelection( iframe ),
				range = selection.getRangeAt( 0 ),
				container = range.commonAncestorContainer,
				parentNode = ( container.nodeType == 3 ) ? container.parentNode : container;
				
				ex = doc.body.createTextRange();
				ex.moveToElementText(parentNode);
			}
			
	        ex.execCommand("justifycenter", false, null );
			YAEditor.focus();
		},
		justifyright: function(){
			var
			iframe = YAEditor.$iframe().get( 0 ), 
			doc = iframe.contentWindow.document,
			ex = doc;
			
			if(typeof document.body.createTextRange != "undefined"){ //ie
				var
				selection = rangy.getIframeSelection( iframe ),
				range = selection.getRangeAt( 0 ),
				container = range.commonAncestorContainer,
				parentNode = ( container.nodeType == 3 ) ? container.parentNode : container;
				
				ex = doc.body.createTextRange();
				ex.moveToElementText(parentNode);
			}			
			
			ex.execCommand( "justifyright", false, null );
			YAEditor.focus();
		},
		justifyfull: function(){
			var
			iframe = YAEditor.$iframe().get( 0 ), 
			doc = iframe.contentWindow.document,
			ex = doc;
			
			if(typeof document.body.createTextRange != "undefined"){ //ie
				var
				selection = rangy.getIframeSelection( iframe ),
				range = selection.getRangeAt( 0 ),
				container = range.commonAncestorContainer,
				parentNode = ( container.nodeType == 3 ) ? container.parentNode : container;
				
				ex = doc.body.createTextRange();
				ex.moveToElementText(parentNode);
			}
			
			ex.execCommand( "justifyfull", false, null );
			YAEditor.focus();
		},
		insertorderedlist: function(){
			var
			iframe = YAEditor.$iframe().get( 0 ), 
			doc = iframe.contentWindow.document,
			ex = doc;
			
			if(typeof document.body.createTextRange != "undefined"){ //ie
				var
				selection = rangy.getIframeSelection( iframe ),
				range = selection.getRangeAt( 0 ),
				container = range.commonAncestorContainer,
				parentNode = ( container.nodeType == 3 ) ? container.parentNode : container;
				
				ex = doc.body.createTextRange();
				ex.moveToElementText(parentNode);
			}

			ex.execCommand( "insertorderedlist", false, null );
			YAEditor.focus();
		},
		insertunorderedlist: function(){
			var
			iframe = YAEditor.$iframe().get( 0 ), 
			doc = iframe.contentWindow.document,
			ex = doc;
			
			if(typeof document.body.createTextRange != "undefined"){ //ie
				var
				selection = rangy.getIframeSelection( iframe ),
				range = selection.getRangeAt( 0 ),
				container = range.commonAncestorContainer,
				parentNode = ( container.nodeType == 3 ) ? container.parentNode : container;
				
				ex = doc.body.createTextRange();
				ex.moveToElementText(parentNode);
			}			
			
			ex.execCommand( "insertunorderedlist", false, null );
			YAEditor.focus();
		},
		outdent: function(){
			var
			iframe = YAEditor.$iframe().get( 0 ), 
			doc = iframe.contentWindow.document,
			ex = doc;
			
			if(typeof document.body.createTextRange != "undefined"){ //ie
				var
				selection = rangy.getIframeSelection( iframe ),
				range = selection.getRangeAt( 0 ),
				container = range.commonAncestorContainer,
				parentNode = ( container.nodeType == 3 ) ? container.parentNode : container;
				
				ex = doc.body.createTextRange();
				ex.moveToElementText(parentNode);
			}	
			
			ex.execCommand( "outdent", false, null );
			YAEditor.focus();
		},
		indent: function(){
			var
			iframe = YAEditor.$iframe().get( 0 ), 
			doc = iframe.contentWindow.document,
			ex = doc;
			
			if(typeof document.body.createTextRange != "undefined"){ //ie
				var
				selection = rangy.getIframeSelection( iframe ),
				range = selection.getRangeAt( 0 ),
				container = range.commonAncestorContainer,
				parentNode = ( container.nodeType == 3 ) ? container.parentNode : container;
				
				ex = doc.body.createTextRange();
				ex.moveToElementText(parentNode);
			}	
			
			ex.execCommand( "indent", false, null );
			YAEditor.focus();
		},
		blockquote: function(){
			var
			iframe = YAEditor.$iframe().get(0), 
			doc = iframe.contentWindow.document,
			selection = rangy.getIframeSelection(iframe),
			range = selection.getRangeAt(0);
			
			var elNewParent = document.createElement("BLOCKQUOTE");
			range.surroundContents(elNewParent);
			
			
			
//			doc.execCommand( "formatBlock", false, "blockquote" );
            
            
			YAEditor.focus();
		}		
	}
};

$.extend( TextDecorator, {
	type: "command",
	onCommand: TextDecorator[ "__event__" ].onCommand.bind( TextDecorator[ "__event__" ] ),
	onContext: TextDecorator[ "__event__" ].onContext.bind( TextDecorator[ "__event__" ] )
});
