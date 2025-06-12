package com.test;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class IBPService {
    
public double Avg(int[] mass) {
        double total = 0;
        for (int i = 0; i < mass.length; i++) {
            total += mass[i];
        }
        double avg = total / mass.length;
        return avg;
    }

    public int Max(int[] mass) {
        for (int i = 0; i < mass.length; i++) {
            for (int j = 0; j < mass.length - 1; j++) {
                if (mass[j] > mass[j + 1]) {
                    int temp = mass[j];
                    mass[j] = mass[j + 1];
                    mass[j + 1] = temp;
                }
            }
        }
        int max = mass[mass.length - 1];
        return max;
    }

    public String[] Values(List < IBP > massIBP) {
        Map < String, Integer > map = new HashMap();
        int count = 0;
        for (int i = 0; i < massIBP.size(); i++) {
            IBP el = massIBP.get(i);
            if (el.host != null && !map.containsKey(el.host)) {
                map.put(el.host, count++);
            } else {
                continue;
            }
        }
        String[] mass = new String[map.size()];
        int i = 0;
        for (String key: map.keySet()) {
            mass[i++] = key;
        }
        return mass;
    }

}
