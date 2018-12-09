package com.money.transfer.api;

import com.money.transfer.api.dto.Account;
import com.money.transfer.api.dto.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.Date;

@Path("/api")
public class BankController {

    @XmlRootElement
    public enum Status {
        OK
    }

    @GET
    @Path("status")
    @Produces(MediaType.APPLICATION_JSON)
    public Status status() {
        return Status.OK;
    }

    @GET
    @Path("user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User user(@PathParam("id") String userId) {
        // todo make me more dynamic
        User user = new User();
        user.setId(userId);
        user.setFirstName("Peter");
        user.setLastName("Doe");
        user.setAccounts(Arrays.asList(account("random_account")));

        return user;
    }

    @GET
    @Path("account/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Account account(@PathParam("id") String accountId) {
        // todo make me more dynamic
        Account account = new Account();
        account.setCreated(new Date());
        account.setId(accountId);
        account.setMoney(250.75D);
        account.setStatus(Account.Status.ACTIVE);
        account.setLastModified(new Date());

        return account;
    }

}
