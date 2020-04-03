package org.shrek.hadata.service.hwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "t_receipt")
public class TReceipt extends BasicEntity {
    @Id
    @Column(name = "receipt_identity")
    private Integer receiptIdentity;

    @Column(name = "receipt_id")
    private String receiptId;

    @Column(name = "vendor_code")
    private String vendorCode;

    @Column(name = "po_number")
    private String poNumber;

    @OrderBy
    @Column(name = "receipt_date")
    private Date receiptDate;

    @Column(name = "scac_code")
    private String scacCode;

    private String status;

    @Column(name = "item_number")
    private String itemNumber;

    @Column(name = "lot_number")
    private String lotNumber;

    @Column(name = "line_number")
    private String lineNumber;

    @Column(name = "schedule_number")
    private Integer scheduleNumber;

    @Column(name = "qty_received")
    private Double qtyReceived;

    @Column(name = "qty_damaged")
    private Double qtyDamaged;

    @Column(name = "hu_id")
    private String huId;

    @Column(name = "pack_slip")
    private String packSlip;

    @Column(name = "fork_id")
    private String forkId;

    @Column(name = "tran_status")
    private String tranStatus;

    @Column(name = "receipt_uom")
    private String receiptUom;

    @Column(name = "shipment_number")
    private String shipmentNumber;

    @Column(name = "wh_id")
    private String whId;

    @Column(name = "stored_attribute_id")
    private Integer storedAttributeId;

    @Column(name = "hold_reason_id")
    private String holdReasonId;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "cabinet_number")
    private String cabinetNumber;

    @Column(name = "storage_type")
    private String storageType;

    @Column(name = "location_id")
    private String locationId;

    @Column(name = "box_num")
    private Double boxNum;

    @Column(name = "bulk_num")
    private Double bulkNum;


}