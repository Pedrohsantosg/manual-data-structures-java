package br.com.unidade2.restaurante.service;

import br.com.unidade2.restaurante.domain.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AtendimentoRestauranteService")
class AtendimentoRestauranteServiceTest {

    private AtendimentoRestauranteService service;

    // helpers
    private static Pedido pedido(int id, String cliente, int prioridade) {
        return new Pedido(id, cliente, "desc-" + id, prioridade);
    }

    @BeforeEach
    void setUp() {
        service = new AtendimentoRestauranteService();
    }

    // ── registrarPedido ───────────────────────────────────────────────────────

    @Test
    @DisplayName("registrarPedido deve adicionar o pedido e incrementar total pendente")
    void registrarPedidoIncrementaTotal() {
        service.registrarPedido(pedido(1, "Ana", 2));
        assertEquals(1, service.totalPedidosPendentes());
    }

    @Test
    @DisplayName("registrarPedido deve contabilizar pendentes por cliente")
    void registrarPedidoContabilizaPorCliente() {
        service.registrarPedido(pedido(1, "Ana", 1));
        service.registrarPedido(pedido(2, "Ana", 2));
        service.registrarPedido(pedido(3, "Bob", 1));

        assertEquals(2, service.totalPedidosPendentesCliente("Ana"));
        assertEquals(1, service.totalPedidosPendentesCliente("Bob"));
    }

    @Test
    @DisplayName("registrarPedido com ID duplicado deve lançar IllegalArgumentException")
    void registrarPedidoIdDuplicadoLancaExcecao() {
        service.registrarPedido(pedido(1, "Ana", 1));
        assertThrows(IllegalArgumentException.class,
                () -> service.registrarPedido(pedido(1, "Bob", 2)));
    }

    // ── buscarPedidoPorId ─────────────────────────────────────────────────────

    @Test
    @DisplayName("buscarPedidoPorId deve retornar o pedido correto")
    void buscarPedidoPorIdRetornaCorreto() {
        Pedido p = pedido(10, "Carlos", 3);
        service.registrarPedido(p);
        Pedido encontrado = service.buscarPedidoPorId(10);
        assertNotNull(encontrado);
        assertEquals(10, encontrado.getId());
        assertEquals("Carlos", encontrado.getCliente());
    }

    @Test
    @DisplayName("buscarPedidoPorId para ID inexistente deve retornar null")
    void buscarPedidoPorIdInexistenteRetornaNull() {
        assertNull(service.buscarPedidoPorId(999));
    }

    // ── chamarProximoPedidoFIFO ───────────────────────────────────────────────

    @Test
    @DisplayName("chamarProximoPedidoFIFO deve retornar pedidos na ordem de chegada")
    void chamarFIFOOrdemDeChegada() {
        service.registrarPedido(pedido(1, "Ana", 3));
        service.registrarPedido(pedido(2, "Bob", 1));
        service.registrarPedido(pedido(3, "Carlos", 2));

        assertEquals(1, service.chamarProximoPedidoFIFO().getId());
        assertEquals(2, service.chamarProximoPedidoFIFO().getId());
        assertEquals(3, service.chamarProximoPedidoFIFO().getId());
    }

    @Test
    @DisplayName("chamarProximoPedidoFIFO deve decrementar total de pendentes")
    void chamarFIFODecrementaTotal() {
        service.registrarPedido(pedido(1, "Ana", 1));
        service.registrarPedido(pedido(2, "Ana", 2));

        service.chamarProximoPedidoFIFO();

        assertEquals(1, service.totalPedidosPendentes());
        assertEquals(1, service.totalPedidosPendentesCliente("Ana"));
    }

    @Test
    @DisplayName("chamarProximoPedidoFIFO em serviço vazio deve retornar null")
    void chamarFIFOVazioRetornaNull() {
        assertNull(service.chamarProximoPedidoFIFO());
    }

    @Test
    @DisplayName("chamarProximoPedidoFIFO deve tornar o pedido inacessível por busca")
    void chamarFIFORemoveDaBusca() {
        service.registrarPedido(pedido(1, "Ana", 1));
        service.chamarProximoPedidoFIFO();
        assertNull(service.buscarPedidoPorId(1));
    }

    @Test
    @DisplayName("chamarProximoPedidoFIFO deve zerar pendentes do cliente após seu último pedido")
    void chamarFIFOZeraPendentesCliente() {
        service.registrarPedido(pedido(1, "Ana", 1));
        service.chamarProximoPedidoFIFO();
        assertEquals(0, service.totalPedidosPendentesCliente("Ana"));
    }

    // ── chamarPedidoMaisPrioritario ───────────────────────────────────────────

    @Test
    @DisplayName("chamarPedidoMaisPrioritario deve retornar o pedido de menor prioridade numérica primeiro")
    void chamarPrioritarioMenorNumero() {
        service.registrarPedido(pedido(1, "Ana", 5));
        service.registrarPedido(pedido(2, "Bob", 1));
        service.registrarPedido(pedido(3, "Carlos", 3));

        Pedido primeiro = service.chamarPedidoMaisPrioritario();
        assertNotNull(primeiro);
        assertEquals(2, primeiro.getId()); // prioridade 1 é a maior
    }

    @Test
    @DisplayName("chamarPedidoMaisPrioritario deve entregar pedidos em ordem crescente de prioridade")
    void chamarPrioritarioOrdemCrescente() {
        service.registrarPedido(pedido(1, "Ana", 3));
        service.registrarPedido(pedido(2, "Bob", 1));
        service.registrarPedido(pedido(3, "Carlos", 2));

        assertEquals(1, service.chamarPedidoMaisPrioritario().getPrioridade());
        assertEquals(2, service.chamarPedidoMaisPrioritario().getPrioridade());
        assertEquals(3, service.chamarPedidoMaisPrioritario().getPrioridade());
    }

    @Test
    @DisplayName("chamarPedidoMaisPrioritario em serviço vazio deve retornar null")
    void chamarPrioritarioVazioRetornaNull() {
        assertNull(service.chamarPedidoMaisPrioritario());
    }

    @Test
    @DisplayName("chamarPedidoMaisPrioritario deve decrementar total de pendentes")
    void chamarPrioritarioDecrementaTotal() {
        service.registrarPedido(pedido(1, "Ana", 2));
        service.registrarPedido(pedido(2, "Ana", 1));

        service.chamarPedidoMaisPrioritario();

        assertEquals(1, service.totalPedidosPendentes());
        assertEquals(1, service.totalPedidosPendentesCliente("Ana"));
    }

    @Test
    @DisplayName("chamarPedidoMaisPrioritario deve tornar o pedido inacessível por busca")
    void chamarPrioritarioRemoveDaBusca() {
        service.registrarPedido(pedido(1, "Ana", 1));
        service.chamarPedidoMaisPrioritario();
        assertNull(service.buscarPedidoPorId(1));
    }

    // ── mistura FIFO e Prioritário (pedidos ativos) ───────────────────────────

    @Test
    @DisplayName("Pedido chamado via FIFO não deve aparecer na fila prioritária")
    void pedidoChamadoFIFONaoAparecePrioritario() {
        service.registrarPedido(pedido(1, "Ana", 1));
        service.registrarPedido(pedido(2, "Bob", 2));

        // Consome o pedido 1 por FIFO
        Pedido viaFifo = service.chamarProximoPedidoFIFO();
        assertEquals(1, viaFifo.getId());

        // A fila prioritária deve pular o pedido 1 (já inativo) e retornar o 2
        Pedido viaPrio = service.chamarPedidoMaisPrioritario();
        assertNotNull(viaPrio);
        assertEquals(2, viaPrio.getId());
    }

    @Test
    @DisplayName("Pedido chamado via prioritário não deve aparecer na fila FIFO")
    void pedidoChamadoPrioritarioNaoApareceFIFO() {
        service.registrarPedido(pedido(1, "Ana", 2));
        service.registrarPedido(pedido(2, "Bob", 1));

        // Consome o pedido de maior prioridade (id=2, prio=1)
        Pedido viaPrio = service.chamarPedidoMaisPrioritario();
        assertEquals(2, viaPrio.getId());

        // A fila FIFO deve pular o id=2 (inativo) e retornar o id=1
        Pedido viaFifo = service.chamarProximoPedidoFIFO();
        assertNotNull(viaFifo);
        assertEquals(1, viaFifo.getId());
    }

    // ── listarPedidosPendentesOrdenadosPorId ──────────────────────────────────

    @Test
    @DisplayName("listarPedidosPendentesOrdenadosPorId deve retornar lista em ordem crescente de ID")
    void listarOrdenadosPorId() {
        service.registrarPedido(pedido(30, "Ana", 1));
        service.registrarPedido(pedido(10, "Bob", 2));
        service.registrarPedido(pedido(20, "Carlos", 3));

        List<Pedido> lista = service.listarPedidosPendentesOrdenadosPorId();

        assertEquals(3, lista.size());
        assertEquals(10, lista.get(0).getId());
        assertEquals(20, lista.get(1).getId());
        assertEquals(30, lista.get(2).getId());
    }

    @Test
    @DisplayName("listarPedidosPendentesOrdenadosPorId em serviço vazio deve retornar lista vazia")
    void listarVazioRetornaListaVazia() {
        assertTrue(service.listarPedidosPendentesOrdenadosPorId().isEmpty());
    }

    // ── totalPedidosAtePrioridade ─────────────────────────────────────────────

    @Test
    @DisplayName("totalPedidosAtePrioridade deve contar apenas pedidos com prioridade <= limite")
    void totalPedidosAtePrioridadeContaCorreto() {
        service.registrarPedido(pedido(1, "Ana", 1));
        service.registrarPedido(pedido(2, "Bob", 2));
        service.registrarPedido(pedido(3, "Carlos", 3));
        service.registrarPedido(pedido(4, "Davi", 5));

        assertEquals(2, service.totalPedidosAtePrioridade(2));
        assertEquals(3, service.totalPedidosAtePrioridade(3));
        assertEquals(4, service.totalPedidosAtePrioridade(5));
    }

    @Test
    @DisplayName("totalPedidosAtePrioridade com prioridade 0 deve lançar IllegalArgumentException")
    void totalPedidosAtePrioridadeZeroLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> service.totalPedidosAtePrioridade(0));
    }

    @Test
    @DisplayName("totalPedidosAtePrioridade deve retornar 0 quando nenhum pedido satisfaz o limite")
    void totalPedidosAtePrioridadeSemResultados() {
        service.registrarPedido(pedido(1, "Ana", 5));
        service.registrarPedido(pedido(2, "Bob", 10));

        assertEquals(0, service.totalPedidosAtePrioridade(3));
    }

    // ── totalPedidosPendentesCliente ──────────────────────────────────────────

    @Test
    @DisplayName("totalPedidosPendentesCliente para cliente sem pedidos deve retornar 0")
    void totalPendentesClienteInexistenteRetornaZero() {
        assertEquals(0, service.totalPedidosPendentesCliente("Ninguem"));
    }

    // ── fluxo completo ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Fluxo completo: registrar, atender por FIFO e por prioridade até zerar a fila")
    void fluxoCompletoZeraFila() {
        service.registrarPedido(pedido(1, "Ana", 3));
        service.registrarPedido(pedido(2, "Bob", 1));
        service.registrarPedido(pedido(3, "Ana", 2));
        service.registrarPedido(pedido(4, "Carlos", 1));

        // Atende 2 via FIFO e 2 via prioritário, em qualquer ordem
        service.chamarProximoPedidoFIFO();
        service.chamarPedidoMaisPrioritario();
        service.chamarProximoPedidoFIFO();
        service.chamarPedidoMaisPrioritario();

        assertEquals(0, service.totalPedidosPendentes());
        assertNull(service.chamarProximoPedidoFIFO());
        assertNull(service.chamarPedidoMaisPrioritario());
    }
}