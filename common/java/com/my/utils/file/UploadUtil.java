package com.my.utils.file;

import com.common.utils.config.GlobalConfig;
import com.common.utils.datetime.DateUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class UploadUtil {
    /**
     * FTP上传
     * @param request
     * @param response
     *
     * @param picPath
     * @return
     * @throws IOException
     */
    public static String ftpUpload(HttpServletRequest request, HttpServletResponse response,String picPath) throws IOException {
        boolean flag = false;
        String newfilename = "";
        // 创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        // 判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                // 取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                	String myFileName = file.getOriginalFilename();//文件名称
                	newfilename = DateUtil.getTime() + myFileName.substring(myFileName.lastIndexOf("."));//新文件名
                	
                	String path = request.getSession().getServletContext().getRealPath("");//当前项目物理路径
                	File fileTemp = new File(path+File.separator+"temp");//临时保存图片路径
                	if(!fileTemp.exists()) fileTemp.mkdirs();
                	
                	//对图片文件进行压缩
                	FilesZip.compressPic(file.getInputStream(),path+File.separator+"temp"+File.separator+newfilename);
                	
                    FtpUtil ftp = new FtpUtil();
                    ftp.connectServer(GlobalConfig.PIC_SERVER_IP, FTPClient.DEFAULT_PORT, GlobalConfig.PIC_SERVER_USER, GlobalConfig.PIC_SERVER_PWD, picPath);
                    
                    flag = ftp.uploadFile(path+File.separator+"temp"+File.separator+newfilename, newfilename);
                }
            }
        }
        if (flag) {
            return newfilename;
        } else {
            return "";
        }
    }
    
    
   /* *//**
	 * Spring 上传通用方法，返回上传文件的文件名
	 * 
	 * @param request
	 * 
	 * @param reponse
	 * 
	 * @param path
	 *            文件保存路径
	 * @param flag: 1:影片，2：影院，3：  商品          
	 * @return
	 *//*
	public static String upload(HttpServletRequest request,
			HttpServletResponse response, String path,String flag) {
		response.setContentType("text/html");
		// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				// 取得上传文件
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					// 取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
					if (myFileName.trim() != "") {
						// 重命名上传后的文件名
						String newFileName = DateUtil.getTime()
								+ myFileName.substring(myFileName
										.lastIndexOf("."));
						String fileName = newFileName;
						
						Properties props = System.getProperties(); //获得系统属性集
						String osName = props.getProperty("os.name"); //操作系统名称
						
						System.out.println(osName);
						
						// 定义上传路径
						String realPath =  "";
						if(osName.contains("Windows")){
							realPath = request.getSession().getServletContext().getRealPath("/") + "\\" + path + "\\" +fileName;  //windows路径
						}
						else if(osName.contains("Linux")){
							if(flag.equals("1")){//商品上传图片
								realPath = ConfigurationUtil.getConfigurationValue("filepath.template.mallmanage") + fileName; //linux路径
							}
							else if(flag.equals("2")){//广告上传图片
								realPath = ConfigurationUtil.getConfigurationValue("filepath.template.mallmanage") + fileName; //linux路径
							}
						}
						
			//			String realPath =UploadUtil.getRootPath()
			//					+ System.getProperty("file.separator") + path + fileName;  //兼容Linux和windows路径
						
						File localFile = new File(realPath);
						try {
							file.transferTo(localFile);
							return newFileName;
						} catch (Exception e) {
							e.printStackTrace();
							return "";
						}
					}
				}
			}
		}
		return "";
	}
	
	*//***
	 * copy图片文件
	 * @param srcfile:源图片文件
	 * @param destfile:目的图片文件
	 *//*
	public static void copyImagFile(File srcfile, File destfile){
		
		FileInputStream fi = null;

        FileOutputStream fo = null;

        FileChannel in = null;

        FileChannel out = null;
        
		try {
			fi = new FileInputStream(srcfile);
            fo = new FileOutputStream(destfile);
            in = fi.getChannel();//得到对应的输入流文件通道
            out = fo.getChannel();//得到对应的输出流文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			 try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
//	public static void main(String[] args) {
//		copyImagFile(new File("c:\\test\\111.png"), new File("d:\\test\\111.png"));
//	}
*/    
}
