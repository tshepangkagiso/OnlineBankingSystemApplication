import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { baseUrl } from './apiRoute';
import { Observable } from 'rxjs';
import { Client } from '../interfaces/Client';
import { OpenAccount } from '../interfaces/OpenAccount';

@Injectable({
  providedIn: 'root',
})
export class ClientService 
{
  private httpClient = inject(HttpClient);
  private clientsUrl = baseUrl + '/clients/';

  //get client by email - /{email}
  getClientByEmail(email:String): Observable<Client>
  {
    return this.httpClient.get<Client>( this.clientsUrl + email)
  }

  //open savings - account /open/savings
  openSavingsAccount(account: OpenAccount): Observable<any> 
  {
    return this.httpClient.put(this.clientsUrl + "open/savings", account);
  }

  //open check account - /open/check
  openCheckAccount(account: OpenAccount): Observable<any> 
  {
    return this.httpClient.put(this.clientsUrl + "open/check", account);
  }

}
