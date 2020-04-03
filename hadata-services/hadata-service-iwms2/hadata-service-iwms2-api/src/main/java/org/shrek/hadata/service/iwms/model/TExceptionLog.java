package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "t_exception_log")
public class TExceptionLog extends BasicEntity {
    @Column(name = "exception_id")
    private Integer exceptionId;

    @Column(name = "tran_type")
    private String tranType;

    private String description;

    @Column(name = "exception_date")
    private Date exceptionDate;

    @Column(name = "exception_time")
    private Date exceptionTime;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "wh_id")
    private String whId;

    @Column(name = "suggested_value")
    private String suggestedValue;

    @Column(name = "entered_value")
    private String enteredValue;

    @Column(name = "location_id")
    private String locationId;

    @Column(name = "item_number")
    private String itemNumber;

    @Column(name = "lot_number")
    private String lotNumber;

    private Double quantity;

    @Column(name = "hu_id")
    private String huId;

    @Column(name = "load_id")
    private String loadId;

    @Column(name = "control_number")
    private String controlNumber;

    @Column(name = "line_number")
    private String lineNumber;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "error_message")
    private String errorMessage;

    private String status;

    private Long type;

    @Column(name = "po_number")
    private String poNumber;

    @Column(name = "after_lot_number")
    private String afterLotNumber;

    @Column(name = "before_expiration_date")
    private Date beforeExpirationDate;

    @Column(name = "after_expiration_date")
    private Date afterExpirationDate;

    @Column(name = "stored_attribute_id2")
    private Long storedAttributeId2;

    @Column(name = "stored_attribute_id")
    private Long storedAttributeId;

   }