package org.shrek.hadata.service.iwms.service;

import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.service.iwms.model.*;

import java.util.Date;
import java.util.List;

/**
 * 入库订单
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年08月07日 11:00
 */
public interface InBoundService {

    /**
     * 创建出库单
     *
     * @param poMaster
     * @return
     */
    public BaseResponse createInBoundOrder(TPoMaster poMaster);

    /**
     * 创建SY入库单
     *
     * @param poMaster
     * @return
     */
    public BaseResponse createOrUpdateInBoundOrder(TPoMaster poMaster);
    /**
     * 取消入库单
     *
     * @param orderCode
     * @param whid
     * @return
     */
    public BaseResponse cancelInBoundOrder(String orderCode, String client, String whid);

    /**
     * 查询入库订单确认信息
     *
     * @param whse
     * @return
     */
    public List<TPoMaster> queryConfirmInBoundsByCodeWhse(String clientCode, String whse);
    /**
     * 查询入库订单确认信息
     *
     * @param whse
     * @return
     */
    public List<TPoMaster> queryConfirmInBoundsByWhse(String whse);
    /**
     * 查询入库订单确认信息
     *
     * @param whse
     * @return
     */
    public List<TPoMaster> queryConfirmInBoundsByWhse(String clientCode, String whse, int type);
    /**
     * 查询货主和仓库
     *
     * @paramqueryConfirmInBoundsByWhseAndClient
     * @return
     */
    public List<TClient> queryInBoundsByFax(String fax);

    /**
     * 查询入库订单确认信息
     *
     * @param whse
     * @return
     */
    public List<TPoMaster> queryConfirmInBoundsByWhseAndClient(String whse, String clientCode);
    /**
     * 查询订单运单关联信息
     *
     * @param whse
     * @return
     */
    public TRcptShipPo queryInfoByWhseAndPoNumber(String whse, String poNumber);
    /**
     * 查询订单运单信息
     *
     * @param whse
     * @return
     */
    public TRcptShip queryInfoByWhseAndShipmentNumber(String whse, String shipmentNumber);
    /**
     * 查询状态(商品、赠品、样品)
     *
     * @param whse
     * @return
     */
    public String queryConfirmInBoundsByItemAndWhse(String itemNumber, String whse);
    public TPoDetail queryPoDetailByPoNumberAndWhse(String poNumber, String whse,String lineNumber);
    public TStoAttribCollectionDetail queryTStoAttribByStoredAttributeId(Long storedAttributeId, String attributeId);
    public List<TStoredCount> queryTStoredItemList(String whId);
    /**
     * 查询收件信息
     *
     * @param whse
     * @return
     */
    public List<TReceiptCount> queryReceiptInfoByPoNumberAndWhse(String poNumber, String whse);
    /**
     * 查询状态(商品、赠品、样品)
     *
     * @param whse
     * @return
     */
    public String queryConfirmInBoundsByZone(String locationId, String whse);

    /**
     * 取消入库单
     *
     * @param poNumber
     * @param whId
     * @return
     */
    void updateInBoundsBack(String whId, String poNumber);
    public int updateInOrderCancel(TPoMaster tpoMaster);
    /**
     * 查询日志表
     */
    public List<TranLog> queryTranLogByOrderNoAndTranType(String orderNo, String tranType);
    /**
     *查询释放波次信息
     */
    public TPickDetail queryTPickDetailByPickId(Integer pickId);
    /**
     *查询收货记录
     */
    public List<TReceipt> queryTReceiptByPoNumberAndWhId(String poNumber, String WhId);
    void updateScfInboundsStatus(String whId, String poNumber);
    public List<TPoMaster> queryScfInbounds(String whse, String clientCode);
    public List<String> queryPoNumber(String whse,String clientCode);
    /**
     *更新POP运单回传状态
     */
    public void updatePopInBoundsBack(Integer id);
    /**
     *查询pop运单信息
     */
    public List<TblInfoImpReceiptPop> queryConfirmPopInBoundsByCodeWhse(String clientCode, String whId);
    /**
     *查询入库单信息
     */
    public TPoMaster queryTPoMasterByWhIdAndPoNumber(String whId, String poNumber);
    public List<TStoredItem> queryTStoredItemByWhId(String whId);
    public List<TWork> queryTWorkByWhId(String whId,String clientCode);
    public List<TPoMaster> queryConfirmInBoundsByCodeWhseAndTypeId(String clientCode, String whse,Integer typeId);

    public List<TPoMaster> queryPoToIWMS(String whse, String clientCode);
    public List<TStoredItem> queryTStoredItemByItemNumber(String itemNumber);
    public void confirmInbound(String whse,String clientCode,String poNumber,String lineNumber,String itemNumber,Long qty);
    public List<BigScreen> queryBigScreen(QueryCity queryCity);
    public List<BigScreenStorck> queryBigScreenStorck(QueryCity queryCity);
}
