package org.shrek.hadata.service.bus.service;


import com.alibaba.dubbo.config.annotation.Reference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.shrek.hadata.commons.util.FileUtil;
import org.shrek.hadata.service.iwms.model.TClient;
import org.shrek.hadata.service.iwms.model.TOrder;
import org.shrek.hadata.service.iwms.model.TOrderDetail;
import org.shrek.hadata.service.iwms.model.TWhse;
import org.shrek.hadata.service.iwms.service.ClientService;

import org.shrek.hadata.service.iwms.service.OutBoundService;
import org.shrek.hadata.service.iwms.service.WarehouseService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Slf4j
@Service
public class OTMSOrderToWmsService {
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    WarehouseService warehouseService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    OutBoundService outBoundService;
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    ClientService clientService;
    public void readOrderToWms(){
        ArrayList<String> listFileName=new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FileUtil.getAllFileName("E:\\OTMSTOWMS\\",listFileName);
        for(int i=0;i<listFileName.size();i++){
            TClient client=null;
            TWhse warehouse=null;
            TOrder order=new TOrder();
            List<TOrderDetail> orderDetails=new ArrayList<TOrderDetail>();
            File file = new File(listFileName.get(i));
            if(file.isFile() && file.exists()) {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "GBK");
                    BufferedReader bufferReader = new BufferedReader(inputStreamReader);
                    String lineStr = null;
                    int lineNum=1;
                    try {
                        while((lineStr = bufferReader.readLine()) != null) {
                        if(lineNum==1){
                            lineNum++;
                            continue;
                        }else{
                            //每一行的分隔符替换成数组分隔符便于转译
                            String contextString= lineStr.replace("|", ",");
                            //根据分隔符转换成数组
                            String[] contextArr= contextString.split(",");
                            //转换成List(因其有序)
                            List<String> contextList=new ArrayList<String>();
                            contextList.addAll(Arrays.asList(contextArr));
                            if(lineNum==2){
                                warehouse=warehouseService.getWarehouseByWhId("304");
                                client=clientService.getClientByWhAndCode(warehouse.getWhId(), contextList.get(4));
                                order.setOrderNumber(contextList.get(1));
                                order.setStoreOrderNumber(contextList.get(2));
                                order.setDisplayOrderNumber(contextList.get(3));
                                order.setClientCode(client.getClientCode());
                                order.setDeliveryAddr1(contextList.get(6));
                                order.setTypeId(1154);
                                order.setStatus("N");
                                order.setPartialOrderFlag("N");
                                order.setShipToResidentialFlag("N");
                                order.setBackorder("N");
                                order.setOrderDate(sdf.parse(contextList.get(14)));
                                order.setPriority("10");
                                order.setWhId("304");
                                order.setDeliveryAddr2(contextList.get(6));
                                order.setDeliveryAddr3(contextList.get(6));
                                order.setDeliveryName(contextList.get(7));
                                order.setDeliveryPhone(contextList.get(8));
                                order.setShipToAddr1(contextList.get(10));
                                order.setShipToAddr2(contextList.get(10));
                                order.setShipToAddr3(contextList.get(10));
                                order.setShipToName(contextList.get(11));
                                order.setShipToPhone(contextList.get(12));
                                order.setCarrierNumber(contextList.get(13));
                                order.setCreateDate(sdf.parse(contextList.get(14)));
                                TOrderDetail orderDetail=new TOrderDetail();
                                orderDetail.setItemNumber(client.getClientCode()+"-"+contextList.get(15));
                                orderDetail.setItemDescription(contextList.get(16));
                                orderDetail.setQty(Double.valueOf(contextList.get(17)));
                                orderDetail.setAfoPlanQty(Double.valueOf(contextList.get(17)));
                                orderDetail.setOrderNumber(contextList.get(1));
                                orderDetail.setLineNumber(String.valueOf(lineNum-1));
                                orderDetail.setWhId("304");
                                orderDetails.add(orderDetail);
                                lineNum++;
                            }else{
                                TOrderDetail orderDetail=new TOrderDetail();
                                orderDetail.setItemNumber(client.getClientCode()+"-"+contextList.get(15));
                                orderDetail.setItemDescription(contextList.get(16));
                                orderDetail.setQty(Double.valueOf(contextList.get(17)));
                                orderDetail.setAfoPlanQty(Double.valueOf(contextList.get(17)));
                                orderDetail.setOrderNumber(contextList.get(1));
                                orderDetail.setLineNumber(String.valueOf(lineNum-1));
                                orderDetail.setWhId("304");
                                orderDetails.add(orderDetail);
                                lineNum++;
                            }
                        }
                        }
                        order.setOrderDetailList(orderDetails);
                        outBoundService.createOutBoundOrder(order);
                        bufferReader.close();
                        inputStreamReader.close();
                        File startFile=new File(listFileName.get(i));
                        File endFile=new File("E:\\OTMSFILE\\"+ startFile.getName());
                        try {
                            FileUtils.moveFile(startFile,endFile);
                            log.info("文件移动成功！目标路径：{"+endFile.getAbsolutePath()+"}");
                        }catch(Exception e) {
                            log.error("文件移动出现异常！起始路径：{"+startFile.getAbsolutePath()+"}");
                        }
                        if(startFile.delete()){
                            log.info("文件删除成功，文件名:"+startFile.getName());
                        }

                    } catch (IOException e) {
                        log.error("文件读取出现错误!");
                    }
                } catch (UnsupportedEncodingException e) {
                    log.error("文件编码错误!");
                } catch (FileNotFoundException e) {
                    log.error("找不到文件!");
                }catch (ParseException e) {
                    log.error("时间格式转换异常!");
                }
            }else {
                log.error("文件不是文件类型或者文件找不到!");
            }
        }
    }
}
