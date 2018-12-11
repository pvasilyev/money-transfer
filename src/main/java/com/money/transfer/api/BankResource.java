package com.money.transfer.api;

import com.money.transfer.api.dto.Account;
import com.money.transfer.api.dto.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Date;

@Api(value = "/", tags = {"Bank Resource"})
@Path("/")
public class BankResource {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @XmlRootElement
    public enum Status {
        OK
    }

    @GET
    @Path("status")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Status", notes = "Simple end-point to check if Bank Service is alive.", response = Status.class)
    public Status status() {
        LOG.info("Bank service is alive.");
        return Status.OK;
    }

    @GET
    @Path("user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get user", notes = "Get user by id", response = User.class)
    public User user(@ApiParam(value = "user id", required = true) @PathParam("id") String userId) {
        // todo make me more dynamic
        User user = new User();
        user.setId(userId);
        user.setFirstName("Peter");
        user.setLastName("Doe");
        user.setAccounts(Arrays.asList(account("random_account")));
        LOG.info("Retrieving static user {}", user);
        return user;
    }

    @GET
    @Path("account/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get account", notes = "Get account by id", response = User.class)
    public Account account(@ApiParam(value = "account id", required = true) @PathParam("id") String accountId) {
        // todo make me more dynamic
        Account account = new Account();
        account.setCreated(new Date());
        account.setId(accountId);
        account.setMoney(250.75D);
        account.setStatus(Account.Status.ACTIVE);
        account.setLastModified(new Date());
        LOG.info("Retrieving static account {}", account);
        return account;
    }

}
