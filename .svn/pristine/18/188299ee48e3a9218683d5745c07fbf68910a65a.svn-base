/*	****************************************************************
	*
	*	iOS Dialog Box
	* 	www.sucaijiayuan.com
	****************************************************************
	*
	*	Version:
	*		0.0.1 PreAlpha
	*
	*	Last Revision:
	*		Aug 11, 2012
	*
	****************************************************************
	*
	*	License:
	*
	*		Creative Commons España:
	*
	*			Reconocimiento - CompartirIgual (by-sa):
	*				Se permite el uso comercial de la obra y de las
	*				posibles obras derivadas, la distribución de las
	*				cuales se debe hacer con una licencia igual a la
	*				que regula la obra original.
	*
	****************************************************************
	*
	*	Author:
	*		David Garcia
	*		david.garcia@deralia.com
	*
	****************************************************************
	*
	*	How to use:
	*
	*		Alert Dialog Box:
	*
	*			$('#id').click(function(){
	*				$(this).iOSdialogBoxAlert( {
	*					'title':'' , 'message':'' , 'button':''
	*				} , callback_function });
	*
	*		Confirm Dialog Box:
	*
	*			$('#id').click(function(){
	*				$(this).iOSdialogBoxConfirm( {
	*					'title':'' , 'message':'' , 'button1':'' , 'button2':''
	*				} , callback_function });
	*
	**************************************************************** */

(function($){




	/* ****************************************************************
	* iOS Dialog Box - Alert
	**************************************************************** */

	$.fn.iOSdialogBoxAlert = function( opciones , callback ){
		if ( !opciones || typeof opciones != 'object' ) { opciones = {}  }
		if ( !opciones['title'] ) { opciones['title'] = 'Alert'; }
		if ( !opciones['message'] ) { opciones['message'] = 'This is the iOS Alert Dialog Box'; }
		if ( !opciones['button'] ) { opciones['button'] = 'OK'; }
		if ( !callback ) { callback = null ; }
		$('body').append( $('<div>').attr('id','iOSdialogBoxLockScreen') )
				 .append(
				 	$('<div>').attr('id','iOSdialogBoxWindow')
							  .append( $('<div>').attr('id','iOSdialogBoxWindowTitle').html( opciones['title'] ) )
							  .append( $('<div>').attr('id','iOSdialogBoxWindowMessage').html( opciones['message'] ) )
							  .append( $('<div>').attr('id','iOSdialogBoxWindowButtons')
							  					 .append(
												 	$('<div>').attr('class','iOSdialogBoxButton')
															  .css({'position':'absolute','left':'-20px'})
															  .append(
															  	$('<div>').html( opciones['button'] )
																		  .click(function(e){
																			  $('#iOSdialogBoxLockScreen').remove();
																			  $('#iOSdialogBoxWindow').remove();
																			  if ( callback != null ) { callback( 1 ) ; }
																		  })
															  )
												 )
							  )
				 );
		var	altura = $('.iOSdialogBoxButton div').height(); altura /= 2 ; altura *= -1 ; altura += 'px' ;
		$('.iOSdialogBoxButton div').css('margin-top',altura);
		$('.iOSdialogBoxButton').css('margin-left','128px');
	};

	/* ****************************************************************
	* iOS Dialog Box - Confirm
	**************************************************************** */

	$.fn.iOSdialogBoxConfirm = function( opciones , callback ){
		if ( !opciones || typeof opciones != 'object' ) { opciones = {}  }
		if ( !opciones['title'] ) { opciones['title'] = 'Confirm'; }
		if ( !opciones['message'] ) { opciones['message'] = 'This is the iOS Confirm Dialog Box'; }
		if ( !opciones['button1'] ) { opciones['button1'] = 'Confirm'; }
		if ( !opciones['button2'] ) { opciones['button2'] = 'Cancel'; }
		if ( !callback ) { callback = null ; }
		$('body').append( $('<div>').attr('id','iOSdialogBoxLockScreen') )
				 .append(
				 	$('<div>').attr('id','iOSdialogBoxWindow')
							  .append( $('<div>').attr('id','iOSdialogBoxWindowTitle').html( opciones['title'] ) )
							  .append( $('<div>').attr('id','iOSdialogBoxWindowMessage').html( opciones['message'] ) )
							  .append( $('<div>').attr('id','iOSdialogBoxWindowButtons')
							  					 .append(
												 	$('<div>').attr('class','iOSdialogBoxButton')
															  .css('float','left')
															  .append(
															    $('<div>').html( opciones['button1'] )
																		  .click(function(e){
																			  $('#iOSdialogBoxLockScreen').remove();
																			  $('#iOSdialogBoxWindow').remove();
																			  if ( callback != null ) { callback( 1 ) ; }
																		  })
															    )
												 )
							  					 .append(
												 	$('<div>').attr('class','iOSdialogBoxButton')
															  .css('float','right')
															  .append(
															    $('<div>').html( opciones['button2'] )
																		  .click(function(e){
																			  $('#iOSdialogBoxLockScreen').remove();
																			  $('#iOSdialogBoxWindow').remove();
																			  if ( callback != null ) { callback( 2 ) ; }
																		  })
															    )
												 )
							  )
				 );
		var	altura = $('.iOSdialogBoxButton div').height(); altura /= 2 ; altura *= -1 ; altura += 'px' ;
		$('.iOSdialogBoxButton div').css('margin-top',altura);
	};




})(jQuery);