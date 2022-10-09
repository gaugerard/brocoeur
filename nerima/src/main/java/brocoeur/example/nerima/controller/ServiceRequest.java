package brocoeur.example.nerima.controller;

import brocoeur.example.nerima.service.GameStrategyTypes;

public record ServiceRequest(String userId, GameStrategyTypes gameStrategyTypes) {
}
