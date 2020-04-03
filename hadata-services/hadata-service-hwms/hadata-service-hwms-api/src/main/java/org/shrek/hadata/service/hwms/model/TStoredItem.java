package org.shrek.hadata.service.hwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "t_stored_item")
public class TStoredItem extends BasicEntity {
    @Column(name = "sto_id")
    private Long stoId;

    private Integer sequence;

    @Column(name = "item_number")
    private String itemNumber;

    @Column(name = "actual_qty")
    private Double actualQty;

    @Column(name = "unavailable_qty")
    private Double unavailableQty;

    private String status;

    @Column(name = "wh_id")
    private String whId;

    @Column(name = "location_id")
    private String locationId;

    @Column(name = "fifo_date")
    private Date fifoDate;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "reserved_for")
    private String reservedFor;

    @Column(name = "lot_number")
    private String lotNumber;

    @Column(name = "inspection_code")
    private String inspectionCode;

    private Long type;

    @Column(name = "put_away_location")
    private String putAwayLocation;

    @Column(name = "stored_attribute_id")
    private Long storedAttributeId;

    @Column(name = "hu_id")
    private String huId;

    @Column(name = "shipment_number")
    private String shipmentNumber;

}