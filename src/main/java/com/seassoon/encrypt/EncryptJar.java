/**  
 * All rights Reserved, Designed By www.seassoon.com
 * @Title:  EncryptJar.java   
 * @Package com.seassoon.encrypt   
 * @Description:TODO(用一句话描述该文件做什么)   
 * @author: 徐建文
 * @date:   2018年5月30日 下午2:59:01
 * @version V2.0
 * @Copyright: 2018 www.seassoon.com Inc. All rights reserved. 
 * 注意：本内容仅限于上海思贤信息技术股份有限公司内部传阅，禁止外泄以及用于其他的商业行为
 */
package com.seassoon.encrypt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
/**   
 * @ClassName:  EncryptJar   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: 徐建文 
 * @date:2018年5月30日 下午2:59:01  
 * @Copyright: 2018 www.seassoon.com Inc. All rights reserved. 
 * 注意：本内容仅限于上海思贤信息技术股份有限公司内部传阅，禁止外泄以及用于其他的商业行为 
 */

public class EncryptJar {	
	native byte[] genEncKey(byte[] keyorg);
	native byte[] encrypt(byte[] _buf, byte[] key, byte[] keyorg);
//	native byte[] decrypt(byte[] _buf, byte[] key, byte[] keyorg);
	static {
		System.out.println(System.getProperty("java.library.path"));
		System.loadLibrary("libEncJarLib");
//		System.loadLibrary("libDecJarLib");
	}
	
	static String springClass = "SimpleMetadataReader";
	static String springClass2 ="LocalVariableTableParameterNameDiscoverer";
//	static String notStr="$";
	static String notCheckStr="$$";
	static String projectStr = "GetDDL";
//	static String projectStr2 = "org/eclipse/jdt/internal/jarinjarloader";
	static String projectStr2 = "com/seassoon";
	
	static String springJar = "spring-core";
	static String suichaoJar="microservice-";
	// 获取参数
	static Map<String, String> getArgMap(String[] args) {
		Map<String, String> map = new HashMap<>();
		String key = null, val = null;
		for (String tmp : args) {
			if (tmp.startsWith("-")) {
				if (key != null)
					map.put(key, val);
				key = tmp;
				val = null;
			} else {
				val = tmp;
			}
		}
		if (key != null) {
			map.put(key, val);
		}

		return map;
	}

	/** 
	* 读取流 
	*  
	* @param inStream 
	* @return 字节数组 
	 * @throws IOException 
	* @throws Exception 
	*/
	public static byte[] readStream(InputStream inStream) throws IOException {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}

	/**
	 * 判断是否需要加密
	 * @Title: isEncrypt   
	 * @Description: TODO(这里用一句话描述这个方法的作用)   
	 * @param: @param name
	 * @param: @return      
	 * @return: boolean      
	 * @throws
	 */
	public static boolean isEncrypt(String name) {
		
		if (name != null) {
//			if (name.endsWith(".class") && name.indexOf(notStr) == -1 ) {
//			if (name.endsWith(".class") ) {
			if (name.endsWith(".class") && name.indexOf(notCheckStr) == -1 ) {
				if (name.indexOf(projectStr) != -1 ||name.indexOf(projectStr2) != -1 || name.indexOf(springClass) != -1 || name.indexOf(springClass2) != -1) {
//					System.err.println("encrypt is true!!");
					return true;
				}
			}
			if (name.endsWith(".jar")) {
				if (name.indexOf(springJar) != -1 || name.indexOf(suichaoJar) != -1) {
					return true;
				}
			}

		}
		return false;

	}

	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {
			System.out.println("创建目录" + destDirName + "，目标目录已经存在");
			return false;
		}
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		// 创建目录
		if (dir.mkdirs()) {
			System.out.println("创建目录" + destDirName + "成功！");
			return true;
		} else {
			System.out.println("创建目录" + destDirName + "失败！");
			return false;
		}
	}

	public static boolean deleteFile(String fileName){
        File file = new File(fileName);
        if(file.isFile() && file.exists()){
            Boolean succeedDelete = file.delete();
            if(succeedDelete){
                System.out.println("删除单个文件"+fileName+"成功！");
                return true;
            }
            else{
                System.out.println("删除单个文件"+fileName+"失败！");
                return true;
            }
        }else{
            System.out.println("删除单个文件"+fileName+"失败！");
            return false;
        }
    }


    public static boolean deleteDirectory(String dir){
        //如果dir不以文件分隔符结尾，自动添加文件分隔符
        if(!dir.endsWith(File.separator)){
            dir = dir+File.separator;
        }
        File dirFile = new File(dir);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if(!dirFile.exists() || !dirFile.isDirectory()){
            System.out.println("删除目录失败"+dir+"目录不存在！");
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for(int i=0;i<files.length;i++){
            //删除子文件
            if(files[i].isFile()){
                flag = deleteFile(files[i].getAbsolutePath());
                if(!flag){
                    break;
                }
            }
            //删除子目录
            else{
                flag = deleteDirectory(files[i].getAbsolutePath());
                if(!flag){
                    break;
                }
            }
        }

        if(!flag){
            System.out.println("删除目录失败");
            return false;
        }

        //删除当前目录
        if(dirFile.delete()){
            System.out.println("删除目录"+dir+"成功！");
            return true;
        }else{
            System.out.println("删除目录"+dir+"失败！");
            return false;
        }
    }
    
	public static void inputStreamToFile(InputStream ins, File file) throws IOException {
		OutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		ins.close();
	}

	public static byte[] getFileBytes(File file) throws IOException {
		byte[] buffer;
		FileInputStream fis = new FileInputStream(file);
		ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
		byte[] b = new byte[1000];
		int n;
		while ((n = fis.read(b)) != -1) {
			bos.write(b, 0, n);
		}
		fis.close();
		bos.close();
		buffer = bos.toByteArray();
		return buffer;
	}

	public static void encJar(String src_name, String dst_name,byte[] key, byte[] keyOrg) throws IOException {
		
		File src_file = new File(src_name);
		File dst_file = new File(dst_name);

		JarFile src_jar = new JarFile(src_file);
		Enumeration<JarEntry> jarEntrys = src_jar.entries();

		JarOutputStream dst_jar = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(dst_file)));

		byte[] buf = new byte[1024];
		while (jarEntrys.hasMoreElements()) {
			JarEntry entry = jarEntrys.nextElement();
			// 文件名称
			String name = entry.getName();
			// 文件大小
			long size = entry.getSize();
			// 压缩后的大小
			long compressedSize = entry.getCompressedSize();
//			 System.out.println(name + "\t" + size + "\t" + compressedSize);
			if (isEncrypt(name)) {
				System.out.println("encrypt " + name);
				if (name.endsWith(".class")) {

					try {
						
						byte[] bytes_tmp;
						
						if ((name.indexOf(springClass) != -1) || (name.indexOf(springClass2)) != -1) {
							System.out.println("spring replace class2:" + name);
							String readFileName="rpl/classes/"+name;
							System.out.println("readFile="+readFileName);
							File readFile=new File(readFileName);
							bytes_tmp=getFileBytes(readFile);
//							System.err.println("read len2="+bytes_tmp.length);
							
							JarEntry ne = new JarEntry(name);
							dst_jar.putNextEntry(ne);
							dst_jar.write(bytes_tmp);
							
						} else {
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							BufferedInputStream is = new BufferedInputStream(src_jar.getInputStream(entry));
							int len;
							while ((len = is.read(buf, 0, buf.length)) != -1) {
								baos.write(buf, 0, len);
							}
							is.close();
							bytes_tmp = baos.toByteArray();
//							System.err.println("read len2="+bytes_tmp.length);
							baos.close();
							EncryptJar coder = new EncryptJar();
							byte[] bytes = coder.encrypt(bytes_tmp,key,keyOrg);// 加密CLASS
							
							JarEntry ne = new JarEntry(name);
							dst_jar.putNextEntry(ne);
							dst_jar.write(bytes);
						}
						
						
						dst_jar.closeEntry();

					} catch (Exception e) {
						System.out.println("encrypt error happend~");
						e.printStackTrace();
					}

				} else if (name.endsWith(".jar")) {

					// 获得实体流
					InputStream is = src_jar.getInputStream(entry);

					// 创建本地临时目录和文件
					long dt = new Date().getTime();
					String tmpDir = "tmp/" + dt;
					createDir(tmpDir);

					String tmpFile = name.substring(name.lastIndexOf("/") + 1);
					String tmp_src_name = tmpDir + "/" + tmpFile;
					System.err.println("tmp_src_name=" + tmp_src_name);
					File tmpJarFile_src = new File(tmp_src_name);
					// 将实体流写入本地临时文件
					inputStreamToFile(is, tmpJarFile_src);
					is.close();

					// 递归调用jar包加密方法,获得嵌套的加密报
					String tmp_dst_name = tmp_src_name.substring(0, tmp_src_name.length() - 4) + "_encrypt.jar";
					encJar(tmp_src_name, tmp_dst_name,key,keyOrg);
					// 读取jar包数据写入大JAR文件
					File small_jar = new File(tmp_dst_name);
					InputStream input = new FileInputStream(small_jar);
					byte[] byt = readStream(input);

					JarEntry ne = new JarEntry(name);

					/** ZipEntry.STORED */
					ne.setMethod(ZipEntry.STORED);
					ne.setCompressedSize(small_jar.length());
					ne.setSize(small_jar.length());
					CRC32 crc = new CRC32();
					crc.update(getFileBytes(small_jar));
					ne.setCrc(crc.getValue());
					/** ZipEntry.STORED */

					dst_jar.putNextEntry(ne);

					dst_jar.write(byt);
					input.close();
					dst_jar.closeEntry();
					
					//删除临时文件夹
					deleteDirectory(tmpDir);
				
//					if (name.indexOf(springJar) != -1 || name.indexOf(suichaoJar) != -1) {}
					
				}

			} else {
				// System.out.println("no encrypt " + name);
				dst_jar.putNextEntry(entry);
				BufferedInputStream is = new BufferedInputStream(src_jar.getInputStream(entry));
				int len;
				while ((len = is.read(buf, 0, buf.length)) != -1) {
					// baos.write(buf, 0, len);
					dst_jar.write(buf, 0, len);
				}

				is.close();
				dst_jar.closeEntry();
			}

		}

		dst_jar.finish();
		dst_jar.close();
		src_jar.close();

	}
	/**
	 * @throws IOException    
	 * @Title: main   
	 * @Description: TODO(这里用一句话描述这个方法的作用)   
	 * @param: @param args      
	 * @return: void      
	 * @throws   
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Map<String, String> map = getArgMap(args);

		String src_name = map.get("-src");
//		String src_name="E:\\g工作\\x项目\\suicao\\元数据\\jar\\jar\\get-ddl.jar";
//		String src_name="F:\\BaiduNetdiskDownload\\随巢部署相关文件-验证加密\\程序\\govern\\suichao-govern.jar";
//		String src_name="F:\\BaiduNetdiskDownload\\随巢部署相关文件-验证加密\\程序\\spark-shell\\suichao-spark-shell.jar";
//		String src_name="E:\\STS_WORKSPACE\\SUICHAO\\suichao-ms-3\\encSpringTest\\target\\encSpringTest-0.0.1-SNAPSHOT.jar";
		
		//suichao3.0
//		String src_name="F:\\suichao-files\\files\\spark\\profile_task\\suichao-spark-shell.jar";
//		String src_name="F:\\suichao-files\\files\\spark\\interactive_task\\suichao-spark-shell.jar";
		
//		String src_name="F:\\suichao-files\\files\\microservices\\app\\auth\\auth-3.1.0.jar";
//		String src_name="F:\\suichao-files\\files\\microservices\\app\\catalog\\suichao-catalog-3.1.0.jar";
//		String src_name="F:\\suichao-files\\files\\microservices\\app\\dataoverview\\suichao-dataoverview-3.1.0.jar";
//		String src_name="F:\\suichao-files\\files\\microservices\\app\\datausage\\datausage-3.1.0.jar";
//		String src_name="F:\\suichao-files\\files\\microservices\\app\\datawarngling\\datawarngling-3.1.0.jar";
//		String src_name="F:\\suichao-files\\files\\microservices\\app\\govern\\govern-3.1.0.jar";
//		String src_name="F:\\suichao-files\\files\\microservices\\app\\project\\project-3.1.0.jar";
//		String src_name="F:\\suichao-files\\files\\microservices\\app\\system\\suichao-system-3.1.0.jar";
//		String src_name="F:\\suichao-files\\files\\microservices\\app\\understand\\understand-3.1.0.jar";
		
//		String src_name="F:\\suichao-files\\files\\microservices\\gateway\\eureka\\eureka_server.jar";
//		String src_name="F:\\suichao-files\\files\\microservices\\gateway\\zuul\\service-zuul.jar";
		
		if (src_name == null) {
			System.out.println("usage: java Encrypt -src xxx.jar");
			return;
		}
		
		String dst_name = map.get("-dst");
		if (dst_name == null || dst_name.equals(src_name))
			dst_name = src_name.substring(0, src_name.length() - 4) + "_encrypt.jar";

		System.out.printf("encode jar file: [%s ==> %s ]\n", src_name, dst_name);

		Charset charset = Charset.forName("UTF-8");
		EncryptJar coder = new EncryptJar();
		
		//调用动态库加密和解密
		//1.获得密钥
		String keyOrg="suichaojar";
		System.out.println("enckeyOrg Str:"+keyOrg);	
		byte[] keyOrgBytes=keyOrg.getBytes(charset);
		System.out.println("keyOrgBytes byte:len="+keyOrgBytes.length+",content="+Arrays.toString(keyOrgBytes));
		byte[] key = coder.genEncKey(keyOrg.getBytes(charset));
		System.out.println("enckey byte:len="+key.length+",content="+Arrays.toString(key));
//		System.out.println("enckey str:"+new String(key)); 
		
//		System.out.println("enckeyOrg Str:"+enckeyOrg);	
//		byte[] enckey2= {33, -49, -77, 49, 106, -5, -99, 0, -5, -72};//保存KEY
		
		encJar(src_name, dst_name,key,keyOrgBytes);
		
		
		
	}

}
