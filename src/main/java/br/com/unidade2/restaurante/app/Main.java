package br.com.unidade2.restaurante.app;

import br.com.unidade2.restaurante.domain.Pedido;
import br.com.unidade2.restaurante.service.AtendimentoRestauranteService;

public class Main {

    public static void main(String[] args) {
        try {
            RestauranteFxApp.launch(RestauranteFxApp.class, args);
            return;
        } catch (Throwable ex) {
            System.out.println("JavaFX indisponivel no classpath atual. Executando modo console.");
        }

        AtendimentoRestauranteService service = new AtendimentoRestauranteService();

        service.registrarPedido(new Pedido(1, "Ana", "Hamburguer", 3));
        service.registrarPedido(new Pedido(2, "Bia", "Salada", 1));
        service.registrarPedido(new Pedido(3, "Carlos", "Suco", 2));

        Pedido prioritario = service.chamarPedidoMaisPrioritario();
        System.out.println("Atendimento por prioridade: " + prioritario);

        Pedido fifo = service.chamarProximoPedidoFIFO();
        System.out.println("Atendimento por ordem de chegada: " + fifo);

        System.out.println("Pedidos pendentes da Ana: " + service.totalPedidosPendentesCliente("Ana"));
        System.out.println("Total de pedidos pendentes: " + service.totalPedidosPendentes());
    }
}
