// src/app/models/booking.model.ts
export interface Booking {
  id?: number;        // si ton backend le renvoie
  propertyId: number;
  guestName: string;
  guestEmail: string;
  startDate: string;
  endDate: string;
  totalPrice: number;
  status: string;     // "PENDING", "CONFIRMED", ...
}
