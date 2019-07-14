<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib  prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:import url="/WEB-INF/views/common/head.jsp">
	<c:param name="title">모두의 소식</c:param>
	<c:param name="bsUsed">NO</c:param>
	<c:param name="aceUsed">NO</c:param>
	<c:param name="hmoUsed">YES</c:param>
</c:import>
<script src="/assets/sunny/2.0/js/composer/story-writer/core.js"></script>
<script src="/assets/sunny/2.0/js/uncompressed/mention-input-listener.js"></script>
<script src="/assets/sunny/2.0/js/uncompressed/file-uploader.js"></script>
<c:choose>
	<c:when test="${sunny.device.mobile || sunny.device.tablet }">
		<script src="/assets/sunny/2.0/js/uncompressed/file-viewer-m.js"></script>
	</c:when>
	<c:otherwise>
		<script src="/assets/sunny/2.0/js/uncompressed/file-viewer.js"></script>
	</c:otherwise>
</c:choose>
<script>
document.domain=window.__g__.documentDomain;

/* -- Reply Object-- */
var Reply = {
	MAX_FILESIZE:__usize,
	session: {},
	_wait: 0,
	_ejsReplyTemplate: null,
	_autoresizeAdjustActive: false,
	_autoresizeInputOffset: 0,
	_$autoresizeMirror: null,
	_inputListeners: [],
	_keyContextLookupInterval: null,
	_$fileAttachPreview: null,
	_$composerOut: null,
	init: function() {
		//
		this._ejsReplyTemplate = new EJS({ url: "/assets/sunny/2.0/js/template/story-stream-reply.ejs?v=1.0" });
		
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
		$.log( "Reply.onFocus" );
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
			$.error("error:Reply.onReplyStream:$.ajax-" + data.message);
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


/* -- Object Story -- */
var Story = {
	_waitStoryModify: 0,			
	_ejsEditableComposerTemplate: null,
	_ejsStoryMessageEditedTemplate: null,
	_ejsStoryMediasEditedTemplate: null,
	_$streamList: null,
	init: function(){
		//
		StoryWriter.attachInputListener( MentionInputListener );
		//
		this._ejsEditableComposerTemplate = new EJS({
			url: "/assets/sunny/2.0/js/template/story-editable-composer.ejs?v=1.0"
		});
		this._ejsStoryMessageEditedTemplate = new EJS({
			url: "/assets/sunny/2.0/js/template/story-message-edited.ejs?v=1.0"
		});
		this._ejsStoryMediasEditedTemplate = new EJS({
			url: "/assets/sunny/2.0/js/template/story-medias-edited.ejs?v=1.0"
		});
		//			
		this._$streamList = $( "#story-stream-list" );
		//
		$( document.body ).onHMOClick( ".composer-button-text-bold", this.onButtonTextBoldClicked );
		$( document.body ).onHMOClick( ".file-grid-item-remove-button", this.onFileGridItemRemoveButtonClicked );
		//
		$( document.body ).onHMOClick(".generated-mention", this.onMentionClicked );
		$( document.body ).onHMOClick(".generated-hash", this.onHashClicked );
		//
		timesince();
	},
	loadImages: function() {
		var
		$container = $( "#story-content-container" ),
		wc = $container.width(),
		$images = $( ".photo-image" ),
		count = $images.length;
		
		for( var i = 0; i < count; i++ ) {
			var image = new Image();
			image.$ = $( $images[ i ] );
			image.onload = function() {
				var 
				wrap = this.$.parents(".stage-wrapper"),
				sc = this.$.parents(".stage-container"),
				con = this.$.parents(".stage-container-inner");
				if( sc.height() > this.height ) {
					con.css( "height", "100%" );
				}
				if( !__mobile__["is"] ) {
					this.$.attr( "src", this.src ).css( "width", ( wc < this.width ) ? "100%" : this.width ).show();
					setTimeout( function() {
						var
						ph = wrap.height(),
						ch = con.height();
		
						if( ch > ph ) {
							wrap.css( "top", ( ( ch - ph) / 2 ) + "px" ); 
						}
					}.bind(this), 10);
				} else {
					this.$.attr( "src", this.src ).show();
				}
			};
			image.src = image.$.attr( "data-src" ); 
		}		
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
			error:function(jqXHR,textStatus,errorThrown) {
				MessageBox( "스토리", "스토리 수정중 다음과 같은 오류가 발생했습니다.<br>" + errorThrown, MB_ERROR );
			}
		});
		
		return false;
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
	onAjaxStoryMofied: function( data ) {
		var
		_this = Story,
		story = data.data,
		$editableContents = $( "#editable-contents-" + story.id ),
		$editableMedias = $( "#editable-medias-" + story.id );
		
		_this._waitStoryModify = 0;
		if( data.result != "success" ) {
			MessageBox( "스토리", "스토리 수정중 다음과 같은 오류가 발생 했습니다.<br>" + data.message, MB_ERROR );
			return;
		}

		$editableMedias.html( Story._ejsStoryMediasEditedTemplate.render( story ) );
		$editableContents.html( Story._ejsStoryMessageEditedTemplate.render( story ) );
		
		Story.endEdit( story.id );
		Story.loadImages();
	},	
	onMentionClicked: function( $event ){
		$event.preventDefault();
		location.href="/user/" + $(this).data("uid");
	},
	onHashClicked: function( $event ) {
		$event.preventDefault();
		location.href="${param.currentUrl}?q%5B%5D=" + "%23" + $(this).text().substring($(this).text().indexOf("#") + 1);
	},
	onButtonTextBoldClicked: function( $event ) {
		MentionInputListener.bold();
	},	
	onFileGridItemRemoveButtonClicked: function( $event ){
		$event.preventDefault();
		
		var
		$button = $(this),
		$tiles = $button.parents( ".composer-file-tiles" );
		if( !FileUploader.$fileItemBox ) {
			FileUploader.$fileItemBox = $button.parents( ".composer-files" );
		}
		
		$button.parents(".photo-grid-item").remove();
		$button.parents(".file-grid-item").remove();
		
		if( $tiles.children().length == 1 ) {
			FileUploader.$fileItemBox.hide();
		}
		
		return false;
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
	},
	copyURlToClipboard: function( anchor ){
		window.prompt ("Ctrl+C 또는 CMD+C(Apple,Mac)를 누르면 이 스토리의  URL이 복사됩니다.", $(anchor).attr("href"));	
	}		
};


/* -- Object Dialogs    -- */
var DialogFeelUsers = {
	_iscroll: null,		
	onAjaxFeelUsers: function( data ) {
		//$.log( data );
		var
		__this = DialogFeelUsers,
		/* believe EJS caching */
		html = new EJS({ url : "/assets/sunny/2.0/js/template/feel-users.ejs?v=1.0" }).render( data ); 	
		_iscroll = new iScroll( "dialog-feel-users-scrollable-area", {
			onScrollEnd: __this.onScrollEnd.bind( __this )
		});		
		
		$("#dialog-feel-users-list").html( html );
	},
	onScrollEnd: function(){
		//invite_friend_streaming(false);
		//this._iscroll.refresh();
	},
	onClose: function() {
		$.log( this.get(0).tagName );		
		$("#dialog-feel-users-list").html( "" );
	}
};

var DialogBookmark = {
	onRegisterClicked: function(){
		var
		_this = DialogBookmark;
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
		    success: _this.onResult,
			error:function( jqXHR,textStatus,errorThrown ) {
				$.log( "error:Event.__inlineSubmit:" + errorThrown );
			}
		});		
	},
	onResult: function( data ) {
    	if( data.result == "fail" ){
    		MessageBox( "스크랩", "다음과 같은 에러가 발생했습니다.<br>"+ data.message, MB_ERROR );
    		return false; 
    	}
		MessageBox( "스크랩", "스크랩 등록이 성공 했습니다.", MB_INFORMATION );
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
		html = new EJS({ url : "/assets/sunny/2.0/js/template/content-permissions.ejs?v=1.0" }).render( data ); 	

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

$(function(){
	Story.loadImages();
	timesince();	
	Story.init();
	Reply.init();
});

</script>
</head>
<body class="scroll-y">
<div class="rw-snn-wrap" id="rw-snn-wrap">
	<div id="rw-snn-navi-wrap"></div>
	<div id="rw-snn-navir-wrap">
		<c:import url="/WEB-INF/views/common/nav-list-right.jsp">
		</c:import>
	</div>	
	<div id="rw-snn-main" class="rw-snn">
		<div id="rw-view-shield"></div>
		<!-- BEGIN:navbar -->
		<div class="navbar" id="navbar">
			<c:import url="/WEB-INF/views/common/navbar.jsp">
			</c:import>
		</div>		
		<!-- END:navbar -->
		<!-- BEGIN:main-container -->
		<div class="main-container container-fluid z1" id="main-container">
			<!-- BEGIN:sidebar -->
			<div class="sidebar" id="snn-sidebar">
				<!-- BEGIN:welcome-box -->		
				<c:import url="/WEB-INF/views/common/welcome-box.jsp">
				</c:import>
				<!-- END:welcome-box -->
	
				<!-- BEGIN:nav-list -->
				<c:import url="/WEB-INF/views/common/nav-list.jsp">
				</c:import>			
				<!-- END:nav-list -->
			</div>					
			<!-- END:sidebar -->
			<!-- BEGIN:main-content -->		
			<div class="main-content z1">
			
				<c:choose>
					<c:when test="${ not sunny.device.mobile }">
						<!-- BEGIN:breadcrumbs&search -->
						<c:set var="breadcrumbs" value="스토리 보기" scope="request"/>
						<c:set var="breadcrumbLinks" value="#" scope="request"/>
						<div class="breadcrumbs" id="breadcrumbs">
							<c:import url="/WEB-INF/views/common/breadcrumbs.jsp">
								<c:param name="breadcrumbs">${breadcrumbs }</c:param>	
								<c:param name="breadcrumbLinks">${breadcrumbLinks }</c:param>	
							</c:import>			
						</div>					
						<!-- END:breadcrumbs&search -->
					</c:when>
					<c:otherwise>
						<div class="breadcrumbs" id="breadcrumbs">
							<div class="breadcrumbs-inner  story-view-header-mobile">
								<div class="ui-header">
									<div class="ui-header-top">
										<h2 class="ui-header-title">스토리 보기</h2>
									</div>
									
									<c:if test="${ not story.systemMessage }">
										<div class="z1 ui-header-subtitle">
											<div class="l-ft fwn fcg">
												<a href="/user/${story.postUserId }">${story.postUserName }님의 프로필 보기</a>
											</div>
										</div>
									</c:if>
								</div>
							</div>
						</div>					
					</c:otherwise>						
				
				</c:choose>
				
				<!-- BEGIN:page-content -->						
				<div class="page-content z1">
					<div class="rw-content-area-wrap">
						<div class="rw-content-area">
							<div class="story-view-header">
								<div class="ui-header pbs">
									<div class="z1 ui-header-top">
										<h2 class="ui-header-title">스토리 보기</h2>
									</div>								
									<c:if test="${ not story.systemMessage }">
										<div class="z1">
											<div class="l-ft fwn fcg">
												<a href="/user/${story.postUserId }">${story.postUserName }님의 프로필 보기</a>
											</div>
										</div>
									</c:if>
								</div>
							</div>
							<div class="rw-pagelet-wrap">	
								<div class="ui-paglet" id="story-content-container">
									<div id="editable-medias-${story.id }">
										<table class="ui-grid _51mx media-stage">
											<tbody>
												<c:set value="${fn:length(story.mediaes) }" var="mediaCount"/>
												<c:if test="${story.type == 1 && mediaCount > 0 }">
													<c:forEach begin="1" end="${mediaCount }" step="1" var="index">
														<c:set value="${story.mediaes[ index-1] }" var="media"/>
														<c:choose>
															<c:when test="${media.mediaType != 2 }">
																<c:if test="${sunny.device.mobile }">
																	<tr class="_51mx">
																		<td class="v-mid h-cent stage-container f-container">
																			<div class="stage-container-inner" >
																				<div class="stage-wrapper">
																					<div class="f-stage-wrap">
																						<div class="f-stage">
																							<div class="f-stage-image">
																								<div class="file-desc-item">
																									${media.fileName }
																								</div>
																								<div class="file-desc-item _l">
																									${media.formatSize }
																								</div> 
																							</div>
																						</div>
																					</div>
																				</div>
																			</div>
																		</td>
																	</tr>
																	<tr class="_51mx">
																		<td class="menu-contaoner" >
																			<div class="menu-footerline-wrap">
																				<div class="z1 menu-footerline-menus menu-navi">
																					<a	class="mf-hl-m-item _l"
																						href="/download/${media.id}/${media.fileName }"
																						id="cv-counter-reader">
																						<i class="fa fa-download fa-1g"></i>
																						<span>다운로드</span>
																					</a>
																				</div>
																			</div>
																		</td>
																	</tr>																
																</c:if>
															</c:when>												
															<c:when test="${media.mediaType == 2 }">
																<tr class="_51mx">
																	<td class="v-mid h-cent stage-container">
																		<div class="stage-container-inner">
																			<c:if test="${not sunny.device.mobile }">
																				<a	onclick="FileViewer&amp;&amp;FileViewer.load(this); return false;"
																					href="/story/${story.id }?pid=${media.id }&cnt=${mediaCount }&idx=${index-1 }&mode=theater"
																					class="image-view-lager"
																					rel="sync-get"
																					data-request-map="{}">
																					<i class="fa fa-search-plus fa-1g"></i></a>
																			</c:if>
																			<div class="stage-wrapper">
																				<div class="image-stage">
																					<a	onclick="FileViewer&amp;&amp;FileViewer.load(this); return false;"
																						href="/story/${story.id }?pid=${media.id }&cnt=${mediaCount }&idx=${index-1 }&mode=theater"
																						class="ui-photo-thumb" 
																						rel="sync-get"
																						data-request-map="{}">
																						<img class="photo-image img" data-src="${media.hugePath }" style="display:none">
																					</a>	 
																				</div>
																			</div>
																		</div>
																	</td>
																</tr>
																<c:if test="${sunny.device.mobile }">
																<tr class="_51mx">
																	<td class="menu-contaoner" >
																		<div class="menu-footerline-wrap">
																			<div class="z1 menu-footerline-menus menu-navi">
																				<a	onclick="FileViewer&amp;&amp;FileViewer.load(this); return false;"
																					href="/story/${story.id }?pid=${media.id }&cnt=${mediaCount }&idx=${index-1 }&mode=theater"
																					class="mf-hl-m-item _l"
																					rel="sync-get"
																					data-request-map="{}"
																					id="cv-counter-reader">
																					<i class="fa fa-search-plus bigger-130"></i>
																					<span>크게보기</span>
																				</a>
																			</div>
																		</div>
																	</td>
																</tr>
																</c:if>
															</c:when>
														</c:choose>
														
													</c:forEach>
												</c:if>
											</tbody>
										</table>
										
										<c:if test="${not sunny.device.mobile && story.type == 1 && mediaCount > 0 }">
											<table class="ui-grid _51mx media-stage">
												<tbody>
													<c:forEach begin="1" end="${mediaCount }" step="1" var="index">
														<c:set value="${story.mediaes[ index-1] }" var="media"/>
														<c:if test="${media.mediaType != 2 }">
															<tr class="_51mx">
																<td class="v-mid h-cent stage-container file">
																	<div class="stage-container-inner">
																		<a	data-hover="tooltip"
																			aria-label="다운로드"
																			class="image-view-lager"
																			href="/download/${media.id}/${media.fileName }">
																			<i class="fa fa-download fa-1g"></i>
																		</a>
																		<div class="stage-wrapper">
																			<div class="f-stage" style="top:55px">
																				<a href="/download/${media.id}/${media.fileName }">
																					<img class="file-image img" src="/assets/sunny/2.0/img/file-icon.png">
																				</a>	
																				<div class="file-desc-item">
																					${media.fileName }
																				</div>
																				<div class="file-desc-item _l">
																					${media.formatSize }
																				</div> 
																			</div>
																		</div>
																	</div>	
																</td>
															</tr>												
														</c:if>
													</c:forEach>
												</tbody>
											</table>
										</c:if>										
									</div>
									<div class="timeline-container rw-ui-stream-story" id="story-box-${story.id }" data-map="{'id':'${story.id }'}">
										<div class="timeline-items-wrap">

												<div class="timeline-items">
													<div class="timeline-item item-header">
														<div class="rw-story-title">
															<a class="profile-pic" href="${not story.systemMessage ? '/user/${story.postUserId}' : '#'}">
																<img class="img" alt="${story.userName }" aria-label="${story.userName }" src="${not empty story.postUserProfilePic ? story.postUserProfilePic : story.userProfilePic}">	
															</a>
															<div class="c">
																<h3>
																	<c:choose>
																		<c:when test="${story.groupStory == true }">
													 						<a href="${not story.systemMessage ? '/user/${story.userId}' : '#'}">
																					<strong class="actor">${story.userName }</strong>
																			</a>
																			<c:if test="${ not story.systemMessage }">
																				<i class="fa fa-caret-right bigger-130"></i>
																				<a href="/group/${story.smallGroupId}">
																					<strong class="actor">${story.smallGroupName }${story.smallGroupType == 3 ? '(부서)' : ( story.smallGroupType == 4 ? '(프로젝트)' : '(소그룹)')}</strong>
																				</a>
																			</c:if>
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
																		<abbr data-utime="${story.arriveDate }" class="timestamp livetimestamp"  data-hover="tooltip" data-tooltip-alignh="left" aria-label="">&nbsp;</abbr>
																	</span>
																	
																	<c:if test="${ not story.systemMessage }">
																		<c:choose>																		
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
																		<span class="ip-address">
																			<c:forTokens var="part" items="${story.ipAddress }" delims="." varStatus="status">${status.first ? '' : '.'}${status.index == 2 ? 'xxx' : part }</c:forTokens>
																		</span>
																	</c:if>
																	<span>&nbsp;</span>
																	<a href="/story/${story.id }" onclick="Story.copyURlToClipboard(this); return false;" role="button" data-hover="tooltip" data-tooltip-alignh="left" title="스토리 URL 클립보드로 복사"><i class="fa fa-clipboard fa-1g"></i></a>
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
																<ul id="story-${story.id }-context-popup-menus"
																	class="ui-toggle-flyout dropdown-menu pull-right"
																	data-popup-trigger="story-${story.id }-context-trigger"
																	data-popup-group="global"													
																	data-fn-show="ContextMenuPopupToggler.onMenuShow"
																	data-fn-hide="ContextMenuPopupToggler.onMenuHide">
																	<c:if test="${isAuthenticated==true && ( empty smallGroup || not empty smallGroupUser ) && ( story.postUserId==authUserId || story.userId==authUserId ) }">
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
															</div>
														</div>
													</div>
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
													</div>
													<div class="timeline-item item-body item-actions">
														<span class="ui-action-links-bottom react-icons-${story.id }">
															<c:if test="${isAuthenticated == true && ( empty smallGroup || not empty smallGroupUser ) && not story.systemMessage }">
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
																		data-request-map="{&quot;contentId&quot;:&quot;${story.id }&quot;,&quot;feelId&quot;:&quot;1&quot;}">좋아요 <span class="react-icon <c:if test='${story.feeledId == 1}'>reacted</c:if>">취소</span></a>
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
																		rel="sync-get" 
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
														<c:if test="${not story.systemMessage}">
															<div class="rw-comment-container">
																<c:if test="${story.replyCount > 3 }">
																	<c:set value="10" var="requestReplyCount" />
																	<c:set value="${story.replyCount-3 }" var="remainReplyCount" />
																	<div class="cmnt-row-wrap">
																		<div class="timeline-feedback-actions _b cearfix">
																			<span> 
																				<a	rel="async-get"
																					href="/reply/stream"
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
																								class="ui-button-opa-a ui-comment-close-button"><i class="fa fa-times fa-1g"></i>
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
																										<a href="/download/${reply.mediaId}/${reply.fileName }">
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
																										<a	href="/feel/okay"
																											rel="sync-get"
																											ajaxify="Reply.onAjaxFeelOkay"
																											class="ui-reply-like-icon"
																											data-control-class="ui-reply-like-button-${reply.id }"
																											role="checkbox"
																											aria-checked="${reply.feeledId == 1 ? 'true':'false' }"
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
																											data-title="댓글  평가 사용자"
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
																											data-title="댓글  평가 사용자"
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
													</c:if>
												</div>
											</div>
											<c:if test="${ not story.systemMessage  }">
												<div class="timeline-items-footer"></div>
											</c:if>
										</div>
									</div>
								</div>
							</div>
							<c:if test="${ not sunny.device.mobile }">
								<c:import url="/WEB-INF/views/common/footer.jsp">
								</c:import>
							</c:if>							
						</div>
					</div>
					<div class="rw-right-col">
						<div class="rw-pagelet-blank"></div>
						<!-- BEGIN:rightcol -->
						<c:import url="/WEB-INF/views/common/right-col.jsp">
						</c:import>
						<!-- END:rightcol -->
					</div>				
				</div>
			</div>
			<!-- END:main-content -->		
		</div>
	</div>
</div>
<div id="reply-photo-viewer-full-screen" class="z1 ui-layer _l3 _3qx _3qw _abs _3qz full-screen-photo-viewer" style="display:none" onclick="Reply.closePhotoFullScreen(); return false;"">
	<div class="_l2">
		<div class="_l1">
			<c:choose>
				<c:when test="${sunny.device.mobile || sunny.device.tablet }">
					<img src="" style="width:100%">
				</c:when>
				<c:otherwise>
					<img src="">
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>
<div id="photo-viewer-full-screen" class="z1 ui-layer _l3 _3qx _3qw _abs _3qz full-screen-photo-viewer" style="display:none"  onclick="FileViewer.closeFullScreen(); return false;">
	<div class="_l2">
		<div class="_l1">
			<img src="" style="display:none">
		</div>
	</div>
</div>
<div id="photos-viewer" class="ui-layer _l3 _3qx _3qw  photos-viewer full-screen-available">
	<div class="_l2">
		<div class="_l1">
			<div id="photo-viewer-container" class="photo-viewer-container ui-contextual-layer-parent">
				<div class="z1 photo-viewer-popup">
					<c:choose>
						<c:when test="${not sunny.device.mobile &&  not sunny.device.tablet }">
							<div class="stage-wrapper l-ft">
								<div id="full-screen-switch" class="photo-viewer-full-screen full-screen-switch">
									<a	id="photo-viewer-full-screen-switch" 
										role="button"
										href="#"
										data-tooltip-alignh="left"
										data-tooltip-position="below"
										aria-label="사진 원본 보기"
										onclick="FileViewer.openFullScreen(); return false;"
										data-hover="tooltip"
										style="display:;">
										<i class="fa fa-expand  fa-1g"></i></a>
											
									<a	id="file-download-switch" 
										role="button"
										href="#"
										data-tooltip-alignh="left"
										data-tooltip-position="below"
										aria-label="파일 다운로드"
										data-hover="tooltip"
										style="display:none">
										<i class="fa fa-download fa-1g"></i></a>
								</div>
								<div class="stage photo-stage" onclick="FileViewer.changeImage(); return false;" >
									<img class="spotlight" alt="" src="" style="display:none" >
									<div class="stage-outter"></div>
								</div>
								<div class="stage file-stage" onclick="FileViewer.changeImage(); return false;" style="display:none">
									<div class="file-spot">
										<img src="/assets/sunny/2.0/img/file-icon.png">
										<div class="file-desc">
											<span class="file-desc-item" id="file-stage-name"></span>
											<span class="file-desc-item _l" id="file-stage-size"></span>																			
										</div>
									</div>
								</div>
								<a class="snow-lift-pager prev" title="이전"><i class="fa fa-arrow-left fa-1g" onclick="FileViewer.prevImage(); return false;"></i></a>
								<a class="snow-lift-pager next" title="다음"><i class="fa fa-arrow-right fa-1g" onclick="FileViewer.nextImage(); return false;"></i></a>
								<div id="bottom-stage" class="bottom-stage"></div>
							</div>
							<div class="story-ufi-container">
								<div class="detail-content-header">
									<div class="photos-viewer-controls">
										<a	class="close-theater" 
											role="button"
											aria-label="Esc 키를 누르면 창이 닫힙니다."
											data-hover="tooltip"
											data-tooltip-alignh="right"
											data-tooltip-position="below">
											<i class="fa fa-times fa-1g"></i></a>
									</div>
								</div>
								<div id="detail-content-body"></div>
							</div>
						</c:when>
						<c:otherwise>
							<div class="stage-wrapper" style="display:table; overflow:hidden">
								<div class="stage-swipe-list swipe-stage" style="position:relative;">
									<div class="stage-swipe-wrap" style="position:relative; width:20000px" id="stage-swipe_list">
									</div>
								</div>																
							</div>							
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
		<div class="_l4" id="closer-theater" style="position:absolute; top:10px; right:10px; z-index:10">
			<a	class="close-theater-button hmo-button"
				role="button"
				href="#">
				<span>닫기</span></a>
		</div>
		<div id="mention-select-box-wrap" style="position:absolute; height:0; width:271px;">
			<div class="mention-select-box" style="display:none">
				<ul></ul>	
			</div>
		</div>									
	</div>
	<c:if test="${sunny.device.mobile  ||  sunny.device.tablet }">
		<div class="story-ufh-container" id="story-ufh-container"></div>
	</c:if>
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
<form>
<textarea tabindex="-1" id="ta-cmmnt-mirroring" class="textarea-mirroring cmmnt-mirroring"></textarea>
</form>
</body>
</html>