import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { 
  faHistory, 
  faFilter, 
  faCalendarAlt, 
  faMoneyBillWave,
  faExchangeAlt,
  faTimes,
  faCheckCircle,
  faExclamationCircle,
  faSpinner,
  faSearch,
  faUndo,
  faArrowUp,
  faArrowDown,
  faBan,
  faClock,
  faPercent,
  faWallet,
  faCreditCard,
  faChartLine,
  faInfoCircle,
  faDownload
} from '@fortawesome/free-solid-svg-icons';
import { ACCOUNTTYPE } from '../../enums/ACCOUNTTYPE';
import { TRANSACTIONTYPE } from '../../enums/TRANSACTIONTYPE';
import { TransactionLog } from '../../interfaces/TransactionLog';
import { CookieService } from '../../services/cookie-service';
import { TransactionLogService } from '../../services/transaction-log-service';

@Component({
  selector: 'app-transaction-logs',
  imports: [CommonModule, FormsModule, FontAwesomeModule, RouterLink],
  templateUrl: './transaction-logs.html',
  styleUrl: './transaction-logs.css',
})
export class TransactionLogs implements OnInit {
  private transactionLogService = inject(TransactionLogService);
  private cookieService = inject(CookieService);
  
  public transactionLogs: TransactionLog[] = [];
  public filteredLogs: TransactionLog[] = [];
  public isLoading: boolean = false;
  public errorMsg: string = '';
  public Math = Math;
  
  // Filters
  public selectedAccountType: string = 'ALL';
  public selectedTransactionType: string = 'ALL';
  public startDate: string = '';
  public endDate: string = '';
  public showRecentOnly: boolean = false;
  public highValueThreshold: number = 1000;
  
  // Modal properties
  public showModal: boolean = false;
  public modalMessage: string = '';
  public modalTitle: string = '';
  public modalType: 'success' | 'error' | 'info' = 'success';
  public modalData: any = null;
  
  // Icons
  faHistory = faHistory;
  faFilter = faFilter;
  faCalendarAlt = faCalendarAlt;
  faMoneyBillWave = faMoneyBillWave;
  faExchangeAlt = faExchangeAlt;
  faTimes = faTimes;
  faCheckCircle = faCheckCircle;
  faExclamationCircle = faExclamationCircle;
  faSpinner = faSpinner;
  faSearch = faSearch;
  faUndo = faUndo;
  faArrowUp = faArrowUp;
  faArrowDown = faArrowDown;
  faBan = faBan;
  faClock = faClock;
  faPercent = faPercent;
  faWallet = faWallet;
  faCreditCard = faCreditCard;
  faChartLine = faChartLine;
  faInfoCircle = faInfoCircle;
  faDownload = faDownload;
  
  // Account types for filter dropdown
  public accountTypes = [
    { value: 'ALL', label: 'All Accounts', icon: faWallet },
    { value: 'SAVINGSACCOUNT', label: 'Savings Account', icon: faChartLine },
    { value: 'CHECKACCOUNT', label: 'Check Account', icon: faCreditCard }
  ];
  
  // Transaction types for filter dropdown
  public transactionTypes = [
    { value: 'ALL', label: 'All Transactions', icon: faExchangeAlt },
    { value: 'DEPOSIT', label: 'Deposit', icon: faArrowUp },
    { value: 'WITHDRAWAL', label: 'Withdrawal', icon: faArrowDown },
    { value: 'APPLIEDINTERESTTOSAVINGSACCOUNT', label: 'Interest Applied', icon: faPercent },
    { value: 'ACCOUNTOPENING', label: 'Account Opening', icon: faWallet },
    { value: 'OVERDRAFTSETTER', label: 'Overdraft Setter', icon: faBan }
  ];

  ngOnInit(): void {
    this.loadTransactionLogs();
  }

  showSuccessModal(title: string, message: string, data?: any) {
    this.modalTitle = title;
    this.modalMessage = message;
    this.modalType = 'success';
    this.modalData = data;
    this.showModal = true;
    setTimeout(() => {
      this.closeModal();
    }, 2500);
  }

  showErrorModal(title: string, message: string) {
    this.modalTitle = title;
    this.modalMessage = message;
    this.modalType = 'error';
    this.showModal = true;
  }

  showInfoModal(title: string, message: string) {
    this.modalTitle = title;
    this.modalMessage = message;
    this.modalType = 'info';
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    this.modalMessage = '';
    this.modalTitle = '';
    this.modalData = null;
  }

  loadTransactionLogs(): void {
    const cookie = this.cookieService.onReturnCookie();
    if (!cookie || !cookie.email) {
      this.showErrorModal('Authentication Error', 'Please login to view transaction history');
      return;
    }
    
    this.isLoading = true;
    this.errorMsg = '';
    
    if (this.showRecentOnly) {
      this.transactionLogService.getRecentTransactions(cookie.email as string).subscribe({
        next: (logs) => {
          this.transactionLogs = logs;
          this.filteredLogs = [...this.transactionLogs];
          this.isLoading = false;
          if (logs.length === 0) {
            this.showInfoModal('No Transactions', 'No recent transactions found.');
          }
        },
        error: (err) => {
          console.error('Error loading recent transactions:', err);
          this.isLoading = false;
          this.showErrorModal('Loading Failed', err.error?.error || 'Failed to load recent transactions');
        }
      });
    } else {
      this.transactionLogService.getHistoryByEmail(cookie.email as string).subscribe({
        next: (logs) => {
          this.transactionLogs = logs;
          this.filteredLogs = [...this.transactionLogs];
          this.isLoading = false;
          if (logs.length === 0) {
            this.showInfoModal('No Transactions', 'No transaction history found.');
          }
        },
        error: (err) => {
          console.error('Error loading transaction history:', err);
          this.isLoading = false;
          this.showErrorModal('Loading Failed', err.error?.error || 'Failed to load transaction history');
        }
      });
    }
  }

  loadByAccountType(accountType: string): void {
    const cookie = this.cookieService.onReturnCookie();
    if (!cookie || !cookie.email) return;
    
    this.isLoading = true;
    this.errorMsg = '';
    
    const typeEnum = ACCOUNTTYPE[accountType as keyof typeof ACCOUNTTYPE];
    
    this.transactionLogService.getHistoryByAccountType(cookie.email as string, typeEnum).subscribe({
      next: (logs) => {
        this.transactionLogs = logs;
        this.filteredLogs = [...this.transactionLogs];
        this.isLoading = false;
        const accountLabel = this.accountTypes.find(t => t.value === accountType)?.label;
        this.showSuccessModal('Filter Applied', `Showing ${logs.length} transactions for ${accountLabel}.`);
      },
      error: (err) => {
        console.error('Error filtering by account type:', err);
        this.isLoading = false;
        this.showErrorModal('Filter Failed', err.error?.error || 'Failed to filter transactions');
      }
    });
  }

  loadTransactionsByDateRange(): void {
    if (!this.startDate || !this.endDate) {
      this.showErrorModal('Invalid Date Range', 'Please select both start and end dates');
      return;
    }
    
    this.isLoading = true;
    this.errorMsg = '';
    
    this.transactionLogService.getTransactionsByDateRange(this.startDate, this.endDate).subscribe({
      next: (logs) => {
        this.transactionLogs = logs;
        this.filteredLogs = [...this.transactionLogs];
        this.isLoading = false;
        
        if (logs.length === 0) {
          this.showInfoModal('No Results', `No transactions found between ${this.startDate} and ${this.endDate}.`);
        } else {
          this.showSuccessModal('Search Complete', `Found ${logs.length} transactions in this date range.`);
        }
      },
      error: (err) => {
        console.error('Error loading transactions by date range:', err);
        this.isLoading = false;
        this.showErrorModal('Search Failed', err.error?.error || 'Failed to load transactions by date range');
      }
    });
  }

  loadTransactionsByType(): void {
    if (this.selectedTransactionType === 'ALL') {
      this.showErrorModal('Invalid Selection', 'Please select a transaction type');
      return;
    }
    
    this.isLoading = true;
    this.errorMsg = '';
    
    this.transactionLogService.getTransactionsByType(this.selectedTransactionType).subscribe({
      next: (logs) => {
        this.transactionLogs = logs;
        this.filteredLogs = [...this.transactionLogs];
        this.isLoading = false;
        const typeLabel = this.transactionTypes.find(t => t.value === this.selectedTransactionType)?.label;
        
        if (logs.length === 0) {
          this.showInfoModal('No Results', `No ${typeLabel} transactions found.`);
        } else {
          this.showSuccessModal('Search Complete', `Found ${logs.length} ${typeLabel} transactions.`);
        }
      },
      error: (err) => {
        console.error('Error loading transactions by type:', err);
        this.isLoading = false;
        this.showErrorModal('Search Failed', err.error?.error || 'Failed to load transactions by type');
      }
    });
  }

  loadHighValueTransactions(): void {
    if (!this.highValueThreshold || this.highValueThreshold <= 0) {
      this.showErrorModal('Invalid Amount', 'Please enter a valid threshold amount');
      return;
    }
    
    this.isLoading = true;
    this.errorMsg = '';
    
    this.transactionLogService.getHighValueTransactions(this.highValueThreshold).subscribe({
      next: (logs) => {
        this.transactionLogs = logs;
        this.filteredLogs = [...this.transactionLogs];
        this.isLoading = false;
        
        if (logs.length === 0) {
          this.showInfoModal('No Results', `No transactions found above R${this.highValueThreshold}. Try a lower amount.`);
        } else {
          this.showSuccessModal('Search Complete', `Found ${logs.length} transactions above R${this.highValueThreshold}.`);
        }
      },
      error: (err) => {
        console.error('Error loading high value transactions:', err);
        this.isLoading = false;
        this.showErrorModal('Search Failed', err.error?.error || 'Failed to load high value transactions');
      }
    });
  }

  onAccountTypeChange(): void {
    if (this.selectedAccountType === 'ALL') {
      this.loadTransactionLogs();
    } else {
      this.loadByAccountType(this.selectedAccountType);
    }
  }

  applyFilters(): void {
    let logs = [...this.transactionLogs];
    let filterCount = 0;
    
    if (this.selectedTransactionType !== 'ALL') {
      logs = logs.filter(log => {
        const typeString = log.transactionType.toString();
        return typeString === this.selectedTransactionType;
      });
      filterCount++;
    }
    
    if (this.startDate) {
      const start = new Date(this.startDate);
      start.setHours(0, 0, 0);
      logs = logs.filter(log => new Date(log.transactionDatetime) >= start);
      filterCount++;
    }
    
    if (this.endDate) {
      const end = new Date(this.endDate);
      end.setHours(23, 59, 59);
      logs = logs.filter(log => new Date(log.transactionDatetime) <= end);
      filterCount++;
    }
    
    this.filteredLogs = logs;
    
    if (filterCount > 0) {
      if (logs.length === 0) {
        this.showInfoModal('No Matches', 'No transactions match your current filters.');
      } else {
        this.showSuccessModal('Filters Applied', `Showing ${logs.length} of ${this.transactionLogs.length} transactions.`);
      }
    }
  }

  resetFilters(): void {
    this.selectedAccountType = 'ALL';
    this.selectedTransactionType = 'ALL';
    this.startDate = '';
    this.endDate = '';
    this.showRecentOnly = false;
    this.highValueThreshold = 1000;
    this.errorMsg = '';
    this.loadTransactionLogs();
    this.showSuccessModal('Filters Reset', 'All filters have been cleared.');
  }

  clearAmountFilter(): void {
    this.highValueThreshold = 1000;
    this.loadTransactionLogs();
    this.showSuccessModal('Filter Cleared', 'Amount filter has been removed.');
  }

  clearTypeFilter(): void {
    this.selectedTransactionType = 'ALL';
    this.applyFilters();
    this.showSuccessModal('Filter Cleared', 'Transaction type filter has been removed.');
  }

  clearDateFilter(): void {
    this.startDate = '';
    this.endDate = '';
    this.applyFilters();
    this.showSuccessModal('Filter Cleared', 'Date range filter has been removed.');
  }

  toggleRecentOnly(): void {
    this.showRecentOnly = !this.showRecentOnly;
    this.loadTransactionLogs();
  }

  getAccountTypeLabel(accountType: ACCOUNTTYPE): string {
    switch(accountType.toString()) {
      case "SAVINGSACCOUNT":
        return 'Savings Account';
      case "CHECKACCOUNT":
        return 'Check Account';
      default:
        return 'Unknown';
    }
  }

  getTransactionTypeLabel(transactionType: TRANSACTIONTYPE): string {
    switch(transactionType.toString()) {
      case "DEPOSIT":
        return 'Deposit';
      case "WITHDRAWAL":
        return 'Withdrawal';
      case "APPLIEDINTERESTTOSAVINGSACCOUNT":
        return 'Interest Applied';
      case "ACCOUNTOPENING":
        return 'Account Opening';
      case "OVERDRAFTSETTER":
        return 'Overdraft Setter';
      default:
        return 'Unknown';
    }
  }

  getTransactionClass(transactionType: TRANSACTIONTYPE): string {
    switch(transactionType.toString()) {
      case "DEPOSIT":
      case "APPLIEDINTERESTTOSAVINGSACCOUNT":
        return 'positive';
      case "WITHDRAWAL":
        return 'negative';
      default:
        return 'neutral';
    }
  }

  getAmountChange(log: TransactionLog): number {
    return Number(log.postTransactionBalance) - Number(log.preTransactionBalance);
  }

  getTransactionIcon(transactionType: TRANSACTIONTYPE): any {
    const typeString = transactionType.toString();
    switch(typeString) {
      case "DEPOSIT":
        return faArrowUp;
      case "WITHDRAWAL":
        return faArrowDown;
      case "APPLIEDINTERESTTOSAVINGSACCOUNT":
        return faPercent;
      case "ACCOUNTOPENING":
        return faWallet;
      case "OVERDRAFTSETTER":
        return faBan;
      default:
        return faExchangeAlt;
    }
  }

  formatCurrency(amount: any): string {
    const numericAmount = Number(amount || 0);
    return new Intl.NumberFormat('en-ZA', { style: 'currency', currency: 'ZAR' }).format(numericAmount);
  }

  formatDate(date: Date): string {
    return new Date(date).toLocaleString();
  }
}