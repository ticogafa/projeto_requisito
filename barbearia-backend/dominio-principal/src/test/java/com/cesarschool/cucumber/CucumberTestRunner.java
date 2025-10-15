package com.cesarschool.cucumber;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/resources/agendamento.feature",
    snippets = CucumberOptions.SnippetType.CAMELCASE,
    monochrome = true
)
public class CucumberTestRunner {
}