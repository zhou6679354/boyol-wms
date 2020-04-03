package org.shrek.hadata.service.jztey.web.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月03日 19:21
 */
@Data
public class RequestMaterial implements Serializable {
    @JacksonXmlProperty(localName ="LASTMODIFYTIME")
    String lastmodifytime;
    @JacksonXmlProperty(localName ="PLATPRODNO")
    String platprodno;
    @JacksonXmlProperty(localName ="PLATPRODNAME")
    String platprodname;
    @JacksonXmlProperty(localName ="PRODID")
    String prodid;
    @JacksonXmlProperty(localName ="PRODNO")
    String prodno;
    @JacksonXmlProperty(localName ="PRODNAME")
    String prodname;
    @JacksonXmlProperty(localName ="BRANCHID")
    String branchid;
}
