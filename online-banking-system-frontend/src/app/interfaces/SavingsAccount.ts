import Decimal from "decimal.js";

export interface SavingsAccount
{
    balance: Decimal,
    savingsAccountId: number,
    interestRate: Decimal,
    latestDepositDate: Date;
}