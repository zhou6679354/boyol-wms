package org.shrek.hadata.service.hwms.service;

import org.shrek.hadata.service.hwms.model.TItemMaster;

import java.util.List;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月11日 06:44
 */
public interface MaterielService {

    /**
     * 创建客户物料信息
     *
     * @param itemMasters
     * @return
     */
    public boolean createMaterials(List<TItemMaster> itemMasters);


    public List<TItemMaster> getItemsByClientAndWhse(String client,String whse);

    public TItemMaster getItemByCode(String code, String client);
}
