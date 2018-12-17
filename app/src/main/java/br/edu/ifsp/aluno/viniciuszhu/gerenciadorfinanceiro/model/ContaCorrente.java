package br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.model;

import java.io.Serializable;

public class ContaCorrente implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String descricao;
    private double saldo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
