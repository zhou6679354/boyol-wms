package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "t_work_q")
public class TWork extends BasicEntity {

	@Id
    @Column(name = "work_q_id")
    private String workQId;

    @Column(name = "work_type")
    private String workType;

    @Column(name = "description")
    private String description;

    @Column(name = "pick_ref_number")
    private String pickRefNumber;

    @Column(name = "priority")
    private String priority;

    @Column(name = "date_due")
    private Date dateDue;

    @Column(name = "time_due")
    private Date timeDue;

    @Column(name = "item_number")
    private String itemNumber;

    private String itemName;
    private String clientCode;
    @Column(name = "wh_id")
    private String whId;

    @Column(name = "location_id")
    private String locationId;

    @Column(name = "from_location_id")
    private String fromLocationId;
    @Column(name = "work_status")
    private String workStatus;
    private String stdQtyUom;
    private String uom;
    @Column(name = "qty")
    private double qty;

    @Column(name = "workers_required")
    private int workersRequired;

    @Column(name = "workers_assigned")
    private int workersAssigned;

    @Column(name = "zone")
    private String zone;

    @Column(name = "employee_id")
    private String employeeId;
    @Column(name = "datetime_stamp")
    private Date datetimeStamp;

    @Column(name = "lot_number")
    private String lotNumber;

    @Column(name = "expiration_date")
    private Date expirationDate;
    @Column(name = "stored_attribute_id")
    private int storedAttributeId;
    @Column(name = "hu_id")
    private String huId;

    @Column(name = "from_qty")
    private float from_qty;

    @Column(name = "mantissa_location_id")
    private String mantissaLlocationId;

    @Column(name = "mantissa_hu_id")
    private String mantissaHuId;

    @Column(name = "mantissa_qty")
    private float mantissaQty;

    @Column(name = "initial_sto_id")
    private long initial_sto_id;

    @Column(name = "shipment_number")
    private String shipmentNumber;
    @Column(name = "control_number")
    private String controlNumber;

    @Column(name = "storage_type")
    private long storageType;
    @Column(name = "tran_log_id")
    private long tranLogId;
    @Column(name = "des_hu_id")
    private String desHuId;
}