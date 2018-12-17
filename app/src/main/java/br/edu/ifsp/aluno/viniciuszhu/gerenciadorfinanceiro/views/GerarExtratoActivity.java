package br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.R;
import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.controller.TransacaoAdapter;
import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.controller.TransacaoDAO;
import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.model.ContaCorrente;
import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.model.Transacao;

public class GerarExtratoActivity extends AppCompatActivity {

    private ContaCorrente contaCorrente;
    String mesExtrato = null;

    private TransacaoDAO opmDAO ;
    private RecyclerView recyclerViewOperacoesMonetarias;

    private List<Transacao> operacoesMonetarias = new ArrayList<>();
    private TextView textViewListaOperacoesVazia;

    private TransacaoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerar_extrato);

        this.contaCorrente = (ContaCorrente) getIntent().getSerializableExtra("contaCorrente");

        mesExtrato = (getIntent().getStringExtra("mes_extrato"));

        opmDAO= new TransacaoDAO(this);
        textViewListaOperacoesVazia = findViewById(R.id.lista_operacoes_vazia);

        recyclerViewOperacoesMonetarias = findViewById(R.id.recycler_view_operacoes_monetarias);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        recyclerViewOperacoesMonetarias.setLayoutManager(layout);

        adapter = new TransacaoAdapter(operacoesMonetarias, this);
        recyclerViewOperacoesMonetarias.setAdapter(adapter);

        gerarExtrato();
    }

    private void gerarExtrato(){
        String id_conta = String.valueOf(this.contaCorrente.getId());
        operacoesMonetarias.clear();

        String tipoExtrato = getIntent().getStringExtra("tipo_extrato");

        switch (tipoExtrato){
            case "Mês":
                int mesExtrato = Integer.parseInt(getIntent().getStringExtra("mes_extrato"));
                operacoesMonetarias.addAll(opmDAO.gerarExtratoPorMes(id_conta, String.valueOf(mesExtrato)));
                break;
            case "Categoria":
                String categoriaOperacao = getIntent().getStringExtra("categoria_extrato");
                operacoesMonetarias.addAll(opmDAO.gerarExtratoPorCategoria(id_conta, categoriaOperacao));
                break;
            case "Geral":
                operacoesMonetarias.addAll(opmDAO.gerarExtrato(id_conta));
                break;
        }

        recyclerViewOperacoesMonetarias.getAdapter().notifyDataSetChanged();

        if (recyclerViewOperacoesMonetarias.getAdapter().getItemCount() == 0) {
            textViewListaOperacoesVazia.setVisibility(View.VISIBLE);
        }
        else {
            textViewListaOperacoesVazia.setVisibility(View.GONE);
        }
    }
}
