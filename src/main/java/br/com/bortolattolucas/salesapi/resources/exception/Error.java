package br.com.bortolattolucas.salesapi.resources.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Error {
    private String message;
    private Map<String, String> fields;
    private LocalDateTime timestamp;
    private String path;
}
