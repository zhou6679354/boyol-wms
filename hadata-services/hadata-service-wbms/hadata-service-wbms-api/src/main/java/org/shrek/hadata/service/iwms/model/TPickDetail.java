package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "t_pick_detail")
public class TPickDetail extends BasicEntity {
    @Id
    @Column(name = "pick_id")
    private Integer pickId;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "line_number")
    private String lineNumber;

    @Column(name = "type")
    private String type;

    @Column(name = "uom")
    private String uom;

    @Column(name = "work_q_id")
    private String workQid;

    @Column(name = "work_type")
    private String workType;

    @Column(name = "status")
    private String status;

    @Column(name = "item_number")
    private String itemNumber;

    @Column(name = "lot_number")
    private String lotNumber;

    @Column(name = "unplanned_quantity")
    private Double unplannedQuantity;

    @Column(name = "planned_quantity")
    private Double plannedQuantity;

    @Column(name = "picked_quantity")
    private Double pickedQuantity;

    @Column(name = "staged_quantity")
    private Double stagedQuantity;

    @Column(name = "loaded_quantity")
    private Double loadedQuantity;

    @Column(name = "packed_quantity")
    private Double packedQuantity;

    @Column(name = "shipped_quantity")
    private Double shippedQuantity;

    @Column(name = "pick_location")
    private String pickLocation;

    @Column(name = "picking_flow")
    private String pickingFlow;

    @Column(name = "staging_location")
    private String stagingLocation;

    @Column(name = "zone")
    private String zone;

    @Column(name = "wave_id")
    private String waveId;

    @Column(name = "load_id")
    private String loadId;

    @Column(name = "load_sequence")
    private Integer loadSequence;

    @Column(name = "stop_id")
    private String stopId;

    @Column(name = "container_id")
    private String containerId;

    @Column(name = "pick_category")
    private String pickCategory;

    @Column(name = "user_assigned")
    private String userAssigned;

    @Column(name = "bulk_pick_flag")
    private String bulkPickFlag;

    @Column(name = "stacking_sequence")
    private Integer stackingSequence;

    @Column(name = "pick_area")
    private String pickArea;

    @Column(name = "wh_id")
    private String whId;

    @Column(name = "cartonization_batch_id")
    private String cartonizationBatchId;

    @Column(name = "manifest_batch_id")
    private String manifestBatchId;

    @Column(name = "stored_attribute_id")
    private Long storedAttributeId;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "before_pick_rule")
    private String beforePickRule;

    @Column(name = "during_pick_rule")
    private String duringPickRule;

    @Column(name = "pick_location_change_date")
    private Date pickLocationChangeDate;

    @Column(name = "hold_reason_id")
    private String holdReasonId;

    @Column(name = "allocated_qty")
    private Double allocatedQty;

    @Column(name = "hu_id")
    private String huId;

    @Column(name = "storage_type")
    private Long storageType;


}
