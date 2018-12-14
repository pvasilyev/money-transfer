package com.money.transfer.dao;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.money.transfer.api.dto.Account;
import com.money.transfer.api.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of {@link BankDao} which holds all objects in RAM.
 * <p>
 * <b>Note</b>: after this DAO is instantiated by IoC the {@link #postConstruct()} will be invoked which
 * currently implemented to load-up some sample data. That's solely for demo purposes.
 *
 * @author pvasilyev
 */
public class InMemoryBankDao implements BankDao {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Map<String, User> userById = new ConcurrentHashMap<>();
    private final Map<String, Account> accountById = new ConcurrentHashMap<>();

    @Override
    public User findUser(String id) {
        if (!userExists(id)) {
            throw new IllegalArgumentException("There is no such user with id=" + id);
        }
        return userById.get(id);
    }

    @Override
    public boolean userExists(String id) {
        return userById.containsKey(id);
    }

    @Override
    public void createUser(String userId, User user) {
        if (userById.containsKey(userId)) {
            throw new IllegalArgumentException("There is already user with id=" + user.getId());
        }
        if (!userId.equals(user.getId())) {
            throw new IllegalArgumentException("Provided userId=" + userId + " doesn't match with user's id=" + user.getId());
        }
        // operation of user creation is not so frequent in our Bank, so it should be ok to sync on 'this':
        synchronized (this) {
            userById.put(userId, user);
            for (Account account : user.getAccounts()) {
                accountById.put(account.getId(), account);
            }
        }
    }

    @Override
    public Account findAccount(String id) {
        if (!accountExists(id)) {
            throw new IllegalArgumentException("There is no such account with id=" + id);
        }
        return accountById.get(id);
    }

    @Override
    public boolean accountExists(String id) {
        return accountById.containsKey(id);
    }

    @Override
    public void createAccount(String userId, String accountId, Account account) {
        if (accountById.containsKey(accountId)) {
            throw new IllegalArgumentException("There is already account with id=" + accountId);
        }
        if (!userById.containsKey(userId)) {
            throw new IllegalArgumentException("There is no such user with id=" + userId);
        }
        if (!accountId.equals(account.getId())) {
            throw new IllegalArgumentException("Provided accountId=" + accountId + " doesn't match with account's id=" + account.getId());
        }
        // operation of account creation is not so frequent in our Bank, so it should be ok to sync on 'this':
        synchronized (this) {
            final User user = userById.get(userId);
            final List<Account> accounts = user.getAccounts();
            accounts.add(account);
            accountById.put(accountId, account);
        }
    }

    @PostConstruct
    public void postConstruct() {
        LOG.info("Loading some sample data into in-memory Bank DAO.");
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        final String resourceName = "/sample-data.json";
        final InputStream resourceAsStream = this.getClass().getResourceAsStream(resourceName);
        if (resourceAsStream != null) {
            try {
                final User[] users = mapper.readValue(resourceAsStream, User[].class);
                for (User user : users) {
                    createUser(user.getId(), user);
                }
                LOG.info("Loaded {} user(s) into in-memory Bank DAO", users.length);
            } catch (IOException e) {
                LOG.error("Error while parsing content of '" + resourceName + "' into Java object.");
            } finally {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    LOG.error("Error while closing resource stream", e);
                }
            }
        } else {
            LOG.warn("Can't find resource by name: {}, something is wrong with class-path...", resourceName);
        }
    }

}
