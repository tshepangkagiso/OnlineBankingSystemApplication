import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth-service';
import { AuthResponse } from '../../interfaces/AuthResponse';
import { AuthRequest } from '../../interfaces/AuthRequest';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login 
{
  private authService = inject(AuthService);
  private response!: AuthResponse;
  public request: AuthRequest =  {password:'', email:''};
  
  onSubmit()
  {
   
    this.authService.login(this.request).subscribe({
      next: (res) =>
      {
        console.log('success: ' + res);
        alert('successful login')
        this.response = res;
        console.log(this.response);
      },
      error: (err)=>{
        console.log('failed: ' + err);
        alert('failed to login')
      }
    })
  }


}
