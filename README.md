# Brassaria Internacional — Intranet 780 anos

Trabalho semestral de **Back-end Frameworks** (UNG, Prof. Roger Diniz).
Intranet institucional da Brassaria Internacional: back-end em **Java 17 + Spring Boot 3**, persistência com **JPA/Hibernate + MySQL** (também roda em PostgreSQL) e um site (HTML/CSS/JS) que obtém **todas** as informações exibidas pela API REST.

## Como rodar

### Com MySQL via Docker (recomendado)
Pré-requisitos: Java 17+, Maven e Docker.

```bash
docker compose up -d      # sobe o MySQL 8.4 na porta 3306
mvn spring-boot:run       # sobe a API e o site
```
Acesse **http://localhost:8080**. O site é servido pelo próprio Spring Boot.

O banco `brassaria_intranet` é criado pelo container e as tabelas são geradas pelo Hibernate a partir das entidades (`ddl-auto=update`). Os dados ficam salvos no volume `brassaria-dados`, então continuam lá depois de reiniciar.

Na primeira execução, a classe `CargaInicialDados` preenche o banco com os dados da empresa, o portfólio (11 bebidas em 4 categorias) e 4 eventos.

### Com um MySQL já instalado
As credenciais não ficam no código: a aplicação lê as variáveis de ambiente `DB_URL`, `DB_USERNAME` e `DB_PASSWORD` (veja `.env.example`). Sem elas, usa `brassaria` / `brassaria123` em `localhost:3306`.

```bash
# Linux/macOS
DB_USERNAME=root DB_PASSWORD=sua_senha mvn spring-boot:run

# Windows (PowerShell)
$env:DB_USERNAME="root"; $env:DB_PASSWORD="sua_senha"; mvn spring-boot:run
```

### Usando outro banco de dados
A aplicação funciona com **MySQL**, **PostgreSQL** ou **H2**. Para trocar, basta mudar as variáveis `DB_URL`, `DB_USERNAME` e `DB_PASSWORD`. O driver é escolhido pela URL e o Hibernate cria as tabelas sozinho.

| Banco | Exemplo de `DB_URL` |
|---|---|
| MySQL | `jdbc:mysql://HOST:3306/brassaria_intranet?createDatabaseIfNotExist=true` |
| PostgreSQL | `jdbc:postgresql://HOST:5432/NOME_DO_BANCO` |

### Sem MySQL (perfil `dev`, banco H2 em memória)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
Console do H2: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:brassaria`, usuário `sa`).

### Testes
```bash
mvn test
```
`ApiIntegracaoTest` sobe a aplicação com H2 e testa os endpoints (dados institucionais, filtros do portfólio, regra dos 3 últimos eventos, validações, 404).

### Front-end em outro servidor (opcional)
Os arquivos do site estão em `src/main/resources/static`. Se você abrir com o Live Server do VS Code (porta 5500), o `js/api.js` aponta automaticamente para `http://localhost:8080/api`, e o `CorsConfig` libera o acesso.

## Publicar online (Render)
O projeto tem um `Dockerfile`, então pode ser hospedado de graça no [Render](https://render.com) com um PostgreSQL.

1. Crie uma conta no Render entrando com o GitHub.
2. **New → Postgres**: dê um nome, escolha o plano **Free** e crie. Na aba *Connections*, anote *Hostname*, *Port*, *Database*, *Username* e *Password*.
3. **New → Web Service**: escolha o repositório `brassaria-intranet`, *Language* **Docker**, a **mesma região** do banco e o plano **Free**.
4. Em *Environment Variables*, adicione:
   - `DB_URL` = `jdbc:postgresql://HOSTNAME:5432/DATABASE`
   - `DB_USERNAME` = o *Username* do banco
   - `DB_PASSWORD` = o *Password* do banco
5. Clique em **Deploy**. Quando terminar, o site fica em `https://NOME-DO-SERVICO.onrender.com`.

A cada `git push` na branch `main`, o Render publica a nova versão sozinho.
No plano gratuito, o serviço "dorme" depois de 15 minutos sem acesso, e o primeiro acesso seguinte leva cerca de 1 minuto. O PostgreSQL gratuito expira depois de 30 dias.

## Arquitetura em camadas

```
br.com.brassaria.intranet
├── controller   → camada de apresentação da API: recebe HTTP, valida (@Valid) e devolve JSON
├── service      → camada de negócio: regras e transações (@Transactional)
├── repository   → camada de acesso a dados: interfaces Spring Data JPA
├── model        → entidades JPA (mapeadas pelo Hibernate)
├── dto          → objetos de entrada (Request) e saída (Response) da API — as entidades nunca são expostas direto
├── exception    → exceções de negócio + @RestControllerAdvice com respostas de erro padronizadas
└── config       → CORS e carga inicial de dados
```

Outros arquivos na raiz: `Dockerfile` (imagem para hospedagem), `docker-compose.yml` (MySQL local), `.env.example` (variáveis do banco).

Fluxo: **Site (fetch) → Controller → Service → Repository → Hibernate → MySQL**.

## Modelo de dados (entidades e relacionamentos)

| Entidade | Relacionamentos |
|---|---|
| `Categoria` | **1:N** com `Bebida` (`@OneToMany(mappedBy = "categoria")`) |
| `Bebida` | **N:1** com `Categoria` (`@ManyToOne`, coluna `categoria_id`) |
| `Evento` | **N:N** com `Bebida` — bebidas servidas no evento (`@ManyToMany` + tabela de junção `evento_bebidas`) |
| `Empresa` | **1:N** com `Valor` (`cascade = ALL`, `orphanRemoval = true`) — missão, visão e história |
| `Valor` | **N:1** com `Empresa` |

## Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/empresa` | História, missão, visão e valores |
| PUT | `/api/empresa` | Atualiza os dados institucionais |
| GET | `/api/categorias` | Lista as categorias (com total de bebidas) |
| GET | `/api/categorias/{id}` | Consulta uma categoria |
| POST | `/api/categorias` | Cadastra categoria |
| PUT | `/api/categorias/{id}` | Atualiza categoria |
| DELETE | `/api/categorias/{id}` | Exclui (bloqueado se houver bebidas) |
| GET | `/api/bebidas?categoriaId=&alcoolica=&nome=` | Portfólio com filtros opcionais |
| GET | `/api/bebidas/{id}` | Consulta uma bebida |
| POST | `/api/bebidas` | Cadastra bebida |
| PUT | `/api/bebidas/{id}` | Atualiza bebida |
| DELETE | `/api/bebidas/{id}` | Exclui bebida |
| GET | `/api/eventos` | Todos os eventos (por data do evento) |
| GET | `/api/eventos/recentes` | **Os 3 últimos eventos cadastrados** (tela inicial) |
| GET | `/api/eventos/{id}` | Notícia completa do evento |
| POST | `/api/eventos` | Cadastra evento |
| PUT | `/api/eventos/{id}` | Atualiza evento |
| DELETE | `/api/eventos/{id}` | Exclui evento |

Exemplo de cadastro de evento:
```json
POST /api/eventos
{
  "titulo": "Happy Hour Zero",
  "resumo": "Confraternização com a linha sem álcool.",
  "conteudo": "Texto completo da notícia...",
  "local": "Sede — Guarulhos/SP",
  "dataEvento": "2026-12-20",
  "bebidaIds": [7, 8]
}
```

Erros seguem um formato único (`ErroResponse`): 400 (validação, com os campos), 404 (não encontrado), 409 (integridade) e 422 (regra de negócio).

## Regras de negócio
- A página inicial mostra os **três últimos eventos cadastrados** (`findTop3ByOrderByDataCadastroDescIdDesc`); a data de cadastro é preenchida pelo `@PrePersist`.
- Bebida alcoólica precisa ter teor acima de 0,5%; bebida sem álcool, no máximo 0,5%.
- Não é possível cadastrar duas categorias com o mesmo nome nem excluir uma categoria com bebidas.
- Ao excluir uma bebida, ela é removida dos eventos antes (evita erro de chave estrangeira na tabela N:N).

## Páginas do site
- `index.html` — destaque dos 780 anos, 3 últimos eventos, um destaque de cada linha do portfólio.
- `portfolio.html` — portfólio com filtro por categoria, com/sem álcool e busca por nome (tudo via query params da API).
- `institucional.html` — história, missão, visão e valores, reforçando consumo consciente e saúde.
- `eventos.html` — lista, notícia completa, cadastro (com erros de validação do back-end exibidos por campo) e exclusão.
