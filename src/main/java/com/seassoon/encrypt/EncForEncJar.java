/**  
 * All rights Reserved, Designed By www.seassoon.com
 * @Title:  EncForEncJar.java   
 * @Package com.seassoon.encrypt   
 * @Description:TODO(用一句话描述该文件做什么)   
 * @author: 徐建文
 * @date:   2018年5月31日 下午5:38:49
 * @version V2.0
 * @Copyright: 2018 www.seassoon.com Inc. All rights reserved. 
 * 注意：本内容仅限于上海思贤信息技术股份有限公司内部传阅，禁止外泄以及用于其他的商业行为
 */
package com.seassoon.encrypt;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**   
 * @ClassName:  EncForEncJar   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: 徐建文 
 * @date:2018年5月31日 下午5:38:49  
 * @Copyright: 2018 www.seassoon.com Inc. All rights reserved. 
 * 注意：本内容仅限于上海思贤信息技术股份有限公司内部传阅，禁止外泄以及用于其他的商业行为 
 */

public class EncForEncJar {

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
		//先MAVEN PACKAGE变为jar包
		
		Map<String, String> map = getArgMap(args);
		String src_name="E:\\STS_WORKSPACE\\SUICHAO\\suichao-ms-3\\seasson-encrypt\\target\\seasson-encrypt-0.0.1-SNAPSHOT.jar";
		
		//suichao3.0
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
		
		coder.encJar(src_name, dst_name,key,keyOrgBytes);
	
	}

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
}
