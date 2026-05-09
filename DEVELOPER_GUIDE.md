# تطبيق دفتر المشتريات المنزلية (Home Purchase Manager)
## دليل المستخدم والمطور

---

## 📱 نظرة عامة على التطبيق

تطبيق أندرويد شامل لإدارة وتتبع المشتريات اليومية المنزلية. يوفر واجهة رسومية سهلة الاستخدام مع قاعدة بيانات محلية (SQLite) ودعم كامل للثيمات المخصصة.

---

## 🎯 الميزات الرئيسية

### 1. إدارة المشتريات (CRUD)
- **إضافة مشتريات جديدة**: واجهة بسيطة لإدخال تفاصيل المشتري
- **تعديل المشتريات**: تحديث البيانات المسجلة مسبقاً
- **حذف المشتريات**: حذف بنقرة طويلة على العنصر
- **عرض المشتريات**: قائمة ديناميكية بجميع المشتريات

### 2. حساب تلقائي للتكاليف
- حساب `total_cost = price × quantity` بشكل فوري
- عرض الحساب أثناء الإدخال

### 3. نظام الفئات
- تصنيف المشتريات حسب الفئة (طعام، فواتير، ملابس، إلخ)
- تصفية سريعة حسب الفئة من قائمة الخيارات

### 4. الإحصائيات
- **إجمالي المصاريف العام**: مجموع كل النفقات
- **المصاريف حسب الفئة**: تفصيل تكاليف كل فئة على حدة

### 5. نظام الثيمات
- **Light Theme**: واجهة فاتحة للاستخدام اليومي
- **Dark Theme**: واجهة مظلمة لتخفيف إجهاد العين
- **Accent Theme**: واجهة مميزة بألوان جريئة
- **حفظ تلقائي**: يتم حفظ اختيار الثيم وتطبيقه تلقائياً

### 6. التاريخ والوقت
- منتقي تاريخ متقدم (Date Picker)
- تسجيل التاريخ بدقة الميلي ثانية
- عرض التاريخ بصيغة قابلة للقراءة (dd/MM/yyyy)

### 7. المؤثرات الصوتية
- صوت تنبيهي عند الحفظ بنجاح
- إدارة ذكية للذاكرة لتجنب التسرب

---

## 🗄️ هيكل قاعدة البيانات

### جدول المشتريات (purchases)
| العمود | النوع | الوصف |
|--------|-------|-------|
| id | INTEGER PRIMARY KEY | معرف فريد تلقائي |
| item_name | TEXT NOT NULL | اسم العنصر (إلزامي) |
| category | TEXT | الفئة (اختياري) |
| price | REAL | السعر الواحد |
| quantity | INTEGER | الكمية |
| total_cost | REAL | التكلفة الإجمالية (price × quantity) |
| date | INTEGER | التاريخ (ملحوظة بالميلي ثانية) |

### جدول الفئات (categories)
| العمود | النوع | الوصف |
|--------|-------|-------|
| id | INTEGER PRIMARY KEY | معرف فريد تلقائي |
| name | TEXT NOT NULL | اسم الفئة (إلزامي) |
| description | TEXT | وصف الفئة (اختياري) |

---

## 📂 هيكل المشروع

```
app/src/main/
├── java/com/example/bmp601_hw/
│   ├── MainActivity.java              # النشاط الرئيسي
│   ├── AddEditActivity.java           # إضافة/تعديل المشتريات
│   ├── StatisticsActivity.java        # عرض الإحصائيات
│   ├── db/
│   │   └── DatabaseHelper.java        # إدارة قاعدة البيانات
│   ├── model/
│   │   ├── Purchase.java              # نموذج المشتري
│   │   └── Category.java              # نموذج الفئة
│   ├── adapter/
│   │   ├── PurchaseAdapter.java       # عرض المشتريات
│   │   └── CategoryExpenseAdapter.java# عرض الإحصائيات
│   └── utils/
│       ├── PreferenceManager.java     # إدارة التفضيلات
│       └── AudioHelper.java           # إدارة الأصوات
├── res/
│   ├── layout/
│   │   ├── activity_main.xml          # الشاشة الرئيسية
│   │   ├── activity_add_edit.xml      # واجهة الإضافة/التعديل
│   │   ├── activity_statistics.xml    # شاشة الإحصائيات
│   │   ├── item_purchase.xml          # عنصر واحد من المشتريات
│   │   └── item_category_expense.xml  # عنصر الإحصائية
│   ├── values/
│   │   ├── colors.xml                 # الألوان (Light/Dark/Accent)
│   │   ├── themes.xml                 # الأنماط
│   │   ├── strings.xml                # النصوص بالعربية
│   ├── menu/
│   │   └── menu_main.xml              # قائمة الخيارات
│   └── raw/
│       ├── save_sound.wav             # صوت الحفظ
│       └── click_sound.wav            # صوت النقر
└── AndroidManifest.xml                # ملف المشروع
```

---

## 🎨 الألوان والأنماط

### Light Theme
- Primary: `#6200EE` (بنفسجي)
- Primary Dark: `#3700B3`
- Secondary: `#03DAC6` (فيروزي)
- Background: `#FFFFFF` (أبيض)

### Dark Theme
- Primary: `#BB86FC` (بنفسجي فاتح)
- Primary Dark: `#3700B3`
- Secondary: `#03DAC6` (فيروزي)
- Background: `#121212` (أسود)

### Accent Theme
- Primary: `#FF6B6B` (أحمر)
- Primary Dark: `#EE5A6F`
- Secondary: `#4ECDC4` (أخضر)
- Background: `#F7F7F7` (رمادي فاتح)

---

## 🔧 المكتبات المستخدمة

### معتمدة بشكل أساسي
- `androidx.appcompat` - دعم المكونات الكلاسيكية
- `com.google.android.material` - Material Design
- `androidx.recyclerview` - عرض القوائم الديناميكية
- `androidx.lifecycle` - إدارة دورة حياة النشاط

### الاستخدام الخارجي
- SQLite - قاعدة بيانات محلية
- SharedPreferences - تخزين التفضيلات
- SoundPool - تشغيل الأصوات

---

## 👨‍💻 دليل المطور

### إضافة فئة جديدة

```java
DatabaseHelper dbHelper = new DatabaseHelper(this);
Category category = new Category("طعام", "مصاريف الطعام واللحوم");
long categoryId = dbHelper.addCategory(category);
```

### إضافة مشتري جديد

```java
Purchase purchase = new Purchase(
    "شراء برتقال",
    "طعام",
    150.00,      // السعر
    2,           // الكمية
    System.currentTimeMillis()  // التاريخ الحالي
);
purchase.calculateTotalCost();  // حساب الإجمالي
long purchaseId = dbHelper.addPurchase(purchase);
```

### الحصول على جميع المشتريات

```java
List<Purchase> purchases = dbHelper.getAllPurchases();
```

### تصفية حسب الفئة

```java
List<Purchase> foodPurchases = dbHelper.getPurchasesByCategory("طعام");
```

### تصفية حسب التاريخ

```java
Calendar startDate = Calendar.getInstance();
Calendar endDate = Calendar.getInstance();
endDate.add(Calendar.DAY_OF_MONTH, -7);  // آخر 7 أيام

List<Purchase> recentPurchases = dbHelper.getPurchasesByDateRange(
    endDate.getTimeInMillis(),
    startDate.getTimeInMillis()
);
```

### الحصول على الإحصائيات

```java
double totalExpenses = dbHelper.getTotalExpenses();
double categoryExpense = dbHelper.getTotalExpensesByCategory("طعام");
int purchaseCount = dbHelper.getPurchaseCount();
```

### إدارة الثيمات

```java
PreferenceManager prefManager = new PreferenceManager(this);

// تعيين الثيم
prefManager.setTheme(PreferenceManager.THEME_DARK);

// الحصول على الثيم الحالي
String currentTheme = prefManager.getTheme();

// التحقق من الثيم
if (prefManager.isDarkTheme()) {
    // تطبيق Dark Theme
}
```

---

## 🚀 كيفية الاستخدام

### الشاشة الرئيسية
1. اضغط على زر **+** العائم لإضافة مشتري جديد
2. اضغط على أي عنصر لتعديله
3. اضغط طويلاً على عنصر لحذفه
4. استخدم قائمة الخيارات (⋮) للإحصائيات والثيمات

### إضافة مشتري
1. أدخل اسم العنصر (إلزامي)
2. اختر الفئة (اختياري)
3. أدخل السعر والكمية
4. سيتم حساب الإجمالي تلقائياً
5. اختر التاريخ من Date Picker
6. اضغط **حفظ**

### عرض الإحصائيات
1. اضغط على **إحصائيات** من القائمة
2. شاهد الإجمالي العام
3. شاهد تفصيل المصاريف حسب الفئة

### تغيير الثيم
1. اضغط على **تغيير الثيم** من القائمة
2. اختر أحد الأنماط (فاتح / مظلم / مميز)
3. سيتم حفظ اختيارك تلقائياً

---

## 🐛 معالجة الأخطاء

التطبيق يتعامل مع:
- ✓ الحقول الفارغة
- ✓ الأرقام غير الصحيحة
- ✓ التاريخ غير الصالح
- ✓ تسرب الذاكرة (Audio Release)

---

## 📝 ملاحظات ملخصة

- جميع النصوص بالعربية
- دعم كامل للـ RTL (Right-to-Left)
- تعليقات توضيحية شاملة بالعربية
- بدون متطلبات صلاحيات إضافية
- مستقل تماماً وبدون اتصال بالإنترنت

---

**إصدار 1.0** - المشروع جاهز للاستخدام والتطوير 🎉
