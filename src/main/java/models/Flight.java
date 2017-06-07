package models;

import lombok.Data;

/**
 * Created by krystian on 07.06.17.
 */
@Data
public class Flight {
    private String id;
    private String flightName;
    private String flightDirection;
    private String flightDestination;
    private String scheduleDate;
    private String scheduleTime;
    private String mainFlight;
    private String gate;
    private String terminal;
    private int flightNumber;
}
