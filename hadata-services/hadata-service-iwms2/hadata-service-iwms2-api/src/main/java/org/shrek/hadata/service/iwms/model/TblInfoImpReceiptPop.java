package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "tbl_inf_imp_receipt_pop")
public class TblInfoImpReceiptPop extends BasicEntity {
    @Id
    private Integer id;

    @Column(name = "CustomeOrder")
    private String customeOrder;

    @Column(name = "CargoCode")
    private String cargoCode;

    @Column(name = "CargoName")
    private String cargoName;

    @Column(name = "CommodityCode")
    private String commodityCode;

    @Column(name = "Departmant")
    private String departmant;

    @Column(name = "Num")
    private Integer num;

    @Column(name = "InBoundTime")
    private Date inBoundTime;

    @Column(name = "GiftsSource")
    private Integer giftsSource;

    @Column(name = "CreateTime")
    private Date createTime;

    @Column(name = "wh_id")
    private String whId;

    @Column(name = "shipment_number")
    private String shipmentNumber;

    @Column(name = "proc_flag")
    private String procFlag;

}
