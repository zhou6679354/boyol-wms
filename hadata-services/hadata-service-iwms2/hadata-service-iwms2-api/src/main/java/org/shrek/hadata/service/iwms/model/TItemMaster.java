package org.shrek.hadata.service.iwms.model;

import com.google.common.collect.Lists;
import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * @author chengjian
 */
@Data
@Table(name = "t_item_master")
public class TItemMaster extends BasicEntity {
    @Id
    @Column(name = "item_master_id", updatable = false)
    private Integer itemMasterId;

    @Column(name = "item_number")
    private String itemNumber;

    private String description;

    private String uom;

    @Column(name = "inventory_type")
    private String inventoryType;

    @Column(name = "shelf_life")
    private Integer shelfLife;

    @Column(name = "alt_item_number")
    private String altItemNumber;

    @Column(name = "commodity_code")
    private String commodityCode;

    @Column(name = "nafta_pref_criteria")
    private String naftaPrefCriteria;

    @Column(name = "nafta_producer")
    private String naftaProducer;

    @Column(name = "nafta_net_cost")
    private String naftaNetCost;

    private Double price;

    @Column(name = "std_hand_qty")
    private Double stdHandQty;

    @Column(name = "std_qty_uom")
    private String stdQtyUom;

    @Column(name = "inspection_code")
    private String inspectionCode;

    @Column(name = "serial_control")
    private String serialControl;

    @Column(name = "lot_control")
    private String lotControl;

    @Column(name = "wh_id")
    private String whId;

    @Column(name = "reorder_point")
    private Double reorderPoint;

    @Column(name = "reorder_qty")
    private Double reorderQty;

    @Column(name = "cycle_count_class")
    private String cycleCountClass;

    @Column(name = "last_count_date")
    private Date lastCountDate;

    @Column(name = "class_id")
    private String classId;

    @Column(name = "pick_location")
    private String pickLocation;

    @Column(name = "stacking_seq")
    private String stackingSeq;

    @Column(name = "comment_flag")
    private String commentFlag;

    @Column(name = "ver_flag")
    private String verFlag;

    private String upc;

    @Column(name = "unit_weight")
    private Double unitWeight;

    @Column(name = "tare_weight")
    private Double tareWeight;

    @Column(name = "haz_material")
    private String hazMaterial;

    @Column(name = "inv_cat")
    private String invCat;

    @Column(name = "inv_class")
    private String invClass;

    @Column(name = "unit_volume")
    private Double unitVolume;

    @Column(name = "nested_volume")
    private Double nestedVolume;

    @Column(name = "xdock_profile_id")
    private String xdockProfileId;

    @Column(name = "pick_put_id")
    private String pickPutId;

    private Double length;

    private Double width;

    private Double height;

    @Column(name = "sample_rate")
    private Integer sampleRate;

    @Column(name = "compatibility_id")
    private Integer compatibilityId;

    @Column(name = "commodity_type_id")
    private Integer commodityTypeId;

    @Column(name = "freight_class_id")
    private Integer freightClassId;

    @Column(name = "audit_required")
    private String auditRequired;

    @Column(name = "msds_url")
    private String msdsUrl;

    @Column(name = "expiration_date_control")
    private String expirationDateControl;

    @Column(name = "ucc_company_prefix")
    private String uccCompanyPrefix;

    @Column(name = "attribute_collection_id")
    private Integer attributeCollectionId;

    @Column(name = "display_item_number")
    private String displayItemNumber;

    @Column(name = "client_code")
    private String clientCode;

    @Column(name = "production_date_control")
    private String productionDateControl;

    @Column(name = "lot_status_control")
    private String lotStatusControl;

    @Transient
    List<TClient> clients=Lists.newArrayList();

    @Transient
    List<TItemUom> uoms= Lists.newArrayList();

}