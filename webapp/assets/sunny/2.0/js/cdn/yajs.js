var	yaJS = {
	emptyFunction: function(){},
	falseFunction: function(){ return false; }
};

// the object Of Application
var theApp = {};

//
yaJS.Element = {
	idCounter: 0,
	$prefix: function( id ){
		return $("#" + id);
	}
}

var $E = yaJS.Element.$prefix;

// Array Shortcut
var $A = Array.from = function( iterable ) {
	if ( !iterable )
		return [];
	if ( iterable.toArray ) {
		return iterable.toArray();
	} else {
		var results = [];
		for ( var i = 0, length = iterable.length; i < length; i++)
			results.push( iterable[i] );
		return results;
	}
}

// extends Function
$.extend( Function.prototype, {
  argumentNames: function() {
    var names = this.toString().match(/^[\s\(]*function[^(]*\((.*?)\)/)[1].split(",").invoke("strip");
    return names.length == 1 && !names[0] ? [] : names;
  },
  bind: function() {
    if (arguments.length < 2 && arguments[0] === undefined) {
    	return this;
    }
    
    var __method = this, 
    	args = $A( arguments ), 
    	object = args.shift();
    
    return function() {
      return __method.apply( object, args.concat( $A( arguments ) ) );
    }
  },
  methodize: function() {
    if (this._methodized) return this._methodized;
    var __method = this;
    return this._methodized = function() {
      return __method.apply(null, [this].concat( $A( arguments ) ) );
    };
  }
});


// utils
(function( $ ) {
	
	// log, info, warn, error, assert
	$.each([ "log", "info", "warn", "error", "assert" ], function( i, fn ) {
        $[ fn ] = function() {
            if ( !window.console ) {
            	return;
            }
            console[ fn ].apply( console, arguments );
        };
//        $.fn[fn] = function() {
//            var p = [ this ];
//            for( var i = 0; i < arguments.length; i++ ){
//            	p.push( arguments[i] );
//            }
//            $[fn].apply( this, p );
//            return this;
//        };
    });
	
	// identify
	$.fn.identify = function() {
		if( this.length != 1 ) {
			$.warn( "1 More Element Selected. Not Applied" );
			return null;
		}		
		if( !this[0].tagName ) {
			$.warn( "Only Support HTMLElement(HTML TAG), Not Apply" );
			return null;
		}
		var $e = $( this[0] );
		var id = $e.attr( "id" ); 
		if( id ) {
			return id;
		}
        do { 
        	id = "anonymous_element_" + yaJS.Element.idCounter++;
        } while ( $( "#" + id ).length > 0 );
        $e.attr( "id", id );
        return id;
	}
	
})( jQuery );



/**
 * 	requestURL
 */
var requestURL = {
	server: "www.yacamp.com",
	port: 8080,
	get: function( uri ) {
		return ( "http://" + this.server + ":" + this.port + uri );
	}
}

// entry
$(window.document).bind( "ready", function(){
	//alert(this === window.document );
	
	// detach handler
	$( this ).unbind( "ready" );

	//instantiate theApp Object
	( theApp.onInitInstance || yaJS.emptyFunction ).apply( theApp );
});