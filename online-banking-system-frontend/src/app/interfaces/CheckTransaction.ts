import Decimal from "decimal.js";

export interface CheckTransaction
{
    checkAccountId: number,
    amount: Decimal
}