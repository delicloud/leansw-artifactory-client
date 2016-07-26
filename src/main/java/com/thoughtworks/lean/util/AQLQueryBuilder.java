package com.thoughtworks.lean.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by qmxie on 4/27/16.
 */
public class AQLQueryBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(AQLQueryBuilder.class);

    private Map<String, Object> findInnerMap;
    private Integer limit;
    private Integer offset;
    private String[] includeFields;

    public static class Operation {
        private String operationName;
        private String field;
        private String value;

        public Operation(String operation, String field, String value) {
            this.operationName = operation;
            this.field = field;
            this.value = value;
        }

        public String getField() {
            return field;
        }

        public String getOperation() {
            return operationName;
        }

        public String getValue() {
            return value;
        }

        public Map.Entry<String, String> getExpression() {
            return Maps.immutableEntry(operationName, value);
        }

        public Map.Entry<String, Map.Entry<String, String>> toImmutableEntry() {
            return Maps.immutableEntry(field, Maps.immutableEntry(operationName, value));
        }
    }

    public AQLQueryBuilder() {
        findInnerMap = Maps.newTreeMap();
    }

    public static Operation eq(String field, String value) {
        return new Operation("$eq", field, value);
    }

    public static Operation ne(String field, String value) {
        return new Operation("$ne", field, value);
    }

    public static Operation gt(String field, String value) {
        return new Operation("$gt", field, value);
    }

    public static Operation gte(String field, String value) {
        return new Operation("$gte", field, value);
    }

    public static Operation lt(String field, String value) {
        return new Operation("$lt", field, value);
    }

    public static Operation lte(String field, String value) {
        return new Operation("$lte", field, value);
    }

    public static Operation match(String field, String value) {
        return new Operation("$match", field, value);
    }

    public static Operation nmatch(String field, String value) {
        return new Operation("$nmatch", field, value);
    }

    public static Operation before(String field, String value) {
        return new Operation("$before", field, value);
    }

    public static Operation last(String field, String value) {
        return new Operation("$last", field, value);
    }

    public AQLQueryBuilder add(Operation operation) {
        this.findInnerMap.put(operation.getField(), operation.getExpression());
        return this;
    }

    private AQLQueryBuilder conditions(String conditionOperation, List<Operation> operationList) {
        checkNotNull(operationList);
        this.findInnerMap.put(conditionOperation, operationList.stream().map(Operation::toImmutableEntry).collect(Collectors.toList()));
        return this;
    }

    public AQLQueryBuilder or(List<Operation> orConditions) {
        return this.conditions("$or", orConditions);
    }

    public AQLQueryBuilder or(Operation... orConditions) {
        return this.or(Arrays.asList(orConditions));
    }

    public AQLQueryBuilder and(List<Operation> andConditions) {
        return this.conditions("$and", andConditions);
    }

    public AQLQueryBuilder and(Operation... andConditions) {
        return this.and(Arrays.asList(andConditions));
    }

    public AQLQueryBuilder matchOnSingleProperty(List<Operation> mspConditions) {
        return this.conditions("$msp", mspConditions);
    }

    public AQLQueryBuilder matchOnSingleProperty(Operation... mspConditions) {
        return this.matchOnSingleProperty(Arrays.asList(mspConditions));
    }

    public AQLQueryBuilder include(String... fields) {
        checkNotNull(fields);
        this.includeFields = fields;
        return this;
    }

    public AQLQueryBuilder limit(Integer number) {
        this.limit = number;
        return this;
    }

    public AQLQueryBuilder offset(Integer number) {
        this.offset = number;
        return this;
    }

    public String build() {
        try {
            String findInnerJson = new ObjectMapper().writeValueAsString(this.findInnerMap);
            String includePart = null != includeFields ? ".include(\"" + Joiner.on("\",\"").join(includeFields) + "\")" : "";
            String limitPart = null != limit ? ".limit(" + limit + ")" : "";
            String offsetPart = null != offset ? ".offset(" + offset + ")" : "";
            return "items.find(" + findInnerJson + ")" + includePart + offsetPart + limitPart;
        } catch (JsonProcessingException e) {
            LOGGER.debug("encode AQL Json Error", e);
            return "{}";
        }
    }

    @Override
    public String toString() {
        return this.build();
    }
}
