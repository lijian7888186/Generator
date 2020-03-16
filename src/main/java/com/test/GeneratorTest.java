package com.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.entity.EntityMeta;
import com.test.entity.FieldMate;
import com.test.util.ToolUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lijian
 * @date 2019/7/29 16:01
 * @desc
 */
public class GeneratorTest {
    private static VelocityEngine engine = null;
    private static List<FileName> fileList = new ArrayList<>();
    private static final String javaSuffix = ".java";
    private static final String sqlSuffix = ".xml";
    private static final String fileSeparator = System.getProperty("file.separator");
    static {
        engine = new VelocityEngine();
        engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        engine.setProperty(RuntimeConstants.INPUT_ENCODING, "utf-8");
        engine.setProperty(RuntimeConstants.OUTPUT_ENCODING, "utf-8");
        engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        engine.init();
        fileList.add(new FileName("entity.vm", "entity", "", javaSuffix));
        fileList.add(new FileName("service.vm", "service", "Service", javaSuffix));
        fileList.add(new FileName("serviceimpl.vm", "service" + fileSeparator + "impl", "ServiceImpl", javaSuffix));
        fileList.add(new FileName("mapper.vm", "mapper", "Mapper", javaSuffix));
        fileList.add(new FileName("mapperxml.vm", "resources", "Mapper", sqlSuffix));
    }

    public static void generatorFile(Template template, VelocityContext context, String packageName, String className) {
        String property = System.getProperty("user.dir");
        String packageFile = property + fileSeparator + "generator" + fileSeparator + MysqlData.fileName + fileSeparator + packageName;
        File file = new File(packageFile);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            file = new File(packageFile + fileSeparator + className);
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter writer = new FileWriter(file);
            template.merge(context, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generatorJavaFile() {
        List<String> tables = MysqlData.tables;
        for (String table : tables) {
            generatorClass(table);
        }
        MysqlData.closeConnection();
    }

    public static void generatorClass(String table) {
        List<FieldMate> list = MysqlData.connection(table);
        if (list == null || list.size() == 0) {
            System.out.println("没有数据");
            return;
        }
        VelocityContext context = new VelocityContext();
        EntityMeta entityMeta = new EntityMeta();
        String line2Hump = ToolUtil.lineToHump(table);
        entityMeta.setClassName(table.substring(0, 1).toUpperCase() + line2Hump.substring(1));
        entityMeta.setPackageName(MysqlData.packageName);
        entityMeta.setTableName(MysqlData.tables.get(0));
        entityMeta.setLowerClassName(line2Hump);
        entityMeta.setFieldMetaList(list);
        entityMeta.setComment(MysqlData.findComment(table));
        List<String> importList = new ArrayList<>();
        for (FieldMate object : list) {
            if (StringUtils.isNotBlank(object.getImportClass())) {
                importList.add(object.getImportClass());
            }
            if (object.isPrimaryKey()) {
                entityMeta.setPkFieldMeta(object);
            }
        }
        entityMeta.setImportClassList(importList);
        context.put("entityMeta", entityMeta);
        for (FileName fileName : fileList) {
            Template template = engine.getTemplate(fileName.getTemplateName());
            String className = entityMeta.getClassName() + fileName.getClassSuffix();
            generatorFile(template, context, fileName.getFileName(), className + fileName.getSuffix());
        }

    }
    static class FileName {
        private String templateName;
        private String fileName;
        private String classSuffix;
        private String suffix;

        public FileName() {
        }

        public FileName(String templateName, String fileName, String classSuffix, String suffix) {
            this.templateName = templateName;
            this.fileName = fileName;
            this.classSuffix = classSuffix;
            this.suffix = suffix;
        }

        public String getTemplateName() {
            return templateName;
        }

        public void setTemplateName(String templateName) {
            this.templateName = templateName;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }

        public String getClassSuffix() {
            return classSuffix;
        }

        public void setClassSuffix(String classSuffix) {
            this.classSuffix = classSuffix;
        }
    }

    public static void main(String[] args) {
        generatorJavaFile();
    }
}
