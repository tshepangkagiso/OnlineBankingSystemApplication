import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth-service';
import { Client } from '../../interfaces/Client';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClientRegister } from '../../interfaces/ClientRegister';

@Component({
  selector: 'app-register',
  imports: [CommonModule, FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register 
{
  private authService = inject(AuthService);
  private client! : Client;
  public request: ClientRegister = {accountHolder:'', password:'', email:''}

  onSubmit()
  {
    this.authService.register(this.request).subscribe({
      next: (res) =>
      {
        console.log('success: ' + res);
        alert('successful registration')
        this.client = res;
        console.log(this.client);
      },
      error: (err)=>{
        console.log('failed: ');
        alert('failed to registration')
        console.log(err)
      }
    })
  }

}
