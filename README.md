# 📖 Fiadopay - Refatoração com Anotações e Processamento Assíncrono

Este projeto foi refatorado com foco em engenharia, adicionando **anotações customizadas** e **processamento assíncrono**.  
O objetivo foi melhorar clareza, extensibilidade e responsividade sem alterar o contrato da API existente.

---

## 🎯 Objetivos da Refatoração

- Tornar explícito no código quais classes tratam métodos de pagamento, quais métodos possuem regras antifraude e onde os webhooks são emitidos.
- Permitir que novos métodos de pagamento e regras sejam adicionados sem reescrever rotas.
- Evitar bloqueio de requisições durante operações demoradas (aprovação, envio de webhooks).
- Preservar o contrato da API: rotas, autenticação fake, idempotência, cálculo de juros parcelado e formato de webhook.

---

## 🛠️ Elementos Adicionados

### 1. Anotações Customizadas
- `@PaymentMethod(type="CARD")`  
  Marca classes que representam um tipo de pagamento específico.
- `@AntiFraud(name="HighAmount", threshold=5000.0)`  
  Declara regras antifraude diretamente no método de criação de pagamento.
- `@WebhookSink("payment.updated")`  
  Identifica métodos responsáveis por enviar webhooks e o tipo de evento.

### 2. Processamento Assíncrono
- Uso de `ExecutorService` para rodar tarefas em segundo plano:
  ```java
  private final ExecutorService executor = Executors.newFixedThreadPool(2);

  @WebhookSink("payment.updated")
  public void sendWebhookAsync(Payment p) {
      executor.submit(() -> sendWebhook(p));
  }

Isso garante que o envio de webhooks não bloqueie a resposta da API.

## Estrutura alterada/adição de algumas coisas
src/main/java/edu/ucsal/fiadopay/
 ├── annotations/
 │    ├── PaymentMethod.java
 │    ├── AntiFraud.java
 │    └── WebhookSink.java
 ├── controller/
 │    └── PaymentController.java
 └── service/
      └── PaymentService.java

## Prints

<img width="872" height="402" alt="Annotationsexample" src="https://github.com/user-attachments/assets/633f0183-c25f-48bb-b984-fe03a8445adc" />

## ➕MAIS

## FiadoPay Simulator (Spring Boot + H2)

Gateway de pagamento **FiadoPay** para a AVI/POOA.
Substitui PSPs reais com um backend em memória (H2).

## Rodar
```bash
./mvnw spring-boot:run
# ou
mvn spring-boot:run
```

H2 console: http://localhost:8080/h2  
Swagger UI: http://localhost:8080/swagger-ui.html

## Fluxo

1) **Cadastrar merchant**
```bash
curl -X POST http://localhost:8080/fiadopay/admin/merchants   -H "Content-Type: application/json"   -d '{"name":"MinhaLoja ADS","webhookUrl":"http://localhost:8081/webhooks/payments"}'
```

2) **Obter token**
```bash
curl -X POST http://localhost:8080/fiadopay/auth/token   -H "Content-Type: application/json"   -d '{"client_id":"<clientId>","client_secret":"<clientSecret>"}'
```

3) **Criar pagamento**
```bash
curl -X POST http://localhost:8080/fiadopay/gateway/payments   -H "Authorization: Bearer FAKE-<merchantId>"   -H "Idempotency-Key: 550e8400-e29b-41d4-a716-446655440000"   -H "Content-Type: application/json"   -d '{"method":"CARD","currency":"BRL","amount":250.50,"installments":12,"metadataOrderId":"ORD-123"}'
```

4) **Consultar pagamento**
```bash
curl http://localhost:8080/fiadopay/gateway/payments/<paymentId>
```
