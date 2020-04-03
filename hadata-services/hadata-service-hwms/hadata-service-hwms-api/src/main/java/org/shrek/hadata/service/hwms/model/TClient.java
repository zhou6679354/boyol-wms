package org.shrek.hadata.service.hwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author chengjian
 */
@Data
@Table(name = "t_client")
public class TClient extends BasicEntity {
    @Id
    @Column(name = "wh_id")
    private String whId;

    @Id
    @Column(name = "client_code")
    private String clientCode;

    private String name;

    private String addr1;

    private String addr2;

    private String addr3;

    private String city;

    private String state;

    private String zip;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "country_name")
    private String countryName;

    private String phone;

    private String email;

    private String contact;

    private String fax;

    @Column(name = "long_client_code")
    private String longClientCode;

    @Column(name = "ext_code")
    private String extCode;


    @Transient
    //@Column(name = "send_control")
    private String sendControl;


}