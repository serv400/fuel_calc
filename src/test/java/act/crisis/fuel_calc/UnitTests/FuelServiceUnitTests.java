package act.crisis.fuel_calc.UnitTests;

import act.crisis.fuel_calc.service.FuelService;
import act.crisis.fuel_calc.service.FuelServiceImpl;
import act.crisis.fuel_calc.service.ScrapingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class FuelServiceUnitTests {
    @Mock
    ScrapingService scrapingService = new ScrapingService();
    @Mock
    FuelService fuelService = new FuelServiceImpl(scrapingService);

    @Test
    public void fillHashmapFromWebPage() {
        assertThat(this.scrapingService.getFuels().entrySet().size()).isEqualTo(3);
    }

    @Test
    public void displayAllPricesValidTest() {
        assertThat(this.fuelService.displayAllFuelsAndPricesScrpd())
                .contains(LocalDateTime.now(ZoneId.of("GMT+2")).format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")),
                        String.format("%.2f",this.scrapingService.getFuels().values().stream().findAny().get()),
                        this.scrapingService.getFuels().keySet().stream().findAny().get());
    }

    @Test
    public void displayAllPricesForExactFuelType() {
        assertThat(this.fuelService.displayTypeOfFuelAndPrice("LPG"))
                .contains(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")),
                        "LPG : "
                                + String.format("%.2f",this.scrapingService.getFuels().entrySet()
                                .stream()
                                .filter(fuel -> fuel.getKey().equalsIgnoreCase("LPG"))
                                .collect(Collectors.toList())
                                .get(0)
                                .getValue())
                );
    }

    @Test
    public void displayAllPricesForInvalidFuelType() {
        Assertions.assertThrows(RuntimeException.class,()->{
            this.fuelService.displayTypeOfFuelAndPrice("Unleaded100");
        });
    }

    @Test
    public void calculateTotalPricesForValidFuelTypeAndValidTank() {
        assertThat(this.fuelService.calculateConsumption("LPG","50,0"))
                .contains(LocalDateTime.now(ZoneId.of("GMT+2")).format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")),
                        String.format("Total cost : %.2f",
                                (this.scrapingService.getFuels().entrySet()
                                .stream()
                                .filter(fuel -> fuel.getKey().equalsIgnoreCase("LPG"))
                                .collect(Collectors.toList())
                                .get(0)
                                .getValue() * Float.parseFloat("50")))
                );
    }

    @Test
    public void calculateTotalPricesForInValidFuelTypeAndValidTank() {
        Assertions.assertThrows(RuntimeException.class,()->{
           this.fuelService.calculateConsumption("Log","50,0");
        });
    }

    @Test
    public void calculateTotalPricesForValidFuelTypeAndZeroTank() {
        Assertions.assertThrows(RuntimeException.class,()->{
           this.fuelService.calculateConsumption("Lpg","0");
        });
    }
}
