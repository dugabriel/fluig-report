<script src="/fluig-tools/resources/js/fluig-tools.js" type="text/javascript"></script>
<script src="/fluig-tools/resources/js/jquery.form.js" type="text/javascript"></script>


<div class="wcm-widget-class super-widget fluig-style-guide main-div" id="fluigToolsObj_${instanceId}" 
	data-params="fluigToolsObj.instance({mode: 'view'})">
	
	<form role="form" id="formReport" name="formReport" method="POST" action="/fluig-tools/rest/tools/execute">
		<div class="row" style="visibility: hidden">
			<div class="form-group col-md-5">
				<div class="checkbox">
			        <label>
			        <input type="checkbox" id="detalhado" name="detalhado"> Relatório Detalhado?
			        </label>
			    </div>
			 </div>
	    </div>
	    <div class="row">
			<div class="form-group col-md-6">
		        <label for="process">Solicitações</label>
		        <input type="text" class="form-control" id="process" name="process" placeholder="Código das solicitações separados por vírgula">
		    </div>
	  	</div>
	  	<hr>
	 	<div class="row">
	 		<div class="form-group col-md-4">
	 			<h3>Data de Abertura</h3>
		 		<div class="checkbox">
			        <label>
			        <input type="checkbox" id="consultaAbertura" name="consultaAbertura"> Consulta Data de Abertura?
			        </label>
			    </div>
		   	</div>
		</div>
		<div class="row">
			<div class="form-group col-md-3">
		        <label for="dtAberturaIni">Data Inicial</label>
		        <div class="input-group date">
			         <input type="text" class="form-control" id="dtAbertura" name="dtAbertura" readonly placeholder="dd/mm/aaaa">
			        <span class="input-group-addon">
			            <span class="fluigicon fluigicon-calendar"></span>
			        </span>
			    </div>
		    </div>
		    <div class="form-group col-md-3">
		        <label for="dtFinal">Data Final</label>
		         <div class="input-group date">
			        <input type="text" class="form-control" id="dtFinal" name="dtFinal" readonly placeholder="dd/mm/aaaa">
			        <span class="input-group-addon">
			            <span class="fluigicon fluigicon-calendar"></span>
			        </span>
			    </div>
		    </div>
	  	</div>
	  	<hr>
	  	<div class="row">
	 		<div class="form-group col-md-4">
	 			<h3>Data de Vencimento</h3>
		 		<div class="checkbox">
			        <label>
			        <input type="checkbox" id="consultaVencimento" name="consultaVencimento"> Consulta Data de Vencimento?
			        </label>
			    </div>
		   	</div>
		</div>
		<div class="row">
			<div class="form-group col-md-3">
		        <label for="dtVencimentoIni">Data Inicial</label>
		        <div class="input-group date">
			         <input type="text" class="form-control" id="dtVencimentoIni" name="dtVencimentoIni" readonly placeholder="dd/mm/aaaa">
			        <span class="input-group-addon">
			            <span class="fluigicon fluigicon-calendar"></span>
			        </span>
			    </div>
		    </div>
		    <div class="form-group col-md-3">
		        <label for="dtVencimentoEnd">Data Final</label>
		         <div class="input-group date">
			        <input type="text" class="form-control" id="dtVencimentoEnd" name="dtVencimentoEnd" readonly placeholder="dd/mm/aaaa">
			        <span class="input-group-addon">
			            <span class="fluigicon fluigicon-calendar"></span>
			        </span>
			    </div>
		    </div>
	  	</div>
	    <hr>
	    <div class="row">
			<div class="form-group col-md-4">
		        <label for="area">Área</label>
		        <input type="text" class="form-control" id="area" name="area" placeholder="">
		        <input type="hidden" id="areaUnicode" name="areaUnicode">
		    </div>
	  	</div>
	  	<div class="row">
			<div class="form-group col-md-4">
		        <label for="processWorkflow">Processo</label>
		        <input type="text" class="form-control" id="processWorkflow" name="processWorkflow" placeholder="">
		        <input type="hidden" id="processWorkflowUnicode" name="processWorkflowUnicode">
		    </div>
	  	</div>
	  	<div class="row">
			<div class="form-group col-md-4">
		        <label for="openUser">Matrícula do Usuário de Abertura</label>
		        <input type="text" class="form-control" id="openUser" name="openUser" placeholder="">
		        <input type="hidden" id="openUserUnicode" name="openUserUnicode">
		    </div>
	  	</div>
	  	<div class="row">
			<div class="form-group col-md-4">
		        <label for="currentUser">Matrícula do Usuário Responsável</label>
		        <input type="text" class="form-control" id="currentUser" name="currentUser" placeholder="">
		        <input type="hidden" id="currentUserUnicode" name="currentUserUnicode">
		    </div>
	  	</div>
	  	<div class="row">
  			<div class="form-group col-md-4">
				<button type="submit" id="sendValues" name="sendValues" class="btn btn-default">Gerar Relatório</button>
			</div>
		</div>
	</form>
</div>