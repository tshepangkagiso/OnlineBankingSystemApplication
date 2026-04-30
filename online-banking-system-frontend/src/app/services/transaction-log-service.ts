import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { baseUrl } from './apiRoute';
import { ACCOUNTTYPE } from '../enums/ACCOUNTTYPE';
import { TransactionLog } from '../interfaces/TransactionLog';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TransactionLogService 
{
  private httpClient = inject(HttpClient);
  private transactionLogUrl = baseUrl + '/transactionlogs/'; 

  // Get full history by email - /history/{email} -lists
  getHistoryByEmail(email: string): Observable<TransactionLog[]>
  {
    return this.httpClient.get<TransactionLog[]>(this.transactionLogUrl + `history/${email}`);
  }

  // Get history filtered by account type (CHECKACCOUNT / SAVINGSACCOUNT) - /history/{email}/{type} -lists
  getHistoryByAccountType(email: string, type: ACCOUNTTYPE): Observable<TransactionLog[]> {
    const typeString = type === ACCOUNTTYPE.SAVINGSACCOUNT ? 'SAVINGSACCOUNT' : 'CHECKACCOUNT';
    return this.httpClient.get<TransactionLog[]>(this.transactionLogUrl + `history/${email}/${typeString}`);
  }

  // Get recent transactions (e.g., top 5 or 10) - /recent/{email} -lists
  getRecentTransactions(email: string): Observable<TransactionLog[]>
  {
    return this.httpClient.get<TransactionLog[]>(this.transactionLogUrl + `recent/${email}`);
  }


  // Find transactions within a specific date range
  getTransactionsByDateRange(start: string, end: string): Observable<TransactionLog[]> {
    return this.httpClient.get<TransactionLog[]>(this.transactionLogUrl + `admin/range?start=${start}&end=${end}`);
  }

  // Find transactions by type (DEPOSIT, WITHDRAWAL, etc.)
  getTransactionsByType(type: string): Observable<TransactionLog[]> {
    return this.httpClient.get<TransactionLog[]>(this.transactionLogUrl + `admin/type/${type}`);
  }

  // Find transactions exceeding a specific amount
  getHighValueTransactions(threshold: number): Observable<TransactionLog[]> {
    return this.httpClient.get<TransactionLog[]>(this.transactionLogUrl + `admin/high-value?threshold=${threshold}`);
  }
}
