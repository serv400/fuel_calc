package act.crisis.fuel_calc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class FuelServiceImpl implements FuelService {

    private ScrapingService scrapingService;

    @Autowired
    public FuelServiceImpl(ScrapingService scrapingService) {
        this.scrapingService = scrapingService;
        initScraping();
    }

    private void initScraping() {
        scrapingService.initHashMap();
        scrapingService.showFuelsDTO();
    }

    public String displayAllFuelsAndPricesScrpd() {
        StringBuilder response = new StringBuilder("Average Price" + "\nToday: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")) + "\n");
        for (Map.Entry<String, Float> fuel : scrapingService.getFuels().entrySet()) {
            String typeOfFuel = fuel.getKey();
            Float pricePerLitre = fuel.getValue();
            response.append(typeOfFuel).append(" : ").append(pricePerLitre).append("\n");
        }
        return response.toString();
    }

    public String displayTypeOfFuelAndPrice(String type) {
        StringBuilder response = new StringBuilder("Average Price" + "\nToday: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")) + "\n");
        boolean found = false;
        for (Map.Entry<String, Float> fuel : scrapingService.getFuels().entrySet()) {
            String typeOfFuel = fuel.getKey();
            Float pricePerLitre = fuel.getValue();
            if (type.equalsIgnoreCase(typeOfFuel)) {
                response.append(typeOfFuel).append(" : ").append(pricePerLitre).append("\n");
                found = true;
                break;
            }
        }
        if (!found) {
            throw new RuntimeException();
        }
        return response.toString();
    }

    @Override
    public String calculateConsumption(String type, String tank) {
        StringBuilder response = new StringBuilder("Today: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")) + "\nYou need to spend" + "\n");
        boolean found = false;
        for (Map.Entry<String, Float> fuel : scrapingService.getFuels().entrySet()) {
            String typeOfFuel = fuel.getKey();
            Float pricePerLitre = fuel.getValue();
            tank = tank.replaceAll(",", ".");
            if (type.equalsIgnoreCase(typeOfFuel) && Float.parseFloat(tank) > 0.0) {
                float total = pricePerLitre * Float.parseFloat(tank);
                response.append(typeOfFuel).append(" : ").append(pricePerLitre).append("\n").append("Total cost : ").append(String.format("%.2f", total));
                found = true;
                break;
            }
        }
        if (!found) {
            throw new RuntimeException();
        }
        return response.toString();
    }
}
