package br.com.unidade2.restaurante.service;

import br.com.unidade2.restaurante.datastructures.AVLTree;
import br.com.unidade2.restaurante.datastructures.HashTableChaining;
import br.com.unidade2.restaurante.datastructures.MinHeap;
import br.com.unidade2.restaurante.datastructures.QueueWithTwoStacks;
import br.com.unidade2.restaurante.domain.Pedido;
import java.util.ArrayList;
import java.util.List;

public class AtendimentoRestauranteService {

    private final QueueWithTwoStacks<Integer> filaPedidos = new QueueWithTwoStacks<>();
    private final AVLTree<Integer, Pedido> pedidosPorId = new AVLTree<>();
    private final MinHeap<Pedido> filaPrioridade = new MinHeap<>();
    private final HashTableChaining<String, Integer> pedidosPendentesPorCliente = new HashTableChaining<>();
    private final HashTableChaining<Integer, Boolean> pedidosAtivos = new HashTableChaining<>();

    public void registrarPedido(Pedido pedido) {
        if (pedidosPorId.containsKey(pedido.getId())) {
            throw new IllegalArgumentException("Ja existe pedido com esse ID");
        }

        pedidosPorId.put(pedido.getId(), pedido);
        filaPedidos.enqueue(pedido.getId());
        filaPrioridade.insert(pedido);
        pedidosAtivos.put(pedido.getId(), true);

        Integer totalCliente = pedidosPendentesPorCliente.get(pedido.getCliente());
        pedidosPendentesPorCliente.put(pedido.getCliente(), totalCliente == null ? 1 : totalCliente + 1);
    }

    public Pedido chamarProximoPedidoFIFO() {
        while (!filaPedidos.isEmpty()) {
            int id = filaPedidos.dequeue();
            if (pedidosAtivos.remove(id) != null) {
                Pedido pedido = pedidosPorId.remove(id);
                decrementarPendenteCliente(pedido.getCliente());
                return pedido;
            }
        }
        return null;
    }

    public Pedido chamarPedidoMaisPrioritario() {
        while (!filaPrioridade.isEmpty()) {
            Pedido pedido = filaPrioridade.extractMin();
            if (pedidosAtivos.remove(pedido.getId()) != null) {
                pedidosPorId.remove(pedido.getId());
                decrementarPendenteCliente(pedido.getCliente());
                return pedido;
            }
        }
        return null;
    }

    public Pedido buscarPedidoPorId(int id) {
        return pedidosPorId.get(id);
    }

    public int totalPedidosPendentesCliente(String cliente) {
        Integer total = pedidosPendentesPorCliente.get(cliente);
        return total == null ? 0 : total;
    }

    public int totalPedidosPendentes() {
        return pedidosPorId.size();
    }

    public List<Pedido> listarPedidosPendentesOrdenadosPorId() {
        List<Pedido> pedidos = new ArrayList<>();
        pedidosPorId.inOrderTraversal((id, pedido) -> pedidos.add(pedido));
        return pedidos;
    }

    public int totalPedidosAtePrioridade(int prioridadeMaxima) {
        if (prioridadeMaxima <= 0) {
            throw new IllegalArgumentException("Prioridade maxima deve ser maior que zero");
        }

        int total = 0;
        List<Pedido> pedidos = listarPedidosPendentesOrdenadosPorId();
        for (Pedido pedido : pedidos) {
            if (pedido.getPrioridade() <= prioridadeMaxima) {
                total++;
            }
        }
        return total;
    }

    private void decrementarPendenteCliente(String cliente) {
        Integer total = pedidosPendentesPorCliente.get(cliente);
        if (total == null) {
            return;
        }
        if (total <= 1) {
            pedidosPendentesPorCliente.remove(cliente);
        } else {
            pedidosPendentesPorCliente.put(cliente, total - 1);
        }
    }
}
