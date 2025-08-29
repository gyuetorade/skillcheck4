package com.example.sqlreal;

public final class StudentContract {
    private StudentContract() {}

    public static class StudentEntry {
        public static final String TABLE_NAME = "students";
        public static final String _ID = "_id";
        public static final String COLUMN_STUDENT_NUMBER = "student_number";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_GRADE = "grade";
    }
}
