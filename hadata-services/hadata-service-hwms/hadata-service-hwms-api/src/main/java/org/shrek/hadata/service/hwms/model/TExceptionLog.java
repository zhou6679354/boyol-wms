package org.shrek.hadata.service.hwms.model;

import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

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

    /**
     * @return exception_id
     */
    public Integer getExceptionId() {
        return exceptionId;
    }

    /**
     * @param exceptionId
     */
    public void setExceptionId(Integer exceptionId) {
        this.exceptionId = exceptionId;
    }

    /**
     * @return tran_type
     */
    public String getTranType() {
        return tranType;
    }

    /**
     * @param tranType
     */
    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return exception_date
     */
    public Date getExceptionDate() {
        return exceptionDate;
    }

    /**
     * @param exceptionDate
     */
    public void setExceptionDate(Date exceptionDate) {
        this.exceptionDate = exceptionDate;
    }

    /**
     * @return exception_time
     */
    public Date getExceptionTime() {
        return exceptionTime;
    }

    /**
     * @param exceptionTime
     */
    public void setExceptionTime(Date exceptionTime) {
        this.exceptionTime = exceptionTime;
    }

    /**
     * @return employee_id
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId
     */
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return wh_id
     */
    public String getWhId() {
        return whId;
    }

    /**
     * @param whId
     */
    public void setWhId(String whId) {
        this.whId = whId;
    }

    /**
     * @return suggested_value
     */
    public String getSuggestedValue() {
        return suggestedValue;
    }

    /**
     * @param suggestedValue
     */
    public void setSuggestedValue(String suggestedValue) {
        this.suggestedValue = suggestedValue;
    }

    /**
     * @return entered_value
     */
    public String getEnteredValue() {
        return enteredValue;
    }

    /**
     * @param enteredValue
     */
    public void setEnteredValue(String enteredValue) {
        this.enteredValue = enteredValue;
    }

    /**
     * @return location_id
     */
    public String getLocationId() {
        return locationId;
    }

    /**
     * @param locationId
     */
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    /**
     * @return item_number
     */
    public String getItemNumber() {
        return itemNumber;
    }

    /**
     * @param itemNumber
     */
    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    /**
     * @return lot_number
     */
    public String getLotNumber() {
        return lotNumber;
    }

    /**
     * @param lotNumber
     */
    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    /**
     * @return quantity
     */
    public Double getQuantity() {
        return quantity;
    }

    /**
     * @param quantity
     */
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    /**
     * @return hu_id
     */
    public String getHuId() {
        return huId;
    }

    /**
     * @param huId
     */
    public void setHuId(String huId) {
        this.huId = huId;
    }

    /**
     * @return load_id
     */
    public String getLoadId() {
        return loadId;
    }

    /**
     * @param loadId
     */
    public void setLoadId(String loadId) {
        this.loadId = loadId;
    }

    /**
     * @return control_number
     */
    public String getControlNumber() {
        return controlNumber;
    }

    /**
     * @param controlNumber
     */
    public void setControlNumber(String controlNumber) {
        this.controlNumber = controlNumber;
    }

    /**
     * @return line_number
     */
    public String getLineNumber() {
        return lineNumber;
    }

    /**
     * @param lineNumber
     */
    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * @return tracking_number
     */
    public String getTrackingNumber() {
        return trackingNumber;
    }

    /**
     * @param trackingNumber
     */
    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    /**
     * @return error_code
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return error_message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return type
     */
    public Long getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(Long type) {
        this.type = type;
    }

    /**
     * @return po_number
     */
    public String getPoNumber() {
        return poNumber;
    }

    /**
     * @param poNumber
     */
    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    /**
     * @return after_lot_number
     */
    public String getAfterLotNumber() {
        return afterLotNumber;
    }

    /**
     * @param afterLotNumber
     */
    public void setAfterLotNumber(String afterLotNumber) {
        this.afterLotNumber = afterLotNumber;
    }

    /**
     * @return before_expiration_date
     */
    public Date getBeforeExpirationDate() {
        return beforeExpirationDate;
    }

    /**
     * @param beforeExpirationDate
     */
    public void setBeforeExpirationDate(Date beforeExpirationDate) {
        this.beforeExpirationDate = beforeExpirationDate;
    }

    /**
     * @return after_expiration_date
     */
    public Date getAfterExpirationDate() {
        return afterExpirationDate;
    }

    /**
     * @param afterExpirationDate
     */
    public void setAfterExpirationDate(Date afterExpirationDate) {
        this.afterExpirationDate = afterExpirationDate;
    }

    /**
     * @return stored_attribute_id2
     */
    public Long getStoredAttributeId2() {
        return storedAttributeId2;
    }

    /**
     * @param storedAttributeId2
     */
    public void setStoredAttributeId2(Long storedAttributeId2) {
        this.storedAttributeId2 = storedAttributeId2;
    }

    /**
     * @return stored_attribute_id
     */
    public Long getStoredAttributeId() {
        return storedAttributeId;
    }

    /**
     * @param storedAttributeId
     */
    public void setStoredAttributeId(Long storedAttributeId) {
        this.storedAttributeId = storedAttributeId;
    }
}