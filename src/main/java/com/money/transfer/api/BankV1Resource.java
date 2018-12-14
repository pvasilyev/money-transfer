package com.money.transfer.api;

import com.money.transfer.api.dto.Account;
import com.money.transfer.api.dto.Transfer;
import com.money.transfer.api.dto.User;
import com.money.transfer.dao.BankDao;
import com.money.transfer.service.BankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.glassfish.hk2.api.Immediate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;
import java.lang.invoke.MethodHandles;

/**
 * Bank-v1 resource which exposes all REST methods that might come in handy for working with {@link User}s
 * and/or {@link Account}s.
 * <p>
 * As one can notice current API is write-once, i.e. currently it doesn't support operation of updating data
 * either of {@link User} or {@link Account}. The only updatable item within current implementation is account-balance.
 * I.e. money can be transferred from one account to another. This is not a limitation at the moment.
 * This behavior can be extended in future.
 * <p>
 * This resource make use of DI to inject {@link BankDao} and {@link BankService} to work with.
 *
 * @author pvasilyev
 */
@Api(value = "/v1", tags = {"Bank Resource v1"})
@Path("/v1")
@Immediate
public class BankV1Resource {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Inject
    private BankDao bankDao;
    @Inject
    private BankService bankService;

    @XmlRootElement
    public enum Status {
        OK
    }

    @POST
    @Path("transfer")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Transfer money", notes = "Create account by id", response = User.class)
    public Status transfer(@ApiParam(value = "Transfer JSON", required = true) Transfer transfer) {
        LOG.info("Received following transfer request: {}", transfer);
        bankService.transferMoney(transfer);
        LOG.info("Transfer request succeeded.");
        return Status.OK;
    }

    @GET
    @Path("user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get user", notes = "Get user by id", response = User.class)
    public User user(@ApiParam(value = "User id", required = true) @PathParam("id") String userId) {
        final User user = bankDao.findUser(userId);
        LOG.info("Retrieving user from DAO: {}", user);
        return user;
    }

    @POST
    @Path("user/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create user", notes = "Create user by id", response = User.class)
    public Status createUser(@ApiParam(value = "User id", required = true) @PathParam("id") String userId,
                             @ApiParam(value = "User JSON", required = true) User user) {
        LOG.info("Received following user: {}", user);
        bankDao.createUser(userId, user);
        LOG.info("Following userId={} was persisted into DAO", userId);
        return Status.OK;
    }

    @GET
    @Path("account/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get account", notes = "Get account by id", response = User.class)
    public Account account(@ApiParam(value = "account id", required = true) @PathParam("id") String accountId) {
        final Account account = bankDao.findAccount(accountId);
        LOG.info("Retrieving account from DAO: {}", account);
        return account;
    }

    @POST
    @Path("user/{userId}/account/{accountId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create user", notes = "Create account by id", response = User.class)
    public Status createAccount(@ApiParam(value = "User id", required = true) @PathParam("userId") String userId,
                                @ApiParam(value = "Account id", required = true) @PathParam("accountId") String accountId,
                                @ApiParam(value = "Account JSON", required = true) Account account) {
        LOG.info("Received following user: {}", account);
        bankDao.createAccount(userId, accountId, account);
        LOG.info("Following accountId={} was persisted into DAO under userId={}", accountId, userId);
        return Status.OK;
    }

    @GET
    @Path("status")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Status", notes = "Simple end-point to check if Bank Service is alive.", response = Status.class)
    public Status status() {
        LOG.info("Bank service is alive.");
        if (bankDao != null) {
            return Status.OK;
        } else {
            return null;
        }
    }

}
