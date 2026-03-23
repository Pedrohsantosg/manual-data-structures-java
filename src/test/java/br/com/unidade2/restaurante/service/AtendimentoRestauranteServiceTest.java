package br.com.unidade2.restaurante.service;

import br.com.unidade2.restaurante.domain.Pedido;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

class AtendimentoRestauranteServiceTest {

    @Test
    void deveAtenderPedidoMaisPrioritario() {
        AtendimentoRestauranteService service = new AtendimentoRestauranteService();

        service.registrarPedido(new Pedido(1, "Ana", "Prato 1", 3));
        service.registrarPedido(new Pedido(2, "Bia", "Prato 2", 1));
        service.registrarPedido(new Pedido(3, "Ana", "Prato 3", 2));

        Pedido atendido = service.chamarPedidoMaisPrioritario();

        assertNotNull(atendido);
        assertEquals(2, atendido.getId());
        assertEquals(2, service.totalPedidosPendentes());
        assertEquals(2, service.totalPedidosPendentesCliente("Ana"));
    }

    @Test
    void deveAtenderPorFIFO() {
        AtendimentoRestauranteService service = new AtendimentoRestauranteService();

        service.registrarPedido(new Pedido(10, "Carlos", "Prato A", 3));
        service.registrarPedido(new Pedido(11, "Duda", "Prato B", 1));

        Pedido atendido = service.chamarProximoPedidoFIFO();

        assertNotNull(atendido);
        assertEquals(10, atendido.getId());
        assertEquals(1, service.totalPedidosPendentes());
    }
}
