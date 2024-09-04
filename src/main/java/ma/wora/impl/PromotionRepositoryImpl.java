package main.java.ma.wora.impl;

import main.java.ma.wora.config.JdbcPostgresqlConnection;
import main.java.ma.wora.models.entities.Promotion;
import main.java.ma.wora.models.enums.DiscountType;
import main.java.ma.wora.models.enums.PromotionStatus;
import main.java.ma.wora.repositories.PromotionRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PromotionRepositoryImpl implements PromotionRepository {

    private final Connection connection = JdbcPostgresqlConnection.getInstance().getConnection();
    private final String tableName = "promotions";

    public PromotionRepositoryImpl() throws SQLException {
    }

    @Override
    public Promotion findById(UUID id) {
        final String query = "SELECT * FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            // Set the parameter value in the prepared statement
            pstmt.setObject(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Retrieving values from the result set
                String promotionIdString = rs.getString("id");
                UUID promotionId = promotionIdString != null ? UUID.fromString(promotionIdString) : null;

                String promotionName = rs.getString("name");
                if (promotionName == null) {
                    throw new RuntimeException("Promotion name is null for ID: " + id);
                }

                String promotionDescription = rs.getString("description");
                LocalDate promotionStartDate = rs.getDate("start_date").toLocalDate();
                LocalDate promotionEndDate = rs.getDate("end_date").toLocalDate();
                DiscountType discountType = DiscountType.valueOf(rs.getString("discount_type"));
                BigDecimal discountAmount = rs.getBigDecimal("discount_value");
                String conditions = rs.getString("conditions");
                PromotionStatus promotionStatus = PromotionStatus.valueOf(rs.getString("status"));
                String contractIdString = rs.getString("contract_id");
                UUID contractId = contractIdString != null ? UUID.fromString(contractIdString) : null;

                // Create a new Promotion object and set its properties
                Promotion promotion = new Promotion(
                        promotionId,
                        promotionName,
                        promotionDescription,
                        promotionStartDate,
                        promotionEndDate,
                        discountType,
                        discountAmount,
                        promotionStatus,
                        conditions,
                        contractId
                );

                // Set additional properties
                promotion.setId(promotionId);
                promotion.setOfferName(promotionName);
                promotion.setDescription(promotionDescription);
                promotion.setStartDate(promotionStartDate);
                promotion.setEndDate(promotionEndDate);
                promotion.setStatus(promotionStatus);
                promotion.setDiscountType(discountType);
                promotion.setConditions(conditions);
                promotion.setDiscountValue(discountAmount);

                return promotion;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding promotion by ID", e);
        }
        return null;
    }

    @Override

    public Promotion create(Promotion promotion) {
        String query = "INSERT INTO "+tableName+" (id, contract_id,name, description, start_date, end_date, discount_type, discount_value, conditions, status) VALUES (?, ?, ?, ?, ?, ?, ?::discount_type, ?, ?, ?::promotion_status)";
        boolean added = false;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setObject(1,promotion.getId());
            pstmt.setObject(2,promotion.getContract());
            pstmt.setString(3,promotion.getOfferName());
            pstmt.setString(4,promotion.getDescription());
            pstmt.setDate(5, java.sql.Date.valueOf(promotion.getStartDate()));
            pstmt.setDate(6, java.sql.Date.valueOf(promotion.getEndDate()));
            pstmt.setString(7,promotion.getDiscountType().name());
            pstmt.setBigDecimal(8,promotion.getDiscountValue());
            pstmt.setString(9,promotion.getConditions());
            pstmt.setString(10,promotion.getStatus().name());

            int affectedRows = pstmt.executeUpdate();
            added = (affectedRows > 0);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return promotion;
    }



    @Override
    public Promotion update(Promotion offer) {
        String sql = "UPDATE "+tableName+" SET contract_id = ?, name = ?, description = ?, " +
                "start_date = ?, end_date = ?, discount_type = ?::discount_type, discount_value = ?, " +
                "conditions = ?, status = ?::promotion_status WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql))
        {

            pstmt.setObject(1, offer.getContract());
            pstmt.setString(2, offer.getOfferName());
            pstmt.setString(3, offer.getDescription());
            pstmt.setDate(4, Date.valueOf(offer.getStartDate()));
            pstmt.setDate(5, Date.valueOf(offer.getEndDate()));
            pstmt.setString(6, offer.getDiscountType().name());
            pstmt.setBigDecimal(7, offer.getDiscountValue());
            pstmt.setString(8, offer.getConditions());
            pstmt.setString(9, offer.getStatus().name());
            pstmt.setObject(10, offer.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating promotional offer failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offer;
    }
    @Override
    public Promotion delete(Promotion promotion) {
        final String query = "DELETE FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setObject(1, promotion.getId());
            pstmt.executeUpdate();
            return promotion;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting promotion", e);
        }
    }

    @Override
    public List<Promotion> findAll() {
        List<Promotion> offers = new ArrayList<>();
        String query = "SELECT * FROM " + tableName;

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // Handle potential nulls
                String idString = rs.getString("id");
                UUID promotionId = idString != null ? UUID.fromString(idString) : null;

                String name = rs.getString("name");
                String description = rs.getString("description");

                LocalDate startDate = rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null;
                LocalDate endDate = rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null;

                String discountTypeStr = rs.getString("discount_type");
                DiscountType discountType = discountTypeStr != null ? DiscountType.valueOf(discountTypeStr) : null;

                BigDecimal discountAmount = rs.getBigDecimal("discount_value");

                String conditions = rs.getString("conditions");

                String statusStr = rs.getString("status");
                PromotionStatus promotionStatus = statusStr != null ? PromotionStatus.valueOf(statusStr) : null;

                String contractIdString = rs.getString("contract_id");
                UUID contractId = contractIdString != null ? UUID.fromString(contractIdString) : null;

                // Create Promotion object
                Promotion offer = new Promotion(promotionId, name, description, startDate, endDate, discountType, discountAmount, promotionStatus, conditions, contractId);

                // Add to the list
                offers.add(offer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return offers;
    }

    @Override
    public List<Promotion> findArchived() {
        final String query = "SELECT * FROM " + tableName + " WHERE status = 'ARCHIVED'";
        List<Promotion> promotions = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                UUID promotionId = UUID.fromString(rs.getString("id"));
                String promotionName = rs.getString("name");
                String promotionDescription = rs.getString("description");
                LocalDate promotionStartDate = rs.getDate("start_date").toLocalDate();
                LocalDate promotionEndDate = rs.getDate("end_date").toLocalDate();
                DiscountType discountType = DiscountType.valueOf(rs.getString("discount_type"));
                BigDecimal discountAmount = rs.getBigDecimal("discount_value");
                String conditions = rs.getString("conditions");
                PromotionStatus promotionStatus = PromotionStatus.valueOf(rs.getString("status"));
                UUID contract_id = UUID.fromString(rs.getString("contract_id"));
                Promotion promotion = new Promotion( promotionId, promotionName, promotionDescription, promotionStartDate , promotionEndDate, discountType, discountAmount, promotionStatus,conditions, contract_id);
                promotion.setId(promotionId);
                promotion.setOfferName(promotionName);
                promotion.setDescription(promotionDescription);
                promotion.setStartDate(promotionStartDate);
                promotion.setEndDate(promotionEndDate);
                promotion.setStatus(promotionStatus);
                promotion.setDiscountType(discountType);
                promotion.setConditions(conditions);
                promotion.setDiscountValue(discountAmount);

                promotions.add(promotion);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding archived promotions", e);
        }
        return promotions;
    }
}
