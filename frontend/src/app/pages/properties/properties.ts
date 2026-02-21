import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
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

  constructor(
    private propertyService: PropertyService,
    private cdr: ChangeDetectorRef          // 👈 nouveau
  ) {}

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
        this.properties = data ?? [];
        this.loading = false;
        this.cdr.detectChanges();           // 👈 force MAJ de la vue
      },
      error: (err) => {
        console.error('Erreur GET /api/owner/properties', err);
        this.loading = false;
        this.errorMessage = this.extractErrorMessage(
          err,
          'Erreur lors du chargement des logements.'
        );
        this.cdr.detectChanges();           // 👈 idem en cas d’erreur
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
    this.cdr.detectChanges();               // optionnel mais propre
  }

  editProperty(p: Property): void {
    this.errorMessage = '';
    this.successMessage = '';
    this.current = { ...p };                // clone
    this.cdr.detectChanges();
  }

  saveProperty(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.current.title || !this.current.city || !this.current.address) {
      this.errorMessage = 'Titre, ville et adresse sont obligatoires.';
      return;
    }

    const payload: Property = {
      ...this.current,
      ownerId: this.ownerId
    };

    const obs = this.current.id
      ? this.propertyService.updateOwnerProperty(
          this.ownerId,
          this.current.id!,   // ID logement
          payload
        )
      : this.propertyService.createOwnerProperty(this.ownerId, payload);

    obs.subscribe({
      next: (saved) => {
        if (this.current.id) {
          // mise à jour
          this.properties = this.properties.map(p =>
            p.id === saved.id ? saved : p
          );
          this.successMessage = 'Logement mis à jour avec succès.';
        } else {
          // ajout
          this.properties = [...this.properties, saved];
          this.successMessage = 'Logement créé avec succès.';
        }

        this.current = this.emptyProperty();
        this.cdr.detectChanges();           // 👈 refresh après save
      },
      error: (err) => {
        console.error('Erreur saveProperty', err);
        this.errorMessage = 'Erreur lors de l’enregistrement du logement.';
        this.cdr.detectChanges();
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

  deleteProperty(p: Property): void {
    if (!p.id) {
      return;
    }

    const ok = confirm(`Supprimer le logement "${p.title}" ?`);
    if (!ok) {
      return;
    }

    this.errorMessage = '';
    this.successMessage = '';

    this.propertyService.deleteOwnerProperty(this.ownerId, p.id).subscribe({
      next: () => {
        this.properties = this.properties.filter(prop => prop.id !== p.id);
        this.successMessage = 'Logement supprimé avec succès.';
        this.cdr.detectChanges();           // 👈 refresh après delete
      },
      error: (err) => {
        console.error('Erreur deleteProperty', err);
        this.errorMessage = 'Erreur lors de la suppression du logement.';
        this.cdr.detectChanges();
      }
    });
  }

}
