package com.comsense.assignment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Builder
@ToString
@Data
public class CsvErrorResponse extends Response{

    private String status;
    private String message;
    private Map<Integer, List<String>> errors;
}
