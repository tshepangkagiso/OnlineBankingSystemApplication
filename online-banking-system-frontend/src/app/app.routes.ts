import { Routes } from '@angular/router';
import { Dashboard } from './components/dashboard/dashboard';
import { Login } from './components/login/login';
import { Register } from './components/register/register';

export const routes: Routes = [
    {path:'', component: Dashboard, title: 'Dashboard Page'},
    {path:'login', component: Login, title: 'Login Page'},
    {path: 'register', component: Register, title: 'Registration Page'}
];
