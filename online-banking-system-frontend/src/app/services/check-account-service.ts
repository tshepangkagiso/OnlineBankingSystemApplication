import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { baseUrl } from './apiRoute';

@Injectable({
  providedIn: 'root',
})
export class CheckAccountService 
{
  private httpClient = inject(HttpClient);
  private checkAcountUrl = baseUrl + '/checkaccounts/';
}
