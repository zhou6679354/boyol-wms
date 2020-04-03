package org.shrek.hadata.service.jztey.web.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月11日 12:47
 */
@JacksonXmlRootElement(localName ="root")
public class ResponseWrapper<T>  implements Serializable {
    
    @Setter
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "bill")
    List<T> list;
    public List<T> getList() {
        if(Optional.ofNullable(list).isPresent()==false) {
            list = Lists.newArrayList();
        }
        return list;
    }
}
