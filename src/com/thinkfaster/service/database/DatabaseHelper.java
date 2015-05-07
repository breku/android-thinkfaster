package com.thinkfaster.service.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.thinkfaster.model.json.DeviceParameters;
import com.thinkfaster.service.DeviceParametersService;
import com.thinkfaster.util.LevelDifficulty;
import com.thinkfaster.util.MathParameter;
import org.apache.commons.lang3.StringUtils;

import static com.thinkfaster.service.DeviceParametersService.getDeviceName;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * User: Breku
 * Date: 07.10.13
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "myDB_brain_watt";
    private static final String HIGH_SCORES_TABLE_NAME = "HIGH_SCORES";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_LEVEL_DIFFICULTY = "LEVEL_DIFFICULTY";
    private static final String COLUMN_MATH_PARAMETER = "MATH_PARAMETER";
    private static final String COLUMN_SCORE = "SCORE";
    private static final String COLUMN_LOCKED = "LOCKED";

    private static final String OPTIONS_TABLE_NAME = "OPTIONS";
    private static final String COLUMN_USERNAME = "USERNAME";

    private static final int DATABASE_VERSION = 25;


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when database does not exists
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createScoreTable(sqLiteDatabase);
        createOptionsTable(sqLiteDatabase);
        createDefaultHighScoreValues(sqLiteDatabase);
        createDefaultOptions(sqLiteDatabase);
    }

    private void createDefaultOptions(SQLiteDatabase sqLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, getDeviceName());
        sqLiteDatabase.insert(OPTIONS_TABLE_NAME, null, contentValues);
    }

    /**
     * Is called when DATABASE_VERSION is upgraded
     *
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HIGH_SCORES_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OPTIONS_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public Integer getHighScoresFor(LevelDifficulty levelDifficulty, MathParameter mathParameter) {
        Integer result = null;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_SCORE + " FROM " + HIGH_SCORES_TABLE_NAME + " WHERE " + COLUMN_LEVEL_DIFFICULTY + " = ? AND " + COLUMN_MATH_PARAMETER + " = ?",
                new String[]{levelDifficulty.name(), mathParameter.name()});
        while (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return result;
    }

    public void updateRecordFor(LevelDifficulty levelDifficulty, MathParameter mathParameter, Integer score) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("UPDATE HIGH_SCORES SET SCORE = ? WHERE LEVEL_DIFFICULTY = ? AND MATH_PARAMETER = ?", new Object[]{score, levelDifficulty.name(), mathParameter.name()});
        database.close();
    }

    public void unlockLevel(LevelDifficulty levelDifficulty, MathParameter mathParameter) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("UPDATE HIGH_SCORES SET LOCKED = 0 WHERE LEVEL_DIFFICULTY = ? AND MATH_PARAMETER = ?", new String[]{levelDifficulty.name(), mathParameter.name()});
        database.close();
    }

    public boolean isLevelUnlocked(LevelDifficulty levelDifficulty, MathParameter mathParameter) {
        Integer result = null;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_LOCKED + " FROM " + HIGH_SCORES_TABLE_NAME + " WHERE " + COLUMN_LEVEL_DIFFICULTY + " = ? AND " + COLUMN_MATH_PARAMETER + " = ?",
                new String[]{levelDifficulty.name(), mathParameter.name()});
        while (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return result == 0;
    }


    public void updateUsername(String username) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("UPDATE OPTIONS SET USERNAME = ?", new String[]{username});
        database.close();
    }

    public String getUsername() {
        String result = EMPTY;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT USERNAME FROM " + OPTIONS_TABLE_NAME, new String[]{});
        while (cursor.moveToNext()) {
            result = cursor.getString(0);
        }
        cursor.close();
        database.close();
        return result;
    }

    private void createScoreTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + HIGH_SCORES_TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "LEVEL_DIFFICULTY TEXT, " +
                "MATH_PARAMETER TEXT, " +
                "SCORE INTEGER, " +
                "LOCKED INTEGER" +
                ")");
    }

    private void createOptionsTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + OPTIONS_TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "USERNAME TEXT" +
                ")");
    }

    private void createDefaultHighScoreValues(SQLiteDatabase sqLiteDatabase) {

        createDefaultHighScoreRecord(sqLiteDatabase, LevelDifficulty.EASY, MathParameter.ADD, 0);
        createDefaultHighScoreRecord(sqLiteDatabase, LevelDifficulty.EASY, MathParameter.SUB, 1);
        createDefaultHighScoreRecord(sqLiteDatabase, LevelDifficulty.EASY, MathParameter.MUL, 1);
        createDefaultHighScoreRecord(sqLiteDatabase, LevelDifficulty.EASY, MathParameter.DIV, 1);
        createDefaultHighScoreRecord(sqLiteDatabase, LevelDifficulty.EASY, MathParameter.ALL, 1);

        createDefaultHighScoreRecord(sqLiteDatabase, LevelDifficulty.MEDIUM, MathParameter.ADD, 1);
        createDefaultHighScoreRecord(sqLiteDatabase, LevelDifficulty.MEDIUM, MathParameter.SUB, 1);
        createDefaultHighScoreRecord(sqLiteDatabase, LevelDifficulty.MEDIUM, MathParameter.MUL, 1);
        createDefaultHighScoreRecord(sqLiteDatabase, LevelDifficulty.MEDIUM, MathParameter.DIV, 1);
        createDefaultHighScoreRecord(sqLiteDatabase, LevelDifficulty.MEDIUM, MathParameter.ALL, 1);

        createDefaultHighScoreRecord(sqLiteDatabase, LevelDifficulty.HARD, MathParameter.ADD, 1);
        createDefaultHighScoreRecord(sqLiteDatabase, LevelDifficulty.HARD, MathParameter.SUB, 1);
        createDefaultHighScoreRecord(sqLiteDatabase, LevelDifficulty.HARD, MathParameter.MUL, 1);
        createDefaultHighScoreRecord(sqLiteDatabase, LevelDifficulty.HARD, MathParameter.DIV, 1);
        createDefaultHighScoreRecord(sqLiteDatabase, LevelDifficulty.HARD, MathParameter.ALL, 1);
    }


    private void createDefaultHighScoreRecord(SQLiteDatabase sqLiteDatabase, LevelDifficulty levelDifficulty, MathParameter mathParameter, Integer locked) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_LEVEL_DIFFICULTY, levelDifficulty.name());
        contentValues.put(COLUMN_MATH_PARAMETER, mathParameter.name());
        contentValues.put(COLUMN_SCORE, 0);
        contentValues.put(COLUMN_LOCKED, locked);
        sqLiteDatabase.insert(HIGH_SCORES_TABLE_NAME, null, contentValues);

    }


}
