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

/**
 * Extension of the {@link ResourceConfig} which aims to bind together some services (interfaces) with corresponding
 * implementation. Works using HK2 as a lightweight DI framework.
 * <p>>
 * Class can be amended if additional packages for scanning will be needed. Or if additional services with corresponding
 * implementations will arise.
 *
 * @author pvasilyev
 */
@ApplicationPath("/api")
public class ApplicationConfig extends ResourceConfig {

    @Inject
    public ApplicationConfig(ServiceLocator locator) {
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