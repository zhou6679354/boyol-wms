package org.shrek.hadata.service.bus.service;

import com.alibaba.dubbo.config.annotation.Reference;
import lombok.extern.slf4j.Slf4j;
import org.shrek.hadata.commons.util.SFtpClientUtil;
import org.shrek.hadata.service.iwms.model.*;
import org.shrek.hadata.service.iwms.service.ClientService;
import org.shrek.hadata.service.iwms.service.InBoundService;
import org.shrek.hadata.service.iwms.service.OutBoundService;
import org.shrek.hadata.service.iwms.service.WarehouseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class BinCardService {
	@Reference(version = "1.0.0",
			check = false, registry = "zookeeper", timeout = 15000)
	WarehouseService warehouseService;
	@Reference(version = "1.0.0",
			check = false, registry = "zookeeper", timeout = 15000)
	OutBoundService outBoundService;
	@Reference(version = "1.0.0",
			check = false, registry = "zookeeper", timeout = 15000)
	InBoundService inBoundService;
	@Reference(version = "1.0.0",
			check = false, registry = "zookeeper", timeout = 15000)
	ClientService clientService;
	SFtpClientUtil sftpClientUtil;
	@Value("${ftp.host.port}")
	private int port;
	@Value("${ftp.host}")
	private String host;
	@Value("${ftp.host.userName}")
	private String userName;
	@Value("${ftp.host.userPassword}")
	private String userPassword;
	@Value("${ftp.ftpReadDirectory}")
	private String ftpDirectory;
	@Value("${local.fileDirectory}")
	private String fileDirectory;





	/**
	 *BinCard定任务
	 */
	@SuppressWarnings({ "unchecked"})
	public void createOrder(){
		log.info("连接惠氏FTP");
		//初始化FTP连接工具
		sftpClientUtil=new SFtpClientUtil(host,port,userName,userPassword);
		//开启FTP连接 成功true反之false
		boolean openResult=sftpClientUtil.open();
		if(openResult){
			log.info("连接{}SFTP成功" , host);
		}else{
			log.error("连接{}SFTP失败" , host);
		}
		//获取FTP下文件名列表
		List<String> fileNameList=new ArrayList<String>();
		try {
			List<String> fileNames = sftpClientUtil.fileNameList(ftpDirectory);
			for (int i=0;i<fileNames.size();i++){
				String fn=fileNames.get(i).toLowerCase();
				if(fn.startsWith("bincardto3pl")||fn.startsWith("bincardto3pldoea")){
				fileNameList.add(fileNames.get(i));
				}
			}
		} catch (Exception e) {
			log.error("获取文件列表异常{}" , e.toString());
		}
		for (int x = 0; x < fileNameList.size(); x++) {
			log.info("正在下载文件："+fileNameList.get(x)+"总计"+fileNameList.size()+"个文件");
			//FTP下要下载的文件名
			String dowloadFilePath=fileNameList.get(x);
			//FTP文件下载到本地的全路径
			String filePath=fileDirectory+"/"+dowloadFilePath;
			try {
				if (x == 0) {
					//执行下载文件
					sftpClientUtil.download(ftpDirectory, dowloadFilePath, filePath);
				} else {
					//执行下载文件
					sftpClientUtil.download(null, dowloadFilePath, filePath);
				}
				log.info("正在读取文件："+fileNameList.get(x)+"总计"+fileNameList.size()+"个文件");
				try {
					//FTP下要下载的文件名
					String readFileName=fileNameList.get(x);
					String readFilePath=fileDirectory+"/"+readFileName;
					//获取文件读取流
					InputStreamReader fReader = new InputStreamReader(new FileInputStream(readFilePath), "GBK");
					//转换成缓冲字符流
					BufferedReader bf = new BufferedReader(fReader);
					String context = null;
					//初始化 出库主单序列 与 明细单序列
					List<TOrder> tOrderList=new ArrayList<TOrder>();
					List<TOrderDetail> tOrderDetailsList=new ArrayList<TOrderDetail>();
					List<TPoMaster> tPoMasterList=new ArrayList<TPoMaster>();
					List<TPoDetail> tPoMasterDetailList=new ArrayList<TPoDetail>();
					//一行一行循环读取
					while ((context = bf.readLine()) != null) {
						//每一行的分隔符替换成数组分隔符便于转译
						String contextString= context.replace("|", ",");
						//根据分隔符转换成数组
						String[] contextArr= contextString.split(",");
						//转换成List(因其有序)
						List<String> contextList=new ArrayList<String>();
						contextList.addAll(Arrays.asList(contextArr));
						while(contextList.size()<22){
							contextList.add("");
						}
						TClient client=null;
						TWhse warehouse=null;
						String queryWhId=null;
						if(readFileName.contains("BinCardto3plSamplepo")||readFileName.contains("BinCardto3plGiftspo")){
							TPoMaster tpoMaster =new TPoMaster();
							try{
								if(contextList.get(17).equals("苏州RDC")){
									queryWhId="304";
								}else if(contextList.get(17).equals("广州RDC")){
									queryWhId="301";
								}else if(contextList.get(17).equals("成都RDC")){
									queryWhId="302";
								}
								warehouse=warehouseService.getWarehouseByWhId(queryWhId);
								client=clientService.getClientByWhAndCode(warehouse.getWhId(), contextList.get(3));
								if(warehouse==null){
									log.error("获取不到入库单仓库信息，文件名"+readFileName+"单号"+contextList.get(1)+"仓库编码304");
								}
								if(client==null){
									log.error("获取不到入库单货主信息，文件名"+readFileName+"单号"+contextList.get(1)+"货主"+contextList.get(3));
								}
							} catch (Exception e){
								log.error("获取客户信息失败！失败原因：{}",e.toString());
							}
							//操作类型
							if(contextList.contains("采购入库")){
								tpoMaster.setTypeId(1119);
							}else if(contextList.contains("转仓入库")){
								tpoMaster.setTypeId(1120);
							}else if(contextList.contains("投诉入库")){
								tpoMaster.setTypeId(2532);
							}else if(contextList.contains("惠营养入库")){
								tpoMaster.setTypeId(2535);
							}
							//主单号
							tpoMaster.setPoNumber(contextList.get(1));
							//仓库ID
							tpoMaster.setWhId(client.getWhId());
							//时间格式
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							ParsePosition pos = new ParsePosition(0);
							//将字符串转换成时间
							Date createDate = formatter.parse(contextList.get(2), pos);
							//入库时间
							tpoMaster.setCreateDate(createDate);
							//货主
							tpoMaster.setClientCode(client.getClientCode());
							//收货人
							tpoMaster.setShipFromName(contextList.get(10));
							//收货人联系方式
							tpoMaster.setShipFromPhone(contextList.get(11));
							//送货城市
							tpoMaster.setShipFromCity(contextList.get(8));
							//送货地址
							tpoMaster.setShipFromAddr1(contextList.get(9));
							//备注
							tpoMaster.setServiceLevel(contextList.get(20));
							//状态
							tpoMaster.setStatus("O");
							//明细信息
							TPoDetail detail = new TPoDetail();
							//主单号
							detail.setPoNumber(contextList.get(1));
							//明细单号-商品代码代码
							detail.setItemNumber(client.getClientCode()+"-"+contextList.get(12));
							//数量
							detail.setQty(Double.valueOf(contextList.get(14)).doubleValue());
							//仓库ID
							detail.setWhId(client.getWhId());
							tPoMasterList.add(tpoMaster);
							tPoMasterDetailList.add(detail);
						}else if(readFileName.contains("BinCardto3pldo")||readFileName.contains("BinCardTO3plSampledo")||readFileName.contains("BinCardTO3plGiftsdo")){
							if(contextList.get(0).equals("H00")){
								TOrder tOrder = new TOrder();
								try{
									if(contextList.get(17).equals("苏州RDC")){
										queryWhId="304";
									}else if(contextList.get(17).equals("广州RDC")){
										queryWhId="301";
									}else if(contextList.get(17).equals("成都RDC")){
										queryWhId="302";
									}
									warehouse=warehouseService.getWarehouseByWhId(queryWhId);
									client=clientService.getClientByWhAndCode(warehouse.getWhId(), contextList.get(3));
									if(warehouse==null){
										log.error("获取不到出库单仓库信息，文件名"+readFileName+"单号"+contextList.get(1)+"仓库编码304");
									}
									if(client==null){
										log.error("获取不到出库单货主信息，文件名"+readFileName+"单号"+contextList.get(1)+"货主"+contextList.get(3));
									}
								} catch (Exception e){
									log.error("获取客户信息失败！失败原因：{}",e.toString());
								}
								//主单号
								tOrder.setOrderNumber(contextList.get(2));
								//操作类型
								if(contextList.contains("正常出库")){
									tOrder.setTypeId(1154);
								}else if(contextList.contains("转仓出库")){
									tOrder.setTypeId(1155);
								}else if(contextList.contains("投诉出库")){
									tOrder.setTypeId(2529);
								}else if(contextList.contains("惠营养出库")){
									tOrder.setTypeId(2533);
								}
								//仓库ID
								tOrder.setWhId(client.getWhId());
								//时间格式
								SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								ParsePosition pos = new ParsePosition(0);
								//将字符串转换成时间
								Date createDate = formatter.parse(contextList.get(8), pos);
								//入库时间
								tOrder.setCreateDate(createDate);
								//货主
								tOrder.setClientCode(client.getClientCode());
								//接收人
								tOrder.setShipToName(contextList.get(9));
								//接收人联系方式
								tOrder.setShipToPhone(contextList.get(10));
								//发货城市
								tOrder.setShipToCity(contextList.get(4));
								//发货地址
								tOrder.setShipToAddr2(contextList.get(6));
								//申请人
								tOrder.setBillToName(contextList.get(15));
								//申请人联系方式
								tOrder.setBillToPhone(contextList.get(16));
								//主档GUID
								tOrder.setInterfaceId(contextList.get(18));
								tOrder.setServiceLevel(contextList.get(5));
								//状态
								tOrder.setStatus("N");
								tOrderList.add(tOrder);
							}else{
								//明细信息
								TOrderDetail detail = new TOrderDetail();
								//默认实际数量
								detail.setAfoPlanQty(Double.valueOf(contextList.get(5)).doubleValue());
								//主档GUID
								detail.setBOLCode(contextList.get(7));
								detail.setItemNumber(contextList.get(3));
								//数量
								detail.setQty(Double.valueOf(contextList.get(5)).doubleValue());
								tOrderDetailsList.add(detail);
							}
						}
					}
					fReader.close();
					bf.close();
					if(readFileName.contains("BinCardto3plSamplepo")||readFileName.contains("BinCardto3plGiftspo")){
						//入库主单去重复
						tPoMasterList=repeatOrder(tPoMasterList);
						//匹配入库主单与明细单
						inOrderMatch(tPoMasterList,tPoMasterDetailList,readFileName);
					}else if(readFileName.contains("BinCardto3pldo")||readFileName.contains("BinCardTO3plSampledo")||readFileName.contains("BinCardTO3plGiftsdo")){
						//匹配出库主单与明细单
						outOrderMatch(tOrderList,tOrderDetailsList,readFileName);
					}
					sftpClientUtil.delete(fileNameList.get(x));
				} catch (Exception e) {
					log.error("任务执行失败！失败原因：{}",e.toString());
				}
			} catch (Exception e) {
				log.error("任务执行失败！失败原因：{}",e.toString());
			}
		}
		//关闭连接
		sftpClientUtil.close();
	}
	/**
	 *匹配出库主单和明细单
	 */
	public void outOrderMatch(List<TOrder> orderList,List<TOrderDetail> orderDetailList,String dowloadFilePath){
		for (int i = 0; i < orderList.size(); i++) {
			TOrder order=orderList.get(i);
			List<TOrderDetail> orderDetails=new ArrayList<TOrderDetail>();
			for (int j = 0; j < orderDetailList.size(); j++) {
				TOrderDetail orderDetail=orderDetailList.get(j);
				if(orderDetail.getBOLCode().equals(order.getInterfaceId())){
					orderDetail.setOrderNumber(order.getOrderNumber());
					orderDetail.setLineNumber(String.valueOf(j+1));
					//明细单号-商品代码
					orderDetail.setItemNumber(order.getClientCode()+"-"+orderDetail.getItemNumber());
					orderDetail.setWhId(order.getWhId());
					orderDetails.add(orderDetail);
				}
			}
			order.setOrderDetailList(orderDetails);
		}
		for(int i = 0; i < orderList.size(); i++){
			TOrder realOrder=orderList.get(i);
			realOrder.setInterfaceId(null);
			for (int j = 0; j < realOrder.getOrderDetailList().size(); j++) {
				realOrder.getOrderDetailList().get(j).setBOLCode(null);
			}
			try {
				outBoundService.createOutBoundOrder(realOrder);
				log.info("出库单入库成功，文件名"+dowloadFilePath+"单号"+realOrder.getOrderNumber());
			} catch (Exception e) {
				log.error("出库单入库失败，文件名"+dowloadFilePath+"单号"+realOrder.getOrderNumber());
				log.error("出库单入库异常！异常原因：{}",e.toString());
			}
		}
	}
	/**
	 *匹配入库主单和明细单
	 */
	public void inOrderMatch(List<TPoMaster> poMasterList,List<TPoDetail> poMasterDetailList,String dowloadFilePath){
		for (int i = 0; i < poMasterList.size(); i++) {
			TPoMaster poMaster=poMasterList.get(i);
			List<TPoDetail> poDetailList=new ArrayList<TPoDetail>();
			for (int j = 0; j < poMasterDetailList.size(); j++) {
				TPoDetail tPoDetail=poMasterDetailList.get(j);
				if(tPoDetail.getPoNumber().equals(poMaster.getPoNumber())){
					tPoDetail.setLineNumber(String.valueOf(j+1));
					poDetailList.add(tPoDetail);
				}
			}
			poMaster.setDetailList(poDetailList);
			try {
				inBoundService.createInBoundOrder(poMaster);
				log.info("入库单入库成功，文件名"+dowloadFilePath+"单号"+poMaster.getPoNumber());
			} catch (Exception e) {
				log.error("入库单入库失败，文件名"+dowloadFilePath+"单号"+poMaster.getPoNumber());
				log.error("入库单入库异常！异常原因：{}",e.toString());
			}
		}
	}
	/**
	 *入库主单去重复
	 */
	public List<TPoMaster> repeatOrder(List<TPoMaster> tPoMasterList){
		for(int i = 0; i < tPoMasterList.size()-1; i++){
			for(int y = tPoMasterList.size()-1; y > i; y--){
				if(tPoMasterList.get(i).getPoNumber().equals(tPoMasterList.get(y).getPoNumber())){
					try {
						tPoMasterList.remove(y);
					} catch (Exception e) {
						log.error("入库单去重复失败！失败原因：{}",e.toString());
					}
				}
			}
		}
		return tPoMasterList;
	}
}
