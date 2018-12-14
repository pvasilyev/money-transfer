package com.money.transfer.service;

import com.money.transfer.api.dto.Transfer;

public interface BankService {

    void transferMoney(Transfer transfer);

}
