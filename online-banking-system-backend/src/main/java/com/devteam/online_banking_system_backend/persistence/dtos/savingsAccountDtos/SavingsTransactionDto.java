package com.devteam.online_banking_system_backend.persistence.dtos.savingsAccountDtos;

public class SavingsTransactionDto
{
    private Long savingsAccountId;
    private Double amount;

    public SavingsTransactionDto(){}
    public SavingsTransactionDto(Long savingsAccountId, Double amount)
    {
        this.setSavingsAccountId(savingsAccountId);
        this.setAmount(amount);
    }

    public Long getSavingsAccountId(){return this.savingsAccountId;}
    public void setSavingsAccountId(Long savingsAccountId){this.savingsAccountId = savingsAccountId;}

    public Double getAmount(){return this.amount;}
    public void setAmount(Double amount) {this.amount = amount;}

}
