/**
 * 
 *	FileUploader
 * 
 */
var FileUploader = {
	MAX_FILESIZE:__usize,
	$fileItemBox: null,
	uploadIdCounter: 0,
	session: {},
	isDraft: typeof(draftId) != "undefined" ? true: false, 
	init: function() {
		$( document.body ).onHMOClick( ".file-grid-item-remove-button", this.onRemoveButtonClicked );
	},
	onFileSelected:function( file ) {
		var
		f = ( file.files ) ? file.files : [ file ],
		countFile = f.length;
		
		if( countFile == 0 ) {
			return;
		}
		
		//
		this.$fileItemBox = $(file).parents( ".composer-files" );
		if( this.$fileItemBox.length == 0 ) {
			this.$fileItemBox = $(file).parents( ".ui-composer-bottom" ).prev();
		}
		var $nextItemTrigger = this.$fileItemBox.find( ".next-set-item" );
		
		//check file size
		for( var i = 0; i < countFile; i++ ) {
			if( f[i].size > this.MAX_FILESIZE ) {
				MessageBox( "파일업로드", "다음 파일이 업로드 용량을 초과 하였습니다.<br>" + f[i].name + " : " + " 파일 사이즈 " + this.MAX_FILESIZE + "byte", MB_INFORMATION );
				return;		
			}		
		}

		// counting and generate session id
		var uploadId = "Up" + this.uploadIdCounter++;
		
		// #number session is...
		this.session[ uploadId ] = {
			id: uploadId,	
			countFile: countFile,
			onUploadFailure: function( result ) {
				MessageBox( "파일업로드", "파일 업로드가 실패하였습니다.<br>" + result.message, MB_ERROR );
			},
			onUploadComplete: function( result ) {
				var result = $.parseJSON( result ),
					datas = result.data;

				for( var i = 0; i < datas.length; i++ ) {	

					var
					data = datas[i],
					item = $( "#item-" + this.id + "-" + i );
					
					//$.log( this.id + "::" + data );
					//alert( this.id + "::" + JSON.stringify( data ) );
					
					if( data.mediaType != 2 ) { // not image
						var html = "<span class='uploaded-files file'><span>" + data.fileName + "</span></span>";
						
						item.removeClass( "file-uploading" );
						item.find(".ui-scaled-image-container").html(html);
						item.find("span.file").attr( "data-up-file", data.id );		
						
					} else if( data.mediaType == 2) { // image
						
						var dimension, classname, style;
						
						if( data.width > data.height ) {
							classname="uploaded-files img";
							dimension="height=96px";
							style="left:-" + ((((96*data.width)/data.height)-96)/2) + "px";
						}else{
							classname="uploaded-files img scaled-image-fit-width";
							dimension="width=96px";
							style="top:-" + ((((96*data.height)/data.width)-96)/2 ) + "px";
						}
						
						var html = "<img class='" + classname + "' src='" + data.relativePath + "/" + data.prefix + "_" + data.id + "_m.jpg' style='" + style + "' alt='' " + dimension + ">";
						
						item.removeClass( "file-uploading" );
						item.find(".ui-scaled-image-container").html(html);
						if( FileUploader.isDraft ) {
							item.find(".file-grid-item-insert-button").html( '<a href="#" data-hugesrc="' + data.relativePath + '/' + data.prefix + '_' + data.id + '_h.jpg' + '" onclick="return insert_to_focus(this);"><i class="fa fa-arrow-circle-o-up bigger-250"></i></a>' );
						};
						
						item.find("img").attr( "data-up-file", data.id );		
					}
				}
			} 
		}
		
		//
		this.$fileItemBox.show();
		
		//
		this[ "onUploadFailure" ] = this.session[ uploadId ].onUploadFailure.bind( this.session[ uploadId ] );
		this[ "onUploadComplete" + uploadId ] = this.session[ uploadId ].onUploadComplete.bind( this.session[ uploadId ] );
		
		//
		var pushToFocus = "";
		if( this.isDraft ){
			pushToFocus = '<label class="file-grid-item-insert-button" data-tooltip-alignh="left" aria-label="이 사진을 커서에 넣습니다." data-hover="tooltip" for=""></label>';
		}
		for( var i = 0; i < countFile; i++ ) {
			var divFileItem = $("<div class='file-grid-item upload-image-item file-uploading' id='item-" + uploadId + "-" + i + "'></div>");
			divFileItem.insertBefore( $nextItemTrigger );
			divFileItem.html(
				"<div class='ui-scaled-image-container'><span>업로딩</span></div>" + 
				pushToFocus + 
				"<label class='ui-button-opa' data-tooltip-alignh='left' aria-label='이 파일을 삭제합니다.' data-hover='tooltip' for=''>" + 
				"<a href='#' class='ui-button-opa-a file-grid-item-remove-button'><i class='fa fa-times fa-1g'></i></a></label>" );
		}
		
		//
		var $iframe = $( "<iframe data-on-submit='false' name='if-file-upload-" + uploadId + "' style='display:none; width:0; height:0;'></iframe>" );
		$iframe.insertAfter( $( "body" ) );
		$iframe.load( function() {
			var $form = $(file).parent();
			var onSubmit = $iframe.attr( "data-on-submit" ) == "true";
			if( onSubmit ) {
				$.log("onsubmit");
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
	clear:function(){
		$(".upload-image-item").remove();
		this.$fileItemBox.hide();
		this.uploadIdCounter = 0;
	},
	onRemoveButtonClicked:function( $event ){
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
	}
};