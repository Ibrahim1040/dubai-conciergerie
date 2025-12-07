// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { BookingsComponent } from './pages/bookings/bookings';
import { PropertiesComponent } from './pages/properties/properties';


export const routes: Routes = [
  { path: 'bookings', component: BookingsComponent },
  { path: 'properties', component: PropertiesComponent },
  { path: '', redirectTo: 'properties', pathMatch: 'full' },
  { path: '**', redirectTo: 'properties' }
  
];



