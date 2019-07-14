
/** Channel */
var Channel = {
	_$win: null,
	_$doc: null,
	_$grobalWrap: null,
	_$streamList: null,
	_$channelList: null,
	_isStreaming: false,
	_isBackwardEnd: false,
	_rewind: false,	
	_interval_id_timesince: null,
	_ejsChannelStreamTemplate: null,
	_TIMESINCE_REFRESH_MILLIS: 60000,
	_DETECT_SCROLL_GAP: 200,
	init: function() {
		//
		this._$win = $( window ),
		this._$doc = $( document ),
		this._$grobalWrap = $( "#rw-snn-wrap" ),
		this._$streamList = $( "#channel-stream-list" );
		this._$channelList = $( "#channel-list" );
		//
		this._ejsChannelStreamTemplate = new EJS( { url: "/assets/sunny/2.0/js/template/message-channel.ejs?v=1.0" } );
		//
		this.timesince();
		//
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

		setTimeout( function() {
			var
			url = Channel._$streamList.attr( "data-stream-url" ),
			mode = Channel._$streamList.attr( "data-async" );
			if( !mode ) { 
				mode = "async-get"; 
			}
				
			var
			modes = mode.split( "-" ),
			async = ( modes[0] == "async" ),
			type = ( modes[1] == "post" ) ? "post" : "get",
			data = $.parseJSON( Channel._$streamList.attr( direction ? "data-fw-map" : "data-bw-map" ) );
			$.ajax({
				url:url,
				async:async,
				type:type,
				dataType:"json",
				contentType:"application/json",
				data:data,
				timeout: Channel.__TIMEOUT,
				success:( direction ? Channel.onForwardStreaming : Channel.onBackwardStreaming ),
				error:function( jqXHR, textStatus, errorThrown ) {
					if( textStatus == "timeout" ) {
						Channel._rewind = true;
						Channel._isStreaming=false;
						// Channel._$streamPager.hide().removeClass( "error no-more more-loader" ).addClass( "error" ).show();
					} else {
						MessageBox( "메세지 채널 스트리밍", "메세지 채널 스트리밍에 다음과 같은 에러가 발생 헀습니다.<br>" + errorThrown, MB_ERROR );
					}
				}	
			});
		}, 10);
	},
	timesince: function() {
		this._interval_id_timesince && clearInterval( this._interval_id_timesince );
		$( ".livetimestamp" ).each( Channel.refreshTimeSince );
		this._interval_id_timesince = setInterval( function() {
			$( ".livetimestamp" ).each( Channel.refreshTimeSince );
		}, this._TIMESINCE_REFRESH_MILLIS );
	},
	refreshTimeSince: function() {
		var
		$el=$(this),
		t=$el.attr("data-utime");
		if( !t ) {
		    var 
		    s=function(a,b){ return(1e15+a+"").slice(-b); },
		    d=new Date(parseInt($el.attr("data-timestamp"), 10)),
		    t=d.getFullYear() + '-' +
			  s(d.getMonth()+1,2) + '-' +
		      s(d.getDate(),2) + ' ' +
		      s(d.getHours(),2) + ':' +
		      s(d.getMinutes(),2) + ':' +
		      s(d.getSeconds(),2);
		      
		    $el.attr("data-utime", t);
		}
		
		var	dt,	secs, itv, h, m, s = $.trim(t);
	    
		s=s.replace(/\.\d+/,"");
	    s=s.replace(/-/,"/").replace(/-/,"/");
	    s=s.replace(/T/," ").replace(/Z/," UTC");
	    s=s.replace(/([\+\-]\d\d)\:?(\d\d)/," $1$2");
	    dt=new Date(s);
		now = new Date();
	    
		if( now.getFullYear() != dt.getFullYear() ){
	    	$el.text( dt.getFullYear() + "-" + ( dt.getMonth()+1 ) + "-" + dt.getDate() );
	    }else if( now.getMonth() != dt.getMonth() ){
	    	$el.text( (dt.getMonth()+1) + "월 " + dt.getDate() + "일" );
	    }else if( now.getDate() != dt.getDate() ){
	    	$el.text( (dt.getMonth()+1) + "월 " + dt.getDate() + "일" );
	    }else{
	    	$el.text( ( ( h = dt.getHours() ) > 12 ? ( "오후 " + (h-12) ) : ( "오전 " + h ) ) + ":" + ( ( m = dt.getMinutes() ) < 10 ? "0" : "" ) + m );
	    }
	    
	    $el.attr("title", dt.getFullYear()+"년 "+(dt.getMonth()+1)+"월 "+dt.getDate()+"일 "+((h=dt.getHours())>12?("오후 "+(h-12)):("오전 "+h))+':'+((m=dt.getMinutes())<10?"0":"")+m);
	},
	onForwardStreaming: function( result ) {
	},
	onBackwardStreaming: function( result ) {
		var
		data = result.data.contents;
		
		if( !data || data.length == 0 ) {
			Channel._isStreaming = false;
			Channel._isBackwardEnd = true;
			return;
		}
		
		try {
			var	l = data.length;
			for( var i = 0; i < l; i++ ) {
				( function( d ) {
					var ld = d;
					setTimeout( function() {
						Channel._$channelList.append(  Channel._ejsChannelStreamTemplate.render( ld ) );
					}, __mobile__["is"] ? 60 : 0 );
				}) ( data[ i ] );
			}
			setTimeout( function() {
				if( l > 0 ) {
					var
					dataMap = $.parseJSON( Channel._$streamList.attr( "data-bw-map" ) );
					dataMap["page"] = dataMap["page"] + 1;
					Channel._$streamList.attr( "data-bw-map", JSON.stringify( dataMap ) );
				}
					
				Channel.timesince();
				Channel._isStreaming = false;
				
			}, __mobile__["is"] ? 1200 : 0 );
		} catch ( e ) {
			MessageBox( "메세지 채널", "메세지 채널을 가져오는 중에 다음과 같은 오류가 발생했습니다." + e, MB_ERROR );
		}
	},
	onChannelRowClicked: function( row ) {
		var
		$row = $( row );
		window.location.href = "/message/" + $row.parent().attr( "data-channel-id" );
	},
	onAjaxExitChannel: function( data ) {
		if( data.result != "success" ) {
			MessageBox( "메세지 채널", "대화방을 나가기에 실패했습니다.", MB_ERROR );
			return;
		}

		var
		$anchor = $( this ),
		$li = $anchor.parents( ".channel-row" );
		
		$li.fadeOut( 600, "linear", function(){ $li.remove(); });	
	},
	onWindowScroll: function( $event ) {
		var
		wct = Channel._$win.scrollTop(),
		wh = Channel._$win.height(),
		dh = Channel._$doc.height();
			
		if( ( ( wct + wh + Channel._DETECT_SCROLL_GAP ) >= dh ) &&
			( Channel._$grobalWrap.hasClass( "open-menu" ) == false ) &&
			( Channel._$grobalWrap.hasClass( "_theater" ) == false ) && 
			( !NotifyFlyout || !NotifyFlyout.isFlyout ) ) {
			Channel.streaming( false );
		}		
	}
};


/** Messenger */
var Messenger = {
	_$w: null,
	_$scrollabaleArea: null,
	_$scrollabaleContent: null,
	_$editArea: null,
	_$streamList: null,
	__inputOffset: 0,
	__inputMinHeight: 0,
	__contentAreaHeightBeforeScroll: -1,
	__adjust_active: false,
	__POSTFIX_MIRROR_ID__: "-mirror",
	__iscroll: null,
	__isStreaming: false,
	__isAllFetched: false,
	__waitSendingMessage: 0,
	__ejsMessageStreamTemplate: null,
	__intervalIdForTimeSince: null,
	__listReadNotification: [],
	__isChkReadStreaming: false,
	channel: { "id": null, "usrs": [] },
	UNFOLDING_HEIGHT: 48,	
	INPUT_HEIGHT_OFFSET: 0,
	INPUT_KOR_MIRROR_OFFSET: 4,
	BOTTOM_OFFSET: ( __mobile__[ "is"] ) ? 0 : 5,
	TIMESINCE_REFRESH_MILLIS: 60000,
	init: function() {
		// 0.
		this.__ejsMessageStreamTemplate = new EJS( { url: "/assets/sunny/2.0/js/template/message-stream.ejs?v=1.0" } );
		
		// 1.주요 엘러멘트 세팅
		this._$w = $( window );
		this._$scrollabaleArea = $( "#msgr-message-v" );
		this._$scrollabaleContent = $( "#msgr-scrollable-area-content" );
		this._$editArea = $( "#msgr-edit" );
		this._$streamList = $( "#msgr-stream-list" );
		
		// 2.init message textarea
		this.textareaAutoResize();
		
		// 3.create iscroll
		this.__iscroll = new IScroll( "#msgr-scrollable-area-body", {
			bounce:false,
			//momentum:false,
			scrollbars: true,
			mouseWheel: true,
			interactiveScrollbars: true,
			shrinkScrollbars: 'scale',
			fadeScrollbars: true
		});
		
		//iscroll event mapping
		this.__iscroll.on( 'scrollEnd', this.onScrollEnd );		
		
		// 4. 윈도우창 리사이징 이벤트  맵핑
		this._$w.on( "resize", this.onWindowResize );

		// 5. 기본 레이이아웃 잡기
		this.recalcLayout();

		// 6. 모바일이 아닌 경우에만 디폴트 포커스
		if( __mobile__["is"] ) {
			$( "#btn-send-message" ).onHMOClick( null, this.onSendMessageClicked );
		} else {
			$( "#ta-message-text" ).focus();
		}

		// 7. 첫번째 메세지 스트림밍 
		this.streaming( false, true );
	},
	refreshChannel: function() {
		var
		data = { "id": this.channel["id"] };
		
		$.ajax( { 
            url: "/channel/info", 
            async: false, 
            type: "GET", 
            dataType: "json", 
            contentType: 'application/json', 
            headers: { 
                "Accept": "application/json", 
                "Content-Type": "application/json" 
            }, 
            data: data, 
            success: this.onAjaxRefreshChannel, 
            error: function( jqXHR, textStatus, errorThrown ) { 
                MessageBox( "메신저", "채널정보 업데이트중 다음과 같은 문제가 발생했습니다." + errorThrown, MB_OK );
            }         
        }); 
	},
	getChannelUser: function( userId ) {
		var
		count = this.channel.usrs.length;
		
		for( var i = 0; i < count; i++ ) {
			var
			usr = this.channel.usrs[ i ];
			if( usr.id == userId ) {
				return usr;
			}
		}
		
		return null;
	},
	sendMessage: function( $ta ) {
		// 보내는 중.
		if( this.__waitSendingMessage == 1 ) {
			this.__waitSendingMessage = 0;
			return;
		}
		//
		var 
		$form = $ta.parents( "form" ),
		rel = $form.attr( "rel" ),
		url = $form.attr( "action" ),
		type = $form.attr( "method" ),
		data = $form.serializeObject();
		// 비어있으면?
		if( data[ "text" ] === "" ) {
			$ta.focus();
			return;
		}
		// 지금부터 보내는 중.
		this.__waitSendingMessage = 1;
		// 텍스트 에리어 비우기
		$ta.val("");
		this.adjustTa.call( $ta );
		// 발송!
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
			data:JSON.stringify( data ),
			success: this.onAjaxSendMessage.bind( $ta ),
			error: function( jqXHR, textStatus, errorThrown ){
				MessageBox( "메세지", "메세지를 보내는 중 다음과 같은 에러가 발샐 했습니다. + " + errorThrow, MB_ERROR );	
			}		
		});		
	},
	streaming: function( direction, initLoad ) {
		if( direction && Messenger.__isAllFetched ) {
			/* 모두 가져왔음! */
			return;
		}
		
		if( this.__isStreaming ) { 
			return; 
		}
		
		this.__isStreaming = true;
		if( initLoad ) {
			this.__isChkReadStreaming = true;
		}
		
		var
		url = this._$streamList.attr( "data-stream-url" ),
		mode = this._$streamList.attr( "data-async" );
		if( !mode ) {
			mode = "async-get";
		}
		
		var
		modes = mode.split("-"),
		async = ( modes[0] == "async" ),
		type  = ( modes[1] == "post" ) ? "post" : "get",
		data  = $.parseJSON( this._$streamList.attr( direction ? "data-fw-map" : "data-bw-map" ) );
		
		$.ajax( {
			url: url,
			async: async,
			type: type,
			dataType: "json",
			contentType: "application/json",
			data: data,
			success:( direction ? this.onForwradStreaming : this.onBackwardStreaming ),
			error: function( jqXHR,textStatus,errorThrown ) {
				MessageBox( "메신저",  "메세지를 가져오는 중  에러가 발생 했습니다.<br>" + errorThrown, MB_ERROR );
			}	
		});		
	},
	readMessage: function() {
		var
		count = this.__listReadNotification.length; // 전체 처리해야 하는 리드노티 리스트에서의 리드노티 갯수
		
		for( var i = 0; i < count;) {
			
			var
			notify = this.__listReadNotification[ i ],
			user = this.getChannelUser( notify.ruid ),
			lastMessageId = notify.mid;

			// 사용자가 없으면, 리드노티 리스트에서 없앰
			if( user == null ) {
				$.log( "현재 채널에서 사용자[" + notify.ruid + "]를 찾을 수 없습니다." );
				
				this.__listReadNotification.splice( i, 1 );
				count--;
				continue;
			}
			
			// 대화창에 해당 메세지가 없으면,
			// 대화 노티가 늦게 도착할 수 있으니 후에 처리...
			if( $( "#message-" + lastMessageId ).length == 0 ) {
			//	$.log( "메세지[" + lastMessageId + "]가 없음...." );
				i++;
				continue;
			}
			
			// 리드노티 처리
			var
			currentMessageId = user[ "lMsgId" ];
			$.log( "사용자[" + notify.ruid + "]의 읽은 메세지 정보  현재: #" + currentMessageId + ", 노티에서 읽은 마지막 메세지: #" + lastMessageId );
			
			for(var j = currentMessageId + 1; j <= lastMessageId; j++ ) {
				var
				$messageCount = $( "#message-" + j );
			
				if( $messageCount.length == 0 ) {
					continue;
				}

				var
				cnt = $messageCount.attr( "data-count" );
				
				// $.log( "--> #" + j + " 메세지 읽은 처리! " );
				$messageCount.attr( "data-count", cnt = ( cnt > 0 ? cnt-1 : 0 ) );
				$messageCount.text( cnt );
				
				// 0이면 화면에서 없앰	
				( cnt == 0 ) && $messageCount.hide();
			}
			
			user[ "lMsgId" ] = lastMessageId;
			this.__listReadNotification.splice( i, 1 );
			count--;
		}
		//$.log( "listReadNotification : " + this.__listReadNotification.length );
	},
	textareaAutoResize: function() {
		var
		$textarea = $( "#ta-message-text" );
		
		// 미러링용 textarea 생성
		$( "<textarea/>", {
			"id": $textarea.identify() + this.__POSTFIX_MIRROR_ID__,
			"class": "ui-textarea-message-mirror",
			"tabindex": -1	
		}).
		appendTo( $("body") ).
		css( "width", $textarea.width() - this.INPUT_KOR_MIRROR_OFFSET );
		
		// 높이 보정
		if( $textarea.css( 'box-sizing' ) === "border-box" || 
			$textarea.css( '-moz-box-sizing' ) === "border-box" || 
			$textarea.css( '-webkit-box-sizing' ) === "border-box" ) {
			this.__inputOffset = $textarea.outerHeight() - $textarea.height();
		}
		
		// 미러 레퍼런스 저장 및 이벤트 매핑 ( 기본  )
		$textarea.
		attr( "aria-mirror", $textarea.identify() + this.__POSTFIX_MIRROR_ID__ ).
		keydown( this.onTaMessageKeydown ).
		focus( this.onFocus );		
		
		// 이벤트 맵핑 ( change )
		var ta = $textarea.get( 0 );
		if( "onpropertychange" in ta ) {
			if( "oninput" in ta) {
		    	ta.oninput = Messenger.adjustTa;
		    	$textarea.keydown( function(){ 
		    		setTimeout( Messenger.adjustTa.bind( $textarea ), 50 ); 
		    	} );
			} else {
				ta.onpropertychange =  Messenger.adjustTa;
		    }
		} else {
		    ta.oninput =  Messenger.adjustTa;
		}
	},
	adjustTa: function() {
		if( Messenger.__adjust_active ) { 
			return; 
		}
		
		var
		$ta = $( this ),
		$mirror = $( "#" + $ta.attr( "aria-mirror" ) ),
		mirror = $mirror.get(0),
		original = $ta.height(),
		height;

		Messenger.__adjust_active = true;
		$mirror.val( $ta.val() );
		
		mirror.scrollTop = 0;
		mirror.scrollTop = 9e4;
		height = mirror.scrollTop + Messenger.__inputOffset;
		if( original !== height ) {
			if( height < Messenger.__inputMinHeight ) {
				height = Messenger.__inputMinHeight;			
			}
			$ta.css( "height", height );
			Messenger.recalcLayout();
		}
		setTimeout( function() { 
			Messenger.__adjust_active = false; 
		}, 10);
	},
	recalcLayout: function() {
		var
		offset = this._$scrollabaleArea.offset(),
 		wh = this._$w.height(),
		eh = this._$editArea.height();
		
		//
		this._$scrollabaleArea.css({ "height": ( wh - offset.top - eh - this.BOTTOM_OFFSET ) });
		this.__iscroll.refresh();

		var
		sh = this._$scrollabaleArea.height(), 
		ch = this._$scrollabaleContent.height();
		
		if( ch > sh ) {
			this._$scrollabaleContent.css( { "bottom": "auto" });
			if( this.__contentAreaHeightBeforeScroll > 0 ) {
				this.__iscroll.scrollTo( 0, this.__contentAreaHeightBeforeScroll - ch,  0 );
				this.__contentAreaHeightBeforeScroll = -1;
			} else {
				this.__iscroll.scrollTo( 0, this.__iscroll.maxScrollY, 0 );
			}
		} else {
			this._$scrollabaleContent.css( { "bottom": 0 });
		}

		// 모바일 크롬 돔 업데이트 버그 픽스
		var	$lis = this._$streamList.children();
		setTimeout( function() {
			var li = $lis.get( 0 );
			if( !li ) {
				return;
			}
			li.style.display = "none";
			li.offsetHeight;
			li.style.display = "list-item";
		}, 0 );
	},
	testEnter: function() {
		var
		$ta = $( this ),
		$mirror = $( "#" + $ta.attr( "aria-mirror" ) ),
		text = $ta.val(),
		mirror = $mirror.get( 0 ),
		original = $ta.height(),
		height;

		$mirror.val( text + "\n" );
		mirror.scrollTop = 0;
		mirror.scrollTop = 9e4;
		height = mirror.scrollTop + Messenger.__inputOffset;
		
		if( original !== height ) {
			if( height < Messenger.__inputMinHeight ) {
				height = Messenger.__inputMinHeight;			
			}
			$ta.css( "height", height );
			Messenger.recalcLayout();
		}
		
		$mirror.val( text );		
	},
	timeSince: function() {
		this.__intervalIdForTimeSince && clearInterval( this.__intervalIdForTimeSince );
		$( ".livetimestamp" ).each( Messenger.refreshTimeSince );
		
		this.__intervalIdForTimeSince = setInterval( function() {
			$( ".livetimestamp" ).each( Messenger.refreshTimeSince );
		}, this.TIMESINCE_REFRESH_MILLIS );
	},
	refreshTimeSince: function() {
		var
		$el=$(this),
		t=$el.attr("data-utime");
		
		if( !t ) {
		    var 
		    s=function(a,b){ return(1e15+a+"").slice(-b); },
		    d=new Date(parseInt($el.attr("data-timestamp"), 10)),
		    t=d.getFullYear() + '-' +
			  s(d.getMonth()+1,2) + '-' +
		      s(d.getDate(),2) + ' ' +
		      s(d.getHours(),2) + ':' +
		      s(d.getMinutes(),2) + ':' +
		      s(d.getSeconds(),2);
		      
		    $el.attr("data-utime", t);
		}
		
		var	dt,	secs, itv, h, m, s = $.trim(t);
	    
		s = s.replace(/\.\d+/,"");
	    s = s.replace(/-/,"/").replace(/-/,"/");
	    s = s.replace(/T/," ").replace(/Z/," UTC");
	    s = s.replace(/([\+\-]\d\d)\:?(\d\d)/," $1$2");
	    dt = new Date(s);
		now = new Date();
	    
		if( now.getFullYear() != dt.getFullYear() ){
	    	$el.text( dt.getFullYear() + "-" + ( dt.getMonth()+1 ) + "-" + dt.getDate() );
	    }else if( now.getMonth() != dt.getMonth() ){
	    	$el.text( (dt.getMonth()+1) + "월 " + dt.getDate() + "일 " + ( ( h = dt.getHours() ) > 12 ? ( "오후 " + (h-12) ) : ( "오전 " + h ) ) + ":" + ( ( m = dt.getMinutes() ) < 10 ? "0" : "" ) + m );
	    }else if( now.getDate() != dt.getDate() ){
	    	$el.text( (dt.getMonth()+1) + "월 " + dt.getDate() + "일 " + ( ( h = dt.getHours() ) > 12 ? ( "오후 " + (h-12) ) : ( "오전 " + h ) ) + ":" + ( ( m = dt.getMinutes() ) < 10 ? "0" : "" ) + m );
	    }else{
	    	$el.text( ( ( h = dt.getHours() ) > 12 ? ( "오후 " + (h-12) ) : ( "오전 " + h ) ) + ":" + ( ( m = dt.getMinutes() ) < 10 ? "0" : "" ) + m );
	    }
	    
	    $el.attr("title", dt.getFullYear()+"년 "+(dt.getMonth()+1)+"월 "+dt.getDate()+"일 "+((h=dt.getHours())>12?("오후 "+(h-12)):("오전 "+h))+':'+((m=dt.getMinutes())<10?"0":"")+m);
	},	
	onNotify: function( notify ) {
		
		 $.log( "==============================");
		 $.log( "onNotify:" + JSON.stringify( notify ) );
		 $.log( "==============================");
		
		if( notify.cid != this.channel.id ) {
			return;
		}	
		
		if( notify.t == 0 ) { // 메세지 수신
			Messenger.streaming( false );
		} else if( notify.t == 1 ) { // 읽음 알림
			Messenger.__listReadNotification.push({ "ruid": notify.ruid, "mid": notify.mid });
			Messenger.readMessage();
		}
	},
	// 글을 쓰고 나서 백워드
	onBackwardStreaming: function( result ) {
		var data = result.data;

		if( !data || data.length == 0 ) {
			Messenger.__isStreaming = false;
			return;
		}

		//$.log( data );
		try {
			result[ "direction" ] = false; // tag direction
			if( Messenger.__isChkReadStreaming ) {
				result[ "initLoad" ] = true;
				Messenger.__isChkReadStreaming = false;
			} else {
				result[ "initLoad" ] = false;
			}
			var	html = Messenger.__ejsMessageStreamTemplate.render( result );
			Messenger._$streamList.append( html );
	
			var l = data.length;
			if( l > 0 ){

				var dataMap;
				dataMap = $.parseJSON( Messenger._$streamList.attr( "data-bw-map" ) );
				dataMap["msgId"] = data[0].info.id;
				Messenger._$streamList.attr( "data-bw-map", JSON.stringify( dataMap ) );
				
				dataMap = $.parseJSON( Messenger._$streamList.attr( "data-fw-map" ) );
				if( dataMap[ "msgId" ] == null || dataMap[ "msgId" ] == "" ){
					dataMap[ "msgId" ] = data[ l - 1 ].info.id;
					Messenger._$streamList.attr( "data-fw-map", JSON.stringify( dataMap ) );
				}
			}
			
			Messenger.__isStreaming = false;
			Messenger.recalcLayout();
			Messenger.timeSince();
			Messenger.readMessage();
			//$.log( "---------------- 메세지 수신 처리 끝 ------------ " );
		} catch( e ) {
			MessageBox( "메신저", "메세지를 가져오는 중  에러가 발생 했습니다.<br>" + e, MB_ERROR );
		}
	},
	// 상단으로 쭉쭉
	onForwradStreaming: function( result ) {
		//$.log( result );
		var data = result.data;

		if( !data || data.length == 0 ) {
			Messenger.__isAllFetched = true;
			Messenger.__isStreaming = false;
			return;
		}

		//$.log( data );
		try {
			result[ "direction" ] = false; // tag direction
			result[ "initLoad" ] = true; //  check read count
			var	html = Messenger.__ejsMessageStreamTemplate.render( result );
			Messenger._$streamList.prepend( html );
			
			var l = data.length;
			
			if( l > 0 ) {
				var dataMap;
				
				dataMap = $.parseJSON( Messenger._$streamList.attr( "data-fw-map" ) );
				dataMap[ "msgId" ] = data[ l-1 ].info.id;
				Messenger._$streamList.attr( "data-fw-map", JSON.stringify( dataMap ) );
				

				dataMap = $.parseJSON( Messenger._$streamList.attr( "data-bw-map" ) );
				if( dataMap[ "msgId" ] == null || dataMap[ "msgId" ] == "" ){
					dataMap[ "msgId" ] = data[ 0 ].info.id;
					Messenger._$streamList.attr( "data-bw-map", JSON.stringify( dataMap ) );
				}
				
			}

			Messenger.__isStreaming = false;
			Messenger.recalcLayout();
			Messenger.timeSince();
			
		} catch( e ) {
			MessageBox( "메신저", "메세지를 가져오는 중  에러가 발생 했습니다.<br>" + e, MB_ERROR );
		}		
	},
	onAjaxRefreshChannel: function( data ){
		if( data.result != "success" ) {
            MessageBox( "메신저", "채널정보 업데이트중 다음과 같은 문제가 발생했습니다." + data.message );
            return;
		}
		
		var
		d = data.data,
		users = d.users,
		l = users.length;
		
		// clear
		Messenger.channel.usrs = [];
		for( var i = 0; i < l; i++ ) {
			var user = users[ i ];
			Messenger.channel.usrs.push({
				"id": user[ "id" ],
				"name": user[ "name" ],
				"lMsgId": user[ "lastReadMessageInfoId" ]
			});
		}
		
		$.log( Messenger.channel.usrs );
		
		var
		countIn = Messenger.channel.usrs.length,
		usersText = "",
		maxUserCount = __mobile__[ "is" ] ? 3 : 6,
		userCount = ( countIn > maxUserCount ) ? maxUserCount : countIn;
			
		for( var i = 0; i < userCount; i++ ) {
			var 
			usr = Messenger.channel.usrs[ i ];
			usersText += ( usr.name + ( ( i + 1 < userCount ) ? ", " : " " ) );
		}
			
		if( countIn > maxUserCount ) {
			usersText += ( " + " + ( countIn - maxUserCount ) );
		}
		
		$( "._users" ).find( "h2" ).text( usersText ); 
	},	
	onAjaxExitChannel: function( data ) {
		if( data.result != "success" ) {
			MessageBox( "메세지 채널", "대화방을 나가기에 실패했습니다.", MB_ERROR );
			return;
		}
		window.location.href = "/message/channels";
	},	
	onAjaxSendMessage: function( data ) {
		if( data.result != "success" ) {
			MessageBox( "메신저", "메세지 전송 중  다음과 같은 에러가 발생했습니다.<br>" + data.message, MB_ERROR );
			return;
		}		
		Messenger.__waitSendingMessage = 0;
	},
	onScrollEnd: function() {
		// $.log( this.y + ":" + this.maxScrollY );
		if( this.y == 0 ) {
			Messenger.__contentAreaHeightBeforeScroll = Messenger._$scrollabaleContent.height();
			Messenger.streaming( true );
		}	
	},	
	onFocus: function ( $event ) {
		var $ta = $( this );
		if( $ta.val() == "" ) {
			$ta.css( "height", Messenger.UNFOLDING_HEIGHT );
			Messenger.__inputMinHeight = $ta.height() + Messenger.INPUT_HEIGHT_OFFSET;
		}
	},
	onTaMessageKeydown: function( $event ) {
		var
		ta = this,
		$ta = $( ta );
		
		if( $event.keyCode != 10 && $event.keyCode != 13 ) {
			if( $event.keyCode == 27 ) {
				$ta.blur();			
			}
			return;
		}
		if( !__mobile__["is"] && $event.shiftKey === false ) {
			$event.preventDefault();		
			setTimeout( function() {
				Messenger.sendMessage( $ta );
			}, 10 );		
			return;								
		}
		
		Messenger.testEnter.call( $ta );
	},
	onSendMessageClicked: function( $event ) {
		var
		$btn = $( this ),
		$ta = $btn.parents( "._1rt" ).find( ".ui-textarea-message" );
		Messenger.sendMessage( $ta );
	},	
	onWindowResize: function( $event ) {
		Messenger.recalcLayout();
	}
};


/* -- dialog object ( Dialog ) -- */
var DialogChatUser = {
	page:1,
	isNextStreamLoading:false,
	_iscroll: null,		
	onAjaxUserStreamOrder: function( data ) {
		//$.log( data );
		if( DialogChatUser.page != 1 ){
			return;
		}
		var
		_this = DialogChatUser,
		html = new EJS({ url : "/assets/sunny/2.0/js/template/user-stream.ejs?v=1.2" }).render( data ); 	
		
		_this._iscroll = new iScroll( "dialog-start-chat-scrollable-area", {
			onScrollEnd: _this.onScrollEnd.bind( _this )
		});
		
		$("#dialog-start-chat-list").html( html );
		
		_this._iscroll.refresh();

		//
		$( document.body ).onHMOClick( ".checkbox-label", DialogChatUser.onCheckBoxClicked  );
		DialogChatUser.page = 2;
	},
	onCheckBoxClicked: function( $event ) {
		var
		$label = $( this );

		if( $label.hasClass( "checked" ) ) {
			$label.removeClass( "checked" );
		} else {
			$label.addClass( "checked" );
		}
		$label.siblings( "input[name='invite-checked']" ).prop("checked", true);
		$event.preventDefault();
	},
	onScrollEnd: function() {
		
		if( DialogChatUser._iscroll.y == DialogChatUser._iscroll.maxScrollY && DialogChatUser.isNextStreamLoading == false ) {
			DialogChatUser.isNextStreamLoading = true;
			
			var
			data = { "page": DialogChatUser.page,
					"status[]" : "0,1"};
			$.ajax( { 
	            url: "/user/stream/order", 
	            async: false, 
	            type: "GET", 
	            dataType: "json", 
	            contentType: 'application/json', 
	            headers: { 
	                "Accept": "application/json", 
	                "Content-Type": "application/json" 
	            }, 
	            data: data, 
	            success: function(data){
	            	
	            	if( data.result != "success" ){
		                MessageBox( "메신저", "사용자 리스트를 가져오는 중 다음과 같은 문제가 발생했습니다." + errorThrown, MB_OK );
		                DialogChatUser.isNextStreamLoading = false;
		                return false;
	            	}
	            	
	            	var _this = DialogChatUser, html = new EJS({ url : "/assets/sunny/2.0/js/template/user-stream.ejs?v=1.2" }).render( data ); 	
	        		
	        		$("#dialog-start-chat-list").append( html );
	        		
	        		_this._iscroll.refresh();
	        		
	            	// 마지막 페이지에 도달했으면 다음것을 영원히 갖고오지 않는다. 
	            	if( data.data.totalPages == data.data.pageNumber ){
	            		DialogChatUser.isNextStreamLoading = true;	
	            	}else{
	            		DialogChatUser.page = DialogChatUser.page + 1;
	            		DialogChatUser.isNextStreamLoading = false;
	            	}
	            	
	            },
	            error: function( jqXHR, textStatus, errorThrown ) { 
	                MessageBox( "메신저", "사용자 리스트를 가져오는 중 다음과 같은 문제가 발생했습니다." + errorThrown, MB_OK );
	            }         
	        });
		}
	},
	onStartChat: function() {
		var data = [];
		
		$( "input[name='invite-checked']:checked" ).each ( function( index ){
			data[ index ] = $( this ).val();
		});
		console.debug("전체", $("input[name='invite-checked']").length);
		console.debug($("input[name='invite-checked']:checked").length);
		
		if(  data.length < 1 ) {
			console.log(data);
			alert( "사용자를 선택하지 않았습니다." );
			return true;
		}
		
		$.ajax( {
			url : "/message/create",
			async : false,
			type : "post",
			dataType : "json",
			contentType : "application/json",
			data : JSON.stringify( {
				"userIds" : data
			} ),
			success : function( data ) {
				location.href = "/message/"	+ data.data.id;
			},
			error : function( jqXHR, textStatus, errorThrown ) {
				alert( "새대화방을 생성하는 중 오류가 발생했습니다." + errorThrown );
				return true;
			}
		});
		
		return false;
	},
	onInviteUser: function() {
		var
		result, // 리턴 값에 따라 다이알로그를 닫을 지 말지 결정할 수 있다. true: 안닫음, false: 닫음  
		$anchor = $( this ),
		data = [];
		
		$( "input[name='invite-checked']:checked" ).each ( function( index ){
			data[ index ] = $( this ).val();
		});
		
		if( data.length < 1 ) {
			alert( "사용자를 선택하지 않았습니다." );
			return true;
		}
		
		if( !Messenger.channel ) {
			alert( "채널 정보에 문제가 발생했습니다. 다시 대화방에 입장에 해주세요." );
			return true;
		}
		
		// 1:1  에서의 스페셜 처리
		if( Messenger.channel.type == 0 ) {
			
			var l = Messenger.channel.usrs.length;
			
			for( var i = 0; i < l; i++ ) {
				
				var usr = Messenger.channel.usrs[ i ];
				
				if( usr.id == __a__[ "id" ] || ( function( id ) {
					var c = data.length;
					for( var i = 0; i < c; i++ ) {
						if( data[ i ] ==  id ) {
							return true;
						}
					}
					return false;
				})(usr.id) ) {
					continue;
				}
				
				data.push( usr.id );
			}
			if( data.length == 1 && (function(){
				for( var i = 0; i < l; i++ ) {
					var usr = Messenger.channel.usrs[ i ];
					if( usr.id == data[0] ) {
						return true;
					}
				}
				return false;
			})()) {
				alert( "현재 채널에서 대화중 입니다." );
				return true;
			}

			// 채널(새 대화방) 개설
			result = false;
			$.ajax( {
				url : "/message/create",
				async : false,
				type : "post",
				dataType : "json",
				contentType : "application/json",
				data : JSON.stringify( {
					"userIds" : data
				} ),
				success : function( data ) {
					location.href = "/message/"	+ data.data.id;
				},
				error : function( jqXHR, textStatus, errorThrown ) {
					alert( "이 채널로 사용자를 초대하는 중 오류가 발생했습니다." + errorThrown );
					result = true; 
				}
			});
			
			return result;
		}
		
		//초대
		result = false;
		$.ajax({
			url: "/channel/invite?id=" + $anchor.attr( "data-channel-id" ),
			async: false,
			type: "post",
			dataType: "json",
			contentType: "application/json",
			data: JSON.stringify( data ),
			success: function( data ) {
				$.log( data );
				if( data.result != "success" ){
					alert( data.message );
					result = true; 
				}
			},
			error: function(jqXHR,textStatus,errorThrown){
				alert( "이 채널로 사용자를 초대하는 중 오류가 발생했습니다." + errorThrown );
				result = true; 
			}
		});
		
		return result;
	},
	onClose: function() {
		//$("#dialog-start-chat-list").html( "" );
	}
};