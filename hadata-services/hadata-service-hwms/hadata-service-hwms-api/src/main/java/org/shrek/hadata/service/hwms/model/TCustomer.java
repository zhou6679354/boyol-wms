package org.shrek.hadata.service.hwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "t_customer")
public class TCustomer extends BasicEntity {
    @Id
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "customer_code")
    private String customerCode;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_addr1")
    private String customerAddr1;

    @Column(name = "customer_addr2")
    private String customerAddr2;

    @Column(name = "customer_addr3")
    private String customerAddr3;

    @Column(name = "customer_city")
    private String customerCity;

    @Column(name = "customer_state")
    private String customerState;

    @Column(name = "customer_zip")
    private String customerZip;

    @Column(name = "customer_country_code")
    private String customerCountryCode;

    @Column(name = "customer_country_name")
    private String customerCountryName;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_category")
    private String customerCategory;

    @Column(name = "customer_priority")
    private String customerPriority;

    @Column(name = "customer_ship_method")
    private String customerShipMethod;

    @Column(name = "customer_route")
    private String customerRoute;

    @Column(name = "wh_id")
    private String whId;

    @Column(name = "pick_put_id")
    private String pickPutId;

    private String linkman;

    @Column(name = "lot_qty_limit")
    private Long lotQtyLimit;

    @Column(name = "source_code")
    private String sourceCode;

}