import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { baseUrl } from './apiRoute';
import { Observable } from 'rxjs';
import { SavingsAccount } from '../interfaces/SavingsAccount';
import { SavingsTransaction } from '../interfaces/SavingsTransaction';

@Injectable({
  providedIn: 'root',
})
export class SavingsAccountService 
{
  private httpClient = inject(HttpClient);
  private savingsAccountUrl = baseUrl + '/savingsaccounts/';

  //get by id - /{id}
  getById(id:number): Observable<SavingsAccount>
  {
    return this.httpClient.get<SavingsAccount>(this.savingsAccountUrl + id);
  }

  //deposit - /deposit/{email}
  deposit(email:string, transaction: SavingsTransaction) : Observable<SavingsAccount>
  {
    return this.httpClient.put<SavingsAccount>(this.savingsAccountUrl + `deposit/${email}`,transaction);
  }

  //withdraw - /withdraw/{email}
  withdraw(email:string, transaction: SavingsTransaction) : Observable<SavingsAccount>
  {
    return this.httpClient.put<SavingsAccount>(this.savingsAccountUrl + `withdraw/${email}`,transaction);
  }
}
