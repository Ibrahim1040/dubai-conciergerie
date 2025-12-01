import { Component, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { BookingService } from '../../services/booking.service';
import { Booking } from '../../models/booking.model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-bookings',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './bookings.html',
  styleUrl: './bookings.scss'
})
export class BookingsComponent implements OnInit {

  @ViewChild('bookingForm') bookingForm!: NgForm;

  bookings: Booking[] = [];

  newBooking: Booking = {
    propertyId: 0,
    guestName: '',
    guestEmail: '',
    startDate: '',
    endDate: '',
    totalPrice: 0,
    status: 'PENDING'
  };

  loading = false;
  errorMessage = '';
  successMessage = '';
  todayString = new Date().toISOString().split('T')[0];// "2025-12-01"
  // --- Filtres ---
  filterStatus: 'ALL' | 'PENDING' | 'CONFIRMED' | 'CANCELED' = 'ALL';
  filterClient: string = '';
  filterStartDate: string = '';
  filterEndDate: string = '';

  get filteredBookings(): Booking[] {
    return this.bookings.filter(b => {

      // filtre statut
      if (this.filterStatus !== 'ALL' && b.status !== this.filterStatus) {
        return false;
      }

      // filtre client (nom ou email)
      const search = this.filterClient.trim().toLowerCase();
      if (search) {
        const name = (b.guestName ?? '').toLowerCase();
        const email = (b.guestEmail ?? '').toLowerCase();
        if (!name.includes(search) && !email.includes(search)) {
          return false;
        }
      }

      // filtre date de début (Du)
      if (this.filterStartDate) {
        const min = new Date(this.filterStartDate);
        const start = new Date(b.startDate);
        // on compare en “date pure”
        min.setHours(0, 0, 0, 0);
        start.setHours(0, 0, 0, 0);
        if (start < min) {
          return false;
        }
      }

      // filtre date de fin (Au)
      if (this.filterEndDate) {
        const max = new Date(this.filterEndDate);
        const end = new Date(b.endDate);
        max.setHours(0, 0, 0, 0);
        end.setHours(0, 0, 0, 0);
        if (end > max) {
          return false;
        }
      }

      return true;
    });
  }

  resetFilters(): void {
    this.filterStatus = 'ALL';
    this.filterClient = '';
    this.filterStartDate = '';
    this.filterEndDate = '';
  }



  constructor(
    private bookingService: BookingService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadBookings();
  }

  getStatusClass(status?: string): string {
    switch (status) {
      case 'CONFIRMED':
        return 'status-badge status-confirmed';
      case 'PENDING':
        return 'status-badge status-pending';
      case 'CANCELED':
        return 'status-badge status-canceled';
      default:
        return 'status-badge';
    }
  }

  private priceFormatter = new Intl.NumberFormat('de-DE', {
    minimumFractionDigits: 0,
    maximumFractionDigits: 0
  });

  formatPrice(value?: number): string {
    if (value == null) {
      return '';
    }
    return this.priceFormatter.format(value); // ex: 5000 -> "5.000"
  }


  loadBookings(): void {
    console.log('loadBookings() appelé');
    this.loading = true;
    this.errorMessage = '';

    this.bookingService.getAll().subscribe({
      next: (data) => {
        console.log('GET bookings -> succès', data);
        this.bookings = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('GET bookings -> ERREUR', err);
        this.errorMessage = 'Erreur lors du chargement des réservations';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  createBooking(): void {
    this.errorMessage = '';
    this.successMessage = '';

    // validations front
    if (!this.newBooking.propertyId ||
      !this.newBooking.guestName ||
      !this.newBooking.guestEmail ||
      !this.newBooking.startDate ||
      !this.newBooking.endDate) {
      this.errorMessage =
        'propertyId, guestName, guestEmail, startDate et endDate sont obligatoires';
      return;
    }

    if (this.newBooking.totalPrice < 0) {
      this.errorMessage = 'totalPrice ne peut pas être négatif';
      return;
    }

    // empêcher les dates passées
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const startDate = new Date(this.newBooking.startDate);

    if (startDate < today) {
      this.errorMessage = 'La date d\'arrivée ne peut pas être dans le passé.';
      return;
    }

    console.log('POST booking ->', this.newBooking);

    this.bookingService.create(this.newBooking).subscribe({
      next: (created) => {
        console.log('Réservation créée ->', created);

        this.successMessage = 'Réservation créée avec succès';

        // on ajoute l’élément renvoyé par l’API
        this.bookings = [...this.bookings, created];

        // reset du modèle
        this.newBooking = {
          propertyId: 0,
          guestName: '',
          guestEmail: '',
          startDate: '',
          endDate: '',
          totalPrice: 0,
          status: 'PENDING'
        };

        // reset du formulaire angular
        if (this.bookingForm) {
          this.bookingForm.resetForm({
            propertyId: 0,
            guestName: '',
            guestEmail: '',
            startDate: '',
            endDate: '',
            totalPrice: 0,
            status: 'PENDING'
          });
        }

        this.cdr.detectChanges();
      },
      error: (err: HttpErrorResponse) => {
        console.error('Erreur POST /bookings', err);
        this.successMessage = '';

        const body: any = err.error;
        let msg = 'Erreur lors de la création de la réservation.';

        if (err.status === 0) {
          msg = 'Impossible de contacter le serveur (API hors ligne ?).';
        } else if (body) {
          if (typeof body === 'string') {
            msg = body;
          } else if (body.message) {
            msg = body.message;
          } else if (body.detail) {
            msg = body.detail;
          } else if (body.error) {
            msg = body.error;
          } else {
            msg = JSON.stringify(body);
          }
        } else if (err.message) {
          msg = err.message;
        }

        this.errorMessage = msg;
        this.cdr.detectChanges();
      }
    });
  }

  cancelBooking(booking: Booking): void {
    if (!booking.id) {
      return;
    }

    const ok = confirm(
      `Tu veux vraiment annuler la réservation #${booking.id} pour ${booking.guestName} ?`
    );
    if (!ok) {
      return;
    }

    this.errorMessage = '';
    this.successMessage = '';

    this.bookingService.cancel(booking.id).subscribe({
      next: () => {
        // on recharge la liste depuis l'API
        this.successMessage = 'Réservation annulée avec succès';
        this.loadBookings();
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erreur DELETE /bookings', err);

        const body: any = err.error;
        let msg = 'Erreur lors de l’annulation de la réservation.';

        if (err.status === 0) {
          msg = 'Impossible de contacter le serveur (API hors ligne ?).';
        } else if (body?.message) {
          msg = body.message;
        } else if (body?.detail) {
          msg = body.detail;
        } else if (body?.error) {
          msg = body.error;
        }

        this.errorMessage = msg;
        this.cdr.detectChanges();
      }
    });
  }

}
