package com.devteam.online_banking_system_backend.persistence.dtos.savingsAccountDtos;

import java.math.BigDecimal;

public class SavingsTransactionDto
{
    private Long savingsAccountId;
    private BigDecimal amount;

    public SavingsTransactionDto(){}
    public SavingsTransactionDto(Long savingsAccountId, BigDecimal amount)
    {
        this.setSavingsAccountId(savingsAccountId);
        this.setAmount(amount);
    }

    public Long getSavingsAccountId(){return this.savingsAccountId;}
    public void setSavingsAccountId(Long savingsAccountId){this.savingsAccountId = savingsAccountId;}

    public BigDecimal getAmount(){return this.amount;}
    public void setAmount(BigDecimal amount) {this.amount = amount;}

}
