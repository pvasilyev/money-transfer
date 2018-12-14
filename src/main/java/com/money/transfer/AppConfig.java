package com.money.transfer;

import com.money.transfer.dao.BankDao;
import com.money.transfer.dao.InMemoryBankDao;
import com.money.transfer.service.BankService;
import com.money.transfer.service.BankServiceImpl;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class AppConfig extends ResourceConfig {

    @Inject
    public AppConfig(ServiceLocator locator) {
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(InMemoryBankDao.class).to(BankDao.class).in(Singleton.class);
                bind(BankServiceImpl.class).to(BankService.class).in(Singleton.class);
            }
        });
        packages(true, "io.swagger.jaxrs.json;io.swagger.jaxrs.listing");
        ServiceLocatorUtilities.enableImmediateScope(locator);
        packages(true, "com.money.transfer.api");
    }

}