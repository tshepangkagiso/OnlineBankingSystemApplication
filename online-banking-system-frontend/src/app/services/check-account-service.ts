import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { baseUrl } from './apiRoute';
import { CheckAccount } from '../interfaces/CheckAccount';
import { Observable } from 'rxjs';
import { CheckTransaction } from '../interfaces/CheckTransaction';
import { OverdraftToggle } from '../interfaces/OverdraftToggle';

@Injectable({
  providedIn: 'root',
})
export class CheckAccountService 
{
  private httpClient = inject(HttpClient);
  private checkAcountUrl = baseUrl + '/checkaccounts/';


  //get by id - /{id}
  getById(id:number): Observable<CheckAccount>
  {
    return this.httpClient.get<CheckAccount>(this.checkAcountUrl + id);
  }

  //deposit - /deposit/{email}
  deposit(email:string, transaction:CheckTransaction): Observable<CheckAccount>
  {
    return this.httpClient.put<CheckAccount>(this.checkAcountUrl +`deposit/${email}`,transaction)
  }

  //withdraw - /withdraw/{email}
  withdraw(email:string, transaction:CheckTransaction): Observable<CheckAccount>
  {
    return this.httpClient.put<CheckAccount>(this.checkAcountUrl +`withdraw/${email}`,transaction)
  }

  //overdraft limit setter - /overdraft/{email}
  overdraft(email:string, transaction:CheckTransaction): Observable<CheckAccount>
  {
    return this.httpClient.put<CheckAccount>(this.checkAcountUrl +`overdraft/${email}`,transaction)
  }

  //overdraft toggle - /toggle/{email}
  toggleOverdraft(email:string, toggle:OverdraftToggle): Observable<CheckAccount>
  {
    return this.httpClient.put<CheckAccount>(this.checkAcountUrl +`toggle/${email}`,toggle)
  }
}
