package act.crisis.fuel_calc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class FuelServiceImpl implements FuelService {

    private ScrapingService scrapingService;

    @Autowired
    public FuelServiceImpl(ScrapingService scrapingService) {
        this.scrapingService = scrapingService;
        try {
            initScraping();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void initScraping() {
        scrapingService.initHashMap(1);
        scrapingService.initHashMap(4);
        scrapingService.initHashMap(6);
        scrapingService.showFuelsDTO();
    }

    public String displayAllFuelsAndPricesScrpd() {
        if (!scrapingService.getFuels().isEmpty()) {
            StringBuilder response = new StringBuilder("Average Price" + "\nToday: " + LocalDateTime.now(ZoneId.of("GMT+2")).format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")) + "\n");
            for (Map.Entry<String, Float> fuel : scrapingService.getFuels().entrySet()) {
                String typeOfFuel = fuel.getKey();
                Float pricePerLitre = fuel.getValue();
                response.append(typeOfFuel).append(" : ").append(String.format("%.2f", pricePerLitre)).append("\n");
            }
            return response.toString();
        } else {
            return "Sorry but our provider is down at the moment";
        }
    }

    public String displayTypeOfFuelAndPrice(String type) {
        if (!scrapingService.getFuels().isEmpty()) {
            StringBuilder response = new StringBuilder("Average Price" + "\nToday: " + LocalDateTime.now(ZoneId.of("GMT+2")).format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")) + "\n");
            boolean found = false;
            for (Map.Entry<String, Float> fuel : scrapingService.getFuels().entrySet()) {
                String typeOfFuel = fuel.getKey();
                Float pricePerLitre = fuel.getValue();
                if (type.equalsIgnoreCase(typeOfFuel)) {
                    response.append(typeOfFuel).append(" : ").append(String.format("%.2f", pricePerLitre)).append("\n");
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new RuntimeException();
            }
            return response.toString();
        } else {
            return "Sorry but our provider is down at the moment";
        }
    }

    @Override
    public String calculateConsumption(String type, String tank) {
        if (!scrapingService.getFuels().isEmpty()) {
            StringBuilder response = new StringBuilder("Today: " + LocalDateTime.now(ZoneId.of("GMT+2")).format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")) + "\n");
            boolean found = false;
            for (Map.Entry<String, Float> fuel : scrapingService.getFuels().entrySet()) {
                String typeOfFuel = fuel.getKey();
                Float pricePerLitre = fuel.getValue();
                tank = tank.replaceAll(",", ".");
                if (type.equalsIgnoreCase(typeOfFuel) && Float.parseFloat(tank) > 0.0) {
                    float total = pricePerLitre * Float.parseFloat(tank);
                    response.append(typeOfFuel).append(" : ").append(String.format("%.2f", pricePerLitre)).append(" & Tank : ").append(tank).append("\n").append("Total cost : ").append(String.format("%.2f", total));
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new RuntimeException();
            }
            return response.toString();
        } else {
            return "Sorry but our provider is down at the moment";
        }
    }
}
