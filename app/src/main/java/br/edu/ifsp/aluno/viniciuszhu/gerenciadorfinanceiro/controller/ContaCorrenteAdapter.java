package br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.R;
import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.model.ContaCorrente;

public class ContaCorrenteAdapter extends RecyclerView.Adapter<ContaCorrenteAdapter.ContaViewHolder>  {

    private List<ContaCorrente> contasCorrentes;
    private Context context;

    private static ItemClickListener clickListener;

    public ContaCorrenteAdapter(List<ContaCorrente> contasCorrentes, Context context) {
        this.contasCorrentes = contasCorrentes;
        this.context = context;
    }

    // transforma o arquivo xml em uma view na activity.
    @Override
    public ContaViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.celula_conta, parent, false);
        return new ContaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContaViewHolder contaViewHolder, int position) {
        ContaCorrente contaCorrente = contasCorrentes.get(position) ;
        contaViewHolder.descricaoConta.setText(contaCorrente.getDescricao());
    }

    @Override
    public int getItemCount() {
        return contasCorrentes.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }


    public  class ContaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView descricaoConta;

        ContaViewHolder(View view) {
            super(view);
            descricaoConta = view.findViewById(R.id.descricao_conta);

            descricaoConta.setOnClickListener(this);
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
