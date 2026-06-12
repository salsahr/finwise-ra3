# Finwise — RA3 (Aplicação Desktop JavaFX)

Aplicação desktop desenvolvida para o projeto de avaliação **RA3**, baseada na
plataforma **Finwise** (projeto de Experiência Criativa — Engenharia de Software, PUCPR).

O sistema gerencia, via interface gráfica, as entidades do domínio Finwise, com
operações completas de **CRUD** (Cadastro, Consulta, Atualização e Exclusão) para
cada classe.

## Tecnologias

- **Java** (JDK 26)
- **JavaFX** 26.0.1 (interface gráfica)
- **Maven** (build e dependências)
- **IntelliJ IDEA**

## Regras importantes do projeto

- Interface gráfica construída **100% por código** — sem SceneBuilder e sem FXML.
- Persistência de dados em **arquivo CSV** (sem banco de dados).
- **Tratamento de exceções** com `try-catch` nas operações de risco (leitura/escrita
  de arquivo, conversão de números).

## Organização do código

O projeto é dividido em três pacotes, separando domínio, persistência e interface:

| Pacote         | Responsabilidade                                                        |
|----------------|-------------------------------------------------------------------------|
| `modelo`       | Classes de domínio (apenas dados: atributos, construtor, getters/setters). |
| `persistencia` | Classes DAO que leem e gravam cada entidade em seu arquivo CSV.          |
| `ui`           | Telas JavaFX de CRUD (formulário + tabela + botões na mesma janela).     |

## Padrão de CRUD

- Os dados ficam em uma lista em memória; o arquivo CSV é o espelho dela no disco.
- A cada alteração (inserir/editar/excluir), a lista é modificada e o arquivo é
  reescrito por completo.
- As telas nunca acessam o arquivo diretamente — solicitam isso ao DAO
  correspondente (separação entre domínio e interface).

 

## Como executar

1. Abrir o projeto no IntelliJ IDEA.
2. Garantir que o **JDK 26** está configurado.
3. Executar pelo Maven: `javafx:run` (a primeira execução baixa o JavaFX
   automaticamente).

## Equipe

- Artur Kuzma Marques
- Giovane Renato Trevisan
- Guilherme Hoshino Rouver
