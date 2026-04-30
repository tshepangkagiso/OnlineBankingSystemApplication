import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth-service';
import { Client } from '../../interfaces/Client';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClientRegister } from '../../interfaces/ClientRegister';
import { Router, RouterLink } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { 
  faArrowLeft, 
  faUserPlus, 
  faEnvelope,
  faLock,
  faUser,
  faTimes,
  faCheckCircle,
  faExclamationCircle
} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-register',
  imports: [CommonModule, FormsModule, RouterLink, FontAwesomeModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private authService = inject(AuthService);
  private router = inject(Router);
  
  public client!: Client;
  public request: ClientRegister = { accountHolder: '', password: '', email: '' };
  public errorMsg: string = '';
  public isLoading: boolean = false;
  
  // Modal properties
  public showModal: boolean = false;
  public modalMessage: string = '';
  public modalTitle: string = '';
  public modalType: 'success' | 'error' = 'success';
  
  // Icons
  faArrowLeft = faArrowLeft;
  faUserPlus = faUserPlus;
  faEnvelope = faEnvelope;
  faLock = faLock;
  faUser = faUser;
  faTimes = faTimes;
  faCheckCircle = faCheckCircle;
  faExclamationCircle = faExclamationCircle;

  onSubmit() {
    this.isLoading = true;
    this.errorMsg = '';
    
    this.authService.register(this.request).subscribe({
      next: (res) => {
        this.client = res;
        this.showSuccessModal('Registration Successful!', `Welcome to iNOVA Bank, ${this.client.accountHolder}! Please login to continue.`);
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err) => {
        this.isLoading = false;
        this.showErrorModal('Registration Failed', err.error?.error || 'Unable to create account. Please check your information and try again.');
      }
    });
  }

  showSuccessModal(title: string, message: string) {
    this.modalTitle = title;
    this.modalMessage = message;
    this.modalType = 'success';
    this.showModal = true;
    setTimeout(() => {
      this.closeModal();
    }, 2000);
  }

  showErrorModal(title: string, message: string) {
    this.modalTitle = title;
    this.modalMessage = message;
    this.modalType = 'error';
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    this.modalMessage = '';
    this.modalTitle = '';
  }

  goBack() {
    this.router.navigate(['/']);
  }
}
