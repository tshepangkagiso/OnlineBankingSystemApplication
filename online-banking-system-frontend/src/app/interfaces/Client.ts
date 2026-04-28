import { ROLE } from "../enums/ROLE";
import { CheckAccount } from "./CheckAccount";
import { SavingsAccount } from "./SavingsAccount";
import { UUID } from "./UUID";

export interface Client
{
    accountNumber: UUID,
    accountHolder: string,
    password: string
    email: string,
    savingsAccount: SavingsAccount,
    checkAccount: CheckAccount ,
    roles: ROLE[]; 
}