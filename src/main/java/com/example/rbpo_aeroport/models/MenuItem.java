package com.example.rbpo_aeroport.models;

import lombok.Getter;
import lombok.Builder;
import lombok.ToString;
import java.util.Map;

@Getter
@Builder
@ToString
public class MenuItem {
    private final Long id;
    private final String name;
    private final Map<String, Double> positions;
}
