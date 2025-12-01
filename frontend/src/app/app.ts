import { Component, OnInit } from '@angular/core';
import { BookingService } from './services/booking.service';
import { Booking } from './models/booking.model';
import {RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class AppComponent implements OnInit {

  bookings: Booking[] = [];

  constructor(private bookingService: BookingService) {}

  ngOnInit(): void {
    this.bookingService.getAll().subscribe({
      next: (data:any) => this.bookings = data,
      error: (err:any) => console.error('Erreur chargement bookings', err),
    });
  }
}
