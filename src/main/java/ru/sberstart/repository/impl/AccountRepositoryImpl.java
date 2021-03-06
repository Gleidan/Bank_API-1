package ru.sberstart.repository.impl;

import lombok.AllArgsConstructor;
import ru.sberstart.entity.Account;
import ru.sberstart.repository.AccountRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {
    private final Connection connection;


    public Account findOne(long id) throws SQLException {
        PreparedStatement prStatement = connection.prepareStatement("SELECT * FROM accounts WHERE id = ?");
        prStatement.setLong(1, id);
        ResultSet rs = prStatement.executeQuery();
        rs.next();
        Account account = fetch(rs);
        rs.close();
        prStatement.close();
        return account;

    }

    public List<Account> findAll() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        PreparedStatement prStatement = connection.prepareStatement("SELECT * FROM accounts");
        ResultSet rs = prStatement.executeQuery();
        while(rs.next()) {
            accounts.add(fetch(rs));
        }
        prStatement.close();
        return accounts;
    }

    public Account persist(Account account) throws SQLException {
        PreparedStatement prStatement = connection.prepareStatement("INSERT INTO accounts values (default)",
                Statement.RETURN_GENERATED_KEYS);
        prStatement.executeUpdate();
        ResultSet rs = prStatement.getGeneratedKeys();
        if (rs.next()) {
            long id = rs.getLong(1);
            account.setId(id);
        }
        prStatement.close();
        return account;
    }

    public boolean removeOne(long id) throws SQLException {
        PreparedStatement prStatement = connection.prepareStatement("DELETE FROM accounts WHERE id = ?; " +
                "DELETE FROM cards WHERE account_id = ?");
        prStatement.setLong(1, id);
        prStatement.setLong(2, id);
        int i = prStatement.executeUpdate();
        prStatement.close();
        return i > 0;
    }



    private Account fetch(ResultSet row) throws SQLException {
        if(row == null) return null;
        Account account = new Account();
        account.setId(row.getLong("id"));
        return account;
    }
}
