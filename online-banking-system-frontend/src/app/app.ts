import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { AuthService } from './services/auth-service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faBuildingColumns, faHouse, faClockRotateLeft, faRightFromBracket } from '@fortawesome/free-solid-svg-icons';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, CommonModule, FontAwesomeModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  private authService = inject(AuthService);
  faBuildingColumns = faBuildingColumns;
  faHouse = faHouse;
  faClockRotateLeft = faClockRotateLeft;
  faRightFromBracket = faRightFromBracket;

  isNavigate(): Boolean {
    return this.authService.isLoggedIn();
  }

  onLogout() {
    this.authService.logout();
  }

}