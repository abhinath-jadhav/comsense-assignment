package com.comsense.assignment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString
public class SuccessResponse extends Response{
    private String message;
    private String status;
}
