package com.money.transfer.api.dto;

import com.google.common.base.MoreObjects;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement
public class Transfer {

    @NotNull
    private String fromAccountId;
    @NotNull
    private String toAccountId;
    @NotNull
    private Double amount;

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transfer transfer = (Transfer) o;
        return Objects.equals(fromAccountId, transfer.fromAccountId) &&
                Objects.equals(toAccountId, transfer.toAccountId) &&
                Objects.equals(amount, transfer.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromAccountId, toAccountId, amount);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("fromAccountId", fromAccountId)
                .add("toAccountId", toAccountId)
                .add("amount", amount)
                .toString();
    }
}
