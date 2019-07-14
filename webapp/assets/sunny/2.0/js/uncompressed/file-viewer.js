/**
 * 	FileViewer V 2.0 ( FileViewer HMO Desktop Version )
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
			// ui 모듈 초기화
			this[ "__ui__" ].init();
			// event 모듈 초기화
			this[ "__event__" ].init();
			// hash 모듈 초기화
			this[ "__hash__" ].init();
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
				//
				// FileViewer[ "__hash__" ].pushState( url );
			}
			rels = rel.split("-");
			async = rels[0] == "async";
			type = ( rels[1] == "post") ? "post" : "get";
			data = (type == "post") ? data : $.parseJSON(data);
			//
			var urlToks = url.split( "?" );
			this.requestUrl = urlToks[0];
				
			//
			var params = urlToks[1].split( "&" );
			for( var i = 0; i < params.length; i++ ){
				var paramToks = params[i].split( "=" );
				if( paramToks[0] === "pid" ){
					FileViewer[ "__ui__" ].currentMediaId = paramToks[1];
					break;
				}
			}
				
			$.ajax({
				url: ( this.requestUrl + "?rnd=" + Math.floor( Math.random() * 999999999 ) ),
				async: async,
				type: type,
				dataType: "json",
				contentType: "application/json",
				data: data,
				success: FileViewer[ "__event__" ].onAjaxFetchStoryData,
				error: function(jqXHR,textStatus,errorThrown){
					$.error( "FileViewer[ __core__ ].load : " + errorThrown );
				}		
			});
		},
		close: function(){
				( FileViewer[ "__core__" ].countMediaes() > 1 ) && 
				$( ".snow-lift-pager").removeClass( "show" ).removeClass( "high-lighter" );
				
				$( "#photo-viewer-full-screen-switch" ).removeClass( "show" );
				$( "#file-download-switch" ).removeClass( "show" );
				FileViewer[ "__ui__" ].$thumbnailSliderBar.hide();
//				FileViewer[ "__ui__" ].scrollThumbnailSlider( 0 );
				//
				FileViewer[ "__ui__" ].hide();
				//
				var previousHash = FileViewer[ "__hash__" ].previousHash;
				window.history.replaceState && window.history.replaceState( null, null, previousHash );			
//				window.history.go(-1);
		},
		countMediaes: function() {
				return this.cache[ "story" ].mediaes.length;
			},
			indexMedia: function(){
				var 
				mediaes = this.cache[ "story" ].mediaes,
				mediaCount = mediaes.length;
				for( var i = 0; i < mediaCount; i++ ){
					var media = mediaes[i];
					if( media[ "id" ] ==  FileViewer["__ui__"].currentMediaId){
						return i;
					}
				}
				return -1;
		},
		mediaesFromCache: function(){
			return this.cache[ "story" ].mediaes;
		},
		mediaDataFromCache: function( pid ){
				var 
				mediaes = this.cache[ "story" ].mediaes,
				mediaCount = mediaes.length;
				
				for( var i = 0; i < mediaCount; i++ ){
					var media = mediaes[i];
					if( media[ "id" ] == pid ){
						return media;
					}
				}
				return null;
		},
		changeImage: function() {
			if( this.countMediaes() < 2 ){
				return;
			}
			if( $(".snow-lift-pager.prev").hasClass( "high-lighter" )){
				this.prevImage();
			} else {
				this.nextImage();
			}
		},
		nextImage: function() {
			var 
			index = -1,
			mediaes = this.cache[ "story" ].mediaes;
			mediaCount = mediaes.length;
			for( var i = 0; i < mediaCount; i++ ){
				var media = mediaes[i];
				if( media[ "id" ] == FileViewer[ "__ui__" ].currentMediaId ){
					index = i;
					break;
				}
			}
			
			if( ++index == mediaCount ) {
				index = 0;
			}
			
			FileViewer[ "__ui__" ].currentMediaId = mediaes[ index ].id;
			
			var mediaType = mediaes[ index ].mediaType;
			//$.log( mediaType );
			if( mediaType == 2 ) {
				$( "#photo-viewer-full-screen-switch" ).show();
				$( "#file-download-switch" ).hide();
				FileViewer[ "__ui__" ].loadImage();
			} else {
				$( "#photo-viewer-full-screen-switch" ).hide();
				$( "#file-download-switch" ).attr( "href", "/download/" +  media.id + "/" + media.fileName ).show();
				FileViewer[ "__ui__" ].loadMedia();
			}
		},
		prevImage: function() {
			var 
			index = -1,
			mediaes = this.cache[ "story" ].mediaes;
			mediaCount = mediaes.length;
			for( var i = 0; i < mediaCount; i++ ){
				var media = mediaes[i];
				if( media[ "id" ] == FileViewer[ "__ui__" ].currentMediaId ){
					index = i;
					break;
				}
			}
			
			if( --index < 0 ) {
				index = mediaCount-1;
			}
			
			FileViewer[ "__ui__" ].currentMediaId = mediaes[ index ].id;

			var mediaType = mediaes[ index ].mediaType;
			if( mediaType == 2 ) {
				$( "#photo-viewer-full-screen-switch" ).show();
				$( "#file-download-switch" ).hide();
				FileViewer[ "__ui__" ].loadImage();
			} else {
				$( "#photo-viewer-full-screen-switch" ).hide();
				$( "#file-download-switch" ).attr( "href", "/download/" +  media.id + "/" + media.fileName ).show();
				FileViewer[ "__ui__" ].loadMedia();
			}
		}		
	},
		
	__ui__: {
			
		WIDTH_DETAIL_VIEW: 360,
		WIDTH_MIN_IMAGE_VIEW: 520,
		HEIGHT_MIN_IMAGE_VIEW: 500,
			
		$w: null,
		$doc: null,
		$globalWrapper: null,
		$viewer: null,
		$container: null,
		$contentArea: null,
		$viewerPopup: null,
		$stageWrapper: null,
		$stageOutter:null,
		$photoImage: null,
		$storyDetail: null,
		$bottomStage: null,
		$thumbnailSliderBar: null,
		$closeTheater: null,
		
		$fileStage: null,
		$photoStage: null,
		
		$mentionBoxWrap: null,

		image: null,
		media: null,
		currentMediaId: null,

		ejsTemplateStoryDetail: null,
		ejsTemplateStoryStreamComment: null,
		ejsTemplateThumbnailSilderBar: null,
		
		__iscroll: null,
		__scrollTSBInfo: {
			MILSEC: 50,
			MOVING_DISTANCE: 7,
			direction: 0,
			$pane: null,
			$slider: null
		},
			
		init: function(){
				
				// 템플릿 다운로드 
				this.ejsTemplateStoryDetail = new EJS( { url:"/assets/sunny/2.0/js/template/file-viewer-story-detail.ejs?v=1.0" } );
				this.ejsTemplateStoryStreamComment = new EJS( { url: "/assets/sunny/2.0/js/template/file-viewer-story-detail-comment.ejs?v=1.0" } );
				this.ejsTemplateThumbnailSilderBar = new EJS( { url: "/assets/sunny/2.0/js/template/file-viewer-thumbnail-slider-bar.ejs?v=1.0" } );

				//
				this.$w = $( window );
				this.$doc = $( window.document );
				
				// 유아이 관련된 엘리멘트 $객체로 셀렉트해서 미리 저장 
				this.$globalWrapper = $( "#rw-snn-wrap" );
				this.$contentArea = $( "#main-container" );

				this.$viewer = $( "#photos-viewer" );
				this.$container = $( "#photo-viewer-container" );
				this.$detailContentsBody = $("#detail-content-body");
				this.$bottomStage = $("#bottom-stage");
				
				//
				this.$viewerPopup = this.$container.find( ".photo-viewer-popup" );
				this.$stageWrapper = this.$container.find( ".stage-wrapper" );
				this.$stageOutter = this.$container.find( ".stage-outter" );
				this.$photoImage = this.$container.find( ".spotlight" );
				this.$closeTheater = this.$container.find( ".close-theater" );

				//
				this.$photoStage = this.$stageWrapper.find( ".photo-stage" );
				this.$fileStage = this.$stageWrapper.find( ".file-stage" );
				
				//
				this.$mentionBoxWrap = $( "#mention-select-box-wrap" );
				
				//
				this.image = new Image();
				this.image.onload = FileViewer[ "__event__" ].onImageLoad;
				
				//
				$(document).on( "click", ".thumbnail-item-link", FileViewer["__event__"].onTumbnailLinkClicked );
				//
				this.$closeTheater.on( "click", FileViewer["__event__"].onCloseTheaterClicked )
		},
		hide: function(){
			// 뷰어 팝업 디액티베이트
			this.__deactivate();
		},
		testPtInContainer: function( x, y ) {
			var
			offset = this.$container.offset(),
			height = this.$container.height(),
			width = this.$container.width();
				
			return ( offset.left <= x && x <= offset.left + width ) && ( offset.top <= y && y <= offset.top + height );
		},
		testPtInStage: function( x, y ) {
			var
			offset = this.$stageWrapper.offset(),
			height = this.$stageWrapper.height(),
			width = this.$stageWrapper.width();

			if( ( offset.left <= x && x <= offset.left + width ) && ( offset.top <= y && y <= offset.top + height ) ) {
				return ( offset.left <= x && x <= offset.left + width/2 ) ? 0 : 1; 
			}
			
			return -1;
		},
		testPtInThumbnailSilerBar: function(x, y) {
			var
			result = 0,
			offset = this.$thumbnailSliderBar.offset(),
			height = this.$thumbnailSliderBar.height(),
			width = this.$thumbnailSliderBar.width();
			
			if( ( offset.left <= x && x <= offset.left + width ) && ( offset.top <= y && y <= offset.top + height ) ) {
				if( x < ( offset.left + width/4 ) ) {
					result = -1;
				} else if( ( offset.left + ( width*3 )/4 ) < x ) {
					result = 1;
				}
			}
			return result;
		},		
		render: function(){
				// 오른쪽 스토리 디테일 렌더링
				this.$detailContentsBody.append( this.ejsTemplateStoryDetail.render( FileViewer[ "__core__" ].cache["story"] ) );

				// 썸네일 슬라이드 렌더링
				this.$bottomStage.append( this.ejsTemplateThumbnailSilderBar.render( FileViewer[ "__core__" ].cache["story"] ) ).hide();
				
				// set 
				this.$storyDetail = $("#story-detail"); 
				this.$thumbnailSliderBar = $("#thumbnail-slider-bar");
				this.__scrollTSBInfo["$pane"] = $(".slider-scroll-wrap");
				this.__scrollTSBInfo["$slider"] = $(".slider-scroll");
				
				// 뷰어 팝업 액티베이트
				this.__activate();
				
				// 이미지 로딩
				this.loadImage();
				
				// timesince 실행
				FileViewer[ "__util__" ].timesince();
				
				//
				this.__iscroll = new iScroll( "u-10-01", {
					bounce: false,
					bounceLock: true,
					useTransform: false
				});

				//
				//pv_comment_ta_autoresize();			
		},
		loadImage: function() {
			this.$fileStage.hide();
 			this.$photoStage.show();
 			
	 		this.$photoImage.hide();
	 
	 		if( !this.currentMediaId ) {
	 			$.error( "FileViewer[ __ui__ ]:loadImage:currentMediaId is null" );
	 			retunr;
	 		}
	 			
	 		this.media = FileViewer[ "__core__" ].mediaDataFromCache( this.currentMediaId );
	 		//$.log( this.currentMediaId + ":" + JSON.stringify( this.media ) );
	 			
	 		setTimeout(function() { 
	 			FileViewer[ "__ui__" ].recalcLayout();
	 			FileViewer[ "__ui__" ].$bottomStage.show();
	 			//
	 			FVReply.textareaAutoResize();
	 		}, 1);
 		},
 		loadMedia: function() {
			this.$photoStage.hide();
			this.$fileStage.show();
 			
 			if( !this.currentMediaId ) {
 				$.error( "FileViewer[ __ui__ ]:loadImage:currentMediaId is null" );
 				retunr;
 			}
 			
 			this.media = FileViewer[ "__core__" ].mediaDataFromCache( this.currentMediaId );
 			this.media["width"] = 300;
			this.media["height"] = 300;

			//
			$( "#file-stage-name" ).text( this.media["fileName"] );
			$( "#file-stage-size" ).text( FileViewer["__util__"].formatFileSize( this.media["size"] ) );
 			
 			//
	 		setTimeout(function() { 
	 			FileViewer[ "__ui__" ].recalcLayout();
	 		}, 1);
 		},
 		__activate: function( active ) {
		
 			var	st = this.$w.scrollTop();
				
			// 씨어터 모드 온
			this.$globalWrapper.addClass( "_theater" );
				
			// 레이어 밑에 숨은 페이지 위치 강제로 조정
			this.$contentArea.css({ "top": -1*st  });
				
			// 레디 모등에서 팝업 액티베이트
			this.$viewer.removeClass( "paging-ready" );
			this.$viewer.addClass( "paging-activated" );
				
			// 전체 그레이 레이어 클릭 이벤트 핸들러 바인딩
			this.$viewer.bind( "click", FileViewer[ "__event__" ].onViewerClick );
				
			// 전체 그레이 레이어 클릭 이벤트 핸들러 바인딩
			this.$viewer.bind( "mousemove", FileViewer[ "__event__" ].onViewerMousemove );
				
			// 윈도우창 리사이징
			this.$w.bind( "resize", FileViewer[ "__event__" ].onWindowResize );
				
			// 도큐멘트 키보드 
			this.$doc.bind( "keydown", FileViewer[ "__event__" ].onDocumentKeydown );
				
			//
			this.$viewer.focus();
		},
		__deactivate: function(){
				
				var st = parseInt( this.$contentArea.css("top"), 10 );
				
				// 오른쪽 스토리 정보 지움
				this.$detailContentsBody.html("");
				
				// 썸네일 스라이더 바 내용 지움
				this.$bottomStage.html("");
				
				// 전체 그레이 레이어 클릭 이벤트 핸들러 언바인딩
				this.$viewer.unbind( "click", FileViewer[ "__event__" ].onViewerClick );
				
				// 전체 그레이 레이어 클릭 이벤트 핸들러 언바인딩
				this.$viewer.unbind( "mousemove", FileViewer[ "__event__" ].onViewerMousemove );
				
				// 씨어터 모드 오프
				this.$globalWrapper.removeClass("_theater");
				
				// 스크롤 위치 복구
				this.$contentArea.css( { "top":0  } );
				this.$w.scrollTop( -1*st );
				
				// 팝업 티액티베이트
				this.$viewer.removeClass("paging-activated");
				
				// 윈도우창 리사이징
				this.$w.unbind( "resize", FileViewer[ "__event__" ].onWindowResize );
				
				// 도큐멘트 키보드 
				this.$doc.unbind( "keydown", FileViewer[ "__event__" ].onDocumentKeydown );
		},
		__scrollSlider: function(){
			if( this.__scrollTSBInfo["direction"] == 0 ){
				return;
			}
			//
			var l = parseInt( this.__scrollTSBInfo["$slider"].css("left"), 10 );
			if( this.__scrollTSBInfo["direction"] < 0 ){
				if( l == 0 ) { 
					this.__scrollTSBInfo["direction"] = 0;
					return; 
				}
				l += this.__scrollTSBInfo["MOVING_DISTANCE"];
				this.__scrollTSBInfo["$slider"].css( "left", ( l > 0 ) ? 0 : l );
				//$.log( "sliding<<" + l );
			} else {
				var 
				wp = this.__scrollTSBInfo["$pane"].width(),
				ws = this.__scrollTSBInfo["$slider"].width(),
				wg = ws - wp;
				if( wg <= 0 || wg == -1*l ){
					this.__scrollTSBInfo["direction"] = 0;
					return;
				}
				
				l -= this.__scrollTSBInfo["MOVING_DISTANCE"];
				this.__scrollTSBInfo["$slider"].css( "left", ( -1*l >= wg ) ? -1*wg : l );
			}
			setTimeout( this.__scrollSlider.bind(this), this.__scrollTSBInfo["MILSEC"] );
		},
 		selectThumbnail: function(){
 			var $anchor = $("#media-viewer-thumbnail-" + this.currentMediaId );
 			
 			if( $anchor.length == 0 ){
 				return;
 			}
 			
 			// select
 			$(".thumbnail-item-link.selected").removeClass("selected");
 			$anchor.addClass("selected");
 			
 			// let it seen
 			var 
 			wp = this.__scrollTSBInfo["$pane"].width(),
			ws = this.__scrollTSBInfo["$slider"].width(),
			wg = ws - wp,
 			cntTPP = Math.floor( wp / 81 ),
 			cnt = FileViewer["__core__"].countMediaes(),
 			idx = FileViewer["__core__"].indexMedia(),
 			pg = Math.floor( idx / cntTPP ),
			ift = pg*cntTPP,
 			ilt = ift + cntTPP - 1;
 			if( pg > 0 && ilt > cnt-1 ) {
 				ift -= (ilt - cnt + 1 );
 			}
 			
			var l = -1*ift*81;
			this.__scrollTSBInfo["$slider"].css("left", ( wg > 0 && -1*l >= wg ) ? -1*wg : l );

			$("#disp-position").text( idx+1 );
 		},		
 		recalcLayout: function(){
	 			var 
	 			$win = $(window),
	 			ww = $win.width(),
	 			wh = $win.height(),
	 			pw = this.media.width,
	 			ph = this.media.height,
	 			sw, // stage width  ?
	 			sh, // stage height ?		
	 			iw, // image width  ?
	 			ih; // image height ?
	 					
	 			// 1. 이미지 높이 조정
	 			ih = ( wh > ph + 40 ) ? ph : ( wh - 40 );
				
	 			// 2.세로 사진의  최소 높이  ( 정사각형도 포함! ) 결정
	 			if( pw <= ph ) { 
	 	 			if( ih < this.HEIGHT_MIN_IMAGE_VIEW ){
	 					ih = this.HEIGHT_MIN_IMAGE_VIEW;
	 				}
	 			}

				// 3. 스테이지 놈이는 기본적으로 이미지 높이와 같다.	 			
				sh = ih;
				
				// 4. 실제 사진 높이 보다 크면, 사진 높이는 실제 사진과 같게...
				if( ih > ph ) {
					ih = ph;
				}
				
				// 4. 기본 사진 폭 구하기
	 			iw = Math.round( pw * ih / ph );
				
				// 5. 가로 사진에 대한 보정 작업 			
				if( pw > ph ){
					
					// 기본  WIDTH_MIN_IMAGE_VIEW 보다는 커야 한다.
					if( iw < this.WIDTH_MIN_IMAGE_VIEW ){
		 				iw = this.WIDTH_MIN_IMAGE_VIEW;
		 			}
					
					// 혹시라도 넉넉하게 브라우저 안에 안들어 오면?
		 			if( iw > ww - ( this.WIDTH_DETAIL_VIEW + 80 ) ){
		 				iw = ww - ( this.WIDTH_DETAIL_VIEW + 80 );
		 			}

		 			// WIDTH_MIN_IMAGE_VIEW 안에 있는 지 확인!
		 			if( iw < this.WIDTH_MIN_IMAGE_VIEW ) {
	 					iw = this.WIDTH_MIN_IMAGE_VIEW;
		 			}

					// 마지막으로  실제 그림보다 크면,
		 			if( iw > pw ) {
			 			iw = pw;
			 		}

		 			// 다시 높이 구하기
		 			ih = Math.round( iw * ph / pw );
		 			
		 			// 스테이지 높이 결정
	 				sh = ( sh >= this.WIDTH_MIN_IMAGE_VIEW ) ? sh : this.WIDTH_MIN_IMAGE_VIEW;
				}	 			
	 			
	 			// 5. 스테이지 폭 걸정
	 			sw = ( iw < this.WIDTH_MIN_IMAGE_VIEW - 40 ) ? this.WIDTH_MIN_IMAGE_VIEW : iw;

	 			
	 			// 6. CSS 세팅 
	 			this.$viewerPopup.css( { "width":sw + this.WIDTH_DETAIL_VIEW, "height":sh } );
				this.$stageWrapper.css( { "width":sw, "line-height": ( sh/parseInt(this.$stageWrapper.css( "font-size" ), 10) ) + "em"  } );
				
				$.browser.mozilla && this.$stageOutter.css( {"height": sh-2 } );
				$.browser.msie &&  this.$stageOutter.css( {"height": sh } ); 
				
				this.$photoImage.css( { "width":iw, "height":ih } );
			 	
				this.__scrollTSBInfo["$pane"].css( { "width":sw } );
				this.__scrollTSBInfo["$slider"].css( { "width" : FileViewer["__core__"].mediaesFromCache().length*81 } ); 

				// 
				this.selectThumbnail();
					
				// 7. 이미지 로딩
				this.image.src = ( (this.media.extName == "gif") ? this.media.originalPath : this.media.hugePath ) + "?rnd=" + Math.floor( Math.random() * 999999999 );
	 	
				// 8. 디테일 뷰 높이 조정
				this.calcDetailViewHeight();
 		},
 		relocateMentionBox: function(){
 			var
 			$wrapTA = $( "#ui-composer-out-fv" ),
 			height = $wrapTA.height(),
 			offset = $wrapTA.offset();
 			
 			this.$mentionBoxWrap.css( {
 				"left": offset.left + 1,
 				"top" : offset.top + height + 1
 			});
 		},
 		calcDetailViewHeight: function(){
 			if( !this.$storyDetail ) {
				return;
			}
 			var
			headerHeight = $("#photo-viewer-container .detail-content-header").height(),
			bottomInputHeight = $("#story-feedback-input ul").height(),
 			containerHeight = $( ".story-ufi-container" ).height(),
			detailHeight = this.$storyDetail.height(),
			scrollAreaHeight = containerHeight - headerHeight - bottomInputHeight;
				
			if( scrollAreaHeight < detailHeight ) { 
				$("#photo-viewer-container .ui-scrollable-area").height( detailHeight );
				$("#photo-viewer-container .slimScrollDiv").height( scrollAreaHeight ); 
				$('#u-10-01').height( scrollAreaHeight );
					
				$("#story-feedback-input").css( { "top" : scrollAreaHeight } );
				$("#story-feedback-input").addClass( "_l8" );
				$("._lf").addClass( "last-item" );
			} else {
				$("#photo-viewer-container .ui-scrollable-area").height( detailHeight );
				$("#photo-viewer-container .slimScrollDiv").height( detailHeight + 5 ); 
				$('#u-10-01').height( detailHeight + 5);
					
				$("#story-feedback-input").removeClass( "_l8" );
				$("._lf").removeClass( "last-item" );
				$("#story-feedback-input").css( { "top" : detailHeight+1 } );
			}
			//
			this.__iscroll.refresh();
			//
			this.relocateMentionBox();
	 	},
		openFullScreen: function(){
	 			var
	 			$img = $( "#photo-viewer-full-screen" ).find("img");
	 			$img.attr( "src", this.media[ "originalPath" ] );
	 			setTimeout( function(){
	 				$img.show();
	 				$( "#photo-viewer-full-screen" ).show();
		 		}, 100 );
		},
		closeFullScreen: function(){
			$( "#photo-viewer-full-screen" ).find("img").hide();
			$( "#photo-viewer-full-screen" ).hide();
		},
		scrollBottom: function() {
			this.__iscroll.scrollTo( 0, this.__iscroll.maxScrollY, 0 );
		},
		scrollThumbnailSlider: function( direction ) {
			if( this.__scrollTSBInfo["direction"] == direction){
				return;
			} 
			//$.log( "direction::::" + direction );
			if( direction == 0 ) { // stop scrolling
				this.__scrollTSBInfo["direction"] = 0;
				return;
			}
			this.__scrollTSBInfo["direction"] = direction;	
			setTimeout( this.__scrollSlider.bind(this), this.__scrollTSBInfo["MILSEC"] );
		},	 	
		showTools: function( show, direction ) {
			if( show ) {
				$("#photo-viewer-full-screen-switch").addClass("show");
				$("#file-download-switch").addClass("show");
				
				if( FileViewer[ "__core__" ].countMediaes() > 1 ){
					$( ".snow-lift-pager" ).addClass("show").removeClass( "high-lighter" );
					$( ".snow-lift-pager" + ( ( direction == 0) ? ".prev" : ".next" ) ).addClass( "high-lighter" );
					FileViewer[ "__ui__" ].$thumbnailSliderBar.show();
				}
			} else {
				( FileViewer[ "__core__" ].countMediaes() > 1 ) && $(".snow-lift-pager").removeClass("show").removeClass("high-lighter");
				$("#photo-viewer-full-screen-switch").removeClass("show");
				$("#file-download-switch").removeClass("show");
				FileViewer[ "__ui__" ].$thumbnailSliderBar.hide();
				FileViewer["__ui__"].scrollThumbnailSlider( 0 );
			}
		}
	},
	__event__: {
		init: function() {},
		onImageLoad: function(){
			FileViewer[ "__ui__" ].$photoImage.attr( "src", FileViewer[ "__ui__" ].image.src ).show();
				
			// 원본 사진 보기 모드에선 사진을 바꿔 주야 함.
			$("#photo-viewer-full-screen").is(":visible") && FileViewer[ "__ui__" ].openFullScreen();
				
		},
		onCloseTheaterClicked: function( e ) {
			e.preventDefault();
			setTimeout( function() {
				FileViewer[ "__core__" ].close();
			}, 100);
		},
		onTumbnailLinkClicked: function( e ){
			e.preventDefault();	

			var
			$anchor = $(this),
			currentMediaId = $anchor.attr( "data-media-id" ),
			currentMediaType = $anchor.attr( "data-media-type" );
			

			if( FileViewer[ "__ui__" ].currentMediaId == currentMediaId ) {
				return;
			}
				
			FileViewer[ "__ui__" ].currentMediaId = currentMediaId;

			if( currentMediaType == 2 ) {
				FileViewer[ "__ui__" ].loadImage();
			} else {
				FileViewer[ "__ui__" ].loadMedia();
			}
		},
		onViewerClick: function( event ) {
			// 
			if( MentionSelectBox.isVisible() ) {
				return;
			}
			
			//팝업 박스 안(컨테이너 내부) 클릭이면 무시! 
			if( FileViewer[ "__ui__" ].testPtInContainer( event.pageX, event.pageY ) ){
				return;
			}

			// 팝업박스 닫기			 
			FileViewer[ "__core__" ].close();
		}, 
		onViewerMousemove: function( event ){
			var result = FileViewer[ "__ui__" ].testPtInStage( event.pageX, event.pageY );
			if( result == -1 ){
				FileViewer[ "__ui__" ].showTools( false, result );
				return;
			}
			FileViewer[ "__ui__" ].showTools( true, result );
			result =  FileViewer[ "__ui__" ].testPtInThumbnailSilerBar( event.pageX, event.pageY );
			FileViewer["__ui__"].scrollThumbnailSlider( result );
		},
		onWindowResize: function( event ){
			FileViewer[ "__ui__" ].recalcLayout();		
		},
		onDocumentKeydown: function( e ){
			$ta = $("#photoviewer-add-comment-text");
			if( $ta.is(":focus") ) {
				return;
			}
			
			var code = (e.keyCode ? e.keyCode : e.which);
			//$.log( code );
			if( code == 27 ) { 
				//ESC
				var f = $("#photo-viewer-full-screen").is(":visible") ? FileViewer[ "__ui__" ].closeFullScreen :  FileViewer["__core__"].close;
				setTimeout( function() { f.call(null); }, 10);
			} else if( code == 37 ){
				// left arrow
				if( FileViewer[ "__core__" ].countMediaes() <= 1 ) { return; }
				FileViewer[ "__core__" ].prevImage();
			} else if( code == 39 ) { 
				// right arrow
				if( FileViewer[ "__core__" ].countMediaes() <= 1 ) { return; }
				FileViewer[ "__core__" ].nextImage();
			} else if( code == 32) {
				// space
				if( FileViewer[ "__core__" ].countMediaes() <= 1 ) { return; }
				FileViewer[ "__core__" ].changeImage();
			} else if( code == 107 ) {
				// +
				$("#photo-viewer-full-screen").is(":visible") ||
				FileViewer[ "__ui__" ].openFullScreen();
			} else if( code == 109 ) {
				// -
				$("#photo-viewer-full-screen").is(":visible") &&
				FileViewer[ "__ui__" ].closeFullScreen();
			} else if( code == 38 ) {
				// up arrow
				FileViewer[ "__ui__" ].showTools( true, -1 );
			} else if( code == 40 ) {
				// down arrow
				FileViewer[ "__ui__" ].showTools( false, -1 );
			} else {	
				//$.log( code );
			}
		},
		onAjaxFetchStoryData: function( result ){
				if( result.result != "success" ){
					$.error( "FileViewer:$.ajax-" + result.message );
					return;
				}
				//$.log( result.data );
				FileViewer[ "__core__" ].cache["story"] = result.data;
				setTimeout( function() { 
					FileViewer[ "__ui__" ].render();
				}, 1);
		},
		onWindowUnload: function() {
				var previousHash = FileViewer[ "__hash__" ].previousHash;
				window.history.replaceState && window.history.replaceState(null, null, previousHash);
		},
		onWindowPopSate: function() {
				var previousHash = FileViewer[ "__hash__" ].previousHash;
//				$.log( document.location.pathname + ":" + previousHash );
				if( document.location.pathname !== previousHash ){
//				    $.log("popup story:" + document.location.pathname );
				} else {
//					$.log("popup off");
				}			
		},
		onCheckHash: function(){
				clearInterval( FileViewer[ "__hash__" ].hashCheckInterval );
				var 
				previousHash = FileViewer[ "__hash__" ].previousHash,
				hash = document.location.hash.split( FileViewer[ "__hash__" ].HASH_STRING )[1];
				
//				$.log( hash + ":" +  previousHash );
				
				if( hash !== previousHash ){
					if(hash){
//						$.log( "hash alive" + hash );
					}
//					$.log( "popup off" );
				} else {
					if(hash){
//					    $.log( "popup story:" + hash );
					}
				}
				FileViewer[ "__hash__" ].hashCheckInterval = setInterval( FileViewer[ "__event__" ].onCheckHash, 200 );			
		}
	},
	__hash__: {
		HASH_STRING:"#!",
		previousHash: null,
		hashCheckInterval: null,
		init: function(){
			if ( window.history.pushState ) {
				var $win = $( window );
					
				this.previousHash = document.location.pathname;
				$win.bind( "popstate", FileViewer[ "__event__" ].onWindowPopSate );
				$win.unload( FileViewer[ "__event__" ].onWindowUnload );
			} else {
				this.hashCheckInterval = setInterval( FileViewer[ "__event__" ].onCheckHash, 200 );
			}			
		},
		pushState: function( path ){
			if ( window.history.pushState ) {
			    window.history.pushState(null, null, path);
			} else {
				this.previousHash = path;
			    window.location.hash = this.HASH_STRING + path;
			}				
		}
	},
	__util__: {
		timesince: function() {
			timesince && timesince();
		},
		copyURlToClipboard: function( anchor ){
			window.prompt ("Ctrl+C 또는 CMD+C(Apple,Mac)를 누르면 이 스토리의  URL이 복사됩니다.", $(anchor).attr("href"));	
		},
		formatFileSize: function( val ) {
			var size, unit, n = Math.log(val) / Math.LN10;
			if( n < 9 ) {
				size = "" + Math.ceil( val / 1024 );
				unit = "KB";
			} else if( n < 15 ) {
				size = "" + Math.ceil( val / 1024 / 1024 );
				unit = "MB";
			}
			var len = size.length;
			for( var i = len-3; i > 0; ) {
				size = size.slice( 0, i ) + "," + size.slice( i );
				i -= 3;
			}	
			
			return size + unit;
		},
		css_add_class: function($o, c) {
			$o.addClass(c);
			FileViewer.calcDetailViewHeight();
		}		
	}
};

//Export Functions of FileViewer' Internel Objects ( external access explicitly )
$.extend( FileViewer, { 
	init: FileViewer[ "__core__" ].init,	
	load: FileViewer[ "__core__" ].load,
	close: FileViewer[ "__core__" ].close,
	changeImage: FileViewer[ "__core__" ].changeImage.bind( FileViewer[ "__core__" ] ),
	nextImage: FileViewer[ "__core__" ].nextImage.bind( FileViewer[ "__core__" ] ),
	prevImage: FileViewer[ "__core__" ].prevImage.bind( FileViewer[ "__core__" ] ),
	openFullScreen: FileViewer[ "__ui__" ].openFullScreen.bind( FileViewer[ "__ui__" ] ),
	closeFullScreen: FileViewer[ "__ui__" ].closeFullScreen.bind( FileViewer[ "__ui__" ] ),
	calcDetailViewHeight: FileViewer["__ui__"].calcDetailViewHeight.bind( FileViewer[ "__ui__" ] ),
	scrollBottom: FileViewer["__ui__"].scrollBottom.bind( FileViewer["__ui__"] ),
	copyURlToClipboard: FileViewer[ "__util__" ].copyURlToClipboard,
	css_add_class: FileViewer[ "__util__" ].css_add_class
} );


/**
 * FileViewerReply
 */
var FVReply = {
	_wait: 0,
	_ejsReplyTemplate: null,
	_autoresizeAdjustActive: false,
	_autoresizeInputOffset: 0,
	_$replyList: null,
	_$autoresizeMirror: null,
	_inputListeners: [],
	_keyContextLookupInterval: null,
	init: function() {
		//
		this._ejsReplyTemplate = new EJS({ url: "/assets/sunny/2.0/js/template/file-viewer-story-detail-comment.ejs?v=1.0" });
		//	
		this._$autoresizeMirror = $( "#fv-ta-cmmnt-mirroring" );
		//
		this.attachInputListener( MentionInputListener );
		//
		$( document.body ).
		on( "keydown", ".ui-add-comment-input-fv", this.onKeydown ).
		on( "focus", ".ui-add-comment-input-fv", this.onFocus ).
		on( "blur", ".ui-add-comment-input-fv", this.onBlur );
	},
	attachInputListener: function( inputListener ) {
		var index = this._inputListeners.length;
		
		if( inputListener && inputListener.onContext ) {
			this._inputListeners[ index ] = inputListener;
			this._inputListeners[ index ].onAttached && this._inputListeners[ index ].onAttached();
		}
	},	
	textareaAutoResize: function(){
		$( ".ui-add-comment-input-fv" ).
		each( function() {
			var 
			ta = this, 
			$ta = $( this );

			FVReply._$autoresizeMirror.css( "width", $ta.width() - 10) ;
			
			if( $ta.css( "box-sizing") === "border-box" || 
				$ta.css( "-moz-box-sizing") === "border-box" || 
				$ta.css( "-webkit-box-sizing") === "border-box" ) {
				FVReply._autoresizeInputOffset = $ta.outerHeight() - $ta.height();
			}
			
			if( "onpropertychange" in ta ) {
				if( "oninput" in ta ) {
			    	ta.oninput = FVReply.onAdjustTextArea;
			    	$ta.keydown( function() { 
			    		setTimeout( FVReply.onAdjustTextArea.bind(ta), 50 );
			    	});
				} else {
					ta.onpropertychange = FVReply.onAdjustTextArea;
			    }
			} else {
			    ta.oninput = FVReply.onAdjustTextArea;
			}
		});	
	},
	clearTextarea: function( $ta ) {
		$ta.val("");
		setTimeout( function(){
			FVReply.onAdjustTextArea.call( $ta.get( 0 ) )
		}, 10 );
	},	
	testEnter: function( $ta ){
		var	
		text = $ta.val(),
		mirror= this._$autoresizeMirror.get(0),
		original = $ta.height(),
		height;

		this._$autoresizeMirror.val( text + "\n" );
		mirror.scrollTop = 0;
		mirror.scrollTop = 9e4;
		height = mirror.scrollTop + this._autoresizeInputOffset;

		( original !== height ) && $ta.css( "height", height );
		this._$autoresizeMirror.val( text );
	},
	ajaxPost: function( $ta ) {
		if( this._wait == 1 ) {
			this._wait = 0;
			return;
		}
		
		this._wait = 1;

		var 
		$form = $ta.parents( "form" ),
		rel = $form.attr( "rel" ),
		url = $form.attr( "action" ),
		type = $form.attr( "method" ),
		data = $form.serializeObject(),
		fileAttached = false;
		
		if( fileAttached == false && $.trim(data["text"]) == "" ) {
			$ta.focus();
			this._wait = 0;
			return;
		}
		
		$.log( data );
		$.ajax({
			url: url,
			async: rel == "async",
			type: type,
			dataType: "json",
			contentType: 'application/json',
			headers: {
				"Accept": "application/json",
				"Content-Type": "application/json"
			},
			data:JSON.stringify(data),
			success: this.onAjaxReplyPost.bind( $ta ),
			error: function( jqXHR, textStatus, errorThrown ){
				$.error( "reply post: " + errorThrown );
			}		
		});		
	},
	onAddCommentClicked: function( $event ) {
		var
		$btn = $( this ),
		$ta = $btn.prev();
		FVReply.ajaxPost( $ta );
		$ta.blur();
	},
	onAjaxReplyPost: function( data ) {
		var
		$li,
		$ta = $( this );
		
		FVReply._wait = 0;
		FVReply.clearTextarea( $ta );
		
		if( data.result != "success" ) {
			MessageBox( "댓글", "댓글 게시중에 에러가 발생했습니다.<br>" + data.message, MB_ERROR );
			return;
		}

		( $li = $( "<li class='ui-react-row _lf'>" ) ).
		html( FVReply._ejsReplyTemplate.render( data.data ) );

		$( "#photoviewer-react-container ul" ).append( $li );
		
		setTimeout( function() {
			var $prev = $li.prev();
			
			$prev.
			removeClass( "_lf" ).
			removeClass( "last-item" );
			
			FileViewer.calcDetailViewHeight();
			FileViewer.scrollBottom();
			
			refresh_timesince.call( $li.find( ".livetimestamp" ) );
			
		}, 10);
	},
	onKeydown: function( $event ) {
		if( MentionSelectBox.isVisible() ) {
			return;
		}		
		
		var
		ta = this,
		$ta = $( ta );
		
		if( $event.keyCode != 10 && $event.keyCode != 13 ) {
			if( $event.keyCode == 27 ) {
				$ta.blur();			
			}
			FileViewer.calcDetailViewHeight();
			return;
		}
		
		if( $event.shiftKey === false ) {
			$event.preventDefault();		
			setTimeout( function() {
				FVReply.ajaxPost( $ta );
			}, 10 );		
			return;								
		}
		
		FVReply.testEnter( $ta );
		FileViewer.calcDetailViewHeight();
	},
	onFocus: function( $event ) {
		// $.log( "Reply.onFocus" );
		var $ta = $( this );
		FVReply._keyContextLookupInterval || FVReply.onContext.call( $ta );
	},
	onBlur: function( $event ) {
		// $.log( "Reply.onBlur" );
		var $ta = $( this );
		if( FVReply._keyContextLookupInterval ) {
            window.clearInterval( FVReply._keyContextLookupInterval );
            FVReply._keyContextLookupInterval = null;
		}
	},
	onContext: function() {
		//
		FVReply._keyContextLookupInterval && window.clearInterval( FVReply._keyContextLookupInterval );
		
		//
		var $ta = $( this ), ta = $ta.get(0), offset = 0;
		
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
		
		//
		var countListeners = FVReply._inputListeners.length;
		for( var i = 0; i < countListeners; i++ ) {
			FVReply._inputListeners[ i ].onContext && FVReply._inputListeners[ i ].onContext( $ta, $ta.val(), offset );
		}			
		
	    //
		FVReply._keyContextLookupInterval = window.setInterval( FVReply.onContext.bind( $ta ), 5 );
	},	
	onAdjustTextArea: function() {
		if( FVReply._autoresizeAdjustActive ) { 
			return; 
		}
		
		var	
		ta = this,
		$ta = $(ta), 
		mirror = FVReply._$autoresizeMirror.get(0),
		original = $ta.height(),
		height;

		FVReply._autoresizeAdjustActive = true;
		FVReply._$autoresizeMirror.val( $ta.val() );
			
		mirror.scrollTop = 0;
		mirror.scrollTop = 9e4;
		height = mirror.scrollTop + FVReply._autoresizeInputOffset;

		(original !== height) && $ta.css( "height",height );
		setTimeout( function (){ 
			FVReply._autoresizeAdjustActive = false; 
		}, 1 );
	},	
	onAjaxReplyDelete: function( data ) {
		if( data.result != "success" ) {
			MessageBox( "댓글", "댓글 삭제중에 에러가 발생했습니다.<br>" + data.message, MB_ERROR );
			return;
		}
		$( this ).parents( ".ui-react-row" ).remove();
		//
		FileViewer && FileViewer.calcDetailViewHeight && FileViewer.calcDetailViewHeight();
	},
	onAjaxFeel: function( data ) {
		if( data.result != "success" ) {
			MessageBox( "스토리", "스토리 평가중 에러가 발생했습니다.<br>" + data.message, MB_ERROR );			
			return;
		}
		var 
		$anchor = $(this),
		$span = $anchor.prev(),
		checked = $anchor.attr("aria-checked")=="true",
		feeled = false;
		
		$( ".react-icons-" + $anchor.attr( "data-ft" ) ).each(function(){
			
			$(this).find(".story-react").each(function(){
				var 
				$r=$(this),
				$span=$r.find("span"),
				$anchor=$r.find("a");
				
				if( $span.hasClass( "reacted" ) ){
					feeled = true; 
				}
				$span.removeClass( "reacted" );
				$anchor.attr( "aria-checked","false" );
			});	
		});

		if( !checked ) {
			$( "." + $anchor.attr("data-ft-class") ).each( function(){
				$(this).prev().addClass("reacted");
				$(this).attr("aria-checked","true");
			});
			
			if(!feeled){
				var 
				$counter=$("."+$anchor.attr("aria-controls")),
				count=parseInt($counter.attr("data-count"),10);			
				$counter.attr("data-count",++count);
				$counter.find(".count-text").text(count+"명");
				(count==1) && $counter.parents(".cmnt-row-wrap").show();
			}
		} else {
			var 
			$counter=$("."+$anchor.attr("aria-controls")),
			count=parseInt($counter.attr("data-count"),10);
			$counter.attr("data-count",--count);
			$counter.find(".count-text").text(count+"명");
			(count==0) && $counter.parents(".cmnt-row-wrap").hide();
		}
		
		//
		FileViewer && FileViewer.calcDetailViewHeight && FileViewer.calcDetailViewHeight();
	},	
	onAjaxFeelOkay: function( data ) {
		if( data.result != "success" ){
			MessageBox( "댓글", "댓글에 좋아요 표시중에 에러가 발생했습니다.<br>" + data.message, MB_ERROR );
			return;
		}
			
		var 
		$trigger = $( this ),
		checked = $trigger.attr( "aria-checked" ) == "true";

		$( "." + $trigger.attr( "data-control-class" ) ).each( function() {
			var 
			$parent=$(this),
			$anchor=$parent.find(".ui-reply-like-icon");
			$score=$parent.find(".ui-reply-feel-score"),
			score=parseInt($score.attr("data-feel-score"),10);
				
			if(checked){
				$parent.removeClass("mt-1-like");
				$parent.addClass("mt-0-like");
				$anchor.attr("title","좋아요");
				$anchor.attr("aria-checked","false");
				$score.attr("data-feel-score", --score);
			}else{
				$parent.removeClass("mt-0-like");
				$parent.addClass("mt-1-like");
				$anchor.attr("title","좋아요 취소");
				$anchor.attr("aria-checked","true");
				$score.attr("data-feel-score", ++score);
			}
			$score.text(score + "명");
		});
	},
	onReplyStreamBefore: function() {
		var $anchor = $(this);
		$anchor.find(".fa-comment").hide();
		$anchor.find(".fa-spin").show();
		$anchor.find("span").text("가져오는 중...");
	},
	onReplyStream: function( data ) {
		if( data.result != "success" ) {
			$.error("error:onReplyStream:$.ajax-" + data.message);
			return;
		}
		
		//$.log( data );
		var 
		$anchor = $( this ),
		$parent = $anchor.parents( ".cmnt-count-row" ),
		count = data.data.length,
		remains = parseInt( $anchor.attr( "data-reply-count" ),10 ),
		reuestData = $.parseJSON( $anchor.attr( "data-request-map" ) );

		for( var i=0; i < count; i++ ) {
			var $li;
			
			( $li = $( "<li class='ui-react-row'>" ) ).
			html( FVReply._ejsReplyTemplate.render( data.data[i] ) );
			
			$parent.after( $li );
			FileViewer.calcDetailViewHeight();			
			
			refresh_timesince.call( $li.find( ".livetimestamp" ) );
		}
		
		if( count > 0 ){
			reuestData[ "replyId" ] = data.data[ count-1 ].id;
			$anchor.attr( "data-request-map", JSON.stringify( reuestData ) );
		}
		
		remains -= count;
		
		if(remains<0){ 
			remains = 0; 
		}
		$anchor.attr( "data-reply-count", remains );

		$anchor.find( ".fa-spin" ).hide();
		$anchor.find( ".fa-comment" ).show();
		
		if( remains == 0 ) {
			$parent.remove();
		} else if( remains < 10 ){
			$anchor.find( "span" ).text( "댓글" + remains+"개 더 보기" );
		} else {
			$anchor.find( "span" ).text( "지난 댓글 더보기" );
		} 
		
		//
		FileViewer && FileViewer.calcDetailViewHeight && FileViewer.calcDetailViewHeight();
	}
};


$(function(){
	FileViewer.init();
	FVReply.init();
});