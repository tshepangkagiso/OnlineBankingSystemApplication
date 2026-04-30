import { Injectable } from '@angular/core';
import { Cookie } from '../interfaces/Cookie';

@Injectable({
  providedIn: 'root',
})
export class CookieService 
{
 onSaveCookie(cookie:Cookie)
 {
    sessionStorage.setItem("onlinebankingsystem", JSON.stringify(cookie))
 }
 
 onDeleteCookie()
 {  
    sessionStorage.removeItem("onlinebankingsystem")
 }

  onReturnCookie(){
    const cookieString = sessionStorage.getItem('onlinebankingsystem');
   
    if (!cookieString) {
      return null;
    }
    
    try {
      return JSON.parse(cookieString) as Cookie;
    } catch (e) {
      console.error('Error parsing cookie:', e);
      return null;
    }
  }
}
