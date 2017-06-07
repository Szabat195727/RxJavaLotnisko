package apiService;

import helpers.FlightFactory;
import models.Flight;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    private static final LinkedList<Flight> flightsList = new LinkedList<>();

    public void getFlightsFromJson(JsonNode json) {
        for (JsonNode flight : json) {
            Flight f = FlightFactory.makeFlight(flight);
            flightsList.add(f);
        }
    }

    public LinkedList<Flight> getFlightsFromApi() {
        String flightKey = "flights";
        try {
            for (int i = 0; i < 5; i++){
                HttpClient httpClient = HttpClients.createDefault();
                HttpGet request = new HttpGet(API_URL + "flights?app_id=" + APP_ID + "&app_key=" + APP_KEY + "&scheduletime=15%3A00&page=" + i);
                request.addHeader("ResourceVersion", "v3");

                HttpResponse response = httpClient.execute(request);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

                    JsonNode responseJson = objectMapper.readTree(responseBody);

                    if (responseJson.has(flightKey) && responseJson.get(flightKey).isArray()) {

                        getFlightsFromJson(responseJson.get(flightKey));

                    } else {
                        System.out.println("Wrong json. Not contains flights key");
                    }
                } else {
                    System.out.println(
                            "Oops something went wrong\nHttp response code: " + response.getStatusLine().getStatusCode() + "\nHttp response body: "
                                    + EntityUtils.toString(response.getEntity()));
                }
            }

            return flightsList;

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Oops something went wrong\nPlease insert your APP_ID and APP_KEY as arguments");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new LinkedList<Flight>();
    }

    public List<Flight> findAll(){
        return getFlightsFromApi();
    }

    public Observable<LinkedList<Flight>> getAllFlights() {
        Observable<LinkedList<Flight>> observable = Observable.fromCallable(this::getFlightsFromApi);
        observable.subscribeOn(Schedulers.io());

        return observable;
    }

    public Observable<List<Flight>> getAllArrivalOnWeekendFlights() {
        Observable<List<Flight>> observable = Observable.fromCallable(this::getAllArivalOnWeekends);
        observable.subscribeOn(Schedulers.io());

        return observable;
    }

    public List<Flight> getAllArivalOnWeekends() {
        Set<String> ids = new HashSet<>();

        List<Flight> weekendFlights = getFlightsFromApi().stream().filter(f -> checkIfDateInWeekend(f.getScheduleDate()) && f.getFlightDirection().equalsIgnoreCase("A")).collect(Collectors.toList());

        return weekendFlights;
    }

    public boolean checkIfDateInWeekend(String date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date flightDate = null;
        try {
            flightDate = format.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(flightDate);
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
            if (dayOfWeek == 0 || dayOfWeek == 6){
                return true;
            } else {
                return false;
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return false;
        }

    }

}
