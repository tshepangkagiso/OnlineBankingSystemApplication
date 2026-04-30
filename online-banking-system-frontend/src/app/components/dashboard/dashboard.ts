import { Component, inject, OnInit } from '@angular/core';
import { Client } from '../../interfaces/Client';
import { Cookie } from '../../interfaces/Cookie';
import { CookieService } from '../../services/cookie-service';
import { CommonModule } from '@angular/common';
import { CheckAccountService } from '../../services/check-account-service';
import { ClientService } from '../../services/client-service';
import { SavingsAccountService } from '../../services/savings-account-service';
import { OpenAccount } from '../../interfaces/OpenAccount';
import { CheckTransaction } from '../../interfaces/CheckTransaction';
import { OverdraftToggle } from '../../interfaces/OverdraftToggle';
import { SavingsTransaction } from '../../interfaces/SavingsTransaction';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { 
  faUserCircle, 
  faEnvelope, 
  faIdCard,
  faMoneyBillWave,
  faCreditCard,
  faArrowUp,
  faArrowDown,
  faWallet,
  faChartLine,
  faShieldAlt,
  faToggleOn,
  faToggleOff,
  faTimes,
  faCheckCircle,
  faExclamationCircle,
  faSpinner,
  faBank,
  faCoins,
  faPercent,
  faCalendarAlt,
  faPlusCircle,
  faMinusCircle,
  faSlidersH,
  faLock,
  faUnlockAlt
} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, FormsModule, FontAwesomeModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {
  private cookieService = inject(CookieService);
  private clientService = inject(ClientService);
  private savingsService = inject(SavingsAccountService);
  private checkService = inject(CheckAccountService);

  public client: Client | null = null;
  public isLoading: boolean = false;
  public errorMsg: string = '';
  
  // Use number for UI, convert to Decimal for backend
  public savingsAmount: number = 0;
  public checkAmount: number = 0;
  public overdraftLimit: number = 0;
  
  public overdraftToggle: OverdraftToggle = {
    checkAccountId: 0,
    toggle: false
  };

  // Modal properties
  public showModal: boolean = false;
  public modalMessage: string = '';
  public modalTitle: string = '';
  public modalType: 'success' | 'error' | 'info' = 'success';
  public modalAction: string = '';

  // Icons
  faUserCircle = faUserCircle;
  faEnvelope = faEnvelope;
  faIdCard = faIdCard;
  faMoneyBillWave = faMoneyBillWave;
  faCreditCard = faCreditCard;
  faArrowUp = faArrowUp;
  faArrowDown = faArrowDown;
  faWallet = faWallet;
  faChartLine = faChartLine;
  faShieldAlt = faShieldAlt;
  faToggleOn = faToggleOn;
  faToggleOff = faToggleOff;
  faTimes = faTimes;
  faCheckCircle = faCheckCircle;
  faExclamationCircle = faExclamationCircle;
  faSpinner = faSpinner;
  faBank = faBank;
  faCoins = faCoins;
  faPercent = faPercent;
  faCalendarAlt = faCalendarAlt;
  faPlusCircle = faPlusCircle;
  faMinusCircle = faMinusCircle;
  faSlidersH = faSlidersH;
  faLock = faLock;
  faUnlockAlt = faUnlockAlt;

  ngOnInit(): void {
    const cookie = this.cookieService.onReturnCookie();
    if (cookie && cookie.email) {
      this.loadClientData(cookie.email as string);
    } else {
      this.showErrorModal('Session Expired', 'Please login again to continue.');
    }
  }

  loadClientData(email: string): void {
    this.isLoading = true;
    this.errorMsg = '';
    
    this.clientService.getClientByEmail(email).subscribe({
      next: (res) => {
        this.client = res;
        this.isLoading = false;
        
        if (this.client?.savingsAccount) {
          this.overdraftToggle.checkAccountId = this.client.checkAccount?.checkAccountId || 0;
          this.overdraftToggle.toggle = Number(this.client.checkAccount?.overdraftLimit || 0) > 0;
        }
      },
      error: (err) => {
        console.error('Error loading client:', err);
        this.isLoading = false;
        this.showErrorModal('Loading Failed', 'Unable to load your account data. Please refresh the page.');
      }
    });
  }

  showSuccessModal(title: string, message: string, action: string = '') {
    this.modalTitle = title;
    this.modalMessage = message;
    this.modalType = 'success';
    this.modalAction = action;
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
    this.modalAction = '';
  }

  // Savings Account Methods
  onSavingsDeposit(): void {
    if (!this.client?.email) {
      this.showErrorModal('Error', 'Client email not found');
      return;
    }
    
    if (this.savingsAmount <= 0) {
      this.showErrorModal('Invalid Amount', 'Please enter a valid amount to deposit.');
      return;
    }
    
    const transaction: SavingsTransaction = {
      savingsAccountId: this.client.savingsAccount?.savingsAccountId || 0,
      amount: this.savingsAmount as any
    };
    
    this.isLoading = true;
    this.savingsService.deposit(this.client.email, transaction).subscribe({
      next: (res) => {
        this.loadClientData(this.client!.email);
        this.savingsAmount = 0;
        this.showSuccessModal('Deposit Successful', `R${transaction.amount} has been added to your Savings Account.`, 'deposit');
      },
      error: (err) => {
        this.isLoading = false;
        this.showErrorModal('Deposit Failed', err.error?.error || 'Unable to complete deposit. Please try again.');
      }
    });
  }

  onSavingsWithdraw(): void {
    if (!this.client?.email) {
      this.showErrorModal('Error', 'Client email not found');
      return;
    }
    
    if (this.savingsAmount <= 0) {
      this.showErrorModal('Invalid Amount', 'Please enter a valid amount to withdraw.');
      return;
    }
    
    if (this.savingsAmount > Number(this.client.savingsAccount?.balance || 0)) 
    {
      this.showErrorModal('Insufficient Funds', `Your current balance is R${this.client.savingsAccount?.balance}. Please enter a lower amount.`);
      return;
    }
    
    const transaction: SavingsTransaction = {
      savingsAccountId: this.client.savingsAccount?.savingsAccountId || 0,
      amount: this.savingsAmount as any
    };
    
    this.isLoading = true;
    this.savingsService.withdraw(this.client.email, transaction).subscribe({
      next: (res) => {
        this.loadClientData(this.client!.email);
        this.savingsAmount = 0;
        this.showSuccessModal('Withdrawal Successful', `R${transaction.amount} has been withdrawn from your Savings Account.`, 'withdraw');
      },
      error: (err) => {
        this.isLoading = false;
        this.showErrorModal('Withdrawal Failed', err.error?.error || 'Unable to complete withdrawal. Please try again.');
      }
    });
  }

  // Check Account Methods
  onCheckDeposit(): void {
    if (!this.client?.email) {
      this.showErrorModal('Error', 'Client email not found');
      return;
    }
    
    if (this.checkAmount <= 0) {
      this.showErrorModal('Invalid Amount', 'Please enter a valid amount to deposit.');
      return;
    }
    
    const transaction: CheckTransaction = {
      checkAccountId: this.client.checkAccount?.checkAccountId || 0,
      amount: this.checkAmount as any
    };
    
    this.isLoading = true;
    this.checkService.deposit(this.client.email, transaction).subscribe({
      next: (res) => {
        this.loadClientData(this.client!.email);
        this.checkAmount = 0;
        this.showSuccessModal('Deposit Successful', `R${transaction.amount} has been added to your Check Account.`, 'deposit');
      },
      error: (err) => {
        this.isLoading = false;
        this.showErrorModal('Deposit Failed', err.error?.error || 'Unable to complete deposit. Please try again.');
      }
    });
  }

  onCheckWithdraw(): void {
    if (!this.client?.email) {
      this.showErrorModal('Error', 'Client email not found');
      return;
    }
    
    if (this.checkAmount <= 0) {
      this.showErrorModal('Invalid Amount', 'Please enter a valid amount to withdraw.');
      return;
    }
    
    const currentBalance = Number(this.client.checkAccount?.balance || 0);
    const overdraftLimit = Number(this.client.checkAccount?.overdraftLimit || 0);
    const availableFunds = currentBalance + overdraftLimit;
    
    if (this.checkAmount > availableFunds) {
      this.showErrorModal('Insufficient Funds', `Your available funds (balance + overdraft) is R${availableFunds}. Please enter a lower amount.`);
      return;
    }
    
    const transaction: CheckTransaction = {
      checkAccountId: this.client.checkAccount?.checkAccountId || 0,
      amount: this.checkAmount as any
    };
    
    this.isLoading = true;
    this.checkService.withdraw(this.client.email, transaction).subscribe({
      next: (res) => {
        this.loadClientData(this.client!.email);
        this.checkAmount = 0;
        this.showSuccessModal('Withdrawal Successful', `R${transaction.amount} has been withdrawn from your Check Account.`, 'withdraw');
      },
      error: (err) => {
        this.isLoading = false;
        this.showErrorModal('Withdrawal Failed', err.error?.error || 'Unable to complete withdrawal. Please try again.');
      }
    });
  }

  onSetOverdraft(): void {
    if (!this.client?.email) {
      this.showErrorModal('Error', 'Client email not found');
      return;
    }
    
    if (this.overdraftLimit <= 0) {
      this.showErrorModal('Invalid Limit', 'Please enter a valid overdraft limit amount.');
      return;
    }
    
    const transaction: CheckTransaction = {
      checkAccountId: this.client.checkAccount?.checkAccountId || 0,
      amount: this.overdraftLimit as any
    };
    
    this.isLoading = true;
    this.checkService.overdraft(this.client.email, transaction).subscribe({
      next: (res) => {
        this.loadClientData(this.client!.email);
        this.overdraftLimit = 0;
        this.showSuccessModal('Overdraft Limit Updated', `Your new overdraft limit is R${transaction.amount}.`, 'overdraft');
      },
      error: (err) => {
        this.isLoading = false;
        this.showErrorModal('Update Failed', err.error?.error || 'Unable to set overdraft limit. Please try again.');
      }
    });
  }

  onToggleOverdraft(): void {
    if (!this.client?.email) {
      this.showErrorModal('Error', 'Client email not found');
      return;
    }
    
    this.isLoading = true;
    this.checkService.toggleOverdraft(this.client.email, this.overdraftToggle).subscribe({
      next: (res) => {
        this.loadClientData(this.client!.email);
        const status = this.overdraftToggle.toggle ? 'enabled' : 'disabled';
        this.showSuccessModal('Overdraft Updated', `Overdraft protection has been ${status}.`, 'toggle');
      },
      error: (err) => {
        this.isLoading = false;
        this.showErrorModal('Update Failed', err.error?.error || 'Unable to toggle overdraft. Please try again.');
      }
    });
  }

  onOpenSavings(): void {
    if (!this.client?.email) {
      this.showErrorModal('Error', 'Client email not found');
      return;
    }
    
    const openAccount: OpenAccount = { email: this.client.email };
    
    this.isLoading = true;
    this.clientService.openSavingsAccount(openAccount).subscribe({
      next: () => {
        this.loadClientData(this.client!.email);
        this.showSuccessModal('Account Opened', 'Your Savings Account has been successfully opened! Start saving today.', 'open');
      },
      error: (err) => {
        this.isLoading = false;
        this.showErrorModal('Failed to Open Account', err.error?.error || 'Unable to open savings account. Please try again.');
      }
    });
  }

  onOpenCheck(): void {
    if (!this.client?.email) {
      this.showErrorModal('Error', 'Client email not found');
      return;
    }
    
    const openAccount: OpenAccount = { email: this.client.email };
    
    this.isLoading = true;
    this.clientService.openCheckAccount(openAccount).subscribe({
      next: () => {
        this.loadClientData(this.client!.email);
        this.showSuccessModal('Account Opened', 'Your Check Account has been successfully opened! Enjoy flexible banking.', 'open');
      },
      error: (err) => {
        this.isLoading = false;
        this.showErrorModal('Failed to Open Account', err.error?.error || 'Unable to open check account. Please try again.');
      }
    });
  }


  formatCurrency(amount: any): string {
    const numericAmount = Number(amount || 0);
    return new Intl.NumberFormat('en-ZA', { style: 'currency', currency: 'ZAR' }).format(numericAmount);
  }

  getTotalBalance(): number {
    const savingsBalance = Number(this.client?.savingsAccount?.balance || 0);
    const checkBalance = Number(this.client?.checkAccount?.balance || 0);
    return savingsBalance + checkBalance;
  }

    isOverdraftEnabled(): boolean {
    return Number(this.client?.checkAccount?.overdraftLimit || 0) > 0;
  }
}