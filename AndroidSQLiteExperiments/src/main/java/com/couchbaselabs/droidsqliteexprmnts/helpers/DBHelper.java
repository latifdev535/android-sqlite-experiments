package com.couchbaselabs.droidsqliteexprmnts.helpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.couchbaselabs.droidsqliteexprmnts.ExperimentUIActivity;

import java.io.File;

public class DBHelper {

    public static final String SCHEMA = "" +
            "CREATE TABLE testrows ( " +
            "        row_id INTEGER PRIMARY KEY, " +
            "        row_text TEXT UNIQUE NOT NULL); ";

    public static SQLiteDatabase openRandomDatabase(File baseDirectory) {

        String randomDatabaseName;
        SQLiteDatabase sqLiteDatabase;

        randomDatabaseName = getRandomDatabaseName();
        File randomDatabasePath = new File(baseDirectory, randomDatabaseName);
        sqLiteDatabase = SQLiteDatabase.openDatabase(randomDatabasePath.getPath(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return sqLiteDatabase;

    }

    public static void createTestTables(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(SCHEMA);
        } catch (SQLException e) {
            sqLiteDatabase.close();
            throw new RuntimeException(e);
        }
    }

    public static void runFakeQuery(SQLiteDatabase sqLiteDatabase) {
        String sql = "SELECT * FROM testrows";
        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery(sql, null);
            Log.d(ExperimentUIActivity.TAG, "Iterating over rows");
            while (cursor.moveToNext()) {
                long id = cursor.getLong(0);
                Log.d(ExperimentUIActivity.TAG, "Row: " + id);
            }
            Log.d(ExperimentUIActivity.TAG, "Done iterating over rows");

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void beginTransaction(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.beginTransaction();
    }

    public static void doFakeInsert(SQLiteDatabase sqLiteDatabase) {
        long rowId = 0;
        ContentValues args = new ContentValues();
        args.put("row_text", System.currentTimeMillis());
        rowId = sqLiteDatabase.insert("testrows", null, args);
        Log.d(ExperimentUIActivity.TAG, "Insert fake row: " + rowId + " with text: " + args.get("row_text"));
        if (rowId == -1) {
            throw new RuntimeException("Could not insert new row");
        }
    }

    public static void endTransaction(SQLiteDatabase sqLiteDatabase, boolean commit) {
        if (commit) {
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
        } else {
            sqLiteDatabase.endTransaction();
        }
    }

    private static String getRandomDatabaseName() {
        return String.format("testdb-%s", Long.toString(System.currentTimeMillis()));
    }

}

