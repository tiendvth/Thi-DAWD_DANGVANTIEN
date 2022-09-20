package com.example.dawd_dangvantien;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "PracticalApp";

    private static final String TABLE_EMPLOYEE = "employees";

    private static final String KEY_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESIGNATION = "designation";
    private static final String COLUMN_SALARY = "salary";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EMPLOYEE_TABLE = "CREATE TABLE " + TABLE_EMPLOYEE
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " VARCHAR(255),"
                + COLUMN_DESIGNATION + " VARCHAR(255),"
                + COLUMN_SALARY + " DOUBLE" + ")";
        db.execSQL(CREATE_EMPLOYEE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
        // Create tables again
        onCreate(db);
    }

    public void createEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, employee.getName());
        values.put(COLUMN_DESIGNATION, employee.getDesignation());
        values.put(COLUMN_SALARY, employee.getSalary());
        db.insert(TABLE_EMPLOYEE, null, values);
        db.close();
    }

    public Employee findEmployeeById(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_EMPLOYEE, new String[] {
                        KEY_ID,COLUMN_NAME,COLUMN_DESIGNATION, COLUMN_SALARY
                }, KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        Employee employee = new Employee(Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getLong(3)
        );
        return employee;
    }

    public List<Employee> findAllEmployee() {
        List<Employee> employees = new ArrayList<>();
        String txtQuery = "SELECT * FROM " + TABLE_EMPLOYEE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(txtQuery, null);
        if(cursor.moveToFirst()) {
            do {
                Employee employee = new Employee();
                employee.setId(cursor.getLong(0));
                employee.setName(cursor.getString(1));
                employee.setDesignation(cursor.getString(2));
                employee.setSalary(cursor.getLong(3));
                employees.add(employee);
            }while (cursor.moveToNext());
        }
        return employees;
    }

    public int updateEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, employee.getName());
        values.put(COLUMN_DESIGNATION, employee.getDesignation());
        values.put(COLUMN_SALARY, employee.getSalary());

        // updating row
        return db.update(TABLE_EMPLOYEE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(employee.getId()) });
    }

    public void deleteEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EMPLOYEE, KEY_ID + " = ?",
                new String[] { String.valueOf(employee.getId()) });
        db.close();
    }
}