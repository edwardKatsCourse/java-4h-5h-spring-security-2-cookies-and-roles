package com.telran.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<List<String>, String> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    @SneakyThrows
    public String convertToDatabaseColumn(List<String> strings) {
        return OBJECT_MAPPER.writeValueAsString(strings);
    }

    @Override
    @SneakyThrows
    public List<String> convertToEntityAttribute(String s) {
        return OBJECT_MAPPER.readValue(s, new TypeReference<List<String>>() {});
    }
}
