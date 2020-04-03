package org.shrek.hadata.service.iwms.model;

import com.google.common.collect.Lists;
import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Data
@Table(name = "ti_receive_order_master")
public class TiReceiveOrderMaster<T> extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select next value for dbo.sequence_main_id")
    private String id;

    @Column(name = "store_code")
    private String storeCode;

    @Column(name = "cust_code")
    private String custCode;

    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "order_date")
    private String orderDate;

    @Column(name = "dealer_code")
    private String dealerCode;

    @Column(name = "proc_flag")
    private String procFlag;

    private String remark;

    @Column(name = "active_flag")
    private String activeFlag;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "last_updated_by")
    private String lastUpdatedBy;

    @Column(name = "last_updated_date")
    private Date lastUpdatedDate;

    @Transient
    T extend;
    @Transient
    List<TiReceiveOrderDetail> details = null;

    public List<TiReceiveOrderDetail> getDetails() {
        if (Optional.ofNullable(details).isPresent() == false) {
            details = Lists.newArrayList();
        }
        return details;
    }


}