package com.cesarschool.barbearia.dominio.profissionais;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

// Este é o seu CucumberTestRunner.java
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/resources/gestaoDeProfissionais.feature",
    glue = "com.cesarschool.barbearia.dominio.profissionais",
    plugin = { 
        "pretty", 
        "summary", 
        "usage:target/snippets.txt" 
    },
    monochrome = true
)
public class CucumberTestRunner {
}