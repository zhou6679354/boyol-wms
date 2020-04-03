package org.shrek.hadata.service.iwms.service;

import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.service.iwms.model.*;

import java.util.Date;
import java.util.List;

/**
 * 出库订单
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年08月07日 11:13
 */
public interface OutBoundService {
    /**
     * 创建出库单
     *
     * @param order
     * @return
     */
    public BaseResponse createOutBoundOrder(TOrder order);
    /**
     * 创建SY出库单
     *
     * @param order
     * @return
     */
    public BaseResponse createOrUpdateOutBoundOrder(TOrder order);

    /**
     * 取消出库单
     *
     * @param orderCode
     * @param whid
     * @return
     */
    public BaseResponse cancelOutBoundOrder(String orderCode, String client, String whid);

    /**
     * 查询出库订单确认信息
     *
     * @param whse
     * @param client
     * @return
     */
    public List<TOrder> queryBeforeDayConfirmOutBoundsByWhseAnd(String whse, String client);
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
     * 查询出库订单确认信息
     *
     * @param whse
     * @return
     */
    public List<TOrder> queryConfirmOutBoundsByWhse(String whse);
    /**
     * 根据多个仓库代码查询出库订单确认信息
     *
     * @param list
     * @return
     */
    public List<TOrder> queryConfirmOutBoundsByWhses(List<String> list);
    /**
     * 查询出库订单确认信息
     *
     * @param whse
     * @return
     */
    public List<TOrder> queryConfirmOutBoundsByWhseAndClient(String whse, String client);
    /**
     * 根据指定类型查询出库订单确认信息
     *
     * @param whse
     * @return
     */
    public List<TOrder> queryConfirmOutBoundsByWhseAndClientAndTypeId(String whse, String client,Integer typeId);
    /**
     * 查询出库状态(商品，赠品，样品)
     *
     * @param whse
     * @return
     */
    public TOrderDetail queryConfirmOutBoundsByWhseAndNoAndId(String lineNumber, String orderNumber, String whse);
    public TOrderDetail queryConfirmOutBoundsByWhseAndLineNoAndId(String lineNumber,String orderNumber,String whse);
    /**
     * 查询出库状态(商品，赠品，样品)
     *
     * @param whse
     * @return
     */
    public String queryConfirmOutBoundsByItemAndWhse(String lineNumber, String whse);

    /**
     * 查询预扫码客户编号
     *
     * @param whse
     * @return
     */
    public String queryConfirmOutBoundsByCusAndWhse(Integer customerId, String whse);

    /**
     * 查询出库订单确认信息
     *
     * @param whse   仓库
     * @param client 客户
     * @param type   类型
     * @return
     */
    public List<TOrder> queryConfirmOutBoundsByWhseAndClientAndType(String whse, String client, int type);

    /**
     * 查询到货订单确认信息
     *
     * @param whse   仓库
     * @param client 客户
     * @return
     */
    public List<TOrder> queryConfirmGoodsReceiveByWhseAndClient(String whse, String client);

    /**
     * 包材查询信息
     * @return
     */
    public List<TblCmRequest> queryConfirmTblcmByWhseAndClient();
    /**
     * 查询出库订单确认信息
     *
     * @return
     */
    public List<TOrder> queryMassiveOutbounds(String whse, String clientCode);

    public int updateMassiveOutboundsStatus(String clientCode, String whse, String orderCode, String status);

    public void createFinOrderAcceptNotice(String clientCode, String whse, String orderCode, String status);
    /**
     * 更新是否回传
     *
     * @param id
     * @return
     */
    public int updateOutBoundsBack(Integer id);
    /**
     * 更新回传成功
     */
    public int updateOutBoundsBackTrue(Integer orderId,String status,String msg,Date date);
    /**
     * 更新回传失败
     */
    public int updateOutBoundsBackFlase(Integer orderId,String status,String msg,Date date);
    /**
     * 更新是否回传
     *
     * @param id
     * @return
     */
    public int updateOutBoundsGoodsBack(Integer id);
    public int updateOutBoundsToTmsStatus(Integer id);
    public int updateOutOrderCancel(TOrder order);
    public String getOrderSerialNumber(String whse);
    public BaseResponse updateExpressInfo (String listJson , String shipperCode, String logisticCode);
    public TOrder queryTOrderByWhIdAndOrderNumber(String whId,String orderNumber);
    public List<TPickDetail> queryTPickDetailByWhId(String whId);
    public List<TPickDetail> queryTPickDetailByWhIdAndOrderNumber(String whId,String orderNumber);
    /**
     * 根据出库单号查询出库明细
     */
    public List<TOrderDetail> queryTOrderDetailByWhIdAndOrderNumber(String whId, String orderNumber);
    public int updateTPickDetailByOrderNumberAndWhId(String itemNumber,String whId);
    public int updateOutOrderSendBack(String whId, String orderNumber);
    public List<StorageStock> queryStorageStockByWhId(String whId,String clientCode);
    public List<TOrder> queryOutboundsToIWMS(String whse, String clientCode);
    public List<String> queryOrderNumber(String whse,String clientCode);
    public void confirmOutbound(String whse,String poNumber,String lineNumber,Long qty);
    public List<TOrder> queryOrderForTMS(String whse);
    public List<String> queryWaIdByWhId(String whId,String waveSynStatus,String waveCancelStatus,String pickingMethod);
    int updateWaIdStatusTrue(String waId, String whId,String pickingMethod,String waveSynStatus);
    public int updateWaveCancelStatusDoing(String waId, String whId,String pickingMethod,String waveCancelStatus,String waveCancelRemark);
    public String doAllPick(String whId,String waId,String useId);
    public String doPartPick(String whId,String waId);
    public String doCancleWave(String wh_id,String wave_id,String user);
}
