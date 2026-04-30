import { inject, Injectable } from '@angular/core';
import {CanActivate,Router } from '@angular/router';
import { AuthService } from './auth-service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuardService implements CanActivate
{
  private router = inject(Router);
  private authService = inject(AuthService);

canActivate(): boolean {
    if (this.authService.isLoggedIn()) 
    {
      return true;
    } 
    else 
    { 
      alert('Please login first. If you don\'t have an account, please register.');
      this.router.navigate(['/']);
      return false;
    }
  }
  
}
