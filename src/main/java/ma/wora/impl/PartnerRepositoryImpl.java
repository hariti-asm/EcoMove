package main.java.ma.wora.impl;

import main.java.ma.wora.config.JdbcPostgresqlConnection;
import main.java.ma.wora.models.entities.Partner;
import main.java.ma.wora.models.enums.PartnerStatus;
import main.java.ma.wora.repositories.PartnerRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PartnerRepositoryImpl implements PartnerRepository {
    private final Connection connection = JdbcPostgresqlConnection.getInstance().getConnection();
    private final String tableName = "partners";

    public PartnerRepositoryImpl() throws SQLException {
    }

    @Override

    public List<String> findAll() {
        List<String> partners = new ArrayList<>();
        String query = "SELECT company_name FROM "+tableName;

        try (Statement stmt = connection.createStatement()){
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()){
                partners.add(resultSet.getString("company_name"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return partners;
    }
    @Override
    public void UpdatePartnerStatus(UUID partnerId, PartnerStatus newStatus) {

        String query = "UPDATE "+tableName+" SET status = ? WHERE id = id::uuid";

        try (PreparedStatement pstmt = connection.prepareStatement(query)){

            pstmt.setString(1, newStatus.name());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Partner findByName(String companyName) {
        String query = "SELECT * FROM partners WHERE company_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, companyName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                String name = rs.getString("company_name");
                String transportType = rs.getString("transport_type");
                String geographicalZone = rs.getString("geographical_zone");
                String specialConditions = rs.getString("special_conditions");
                String status = rs.getString("status");
                LocalDate creationDate = rs.getDate("creation_date").toLocalDate();

                Partner partner = new Partner(id, name, transportType, geographicalZone, specialConditions, status, Date.valueOf(creationDate));
                return partner;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Partner add(Partner partner) {
        if (partner.getId() == null) {
            partner.setId(UUID.randomUUID());
        }
        String creationDateValue = (partner.getCreationDate() != null) ?
                "'" + partner.getCreationDate() + "'" : "NULL";

        final String query = "INSERT INTO " + tableName +
                " (id, company_name, transport_type, geographical_zone, special_conditions, status, creation_date) " +
                "VALUES ('" + partner.getId() + "', '" + partner.getCompanyName() + "', '" +
                partner.getTransportType() + "', '" + partner.getGeographicalZone() + "', '" +
                partner.getSpecialConditions() + "', '" + partner.getStatus() + "', " +
                creationDateValue + ")";

        try (final Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);

            if (rowsAffected > 0) {
                System.out.println("Partner inserted successfully.");
                return partner;
            } else {
                System.out.println("Partner wasn't inserted.");
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
@Override

public Partner update(Partner partner) {
    final String query = "UPDATE " + tableName +
            " SET company_name = ?, transport_type = ?, geographical_zone = ?, special_conditions = ?, status = ?, creation_date = ?" +
            " WHERE id = ?";

    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
        pstmt.setString(1, partner.getCompanyName());
        pstmt.setString(2, String.valueOf(partner.getTransportType()));
        pstmt.setString(3, partner.getGeographicalZone());
        pstmt.setString(4, partner.getSpecialConditions());
        pstmt.setString(5, String.valueOf(partner.getStatus()));

        // Convert java.util.Date to java.sql.Date
        if (partner.getCreationDate() != null) {
            pstmt.setDate(6, new java.sql.Date(partner.getCreationDate().getTime()));
        } else {
            pstmt.setNull(6, java.sql.Types.DATE);
        }

        pstmt.setObject(7, partner.getId());

        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Partner updated successfully.");
            return partner;
        } else {
            System.out.println("Partner not found or wasn't updated.");
            return null;
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

    @Override
    public boolean remove(UUID id) {
        final String query = "DELETE FROM " + tableName + " WHERE id = '" + id.toString() + "'";

        try (final Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);

            if (rowsAffected > 0) {
                System.out.println("Partner with ID " + id + " was removed successfully.");
                return true;
            } else {
                System.out.println("Partner with ID " + id + " not found.");
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove partner with ID: " + id, e);
        }
    }
    @Override
    public boolean changeStatus(UUID id, PartnerStatus newStatus) {
        final String query = "UPDATE " + tableName + " SET status = '" + newStatus.name() + "' WHERE id = '" + id.toString() + "'";

        try (final Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);

            if (rowsAffected > 0) {
                System.out.println("Partner status updated successfully.");
                return true;
            } else {
                System.out.println("Partner with ID " + id + " not found.");
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update partner status with ID: " + id, e);
        }
    }
    @Override
    public Partner findById(UUID id) {
        final String query = "SELECT * FROM " + tableName + " WHERE id = ?";
        try (final PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setObject(1, id);
            final ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return new Partner(
                        UUID.fromString(resultSet.getString("id")),
                        resultSet.getString("company_name"),
                        resultSet.getString("transport_type"),
                        resultSet.getString("geographical_zone"),
                        resultSet.getString("special_conditions"),
                        resultSet.getString("status"),
                        resultSet.getDate("creation_date")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


}
