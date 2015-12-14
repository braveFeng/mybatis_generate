package org.mybatis.plugin.tools;

import java.io.InputStream;
import java.util.Properties;

public class CommonUtil {

	//获取资源文件
	public static Properties getPropertiesByInputStream(InputStream stream) throws Exception{
		
		Properties properties = new Properties();
		try {
			properties.load(stream);
		} catch (Exception e) {
			throw e;
		}
		return properties;
	}
}
