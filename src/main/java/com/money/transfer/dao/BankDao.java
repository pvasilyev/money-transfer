package com.money.transfer.dao;

import com.money.transfer.api.dto.Account;
import com.money.transfer.api.dto.User;

/**
 * Straight-forward DAO to work with Bank objects such as {@link User} and/or {@link Account}.
 *
 * @author pvasilyev
 */
public interface BankDao {

    /**
     * Operation of looking up user by given <code>id</code>.
     *
     * @param id is the user's identifier.
     * @return corresponding {@link User} if it exists in the Bank, otherwise not specified. It is supposed that
     * corresponding check whether user exists will be performed beforehand ({@link #userExists(String)}).
     * @see #userExists(String)
     */
    User findUser(String id);

    /**
     * Operation to check if corresponding user exists in the Bank or not.
     *
     * @param id is the user's identifier.
     * @return true if user with given <code>id</code> exists in the Bank, false otherwise.
     */
    boolean userExists(String id);

    /**
     * Operation to create user using <code>id</code> and entire payload with all information about user.
     *
     * @param userId is the user's identifier.
     * @param user   is holder of all information about user, like first-name and/or last-name, etc.
     */
    void createUser(String userId, User user);

    /**
     * Operation of looking up account by given <code>id</code>.
     *
     * @param id is the account's identifier.
     * @return corresponding {@link Account} if it exists in the Bank, otherwise not specified. It is supposed that
     * corresponding check whether account exists will be performed beforehand ({@link #accountExists(String)}).
     * @see #accountExists(String)
     */
    Account findAccount(String id);

    /**
     * Operation to check if corresponding account exists in the Bank or not.
     *
     * @param id is the account's identifier.
     * @return true if account with given <code>id</code> exists in the Bank, false otherwise.
     */
    boolean accountExists(String id);

    /**
     * Operation to create account using <code>userId</code>, <code>accountId</code> and entire
     * payload with all information about account.
     *
     * @param userId    is the user's identifier.
     * @param accountId is the account's identifier.
     * @param account   is holder of all information about account, like its name (purpose), balance, etc.
     */
    void createAccount(String userId, String accountId, Account account);

}
