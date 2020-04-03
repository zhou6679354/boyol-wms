package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
@Data
@Table(name = "t_vendor")
public class TVendor extends BasicEntity {
    @Id
    @Column(name = "vendor_id", updatable = false)
    private Integer vendorId;
    @Column(name = "vendor_code")
    private String vendorCode;
    @Column(name = "vendor_name")
    private String vendorName;
    @Column(name = "inspection_flag")
    private String inspectionFlag;
    @Column(name = "ownership_control")
    private String ownershipControl;
    @Column(name = "audit_percent")
    private Integer auditPercent;
    @Column(name = "vqm_profile")
    private String vqmProfile;



}