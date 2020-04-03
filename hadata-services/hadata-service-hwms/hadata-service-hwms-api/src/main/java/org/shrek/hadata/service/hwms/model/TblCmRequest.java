package org.shrek.hadata.service.hwms.model;

import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tbl_cm_request")
public class TblCmRequest extends BasicEntity {
    @Id
    private Integer id;

    @Column(name = "wh_id")
    private String whId;

    @Column(name = "client_code")
    private String clientCode;

    @Column(name = "item_number")
    private String itemNumber;

    @Column(name = "req_qty")
    private Double reqQty;

    @Column(name = "req_user")
    private String reqUser;

    @Column(name = "req_datetime")
    private Date reqDatetime;

    @Column(name = "send_flag")
    private Integer sendFlag;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
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
     * @return client_code
     */
    public String getClientCode() {
        return clientCode;
    }

    /**
     * @param clientCode
     */
    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
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
     * @return req_qty
     */
    public Double getReqQty() {
        return reqQty;
    }

    /**
     * @param reqQty
     */
    public void setReqQty(Double reqQty) {
        this.reqQty = reqQty;
    }

    /**
     * @return req_user
     */
    public String getReqUser() {
        return reqUser;
    }

    /**
     * @param reqUser
     */
    public void setReqUser(String reqUser) {
        this.reqUser = reqUser;
    }

    /**
     * @return req_datetime
     */
    public Date getReqDatetime() {
        return reqDatetime;
    }

    /**
     * @param reqDatetime
     */
    public void setReqDatetime(Date reqDatetime) {
        this.reqDatetime = reqDatetime;
    }

    /**
     * @return send_flag
     */
    public Integer getSendFlag() {
        return sendFlag;
    }

    /**
     * @param sendFlag
     */
    public void setSendFlag(Integer sendFlag) {
        this.sendFlag = sendFlag;
    }
}