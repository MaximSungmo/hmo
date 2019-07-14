//
//	MentionInputListener 0.5.0
//
var MentionInputListener = {
		
	/////////////////////////////////////////////////////////////////////////////
	/* event */
	__event__: {
		cache: {},
		onInit: function() {
			MentionSelectBox.init();
		},
		onContext: function( text, offset ) {
			
//			logger.log( "+++++" + text + "++++++ : [" + offset + "]" );
			
			// check text
			if( text == "" ) {
//				logger.log( "-> lineText is null or not assigned : exit");
				this.cache[ "text" ] = "";
				MentionSelectBox.hide();			
				return;
			}
			
			// get word
			var word = MentionInputListener[ "__util__" ].word( text, offset );
			
			// check word
			if( word == null ) {
//				logger.log( "word is null" );
				this.cache[ "text" ] = "";
				MentionSelectBox.hide();			
				return;
			}
			
			//get text
			var dist = word[ "text" ].length - ( word[ "endAt" ] - offset ),
				text = word[ "text" ].substring( 1, dist );
			
			if( text == this.cache[ "text" ] ) {
//				logger.log( "-> text is same as the previous : exit");
				return;
			}

			// cache text
			this.cache[ "text" ] = text;
			
			// check text on cache 
			if( this.cache[ "text" ] == "" ) {
//				logger.log( "-> text is empty" );
				this.cache[ "text" ] = "";
				MentionSelectBox.hide();			
				return;
			}
			
			//
			logger.log( "--> fetch data [" +  this.cache[ "text" ]  + "]" );
//			$.ajax( {
//				url: "",
//				asynchonous: false,
//				dataType: "json",
//				contentType: 'application/json',
//				data: {
//					"name" : this.cache[ "text" ]
//				},
//				success: this.onDataRequest.bind( this ) 	
//			} );
			for( var i = 0; i < 1000000; i++ ) {
			}
			this.onDataRequest();
		},
		onDataRequest: function ( data ) {
			// test data
//			data = [{ 
//				img: "http://www.guillobelbjj.com/wp-content/uploads/2010/11/icon-schedule-lg.png",
//				name: "일정",
//				rel: "",
//				data: ""
//		    },{
//				img: "http://www.hel.fi/wps/wcm/connect/ef9aab00484dbc2baa71eb2ef3af4b66/42/Kokoustila_m.jpg?MOD=AJPERES&CACHEID=ef9aab00484dbc2baa71eb2ef3af4b66/42",
//				name: "장소",
//				rel: "",
//				data: ""
//		    },{
//				img: "http://carstenknoch.com/wp-content/uploads/2013/02/MS-Project.png",
//				name: "작업",
//				rel: "",
//				data: ""
//		    },{
//				img: "https://www.google.co.kr/images/icons/onebox/calculator-40.gif",
//				name: "홍두깨",
//				rel: "친구",
//				data: ""
//		    },{
//				img: "https://www.google.co.kr/images/icons/onebox/calculator-40.gif",
//				name: "홍익대학교",
//				rel: "친구",
//				data: ""
//		    }];
			
//			data = [{ 
//				img:  "http://carstenknoch.com/wp-content/uploads/2013/02/MS-Project.png",
//				name: "A1-0002 프로젝트",
//				rel:  "발주 S물산 2013-02-01",
//				data: ""
//		    },{
//				img: "http://carstenknoch.com/wp-content/uploads/2013/02/MS-Project.png",
//				name: "A1-0001 프로젝트",
//				rel: "발주 K사 2013-01-08",
//				data: ""
//		    },{
//				img: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRstzhajbjhPWERNeSGQr7_gC3-BXOIJGMZBWkAcqX6CfRpfkOijA",
//				name: "회의장소",
//				rel: "K10동 4층 A1컨퍼런스 룸",
//				data: ""
//		    },{
//				img: "https://www.google.co.kr/images/icons/onebox/calculator-40.gif",
//				name: "홍두깨",
//				rel: "친구",
//				data: ""
//		    },{
//				img: "https://www.google.co.kr/images/icons/onebox/calculator-40.gif",
//				name: "홍익대학교",
//				rel: "친구",
//				data: ""
//		    }];	
		
//			data = [{ 
//			img:  "http://www.hel.fi/wps/wcm/connect/ef9aab00484dbc2baa71eb2ef3af4b66/42/Kokoustila_m.jpg?MOD=AJPERES&CACHEID=ef9aab00484dbc2baa71eb2ef3af4b66/42",
//			name: "회의장소(K1동-2층 B1)",
//			rel:  "K1동-2층 B1",
//			data: ""
//	    },{
//			img: "http://icons.iconarchive.com/icons/babasse/imod/256/Lab-icon.png",
//			name: "연구실(R1-1004)",
//			rel: "연구실",
//			data: ""
//	    },{
//			img: "http://www.hel.fi/wps/wcm/connect/ef9aab00484dbc2baa71eb2ef3af4b66/42/Kokoustila_m.jpg?MOD=AJPERES&CACHEID=ef9aab00484dbc2baa71eb2ef3af4b66/42",
//			name: "K10동 4층 A1컨퍼런스 룸",
//			rel: "회의장소",
//			data: ""
//	    },{
//			img: "https://www.google.co.kr/images/icons/onebox/calculator-40.gif",
//			name: "홍두깨",
//			rel: "친구",
//			data: ""
//	    },{
//			img: "https://www.google.co.kr/images/icons/onebox/calculator-40.gif",
//			name: "홍익대학교",
//			rel: "친구",
//			data: ""
//	    }];				
	
			
			data = [{ 
				img:  "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-prn1/c10.10.160.160/22505_536211706393773_1504278633_a.jpg",
				name: "홍문수",
				rel:  "개발이사",
				data: ""
		    },{
				img: "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-ash3/c12.12.156.156/s100x100/529412_10200498991870916_1988247985_a.jpg",
				name: "홍성묵",
				rel: "선임연구원",
				data: ""
		    },{
				img:  "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-ash4/c34.0.113.113/s100x100/252231_1002029915278_1941483569_a.jpg",
				name: "홍길동",
				rel:  "팀장",
				data: ""
		    },{
				img: "https://www.google.co.kr/images/icons/onebox/calculator-40.gif",
				name: "김개발(선임연구원)",
				rel: "친구",
				data: ""
		    },{
				img: "https://www.google.co.kr/images/icons/onebox/calculator-40.gif",
				name: "이자인(디자이너)",
				rel: "친구",
				data: ""
		    }];	
			
			
			//
			MentionSelectBox.update( data );
			MentionSelectBox.popup( YAEditor.rectCaret( "after", MentionInputListener[ "__util__" ].triggerChar ) );
			MentionSelectBox.selectItem( 0 );
		},
		onSelectChanged: function() {
			var itemData = MentionSelectBox.getItemSelected();
			if( !itemData ) {
				return;
			}
			// 상황에 따라 바꾸기!
			YAEditor.html( "<span style='text-decoration:underline; color:#04f'><strong>" + itemData.name + "</strong></span>", "after", MentionInputListener[ "__util__" ].triggerChar );
		}
	},
	
	
	/////////////////////////////////////////////////////////////////////////////
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
					if( charCode == 32 || charCode == 10 || charCode == 160 ) {
						triggerOn = false;
						endAt = i;
						words[ words.length ] = { text: word, beginAt: beginAt, endAt : endAt };
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
		}
	}
};


//
// Define MentionInputListener Plugin
//
$.extend( MentionInputListener, {
	type: "listener",
	onInit: MentionInputListener[ "__event__" ].onInit.bind( MentionInputListener[ "__event__" ] ),
	onContext: MentionInputListener[ "__event__" ].onContext.bind( MentionInputListener[ "__event__" ] ),
	onSelectChanged: MentionInputListener[ "__event__" ].onSelectChanged.bind( MentionInputListener[ "__event__" ] )
});


//
// MentionSelectBox
//
var MentionSelectBox = {
	
	/////////////////////////////////////////////////////////////////////////////
	/* operation */
	__operation__: {	
		init: function() {
			var $el = $( ".mention-select-box" );
			if( $el.length != 1 ) {
				logger.error( "MentionSelectBox : 1 More Element or None Selected. Not Applied" );
				return;
			}
			
			var $box = $( $el.get( 0 ) );
			MentionSelectBox[ "__ui__" ].boxHeight = $box.height();
			$box.attr( "id", MentionSelectBox[ "__util__" ].idBox() ).hide();
			
			var $list = $( $box.children( "ul" )[ 0 ] );
			if( !$list ) {
				logger.error( "MentionSelectBox : List Element Ommitted" );
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
				name: $item.find( "p strong" ).text(),
				id: "1234567890"
			};
		}
	},
	
	/////////////////////////////////////////////////////////////////////////////
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
			MentionSelectBox[ "__util__" ].$box().scrollTop( 0 );
		},
		insertItem: function( data ) {
			var $list = MentionSelectBox[ "__util__" ].$list();
			var html = "<li>" +
					   "<img src='"  + data[ "img" ] + "' width='32px'/>" +
					   "<p>" +
					   "<strong>" + data[ "name"] + "</strong><br/>" +
					   "<span>" + data[ "rel" ] + "</span>" + 
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
			
			// scrollTop
			this.scroll();
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
			var $box = MentionSelectBox[ "__util__" ].$box(),
				$firstItem = MentionSelectBox[ "__util__" ].$item( 0 );
			
			$box.css( { left: rect.left + "px", top: ( rect.top + rect.height ) + "px" } ).show();
			
			//calc item dimension
			this.itemHeight = ( $firstItem.length ) ? $firstItem.height() : 0;
			this.itemCountInBox = ( this.itemHeight ) ? Math.ceil( this.boxHeight / this.itemHeight ) : 0;
			
			// event binding for document's keydown
			YAEditor.$iframe().contents().bind( "keydown", MentionSelectBox[ "__event__"].onKeydown.bind( MentionSelectBox[ "__event__"] ) );
	
//			logger.log( "item height:" + this.__itemHeight + ", item count in box:" + this.__itemCountInBox );
		},
		hide: function() {
			if( !this.isVisible() ) {
				return;
			}

			//
			MentionSelectBox[ "__util__" ].$box().hide();
			this.indexSelected = -1;
	
			// event unbinding for document's keydown
			YAEditor.$iframe().contents().unbind( "keydown" );
		},
		scroll: function() {
			var top = ( this.indexSelected >= this.itemCountInBox )  ?  ( this.indexSelected - this.itemCountInBox + 1 ) * this.itemHeight : 0;
			MentionSelectBox[ "__util__" ].$box().scrollTop( top );
		},
		isVisible: function() {
			return MentionSelectBox[ "__util__" ].$box().is( ":visible" );
		}
	},
	
	/////////////////////////////////////////////////////////////////////////////
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
	
	
	/////////////////////////////////////////////////////////////////////////////
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

//
//Export Functions of MentionInputListener's Internel Objects ( external access explicitly )
//
$.extend( MentionSelectBox, {
	init: MentionSelectBox[ "__operation__" ].init,
	getItemSelected: MentionSelectBox[ "__operation__" ].getItemSelected,
	selectItem: MentionSelectBox[ "__ui__" ].selectItem.bind( MentionSelectBox[ "__ui__" ] ),
	update: MentionSelectBox[ "__ui__" ].update.bind( MentionSelectBox[ "__ui__" ] ),
	popup: MentionSelectBox[ "__ui__" ].popup.bind( MentionSelectBox[ "__ui__" ] ),
	hide: MentionSelectBox[ "__ui__" ].hide.bind( MentionSelectBox[ "__ui__" ] )
});
