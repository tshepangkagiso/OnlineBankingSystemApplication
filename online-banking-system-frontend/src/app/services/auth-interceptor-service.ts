import { inject, Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cookie } from '../interfaces/Cookie';
import { CookieService } from './cookie-service';

@Injectable({
  providedIn: 'root',
})
export class AuthInterceptorService implements HttpInterceptor {
  private cookieService = inject(CookieService);

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const cookie: Cookie | null = this.cookieService.onReturnCookie();

    // Add null check before accessing .token
    if (cookie && cookie.token) {
      const cloned = req.clone({ 
        setHeaders: { Authorization: `Bearer ${cookie.token}` }
      });
      return next.handle(cloned);
    }
    
    return next.handle(req);
  }
}
