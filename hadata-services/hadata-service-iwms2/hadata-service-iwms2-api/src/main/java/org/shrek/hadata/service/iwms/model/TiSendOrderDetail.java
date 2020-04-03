package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author chengjian
 */
@Data
@Table(name = "ti_send_order_detail")
public class TiSendOrderDetail extends BasicEntity {
    @Id
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

    @Column(name = "product_num")
    private String productNum;

    @Column(name = "product_box_num")
    private String productBoxNum;

    @Column(name = "product_bulk_num")
    private String productBulkNum;

    @Column(name = "product_unit_price")
    private String productUnitPrice;

    @Column(name = "product_total_price")
    private String productTotalPrice;

    @Column(name = "product_batch_ask")
    private String productBatchAsk;

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