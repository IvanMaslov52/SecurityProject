package com.example.security.Filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Filter {

    private String title;

    private String description;

    private String status;

    private String priority;

    private String author;

    private String executor;
}
