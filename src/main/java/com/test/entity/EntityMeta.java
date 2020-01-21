package com.test.entity;

import java.util.List;

/**
 * @author lijian
 * @date 2019/7/29 17:56
 * @desc
 */
public class EntityMeta {
    private String packageName;
    private String className;
    private String lowerClassName;
    private String tableName;
    private String comment;
    private FieldMate pkFieldMeta;
    private List<FieldMate> fieldMetaList;
    private List<String> importClassList;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<FieldMate> getFieldMetaList() {
        return fieldMetaList;
    }

    public void setFieldMetaList(List<FieldMate> fieldMetaList) {
        this.fieldMetaList = fieldMetaList;
    }

    public List<String> getImportClassList() {
        return importClassList;
    }

    public void setImportClassList(List<String> importClassList) {
        this.importClassList = importClassList;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public FieldMate getPkFieldMeta() {
        return pkFieldMeta;
    }

    public void setPkFieldMeta(FieldMate pkFieldMeta) {
        this.pkFieldMeta = pkFieldMeta;
    }

    public String getLowerClassName() {
        return lowerClassName;
    }

    public void setLowerClassName(String lowerClassName) {
        this.lowerClassName = lowerClassName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
