package com.money.transfer.service;

import com.money.transfer.api.dto.Transfer;
import com.money.transfer.dao.BankDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.invoke.MethodHandles;

public class BankServiceImpl implements BankService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Inject
    private BankDao bankDao;

    @Override
    public void transferMoney(Transfer transfer) {
        // todo implement me
    }

}
