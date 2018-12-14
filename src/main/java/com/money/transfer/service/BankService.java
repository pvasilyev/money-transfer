package com.money.transfer.service;

import com.money.transfer.api.dto.Transfer;

/**
 * Simple Bank Service which currently supports operation of money-transferring.
 * It can be extended with additional behavior as well whenever needed.
 *
 * @author pvasilyev
 */
public interface BankService {

    /**
     * Simple method for transferring money. In current model this method doesn't return
     * any value, which means operations should always succeed. Even though there is no sufficient amount in balance
     * it will succeed, and presumably the account balance will go negative (like credit account).
     *
     * @param transfer holds comprehensive information about accounts credentials that are required for transfer itself.
     */
    void transferMoney(Transfer transfer);

}
