import { Component, OnInit } from '@angular/core';
import { LoginResponse } from '../../models/auth/login-response';
import { AuthService } from '../../services/auth.service';
import { AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { MatSnackBar, MatSnackBarVerticalPosition  } from '@angular/material/snack-bar';
import { TopicService } from '../../services/topic.service';
import { Subscription } from '../../models/subscription';

@Component({
  selector: 'app-profil',
  templateUrl: './profil.component.html',
  styleUrl: './profil.component.css'
})
export class ProfilComponent implements OnInit {
  public currentUser!: LoginResponse;
  public userForm!: FormGroup;
  public isLoading : boolean = false;
  public onError: boolean = false;
  public errorMessage: string = '';
  public userTopics: Subscription[] = [];

  constructor(
    private authService: AuthService,
    private topicService: TopicService, 
    private fb: FormBuilder, 
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    this.initForm(this.currentUser);
    this.loadUserSubscription();
  }

  initForm(currentUser: LoginResponse) {

    this.userForm = this.fb.group({
      email: [currentUser.mail, [Validators.required, Validators.email]],
      username: [currentUser.username, Validators.required],
      password: ['', [Validators.required, this.passwordValidator()]] 
    });

  }

  onSubmit() {

    if (this.userForm.valid) {
      this.isLoading = true;
      this.authService.updateCredentials(this.userForm.value, this.currentUser.token).subscribe({
        next: (response) => {
          localStorage.removeItem('currentUser');
          localStorage.setItem('currentUser', JSON.stringify(response));
          this.isLoading = false;

          this.snackBar.open('Vos informations ont été mises à jour avec succès', 'Fermer', {
            duration: 5000, 
            verticalPosition: 'top', 
          });
        },
        error: (error) => {
          this.isLoading = false;
          this.handleError(error);
        },
      });
    }

  }

  loadUserSubscription() {
    this.topicService.getUserTopics(this.currentUser.token).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.userTopics = response;
      },
      error: (error) => {
        this.isLoading = false;
        this.onError = true;
        this.errorMessage = "Erreur : une erreur est survenue lors de la récupération des abonnements"
      },
    });
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
      this.errorMessage = 'Erreur : une erreur est survenue lors de la modification des données.';
    }

    this.onError = true;
  }

}
