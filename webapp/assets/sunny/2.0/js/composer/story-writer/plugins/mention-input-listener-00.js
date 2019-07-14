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
		__diff:null,
		addMentionData: function( data ) {
//			$.log( "[contextWordData]" +  this.__contextWordData[ "beginAt" ] + ":" + this.__contextWordData[ "endAt" ] + ":" + this.__contextWordData[ "textSelected" ] );
//			$.log( "[data]" +  data[ "name" ] + ":" + data[ "id" ]  );
			var inputId = this.__input.attr("id"),
				contextWordData = this.__contextWordData,
				beginAt =  this.__contextWordData[ "beginAt" ],
				endAt = this.__contextWordData[ "endAt" ],
				textSelected = this.__contextWordData[ "textSelected" ],
				mentionData = {
					"beginAt": beginAt,
					"endAt": beginAt + data["name"].length-1,
					"text": data["name"],
					"id": data["id"]
				};
			// add mention data
			var count = this.__mentionData[ inputId ].length, index = 0;
			for( var i = 0; i < count; i++ ) {
				var data = this.__mentionData[ inputId ][ i ];
				if( mentionData[ "beginAt" ] <= data[ "beginAt" ] ){
					break;
				}
				index++;
			}
			this.__mentionData[ inputId ].splice( index, 0, mentionData );
			$.log( this.__mentionData[ inputId ] );
			
			// update textarea
			inputText = this.__input.val();
			inputText = inputText.slice( 0, beginAt ) + mentionData[ "text" ] + inputText.slice( endAt+1 );  
			this.__input.val( inputText );

			// update storage
			// this.updateMentionText();
			
		},
		updateMentionData: function() {
			//
			var inputText = this.__input.val();
			
			//
			if( this.__inputText == inputText ) {
				return;
			}

			//
			var diff = this.__diff.diff_main( inputText, this.__inputText ),
				diffCount = diff.length;
			// $.log( "" + inputText + ":" + this.__inputText );

			// 
			for( var i = 0; i < diffCount; i++ ){
				var d = diff[i], diffCode = d[ 0 ], diffString = d[ 1 ];
				$.log( "diff>" + diffCode + ":" + diffString );
			}
			
			//
			
		},
		updateMentionText: function() {
			this.__inputText = inputText;
		}
	},	
	/* event */
	__event__: {
		cache: {},
		onInit: function() {
			MentionInputListener[ "__core__" ].__diff = new diff_match_patch();
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
			// MentionInputListener[ "__core__" ].updateMentionText( text );
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
			//$.log( $.stringify( result ) );
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
				name: $item.find( "p strong" ).text(),
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
			var $list = MentionSelectBox[ "__util__" ].$list();
			var html = "<li data-uid='" + data[ "id" ]  + "'>" +
					   "<img src='"  + data[ "profilePic" ] + "' width='32px'>" +
					   "<p>" +
					   "<strong>" + data[ "name" ] + "</strong><br>" +
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
			
			$box.css( { left: rect.left + "px", top: ( rect.bottom + 10) + "px" } ).show();
			
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




/**
 * Diff Match and Patch
 *
 * Copyright 2006 Google Inc.
 * http://code.google.com/p/google-diff-match-patch/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
(function(){function diff_match_patch(){this.Diff_Timeout=1;this.Diff_EditCost=4;this.Match_Threshold=0.5;this.Match_Distance=1E3;this.Patch_DeleteThreshold=0.5;this.Patch_Margin=4;this.Match_MaxBits=32}
diff_match_patch.prototype.diff_main=function(a,b,c,d){"undefined"==typeof d&&(d=0>=this.Diff_Timeout?Number.MAX_VALUE:(new Date).getTime()+1E3*this.Diff_Timeout);if(null==a||null==b)throw Error("Null input. (diff_main)");if(a==b)return a?[[0,a]]:[];"undefined"==typeof c&&(c=!0);var e=c,f=this.diff_commonPrefix(a,b);c=a.substring(0,f);a=a.substring(f);b=b.substring(f);var f=this.diff_commonSuffix(a,b),g=a.substring(a.length-f);a=a.substring(0,a.length-f);b=b.substring(0,b.length-f);a=this.diff_compute_(a,
b,e,d);c&&a.unshift([0,c]);g&&a.push([0,g]);this.diff_cleanupMerge(a);return a};
diff_match_patch.prototype.diff_compute_=function(a,b,c,d){if(!a)return[[1,b]];if(!b)return[[-1,a]];var e=a.length>b.length?a:b,f=a.length>b.length?b:a,g=e.indexOf(f);return-1!=g?(c=[[1,e.substring(0,g)],[0,f],[1,e.substring(g+f.length)]],a.length>b.length&&(c[0][0]=c[2][0]=-1),c):1==f.length?[[-1,a],[1,b]]:(e=this.diff_halfMatch_(a,b))?(f=e[0],a=e[1],g=e[2],b=e[3],e=e[4],f=this.diff_main(f,g,c,d),c=this.diff_main(a,b,c,d),f.concat([[0,e]],c)):c&&100<a.length&&100<b.length?this.diff_lineMode_(a,b,
d):this.diff_bisect_(a,b,d)};
diff_match_patch.prototype.diff_lineMode_=function(a,b,c){var d=this.diff_linesToChars_(a,b);a=d.chars1;b=d.chars2;d=d.lineArray;a=this.diff_main(a,b,!1,c);this.diff_charsToLines_(a,d);this.diff_cleanupSemantic(a);a.push([0,""]);for(var e=d=b=0,f="",g="";b<a.length;){switch(a[b][0]){case 1:e++;g+=a[b][1];break;case -1:d++;f+=a[b][1];break;case 0:if(1<=d&&1<=e){a.splice(b-d-e,d+e);b=b-d-e;d=this.diff_main(f,g,!1,c);for(e=d.length-1;0<=e;e--)a.splice(b,0,d[e]);b+=d.length}d=e=0;g=f=""}b++}a.pop();return a};
diff_match_patch.prototype.diff_bisect_=function(a,b,c){for(var d=a.length,e=b.length,f=Math.ceil((d+e)/2),g=f,h=2*f,j=Array(h),i=Array(h),k=0;k<h;k++)j[k]=-1,i[k]=-1;j[g+1]=0;i[g+1]=0;for(var k=d-e,q=0!=k%2,r=0,t=0,p=0,w=0,v=0;v<f&&!((new Date).getTime()>c);v++){for(var n=-v+r;n<=v-t;n+=2){var l=g+n,m;m=n==-v||n!=v&&j[l-1]<j[l+1]?j[l+1]:j[l-1]+1;for(var s=m-n;m<d&&s<e&&a.charAt(m)==b.charAt(s);)m++,s++;j[l]=m;if(m>d)t+=2;else if(s>e)r+=2;else if(q&&(l=g+k-n,0<=l&&l<h&&-1!=i[l])){var u=d-i[l];if(m>=
u)return this.diff_bisectSplit_(a,b,m,s,c)}}for(n=-v+p;n<=v-w;n+=2){l=g+n;u=n==-v||n!=v&&i[l-1]<i[l+1]?i[l+1]:i[l-1]+1;for(m=u-n;u<d&&m<e&&a.charAt(d-u-1)==b.charAt(e-m-1);)u++,m++;i[l]=u;if(u>d)w+=2;else if(m>e)p+=2;else if(!q&&(l=g+k-n,0<=l&&(l<h&&-1!=j[l])&&(m=j[l],s=g+m-l,u=d-u,m>=u)))return this.diff_bisectSplit_(a,b,m,s,c)}}return[[-1,a],[1,b]]};
diff_match_patch.prototype.diff_bisectSplit_=function(a,b,c,d,e){var f=a.substring(0,c),g=b.substring(0,d);a=a.substring(c);b=b.substring(d);f=this.diff_main(f,g,!1,e);e=this.diff_main(a,b,!1,e);return f.concat(e)};
diff_match_patch.prototype.diff_linesToChars_=function(a,b){function c(a){for(var b="",c=0,f=-1,g=d.length;f<a.length-1;){f=a.indexOf("\n",c);-1==f&&(f=a.length-1);var r=a.substring(c,f+1),c=f+1;(e.hasOwnProperty?e.hasOwnProperty(r):void 0!==e[r])?b+=String.fromCharCode(e[r]):(b+=String.fromCharCode(g),e[r]=g,d[g++]=r)}return b}var d=[],e={};d[0]="";var f=c(a),g=c(b);return{chars1:f,chars2:g,lineArray:d}};
diff_match_patch.prototype.diff_charsToLines_=function(a,b){for(var c=0;c<a.length;c++){for(var d=a[c][1],e=[],f=0;f<d.length;f++)e[f]=b[d.charCodeAt(f)];a[c][1]=e.join("")}};diff_match_patch.prototype.diff_commonPrefix=function(a,b){if(!a||!b||a.charAt(0)!=b.charAt(0))return 0;for(var c=0,d=Math.min(a.length,b.length),e=d,f=0;c<e;)a.substring(f,e)==b.substring(f,e)?f=c=e:d=e,e=Math.floor((d-c)/2+c);return e};
diff_match_patch.prototype.diff_commonSuffix=function(a,b){if(!a||!b||a.charAt(a.length-1)!=b.charAt(b.length-1))return 0;for(var c=0,d=Math.min(a.length,b.length),e=d,f=0;c<e;)a.substring(a.length-e,a.length-f)==b.substring(b.length-e,b.length-f)?f=c=e:d=e,e=Math.floor((d-c)/2+c);return e};
diff_match_patch.prototype.diff_commonOverlap_=function(a,b){var c=a.length,d=b.length;if(0==c||0==d)return 0;c>d?a=a.substring(c-d):c<d&&(b=b.substring(0,c));c=Math.min(c,d);if(a==b)return c;for(var d=0,e=1;;){var f=a.substring(c-e),f=b.indexOf(f);if(-1==f)return d;e+=f;if(0==f||a.substring(c-e)==b.substring(0,e))d=e,e++}};
diff_match_patch.prototype.diff_halfMatch_=function(a,b){function c(a,b,c){for(var d=a.substring(c,c+Math.floor(a.length/4)),e=-1,g="",h,j,n,l;-1!=(e=b.indexOf(d,e+1));){var m=f.diff_commonPrefix(a.substring(c),b.substring(e)),s=f.diff_commonSuffix(a.substring(0,c),b.substring(0,e));g.length<s+m&&(g=b.substring(e-s,e)+b.substring(e,e+m),h=a.substring(0,c-s),j=a.substring(c+m),n=b.substring(0,e-s),l=b.substring(e+m))}return 2*g.length>=a.length?[h,j,n,l,g]:null}if(0>=this.Diff_Timeout)return null;
var d=a.length>b.length?a:b,e=a.length>b.length?b:a;if(4>d.length||2*e.length<d.length)return null;var f=this,g=c(d,e,Math.ceil(d.length/4)),d=c(d,e,Math.ceil(d.length/2)),h;if(!g&&!d)return null;h=d?g?g[4].length>d[4].length?g:d:d:g;var j;a.length>b.length?(g=h[0],d=h[1],e=h[2],j=h[3]):(e=h[0],j=h[1],g=h[2],d=h[3]);h=h[4];return[g,d,e,j,h]};
diff_match_patch.prototype.diff_cleanupSemantic=function(a){for(var b=!1,c=[],d=0,e=null,f=0,g=0,h=0,j=0,i=0;f<a.length;)0==a[f][0]?(c[d++]=f,g=j,h=i,i=j=0,e=a[f][1]):(1==a[f][0]?j+=a[f][1].length:i+=a[f][1].length,e&&(e.length<=Math.max(g,h)&&e.length<=Math.max(j,i))&&(a.splice(c[d-1],0,[-1,e]),a[c[d-1]+1][0]=1,d--,d--,f=0<d?c[d-1]:-1,i=j=h=g=0,e=null,b=!0)),f++;b&&this.diff_cleanupMerge(a);this.diff_cleanupSemanticLossless(a);for(f=1;f<a.length;){if(-1==a[f-1][0]&&1==a[f][0]){b=a[f-1][1];c=a[f][1];
d=this.diff_commonOverlap_(b,c);e=this.diff_commonOverlap_(c,b);if(d>=e){if(d>=b.length/2||d>=c.length/2)a.splice(f,0,[0,c.substring(0,d)]),a[f-1][1]=b.substring(0,b.length-d),a[f+1][1]=c.substring(d),f++}else if(e>=b.length/2||e>=c.length/2)a.splice(f,0,[0,b.substring(0,e)]),a[f-1][0]=1,a[f-1][1]=c.substring(0,c.length-e),a[f+1][0]=-1,a[f+1][1]=b.substring(e),f++;f++}f++}};
diff_match_patch.prototype.diff_cleanupSemanticLossless=function(a){function b(a,b){if(!a||!b)return 6;var c=a.charAt(a.length-1),d=b.charAt(0),e=c.match(diff_match_patch.nonAlphaNumericRegex_),f=d.match(diff_match_patch.nonAlphaNumericRegex_),g=e&&c.match(diff_match_patch.whitespaceRegex_),h=f&&d.match(diff_match_patch.whitespaceRegex_),c=g&&c.match(diff_match_patch.linebreakRegex_),d=h&&d.match(diff_match_patch.linebreakRegex_),i=c&&a.match(diff_match_patch.blanklineEndRegex_),j=d&&b.match(diff_match_patch.blanklineStartRegex_);
return i||j?5:c||d?4:e&&!g&&h?3:g||h?2:e||f?1:0}for(var c=1;c<a.length-1;){if(0==a[c-1][0]&&0==a[c+1][0]){var d=a[c-1][1],e=a[c][1],f=a[c+1][1],g=this.diff_commonSuffix(d,e);if(g)var h=e.substring(e.length-g),d=d.substring(0,d.length-g),e=h+e.substring(0,e.length-g),f=h+f;for(var g=d,h=e,j=f,i=b(d,e)+b(e,f);e.charAt(0)===f.charAt(0);){var d=d+e.charAt(0),e=e.substring(1)+f.charAt(0),f=f.substring(1),k=b(d,e)+b(e,f);k>=i&&(i=k,g=d,h=e,j=f)}a[c-1][1]!=g&&(g?a[c-1][1]=g:(a.splice(c-1,1),c--),a[c][1]=
h,j?a[c+1][1]=j:(a.splice(c+1,1),c--))}c++}};diff_match_patch.nonAlphaNumericRegex_=/[^a-zA-Z0-9]/;diff_match_patch.whitespaceRegex_=/\s/;diff_match_patch.linebreakRegex_=/[\r\n]/;diff_match_patch.blanklineEndRegex_=/\n\r?\n$/;diff_match_patch.blanklineStartRegex_=/^\r?\n\r?\n/;
diff_match_patch.prototype.diff_cleanupEfficiency=function(a){for(var b=!1,c=[],d=0,e=null,f=0,g=!1,h=!1,j=!1,i=!1;f<a.length;){if(0==a[f][0])a[f][1].length<this.Diff_EditCost&&(j||i)?(c[d++]=f,g=j,h=i,e=a[f][1]):(d=0,e=null),j=i=!1;else if(-1==a[f][0]?i=!0:j=!0,e&&(g&&h&&j&&i||e.length<this.Diff_EditCost/2&&3==g+h+j+i))a.splice(c[d-1],0,[-1,e]),a[c[d-1]+1][0]=1,d--,e=null,g&&h?(j=i=!0,d=0):(d--,f=0<d?c[d-1]:-1,j=i=!1),b=!0;f++}b&&this.diff_cleanupMerge(a)};
diff_match_patch.prototype.diff_cleanupMerge=function(a){a.push([0,""]);for(var b=0,c=0,d=0,e="",f="",g;b<a.length;)switch(a[b][0]){case 1:d++;f+=a[b][1];b++;break;case -1:c++;e+=a[b][1];b++;break;case 0:1<c+d?(0!==c&&0!==d&&(g=this.diff_commonPrefix(f,e),0!==g&&(0<b-c-d&&0==a[b-c-d-1][0]?a[b-c-d-1][1]+=f.substring(0,g):(a.splice(0,0,[0,f.substring(0,g)]),b++),f=f.substring(g),e=e.substring(g)),g=this.diff_commonSuffix(f,e),0!==g&&(a[b][1]=f.substring(f.length-g)+a[b][1],f=f.substring(0,f.length-
g),e=e.substring(0,e.length-g))),0===c?a.splice(b-d,c+d,[1,f]):0===d?a.splice(b-c,c+d,[-1,e]):a.splice(b-c-d,c+d,[-1,e],[1,f]),b=b-c-d+(c?1:0)+(d?1:0)+1):0!==b&&0==a[b-1][0]?(a[b-1][1]+=a[b][1],a.splice(b,1)):b++,c=d=0,f=e=""}""===a[a.length-1][1]&&a.pop();c=!1;for(b=1;b<a.length-1;)0==a[b-1][0]&&0==a[b+1][0]&&(a[b][1].substring(a[b][1].length-a[b-1][1].length)==a[b-1][1]?(a[b][1]=a[b-1][1]+a[b][1].substring(0,a[b][1].length-a[b-1][1].length),a[b+1][1]=a[b-1][1]+a[b+1][1],a.splice(b-1,1),c=!0):a[b][1].substring(0,
a[b+1][1].length)==a[b+1][1]&&(a[b-1][1]+=a[b+1][1],a[b][1]=a[b][1].substring(a[b+1][1].length)+a[b+1][1],a.splice(b+1,1),c=!0)),b++;c&&this.diff_cleanupMerge(a)};diff_match_patch.prototype.diff_xIndex=function(a,b){var c=0,d=0,e=0,f=0,g;for(g=0;g<a.length;g++){1!==a[g][0]&&(c+=a[g][1].length);-1!==a[g][0]&&(d+=a[g][1].length);if(c>b)break;e=c;f=d}return a.length!=g&&-1===a[g][0]?f:f+(b-e)};
diff_match_patch.prototype.diff_prettyHtml=function(a){for(var b=[],c=/&/g,d=/</g,e=/>/g,f=/\n/g,g=0;g<a.length;g++){var h=a[g][0],j=a[g][1],j=j.replace(c,"&amp;").replace(d,"&lt;").replace(e,"&gt;").replace(f,"&para;<br>");switch(h){case 1:b[g]='<ins style="background:#e6ffe6;">'+j+"</ins>";break;case -1:b[g]='<del style="background:#ffe6e6;">'+j+"</del>";break;case 0:b[g]="<span>"+j+"</span>"}}return b.join("")};
diff_match_patch.prototype.diff_text1=function(a){for(var b=[],c=0;c<a.length;c++)1!==a[c][0]&&(b[c]=a[c][1]);return b.join("")};diff_match_patch.prototype.diff_text2=function(a){for(var b=[],c=0;c<a.length;c++)-1!==a[c][0]&&(b[c]=a[c][1]);return b.join("")};diff_match_patch.prototype.diff_levenshtein=function(a){for(var b=0,c=0,d=0,e=0;e<a.length;e++){var f=a[e][0],g=a[e][1];switch(f){case 1:c+=g.length;break;case -1:d+=g.length;break;case 0:b+=Math.max(c,d),d=c=0}}return b+=Math.max(c,d)};
diff_match_patch.prototype.diff_toDelta=function(a){for(var b=[],c=0;c<a.length;c++)switch(a[c][0]){case 1:b[c]="+"+encodeURI(a[c][1]);break;case -1:b[c]="-"+a[c][1].length;break;case 0:b[c]="="+a[c][1].length}return b.join("\t").replace(/%20/g," ")};
diff_match_patch.prototype.diff_fromDelta=function(a,b){for(var c=[],d=0,e=0,f=b.split(/\t/g),g=0;g<f.length;g++){var h=f[g].substring(1);switch(f[g].charAt(0)){case "+":try{c[d++]=[1,decodeURI(h)]}catch(j){throw Error("Illegal escape in diff_fromDelta: "+h);}break;case "-":case "=":var i=parseInt(h,10);if(isNaN(i)||0>i)throw Error("Invalid number in diff_fromDelta: "+h);h=a.substring(e,e+=i);"="==f[g].charAt(0)?c[d++]=[0,h]:c[d++]=[-1,h];break;default:if(f[g])throw Error("Invalid diff operation in diff_fromDelta: "+
f[g]);}}if(e!=a.length)throw Error("Delta length ("+e+") does not equal source text length ("+a.length+").");return c};diff_match_patch.prototype.match_main=function(a,b,c){if(null==a||null==b||null==c)throw Error("Null input. (match_main)");c=Math.max(0,Math.min(c,a.length));return a==b?0:a.length?a.substring(c,c+b.length)==b?c:this.match_bitap_(a,b,c):-1};
diff_match_patch.prototype.match_bitap_=function(a,b,c){function d(a,d){var e=a/b.length,g=Math.abs(c-d);return!f.Match_Distance?g?1:e:e+g/f.Match_Distance}if(b.length>this.Match_MaxBits)throw Error("Pattern too long for this browser.");var e=this.match_alphabet_(b),f=this,g=this.Match_Threshold,h=a.indexOf(b,c);-1!=h&&(g=Math.min(d(0,h),g),h=a.lastIndexOf(b,c+b.length),-1!=h&&(g=Math.min(d(0,h),g)));for(var j=1<<b.length-1,h=-1,i,k,q=b.length+a.length,r,t=0;t<b.length;t++){i=0;for(k=q;i<k;)d(t,c+
k)<=g?i=k:q=k,k=Math.floor((q-i)/2+i);q=k;i=Math.max(1,c-k+1);var p=Math.min(c+k,a.length)+b.length;k=Array(p+2);for(k[p+1]=(1<<t)-1;p>=i;p--){var w=e[a.charAt(p-1)];k[p]=0===t?(k[p+1]<<1|1)&w:(k[p+1]<<1|1)&w|((r[p+1]|r[p])<<1|1)|r[p+1];if(k[p]&j&&(w=d(t,p-1),w<=g))if(g=w,h=p-1,h>c)i=Math.max(1,2*c-h);else break}if(d(t+1,c)>g)break;r=k}return h};
diff_match_patch.prototype.match_alphabet_=function(a){for(var b={},c=0;c<a.length;c++)b[a.charAt(c)]=0;for(c=0;c<a.length;c++)b[a.charAt(c)]|=1<<a.length-c-1;return b};
diff_match_patch.prototype.patch_addContext_=function(a,b){if(0!=b.length){for(var c=b.substring(a.start2,a.start2+a.length1),d=0;b.indexOf(c)!=b.lastIndexOf(c)&&c.length<this.Match_MaxBits-this.Patch_Margin-this.Patch_Margin;)d+=this.Patch_Margin,c=b.substring(a.start2-d,a.start2+a.length1+d);d+=this.Patch_Margin;(c=b.substring(a.start2-d,a.start2))&&a.diffs.unshift([0,c]);(d=b.substring(a.start2+a.length1,a.start2+a.length1+d))&&a.diffs.push([0,d]);a.start1-=c.length;a.start2-=c.length;a.length1+=
c.length+d.length;a.length2+=c.length+d.length}};
diff_match_patch.prototype.patch_make=function(a,b,c){var d;if("string"==typeof a&&"string"==typeof b&&"undefined"==typeof c)d=a,b=this.diff_main(d,b,!0),2<b.length&&(this.diff_cleanupSemantic(b),this.diff_cleanupEfficiency(b));else if(a&&"object"==typeof a&&"undefined"==typeof b&&"undefined"==typeof c)b=a,d=this.diff_text1(b);else if("string"==typeof a&&b&&"object"==typeof b&&"undefined"==typeof c)d=a;else if("string"==typeof a&&"string"==typeof b&&c&&"object"==typeof c)d=a,b=c;else throw Error("Unknown call format to patch_make.");
if(0===b.length)return[];c=[];a=new diff_match_patch.patch_obj;for(var e=0,f=0,g=0,h=d,j=0;j<b.length;j++){var i=b[j][0],k=b[j][1];!e&&0!==i&&(a.start1=f,a.start2=g);switch(i){case 1:a.diffs[e++]=b[j];a.length2+=k.length;d=d.substring(0,g)+k+d.substring(g);break;case -1:a.length1+=k.length;a.diffs[e++]=b[j];d=d.substring(0,g)+d.substring(g+k.length);break;case 0:k.length<=2*this.Patch_Margin&&e&&b.length!=j+1?(a.diffs[e++]=b[j],a.length1+=k.length,a.length2+=k.length):k.length>=2*this.Patch_Margin&&
e&&(this.patch_addContext_(a,h),c.push(a),a=new diff_match_patch.patch_obj,e=0,h=d,f=g)}1!==i&&(f+=k.length);-1!==i&&(g+=k.length)}e&&(this.patch_addContext_(a,h),c.push(a));return c};diff_match_patch.prototype.patch_deepCopy=function(a){for(var b=[],c=0;c<a.length;c++){var d=a[c],e=new diff_match_patch.patch_obj;e.diffs=[];for(var f=0;f<d.diffs.length;f++)e.diffs[f]=d.diffs[f].slice();e.start1=d.start1;e.start2=d.start2;e.length1=d.length1;e.length2=d.length2;b[c]=e}return b};
diff_match_patch.prototype.patch_apply=function(a,b){if(0==a.length)return[b,[]];a=this.patch_deepCopy(a);var c=this.patch_addPadding(a);b=c+b+c;this.patch_splitMax(a);for(var d=0,e=[],f=0;f<a.length;f++){var g=a[f].start2+d,h=this.diff_text1(a[f].diffs),j,i=-1;if(h.length>this.Match_MaxBits){if(j=this.match_main(b,h.substring(0,this.Match_MaxBits),g),-1!=j&&(i=this.match_main(b,h.substring(h.length-this.Match_MaxBits),g+h.length-this.Match_MaxBits),-1==i||j>=i))j=-1}else j=this.match_main(b,h,g);
if(-1==j)e[f]=!1,d-=a[f].length2-a[f].length1;else if(e[f]=!0,d=j-g,g=-1==i?b.substring(j,j+h.length):b.substring(j,i+this.Match_MaxBits),h==g)b=b.substring(0,j)+this.diff_text2(a[f].diffs)+b.substring(j+h.length);else if(g=this.diff_main(h,g,!1),h.length>this.Match_MaxBits&&this.diff_levenshtein(g)/h.length>this.Patch_DeleteThreshold)e[f]=!1;else{this.diff_cleanupSemanticLossless(g);for(var h=0,k,i=0;i<a[f].diffs.length;i++){var q=a[f].diffs[i];0!==q[0]&&(k=this.diff_xIndex(g,h));1===q[0]?b=b.substring(0,
j+k)+q[1]+b.substring(j+k):-1===q[0]&&(b=b.substring(0,j+k)+b.substring(j+this.diff_xIndex(g,h+q[1].length)));-1!==q[0]&&(h+=q[1].length)}}}b=b.substring(c.length,b.length-c.length);return[b,e]};
diff_match_patch.prototype.patch_addPadding=function(a){for(var b=this.Patch_Margin,c="",d=1;d<=b;d++)c+=String.fromCharCode(d);for(d=0;d<a.length;d++)a[d].start1+=b,a[d].start2+=b;var d=a[0],e=d.diffs;if(0==e.length||0!=e[0][0])e.unshift([0,c]),d.start1-=b,d.start2-=b,d.length1+=b,d.length2+=b;else if(b>e[0][1].length){var f=b-e[0][1].length;e[0][1]=c.substring(e[0][1].length)+e[0][1];d.start1-=f;d.start2-=f;d.length1+=f;d.length2+=f}d=a[a.length-1];e=d.diffs;0==e.length||0!=e[e.length-1][0]?(e.push([0,
c]),d.length1+=b,d.length2+=b):b>e[e.length-1][1].length&&(f=b-e[e.length-1][1].length,e[e.length-1][1]+=c.substring(0,f),d.length1+=f,d.length2+=f);return c};
diff_match_patch.prototype.patch_splitMax=function(a){for(var b=this.Match_MaxBits,c=0;c<a.length;c++)if(!(a[c].length1<=b)){var d=a[c];a.splice(c--,1);for(var e=d.start1,f=d.start2,g="";0!==d.diffs.length;){var h=new diff_match_patch.patch_obj,j=!0;h.start1=e-g.length;h.start2=f-g.length;""!==g&&(h.length1=h.length2=g.length,h.diffs.push([0,g]));for(;0!==d.diffs.length&&h.length1<b-this.Patch_Margin;){var g=d.diffs[0][0],i=d.diffs[0][1];1===g?(h.length2+=i.length,f+=i.length,h.diffs.push(d.diffs.shift()),
j=!1):-1===g&&1==h.diffs.length&&0==h.diffs[0][0]&&i.length>2*b?(h.length1+=i.length,e+=i.length,j=!1,h.diffs.push([g,i]),d.diffs.shift()):(i=i.substring(0,b-h.length1-this.Patch_Margin),h.length1+=i.length,e+=i.length,0===g?(h.length2+=i.length,f+=i.length):j=!1,h.diffs.push([g,i]),i==d.diffs[0][1]?d.diffs.shift():d.diffs[0][1]=d.diffs[0][1].substring(i.length))}g=this.diff_text2(h.diffs);g=g.substring(g.length-this.Patch_Margin);i=this.diff_text1(d.diffs).substring(0,this.Patch_Margin);""!==i&&
(h.length1+=i.length,h.length2+=i.length,0!==h.diffs.length&&0===h.diffs[h.diffs.length-1][0]?h.diffs[h.diffs.length-1][1]+=i:h.diffs.push([0,i]));j||a.splice(++c,0,h)}}};diff_match_patch.prototype.patch_toText=function(a){for(var b=[],c=0;c<a.length;c++)b[c]=a[c];return b.join("")};
diff_match_patch.prototype.patch_fromText=function(a){var b=[];if(!a)return b;a=a.split("\n");for(var c=0,d=/^@@ -(\d+),?(\d*) \+(\d+),?(\d*) @@$/;c<a.length;){var e=a[c].match(d);if(!e)throw Error("Invalid patch string: "+a[c]);var f=new diff_match_patch.patch_obj;b.push(f);f.start1=parseInt(e[1],10);""===e[2]?(f.start1--,f.length1=1):"0"==e[2]?f.length1=0:(f.start1--,f.length1=parseInt(e[2],10));f.start2=parseInt(e[3],10);""===e[4]?(f.start2--,f.length2=1):"0"==e[4]?f.length2=0:(f.start2--,f.length2=
parseInt(e[4],10));for(c++;c<a.length;){e=a[c].charAt(0);try{var g=decodeURI(a[c].substring(1))}catch(h){throw Error("Illegal escape in patch_fromText: "+g);}if("-"==e)f.diffs.push([-1,g]);else if("+"==e)f.diffs.push([1,g]);else if(" "==e)f.diffs.push([0,g]);else if("@"==e)break;else if(""!==e)throw Error('Invalid patch mode "'+e+'" in: '+g);c++}}return b};diff_match_patch.patch_obj=function(){this.diffs=[];this.start2=this.start1=null;this.length2=this.length1=0};
diff_match_patch.patch_obj.prototype.toString=function(){var a,b;a=0===this.length1?this.start1+",0":1==this.length1?this.start1+1:this.start1+1+","+this.length1;b=0===this.length2?this.start2+",0":1==this.length2?this.start2+1:this.start2+1+","+this.length2;a=["@@ -"+a+" +"+b+" @@\n"];var c;for(b=0;b<this.diffs.length;b++){switch(this.diffs[b][0]){case 1:c="+";break;case -1:c="-";break;case 0:c=" "}a[b+1]=c+encodeURI(this.diffs[b][1])+"\n"}return a.join("").replace(/%20/g," ")};
this.diff_match_patch=diff_match_patch;this.DIFF_DELETE=-1;this.DIFF_INSERT=1;this.DIFF_EQUAL=0;})()
