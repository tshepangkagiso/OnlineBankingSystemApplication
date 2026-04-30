import { Routes } from '@angular/router';
import { Dashboard } from './components/dashboard/dashboard';
import { Login } from './components/login/login';
import { Register } from './components/register/register';
import { AuthGuardService } from './services/auth-guard-service';
import { TransactionLogs } from './components/transaction-logs/transaction-logs';
import { Home } from './components/home/home';

export const routes: Routes = [
    { path: '', component: Home, title: 'iNOVA Bank - Home' },
    { path: 'login', component: Login, title: 'Sign In - iNOVA Bank' },
    { path: 'register', component: Register, title: 'Open Account - iNOVA Bank' },
    { path: 'dashboard', component: Dashboard, title: 'Dashboard - iNOVA Bank', canActivate: [AuthGuardService] },
    { path: 'transactions', component: TransactionLogs, title: 'Transaction History - iNOVA Bank', canActivate: [AuthGuardService] }
];
