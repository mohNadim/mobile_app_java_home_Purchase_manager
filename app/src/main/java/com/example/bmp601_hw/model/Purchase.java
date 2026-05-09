package com.example.bmp601_hw.model;

/**
 * نموذج بيانات المشتريات
 * يمثل عنصر واحد من عناصر المشتريات المنزلية
 */
public class Purchase {
    private long id;              // مفتاح أساسي تلقائي
    private String itemName;      // اسم العنصر (إلزامي)
    private String category;      // الفئة (اختياري)
    private double price;         // السعر الواحد
    private int quantity;         // الكمية
    private double totalCost;     // إجمالي التكلفة = السعر × الكمية
    private long date;            // التاريخ (ملحوظة بالميلي ثانية)

    /**
     * منشئ البيانات الكامل
     */
    public Purchase(long id, String itemName, String category, double price, int quantity, double totalCost, long date) {
        this.id = id;
        this.itemName = itemName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.totalCost = totalCost;
        this.date = date;
    }

    /**
     * منشئ بسيط (بدون ID للعناصر الجديدة)
     */
    public Purchase(String itemName, String category, double price, int quantity, long date) {
        this.itemName = itemName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.totalCost = price * quantity;
        this.date = date;
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public long getDate() {
        return date;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public void setDate(long date) {
        this.date = date;
    }

    /**
     * طريقة لحساب التكلفة الإجمالية
     */
    public void calculateTotalCost() {
        this.totalCost = this.price * this.quantity;
    }
}
