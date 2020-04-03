package org.shrek.hadata.service.iwms.model;

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
 * @author rescom
 * @version 1.0
 * @date 2019年08月06日 11:00
 */
@Data
@Table(name = "tbl_fin_order_accept_notice")
public class TblFinOrderAcceptNotice extends BasicEntity {

    @Column(name = "wh_id")
    private String whse;


    @Column(name = "client_code")
    private String clientCode;


    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "accept_status")
    private String acceptStatus;

/*
    @Column(name = "accept_time")
    private String acceptTime;*/

}