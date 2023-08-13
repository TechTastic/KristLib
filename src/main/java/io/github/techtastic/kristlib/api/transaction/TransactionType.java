package io.github.techtastic.kristlib.api.transaction;

/**
 * This class is for giving the type of a transaction in a less vague format than String
 *
 * @author TechTastic
 */
public enum TransactionType {
    MINED,
    TRANSFER,
    NAME_PURCHASE,
    NAME_A_RECORD,
    NAME_TRANSFER
}
