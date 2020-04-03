package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "ti_receive_order_detail")
public class TiReceiveOrderDetail extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select next value for dbo.sequence_main_id")
    private String id;

    @Id
    @Column(name = "master_id")
    private String masterId;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "product_desc")
    private String productDesc;

    @Column(name = "product_unit")
    private String productUnit;

    @Column(name = "product_batch")
    private String productBatch;

    @Column(name = "product_make_date")
    private String productMakeDate;

    @Column(name = "product_expiration_date")
    private String productExpirationDate;

    @Column(name = "product_expiry_date")
    private String productExpiryDate;

    @Column(name = "product_num")
    private String productNum;

    @Column(name = "product_box_num")
    private String productBoxNum;

    @Column(name = "product_bulk_num")
    private String productBulkNum;

    @Column(name = "product_arrival_date")
    private String productArrivalDate;

    @Column(name = "product_unit_price")
    private String productUnitPrice;

    @Column(name = "product_total_price")
    private String productTotalPrice;

    @Column(name = "product_quality")
    private String productQuality;

    @Column(name = "product_back_reason")
    private String productBackReason;

    private String remark;

    @Column(name = "active_flag")
    private String activeFlag;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "last_updated_by")
    private String lastUpdatedBy;

    @Column(name = "last_updated_date")
    private Date lastUpdatedDate;

}