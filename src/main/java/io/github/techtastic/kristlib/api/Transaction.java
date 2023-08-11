package io.github.techtastic.kristlib.api;

public record Transaction(
        int id,
        String from,
        String to,
        int value,
        String time,
        String name,
        String metadata,
        String sent_metaname,
        String sent_name,
        TransactionType type
) {}
