package com.testtask.flights.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testtask.flights.model.Flight;
import com.testtask.flights.model.Root;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/flights")
public class FlightsController {
    private static ObjectMapper mapper = new ObjectMapper();
    private static String pathToFile = "/json/flights.json";
    private static InputStream is;
    private Root root = downloadData();
    private List<Flight> flights = root.getFlights();

    //максимальное и минимальное значения прайса, используются для получения срденего значения прайса
    private int expPrice = getTheMostExpensiveFlightsFromMoscowToKhabarovsk().get().getPrice();
    private int minPrice = getTheCheapestFlightsFromMoscowToKhabarovsk().get().getPrice();

    /**
     * Считываю json
     * @return
     */
    public Root downloadData() {
        URL resource = getClass().getResource(pathToFile);
        try {
            is = new FileInputStream(new File(resource.toURI()));
            root = mapper.readValue(is, new TypeReference<>() {});
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return root;
    }

    /**
     * Отдельный метод для соритровки по заданным полям
     * @return
     */
    public List<Flight> getSortedListFromMoscowToKhabarovsk() {
        return flights.stream()
                .filter(i -> i.getFromCity().equalsIgnoreCase("Москва"))
                .filter(i -> i.getToCity().equalsIgnoreCase("Хабаровск"))
                .collect(Collectors.toList());
    }

    /**
     * Максимальная стоимость полета
     * @return
     */
    @GetMapping("/expensive")
    public Optional<Flight> getTheMostExpensiveFlightsFromMoscowToKhabarovsk() {
        return getSortedListFromMoscowToKhabarovsk().stream()
                .max(Comparator.comparingInt(Flight::getPrice));
    }

    /**
     * Минимальная стоимость полета
     * @return
     */
    @GetMapping("/cheapest")
    public Optional<Flight> getTheCheapestFlightsFromMoscowToKhabarovsk() {
        return getSortedListFromMoscowToKhabarovsk().stream()
                .min(Comparator.comparingInt(Flight::getPrice));
    }

    /**
     * Средняя стоимость полета
     * @return
     */
    @GetMapping("/avg")
    public List<Flight> getAvgFlightsFromMoscowToKhabarovsk() {
        return getSortedListFromMoscowToKhabarovsk().stream()
                .filter(i -> i.getPrice() < expPrice && i.getPrice() > minPrice)
                .collect(Collectors.toList());
    }
}
