package main.java.ma.wora.impl;

import main.java.ma.wora.config.JdbcPostgresqlConnection;
import main.java.ma.wora.models.entities.Contract;
import main.java.ma.wora.models.entities.Partner;
import main.java.ma.wora.models.enums.ContractStatus;
import main.java.ma.wora.models.enums.DiscountType;
import main.java.ma.wora.models.enums.PartnerStatus;
import main.java.ma.wora.models.enums.TransportType;
import main.java.ma.wora.repositories.ContractRepository;
import main.java.ma.wora.repositories.PartnerRepository;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ContractRepositoryImpl implements ContractRepository {

    private final Connection connection = JdbcPostgresqlConnection.getInstance().getConnection();
    private final String tableName = "contracts";
    private PartnerRepository partnerRepository;

    public ContractRepositoryImpl(PartnerRepository partnerRepository) throws SQLException {
        this.partnerRepository = partnerRepository;
    }
    private ContractRepository contractRepository;

    public void Contract(ContractRepository contractRepository, PartnerRepository partnerRepository) {
        this.contractRepository = contractRepository;
        this.partnerRepository = partnerRepository;
    }

    @Override
    public Contract createContract(Contract contract) {
        if (contract.getId() == null) {
            contract.setId(UUID.randomUUID());
        }

        final String query = "INSERT INTO contracts " +
                "(id, start_date, end_date, special_rate, agreement_conditions, renewable, status, partner_id, discount_type) " +
                "VALUES ('" + contract.getId() + "', " +
                (contract.getStartDate() != null ? "'" + new SimpleDateFormat("yyyy-MM-dd").format(contract.getStartDate()) + "'" : "NULL") + ", " +
                (contract.getEndDate() != null ? "'" + new SimpleDateFormat("yyyy-MM-dd").format(contract.getEndDate()) + "'" : "NULL") + ", " +
                (contract.getSpecialRate() != null ? contract.getSpecialRate() : "NULL") + ", " +
                (contract.getAgreementConditions() != null ? "'" + contract.getAgreementConditions() + "'" : "NULL") + ", " +
                (contract.isRenewable() ? "TRUE" : "FALSE") + ", " +
                (contract.getStatus() != null ? "'" + contract.getStatus().name() + "'" : "NULL") + ", " +
                (contract.getPartner() != null ? "'" + contract.getPartner().getId() + "'" : "NULL") + ", " +
                (contract.getDiscountType() != null ? "'" + contract.getDiscountType().name() + "'::discount_type" : "NULL") +
                ")";

        try (final Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);

            if (rowsAffected > 0) {
                System.out.println("Contract inserted successfully.");
                return contract;
            } else {
                System.out.println("Contract wasn't inserted.");
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting contract: " + e.getMessage(), e);
        }
    }


    @Override
    public Contract getContractById(UUID id) {
        final String query = "SELECT * FROM contracts WHERE id = ?";

        try (final PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setObject(1, id);
            final ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                UUID partnerId = UUID.fromString(resultSet.getString("partner_id"));

                Partner partner = partnerRepository.findById(partnerId);
                return new Contract(
                        UUID.fromString(resultSet.getString("id")),
                        resultSet.getDate("start_date"),
                        resultSet.getDate("end_date"),
                        resultSet.getBigDecimal("special_rate"),
                        resultSet.getString("agreement_conditions"),
                        resultSet.getBoolean("renewable"),
                        ContractStatus.valueOf(resultSet.getString("status")),
                         partner,
                        DiscountType.valueOf(resultSet.getString("discount_type"))
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving contract: " + e.getMessage(), e);
        }
        return null;
    }


    @Override
    public List<Contract> getAllContracts() {

        final List<Contract> contracts = new ArrayList<>();
        final String query = "SELECT * FROM " + tableName;

        try (final Statement stmt = connection.createStatement()) {
            final ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                final Contract contract = new Contract(
                        UUID.fromString(resultSet.getString("id")),
                        resultSet.getDate("start_date"),
                        resultSet.getDate("end_date"),
                        resultSet.getBigDecimal("special_rate"),
                        resultSet.getString("agreement_conditions"),
                        resultSet.getBoolean("renewable"),
                        ContractStatus.valueOf(resultSet.getString("status")),
                        partnerRepository.findById(UUID.fromString(resultSet.getString("partner_id"))),
                        DiscountType.valueOf(resultSet.getString("discount_type"))
                );
                contracts.add(contract);
            }
            return contracts;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean updateContract(UUID contractId, Contract updatedContract) {
        String query = "UPDATE contracts SET partner_id = ?, start_date = ?, end_date = ?, special_rate = ?, agreement_conditions = ?, renewable = ?, status = ?::contract_status, discount_type = ?::discount_type WHERE id = ?";
        boolean updated = false;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setObject(1, updatedContract.getPartner().getId());
            pstmt.setDate(2, updatedContract.getStartDate());
            pstmt.setDate(3, updatedContract.getEndDate());
            pstmt.setBigDecimal(4, updatedContract.getSpecialRate());
            pstmt.setString(5, updatedContract.getAgreementConditions());
            pstmt.setBoolean(6, updatedContract.isRenewable());
            pstmt.setString(7, updatedContract.getStatus().name());
            pstmt.setString(8, updatedContract.getDiscountType().name());
            pstmt.setObject(9, contractId);

            int affectedRows = pstmt.executeUpdate();
            updated = (affectedRows > 0);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating contract by ID", e);
        }

        return updated;
    }
        @Override
    public boolean deleteContract(UUID id) {
        final String query = "DELETE FROM " + tableName + " WHERE id = '" + id.toString() + "'";

        try (final Statement stmt = connection.createStatement()) {
            int rowsAffected = stmt.executeUpdate(query);

            if (rowsAffected > 0) {
                System.out.println("Contract with ID " + id + " was removed successfully.");
                return true;
            } else {
                System.out.println("Contract with ID " + id + " not found.");
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove contract with ID: " + id, e);
        }
    }
    @Override
    public List<Contract> getTerminatedContracts() {
        final String query = "SELECT * FROM contracts WHERE status = ?::contract_status";
        List<Contract> terminatedContracts = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, ContractStatus.TERMINATED.name()); // Set the enum value
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                Contract contract = new Contract(
                                        UUID.fromString(resultSet.getString("id")),
                                        resultSet.getDate("start_date"),
                                        resultSet.getDate("end_date"),
                                        resultSet.getBigDecimal("special_rate"),
                                        resultSet.getString("agreement_conditions"),
                                        resultSet.getBoolean("renewable"),
                                        ContractStatus.valueOf(resultSet.getString("status")),
                        partnerRepository.findById(UUID.fromString(resultSet.getString("partner_id"))),
                        DiscountType.valueOf(resultSet.getString("discount_type"))
                                );
                terminatedContracts.add(contract);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving terminated contracts: " + e.getMessage(), e);
        }

        return terminatedContracts;
    }
    public List<Contract> getContractsByPartner(UUID partnerId) {
        final String query = "SELECT " +
                "c.id, " +
                "c.partner_id, " +
                "c.start_date, " +
                "c.end_date, " +
                "c.special_rate, " +
                "c.agreement_conditions, " +
                "c.renewable, " +
                "c.status, " +
                "c.discount_type, " +
                "p.company_name, " +
                "p.transport_type, " +
                "p.geographical_zone, " +
                "p.special_conditions, " +
                "p.status AS partner_status, " +
                "p.creation_date " +
                "FROM contracts c " +
                "JOIN partners p ON c.partner_id = p.id " +
                "WHERE c.partner_id = ?";

        List<Contract> contracts = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setObject(1, partnerId);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                // Create Partner object from the result set
                Partner partner = new Partner(
                        UUID.fromString(resultSet.getString("partner_id")),
                        resultSet.getString("company_name"),
                        TransportType.valueOf(resultSet.getString("transport_type")),
                        resultSet.getString("geographical_zone"),
                        resultSet.getString("special_conditions"),
                        PartnerStatus.valueOf(resultSet.getString("partner_status")),
                        resultSet.getDate("creation_date")
                );

                Contract contract = new Contract(
                                        UUID.fromString(resultSet.getString("id")),
                                        resultSet.getDate("start_date"),
                                        resultSet.getDate("end_date"),
                                        resultSet.getBigDecimal("special_rate"),
                                        resultSet.getString("agreement_conditions"),
                                        resultSet.getBoolean("renewable"),
                                        ContractStatus.valueOf(resultSet.getString("status")),
                        partnerRepository.findById(UUID.fromString(resultSet.getString("partner_id"))),
                        DiscountType.valueOf(resultSet.getString("discount_type"))
                                        );

                contracts.add(contract);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving contracts by partner: " + e.getMessage(), e);
        }

        return contracts;
    }

}
