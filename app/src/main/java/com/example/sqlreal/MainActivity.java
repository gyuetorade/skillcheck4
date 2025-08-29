package com.example.sqlreal;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sqlreal.DatabaseHelper;
import com.example.sqlreal.R;
import com.example.sqlreal.StudentContract;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText editTextStudentNumber, editTextName, editTextGrade;
    private TextView textViewResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Initialize the UI components
        editTextStudentNumber = findViewById(R.id.editTextStudentNumber);
        editTextName = findViewById(R.id.editTextName);
        editTextGrade = findViewById(R.id.editTextGrade);
        textViewResults = findViewById(R.id.textViewResults);

        // Button onClick listeners
        Button buttonInsert = findViewById(R.id.buttonInsert);
        buttonInsert.setOnClickListener(v -> insertStudent());

        Button buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(v -> updateStudent());

        Button buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(v -> deleteStudent());

        Button buttonView = findViewById(R.id.buttonView);
        buttonView.setOnClickListener(v -> viewAllStudents());

        // Display the most recent student by default
        viewRecentStudent();
    }

    // Insert a new student
    private void insertStudent() {
        String studentNumber = editTextStudentNumber.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String grade = editTextGrade.getText().toString().trim();

        if (studentNumber.isEmpty() || name.isEmpty() || grade.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(StudentContract.StudentEntry.COLUMN_STUDENT_NUMBER, studentNumber);
        values.put(StudentContract.StudentEntry.COLUMN_NAME, name);
        values.put(StudentContract.StudentEntry.COLUMN_GRADE, grade);

        long newRowId = dbHelper.insertStudent(values);

        if (newRowId != -1) {
            Toast.makeText(this, "Student inserted successfully!", Toast.LENGTH_SHORT).show();
            clearInputFields();
            viewRecentStudent();  // Refresh to show the most recent student
        } else {
            Toast.makeText(this, "Error inserting student (Number might already exist).", Toast.LENGTH_LONG).show();
        }
    }

    // Update an existing student
    private void updateStudent() {
        String studentNumber = editTextStudentNumber.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String grade = editTextGrade.getText().toString().trim();

        if (studentNumber.isEmpty()) {
            Toast.makeText(this, "Please enter Student Number to update", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.isEmpty() && grade.isEmpty()) {
            Toast.makeText(this, "Please enter new Name or Grade to update", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        if (!name.isEmpty()) values.put(StudentContract.StudentEntry.COLUMN_NAME, name);
        if (!grade.isEmpty()) values.put(StudentContract.StudentEntry.COLUMN_GRADE, grade);

        int rowsAffected = dbHelper.updateStudent(studentNumber, values);

        if (rowsAffected > 0) {
            Toast.makeText(this, rowsAffected + " student(s) updated!", Toast.LENGTH_SHORT).show();
            clearInputFields();
            viewRecentStudent(); // Refresh to show the most recent student
        } else {
            Toast.makeText(this, "No student found with that number or no data changed.", Toast.LENGTH_LONG).show();
        }
    }

    // Delete a student
    private void deleteStudent() {
        String studentNumber = editTextStudentNumber.getText().toString().trim();

        if (studentNumber.isEmpty()) {
            Toast.makeText(this, "Please enter Student Number to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        int rowsDeleted = dbHelper.deleteStudent(studentNumber);

        if (rowsDeleted > 0) {
            Toast.makeText(this, rowsDeleted + " student(s) deleted!", Toast.LENGTH_SHORT).show();
            clearInputFields();
            viewRecentStudent(); // Refresh to show the most recent student
        } else {
            Toast.makeText(this, "No student found with that number.", Toast.LENGTH_LONG).show();
        }
    }

    // View the most recent student (default view)
    private void viewRecentStudent() {
        Cursor cursor = dbHelper.getAllStudents();

        if (cursor != null && cursor.moveToLast()) {
            String studentNumber = cursor.getString(cursor.getColumnIndexOrThrow(StudentContract.StudentEntry.COLUMN_STUDENT_NUMBER));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(StudentContract.StudentEntry.COLUMN_NAME));
            String grade = cursor.getString(cursor.getColumnIndexOrThrow(StudentContract.StudentEntry.COLUMN_GRADE));

            String result = "Number: " + studentNumber + "\n" +
                    "Name: " + name + "\n" +
                    "Grade: " + grade + "\n" +
                    "----------------------------------";

            textViewResults.setText(result);
            cursor.close();
        }
    }

    // View all students
    private void viewAllStudents() {
        Cursor cursor = dbHelper.getAllStudents();

        StringBuilder results = new StringBuilder();
        results.append("Students:\n----------------------------------\n");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String studentNumber = cursor.getString(cursor.getColumnIndexOrThrow(StudentContract.StudentEntry.COLUMN_STUDENT_NUMBER));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(StudentContract.StudentEntry.COLUMN_NAME));
                String grade = cursor.getString(cursor.getColumnIndexOrThrow(StudentContract.StudentEntry.COLUMN_GRADE));

                results.append("Number: ").append(studentNumber).append("\n")
                        .append("Name: ").append(name).append("\n")
                        .append("Grade: ").append(grade).append("\n")
                        .append("----------------------------------\n");
            }
            cursor.close();
        }

        textViewResults.setText(results.toString());
    }

    // Clear input fields after an operation
    private void clearInputFields() {
        editTextStudentNumber.setText("");
        editTextName.setText("");
        editTextGrade.setText("");
    }
}
