package org.shrek.hadata.service.iwms.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.ibatis.session.RowBounds;
import org.shrek.hadata.commons.base.BaseResponse;
import org.shrek.hadata.commons.util.DateUtil;
import org.shrek.hadata.service.iwms.mapper.*;
import org.shrek.hadata.service.iwms.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chengjian
 * @version 1.0
 * @date 2018年08月07日 11:14
 */
@Service(
        version = "1.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class BaseInBoundServiceImpl implements InBoundService {

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
    TExceptionLogMapper tExceptionLogMapper;
    @Autowired
    TReceiptMapper tReceiptMapper;
    @Autowired
    TZoneLocaMapper tZoneLocaMapper;
    @Autowired
    TItemUomMapper tItemUomMapper;
    @Autowired
    TClientMapper tClientMapper;
    @Autowired
    TblInfoImpReceiptPopMapper tblInfoImpReceiptPopMapper;
    @Autowired
    TStoAttribMapper tStoAttribMapper;
    @Autowired
    TStoredItemMapper tStoredItemMapper;
    @Autowired
    TTranLogMapper tTranLogMapper;
    @Autowired
    TPickDetailMapper tPickDetailMapper;
    @Autowired
    StorageStockMapper storageStockMapper;
    @Autowired
    TWorkMapper workMapper;
    @Autowired
    BigScreenMapper bigScreenMapper;
    @Override
    public List<TClient> queryInBoundsByFax(String fax){
        TClient tClient = new TClient();
        tClient.setFax(fax);
        return tClientMapper.select(tClient);
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
        TReceipt tReceipt = new TReceipt();

        tPoMasters.forEach(po -> {
            tPoDetail.setWhId(whse);
            tPoDetail.setPoNumber(po.getPoNumber());
            List<TPoDetail> tPoDetails = tPoDetailMapper.select(tPoDetail);
            tReceipt.setPoNumber(po.getPoNumber());
            tReceipt.setWhId(whse);
            tPoDetails.forEach(pl -> {
                tReceipt.setLineNumber(pl.getLineNumber());
                List<TReceipt> tReceipts = tReceiptMapper.select(tReceipt);
                if (tReceipts != null && tReceipts.size()>0)
                {
                    double totalQty = 0d;
                    for(int i=0;i<tReceipts.size();i++)
                    {
                        totalQty = totalQty + tReceipts.get(i).getQtyReceived();
                    }
                    pl.setActualQty(totalQty);
                }
                else
                    pl.setActualQty(0d);
            });
            po.setDetailList(tPoDetails);
        });
        return tPoMasters;
    }
    @Override
    public List<TPoMaster> queryConfirmInBoundsByCodeWhse(String clientCode, String whse) {
        TPoMaster tPoMaster = new TPoMaster();
        tPoMaster.setWhId(whse);
        tPoMaster.setClientCode(clientCode);
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
                tReceipt.setLineNumber(detail.getLineNumber());
                List<TReceipt> tReceipts = tReceiptMapper.select(tReceipt);
                tReceipts.forEach(detail1 -> {
                    if(detail.getLotNumber() == null){
                        detail.setLotNumber("-");
                    }
                    if(detail.getExpirationDate() == null)
                    {
                        detail.setExpirationDate(new Date(2099,1,1));
                    }
                });
                Map<Optional<String>, List<TReceipt>> lotGroup = tReceipts.stream()
                        .collect(Collectors.groupingBy(event -> Optional.ofNullable(event.getLotNumber()==null?"-":event.getLotNumber())));
                lotGroup.forEach((path, receipts) -> {
                    detail.setLotNumber2(path.get()=="-"?"":path.get());
                    Map<Optional<Date>, List<TReceipt>> expGroup = receipts.stream()
                            .collect(Collectors.groupingBy(event -> Optional.ofNullable(event.getExpirationDate()==null?new Date(2099,1,1):event.getExpirationDate())));
                    expGroup.forEach((k,v)->{
                        Map<String,List<TReceipt>> locGroup = v.stream()
                                .collect(Collectors.groupingBy(TReceipt::getLocationId));
                        locGroup.forEach((k1,v1)-> {

                            TPoDetail poDetail = new TPoDetail();
                            BeanUtils.copyProperties(detail, poDetail);
                            poDetail.setExpirationDate2(v1.get(0).getExpirationDate());
                            poDetail.setLocationId(v1.get(0).getLocationId());
                            poDetail.setStoredAttributeId(v1.get(0).getStoredAttributeId().longValue());
                            poDetail.setActualQty(v1.stream().mapToDouble(TReceipt::getQtyReceived).sum());
                            details.add(poDetail);
                        });
                    });

                });
            });
            po.setDetailList(details);
        });
        return tPoMasters;
    }

    @Override
    public List<TPoMaster> queryConfirmInBoundsByWhse(String whse) {
        TPoMaster tPoMaster = new TPoMaster();
        tPoMaster.setWhId(whse);
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
                tReceipt.setLineNumber(detail.getLineNumber());
                List<TReceipt> tReceipts = tReceiptMapper.select(tReceipt);
                tReceipts.forEach(detail1 -> {
                    if(detail.getLotNumber() == null){
                        detail.setLotNumber("-");
                    }
                    if(detail.getExpirationDate() == null)
                    {
                        detail.setExpirationDate(new Date(2099,1,1));
                    }
                });
                Map<Optional<String>, List<TReceipt>> lotGroup = tReceipts.stream()
                        .collect(Collectors.groupingBy(event -> Optional.ofNullable(event.getLotNumber()==null?"-":event.getLotNumber())));
                lotGroup.forEach((path, receipts) -> {
                    detail.setLotNumber2(path.get()=="-"?"":path.get());
                    Map<Optional<Date>, List<TReceipt>> expGroup = receipts.stream()
                            .collect(Collectors.groupingBy(event -> Optional.ofNullable(event.getExpirationDate()==null?new Date(2099,1,1):event.getExpirationDate())));
                    expGroup.forEach((k,v)->{
                        Map<String,List<TReceipt>> locGroup = v.stream()
                                .collect(Collectors.groupingBy(TReceipt::getLocationId));
                        locGroup.forEach((k1,v1)-> {

                            TPoDetail poDetail = new TPoDetail();
                            BeanUtils.copyProperties(detail, poDetail);
                            poDetail.setExpirationDate2(v1.get(0).getExpirationDate());
                            poDetail.setLocationId(v1.get(0).getLocationId());
                            poDetail.setStoredAttributeId(v1.get(0).getStoredAttributeId().longValue());
                            poDetail.setActualQty(v1.stream().mapToDouble(TReceipt::getQtyReceived).sum());
                            details.add(poDetail);
                        });
                    });

                });
            });
            po.setDetailList(details);
        });
        return tPoMasters;
    }

    @Override
    public List<TPoMaster> queryConfirmInBoundsByWhses(List<String> list) {
        List<TPoMaster> tPoMasters = tPoMasterMapper.selectByWhses(list);
        tPoMasters.forEach(po -> {
            TPoDetail tPoDetail = new TPoDetail();
            tPoDetail.setWhId(po.getWhId());
            tPoDetail.setPoNumber(po.getPoNumber());
            List<TPoDetail> tPoDetails = tPoDetailMapper.select(tPoDetail);
            po.setDetailList(tPoDetails);
        });
        return tPoMasters;
    }

    @Override
    public List<TPoMaster> queryConfirmInBoundsByCodeWhseAndTypeId(String clientCode, String whse,Integer typeId) {
        TPoMaster tPoMaster = new TPoMaster();
        tPoMaster.setWhId(whse);
        tPoMaster.setClientCode(clientCode);
        tPoMaster.setStatus("C");
        tPoMaster.setIsSendBack("0");
        tPoMaster.setTypeId(typeId);
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
                tReceipt.setLineNumber(detail.getLineNumber());
                List<TReceipt> tReceipts = tReceiptMapper.select(tReceipt);
                tReceipts.forEach(detail1 -> {
                    if(detail.getLotNumber() == null){
                        detail.setLotNumber("-");
                    }
                    if(detail.getExpirationDate() == null)
                    {
                        detail.setExpirationDate(new Date(2099,1,1));
                    }
                });
                Map<Optional<String>, List<TReceipt>> lotGroup = tReceipts.stream()
                        .collect(Collectors.groupingBy(event -> Optional.ofNullable(event.getLotNumber()==null?"-":event.getLotNumber())));
                lotGroup.forEach((path, receipts) -> {
                    detail.setLotNumber2(path.get()=="-"?"":path.get());
                    Map<Optional<Date>, List<TReceipt>> expGroup = receipts.stream()
                            .collect(Collectors.groupingBy(event -> Optional.ofNullable(event.getExpirationDate()==null?new Date(2099,1,1):event.getExpirationDate())));
                    expGroup.forEach((k,v)->{
                        Map<String,List<TReceipt>> locGroup = v.stream()
                                .collect(Collectors.groupingBy(TReceipt::getLocationId));
                        locGroup.forEach((k1,v1)-> {

                            TPoDetail poDetail = new TPoDetail();
                            BeanUtils.copyProperties(detail, poDetail);
                            poDetail.setExpirationDate2(v1.get(0).getExpirationDate());
                            poDetail.setLocationId(v1.get(0).getLocationId());
                            poDetail.setStoredAttributeId(v1.get(0).getStoredAttributeId().longValue());
                            poDetail.setActualQty(v1.stream().mapToDouble(TReceipt::getQtyReceived).sum());
                            details.add(poDetail);
                        });
                    });

                });
            });
            po.setDetailList(details);
        });
        return tPoMasters;
    }
    @Override
    public List<TPoMaster> queryConfirmInBoundsByWhseAndClient(String whse, String clientCode) {
        TPoMaster tPoMaster = new TPoMaster();
        tPoMaster.setWhId(whse);
        tPoMaster.setClientCode(clientCode);
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
    public TRcptShipPo queryInfoByWhseAndPoNumber(String whse, String poNumber) {
        TRcptShipPo tRcptShipPo=new TRcptShipPo();
        tRcptShipPo.setPoNumber(poNumber);
        tRcptShipPo.setWhId(whse);
        return tRcptShipPoMapper.selectOne(tRcptShipPo);
    }
    @Override
    public TRcptShip queryInfoByWhseAndShipmentNumber(String whse, String shipmentNumber) {
        TRcptShip tRcptShip=new TRcptShip();
        tRcptShip.setShipmentNumber(shipmentNumber);
        tRcptShip.setWhId(whse);
        return tRcptShipMapper.selectOne(tRcptShip);
    }
    @Override
    public String queryConfirmInBoundsByItemAndWhse (String itemNumber, String whse){
        TItemUom tItemUom = new TItemUom();
        tItemUom.setItemNumber(itemNumber);
        tItemUom.setWhId(whse);
        tItemUom.setUom("EA");
        return tItemUomMapper.selectOne(tItemUom).getClassId();
    }

    @Override
    public TPoDetail queryPoDetailByPoNumberAndWhse(String poNumber, String whse, String lineNumber) {
        TPoDetail tPoDetail=new TPoDetail();
        tPoDetail.setPoNumber(poNumber);
        tPoDetail.setWhId(whse);
        tPoDetail.setLineNumber(lineNumber);
        return tPoDetailMapper.selectOne(tPoDetail);
    }

    @Override
    public TStoAttribCollectionDetail queryTStoAttribByStoredAttributeId(Long storedAttributeId, String attributeId) {
        TStoAttribCollectionDetail tStoAttribCollectionDetail=new TStoAttribCollectionDetail();
        tStoAttribCollectionDetail.setStoredAttributeId(storedAttributeId);
        tStoAttribCollectionDetail.setAttributeId(attributeId);
        return tStoAttribMapper.selectOne(tStoAttribCollectionDetail);
    }

    @Override
    public List<TStoredCount> queryTStoredItemList(String whId) {
        return tStoredItemMapper.queryTStoredItemList(whId);
    }

    @Override
    public List<TReceiptCount> queryReceiptInfoByPoNumberAndWhse(String poNumber, String whse) {
        TReceipt tReceipt=new TReceipt();
        tReceipt.setPoNumber(poNumber);
        tReceipt.setWhId(whse);
        return tReceiptMapper.selectTReceiptInfo(tReceipt);
    }

    @Override
    public String queryConfirmInBoundsByZone (String locationId, String whse){
        TZoneLoca tZoneLoca = new TZoneLoca();
        tZoneLoca.setLocationId(locationId);
        tZoneLoca.setWhId(whse);
        List<TZoneLoca> tZoneLocaList =  tZoneLocaMapper.select(tZoneLoca);
        return tZoneLocaList.get(0).getZone();
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
    @Transactional
    public BaseResponse createInBoundOrder(TPoMaster poMaster) {

        //判断出库订单是否存在
        TPoMaster example = new TPoMaster();
        example.setWhId(poMaster.getWhId());
        example.setPoNumber(poMaster.getPoNumber());
        int count = tPoMasterMapper.selectCount(example);//count 不为0 时候代表运单存在，意思就是数据库有对应数据
        if (count > 0) {
            return BaseResponse.success();
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
    @Transactional
    public BaseResponse createOrUpdateInBoundOrder(TPoMaster poMaster) {

        //判断出库订单是否存在
        TPoMaster example = new TPoMaster();
        example.setWhId(poMaster.getWhId());
        example.setPoNumber(poMaster.getPoNumber());
        TPoMaster count = tPoMasterMapper.selectOne(example);//count 不为0 时候代表运单存在，意思就是数据库有对应数据
        if (count !=null) {
            poMaster.setStatus(count.getStatus());
            tPoMasterMapper.updateByPrimaryKeySelective(poMaster);
            for(int i=0;i<poMaster.getDetailList().size();i++){
                TPoDetail poExample = new TPoDetail();
                poExample.setWhId(poMaster.getDetailList().get(i).getWhId());
                poExample.setPoNumber(poMaster.getDetailList().get(i).getPoNumber());
                poExample.setLineNumber(poMaster.getDetailList().get(i).getLineNumber());
                TPoDetail poCount = tPoDetailMapper.selectOne(poExample);//count 不为0 时候代表运单存在，意思就是数据库有对应数据
                poMaster.getDetailList().get(i).setScheduleNumber(poCount.getScheduleNumber());
                tPoDetailMapper.updateByPrimaryKeySelective(poMaster.getDetailList().get(i));
            }
            //生成运单
            String shipmentNumber = getSequence(poMaster.getWhId(), "SHIPMENT_NUMBER", "Shipment number identifier");
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
                tRcptShipPoDetailMapper.updateByPrimaryKey(tRcptShipPoDetail);
            });
            return BaseResponse.success("订单更新成功!");
        } else {
            tPoMasterMapper.insertSelective(poMaster);
            poMaster.getDetailList().forEach(tPoDetail -> {
                tPoDetailMapper.insertSelective(tPoDetail);
            });
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
    }
    /**
     * 取消入库订单
     *
     * @param orderCode
     * @param whid
     * @return
     */
    @Override
    @Transactional
    public BaseResponse cancelInBoundOrder(String orderCode, String client, String whid) {

        TPoMaster tPoMaster = new TPoMaster();
        tPoMaster.setPoNumber(orderCode);
        tPoMaster.setClientCode(client);
        tPoMaster.setWhId(whid);
        tPoMaster = tPoMasterMapper.selectOne(tPoMaster);
        if (tPoMaster != null) {
            if (tPoMaster.getStatus().equals("D")) {
                return BaseResponse.success();
            } else if (!tPoMaster.getStatus().equals("O")) {
                return BaseResponse.fail("订单号:" + orderCode + "非初始状态,不允许取消!");
            } else {
                tPoMaster.setStatus("D");
                Example up_TPoMaster = new Example(TPoMaster.class);
                up_TPoMaster.createCriteria().andEqualTo("poNumber", orderCode).andEqualTo("whId", whid);
                tPoMasterMapper.updateByExampleSelective(tPoMaster, up_TPoMaster);
            }
        } else {
            return BaseResponse.fail("订单号:" + orderCode + "不存在!");
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
    public void updateInBoundsBack(String whId, String poNumber,String status,String msg,Date date) {
        TPoMaster tPoMaster = new TPoMaster();
        tPoMaster.setIsSendBack(status);
        tPoMaster.setSendBackMsg(msg);
        tPoMaster.setSendBackDate(date);
        tPoMaster.setWhId(whId);
        tPoMaster.setPoNumber(poNumber);
        tPoMasterMapper.updateByPrimaryKeySelective(tPoMaster);
    }
    @Override
    public void updateInBoundsUnback(String whId, String poNumber,String status,String msg,Date date) {
        TPoMaster tPoMaster = new TPoMaster();
        tPoMaster.setIsSendBack(status);
        tPoMaster.setSendBackMsg(msg);
        tPoMaster.setSendBackDate(date);
        tPoMaster.setWhId(whId);
        tPoMaster.setPoNumber(poNumber);
        tPoMasterMapper.updateByPrimaryKeySelective(tPoMaster);
    }
    @Override
    public int updateInOrderCancel(TPoMaster tpoMaster) {
        return tPoMasterMapper.updateInOrderCancel(tpoMaster);
    }

    @Override
    public List<TranLog> queryTranLogByOrderNoAndTranType(String orderNo, String tranType) {
        TranLog tranLog = new TranLog();
        tranLog.setControlNumber(orderNo);
        tranLog.setTranType(tranType);
        return tTranLogMapper.selectTranLogInfo(tranLog);
    }

    @Override
    public TPickDetail queryTPickDetailByPickId(Integer pickId) {
        TPickDetail tPickDetail = new TPickDetail();
        tPickDetail.setPickId(pickId);
        return tPickDetailMapper.selectOne(tPickDetail);
    }

    @Override
    public List<TReceipt> queryTReceiptByPoNumberAndWhId(String poNumber, String WhId) {
        TReceipt tReceipt = new TReceipt();
        tReceipt.setPoNumber(poNumber);
        tReceipt.setWhId(WhId);
        return tReceiptMapper.selectTReceipt(tReceipt);
    }

    @Override
    public List<TReceipt> queryTReceiptByPoNumberAndWhIdAndLoadId(String poNumber, String WhId) {
        TReceipt tReceipt = new TReceipt();
        tReceipt.setPoNumber(poNumber);
        tReceipt.setWhId(WhId);
        return tReceiptMapper.selectTReceiptGroupByLocationId(tReceipt);
    }

    @Override
    public List<TReceipt> queryTReceiptByWhId(String WhId) {
        TReceipt tReceipt = new TReceipt();
        tReceipt.setWhId(WhId);
        return tReceiptMapper.select(tReceipt);
    }

    @Override
    public void updatePopInBoundsBack(Integer id) {
        TblInfoImpReceiptPop tReceipt = new TblInfoImpReceiptPop();
        tReceipt.setProcFlag("1");
        tReceipt.setId(id);
        tblInfoImpReceiptPopMapper.updateById(tReceipt);
    }
    @Override
    public void updateScfInboundsStatus(String whId, String poNumber) {
        TPoMaster tPoMaster = new TPoMaster();
        tPoMaster.setResidentialFlag("Y");
        tPoMaster.setWhId(whId);
        tPoMaster.setPoNumber(poNumber);
        tPoMasterMapper.updateByPrimaryKeySelective(tPoMaster);
    }
    @Override
    public List<TPoMaster> queryScfInbounds(String whse, String clientCode) {
        TPoMaster tOrder = new TPoMaster();
        tOrder.setWhId(whse);
        tOrder.setClientCode(clientCode);
        tOrder.setStatus("C");
        tOrder.setResidentialFlag("N");
        List<TPoMaster> orders = tPoMasterMapper.select(tOrder);
        TPoDetail tOrderDetail = new TPoDetail();
        orders.forEach(order -> {
            tOrderDetail.setWhId(whse);
            tOrderDetail.setPoNumber(order.getPoNumber());
            List<TPoDetail> details = tPoDetailMapper.selectReceivePoDetail(whse,order.getPoNumber());
            order.setDetailList(details);
            TRcptShipPo tRcptShipPo = new TRcptShipPo();
            tRcptShipPo.setWhId(order.getWhId());
            tRcptShipPo.setPoNumber(order.getPoNumber());
            String ShipNo = tRcptShipPoMapper.selectOne(tRcptShipPo).getShipmentNumber();
            TRcptShip tRcptShip = new TRcptShip();
            tRcptShip.setWhId(order.getWhId());
            tRcptShip.setShipmentNumber(ShipNo);
            order.setConfirmDateTime(tRcptShipMapper.selectOne(tRcptShip).getDateReceived());
        });
        return orders;
    }

    @Override
    public List<String> queryPoNumber(String whse,String clientCode) {
        TPoMaster poMaster = new TPoMaster();
        poMaster.setWhId(whse);
        poMaster.setClientCode(clientCode);
        return tPoMasterMapper.queryPoNumber(poMaster);
    }

    @Override
    public List<TblInfoImpReceiptPop> queryConfirmPopInBoundsByCodeWhse(String clientCode, String whId) {
        TblInfoImpReceiptPop tblReceipt = new TblInfoImpReceiptPop();
        tblReceipt.setDepartmant(clientCode);
        tblReceipt.setProcFlag("0");
        tblReceipt.setWhId(whId);
        List<TblInfoImpReceiptPop> tblReceipts = tblInfoImpReceiptPopMapper.select(tblReceipt);
        return tblReceipts;
    }

    @Override
    public TPoMaster queryTPoMasterByWhIdAndPoNumber(String whId, String poNumber) {
        TPoMaster tPoMaster=new TPoMaster();
        tPoMaster.setWhId(whId);
        tPoMaster.setPoNumber(poNumber);
        return tPoMasterMapper.selectOne(tPoMaster);
    }



    @Override
    public List<TStoredItem> queryTStoredItemByWhId(String whId) {
        TStoredItem storedItem=new TStoredItem();
        storedItem.setWhId(whId);
        return storageStockMapper.queryTStoredItemByWhId(storedItem);
    }

    @Override
    public List<TPoMaster> queryPoToIWMS(String whse, String clientCode) {
        TPoMaster tPoMaster = new TPoMaster();
        tPoMaster.setWhId(whse);
        tPoMaster.setClientCode(clientCode);
        tPoMaster.setStatus("O");
        tPoMaster.setResidentialFlag("N");
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
    public List<TStoredItem> queryTStoredItemByItemNumber(String itemNumber) {
        TStoredItem storedItem=new TStoredItem();
            storedItem.setItemNumber(itemNumber);
        return tStoredItemMapper.queryTStoredItemByItemNumber(storedItem);
    }

    @Override
    public void confirmInbound(String whse,String clientCode,String poNumber,String lineNumber,String itemNumber,Long qty) {
        try {
            TPoMaster tPoMaster = new TPoMaster();
            tPoMaster.setWhId(whse);
            tPoMaster.setPoNumber(poNumber);
            tPoMaster = tPoMasterMapper.selectOne(tPoMaster);

            if (tPoMaster != null) {
                tPoMaster.setStatus("C");
                tPoMasterMapper.updateByPrimaryKeySelective(tPoMaster);
            }

            TReceipt tReceipt = new TReceipt();
            tReceipt.setWhId(whse);
            tReceipt.setLineNumber(lineNumber);
            tReceipt.setPoNumber(poNumber);
            tReceipt = tReceiptMapper.selectOne(tReceipt);

            if (tReceipt == null) {
                tReceipt = new TReceipt();
                tReceipt.setWhId(whse);
                tReceipt.setLineNumber(lineNumber);
                tReceipt.setPoNumber(poNumber);
                tReceipt.setReceiptId("999999999");
                tReceipt.setStatus("C");
                tReceipt.setItemNumber(clientCode + "-" + itemNumber);
                tReceipt.setScheduleNumber(new Integer(lineNumber));
                tReceipt.setQtyReceived(qty.doubleValue());
                tReceipt.setQtyDamaged(0D);
                tReceipt.setStoredAttributeId(70238);

                tReceiptMapper.insertSelective(tReceipt);

            }

        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public List<BigScreen> queryBigScreen(QueryCity queryCity) {
        return bigScreenMapper.queryBigScreen(queryCity);
    }
    @Override
    public List<BigScreenStorck> queryBigScreenStorck(QueryCity queryCity) {
        return bigScreenMapper.queryBigScreenStorck(queryCity);
    }

    @Override
    public List<TStoredItem> queryStoredItemByWhId(List<String> list) {

        return tStoredItemMapper.queryStoredItemByWhId(list);
    }

    @Override
    public List<TWork> queryTWorkByWhId(String whId,String clientCode) {
        TWork work=new TWork();
        work.setWhId(whId);
        work.setClientCode(clientCode);
        return workMapper.queryTWorkByWhId(work);
    }


}
