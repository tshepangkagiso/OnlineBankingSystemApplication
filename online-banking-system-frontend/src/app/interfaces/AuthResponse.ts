import type {UUID} from "./UUID";

export interface AuthResponse
{
    accountNumber: UUID,
    accountHolder: string,
    email: string,
    token: string
}