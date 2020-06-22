package com.test;

import com.alibaba.fastjson.JSON;
import com.test.entity.FieldMate;
import com.test.util.ToolUtil;
import com.test.util.TypeUtil;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

/**
 * @author lijian
 * @date 2019/7/29 16:06
 * @desc
 */
public class MysqlData {
    private static String url = "jdbc:mysql://localhost:3306/test?characterEncoding=UTF-8";
    private static String username = "root";
    private static String password = "mysql";
    public static List<String> tables;
    private static String connector;
    private static final String line = "line";
    private static final String hump = "hump";
    private static Connection connection;
    public static String packageName;
    public static String fileName;
    public static final String fileSeparator = System.getProperty("file.separator");
    static {
        try(InputStream stream = MysqlData.class.getClassLoader().getResourceAsStream("jdbc.properties")) {
            Properties properties = new Properties();
            properties.load(stream);
            String param = System.getProperty("param");
            if (StringUtils.isNotBlank(param)) {
                Map<String, String> map = JSON.parseObject(param, Map.class);
                Set<Map.Entry<String, String>> entries = map.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    if (StringUtils.isNotBlank(entry.getValue())) {
                        System.setProperty(entry.getKey(), entry.getValue());
                    }
                }
            }
            String table = System.getProperty("tables");
            if (StringUtils.isBlank(table)) {
                table = properties.getProperty("tables");
            }
            String[] split = table.split(",");
            tables = Arrays.asList(split);
            url = System.getProperty("url");
            if (StringUtils.isBlank(url)) {
                url = properties.getProperty("url");
            }
            username = System.getProperty("username");
            if (StringUtils.isBlank(username)) {
                username = properties.getProperty("username");
            }
            password = System.getProperty("password");
            if (StringUtils.isBlank(password)) {
                password = properties.getProperty("password");
            }
            connector = System.getProperty("connector");
            if (StringUtils.isBlank(connector)) {
                connector = properties.getProperty("connector");
            }
            packageName = System.getProperty("packageName");
            if (StringUtils.isBlank(packageName)) {
                packageName = properties.getProperty("packageName");
            }
            if (fileSeparator.equals("\\")) {
                fileName = packageName.replaceAll("[.]", "\\\\");
            } else {
                fileName = packageName.replaceAll("[.]", fileSeparator);
            }
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(url, username, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String findComment(String table) {
        PreparedStatement preparedStatement = null;
        String comment = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT table_comment 'tableComment' " +
                    "FROM information_schema.TABLES " +
                    "WHERE  table_name = ?");
            preparedStatement.setString(1, table);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                comment = resultSet.getString("tableComment");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return comment;
    }


    public static List<FieldMate> connection(String table) {
        PreparedStatement preparedStatement = null;
        PreparedStatement commentStatement = null;
        try {
            preparedStatement = connection.prepareStatement("select * from " + table);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            commentStatement = connection.prepareStatement("show full columns from " + table);
            ResultSet comments = commentStatement.executeQuery();
            List<FieldMate> list = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                FieldMate fieldMate = new FieldMate();
                comments.next();
                fieldMate.setJavaName(metaData.getColumnName(i));
                if (line.equals(connector)) {
                    fieldMate.setJavaName(ToolUtil.lineToHump(fieldMate.getJavaName()));
                }
                fieldMate.setColumnName(metaData.getColumnName(i));
                fieldMate.setColumnType(metaData.getColumnTypeName(i));
                if ("INT".equalsIgnoreCase(metaData.getColumnTypeName(i))) {
                    fieldMate.setColumnType("INTEGER");
                } else if (metaData.getColumnTypeName(i).startsWith("BIGINT")) {
                    fieldMate.setColumnType("BIGINT");
                }
                Map<String, Object> map = TypeUtil.findClass(fieldMate.getColumnType());
                fieldMate.setJavaType((String) map.get("className"));
                fieldMate.setJavaClassName(((Class) map.get("classType")).getName());
                fieldMate.setUpperJavaName(fieldMate.getJavaName().substring(0, 1).toUpperCase() + fieldMate.getJavaName().substring(1));
                fieldMate.setComment(comments.getString("comment"));
                fieldMate.setImportClass((String) map.get("importClass"));
                if (StringUtils.isNotBlank(comments.getString("key"))
                        && "pri".equalsIgnoreCase(comments.getString("key"))) {
                    fieldMate.setPrimaryKey(true);
                }
                list.add(fieldMate);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
                commentStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        connection("test");
    }
}
