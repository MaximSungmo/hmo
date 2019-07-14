/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicity call 'app.receivedEvent(...);'
    onDeviceReady: function() {
        app.receivedEvent('deviceready');
        
        
        $("#startDownload").click(function(e){
        	e.preventDefault();
        	startCordovaDownload(e);
        });
        
        
    },
    // Update DOM on a Received Event
    receivedEvent: function(id) {
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);
    }
};

var fileAttemptCount = 0;
function getDirEntry(rootFS, dirName){
	var dataDir = rootFS.getDirectory(dirName, {create: true, exclusive: false});		
	var dirEntry = null;
	window.resolveLocalFileSystemURI(
		rootFS.fullPath + "/" + dirName + "/",
		function(entry){
    		if(entry.isFile){
    			entry.remove( 
					function(){
						entry = getDirEntry( rootFS, dirName );
					},
					function(){
						alert("HelloMyOffice 란 파일이 이미 존재합니다. 삭제하신 뒤 다시 작업해주세요");
						return;
					}
    			)
    		}
    		dirEntry = entry;
    	},
    	function(evt){
    		console.log(JSON.stringify(evt));
    	}
	)
	return dirEntry;		
	
}

function parseToAvailableFilename( dirEntry, filename ){
	
	if( fileAttemptCount == 0 ){
	}else if( fileAttemptCount == 1 ){
		filename = fileAttemptCount + "_" + filename;
	}else{
		filename = fileAttemptCount + filename.substring(filename.indexOf("_"));
	}
	
	var availableFilename = filename;
	var exists = false; 		
	dirEntry.getFile(
		filename,
		{create:false},
		function(entry){
			exists = true;
    		// 파일이 이미 존재하면 다시 시도
    	},
    	function(evt){
    		// 에러 핸들러는 비동기로 처리되는듯.. 여기서 뭐 하면 안됨
    		console.log( JSON.stringify(evt) );
    	}
	);
	
	if( exists == true ){
		fileAttemptCount++;
		return parseToAvailableFilename( dirEntry, filename );
	}else{
		return availableFilename;
	}
			
}

function startCordovaDownload(e){
	e.preventDefault();

	var href = "https://fbcdn-sphotos-g-a.akamaihd.net/hphotos-ak-frc3/t1.0-9/1511072_688920744483254_1771225626_n.jpg";
	
	var filename = "1511072_688920744483254_1771225626_n.jpg";
	
	var rootFS = null;
	
	window.requestFileSystem  = window.requestFileSystem || window.webkitRequestFileSystem;
    window.requestFileSystem(
    		LocalFileSystem.PERSISTENT, 
    		0,
    		function(fileSystem){
    			rootFS = fileSystem.root;
    		}, 
    		function(){
        		console.log("error getting LocalFileSystem");
    		}
    );

    // Directory 를 읽어온다.
	var dirEntry = getDirEntry( rootFS, "HelloMyOffice");	   

    var availableFilename = parseToAvailableFilename( dirEntry, filename );
	fileAttemptCount = 0;
    
	var fileTransfer = new FileTransfer();
	
	var uri = encodeURI(href);
	
	
//	alert( uri );
//	fileTransfer.onprogress =  function(progressEvent) {
//	    if (progressEvent.lengthComputable) {
//	    	loadingStatus.setPercentage(progressEvent.loaded / progressEvent.total);
//	    } else {
//	      loadingStatus.increment();
//	    }
//	}
//	
	var localPath = dirEntry.fullPath + "/" + availableFilename;

	fileTransfer.abort( win, fail );
	fileTransfer.download(
	    uri,
	    localPath,
	    win,
	    fail,
	    true,
	    {}
	);
}
var win = function(entry) {
	alert("Success!!" + entry.fullPath);
	window.open(encodeURI(entry.fullPath),"_system","location=yes");
    console.log("download complete: " + entry.fullPath);
}

var fail = function(error) {
    alert("An error has occurred: Code = " + error.code);
    console.log("upload error source " + error.source);
    console.log("upload error target " + error.target);
}
function empty(){};
