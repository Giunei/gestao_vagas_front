package br.com.giunei.gestao_vagas_front.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ErrorMessage {

    public static String formatErrorMessage(String message) {
        ObjectMapper objectMapper = new ObjectMapper();

        System.out.println(message);
        try {
            JsonNode rootNode = objectMapper.readTree(message);
            if (rootNode.isArray()) {
                return formatArrayErrorMessage(rootNode);
            }
            return rootNode.asText();
        } catch (Exception e) {
            return message;
        }
    }

    public static String formatArrayErrorMessage(JsonNode arrayNode) {
        StringBuilder formatMessager = new StringBuilder();
        for(JsonNode node : arrayNode) {
            formatMessager.append("- ").append(node.get("message").asText()).append("\n");
        }

        return formatMessager.toString();
    }
}
