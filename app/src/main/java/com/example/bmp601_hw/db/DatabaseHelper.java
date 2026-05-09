package com.example.bmp601_hw.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bmp601_hw.model.Category;
import com.example.bmp601_hw.model.Purchase;

import java.util.ArrayList;
import java.util.List;

/**
 * مساعد قاعدة البيانات (SQLite)
 * يدير جميع عمليات قاعدة البيانات للتطبيق
 * يتضمن الجداول: purchases و categories
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // معلومات قاعدة البيانات
    private static final String DATABASE_NAME = "home_purchases.db";
    private static final int DATABASE_VERSION = 1;

    // أسماء الجداول والأعمدة
    // جدول المشتريات (purchases)
    private static final String TABLE_PURCHASES = "purchases";
    private static final String COLUMN_PURCHASE_ID = "id";
    private static final String COLUMN_ITEM_NAME = "item_name";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_TOTAL_COST = "total_cost";
    private static final String COLUMN_DATE = "date";

    // جدول الفئات (categories)
    private static final String TABLE_CATEGORIES = "categories";
    private static final String COLUMN_CATEGORY_ID = "id";
    private static final String COLUMN_CATEGORY_NAME = "name";
    private static final String COLUMN_CATEGORY_DESCRIPTION = "description";

    /**
     * منشئ DatabaseHelper
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * يُستدعى عند إنشاء قاعدة البيانات أول مرة
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // إنشاء جدول الفئات
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
                + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CATEGORY_NAME + " TEXT NOT NULL,"
                + COLUMN_CATEGORY_DESCRIPTION + " TEXT"
                + ")";

        // إنشاء جدول المشتريات
        String CREATE_PURCHASES_TABLE = "CREATE TABLE " + TABLE_PURCHASES + "("
                + COLUMN_PURCHASE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ITEM_NAME + " TEXT NOT NULL,"
                + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_PRICE + " REAL,"
                + COLUMN_QUANTITY + " INTEGER,"
                + COLUMN_TOTAL_COST + " REAL,"
                + COLUMN_DATE + " INTEGER"
                + ")";

        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_PURCHASES_TABLE);
    }

    /**
     * يُستدعى عند ترقية إصدار قاعدة البيانات
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PURCHASES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    // ======================== عمليات المشتريات (CRUD) ========================

    /**
     * إضافة مشتري جديد إلى قاعدة البيانات
     * @param purchase كائن المشتري المراد إضافته
     * @return معرف الصف المُضاف
     */
    public long addPurchase(Purchase purchase) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ITEM_NAME, purchase.getItemName());
        values.put(COLUMN_CATEGORY, purchase.getCategory());
        values.put(COLUMN_PRICE, purchase.getPrice());
        values.put(COLUMN_QUANTITY, purchase.getQuantity());
        values.put(COLUMN_TOTAL_COST, purchase.getPrice() * purchase.getQuantity()); // حساب التكلفة الإجمالية
        values.put(COLUMN_DATE, purchase.getDate());

        long id = db.insert(TABLE_PURCHASES, null, values);
        db.close();
        return id;
    }

    /**
     * الحصول على مشتري واحد بواسطة معرفه
     * @param id معرف المشتري
     * @return كائن Purchase أو null إذا لم يُعثر عليه
     */
    public Purchase getPurchaseById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PURCHASES,
                new String[]{COLUMN_PURCHASE_ID, COLUMN_ITEM_NAME, COLUMN_CATEGORY, COLUMN_PRICE, COLUMN_QUANTITY, COLUMN_TOTAL_COST, COLUMN_DATE},
                COLUMN_PURCHASE_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        Purchase purchase = null;
        if (cursor != null && cursor.moveToFirst()) {
            purchase = new Purchase(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3),
                    cursor.getInt(4),
                    cursor.getDouble(5),
                    cursor.getLong(6)
            );
            cursor.close();
        }
        db.close();
        return purchase;
    }

    /**
     * الحصول على جميع المشتريات
     * @return قائمة بجميع المشتريات
     */
    public List<Purchase> getAllPurchases() {
        List<Purchase> purchases = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PURCHASES,
                new String[]{COLUMN_PURCHASE_ID, COLUMN_ITEM_NAME, COLUMN_CATEGORY, COLUMN_PRICE, COLUMN_QUANTITY, COLUMN_TOTAL_COST, COLUMN_DATE},
                null, null, null, null, COLUMN_DATE + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Purchase purchase = new Purchase(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getInt(4),
                        cursor.getDouble(5),
                        cursor.getLong(6)
                );
                purchases.add(purchase);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return purchases;
    }

    /**
     * الحصول على المشتريات المفلترة حسب الفئة
     * @param category اسم الفئة
     * @return قائمة بالمشتريات ذات الفئة المحددة
     */
    public List<Purchase> getPurchasesByCategory(String category) {
        List<Purchase> purchases = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PURCHASES,
                new String[]{COLUMN_PURCHASE_ID, COLUMN_ITEM_NAME, COLUMN_CATEGORY, COLUMN_PRICE, COLUMN_QUANTITY, COLUMN_TOTAL_COST, COLUMN_DATE},
                COLUMN_CATEGORY + "=?",
                new String[]{category},
                null, null, COLUMN_DATE + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Purchase purchase = new Purchase(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getInt(4),
                        cursor.getDouble(5),
                        cursor.getLong(6)
                );
                purchases.add(purchase);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return purchases;
    }

    /**
     * الحصول على المشتريات المفلترة حسب التاريخ (نطاق معين)
     * @param startDate تاريخ البداية (ملحوظة بالميلي ثانية)
     * @param endDate تاريخ النهاية (ملحوظة بالميلي ثانية)
     * @return قائمة بالمشتريات ضمن النطاق المحدد
     */
    public List<Purchase> getPurchasesByDateRange(long startDate, long endDate) {
        List<Purchase> purchases = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PURCHASES,
                new String[]{COLUMN_PURCHASE_ID, COLUMN_ITEM_NAME, COLUMN_CATEGORY, COLUMN_PRICE, COLUMN_QUANTITY, COLUMN_TOTAL_COST, COLUMN_DATE},
                COLUMN_DATE + " BETWEEN ? AND ?",
                new String[]{String.valueOf(startDate), String.valueOf(endDate)},
                null, null, COLUMN_DATE + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Purchase purchase = new Purchase(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getInt(4),
                        cursor.getDouble(5),
                        cursor.getLong(6)
                );
                purchases.add(purchase);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return purchases;
    }

    /**
     * تحديث مشتري موجود
     * @param purchase كائن المشتري المراد تحديثه (يجب أن يحتوي على معرف صحيح)
     * @return عدد الصفوف المتأثرة
     */
    public int updatePurchase(Purchase purchase) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ITEM_NAME, purchase.getItemName());
        values.put(COLUMN_CATEGORY, purchase.getCategory());
        values.put(COLUMN_PRICE, purchase.getPrice());
        values.put(COLUMN_QUANTITY, purchase.getQuantity());
        values.put(COLUMN_TOTAL_COST, purchase.getPrice() * purchase.getQuantity()); // إعادة حساب التكلفة
        values.put(COLUMN_DATE, purchase.getDate());

        int rowsAffected = db.update(TABLE_PURCHASES, values, COLUMN_PURCHASE_ID + "=?", new String[]{String.valueOf(purchase.getId())});
        db.close();
        return rowsAffected;
    }

    /**
     * حذف مشتري من قاعدة البيانات
     * @param id معرف المشتري المراد حذفه
     * @return عدد الصفوف المحذوفة
     */
    public int deletePurchase(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_PURCHASES, COLUMN_PURCHASE_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    // ======================== عمليات الفئات (CRUD) ========================

    /**
     * إضافة فئة جديدة إلى قاعدة البيانات
     * @param category كائن الفئة المراد إضافته
     * @return معرف الصف المُضاف
     */
    public long addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_CATEGORY_NAME, category.getName());
        values.put(COLUMN_CATEGORY_DESCRIPTION, category.getDescription());

        long id = db.insert(TABLE_CATEGORIES, null, values);
        db.close();
        return id;
    }

    /**
     * الحصول على جميع الفئات
     * @return قائمة بجميع الفئات
     */
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORIES,
                new String[]{COLUMN_CATEGORY_ID, COLUMN_CATEGORY_NAME, COLUMN_CATEGORY_DESCRIPTION},
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Category category = new Category(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2)
                );
                categories.add(category);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return categories;
    }

    /**
     * حذف فئة من قاعدة البيانات
     * @param id معرف الفئة المراد حذفها
     * @return عدد الصفوف المحذوفة
     */
    public int deleteCategory(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_CATEGORIES, COLUMN_CATEGORY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    // ======================== عمليات الإحصائيات ========================

    /**
     * الحصول على إجمالي المصاريف العام
     * @return مجموع كل المصاريف
     */
    public double getTotalExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_TOTAL_COST + ") FROM " + TABLE_PURCHASES, null);

        double total = 0;
        if (cursor != null && cursor.moveToFirst()) {
            total = cursor.getDouble(0);
            cursor.close();
        }
        db.close();
        return total;
    }

    /**
     * الحصول على إجمالي المصاريف حسب الفئة
     * @param category اسم الفئة
     * @return مجموع مصاريف الفئة المحددة
     */
    public double getTotalExpensesByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_TOTAL_COST + ") FROM " + TABLE_PURCHASES + " WHERE " + COLUMN_CATEGORY + "=?", new String[]{category});

        double total = 0;
        if (cursor != null && cursor.moveToFirst()) {
            total = cursor.getDouble(0);
            cursor.close();
        }
        db.close();
        return total;
    }

    /**
     * الحصول على عدد المشتريات
     * @return عدد صفوف المشتريات
     */
    public int getPurchaseCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PURCHASES, null);

        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        db.close();
        return count;
    }
}
