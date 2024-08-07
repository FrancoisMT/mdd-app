import { Component, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subscription } from 'rxjs';
import { SignupResponse } from '../../../models/auth/signup-response';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit, OnDestroy {
  registerForm!: FormGroup;
  onError: boolean = false;
  errorMessage: string = '';
  isLoading = false;
  registerSubscription: Subscription = new Subscription();

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private snackBar: MatSnackBar,
    private router: Router) {
  }

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, this.passwordValidator()]],
      username: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      this.isLoading = true;

      this.registerSubscription = this.authService.register(this.registerForm.value).subscribe({
        next: (response: SignupResponse) => {
          this.isLoading = false;
          this.snackBar.open(response.message, 'Fermer', {
            duration: 5000,
            verticalPosition: 'top',
          });
          this.router.navigate(['/login']);
        },
        error: (error: unknown) => {
          this.isLoading = false;
          this.handleError(error);
        }
      });
    }
  }

  passwordValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: boolean } | null => {
      const value = control.value;

      if (!value) {
        return { 'passwordInvalid': true };
      }

      const passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\W\_])[0-9a-zA-Z\W\_]{8,}$/;
      const valid = passwordRegex.test(value);

      return valid ? null : { 'passwordInvalid': true };
    };
  }

  private handleError(error: unknown): void {
    if (error instanceof HttpErrorResponse) {
      if (error.status === 409) {
        this.errorMessage = 'Erreur : un mail est déjà associé à ce compte.';
      } else {
        this.errorMessage = "Erreur : une erreur est survenue lors de l'inscription de l'utilusateur.";
      }
    } else {
      this.errorMessage = "Erreur : une erreur inattendue est survenue. Veuillez réessayer plus tard.";
    }

    this.onError = true;
  }

  back(): void {
    this.router.navigate(['/home']);
  }

  ngOnDestroy(): void {
    this.registerSubscription.unsubscribe();
  }

}
