package com.test;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

@RestController
@RequestMapping("/ibp")
public class IBPController {

    private final IBPService service;
    private final OpenSearchService openSearch;

    public IBPController(IBPService service, OpenSearchService openSearch) {
        this.service = service;
        this.openSearch = openSearch;
    }

    public int waitUntilAvailable() {
        System.out.println("Ожидание запуска OpenSearch");
        for (int i = 1; i <= 10; i++) {
            try (Socket socket = new Socket("opensearch", 9200)) {
                System.out.println("Соединение прошло успешно, OpenSearch работает");
                return 1;
            } catch (IOException err) {
                System.out.println("Ошибка пинга к http://openserach:9200 - " + err);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        System.out.println("Не удалось дождаться запуска OPenSearch");
        return -1;
    }

    @GetMapping("/avg")
    public double avg() {
        int res = waitUntilAvailable();
        if (res == 1) {
            List < IBP > IBPlist = openSearch.getDataFromOpenSearch();
            System.out.println("Размер IBP списка: " + IBPlist.size());
            System.out.println("Примеры IBP:");
            IBPlist.stream()
            .limit(10)
            .forEach(i -> System.out.println("TimeRemaining: " + i.ups_adv_battery_run_time_remaining));

            if (IBPlist == null || IBPlist.isEmpty()) {
                System.out.print("getDataFromOpenSearch вернул null");
                return 0.0;
            }
            int[] mass = IBPlist.stream()
                .filter(i -> i.ups_adv_battery_run_time_remaining != null)
                .mapToInt(i -> i.ups_adv_battery_run_time_remaining)
                .toArray();
            return service.Avg(mass);
        } else {
            System.out.println("Не удалось достучаться до OpenSearch");
            return 0.0;
        }
    }

    @GetMapping("/max")
    public int max() {
        int res = waitUntilAvailable();
        if (res == 1) {
            List < IBP > IBPlist = openSearch.getDataFromOpenSearch();
            System.out.println("Размер IBP списка: " + IBPlist.size());
            if (IBPlist == null || IBPlist.isEmpty()) {
                return 0;
            }
            int[] mass = IBPlist.stream()
                .filter(i -> i.ups_adv_output_voltage != null)
                .mapToInt(i -> i.ups_adv_output_voltage)
                .toArray();
            return service.Max(mass);
        } else {
            System.out.println("Не удалось достучаться до OpenSearch");
            return 0;
        }
    }

    @GetMapping("/uniquevalues")
    public String[] uniquevalues() {
        int res = waitUntilAvailable();
        if (res == 1) {
            List < IBP > IBPlist = openSearch.getDataFromOpenSearch();
            System.out.println("Размер IBP списка: " + IBPlist.size());
            if (IBPlist == null || IBPlist.isEmpty()) {
                String[] mass = new String[0];
                return mass;
            }
            return service.Values(IBPlist);
        } else {
            String[] output = new String[0];
            System.out.println("Не удалось достучаться до OpenSearch");
            return output;
        }
    }
}