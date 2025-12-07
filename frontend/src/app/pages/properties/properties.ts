import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Property } from '../../models/property.model';
import { PropertyService } from '../../services/property.service';
import { APP_CONFIG } from '../../app-config';
@Component({
  selector: 'app-properties',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './properties.html',
  styleUrls: ['./properties.scss']
})
export class PropertiesComponent implements OnInit {

  // Pour l’instant on prend un ownerId fixe (celui que tu as en BDD)
  readonly ownerId = APP_CONFIG.defaultOwnerId;

  properties: Property[] = [];

  // formulaire
  current: Property = this.emptyProperty();
  loading = false;
  errorMessage = '';
  successMessage = '';

  // filtre recherche
  filterCityOrTitle = '';

  constructor(private propertyService: PropertyService) {}

  ngOnInit(): void {
    this.loadProperties();
  }

  private emptyProperty(): Property {
    return {
      title: '',
      city: '',
      address: '',
      capacity: 1,
      rentalType: 'SHORT_TERM',
      nightlyPrice: null,
      monthlyPrice: null,
      ownerId: this.ownerId
    };
  }

  loadProperties(): void {
    this.loading = true;
    this.errorMessage = '';
    this.propertyService.getOwnerProperties(this.ownerId).subscribe({
      next: (data) => {
        this.properties = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Erreur GET /api/owner/properties', err);
        this.loading = false;
        this.errorMessage = this.extractErrorMessage(err, 'Erreur lors du chargement des logements.');
      }
    });
  }

  get filteredProperties(): Property[] {
    const term = this.filterCityOrTitle.trim().toLowerCase();
    if (!term) return this.properties;

    return this.properties.filter(p =>
      (p.title && p.title.toLowerCase().includes(term)) ||
      (p.city && p.city.toLowerCase().includes(term))
    );
  }

  resetForm(): void {
    this.current = this.emptyProperty();
    this.errorMessage = '';
    this.successMessage = '';
  }

  saveProperty(): void {
    this.errorMessage = '';
    this.successMessage = '';

    // validations front basées sur le DTO
    if (!this.current.title || !this.current.city || !this.current.address) {
      this.errorMessage = 'title, city et address sont obligatoires.';
      return;
    }
    if (!this.current.rentalType) {
      this.errorMessage = 'rentalType est obligatoire.';
      return;
    }
    if (!this.current.capacity || this.current.capacity < 1) {
      this.errorMessage = 'capacity doit être au moins 1.';
      return;
    }

    const payload: Property = {
      ...this.current,
      ownerId: this.ownerId,
      nightlyPrice: this.current.nightlyPrice ?? null,
      monthlyPrice: this.current.monthlyPrice ?? null
    };

    this.propertyService.createOwnerProperty(this.ownerId, payload).subscribe({
      next: (created) => {
        this.successMessage = 'Logement créé avec succès.';
        this.properties = [...this.properties, created];
        this.resetForm();
      },
      error: (err) => {
        console.error('Erreur POST /api/owner/properties', err);
        this.errorMessage = this.extractErrorMessage(err, 'Erreur lors de la création du logement.');
      }
    });
  }

  private extractErrorMessage(err: any, fallback: string): string {
    const body: any = err?.error;
    if (err.status === 0) {
      return 'Impossible de contacter le serveur (API hors ligne ?).';
    }
    if (body) {
      if (typeof body === 'string') return body;
      if (body.message) return body.message;
      if (body.detail) return body.detail;
      if (body.error) return body.error;
      try {
        return JSON.stringify(body);
      } catch {
        return fallback;
      }
    }
    if (err.message) return err.message;
    return fallback;
  }
}
