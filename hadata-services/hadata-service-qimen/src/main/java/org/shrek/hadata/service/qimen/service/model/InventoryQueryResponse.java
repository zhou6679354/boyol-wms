package org.shrek.hadata.service.qimen.service.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.qimen.api.QimenResponse;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;
import lombok.Data;

import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年12月03日 16:40
 */
@Data
@JacksonXmlRootElement(localName = "InventoryQueryResponse")
public class InventoryQueryResponse  extends QimenResponse {
    //@JacksonXmlElementWrapper(localName="items")
    //@JacksonXmlProperty(localName ="item")
    @ApiListField("items")
    @ApiField("item")
    private List<com.qimen.api.response.InventoryQueryResponse.Item> items;
    @JacksonXmlElementWrapper(localName="items",useWrapping = true)
    @JacksonXmlProperty(localName ="item")
    public List<com.qimen.api.response.InventoryQueryResponse.Item> getItems()
    {
        return this.items;
    };
}
