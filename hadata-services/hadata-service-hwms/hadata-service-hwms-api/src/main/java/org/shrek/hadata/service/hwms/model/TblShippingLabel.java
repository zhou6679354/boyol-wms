package org.shrek.hadata.service.hwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Data
@Table(name = "tbl_shipping_label")
public class TblShippingLabel extends BasicEntity {
    @Id
    @Column(name = "carton_number")
    private String cartonNumber;

    @Id
    @Column(name = "wh_id")
    private String whId;

    @Column(name = "ship_label_barcode")
    private String shipLabelBarcode;

    private String route;

    @Column(name = "carton_type")
    private String cartonType;

    @Column(name = "origal_carton_type")
    private String origalCartonType;

    private String carrier;

    @Column(name = "logical_weight")
    private Double logicalWeight;

    @Column(name = "actual_weight")
    private Double actualWeight;

    @Column(name = "origal_actual_weight")
    private Double origalActualWeight;

    private String status;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "create_time")
    private Date createTime;

    private String remark;

    @Column(name = "weight_time")
    private Date weightTime;

    @Column(name = "weight_status")
    private String weightStatus;

    @Column(name = "is_nts_carton")
    private String isNtsCarton;

    @Column(name = "delivery_number")
    private String deliveryNumber;

    @Column(name = "delivery_date")
    private Date deliveryDate;

    @Column(name = "loaded_date")
    private Date loadedDate;

    @Column(name = "confirm_date")
    private Date confirmDate;

    @Column(name = "is_pe_carton")
    private String isPeCarton;

    @Column(name = "relate_carton_number")
    private String relateCartonNumber;

    @Column(name = "user_assigned")
    private String userAssigned;

    @Column(name = "is_shutoff")
    private String isShutoff;

    @Column(name = "line_number")
    private Integer lineNumber;


}