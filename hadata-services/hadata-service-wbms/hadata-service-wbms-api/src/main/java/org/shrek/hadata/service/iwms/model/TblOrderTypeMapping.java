package org.shrek.hadata.service.iwms.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tbl_order_type_mapping")
public class TblOrderTypeMapping {
    @Id
    @Column(name = "wh_id")
    private String whId;
    @Id
    @Column(name = "type_flag")
    private String typeFlag;
    @Id
    @Column(name = "client_code")
    private String clientCode;
    @Id
    @Column(name = "order_type")
    private String orderType;
    @Id
    @Column(name = "interface_order_type")
    private String interfaceOrderType;
    @Column(name = "interface_post_flag")
    private String interfacePostFlag;
    @Column(name = "ceiling_range")
    private float ceilingRange;
    @Column(name = "lot_status")
    private String lotStatus;
    @Column(name = "zone_type")
    private String zoneType;
    @Column(name = "show_add_flag")
    private String showAddFlag;
}
