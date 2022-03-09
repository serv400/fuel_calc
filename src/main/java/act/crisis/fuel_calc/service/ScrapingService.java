package act.crisis.fuel_calc.service;

import act.crisis.fuel_calc.dto.FuelDTO;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ScrapingService {

    private String baseUrl;
    private HashMap<String, Float> fuels;
    private List<FuelDTO> fuelDTOList;

    public ScrapingService() {
        this.fuels =  new HashMap<>();
        this.fuelDTOList = new ArrayList<>();
    }

    public void setBaseUrl(int typeOfFuel) {
        this.baseUrl = "http://www.fuelprices.gr/price_stats_ng.view?prodclass="+typeOfFuel;
    }

    protected void initHashMap(int typeOfFuel){
        StringBuilder data= new StringBuilder();
        try (final WebClient client = new WebClient()) {
            client.getOptions().setCssEnabled(false);
            client.getOptions().setJavaScriptEnabled(false);
            setBaseUrl(typeOfFuel);
            HtmlPage page = client.getPage(baseUrl);
            List<HtmlElement> itemList = new ArrayList<>();
            for (int i = 6; i <= 58; i++) {
                itemList.addAll(page.getByXPath("//*[@class='mainArea']/table[2]/tbody/tr[" + i + "]/td[4]"));
            }
            if (itemList.isEmpty())
                throw new RuntimeException();
            else
                for (HtmlElement item : itemList) {
                    data.append(item.asNormalizedText()+"\n");
                }
            List<String> tempList = new ArrayList<>(Arrays.asList(data.toString().split("\\n+")));
                float sum  = 0;
            for (int i=0; i<tempList.size(); i++){
                sum += Float.parseFloat(tempList.get(i).replace(",","."));
            }
            String typeFuel = "";
            switch (typeOfFuel){
                case 1 : typeFuel = "Unleaded";
                break;
                case 4: typeFuel = "Diesel";
                break;
                case 6: typeFuel = "LPG";
            }
            fuels.put(typeFuel,sum/tempList.size());
            setFuelsToDTOModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Float> getFuels() {
        return fuels;
    }

    private void setFuelsToDTOModel(){
        for (Map.Entry<String, Float> fuel : fuels.entrySet()) {
            String typeOfFuel = fuel.getKey();
            Float pricePerLitre = fuel.getValue();
            fuelDTOList.add(new FuelDTO(typeOfFuel,pricePerLitre));
        }
    }

    public void showFuelsDTO(){
        for (FuelDTO f : fuelDTOList){
            System.out.println(f.getName()+" "+f.getPrice());
        }
    }
}
