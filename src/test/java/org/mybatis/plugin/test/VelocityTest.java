package org.mybatis.plugin.test;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.mybatis.plugin.model.ModelDetailDto;

public class VelocityTest {

	public static void main(String[] args) throws Exception {
		
		String templateFile = "template/XXXXXXDao.vm";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		ModelDetailDto detailInfoDto = new ModelDetailDto();
		detailInfoDto.setClassName("UserInfo");
		paramMap.put("detailInfoDto", detailInfoDto);
		VelocityContext context = new VelocityContext();	
		Set<Entry<String, Object>> entrySet = paramMap.entrySet();
		for(Entry<String, Object> entry:entrySet){
			context.put(entry.getKey(), entry.getValue());
		}	
		StringWriter sw = new StringWriter();

		VelocityEngine engine = getVelocityEngine();

		Template template = engine.getTemplate(templateFile, "UTF-8");

		template.merge(context, sw);
		System.out.println(sw.toString());
	}

	private static VelocityEngine getVelocityEngine() throws Exception {
		Properties properties = new Properties();
		// 可选值："class"--从classpath中读取，"file"--从文件系统中读取
		properties.setProperty("resource.loader", "file");
		// 如果从文件系统中读取模板，那么属性值为org.apache.velocity.runtime.resource.loader.FileResourceLoader
		properties
				.setProperty("jar.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		// properties.setProperty("jar.resource.loader.path", "jar:file:/" +
		// DAOTool.getLibFilePath() + ToolsConst.SEPARATOR +
		// ToolsConst.DAOTOOLS_JAR_NAME);
		properties.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS,
				"org.apache.velocity.runtime.log.NullLogSystem");

		VelocityEngine engine = new VelocityEngine();

		engine.init(properties);
		return engine;

	}
}
