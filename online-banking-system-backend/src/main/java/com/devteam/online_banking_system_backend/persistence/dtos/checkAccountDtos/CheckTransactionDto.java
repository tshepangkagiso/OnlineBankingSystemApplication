package com.devteam.online_banking_system_backend.persistence.dtos.checkAccountDtos;

import java.math.BigDecimal;

public class CheckTransactionDto
{
    private Long checkAccountId;
    private BigDecimal amount;

    public CheckTransactionDto(){}
    public CheckTransactionDto(Long checkAccountId, BigDecimal amount)
    {
        this.setCheckAccountId(checkAccountId);
        this.setAmount(amount);
    }

    public Long getCheckAccountId(){return this.checkAccountId;}
    public void setCheckAccountId(Long checkAccountId){this.checkAccountId = checkAccountId;}

    public BigDecimal getAmount(){return this.amount; }
    public void setAmount(BigDecimal amount){ this.amount = amount;}
}
