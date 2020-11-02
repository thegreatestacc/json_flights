package com.testtask.flights.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class Root {
    public List<Flight> flights = new ArrayList<>();
}
