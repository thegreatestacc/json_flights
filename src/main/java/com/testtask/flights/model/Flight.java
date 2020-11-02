package com.testtask.flights.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class Flight {
    private String fromCity;
    private String toCity;
    private int price;
}
