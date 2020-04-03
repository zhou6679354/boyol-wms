package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import java.io.Serializable;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年09月27日 09:04
 */
@Data
public class TOrderPackageItem extends BasicEntity {
    String displayItemNumber;
    String description;
    Long qty;
}
