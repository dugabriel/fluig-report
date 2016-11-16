package com.fluig.rest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fluig.dao.ExecuteQuery;

@Path("tools")
public class Tools {

	private Logger log = LoggerFactory.getLogger(Tools.class);

	@POST
	@Path("execute")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response executeSQL(@FormParam("process") String process,
							   @FormParam("detalhado") String detalhado,
							   @FormParam("consultaAbertura") String consultaAbertura,
							   @FormParam("dtFinal") String dtFinal,
							   @FormParam("dtAbertura") String dtAbertura,
							   @FormParam("consultaVencimento") String consultaVencimento,
							   @FormParam("dtVencimentoIni") String dtVencimentoIni,
							   @FormParam("dtVencimentoEnd") String dtVencimentoEnd,
							   @FormParam("areaUnicode") String area,
							   @FormParam("processWorkflowUnicode") String processWorkflow,
							   @FormParam("openUserUnicode") String openUser,
							   @FormParam("currentUserUnicode") String currentUser) throws SQLException, IOException{
		log.info("#initialize report alliar");
		
		ExecuteQuery query = new ExecuteQuery();
		query.makeQuery(process,detalhado,consultaAbertura,dtAbertura,
						dtFinal,consultaVencimento,dtVencimentoIni,dtVencimentoEnd,area,
						processWorkflow,openUser,currentUser);
		Workbook content = query.executeQuery();
		File temp = null;

		try {
			temp = File.createTempFile("temp", "sla");
			temp.deleteOnExit();
			
			FileOutputStream fout = new FileOutputStream(temp.getAbsolutePath());
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			content.write(outputStream);
			
			outputStream.writeTo(fout);
			outputStream.close();
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		ResponseBuilder response = Response.ok((Object) temp);
	    response.header("Content-Disposition", "attachment;filename=sla.xls");
	    return response.build();
	}
}