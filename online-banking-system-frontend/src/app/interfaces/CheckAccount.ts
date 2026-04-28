import Decimal from "decimal.js";

export interface CheckAccount
{
    balance: Decimal,
    checkAccountId: number,
    overDraftLimit: Decimal
}