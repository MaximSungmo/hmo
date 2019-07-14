/**
 * URLInputListener 0.5.2
 * 
 */

var URLInputListener = {
	__core__: {
		lookupURL: function( words ) {
			var url = URLInputListener[ "__util__" ].getURL( words );
			if( !url ) {
				return;
			}
			/* Youtube */
			var vid = URLInputListener[ "__util__" ].vidYoutubeURL( url.text );
			if( vid ) { 
				$.log( "유튜브 스토리: ----->" + url.text + ":" + vid + "<------" );
				URLInputListener[ "__ui__" ].updateYoutubeContents( url.text, vid );
				return;
			} 
			/* site */
			$.log( "사이트 포함 스토리:----->" + url.text + "<------" );
		},
		isShareContentLocked: function() {
			$.log( ":::::::::::::" + $( "form[name='story-share-contents-config-form'] :input[name='share-content-lock']" ).val() + ":::::::::::::" );
			return $( "form[name='story-share-contents-config-form'] :input[name='share-content-lock']" ).val() == "true";
		},
		lockShareContent: function( lock ) {
			var $inputLock = $( "form[name='story-share-contents-config-form'] :input[name='share-content-lock']" ),
				$inputStoryType = $( "form[name='story-share-contents-config-form'] :input[name='story-type']" );

				$buttonPhoto = $( "#composer-tool-button-photo" );
			
			if( lock ) {
				$inputLock.val( "true" );
				$inputStoryType.val( "3" );
				$buttonPhoto.hide();
			} else {
				$inputLock.val( "false" );
				$inputStoryType.val( "0" );
				$buttonPhoto.show();
			}
		}
	},
	__event__: {
		onInit: function() {
			URLInputListener[ "__ui__" ].init();
		},
		onContext: function( input, text, offset ) {
			if( URLInputListener[ "__core__" ].isShareContentLocked() ){
				return;
			}
			var words = URLInputListener[ "__util__" ].wordsInText( text, false );
			URLInputListener[ "__core__" ].lookupURL( words );
		},
		onPaste: function( input, text, offset ) {
			if( URLInputListener[ "__core__" ].isShareContentLocked() ){
				return;
			}
			var words = URLInputListener[ "__util__" ].wordsInText( text, true );
			URLInputListener[ "__core__" ].lookupURL( words );
		},
		onCloseClick: function( event ){
			URLInputListener[ "__ui__" ].clearYoutubeContents();
		}
	},
	__ui__: {
		__$elementCompser: null,
		__isUpdateYoutubeContents: false,
		__url: "",
		init: function(){
			var $inputStoryType = $( "form[name='story-share-contents-config-form'] :input[name='story-type']" );
				$formInputVideoLinkUrl = $("form[name='story-share-contents-video-form'] :input[name='videoLinkUrl']"), 
				$formInputVideoLinkDoamin = $("form[name='story-share-contents-video-form'] :input[name='videoLinkDoamin']"),
				$formInputVideoSrc = $("form[name='story-share-contents-video-form'] :input[name='videoSrc']"), 
				$formInputVideoThumbnailUrl = $("form[name='story-share-contents-video-form'] :input[name='videoThumbnailUrl']"),
				$formInputVideoThumbnailWidth = $("form[name='story-share-contents-video-form'] :input[name='videoThumbnailWidth']"),
				$formInputVideoThumbnailHeight = $("form[name='story-share-contents-video-form'] :input[name='videoThumbnailHeight']"),
				$formInputVideoTitle = $("form[name='story-share-contents-video-form'] :input[name='videoTitle']"), 
				$formInputVideoSummary = $("form[name='story-share-contents-video-form'] :input[name='videoSummary']");

			//	
			$inputStoryType.val( "0" );
			//
			$formInputVideoLinkUrl.val( "" );
			$formInputVideoLinkDoamin.val( "" );
			$formInputVideoSrc.val("");
			$formInputVideoThumbnailUrl.val( "" );
			$formInputVideoThumbnailWidth.val( "" );
			$formInputVideoThumbnailHeight.val( "" );
			$formInputVideoTitle.val( "" );
			$formInputVideoSummary.val( "" );
			//
			this.__$elementCompser = $( "#composer-youtube" );
			$("#composer-youtube-stage-close").click( URLInputListener[ "__event__" ].onCloseClick );
		},
		updateYoutubeContents: function( url, vid ){
			if( this.__isUpdateYoutubeContents ) {
				return; 
			}
			
			var $thumbs = URLInputListener[ "__ui__" ].__$elementCompser.find( ".ui-thumb-pager-thumbs" ),
				$indicator =  URLInputListener[ "__ui__" ].__$elementCompser.find( ".ui-thumb-pager-loader" );
			
			this.__isUpdateYoutubeContents = true;
			this.__url = url;
			
			URLInputListener[ "__ui__" ].__$elementCompser.show();
			$thumbs.hide();
			$indicator.show();

			setTimeout( function(){
				$.ajax( {
					url: "https://gdata.youtube.com/feeds/api/videos/" + vid + "?v=2&alt=json",
					async: false,
					type: "get",
					dataType: "json",
					contentType: "application/json",
					success: URLInputListener[ "__ui__" ].onYoutubeDataRequest,
					error: function( jqXHR, textStatus, errorThrown ){
						$.error("add comments:"+errorThrown);
					}	
				} );
			}, 1);
		},
		clearYoutubeContents: function() {
			var $title = URLInputListener[ "__ui__" ].__$elementCompser.find( ".ui-sharestage-title" ),
				$subtitle = URLInputListener[ "__ui__" ].__$elementCompser.find( ".ui-sharestage-subtitle" ),
				$summary = URLInputListener[ "__ui__" ].__$elementCompser.find( ".ui-sharestage-summary p a" ),
				$thumb = URLInputListener[ "__ui__" ].__$elementCompser.find( ".ui-thumb-pager-thumbs" ),
				$indicator =  URLInputListener[ "__ui__" ].__$elementCompser.find( ".ui-thumb-pager-loader" ),

				$formInputVideoLinkUrl = $("form[name='story-share-contents-video-form'] :input[name='videoLinkUrl']"), 
				$formInputVideoLinkDoamin = $("form[name='story-share-contents-video-form'] :input[name='videoLinkDoamin']"),
				$formInputVideoSrc = $("form[name='story-share-contents-video-form'] :input[name='videoSrc']"), 
				$formInputVideoThumbnailUrl = $("form[name='story-share-contents-video-form'] :input[name='videoThumbnailUrl']"),
				$formInputVideoThumbnailWidth = $("form[name='story-share-contents-video-form'] :input[name='videoThumbnailWidth']"),
				$formInputVideoThumbnailHeight = $("form[name='story-share-contents-video-form'] :input[name='videoThumbnailHeight']"),
				$formInputVideoTitle = $("form[name='story-share-contents-video-form'] :input[name='videoTitle']"), 
				$formInputVideoSummary = $("form[name='story-share-contents-video-form'] :input[name='videoSummary']"); 

			//	
			$formInputVideoLinkUrl.val( "" );
			$formInputVideoLinkDoamin.val( "" );
			$formInputVideoThumbnailUrl.val( "" );
			$formInputVideoThumbnailWidth.val( "" );
			$formInputVideoThumbnailHeight.val( "" );
			$formInputVideoTitle.val( "" );
			$formInputVideoSummary.val( "" );
			
			//	
			$thumb.hide();
			$indicator.show();

			$title.text( "" );
			$subtitle.text( "" );
			$summary.text( "" );
			
			//
			URLInputListener[ "__ui__" ].__$elementCompser.hide();
			
			//
			URLInputListener[ "__core__" ].lockShareContent( false );			
		},
		onYoutubeDataRequest: function ( result ) {
			var $title = URLInputListener[ "__ui__" ].__$elementCompser.find( ".ui-sharestage-title" ),
				$subtitle = URLInputListener[ "__ui__" ].__$elementCompser.find( ".ui-sharestage-subtitle" ),
				$summary = URLInputListener[ "__ui__" ].__$elementCompser.find( ".ui-sharestage-summary p a" ),
				$thumbs = URLInputListener[ "__ui__" ].__$elementCompser.find( ".ui-thumb-pager-thumbs" ),
				$indicator =  URLInputListener[ "__ui__" ].__$elementCompser.find( ".ui-thumb-pager-loader" ),
				summaryText = result.entry["media$group"]["media$description"]["$t"],
				
				$formInputVideoLinkUrl = $("form[name='story-share-contents-video-form'] :input[name='videoLinkUrl']"), 
				$formInputVideoLinkDoamin = $("form[name='story-share-contents-video-form'] :input[name='videoLinkDoamin']"),
				$formInputVideoSrc = $("form[name='story-share-contents-video-form'] :input[name='videoSrc']"), 
				$formInputVideoThumbnailUrl = $("form[name='story-share-contents-video-form'] :input[name='videoThumbnailUrl']"),
				$formInputVideoThumbnailWidth = $("form[name='story-share-contents-video-form'] :input[name='videoThumbnailWidth']"),
				$formInputVideoThumbnailHeight = $("form[name='story-share-contents-video-form'] :input[name='videoThumbnailHeight']"),
				$formInputVideoTitle = $("form[name='story-share-contents-video-form'] :input[name='videoTitle']"), 
				$formInputVideoSummary = $("form[name='story-share-contents-video-form'] :input[name='videoSummary']"), 
				
				image = new Image();
			
			$.log( JSON.stringify( result ) );
			
			/*
			 * set data
			 */
			// url
			$formInputVideoLinkUrl.val( URLInputListener[ "__ui__" ].__url );
			// domain
			var matches = URLInputListener[ "__ui__" ].__url.match(/^https?\:\/\/([^\/?#]+)(?:[\/?#]|$)/i);
			var domain = matches && matches[1];
			$formInputVideoLinkDoamin.val( domain );
			// video src
			var videoSrc  = result.entry["content"]["src"].replace(/&/g, "&amp;") + "&amp;autohide=1&amp;autoplay=1&amp;rel=0&amp;showinfo=0&amp;showsearch=0";
			$formInputVideoSrc.val( videoSrc );
			// thumbnail info
			var thumbCount = result.entry["media$group"]["media$thumbnail"].length;
			for( var i = 0; i < thumbCount; i++ ) {
				var thumbnail = result.entry["media$group"]["media$thumbnail"][i];
				if( thumbnail[ "yt$name" ] == "hqdefault" ) {
					$formInputVideoThumbnailUrl.val( thumbnail[ "url" ]  );
					$formInputVideoThumbnailWidth.val( thumbnail[ "width" ]  );
					$formInputVideoThumbnailHeight.val( thumbnail[ "height" ] );
					break;
				}
			}
			// title
			$formInputVideoTitle.val( result.entry["title"]["$t"] );
			// summary
			$formInputVideoSummary.val( summaryText ); 
			
			
			//update screen
			$title.text( result.entry["title"]["$t"] );
			$subtitle.text( URLInputListener[ "__ui__" ].__url );
			if( summaryText.length > 200 ) {
				summaryText = summaryText.substring(0, 192) + "...";
			}
			$summary.html( summaryText );
			//
			image.onload = function() {
				var $thumb = URLInputListener[ "__ui__" ].__$elementCompser.find( ".ui-thumb-pager-thumb" );
				$thumb.attr( "src", image.src );
				$indicator.hide();
				$thumbs.show();
				
				URLInputListener[ "__core__" ].lockShareContent( true );
			};
			image.src = result.entry["media$group"]["media$thumbnail"][0]["url"];

			URLInputListener[ "__ui__" ].__isUpdateYoutubeContents = false;
		}	
	},
	__util__: {
		getURL: function( words ) {
			var countWords = words.length;
			for( var i=0; i < countWords; i++){
				var word = words[i];
				if( /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test( word.text ) ){
					//$.log( "---[" + word.text + ":" + word.beginAt + ":" + word.endAt + "]---" );
					return word;
				}
			}
			return null;
		},
		vidYoutubeURL: function( url ) {
		  var p = /^(?:https?:\/\/)?(?:www\.)?(?:youtu\.be\/|youtube\.com\/(?:embed\/|v\/|watch\?v=|watch\?.+&v=))((\w|-){11})(?:\S+)?$/;
		  return ( url.match( p ) ) ? RegExp.$1 : false;
		},
		wordsInText: function( text, onPaste ){
			var length = text.length,
				words = [],
				word = "",
				triggerOn = false,
				beginAt = 0,
				endAt = 0;
			
			for( var i = 0; i < length; i++ ) {
				var	ch = text.charAt( i ),
					charCode = ch.charCodeAt( 0 );
				if( charCode == 32 /*space*/ || charCode == 9 /*\t*/ || charCode == 13 /*\r*/ || charCode == 10 /*\n*/ ) {
					if( triggerOn == false ) {
						continue;
					}
					triggerOn = false;
					endAt = i;
					words[ words.length ] = { text: word, beginAt: beginAt, endAt : endAt };
				} else {
					if( triggerOn == true ) {
						word += ch;
					} else {
						triggerOn = true;
						word = ch;
						beginAt = i + 1;
					}
					if( onPaste && ( i + 1 == length ) ) {
						endAt = i + 1;
						words[ words.length ] = { text: word, beginAt: beginAt, endAt: endAt };
					}
				}
			}
			
			return words;
		}
	}
}

//Define URLInputListener Plugin
$.extend( URLInputListener, {
	type: "listener",
	onInit: URLInputListener[ "__event__" ].onInit.bind( URLInputListener[ "__event__" ] ),
	onContext: URLInputListener[ "__event__" ].onContext.bind( URLInputListener[ "__event__" ] ),
	onPaste: URLInputListener[ "__event__" ].onPaste.bind( URLInputListener[ "__event__" ] ),
	clearYoutubeContents: URLInputListener[ "__ui__" ].clearYoutubeContents.bind( URLInputListener[ "__ui__" ] )
} );