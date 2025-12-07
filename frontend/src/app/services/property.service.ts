import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Property } from '../models/property.model';
import { APP_CONFIG } from '../app-config';

@Injectable({
  providedIn: 'root'
})
export class PropertyService {

  private baseUrl = `${APP_CONFIG.apiUrl}/owner/properties`;

  constructor(private http: HttpClient) {}

  // Récupérer les propriétés d’un owner
  getOwnerProperties(ownerId: number): Observable<Property[]> {
    return this.http.get<Property[]>(`${this.baseUrl}/${ownerId}`);
  }

  // Créer une propriété pour un owner
  createOwnerProperty(ownerId: number, dto: Property): Observable<Property> {
    return this.http.post<Property>(`${this.baseUrl}/${ownerId}`, dto);
  }

  // (optionnel pour plus tard)
  getOwnerProperty(ownerId: number, propertyId: number): Observable<Property> {
    return this.http.get<Property>(`${this.baseUrl}/${ownerId}/${propertyId}`);
  }

  updateOwnerProperty(ownerId: number, propertyId: number, dto: Property): Observable<Property> {
    return this.http.put<Property>(`${this.baseUrl}/${ownerId}/${propertyId}`, dto);
  }

  deleteOwnerProperty(ownerId: number, propertyId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${ownerId}/${propertyId}`);
  }
}
