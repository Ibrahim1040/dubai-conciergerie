import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

interface AuthResponse {
  token: string;
  email: string;
  role: string;
}

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  // si tu as un SCSS/ CSS pour ce composant :
  // styleUrls: ['./login.component.scss'],
  imports: [CommonModule, FormsModule],   // 👈 IMPORTANT
})
export class LoginComponent {

  email = '';
  password = '';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  login() {
    const body = {
      email: this.email,
      password: this.password,
    };

    this.http.post<AuthResponse>('http://localhost:8080/api/auth/login', body)
      .subscribe({
        next: (res) => {
          console.log('[Login] token reçu :', res.token);
          this.authService.setToken(res.token);
          // TODO : rediriger vers /properties
        },
        error: (err) => console.error('[Login] erreur', err),
      });
  }
}
