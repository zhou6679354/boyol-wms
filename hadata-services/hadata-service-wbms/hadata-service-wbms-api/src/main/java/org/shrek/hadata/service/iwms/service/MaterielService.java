package org.shrek.hadata.service.iwms.service;

import org.shrek.hadata.service.iwms.model.TItemMaster;
import org.shrek.hadata.service.iwms.model.TblItemSynWisdom;

import java.util.List;

/**
 * 物料数据
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年08月07日 11:13
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
    public TItemMaster getItemMasterByWhIdandItemNumber(String whId, String itemNumber);
    public List<TblItemSynWisdom> getTblItemSynWisdom(String whId,Integer synFlag);
    public List<String> getItemNumberByClientAndWhse(String client,String whse);
    public boolean createOrUpdateMaterials(TItemMaster master);
}
