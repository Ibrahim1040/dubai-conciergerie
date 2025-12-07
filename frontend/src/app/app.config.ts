import { ApplicationConfig, LOCALE_ID } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';

import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';

import { MAT_DATE_LOCALE } from '@angular/material/core';
import { registerLocaleData } from '@angular/common';
import localeFr from '@angular/common/locales/fr';

// Locale FR pour les pipes (date, number, etc.)
registerLocaleData(localeFr);

export const appConfig: ApplicationConfig = {
  providers: [
    // 🚦 Router
    provideRouter(routes),

    // 🌐 HttpClient (obligatoire pour tes services Angular)
    provideHttpClient(withInterceptorsFromDi()),
    // si ça pose problème, tu peux simplement mettre : provideHttpClient()

    // 🎭 Animations nécessaires pour Angular Material (datepicker, etc.)
    provideAnimations(),

    // 🇫🇷 Locale globale
    { provide: LOCALE_ID, useValue: 'fr-FR' },

    // 🇫🇷 Locale pour MatDatepicker
    { provide: MAT_DATE_LOCALE, useValue: 'fr-FR' },
  ]
};
