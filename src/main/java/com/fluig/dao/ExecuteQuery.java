package com.fluig.dao;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;







import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fluig.jdbc.ConnectionFactory;





public class ExecuteQuery {
	private Logger log = LoggerFactory.getLogger(ExecuteQuery.class);
	private Connection con = null;
	private ResultSet rs;
	private Statement stmt;
	private String query = null;

	public ExecuteQuery() {
		con = new ConnectionFactory().getConnection();
	}

	public Workbook executeQuery() {
		Workbook xls = null;
		try {
			this.stmt = this.con.createStatement();
			if(query != null){
				this.rs = this.stmt.executeQuery(query);
				xls = generateWB();
			}
		} catch (SQLException e) {
			log.error("# Erro ao consultar datasets: " + e.getMessage());
			e.printStackTrace();
		} finally {
			this.close(rs, stmt, con);
		}
		return xls;
	}

	private void close(ResultSet rs, Statement stmt, Connection con) {
		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (con != null)
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void makeQuery(String process, String detalhado, String consultaAbertura, String dtAbertura, String dtFinal, 
			String consultaVencimento, String dtVencimentoIni, String dtVencimentoEnd, String area, String processWorkflow,
			String openUser, String currentUser){

		log.info("#================ make query sql ================");
		log.info("#process: " + process);
		log.info("#detalhado: " + detalhado);
		log.info("#consultaAbertura: " + consultaAbertura);
		log.info("#dtAbertura: " + dtAbertura);
		log.info("#dtFinal: " + dtFinal);
		log.info("#consultaVencimento: " + consultaVencimento);
		log.info("#dtVencimentoIni: " + dtVencimentoIni);
		log.info("#dtVencimentoEnd: "+ dtVencimentoEnd);
		log.info("#area: " + area);
		log.info("#processWorkflow: " + processWorkflow);
		log.info("#openUser: " + openUser);
		log.info("#currentUser: " + currentUser);


		StringBuilder sql = new StringBuilder();

		sql.append("SELECT p.NUM_PROCES,"); 
		sql.append("nf.documentid, ");
		sql.append("nf.filial, ");
		sql.append("DATE_FORMAT(t.dat_fim_praz,'%d/%m/%y') as dataPrazo, ");
		sql.append("CAST(sec_to_time(T.NUM_HORA_FIM_PRAZ) as char) as horaPrazo, ");
		sql.append("CAST(sec_to_time(e.NUM_HRS_PRAZ) as char) as prazoSLA, ");
		sql.append("nf.complemento, ");
		sql.append("nf.version, ");
		sql.append("nf.tabela, ");
		sql.append("nf.solicitante, ");
		//sql.append("p.COD_MATR_REQUISIT, "); 
		//sql.append("t.CD_MATRICULA, "); 
		//sql.append("e.COD_DEF_PROCES, "); 
		sql.append("e.DES_ESTADO, "); 
		//sql.append("t.IDI_STATUS, "); 
		sql.append("d.DES_DEF_PROCES as COD_DEF_PROCES, ");
		sql.append("SUBSTRING_INDEX(d.cod_categ, '.', -1) as COD_CATEG, ");
		sql.append("requisitante_name.full_name as COD_MATR_REQUISIT, ");
		sql.append("coalesce(responsavel_name.full_name,t.cd_matricula) as CD_MATRICULA, ");
		sql.append("CASE ");
		sql.append("WHEN (t.idi_status is null and h.dat_movto is not null ) THEN 'Concluído' ");
		sql.append("WHEN t.idi_status = '0' THEN ");
		sql.append("( ");
		sql.append("case ");
		sql.append("when (now() > date_add(t.dat_fim_praz, interval T.NUM_HORA_FIM_PRAZ SECOND)) and (    LOCATE('Pool:',t.cd_matricula)  > 0 ) then 'Vencido não assumido' ");
		sql.append("when (now() < date_add(t.dat_fim_praz, interval T.NUM_HORA_FIM_PRAZ SECOND)) and (   t.dat_fim_praz = cast(now() as DATE)   ) then 'À vencer no dia' ");
		sql.append("when (now() > date_add(t.dat_fim_praz, interval T.NUM_HORA_FIM_PRAZ SECOND)) and (    LOCATE('Pool:',t.cd_matricula)  = 0 ) then 'Vencido' ");
		sql.append("when (now() < date_add(t.dat_fim_praz, interval T.NUM_HORA_FIM_PRAZ SECOND)) then 'À vencer' when t.dat_fim_praz is null then 'Aberto sem prazo' ");
		sql.append("else '' end ");
		sql.append(") ");
		sql.append("WHEN t.idi_status= '1' THEN 'Completou a tarefa' ");
		sql.append("WHEN t.idi_status = '2' THEN 'Concluído' ");
		sql.append("WHEN t.idi_status = '3' THEN 'Transferida' ");
		sql.append("WHEN t.idi_status = '4' THEN 'Cancelado' ");
		sql.append("ELSE '' ");
		sql.append("END as IDI_STATUS, ");
		//sql.append("d.COD_CATEG, "); 
		sql.append("DATE_FORMAT(t.DAT_CONCLUS_TAR,'%d/%m/%y') as DAT_CONCLUS_TAR,"); 
		sql.append("CAST(sec_to_time(t.NUM_HORA_CONCLUS_TAR) as char) as NUM_HORA_CONCLUS_TAR, "); 
		sql.append("t.DAT_INIC_PRAZ, "); 
		sql.append("DATE_FORMAT(h.DAT_MOVTO,'%d/%m/%y') as DAT_MOVTO,"); 
		sql.append(" h.HRA_MOVTO, "); 
		sql.append("t.DAT_FIM_PRAZ, "); 
		sql.append("CAST(sec_to_time(t.NUM_HORA_INIC_PRAZ) as char) as NUM_HORA_INIC_PRAZ,"); 
		sql.append("CAST(sec_to_time(sla.segundos) as char) as slaRealizado "); 
		//sql.append("1 as execucao "); 
		sql.append("FROM PROCES_WORKFLOW p "); 
		sql.append("INNER JOIN HISTOR_PROCES h ON (h.NUM_SEQ_CONVER = 0 and h.COD_EMPRESA = p.COD_EMPRESA and "); 
		sql.append("h.NUM_PROCES = p.NUM_PROCES) "); 
		sql.append("INNER JOIN ESTADO_PROCES e ON (e.idi_tip_bpmn not in (60,65,68) and p.COD_EMPRESA = e.COD_EMPRESA and "); 
		sql.append(" h.NUM_SEQ_ESTADO = e.NUM_SEQ and "); 
		sql.append("p.NUM_VERS = e.NUM_VERS and "); 
		sql.append(" p.COD_DEF_PROCES = e.COD_DEF_PROCES AND e.LOG_DECIS_AUTOM = false) "); 
		sql.append(" INNER JOIN DEF_PROCES d "); 
		sql.append("ON (p.COD_EMPRESA = d.COD_EMPRESA and "); 
		sql.append(" p.COD_DEF_PROCES = d.COD_DEF_PROCES) "); 
		sql.append("LEFT JOIN TAR_PROCES t "); 
		sql.append(" ON (p.COD_EMPRESA = t.COD_EMPRESA and "); 
		sql.append("p.NUM_PROCES = t.NUM_PROCES and "); 
		sql.append("h.NUM_SEQ_MOVTO = t.NUM_SEQ_MOVTO "); 
		sql.append("  and t.IDI_STATUS <> 3 "); 
		sql.append(" ) "); 
		sql.append("INNER JOIN HISTOR_PROCES data "); 
		sql.append(" ON (p.NUM_PROCES = data.NUM_PROCES and "); 
		sql.append("p.COD_EMPRESA = data.COD_EMPRESA and "); 
		sql.append("data.NUM_SEQ_MOVTO = 1) "); 
		sql.append("INNER JOIN ANEXO_PROCES a "); 
		sql.append("ON (p.NUM_PROCES = a.NUM_PROCES and "); 
		sql.append("p.COD_EMPRESA = a.COD_EMPRESA and "); 
		sql.append("a.NUM_SEQ_ANEXO = 1) "); 
		sql.append("                                                    LEFT OUTER JOIN "); 
		sql.append("                                                    nova_ficha");
		sql.append("                                                    nf on (nf.version = a.nr_versao and nf.documentid = a.nr_documento) "); 
		sql.append("                                                    LEFT OUTER JOIN (select distinct * from sla_em_segundos_atividades)  sla on (p.NUM_PROCES = sla.num_proces and h.NUM_SEQ_MOVTO = sla.num_seq_movto and sla.ativo = 1) "); 
		sql.append("                                                    left outer join fdn_usertenant requisitante on (requisitante.TENANT_ID = 1 and  requisitante.user_code = p.cod_matr_requisit ) ");
		sql.append("                                                    left outer join fdn_user requisitante_name on (requisitante_name.user_id = requisitante.user_tenant_id) ");
		sql.append("                                                    left outer join fdn_usertenant responsavel on (responsavel.TENANT_ID = 1 and  responsavel.user_code = t.cd_matricula ) ");
		sql.append("                                                    left outer join fdn_user responsavel_name on (responsavel_name.user_id = responsavel.user_tenant_id) ");
		sql.append("     where p.COD_EMPRESA = 1 "); 

		if(!process.equals("")){
			sql.append("and p.NUM_PROCES in("+process.toString()+") ");
		}

		if(consultaAbertura != null){
			sql.append("and h.DAT_MOVTO between STR_TO_DATE('"+dtAbertura+"', '%d/%m/%Y') and STR_TO_DATE('"+dtFinal+"', '%d/%m/%Y') ");
		}

		if(consultaVencimento != null){
			sql.append("and t.DAT_CONCLUS_TAR between STR_TO_DATE('"+dtVencimentoIni+"', '%d/%m/%Y') and STR_TO_DATE('"+dtVencimentoEnd+"', '%d/%m/%Y') ");
		}

		if(!processWorkflow.equals("")){
			try {
				sql.append("and d.DES_DEF_PROCES like '%"+java.net.URLDecoder.decode(processWorkflow, "UTF-8")+"%' ");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		if(!area.equals("")){
			try {
				sql.append("and d.COD_CATEG like '%"+java.net.URLDecoder.decode(area, "UTF-8")+"%' ");
			}catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		if(!openUser.equals("")){
			try {
				sql.append("and p.COD_MATR_REQUISIT = '"+java.net.URLDecoder.decode(openUser, "UTF-8")+"' ");
			}catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		if(!currentUser.equals("")){
			try {
				sql.append("and t.CD_MATRICULA = '"+java.net.URLDecoder.decode(currentUser, "UTF-8")+"' ");
			}catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		sql.append(" order by d.DES_DEF_PROCES ASC, p.NUM_PROCES ASC, h.NUM_SEQ_MOVTO ASC "); 

		log.info("#SQL: "+sql.toString());

		this.query = sql.toString();
	}

	public Workbook generateWB(){
		log.info("#create sheet");
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("SLA-Processos");
		String[] headers = new String[] {"Solicitação","Solicitação Origem","Data Abertura","Hora Abertura","Área","Processo",
				"Atividade","Cliente","Solicitante Registro","Solicitante","Responsável","Data Conclusão",
				"Hora Conclusão","Status","Data Prazo","Hora Prazo","SLA Previsto","SLA Realizado"};


		Row header = sheet.createRow(0);
		for(int rn=0; rn<headers.length; rn++) {
			header.createCell(rn).setCellValue(headers[rn]);
		}

		int row = 1;
		try {
			while(rs.next()) {
				Row dataRow = sheet.createRow(row);
				dataRow.createCell(0).setCellValue(Double.parseDouble(rs.getString("NUM_PROCES")));
				dataRow.createCell(1).setCellValue(rs.getString("complemento"));
				dataRow.createCell(2).setCellValue(rs.getString("DAT_MOVTO"));
				dataRow.createCell(3).setCellValue(rs.getString("HRA_MOVTO"));
				dataRow.createCell(4).setCellValue(rs.getString("COD_CATEG"));
				dataRow.createCell(5).setCellValue(rs.getString("COD_DEF_PROCES"));
				dataRow.createCell(6).setCellValue(rs.getString("DES_ESTADO"));
				dataRow.createCell(7).setCellValue(rs.getString("filial"));
				dataRow.createCell(8).setCellValue(rs.getString("COD_MATR_REQUISIT"));
				dataRow.createCell(9).setCellValue(rs.getString("solicitante"));
				dataRow.createCell(10).setCellValue(rs.getString("CD_MATRICULA"));
				dataRow.createCell(11).setCellValue(rs.getString("DAT_CONCLUS_TAR"));
				dataRow.createCell(12).setCellValue(rs.getString("NUM_HORA_CONCLUS_TAR"));
				dataRow.createCell(13).setCellValue(rs.getString("IDI_STATUS"));
				dataRow.createCell(14).setCellValue(rs.getString("dataPrazo"));
				dataRow.createCell(15).setCellValue(rs.getString("horaPrazo"));
				dataRow.createCell(16).setCellValue(rs.getString("prazoSLA"));
				dataRow.createCell(17).setCellValue(rs.getString("slaRealizado"));
				//dataRow.createCell(18).setCellValue(rs.getString("execucao"));
				row++;
			}
		} catch (Exception e) {
			log.error("Erro ao gerar relatorio xls: " + e.toString());
		}
		return wb;
	}

}
