package com.example.bmp601_hw.model;

/**
 * نموذج بيانات الفئات
 * يمثل فئة واحدة من فئات المشتريات
 */
public class Category {
    private long id;              // مفتاح أساسي تلقائي
    private String name;          // اسم الفئة (إلزامي)
    private String description;   // وصف الفئة (اختياري)

    /**
     * منشئ البيانات الكامل
     */
    public Category(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * منشئ بسيط (بدون ID للفئات الجديدة)
     */
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * منشئ باسم الفئة فقط
     */
    public Category(String name) {
        this.name = name;
        this.description = "";
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}
