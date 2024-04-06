package com.syrtin.dataprocessor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResumePrintResponse implements Serializable {
    private Long statusCode;

    private String message;

    private String link;

}
