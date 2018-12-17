package br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.R;
import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.controller.ContaCorrenteAdapter;
import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.controller.ContaCorrenteDAO;
import br.edu.ifsp.aluno.viniciuszhu.gerenciadorfinanceiro.model.ContaCorrente;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ContaCorrenteDAO cDAO ;

    double saldoTotal = 0;

    private LinearLayout linearLayoutContas;
    private LinearLayout linearLayoutSaldoTotal;
    private RecyclerView recyclerViewContas;

    private List<ContaCorrente> contasCorrentes = new ArrayList<>();
    private TextView empty;
    private TextView saldoTotalContas;

    private ContaCorrenteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cDAO= new ContaCorrenteDAO(this);
        empty = findViewById(R.id.lista_contas_vazia);
        linearLayoutContas = findViewById(R.id.linear_layout_contas);
        saldoTotalContas = findViewById(R.id.saldo_total_contas);

        linearLayoutSaldoTotal = findViewById(R.id.linear_layout_saldo_total);

        recyclerViewContas = findViewById(R.id.recycler_view_contas);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        recyclerViewContas.setLayoutManager(layout);

        adapter = new ContaCorrenteAdapter(contasCorrentes, this);
        recyclerViewContas.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        atualizarUI();
    }

    @Override
    public void onBackPressed() {
        // Faz o tratamento para fechar o NavigationDrawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            linearLayoutSaldoTotal.setVisibility(View.GONE);
            linearLayoutContas.setVisibility(View.VISIBLE);
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.adicionar_conta:
                Intent intent = new Intent(getApplicationContext(), ContaActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.nav_saldo:
                List<ContaCorrente> contasCorrentes;
                contasCorrentes = cDAO.buscarTodasContas();
                saldoTotal = calcularSaldoTotal(contasCorrentes);
                linearLayoutSaldoTotal.setVisibility(View.VISIBLE);
                linearLayoutContas.setVisibility(View.GONE);
                saldoTotalContas.setText(getResources().getText(R.string.currency) +String.valueOf(saldoTotal));
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                List<ContaCorrente> contasCorrentes;
                contasCorrentes = cDAO.buscarTodasContas();
                saldoTotal = 0;
                saldoTotal = calcularSaldoTotal(contasCorrentes);
                saldoTotalContas.setText(getResources().getText(R.string.currency) +String.valueOf(saldoTotal));

                showSnackBar(getResources().getString(R.string.conta_cadastrada_com_sucesso));
                atualizarUI();
            }
        }

        if (requestCode == 2) {             // REQUEST CODE
            if (resultCode == RESULT_OK)
                showSnackBar(getResources().getString(R.string.conta_modificada));

            if (resultCode == 3) {                //RESULT CODE
                showSnackBar(getResources().getString(R.string.conta_excluida));
                List<ContaCorrente> contasCorrentes;
                contasCorrentes = cDAO.buscarTodasContas();
                saldoTotal = 0;
                saldoTotal = calcularSaldoTotal(contasCorrentes);
                saldoTotalContas.setText(getResources().getText(R.string.currency) +String.valueOf(saldoTotal));
            }

            atualizarUI();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        configurarRecyclerView();
    }

        private void showSnackBar(String msg) {
        CoordinatorLayout coordinatorlayout = findViewById(R.id.coordinator_layout_main);
        Snackbar.make(coordinatorlayout, msg, Snackbar.LENGTH_LONG).show();
    }

    private void configurarRecyclerView() {
        adapter.setClickListener(new ContaCorrenteAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                final ContaCorrente contaCorrente;

                contaCorrente = contasCorrentes.get(position);
                Intent i = new Intent(getApplicationContext(), ContaActivity.class);
                i.putExtra("contaCorrente", contaCorrente);
                startActivityForResult(i, 2);

            }
        });
    }

    private double calcularSaldoTotal(List<ContaCorrente> contasCorrentes){
        double saldoTotal = 0;
        for(ContaCorrente c : contasCorrentes){
            saldoTotal += c.getSaldo();
        }

        return saldoTotal;
    }

    private void atualizarUI() {
        contasCorrentes.clear();

        contasCorrentes.addAll(cDAO.buscarTodasContas());

        recyclerViewContas.getAdapter().notifyDataSetChanged();

        if (recyclerViewContas.getAdapter().getItemCount() == 0) {
            recyclerViewContas.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }
        else {
            empty.setVisibility(View.GONE);
            recyclerViewContas.setVisibility(View.VISIBLE);
        }
    }
}
