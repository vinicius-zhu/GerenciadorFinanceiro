package br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.model.ContaCorrente;

public class ContaCorrenteDAO {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    public ContaCorrenteDAO(Context context) {
        this.dbHelper = new SQLiteHelper(context);
    }

    public void salvaConta(ContaCorrente c) {
        database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.ACCOUNT_TITLE, c.getDescricao());
        values.put(SQLiteHelper.ACCOUNT_VALUE, c.getSaldo());


        if (c.getId() > 0) {
            database.update(SQLiteHelper.TABLE_ACCOUNT, values, SQLiteHelper.ACCOUNT_ID + "=" + c.getId(), null);

        }    else {
            database.insert(SQLiteHelper.TABLE_ACCOUNT, null, values);
        }

        database.close();
    }

    public void excluirConta(ContaCorrente c) {
        database = dbHelper.getWritableDatabase();
        database.delete(SQLiteHelper.TABLE_ACCOUNT, SQLiteHelper.ACCOUNT_ID + "=" + c.getId(), null);
        database.close();
    }


    public ContaCorrente buscarConta(String id_conta) {
        database = dbHelper.getReadableDatabase();
        ContaCorrente contaCorrente = null;

        Cursor cursor;

        String[] cols = new String[] { SQLiteHelper.ACCOUNT_ID, SQLiteHelper.ACCOUNT_TITLE, SQLiteHelper.ACCOUNT_VALUE};

        String where = SQLiteHelper.ACCOUNT_ID + " = ?";
        String[] argWhere = new String[]{ id_conta };

        cursor = database.query(SQLiteHelper.TABLE_ACCOUNT, cols, where , argWhere, null, null, null);

        if (cursor.moveToFirst()) {
            contaCorrente = new ContaCorrente();

            contaCorrente.setId(cursor.getInt(0));
            contaCorrente.setDescricao(cursor.getString(1));
            contaCorrente.setSaldo(cursor.getDouble(2));
        }

        cursor.close();

        database.close();
        return contaCorrente;
    }

    public List<ContaCorrente> buscarTodasContas() {
        database = dbHelper.getReadableDatabase();
        List<ContaCorrente> contasCorrentes = new ArrayList<>();

        Cursor cursor;

        String[] cols = new String[] {
                SQLiteHelper.ACCOUNT_ID,
                SQLiteHelper.ACCOUNT_TITLE,
                SQLiteHelper.ACCOUNT_VALUE
        };

        cursor = database.query(SQLiteHelper.TABLE_ACCOUNT, cols, null , null,
                null, null, SQLiteHelper.ACCOUNT_TITLE);

        while (cursor.moveToNext()) {
            ContaCorrente contaCorrente = new ContaCorrente();

            contaCorrente.setId(cursor.getInt(0));
            contaCorrente.setDescricao(cursor.getString(1));
            contaCorrente.setSaldo(cursor.getDouble(2));

            contasCorrentes.add(contaCorrente);
        }

        cursor.close();

        database.close();
        return contasCorrentes;
    }

}

