package controllers;

import apiService.AirportInfoService;
import io.reactivex.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Flight;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * Created by krystian on 07.06.17.
 */
public class MVController implements Initializable {

    @FXML
    private TableView<Flight> flightsTable;

    @FXML
    private TableColumn<Flight, String> fNameColumn;

    @FXML
    private TableColumn<Flight, String> fNumColumn;

    @FXML
    private TableColumn<Flight, String> fDirColumn;

    @FXML
    private TableColumn<Flight, String> fDestColumn;

    @FXML
    private TableColumn<Flight, String> fStartDateColumn;

    @FXML
    private TableColumn<Flight, String> fStartTimeColumn;

    @FXML
    private TableColumn<Flight, String> fGateColumn;

    @FXML
    private TableColumn<Flight, String> fTerminalColumn;

    private AirportInfoService service = new AirportInfoService();
    private ObservableList<Flight> flightsObservable = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fNameColumn.setCellValueFactory(new PropertyValueFactory<>("flightName"));
        fNumColumn.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        fDirColumn.setCellValueFactory(new PropertyValueFactory<>("flightDirection"));
        fDestColumn.setCellValueFactory(new PropertyValueFactory<>("flightDestination"));
        fStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("scheduleDate"));
        fStartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("scheduleTime"));
        fGateColumn.setCellValueFactory(new PropertyValueFactory<>("gate"));
        fTerminalColumn.setCellValueFactory(new PropertyValueFactory<>("terminal"));
        flightsTable.setItems(flightsObservable);

        Observable.interval(5, TimeUnit.SECONDS)
                .concatMapIterable((evt) -> service.findAll())
                .distinct()
                .subscribe(this::reRenderTable);


    }

    private void reRenderTable(Flight newFlight) {
        flightsObservable.add(newFlight);
        System.out.println("departures updated!");
    }

}
