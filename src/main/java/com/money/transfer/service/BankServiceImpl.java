package com.money.transfer.service;

import com.money.transfer.api.dto.Account;
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
        final String fromAccountId = transfer.getFromAccountId();
        final Account from = bankDao.findAccount(fromAccountId);

        final String toAccountId = transfer.getToAccountId();
        final Account to = bankDao.findAccount(toAccountId);

        if (fromAccountId.compareTo(toAccountId) < 0) {
            synchronized (from) {
                synchronized (to) {
                    transfer(from, to, transfer);
                }
            }
        } else {
            synchronized (to) {
                synchronized (from) {
                    transfer(from, to, transfer);
                }
            }
        }
    }

    private void transfer(Account from, Account to, Transfer transfer) {
        final double transferAmount = transfer.getAmount();
        from.setBalance(from.getBalance() - transferAmount);
        to.setBalance(to.getBalance() + transferAmount);
    }

}
