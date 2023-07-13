package com.nerdysermons.services;

import io.micronaut.context.annotation.Bean;

@Bean
public class EchoService {
    public String echo(String arg) {
        return arg;
    }
}
