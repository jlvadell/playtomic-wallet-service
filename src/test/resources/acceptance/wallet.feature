Feature: Wallet API controller operations

  Background:
    * url 'http://localhost:8090/v1/wallets'

  Scenario: Get Wallet by ID
    Given path 'wallet1'
    And headers { 'Accept': 'application/json', 'Authorization': 'Bearer someToken'}
    When method GET
    Then status 200
    And match response == { id: 'wallet1', balance: { value: '#? _ >= 0', decimal: 2, currency: 'EUR' } }

  Scenario: Top-up Wallet
    Given path 'wallet1', 'transactions'
    And headers { 'Accept': 'application/json', 'Authorization': 'Bearer someToken'}
    And request { card: 'tkn_1234', amount: { value: 5000, decimal: 2, currency: 'EUR' } }
    When method POST
    Then status 200
    And match response.status == 'CONFIRMED'

  Scenario: Spend from Wallet
    Given path 'wallet1', 'transactions'
    And headers { 'Accept': 'application/json', 'Authorization': 'Bearer someToken'}
    And request { amount: {value: -5000, decimal: 2, currency: 'EUR' }}
    When method POST
    Then status 200
    And match response.status == 'CONFIRMED'