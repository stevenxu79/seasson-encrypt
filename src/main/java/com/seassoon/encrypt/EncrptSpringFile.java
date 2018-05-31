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
import java.io.IOException;
import java.nio.charset.Charset;

/**   
 * @ClassName:  EncrptSpringFile   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: 徐建文 
 * @date:2018年5月31日 上午9:07:14  
 * @Copyright: 2018 www.seassoon.com Inc. All rights reserved. 
 * 注意：本内容仅限于上海思贤信息技术股份有限公司内部传阅，禁止外泄以及用于其他的商业行为 
 */

public class EncrptSpringFile {
	
	
	native byte[] encrypt(byte[] _buf, byte[] key, byte[] keyorg);
	
	static {
		System.out.println(System.getProperty("java.library.path"));
		System.loadLibrary("libEncJarLib");
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
		String fileName1="rpl/classes/org/springframework/core/LocalVariableTableParameterNameDiscoverer.class";
		String fileName2="rpl/classes/org/springframework/core/type/classreading/SimpleMetadataReader.class";
		File readFile1=new File(fileName1);
		byte[] bytes_tmp1=getFileBytes(readFile1);
		System.err.println("read len2="+bytes_tmp1.length);
		EncrptSpringFile coder = new EncrptSpringFile();
		byte[] key= {33, -49, -77, 49, 106, -5, -99, 0, -5, -72};//保存KEY
		String keyOrg="suichaojar";
		System.out.println("enckeyOrg Str:"+keyOrg);	
		Charset charset = Charset.forName("UTF-8");
		byte[] keyOrgBytes=keyOrg.getBytes(charset);
		byte[] bytes = coder.encrypt(bytes_tmp1,key,keyOrgBytes);// 加密CLASS
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
}
