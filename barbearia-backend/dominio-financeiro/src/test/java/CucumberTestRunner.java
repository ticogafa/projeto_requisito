package com.cesarschool.barbearia.dominio.financeiro;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/com/cesarschool/barbearia/dominio/financeiro",
    glue = "com.cesarschool.barbearia.dominio.financeiro"
)
public class CucumberTestRunner {
}