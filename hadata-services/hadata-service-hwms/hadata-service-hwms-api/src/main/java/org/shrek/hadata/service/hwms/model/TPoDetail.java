package org.shrek.hadata.service.hwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;


/**
 * 入库单明细
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年08月07日 11:00
 */
@Data
@Table(name = "t_po_detail")
public class TPoDetail extends BasicEntity {
    @Id
    @Column(name = "po_number")
    private String poNumber;

    @Id
    @Column(name = "line_number")
    private String lineNumber;

    @Id
    @Column(name = "schedule_number")
    private Integer scheduleNumber;

    @Id
    @Column(name = "wh_id")
    private String whId;

    @Column(name = "item_number")
    private String itemNumber;

    private Double qty;

    @Column(name = "vendor_item_number")
    private String vendorItemNumber;

    @Column(name = "delivery_date")
    private Date deliveryDate;

    private String originator;

    @Column(name = "order_uom")
    private String orderUom;

    @Column(name = "special_processing")
    private String specialProcessing;

    @Column(name = "location_id")
    private String locationId;

    @Column(name = "distro_process")
    private String distroProcess;

    @Column(name = "vas_profile_id")
    private Integer vasProfileId;

    @Column(name = "stored_attribute_id")
    private Long storedAttributeId;

    @Column(name = "earliest_ship_date")
    private Date earliestShipDate;

    @Column(name = "latest_ship_date")
    private Date latestShipDate;

    @Column(name = "earliest_delivery_date")
    private Date earliestDeliveryDate;

    @Column(name = "latest_delivery_date")
    private Date latestDeliveryDate;

    @Column(name = "lot_number")
    private String lotNumber;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "so_number")
    private String soNumber;

    @Column(name = "so_line_number")
    private String soLineNumber;

    @Transient
    private Double actualQty;

    @Transient
    private String lotNumber2;

    @Transient
    private Date expirationDate2;
    @Transient
    private Double price;
    @Transient
    private Double price2;
}