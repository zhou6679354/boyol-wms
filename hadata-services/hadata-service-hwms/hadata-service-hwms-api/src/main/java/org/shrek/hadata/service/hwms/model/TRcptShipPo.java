package org.shrek.hadata.service.hwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "t_rcpt_ship_po")
public class TRcptShipPo extends BasicEntity {
    @Id
    @Column(name = "wh_id")
    private String whId;

    @Id
    @Column(name = "shipment_number")
    private String shipmentNumber;

    @Id
    @Column(name = "po_number")
    private String poNumber;

    @Column(name = "cases_expected")
    private Double casesExpected;

    @Column(name = "cases_received")
    private Double casesReceived;

    @Column(name = "open_to_buy_date")
    private Date openToBuyDate;
}