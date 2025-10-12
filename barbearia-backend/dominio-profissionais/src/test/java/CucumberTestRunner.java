package com.cesarschool.barbearia.dominio.financeiro;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/main/java/com/cesarschool/barbearia/dominio/profissionais/resources/gestaoDeProfissionais.feature",
    glue = "com.cesarschool.barbearia.dominio.profissionais"
)
public class CucumberTestRunner {

}