package com.example.bmp601_hw;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bmp601_hw.db.DatabaseHelper;
import com.example.bmp601_hw.model.Purchase;
import com.example.bmp601_hw.utils.AudioHelper;
import com.example.bmp601_hw.utils.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * نشاط إضافة وتعديل المشتريات
 * يوفر واجهة لإدخال بيانات المشتري الجديد أو تعديل موجود
 */
public class AddEditActivity extends AppCompatActivity {

    private EditText etItemName, etPrice, etQuantity, etDate;
    private AutoCompleteTextView actvCategory;
    private TextView tvCalculatedTotal, tvTitle;
    private Button btnSave, btnCancel;
    private DatabaseHelper databaseHelper;
    private AudioHelper audioHelper;
    private PreferenceManager preferenceManager;

    private long selectedDate = System.currentTimeMillis();
    private long purchaseId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // تطبيق الثيم يجب أن يكون قبل super.onCreate
        preferenceManager = new PreferenceManager(this);
        applyTheme(preferenceManager.getTheme());

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit);

        // تهيئة المكونات
        initializeComponents();
        checkEditMode();
    }

    /**
     * تهيئة المكونات
     */
    private void initializeComponents() {
        databaseHelper = new DatabaseHelper(this);
        audioHelper = new AudioHelper(this);

        etItemName = findViewById(R.id.etItemName);
        etPrice = findViewById(R.id.etPrice);
        etQuantity = findViewById(R.id.etQuantity);
        etDate = findViewById(R.id.etDate);
        actvCategory = findViewById(R.id.actvCategory);
        tvCalculatedTotal = findViewById(R.id.tvCalculatedTotal);
        tvTitle = findViewById(R.id.tvTitle);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // تعيين التاريخ الحالي
        updateDateDisplay();

        // مستمع حقل التاريخ
        etDate.setOnClickListener(v -> showDatePicker());

        // مستمع حقول السعر والكمية لحساب الإجمالي
        etPrice.setOnFocusChangeListener((v, hasFocus) -> calculateTotal());
        etQuantity.setOnFocusChangeListener((v, hasFocus) -> calculateTotal());

        // مستمع زر الحفظ
        btnSave.setOnClickListener(v -> savePurchase());

        // مستمع زر الإلغاء
        btnCancel.setOnClickListener(v -> {
            audioHelper.playClickSound();
            finish();
        });
    }

    /**
     * التحقق من كون النشاط في وضع التعديل
     */
    private void checkEditMode() {
        Intent intent = getIntent();
        if (intent.hasExtra("purchase_id")) {
            purchaseId = intent.getLongExtra("purchase_id", -1);
            isEditMode = true;
            tvTitle.setText("تعديل المشتري");

            // تحميل بيانات المشتري
            Purchase purchase = databaseHelper.getPurchaseById(purchaseId);
            if (purchase != null) {
                etItemName.setText(purchase.getItemName());
                actvCategory.setText(purchase.getCategory());
                etPrice.setText(String.valueOf(purchase.getPrice()));
                etQuantity.setText(String.valueOf(purchase.getQuantity()));
                selectedDate = purchase.getDate();
                updateDateDisplay();
            }
        } else {
            tvTitle.setText("إضافة مشتري جديد");
        }
    }

    /**
     * عرض منتقي التاريخ
     */
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedDate);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year, month, dayOfMonth);
                    selectedDate = selectedCalendar.getTimeInMillis();
                    updateDateDisplay();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    /**
     * تحديث عرض التاريخ
     */
    private void updateDateDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        etDate.setText(sdf.format(new Date(selectedDate)));
    }

    /**
     * حساب وعرض التكلفة الإجمالية
     */
    private void calculateTotal() {
        try {
            double price = Double.parseDouble(etPrice.getText().toString());
            int quantity = Integer.parseInt(etQuantity.getText().toString());
            double total = price * quantity;
            tvCalculatedTotal.setText(String.format(Locale.getDefault(), "%.2f", total));
        } catch (NumberFormatException e) {
            tvCalculatedTotal.setText("0.00");
        }
    }

    /**
     * حفظ المشتري
     */
    private void savePurchase() {
        // التحقق من الحقول المطلوبة
        if (etItemName.getText().toString().isEmpty()) {
            Toast.makeText(this, "يرجى إدخال اسم العنصر", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(etPrice.getText().toString());
            int quantity = Integer.parseInt(etQuantity.getText().toString());

            if (price <= 0 || quantity <= 0) {
                Toast.makeText(this, "السعر والكمية يجب أن تكون أكبر من صفر", Toast.LENGTH_SHORT).show();
                return;
            }

            // تشغيل الصوت
            audioHelper.playSaveSound();

            if (isEditMode) {
                // تحديث المشتري الموجود
                Purchase purchase = new Purchase(
                        purchaseId,
                        etItemName.getText().toString(),
                        actvCategory.getText().toString(),
                        price,
                        quantity,
                        price * quantity,
                        selectedDate
                );
                databaseHelper.updatePurchase(purchase);
                Toast.makeText(this, "تم تحديث المشتري بنجاح", Toast.LENGTH_SHORT).show();
            } else {
                // إضافة مشتري جديد
                Purchase purchase = new Purchase(
                        etItemName.getText().toString(),
                        actvCategory.getText().toString(),
                        price,
                        quantity,
                        selectedDate
                );
                databaseHelper.addPurchase(purchase);
                Toast.makeText(this, "تم إضافة المشتري بنجاح", Toast.LENGTH_SHORT).show();
            }

            // العودة إلى النشاط الرئيسي
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "يرجى إدخال أرقام صحيحة", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * تطبيق الثيم
     */
    private void applyTheme(String theme) {
        switch (theme) {
            case PreferenceManager.THEME_DARK:
                setTheme(R.style.Theme_BMP601_HW_Dark);
                break;
            case PreferenceManager.THEME_ACCENT:
                setTheme(R.style.Theme_BMP601_HW_Accent);
                break;
            case PreferenceManager.THEME_LIGHT:
            default:
                setTheme(R.style.Theme_BMP601_HW_Light);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioHelper != null) {
            audioHelper.release();
        }
    }
}
