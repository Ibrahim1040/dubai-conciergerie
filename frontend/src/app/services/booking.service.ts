import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Booking } from '../models/booking.model';

@Injectable({
  providedIn: 'root'
})
export class BookingService {

  private baseUrl = 'http://localhost:8080/api/owner/bookings';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Booking[]> {
    return this.http.get<Booking[]>(this.baseUrl);
  }

  create(booking: Booking): Observable<Booking> {
    // pas de options bizarres, HttpClient déclenche bien error pour 400
    return this.http.post<Booking>(this.baseUrl, booking);
  }

  // 🔹 annuler une réservation (DELETE /api/owner/bookings/{id})
  cancel(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
