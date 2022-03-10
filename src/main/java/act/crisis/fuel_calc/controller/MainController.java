package act.crisis.fuel_calc.controller;

import act.crisis.fuel_calc.service.FuelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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
    public ResponseEntity<String> showFuelAndPriceByType(@RequestParam String type, @RequestParam @Nullable String tank) {
        try {
            if (tank!=null && !tank.isEmpty())
                return ResponseEntity.ok().body(fuelService.calculateConsumption(type, tank));
            else
                return ResponseEntity.ok().body(fuelService.displayTypeOfFuelAndPrice(type));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Maybe you have done a mistake on your parameters given!");
        }
    }

    @GetMapping("/calculator")
    public ResponseEntity<String> calculateGivenFuelPriceBasedOnMoneyToSpend(@RequestParam String price, @RequestParam String money) {
        try {
                return ResponseEntity.ok().body(fuelService.calculateLitre(price,money));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Maybe you have done a mistake on your parameters given!");
        }
    }
}
