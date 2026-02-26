# Manual Data Structures Java

Projeto desenvolvido para a **Atividade da Unidade 2 da disciplina de Estruturas de Dados**, com foco na **implementação manual de estruturas de dados em Java** e na construção de um **mini-sistema funcional** que utilize essas estruturas de forma prática e integrada.

## Visão geral

Este trabalho tem como objetivo projetar, implementar e analisar um sistema computacional cujo funcionamento dependa explicitamente de estruturas de dados implementadas manualmente. A proposta reforça a compreensão prática dos principais conceitos estudados na disciplina, exigindo não apenas a construção das estruturas, mas também sua aplicação em um sistema funcional, acompanhada de testes automatizados e análise de complexidade assintótica.

## Objetivos da atividade

- Implementar manualmente estruturas de dados em **Java**
- Utilizar essas estruturas em um **mini-sistema funcional**
- Aplicar conceitos de **programação orientada a objetos**
- Desenvolver **testes com JUnit**
- Realizar **análise de complexidade** das operações principais
- Demonstrar domínio prático sobre estruturas clássicas de dados

## Estruturas de dados implementadas

O projeto contempla a implementação das seguintes estruturas:

### Fila
A fila deve ser implementada **exclusivamente com duas pilhas**, sem o uso de estruturas auxiliares além dessas duas instâncias.

### Pilha
Cada pilha é implementada utilizando **lista simplesmente encadeada**, com operações desenvolvidas manualmente.

### Lista simplesmente encadeada
Estrutura base utilizada para sustentar a implementação das pilhas.

### Árvore Binária de Busca Balanceada
Implementação de uma BST balanceada, podendo ser:
- **AVL**, ou
- **PV (Preto e Vermelho / Red-Black Tree)**

### Heap Binária
Implementação manual de uma fila de prioridade utilizando:
- **Min Heap**, ou
- **Max Heap**

### Tabela Hash
Implementação de **Hash Table com tratamento de colisão por chaining**.

## Mini-sistema funcional

Além das estruturas de dados, o projeto inclui um **mini-sistema funcional** que obrigatoriamente utiliza:

- **Fila**
- **BST balanceada**
- **Heap**
- **Tabela Hash**

O domínio do sistema foi escolhido pelo grupo conforme as exigências da atividade, com o objetivo de aplicar as estruturas de forma prática, organizada e coerente com um problema computacional real.

## Tecnologias utilizadas

- **Java**
- **JUnit**
- **Programação Orientada a Objetos**
- **Implementação manual de estruturas de dados**

## Funcionalidades esperadas

O sistema contempla operações de inserção, remoção, consulta, busca, organização, priorização e processamento de dados, explorando o papel de cada estrutura no funcionamento geral da aplicação. Além disso, o projeto inclui testes automatizados para validar o comportamento das implementações desenvolvidas.

## Requisitos da atividade atendidos

- Implementação manual de **Fila**, **Pilha** e **Lista Encadeada**
- Fila construída com **duas pilhas**
- Pilhas implementadas com **lista simplesmente encadeada**
- Implementação de **BST balanceada**
- Implementação de **Heap Binária**
- Implementação de **Tabela Hash com chaining**
- Desenvolvimento de **mini-sistema funcional**
- Criação de **testes com JUnit**
- **Análise de complexidade assintótica**

## Como executar

Para executar o projeto, basta clonar o repositório, abrir em uma IDE Java de sua preferência, compilar os arquivos e executar a aplicação principal. Também é possível rodar os testes JUnit para validar o funcionamento das estruturas implementadas e das funcionalidades do sistema.

## Como testar

Os testes automatizados foram desenvolvidos com **JUnit**, cobrindo as operações essenciais das estruturas e do sistema. Entre os cenários de teste, destacam-se o empilhamento e desempilhamento de elementos, o enfileiramento e desenfileiramento com duas pilhas, a inserção, busca e remoção em árvore balanceada, as operações de heap e o tratamento de colisões na tabela hash.

## Análise de complexidade

O projeto também contempla a análise de complexidade das operações principais de cada estrutura, observando seus custos assintóticos e o impacto dessas operações no desempenho do sistema. Essa etapa é importante para justificar as escolhas de implementação e compreender a eficiência de cada estrutura em diferentes cenários de uso.

## Entrega acadêmica

De acordo com as orientações da atividade, a entrega deve conter os arquivos `.java`, o vídeo explicativo ou um documento com o link do vídeo no YouTube, todos organizados em uma pasta no Google Drive com os arquivos soltos, sem compactação. A pasta deve estar compartilhada apenas com os integrantes do grupo, o professor e os monitores indicados na atividade.

## Integrantes

- **Pedro Henrique**
- **Júlio Pedro**
- **Lívia Ferreira**
- **beamatss**

## Considerações finais

Este projeto foi desenvolvido com o propósito de consolidar, de forma prática, os conteúdos da disciplina de **Estruturas de Dados**, permitindo aplicar teoria, implementação, testes e análise de desempenho em um único sistema. A proposta fortalece competências fundamentais em desenvolvimento de software, raciocínio algorítmico e modelagem de soluções computacionais em Java.
