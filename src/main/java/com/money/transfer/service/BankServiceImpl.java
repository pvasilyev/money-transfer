package com.money.transfer.service;

import com.money.transfer.api.dto.Account;
import com.money.transfer.api.dto.Transfer;
import com.money.transfer.dao.BankDao;

import javax.inject.Inject;
import java.util.Date;

/**
 * Straight-forward implementation of {@link BankService}.
 * <p>
 * Note: the implementation of bank-transferring logic relies on the fact that all objects are stored in RAM.
 *
 * @author pvasilyev
 */
public class BankServiceImpl implements BankService {

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
        from.setLastModified(new Date());
        to.setLastModified(new Date());
    }

    private void transfer(Account from, Account to, Transfer transfer) {
        final double transferAmount = transfer.getAmount();
        from.setBalance(from.getBalance() - transferAmount);
        to.setBalance(to.getBalance() + transferAmount);
    }

}
