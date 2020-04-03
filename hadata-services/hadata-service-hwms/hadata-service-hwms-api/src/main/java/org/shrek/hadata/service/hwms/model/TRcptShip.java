package org.shrek.hadata.service.hwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Data
@Table(name = "t_rcpt_ship")
public class TRcptShip extends BasicEntity {
    @Id
    @Column(name = "wh_id")
    private String whId;

    @Id
    @Column(name = "shipment_number")
    private String shipmentNumber;

    @Column(name = "carrier_id")
    private Integer carrierId;

    @Column(name = "trailer_number")
    private String trailerNumber;

    @Column(name = "date_expected")
    private Date dateExpected;

    @Column(name = "date_received")
    private Date dateReceived;

    @Column(name = "date_shipped")
    private Date dateShipped;

    private String status;

    private String comments;

    @Column(name = "workers_assigned")
    private Integer workersAssigned;

    @Column(name = "pro_number")
    private String proNumber;

    /**
     * @return wh_id
     */
    public String getWhId() {
        return whId;
    }

}