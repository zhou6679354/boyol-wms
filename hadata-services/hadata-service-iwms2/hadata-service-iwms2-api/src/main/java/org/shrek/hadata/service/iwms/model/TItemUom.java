package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author chengjian
 */
@Data
@Table(name = "t_item_uom")
public class TItemUom extends BasicEntity {
    @Id
    @Column(name = "item_uom_id",insertable = false,updatable = false)
    private Integer itemUomId;

    @Id
    private String uom;

    @Column(name = "item_master_id")
    private Integer itemMasterId;

    @Column(name = "item_number")
    private String itemNumber;

    @Column(name = "wh_id")
    private String whId;

    @Column(name = "conversion_factor")
    private Double conversionFactor;

    @Column(name = "package_weight")
    private Double packageWeight;

    @Column(name = "units_per_layer")
    private Double unitsPerLayer;

    @Column(name = "layers_per_uom")
    private Double layersPerUom;

    @Column(name = "uom_weight")
    private Double uomWeight;

    private String pickable;

    @Column(name = "box_type")
    private Integer boxType;

    private Double length;

    private Double width;

    private Double height;

    @Column(name = "no_overhang_on_top")
    private Integer noOverhangOnTop;

    @Column(name = "stack_code")
    private Integer stackCode;

    private Integer batch;

    @Column(name = "use_orientation_data")
    private Integer useOrientationData;

    private Integer turnable;

    @Column(name = "on_bottom_ok")
    private Integer onBottomOk;

    @Column(name = "on_side_ok")
    private Integer onSideOk;

    @Column(name = "on_end_ok")
    private Integer onEndOk;

    @Column(name = "bottom_only")
    private Integer bottomOnly;

    @Column(name = "top_only")
    private Integer topOnly;

    @Column(name = "max_in_layer")
    private Integer maxInLayer;

    @Column(name = "max_support_weight")
    private Double maxSupportWeight;

    @Column(name = "stack_index")
    private Integer stackIndex;

    @Column(name = "container_value")
    private Double containerValue;

    @Column(name = "load_separately")
    private Integer loadSeparately;

    @Column(name = "nesting_height_increase")
    private Double nestingHeightIncrease;

    @Column(name = "nested_volume")
    private Double nestedVolume;

    @Column(name = "unit_volume")
    private Double unitVolume;

    private String pattern;

    private Integer priority;

    private String status;

    @Column(name = "uom_prompt")
    private String uomPrompt;

    @Column(name = "default_receipt_uom")
    private String defaultReceiptUom;

    @Column(name = "default_pick_uom")
    private String defaultPickUom;

    @Column(name = "class_id")
    private String classId;

    @Column(name = "pick_put_id")
    private String pickPutId;

    private String conveyable;

    @Column(name = "std_hand_qty")
    private Double stdHandQty;

    @Column(name = "min_hand_qty")
    private Double minHandQty;

    @Column(name = "max_hand_qty")
    private Double maxHandQty;

    @Column(name = "default_pick_area")
    private String defaultPickArea;

    @Column(name = "pick_location")
    private String pickLocation;

    @Column(name = "display_config")
    private String displayConfig;

    @Column(name = "vas_profile")
    private String vasProfile;

    @Column(name = "cartonization_flag")
    private String cartonizationFlag;

    private String gtin;

    @Column(name = "shippable_uom")
    private String shippableUom;

    @Column(name = "units_per_grab")
    private BigDecimal unitsPerGrab;

    @Column(name = "upright_only")
    private Integer uprightOnly;

}