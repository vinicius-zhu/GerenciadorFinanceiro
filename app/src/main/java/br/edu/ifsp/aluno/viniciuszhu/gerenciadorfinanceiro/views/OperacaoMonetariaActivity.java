package br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;

import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.R;
import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.controller.TransacaoDAO;
import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.model.ContaCorrente;
import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.model.Transacao;

public class OperacaoMonetariaActivity extends AppCompatActivity implements View.OnClickListener {

    private ContaCorrente contaCorrente;
    private String tipoOperacao;
    private ViewHolder holder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operacao_monetaria);

        this.contaCorrente = (ContaCorrente) getIntent().getSerializableExtra("contaCorrente");

        holder.spinnerTransact = findViewById(R.id.spinner_categorias);
        holder.spinnerMesOperacao = findViewById(R.id.spinner_mes_operacao);

        holder.editTextDescricaoOperacao = findViewById(R.id.editText_descricao_operacao);
        holder.editTextValorOperacao = findViewById(R.id.editText_valor_operacao);

        holder.radioGroupOperacaoSeRepete = findViewById(R.id.radio_group_operacao_se_repete);

        holder.linearLayoutPeriodoRepeticao = findViewById(R.id.linear_layout_periodo_repeticao);

        holder.btnCadastrarOperacao = findViewById(R.id.btn_cadastrar_operacao);

        holder.btnCadastrarOperacao.setOnClickListener(this);

        tipoOperacao = getIntent().getStringExtra("tipoOperacao");

        holder.spinnerTransact.setVisibility(View.VISIBLE);

        holder.radioGroupOperacaoSeRepete.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int opcaoSelecionada) {

                switch (opcaoSelecionada){
                    case R.id.radio_button_sim_repete:
                        holder.linearLayoutPeriodoRepeticao.setVisibility(View.VISIBLE);
                        break;

                    case R.id.radio_button_nao_repete:
                        holder.linearLayoutPeriodoRepeticao.setVisibility(View.GONE);
                        break;
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String categoriaOperacao;
        String mesOperacao;

        switch (id){
            case R.id.btn_cadastrar_operacao:
                categoriaOperacao = holder.spinnerTransact.getSelectedItem().toString();

                mesOperacao = holder.spinnerMesOperacao.getSelectedItem().toString();

                Transacao opm = new Transacao();
                opm.setDescricao(holder.editTextDescricaoOperacao.getText().toString());
                opm.setValor(Double.parseDouble(holder.editTextValorOperacao.getText().toString()));
                opm.setTipo(categoriaOperacao);
                opm.setMes(mesOperacao);
                opm.setId_conta(this.contaCorrente.getId());

                ArrayList<String> credito = new ArrayList<>();
                Collections.addAll(credito,getResources().getStringArray(R.array.categorias_credito));
                if(credito.contains(categoriaOperacao))
                {
                    tipoOperacao = "credito";
                }
                else
                {
                    tipoOperacao = "debito";
                }

                if(this.tipoOperacao.equals("credito")){
                    cadastrarCredito(opm);
                    double saldoAntigo = this.contaCorrente.getSaldo();
                    double valor = Double.parseDouble(holder.editTextValorOperacao.getText().toString());
                    double novoSaldo = (saldoAntigo + valor);
                    this.contaCorrente.setSaldo(novoSaldo);
                    atualizarSaldo(this.contaCorrente);
                }   else{
                    cadastrarDebito(opm);
                    double saldoAntigo = this.contaCorrente.getSaldo();
                    double valor = Double.parseDouble(holder.editTextValorOperacao.getText().toString());
                    double novoSaldo = (saldoAntigo - valor);
                    this.contaCorrente.setSaldo( novoSaldo );
                    atualizarSaldo(this.contaCorrente);
                }

                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();

                break;
            case R.id.btn_excluir_operacao:
                break;
        }
    }

    private void cadastrarCredito(Transacao opm){
        TransacaoDAO dao = new TransacaoDAO(this);
        dao.cadastrarCredito(opm);
    }

    private void cadastrarDebito(Transacao opm){
        TransacaoDAO dao = new TransacaoDAO(this);
        dao.cadastrarDebito(opm);
    }

    private void atualizarSaldo(ContaCorrente contaCorrente){
        TransacaoDAO dao = new TransacaoDAO(this);
        dao.atualizarSaldo(contaCorrente);
    }

    private static class ViewHolder{
        EditText editTextDescricaoOperacao;
        EditText editTextValorOperacao;

        RadioGroup radioGroupOperacaoSeRepete;

        Spinner spinnerTransact;
        Spinner spinnerMesOperacao;

        LinearLayout linearLayoutPeriodoRepeticao;

        Button btnCadastrarOperacao;
    }
}
