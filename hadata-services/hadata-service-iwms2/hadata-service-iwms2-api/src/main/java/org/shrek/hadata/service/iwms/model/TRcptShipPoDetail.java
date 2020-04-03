package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Data
@Table(name = "t_rcpt_ship_po_detail")
public class TRcptShipPoDetail extends BasicEntity {
    @Id
    @Column(name = "wh_id")
    private String whId;

    @Id
    @Column(name = "shipment_number")
    private String shipmentNumber;

    @Id
    @Column(name = "po_number")
    private String poNumber;

    @Id
    @Column(name = "line_number")
    private String lineNumber;

    @Id
    @Column(name = "item_number")
    private String itemNumber;

    @Id
    @Column(name = "schedule_number")
    private Integer scheduleNumber;

    @Column(name = "expected_qty")
    private Double expectedQty;

    @Column(name = "received_qty")
    private Double receivedQty;

    @Column(name = "reconciled_date")
    private Date reconciledDate;

    private String status;

    @Column(name = "hu_id")
    private String huId;

    @Column(name = "lot_number")
    private String lotNumber;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "stored_attribute_id")
    private Long storedAttributeId;

    @Column(name = "so_number")
    private String soNumber;

    @Column(name = "so_line_number")
    private String soLineNumber;
}