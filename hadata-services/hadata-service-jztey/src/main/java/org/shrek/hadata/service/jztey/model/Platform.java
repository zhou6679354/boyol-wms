package org.shrek.hadata.service.jztey.model;

import lombok.Data;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年10月12日 10:38
 */
@Data
public class Platform {
    /**
     * 平台名称
     */
    String platname;
    /**
     * 收货人
     */
    String shy;
    /**
     * 验收人
     */
    String zjy;
    /**
     * 采购人
     */
    String cgy;
    /**
     * 销售员
     */
    String xsy;
    /**
     * 仓库
     */
    String store;
    /**
     * 部门
     */
    String branch;
}
