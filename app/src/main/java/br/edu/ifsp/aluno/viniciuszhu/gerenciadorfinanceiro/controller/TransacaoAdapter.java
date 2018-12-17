package br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.R;
import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.model.Transacao;

public class TransacaoAdapter extends RecyclerView.Adapter<TransacaoAdapter.OperacaoMonetariaViewHolder>   {

    private List<Transacao> operacoesMonetarias;
    private Context context;

    private static ItemClickListener clickListener;

    public TransacaoAdapter(List<Transacao> operacoesMonetarias, Context context) {
        this.operacoesMonetarias = operacoesMonetarias;
        this.context = context;
    }

    @Override
    public OperacaoMonetariaViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.celula_operacao_monetaria, parent, false);
        return new OperacaoMonetariaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OperacaoMonetariaViewHolder operacaoMonetariaViewHolder, int position) {
        Transacao transacao = operacoesMonetarias.get(position) ;
        operacaoMonetariaViewHolder.descricaoOperacao.setText(transacao.getDescricao());
        operacaoMonetariaViewHolder.valorOperacao.setText("R$ "+ transacao.getValor());
    }

    @Override
    public int getItemCount() {
        return operacoesMonetarias.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }


    public  class OperacaoMonetariaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView descricaoOperacao;
        final TextView valorOperacao;

        OperacaoMonetariaViewHolder(View view) {
            super(view);
            descricaoOperacao = view.findViewById(R.id.text_view_descricao_operacao);
            valorOperacao = view.findViewById(R.id.text_view_valor_operacao);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onItemClick(getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

}