package br.bugtracker.aplicacao;

import br.bugtracker.arquivos.BugRepositorio;
import br.bugtracker.servicos.GerenciadorDeBugs;
import br.bugtracker.tarefas.TarefaAutoSalvar;

public class Main {

    public static void main(String[] args) {

        BugRepositorio repositorio = new BugRepositorio("dados/bugs.txt");
        GerenciadorDeBugs gerenciador = new GerenciadorDeBugs(repositorio);

        // Carrega bugs já existentes do arquivo
        gerenciador.carregar();

        // Thread de auto-salvamento a cada 60 segundos
        TarefaAutoSalvar tarefaAutoSalvar = new TarefaAutoSalvar(gerenciador, 60000);
        Thread threadAutoSalvar = new Thread(tarefaAutoSalvar, "AutoSalvarThread");
        threadAutoSalvar.start();

        Menu menu = new Menu();
        menu.escolha(gerenciador);

        // Finalizando o sistema
        tarefaAutoSalvar.parar();
        gerenciador.salvar();
    }
}