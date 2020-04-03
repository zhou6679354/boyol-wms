package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "t_order_detail")
public class TOrderDetail extends BasicEntity {
    @Id
    @Column(name = "order_detail_id", updatable = false)
    private Integer orderDetailId;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "item_master_id")
    private Integer itemMasterId;

    @Column(name = "wh_id")
    private String whId;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "line_number")
    private String lineNumber;

    @Column(name = "item_number")
    private String itemNumber;

    @Column(name = "bo_qty")
    private Double boQty;

    @Column(name = "bo_description")
    private String boDescription;

    @Column(name = "bo_weight")
    private Double boWeight;

    private Double qty;

    @Column(name = "afo_plan_qty")
    private Double afoPlanQty;

    @Column(name = "unit_pack")
    private String unitPack;

    @Column(name = "item_weight")
    private Double itemWeight;

    @Column(name = "item_tare_weight")
    private Double itemTareWeight;

    @Column(name = "haz_material")
    private String hazMaterial;

    @Column(name = "b_o_l_class")
    private String bOLClass;

    @Column(name = "b_o_l_line1")
    private String bOLLine1;

    @Column(name = "b_o_l_line2")
    private String bOLLine2;

    @Column(name = "b_o_l_line3")
    private String bOLLine3;

    @Column(name = "b_o_l_plac_code")
    private String bOLPlacCode;

    @Column(name = "b_o_l_plac_desc")
    private String bOLPlacDesc;

    @Column(name = "b_o_l_code")
    private String bOLCode;

    @Column(name = "qty_shipped")
    private Double qtyShipped;

    @Column(name = "line_type")
    private String lineType;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "stacking_seq")
    private String stackingSeq;

    @Column(name = "cust_part")
    private String custPart;

    @Column(name = "lot_number")
    private String lotNumber;

    @Column(name = "picking_flow")
    private String pickingFlow;

    @Column(name = "unit_weight")
    private Double unitWeight;

    @Column(name = "unit_volume")
    private Double unitVolume;

    @Column(name = "extended_weight")
    private Double extendedWeight;

    @Column(name = "extended_volume")
    private Double extendedVolume;

    @Column(name = "over_alloc_qty")
    private Double overAllocQty;

    @Column(name = "date_expected")
    private Date dateExpected;

    @Column(name = "order_uom")
    private String orderUom;

    @Column(name = "host_wave_id")
    private String hostWaveId;

    @Column(name = "tran_plan_qty")
    private Double tranPlanQty;

    @Column(name = "use_shippable_uom")
    private String useShippableUom;

    @Column(name = "unit_insurance_amount")
    private Double unitInsuranceAmount;

    @Column(name = "stored_attribute_id")
    private Long storedAttributeId;

    @Column(name = "hold_reason_id")
    private String holdReasonId;

    @Column(name = "po_number")
    private String poNumber;

    @Column(name = "po_line_number")
    private String poLineNumber;

    @Column(name = "po_item_number")
    private String poItemNumber;

    @Column(name = "cancel_flag")
    private String cancelFlag;

    @Column(name = "ori_line_number")
    private String oriLineNumber;

    private String zone;

    @Column(name = "pick_location")
    private String pickLocation;

    @Column(name = "hu_id")
    private String huId;

    @Column(name = "storage_type")
    private Long storageType;
    @Column(name = "production_date")
    private Date productionDate;
    @Column(name = "expiration_date")
    private Date expirationDate;
    @Column(name = "measuring_number")
    private String measuringNumber;

    @Column(name = "color_number")
    private String colorNumber;
    @Column(name = "st_code")
    private String stCode;
    @Column(name = "st_name")
    private String stName;
}