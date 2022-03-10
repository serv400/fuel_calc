package act.crisis.fuel_calc.IntegrationTests;

import act.crisis.fuel_calc.service.FuelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerIntegTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FuelService fuelService;

    @Test
    public void whenUserAsksForMainPageThenAllFuelPricesAreDisplayed() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(fuelService.displayAllFuelsAndPricesScrpd())));
    }

    @Test
    public void whenUserAsksForFuelTypeThenPriceIsDisplayed() throws Exception {
        this.mockMvc.perform(get("/fuel?type=Lpg"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(fuelService.displayTypeOfFuelAndPrice("Lpg"))));
    }

    @Test
    public void whenUserAsksForIncorrectFuelTypeThenErrorMsgIsDisplayed() throws Exception {
        this.mockMvc.perform(get("/fuel?type=Log"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Maybe you have done a mistake on your parameters given!"));
    }

    @Test
    public void whenUserAsksForFuelTypeAndGivesTankCapacityThenTotalIsDisplayed() throws Exception {
        this.mockMvc.perform(get("/fuel?type=Lpg&tank=30"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(fuelService.calculateConsumption("Lpg","30"))));
    }

    @Test
    public void whenUserAsksForCorrectFuelTypeAndGivesBelowZeroTankCapacityThenErrorMsgIsDisplayed() throws Exception {
        this.mockMvc.perform(get("/fuel?type=Lpg&tank=-10"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Maybe you have done a mistake on your parameters given!"));
    }

    @Test
    public void whenUserAsksForIncorrectFuelTypeAndGivesBelowZeroTankCapacityThenErrorMsgIsDisplayed() throws Exception {
        this.mockMvc.perform(get("/fuel?type=Log&tank=-10"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Maybe you have done a mistake on your parameters given!"));
    }

    @Test
    public void whenUserAsksForIncorrectFuelTypeAndGivesCorrectTankCapacityThenErrorMsgIsDisplayed() throws Exception {
        this.mockMvc.perform(get("/fuel?type=Log&tank=10"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Maybe you have done a mistake on your parameters given!"));
    }

    @Test
    public void whenUserAsksForCorrectPriceAndGivesBelowZeroMoneyThenErrorMsgIsDisplayed() throws Exception {
        this.mockMvc.perform(get("/calculator?price=2.149&money=-10,5"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Maybe you have done a mistake on your parameters given!"));
    }

    @Test
    public void whenUserAsksForZeroPriceAndGivesZeroMoneyThenErrorMsgIsDisplayed()  throws Exception {
        this.mockMvc.perform(get("/calculator?price=0.0&money=0,0"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Maybe you have done a mistake on your parameters given!"));
    }

    @Test
    public void whenUserAsksForInCorrectPriceAndGivesCorrectMoneyThenErrorMsgIsDisplayed() throws Exception {
        this.mockMvc.perform(get("/calculator?price=-2.149&money=20,0"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Maybe you have done a mistake on your parameters given!"));
    }

    @Test
    public void whenUserAsksForCorrectPriceAndGivesCorrectMoneyThenTotalLitresAreDisplayed() throws Exception {
        this.mockMvc.perform(get("/calculator?price=2.149&money=20,0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(fuelService.calculateLitre("2.149","20,0"))));
    }

}
