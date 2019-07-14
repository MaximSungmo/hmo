/**
 * MentionInputListener 0.8.0
 * 
 */

var MentionInputListener = {
/* core */
	__core__: {
		__input: null,
		__inputId: null,
		__inputText: "",
		__contextRange: null,
		__contextWordData: null,
		__mentionData:{},
		__decorationData:{},
		textBold: function() {
			$.log( this.__contextRange );
			
			if( !this.__contextRange || ( this.__contextRange.start == this.__contextRange.end ) ) {
				return;
			}
			
			//
			var
			text = this.__input.val(),
			lengthText = text.length;
			tempData = $A( this.__decorationData[ this.__inputId ] ),
			countDecorationData = tempData.length,
			a = [];
			
			// init
			for( var i = 0; i < lengthText; i++ ) {
				a[ i ] = false;
			}
			//$.log( "init: " +  a );
			this.__decorationData[ this.__inputId ] = [];

			
			// selection
			for( var i = 0; i < countDecorationData; i++ ) {
				var data =  tempData[ i ];
				for( var j = data.beginAt; j <= data.endAt; j++ ){
					a[ j ] = true;
				}
			}
			//$.log( "selection: " +  a );

			// inverse
			var val = a[ this.__contextRange.start ];
			for( var i = this.__contextRange.start; i < this.__contextRange.end; i++ ) {
				//a[ i ] = !a[ i ];
				a[ i ] = !val;
			}
			//$.log( "inverse: " +  a );
			
			//
			var 
			count = a.length,
			beginAt = 0,
			val = a[ 0 ];
			
			for( var i = 1; i < count; i++ ) {
				
				if( val == a[ i ] ) {
					continue;
				}
				
				if( val == false ) {
					beginAt = i;
					val = a[ i ];
					continue;
				}
				
				//
				var endAt = i - 1;
				var index = this.__decorationData[ this.__inputId ].length;
				this.__decorationData[ this.__inputId ].splice( index, 0, {
					"beginAt": beginAt,
					"endAt": endAt,
					"text": text.substr( beginAt, endAt - beginAt + 1 ),
					"type": "bold"
				});
				
				//
				beginAt = i;
				val = a[ i ];
			}
			
			if( val == true ){
				var endAt = count-1;
				var index = this.__decorationData[ this.__inputId ].length;
				this.__decorationData[ this.__inputId ].splice( index, 0, {
					"beginAt": beginAt,
					"endAt": endAt,
					"text": text.substr( beginAt, endAt - beginAt + 1 ),
					"type": "bold"
				});
			}
			
			//$.log( this.__decorationData[ this.__inputId ] );
			//$.log( "selection range[startAt:" + this.__contextRange.start + ", endAt:" + this.__contextRange.end + "][text:" + text + "][selection text:" + textSelection + "]" );
			
			//
			this.__inputText = "";
			this.update( text );
			
			//
			//this.__input.focus();
			MentionInputListener[ "__util__" ].clearInputSelection( this.__input );
		},
		addMentionData: function( data ) {
//			$.log( "[contextWordData]" +  this.__contextWordData[ "beginAt" ] + ":" + this.__contextWordData[ "endAt" ] + ":" + this.__contextWordData[ "textSelected" ] );
//			$.log( "[data]" +  data[ "name" ] + ":" + data[ "id" ]  );
			var
			contextWordData = this.__contextWordData,
			beginAt =  this.__contextWordData[ "beginAt" ],
			endAt = this.__contextWordData[ "endAt" ],
			textSelected = this.__contextWordData[ "textSelected" ],
			mentionData = {
				"beginAt": beginAt,
				"endAt": beginAt + data["name"].length-1,
				"text": data["name"],
				"id": data["id"]
			},
			inputText = this.__input.val(),
			count = this.__mentionData[ this.__inputId ].length;
			
			inputText = inputText.slice( 0, beginAt ) + mentionData[ "text" ] + inputText.slice( endAt+1 );
			this.__mentionData[ this.__inputId ].splice( count, 0, mentionData );
			this.update( inputText );
		},
		insertDecoration: function( $input, decoration ) {
			var id =  $input.identify();
			if( !this.__decorationData[ id ] ) {
				this.__decorationData[ id ] = [];
			}
			
			var count = this.__decorationData[ id ].length;
			this.__decorationData[ id ].splice( count, 0, decoration );
		},
		insertMention: function( $input, mention ) {
			var id =  $input.identify();
			if( !this.__mentionData[ id ] ) {
				this.__mentionData[ id ] = [];
			}
			
			var count = this.__mentionData[ id ].length;
			this.__mentionData[ id ].splice( count, 0, mention );
		},
		clear: function( $input ) {
			var id =  $input.identify();
			
			this.__mentionData[ id ] = null;
			this.__decorationData[ id ] = null;
			this.__input = null;
			this.__inputId = null;
			this.__inputText = "";
		},
		updateHighliter: function( mentionText ) {
			var
			$highlighter = MentionInputListener[ "__util__" ].$highlighter(),
			highlighterHTML = mentionText,
			highlighterTextReplaceMaps = [],
			mentionItems = mentionText.match(/@\[[0-9]+:([^0-9]+)\]/g) || [];
		
			$.each( mentionItems, function( index, mentionText ) {
				var	
				s = mentionText.split(":"),
				highlighterTag = "<b>" + s[1].replace( "]", "</b>" );
//				highlighterTag = "<strong>" + s[1].replace( "]", "</strong>" );
				
				highlighterTextReplaceMaps.push({
					"mentionText": mentionText,
					"highlighterTag": highlighterTag
				});
				//$.log( "@@@@" + mentionText + ":" + index );
			});
			
			//
			$.each( highlighterTextReplaceMaps, function( index, map ){
				highlighterHTML = highlighterHTML.replace( map[ "mentionText" ], map[ "highlighterTag" ] );
			});
			
			//
			highlighterHTML = highlighterHTML.replace( /\[B\]/gi, "<strong>" ).replace( /\[\/B\]/gi, "</strong>" );
			
			//
			$highlighter.html( highlighterHTML.replace( /\r\n/gi, "<br>" ).replace( /\n/gi, "<br>" ) );
		},
		updateText: function() {

			var
			inputText = this.__inputText,
			mentionText = this.__inputText,
			textReaplaceMaps = [],
			count;
				
			// 1. to Make Replacement Info. for Mention Text
			// $.log( "1. >>>>>>>>" + mentionText + "<<<<<<<" );
			count = this.__mentionData[ this.__inputId ].length;
			for( var index = 0; index < count; index++ ) {
				var
				data = this.__mentionData[ this.__inputId ][ index ],
				beginAt = data[ "beginAt" ],
				endAt = data[ "endAt" ],
				text  = inputText.substr( beginAt, endAt - beginAt + 1 ),
				textA = inputText.substr( beginAt+1, endAt - beginAt + 1 ),
				textB = inputText.substr( beginAt-1, endAt - beginAt + 1 );
				
				// $.log( "=" + beginAt + "=" + endAt + "=" + text + "=" + textA + "=" + textB + "=" + data["text"] + "=" );
				if( text == data["text"] ) {
					var hashing = MentionInputListener["__util__"].hashing( data["text"], index );
					mentionText = mentionText.slice( 0, beginAt ) + hashing + mentionText.slice( endAt+1 );
					textReaplaceMaps.push({
						"hashing": hashing,
						"text": "@[" + data[ "id" ] + ":" + data[ "text" ] + "]"
					});
					// $.log( "2. >>>>>>>>" + mentionText + "<<<<<<<" );
				} else if( textA == data["text"] ) {
					//$.log( "앞에 글자가 하나 늘어난 경우:" + index );
					beginAt += 1;
					endAt += 1;
					this.__mentionData[ this.__inputId ].splice( index, 1, {
						"beginAt": beginAt,
						"endAt": endAt,
						"text": data["text"],
						"id": data["id"]							
					});
					var hashing = MentionInputListener["__util__"].hashing( data["text"], index );
					mentionText = mentionText.slice( 0, beginAt ) + hashing + mentionText.slice( endAt+1 );
					textReaplaceMaps.push({
						"hashing": hashing,
						"text": "@[" + data[ "id" ] + ":" + data[ "text" ] + "]"
					});
				} else if( textB == data["text"] ) {
					//$.log( "앞에 글자가 줄어든 경우:" + index );
					beginAt -= 1;
					endAt -= 1;
					this.__mentionData[ this.__inputId ].splice( index, 1, {
						"beginAt": beginAt,
						"endAt": endAt,
						"text": data["text"],
						"id": data["id"]							
					});
					var hashing = MentionInputListener["__util__"].hashing( data["text"], index );
					mentionText = mentionText.slice( 0, beginAt ) + hashing + mentionText.slice( endAt+1 );
					textReaplaceMaps.push({
						"hashing": hashing,
						"text": "@[" + data[ "id" ] + ":" + data[ "text" ] + "]"
					});
				} else {
					/*
					text  = inputText.substr( beginAt, endAt - beginAt ),
					textA = data[ "text" ].substr( 1, data[ "text" ].length-1 );
					if( text == textA ) {
						//inputText = inputText.slice( 0, beginAt ) + inputText.slice( endAt+1 );
						//this.__input.val( inputText );
						mentionText = mentionText.slice( 0, beginAt ) + mentionText.slice( endAt );
						$.log("앞이 진워진 경우:" + text + ":" + textA + "-->" + mentionText + "<---" );
					}
					*/
					MentionInputListener[ "__core__" ].__mentionData[ this.__inputId ].splice( index--, 1 );
					count--;
				}
			}

			
			// 2. to make replacement info. for decoration text
			count = this.__decorationData[ this.__inputId ].length;
			for( var index = count-1; index >= 0; index-- ) {
				var
				data = this.__decorationData[ this.__inputId ][ index ],
				beginAt = data[ "beginAt" ],
				endAt = data[ "endAt" ],
				text  = inputText.substr( beginAt, endAt - beginAt + 1 ),
				textA = inputText.substr( beginAt+1, endAt - beginAt + 1 ),
				textB = inputText.substr( beginAt-1, endAt - beginAt + 1 );
				
				//$.log( inputText + "=" + beginAt + "=" + endAt + "=" + text + "=" + textA + "=" + textB );
				
				if( text == data["text"] ) {
					var hashing = MentionInputListener["__util__"].hashing( data["text"], index );
					mentionText = mentionText.slice( 0, beginAt ) + hashing + mentionText.slice( endAt+1 );
					textReaplaceMaps.push( {
						"hashing": hashing,
						"text": "[B]" + data[ "text" ] + "[/B]"
					} );
				} else if( textA == data["text"] ) {
					//$.log( "앞에 글자가 하나 늘어난 경우:" + index );
					beginAt += 1;
					endAt += 1;
					// text = inputText.substr( beginAt, endAt - beginAt + 1 );
					this.__decorationData[ this.__inputId ].splice( index, 1, {
						"beginAt": beginAt,
						"endAt": endAt,
						"text": data["text"],
						"type": data["type"]							
					});
					var hashing = MentionInputListener["__util__"].hashing( data["text"], index );
					mentionText = mentionText.slice( 0, beginAt ) + hashing + mentionText.slice( endAt+1 );
					textReaplaceMaps.push({
						"hashing": hashing,
						"text": "[B]" + data["text"] + "[/B]"
					});					
				} else if( textB == data["text"] ) {
					//$.log( "앞에 글자가 줄어든 경우:" + index );
					beginAt -= 1;
					endAt -= 1;
					//text = inputText.substr( beginAt, endAt - beginAt + 1 );
					this.__decorationData[ this.__inputId ].splice( index, 1, {
						"beginAt": beginAt,
						"endAt": endAt,
						"text": data["text"],
						"type": data["type"]							
					});
					var hashing = MentionInputListener["__util__"].hashing( data["text"], index );
					mentionText = mentionText.slice( 0, beginAt ) + hashing + mentionText.slice( endAt+1 );
					textReaplaceMaps.push({
						"hashing": hashing,
						"text": "[B]" + data["text"] + "[/B]"
					});
					
				} else {
					var
					dirty = false,
					textEnd2 = data[ "text" ].substr( data[ "text" ].length-1, 1 ),
					textEnd5 = data[ "text" ].substr( data[ "text" ].length-2, 1 ),
					textEnd1 = inputText.substr( endAt+1, 1 ),
					textEnd3 = inputText.substr( endAt, 1 ),
					textEnd4 = inputText.substr( endAt-1, 1 );
						
					//$.log( textEnd1 + ":" + textEnd2 + ":" + textEnd3 + ":" + textEnd4 + ":" + textEnd5  );
					
					if( textEnd1 == textEnd2 ) {
						//$.log( "중간에 1개 글자가 늘어난 경우:" + index );
						endAt += 1;
						text = inputText.substr( beginAt, endAt - beginAt + 1 );
						dirty = true;
					} else if( textEnd3 == textEnd2 ) { 
						//$.log( "한글 에디팅 중...." + index );
						dirty = true;
					} else if( textEnd4 == textEnd2 ){
						//$.log( "중간에 1개 글자가 줄어든 경우:" + index );
						endAt -= 1;
						text = inputText.substr( beginAt, endAt - beginAt + 1 );
						dirty = true;
					} else if( textEnd4 == textEnd5 ){
						//$.log( "끝에 1개 글자가 줄어든 경우:" + index );
						endAt -= 1;
						text = inputText.substr( beginAt, endAt - beginAt + 1 );
						dirty = true;
					} else {
					}
					
					if( dirty ) {
						this.__decorationData[ this.__inputId ].splice( index, 1, {
							"beginAt": beginAt,
							"endAt": endAt,
							"text": text,
							"type": data["type"]							
						});
						var hashing = MentionInputListener["__util__"].hashing( text, index );
						mentionText = mentionText.slice( 0, beginAt ) + hashing + mentionText.slice( endAt+1 );
						textReaplaceMaps.push( {
							"hashing": hashing,
							"text": "[B]" + text + "[/B]"
						} );						
					} else {
						MentionInputListener[ "__core__" ].__decorationData[ this.__inputId ].splice( index--, 1 );
						count--;
					}	
				}
			}
			
			// 3. replace text
			// $.log( "3. >>>>>>>>" + mentionText + "<<<<<<<" );
			$.each( textReaplaceMaps, function( index, map ) {
				mentionText = mentionText.replace( map[ "hashing" ], map[ "text" ] );
			});	
			
			// 4. return
			return mentionText;
		},
		update: function( newText ) {
			
			if( this.__inputText == newText ) {
				return;
			}

			// 1. update textarea
			this.__inputText = newText;
			( this.__inputText !== this.__input.val() ) && this.__input.val( this.__inputText );
			
			// 2. update mention Contents
			var 
			mentionText = this.updateText(),	
			$mentionText = MentionInputListener[ "__util__" ].$mentionText();
			$mentionText.val( mentionText );
			
			// 3. update highlighter
			this.updateHighliter( mentionText );

			// 4. highlighter's visibility
			var $highlighter = MentionInputListener[ "__util__" ].$highlighter();

			if( this.__mentionData[ this.__inputId ].length > 0 || this.__decorationData[ this.__inputId ].length > 0 ) {
				$highlighter.removeClass( "hidden-elem" );
			} else {
				$highlighter.addClass( "hidden-elem" );
			}
		}
	},	
	/* event */
	__event__: {
		cache: {},
		onAttached: function() {
			MentionSelectBox.init();
			MentionSelectBox.init = fn_empty;
		},
		onContext: function( input, text, offset ) {
			//$.log( "+++++" + text + "++++++ : [" + offset + "]" );
			MentionInputListener[ "__core__" ].__input = input;
			MentionInputListener[ "__core__" ].__inputId = MentionInputListener[ "__core__" ].__input.attr("id");

			//
			var inputId = MentionInputListener[ "__core__" ].__inputId;
			
			// decoration data cache set
			if( !MentionInputListener[ "__core__" ].__decorationData[ inputId ] ) { 
				MentionInputListener[ "__core__" ].__decorationData[ inputId ] = [];
			}
			
			// mention data cache set
			if( !MentionInputListener[ "__core__" ].__mentionData[ inputId ] ) { 
				MentionInputListener[ "__core__" ].__mentionData[ inputId ] = [];
			}

			//
			MentionInputListener[ "__core__" ].__contextRange = MentionInputListener[ "__util__" ].getInputSelection( MentionInputListener[ "__core__" ].__input.get( 0 ) );
			//$.log( MentionInputListener[ "__core__" ].__contextRange.start + ", " + MentionInputListener[ "__core__" ].__contextRange.end );
		
			//
			MentionInputListener[ "__core__" ].update( text );
			
			// check text
			if( text == "" ) {
				this.cache[ "text" ] = "";
				MentionSelectBox.hide();			
				return;
			}
			
			// get word
			var word = MentionInputListener[ "__util__" ].word( text, offset );
			if( word == null ) {
				this.cache[ "text" ] = "";
				MentionSelectBox.hide();			
				return;
			}
			
			//get text
			var dist = word[ "text" ].length - ( word[ "endAt" ] - offset ),
				text = word[ "text" ].substring( 1, dist );
			if( text == this.cache[ "text" ] ) {
				return;
			}

			// cache text
			this.cache[ "text" ] = text;
			if( this.cache[ "text" ] == "" ) {
				this.cache[ "text" ] = "";
				MentionSelectBox.hide();			
				return;
			}

			//$.log( word[ "beginAt" ] + ":" + word[ "endAt" ] + ":" + dist + "-" +  text );
			MentionInputListener[ "__core__" ].__contextWordData = {
				beginAt: word[ "beginAt" ] - 1,
				endAt: word[ "beginAt" ] - 2 + dist,
				textSelected: text
			}
			
			//$.log( " --> fetch data [" +  this.cache[ "text" ] + "]" + ":" + encodeURIComponent(this.cache[ "text" ]) + ":" + encodeURIComponent(encodeURIComponent(this.cache[ "text" ])) );
			$.ajax( {
				url: "/user/match",
				async: false,
				type: "get",
				dataType: "json",
				contentType: "application/json",
				data: {
					"key" : this.cache[ "text" ] 
				},
				success: this.onDataRequest.bind( this ),
				error: function( jqXHR, textStatus, errorThrown ){
					$.error("add comments:"+errorThrown);
				}	
			} );
		},
		onDataRequest: function ( result ) {
			// $.log( "++++++++++++++++++++" + JSON.stringify( result ) + "++++++++++++++++++++" );
			MentionSelectBox.update( result.data );
			MentionSelectBox.popup();
			MentionSelectBox.selectItem( 0 );
		},
		onSelectChanged: function() {
			var itemData = MentionSelectBox.getItemSelected();
			itemData && MentionInputListener[ "__core__" ].addMentionData( itemData );
			//$.log( $.stringify( itemData ) );
		}
	},
	/* util */
	__util__: {
		triggerChar: "@",
		word: function( text, offset ) {
			var words = this.wordsInText( text ),
				wc = words.length,
				word = null;
		
			for( var i = 0; i < wc; i++ ) {
				if( words[i].beginAt <= offset && offset <= words[i].endAt ) {
					word = words[ i ];
					break;
				}
			}
			
			return word;
		},
		wordsInText: function( text ){
			var length = text.length,
				words = [],
				word = "",
				triggerOn = false,
				beginAt = 0,
				endAt = 0;
			
			for( var i = 0; i < length; i++ ) {
				var ch = text.charAt( i ),
					charCode = ch.charCodeAt( 0 );
				
				if( triggerOn == true ) {
					if( /*charCode == 32 || */ charCode == 10 || charCode == 13 || charCode == 64 || charCode == 160 ) {
						triggerOn = false;
						endAt = i;
						words[ words.length ] = { text: word, beginAt: beginAt, endAt : endAt };
						if( charCode == 64 ) {
							i--;
						}
					} else {
						word += ch;
						if( i + 1 == length ) {
							endAt = i + 1;
							words[ words.length ] = { text: word, beginAt: beginAt, endAt: endAt };
						}
					}
				} else {
					if( ch == this.triggerChar ) {
						triggerOn = true;
						word = ch;
						beginAt = i + 1;
					}
				}
			}
			return words;
		},
		$highlighter: function(){
			var $input = $( MentionInputListener[ "__core__" ].__input );
			return $input.parents( ".ui-mentions-input" ).find( ".mentions-highlighter" );
		},
		$mentionText: function(){
			var $input = $( MentionInputListener[ "__core__" ].__input );
			return $input.parents( ".ui-mentions-input" ).find( ".mentions-text" );
		},
		hashing: function( str, index ) {
			var l = str.length - 2,
				hashing = "#" + index,
				ss = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789";
			for(var i = 0; i < l; i++) {
				var r = Math.floor(Math.random() * ss.length);
				hashing += ss.charAt( r );
			}
			return hashing;
		},
		getCaret: function( el ) {
			if( el.selectionStart ) {
				return el.selectionStart;
			} else if ( document.selection ) {
			    el.focus();
			    
			    var r = document.selection.createRange();
			    if ( r == null ) {
			      return 0;
			    }

			    var
			    re = el.createTextRange(),
			    rc = re.duplicate();
			    
			    re.moveToBookmark( r.getBookmark() );
			    rc.setEndPoint( "EndToStart", re );

			    return rc.text.length;
			}
			
			return 0;
		},		
		getInputSelection: function( el ) {
		    var start = 0,
		    	end = 0, 
		    	normalizedValue, 
		    	range,
		        textInputRange, 
		        len, 
		        endRange;
		    
		    if( typeof el.selectionStart == "number" && typeof el.selectionEnd == "number" ) {
		    	start = el.selectionStart;
		        end = el.selectionEnd;
		    } else {
		        range = document.selection.createRange();
		        
		        if ( range && range.parentElement() == el ) {
		        	
		            len = el.value.length;
		            normalizedValue = el.value.replace(/\r\n/g, "\n");

		            // Create a working TextRange that lives only in the input
		            textInputRange = el.createTextRange();
		            textInputRange.moveToBookmark( range.getBookmark() );

		            // Check if the start and end of the selection are at the very end
		            // of the input, since moveStart/moveEnd doesn't return what we want
		            // in those cases
		            
		            endRange = el.createTextRange();
		            endRange.collapse(false);

		            if( textInputRange.compareEndPoints( "StartToEnd", endRange ) > -1 ) {
		                start = end = len;
		            } else {
		                start = -textInputRange.moveStart( "character", -len);
		                start += normalizedValue.slice( 0, start ).split( "\n" ).length - 1;

		                if (textInputRange.compareEndPoints( "EndToEnd", endRange) > -1) {
		                    end = len;
		                } else {
		                    end = -textInputRange.moveEnd( "character", -len );
		                    end += normalizedValue.slice(0, end).split( "\n" ).length - 1;
		                }
		            }
		        }
		    }

		    return { start: start, end: end };
		},
		clearInputSelection: function( el ){
		    if( typeof el.selectionStart == "number" && typeof el.selectionEnd == "number" ) {
		        el.selectionStart = el.selectionEnd;
		    } else {
		    	var sel;
		    	if ( ( sel = document.selection ) && sel.empty ) {
		            sel.empty();
		        } else if( window.getSelection ) {
		        	window.getSelection().removeAllRanges();
		        }
		    }    
		}		
	}
};

// Define MentionInputListener Plugin
$.extend( MentionInputListener, {
	type: "listener",
	onAttached: MentionInputListener[ "__event__" ].onAttached.bind( MentionInputListener[ "__event__" ] ),
	onContext: MentionInputListener[ "__event__" ].onContext.bind( MentionInputListener[ "__event__" ] ),
	onSelectChanged: MentionInputListener[ "__event__" ].onSelectChanged.bind( MentionInputListener[ "__event__" ] ),
	bold: MentionInputListener[ "__core__" ].textBold.bind( MentionInputListener[ "__core__" ] ),
	insertMention: MentionInputListener[ "__core__" ].insertMention.bind(  MentionInputListener[ "__core__" ] ),
	insertDecoration: MentionInputListener[ "__core__" ].insertDecoration.bind(  MentionInputListener[ "__core__" ] ),
	clear: MentionInputListener[ "__core__" ].clear.bind(  MentionInputListener[ "__core__" ] )
});

// MentionSelectBox
var MentionSelectBox = {
	/* operation */
	__operation__: {	
		init: function() {
			MentionSelectBox[ "__util__" ].init();
			$(document.body).on( "mouseover", ".mention-select-box li", MentionSelectBox[ "__event__" ].onItemMouseover );
			$(document.body).onHMOClick( ".mention-select-box li", MentionSelectBox[ "__event__" ].onItemClick );
		},
		getItemSelected: function() {
			var indexSelected =  MentionSelectBox[ "__ui__" ].indexSelected; 
			if( indexSelected < 0 ) {
				return null;
			}
			
			var $item = MentionSelectBox[ "__util__" ].$item( indexSelected );
			return {
				name: $item.find( "p strong i" ).text(),
				id: $item.attr( "data-uid" )
			};
		}
	},
	/* ui */
	__ui__: {
		indexSelected: -1,
		countItem: 0,
		update: function( data ) {
			this.deleteAllItems();
			$.each( data, function( idx, d ) {
				this.insertItem( d, idx );
			}.bind( this ) );
		},
		deleteAllItems: function() {
			this.indexSelected = -1;
			this.countItem = 0;
			MentionSelectBox[ "__util__" ].$list().html( "" );
		},
		insertItem: function( data, index ) {
			var departments = "", countDepartment = data[ "departmentStrings" ] ? data[ "departmentStrings" ].length : 0;  
			for( var i = 0; i < countDepartment; i++ ) {
				departments += data[ "departmentStrings" ][ i ] ? ( departments == "" ? data[ "departmentStrings" ][ i ] : ", " + data[ "departmentStrings" ][ i ] ) : "";
			}
			
			var $list = MentionSelectBox[ "__util__" ].$list();
			var html = "<li data-uid='" + data[ "id" ]  + "' data-index=" + index + "'>" +
					   "<img src='"  + ( data[ "profilePic" ] || "/assets/sunny/2.0/img/default-profile-80x80.png" ) + "' width='32px'>" +
					   "<p>" +
					   "<strong> <i>" + data[ "name" ] + "</i> " + ( departments == "" ? "" : "(" + departments + ")" )  + "</strong><br>" +
					   "<span>&nbsp;</span>" + 
					   "</P>" +
				       "</li>" ;
			
			$list.append( html );
			this.countItem++;
		},
		selectItem: function( index ){
			//unselect
			if( this.indexSelected >= 0 ) {
				$item = MentionSelectBox[ "__util__" ].$item( this.indexSelected ).removeClass( "selected" );
			}
			// select
			MentionSelectBox[ "__util__" ].$item( this.indexSelected = index ).addClass( "selected" );
		},
		selectPrevItem: function() {
			var index = (  this.indexSelected == 0 ) ?  this.countItem - 1 : this.indexSelected - 1;
			setTimeout( function() {
				this.selectItem( index );
			}.bind( this ), 1);
		},
		selectNextItem: function() {
			var index = (  this.indexSelected == this.countItem - 1 ) ? 0 : this.indexSelected + 1;
			setTimeout( function() {
				this.selectItem( index );
			}.bind( this ), 1);
		},	
		popup: function() {
			if( this.countItem == 0 ) {
				this.hide();
				return;
			}
			var
			$grobalWrap = $( "#rw-snn-wrap" ),
			$box = MentionSelectBox[ "__util__" ].$box();
			
			$box.show();
			
			if( !$grobalWrap.hasClass( "_theater" ) ) {
				var
				$w = $( window ),
				offset = $box.offset(),
				bh = $box.height(),
				ws = $w.scrollTop(),
				wh = $w.height();
				
				( ( offset.top - ws + bh ) > wh + 10 )  && $box.css({ "top": -1 * ( bh + 34 ) });
			}
			
			// event binding for document's keydown
			$( document ).bind( "keydown", MentionSelectBox[ "__event__"].onKeydown );
			$( document ).onHMOClick( null, MentionSelectBox[ "__event__"].onDocumentClicked );
		},
		hide: function() {
			if( !this.isVisible() ) {
				return;
			}
			var
			$grobalWrap = $( "#rw-snn-wrap" ),
			$box = MentionSelectBox[ "__util__" ].$box();
			
			$box.hide();
			
			if( !$grobalWrap.hasClass( "_theater" ) ) {
				$box.css({ "top": "0px" });
			}
			this.indexSelected = -1;
			// event unbinding
			$( document ).unbind( "keydown", MentionSelectBox[ "__event__"].onKeydown );
			$( document ).offHMOClick( null, MentionSelectBox[ "__event__"].onDocumentClicked );
		},
		isVisible: function() {
			return MentionSelectBox[ "__util__" ].$box().is( ":visible" );
		}
	},
	/* event */
	__event__: {
		onItemClick: function( $e ) {
			var $li = $(this);
			MentionSelectBox[ "__ui__" ].selectItem( parseInt( $li.attr( "data-index" ), 10 ) );
			MentionInputListener.onSelectChanged();
			MentionSelectBox[ "__ui__" ].hide();
		},
		onItemMouseover: function( $e ) {
			var $li = $(this);
			MentionSelectBox[ "__ui__" ].selectItem( parseInt( $li.attr( "data-index" ), 10 ) );
		},
		onKeydown: function( $e ) {
			if( $e.which == 40  ) {
				$e.preventDefault();
				MentionSelectBox[ "__ui__" ].selectNextItem();
			} else if( $e.which == 38 ) {
				$e.preventDefault();
				MentionSelectBox[ "__ui__" ].selectPrevItem();
			} else if( $e.which == 13 ) {
				if( !MentionSelectBox[ "__ui__" ].isVisible() ) {
					return;
				}
				$e.preventDefault();
				MentionInputListener.onSelectChanged();
				MentionSelectBox[ "__ui__" ].hide();
			} else if( $e.which == 27 ) {
				MentionSelectBox[ "__ui__" ].hide();
			} else {
				//$.log( "MentionINputListener[ '__event__' ].onKeydown : " + e.which );
			}
		},
		onDocumentClicked: function( $event ) {
			MentionSelectBox[ "__ui__" ].hide();	
		}
	},
	/* util */
	__util__: {
		_$grobalWrap: null,
		init: function(){
			this._$grobalWrap = $( "#rw-snn-wrap" );
		},
		$box: function() {
			var
			$input = MentionInputListener[ "__core__" ].__input;
			if( !this._$grobalWrap.hasClass( "_theater" ) ) {
				return $input.parents( ".ui-composer-out" ).find( ".mention-select-box" ); 
			}
			// support photo viewer
			var
			$boxWrap =  $( "#mention-select-box-wrap" ),
			$box = $boxWrap.find( ".mention-select-box" );
			return $box;
		},
		$list: function() {
			var $box = this.$box();
			return $box.children( "ul" );
		},
		$item: function( index /* zero based */ ) {
			var
			$list = this.$list(),
			$items = $list.children();
			return $( $items[ index ] );
		}
	}
};

//Export Functions of MentionInputListener's Internel Objects ( external access explicitly )
$.extend( MentionSelectBox, {
	init: MentionSelectBox[ "__operation__" ].init,
	getItemSelected: MentionSelectBox[ "__operation__" ].getItemSelected,
	selectItem: MentionSelectBox[ "__ui__" ].selectItem.bind( MentionSelectBox[ "__ui__" ] ),
	update: MentionSelectBox[ "__ui__" ].update.bind( MentionSelectBox[ "__ui__" ] ),
	popup: MentionSelectBox[ "__ui__" ].popup.bind( MentionSelectBox[ "__ui__" ] ),
	hide: MentionSelectBox[ "__ui__" ].hide.bind( MentionSelectBox[ "__ui__" ] ),
	isVisible: MentionSelectBox[ "__ui__" ].isVisible
});
