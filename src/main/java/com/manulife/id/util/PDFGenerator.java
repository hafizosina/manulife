package com.manulife.id.util;

import com.manulife.id.constant.ResponseCode;
import com.manulife.id.exception.ProcessException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
@Slf4j
public class PDFGenerator {


    public static ByteArrayInputStream generateJasperFile(String template, Map<String, Object> data, JRBeanCollectionDataSource dataSource) {

        try{

            InputStream in = new ClassPathResource(template).getInputStream();

            if (in == null) {
                log.error("Failed to load template: " + template);
                throw new ProcessException("Failed to Load template", ResponseCode.FAILED_TO_LOAD_PDF_TEMPLATE);
            } else {
                log.info(("Template loaded successfully"));
            }

            JasperGenerator jasperGenerator = new JasperGenerator(in, data, dataSource);
            ByteArrayOutputStream out = jasperGenerator.outPdf();
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ProcessException("Cannot Generate PDF", ResponseCode.GENERAL_ERROR);
        }

    }

}
