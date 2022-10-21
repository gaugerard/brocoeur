package brocoeur.example.nerima.controller;

import brocoeur.example.service.GameStrategyTypes;

public record ServiceRequest(String userId, GameStrategyTypes gameStrategyTypes) {
}
