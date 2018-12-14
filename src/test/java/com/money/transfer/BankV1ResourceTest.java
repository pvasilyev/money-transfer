package com.money.transfer;

import com.money.transfer.api.BankV1Resource;
import com.money.transfer.api.dto.Account;
import com.money.transfer.api.dto.Transfer;
import com.money.transfer.api.dto.User;
import com.money.transfer.dao.BankDao;
import com.money.transfer.dao.InMemoryBankDao;
import com.money.transfer.service.BankService;
import com.money.transfer.service.BankServiceImpl;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BankV1ResourceTest extends JerseyTest {

    private static class ImmediateFeature implements Feature {

        @Inject
        public ImmediateFeature(ServiceLocator locator) {
            ServiceLocatorUtilities.enableImmediateScope(locator);
        }

        @Override
        public boolean configure(FeatureContext context) {
            return true;
        }
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(BankV1Resource.class)
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(InMemoryBankDao.class).to(BankDao.class).in(Singleton.class);
                        bind(BankServiceImpl.class).to(BankService.class).in(Singleton.class);
                    }
                })
                .register(ImmediateFeature.class);
    }

    @Test
    public void sillyTest() {
        final String hello = target("/v1/status").request().get(String.class);
        Assert.assertEquals("{\"value\":\"OK\"}", hello);
    }

    @Test
    public void checkClarkKent() throws ParseException {
        final User user = target("/v1/user/ZJFCHF2539").request().get(User.class);
        Assert.assertThat(user.getId(), IsEqual.equalTo("ZJFCHF2539"));
        Assert.assertThat(user.getFirstName(), IsEqual.equalTo("Clark"));
        Assert.assertThat(user.getLastName(), IsEqual.equalTo("Kent"));
        final List<Account> accounts = user.getAccounts();
        Assert.assertThat(accounts, IsNull.notNullValue());
        Assert.assertThat(accounts.size(), IsEqual.equalTo(1));
        Assert.assertThat(accounts.get(0).getId(), IsEqual.equalTo("ZDO2OMTS87"));
        Assert.assertThat(accounts.get(0).getName(), IsEqual.equalTo("Mortgage Account"));
        Assert.assertThat(accounts.get(0).getBalance(), Matchers.closeTo(-4212.32D, 1E-6));
        Assert.assertThat(accounts.get(0).getStatus(), IsEqual.equalTo(Account.Status.ACTIVE));

        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Assert.assertThat(accounts.get(0).getCreated(), IsEqual.equalTo(dateFormat.parse("2018-01-17T22:15:40Z")));
        Assert.assertThat(accounts.get(0).getLastModified(), IsEqual.equalTo(dateFormat.parse("2018-11-22T14:36:45Z")));
    }

    @Test
    public void createNewUserPeterParker() {
        final User user = new User();
        user.setId("BBP7N33GE8");
        user.setFirstName("Peter");
        user.setLastName("Parker");
        final Account account = new Account();
        account.setId("6KSO7HVS8M");
        account.setName("Stock Account");
        account.setBalance(23_214.45D);
        user.setAccounts(Collections.singletonList(account));

        String status = target("/v1/user/BBP7N33GE8")
                .request()
                .post(Entity.entity(user, MediaType.APPLICATION_JSON_TYPE), String.class);
        Assert.assertThat(status, IsEqual.equalTo("{\"value\":\"OK\"}"));

        final User peter = target("/v1/user/BBP7N33GE8").request().get(User.class);
        Assert.assertThat(peter.getId(), IsEqual.equalTo("BBP7N33GE8"));
        Assert.assertThat(peter.getFirstName(), IsEqual.equalTo("Peter"));
        Assert.assertThat(peter.getLastName(), IsEqual.equalTo("Parker"));
        final List<Account> accounts = peter.getAccounts();
        Assert.assertThat(accounts, IsNull.notNullValue());
        Assert.assertThat(accounts.size(), IsEqual.equalTo(1));
        Assert.assertThat(accounts.get(0).getId(), IsEqual.equalTo("6KSO7HVS8M"));
        Assert.assertThat(accounts.get(0).getName(), IsEqual.equalTo("Stock Account"));
        Assert.assertThat(accounts.get(0).getBalance(), Matchers.closeTo(23_214.45D, 1E-6));
    }

    @Test(expected = Exception.class)
    public void lookupNonExistingUser() {
        final User user = target("/v1/user/non-existing-user").request().get(User.class);
    }

    @Test(expected = Exception.class)
    public void lookupNonExistingAccount() {
        final Account account = target("/v1/account/non-existing-account").request().get(Account.class);
    }

    @Test
    public void checkAddingNewAccount() {
        User mickeyMouse = target("/v1/user/W0NIR0CQT6").request().get(User.class);
        Assert.assertThat(mickeyMouse.getId(), IsEqual.equalTo("W0NIR0CQT6"));
        Assert.assertThat(mickeyMouse.getFirstName(), IsEqual.equalTo("Mickey"));
        Assert.assertThat(mickeyMouse.getLastName(), IsEqual.equalTo("Mouse"));
        List<Account> accounts = mickeyMouse.getAccounts();
        Assert.assertThat(accounts, IsNull.notNullValue());
        Assert.assertThat(accounts.size(), IsEqual.equalTo(2));
        Assert.assertThat(accounts.get(0).getId(), IsEqual.equalTo("JWXAO5FD86"));
        Assert.assertThat(accounts.get(1).getId(), IsEqual.equalTo("6COT2XJFKQ"));

        final Account creditAccount = new Account();
        creditAccount.setId("YXTAZQ3IMZ");
        creditAccount.setName("Secure Credit Account");
        creditAccount.setBalance(2000.00D);
        creditAccount.setCreated(new Date());
        creditAccount.setLastModified(new Date());

        final String status = target("/v1/user/W0NIR0CQT6/account/YXTAZQ3IMZ")
                .request()
                .post(Entity.entity(creditAccount, MediaType.APPLICATION_JSON_TYPE), String.class);
        Assert.assertThat(status, IsEqual.equalTo("{\"value\":\"OK\"}"));
        mickeyMouse = target("/v1/user/W0NIR0CQT6").request().get(User.class);
        accounts = mickeyMouse.getAccounts();
        Assert.assertThat(accounts, IsNull.notNullValue());
        Assert.assertThat(accounts.size(), IsEqual.equalTo(3));
        Assert.assertThat(accounts.get(0).getId(), IsEqual.equalTo("JWXAO5FD86"));
        Assert.assertThat(accounts.get(1).getId(), IsEqual.equalTo("6COT2XJFKQ"));
        Assert.assertThat(accounts.get(2).getId(), IsEqual.equalTo("YXTAZQ3IMZ"));
    }

    @Test
    public void sampleTransfer() {
        Account mickeyDeposit = target("/v1/account/JWXAO5FD86").request().get(Account.class);
        Assert.assertThat(mickeyDeposit.getId(), IsEqual.equalTo("JWXAO5FD86"));
        Assert.assertThat(mickeyDeposit.getName(), IsEqual.equalTo("Deposit Account"));
        Assert.assertThat(mickeyDeposit.getBalance(), Matchers.closeTo(78_234.12, 1E-6));

        Account bruceSavings = target("/v1/account/ZW4LIHK67K").request().get(Account.class);
        Assert.assertThat(bruceSavings.getId(), IsEqual.equalTo("ZW4LIHK67K"));
        Assert.assertThat(bruceSavings.getName(), IsEqual.equalTo("Savings Account"));
        Assert.assertThat(bruceSavings.getBalance(), Matchers.closeTo(43_123.21, 1E-6));

        Transfer transfer = new Transfer();
        transfer.setFromAccountId(mickeyDeposit.getId());
        transfer.setToAccountId(bruceSavings.getId());
        transfer.setAmount(1_549.12D);
        String status = target("/v1/transfer")
                .request()
                .post(Entity.entity(transfer, MediaType.APPLICATION_JSON_TYPE), String.class);
        Assert.assertThat(status, IsEqual.equalTo("{\"value\":\"OK\"}"));

        mickeyDeposit = target("/v1/account/JWXAO5FD86").request().get(Account.class);
        Assert.assertThat(mickeyDeposit.getBalance(), Matchers.closeTo(76_685D, 1E-6));

        bruceSavings = target("/v1/account/ZW4LIHK67K").request().get(Account.class);
        Assert.assertThat(bruceSavings.getBalance(), Matchers.closeTo(44_672.33D, 1E-6));
    }

}