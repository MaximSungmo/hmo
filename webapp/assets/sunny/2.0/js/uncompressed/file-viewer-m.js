/**
 * 	FileViewer V 2.0 ( FileViewer HMO Mobile-Tablet Version )
 * 
 * 
 * 	dependency:
 *  mod-timesince,
 *  HMOClick
 * 
 *  kickscar@sunnyvale.co.kr
 * 
 */

var FileViewer = {
	__core__: {
		requestUrl: null,
		cache: { "story": null },		
		init: function(){
			FileViewer[ "__ui__" ].init();
			FileViewer[ "__event__" ].init();
		},
		load: function(){
			if( arguments.length !=1 ){
				$.error("FileViewer['__core__'].load : insufficient argument");
				return;
			}

			var url = arguments[0], rel = "async-get", data = "{}", rels, async, type;
			if( arguments[0].tagName && arguments[0].tagName.toLowerCase() == "a" ){
				var $a = $( arguments[0] );
				url = $a.attr("href");
				rel = $a.attr("rel"),
				data = $a.attr("data-request-map");
			}
			rels = rel.split("-");
			async = rels[0] == "async";
			type = ( rels[1] == "post") ? "post" : "get";
			data = (type == "post") ? data : $.parseJSON(data);
			
			//
			var urlToks = url.split( "?" );
			this.requestUrl = urlToks[0];
					
			//
			var
			paramToks,
			params = urlToks[1].split( "&" );
			
			paramToks = params[1].split( "=" );
			FileViewer[ "__ui__" ].countItem = parseInt( paramToks[1], 0 );
			paramToks = params[2].split( "=" );
			FileViewer[ "__ui__" ].index = parseInt( paramToks[1], 0 );
			
			$.log( FileViewer[ "__ui__" ].countItem );
			//
			$.ajax({
				url: ( this.requestUrl + "?rnd=" + Math.floor( Math.random() * 999999999 ) ),
				async: async,
				type: type,
				dataType: "json",
				contentType: "application/json",
				data: data,
				success: FileViewer[ "__event__" ].onAjaxFetchStoryData,
				error: function(jqXHR,textStatus,errorThrown) {
					$.error( "FileViewer[ __core__ ].load : " + errorThrown );
				}		
			});
		},
		mediaDataFromCache: function(){
			var index = FileViewer[ "__ui__" ].index;
			return  this.cache[ "story" ].mediaes[ index ];
		}
	},
	__ui__: {
		SLIDING_SPEED: 300,
		
		$w: null,
		$doc: null,
		$globalWrapper: null,
		$contentArea: null,
		$viewer: null,
		$container: null,
		$swipeList: null,
		$storyUFHContainer: null,
		$closeTheater: null,
		
		image: null,
		media: null,
		viewWidth: 0,
		viewHeight: 0,
		index: 0,
		countItem: 0,
		
		ejsTemplateStoryDetail: null,
		ejsTemplateMediaItem: null,
			
		init: function(){
			// 템플릿 다운로드 
			this.ejsTemplateStoryDetail = new EJS( { url:"/assets/sunny/2.0/js/template/file-viewer-story-detail-mobile.ejs?v=1.0" } );
			this.ejsTemplateMediaItem = new EJS( { url:"/assets/sunny/2.0/js/template/file-viewer-media-item-mobile.ejs?v=1.0" } );
				
			//
			this.$w = $( window );
			this.$doc = $( window.document );
				
			// UI 관련된 엘리멘트 $객체로 셀렉트해서 미리 저장 
			this.$globalWrapper = $( "#rw-snn-wrap" );
			this.$contentArea = $( "#main-container" );

			//
			this.$viewer = $( "#photos-viewer" );
			this.$container = $( "#photo-viewer-container" );
			this.$swipeList = $( "#stage-swipe_list" );
			this.$storyUFHContainer = $("#story-ufh-container");
			this.$closeTheater = $("#closer-theater");

			//
			this.image = new Image();
			this.image.onload = FileViewer[ "__event__" ].onImageLoad;
		},
		translate: function( dist, speed ) {
			var
			slide = this.$swipeList.get( 0 ),
			style = slide && slide.style;

		    if( !style ){
		    	return;
		    }

		    ( dist == 0 ) && ( function(){
		    	window.setTimeout( function() {
		    		FileViewer[ "__ui__" ].updateMedia();
		    	}, 100);
		    })();
		    
		    dist = ( this.index == 0 ? 0 : -1*this.index) * this.viewWidth + dist; 
		    	
		    style.webkitTransitionDuration =
		    style.MozTransitionDuration =
		    style.msTransitionDuration =
		    style.OTransitionDuration =
		    style.transitionDuration = ( speed < 0 ? this.SLIDING_SPEED : speed )  + 'ms';

		    style.webkitTransform = 'translate(' + dist + 'px,0)' + 'translateZ(0)';
		    style.msTransform =
		    style.MozTransform =
	    	style.OTransform = 'translateX(' + dist + 'px)';
		},		
		activate: function(){
			var storyData =  FileViewer[ "__core__" ].cache["story"];

			// 스토리 디테일 렌더링
			this.$storyUFHContainer.append( this.ejsTemplateStoryDetail.render( storyData ) );

			// 기본 슬라이더 렌더링
			this.$swipeList.append( this.ejsTemplateMediaItem.render( storyData ) );


			/* 뷰어 액티베이트 */
			var	st = this.$w.scrollTop();
			// 씨어터 모드 온
			this.$globalWrapper.addClass( "_theater" );
			// 레이어 밑에 숨은 페이지 위치 강제로 조정
			this.$contentArea.css({ "top": -1*st  });
			// 레디 모등에서 팝업 액티베이트
			this.$viewer.removeClass( "paging-ready" );
			this.$viewer.addClass( "paging-activated" );

			//
	 		this.viewWidth = this.$viewer.width();
	 		this.viewHeight = this.$viewer.height();
	 		
	 		this.$container.find( ".spotlight" ).css( {
	 			"width": this.viewWidth,
	 			"height": this.viewHeight
	 		});

			//
			this.$closeTheater.show();
			this.$closeTheater.onHMOClick( null, FileViewer[ "__event__" ].onTheaterCloseClick );
			
			//
			//this.$viewer.onHMOClick( null, FileViewer[ "__event__" ].onViewerClick );
			
			//
			this.translate( 0, 1 );
		 		
			// iscroller
	 		window.setTimeout( function(){
				this.__iscroll = new iScroll( "u-10-02", {
					bounce: false,
					momentum: false,
					//hideScrollbar: true,
					scrollbarClass: "hmo-fv-iscoll"
				});
	 		}, 1000 );
		},
		deactivate: function(){
			var st = parseInt( this.$contentArea.css("top"), 10 );
			
			// 스토리 디테일 렌더링
			this.$storyUFHContainer.html( "" );
			
			// 슬라이더 내용 지움 
			this.$swipeList.html( "" );			
				
			//
			this.$closeTheater.offHMOClick( null, FileViewer[ "__event__" ].onTheaterCloseClick );
			this.$closeTheater.hide();
				
			// 전체 그레이 레이어 클릭 이벤트 핸들러 언바인딩
			this.$viewer.offHMOClick( null, FileViewer[ "__event__" ].onViewerClick );
				
			// 씨어터 모드 오프
			this.$globalWrapper.removeClass("_theater");
				
			// 스크롤 위치 복구
			this.$contentArea.css( { "top":0  } );
			this.$w.scrollTop( -1*st );
				
			// 팝업 티액티베이트
			this.$viewer.removeClass("paging-activated");
		},
		updateMedia: function(){
			var
			media = FileViewer[ "__core__" ].mediaDataFromCache(),
			$stage = $( "#media-" + media.id );
			
			if( $stage.hasClass( "stage-set" ) ) {
				return;
			}
			
			if( media.mediaType != 2 ) {
				$( "#file-stage-name-" + media.id ).text( media.fileName );
				$( "#file-stage-size-" + media.id ).text( media.formatSize );
				$stage.addClass( "stage-set" );
			} else if( media.mediaType == 2 ) {
				var
	 			mw = media.width,
	 			mh = media.height,
	 			vh = this.viewHeight,
	 			iw,
	 			ih;

				iw = this.viewWidth;
	 			ih = mh * iw / mw;

	 			if( ih > vh ) {
	 				ih = vh;
	 				// 4. 너비 재조정
	 				iw = mw * ih / mh;
	 			}
	 			
	 			$( "#photo-stage-" + media.id ).css( { "width":iw, "height":ih } ).hide();
				this.image.src = ( ( media.extName == "gif" ) ? media.originalPath : media.hugePath ) + "?rnd=" + Math.floor( Math.random() * 999999999 );
				$stage.addClass( "stage-set" );
			}
		}
	},
	__event__: {
		__start: null,
		__isScrolling: undefined,
		__delta: null,
		init: function(){
			$(".swipe-stage").bind( "touchstart", FileViewer[ "__event__" ].onTouchStart );
		},
		onImageLoad: function(){
			var media = FileViewer[ "__core__" ].mediaDataFromCache();
			$( "#photo-stage-" + media.id ).attr( "src", FileViewer[ "__ui__" ].image.src ).show();
		},
		onViewerClick: function( $event ){
			$event.stopPropagation();
		}, 
		onAjaxFetchStoryData: function( result ){
			if( result.result != "success" ){
				$.error( "FileViewer:$.ajax-" + result.message );
				return;
			}
			FileViewer[ "__core__" ].cache["story"] = result.data;
			setTimeout( function() { 
				FileViewer[ "__ui__" ].activate();
			}, 1);
		},
		onTheaterCloseClick: function( $event ){
			$.log( "onTheaterCloseClick" );
			$event.preventDefault();
			FileViewer[ "__ui__" ].deactivate();
		},
		onTouchStart: function( $event ) {
			var 
			event = $event.originalEvent,
			touches = event.touches[0];
			
			FileViewer[ "__event__" ].__start = {
    	        x: touches.pageX,
    	        y: touches.pageY,
    	        time: +new Date
			};			
			
			// used for testing first move event
			FileViewer[ "__event__" ].__isScrolling = undefined;

			// reset delta and end measurements
			FileViewer[ "__event__" ].__delta = {};
		      
			this.addEventListener( "touchmove", FileViewer[ "__event__" ].onTouchMove, false );
			this.addEventListener( "touchend", FileViewer[ "__event__" ].onTouchEnd, false );
		},
		onTouchMove: function( event ){
			// ensure swiping with one touch and not pinching
			if( event.touches.length > 1 || event.scale && event.scale !== 1 ) {
				return;
			}

			var
			touches = event.touches[0],
			countItem = FileViewer[ "__ui__" ].countItem,
			index = FileViewer[ "__ui__" ].index;
		      
			// measure change in x and y
			FileViewer[ "__event__" ].__delta = {
				x: touches.pageX - FileViewer[ "__event__" ].__start.x,
				y: touches.pageY - FileViewer[ "__event__" ].__start.y
			}

			//
			var direction = FileViewer[ "__event__" ].__delta.x < 0;	

			if( ( index == 0 && !direction ) || ( index + 1 == countItem && direction ) ) {
				return;
			}
			
			// determine if scrolling test has run - one time test
			if ( typeof FileViewer[ "__event__" ].__isScrolling == 'undefined') {
				FileViewer[ "__event__" ].__isScrolling = !!( FileViewer[ "__event__" ].__isScrolling || Math.abs( FileViewer[ "__event__" ].__delta.x ) < Math.abs( FileViewer[ "__event__" ].__delta.y ) );
			}

			//if user is not trying to scroll vertically
			if ( !FileViewer[ "__event__" ].__isScrolling ) {			
				event.preventDefault();
				FileViewer[ "__ui__" ].translate( FileViewer[ "__event__" ].__delta.x, 0 );
			}
		},
		onTouchEnd: function( event ){
			//measure duration
			var duration = +new Date - FileViewer[ "__event__" ].__start.time;
			
			// determine if slide attempt triggers next/prev slide
			var isValidSlide = 
				Number(duration) < 250																	// if slide duration is less than 250ms
				&& Math.abs( FileViewer[ "__event__" ].__delta.x ) > 20            						// and if slide amt is greater than 20px
				|| Math.abs( FileViewer[ "__event__" ].__delta.x ) > FileViewer[ "__ui__" ].__width/2;	// or if slide amt is greater than half the width
	
			// determine direction of swipe (true:right, false:left)
			var direction = FileViewer[ "__event__" ].__delta.x < 0;		            
				
			if( !FileViewer[ "__event__" ].__delta.x || ( FileViewer[ "__ui__" ].index == 0 && !direction ) || ( FileViewer[ "__ui__" ].index + 1 == FileViewer[ "__ui__" ].countItem && direction ) ) {
				this.removeEventListener( "touchmove", FileViewer[ "__event__" ].onTouchMove, false );
				this.removeEventListener( "touchend", FileViewer[ "__event__" ].onTouchEnd, false );		
				return;
			}
			
			// if not scrolling vertically
			if( !FileViewer[ "__event__" ].__isScrolling ) {
				if( direction ) {
					FileViewer[ "__ui__" ].index += 1;
				} else {
					FileViewer[ "__ui__" ].index -= 1;
				}

				FileViewer[ "__ui__" ].translate( 0, -1 );
			}
			
			// kill touchmove and touchend event listeners until touchstart called again
			this.removeEventListener( "touchmove", FileViewer[ "__event__" ].onTouchMove, false );
			this.removeEventListener( "touchend", FileViewer[ "__event__" ].onTouchEnd, false );			
		} 
	},
	__util__: {
	}		
};

//Export Functions of FileViewer' Internel Objects ( external access explicitly )
$.extend( FileViewer, { 
	load: FileViewer[ "__core__" ].load
});

$(function(){
	FileViewer[ "__core__" ].init();
});