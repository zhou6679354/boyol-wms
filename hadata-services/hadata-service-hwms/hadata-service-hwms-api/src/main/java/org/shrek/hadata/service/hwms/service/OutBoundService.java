package org.shrek.hadata.service.hwms.service;

import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.service.hwms.model.*;

import java.util.HashMap;
import java.util.List;

/**
 * 出库管理
 *
 * @author chengjian
 * @version 1.0
 * @date 2018年05月11日 06:16
 */
public interface OutBoundService<T> {

    /**
     * 查询出库订单确认信息
     *
     * @param whse
     * @return
     */
    public List<TOrder> queryConfirmOutBoundsByWhse(String whse);

    /**
     * 创建出库单
     *
     * @param order
     * @return
     */
    public BaseResponse createOutBoundOrder(TOrder order);

    /**
     * 取消出库单
     *
     * @param orderCode
     * @param whid
     * @return
     */
    public BaseResponse cancelOutBoundOrder(String orderCode, String whid);


    /**
     * 创建仓库出库订单
     *
     * @param masters
     * @return
     */
    public boolean createOutBounds(List<TiSendOrderMaster<T>> masters);


    /**
     * 查询出库订单
     *
     * @param params
     * @return
     */
    public List<HashMap<String, String>> queryOutBounds(HashMap<String, String> params);

    public List<String> queryTopOutBounds(HashMap<String, String> params);

    /**
     * 包材查询信息
     * @return
     */
    public List<TblCmRequest> queryConfirmTblcmByWhseAndClient();

    /**
     * 更新是否回传
     *
     * @param params
     * @return
     */
    public int updateOutBoundsBack(HashMap<String, List<String>> params);

    /**
     * 更新是否回传
     *
     * @param id
     * @return
     */
    public int updateOutBoundsBack(Integer id);

    /**
     * 根据ID查询出库订单
     *
     * @param orderNumber
     * @param whse
     */
    public TOrder queryOutBounds(String orderNumber,String whse);

    /**
     * 取消出库单
     *
     * @param orderCode
     * @param clientCode
     * @param whId
     * @return
     */
    public  BaseResponse<String> cancelOutBoundOrder(String orderCode, String clientCode, String whId);

    /**
     * 查询出库订单确认信息
     *
     * @param whse 仓库
     * @param client 客户
     * @param type 类型
     * @return
     */
    public List<TOrder> queryConfirmOutBoundsByWhseAndClientAndType(String whse, String client,int type);

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
    public String queryConfirmOutBoundsByItemAndWhse(String lineNumber,String whse);

    /**
     * 查询预扫码客户编号
     *
     * @param whse
     * @return
     */
    public String queryConfirmOutBoundsByCusAndWhse(Integer customerId,String whse);
    /**
     * 查询预扫码客户编号
     */
    public int updateOutOrderCancel(TOrder tocOrder);
    /**
     * 查询预扫码客户编号
     */
    public BaseResponse updateExpressInfo (String listJson ,String shipperCode,String logisticCode);
    /**
     * 更新是否订阅快递节点
     */
    public int updateOutBoundsSubByOrderNo(String whse,String orderNumber);
    /**
     * 更新是否回传
     */
    public int updateOutBoundsBackByOrderNo(String whse,String orderNumber);
    /**
     * 获取需要订阅快递节点的订单
     */
    public List<TOrder> querySubscribeOutBounds(String whse,String client);
    /**
     * 查询出库订单确认信息,以入库单的格式体现，用来回传日砾toC的出库数据至对方博科系统
     */
    public List<TPoDetail> queryOutBoundsByWhse2(String whse);
    /**
     * 查询出库订单确认信息,以入库单的格式体现，用来回传日砾toC的出库数据
     */
    public List<TPoDetail> queryOutBoundsByWhse(String whse);
    public TOrder queryTOrderByWhIdAndOrderNumber(String whId,String orderNumber);
    //public TOrderDetail queryConfirmOutBoundsByWhseAndLineNoAndId(String lineNumber,String orderNumber,String whse);
}
