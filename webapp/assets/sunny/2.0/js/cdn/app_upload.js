theApp = {
	onInitInstance: function() {
		$( ".file-uploader" ).FileUploader( fileUploader );
	}	
};

var fileUploader = {
	maxFileSize: 1000,	
	idForm: "form-upload",
	onUploadStart: function() {
	},
	onUploadCompleted: function( result ) {
	}	
};

(function($) {
	$.fn.FileUploader = function( uploader ) {

		if( this.length == 0 ) {
			$.warn( "1 More Element Selected. Not Applied" );
			return;
		}

		if( uploader == null ) {
			$.warn( "tools object is null" );
			return;
		}
		
//		$(this).children(":file").change( function() {
//				
//			$( "#" + uploader.idForm ).submit();
//		});
		
		$(this).children("button").click( function(e){
			$("#" + uploader.idForm).submit();
		});
		
		return this;
	};
})( jQuery );
