package br.com.unidade2.restaurante.app;

import br.com.unidade2.restaurante.domain.Pedido;
import br.com.unidade2.restaurante.service.AtendimentoRestauranteService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class RestauranteFxApp extends Application {

    private final AtendimentoRestauranteService service = new AtendimentoRestauranteService();
    private final ObservableList<PedidoRow> pendentesRows = FXCollections.observableArrayList();

    private final Label totalPendentesLabel = criarKpiLabel("0");
    private final Label prioridadeAltaLabel = criarKpiLabel("0");
    private final Label ultimoAtendimentoLabel = criarKpiLabel("Nenhum");
    private final Label statusLabel = new Label("Painel pronto para operacao.");
    private final Label modoOperacaoLabel = new Label("Fluxo normal");
    private final Label pedidosVisiveisLabel = new Label("0 exibidos");
    private final Button acaoSugeridaButton = new Button("Atender sugerido");
    private final TextArea logArea = new TextArea();

    private FilteredList<PedidoRow> pedidosFiltrados;
    private TextField filtroClienteField;
    private ComboBox<String> filtroPrioridadeCombo;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Label titulo = new Label("Painel de Operacao - Restaurante");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 26));
        titulo.setTextFill(Color.web("#13293D"));

        Label subtitulo = new Label("Registro, priorizacao e atendimento em tempo real");
        subtitulo.setTextFill(Color.web("#3E5C76"));

        TextField idField = new TextField();
        TextField clienteField = new TextField();
        TextField descricaoField = new TextField();
        TextField prioridadeField = new TextField();

        idField.setPromptText("Ex: 1");
        clienteField.setPromptText("Ex: Ana");
        descricaoField.setPromptText("Ex: Hamburguer");
        prioridadeField.setPromptText("Ex: 2 (menor = mais prioritario)");

        Button registrarButton = new Button("Registrar pedido");
        Button chamarPrioridadeButton = new Button("Chamar por prioridade");
        Button chamarFifoButton = new Button("Chamar por chegada (FIFO)");
        Button buscarPorIdButton = new Button("Buscar por ID");
        Button totalClienteButton = new Button("Pendentes do cliente");
        Button limparLogButton = new Button("Limpar log");
        Button limparFiltrosButton = new Button("Limpar filtros");

        registrarButton.setTooltip(new Tooltip("Registra um novo pedido na fila"));
        chamarPrioridadeButton.setTooltip(new Tooltip("Chama o pedido mais prioritario (menor numero)"));
        chamarFifoButton.setTooltip(new Tooltip("Chama o pedido por ordem de chegada"));
        buscarPorIdButton.setTooltip(new Tooltip("Procura um pedido ativo pelo ID"));
        totalClienteButton.setTooltip(new Tooltip("Mostra quantos pedidos pendentes um cliente possui"));
        limparFiltrosButton.setTooltip(new Tooltip("Remove todos os filtros da grade"));
        limparLogButton.setTooltip(new Tooltip("Limpa o historico de eventos do painel"));
        acaoSugeridaButton.setTooltip(new Tooltip("Executa automaticamente a melhor acao entre prioridade e FIFO"));

        filtroClienteField = new TextField();
        filtroClienteField.setPromptText("Filtrar por cliente");

        filtroPrioridadeCombo = new ComboBox<>();
        filtroPrioridadeCombo.getItems().addAll(
            "Todas as prioridades",
            "Alta (1-2)",
            "Media (3-4)",
            "Baixa (5+)"
        );
        filtroPrioridadeCombo.getSelectionModel().selectFirst();

        acaoSugeridaButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #EF476F, #FF7F50);" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10 16;"
        );

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.addRow(0, new Label("ID:"), idField);
        form.addRow(1, new Label("Cliente:"), clienteField);
        form.addRow(2, new Label("Prato/Descricao:"), descricaoField);
        form.addRow(3, new Label("Prioridade:"), prioridadeField);

        FlowPane actions1 = new FlowPane(10, 10, registrarButton, buscarPorIdButton, totalClienteButton);
        FlowPane actions2 = new FlowPane(10, 10, chamarPrioridadeButton, chamarFifoButton, acaoSugeridaButton, limparLogButton);
        actions1.setPrefWrapLength(330);
        actions2.setPrefWrapLength(330);

        TableView<PedidoRow> pedidosTable = criarTabelaPedidos();
        pedidosFiltrados = new FilteredList<>(pendentesRows, pedido -> true);
        pedidosTable.setItems(pedidosFiltrados);

        HBox faixaFiltros = new HBox(
            10,
            new Label("Filtro cliente:"),
            filtroClienteField,
            new Label("Prioridade:"),
            filtroPrioridadeCombo,
            limparFiltrosButton,
            pedidosVisiveisLabel
        );
        HBox.setHgrow(filtroClienteField, Priority.ALWAYS);
        faixaFiltros.setStyle("-fx-background-color: #F7FAFC; -fx-padding: 10; -fx-background-radius: 8;");

        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setPromptText("Historico operacional do restaurante");
        VBox.setVgrow(logArea, Priority.ALWAYS);

        registrarButton.setOnAction(event -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                int prioridade = Integer.parseInt(prioridadeField.getText().trim());
                String cliente = clienteField.getText().trim();
                String descricao = descricaoField.getText().trim();

                Pedido pedido = new Pedido(id, cliente, descricao, prioridade);
                service.registrarPedido(pedido);
                adicionarLog("Novo pedido na cozinha: " + pedido);
                statusLabel.setText("Pedido ID " + id + " registrado e enviado para fila.");
                limparFormulario(idField, clienteField, descricaoField, prioridadeField);
                atualizarPainel();
            } catch (IllegalArgumentException ex) {
                statusLabel.setText("Erro ao registrar: " + ex.getMessage());
                adicionarLog("Erro ao registrar pedido: " + ex.getMessage());
            }
        });

        chamarPrioridadeButton.setOnAction(event -> atenderPorPrioridade());

        chamarFifoButton.setOnAction(event -> atenderPorFifo());

        acaoSugeridaButton.setOnAction(event -> executarAcaoSugerida());

        buscarPorIdButton.setOnAction(event -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                Pedido pedido = service.buscarPedidoPorId(id);
                if (pedido == null) {
                    statusLabel.setText("Pedido ID " + id + " nao encontrado.");
                    adicionarLog("Busca por ID " + id + ": nao encontrado.");
                } else {
                    statusLabel.setText("Pedido ID " + id + " localizado no painel.");
                    adicionarLog("Busca por ID " + id + ": " + pedido);
                }
            } catch (NumberFormatException ex) {
                statusLabel.setText("Informe um ID numerico valido para buscar.");
                adicionarLog("Erro na busca por ID: valor invalido.");
            }
        });

        totalClienteButton.setOnAction(event -> {
            String cliente = clienteField.getText().trim();
            if (cliente.isEmpty()) {
                statusLabel.setText("Informe o nome do cliente para consultar pendencias.");
                adicionarLog("Consulta de pendencias ignorada: cliente vazio.");
                return;
            }
            int totalCliente = service.totalPedidosPendentesCliente(cliente);
            statusLabel.setText("Cliente " + cliente + " possui " + totalCliente + " pedido(s) pendente(s).");
            adicionarLog("Pendentes de " + cliente + ": " + totalCliente);
        });

        filtroClienteField.textProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
        filtroPrioridadeCombo.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
        limparFiltrosButton.setOnAction(event -> {
            filtroClienteField.clear();
            filtroPrioridadeCombo.getSelectionModel().selectFirst();
            aplicarFiltros();
        });

        limparLogButton.setOnAction(event -> logArea.clear());

        modoOperacaoLabel.setStyle(
            "-fx-background-color: #E6FFFA;" +
            "-fx-text-fill: #0B7285;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 6;" +
            "-fx-padding: 6 10;"
        );

        pedidosVisiveisLabel.setStyle(
            "-fx-text-fill: #334E68;" +
            "-fx-font-weight: bold;"
        );

        HBox cabecalhoOperacao = new HBox(10, new Label("Fila operacional"), modoOperacaoLabel);

        VBox painelAjudaBotoes = criarPainelAjudaBotoes();

        VBox painelCadastro = new VBox(10,
            new Label("Cadastro e consultas"),
            form,
            actions1,
            actions2,
            painelAjudaBotoes
        );
        painelCadastro.setStyle("-fx-background-color: #F0F4F8; -fx-background-radius: 10; -fx-padding: 14;");
        VBox.setVgrow(painelAjudaBotoes, Priority.ALWAYS);

        FlowPane indicadores = new FlowPane(12, 12,
            criarCardIndicador("Pedidos pendentes", totalPendentesLabel),
            criarCardIndicador("Alta prioridade (<=2)", prioridadeAltaLabel),
            criarCardIndicador("Ultimo atendimento", ultimoAtendimentoLabel)
        );
        indicadores.setPrefWrapLength(600);

        VBox painelOperacao = new VBox(10,
            cabecalhoOperacao,
            faixaFiltros,
            pedidosTable,
            indicadores
        );
        painelOperacao.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #D9E2EC; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 14;");
        VBox.setVgrow(pedidosTable, Priority.ALWAYS);

        FlowPane corpo = new FlowPane(12, 12, painelCadastro, painelOperacao);
        corpo.setPrefWrapLength(920);
        painelOperacao.setPrefWidth(540);
        painelCadastro.setPrefWidth(360);

        VBox root = new VBox(12,
            titulo,
            subtitulo,
            new Separator(),
            corpo,
            statusLabel,
            logArea
        );
        root.setPadding(new Insets(16));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #FFF8E8, #F8FBFF);");

        Scene scene = new Scene(root, 920, 580);
        configurarLayoutResponsivo(
            scene,
            corpo,
            painelCadastro,
            painelOperacao,
            actions1,
            actions2,
            faixaFiltros,
            indicadores,
            titulo,
            subtitulo,
            registrarButton,
            buscarPorIdButton,
            totalClienteButton,
            chamarPrioridadeButton,
            chamarFifoButton,
            acaoSugeridaButton,
            limparLogButton,
            limparFiltrosButton
        );
        stage.setTitle("Restaurante - Unidade 2");
        stage.setScene(scene);
        stage.show();

        atualizarPainel();
    }

    private TableView<PedidoRow> criarTabelaPedidos() {
        TableView<PedidoRow> table = new TableView<>();

        TableColumn<PedidoRow, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(80);

        TableColumn<PedidoRow, String> clienteCol = new TableColumn<>("Cliente");
        clienteCol.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        clienteCol.setPrefWidth(170);

        TableColumn<PedidoRow, String> descricaoCol = new TableColumn<>("Pedido");
        descricaoCol.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        descricaoCol.setPrefWidth(220);

        TableColumn<PedidoRow, Integer> prioridadeCol = new TableColumn<>("Prioridade");
        prioridadeCol.setCellValueFactory(new PropertyValueFactory<>("prioridade"));
        prioridadeCol.setPrefWidth(110);

        TableColumn<PedidoRow, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("statusVisual"));
        statusCol.setPrefWidth(150);
        statusCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                    return;
                }

                Label badge = new Label(status);
                badge.setStyle(estiloStatus(status));

                Circle icone = new Circle(5, corStatus(status));
                HBox celula = new HBox(8, icone, badge);
                celula.setAlignment(Pos.CENTER_LEFT);

                setGraphic(celula);
                setText(null);
            }
        });

        table.getColumns().add(idCol);
        table.getColumns().add(clienteCol);
        table.getColumns().add(descricaoCol);
        table.getColumns().add(prioridadeCol);
        table.getColumns().add(statusCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(PedidoRow item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                    return;
                }

                if (item.getPrioridade() <= 2) {
                    setStyle("-fx-background-color: #FFE3E3;");
                } else if (item.getPrioridade() <= 4) {
                    setStyle("-fx-background-color: #FFF4D6;");
                } else {
                    setStyle("-fx-background-color: #E7F5FF;");
                }
            }
        });

        return table;
    }

    private VBox criarCardIndicador(String titulo, Label valorLabel) {
        Label title = new Label(titulo);
        title.setTextFill(Color.web("#486581"));

        VBox card = new VBox(6, title, valorLabel);
        card.setMinWidth(190);
        card.setStyle("-fx-background-color: #F7FAFC; -fx-background-radius: 10; -fx-border-color: #D9E2EC; -fx-border-radius: 10; -fx-padding: 12;");
        return card;
    }

    private VBox criarPainelAjudaBotoes() {
        Label titulo = new Label("Significado dos botoes");
        titulo.setTextFill(Color.web("#334E68"));
        titulo.setFont(Font.font("System", FontWeight.BOLD, 13));

        Label linha1 = new Label("Registrar pedido: adiciona novo pedido na fila.");
        Label linha2 = new Label("Buscar por ID: localiza um pedido ativo especifico.");
        Label linha3 = new Label("Pendentes do cliente: total de pedidos pendentes por cliente.");
        Label linha4 = new Label("Chamar por prioridade: atende primeiro os mais urgentes.");
        Label linha5 = new Label("Chamar por chegada (FIFO): atende por ordem de entrada.");
        Label linha6 = new Label("Atender sugerido: escolhe automaticamente prioridade ou FIFO.");
        Label linha7 = new Label("Limpar log: apaga o historico operacional da tela.");

        linha1.setWrapText(true);
        linha2.setWrapText(true);
        linha3.setWrapText(true);
        linha4.setWrapText(true);
        linha5.setWrapText(true);
        linha6.setWrapText(true);
        linha7.setWrapText(true);

        VBox painel = new VBox(4, titulo, linha1, linha2, linha3, linha4, linha5, linha6, linha7);
        painel.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #D9E2EC; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10;");
        return painel;
    }

    private void configurarLayoutResponsivo(
        Scene scene,
        FlowPane corpo,
        VBox painelCadastro,
        VBox painelOperacao,
        FlowPane actions1,
        FlowPane actions2,
        HBox faixaFiltros,
        FlowPane indicadores,
        Label titulo,
        Label subtitulo,
        Button registrarButton,
        Button buscarPorIdButton,
        Button totalClienteButton,
        Button chamarPrioridadeButton,
        Button chamarFifoButton,
        Button acaoSugeridaButton,
        Button limparLogButton,
        Button limparFiltrosButton
    ) {
        Runnable atualizarLayout = () -> {
            double largura = scene.getWidth();
            boolean compacto = largura < 1000;
            corpo.setPrefWrapLength(Math.max(360, largura - 48));

            titulo.setFont(Font.font("System", FontWeight.BOLD, compacto ? 21 : 26));
            subtitulo.setFont(Font.font("System", compacto ? 12 : 14));

            totalPendentesLabel.setFont(Font.font("System", FontWeight.BOLD, compacto ? 15 : 18));
            prioridadeAltaLabel.setFont(Font.font("System", FontWeight.BOLD, compacto ? 15 : 18));
            ultimoAtendimentoLabel.setFont(Font.font("System", FontWeight.BOLD, compacto ? 15 : 18));

            indicadores.setHgap(compacto ? 8 : 12);
            indicadores.setVgap(compacto ? 8 : 12);
            indicadores.setPrefWrapLength(compacto ? Math.max(320, largura - 90) : 600);

            for (var node : indicadores.getChildren()) {
                if (node instanceof VBox card) {
                    card.setMinWidth(compacto ? 150 : 190);
                    card.setStyle(
                        compacto
                            ? "-fx-background-color: #F7FAFC; -fx-background-radius: 10; -fx-border-color: #D9E2EC; -fx-border-radius: 10; -fx-padding: 8;"
                            : "-fx-background-color: #F7FAFC; -fx-background-radius: 10; -fx-border-color: #D9E2EC; -fx-border-radius: 10; -fx-padding: 12;"
                    );
                }
            }

            ajustarEstiloBotao(registrarButton, compacto, false);
            ajustarEstiloBotao(buscarPorIdButton, compacto, false);
            ajustarEstiloBotao(totalClienteButton, compacto, false);
            ajustarEstiloBotao(chamarPrioridadeButton, compacto, false);
            ajustarEstiloBotao(chamarFifoButton, compacto, false);
            ajustarEstiloBotao(limparLogButton, compacto, false);
            ajustarEstiloBotao(limparFiltrosButton, compacto, false);
            ajustarEstiloBotao(acaoSugeridaButton, compacto, true);

            if (compacto) {
                painelCadastro.setPrefWidth(Math.max(320, largura - 72));
                painelCadastro.setMaxWidth(Double.MAX_VALUE);
                painelOperacao.setPrefWidth(Math.max(320, largura - 72));
                painelOperacao.setMaxWidth(Double.MAX_VALUE);
                actions1.setHgap(8);
                actions2.setHgap(8);
                actions1.setVgap(8);
                actions2.setVgap(8);
                actions1.setPrefWrapLength(Math.max(300, largura - 100));
                actions2.setPrefWrapLength(Math.max(300, largura - 100));
                faixaFiltros.setSpacing(8);
                filtroClienteField.setPrefWidth(140);
                logArea.setPrefRowCount(5);
            } else {
                painelCadastro.setPrefWidth(360);
                painelCadastro.setMaxWidth(360);
                painelOperacao.setPrefWidth(540);
                painelOperacao.setMaxWidth(Double.MAX_VALUE);
                actions1.setHgap(10);
                actions2.setHgap(10);
                actions1.setVgap(10);
                actions2.setVgap(10);
                actions1.setPrefWrapLength(330);
                actions2.setPrefWrapLength(330);
                faixaFiltros.setSpacing(10);
                filtroClienteField.setPrefWidth(220);
                logArea.setPrefRowCount(7);
            }
        };

        scene.widthProperty().addListener((obs, oldWidth, newWidth) -> atualizarLayout.run());
        atualizarLayout.run();
    }

    private void ajustarEstiloBotao(Button botao, boolean compacto, boolean destaque) {
        if (destaque) {
            botao.setStyle(
                "-fx-background-color: linear-gradient(to right, #EF476F, #FF7F50);" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                (compacto ? "-fx-font-size: 11; -fx-padding: 7 10;" : "-fx-font-size: 12; -fx-padding: 10 16;")
            );
            return;
        }

        botao.setStyle(
            "-fx-font-weight: 600;" +
            "-fx-background-radius: 8;" +
            (compacto ? "-fx-font-size: 11; -fx-padding: 6 9;" : "-fx-font-size: 12; -fx-padding: 8 12;")
        );
    }

    private Label criarKpiLabel(String valorInicial) {
        Label label = new Label(valorInicial);
        label.setFont(Font.font("System", FontWeight.BOLD, 18));
        label.setTextFill(Color.web("#102A43"));
        return label;
    }

    private void atualizarPainel() {
        pendentesRows.clear();
        for (Pedido pedido : service.listarPedidosPendentesOrdenadosPorId()) {
            pendentesRows.add(new PedidoRow(
                pedido.getId(),
                pedido.getCliente(),
                pedido.getDescricao(),
                pedido.getPrioridade()
            ));
        }

        totalPendentesLabel.setText(String.valueOf(service.totalPedidosPendentes()));
        int totalAltaPrioridade = service.totalPedidosAtePrioridade(2);
        prioridadeAltaLabel.setText(String.valueOf(totalAltaPrioridade));
        prioridadeAltaLabel.setTextFill(totalAltaPrioridade > 0 ? Color.web("#C92A2A") : Color.web("#102A43"));

        if (totalAltaPrioridade > 0) {
            modoOperacaoLabel.setText("RUSH: alta prioridade na fila");
            modoOperacaoLabel.setStyle(
                "-fx-background-color: #FFE3E3;" +
                "-fx-text-fill: #A61E4D;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 6 10;"
            );
        } else {
            modoOperacaoLabel.setText("Fluxo normal");
            modoOperacaoLabel.setStyle(
                "-fx-background-color: #E6FFFA;" +
                "-fx-text-fill: #0B7285;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 6 10;"
            );
        }

        aplicarFiltros();
        atualizarBotaoSugerido();
    }

    private void aplicarFiltros() {
        if (pedidosFiltrados == null) {
            return;
        }

        String clienteFiltro = filtroClienteField == null ? "" : filtroClienteField.getText().trim().toLowerCase();
        String prioridadeFiltro = filtroPrioridadeCombo == null || filtroPrioridadeCombo.getValue() == null
            ? "Todas as prioridades"
            : filtroPrioridadeCombo.getValue();

        pedidosFiltrados.setPredicate(pedido -> {
            boolean clienteOk = clienteFiltro.isEmpty() || pedido.getCliente().toLowerCase().contains(clienteFiltro);
            boolean prioridadeOk = correspondeFiltroPrioridade(pedido.getPrioridade(), prioridadeFiltro);
            return clienteOk && prioridadeOk;
        });

        pedidosVisiveisLabel.setText(pedidosFiltrados.size() + " exibidos");
    }

    private boolean correspondeFiltroPrioridade(int prioridade, String filtroSelecionado) {
        if ("Alta (1-2)".equals(filtroSelecionado)) {
            return prioridade <= 2;
        }
        if ("Media (3-4)".equals(filtroSelecionado)) {
            return prioridade >= 3 && prioridade <= 4;
        }
        if ("Baixa (5+)".equals(filtroSelecionado)) {
            return prioridade >= 5;
        }
        return true;
    }

    private void executarAcaoSugerida() {
        int totalPendentes = service.totalPedidosPendentes();
        if (totalPendentes == 0) {
            statusLabel.setText("Sem pedidos pendentes para atendimento.");
            adicionarLog("Acao sugerida ignorada: fila vazia.");
            return;
        }

        if (service.totalPedidosAtePrioridade(2) > 0) {
            atenderPorPrioridade();
        } else {
            atenderPorFifo();
        }
    }

    private void atualizarBotaoSugerido() {
        int totalPendentes = service.totalPedidosPendentes();
        int totalAltaPrioridade = service.totalPedidosAtePrioridade(2);

        if (totalPendentes == 0) {
            acaoSugeridaButton.setText("Sem acao sugerida");
            acaoSugeridaButton.setDisable(true);
            return;
        }

        acaoSugeridaButton.setDisable(false);
        if (totalAltaPrioridade > 0) {
            acaoSugeridaButton.setText("Sugerido: atender prioridade");
        } else {
            acaoSugeridaButton.setText("Sugerido: atender FIFO");
        }
    }

    private void atenderPorPrioridade() {
        Pedido pedido = service.chamarPedidoMaisPrioritario();
        if (pedido == null) {
            statusLabel.setText("Nao ha pedidos para chamar por prioridade.");
            adicionarLog("Fila de prioridade vazia.");
        } else {
            statusLabel.setText("Atendimento prioritario iniciado para ID " + pedido.getId() + ".");
            ultimoAtendimentoLabel.setText("ID " + pedido.getId() + " - " + pedido.getCliente());
            adicionarLog("Atendido por prioridade: " + pedido);
        }
        atualizarPainel();
    }

    private void atenderPorFifo() {
        Pedido pedido = service.chamarProximoPedidoFIFO();
        if (pedido == null) {
            statusLabel.setText("Nao ha pedidos para chamar em FIFO.");
            adicionarLog("Fila FIFO vazia.");
        } else {
            statusLabel.setText("Atendimento por ordem de chegada iniciado para ID " + pedido.getId() + ".");
            ultimoAtendimentoLabel.setText("ID " + pedido.getId() + " - " + pedido.getCliente());
            adicionarLog("Atendido por FIFO: " + pedido);
        }
        atualizarPainel();
    }

    private void limparFormulario(TextField idField, TextField clienteField, TextField descricaoField, TextField prioridadeField) {
        idField.clear();
        clienteField.clear();
        descricaoField.clear();
        prioridadeField.clear();
    }

    private void adicionarLog(String texto) {
        if (!logArea.getText().isEmpty()) {
            logArea.appendText(System.lineSeparator());
        }
        logArea.appendText(texto);
    }

    private String estiloStatus(String status) {
        if ("Critico".equals(status)) {
            return "-fx-background-color: #FEE2E2; -fx-text-fill: #9F1239; -fx-font-weight: bold; -fx-background-radius: 999; -fx-padding: 4 10;";
        }
        if ("Atencao".equals(status)) {
            return "-fx-background-color: #FEF3C7; -fx-text-fill: #92400E; -fx-font-weight: bold; -fx-background-radius: 999; -fx-padding: 4 10;";
        }
        return "-fx-background-color: #DBEAFE; -fx-text-fill: #1E3A8A; -fx-font-weight: bold; -fx-background-radius: 999; -fx-padding: 4 10;";
    }

    private Color corStatus(String status) {
        if ("Critico".equals(status)) {
            return Color.web("#D90429");
        }
        if ("Atencao".equals(status)) {
            return Color.web("#F08C00");
        }
        return Color.web("#1D4ED8");
    }

    public static class PedidoRow {
        private final int id;
        private final String cliente;
        private final String descricao;
        private final int prioridade;

        public PedidoRow(int id, String cliente, String descricao, int prioridade) {
            this.id = id;
            this.cliente = cliente;
            this.descricao = descricao;
            this.prioridade = prioridade;
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

        public String getStatusVisual() {
            if (prioridade <= 2) {
                return "Critico";
            }
            if (prioridade <= 4) {
                return "Atencao";
            }
            return "Normal";
        }
    }
}
