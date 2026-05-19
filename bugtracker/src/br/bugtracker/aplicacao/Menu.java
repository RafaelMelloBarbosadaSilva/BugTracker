package br.bugtracker.aplicacao;

import br.bugtracker.modelo.*;
import br.bugtracker.servicos.GerenciadorDeBugs;

import java.util.List;
import java.util.Scanner;

public class Menu {

    public void escolha(GerenciadorDeBugs gerenciador) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            exibirMenu();

            System.out.print("Escolha uma opção: ");
            int opcao;

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida.");
                continue;
            }

            switch (opcao) {
                case 1 -> registrarBug(scanner, gerenciador);
                case 2 -> listarBugs(gerenciador);
                case 3 -> listarPorStatus(scanner, gerenciador);
                case 4 -> listarPorPrioridade(scanner, gerenciador);
                case 5 -> atualizarStatus(scanner, gerenciador);
                case 6 -> removerBug(scanner, gerenciador);
                case 0 -> {
                    continuar = false;
                    System.out.println("Saindo...");
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    public void exibirMenu() {
        System.out.println("\n===== BUG TRACKER =====");
        System.out.println("1 - Registrar novo bug");
        System.out.println("2 - Listar todos os bugs");
        System.out.println("3 - Listar bugs por status");
        System.out.println("4 - Listar bugs por prioridade");
        System.out.println("5 - Atualizar status de um bug");
        System.out.println("6 - Remover bug");
        System.out.println("0 - Sair");
    }

    private static void registrarBug(Scanner scanner, GerenciadorDeBugs gerenciador) {
        System.out.println("\n=== Registrar Bug ===");

        int id = gerenciador.gerarNovoId();

        System.out.print("Título: ");
        String titulo = scanner.nextLine();

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        Prioridade prioridade = lerPrioridade(scanner);
        StatusBug status = StatusBug.ABERTO;

        System.out.print("ID do desenvolvedor responsável ou -1 para nenhum: ");
        int devId = Integer.parseInt(scanner.nextLine());

        Desenvolvedor dev = null;

        if (devId != -1) {
            System.out.print("Nome do desenvolvedor: ");
            String nomeDev = scanner.nextLine();

            System.out.print("Email do desenvolvedor: ");
            String emailDev = scanner.nextLine();

            dev = new Desenvolvedor(devId, nomeDev, emailDev);
        }

        System.out.print("Tipo do bug 1 = Crítico, 2 = Menor: ");
        int tipo = Integer.parseInt(scanner.nextLine());

        Bug bug;

        if (tipo == 1) {
            bug = new BugCritico(id, titulo, descricao, prioridade, status, dev);
        } else {
            bug = new BugMenor(id, titulo, descricao, prioridade, status, dev);
        }

        gerenciador.registrarBug(bug);
        gerenciador.salvar();

        System.out.println("Bug registrado com sucesso! ID: " + bug.getId());
    }

    private static void listarBugs(GerenciadorDeBugs gerenciador) {
        System.out.println("\n=== Lista de Bugs ===");

        List<Bug> bugs = gerenciador.listarTodos();

        if (bugs.isEmpty()) {
            System.out.println("Nenhum bug cadastrado.");
        } else {
            for (Bug b : bugs) {
                System.out.println(b);
            }
        }
    }

    private static void listarPorStatus(Scanner scanner, GerenciadorDeBugs gerenciador) {
        System.out.println("\n=== Listar Bugs por Status ===");

        StatusBug status = lerStatus(scanner);
        List<Bug> bugs = gerenciador.listarPorStatus(status);

        if (bugs.isEmpty()) {
            System.out.println("Nenhum bug com status " + status);
        } else {
            bugs.forEach(System.out::println);
        }
    }

    private static void listarPorPrioridade(Scanner scanner, GerenciadorDeBugs gerenciador) {
        System.out.println("\n=== Listar Bugs por Prioridade ===");

        Prioridade prioridade = lerPrioridade(scanner);
        List<Bug> bugs = gerenciador.listarPorPrioridade(prioridade);

        if (bugs.isEmpty()) {
            System.out.println("Nenhum bug com prioridade " + prioridade);
        } else {
            bugs.forEach(System.out::println);
        }
    }

    private static void atualizarStatus(Scanner scanner, GerenciadorDeBugs gerenciador) {
        System.out.println("\n=== Atualizar Status de Bug ===");

        System.out.print("ID do bug: ");
        int id = Integer.parseInt(scanner.nextLine());

        StatusBug status = lerStatus(scanner);

        gerenciador.atualizarStatus(id, status);
        gerenciador.salvar();
    }

    private static void removerBug(Scanner scanner, GerenciadorDeBugs gerenciador) {
        System.out.println("\n=== Remover Bug ===");

        System.out.print("ID do bug: ");
        int id = Integer.parseInt(scanner.nextLine());

        gerenciador.removerBug(id);
        gerenciador.salvar();
    }

    private static Prioridade lerPrioridade(Scanner scanner) {
        System.out.println("Prioridade:");
        System.out.println("1 - BAIXA");
        System.out.println("2 - MEDIA");
        System.out.println("3 - ALTA");
        System.out.println("4 - CRITICA");
        System.out.print("Escolha: ");

        int op = Integer.parseInt(scanner.nextLine());

        return switch (op) {
            case 1 -> Prioridade.BAIXA;
            case 2 -> Prioridade.MEDIA;
            case 3 -> Prioridade.ALTA;
            case 4 -> Prioridade.CRITICA;
            default -> Prioridade.MEDIA;
        };
    }

    private static StatusBug lerStatus(Scanner scanner) {
        System.out.println("Status:");
        System.out.println("1 - ABERTO");
        System.out.println("2 - EM_ANDAMENTO");
        System.out.println("3 - RESOLVIDO");
        System.out.println("4 - FECHADO");
        System.out.print("Escolha: ");

        int op = Integer.parseInt(scanner.nextLine());

        return switch (op) {
            case 1 -> StatusBug.ABERTO;
            case 2 -> StatusBug.EM_ANDAMENTO;
            case 3 -> StatusBug.RESOLVIDO;
            case 4 -> StatusBug.FECHADO;
            default -> StatusBug.ABERTO;
        };
    }
}