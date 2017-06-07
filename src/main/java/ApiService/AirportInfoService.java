package ApiService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;

/**
 * Klasa odpowiedzialna za inicjalizowanie połączenia z API
 * oraz pobieranie odpowiednich informacji
 * Created by krystian on 07.06.17.
 */
public class AirportInfoService {
    private final String APP_ID = "233ee265";
    private final String APP_KEY = "c44c5c02e8a23b5624cc2aa012736589";
    private final String API_URL = "https://api.schiphol.nl/public-flights/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void getFlightsFromJson(JsonNode json){
        for (JsonNode flight : json){

        }
    }

    public void getFlights() {
        String flightKey = "flights";
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(API_URL + "flights?app_id=" + APP_ID + "&app_key=" + APP_KEY);
            request.addHeader("ResourceVersion", "v3");

            HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

                JsonNode responseJson = objectMapper.readTree(responseBody);

                if (responseJson.has(flightKey) && responseJson.get(flightKey).isArray()){

                    getFlightsFromJson(responseJson.get(flightKey));

                } else {

                }

                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(responseBody);
                JSONArray flights = (JSONArray) jsonObject.get("flights");
                System.out.println("found " + flights.size() + " flights");
                flights.forEach(System.out::println);
            } else {
                System.out.println(
                        "Oops something went wrong\nHttp response code: " + response.getStatusLine().getStatusCode() + "\nHttp response body: "
                                + EntityUtils.toString(response.getEntity()));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Oops something went wrong\nPlease insert your APP_ID and APP_KEY as arguments");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

    }
}
