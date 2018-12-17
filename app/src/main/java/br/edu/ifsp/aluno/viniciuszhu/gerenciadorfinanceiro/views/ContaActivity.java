package br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.views;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.io.Serializable;

import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.R;
import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.controller.ContaCorrenteDAO;
import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.model.ContaCorrente;

public class ContaActivity extends AppCompatActivity implements View.OnClickListener {

    private ContaCorrente contaCorrente;
    private ContaCorrenteDAO contaCorrenteDAO;

    ViewHolder holder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);

        holder.botaoCadastrar = findViewById(R.id.btn_cadastrar_conta);

        holder.botaoCadastrar.setOnClickListener(this);

        if (getIntent().hasExtra("contaCorrente")) {
            this.contaCorrente = (ContaCorrente) getIntent().getSerializableExtra("contaCorrente");

            holder.botaoCadastrar.setText(R.string.modificar_conta);

            holder.botaoExcluir = findViewById(R.id.btn_excluir_conta);
            holder.botaoConsultarExtrato = findViewById(R.id.btn_consultar_extrato);
            holder.botaoGerarExtrato = findViewById(R.id.btn_gerar_extrato);
            holder.editTxt_descricao = findViewById(R.id.editText_descricao_conta);
            holder.editTxt_descricao.setText(contaCorrente.getDescricao());
            holder.editTxt_saldo_inicial = findViewById(R.id.editText_saldo_inicial_conta);
            holder.editTxt_saldo_inicial.setText( Double.toString(contaCorrente.getSaldo()));

            holder.linearLayoutSelecaoExtrato = findViewById(R.id.linear_layout_selecao_extrato);
            holder.radioGroupTipoOperacaoExtrato = findViewById(R.id.radio_group_tipo_operacao_extrato);

            holder.spinnerExtratoMes = findViewById(R.id.spinner_extrato_mes);
            holder.spinnerExtratoCategorias = findViewById(R.id.spinner_extrato_categorias);

            int posicao = contaCorrente.getDescricao().indexOf(" ");

            if (posicao == -1) {
                posicao = contaCorrente.getDescricao().length();
            }

            setTitle(contaCorrente.getDescricao().substring(0,posicao));
            holder.botaoExcluir.setVisibility(View.VISIBLE);
            holder.botaoConsultarExtrato.setVisibility(View.VISIBLE);
            holder.botaoExcluir.setOnClickListener(this);
            holder.botaoConsultarExtrato.setOnClickListener(this);
            holder.botaoGerarExtrato.setOnClickListener(this);

        }

        contaCorrenteDAO = new ContaCorrenteDAO(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (!getIntent().hasExtra("contaCorrente")) {

            MenuItem itemTransact = menu.findItem(R.id.registrar_transacao);
            itemTransact.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.registrar_transacao:
                Intent intentCredito = new Intent(getApplicationContext(), OperacaoMonetariaActivity.class);
                intentCredito.putExtra("contaCorrente", this.contaCorrente);
                startActivityForResult(intentCredito, 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        Intent resultIntent = new Intent();

        switch (id){
            case R.id.btn_cadastrar_conta:
                cadastrarConta();
                setResult(RESULT_OK,resultIntent);
                finish();
                break;
            case R.id.btn_excluir_conta:
                excluirConta();
                setResult(3, resultIntent);
                finish();
                break;
            case R.id.btn_consultar_extrato:
                exibirCamposParaExtrato();
                break;
            case R.id.btn_gerar_extrato:
                int id_radioBtn_selecionado = holder.radioGroupTipoOperacaoExtrato.getCheckedRadioButtonId();
                RadioButton radioBtnSelecionado = holder.radioGroupTipoOperacaoExtrato.findViewById(id_radioBtn_selecionado);
                gerarExtrato(String.valueOf(radioBtnSelecionado.getText()));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                showSnackBar(getResources().getString(R.string.transacao_cadastrada));
            }
        }

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                showSnackBar(getResources().getString(R.string.transacao_cadastrada));
            }
        }

        this.contaCorrente = contaCorrenteDAO.buscarConta(String.valueOf(this.contaCorrente.getId()));

        holder.editTxt_descricao.setText(this.contaCorrente.getDescricao());
        holder.editTxt_saldo_inicial.setText(String.valueOf(this.contaCorrente.getSaldo()));
    }

    private void showSnackBar(String msg) {
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_layout_conta);
        Snackbar.make(constraintLayout, msg, Snackbar.LENGTH_LONG).show();
    }

    private void excluirConta(){
        contaCorrenteDAO.excluirConta(contaCorrente);
        Intent resultIntent = new Intent();
        setResult(3,resultIntent);
        finish();
    }

    private void cadastrarConta() {
        String descricaoConta = ((EditText) findViewById(R.id.editText_descricao_conta)).getText().toString();
        String saldoConta = ((EditText) findViewById(R.id.editText_saldo_inicial_conta)).getText().toString();

        if (contaCorrente == null) {
            contaCorrente = new ContaCorrente();
        }

        contaCorrente.setDescricao(descricaoConta);
        contaCorrente.setSaldo(Double.parseDouble(saldoConta));

        contaCorrenteDAO.salvaConta(contaCorrente);
        Intent resultIntent = new Intent();
        setResult(RESULT_OK,resultIntent);
        finish();
    }

    public void exibirCamposParaExtrato(){
        holder.botaoConsultarExtrato.setVisibility(View.GONE);
        holder.botaoCadastrar.setVisibility(View.GONE);
        holder.botaoExcluir.setVisibility(View.GONE);
        holder.linearLayoutSelecaoExtrato.setVisibility(View.VISIBLE);
        holder.botaoGerarExtrato.setVisibility(View.VISIBLE);

        holder.radioGroupTipoOperacaoExtrato.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int opcaoSelecionada) {

                switch (opcaoSelecionada){
                    case R.id.radio_button_extrato_mes:
                        holder.spinnerExtratoCategorias.setVisibility(View.GONE);
                        holder.spinnerExtratoMes.setVisibility(View.VISIBLE);
                        break;

                    case R.id.radio_button_extrato_categoria:
                        holder.spinnerExtratoMes.setVisibility(View.GONE);
                        holder.spinnerExtratoCategorias.setVisibility(View.VISIBLE);
                        break;

                    case R.id.radio_button_geral:
                        holder.spinnerExtratoMes.setVisibility(View.GONE);
                        holder.spinnerExtratoCategorias.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    private void gerarExtrato(String tipoExtrato){
        String mesExtrato = "0";
        Intent intentGerarExtrato = new Intent(getApplicationContext(), GerarExtratoActivity.class);
        intentGerarExtrato.putExtra("contaCorrente", (Serializable) contaCorrente);
        intentGerarExtrato.putExtra("tipo_extrato", tipoExtrato);

        switch (tipoExtrato){
            case "Mês":
                mesExtrato = holder.spinnerExtratoMes.getSelectedItem().toString();
                break;

            case "Categoria":
                String categoriaOperacao = holder.spinnerExtratoCategorias.getSelectedItem().toString();
                intentGerarExtrato.putExtra("categoria_extrato", categoriaOperacao);
                break;
        }

        intentGerarExtrato.putExtra("mes_extrato", mesExtrato);
        startActivity(intentGerarExtrato);
    }

    private static class ViewHolder{
        EditText editTxt_descricao;
        EditText editTxt_saldo_inicial;

        Button botaoCadastrar;
        Button botaoExcluir;
        Button botaoConsultarExtrato;
        Button botaoGerarExtrato;

        RadioGroup radioGroupTipoOperacaoExtrato;

        Spinner spinnerExtratoMes;
        Spinner spinnerExtratoCategorias;

        LinearLayout linearLayoutSelecaoExtrato;
    }
}