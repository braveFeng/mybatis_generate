package org.mybatis.plugin.type;
/**
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * 
 * @author Jeff Butler
 */
public class JavaTypeResolver {

    protected List<String> warnings;

    protected Properties properties;


    protected boolean forceBigDecimals;

    public  Map<Integer, JdbcTypeInformation> typeMap;
    
    public JavaTypeResolver() {
        properties = new Properties();
        typeMap = new HashMap<Integer, JdbcTypeInformation>();

        typeMap.put(Types.ARRAY, new JdbcTypeInformation("ARRAY",  Object.class.getName()));
        typeMap.put(Types.BIGINT, new JdbcTypeInformation("BIGINT",  Long.class.getName()));
        typeMap.put(Types.BINARY, new JdbcTypeInformation("BINARY",  "byte[]")); 
        typeMap.put(Types.BIT, new JdbcTypeInformation("BIT", Boolean.class.getName()));
        typeMap.put(Types.BLOB, new JdbcTypeInformation("BLOB", "byte[]")); 
        typeMap.put(Types.BOOLEAN, new JdbcTypeInformation("BOOLEAN", Boolean.class.getName()));
        typeMap.put(Types.CHAR, new JdbcTypeInformation("CHAR", String.class.getName()));
        typeMap.put(Types.CLOB, new JdbcTypeInformation("CLOB", String.class.getName()));
        typeMap.put(Types.DATALINK, new JdbcTypeInformation("DATALINK",Object.class.getName()));
        typeMap.put(Types.DATE, new JdbcTypeInformation("DATE", Date.class.getName()));
        typeMap.put(Types.DISTINCT, new JdbcTypeInformation("DISTINCT",  Object.class.getName()));
        typeMap.put(Types.DOUBLE, new JdbcTypeInformation("DOUBLE",  Double.class.getName()));
        typeMap.put(Types.FLOAT, new JdbcTypeInformation("FLOAT", Double.class.getName()));
        typeMap.put(Types.INTEGER, new JdbcTypeInformation("INTEGER",  Integer.class.getName()));
        typeMap.put(Types.JAVA_OBJECT, new JdbcTypeInformation("JAVA_OBJECT", Object.class.getName()));
        typeMap.put(Types.LONGNVARCHAR, new JdbcTypeInformation("LONGNVARCHAR", String.class.getName()));
        typeMap.put(Types.LONGVARBINARY, new JdbcTypeInformation( "LONGVARBINARY",  "byte[]")); 
        typeMap.put(Types.LONGVARCHAR, new JdbcTypeInformation("LONGVARCHAR",  String.class.getName()));
        typeMap.put(Types.NCHAR, new JdbcTypeInformation("NCHAR", String.class.getName()));
        typeMap.put(Types.NCLOB, new JdbcTypeInformation("NCLOB",  String.class.getName()));
        typeMap.put(Types.NVARCHAR, new JdbcTypeInformation("NVARCHAR",  String.class.getName()));
        typeMap.put(Types.NULL, new JdbcTypeInformation("NULL",  Object.class.getName()));
        typeMap.put(Types.OTHER, new JdbcTypeInformation("OTHER",  Object.class.getName()));
        typeMap.put(Types.REAL, new JdbcTypeInformation("REAL", Float.class.getName()));
        typeMap.put(Types.REF, new JdbcTypeInformation("REF", Object.class.getName()));
        typeMap.put(Types.SMALLINT, new JdbcTypeInformation("SMALLINT",  Short.class.getName()));
        typeMap.put(Types.STRUCT, new JdbcTypeInformation("STRUCT", Object.class.getName()));
        typeMap.put(Types.TIME, new JdbcTypeInformation("TIME",  Date.class.getName()));
        typeMap.put(Types.TIMESTAMP, new JdbcTypeInformation("TIMESTAMP", Date.class.getName()));
        typeMap.put(Types.TINYINT, new JdbcTypeInformation("TINYINT",  Byte.class.getName()));
        typeMap.put(Types.VARBINARY, new JdbcTypeInformation("VARBINARY","byte[]")); 
        typeMap.put(Types.VARCHAR, new JdbcTypeInformation("VARCHAR", String.class.getName()));
        
    }

    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);
        forceBigDecimals = StringUtility
                .isTrue(properties
                        .getProperty(PropertyRegistry.TYPE_RESOLVER_FORCE_BIG_DECIMALS));
    }
    /**
	 * Description:通过jdbcType来获取javaType
	 * @param: ResultSetMetaData metaData 元数据
	 *         int i                      序列
	 * @return java类型
	 * @Author:bravefc
	 * @Create Date: 2015-12-11
	 */
    public static Integer calculateJavaType(ResultSetMetaData metaData ,int i) throws SQLException {
        Integer jdbcType = null;
      
        switch (metaData.getColumnType(i)) {
        case Types.DECIMAL:
        case Types.NUMERIC:
        	if(metaData.getScale(i) > 0){
        		jdbcType = 6;
			}else if(metaData.getPrecision(i) > 10){
				jdbcType = -5;
			}else{
				jdbcType = 4;
			}
        default:
        	jdbcType = metaData.getColumnType(i);
            break;
        }
        
        return jdbcType;
    }

  /*  public String calculateJdbcTypeName(IntrospectedColumn introspectedColumn) {
        String answer;
        JdbcTypeInformation jdbcTypeInformation = typeMap
                .get(introspectedColumn.getJdbcType());

        if (jdbcTypeInformation == null) {
            switch (introspectedColumn.getJdbcType()) {
            case Types.DECIMAL:
                answer = "DECIMAL"; //$NON-NLS-1$
                break;
            case Types.NUMERIC:
                answer = "NUMERIC"; //$NON-NLS-1$
                break;
            default:
                answer = null;
                break;
            }
        } else {
            answer = jdbcTypeInformation.getJdbcTypeName();
        }

        return answer;
    }*/

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    

    public Map<Integer, JdbcTypeInformation> getTypeMap() {
		return typeMap;
	}

	public void setTypeMap(Map<Integer, JdbcTypeInformation> typeMap) {
		this.typeMap = typeMap;
	}



	public static class JdbcTypeInformation {
        private String jdbcTypeName;

        private String javaTypeName;

        public JdbcTypeInformation(String jdbcTypeName,
        		String javaTypeName) {
            this.jdbcTypeName = jdbcTypeName;
            this.javaTypeName = javaTypeName;
        }

        public String getJdbcTypeName() {
            return jdbcTypeName;
        }

        public String getJavaTypeName() {
            return javaTypeName;
        }
    }
}
