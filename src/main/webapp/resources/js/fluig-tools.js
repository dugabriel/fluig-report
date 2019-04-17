var fluigToolsObj = SuperWidget.extend({
	mode : null,
	ajax : null,
	loading : null,

	init: function() {

		if(this.mode == "view"){

			FLUIGC.calendar('.date');
			this.maskFields();
			this.createLoading();
		}
	},

	bindings: {
		local: {
			'execute-sql': ['click_execute']
		}
	},
	
	createLoading: function(){
		this.loading = FLUIGC.loading(window, {
		    textMessage:  '<h2>Gerando relatório...</h2>', 
		    title: null,
		    css: {
		        padding:        0,
		        margin:         0,
		        width:          '30%',
		        top:            '40%',
		        left:           '35%',
		        textAlign:      'center',
		        color:          '#000',
		        border:         '3px solid #aaa',
		        backgroundColor:'#fff',
		        cursor:         'wait'
		    },
		    overlayCSS:  { 
		        backgroundColor: '#000', 
		        opacity:         0.6, 
		        cursor:          'wait'
		    }, 
		    cursorReset: 'default',
		    baseZ: 1000,
		    centerX: true,
		    centerY: true,
		    bindEvents: true,
		    fadeIn:  200,
		    fadeOut:  400,
		    timeout: 0,
		    showOverlay: true, 
		    onBlock: null,
		    onUnblock: null,
		    ignoreIfBlocked: false
		});
	},

	execute: function() {
		console.log('execute');
		this.runSQL();
	},

	stop : function(){
		this.ajax.abort();
	},

	maskFields : function(){
		var that = this;
		
		$("#process" ).keypress(function(evt) {
			var theEvent = evt || window.event;
			var key = theEvent.keyCode || theEvent.which;
			key = String.fromCharCode(key);
			var regex = /[0-9]|\,/;
			if(!regex.test(key)){
				theEvent.returnValue = false;
				if(theEvent.preventDefault) theEvent.preventDefault();
			}
		});

		$("#formReport").submit(function(event){
			$("#processWorkflowUnicode").val(encodeURIComponent($("#processWorkflow").val()));
			$("#areaUnicode").val(encodeURIComponent($("#area").val()));
			$("#openUserUnicode").val(encodeURIComponent($("#openUser").val()));
			$("#currentUserUnicode").val(encodeURIComponent($("#currentUser").val()));

			var error = "";
			if($('input[name=consultaAbertura]:checked').length > 0){
				if($("#dtAbertura").val() == ""){
					error += "A data inicial de abertura é obrigatória\n";
				}
				if($("#dtFinal").val() == ""){
					error += "A data final de abertura é obrigatória\n";
				}
			}

			if($('input[name=consultaVencimento]:checked').length > 0){
				if($("#dtVencimentoIni").val() == ""){
					error += "A data inicial de vencimento é obrigatória\n";
				}
				if($("#dtVencimentoEnd").val() == ""){
					error += "A data final de vencimento é obrigatória\n";
				}
			}

			if(error != ""){
				FLUIGC.message.alert({
					message: error,
					title: 'Erro',
					label: 'OK'
				});
				return false;
			}else{
				FLUIGC.message.alert({
					message: 'Este relatório pode levar alguns minutos. Aguarde o download do arquivo excel.',
					title: 'Relatório em processamento.',
					label: 'OK'
				});
			}
		});


	},

});