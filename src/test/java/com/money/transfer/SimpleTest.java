package com.money.transfer;

import com.money.transfer.api.BankResource;
import com.money.transfer.api.dto.User;
import com.money.transfer.dao.BankDao;
import com.money.transfer.dao.InMemoryBankDao;
import com.money.transfer.service.BankService;
import com.money.transfer.service.BankServiceImpl;
import org.glassfish.hk2.api.Immediate;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class SimpleTest extends JerseyTest {

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
        return new ResourceConfig(BankResource.class)
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
        final String hello = target("status").request().get(String.class);
        Assert.assertEquals("{\"value\":\"OK\"}", hello);
    }

    @Test
    public void moreSilly() {
        final User user = target("user/ZJFCHF2539").request().get(User.class);
        Assert.assertEquals("Clark", user.getFirstName());
    }
}