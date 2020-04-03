package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
@Data
@Table(name = "t_whse_control")
public class TWhseControl extends BasicEntity {
    @Id
    @Column(name = "wh_id")
    private String whId;
    @Id
    @Column(name = "control_type")
    private String controlType;
    @Column(name = "description")
    private String description;
    @Column(name = "next_value")
    private int nextValue;
    @Column(name = "config_display")
    private String configDisplay;
    @Column(name = "allow_edit")
    private String allowEdit;
    @Column(name = "c1")
    private String c1;
    @Column(name = "c2")
    private String c2;
    @Column(name = "f1")
    private double f1;


}