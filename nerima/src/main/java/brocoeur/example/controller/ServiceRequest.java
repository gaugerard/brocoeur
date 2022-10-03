package brocoeur.example.repository;

import brocoeur.example.service.GameStrategyTypes;

public record ServiceRequest(String userId, GameStrategyTypes gameStrategyTypes) {
}
