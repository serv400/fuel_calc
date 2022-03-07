package act.crisis.fuel_calc.controller;

import act.crisis.fuel_calc.service.FuelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/")
public class MainController {

    private FuelService fuelService;

    @Autowired
    public MainController(FuelService fuelService) {
        this.fuelService = fuelService;
    }

    @GetMapping("")
    public String showAllFuelsAndPrices() {
        return fuelService.displayAllFuelsAndPricesScrpd();
    }

    @GetMapping("fuel")
    public ResponseEntity<String> showFuelAndPriceByType(@RequestParam String type) {
        try {
            return ResponseEntity.ok().body(fuelService.displayTypeOfFuelAndPrice(type));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("This type of fuel is not very common in Greece");
        }
    }

    @GetMapping("consume")
    public ResponseEntity<String> calculateConsumptionBasedOnFuelAndFuelTank(@RequestParam String type,
                                                                             @RequestParam String tank) {
        if (!tank.isEmpty())
            return ResponseEntity.ok().body(fuelService.calculateConsumption(type, tank));
        else return ResponseEntity.badRequest().body("Insert a valid tank number");
    }
}
