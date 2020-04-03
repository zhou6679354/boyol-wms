package org.shrek.hadata.service.hwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "t_tran_log")
public class TranLog extends BasicEntity {
    @Id
    @Column(name = "tran_log_id")
    private Integer tranLogId;
    @Column(name = "tran_type")
    private String tranType;

    @Column(name = "description")
    private String description;

    @Column(name = "start_tran_date")
    private Date startTranDate;

    @Column(name = "start_tran_time")
    private Date startTranTime;

    @Column(name = "end_tran_date")
    private Date endTranDate;

    @Column(name = "end_tran_time")
    private Date endTranTime;
    @Column(name = "employee_id")
    private String employeeId;
    @Column(name = "control_number")
    private String controlNumber;

    @Column(name = "line_number")
    private Integer lineNumber;

    @Column(name = "control_number_2")
    private String controlNumber2;

    @Column(name = "outside_id")
    private String outsideId;
    @Column(name = "wh_id")
    private String whId;

    @Column(name = "location_id")
    private String locationId;

    @Column(name = "hu_id")
    private String huId;

    @Column(name = "num_items")
    private Integer numItems;

    @Column(name = "item_number")
    private String itemNumber;

    @Column(name = "lot_number")
    private String lotNumber;
    @Column(name = "uom")
    private String uom;
    @Column(name = "tran_qty")
    private Double tranQty;

    @Column(name = "wh_id_2")
    private String whId2;

    @Column(name = "location_id_2")
    private String locationId2;

    @Column(name = "verify_status")
    private String verifyStatus;
    @Column(name = "employee_id_2")
    private String employeeId2;

    @Column(name = "routing_code")
    private Integer routingCode;

    @Column(name = "hu_id_2")
    private String huId2;

    @Column(name = "return_disposition")
    private Date returnDisposition;

    @Column(name = "elapsed_time")
    private Integer elapsedTime;

    @Column(name = "source_storage_type")
    private String sourceStorageType;
    @Column(name = "destination_storage_type")
    private String destinationStorageType;
    @Column(name = "stored_attribute_id")
    private Long storedAttributeId;

    @Column(name = "generic_attribute_1")
    private String genericAttribute1;

    @Column(name = "generic_attribute_2")
    private String genericAttribute2;
    @Column(name = "generic_attribute_3")
    private String genericAttribute3;

    @Column(name = "generic_attribute_4")
    private String genericAttribute4;
    @Column(name = "generic_attribute_5")
    private String genericAttribute5;

    @Column(name = "generic_attribute_6")
    private String genericAttribute6;
    @Column(name = "generic_attribute_7")
    private String genericAttribute7;

    @Column(name = "generic_attribute_8")
    private String genericAttribute8;
    @Column(name = "generic_attribute_9")
    private String genericAttribute9;

    @Column(name = "generic_attribute_10")
    private String genericAttribute10;
    @Column(name = "generic_attribute_11")
    private String genericAttribute11;

    @Column(name = "generic_text1")
    private String genericText1;
    @Column(name = "generic_text2")
    private String genericText2;

    @Column(name = "generic_text3")
    private String genericText3;
    @Column(name = "generic_text4")
    private String genericText4;

    @Column(name = "generic_text5")
    private String genericText5;
    @Column(name = "generic_float1")
    private Double genericFloat1;
    @Column(name = "generic_float2")
    private Double genericFloat2;
    @Column(name = "display_item_number")
    private String displayItemNumber;
    @Column(name = "client_code")
    private String clientCode;

    @Column(name = "trk_summary_id")
    private Integer trkSummaryId;
    @Column(name = "tran_log_holding_id")
    private Integer tranLogHolding_id;

}
