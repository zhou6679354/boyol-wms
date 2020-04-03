package org.shrek.hadata.service.iwms.model;

import lombok.Data;
import org.shrek.hadata.commons.mybatis.BasicEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年09月20日 18:16
 */
@Data
public class TOrderPackage extends BasicEntity {

    String shipLabelBarcode;
    List<TOrderPackageItem> items;
    String carrierCode;
    String carrierName;
    String weight;
    String cartonNumber;
}
