/**  
 * All rights Reserved, Designed By www.seassoon.com
 * @Title:  EncrptSpringFile.java   
 * @Package com.seassoon.encrypt   
 * @Description:TODO(用一句话描述该文件做什么)   
 * @author: 徐建文
 * @date:   2018年5月31日 上午9:07:14
 * @version V2.0
 * @Copyright: 2018 www.seassoon.com Inc. All rights reserved. 
 * 注意：本内容仅限于上海思贤信息技术股份有限公司内部传阅，禁止外泄以及用于其他的商业行为
 */
package com.seassoon.encrypt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**   
 * @ClassName:  EncrptSpringFile   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: 徐建文 
 * @date:2018年5月31日 上午9:07:14  
 * @Copyright: 2018 www.seassoon.com Inc. All rights reserved. 
 * 注意：本内容仅限于上海思贤信息技术股份有限公司内部传阅，禁止外泄以及用于其他的商业行为 
 */

public class EncrptSpringFile {
	
	
//	native byte[] encrypt(byte[] _buf, byte[] key, byte[] keyorg);
//	
//	static {
//		System.out.println(System.getProperty("java.library.path"));
//		System.loadLibrary("libEncJarLib");
//	}

	
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
		List<String> fileNames = new ArrayList();
		fileNames.add("rpl_org/classes/org/springframework/core/LocalVariableTableParameterNameDiscoverer.class");
		fileNames.add("rpl_org/classes/org/springframework/core/LocalVariableTableParameterNameDiscoverer$ParameterNameDiscoveringVisitor.class");
		fileNames.add("rpl_org/classes/org/springframework/core/LocalVariableTableParameterNameDiscoverer$LocalVariableTableVisitor.class");
		fileNames.add("rpl_org/classes/org/springframework/core/type/classreading/SimpleMetadataReader.class");
		fileNames.add("rpl_org/classes/org/springframework/core/type/classreading/SimpleMetadataReaderFactory.class");
		fileNames.add("rpl_org/classes/org/hibernate/boot/archive/scan/spi/ClassFileArchiveEntryHandler.class");
		fileNames.add("rpl_org/classes/org/hibernate/boot/archive/scan/spi/NonClassFileArchiveEntryHandler.class");
		for (String fileName : fileNames) {
			System.out.println("fileName="+fileName);
			File readFile=new File(fileName);
			byte[] bytes_tmp1=getFileBytes(readFile);
			System.err.println("read len2="+bytes_tmp1.length);
			EncryptJar coder = new EncryptJar();
			byte[] key= {33, -49, -77, 49, 106, -5, -99, 0, -5, -72};//保存KEY
			String keyOrg="suichaojar";
			System.out.println("enckeyOrg Str:"+keyOrg);	
			Charset charset = Charset.forName("UTF-8");
			byte[] keyOrgBytes=keyOrg.getBytes(charset);
			byte[] bytes = coder.encrypt(bytes_tmp1,key,keyOrgBytes);// 加密CLASS
			String saveFileName=fileName.replace("rpl_org", "rpl");
			System.err.println("saveFileName="+saveFileName);
			writeFile(saveFileName, bytes);
		}
		
		
		
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
	
	public static void writeFile( String fileName, byte[] content)  
	        throws IOException {  
	    try {  	      
	        FileOutputStream fos = new FileOutputStream(fileName);  
	        fos.write(content);  
	        fos.close();  
	    } catch (IOException e) {  
	        throw new RuntimeException(e);  
	    }  
	}  
	
	
}
