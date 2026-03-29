package br.com.unidade2.restaurante.domain;

/**
 * Classe de pedido ordenação baseada em prioridade e tempo de chegada, o menor valor tem prioridade
 * caso tenha empate no nível de urgência, a prioridade é do pedido mais antigo.
 * @author Pedro, Beatriz, Julio e Livia
 * @since 20/03/2026
 */

public class Pedido implements Comparable<Pedido> {

    private final int id;
    private final String cliente;
    private final String descricao;
    private final int prioridade;
    private final long instanteChegada;

    public Pedido(int id, String cliente, String descricao, int prioridade) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do pedido deve ser positivo");
        }
        if (cliente == null || cliente.isBlank()) {
            throw new IllegalArgumentException("Cliente obrigatorio");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descricao obrigatoria");
        }
        if (prioridade <= 0) {
            throw new IllegalArgumentException("Prioridade deve ser maior que zero");
        }

        this.id = id;
        this.cliente = cliente;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.instanteChegada = System.nanoTime();
    }

    public int getId() {
        return id;
    }

    public String getCliente() {
        return cliente;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public long getInstanteChegada() {
        return instanteChegada;
    }

    @Override
    public int compareTo(Pedido other) {
        int cmp = Integer.compare(this.prioridade, other.prioridade);
        if (cmp != 0) {
            return cmp;
        }
        return Long.compare(this.instanteChegada, other.instanteChegada);
    }

    @Override
    public String toString() {
        return "Pedido{" +
            "id=" + id +
            ", cliente='" + cliente + '\'' +
            ", descricao='" + descricao + '\'' +
            ", prioridade=" + prioridade +
            '}';
    }
}
