package org.shrek.hadata.service.jztey.web.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年05月03日 19:26
 */
@Data
@JacksonXmlRootElement(localName ="wrapper")
public class RequestWrapper<T>  implements Serializable {
    @JacksonXmlElementWrapper(localName="root")
    @JacksonXmlProperty(localName ="root")
    List<T> list;
}
