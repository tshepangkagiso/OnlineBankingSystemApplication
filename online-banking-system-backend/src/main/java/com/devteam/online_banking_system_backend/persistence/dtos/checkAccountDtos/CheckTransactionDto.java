package com.devteam.online_banking_system_backend.persistence.dtos.checkAccountDtos;

public class CheckTransactionDto
{
    private Long checkAccountId;
    private Double amount;

    public CheckTransactionDto(){}
    public CheckTransactionDto(Long checkAccountId, Double amount)
    {
        this.setCheckAccountId(checkAccountId);
        this.setAmount(amount);
    }

    public Long getCheckAccountId(){return this.checkAccountId;}
    public void setCheckAccountId(Long checkAccountId){this.checkAccountId = checkAccountId;}

    public Double getAmount(){return this.amount; }
    public void setAmount(Double amount){ this.amount = amount;}
}
