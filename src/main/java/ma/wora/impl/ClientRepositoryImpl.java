package main.java.ma.wora.impl;

import main.java.ma.wora.config.JdbcPostgresqlConnection;
import main.java.ma.wora.models.entities.Client;
import main.java.ma.wora.repositories.ClientRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClientRepositoryImpl implements ClientRepository {
private final Connection connection = JdbcPostgresqlConnection.getInstance().getConnection();

    public ClientRepositoryImpl( ) throws SQLException {
    }
     private  final  String tableName = "client";


    @Override
    public Client createClient(Client client) {
    String query = "INSERT INTO"+tableName+" (id, first_name, last_name, email , phone_number) VALUES (?, ?, ?)" + "VALUES (?, ?, ?, ?)";
    try(PreparedStatement pst = connection.prepareStatement(query)) {
    pst.setObject(1,client.getId());
    pst.setString(2,client.getFirstName());
    pst.setString(3,client.getLastName());
    pst.setString(4,client.getEmail());
    pst.setObject(5, client.getPhone());
    int affectedRows = pst.executeUpdate();
    if(affectedRows == 0) {
        return null;
    }
    System.out.println("client created successfully");
    return client;

} catch (Exception e) {
    throw new RuntimeException(e);
}

    }

    @Override
    public Client authenticate(String last_name, String email) {
        return null;
    }

    @Override
    public Client getClientByEmail(String email) {
        return null;
    }
    @Override
    public Client updateClient(Client client) {
        return null;
    }
}
