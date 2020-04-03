package org.shrek.hadata.service.hwms.service;

import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.service.hwms.model.*;

import java.util.HashMap;
import java.util.List;

/**
 * 收货入库管理
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年05月10日 18:39
 */
public interface InBoundService<T> {

    /**
     * 查询入库订单确认信息
     *
     * @param whse
     * @return
     */
    public List<TPoMaster> queryConfirmInBoundsByWhse(String whse);

    /**
     * 创建仓库入库订单
     *
     * @param masters
     * @return
     */
    public boolean createInBounds(List<TiReceiveOrderMaster<T>> masters);

    /**
     * 查询仓库入库订单
     *
     * @param params
     * @return
     */
    public List<HashMap<String, String>> queryInBounds(HashMap<String, String> params);

    /**
     * 更新是否回传
     *
     * @param params
     * @return
     */
    public int updateInBoundsBack(HashMap<String, List<String>> params);


    /**
     * 创建出库单
     *
     * @param poMaster
     * @return
     */
    public BaseResponse createInBoundOrder(TPoMaster poMaster);

    /**
     * 取消入库单
     *
     * @param orderCode
     * @param whid
     * @return
     */
    public BaseResponse cancelInBoundOrder(String orderCode, String whid);

    /**
     * 取消入库单
     *
     * @param poNumber
     * @param whId
     * @return
     */
    void updateInBoundsBack(String whId, String poNumber);

    /**
     * 查询状态(商品、赠品、样品)
     *
     * @param whse
     * @return
     */
    public String queryConfirmInBoundsByZone(String locationId, String whse);

    /**
     * 查询状态(商品、赠品、样品)
     *
     * @param whse
     * @return
     */
    public String queryConfirmInBoundsByItemAndWhse(String itemNumber, String whse);

    /**
     * 取消入库单
     *
     * @param orderCode
     * @param clientCode
     * @param whId
     * @return
     */
    public BaseResponse<String> cancelInBoundOrder(String orderCode, String clientCode, String whId);

    List<TPoMaster> queryConfirmInBoundsByWhse(String clientCode, String whse, int type);
    List<TPoMaster> queryConfirmInBoundsByWhseAndType(String clientCode, String whse, int type);
    /**
     * 查询货主和仓库
     *
     * @param
     * @return
     */
    public List<TClient> queryInBoundsByFax(String fax);
    List<TPoDetail> queryConfirmInBoundsByWhseAndCodeAndType(String clientCode, String whse, int type);

    List<TZoneLoca> queryConfirmZoneLocaByWhseAndloca(String locaId, String whse);

    List<TItemUom> queryConfirmItemUomByItemNumber(String itemNumber);
    /**
     * 查询日志表
     */
    public List<TranLog> queryTranLogByOrderNoAndTranType(String orderNo, String tranType);
    public TStoAttribCollectionDetail queryTStoAttribByStoredAttributeId(Long storedAttributeId,String type);
    public TPickDetail queryTPickDetailByPickId(Integer pickId);
    List<TPoDetail> queryConfirmInBoundsByWhseAndCodeAndType2(String clientCode, String whse, int type);
    List<TPoMaster> queryConfirmInBoundsByWhseAndType2(String clientCode, String whse, int type);
    void updateInBoundsBack2(String whId, String poNumber);
    /**
     *查询入库单信息
     */
    public TPoMaster queryTPoMasterByWhIdAndPoNumber(String whId, String poNumber);
}
