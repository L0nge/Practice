package com.my.utils.io;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>类名称：</b>FTPUtils<br/>
 * <b>类描述：</b>FTP工具类<br/>
 */
public class FtpUtil {
	private FTPClient ftpClient;

	public static final int BINARY_FILE_TYPE = FTP.BINARY_FILE_TYPE;

	public static final int ASCII_FILE_TYPE = FTP.ASCII_FILE_TYPE;

	private static final Logger log = LoggerFactory.getLogger(FtpUtil.class);

	/**
	 * 
	 * <b>描述：</b>使用详细信息进行服务器连接<br/>
	 * 
	 * @param server
	 *            服务器地址名称
	 * @param port
	 *            端口号
	 * @param user
	 *            用户名
	 * @param password
	 *            用户密码
	 * @param path
	 *            转移到FTP服务器目录
	 * @throws SocketException
	 * @throws IOException
	 * @author 陶俊清 <b>创建时间：</b>2015年11月27日 下午2:29:10<br/>
	 */
	public void connectServer(String server, int port, String user, String password, String path)
			throws SocketException, IOException {
		ftpClient = new FTPClient();
		ftpClient.connect(server, port);
		log.info("Connected to " + server + ".");
		// 连接成功后的回应码
		log.info(ftpClient.getReplyCode() + "");
		ftpClient.login(user, password);
		if (path != null && path.length() != 0) {
			ftpClient.changeWorkingDirectory(path);
		}
		ftpClient.setBufferSize(1024);// 设置上传缓存大小
		ftpClient.setControlEncoding("UTF-8");// 设置编码
		ftpClient.setFileType(BINARY_FILE_TYPE);// 设置文件类型
	}

	/**
	 * 
	 * <b>描述：</b>设置传输文件类型:FTP.BINARY_FILE_TYPE | FTP.ASCII_FILE_TYPE 二进制文件或文本文件
	 * <br/>
	 * 
	 * @param fileType
	 *            文件类型
	 * @throws IOException
	 * @author 陶俊清 <b>创建时间：</b>2015年11月27日 下午2:45:19<br/>
	 */
	public void setFileType(int fileType) throws IOException {
		ftpClient.setFileType(fileType);
	}

	/**
	 * 
	 * <b>描述：</b>关闭连接<br/>
	 * 
	 * @throws IOException
	 * @author 陶俊清 <b>创建时间：</b>2015年11月27日 下午2:45:33<br/>
	 */
	public void closeServer() throws IOException {
		if (ftpClient != null && ftpClient.isConnected()) {
			ftpClient.logout();// 退出FTP服务器
			ftpClient.disconnect();// 关闭FTP连接
		}
	}

	/**
	 * 
	 * <b>描述：</b>转移到FTP服务器工作目录<br/>
	 * 
	 * @param path
	 *            路径
	 * @return
	 * @throws IOException
	 * @author 陶俊清 <b>创建时间：</b>2015年11月27日 下午2:45:43<br/>
	 */
	public boolean changeDirectory(String path) throws IOException {
		return ftpClient.changeWorkingDirectory(path);
	}

	/**
	 * 
	 * <b>描述：</b>在服务器上创建目录<br/>
	 * 
	 * @param pathName
	 *            路径
	 * @return
	 * @throws IOException
	 * @author 陶俊清 <b>创建时间：</b>2015年11月27日 下午2:45:56<br/>
	 */
	public boolean createDirectory(String pathName) throws IOException {
		return ftpClient.makeDirectory(pathName);
	}

	/**
	 * 
	 * <b>描述：</b>在服务器上删除目录<br/>
	 * 
	 * @param path
	 *            路径
	 * @return
	 * @throws IOException
	 * @author 陶俊清 <b>创建时间：</b>2015年11月27日 下午2:46:10<br/>
	 */
	public boolean removeDirectory(String path) throws IOException {
		return ftpClient.removeDirectory(path);
	}

	/**
	 * 
	 * <b>描述：</b>删除所有文件和目录<br/>
	 * 
	 * @param path
	 *            路径
	 * @param isAll
	 *            是否全部删除
	 * @return
	 * @throws IOException
	 * @author 陶俊清 <b>创建时间：</b>2015年11月27日 下午2:46:26<br/>
	 */
	public boolean removeDirectory(String path, boolean isAll) throws IOException {
		if (!isAll) {
			return removeDirectory(path);
		}
		FTPFile[] ftpFileArr = ftpClient.listFiles(path);
		if (ftpFileArr == null || ftpFileArr.length == 0) {
			return removeDirectory(path);
		}
		for (FTPFile ftpFile : ftpFileArr) {
			String name = ftpFile.getName();
			if (ftpFile.isDirectory()) {
				log.info("* [sD]Delete subPath [" + path + "/" + name + "]");
				removeDirectory(path + "/" + name, true);
			} else if (ftpFile.isFile()) {
				log.info("* [sF]Delete file [" + path + "/" + name + "]");
				deleteFile(path + "/" + name);
			} else if (ftpFile.isSymbolicLink()) {

			} else if (ftpFile.isUnknown()) {

			}
		}
		return ftpClient.removeDirectory(path);
	}

	/**
	 * 
	 * <b>描述：</b>检查目录在服务器上是否存在 true：存在 false：不存在<br/>
	 * 
	 * @param path
	 *            路径
	 * @return
	 * @throws IOException
	 * @author 陶俊清 <b>创建时间：</b>2015年11月27日 下午2:47:07<br/>
	 */
	public boolean existDirectory(String path) throws IOException {
		boolean flag = false;
		FTPFile[] ftpFileArr = ftpClient.listFiles(path);
		for (FTPFile ftpFile : ftpFileArr) {
			if (ftpFile.isDirectory() && ftpFile.getName().equalsIgnoreCase(path)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * 
	 * <b>描述：</b>得到文件列表,listFiles返回包含目录和文件，它返回的是一个FTPFile数组
	 * listNames()：只包含目录的字符串数组 String[] fileNameArr = ftpClient.listNames(path);
	 * <br/>
	 * 
	 * @param path
	 *            路径
	 * @return
	 * @throws IOException
	 * @author 陶俊清 <b>创建时间：</b>2015年11月27日 下午2:47:22<br/>
	 */
	public List<String> getFileList(String path) throws IOException {
		FTPFile[] ftpFiles = ftpClient.listFiles(path);
		List<String> retList = new ArrayList<String>();
		if (ftpFiles == null || ftpFiles.length == 0) {
			return retList;
		}
		for (FTPFile ftpFile : ftpFiles) {
			if (ftpFile.isFile()) {
				retList.add(ftpFile.getName());
			}
		}
		return retList;
	}

	/**
	 * 
	 * <b>描述：</b>删除服务器上文件<br/>
	 * 
	 * @param pathName
	 *            路径
	 * @return
	 * @throws IOException
	 * @author 陶俊清 <b>创建时间：</b>2015年11月27日 下午2:47:58<br/>
	 */
	public boolean deleteFile(String pathName) throws IOException {
		return ftpClient.deleteFile(pathName);
	}

	/**
	 * 
	 * <b>描述：</b>上传文件到ftp服务器 在进行上传和下载文件的时候，设置文件的类型最好是：
	 * ftpUtil.setFileType(FtpUtil.BINARY_FILE_TYPE) localFilePath 本地文件路径和名称
	 * <br/>
	 * 
	 * @param localFilePath
	 *            本地文件路径
	 * @param remoteFileName
	 *            远程文件路径
	 * @return
	 * @throws IOException
	 * @author 陶俊清 <b>创建时间：</b>2015年11月27日 下午2:48:20<br/>
	 */
	public boolean uploadFile(String localFilePath, String remoteFileName) throws IOException {
		boolean flag = false;
		InputStream iStream = null;
		try {
			iStream = new FileInputStream(localFilePath);
			flag = ftpClient.storeFile(remoteFileName, iStream);
		} catch (IOException e) {
			flag = false;
			return flag;
		} finally {
			if (iStream != null) {
				iStream.close();
			}
		}
		return flag;
	}

	/**
	 * 
	 * <b>描述：</b>上传文件到ftp服务器，上传新的文件名称和原名称一样<br/>
	 * 
	 * @param fileName
	 *            文件名称(全路径)
	 * @return
	 * @throws IOException
	 * @author 陶俊清 <b>创建时间：</b>2015年11月27日 下午2:49:12<br/>
	 */
	public boolean uploadFile(String fileName) throws IOException {
		return uploadFile(fileName, fileName);
	}

	/**
	 * 
	 * <b>描述：</b>上传文件到ftp服务器<br/>
	 * 
	 * @param iStream
	 *            输入流
	 * @param newName
	 *            新名称
	 * @return
	 * @throws IOException
	 * @author 陶俊清 <b>创建时间：</b>2015年11月27日 下午2:49:38<br/>
	 */
	public boolean uploadFile(InputStream iStream, String newName) throws IOException {
		boolean flag = false;
		try {
			flag = ftpClient.storeFile(newName, iStream);
		} catch (IOException e) {
			flag = false;
			return flag;
		} finally {
			if (iStream != null) {
				iStream.close();
			}
		}
		return flag;
	}

	/**
	 * 
	 * <b>描述：</b>从ftp服务器上下载文件到本地<br/>
	 * 
	 * @param remoteFileName
	 *            FTP服务器文件名称
	 * @param localFileName
	 *            本地文件名称
	 * @return
	 * @throws IOException
	 * @author 陶俊清 <b>创建时间：</b>2015年11月27日 下午2:50:03<br/>
	 */
	public boolean download(String remoteFileName, String localFileName) throws IOException {
		boolean flag = false;
		File outfile = new File(localFileName);
		OutputStream oStream = null;
		try {
			oStream = new FileOutputStream(outfile);
			flag = ftpClient.retrieveFile(remoteFileName, oStream);
		} catch (IOException e) {
			flag = false;
			return flag;
		} finally {
			oStream.close();
		}
		return flag;
	}

	/**
	 * 
	 * <b>描述：</b>从ftp服务器上下载文件到本地<br/>
	 * 
	 * @param sourceFileName
	 *            服务器资源文件名称
	 * @return
	 * @throws IOException
	 * @author 陶俊清 <b>创建时间：</b>2015年11月27日 下午2:50:31<br/>
	 */
	public InputStream downFile(String sourceFileName) throws IOException {
		return ftpClient.retrieveFileStream(sourceFileName);
	}

	public static void main(String args[]) {
		// testUpload();
		// testDownload();
		try {
			FtpUtil ftpUtil = new FtpUtil();
			ftpUtil.connectServer("192.168.10.172", FTPClient.DEFAULT_PORT, "white", "white", null);
			// 获得ftp服务器上目录名称为DF4下的所有文件名称
			//List<String> list = ftpUtil.getFileList("/DF4");
			// log.info("文件名称列表为:" + list);
			// 上传本地D盘文件aaa.txt到服务器，服务器上名称为bbb.txt
			ftpUtil.createDirectory("/images");
			ftpUtil.uploadFile("d:" + File.separator + "logo.jpg", "logo.jpg");
			// 从服务器上下载文件bbb.txt到本地d盘名称为ccc.txt
			ftpUtil.download("logo.jpg", "f:" + File.separator + "logo.jpg");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
