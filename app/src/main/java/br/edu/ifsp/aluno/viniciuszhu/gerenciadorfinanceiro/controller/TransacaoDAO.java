package br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.model.ContaCorrente;
import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.model.Transacao;

public class TransacaoDAO {

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    public TransacaoDAO(Context context) {
        this.dbHelper = new SQLiteHelper(context);
    }

    public void cadastrarCredito(Transacao transact) {
        database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.TRANSACT_TITLE, transact.getDescricao());
        values.put(SQLiteHelper.TRANSACT_AMOUNT, transact.getValor());
        values.put(SQLiteHelper.TRANSACT_TYPE, transact.getTipo());
        values.put(SQLiteHelper.TRANSACT_MONTH, transact.getMes());
        values.put(SQLiteHelper.TRANSACT_DIRECTION, 1);
        values.put(SQLiteHelper.ACCOUNT_ID, transact.getId_conta());

        database.insert(SQLiteHelper.TABLE_TRANSACT, null, values);

        database.close();
    }

    public void cadastrarDebito(Transacao transact) {
        database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.TRANSACT_TITLE, transact.getDescricao());
        values.put(SQLiteHelper.TRANSACT_AMOUNT, transact.getValor());
        values.put(SQLiteHelper.TRANSACT_TYPE, transact.getTipo());
        values.put(SQLiteHelper.TRANSACT_MONTH, transact.getMes());
        values.put(SQLiteHelper.TRANSACT_DIRECTION, 0);
        values.put(SQLiteHelper.ACCOUNT_ID, transact.getId_conta());

        database.insert(SQLiteHelper.TABLE_TRANSACT, null, values);

        database.close();
    }

    public List<Transacao> gerarExtrato(String id_conta) {
        database = dbHelper.getReadableDatabase();
        List<Transacao> operacoes = new ArrayList<>();

        Cursor cursor;
        int direction;
        String[] cols = new String[] { SQLiteHelper.TRANSACT_ID, SQLiteHelper.TRANSACT_TITLE, SQLiteHelper.TRANSACT_AMOUNT, SQLiteHelper.TRANSACT_DIRECTION};

        String where = SQLiteHelper.ACCOUNT_ID + " = ?";
        String[] argWhere = new String[]{ id_conta };

        cursor = database.query(SQLiteHelper.TABLE_TRANSACT, cols, where , argWhere,
                null, null, SQLiteHelper.TRANSACT_TITLE);

        while (cursor.moveToNext()) {
            Transacao transacao = new Transacao();

            transacao.setId_operacao(cursor.getInt(0));
            transacao.setDescricao(cursor.getString(1));
            direction = cursor.getInt(3);
            if (direction == 0)
            {
                direction = -1;
            }
            transacao.setValor(cursor.getDouble(2)*direction);

            operacoes.add(transacao);
        }

        cursor.close();

        database.close();
        return operacoes;
    }

    public void atualizarSaldo (ContaCorrente contaCorrente) {
        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.ACCOUNT_TITLE, contaCorrente.getDescricao());
        values.put(SQLiteHelper.ACCOUNT_VALUE, contaCorrente.getSaldo());

        database = dbHelper.getWritableDatabase();
        database.update(SQLiteHelper.TABLE_ACCOUNT, values, SQLiteHelper.ACCOUNT_ID + " = " + contaCorrente.getId(), null);
        database.close();
    }

    public List<Transacao> gerarExtratoPorMes(String id_conta, String mes) {
        database = dbHelper.getReadableDatabase();
        List<Transacao> operacoesMes = new ArrayList<>();

        Cursor transacoes;

        String[] cols = new String[]{SQLiteHelper.TRANSACT_ID, SQLiteHelper.TRANSACT_TITLE, SQLiteHelper.TRANSACT_AMOUNT, SQLiteHelper.TRANSACT_DIRECTION};

        String where = SQLiteHelper.ACCOUNT_ID +" = ? AND " + SQLiteHelper.TRANSACT_MONTH + " = ?";

        String[] argWhere = new String[]{id_conta, mes};

        transacoes = database.query(SQLiteHelper.TABLE_TRANSACT, cols, where, argWhere,
                null, null, SQLiteHelper.TRANSACT_TITLE);

        while (transacoes.moveToNext()) {
            Transacao transacao = new Transacao();

            transacao.setId_operacao(transacoes.getInt(0));
            transacao.setDescricao(transacoes.getString(1));
            int direction = transacoes.getInt(3);
            if (direction == 0)
            {
                direction = -1;
            }
            transacao.setValor(transacoes.getDouble(2)*direction);
            operacoesMes.add(transacao);
        }

        transacoes.close();
        database.close();
        return operacoesMes;
    }

    public List<Transacao> gerarExtratoPorCategoria(String id_conta, String categoriaOperacao) {
        database = dbHelper.getReadableDatabase();
        List<Transacao> transacoes = new ArrayList<>();

        Cursor cursor;

        String[] cols = new String[]{SQLiteHelper.TRANSACT_ID, SQLiteHelper.TRANSACT_TITLE, SQLiteHelper.TRANSACT_AMOUNT, SQLiteHelper.TRANSACT_DIRECTION};

        String where = SQLiteHelper.ACCOUNT_ID +" = ? AND " + SQLiteHelper.TRANSACT_TYPE + " = ?";
        String[] argWhere = new String[]{id_conta, categoriaOperacao};

        cursor = database.query(SQLiteHelper.TABLE_TRANSACT, cols, where, argWhere,
                    null, null, SQLiteHelper.TRANSACT_TITLE);

        while (cursor.moveToNext()) {
            Transacao transacao = new Transacao();

            transacao.setId_operacao(cursor.getInt(0));
            transacao.setDescricao(cursor.getString(1));
            int direction = cursor.getInt(3);
            if (direction == 0)
            {
                direction = -1;
            }
            transacao.setValor(cursor.getDouble(2)*direction);

            transacoes.add(transacao);
        }

        cursor.close();

        database.close();
        return transacoes;
    }
}
