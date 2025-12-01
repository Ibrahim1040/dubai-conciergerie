# Dubai Conciergerie

Application full-stack de gestion de réservations pour une conciergerie (Dubai / Bruxelles).

## Stack

- **Backend** : Spring Boot (Java 17), Spring Security (JWT), REST API
- **Frontend** : Angular, formulaires de réservation, filtre par statut / client / période

## Lancer le backend

```bash
mvn spring-boot:run

Par défaut : http://localhost:8080

Lancer le frontend
cd frontend
npm install        # première fois
ng serve


Par défaut : http://localhost:4200

Le frontend consomme l’API GET/POST /api/owner/bookings pour gérer les réservations.

