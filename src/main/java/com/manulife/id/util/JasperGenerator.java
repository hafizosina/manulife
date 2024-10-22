package com.manulife.id.util;

import com.manulife.id.exception.ProcessException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class JasperGenerator {

	private List<JasperPrint> jasperPrintList;
	private ByteArrayOutputStream out;

	Map<String, Object> data;

	@Autowired
	public JasperGenerator()  {
		this.out = new ByteArrayOutputStream();
		try {
			this.jasperPrintList = new ArrayList<>();
		} catch (Exception e) {
			log.error("error", e);
			log.error(e.getMessage());
		}
	}

	public JasperGenerator(InputStream jrxml, Map<String, Object> data, JRDataSource conn) {
		this.out = new ByteArrayOutputStream();
		try {
			this.jasperPrintList = new ArrayList<>();
			JasperReport jasperReport = JasperCompileManager.compileReport(jrxml);
			constructJasperPrint(jasperReport, data, conn);
		} catch (JRException e) {
			log.error(e.getMessage());
		}
		this.data = data;
	}

	public void constructJasperPrint(JasperReport compiledReport, Map<String, Object> data, Object conn)
			throws JRException {
		JasperPrint jasperPrint = null;

		if (conn != null) {
			if (conn instanceof JRDataSource)
				jasperPrint = JasperFillManager.fillReport(compiledReport, data, (JRDataSource) conn);
			else if (conn instanceof Connection)
				jasperPrint = JasperFillManager.fillReport(compiledReport, data, (Connection) conn);
			else
				throw new JRException("Invalid type of object conn.");

		} else {
			jasperPrint = JasperFillManager.fillReport(compiledReport, data);
		}
		this.jasperPrintList.add(jasperPrint);
	}

	public ByteArrayOutputStream outPdf() throws ProcessException {
		try {
			this.out = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(SimpleExporterInput.getInstance((this.jasperPrintList)));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(this.out));
			exporter.exportReport();
		} catch (JRException e) {
			log.error(e.getMessage());
		}
		return out;
	}

	public String toBase64() throws ProcessException {
		return Base64.getEncoder().encodeToString(out.toByteArray());
	}

	public byte[] toByte() throws ProcessException {
		return out.toByteArray();
	}
}
