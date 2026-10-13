# 📚 Tech Advisor — repositório de estudo

> **Atenção: isto NÃO é um projeto.**
> Este é um **repositório de estudo**. Não é uma aplicação pronta para produção, não tem roadmap,
> não tem garantia de nada e não pretende resolver problema de ninguém.
> Aqui eu quebro, testo, erro e aprendo — tudo em relação a **Java, Spring Boot e Spring AI**.

---

## O que é isso?

Uma "migalha de código" onde eu vou decorando na prática os recursos do **Spring AI**.
Cada pacote é basicamente um laboratório isolado: eu leio a documentação, escrevo o teste
no controller e deixo o resultado salvo para consultar depois.

Ou seja: **código de estudo, não código de produção.**

---

## Stack

| Item | Versão / ferramenta |
| --- | --- |
| Linguagem | Java 21 |
| Framework | Spring Boot 4.1.1 |
| IA | Spring AI 2.0.1 |
| Modelo de chat | Google Gemini (`gemini-3.8-flash`) |
| Embeddings | Ollama (`nomic-embed-text`) |
| Banco | PostgreSQL (porta `5433`) + pgvector |
| Build | Maven (wrapper `./mvnw`) |

---

## Onde está o quê

```
src/main/java/com/example/carlos/primeiro_projeto/
├── TechAdvisorController.java          # prompts, variáveis, personas, histórico
├── PromptingTechniquesController.java  # técnicas de prompting (JSON, few-shot, chained...)
├── structured/                         # saída estruturada (JSON -> record)
├── streaming/                          # streaming via SSE
├── advisors/                           # advisors: logging, persona, disclaimer, timing
├── memory/                             # memória de conversa (em memória e persistente)
├── embeddings/                         # embeddings, similaridade e busca semântica
└── config/                             # configuração do vector store (pgvector)

src/main/resources/
├── application.properties              # configurações da aplicação
├── prompts/                            # templates de prompt (.st)
└── static/chat.html                    # telinha de teste para streaming
```

---

## Pré-requisitos

- Java 21
- Docker (para subir o PostgreSQL)
- [Ollama](https://ollama.com) rodando localmente, com o modelo baixado:
  ```bash
  ollama pull nomic-embed-text
  ```
- Uma chave da API do Google AI, exportada como variável de ambiente:
  ```bash
  export GOOGLE_API_KEY="sua-chave"
  ```

---

## Como rodar

1. Sobe o Postgres:

   ```bash
   docker run -d --name pg-study -p 5433:5432 \
     -e POSTGRES_USER=postgres \
     -e POSTGRES_PASSWORD=postgres \
     -e POSTGRES_DB=spring-ai \
     postgres:16
   ```

2. Garante que o Ollama está no ar (`ollama serve`, se não estiver rodando).

3. Sobe a aplicação:

   ```bash
   ./mvnw spring-boot:run
   ```

4. Brinca nos endpoints (abaixo).

---

## Endpoints principais

Como é repo de estudo, os nomes podem mudar de um dia para o outro.

### Prompting / advisor

| Rota | O que testa |
| --- | --- |
| `GET /advisor/advise` | chamada simples |
| `GET /advisor/prompt-variables` | variáveis no prompt |
| `GET /advisor/system-prompt` | system prompt |
| `GET /advisor/external-prompt-file` | prompt em arquivo externo (`.st`) |
| `GET /advisor/conversation-history` | histórico da conversa |
| `GET /prompting/json-output` | resposta em JSON puro |
| `GET /prompting/few-shot` | few-shot prompting |
| `GET /prompting/structured` | saída estruturada |
| `GET /prompting/dynamic-system-prompt` | system prompt dinâmico |
| `GET /prompting/chained-blog-post` | encadeamento de prompts |

### Demais laboratórios

| Rota | O que testa |
| --- | --- |
| `GET /structured/course-summary` | record a partir da resposta do modelo |
| `GET /api/streaming/sse-streaming` | streaming via Server-Sent Events |
| `GET /api/advisors/*` | logging, safeguards, persona, disclaimer, timing, chained |
| `GET /api/memory/*` | memória de conversa em memória |
| `GET /api/persistent/*` | memória persistida no Postgres |
| `POST /api/semantic/documents` | indexar documentos |
| `GET /api/semantic/embed` | gerar embeddings |
| `GET /api/semantic/similarity` | similaridade entre textos |
| `GET /api/semantic/search` | busca semântica |

Exemplo rápido:

```bash
curl "http://localhost:8080/advisor/advise"
```

---

## Regras desta casa

1. **Não é projeto.** Não reporte bug, não mande PR esperando review, não cobre manutenção.
2. **Pode ser quebrado.** Se mudou e parou de funcionar, provavelmente era "material de estudo" mesmo.
3. **Sem garantia.** O que funciona hoje pode não funcionar amanhã.

---

## Licença

Nenhuma. É caderno de anotação, não biblioteca.
