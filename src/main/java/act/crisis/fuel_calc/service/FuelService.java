package act.crisis.fuel_calc.service;

public interface FuelService {

    String displayAllFuelsAndPricesScrpd();

    String displayTypeOfFuelAndPrice(String type);

    String calculateConsumption(String type, String tank);

    String calculateLitre(String price,String moneyToSpend);
}
