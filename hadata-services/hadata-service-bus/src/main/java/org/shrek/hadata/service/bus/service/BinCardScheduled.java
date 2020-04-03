package org.shrek.hadata.service.bus.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.shrek.hadata.commons.util.SFtpClientUtil;
import org.shrek.hadata.service.iwms.model.TClient;
import org.shrek.hadata.service.iwms.model.TblInfoImpReceiptPop;
import org.shrek.hadata.service.iwms.service.InBoundService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class BinCardScheduled {
    @Reference(version = "1.0.0",
            check = false, registry = "zookeeper", timeout = 15000)
    InBoundService inBoundService;
    @Value("${ftp.host.port}")
    private int port;
    @Value("${ftp.host}")
    private String host;
    @Value("${ftp.host.userName}")
    private String userName;
    @Value("${ftp.host.userPassword}")
    private String userPassword;
    @Value("${ftp.ftpDirectory}")
    private String ftpDirectory;
    @Value("${local.fileDirectory}")
    private String fileDirectory;
    SFtpClientUtil sftpClientUtil;
    /**
     * 入库反馈
     */
    public void returnOrder() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date();
        String dateStr=dateFormat.format(now);
        String filename="UT_InBoundDetail_"+dateStr+".txt";
        String filepath=fileDirectory+"/"+filename;
        File file = new File(filepath);
        List<TClient> tClients = inBoundService.queryInBoundsByFax("POP");
        List<TblInfoImpReceiptPop> tblReceipts = new ArrayList<TblInfoImpReceiptPop>();
        if((!tClients.isEmpty()) && tClients.size() >0 ){
            String clientCode = "";
            String whId = "";
            for(int x=0;x<tClients.size();x++){
                clientCode=tClients.get(x).getClientCode();
                whId = tClients.get(x).getWhId();
                List<TblInfoImpReceiptPop> tblReceiptList=inBoundService.queryConfirmPopInBoundsByCodeWhse(clientCode, whId);
                if(tblReceiptList.size()>0){
                    tblReceipts.addAll(tblReceiptList);
                }
            }
        }
        FileOutputStream fos=null;
        OutputStreamWriter osw=null;
        BufferedWriter bw=null;
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }


            if(tblReceipts.size()>0&&tblReceipts!=null) {
                file.createNewFile();
                fos = new FileOutputStream(file);
                osw = new OutputStreamWriter(fos, "GBK");
                bw=new BufferedWriter(osw);
                SimpleDateFormat createdDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (int i=0;i<tblReceipts.size();i++){
                    String createdDateStr=createdDateFormat.format(tblReceipts.get(i).getInBoundTime());
                    String str=tblReceipts.get(i).getCustomeOrder()+"|"+tblReceipts.get(i).getCargoCode()+"|"+
                            tblReceipts.get(i).getCargoName()+"|"+tblReceipts.get(i).getCommodityCode()+"|"+tblReceipts.get(i).getDepartmant()+"|"+
                            createdDateStr+"|"+tblReceipts.get(i).getNum()+"|"+tblReceipts.get(i).getGiftsSource()+"|";
                    bw.write(str+"\r\n");
                    log.info("正在写入第："+i+"行"+str);
                }
                bw.flush();
                log.info("写入文件{}成功" ,filename );
                for (int i=0;i<tblReceipts.size();i++) {
                    inBoundService.updatePopInBoundsBack(tblReceipts.get(i).getId());
                }
                log.info("更新数据回传状态成功，更新数据条数{}" ,tblReceipts.size());
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
                sftpClientUtil.upload(ftpDirectory,filepath);
                sftpClientUtil.close();
            }
        } catch (IOException e) {
            log.error("写入文件异常，异常原因",e.toString());
        } catch (SftpException e) {
            log.error("上传文件异常，异常原因",e.toString());
        } finally {
            try {
                if(bw != null) {
                    bw.close();
                }
                if(osw != null) {
                    osw.close();
                }
                if(fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                log.error("输入流关闭异常，异常原因",e.toString());
            }
        }
    }
}