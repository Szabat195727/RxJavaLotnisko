package helpers;

import models.Flight;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by krystian on 07.06.17.
 */
public class FlightFactory {

    public static Flight makeFlight(JsonNode source){
        Flight f = new Flight();

        f.setId(source.get("id").asText("-"));
        f.setFlightName(source.get("flightName").asText("-"));
        f.setFlightDirection(source.get("flightDirection").asText());
        f.setFlightDestination(source.get("route").get("destinations").toString());
        f.setScheduleDate(source.get("scheduleDate").asText("-"));
        f.setScheduleTime(source.get("scheduleTime").asText("-"));
        f.setMainFlight(source.get("mainFlight").asText("-"));
        f.setGate(source.get("gate").asText("-"));
        f.setTerminal(source.get("terminal").asText("-"));
        f.setFlightNumber(source.get("flightNumber").asInt());

        return f;
    }

}