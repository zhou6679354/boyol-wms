package org.shrek.hadata.commons.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@Slf4j
public class SFtpClientUtil {
	ChannelSftp sftpClient;
	private Session session;  
	 private String host;
	 private int port;
	 private String userName;
	 private String password;

	 public SFtpClientUtil(String host,int port,String userName,String password){
	  this.host=host;
	  this.port=port;
	  this.userName=userName;
		 this.password=password;
	 }
	 /**
	  * 链接到服务器
	  */
	 public boolean open(){
		 boolean result=true;
		try {
			JSch jsch = new JSch();
			log.info("开始连接sftp了host:{} username:{}", host, userName);
			session = jsch.getSession(userName, host, port);
			log.info("会话已经建立了！");
			if (password != null) {
				session.setPassword(password);
			}
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			log.info("会话连接成功");
			this.sftpClient=(ChannelSftp) session.openChannel("sftp");
			log.info("传输通道以连接成功");
			this.sftpClient.connect();
			this.sftpClient.setFilenameEncoding("UTF-8");
			log.info(String.format("sftp连接成功host:[%s] port:[%s]", host, port));
		} catch (Exception e) {
			result=false;
			log.error("连接sftp异常{}", e.toString());
			if(sftpClient!=null&&sftpClient.isConnected())
				sftpClient.disconnect();
			log.info("关闭连接成功");
		}
		 return result;
	 }
	 
	   /** 
	     * 将输入流的数据上传到sftp作为文件  
	     * @param directory 上传到该目录  
	     * @param sftpFileName  sftp端文件名  
	     * @throws SftpException
	     * @throws Exception  
	     */    
	    public void upload(String directory, String sftpFileName, InputStream input) throws SftpException{  
	        try {    
	        	sftpClient.cd(directory);  
	        } catch (SftpException e) {  
	            log.warn("文件夹不存在");  
	            sftpClient.mkdir(directory);  
	            sftpClient.cd(directory);  
	        }  
	        sftpClient.put(input, sftpFileName);  
	        log.info("文件:{}上传成功" , sftpFileName);  
	    }  
	    
	    /**  
	     * 上传单个文件 
	     * @param directory 上传到sftp目录  
	     * @param uploadFile 要上传的文件,包括路径  
	     * @throws FileNotFoundException 
	     * @throws SftpException 
	     * @throws Exception 
	     */  
	    public void upload(String directory, String uploadFile) throws FileNotFoundException, SftpException{  
	        File file = new File(uploadFile);  
	        upload(directory, file.getName(), new FileInputStream(file));  
	    }  
	    /** 
	     * 下载文件  
	     * @param directory  下载目录  
	     * @param downloadFile 下载的文件 
	     * @param saveFile 存在本地的路径 
	     * @throws SftpException 
	     * @throws FileNotFoundException 
	     * @throws Exception 
	     */    
	    public boolean download(String directory, String downloadFile, String saveFile) throws SftpException, FileNotFoundException{
	        if (directory != null && !"".equals(directory)) {
	        	sftpClient.cd(directory);
	        }
	        try {
	        	File file = new File(saveFile);  
		        sftpClient.get(downloadFile, new FileOutputStream(file));  
		        log.info("文件:{}下载成功" , downloadFile); 
		        return true;
			} catch (Exception e) {
				log.error("下载失败，原因{}" , e.toString());
				return false;
			}
	    }  

	    /** 
	     * 列出目录下的文件 
	     * @param directory 要列出的目录 
	     * @return
	     * @throws SftpException 
	     */  
	    @SuppressWarnings("unchecked")
		public List<String> fileNameList(String directory) throws SftpException {
			Vector fileList =sftpClient.ls(directory);
			List<String> fileNameList = new ArrayList<String>();
			Iterator it = fileList.iterator();
			while(it.hasNext()) {
				String fileName = ((ChannelSftp.LsEntry)it.next()).getFilename();
				if(".".equals(fileName) || "..".equals(fileName)){
					continue;
				}
				fileNameList.add(fileName);
			}
			return fileNameList;
	    } 
	  /** 
	     * 删除文件 
	     * @param deleteFile  要删除的文件
	     * @throws SftpException 
	     * @throws Exception 
	     */  
	    public void delete(String deleteFile) throws SftpException{
			try {
	    	sftpClient.rm(deleteFile);
			log.info("文件:{}删除成功" , deleteFile);
			} catch (Exception e) {
				log.error("文件删除失败，失败原因" , e.toString());
			}
		}

	  /**
	   * 关闭链接
	   */
	  public void close(){
	   try{
	       if(sftpClient!=null&&sftpClient.isConnected())
	    	   sftpClient.disconnect();
		   log.info("关闭连接成功");
	   }catch(Exception e){
		   log.info("关闭连接失败，失败原因{}",e.toString());
	   }
	  }
	}
