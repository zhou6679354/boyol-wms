package org.shrek.hadata.service.hwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.*;

@Data
@Table(name = "ti_receive_order_jztey")
public class TiReceiveOrderJztey extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select next value for dbo.sequence_main_id")
    private String id;

    @Column(name = "master_id")
    private String masterId;

    @Column(name = "org_code")
    private String orgCode;

    private String contact;

    private String clerk;

    private String operator;

    @Column(name = "dept_code")
    private String deptCode;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_cn_medicine")
    private String isCnMedicine;

    @Column(name = "inbound_type")
    private String inboundType;

    private String uploader;

    @Column(name = "reject_type")
    private String rejectType;

    @Column(name = "LMIS_invoking_status")
    private String lmisInvokingStatus;

    private String branch;

    @Column(name = "is_stress_check")
    private String isStressCheck;

    private String checker;

    @Column(name = "is_consign")
    private String isConsign = "N";


}