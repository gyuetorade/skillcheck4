package com.example.sqlreal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sqlreal.StudentContract.StudentEntry;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "StudentDatabase.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_STUDENTS_TABLE = "CREATE TABLE " + StudentEntry.TABLE_NAME + " ("
                + StudentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + StudentEntry.COLUMN_STUDENT_NUMBER + " TEXT UNIQUE NOT NULL,"
                + StudentEntry.COLUMN_NAME + " TEXT NOT NULL,"
                + StudentEntry.COLUMN_GRADE + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_STUDENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StudentEntry.TABLE_NAME);
        onCreate(db);
    }

    // Insert a new student
    public long insertStudent(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        long newRowId = db.insert(StudentEntry.TABLE_NAME, null, values);
        db.close();
        return newRowId;
    }

    // Update an existing student's data
    public int updateStudent(String studentNumber, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = StudentEntry.COLUMN_STUDENT_NUMBER + " = ?";
        String[] selectionArgs = { studentNumber };
        int count = db.update(StudentEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();
        return count;
    }

    // Delete a student by student number
    public int deleteStudent(String studentNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = StudentEntry.COLUMN_STUDENT_NUMBER + " = ?";
        String[] selectionArgs = { studentNumber };
        int deletedRows = db.delete(StudentEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
        return deletedRows;
    }

    // Retrieve all students
    public Cursor getAllStudents() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                StudentEntry._ID,
                StudentEntry.COLUMN_STUDENT_NUMBER,
                StudentEntry.COLUMN_NAME,
                StudentEntry.COLUMN_GRADE
        };
        String sortOrder = StudentEntry.COLUMN_NAME + " ASC";
        return db.query(StudentEntry.TABLE_NAME, projection, null, null, null, null, sortOrder);
    }
}
