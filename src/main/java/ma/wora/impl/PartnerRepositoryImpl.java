package main.java.ma.wora.impl;

import main.java.ma.wora.config.JdbcPostgresqlConnection;
import main.java.ma.wora.models.entities.Partner;
import main.java.ma.wora.models.enums.PartnerStatus;
import main.java.ma.wora.models.enums.TransportType;
import main.java.ma.wora.repositories.PartnerRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PartnerRepositoryImpl implements PartnerRepository {
    private final Connection connection = JdbcPostgresqlConnection.getInstance().getConnection();
    private final String tableName = "partners";

    public PartnerRepositoryImpl() throws SQLException {
    }

    @Override
    public List<Partner> findAll() {
        final List<Partner> partners = new ArrayList<>();
        final String query = "SELECT * FROM " + tableName;

        try (final Statement stmt = connection.createStatement()) {
            final ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                final Partner partner = new Partner(
                        UUID.fromString(resultSet.getString("id")),
                        resultSet.getString("company_name"),
                        TransportType.valueOf(resultSet.getString("transport_type").toUpperCase()),
                        resultSet.getString("geographical_zone"),
                        resultSet.getString("special_conditions"),
                        PartnerStatus.valueOf(resultSet.getString("status").toUpperCase()),
                        resultSet.getDate("creation_date")
                );
                partners.add(partner);
            }
            return partners;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Partner findByName(String name) {
        final String query = "SELECT * FROM " + tableName + " WHERE company_name = '" + name + "'";
        try (final Statement stmt = connection.createStatement()) {
            final ResultSet resultSet = stmt.executeQuery(query);
            if (resultSet.next()) {
                return new Partner(
                        UUID.fromString(resultSet.getString("id")),
                        resultSet.getString("company_name"),
                        TransportType.valueOf(resultSet.getString("transport_type").toUpperCase()),
                        resultSet.getString("geographical_zone"),
                        resultSet.getString("special_conditions"),
                        PartnerStatus.valueOf(resultSet.getString("status").toUpperCase()),
                        resultSet.getDate("creation_date")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
        pstmt.setDate(6, Date.valueOf(String.valueOf(partner.getCreationDate())));
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
}
