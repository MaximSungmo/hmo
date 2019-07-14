<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- <script type="text/javascript" src="/assets/ckeditor/ckeditor.js" charset="utf-8"></script> -->


<!-- <link rel="stylesheet" type="text/css" href="/assets/sunny/2.0/css/uncompressed/summernote.css" /> -->
<!-- <script src="/assets/sunny/2.0/js/uncompressed/summernote.min.js"></script> -->


<!-- <link rel="stylesheet" type="text/css" href="/assets/sunny/2.0/css/uncompressed/summernote.css" /> -->
<script src="/assets/tinymce/tinymce.min.js"></script>
<script src="/assets/tinymce/plugins/custom_templates/plugin.js"></script>

<style>
<!--
/* [class^="icon-"], [class*=" icon-"] { background-image:none; }	 */
-->
</style>

<!-- <script type="text/javascript" src="/assets/syntaxhighlighter/scripts/shCore.js" charset="utf-8" ></script> -->
<!-- <script type="text/javascript" src="/assets/syntaxhighlighter/scripts/shBrushJava.js" charset="utf-8" ></script> -->
 
<!-- <link rel="stylesheet" type="text/css" href="/assets/syntaxhighlighter/styles/shCore.css" /> -->
<!-- <link rel="stylesheet" type="text/css" href="/assets/syntaxhighlighter/styles/shThemeDefault.css" /> -->
  
<script>
// document.domain="${sunny.documentDomain}"; 
</script>

<div class="editor approval-write-editor">
	<div id="ReDwOoD_TeXtArEa" style="height:300px">
		${draft.rawText }
	</div>
</div>

<script>

$(document).ready(function(){
// 	$("#ReDwOoD_TeXtArEa").summernote({
// 		height: 300,   //set editable area's height
// 	    focus: false,    //set focus editable area after Initialize summernote
// 	    toolbar: [
// 	              //['style', ['style']], // no style button
// 	              ['style', ['bold', 'italic', 'underline', 'clear']],
// 	              ['fontsize', ['fontsize']],
// 	              ['color', ['color']],
// 	              ['para', ['ul', 'ol', 'paragraph']],
// 	              ['height', ['height']],
// 	              //['insert', ['picture', 'link']], // no insert buttons
// 	              ['table', ['table']], // no table button
// 	              //['help', ['help']] //no help button
// 	            ]
// 	});

	tinymce.init({
		selector: '#ReDwOoD_TeXtArEa',
		language : 'ko_KR',
		plugins: "autolink lists charmap preview hr anchor pagebreak visualblocks visualchars nonbreaking contextmenu directionality fullscreen table template paste textcolor code",
		toolbar1: "undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent ",
		toolbar2: "fullscreen preview | forecolor backcolor | template code",
	    tools: "inserttable"
	    
	    ,
	    template_cdate_classes: "cdate creationdate",
	    template_mdate_classes: "mdate modifieddate",
	    template_selected_content_classes: "selcontent",
	    template_cdate_format: "%m/%d/%Y : %H:%M:%S",
	    template_mdate_format: "%m/%d/%Y : %H:%M:%S",
	    template_replace_values: {
	        username : "${authUserName}"
	    },
	    templates :
	    [
	     	{
	            title: "지출 결의서",
	            url: "/template/pay",
	            description: "지출이 발생했을 시 사용"
	        }
		<c:forEach items="${templates}" var="template" varStatus="status">
			,
	        {
	            title: "${template.title}",
	            url: "/template/${template.id}",
	            description: "${template.description}"
	        }
	    </c:forEach>
		]	    
// 	    ,
// 	    custom_templates : [
//    	        {
//    	            title: "Editor Details",
//    	            url: "editor_details.htm",
//    	            description: "Adds Editor Name and Staff ID"
//    	        },
//    	        {
//    	            title: "Timestamp",
//    	            url: "time.htm",
//    	            description: "Adds an editing timestamp."
//    	        }
// 	    ]
	});
});

function getContentFromTextarea(){ 
// 	return $("#ReDwOoD_TeXtArEa").code();
	return tinyMCE.activeEditor.getContent();
}
function setContentToTextarea(injectTag){
	
	//$(".note-editable").append(injectTag);
	tinyMCE.activeEditor.insertContent(injectTag);
// 	pasteHtmlAtCaret(injectTag, false);
	//$("#ReDwOoD_TeXtArEa").code($("#ReDwOoD_TeXtArEa").code() + injectTag); 
} 

<%--
function pasteHtmlAtCaret(html, selectPastedContent) {
    var sel, range;
    if (window.getSelection) {
        // IE9 and non-IE
        sel = window.getSelection();
        if (sel.getRangeAt && sel.rangeCount) {
            range = sel.getRangeAt(0);
            range.deleteContents();

            // Range.createContextualFragment() would be useful here but is
            // only relatively recently standardized and is not supported in
            // some browsers (IE9, for one)
            var el = document.createElement("div");
            el.innerHTML = html;
            var frag = document.createDocumentFragment(), node, lastNode;
            while ( (node = el.firstChild) ) {
                lastNode = frag.appendChild(node);
            }
            var firstNode = frag.firstChild;
            range.insertNode(frag);

            // Preserve the selection
            if (lastNode) {
                range = range.cloneRange();
                range.setStartAfter(lastNode);
                if (selectPastedContent) {
                    range.setStartBefore(firstNode);
                } else {
                    range.collapse(true);
                }
                sel.removeAllRanges();
                sel.addRange(range);
            }
        }
    } else if ( (sel = document.selection) && sel.type != "Control") {
        // IE < 9
        var originalRange = sel.createRange();
        originalRange.collapse(true);
        sel.createRange().pasteHTML(html);
        if (selectPastedContent) {
            range = sel.createRange();
            range.setEndPoint("StartToStart", originalRange);
            range.select();
        }
    }
}
--%>

</script>

<!-- <script type="text/javascript"> -->
<!-- // CKEDITOR.replace( 'ReDwOoD_TeXtArEa' ); -->

<!-- // function getContentFromTextarea(){ -->
<!-- // 	return CKEDITOR.instances.ReDwOoD_TeXtArEa.getData(); -->
	
<!-- // } -->
<!-- // function setContentToTextarea(injectTag){ -->
<!-- // 	return CKEDITOR.instances.ReDwOoD_TeXtArEa.insertHtml(injectTag); -->
<!-- // } -->
<!-- </script> -->