import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { baseUrl } from './apiRoute';
import { AuthRequest } from '../interfaces/AuthRequest';
import { AuthResponse } from '../interfaces/AuthResponse';
import { Observable } from 'rxjs';
import { ClientRegister } from '../interfaces/ClientRegister';
import { Client } from '../interfaces/Client';

@Injectable({
  providedIn: 'root',
})
export class AuthService 
{
  private httpClient = inject(HttpClient);
  private api = baseUrl + '/auth/'; 

  login(request: AuthRequest): Observable<AuthResponse>
  {
    return this.httpClient.post<AuthResponse>(this.api+'login', request);
  }

  register(request: ClientRegister): Observable<Client>
  {
    return this.httpClient.post<Client>(this.api+"register", request);
  }

}
