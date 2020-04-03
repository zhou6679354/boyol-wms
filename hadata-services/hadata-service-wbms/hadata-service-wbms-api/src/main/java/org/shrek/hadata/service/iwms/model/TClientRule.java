package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
@Data
@Table(name = "t_client_rule")
public class TClientRule extends BasicEntity {
    @Id
    @Column(name = "client_rule_id")
    private Integer clientRuleId;
    @Id
    @Column(name = "client_code")
    private String clientCode;
    @Id
    @Column(name = "rule_id")
    private Integer ruleId;
    @Column(name = "sequence")
    private Integer sequence;
    @Column(name = "active")
    private String active;
    @Column(name = "wh_id")
    private String whId;
}
