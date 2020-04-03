package org.shrek.hadata.service.hwms.service.jztey;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.commons.util.DateUtil;
import org.shrek.hadata.service.hwms.mapper.*;
import org.shrek.hadata.service.hwms.model.*;
import org.shrek.hadata.service.hwms.service.AbstractInBoundService;
import org.shrek.hadata.service.hwms.service.InBoundService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年05月10日 18:58
 */
@Service(
        version = "2.0.1",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class JzteyInBoundServiceImpl extends AbstractInBoundService<TiReceiveOrderJztey> implements InBoundService<TiReceiveOrderJztey> {


        @Autowired
    TiReceiveOrderJzteyMapper tiReceiveOrderJzteyMapper;
    @Autowired
    TPoMasterMapper tPoMasterMapper;
    @Autowired
    TPoDetailMapper tPoDetailMapper;
    @Autowired
    TRcptShipMapper tRcptShipMapper;
    @Autowired
    TRcptShipPoMapper tRcptShipPoMapper;
    @Autowired
    TRcptShipPoDetailMapper tRcptShipPoDetailMapper;
    @Autowired
    TReceiptMapper tReceiptMapper;
    @Autowired
    TZoneLocaMapper tZoneLocaMapper;
    @Autowired
    TItemUomMapper tItemUomMapper;
    @Autowired
    TClientMapper tClientMapper;
    @Autowired
    TTranLogMapper tTranLogMapper;
    @Autowired
    TStoAttribMapper tStoAttribMapper;
    @Autowired
    TPickDetailMapper tPickDetailMapper;
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    protected void insertExtend(TiReceiveOrderMaster<TiReceiveOrderJztey> master) {
        TiReceiveOrderJztey jztey = master.getExtend();
        jztey.setMasterId(master.getId());
        tiReceiveOrderJzteyMapper.insertSelective(jztey);
    }

    @Override
    public List<HashMap<String, String>> queryInBounds(HashMap<String, String> params) {
        return tiReceiveOrderJzteyMapper.selectInBoundsToJztey(params);
    }
    public String getSequence(String whid, String seqType, String seqDesc) {
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("in_vchType", seqType);
        paramMap.put("in_description", seqDesc);
        paramMap.put("out_nUID", "");
        paramMap.put("out_nErrorNumber", 0);
        paramMap.put("out_vchLogMsg", "");
        tRcptShipMapper.callUspGetNextControlValue(paramMap);
        String sequence = (String) paramMap.get("out_nUID");
        return Joiner.on("").join("ST", whid, DateUtil.getReqDate(), sequence);
    }
    @Override
    public BaseResponse createInBoundOrder(TPoMaster poMaster) {
        //判断出库订单是否存在
        TPoMaster example = new TPoMaster();
        example.setWhId(poMaster.getWhId());
        example.setPoNumber(poMaster.getPoNumber());
        int count = tPoMasterMapper.selectCount(example);//count 不为0 时候代表运单存在，意思就是数据库有对应数据
        if (count > 0) {
            return BaseResponse.fail("订单已经存在不允许重复推送!");
        } else {
            tPoMasterMapper.insertSelective(poMaster);
            poMaster.getDetailList().forEach(tPoDetail -> {
                tPoDetailMapper.insertSelective(tPoDetail);
            });
        }

        //生成运单
        String shipmentNumber = getSequence(poMaster.getWhId(), "SHIPMENT_NUMBER", "Shipment number identifier");
        TRcptShip tRcptShip = new TRcptShip();
        tRcptShip.setWhId(poMaster.getWhId());
        tRcptShip.setShipmentNumber(shipmentNumber);
        tRcptShip.setCarrierId(1);
        tRcptShip.setTrailerNumber("0");
        tRcptShip.setDateExpected(new Date());
        tRcptShipMapper.insertSelective(tRcptShip);

        //生成运单与订单关系
        TRcptShipPo tRcptShipPo = new TRcptShipPo();
        tRcptShipPo.setWhId(poMaster.getWhId());
        tRcptShipPo.setShipmentNumber(shipmentNumber);
        tRcptShipPo.setPoNumber(poMaster.getPoNumber());
        tRcptShipPo.setOpenToBuyDate(new Date());
        tRcptShipPoMapper.insertSelective(tRcptShipPo);

        //生成运单明细
        final Integer[] number = {0};
        poMaster.getDetailList().forEach(tPoDetail -> {
            TRcptShipPoDetail tRcptShipPoDetail = new TRcptShipPoDetail();
            tRcptShipPoDetail.setWhId(poMaster.getWhId());
            tRcptShipPoDetail.setShipmentNumber(shipmentNumber);
            tRcptShipPoDetail.setPoNumber(poMaster.getPoNumber());
            tRcptShipPoDetail.setLineNumber(tPoDetail.getLineNumber());
            tRcptShipPoDetail.setItemNumber(tPoDetail.getItemNumber());
            tRcptShipPoDetail.setScheduleNumber(number[0]++);
            tRcptShipPoDetail.setExpectedQty(tPoDetail.getQty());
            tRcptShipPoDetail.setReceivedQty(0.0d);
            tRcptShipPoDetail.setLotNumber(tPoDetail.getLotNumber());
            tRcptShipPoDetail.setExpirationDate(tPoDetail.getExpirationDate());
            tRcptShipPoDetailMapper.insertSelective(tRcptShipPoDetail);
        });
        return BaseResponse.success();
    }

    @Override
    public BaseResponse cancelInBoundOrder(String orderCode, String whid) {
         /*
        t_po_master 入库单
        t_rcpt_ship_po 运单订单
        t_rcpt_ship 运单
        */
        TPoMaster tPoMaster = new TPoMaster();
        tPoMaster.setPoNumber(orderCode);
        tPoMaster.setWhId(whid);
        tPoMaster = tPoMasterMapper.selectOne(tPoMaster);
        if (tPoMaster != null) {
            tPoMaster.setStatus("D");
            Example up_TPoMaster = new Example(TPoMaster.class);
            up_TPoMaster.createCriteria().andEqualTo("poNumber", "orderCode").andEqualTo("whId", "whid");
            int count = tPoMasterMapper.updateByExampleSelective(tPoMaster, up_TPoMaster);
        } else {
            return BaseResponse.fail("查询不到对应数据,修改失败");
        }

        TRcptShipPo tRcptShipPo = new TRcptShipPo();
        tRcptShipPo.setPoNumber(orderCode);
        tRcptShipPo.setWhId(whid);
        tRcptShipPo = tRcptShipPoMapper.selectOne(tRcptShipPo);
        if (tRcptShipPo != null) {
            TRcptShip tRcptShip = new TRcptShip();
            tRcptShip.setShipmentNumber(tRcptShipPo.getShipmentNumber());
            tRcptShip.setWhId(whid);
            tRcptShip = tRcptShipMapper.selectOne(tRcptShip);
            if (tRcptShip != null) {
                tRcptShip.setStatus("C");
                Example up_TRcptShip = new Example(TRcptShip.class);
                up_TRcptShip.createCriteria().andEqualTo("shipmentNumber", tRcptShipPo.getShipmentNumber()).andEqualTo("whId", "whid");
                tRcptShipMapper.updateByExampleSelective(tRcptShip, up_TRcptShip);
                return BaseResponse.success();
            } else {
                return BaseResponse.fail("修改失败");
            }
        } else {

            TExceptionLog tExceptionLog = new TExceptionLog();
            tExceptionLog.setTranType("923");
            tExceptionLog.setControlNumber(orderCode);
            tExceptionLog.setDescription("删除入库订单" + orderCode + ",状态改为D");

            DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            String nowDayDate = date.format(new Date());
            Date nowDate = new Date();
            try {
                nowDate = date.parse(nowDayDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tExceptionLog.setExceptionDate(nowDate);

            DateFormat time = new SimpleDateFormat("HH:mm:ss");
            String nowDayTime = time.format(new Date());
            Date nowTime = new Date();
            try {
                nowTime = time.parse(nowDayTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tExceptionLog.setExceptionTime(nowTime);
            tExceptionLog.setEmployeeId(new TClient().getName());
            tExceptionLog.getWhId();

            return BaseResponse.fail();
        }
    }

    @Override
    public void updateInBoundsBack(String whId, String poNumber) {
        TPoMaster tPoMaster = new TPoMaster();
        tPoMaster.setIsSendBack("1");
        tPoMaster.setWhId(whId);
        tPoMaster.setPoNumber(poNumber);
        tPoMasterMapper.updateByPrimaryKeySelective(tPoMaster);
    }

    @Override
    public String queryConfirmInBoundsByZone(String locationId, String whse) {
        TZoneLoca tZoneLoca = new TZoneLoca();
        tZoneLoca.setLocationId(locationId);
        tZoneLoca.setWhId(whse);
        List<TZoneLoca> tZoneLocaList =  tZoneLocaMapper.select(tZoneLoca);
        return tZoneLocaList.get(0).getZone();
    }

    @Override
    public String queryConfirmInBoundsByItemAndWhse(String itemNumber, String whse) {
        TItemUom tItemUom = new TItemUom();
        tItemUom.setItemNumber(itemNumber);
        tItemUom.setWhId(whse);
        tItemUom.setUom("EA");
        return tItemUomMapper.selectOne(tItemUom).getClassId();
    }

    @Override
    public BaseResponse<String> cancelInBoundOrder(String orderCode, String clientCode, String whId) {
        TPoMaster tPoMaster = new TPoMaster();
        tPoMaster.setPoNumber(orderCode);
        tPoMaster.setClientCode(clientCode);
        tPoMaster.setWhId(whId);
        tPoMaster = tPoMasterMapper.selectOne(tPoMaster);
        if (tPoMaster != null) {
            if (tPoMaster.getStatus().equals("D")) {
                return BaseResponse.success();
            } else if (!tPoMaster.getStatus().equals("O")) {
                return BaseResponse.fail("订单号:" + orderCode + "非初始状态,不允许取消!");
            } else {
                tPoMaster.setStatus("D");
                Example up_TPoMaster = new Example(TPoMaster.class);
                up_TPoMaster.createCriteria().andEqualTo("poNumber", orderCode).andEqualTo("whId", whId);
                tPoMasterMapper.updateByExampleSelective(tPoMaster, up_TPoMaster);
            }
        } else {
            return BaseResponse.fail("订单号:" + orderCode + "不存在!");
        }
        TRcptShipPo tRcptShipPo = new TRcptShipPo();
        tRcptShipPo.setPoNumber(orderCode);
        tRcptShipPo.setWhId(whId);
        tRcptShipPo = tRcptShipPoMapper.selectOne(tRcptShipPo);
        if (tRcptShipPo != null) {
            TRcptShip tRcptShip = new TRcptShip();
            tRcptShip.setShipmentNumber(tRcptShipPo.getShipmentNumber());
            tRcptShip.setWhId(whId);
            tRcptShip = tRcptShipMapper.selectOne(tRcptShip);
            if (tRcptShip != null) {
                tRcptShip.setStatus("C");
                Example up_TRcptShip = new Example(TRcptShip.class);
                up_TRcptShip.createCriteria().andEqualTo("shipmentNumber", tRcptShipPo.getShipmentNumber()).andEqualTo("whId", "whid");
                tRcptShipMapper.updateByExampleSelective(tRcptShip, up_TRcptShip);
                return BaseResponse.success();
            } else {
                return BaseResponse.fail("修改失败");
            }
        } else {

            TExceptionLog tExceptionLog = new TExceptionLog();
            tExceptionLog.setTranType("923");
            tExceptionLog.setControlNumber(orderCode);
            tExceptionLog.setDescription("删除入库订单" + orderCode + ",状态改为D");

            DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            String nowDayDate = date.format(new Date());
            Date nowDate = new Date();
            try {
                nowDate = date.parse(nowDayDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tExceptionLog.setExceptionDate(nowDate);

            DateFormat time = new SimpleDateFormat("HH:mm:ss");
            String nowDayTime = time.format(new Date());
            Date nowTime = new Date();
            try {
                nowTime = time.parse(nowDayTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tExceptionLog.setExceptionTime(nowTime);
            tExceptionLog.setEmployeeId(new TClient().getName());
            tExceptionLog.getWhId();

            return BaseResponse.fail();
        }
    }

    @Override
    public List<TPoMaster> queryConfirmInBoundsByWhse(String clientCode, String whse, int type) {
        TPoMaster tPoMaster = new TPoMaster();
        tPoMaster.setWhId(whse);
        tPoMaster.setClientCode(clientCode);
        tPoMaster.setTypeId(type);
        tPoMaster.setStatus("C");
        tPoMaster.setIsSendBack("0");
        List<TPoMaster> tPoMasters = tPoMasterMapper.select(tPoMaster);
        TPoDetail tPoDetail = new TPoDetail();
        tPoMasters.forEach(po -> {
            tPoDetail.setWhId(whse);
            tPoDetail.setPoNumber(po.getPoNumber());
            List<TPoDetail> tPoDetails = tPoDetailMapper.select(tPoDetail);
            po.setDetailList(tPoDetails);
        });
        return tPoMasters;
    }

    @Override
    public List<TPoMaster> queryConfirmInBoundsByWhseAndType(String clientCode, String whse, int type) {
        TPoMaster tPoMaster = new TPoMaster();
        tPoMaster.setWhId(whse);
        tPoMaster.setClientCode(clientCode);
        tPoMaster.setTypeId(type);
        tPoMaster.setStatus("C");
        tPoMaster.setIsSendBack("0");
        List<TPoMaster> tPoMasters = tPoMasterMapper.select(tPoMaster);
        TPoDetail tPoDetail = new TPoDetail();
        TReceipt tReceipt = new TReceipt();
        tPoMasters.forEach(po -> {
            tPoDetail.setWhId(whse);
            tPoDetail.setPoNumber(po.getPoNumber());
            List<TPoDetail> tPoDetails = tPoDetailMapper.select(tPoDetail);
            tReceipt.setPoNumber(po.getPoNumber());
            tReceipt.setWhId(whse);
            List<TPoDetail> details = Lists.newArrayList();
            tPoDetails.forEach(detail -> {
                tReceipt.setItemNumber(detail.getItemNumber());
                List<TReceipt> tReceipts = tReceiptMapper.select(tReceipt);
                Map<String, List<TReceipt>> lotGroup = tReceipts.stream()
                        .collect(Collectors.groupingBy(TReceipt::getLotNumber));
                lotGroup.forEach((path, receipts) -> {
                    detail.setLotNumber2(path);
                    Map<Date, List<TReceipt>> expGroup = receipts.stream()
                            .collect(Collectors.groupingBy(TReceipt::getExpirationDate));
                    expGroup.forEach((k,v)->{
                        TPoDetail poDetail = new TPoDetail();
                        BeanUtils.copyProperties(detail, poDetail);
                        poDetail.setExpirationDate2(v.get(0).getExpirationDate());
                        poDetail.setLocationId(v.get(0).getForkId());
                        poDetail.setActualQty(v.stream().mapToDouble(TReceipt::getQtyReceived).sum());
                        details.add(poDetail);
                    });

                });
            });
            po.setDetailList(details);
        });
        return tPoMasters;
    }

    @Override
    public List<TPoDetail> queryConfirmInBoundsByWhseAndCodeAndType(String clientCode, String whse, int type) {
        HashMap<String, Object> param = Maps.newHashMap();
        param.put("whse",whse);
        param.put("type",type);
        List<TPoDetail> tPoDetails = tPoDetailMapper.selectInBoundsByOutB2C(param);
        return tPoDetails;
    }
    @Override
    public List<TClient> queryInBoundsByFax(String fax){
        TClient tClient = new TClient();
        tClient.setFax(fax);
        return tClientMapper.select(tClient);
    }

    @Override
    public List<TZoneLoca> queryConfirmZoneLocaByWhseAndloca(String locaId, String whse) {
        TZoneLoca tZoneLoca = new TZoneLoca();
        tZoneLoca.setWhId(whse);
        tZoneLoca.setLocationId(locaId);
        List<TZoneLoca> tZoneLocas = tZoneLocaMapper.select(tZoneLoca);
        return tZoneLocas;
    }

    @Override
    public List<TItemUom> queryConfirmItemUomByItemNumber(String itemNumber) {
        TItemUom tItemUom = new TItemUom();
        tItemUom.setItemNumber(itemNumber);
        tItemUom.setUom("EA");
        List<TItemUom> tItemUoms = tItemUomMapper.select(tItemUom);
        return tItemUoms;
    }

    @Override
    public List<TranLog>  queryTranLogByOrderNoAndTranType(String orderNo, String tranType) {
        TranLog tranLog = new TranLog();
        tranLog.setControlNumber(orderNo);
        tranLog.setTranType(tranType);
        return tTranLogMapper.select(tranLog);
    }

    @Override
    public TStoAttribCollectionDetail queryTStoAttribByStoredAttributeId(Long storedAttributeId, String attributeId) {
        TStoAttribCollectionDetail tStoAttribCollectionDetail=new TStoAttribCollectionDetail();
        tStoAttribCollectionDetail.setStoredAttributeId(storedAttributeId);
        tStoAttribCollectionDetail.setAttributeId(attributeId);
        return tStoAttribMapper.selectOne(tStoAttribCollectionDetail);
    }

    @Override
    public TPickDetail queryTPickDetailByPickId(Integer pickId) {
        TPickDetail tPickDetail = new TPickDetail();
        tPickDetail.setPickId(pickId);
        return tPickDetailMapper.selectOne(tPickDetail);
    }

    @Override
    public List<TPoDetail> queryConfirmInBoundsByWhseAndCodeAndType2(String clientCode, String whse, int type) {
        HashMap<String, Object> param = Maps.newHashMap();
        param.put("whse",whse);
        param.put("type",type);
        List<TPoDetail> tPoDetails = tPoDetailMapper.selectInBoundsByOutB2CT(param);
        return tPoDetails;
    }

    @Override
    public List<TPoMaster> queryConfirmInBoundsByWhse(String whse){
        TPoMaster tPoMaster = new TPoMaster();
        tPoMaster.setTypeId(1121);
        tPoMaster.setStatus("C");
        tPoMaster.setIsSendBack("0");
        List<TPoMaster> tPoMasters = tPoMasterMapper.select(tPoMaster);
        TPoDetail tPoDetail = new TPoDetail();
        TReceipt tReceipt= new TReceipt();
        tPoMasters.forEach(po -> {
            tPoDetail.setWhId(whse);
            tPoDetail.setPoNumber(po.getPoNumber());
            List<TPoDetail> tPoDetails = tPoDetailMapper.select(tPoDetail);
            po.setDetailList(tPoDetails);
            tReceipt.setPoNumber(po.getPoNumber());
            tReceipt.setWhId(whse);
            List<TReceipt> tReceipts= tReceiptMapper.select(tReceipt);
            if(tReceipts.size()>0){
                po.setConfirmDateTime(tReceipts.get(0).getReceiptDate());
                tPoDetail.setActualQty(tReceipts.stream().mapToDouble(TReceipt::getQtyReceived).sum());
            }
        });
        return tPoMasters;
    }
    @Override
    public void updateInBoundsBack2(String whId, String poNumber) {
        TPoMaster tPoMaster = new TPoMaster();
        tPoMaster.setIsSendBack("1");
        tPoMaster.setWhId(whId);
        tPoMaster.setPoNumber(poNumber);
        tPoMasterMapper.updateByPrimaryKeySelective(tPoMaster);
    }
    @Override
    public List<TPoMaster> queryConfirmInBoundsByWhseAndType2(String clientCode, String whse, int type) {
        TPoMaster tPoMaster=new TPoMaster();
        tPoMaster.setClientCode(clientCode);
        tPoMaster.setWhId(whse);
        tPoMaster.setTypeId(type);
        List<TPoMaster> tPoMasters = tPoMasterMapper.select(tPoMaster);
        return tPoMasters;
    }
    @Override
    public TPoMaster queryTPoMasterByWhIdAndPoNumber(String whId, String poNumber) {
        TPoMaster tPoMaster=new TPoMaster();
        tPoMaster.setWhId(whId);
        tPoMaster.setPoNumber(poNumber);
        return tPoMasterMapper.selectOne(tPoMaster);
    }
}