package br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.controller;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "financas.db";

    public static final String TABLE_ACCOUNT = "account";
    public static final String ACCOUNT_ID = "id";
    public static final String ACCOUNT_TITLE = "title";
    public static final String ACCOUNT_VALUE = "value";

    public static final String TABLE_TRANSACT = "transact";
    public static final String TRANSACT_ID = "id";
    public static final String TRANSACT_TITLE = "title";
    public static final String TRANSACT_AMOUNT = "amount";
    public static final String TRANSACT_TYPE = "type";
    public static final String TRANSACT_MONTH = "month";
    public static final String TRANSACT_DIRECTION = "direction";
    public static final String TRANSACT_ACCOUNT_ID = "account_id";

    private static final int VERSAO_DATABASE = 1;

    private static final String CREATE_ACCOUNT_TABLE = "CREATE TABLE " + TABLE_ACCOUNT +" (" +
            ACCOUNT_ID +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ACCOUNT_TITLE + " TEXT NOT NULL, " +
            ACCOUNT_VALUE + " REAL NOT NULL)";

    private static final String CREATE_TRANSACT_TABLE = "CREATE TABLE " + TABLE_TRANSACT + " (" +
            TRANSACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TRANSACT_TITLE + " TEXT, " +
            TRANSACT_AMOUNT + " REAL, " +
            TRANSACT_TYPE + " TEXT, " +
            TRANSACT_MONTH + " TEXT, " +
            TRANSACT_DIRECTION + " INTEGER(1), " +
            TRANSACT_ACCOUNT_ID + " INTEGER, " +
            "FOREIGN KEY(" + TRANSACT_ACCOUNT_ID + ") REFERENCES " + TABLE_ACCOUNT + "(" + ACCOUNT_ID + "))";


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSAO_DATABASE);
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        try{
            database.execSQL(CREATE_ACCOUNT_TABLE);
            database.execSQL(CREATE_TRANSACT_TABLE);
        }   catch(SQLException e){
                Log.d("ERRO_SQL", "Erro ao criar o BD.\n");
                e.getMessage();
                e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
