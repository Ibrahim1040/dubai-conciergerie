// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { BookingsComponent } from './pages/bookings/bookings';

export const routes: Routes = [
  { path: 'bookings', component: BookingsComponent },
  { path: '', redirectTo: 'bookings', pathMatch: 'full' },
  { path: '**', redirectTo: 'bookings' }
];


