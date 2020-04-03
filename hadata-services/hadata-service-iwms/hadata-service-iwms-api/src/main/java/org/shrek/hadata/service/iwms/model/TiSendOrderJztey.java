package org.shrek.hadata.service.iwms.model;


import lombok.Getter;
import lombok.Setter;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.*;


/**
 * @author chengjian
 */
@Getter
@Setter
@Table(name = "ti_send_order_jztey")
public class TiSendOrderJztey extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select next value for dbo.sequence_main_id")
    private String id;

    @Column(name = "master_id")
    private String masterId;

    @Column(name = "unit_in_code")
    private String unitInCode;

    private String salesman;

    @Column(name = "dep_in_code")
    private String depInCode;

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "sales_type")
    private String salesType;

    @Column(name = "dis_type")
    private String disType;

    @Column(name = "cost_price")
    private String costPrice;

    @Column(name = "call_state")
    private String callState;

    @Column(name = "return_cause")
    private String returnCause;

    @Column(name = "return_category")
    private String returnCategory;

    @Column(name = "mass_state")
    private String massState;

    @Column(name = "settlement_price")
    private String settlementPrice;

    @Column(name = "make_date")
    private String makeDate;

    @Column(name = "validity_date")
    private String validityDate;

    @Column(name = "box_number")
    private String boxNumber;

    @Column(name = "is_consign")
    private String isConsign = "N";
}