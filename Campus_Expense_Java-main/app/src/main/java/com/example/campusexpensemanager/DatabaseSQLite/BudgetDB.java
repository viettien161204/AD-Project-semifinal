package com.example.campusexpensemanager.DatabaseSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BudgetDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "budget.db";
    private static final int DATABASE_VERSION = 3; // Tăng phiên bản cơ sở dữ liệu

    public static final String TABLE_BUDGET = "budget";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_STUDENTID = "studentID";// Thêm cột email

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_BUDGET + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_AMOUNT + " REAL, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_STUDENTID + " TEXT" + // Thêm cột studentID
                    ");";

    public BudgetDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
        onCreate(db);
    }

    public float getTotalBudget() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM " + TABLE_BUDGET, null);
        float total = 0;

        if (cursor.moveToFirst()) {
            total = cursor.getFloat(0);
        }
        cursor.close();
        return total;
    }

    public void addBudget(float amount, String email, String studentID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_STUDENTID, studentID); // Lưu studentID
        db.insert(TABLE_BUDGET, null, values);
    }

    public float getAllBudgetByStudentID(String studentID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_BUDGET + " WHERE " + COLUMN_STUDENTID + " = ?", new String[]{studentID});
        float total = 0;

        if (cursor.moveToFirst()) {
            total = cursor.getFloat(0);
        }
        cursor.close();
        return total;
    }

    public float getAllBudgetByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_BUDGET + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
        float total = 0;

        if (cursor.moveToFirst()) {
            total = cursor.getFloat(0);
        }
        cursor.close();
        return total;
    }

    public void updateBudget(float amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_BUDGET + " SET amount = ?", new Object[]{amount});
    }

    public void deleteBudget() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BUDGET);
    }
}