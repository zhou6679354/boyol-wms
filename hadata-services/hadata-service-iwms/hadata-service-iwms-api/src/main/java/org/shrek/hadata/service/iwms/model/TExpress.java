package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "t_express")
public class TExpress extends BasicEntity {

    @Id
    @Column(name = "express_id", updatable = false)
    private Integer expressId;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "shipper_code")
    private String shipperCode;

    @Column(name = "logistic_code")
    private String logisticCode;

    @Column(name = "sequence_id")
    private Integer sequenceId;

    @Column(name = "accept_station")
    private String acceptStation;

    @Column(name = "accept_time")
    private String acceptTime;

    @Column(name = "remark")
    private String remark;




}