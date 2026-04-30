import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { baseUrl } from './apiRoute';
import { AuthRequest } from '../interfaces/AuthRequest';
import { AuthResponse } from '../interfaces/AuthResponse';
import { Observable } from 'rxjs';
import { ClientRegister } from '../interfaces/ClientRegister';
import { Client } from '../interfaces/Client';
import { CookieService } from './cookie-service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService 
{
  private httpClient = inject(HttpClient);
  private api = baseUrl + '/auth/'; 
  private cookieService = inject(CookieService);
  private router = inject(Router);

  login(request: AuthRequest): Observable<AuthResponse>
  {
    return this.httpClient.post<AuthResponse>(this.api+'login', request);
  }

  register(request: ClientRegister): Observable<Client>
  {
    return this.httpClient.post<Client>(this.api+"register", request);
  }

  isLoggedIn(): Boolean
  {
    return !!this.cookieService.onReturnCookie();
  }

  logout()
  {
    this.cookieService.onDeleteCookie();
    this.router.navigate(['/login']);
  }

}
