import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { baseUrl } from './apiRoute';

@Injectable({
  providedIn: 'root',
})
export class ClientService 
{
  private httpClient = inject(HttpClient);
  private clientsUrl = baseUrl + '/clients/';
}
