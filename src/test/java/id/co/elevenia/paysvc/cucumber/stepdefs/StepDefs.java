package id.co.elevenia.paysvc.cucumber.stepdefs;

import id.co.elevenia.paysvc.PaysvcApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = PaysvcApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
