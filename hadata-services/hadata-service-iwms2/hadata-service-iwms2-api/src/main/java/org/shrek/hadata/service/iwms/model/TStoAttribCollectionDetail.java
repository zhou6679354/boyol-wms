package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
@Data
@Table(name = "t_sto_attrib_collection_detail")
public class TStoAttribCollectionDetail extends BasicEntity {
    @Id
    @Column(name = "stored_attribute_id")
    private Long storedAttributeId;
    @Id
    @Column(name = "attribute_id")
    private String attributeId;
    @Column(name = "attribute_value")
    private String attributeValue;
}
