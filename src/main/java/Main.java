import ApiService.AirportInfoService;

/**
 * Created by krystian on 07.06.17.
 */
public class Main {

    public static void main(String[] args) {
        AirportInfoService airportInfoService = new AirportInfoService();
        airportInfoService.getFlights();
    }

}
