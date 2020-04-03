package org.shrek.hadata.service.jztey.web.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月11日 23:51
 */
@Data
public class ResponseStoreInfo implements Serializable {
	@JacksonXmlProperty(localName = "Platprodno")
	String itemNumber;
	@JacksonXmlProperty(localName = "Lotno")
	String lotNumber;
	@JacksonXmlProperty(localName = "Prodstatus")
	String prodStatus;
	@JacksonXmlProperty(localName = "Manudate")
	String manuDate;
	@JacksonXmlProperty(localName = "Expirydate")
	String expiryDate;
	@JacksonXmlProperty(localName = "Whsename")
	String whseName;
	@JacksonXmlProperty(localName = "Qty")
	String qty;
	@JacksonXmlProperty(localName = "Storeid")
	String longClientCode;
	@JacksonXmlProperty(localName = "Storename")
	String storeName;
	@JacksonXmlProperty(localName = "Branchid")
	String extCode;
	@JacksonXmlProperty(localName = "LastModifyTime")
	String fifoDate;
	@JacksonXmlProperty(localName = "PLATNAME")
	String platname;
}
