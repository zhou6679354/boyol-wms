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

@Data
@Table(name = "t_po_master")
public class TPoMaster extends BasicEntity {
    @Id
    @Column(name = "po_number")
    private String poNumber;

    @Id
    @Column(name = "wh_id")
    private String whId;

    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "vendor_code")
    private String vendorCode;

    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "status")
    private String status;

    @Column(name = "display_po_number")
    private String displayPoNumber;

    @Column(name = "client_code")
    private String clientCode;

    @Column(name = "residential_flag")
    private String residentialFlag;

    @Column(name = "ship_from_name")
    private String shipFromName;

    @Column(name = "ship_from_addr1")
    private String shipFromAddr1;

    @Column(name = "ship_from_addr2")
    private String shipFromAddr2;

    @Column(name = "ship_from_city")
    private String shipFromCity;

    @Column(name = "ship_from_state")
    private String shipFromState;

    @Column(name = "ship_from_postal_code")
    private String shipFromPostalCode;

    @Column(name = "ship_from_country_code")
    private String shipFromCountryCode;

    @Column(name = "ship_from_attention")
    private String shipFromAttention;

    @Column(name = "ship_from_phone")
    private String shipFromPhone;

    @Column(name = "ship_from_fax")
    private String shipFromFax;

    @Column(name = "carrier_scac")
    private String carrierScac;

    @Column(name = "carrier_mode")
    private String carrierMode;

    @Column(name = "service_level")
    private String serviceLevel;

    @Column(name = "freight_terms")
    private String freightTerms;

    @Column(name = "lock_flag")
    private String lockFlag;

    @Column(name = "vendor_name")
    private String vendorName;

    @Column(name = "interface_id")
    private String interfaceId;

    @Column(name = "is_send_back")
    private String isSendBack;

    @Column(name = "send_back_msg")
    private String sendBackMsg;
    @Column(name = "send_back_date")
    private Date sendBackDate;
    @Transient
    private Date confirmDateTime;

    @Transient
    List<TPoDetail> detailList = Lists.newArrayList();
}