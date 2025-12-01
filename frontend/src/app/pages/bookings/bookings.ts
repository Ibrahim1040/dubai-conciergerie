// src/app/pages/bookings/bookings.ts
import {
  Component,
  OnInit,
  ViewChild,
  ChangeDetectorRef
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { BookingService } from '../../services/booking.service';
import { Booking } from '../../models/booking.model';

@Component({
  selector: 'app-bookings',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    MatNativeDateModule],
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
  todayString: string = new Date().toISOString().split('T')[0];

  // --- Filtres ---
  filterStatus: 'ALL' | 'PENDING' | 'CONFIRMED' | 'CANCELED' = 'ALL';
  filterClient: string = '';
  filterStartDate: string = '';
  filterEndDate: string = '';
  filterPropertyId: number | null = null;
  excludeCanceled = false;
  startDateModel: Date | null = null;
  endDateModel: Date | null = null;
  reservedRanges: { start: Date; end: Date }[] = [];


  // résumé
  totalFilteredNights = 0;
  totalFilteredAmount = 0;

  constructor(
    private bookingService: BookingService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadBookings();
  }

  // ------- FILTRAGE + RÉSUMÉ --------
  get filteredBookings(): Booking[] {
    const term = this.filterClient.trim().toLowerCase();

    const from = this.filterStartDate ? new Date(this.filterStartDate) : null;
    const to   = this.filterEndDate   ? new Date(this.filterEndDate)   : null;

    if (from) from.setHours(0, 0, 0, 0);
    if (to)   to.setHours(0, 0, 0, 0);

    const propertyId = this.filterPropertyId || null;

    const filtered = this.bookings.filter((b: Booking) => {
      // propertyId
      if (propertyId && b.propertyId !== propertyId) {
        return false;
      }

      // client / email
      if (term) {
        const inName  = b.guestName?.toLowerCase().includes(term);
        const inEmail = b.guestEmail?.toLowerCase().includes(term);
        if (!inName && !inEmail) {
          return false;
        }
      }

      // exclure annulations
      if (this.excludeCanceled && b.status === 'CANCELED') {
        return false;
      }

      // statut
      if (this.filterStatus !== 'ALL' && b.status !== this.filterStatus) {
        return false;
      }

      // période (sur la date d'arrivée)
      if (from || to) {
        const start = new Date(b.startDate);
        start.setHours(0, 0, 0, 0);

        if (from && start < from) {
          return false;
        }
        if (to && start > to) {
          return false;
        }
      }

      return true;
    });

    this.computeSummary(filtered);
    return filtered;
  }

  resetFilters(): void {
    this.filterStatus = 'ALL';
    this.filterClient = '';
    this.filterStartDate = '';
    this.filterEndDate = '';
    this.filterPropertyId = null;
    this.excludeCanceled = false;
  }

  private computeSummary(list: Booking[]): void {
    this.totalFilteredNights = list.reduce(
      (sum, b) => sum + this.getNights(b),
      0
    );
    this.totalFilteredAmount = list.reduce(
      (sum, b) => sum + (b.totalPrice || 0),
      0
    );
  }

  // ------- CHARGEMENT --------
  loadBookings(): void {
    this.loading = true;
    this.errorMessage = '';

    this.bookingService.getAll().subscribe({
      next: (data) => {
        this.bookings = data;

        // on n’est plus en chargement
        this.loading = false;

        // on reset les dates du formulaire de création
        this.startDateModel = null;
        this.endDateModel = null;

        // on met à jour les plages réservées pour le datepicker
        this.refreshReservedRanges();

        // si ton résumé est calculé dans le getter filteredBookings,
        // pas besoin d'appeler computeSummary ici :
        // this.computeSummary(this.filteredBookings);  // optionnel
      },
      error: (err) => {
        console.error('GET bookings -> ERREUR', err);
        this.loading = false;
        this.errorMessage = 'Erreur lors du chargement des réservations';
      }
    });
  }


  private refreshReservedRanges(): void {
    const propertyId = this.newBooking.propertyId;
    if (!propertyId) {
      this.reservedRanges = [];
      return;
    }

    const relevant = this.bookings.filter(
      b => b.propertyId === propertyId && b.status !== 'CANCELED'
    );

    this.reservedRanges = relevant.map(b => ({
      start: new Date(b.startDate),
      end: new Date(b.endDate)
    }));
  }


  // ------- CRÉATION --------
  createBooking(): void {
    this.errorMessage = '';
    this.successMessage = '';

    // 1) Champs obligatoires
    if (!this.newBooking.propertyId ||
      !this.newBooking.guestName ||
      !this.newBooking.guestEmail ||
      !this.startDateModel ||
      !this.endDateModel) {

      this.errorMessage =
        'propertyId, guestName, guestEmail, startDate et endDate sont obligatoires';
      return;
    }

    // 2) Prix
    if (this.newBooking.totalPrice < 0) {
      this.errorMessage = 'totalPrice ne peut pas être négatif';
      return;
    }

    // 3) Cohérence des dates (avec le modèle du datepicker)
    const start = this.toDateOnly(this.startDateModel);
    const end   = this.toDateOnly(this.endDateModel);

    if (end < start) {
      this.errorMessage = 'La date de départ doit être après la date d’arrivée.';
      return;
    }

    // 4) On copie les dates formatées dans newBooking
    this.newBooking.startDate = this.formatDate(this.startDateModel);
    this.newBooking.endDate   = this.formatDate(this.endDateModel);

    // 5) Appel API
    this.bookingService.create(this.newBooking).subscribe({
      next: (created) => {
        this.successMessage = 'Réservation créée avec succès';
        this.bookings = [...this.bookings, created];

        // reset modèle
        this.newBooking = {
          propertyId: 0,
          guestName: '',
          guestEmail: '',
          startDate: '',
          endDate: '',
          totalPrice: 0,
          status: 'PENDING'
        };

        // reset dates du datepicker
        this.startDateModel = null;
        this.endDateModel = null;
        this.refreshReservedRanges();   // on vide les plages (propertyId = 0)

        // reset formulaire Angular
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


  private toDateOnly(date: Date): Date {
    const d = new Date(date);
    d.setHours(0, 0, 0, 0);
    return d;
  }

  private formatDate(date: Date | null): string {
    if (!date) return '';

    const d = this.toDateOnly(date); // garde la date en local (pas d'UTC)
    const year  = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day   = String(d.getDate()).padStart(2, '0');

    // format "yyyy-MM-dd" pour ton backend (LocalDate)
    return `${year}-${month}-${day}`;
  }


  // Filtre pour la date d'arrivée
  startDateFilter = (date: Date | null): boolean => {
    if (!date) return false;

    const day = this.toDateOnly(date).getTime();

    console.log('startDateModel =', this.startDateModel);
    console.log('formatted start =', this.formatDate(this.startDateModel));

    // Pas dans le passé
    const today = this.toDateOnly(new Date()).getTime();
    if (day < today) return false;

    // Si aucun logement sélectionné : pas de blocage spécial
    if (!this.newBooking.propertyId) {
      return true;
    }

    // Ne pas pouvoir choisir un jour qui EST déjà dans une réservation
    const isInReservedRange = this.reservedRanges.some(range => {
      const start = this.toDateOnly(range.start).getTime();
      const end   = this.toDateOnly(range.end).getTime(); // [start, end)
      return day >= start && day < end;
    });

    return !isInReservedRange;
  };

// Filtre pour la date de départ
  endDateFilter = (date: Date | null): boolean => {
    if (!date) return false;

    const end = this.toDateOnly(date).getTime();

    const today = this.toDateOnly(new Date()).getTime();
    if (end < today) return false;

    // On doit avoir choisi une date d'arrivée avant
    if (!this.startDateModel) {
      return false;
    }

    const start = this.toDateOnly(this.startDateModel).getTime();
    // départ doit être après l'arrivée
    if (end <= start) {
      return false;
    }

    // Si aucun logement : pas de blocage spécial
    if (!this.newBooking.propertyId) {
      return true;
    }

    // ❗ Vérifier que l'intervalle [start, end) NE chevauche PAS
    // une réservation existante pour ce logement
    const overlaps = this.reservedRanges.some(range => {
      const rs = this.toDateOnly(range.start).getTime();
      const re = this.toDateOnly(range.end).getTime();

      // condition de recouvrement d'intervalles :
      // [start, end) et [rs, re) se chevauchent si :
      // start < re && rs < end
      return start < re && rs < end;
    });

    return !overlaps;
  };


  // ------- HELPERS D’AFFICHAGE --------
  getNights(b: Booking): number {
    const start = new Date(b.startDate);
    const end = new Date(b.endDate);
    const diffMs = end.getTime() - start.getTime();
    const diffDays = Math.round(diffMs / (1000 * 60 * 60 * 24));
    return isNaN(diffDays) ? 0 : diffDays;
  }

  getPricePerNight(b: Booking): number | null {
    const nights = this.getNights(b);
    if (!nights || nights <= 0) {
      return null;
    }
    return (b.totalPrice || 0) / nights;
  }

  formatAmount(value: number | null | undefined): string {
    const v = value || 0;
    // format 1.000,00 style FR avec point pour les milliers
    return v.toLocaleString('de-DE', { minimumFractionDigits: 0, maximumFractionDigits: 0 });
  }

  formatPricePerNight(value: number): string {
    return value.toLocaleString('de-DE', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
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

  isToday(dateStr: string | Date | undefined): boolean {
    if (!dateStr) return false;
    const d = new Date(dateStr);
    const today = new Date();
    d.setHours(0, 0, 0, 0);
    today.setHours(0, 0, 0, 0);
    return d.getTime() === today.getTime();
  }

  // ------- EXPORT CSV --------
  exportToCsv(): void {
    const data = this.filteredBookings;
    if (!data.length) {
      this.errorMessage = 'Aucune réservation à exporter.';
      return;
    }

    const rows = data.map((b) => {
      const nights = this.getNights(b);
      const pricePerNight = this.getPricePerNight(b);

      return {
        id: b.id ?? '',
        propertyId: b.propertyId,
        guestName: b.guestName,
        guestEmail: b.guestEmail,
        startDate: b.startDate,
        endDate: b.endDate,
        nights,
        totalPrice: this.formatAmount(b.totalPrice),
        pricePerNight: pricePerNight != null ? this.formatPricePerNight(pricePerNight) : '',
        status: b.status
      };
    });

    const headers = Object.keys(rows[0]);

    const csvContent =
      headers.join(';') + '\n' +
      rows
        .map((r) =>
          headers
            .map((h) => {
              const value = (r as any)[h] ?? '';
              return `"${String(value).replace(/"/g, '""')}"`
            })
            .join(';')
        )
        .join('\n');

    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = window.URL.createObjectURL(blob);

    const a = document.createElement('a');
    a.href = url;
    a.download = 'reservations.csv';
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);

    window.URL.revokeObjectURL(url);
  }

  onPropertyChange(): void {
    this.refreshReservedRanges();
  }

  // ------- ANNULATION --------
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
        // On met à jour le statut en front
        booking.status = 'CANCELED';

        this.successMessage = 'Réservation annulée avec succès';

        // On met à jour les plages réservées pour le datepicker
        if (booking.propertyId === this.newBooking.propertyId) {
          this.refreshReservedRanges();
        }

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
