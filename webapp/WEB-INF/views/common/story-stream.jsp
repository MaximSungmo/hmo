<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:useBean id="now" class="java.util.Date" />
<c:set value="${fn:length(stories) }" var="countStory" />
<c:set var="currentDate">
	<fmt:formatDate value="${now }" pattern="yyyy.MM.dd" />
</c:set>
<c:if test="${countStory > 0 }">
	<c:set var="lastStoryCDate">
		<fmt:formatDate value='${stories[countStory-1].createDate }' pattern="yyyy.MM.dd" />
	</c:set>	
</c:if>
<script src="/assets/sunny/2.0/js/uncompressed/mention-input-listener.js"></script>
<script src="/assets/sunny/2.0/js/uncompressed/file-uploader.js"></script>
<script>

/* -- Object Story -- */
var Story = {
	_waitStoryModify: 0,	
	_$streamList: null,
	_ejsEditableComposerTemplate: null,
	_ejsStoryEditedTemplate: null,
	init: function(){
		//
		this._ejsEditableComposerTemplate = new EJS({
			url: "/assets/sunny/2.0/js/template/story-editable-composer.ejs?v=1.0"
		});
		this._ejsStoryEditedTemplate = new EJS({
			url: "/assets/sunny/2.0/js/template/story-edited.ejs?v=1.0"
		});
		//			
		this._$streamList = $( "#story-stream-list" );
		//
		$( document.body ).onHMOClick( ".generated-mention", this.onMentionClicked );
		$( document.body ).onHMOClick( ".generated-hash", this.onHashClicked );
		//
		timesince();
	},
	inlineModifySubmit: function( form, event ) {
		$.Event( event ).preventDefault();
		
		if( this._waitStoryModify == 1 ){
			return;
		}
		
		this._waitStoryModify = 1;
		
		var 
		$form = $(form),
		$textarea = $form.find( "textarea" );
		$uploadedFiles = $form.parent().find(".upload-image-item .uploaded-files"),
		rel = $form.attr("rel"),
		url = $form.attr("action"),
		type = $form.attr("method"),
		data = $form.serializeObject(),
		fileAttached = false;
		
		if( $uploadedFiles.length > 0 ){
			data["mediaIds"] = [];
			$uploadedFiles.each( function() {
				data["mediaIds"].push( $( this ).data( "up-file" ) );
			});
			fileAttached = true;
		}
		
		if( fileAttached == false && $.trim(data["text"]) == "" ) {
			$textarea.focus();
			this._waitStoryModify = 0;
			return false;
		}

		$.ajax({
			url:url,
			type:type,
			dataType:"json",
			contentType: 'application/json',
			headers: {
				"Accept": "application/json",
				"Content-Type": "application/json"
			},
		    data: JSON.stringify(data),
		    success:this.onAjaxStoryMofied,
			error:function(jqXHR,textStatus,errorThrown){
				$.log("error:Event.inlineModifySubmit:"+errorThrown);
			}
		});
		
		return false;
	},
	endEdit: function( storyId ) {
		var
		$input = $( "#ta-message-text-edit-" + storyId ),
		$editableContents = $( "#editable-contents-" + storyId ),
		$editableComposerWrap = $( "#editable-composer-wrap-" + storyId );
		
		MentionInputListener.clear( $input );
		$editableComposerWrap.hide().html( "" );
		$editableContents.show();
	},
	onAjaxStoryMofied: function( data ) {
		$.log( data );
		
		var
		_this = Story,
		story = data.data,
		$editableContents = $( "#editable-contents-" + story.id );
		
		_this._waitStoryModify = 0;
		if( data.result != "success" ) {
			MessageBox( "스토리", "스토리 수정중 다음과 같은 오류가 발생 했습니다.<br>" + data.message, MB_ERROR );
			return;
		}
		
		$editableContents.html( _this._ejsStoryEditedTemplate.render( story ) );
		Story.endEdit( story.id );
	},
	onAjaxModify: function( data ) {
		if( data.result != "success" ) {
			MessageBox( "스토리", "스토리 수정을 위한 수정 데이터를 가져오는 중 에러가 발생했습니다.<br>" + data.message, MB_ERROR );
			return;
		}
		
		var
		_this = Story,
		$anchor = $(this),
		dataRender = data.data,
		d = $.parseJSON( data.data.requestBody ), 
		storyId = $anchor.attr( "data-story-id" ),
		$popup = $( "#story-" + storyId + "-context-popup-menus" ),
		$editableContents = $( "#editable-contents-" + storyId ),
		$editableComposerWrap = $( "#editable-composer-wrap-" + storyId );

		//
		dataRender[ "id" ] = storyId;
		
		//
		popup.hide( $popup );
		$editableContents.hide();
		$editableComposerWrap.html( _this._ejsEditableComposerTemplate.render( dataRender ) );
		$editableComposerWrap.show();
		
		setTimeout( function(){
			
			//
			var
			textareaContentMention = textareaContentDecoration = d["text"].replace( /\r\n/gi, "\n" ),
			$input = $( "#ta-message-text-edit-" + storyId );
			
			//
			$input.storywriter();
			
			//1.
			var
			decoItems = textareaContentDecoration.match(/\[B\].*\[\/B\]/g) || [];
			$.each( decoItems, function( index, decoText ) {
				var	text = decoText.replace( "[B]", "" ).replace( "[/B]", "" );
				textareaContentMention = textareaContentMention.replace( decoText, text );
			});		
			
			//2. 
			var mentionItems = textareaContentMention.match(/@\[[0-9]+:([^0-9]+)\]/g) || [];
			$.each( mentionItems, function( index, mentionText ) {
				var	
				s = mentionText.split(":"),
				id = s[0].replace( "@[", ""),
				text = s[1].replace( "]", "" ),
				beginAt = textareaContentMention.indexOf( mentionText );

				MentionInputListener.insertMention( $input, {
					"beginAt": beginAt,
					"endAt": beginAt + text.length - 1,
					"text": text,
					"id": id
				} );
				
				textareaContentDecoration = textareaContentDecoration.replace( mentionText, text );
				textareaContentMention = textareaContentMention.replace( mentionText, text );
			});

			//3.
			$.each( decoItems, function( index, decoText ) {
				var	
				text = decoText.replace( "[B]", "" ).replace( "[/B]", "" ),
				beginAt = textareaContentDecoration.indexOf( decoText );
				MentionInputListener.insertDecoration( $input, {
					"beginAt": beginAt,
					"endAt": beginAt + text.length - 1,
					"text": text,
					"type": "bold"
				} );
				textareaContentDecoration = textareaContentDecoration.replace( decoText, text );
			});		
			
			//4.
			MentionInputListener.onContext( $input, textareaContentDecoration, -1 );
			StoryWriter.adjust.call( $input );	
			
		}, 100);
	},
	onMentionClicked: function( $event ){
		$event.preventDefault();
		location.href="/user/" + $(this).data("uid");
	},
	onHashClicked: function( $event ) {
		$event.preventDefault();
		location.href="?q%5B%5D=" + "%23" + $(this).text().substring($(this).text().indexOf("#") + 1);
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
				var
				$icon = $(this).find("span"); 
// 				if( $icon.length == 0 ) {
// 					$icon = $(this).children( ":first" );
// 				}
				
				$icon.addClass("reacted");
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
	onAjaxDeleteStory: function( data ) {
		if(data.result != "success"){
			MessageBox( "스토리", "스토리 삭제중 에러가 발생했습니다.<br>" + data.message, MB_ERROR );			
			return;
		}
		
		var
		$si = $(this).parents(".rw-ui-stream-story"),
		$pprev,
		$prev = $si.prev(),
		$next = $si.next();
		
		while( $prev.length > 0 ) {
			if( $prev.hasClass( "rw-ui-stream-story" ) ) {
				break;
			}
			$pprev = $prev;
			$prev = $prev.prev();
		}

		while( $next.length > 0 ) {
			if( $next.hasClass( "rw-ui-stream-story" ) ) {
				break;
			}
			$next = $next.next();
		}
		
		if( $prev.length == 0 ) {
			if( $pprev.hasClass( "timeline-label" ) ) {
				$pprev.addClass( "_empty" );
			}
			
			var dataFWMap = $.parseJSON( Story._$streamList.attr( "data-fw-map" ) );
			
			if( $next.length > 0 ){
				var dm = $next.data( "map" );
				dataFWMap["contentId"] = dm['id'];
			} else {
				dataFWMap["contentId"]="";
			}
			Story._$streamList.attr( "data-fw-map", JSON.stringify( dataFWMap ) );
		}
		
		if( $next.length == 0 ) {
			var dataBWMap = $.parseJSON( Story._$streamList.attr( "data-bw-map" ) );
			
			if( $prev.length > 0 ) {
				var dm = $prev.data( "map" );
				dataBWMap[ "contentId" ] = dm[ "id" ];
			}else{
				dataBWMap[ "contentId" ] = "";
			}
			Story._$streamList.attr( "data-bw-map", JSON.stringify( dataBWMap ) );
		}
		
		$si.fadeOut( 600, "linear", function(){ $( this ).remove(); });			
	},
	copyURlToClipboard: function( anchor ){
		window.prompt ("Ctrl+C 또는 CMD+C(Apple,Mac)를 누르면 이 스토리의  URL이 복사됩니다.", $(anchor).attr("href"));	
	}	
};


/* -- Reply Object-- */
var Reply = {
	MAX_FILESIZE:__usize,
	_wait: 0,
	_ejsReplyTemplate: null,
	_autoresizeAdjustActive: false,
	_autoresizeInputOffset: 0,
	_$autoresizeMirror: null,
	_inputListeners: [],
	_keyContextLookupInterval: null,
	session: {},
	_$fileAttachPreview: null,
	_$composerOut: null,
	init: function() {
		//
		this._ejsReplyTemplate = new EJS({ url: "/assets/sunny/2.0/js/template/story-stream-reply.ejs?v=1.0"});
		
		//	
		this._$autoresizeMirror = $( "#ta-cmmnt-mirroring" );
		
		//
		$( document.body ).onHMOClick( ".btn-add-commnet", this.onAddCommentClicked );
		
		//
		this.textareaAutoResize();
		
		//
		this.attachInputListener( MentionInputListener );
		
		//
		$( document.body ).onHMOClick( ".cmnt-a-pview-close", this.onFileAttachViewCloseClicked );
		
		//
		$( document.body ).
		on( "keydown", ".ui-add-comment-input", this.onKeydown ).
		on( "focus", ".ui-add-comment-input", this.onFocus ).
		on( "blur", ".ui-add-comment-input", this.onBlur );
	},
	openPhotoFullScreen: function( anchor ) {
		var
		$w = $( window ),
		$anchor = $( anchor ),
		$mainContainer = $( "#main-container" ),
		$screen = $( "#reply-photo-viewer-full-screen" ),
		st = $( window ).scrollTop();
		//
		$( "#rw-snn-wrap" ).addClass( "_theater" );
		$screen.find( "img" ).attr( "src", $anchor.attr( "data-src-o" ) );
		$screen.show();
		$mainContainer.css({ "top": -1*st  });
	},
	closePhotoFullScreen: function() {
		var 
		$w = $( window ),
		$screen = $( "#reply-photo-viewer-full-screen" ),
		$mainContainer = $( "#main-container" ),
		st = parseInt( $mainContainer.css( "top" ), 10 );

		$( "#rw-snn-wrap" ).removeClass( "_theater" );	
		$mainContainer.css( { "top":0  } );
		$w.scrollTop( -1*st );
		$screen.hide();
	},	
	clearAttachView: function( $attachView ) {
		$attachView.find( ".ui-scaled-image-container" ).html( "" );
		$attachView.hide();
	},
	onFileAttachViewCloseClicked: function( $event ) {
		$event.preventDefault();
		
		var 
		$anchor = $( this ),
		$preview = $anchor.parents( ".ufi-comment-file-attached-preview" );
		Reply.clearAttachView( $preview );
	},
	onFileSelected:function( file ) {
		var
		f = ( file.files ) ? file.files : [ file ],
		countFile = f.length,
		$file = $( file );
		
		if( countFile == 0 ){
			return;
		}
		
		//
		this._$composerOut = $file.parents( ".ui-composer-out" );
		this._$fileAttachPreview = this._$composerOut.find( ".ufi-comment-file-attached-preview" );
		
		//check file size
		for( var i = 0; i < countFile; i++ ) {
			if( f[i].size > this.MAX_FILESIZE ) {
				MessageBox( "파일업로드", "다음 파일이 업로드 용량을 초과 하였습니다.<br>" + f[i].name + " : " + " 파일 사이즈 " + this.MAX_FILESIZE + "byte", MB_INFORMATION );
				return;		
			}		
		}
		
		// counting and generate session id
		var uploadId = "Up" + FileUploader.uploadIdCounter++;

		// #number session is...
		this.session[ uploadId ] = {
			id: uploadId,	
			countFile: countFile,
			onUploadFailure: function( result ) {
				MessageBox( "파일업로드", "파일 업로드가 실패하였습니다.<br>" + result.message, MB_ERROR );
			},
			onUploadComplete: function( result ) {
				var
				result = $.parseJSON( result ),
				$ta = Reply._$composerOut.find( ".ui-add-comment-input" );
				$container = Reply._$fileAttachPreview.find( ".ui-scaled-image-container" );
				
				if( result.resultCode != 0 ) {
					MessageBox( "파일업로드", "파일 업로드가 실패하였습니다.<br>" + result.message, MB_ERROR );
					return;
				}

				var
				datas = result.data;
				for( var i = 0; i < datas.length; i++ ) {	
					var
					html,
					width = 96,
					height = 96,
					data = datas[i];
					
					if( data.mediaType != 2 ) { // not image
						html =  "<span class='uploaded-files file' data-up-file='" + data.id + "'>" + 
								"<span>" + data.fileName + "</span></span>";
					} else if( data.mediaType == 2) { // image
						if( data.height > 96 ) {
							height = 96;
							width = data.width * height / data.height;
						} else {
							height = data.height;
							width = data.width;
						}
						html =  "<img class='uploaded-files scaled-image-fit-width img' data-up-file='" + data.id + 
								"' src='" + data.relativePath + "/" + data.prefix + "_" + data.id + "_m.jpg' alt='' " + 
								"width='" + width + "' height='" + height + "'>";
					}
					
					$container.css({ "width": width, "height": height });
					$container.html( html );
				}
				
				$ta.focus();
			} 
		}
		
		//
		Reply._$fileAttachPreview.find( ".ui-scaled-image-container" ).html( "<div class='init-loader'><img src='/assets/sunny/2.0/img/ajax-loader-big.gif' style='width:32px; height:32px' ></div>" );
		this._$fileAttachPreview.show();
		
		//
		FileUploader[ "onUploadFailure" ] = this.session[ uploadId ].onUploadFailure.bind( this.session[ uploadId ] );
		FileUploader[ "onUploadComplete" + uploadId ] = this.session[ uploadId ].onUploadComplete.bind( this.session[ uploadId ] );
		
		//
		var $iframe = $( "<iframe data-on-submit='false' name='if-file-upload-" + uploadId + "' style='display:none; width:0; height:0;'></iframe>" );
		$iframe.insertAfter( $( "body" ) );
		$iframe.load( function() {
			var $form = $(file).parent();
			var onSubmit = $iframe.attr( "data-on-submit" ) == "true";
			if( onSubmit ) {
				$form.attr( "target", "" );
				$iframe.attr( "data-on-submit", "false" );
				return;
			}
			
			$form.find("[name=upid]").val( uploadId );
			$form.attr( "target", "if-file-upload-" + uploadId );
			$iframe.attr( "data-on-submit", "true" );
			$form.submit();
		} );
		
		$iframe.attr( "src", "/upload" ); 		
	},
	onFocus: function( $event ) {
		// $.log( "Reply.onFocus" );
		var $ta = $( this );
		Reply._keyContextLookupInterval || Reply.onContext.call( $ta );
	},
	onBlur: function( $event ) {
		// $.log( "Reply.onBlur" );
		var $ta = $( this );
		if( Reply._keyContextLookupInterval ) {
            window.clearInterval( Reply._keyContextLookupInterval );
            Reply._keyContextLookupInterval = null;
		}
	},
	onKeydown: function( $event ) {
		// $.log( "Reply.onKeydown" );
		if( MentionSelectBox.isVisible() ) {
			return;
		}
		var
		ta = this,
		$ta = $( ta );
		if( $event.keyCode != 10 && $event.keyCode != 13 ) {
			if( $event.keyCode == 27 ) {
				$ta.blur();
			} else if( $event.keyCode == 86 && $event.ctrlKey ) {	/* ctrl + v */
				setTimeout( function() {
					Reply.onPaste.call( $ta );
				}, 100);
			}
			return;
		}
		if( !__mobile__["is"] && !__mobile__[ "ipad" ]  && $event.shiftKey === false ) {
			$event.preventDefault();		
			setTimeout( function() {
				Reply.ajaxPost( $ta );
			}, 10 );
			return;								
		}
		
		Reply.testEnter( $ta );
	},
	onPaste: function( $ta ) {
	},
	onContext: function() {
		//
		Reply._keyContextLookupInterval && window.clearInterval( Reply._keyContextLookupInterval );
		
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
		var countListeners = Reply._inputListeners.length;
		for( var i = 0; i < countListeners; i++ ) {
			Reply._inputListeners[ i ].onContext && Reply._inputListeners[ i ].onContext( $ta, $ta.val(), offset );
		}			
		
	    //
		Reply._keyContextLookupInterval = window.setInterval( Reply.onContext.bind( $ta ), 5 );
	},
	attachInputListener: function( inputListener ) {
		var index = this._inputListeners.length;
		
		if( inputListener && inputListener.onContext ) {
			this._inputListeners[ index ] = inputListener;
			this._inputListeners[ index ].onAttached && this._inputListeners[ index ].onAttached();
		}
	},	
	textareaAutoResize: function(){
		$( ".data-textarea-autogrow-set" ).
		each( function() {
			var 
			ta = this, 
			$ta = $( this );

			Reply._$autoresizeMirror.css( "width", $ta.width() - 10) ;
			
			if( $ta.css( "box-sizing") === "border-box" || 
				$ta.css( "-moz-box-sizing") === "border-box" || 
				$ta.css( "-webkit-box-sizing") === "border-box" ) {
				Reply._autoresizeInputOffset = $ta.outerHeight() - $ta.height();
			}
			
			if( "onpropertychange" in ta ) {
				if( "oninput" in ta ) {
			    	ta.oninput = Reply.onAdjustTextArea;
			    	$ta.keydown( function() { 
			    		setTimeout( Reply.onAdjustTextArea.bind(ta), 50 );
			    	});
				} else {
					ta.onpropertychange = Reply.onAdjustTextArea;
			    }
			} else {
			    ta.oninput = Reply.onAdjustTextArea;
			}
			
			$ta.removeClass( "data-textarea-autogrow-set" );
		});	
	},
	clearTextarea: function( $ta ) {
		$ta.val("");
		setTimeout( function(){
			Reply.onAdjustTextArea.call( $ta.get( 0 ) )
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
		$uploadedFiles = $form.parents( ".ui-composer-out" ).find(".uploaded-files"),
		fileAttached = false;

		if( $uploadedFiles.length == 1 ){
			data["mediaId"] = $uploadedFiles.data( "up-file" );
			fileAttached = true;
		}
		
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
		$ta = $btn.parents( ".mentions-type-head" ).find( ".ui-add-comment-input" );
		Reply.ajaxPost( $ta );
	},
	onAjaxReplyPost: function( data ) {
		var
		$li,
		$ta = $( this ),
		$attachView = $ta.parents( ".ui-composer-out" ).find( ".ufi-comment-file-attached-preview" );
		
		Reply._wait = 0;
		
		if( data.result != "success" ) {
			MessageBox( "댓글", "댓글 게시중에 에러가 발생했습니다.<br>" + data.message, MB_ERROR );
			return;
		}

		( $li = $( "<li class='ui-react-row'>" ) ).
		insertBefore( $ta.parents( ".ui-react-row" ) ).
		html( Reply._ejsReplyTemplate.render( data.data ) );
		
		refresh_timesince.call( $li.find( ".livetimestamp" ) );
		
		Reply.clearAttachView( $attachView );
		Reply.clearTextarea( $ta );
		Reply.onContext.call( $ta );
		
		$ta.blur();
	},
	onAdjustTextArea: function() {
		if( Reply._autoresizeAdjustActive ) { 
			return; 
		}
		
		var	
		ta = this,
		$ta = $(ta), 
		mirror = Reply._$autoresizeMirror.get(0),
		original = $ta.height(),
		height;

		Reply._autoresizeAdjustActive = true;
		Reply._$autoresizeMirror.val( $ta.val() );
			
		mirror.scrollTop = 0;
		mirror.scrollTop = 9e4;
		height = mirror.scrollTop + Reply._autoresizeInputOffset;

		(original !== height) && $ta.css( "height",height );
		setTimeout( function (){ 
			Reply._autoresizeAdjustActive = false; 
		}, 1 );
	},	
	onAjaxReplyDelete: function( data ) {
		if( data.result != "success" ) {
			MessageBox( "댓글", "댓글 삭제중에 에러가 발생했습니다.<br>" + data.message, MB_ERROR );
			return;
		}
		$( this ).parents( ".ui-react-row" ).remove();
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
		$parent = $anchor.parents( ".cmnt-row-wrap" ),
		count = data.data.length,
		remains = parseInt( $anchor.attr( "data-reply-count" ),10 ),
		reuestData = $.parseJSON( $anchor.attr( "data-request-map" ) );

		for( var i=0; i < count; i++ ) {
			var $li;
			( $li = $( "<li class='ui-react-row'>" ) ).
			html( Reply._ejsReplyTemplate.render( data.data[i] ) );
			$parent.next().prepend( $li );
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
	}
};


/* -- Object Stream -- */
var Stream = {
	_DETECT_SCROLL_GAP: 200,
	_TIMEOUT: 3000,
	_isStreaming: false,
	_isBackwardEnd: false,
	_rewind: false,	
	_$win: null,
	_$doc: null,
	_$streamList: null,
	_$streamPager: null,
	_$grobalWrap: null,
	_ejsStoryStreamTemplate: null,
	preStoryCreatedate: "${ empty lastStoryCDate ? "" : lastStoryCDate }",
	init: function() {
		//
		this._$win = $( window ),
		this._$doc = $( document ),
		this._$grobalWrap = $( "#rw-snn-wrap" ),
		this._$streamList = $( "#story-stream-list" ),
		this._$streamPager = $( "#ui-stream-pager" );
		//
		this._ejsStoryStreamTemplate = new EJS( { url: "/assets/sunny/2.0/js/template/story-stream.ejs?v=1.0"});
		//	 
		$( "#stream-pager-retry" ).onHMOClick( null, this.onPagerRestryClicked );
		this._$win.scroll( this.onWindowScroll );		
	},
	streaming: function( direction ) {
		if( !direction && this._isBackwardEnd ) { 
			return; 
		} 
		if( this._isStreaming ) { 
			return; 
		}
		if( this._rewind ) {
			this._rewind = false;
			return;
		}
			
		this._isStreaming = true;
			
		if( direction ) {
		} else {
			this._$streamPager.hide().removeClass( "error no-more more-loader" ).addClass( "more-loader" ).show();
		}
			
		setTimeout( function() {
			var
			url = Stream._$streamList.attr( "data-stream-url" ),
			mode = Stream._$streamList.attr( "data-async" );
			if( !mode ) { 
				mode = "async-get"; 
			}
				
			var
			modes = mode.split( "-" ),
			async = ( modes[0] == "async" ),
			type = ( modes[1] == "post" ) ? "post" : "get",
			data = $.parseJSON( Stream._$streamList.attr( direction ? "data-fw-map" : "data-bw-map" ) );
			data["q[]"] = [
			<c:forEach items="${queries }" var="query" varStatus="status">
				"${query }"${status.last ? "" : "," } 
			</c:forEach>
			];
				
			$.ajax({
				url:url,
				async:async,
				type:type,
				dataType:"json",
				contentType:"application/json",
				data:data,
				timeout: Stream.__TIMEOUT,
				success:( direction ? Stream.onForwardStreaming : Stream.onBackwardStreaming ),
				error:function( jqXHR, textStatus, errorThrown ) {
					if( textStatus == "timeout" ) {
						Stream._rewind = true;
						Stream._isStreaming=false;
						Stream._$streamPager.hide().removeClass( "error no-more more-loader" ).addClass( "error" ).show();
					} else {
						MessageBox( "스토리 스트리밍", "스토리 스트리밍에 다음과 같은 에러가 발생 헀습니다.<br>" + errorThrown, MB_ERROR );
					}
				}	
			});
		}, 10);
	},
	onPagerRestryClicked: function( $event ) {
		$event.preventDefault();
		this.blur();
		Stream._rewind = false;
		setTimeout( function(){
			Stream.streaming( false );
		}, 0);
	},
	onWindowScroll: function( event ) {
		var
		wct = Stream._$win.scrollTop(),
		wh = Stream._$win.height(),
		dh = Stream._$doc.height();
			
		if( ( ( wct + wh + Stream._DETECT_SCROLL_GAP ) >= dh ) &&
			( Stream._$grobalWrap.hasClass( "open-menu" ) == false ) &&
			( Stream._$grobalWrap.hasClass( "_theater" ) == false ) && 
			( !NotifyFlyout || !NotifyFlyout.isFlyout ) ) {
			
			Stream.streaming( false );
		}
	},
	onForwardStreaming: function( result ) {
		var
		data=result.data,
		l = data.length;
			
		if( !data || l == 0 ){
			Stream._isStreaming=false;
			return;
		}
			
		try{
			var
			l=data.length,
			lastData = data[ l - 1 ],
			dt = new Date( lastData.createDate ), m, d,
			cdate = dt.getFullYear() + "." + ( ( m = dt.getMonth() + 1 ) < 10 ? "0" : "" ) + m + "." + ( ( d = dt.getDate() ) < 10 ? "0" : "" ) + d,
			$firstTimeLabel = Stream._$streamList.children( ":first" ), /* must exists */
			isPreTimeline = $firstTimeLabel.attr( "data-ltime" ) == cdate,
			$beforeElement;
			
			Stream.preStoryCreatedate = "";
			
			if( isPreTimeline ){ 
				$beforeElement = $firstTimeLabel.next();
			} else {
				$beforeElement = $firstTimeLabel;			
			}
				
			for( var i = 0; i < l; i++ ) {
				(function( d ) {
					var
					ld = d;
					setTimeout( function() {
						var html = Stream._ejsStoryStreamTemplate.render( ld );
						
						isPreTimeline && $firstTimeLabel.remove();
						if( $beforeElement ) {
							$beforeElement.before( html );
						} else {
							Stream._$streamList.append( html );
						}
					}, __mobile__["is"] ? 60 : 0 );
				}) ( data[ i ] );			
			}
				
			setTimeout( function() {
				var dataMap = $.parseJSON( Stream._$streamList.attr( "data-fw-map" ) );
				if( l > 0 ) {
					dataMap["contentId"] = data[l-1]["id"];
					Stream._$streamList.attr( "data-fw-map", JSON.stringify( dataMap ) );
					if( Stream._$streamList.children().length == 1 ) {
						dataMap = $.parseJSON( Stream._$streamList.attr( "data-bw-map" ) );
						dataMap["contentId"] = data[0]["id"];
						Stream._$streamList.attr( "data-bw-map", JSON.stringify( dataMap ) );
					}
				}
				
				timesince();
				Reply.textareaAutoResize();	
				Stream._isStreaming=false;
				
			}, 1000 );
				
		} catch( e ) {
			$.error(e);
		}
	},
	onBackwardStreaming: function( result ) {
		var
		data = result.data;
		if( !data || data.length == 0 ) {
			Stream._isStreaming = false;
			Stream._isBackwardEnd = true;
			Stream._$streamPager.hide().removeClass( "error no-more more-loader" ).addClass( "no-more" ).show();
			return;
		}
		try {
			var	l=data.length;
				
			for( var i = 0; i < l; i++ ) {
				( function( d ) {
					var ld = d;
					setTimeout( function() {
						Stream._$streamList.append(  Stream._ejsStoryStreamTemplate.render( ld ) );
					}, __mobile__["is"] ? 60 : 0 );
				}) ( data[ i ] );
			}

			setTimeout( function() {
				if( l > 0 ) {
					var dataMap;
					if( Stream._$streamList.children().length == 0 ){
						dataMap = $.parseJSON( Stream._$streamList.attr( "data-fw-map" ) );
						dataMap[ "contentId" ] = data[0]["id"];
						Stream._$streamList.attr( "data-fw-map", JSON.stringify( dataMap ) );
					}
					dataMap = $.parseJSON( Stream._$streamList.attr( "data-bw-map" ) );
					dataMap["contentId"] = data[l-1]["id"];
					Stream._$streamList.attr( "data-bw-map", JSON.stringify( dataMap ) );
				}
					
				timesince();
				Reply.textareaAutoResize();	
				Stream._isStreaming = false;
				
			}, __mobile__["is"] ? 1200 : 0 );
				
		} catch ( e ) {
			Stream._$streamPager.hide().removeClass( "error no-more more-loader" ).addClass( "error" ).show();
			//$.error(e);
		}
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
	}
};


/* -- dialog object ( DialogStratChat ) -- */
var DialogFeelUsers = {
	_iscroll: null,		
	onAjaxFeelUsers: function( data ) {
		var
		_this = DialogFeelUsers,
		
		html = new EJS({ url : "/assets/sunny/2.0/js/template/feel-users.ejs" }).render( data ); 	
		
		_this._iscroll = new iScroll( "dialog-feel-users-scrollable-area", {
			onScrollEnd: _this.onScrollEnd.bind( _this )
		});
		
		$("#dialog-feel-users-list").html( html );
	},
	onScrollEnd: function(){
		//invite_friend_streaming(false);
		//this._iscroll.refresh();
	},
	onClose: function() {
		$("#dialog-feel-users-list").html( "" );
	}
};

/* -- dialog object ( DialogContentPermission ) -- */
var DialogContentPermissions = {
	_iscroll: null,	
	onAjaxContentPermissions: function( data ){
		$.log( data );
		

		if(data.result == "fail"){
			MessageBox( "공개범위 정보 가져오기", "공개 범위 정보를 가져오는 중 다음과 같은 오류가 발생했습니다.<br>" + data.message, MB_ERROR );
			return;
		}
		
		var
		_this = DialogContentPermissions;		
		html = new EJS({ url : "/assets/sunny/2.0/js/template/content-permissions.ejs?v=1.0"}); 	

		_this._iscroll = new iScroll( "dialog-content-permissions-scrollable-area", {
			onScrollEnd: _this.onScrollEnd.bind( _this )
		});

		$( "#dialog-content-permissions-list" ).html( html );
	},
	onScrollEnd: function(){
		//invite_friend_streaming(false);
		//this._iscroll.refresh();
	},
	onClose: function() {
		$("#dialog-content-permissions-list").html( "" );
	}	
};


/* -- dialog object ( DialogBookmark ) -- */
var DialogBookmark = {
	onRegisterClicked: function(){
		var
		_this = DialogBookmark,
		data = $( "#form-bookmark").serializeJSON();
		
		data[ "content" ] = { "id": $(this).data("cid") };
		$.ajax({
			url:"/bookmark/add",
			type:"post",
			dataType:"json",
			headers: {
				'Accept':'application/json',
				'Content-Type':'application/json'
			},
		    data: JSON.stringify( deepen( data ) ),
		    success: _this.onAjaxBookmarkAdd,
			error:function( jqXHR,textStatus,errorThrown ) {
				$.log( "error:Event.__inlineSubmit:" + errorThrown );
			}
		});		
	},
	onAjaxBookmarkAdd: function( data ) {
    	if( data.result == "fail" ){
    		MessageBox( "스크랩", "다음과 같은 에러가 발생했습니다.<br>"+ data.message, MB_ERROR );
    		return false; 
    	}
		MessageBox( "스크랩", "스크랩 등록이 성공 했습니다.", MB_INFORMATION );
	}
}


/*-- Popup Extends -- */
var ContextMenuPopupToggler = {
	onMenuShow: function() {
		var $sel = $( "#" + this.attr( "data-popup-trigger" ) );
		$sel.parent().addClass("open-toggler");
		$sel.parent().parent().addClass("hide-block");
	},
	onMenuHide: function(){
		var $sel = $( "#" + this.attr( "data-popup-trigger" ) );
		$sel.parent().removeClass("open-toggler");
		$sel.parent().parent().removeClass("hide-block");
	}	
};


<c:if test="${sunny.device.mobile }">
/* -- mod-story-media-swipe -- */
var SMSwipe = {
	__core__: {
		init: function() {
			if( !__mobile__["is"] ) {
				return;
			}
			SMSwipe[ "__ui__" ].init();
			$(document.body).on( "touchstart", ".swipe", SMSwipe[ "__event__" ].onTouchStart );
		}
	},
	__ui__: {
		__speed: 300,
		__width: null,
		init: function() {
			var
			$scaledThumbs = $( ".ui-scaled-thumb" ),
			$scaledThumb = $( $scaledThumbs.get(0) );
			
			this.__width = $scaledThumb.width();
			if( !this.__width ) {
				return;
			}
			this.__width += parseInt( $scaledThumb.css( "padding-left" ), 10 ) + parseInt( $scaledThumb.css( "padding-right" ), 10);
			this.__width += parseInt( $scaledThumb.css( "margin-left"), 10 ) + parseInt( $scaledThumb.css( "margin-right" ), 10 );
			this.__width += parseInt( $scaledThumb.css( "borderLeftWidth"), 10) + parseInt( $scaledThumb.css("borderRightWidth"), 10 );
		},
		translate: function( element, dist, speed ) {
			var
			$element = $(element),
			index = parseInt( $element.attr( "data-index" ), 10),
			slide = $element.find( ".swipe-wrap" ).get( 0 ),
			style = slide && slide.style;

		    if( !style ){
		    	return;
		    }

		    dist = ( index == 0 ? 0 : -1*index) * this.__width + dist; 
		    	
		    style.webkitTransitionDuration =
		    style.MozTransitionDuration =
		    style.msTransitionDuration =
		    style.OTransitionDuration =
		    style.transitionDuration = ( speed < 0 ? this.__speed : speed )  + 'ms';

		    style.webkitTransform = 'translate(' + dist + 'px,0)' + 'translateZ(0)';
		    style.msTransform =
		    style.MozTransform =
	    	style.OTransform = 'translateX(' + dist + 'px)';

		  },
		  move: function( element, isSliding, direction ) {
			  if( isSliding ) {
				  this.translate( element, 0, this.__speed );
			  } else {
				  this.translate( element, 0, this.__speed );
			  }
		  }		  
	},
	__event__: {
		__start: null,
		__isScrolling: undefined,
		__delta: null,
		onTouchStart: function( $event ) {
			var 
			event = $event.originalEvent,
			touches = event.touches[0];
			
			if( SMSwipe[ "__ui__" ].__width == null ) {
				var
				$scaledThumbs = $(this).find( ".ui-scaled-thumb" ),
				$scaledThumb = $( $scaledThumbs.get(0) );
				
				SMSwipe[ "__ui__" ].__width = $scaledThumb.width();
				SMSwipe[ "__ui__" ].__width += parseInt( $scaledThumb.css( "padding-left" ), 10 ) + parseInt( $scaledThumb.css( "padding-right" ), 10);
				SMSwipe[ "__ui__" ].__width += parseInt( $scaledThumb.css( "margin-left"), 10 ) + parseInt( $scaledThumb.css( "margin-right" ), 10 );
				SMSwipe[ "__ui__" ].__width += parseInt( $scaledThumb.css( "borderLeftWidth"), 10) + parseInt( $scaledThumb.css("borderRightWidth"), 10 );
			}
			SMSwipe[ "__event__" ].__start = {
    	        x: touches.pageX,
    	        y: touches.pageY,
    	        time: +new Date
			};			
			
			// used for testing first move event
			SMSwipe[ "__event__" ].__isScrolling = undefined;

			// reset delta and end measurements
			SMSwipe[ "__event__" ].__delta = {};
		      
			this.addEventListener( "touchmove", SMSwipe[ "__event__" ].onTouchMove, false );
			this.addEventListener( "touchend", SMSwipe[ "__event__" ].onTouchEnd, false );
		},
		onTouchMove: function( event ) {
			// ensure swiping with one touch and not pinching
			if( event.touches.length > 1 || event.scale && event.scale !== 1 ) {
				return;
			}

			var
			$element = $(this),
			touches = event.touches[0],
			countItem = parseInt( $element.attr( "data-count-item" ), 10),
			index = parseInt( $element.attr( "data-index" ), 10);
		      
			// measure change in x and y
			SMSwipe[ "__event__" ].__delta = {
				x: touches.pageX - SMSwipe[ "__event__" ].__start.x,
				y: touches.pageY - SMSwipe[ "__event__" ].__start.y
			}

			//
			var direction = SMSwipe[ "__event__" ].__delta.x < 0;	

			if( ( index == 0 && !direction ) || ( index + 1 == countItem && direction ) ) {
				return;
			}
			
			// determine if scrolling test has run - one time test
			if ( typeof SMSwipe[ "__event__" ].__isScrolling == 'undefined') {
				SMSwipe[ "__event__" ].__isScrolling = !!( SMSwipe[ "__event__" ].__isScrolling || Math.abs( SMSwipe[ "__event__" ].__delta.x ) < Math.abs( SMSwipe[ "__event__" ].__delta.y ) );
			}

			//if user is not trying to scroll vertically
			if ( !SMSwipe[ "__event__" ].__isScrolling ) {			
				event.preventDefault();
				SMSwipe[ "__ui__" ].translate( this, SMSwipe[ "__event__" ].__delta.x, 0 );
			}
		},
		onTouchEnd: function( event ) {
			var $element = $(this),
				countItem = parseInt( $element.attr( "data-count-item" ), 10),
				index = parseInt( $element.attr( "data-index" ), 10);

			//measure duration
			var duration = +new Date - SMSwipe[ "__event__" ].__start.time;
			
			// determine if slide attempt triggers next/prev slide
			var isValidSlide = 
				Number(duration) < 250																// if slide duration is less than 250ms
				&& Math.abs( SMSwipe[ "__event__" ].__delta.x ) > 20            					// and if slide amt is greater than 20px
				|| Math.abs( SMSwipe[ "__event__" ].__delta.x ) > SMSwipe[ "__ui__" ].__width/2;  	// or if slide amt is greater than half the width

			// determine direction of swipe (true:right, false:left)
			var direction = SMSwipe[ "__event__" ].__delta.x < 0;		            
				
			if( !SMSwipe[ "__event__" ].__delta.x || ( index == 0 && !direction ) || ( index + 1 == countItem && direction ) ) {
				this.removeEventListener( "touchmove", SMSwipe[ "__event__" ].onTouchMove, false );
				this.removeEventListener( "touchend", SMSwipe[ "__event__" ].onTouchEnd, false );		
				return;
			}
			
			// if not scrolling vertically
			if( !SMSwipe[ "__event__" ].__isScrolling ) {
				$element.attr( "data-index", direction ? ++index : --index );
				SMSwipe[ "__ui__" ].translate( this, 0, -1 );
			}
			
			// kill touchmove and touchend event listeners until touchstart called again
			this.removeEventListener( "touchmove", SMSwipe[ "__event__" ].onTouchMove, false );
			this.removeEventListener( "touchend", SMSwipe[ "__event__" ].onTouchEnd, false );		
		}
	}	
}
</c:if>

$( function() {
	Stream.init();
	Story.init();
	Reply.init(); 
	typeof(SMSwipe) != "undefined" && SMSwipe && SMSwipe[ "__core__" ].init();
} );
</script>

<c:choose>
	<c:when test="${ countStory == 0 }">
	
		<div id="story-stream-list"
			role="list"
			class="rw-ui-stream-large-headline rw-ui-stream rw-timeline-stream"
			data-fw-map='{"top":true,"contentId":"","size":"${param.sizeFetchForward}"}'
			data-bw-map='{"top":false,"contentId":"","size":"${param.sizeFetchBackward }"}'
			data-stream-url="${param.streamUrl }"
			data-async="async-get">
	
			<div class="timeline-label _today _empty" data-ltime="${currentDate }">
				<div class="label-header">
					<div class="label-header-top">
						<div>
							<h3 class="label-header-title"><fmt:formatDate value='${now }' pattern="MM월 dd일" />, <span class="_today-empty-msg">오늘의 첫 ${empty param.feedback ? '스토리를' : '피드백을'} 등록해 보세요.</span></h3>
						</div>
					</div>
				</div>
				<div class="_mline"></div>
			</div>
			<div class="timeline-label" >
				<%-- 이게 있어야 에러가 안남 firstElement.next() 와 같은 식으로 되어있음 --%>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<div id="story-stream-list"
			role="list"
			class="rw-ui-stream-large-headline rw-ui-stream rw-timeline-stream"
			data-fw-map='{"top":true,"contentId":"${stories[0].id}","size":"${param.sizeFetchForward}"}'
			data-bw-map='{"top":false,"contentId":"${stories[countStory-1].id}","size":"${param.sizeFetchBackward }"}'
			data-stream-url="${param.streamUrl }"
			data-async="async-get">
			
			<c:set var="preStoryCreateDate" value="" />

			<c:forEach items="${stories }" var="story" varStatus="status">
			
				<c:set var="storyCreateDate">
					<fmt:formatDate value='${story.createDate }' pattern="yyyy.MM.dd" />
				</c:set>
				<c:if test="${preStoryCreateDate != storyCreateDate }">
					<c:choose>
						<c:when test="${storyCreateDate == currentDate  }">
							<div class="timeline-label _today" data-ltime="${currentDate }">
								<div class="label-header">
									<div class="label-header-top">
										<div>
											<h3 class="label-header-title"><fmt:formatDate value='${now }' pattern="MM월 dd일" />, <span class="today-text">오늘</span><span class="_today-empty-msg">의 첫 ${empty param.feedback ? '스토리를' : '피드백을'} 등록해 보세요.</span></h3>
										</div>
									</div>
								</div>
								<div class="_mline"></div>
							</div>
						</c:when>
						<c:otherwise>
							<c:if test="${preStoryCreateDate == '' }">
								<div class="timeline-label _today _empty" data-ltime="${currentDate }">
									<div class="label-header">
										<div class="label-header-top">
											<div>
												<h3 class="label-header-title"><fmt:formatDate value='${now }' pattern="MM월 dd일" />, <span class="_today-empty-msg">오늘의 첫 ${empty param.feedback ? '스토리를' : '피드백을'} 등록해 보세요.</span></h3>
											</div>
										</div>
									</div>
									<div class="_mline"></div>
								</div>
							</c:if>
							<div class="timeline-label" data-ltime="${storyCreateDate }">
								<div class="label-header">
									<div class="label-header-top">
										<div>
											<h3 class="label-header-title"><fmt:formatDate value='${story.createDate }' pattern="MM월 dd일" /></h3>
										</div>
									</div>
								</div>
								<div class="_mline"></div>
							</div>
								
						</c:otherwise>
					</c:choose>
					
					<c:set var="preStoryCreateDate" value="${storyCreateDate }" />
										
				</c:if>
				
				<div class="timeline-container rw-ui-stream-story" id="story-box-${story.id }" data-map='{"id":${story.id } }'>
					<div class="timeline-items-wrap">
							<div class="timeline-items">
								<div class="timeline-item item-header">
									<div class="rw-story-title">
										<c:if test="${story.systemMessage }">
											<span class="profile-pic">
												<img class="img" alt="${story.userName }" aria-label="${story.userName }" src="${not empty story.postUserProfilePic ? story.postUserProfilePic : story.userProfilePic}">
											</span>
										</c:if>
										<c:if test="${not story.systemMessage }">
											<a class="profile-pic" href="/user/${story.postUserId}">
												<img class="img" alt="${story.userName }" aria-label="${story.userName }" src="${not empty story.postUserProfilePic ? story.postUserProfilePic : story.userProfilePic}">	
											</a>
										</c:if>
										
										<div class="c">
											<h3>
												<c:choose>
													<c:when test="${story.type == 8 }">
														<a class="actor-link" href="${sunny.path }/user/${story.userId}">
															<strong>${story.userName }</strong>
														</a>
														님이 <a href="#">결재</a>를 상신했습니다.
														<c:choose>
															<c:when test="${story.approval.status == 0 }">
																<span class="label-green ml5 ">진행중</span>
															</c:when>
															<c:when test="${story.approval.status == 1 }">
																<span class="label-red ml5 ">반려됨</span>
															</c:when>
															<c:when test="${story.approval.status == 2 }">
																<span class="label-blue ml5 ">승인됨</span>
															</c:when>
														</c:choose>
														<c:if test="${story.approval.inRequestReject == true }">
															<span class="label-red ml3">반려 요청중</span>
														</c:if>
													</c:when>
													<c:when test="${story.systemMessage }">
														<strong class="actor">${story.userName }</strong>
													</c:when>
													<c:when test="${story.groupStory == true }">
								 						<a href="/user/${story.userId}">
																<strong class="actor">${story.userName }</strong>
														</a>
														<i class="fa fa-caret-right bigger-130"></i>
														<a href="/group/${story.smallGroupId}">
															<strong class="actor">${story.smallGroupName }${story.smallGroupType == 3 ? '(부서)' : ( story.smallGroupType == 5 ? '(소그룹)' : '')}</strong>
														</a>
													</c:when>
													<c:when test="${not empty story.postUserId && story.postUserId != story.userId}" >
														<a class="actor-link" href="/user/${story.postUserId }">
															<strong class="actor">${story.postUserName }</strong>
														</a>
														<i class="fa fa-caret-right bigger-130"></i>
														<a href="/user/${story.userId }">
															<strong class="actor">${story.userName }</strong>
														</a>
													</c:when>
													<c:otherwise>
														<a class="actor-link" href="/user/${story.userId}">
															<strong class="actor">${story.userName }</strong>
														</a>
													</c:otherwise>
												</c:choose>	
											</h3>
											<div class="rw-story-ctime">
												<span>
													<a href="/story/${story.id }"><abbr data-utime="${story.arriveDate }" class="timestamp livetimestamp"  data-hover="tooltip" data-tooltip-alignh="left" aria-label="">&nbsp;</abbr></a>
												</span>
												<c:choose>
													<c:when test="${story.systemMessage }">
													
													</c:when>
													<c:when test="${story.permissionType == 0}">
														<span>&nbsp;</span>
														<i class="fa fa-lock fa-1g"></i>
														<span>&nbsp;</span>
													</c:when>
													<c:when test="${story.permissionType == 1 }">
														<span>&nbsp;</span>
														<i class="fa fa-globe fa-1g"></i>
														<span>&nbsp;</span>
													</c:when>
													<c:when test="${story.permissionType == 2 }">
														<span>&nbsp;</span>
														<i class="fa fa-user fa-1g"></i>
														<span>&nbsp;</span>
													</c:when>
													<c:otherwise>
														<strong>·</strong>
													</c:otherwise>
												</c:choose>
												<c:if test="${not story.systemMessage }">
												<span class="ip-address">
													<c:forTokens var="part" items="${story.ipAddress }" delims="." varStatus="status">${status.first ? '' : '.'}${status.index == 2 ? 'xxx' : part }</c:forTokens>
												</span>	
												</c:if>
												<span>&nbsp;</span>
												<a href="http://www.hellomyoffice.com/story/${story.id }" onclick="Story.copyURlToClipboard(this); return false;" role="button" data-hover="tooltip" data-tooltip-alignh="left" title="스토리 URL 클립보드로 복사"><i class="fa fa-clipboard fa-1g"></i></a>
											</div>
										</div>
									</div>
									<div class="rw-story-context-menu">
										<div class="rw-story-context-menu-item">
											<a	class="dropdown-toggle"
												id="story-${story.id }-context-trigger"
												aria-owns="story-${story.id }-context-popup-menus"
												aria-haspopup="true"
												rel="toggle">
												<i class="fa fa-chevron-down fa-1g"></i>
											</a>
											<c:choose>
											<c:when test="${story.type == 8 }">
												<ul id="story-${story.id }-context-popup-menus"
													class="dropdown-menu dropdown-info pull-right dropdown-caret ui-toggle-flyout"
													data-popup-trigger="story-${story.id }-context-trigger"
													data-popup-group="global">
													<c:choose>
													<c:when test="${authUserIsSuperAdmin || isAuthenticated==true && ( empty smallGroup || not empty smallGroupUser ) && story.userId == authUserId }">
														<li>
<!-- 															<a	href="#"  -->
<%-- 																data-request-map="{&quot;id&quot;:&quot;${story.id }&quot;}" --%>
<!-- 																rel="sync-get" -->
<!-- 																ajaxify="ajax_request_reject">반려요청</a> -->

															<a	href="/approval/${story.id }"
																class="h-icon">
																<i class="fa fa-eye fa-1g"></i>
																<span class="menu-text">결재뷰로 이동</span>
															</a>
														</li>
														<li>
															<a	href="/content/delete"
																role="dialog"
																data-style="messagebox-yesno"
																data-title="스토리 삭제"
																data-message="삭제되면 복구할 수 없습니다.<br>이 스토리를 삭제하시겠습니까?"
																ajaxify-dialog-yes="Story.onAjaxDeleteStory" 
																rel="sync-get"
																data-request-map="{&quot;id&quot;:&quot;${story.id }&quot;}"
																class="h-icon">
																<i class="fa fa-trash-o fa-1g"></i>
																<span class="menu-text">삭제</span>
															</a>
														</li>
													</c:when>
													<c:otherwise>
														<li>
														
															<a	href="/approval/${story.id }"
																class="h-icon">
																<i class="fa fa-eye fa-1g"></i>
																<span class="menu-text">결재뷰로 이동</span>
															</a>
														</li>
													</c:otherwise>
													</c:choose>
											</ul>
											</c:when>
											<c:otherwise>
												<ul	id="story-${story.id }-context-popup-menus"
													class="ui-toggle-flyout dropdown-menu pull-right"
													data-popup-trigger="story-${story.id }-context-trigger"
													data-popup-group="global"													
													data-fn-show="ContextMenuPopupToggler.onMenuShow"
													data-fn-hide="ContextMenuPopupToggler.onMenuHide">
													<c:if test="${authUserIsSuperAdmin || ( isAuthenticated==true && ( empty smallGroup || not empty smallGroupUser ) && ( story.postUserId==authUserId || story.userId==authUserId ) )}">
														<li>
															<a	href="/story/${story.id }/modify"
																rel="sync-get"
																ajaxify="Story.onAjaxModify"
																data-request-map="{}"
																data-story-id="${story.id }"
															  	class="h-icon">
															  	<i class="fa fa-pencil fa-1g"></i>
															   	<span class="menu-text">수정하기</span>
															</a>
														</li>
														<li>
															<a	href="/story/delete"
																role="dialog"
																data-style="messagebox-yesno"
																data-title="스토리 삭제"
																data-message="삭제되면 복구할 수 없습니다.<br>이 스토리를 삭제하시겠습니까?"
																ajaxify-dialog-yes="Story.onAjaxDeleteStory" 
																rel="sync-get"
																data-request-map="{&quot;id&quot;:&quot;${story.id }&quot;}"
																class="h-icon">
																<i class="fa fa-trash-o fa-1g"></i>
																<span class="menu-text">삭제</span>
															</a>
														</li>
													</c:if>
													<c:if test="${not story.systemMessage}">
													<li>
														<a	class="h-icon"
															role="dialog"
															aria-control="dialog-content-permissions"
															data-title="스토리 공개범위"
															data-close-fn="DialogContentPermissions.onClose"
															ajaxify-dialog-init="DialogContentPermissions.onAjaxContentPermissions"
															href="/content/permissions"
															rel="async-get"
															data-request-map='{"contentId":${story.id },"permId":null,"top":true,"size":20}'>
															<i class="fa fa-lock fa-1g"></i>
															<span class="menu-text">공개범위 보기</span>
														</a>
													</li>
													</c:if>
													<li>
														<a	href="/story/${story.id }"
															class="h-icon">
															<i class="fa fa-eye fa-1g"></i>
															<span class="menu-text">스토리뷰</span>
														</a>
													</li>
													<li>
														<a	class="h-icon"
															href="#"
															role="dialog"
															aria-control="dialog-bookmark"
															data-title="스크랩"
															data-custom-nm="등록"
															data-custom-fn="DialogBookmark.onRegisterClicked"
															data-cid="${story.id }">
															<i class="fa fa-bookmark fa-1g"></i>
															<span class="menu-text">스크랩</span>
														</a>
													</li>
												</ul>
											</c:otherwise>
											</c:choose>
										</div>
									</div>
								</div>
								<c:choose>
									<%-- 전자결재일 때 승인자, 회람자 등의 목록 보여지는 부분 --%>
									<c:when test="${story.type == 8 }">
										
										<div class="timeline-item item-body approval-container">
											
												
											
											<div class="approval-container-inner">
												<h2 class="z1">
													${story.title }<span class="approval-container-inner-date"><fmt:formatDate value='${story.createDate }' pattern="YYYY-MM-dd" /></span>
												</h2>
												<table class="z1 table approval-container-inner-table">
													<tr>
													<c:set var="storyApprobatorLength" value="${fn:length(story.approbators) }" />
													<c:forEach begin="0" end="${storyApprobatorLength <= 4 ? 3 : 7 }" var="index" varStatus="status">
														<c:if test="${index == 4 }">
															</tr><tr>
														</c:if>
														<td class="approval-container-inner-td">
															<div class="approval-container-inner-top">
																<c:if test="${ index < storyApprobatorLength}">
																<a href="${sunny.path }/user/${story.approbators[index].smallGroup.onlyMineUser.id}">
																	${story.approbators[index].smallGroup.onlyMineUser.name }
																</a>
																</c:if>
															</div>
															<div class="approval-container-inner-middle">
																	<div>
																	<c:if test="${ index < storyApprobatorLength}">
																		<c:choose>
																			<c:when test="${ story.approbators[index].status == 3 }">
																				<span class="label-red-b">
																					<i class="fa fa-times"></i>
																					반려
																				</span>
																			</c:when>
																			<c:when test="${ story.approbators[index].status == 2 }">
																				<span class="label-blue-b">
																					<i class="fa fa-circle-o"></i>
																					승인함
																				</span>	
																			</c:when>
																			<c:when test="${ story.approbators[index].status == 0 }">
																				<span class="label-yellow-b">
																					<i class="fa fa-ellipsis-h"></i>
																					보기 전
																				</span>	
																			</c:when>
																			<c:when test="${ story.approval.status == 1 && story.approbators[index].status == 1 }">
																			<span class="label-yellow-b">
																				<i class="fa fa-pause"></i>
																				중지
																			</span>
																		</c:when>
																			<c:when test="${ story.approbators[index].status == 1 }">
																				<span class="label-green-b">
																					<i class="fa fa-spinner "></i>
																					진행중
																				</span>
																			</c:when>
																		</c:choose>
																	</c:if>
																	</div>
															</div>
															<div class="approval-container-inner-bottom">
																<c:choose>
																<c:when test="${ index < storyApprobatorLength && not empty story.approbators[index].completeDate}">
																	<fmt:formatDate value='${story.approbators[index].completeDate}' pattern="YYYY-MM-dd" />
																</c:when>
																<c:otherwise>
																	/&nbsp;&nbsp;&nbsp;&nbsp;/
																</c:otherwise>
																</c:choose>
															</div>
														</td>
													</c:forEach>
													</tr>
												</table>
												<c:if test="${story.approval.cooperationCount > 0 }">
												<div class="z1 approval-table-assist-wrap">
													<h4>협조</h4>
													<table class="z1 table approval-table-assist">
													<tr>
													<c:forEach begin="0" end="${story.approval.cooperationCount <= 4 ? 3 : 7 }" var="index" varStatus="status">
														<c:if test="${index == 4 }">
															</tr><tr>
														</c:if>
														<td class="approval-container-inner-td">
															<div class="approval-container-inner-top">
																<c:if test="${ index < story.approval.cooperationCount}">
																<a href="${sunny.path }/user/${story.cooperations[index].smallGroup.onlyMineUser.id}">
																	${story.cooperations[index].smallGroup.onlyMineUser.name }
																</a>
																</c:if>
															</div>
															<div class="approval-container-inner-middle">
																	<div>
																	<c:if test="${ index < story.approval.cooperationCount}">
																		<c:choose>
																			<c:when test="${ story.cooperations[index].status == 3 }">
																				<span class="label-red-b">
																					<i class="fa fa-times"></i>
																					반려
																				</span>
																			</c:when>
																			<c:when test="${ story.cooperations[index].status == 2 }">
																				<span class="label-khaki-b">
																					<i class="fa fa-circle-o"></i>
																					협조함
																				</span>	
																			</c:when>
																			<c:when test="${ story.cooperations[index].status == 0 }">
																				<span class="label-yellow-b">
																					<i class="fa fa-ellipsis-h"></i>
																					보기 전
																				</span>	
																			</c:when>
																			<c:when test="${ story.approval.status == 1 && story.cooperations[index].status == 1 }">
																			<span class="label-yellow-b">
																				<i class="fa fa-pause"></i>
																				중지
																			</span>
																		</c:when>
																			<c:when test="${ story.cooperations[index].status == 1 }">
																				<span class="label-green-b">
																					<i class="fa fa-spinner "></i>
																					진행중
																				</span>
																			</c:when>
																		</c:choose>
																	</c:if>
																	</div>
															</div>
															<div class="approval-container-inner-bottom">
																<c:choose>
																<c:when test="${ index < story.approval.cooperationCount && not empty story.cooperations[index].completeDate}">
																	<fmt:formatDate value='${story.cooperations[index].completeDate}' pattern="YYYY/MM/dd" />
																</c:when>
																<c:otherwise>
																	/&nbsp;&nbsp;&nbsp;&nbsp;/
																</c:otherwise>
																</c:choose>
															</div>
														</td>
													</c:forEach>
													</tr>
												</table>
												</div>
												</c:if>
												<c:if test="${story.approval.receiverCount > 0 || story.approval.circulationCount > 0}">
												<div class="pop-approval-users-wrap">
													<a href="#" class="pop-approval-users" data-map='{"id":"${story.approval.id }", "types":["2", "3"]}'><i class="fa fa-user"></i>&nbsp;수신(${story.approval.receiverOkCount } / ${story.approval.receiverCount }) 및 회람하는 유저(${story.approval.circulationCount }명) 보기</a>
												</div>
												</c:if>
												<div class="z1 approval-request-wrap">
													<a href="${sunny.path }/approval/${story.id}" class="approval-learnmore">
														<c:choose>
															<c:when test="${story.smallGroupApproval.type == 0 }">
																<span class="label-white-b request-label">결재 요청</span>
															</c:when>
															<c:when test="${story.smallGroupApproval.type == 1 }">
																<span class="label-white-b request-label">협조 요청</span>
															</c:when>
															<c:when test="${story.smallGroupApproval.type == 2 }">
																<span class="label-white-b request-label">수신</span>
															</c:when>
															<c:when test="${story.smallGroupApproval.type == 3 }">
															</c:when>
														</c:choose>
														<i class="fa fa-caret-right"></i><span class="approval-learnmore-2">상세보기</span>
													</a>	
												</div>
											</div>
											<div style="text-align:right; padding: 5px 5px 0 0;">
											</div>
										</div>
									<%-- End 전자결재일 때 승인자, 회람자 등의 목록 보여지는 부분 --%>
									</c:when>
									<c:otherwise>
										<div id="editable-composer-wrap-${story.id }" class="timeline-item item-body item-editor"></div>
										<div id="editable-contents-${story.id }">
											<div class="timeline-item item-body item-message">								
												<h5 class="rw-ui-stream-message">
													<span class="rw-message-body">
														<c:choose>
															<c:when test="${not empty story.taggedTextNext }">
																<div class="rw-message-body-wrap" id="message-exposed-${story.id }">
																	<span class="user-content">
																		${story.taggedTextPrev}
																		<span class="text-exposed-hide"><br>...</span>
																		<span class="text-exposed-show">
																			${story.taggedTextNext}
																			<br>
																			<br>
																		</span>
																		<span class="text-exposed-hide">
																			<span class="text-exposed-link">
																				<a href="#" onclick="css_add_class($(&quot;#message-exposed-${story.id }&quot;), &quot;text-exposed&quot;); return false;">더보기 </a>
																			</span>
																		</span>
																	</span>
																</div>
															</c:when>
															<c:otherwise>
																<span class="rw-message-body-wrap" id="message-exposed-${story.id }">
																	<span class="user-content">
																		${story.taggedTextPrev}
																		${story.taggedTextNext}
																	</span>
																</span>
															</c:otherwise>
														</c:choose>
													</span>
												</h5>
											</div>
											<c:set value="${fn:length(story.mediaes) }" var="mediaCount"/>
											<c:if test="${story.type == 1 && mediaCount > 0 }">
												<div class="timeline-item item-body attachments-container">
													<c:choose>
														<c:when test="${mediaCount == 1 }">
															<c:choose>
																<c:when test="${story.mediaes[0].mediaType != 2}">
																	<c:set value="${story.mediaes[0] }" var="media"/>
																	<div class="ui-stream-attachments">
																		<div class="z1 attachments-redesign">
																			<div class="file-redesign-aspect">
																				<div class="file-wrap">
																					<a class="download-media file-type-image" href="/download/${media.id}/${media.fileName }" >
																						<span class="file-type-image">
																							<img src="/assets/sunny/2.0/img/file-icon.png">
																						</span>
																					</a>
																					<div class="file-desc">
																						<div class="file-desc-item">
																							<a class="download-media" href="/download/${media.id}/${media.fileName }">
																								${media.fileName }
																							</a>
																						</div>
																						<div class="file-desc-item _l">
																							${media.formatSize }
																						</div>																			
																					</div>
																				</div>
																			</div>
																		</div>
																	</div>									
																</c:when>
																<c:when test="${story.mediaes[0].mediaType == 2 }">
																	<c:set value="${story.mediaes[0] }" var="photo"/>
																	<c:set value="${photo.height*350/photo.width }" var="photoFitHeight"/>
																	<div class="ui-stream-attachments _photo_1">
																		<div class="z1 attachments-redesign">
																			<a	onclick="FileViewer&amp;&amp;FileViewer.load(this); return false;"
																				href="/story/${story.id }?pid=${photo.id }&cnt=1&idx=0&mode=theater" 
																				class="ui-photo-thumb photo-redesign-aspect"
																				rel="sync-get"
																				data-request-map="{}">
																				<img src="${photo.hugePath }" 
																					 class="img ${photo.width >= photo.height && photo.width > 489 ? '_w489' : '' } ${photo.height > photo.width && photoFitHeight > 320 ? '_h489' :''}"
																					 ${ photo.height > photo.width && photoFitHeight > 320 ? 'style=\"top:'.concat( -1 * ( photoFitHeight-320 ) / 2 ).concat('px\"') :'' }>
																			</a>
																		</div>
																	</div>		
																</c:when>
															</c:choose>
														</c:when>
														
														<c:when test="${mediaCount == 2 || mediaCount == 4 }">
															<div class="ui-stream-attachments _swipe_list swipe" data-count-item="${mediaCount }" data-index="0">
																<div class="z1 attachments-redesign swipe-wrap">
																	<c:forEach begin="1" end="${mediaCount }" step="1" var="index">
																		<c:choose>
																			<c:when test="${story.mediaes[index-1].mediaType != 2}">
																				<c:set value="${story.mediaes[index-1] }" var="media"/>
																				<c:set value="${ index >=3 ? \"\" : \"mb6\" }" var="classNameMarginBottom"/>
																				<div class="file-redesign-aspect _w245">
																					<div class="file-wrap mr7 ${classNameMarginBottom }">
																						<a class="download-media file-type-image" href="/download/${media.id}/${media.fileName }">
																							<span class="file-type-image">
																								<img src="/assets/sunny/2.0/img/file-icon.png">
																							</span>
																						</a>	
																						<div class="file-desc">
																							<div class="file-desc-item">
																								<a class="download-media" href="/download/${media.id}/${story.mediaes[index-1].fileName }">
																									${media.fileName }
																								</a>
																							</div>
																							<div class="file-desc-item _l">
																									${media.formatSize }
																							</div>																			
																						</div>
																					</div>
																				</div>
																			</c:when>
																			<c:when test="${story.mediaes[index-1].mediaType == 2}">
																				<c:set value="${story.mediaes[index-1] }" var="photo"/>
																				<c:set value="${photo.hugePath }" var="imageSrc"/>
																				<c:set value="260" var="imageDimension"/>
																				<c:set value="${ index >= 3 ? \"\" : \"mb6\" }" var="classNameMarginBottom"/>
																				<c:choose>
																					<c:when test="${photo.width >= photo.height }">
																						<c:set value="_h_${imageDimension }" var="dimensionClass"/>
																						<c:set value="" var="classNameScaledImageFitWidth"/>
																						<c:set value="left:-${ ( ( (imageDimension * photo.width) / photo.height) - imageDimension ) / 2}px" var="stylePosition"/>
																					</c:when>
																					<c:otherwise>
																						<c:set value="_w_${imageDimension }" var="dimensionClass"/>
																						<c:set value="scaled-image-fit-width" var="classNameScaledImageFitWidth"/>
																						<c:set value="top:-${ ( ( (imageDimension * photo.height) / photo.width) - imageDimension ) / 2}px" var="stylePosition"/>
																					</c:otherwise>
																				</c:choose>
																				<div class="ui-scaled-thumb photo-redesign-large photo-redesign-link" data-ft="{}">
																					<a  href="/story/${story.id }?pid=${photo.id }&cnt=${mediaCount }&idx=${index-1 }&mode=theater" 
																						rel="sync-get"
																						data-request-map="{}"
																						onclick="FileViewer&amp;&amp;FileViewer.load(this); return false;">
																						<div class="mr7 ${classNameMarginBottom } ui-scaled-image-container photo-wrap">
																							<img class="img ${classNameScaledImageFitWidth } ${dimensionClass }" src="${imageSrc }" alt="" style="${stylePosition }">
																						</div>
																					</a>
																				</div>
																			</c:when>
																		</c:choose>											
																	</c:forEach>
																</div>
															</div>												
														</c:when>
														<c:when test="${mediaCount == 3 || mediaCount == 6 }">
															<div class="ui-stream-attachments _swipe_list swipe" data-count-item="${mediaCount }" data-index="0">
																<div class="z1 attachments-redesign swipe-wrap">
																	<c:forEach begin="1" end="${mediaCount }" step="1" var="index">
																		<c:choose>
																		
																			<c:when test="${story.mediaes[index-1].mediaType != 2}">
																				<c:set value="${story.mediaes[index-1] }" var="media"/> 
																				<c:set value="${ index >=4 ? \"\" : \"mb6\" }" var="classNameMarginBottom"/>	
																				<div class="file-redesign-aspect _w160">
																					<div class="file-wrap mr7 ${classNameMarginBottom }">
																						<a class="download-media file-type-image" href="/download/${media.id}/${media.fileName }">
																							<span class="file-type-image">
																								<img src="/assets/sunny/2.0/img/file-icon.png">
																							</span>
																						</a>
																						<div class="file-desc">
																							<div class="file-desc-item">
																								<a class="download-media" href="/download/${media.id}/${story.mediaes[index-1].fileName }">
																									${media.fileName }
																								</a>
																							</div>
																							<div class="file-desc-item _l">
																								${media.formatSize }																			
																							</div>																			
																						</div>
																					</div>
																				</div>
																			</c:when>
																		
																			<c:when test="${story.mediaes[index-1].mediaType == 2}">
																				<c:set value="${story.mediaes[index-1] }" var="photo"/>
																				<c:set value="${photo.hugePath }" var="imageSrc"/>
																				<c:set value="160" var="imageDimension"/>
																				<c:set value="${ index >=4 ? \"\" : \"mb6\" }" var="classNameMarginBottom"/>
																				<c:choose>
																					<c:when test="${photo.width >= photo.height }">
																						<c:set value="_h_${imageDimension }" var="dimensionClass"/>
																						<c:set value="" var="classNameScaledImageFitWidth"/>
																						<c:set value="left:-${ ( ( (imageDimension * photo.width) / photo.height) - imageDimension ) / 2}px" var="stylePosition"/>
																					</c:when>
																					<c:otherwise>
																						<c:set value="_w_${imageDimension }" var="dimensionClass"/>
																						<c:set value="scaled-image-fit-width" var="classNameScaledImageFitWidth"/>
																						<c:set value="top:-${ ( ( (imageDimension * photo.height) / photo.width) - imageDimension ) / 2}px" var="stylePosition"/>
																					</c:otherwise>
																				</c:choose>
																				<div class="ui-scaled-thumb photo-redesign-small photo-redesign-link" data-ft="{}">
																					<a  href="/story/${story.id }?pid=${photo.id }&cnt=${mediaCount }&idx=${index-1 }&mode=theater" 
																						rel="sync-get"
																						data-request-map="{}"
																						onclick="FileViewer&amp;&amp;FileViewer.load(this); return false;">
																						<div class="mr7 ${classNameMarginBottom } ui-scaled-image-container photo-wrap">
																							<img class="img ${classNameScaledImageFitWidth } ${dimensionClass }" src="${imageSrc }" alt="" style="${stylePosition }">
																						</div>
																					</a>
																				</div>
																			</c:when>
																							
																		</c:choose>
																					
																	</c:forEach>
																</div>
															</div>												
														</c:when>
														<c:when test="${ mediaCount == 5 }">
															<div class="ui-stream-attachments _swipe_list swipe" data-count-item="${mediaCount }" data-index="0">
																<div class="z1 attachments-redesign swipe-wrap">
																
																	<c:forEach begin="1" end="${mediaCount }" step="1" var="index">
																		<c:choose>
																			<c:when test="${story.mediaes[index-1].mediaType != 2}">
																				<c:set value="${story.mediaes[index-1] }" var="media"/>
																				<c:set value="${ index <= 2 ? 245 : 160 }" var="mediaDimension"/>
																				<c:set value="${ index <= 2 ? \"mb6\" : \"\" }" var="classNameMarginBottom"/>
																				<div class="file-redesign-aspect _w${mediaDimension }">
																					<div class="file-wrap mr7 ${classNameMarginBottom }">
																						<a class="download-media file-type-image" href="/download/${media.id}/${media.fileName }">
																							<span class="file-type-image">
																								<img src="/assets/sunny/2.0/img/file-icon.png">
																							</span>
																						</a>	
																						<div class="file-desc">
																							<div class="file-desc-item">
																								<a class="download-media" href="/download/${media.id}/${story.mediaes[index-1].fileName }">
																									${media.fileName }
																								</a>
																							</div>
																							<div class="file-desc-item _l">
																									${media.formatSize }
																							</div>																			
																						</div>
																					</div>
																				</div>
																			</c:when>
																			<c:when test="${story.mediaes[index-1].mediaType == 2}">
																			
																				<c:set value="${story.mediaes[index-1] }" var="photo"/>
																				<c:set value="${photo.hugePath }" var="imageSrc"/>
																				
																				<c:set value="${ index <= 2 ? 260 : 160 }" var="imageDimension"/>
																				<c:set value="${ index <= 2 ? \"photo-redesign-large\" : \"photo-redesign-small\" }" var="classNamePhotoRedesign"/>
																				<c:set value="${ index <= 2 ? \"mb6\" : \"\" }" var="classNameMarginBottom"/>
																				
																				<c:choose>
																					<c:when test="${photo.width >= photo.height }">
																						<c:set value="_h_${imageDimension }" var="dimensionClass"/>
																						<c:set value="" var="classNameScaledImageFitWidth"/>
																						<c:set value="left:-${ ( ( (imageDimension * photo.width) / photo.height) - imageDimension ) / 2}px" var="stylePosition"/>
																					</c:when>
																					<c:otherwise>
																						<c:set value="_w_${imageDimension }" var="dimensionClass"/>
																						<c:set value="scaled-image-fit-width" var="classNameScaledImageFitWidth"/>
																						<c:set value="top:-${ ( ( (imageDimension * photo.height) / photo.width) - imageDimension ) / 2}px" var="stylePosition"/>
																					</c:otherwise>
																				</c:choose>
																				<div class="ui-scaled-thumb ${classNamePhotoRedesign } photo-redesign-link" data-ft="{}">
																					<a  href="/story/${story.id }?pid=${photo.id }&cnt=${mediaCount }&idx=${index-1 }&mode=theater" 
																						rel="sync-get"
																						data-request-map="{}"
																						onclick="FileViewer&amp;&amp;FileViewer.load(this); return false;">
																						<div class="mr7 ${classNameMarginBottom } ui-scaled-image-container photo-wrap">
																							<img class="img ${classNameScaledImageFitWidth } ${dimensionClass }" src="${imageSrc }" alt="" style="${stylePosition }">
																						</div>
																					</a>
																				</div>
																			</c:when>
																		</c:choose>											
																	</c:forEach>
																</div>
															</div>												
														</c:when>											
														<c:when test="${ mediaCount == 7 }">
															<div class="ui-stream-attachments _swipe_list swipe" data-count-item="${mediaCount }" data-index="0">
																<div class="z1 attachments-redesign swipe-wrap">
																
																	<c:forEach begin="1" end="${mediaCount }" step="1" var="index">
																		<c:choose>
																			<c:when test="${story.mediaes[index-1].mediaType != 2}">
																				<c:set value="${story.mediaes[index-1] }" var="media"/>
																				<c:set value="${ ( 3 <= index && index <= 5 ) ? 160 : 245 }" var="mediaDimension"/>
																				<c:set value="${ index <= 6 ? \"mb6\" : \"\" }" var="classNameMarginBottom"/>
																				<div class="file-redesign-aspect _w${mediaDimension }">
																					<div class="file-wrap mr7 ${classNameMarginBottom }">
																						<a class="download-media file-type-image" href="/download/${media.id}/${media.fileName }">
																							<span class="file-type-image">
																								<img src="/assets/sunny/2.0/img/file-icon.png">
																							</span>
																						</a>	
																						<div class="file-desc">
																							<div class="file-desc-item">
																								<a class="download-media" href="/download/${media.id}/${story.mediaes[index-1].fileName }">
																									${media.fileName }
																								</a>
																							</div>
																							<div class="file-desc-item _l">
																									${media.formatSize }
																							</div>																			
																						</div>
																					</div>
																				</div>
																			</c:when>
																			<c:when test="${story.mediaes[index-1].mediaType == 2}">
																			
																				<c:set value="${story.mediaes[index-1] }" var="photo"/>
																				<c:set value="${photo.hugePath }" var="imageSrc"/>
																				
																				<c:set value="${ ( 3 <= index && index <= 5 ) ? 160 : 260 }" var="imageDimension"/>
																				<c:set value="${ ( 3 <= index && index <= 5 ) ? \"photo-redesign-small\" : \"photo-redesign-large\" }" var="classNamePhotoRedesign"/>
																				<c:set value="${ index <= 6 ? \"mb6\" : \"\" }" var="classNameMarginBottom"/>
																				
																				<c:choose>
																					<c:when test="${photo.width >= photo.height }">
																						<c:set value="_h_${imageDimension }" var="dimensionClass"/>
																						<c:set value="" var="classNameScaledImageFitWidth"/>
																						<c:set value="left:-${ ( ( (imageDimension * photo.width) / photo.height) - imageDimension ) / 2}px" var="stylePosition"/>
																					</c:when>
																					<c:otherwise>
																						<c:set value="_w_${imageDimension }" var="dimensionClass"/>
																						<c:set value="scaled-image-fit-width" var="classNameScaledImageFitWidth"/>
																						<c:set value="top:-${ ( ( (imageDimension * photo.height) / photo.width) - imageDimension ) / 2}px" var="stylePosition"/>
																					</c:otherwise>
																				</c:choose>
																				<div class="ui-scaled-thumb ${classNamePhotoRedesign } photo-redesign-link" data-ft="{}">
																					<a  href="/story/${story.id }?pid=${photo.id }&cnt=${mediaCount }&idx=${index-1 }&mode=theater" 
																						rel="sync-get"
																						data-request-map="{}"
																						onclick="FileViewer&amp;&amp;FileViewer.load(this); return false;">
																						<div class="mr7 ${classNameMarginBottom } ui-scaled-image-container photo-wrap">
																							<img class="img ${classNameScaledImageFitWidth } ${dimensionClass }" src="${imageSrc }" alt="" style="${stylePosition }">
																						</div>
																					</a>
																				</div>
																			</c:when>
																		</c:choose>											
																	</c:forEach>
																</div>
															</div>												
														</c:when>
														<c:when test="${ mediaCount == 8 }">
															<div class="ui-stream-attachments _swipe_list swipe" data-count-item="${mediaCount }" data-index="0">
																<div class="z1 attachments-redesign swipe-wrap">
																
																	<c:forEach begin="1" end="${mediaCount }" step="1" var="index">
																		<c:choose>
																			<c:when test="${story.mediaes[index-1].mediaType != 2}">
																				<c:set value="${story.mediaes[index-1] }" var="media"/>
																				<c:set value="${ ( index == 4 || index == 5 ) ? 245 : 160 }" var="mediaDimension"/>
																				<c:set value="${ index <= 6 ? \"mb6\" : \"\" }" var="classNameMarginBottom"/>
																				<div class="file-redesign-aspect _w${mediaDimension }">
																					<div class="file-wrap mr7 ${classNameMarginBottom }">
																						<a class="download-media file-type-image" href="/download/${media.id}/${media.fileName }">
																							<span class="file-type-image">
																								<img src="/assets/sunny/2.0/img/file-icon.png">
																							</span>
																						</a>	
																						<div class="file-desc">
																							<div class="file-desc-item">
																								<a class="download-media" href="/download/${media.id}/${story.mediaes[index-1].fileName }">
																									${media.fileName }
																								</a>
																							</div>
																							<div class="file-desc-item _l">
																									${media.formatSize }
																							</div>																			
																						</div>
																					</div>
																				</div>
																			</c:when>
																			<c:when test="${story.mediaes[index-1].mediaType == 2}">
																			
																				<c:set value="${story.mediaes[index-1] }" var="photo"/>
																				<c:set value="${photo.hugePath }" var="imageSrc"/>
																				
																				<c:set value="${ ( index == 4 || index == 5 ) ? 260 : 160 }" var="imageDimension"/>
																				<c:set value="${ ( index == 4 || index == 5 ) ? \"photo-redesign-large\" : \"photo-redesign-small\" }" var="classNamePhotoRedesign"/>
																				<c:set value="${ index <= 6 ? \"mb6\" : \"\" }" var="classNameMarginBottom"/>
																				
																				<c:choose>
																					<c:when test="${photo.width >= photo.height }">
																						<c:set value="_h_${imageDimension }" var="dimensionClass"/>
																						<c:set value="" var="classNameScaledImageFitWidth"/>
																						<c:set value="left:-${ ( ( (imageDimension * photo.width) / photo.height) - imageDimension ) / 2}px" var="stylePosition"/>
																					</c:when>
																					<c:otherwise>
																						<c:set value="_w_${imageDimension }" var="dimensionClass"/>
																						<c:set value="scaled-image-fit-width" var="classNameScaledImageFitWidth"/>
																						<c:set value="top:-${ ( ( (imageDimension * photo.height) / photo.width) - imageDimension ) / 2}px" var="stylePosition"/>
																					</c:otherwise>
																				</c:choose>
																				<div class="ui-scaled-thumb ${classNamePhotoRedesign } photo-redesign-link" data-ft="{}">
																					<a  href="/story/${story.id }?pid=${photo.id }&cnt=${mediaCount }&idx=${index-1 }&mode=theater" 
																						rel="sync-get"
																						data-request-map="{}"
																						onclick="FileViewer&amp;&amp;FileViewer.load(this); return false;">
																						<div class="mr7 ${classNameMarginBottom } ui-scaled-image-container photo-wrap">
																							<img class="img ${classNameScaledImageFitWidth } ${dimensionClass }" src="${imageSrc }" alt="" style="${stylePosition }">
																						</div>
																					</a>
																				</div>
																			</c:when>
																		</c:choose>											
																	</c:forEach>
																</div>
															</div>												
														</c:when>
														<c:when test="${ mediaCount == 10 }">
															<div class="ui-stream-attachments _swipe_list swipe" data-count-item="${mediaCount }" data-index="0">
																<div class="z1 attachments-redesign swipe-wrap">
																	<c:forEach begin="1" end="${mediaCount }" step="1" var="index">
																		<c:choose>
																			<c:when test="${story.mediaes[index-1].mediaType != 2}">
																				<c:set value="${story.mediaes[index-1] }" var="media"/>
																				<c:set value="${ ( 4 <= index && index <= 7 ) ? 245 : 160 }" var="mediaDimension"/>
																				<c:set value="${ index <= 8 ? \"mb6\" : \"\" }" var="classNameMarginBottom"/>
																				<div class="file-redesign-aspect _w${mediaDimension }">
																					<div class="file-wrap mr7 ${classNameMarginBottom }">
																						<a class="download-media file-type-image" href="/download/${media.id}/${media.fileName }">
																							<span class="file-type-image">
																								<img src="/assets/sunny/2.0/img/file-icon.png">
																							</span>
																						</a>	
																						<div class="file-desc">
																							<div class="file-desc-item">
																								<a class="download-media" href="/download/${media.id}/${story.mediaes[index-1].fileName }">
																									${media.fileName }
																								</a>
																							</div>
																							<div class="file-desc-item _l">
																									${media.formatSize }
																							</div>																			
																						</div>
																					</div>
																				</div>
																			</c:when>
																			<c:when test="${story.mediaes[index-1].mediaType == 2}">
																			
																				<c:set value="${story.mediaes[index-1] }" var="photo"/>
																				<c:set value="${photo.hugePath }" var="imageSrc"/>
																				
																				<c:set value="${ ( 4 <= index && index <= 7 ) ? 260 : 160 }" var="imageDimension"/>
																				<c:set value="${ ( 4 <= index && index <= 7 ) ? \"photo-redesign-large\" : \"photo-redesign-small\" }" var="classNamePhotoRedesign"/>
																				<c:set value="${ index <= 8 ? \"mb6\" : \"\" }" var="classNameMarginBottom"/>
																				
																				<c:choose>
																					<c:when test="${photo.width >= photo.height }">
																						<c:set value="_h_${imageDimension }" var="dimensionClass"/>
																						<c:set value="" var="classNameScaledImageFitWidth"/>
																						<c:set value="left:-${ ( ( (imageDimension * photo.width) / photo.height) - imageDimension ) / 2}px" var="stylePosition"/>
																					</c:when>
																					<c:otherwise>
																						<c:set value="_w_${imageDimension }" var="dimensionClass"/>
																						<c:set value="scaled-image-fit-width" var="classNameScaledImageFitWidth"/>
																						<c:set value="top:-${ ( ( (imageDimension * photo.height) / photo.width) - imageDimension ) / 2}px" var="stylePosition"/>
																					</c:otherwise>
																				</c:choose>
																				<div class="ui-scaled-thumb ${classNamePhotoRedesign } photo-redesign-link" data-ft="{}">
																					<a  href="/story/${story.id }?pid=${photo.id }&cnt=${mediaCount }&idx=${index-1 }&mode=theater" 
																						rel="sync-get"
																						data-request-map="{}"
																						onclick="FileViewer&amp;&amp;FileViewer.load(this); return false;">
																						<div class="mr7 ${classNameMarginBottom } ui-scaled-image-container photo-wrap">
																							<img class="img ${classNameScaledImageFitWidth } ${dimensionClass }" src="${imageSrc }" alt="" style="${stylePosition }">
																						</div>
																					</a>
																				</div>
																			</c:when>
																		</c:choose>											
																	</c:forEach>
																</div>
															</div>												
														</c:when>
														<c:when test="${ mediaCount == 11 }">
															<div class="ui-stream-attachments _swipe_list swipe" data-count-item="${mediaCount }" data-index="0">
																<div class="z1 attachments-redesign swipe-wrap">
																	<c:forEach begin="1" end="${mediaCount }" step="1" var="index">
																		<c:choose>
																			<c:when test="${story.mediaes[index-1].mediaType != 2}">
																				<c:set value="${story.mediaes[index-1] }" var="media"/>
																				<c:set value="${ ( index == 4 || index == 5 ) ? 245 : 160 }" var="mediaDimension"/>
																				<c:set value="${ index <= 8 ? \"mb6\" : \"\" }" var="classNameMarginBottom"/>
																				<div class="file-redesign-aspect _w${mediaDimension }">
																					<div class="file-wrap mr7 ${classNameMarginBottom }">
																						<a class="download-media file-type-image" href="/download/${media.id}/${media.fileName }">
																							<span class="file-type-image">
																								<img src="/assets/sunny/2.0/img/file-icon.png">
																							</span>
																						</a>	
																						<div class="file-desc">
																							<div class="file-desc-item">
																								<a class="download-media" href="/download/${media.id}/${story.mediaes[index-1].fileName }">
																									${media.fileName }
																								</a>
																							</div>
																							<div class="file-desc-item _l">
																									${media.formatSize }
																							</div>																			
																						</div>
																					</div>
																				</div>
																			</c:when>
																			<c:when test="${story.mediaes[index-1].mediaType == 2}">
																			
																				<c:set value="${story.mediaes[index-1] }" var="photo"/>
																				<c:set value="${photo.hugePath }" var="imageSrc"/>
																				
																				<c:set value="${ ( index == 4 || index == 5 ) ? 260 : 160 }" var="imageDimension"/>
																				<c:set value="${ ( index == 4 || index == 5 ) ? \"photo-redesign-large\" : \"photo-redesign-small\" }" var="classNamePhotoRedesign"/>
																				<c:set value="${ index <= 8 ? \"mb6\" : \"\" }" var="classNameMarginBottom"/>
																				
																				<c:choose>
																					<c:when test="${photo.width >= photo.height }">
																						<c:set value="_h_${imageDimension }" var="dimensionClass"/>
																						<c:set value="" var="classNameScaledImageFitWidth"/>
																						<c:set value="left:-${ ( ( (imageDimension * photo.width) / photo.height) - imageDimension ) / 2}px" var="stylePosition"/>
																					</c:when>
																					<c:otherwise>
																						<c:set value="_w_${imageDimension }" var="dimensionClass"/>
																						<c:set value="scaled-image-fit-width" var="classNameScaledImageFitWidth"/>
																						<c:set value="top:-${ ( ( (imageDimension * photo.height) / photo.width) - imageDimension ) / 2}px" var="stylePosition"/>
																					</c:otherwise>
																				</c:choose>
																				<div class="ui-scaled-thumb ${classNamePhotoRedesign } photo-redesign-link" data-ft="{}">
																					<a  href="/story/${story.id }?pid=${photo.id }&cnt=${mediaCount }&idx=${index-1 }&mode=theater" 
																						rel="sync-get"
																						data-request-map="{}"
																						onclick="FileViewer&amp;&amp;FileViewer.load(this); return false;">
																						<div class="mr7 ${classNameMarginBottom } ui-scaled-image-container photo-wrap">
																							<img class="img ${classNameScaledImageFitWidth } ${dimensionClass }" src="${imageSrc }" alt="" style="${stylePosition }">
																						</div>
																					</a>
																				</div>
																			</c:when>
																		</c:choose>											
																	</c:forEach>
																</div>
															</div>												
														</c:when>												
														<c:when test="${mediaCount == 9 || mediaCount >= 12 }">
															<c:set value="${ mediaCount > 12 ? 12 : mediaCount }"  var="mediaCount"/>
															<div class="ui-stream-attachments _swipe_list swipe" data-count-item="${mediaCount }" data-index="0">
																<div class="z1 attachments-redesign swipe-wrap">
																	<c:forEach begin="1" end="${mediaCount }" step="1" var="index">
																	
																		<c:choose>
																			<c:when test="${story.mediaes[index-1].mediaType != 2}">
																				<c:set value="${story.mediaes[index-1] }" var="media"/> 
																				<c:set value="${ index >= mediaCount - 2  ? \"\" : \"mb6\" }" var="classNameMarginBottom"/>	
																				<div class="file-redesign-aspect _w160">
																					<div class="file-wrap mr7 ${classNameMarginBottom }">
																						<a class="download-media file-type-image" href="/download/${media.id}/${media.fileName }">
																							<span class="file-type-image">
																								<img src="/assets/sunny/2.0/img/file-icon.png">
																							</span>
																						</a>
																						<div class="file-desc">
																							<div class="file-desc-item">
																								<a class="download-media" href="/download/${media.id}/${story.mediaes[index-1].fileName }">
																									${media.fileName }
																								</a>
																							</div>
																							<div class="file-desc-item _l">
																								${media.formatSize }																			
																							</div>																			
																						</div>
																					</div>
																				</div>
																			</c:when>
																			<c:when test="${story.mediaes[index-1].mediaType == 2}">
																				<c:set value="${story.mediaes[index-1] }" var="photo"/>
																				<c:set value="${photo.hugePath }" var="imageSrc"/>
																				<c:set value="160" var="imageDimension"/>
																				<c:set value="${ (index >= mediaCount - 2)  ? \"\" : \"mb6\" }" var="classNameMarginBottom"/>
																				<c:choose>
																					<c:when test="${photo.width >= photo.height }">
																						<c:set value="_h_${imageDimension }" var="dimensionClass"/>
																						<c:set value="" var="classNameScaledImageFitWidth"/>
																						<c:set value="left:-${ ( ( (imageDimension * photo.width) / photo.height) - imageDimension ) / 2}px" var="stylePosition"/>
																					</c:when>
																					<c:otherwise>
																						<c:set value="_w_${imageDimension }" var="dimensionClass"/>
																						<c:set value="scaled-image-fit-width" var="classNameScaledImageFitWidth"/>
																						<c:set value="top:-${ ( ( (imageDimension * photo.height) / photo.width) - imageDimension ) / 2}px" var="stylePosition"/>
																					</c:otherwise>
																				</c:choose>
																				
																				<div class="ui-scaled-thumb photo-redesign-small photo-redesign-link" data-ft="{}">
																					<a  href="/story/${story.id }?pid=${photo.id }&cnt=${mediaCount }&idx=${index-1 }&mode=theater" 
																						rel="sync-get"
																						data-request-map="{}"
																						onclick="FileViewer&amp;&amp;FileViewer.load(this); return false;">
																						<div class="mr7 ${classNameMarginBottom } ui-scaled-image-container photo-wrap">
																							<img class="img ${classNameScaledImageFitWidth } ${dimensionClass }" src="${imageSrc }" alt="" style="${stylePosition }">
																						</div>
																					</a>
																				</div>
																			</c:when>
																		</c:choose>
																	</c:forEach>
																</div>
															</div>												
														</c:when>
													</c:choose>
												</div>
											</c:if>
										</div>
									</c:otherwise>
								</c:choose>
								<div class="timeline-item item-body item-actions">
									<span class="ui-action-links-bottom react-icons-${story.id }">
										<%-- 시스템 메시지가 아니고, 로그인 했으며, 혹시 그룹일 경우엔 그룹에 소속된 사용자인 경우만 가능. --%>
										<c:if test="${not story.systemMessage && isAuthenticated==true  && ( empty smallGroup || not empty smallGroupUser )}">
											<span class="story-react">
												
												<a	href="/feel/okay"
													role="radio"
													class="feel-radio-react-del-${story.id }"
													data-ft-class="feel-radio-react-del-${story.id }"
													ajaxify="Story.onAjaxFeel"
													aria-checked="${story.feeledId == 1 ? 'true':'false'}"
													aria-controls="feel-counter-${story.id }"
													rel="sync-get"
													data-ft="${story.id }"
													data-request-map="{&quot;contentId&quot;:&quot;${story.id }&quot;,&quot;feelId&quot;:&quot;1&quot;}">
													좋아요 <span class="react-icon <c:if test='${story.feeledId == 1}'>reacted</c:if>">취소</span></a>
											</span>
											<span class="story-stream-dott"> · </span>
											<a	class="h-icon story-stream-bookmark"
														href="#"
														role="dialog"
														aria-control="dialog-bookmark"
														data-title="스크랩"
														data-custom-nm="등록"
														data-custom-fn="DialogBookmark.onRegisterClicked"
														data-cid="${story.id }">
														<i class="story-stream-bookmark-icon"></i>
														<span class="menu-text story-stream-bookmark-text">스크랩</span>
													</a>
										</c:if>
									</span>
								</div>
				
								<c:set value="${(empty story.feelCount || story.feelCount < 0) ? 0:story.feelCount}" var="feelCount" />
				
								<c:if test="${ not story.systemMessage }">
								<div class="timeline-item item-body ui-comment-container ${ sunny.device.mobile ? 'container-mobile':'' }${ sunny.device.tablet ? 'container-mobile-tablet':'' }">
									<span class="head-line">&nbsp;</span>
										<div class="cmnt-row-wrap" ${feelCount == 0 ? "style='display:none'" : "" }>
											<div class="timeline-feedback-actions _b cearfix">
												<span> 
													<a	class="feel-counter-${story.id }"
														role="dialog"
														aria-control="dialog-feel-users"
														ajaxify-dialog-init="DialogFeelUsers.onAjaxFeelUsers"
														href="/feel/users"
														rel="async-get"
														data-request-map="{&quot;contentId&quot;:${story.id },&quot;feelId&quot;:null,&quot;top&quot;:true,&quot;size&quot;:20}"
														data-title="스토리 평가 사용자"
														data-close-fn="DialogFeelUsers.onClose"
														data-count="${feelCount }">
														<i class="fa fa-heart fa-1g"></i>
														<span class="count-text">${feelCount }명</span>
													</a><span>이 평가했습니다.</span>
												</span>
											</div>
										</div>							
				
										<div class="rw-comment-container">
											<c:if test="${story.replyCount > 3 }">
												<c:set value="10" var="requestReplyCount" />
												<c:set value="${story.replyCount-3 }" var="remainReplyCount" />
												<div class="cmnt-row-wrap">
													<div class="timeline-feedback-actions _b cearfix">
														<span> 
															<a	href="/reply/stream"
																rel="async-get"
																ajaxify="Reply.onReplyStream"
																ajaxify-before="Reply.onReplyStreamBefore"
																data-reply-count="${remainReplyCount }"
																data-request-map="{ &quot;top&quot;:true, &quot;contentId&quot;:${story.id },&quot;replyId&quot;:${story.replys[0].id },&quot;size&quot;:${requestReplyCount } }">
																<i class="fa fa-comment fa-1g"></i>
																<i class="fa fa-spin fa-1g fa-spinner" style="display:none"></i>
																<span>
																<c:choose>
																	<c:when test="${remainReplyCount > requestReplyCount }">지난 댓글 더 보기</c:when>
																	<c:otherwise>댓글 ${remainReplyCount }개 더 보기 </c:otherwise>
																</c:choose>
															</span>
															</a>
														</span>
													</div>
												</div>							
											</c:if>
											<ul class="uc-container">
												<c:forEach items="${story.replys }" var="reply">
														<li class="ui-react-row">
															<div class="z1">
																<div class="cmnt-profile-box">
																	<a	href="/user/${reply.user.id }"
																		data-hovercard="">
																		<img src="${reply.user.profilePic }" alt="" class="cmmnt-profile-pic"></a>
																</div>
																<div>
																	<div class="image-block-content">
																		<c:if test="${isAuthenticated==true && reply.user.id==authUserId }">
																			<div class="ui-button-opa-block">
																				<a	href="/reply/delete"
																					role="dialog"
																					data-style="messagebox-yesno"
																					data-title="댓글 삭제"
																					data-message="삭제되면 복구할 수 없습니다.<br>이 댓글을 삭제하시겠습니까?"
																					ajaxify-dialog-yes="Reply.onAjaxReplyDelete" 
																					rel="sync-get"
																					data-request-map="{&quot;id&quot;:&quot;${reply.id }&quot;}"
																					class="ui-button-opa-a ui-comment-close-button">
																					<i class="fa fa-times fa-1g"></i>
																				</a>
																			</div>	
																		</c:if>																	
																		<div>
																			<div class="ui-comment-content">
																				<a href="/user/${reply.user.id }"	data-hovercard="" class="ui-comment-user-name">${reply.user.name }</a>
																				<c:choose>
																					<c:when test="${not empty reply.taggedTextNext }">
																						<div id="reply-exposed-${reply.id }">
																							<div>${reply.taggedTextPrev}</div>
																							<span class="text-exposed-hide">...</span>
																							<span class="text-exposed-show">${reply.taggedTextNext}<br/><br/></span>
																							<span class="text-exposed-hide">
																								<span class="text-exposed-link">
																									<a href="#" onclick="css_add_class($(&quot;#reply-exposed-${reply.id }&quot;), &quot;text-exposed&quot;); return false;">더보기</a>
																								</span>
																							</span>
																						</div>
																					</c:when>
																					<c:otherwise>
																						<span>${reply.taggedTextPrev}</span>
																					</c:otherwise>
																				</c:choose>																	
																			</div>
																			<c:if test="${reply.mediaCount == 1 }">
																				<c:choose>
																					<c:when test="${reply.mediaType != 2 }">
																						<div class="ui-media-thumb">
																							<a class="download-media" href="/download/${reply.mediaId}/${reply.fileName }">
																								<span class="file">
																									<span>${reply.fileName }</span>
																								</span>
																							</a>	
																						</div>
																					</c:when>
																					<c:when test="${reply.mediaType == 2 }">
																						<c:set value="${reply.hugePath }" var="cmntPhotoPath"/>
																						<div class="ui-media-thumb">
																							<div>
																							<c:choose>
																								<c:when test="${reply.width < reply.height }">
																									<c:set value="${ reply.height > 210 ? 210 : reply.Height }" var="cmntPhotoHeight"/>
																									<c:set value="${ cmntPhotoHeight * reply.width / reply.height }" var="cmntPhotoWidth"/>
																									<div class="ui-scaled-image-container" style="width:${cmntPhotoWidth}px; height:${cmntPhotoHeight}px;">
																										<a class="reply-photo-thumb"
																										   data-src-o="${reply.originalPath }"
																										   href="#"
																										   onclick="Reply.openPhotoFullScreen( this ); return false;">
																										   <img class="img" 
																												src="${cmntPhotoPath }"
																												alt=""
																												width="${cmntPhotoWidth }" height="${cmntPhotoHeight }">
																										</a>	 
																									</div>
																								</c:when>
																								<c:otherwise>
																									<c:set value="${ reply.width > 220 ? 220 : reply.width }" var="cmntPhotoWidth"/>
																									<c:set value="${ cmntPhotoWidth * reply.height / reply.width }" var="cmntPhotoHeight"/>
																									<div class="_46-h" style="width:${cmntPhotoWidth}px; height:${cmntPhotoHeight}px;">
																										<a class="reply-photo-thumb"
																											data-src-o="${reply.originalPath }"
																											href="#"
																											onclick="Reply.openPhotoFullScreen( this ); return false;">
																											<img class="_46-i img" 
																												 src="${cmntPhotoPath }"
																											 	style="left:0px; top:0px;" alt="" width="${cmntPhotoWidth+1 }" height="${cmntPhotoHeight+1 }">
																										</a>	 
																									</div>
																								</c:otherwise>
																							</c:choose>
																							</div>
																						</div>
																								
																					</c:when>
																				</c:choose>
																			</c:if>
																			<div class="ui-comment-actions">
																				<span>
																					<a	href="#"
																						class="ui-link-subtle">
																						<abbr data-utime="${reply.createDate }" class="timestamp livetimestamp"  data-hover="tooltip" data-tooltip-alignh="left" aria-label=""></abbr>
																					</a>
																				</span>
																				<span> · </span>
																				<c:choose>
																					<c:when test="${isAuthenticated==true  && ( empty smallGroup || not empty smallGroupUser )}">
																						<span class="ui-reply-like-button ui-reply-like-button-${reply.id } ${reply.feeledId == 1 ? 'mt-1-like':'mt-0-like' }">
																							<a	role="checkbox"
																								aria-checked="${reply.feeledId == 1 ? 'true':'false' }"
																								class="ui-reply-like-icon"
																								href="/feel/okay"
																								data-control-class="ui-reply-like-button-${reply.id }"
																								rel="sync-get"
																								ajaxify="Reply.onAjaxFeelOkay"
																								data-request-map="{&quot;contentId&quot;:&quot;${reply.id}&quot;,&quot;feelId&quot;:&quot;1&quot; }"
																								title="${reply.feeledId == 1 ? '좋아요 취소':'좋아요' }">
																								<i class="fa fa-heart-c fa-1g"></i>
																							</a>
																							<span> : </span>
																							<a	role="dialog"
																								aria-control="dialog-feel-users"
																								ajaxify-dialog-init="DialogFeelUsers.onAjaxFeelUsers"
																								class="ui-reply-feel-score"
																								href="/feel/users"
																								rel="sync-get"
																								data-request-map="{&quot;contentId&quot;:${reply.id },&quot;feeledId&quot;:null,&quot;top&quot;:true,&quot;size&quot;:20}"
																								data-title="댓글 평가 사용자"
																								data-close-fn="DialogFeelUsers.onClose"
																								data-feel-score="${reply.dynamic.feelScore }">${reply.dynamic.feelScore }명</a>
																						</span>
																					</c:when>
																					<c:otherwise>
																						<span class="ui-reply-like-button">
																							<a	role="dialog"
																								aria-control="dialog-feel-users"
																								ajaxify-dialog-init="DialogFeelUsers.onAjaxFeelUsers"
																								class="ui-reply-feel-score"
																								href="/feel/users"
																								rel="sync-get"
																								data-request-map="{&quot;contentId&quot;:${reply.id },&quot;feeledId&quot;:null,&quot;top&quot;:true,&quot;size&quot;:20}"
																								data-title="댓글 평가 사용자"
																								data-close-fn="DialogFeelUsers.onClose"
																								data-feel-score="${reply.dynamic.feelScore }">${reply.dynamic.feelScore }명</a>
																						</span>
																					</c:otherwise>
																				</c:choose>																	
																			</div>
																		</div>
																	</div>
																</div>	
															</div>
														</li>
												</c:forEach>
												<c:if test="${isAuthenticated==true && ( empty smallGroup || not empty smallGroupUser )}">
													<li class="ui-react-row _l">
														<div class="z1">
															<div class="cmnt-profile-box">
																<div>
																	<img src="${authUserProfilePic }" alt="" class="cmmnt-profile-pic">
																</div>
															</div>
															<div class="block-content-input">
																<div class="ui-composer-out">
																	<div class="ui-mentions-input">
																		<div class="highlighter">
																			<div><span class="highlighter-content hidden-elem mentions-highlighter"></span></div>
																		</div>
																		<div class="ui-type-head mentions-type-head">
																			<c:choose>
																				<c:when test="${ sunny.device.mobile || sunny.device.tablet }">
																					<form	rel="async"
																							name="cmmnt-form-${story.id }" 
																							method="post" 
																							action="/reply/post" 
																							onsubmit="return false">
																						<div class="inner-wrap add-file-comment">
																							<textarea title="댓글달기"
																									  placeholder="댓글달기" 
																									  autocomplete="off"
																									  spellcheck="false"
																									  class="text-input mentions-textarea ui-textarea-auto-grow data-textarea-autogrow-set ui-textarea-no-resize ui-add-comment-input dom-control-placeholder"></textarea>
																						</div>
																						<input name="contentId" value="${story.id }" type="hidden">
																						<input name="text" autocomplete="off" class="mentions-hidden mentions-text" type="hidden" value="">																							
																					</form>
																					<div class="composer-tool-button _m">
																						<a rel="ignore" role="button" class="composer-tool-button-file">
																							<div class="composer-tool-button-inner">
																								<form enctype="multipart/form-data" action="/upload" method="post">
																									<input type="file" name="file" onchange="Reply.onFileSelected(this);" accept="*/*">
																									<input type="hidden" name="upid" value="">
																								</form>
																							</div>
																						</a>
																					</div>
																					<div class="btn-add-commnet-wrap">	
																						<a href="#" class="btn-add-commnet hmo-button hmo-button-white hmo-button-small-3">게시</a>
																					</div>
																				</c:when>
																				<c:otherwise>
																					<form	rel="async"
																							name="cmmnt-form-${story.id }" 
																							method="post" 
																							action="/reply/post" 
																							onsubmit="return false">
																						<div class="inner-wrap attach-file">
																							<textarea	title="댓글달기"
																									  	placeholder="댓글달기" 
																									  	autocomplete="off"
																										spellcheck="false"
																								  		data-request-map="{&quot;contentId&quot;:&quot;${story.id }&quot;}"
																								  		class="text-input mentions-textarea ui-textarea-auto-grow data-textarea-autogrow-set ui-textarea-no-resize ui-add-comment-input dom-control-placeholder"></textarea>
																						</div>
																						<input name="contentId" value="${story.id }" type="hidden">
																						<input name="text" autocomplete="off" class="mentions-hidden mentions-text" type="hidden" value="">
																					</form>
																					<div class="composer-tool-button">
																						<a rel="ignore" role="button" class="composer-tool-button-file">
																							<div class="composer-tool-button-inner">
																								<form enctype="multipart/form-data" action="/upload" method="post">
																									<input type="file" name="file" onchange="Reply.onFileSelected(this);" accept="*/*">
																									<input type="hidden" name="upid" value="">
																								</form>
																							</div>
																						</a>
																					</div>
																				</c:otherwise>
																			</c:choose>
																		</div>
																	</div>
																	<div style="position:relative; height:0">
																		<div class="mention-select-box" style="display:none">
																			<ul></ul>	
																		</div>
																	</div>
																	<div class="ufi-comment-file-attached-preview" style="display:none">
																		<div class="ui-scaled-image-container">
																		</div>
																		<a	href="#"
																			role="button"
																			class="cmnt-a-pview-close"
																			aria-label="Remove Photo"
																			data-hover="tooltip"
																			data-tooltip-alignh="center">
																			<i class="fa fa-times fa-1g"></i></a>
																	</div>
																</div>
															</div>
														</div>	
													</li>
												</c:if>
											</ul>
										</div>
									</div>
								</c:if><%-- 시스템 메시지 아닐때 if문 End --%>
								</div>
								<div class="timeline-items-footer"></div>
						</div>
					</div>
			</c:forEach>
		</div>
	</c:otherwise>
</c:choose>
<div class="ui-stream-pagelet stream-pager">
	<div id="ui-stream-pager" class="">
		<div class="ui-pager-error">오류 또는 타임아웃 [<a href="javascript:;" id="stream-pager-retry">새로고침</a>]</div>
		<div class="ui-pager-no-more">더 이상 표시할 게시물이 없습니다.</div>
		<div class="ui-pager-more-loader"><img class="img" src="/assets/sunny/2.0/img/sunny-loader-big.gif" alt="" height="11px" width="16px"></div>
	</div>
</div>
<div class="hmo-dialog-res" id="dialog-feel-users">
	<div class="hmo-dialog-scrollable-area" id="dialog-feel-users-scrollable-area">
		<div class="d-fuc-l" id="dialog-feel-users-list"></div>
	</div>
</div>
<div class="hmo-dialog-res" id="dialog-bookmark">
	<div class="hmo-dialog-body-inner">
		<form class="form-horizontal rw-form" id="form-bookmark">
			<div class="row-fluid">
				<div class="span12">
					<div class="control-group bm-comment-group">
						<label class="control-label" for="title">설명(선택사항)</label>
						<div class="controls">
							<div class="row-fluid">
								<textarea class="span12 bm-comment-textarea" name="title" id="title" placeholder="설명을 넣어주세요"></textarea>
							</div>
						</div>
					</div>		
				</div>
			</div>
		</form>
	</div>
</div>
<div class="hmo-dialog-res" id="dialog-content-permissions">
	<div class="hmo-dialog-scrollable-area" id="dialog-content-permissions-scrollable-area">
		<div class="d-fuc-l" id="dialog-content-permissions-list"></div>
	</div>
</div>