package org.example.external.persistence;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.example.domain.application.interfaces.persistence.FlightRepository;
import org.example.domain.model.Flight;
import org.example.domain.model.Route;

public class InMemoryFlightRepository implements FlightRepository {

    private Connection connection;

    public InMemoryFlightRepository() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:h2:mem:flightdb");
        createTableIfNotExists();
    }

    @Override
    public List<Flight> searchAll(int limit, int offset) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM flights WHERE available_seats >= 1 LIMIT ? OFFSET ?");
            statement.setInt(1, limit);
            statement.setInt(2, offset);

            ResultSet resultSet = statement.executeQuery();

            List<Flight> flights = new ArrayList<>();
            while (resultSet.next()) {
                Flight flight = new Flight(resultSet.getString("id"));
                Route foundRoute = new Route(resultSet.getString("origin"),
                                        resultSet.getString("destination"));
                flight.setRoute(foundRoute);
                flight.setDepartureTime(resultSet.getTimestamp("departure_time").toLocalDateTime());
                flight.setArrivalTime(resultSet.getTimestamp("arrival_time").toLocalDateTime());
                flight.setAvailableSeats(resultSet.getInt("available_seats"));
                flight.setPrice(resultSet.getBigDecimal("price"));
                flights.add(flight);
            }
            return flights;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Flight> search(Route route, LocalDate departureDate) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM flights WHERE origin = ? AND destination = ? AND departure_time >= ? AND departure_time <= ?");
            statement.setString(1, route.getOrigin());
            statement.setString(2, route.getDestination());
            statement.setTimestamp(3, Timestamp.valueOf(departureDate.atStartOfDay()));
            statement.setTimestamp(4, Timestamp.valueOf(departureDate.atTime(23, 59)));

            ResultSet resultSet = statement.executeQuery();

            List<Flight> flights = new ArrayList<>();
            while (resultSet.next()) {
                Flight flight = new Flight(resultSet.getString("id"));
                Route foundRoute = new Route(resultSet.getString("origin"),
                                        resultSet.getString("destination"));
                flight.setRoute(foundRoute);
                flight.setDepartureTime(resultSet.getTimestamp("departure_time").toLocalDateTime());
                flight.setArrivalTime(resultSet.getTimestamp("arrival_time").toLocalDateTime());
                flight.setAvailableSeats(resultSet.getInt("available_seats"));
                flight.setPrice(resultSet.getBigDecimal("price"));
                flights.add(flight);
            }
            return flights;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Flight flight) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO flights(id, origin, destination, departure_time, arrival_time, available_seats, price) VALUES (?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, flight.getId());
            statement.setString(2, flight.getRoute().getOrigin());
            statement.setString(3, flight.getRoute().getDestination());
            statement.setTimestamp(4, Timestamp.valueOf(flight.getDepartureTime()));
            statement.setTimestamp(5, Timestamp.valueOf(flight.getArrivalTime()));
            statement.setInt(6, flight.getAvailableSeats());
            statement.setBigDecimal(7, flight.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS flights(" +
                "id VARCHAR(100) PRIMARY KEY, " +
                "origin VARCHAR(100), destination VARCHAR(100), " +
                "departure_time TIMESTAMP, " +
                "arrival_time TIMESTAMP, " +
                "available_seats INT, " +
                "price DECIMAL)";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    @Override
    public void createSampleFlights() {

        // Create 5 flights
        Flight flight1 = new Flight("UA101", new Route("New York", "Los Angeles"), LocalDateTime.of(2023, 4, 1, 7, 0), LocalDateTime.of(2023, 4, 1, 10, 30), 100, new BigDecimal(299.990));
        Flight flight2 = new Flight("UA102", new Route("Los Angeles", "New York"), LocalDateTime.of(2023, 4, 2, 7, 0), LocalDateTime.of(2023, 4, 2, 10, 30), 50, new BigDecimal(399.99));
        Flight flight3 = new Flight("AA201", new Route("Chicago", "Dallas"), LocalDateTime.of(2023, 4, 3, 7, 0), LocalDateTime.of(2023, 4, 3, 10, 30), 200, new BigDecimal(199.99));
        Flight flight4 = new Flight("AA202", new Route("Dallas", "Chicago"), LocalDateTime.of(2023, 4, 4, 7, 0), LocalDateTime.of(2023, 4, 4, 10, 30), 150, new BigDecimal(259.99));
        Flight flight5 = new Flight("DL301", new Route("Miami", "Seattle"), LocalDateTime.of(2023, 4, 5, 7, 0), LocalDateTime.of(2023, 4, 5, 10, 30), 300, new BigDecimal(449.99));
        Flight flight6 = new Flight("AA203", new Route("Chicago", "Dallas"), LocalDateTime.of(2023, 4, 3, 14, 0), LocalDateTime.of(2023, 4, 3, 15, 30), 200, new BigDecimal(199.99));

        List<Flight> flights = new ArrayList<>();
        flights.add(flight1);
        flights.add(flight2);
        flights.add(flight3);
        flights.add(flight4);
        flights.add(flight5);
        flights.add(flight6);

        // Save flights to repository
        for (Flight flight : flights) {
            this.save(flight);
        }
    }

}
