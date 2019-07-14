/**
 * MentionInputListener 0.8.0
 * 
 */

var MentionInputListener = {
/* core */
	__core__: {
		__input: null,
		__inputText: "",
		__mentionCount: 0,
		__contextWordData: null,
		__mentionData:{},
		addMentionData: function( data ) {
//			$.log( "[contextWordData]" +  this.__contextWordData[ "beginAt" ] + ":" + this.__contextWordData[ "endAt" ] + ":" + this.__contextWordData[ "textSelected" ] );
//			$.log( "[data]" +  data[ "name" ] + ":" + data[ "id" ]  );
			var contextWordData = this.__contextWordData,
				beginAt =  this.__contextWordData[ "beginAt" ],
				endAt = this.__contextWordData[ "endAt" ],
				textSelected = this.__contextWordData[ "textSelected" ],
				mentionData = {
					"beginAt": beginAt,
					"endAt": beginAt + data["name"].length-1,
					"text": data["name"],
					"id": data["id"]
				};
			
			var inputId = this.__input.attr("id"),
				inputText = this.__input.val();
				inputText = inputText.slice( 0, beginAt ) + mentionData[ "text" ] + inputText.slice( endAt+1 );  
				
			//$.log( "+++++" +  inputText + "+++++[" + mentionData["beginAt"] +  ":"  + mentionData["endAt"] + "]" );
			this.__input.val( inputText );
			
			var count = this.__mentionData[ inputId ].length,
				index = count;
			
			this.__mentionData[ inputId ].splice( index, 0, mentionData );
			
			this.updateMentionText( inputText );
		},
		updateMentionText: function( text ) {
			
			if( this.__inputText == text ) {
				return;
			}
			
			this.__inputText = text;
			
			var inputId = this.__input.attr("id"),
				inputText = this.__inputText,
				mentionText = this.__inputText;
				mentionTextReaplaceMaps = [];
				count = this.__mentionData[ inputId ].length;
				
			//$.log( "1. >>>>>>>>" + mentionText + "<<<<<<<" );
			for( var index = 0; index < count; index++ ){
				var data = this.__mentionData[ inputId ][ index ],
					beginAt = data[ "beginAt" ],
					endAt = data[ "endAt" ],
					text  = inputText.substr( beginAt, endAt - beginAt + 1 ),
					textA = inputText.substr( beginAt+1, endAt - beginAt + 1 );
					textB = inputText.substr( beginAt-1, endAt - beginAt + 1 );
				
				//$.log( "=" + beginAt + "=" + endAt + "=" + text + "=" + textA + "=" + textB );
				
				if( text == data["text"] ) {
					var hashing = MentionInputListener["__util__"].hashing( data["text"], index );
					mentionText = mentionText.slice( 0, beginAt ) + hashing + mentionText.slice( endAt+1 );
					mentionTextReaplaceMaps.push({
						"hashing": hashing,
						"text": "@[" + data[ "id" ] + ":" + data[ "text" ] + "]"
					});
					//$.log( "2. >>>>>>>>" + mentionText + "<<<<<<<" );
				} else if( textA == data["text"] ) {
					$.log( "앞에 글자가 하나 늘어난 경우:" + index );
					beginAt += 1;
					endAt += 1;
					this.__mentionData[ inputId ].splice( index, 1, {
						"beginAt": beginAt,
						"endAt": endAt,
						"text": data["text"],
						"id": data["id"]							
					});
					var hashing = MentionInputListener["__util__"].hashing( data["text"], index );
					mentionText = mentionText.slice( 0, beginAt ) + hashing + mentionText.slice( endAt+1 );
					mentionTextReaplaceMaps.push({
						"hashing": hashing,
						"text": "@[" + data[ "id" ] + ":" + data[ "text" ] + "]"
					});
				} else if( textB == data["text"] ) {
					$.log( "앞에 글자가 줄어든 경우:" + index );
					beginAt -= 1;
					endAt -= 1;
					this.__mentionData[ inputId ].splice( index, 1, {
						"beginAt": beginAt,
						"endAt": endAt,
						"text": data["text"],
						"id": data["id"]							
					});
					var hashing = MentionInputListener["__util__"].hashing( data["text"], index );
					mentionText = mentionText.slice( 0, beginAt ) + hashing + mentionText.slice( endAt+1 );
					mentionTextReaplaceMaps.push({
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
					MentionInputListener[ "__core__" ].__mentionData[ inputId ].splice( index--, 1 );
					count--;
				}
			}
			
			//$.log( "3. >>>>>>>>" + mentionText + "<<<<<<<" );
			$.each( mentionTextReaplaceMaps, function(index, map) {
				mentionText = mentionText.replace( map[ "hashing" ], map[ "text" ] );
			});
			
			//
			var $mentionText = MentionInputListener[ "__util__" ].$mentionText();
			$mentionText.val( mentionText );
			
			//
			var	$highlighter = MentionInputListener[ "__util__" ].$highlighter(),
				highlighterHTML = mentionText,
				highlighterTextReplaceMaps = [],
				mentionItems = mentionText.match(/@\[[0-9]+:([^0-9]+)\]/g) || [];
			
			$.each( mentionItems, function( index, mentionText ) {
				var	s = mentionText.split(":"),
//					highlighterTag = "<b>" + s[1].replace( "]", "</b>" );
					highlighterTag = "<strong>" + s[1].replace( "]", "</strong>" );
				
				highlighterTextReplaceMaps.push({
					"mentionText": mentionText,
					"highlighterTag": highlighterTag
				});
				//$.log( "@@@@" + index );
			});
			
			$.each( highlighterTextReplaceMaps, function( index, map ){
				highlighterHTML = highlighterHTML.replace( map[ "mentionText" ], map[ "highlighterTag" ] );
			});
			
			$highlighter.html( highlighterHTML.replace( /\r\n/gi, "<br>" ).replace( /\n/gi, "<br>" ) );
			
			if( this.__mentionData[ inputId ].length > 0 ) {
				$highlighter.removeClass( "hidden-elem" );
			} else {
				$highlighter.addClass( "hidden-elem" );
			}
		}
	},	
	/* event */
	__event__: {
		cache: {},
		onInit: function() {
			MentionSelectBox.init();
		},
		onContext: function( input, text, offset ) {
			//$.log( "+++++" + text + "++++++ : [" + offset + "]" );
			MentionInputListener[ "__core__" ].__input = input;
			
			// mention data cache set
			var inputId = MentionInputListener[ "__core__" ].__input.attr("id");
			if( !MentionInputListener[ "__core__" ].__mentionData[ inputId ] ) { 
				MentionInputListener[ "__core__" ].__mentionData[ inputId ] = [];
			}

			//
			MentionInputListener[ "__core__" ].updateMentionText( text );
			
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
			//$.log( "++++++++++++++++++++" + JSON.stringify( result ) + "++++++++++++++++++++" );
			MentionSelectBox.update( result.data );
			MentionSelectBox.popup( StoryWriter.rect.call( MentionInputListener[ "__core__" ].__input ) );
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
		}		
	}
};

// Define MentionInputListener Plugin
$.extend( MentionInputListener, {
	type: "listener",
	onInit: MentionInputListener[ "__event__" ].onInit.bind( MentionInputListener[ "__event__" ] ),
	onContext: MentionInputListener[ "__event__" ].onContext.bind( MentionInputListener[ "__event__" ] ),
	onSelectChanged: MentionInputListener[ "__event__" ].onSelectChanged.bind( MentionInputListener[ "__event__" ] )
});

// MentionSelectBox
var MentionSelectBox = {
	/* operation */
	__operation__: {	
		init: function() {
			var $el = $( ".mention-select-box" );
			if( $el.length != 1 ) {
				$.error( "MentionSelectBox : 1 More Element or None Selected. Not Applied" );
				return;
			}
			
			var $box = $( $el.get( 0 ) );
			MentionSelectBox[ "__ui__" ].boxHeight = $box.height();
			$box.attr( "id", MentionSelectBox[ "__util__" ].idBox() ).hide();
			
			var $list = $( $box.children( "ul" )[ 0 ] );
			if( !$list ) {
				$.error( "MentionSelectBox : List Element Ommitted" );
				return;
			}
			$list.attr( "id",  MentionSelectBox[ "__util__" ].idList() );
		},
		getItemSelected: function() {
			var indexSelected =  MentionSelectBox[ "__ui__" ].indexSelected; 
			if( indexSelected < 0 ) {
				return null;
			}
			var $item = MentionSelectBox[ "__util__" ].$item( indexSelected );
			return {
				name: $item.find( "p strong i" ).text(),
				id: $item.attr("data-uid")
			};
		}
	},
	/* ui */
	__ui__: {
		indexSelected: -1,
		boxHeight: 0,
		itemHeight: 0,
		itemCountInBox: 0,
		countItem: 0,
		update: function( data ){
			this.deleteAllItems();
			$.each( data, function( index, d ){
				this.insertItem( d );
			}.bind( this ) );
		},
		deleteAllItems: function() {
			this.indexSelected = -1;
			this.countItem = 0;
			MentionSelectBox[ "__util__" ].$list().html( "" );
		},
		insertItem: function( data ) {
			var departments = "", countDepartment = data[ "departmentStrings" ] ? data[ "departmentStrings" ].length : 0;  
			for( var i = 0; i < countDepartment; i++ ) {
				departments += data[ "departmentStrings" ][ i ] ? ( departments == "" ? data[ "departmentStrings" ][ i ] : ", " + data[ "departmentStrings" ][ i ] ) : "";
			}
			
			var $list = MentionSelectBox[ "__util__" ].$list();
			var html = "<li data-uid='" + data[ "id" ]  + "'>" +
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
		popup: function( rect ) {
			if( this.countItem == 0 ) {
				this.hide();
				return;
			}
			
			var $box = MentionSelectBox[ "__util__" ].$box(),
				$firstItem = MentionSelectBox[ "__util__" ].$item( 0 );
			
			//$box.css( { left: rect.left + "px", top: ( rect.bottom + 10) + "px" } )
			$box.show();
			
			//calc item dimension
			this.itemHeight = ( $firstItem.length ) ? $firstItem.height() : 0;
			this.itemCountInBox = ( this.itemHeight ) ? Math.ceil( this.boxHeight / this.itemHeight ) : 0;
			
			// event binding for document's keydown
			$(document).bind( "keydown", MentionSelectBox[ "__event__"].onKeydown );
			//$.log( "item height:" + this.__itemHeight + ", item count in box:" + this.__itemCountInBox );
		},
		hide: function() {
			if( !this.isVisible() ) {
				return;
			}
			//
			MentionSelectBox[ "__util__" ].$box().hide();
			this.indexSelected = -1;
	
			// event unbinding for document's keydown
			$(document).unbind( "keydown", MentionSelectBox[ "__event__"].onKeydown );
		},
		isVisible: function() {
			return MentionSelectBox[ "__util__" ].$box().is( ":visible" );
		}
	},
	/* event */
	__event__: {
		onKeydown: function( e ) {
			if( e.which == 40  ) {
				e.preventDefault();
				MentionSelectBox[ "__ui__" ].selectNextItem();
			} else if( e.which == 38 ) {
				e.preventDefault();
				MentionSelectBox[ "__ui__" ].selectPrevItem();
			} else if( e.which == 13 ) {
				if( !MentionSelectBox[ "__ui__" ].isVisible() ) {
					return;
				}
				e.preventDefault();
				MentionInputListener.onSelectChanged();
				MentionSelectBox[ "__ui__" ].hide();
			} else if( e.which == 27 ) {
				MentionSelectBox[ "__ui__" ].hide();
			} else {
				$.log( "MentionINputListener[ '__event__' ].onKeydown : " + e.which );
			}
		}
	},
	/* util */
	__util__: {
		PREFIX: "MeNtIoN_SeLeCtBoX_",	
		cache:{},
		idBox: function() {
			return this.PREFIX + "Box" ;
		},
		idList: function() {
			return this.PREFIX + "LisT";
		},
		$box: function() {
			return this.cache[ "box" ] || ( this.cache[ "box" ] = $( "#" + this.idBox() ) ) ;
		},
		$list: function() {
			return this.cache[ "list" ] || ( this.cache[ "list" ] = $( "#" + this.idList() ) ) ;
		},
		$item: function( index /* zero based */ ) {
			return $( "#" + this.idList() + " " + "li:nth-child(" + ( index + 1 ) + ")");
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
	hide: MentionSelectBox[ "__ui__" ].hide.bind( MentionSelectBox[ "__ui__" ] )
});
