import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {
  registerForm!:FormGroup;
  onError: boolean = false;
  errorMessage: string = '';
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private snackBar: MatSnackBar,
    private router: Router) {
  }

  ngOnInit(): void {
    this.initForm();
  }

  initForm() {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, this.passwordValidator()]],
      username: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.registerForm.valid) {
      this.isLoading = true;
      this.authService.register(this.registerForm.value).subscribe({
        next: (response) => {
          this.isLoading = false;
          this.snackBar.open('Utilisateur enregistrer avec succès !', 'Fermer', {
            duration: 5000, 
            verticalPosition: 'top', 
          });
          this.router.navigate(['/login']);
        },
        error: (error) => {
          this.isLoading = false;
          this.handleError(error);
        },
      });
    }
  }

  passwordValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const value = control.value;
      if (!value) {
        return { 'passwordInvalid': true };
      }

      const passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\W\_])[0-9a-zA-Z\W\_]{8,}$/;
      const valid = passwordRegex.test(value);

      return valid ? null : { 'passwordInvalid': true };
    };
  }

  private handleError(error: any): void {

    if (error.status === 409) {
      this.errorMessage = 'Erreur : un mail est déjà associé à ce compte.';
    } else {
      this.errorMessage = "Erreur : une erreur est survenue lors de l'inscription de l'utilusateur.";
    }

    this.onError = true;
  }

  back(): void {
    this.router.navigate(['/home']);
  }

}
