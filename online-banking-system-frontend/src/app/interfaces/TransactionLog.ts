import Decimal from "decimal.js";
import { ACCOUNTTYPE } from "../enums/ACCOUNTTYPE";
import { TRANSACTIONTYPE } from "../enums/TRANSACTIONTYPE";

export interface TransactionLog
{
    transactionLogId: number,
    transactionDatetime: Date,
    clientEmail: string,
    accountType: ACCOUNTTYPE;
    transactionType: TRANSACTIONTYPE;
    preTransactionBalance: Decimal;
    postTransactionBalance: Decimal;
}