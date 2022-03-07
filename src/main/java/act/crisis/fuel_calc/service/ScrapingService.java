package act.crisis.fuel_calc.service;

import act.crisis.fuel_calc.dto.FuelDTO;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ScrapingService {

    private static final String baseUrl = "https://gr.fuelo.net/?lang=en";
    private HashMap<String, Float> fuels;
    private List<FuelDTO> fuelDTOList;

    public ScrapingService() {
        this.fuels =  new HashMap<>();
        this.fuelDTOList = new ArrayList<>();
    }

    protected void initHashMap(){
        StringBuilder data= new StringBuilder();
        try (final WebClient client = new WebClient()) {
            client.getOptions().setCssEnabled(false);
            client.getOptions().setJavaScriptEnabled(false);
            HtmlPage page = client.getPage(baseUrl);
            List<HtmlElement> itemList = page.getByXPath("//*[@class='widget']/div[@class='box'][1]/h4");
            if (itemList.isEmpty())
                throw new RuntimeException();
            else
                for (HtmlElement item : itemList) {
                    data.append(item.asNormalizedText());
                }
            List<String> tempList = new ArrayList<>(Arrays.asList(data.toString().split("\\n+")));
            for (int i=1; i<tempList.size(); i++){
                String[] line = tempList.get(i).split(":");
                String fuel = line[0];
                String price = Arrays.asList(line[1].split(" ")).get(1).replace(",",".");
                fuels.put(fuel,Float.parseFloat(price));
            }
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
