import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth-service';
import { AuthResponse } from '../../interfaces/AuthResponse';
import { AuthRequest } from '../../interfaces/AuthRequest';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Cookie } from '../../interfaces/Cookie';
import { CookieService } from '../../services/cookie-service';
import { Router, RouterLink } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { 
  faArrowLeft, 
  faUserPlus, 
  faArrowRightToBracket,
  faEnvelope,
  faLock,
  faTimes,
  faCheckCircle,
  faExclamationCircle
} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule, RouterLink, FontAwesomeModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private authService = inject(AuthService);
  private cookieService = inject(CookieService);
  private router = inject(Router);

  private response!: AuthResponse;
  public request: AuthRequest = { password: '', email: '' };
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
  faArrowRightToBracket = faArrowRightToBracket;
  faEnvelope = faEnvelope;
  faLock = faLock;
  faTimes = faTimes;
  faCheckCircle = faCheckCircle;
  faExclamationCircle = faExclamationCircle;

  onSubmit() {
    this.isLoading = true;
    this.errorMsg = '';
    
    this.authService.login(this.request).subscribe({
      next: (res) => {
        this.response = res;
        const cookie: Cookie = {
          token: this.response.token,
          expiresAt: this.response.expiresAt,
          email: this.response.email,
          username: this.response.accountHolder
        };
        //console.log(res.token)
        this.cookieService.onSaveCookie(cookie);
        this.showSuccessModal('Login Successful!', `Welcome back, ${this.response.accountHolder}!`);
        setTimeout(() => {
          this.router.navigate(['/dashboard']);
        }, 1500);
      },
      error: (err) => {
        this.isLoading = false;
        this.showErrorModal('Login Failed', err.error?.error || 'Invalid email or password. Please try again.');
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
