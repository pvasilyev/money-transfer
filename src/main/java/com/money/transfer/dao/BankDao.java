package com.money.transfer.dao;

import com.money.transfer.api.dto.Account;
import com.money.transfer.api.dto.User;

public interface BankDao {

    User findUser(String id);
    void createUser(String userId, User user);

    Account findAccount(String id);
    void createAccount(String userId, String accountId, Account account);

}
