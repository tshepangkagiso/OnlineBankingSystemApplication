import Decimal from "decimal.js";

export interface SavingsTransaction
{
    savingsAccountId: number,
    amount: Decimal
}