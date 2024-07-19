import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { LoginResponse } from '../../../models/auth/login-response';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {
  loginForm!: FormGroup;
  onError: boolean = false;
  errorMessage: string = '';
  isLoading = false;
  loginSubscription: Subscription = new Subscription();

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router) {
  }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.isLoading = true;

      this.loginSubscription = this.authService.login(this.loginForm.value).subscribe({
        next: (response: LoginResponse) => {
          localStorage.setItem('currentUser', JSON.stringify(response));
          this.router.navigate(['/dashboard']);
          this.isLoading = false;
        },
        error: (error: unknown) => {
          this.handleError(error);
          this.isLoading = false;
        },
      });
    }
  }

  handleError(error: unknown): void {
    if (error instanceof HttpErrorResponse) {
      switch (error.status) {
        case 404:
          this.errorMessage = 'Erreur : aucun compte n\'est associé à cet email. Veuillez créer un compte.';
          break;
        case 400:
          this.errorMessage = 'Erreur : identifiants invalides.';
          break;
        default:
          this.errorMessage = 'Une erreur est survenue. Veuillez réessayer ultérieurement.';
      }
      console.error('Login failed', error);
    } else if (typeof error === 'string') {
      this.errorMessage = error;
    } else {
      this.errorMessage = 'Une erreur inattendue est survenue. Veuillez réessayer.';
    }
    this.onError = true;
  }

  back(): void {
    this.router.navigate(['/home']);
  }

  ngOnDestroy(): void {
    this.loginSubscription.unsubscribe();
  }

}
