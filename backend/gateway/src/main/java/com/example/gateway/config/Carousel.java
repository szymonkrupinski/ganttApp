package com.example.gateway.config;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Carousel {


    private final EurekaClient eurekaClient;
    private List<InstanceInfo> instances = new ArrayList<>();
    private int currentIndex = 0;

    public Carousel(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
        try {
            initAuthCarousel();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        events();
    }


    public String getUriAuth() {
        if (instances.isEmpty()) {
            return null; // Handle case when instances list is empty
        }
        StringBuilder stringBuilder = new StringBuilder();
        InstanceInfo instance = instances.get(currentIndex);
        stringBuilder.append(instance.getIPAddr()).append(":").append(instance.getPort());
        currentIndex = (currentIndex + 1) % instances.size();
        return stringBuilder.toString();
    }


    private void events() {
        eurekaClient.registerEventListener(eurekaEvent -> {
            initAuthCarousel();
        });
        eurekaClient.unregisterEventListener(eurekaEvent -> {
            try {
                initAuthCarousel();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        });
    }

    private void initAuthCarousel() throws NullPointerException {
        Application application = eurekaClient.getApplication("AUTH-SERVICE");
        if (application != null) {
            instances = application.getInstances();
        } else {
            instances = new ArrayList<>(); // Handle case when application is null
        }
    }
}
