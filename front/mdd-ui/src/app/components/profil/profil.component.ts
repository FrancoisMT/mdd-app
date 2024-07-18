import { Component, OnDestroy, OnInit } from '@angular/core';
import { LoginResponse } from '../../models/auth/login-response';
import { AuthService } from '../../services/auth.service';
import { AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { MatSnackBar, MatSnackBarVerticalPosition  } from '@angular/material/snack-bar';
import { TopicService } from '../../services/topic.service';
import { SubscriptionData} from '../../models/subscription';
import { Subscription } from 'rxjs';
import { MessageResponse } from '../../models/message-response';

@Component({
  selector: 'app-profil',
  templateUrl: './profil.component.html',
  styleUrl: './profil.component.css'
})
export class ProfilComponent implements OnInit, OnDestroy {
  currentUser!: LoginResponse;
  userForm!: FormGroup;
  isLoading : boolean = false;
  onError: boolean = false;
  errorMessage: string = '';
  userTopics: SubscriptionData[] = [];
  subscriptions: Subscription = new Subscription();

  constructor(
    private authService: AuthService,
    private topicService: TopicService, 
    private fb: FormBuilder, 
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    if (this.currentUser?.mail && this.currentUser?.username) {
      this.initForm(this.currentUser);
    }
    this.loadUserSubscription();
  }

  initForm(currentUser: LoginResponse): void {
    this.userForm = this.fb.group({
      email: [currentUser.mail, [Validators.required, Validators.email]],
      username: [currentUser.username, Validators.required],
      password: ['', [Validators.required, this.passwordValidator()]] 
    });
  }

  onSubmit(): void {
    if (this.userForm.valid) {
      this.isLoading = true;
      const updateCredsSub: Subscription = this.authService.updateCredentials(this.userForm.value, this.currentUser?.token).subscribe({
        next: (response: LoginResponse) => {
          localStorage.removeItem('currentUser');
          localStorage.setItem('currentUser', JSON.stringify(response));
          this.isLoading = false;

          this.snackBar.open('Vos informations ont été mises à jour avec succès', 'Fermer', {
            duration: 5000, 
            verticalPosition: 'top', 
          });
        },
        error: (error: any) => {
          this.isLoading = false;
          this.handleError(error);
        },
      });

      this.subscriptions.add(updateCredsSub);
    }

  }

  loadUserSubscription(): void {
    const userSub : Subscription = this.topicService.getUserTopics(this.currentUser?.token).subscribe({
      next: (response: SubscriptionData[]) => {
        this.isLoading = false;
        this.userTopics = response;
      },
      error: (error: any) => {
        this.isLoading = false;
        this.onError = true;
        this.errorMessage = "Erreur : une erreur est survenue lors de la récupération des abonnements"
      },
    });

    this.subscriptions.add(userSub);
  }

  unsubscribeTopic(topic: SubscriptionData): void {
    this.isLoading = true;

    const unsubscribeSub: Subscription = this.topicService.unsubscribe(topic.id, this.currentUser?.token).subscribe({
      next: (response: MessageResponse) => {
        this.isLoading = false;
        this.snackBar.open(response.message, 'Fermer', {
          duration: 3000, 
          verticalPosition: 'top', 
        });

        this.loadUserSubscription();

      },
      error: (error: any) => {
        this.isLoading = false;
        this.onError = true;
        this.errorMessage = "Erreur : une erreur est survenue lors du désabonnement"
      },
    });

    this.subscriptions.add(unsubscribeSub);
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

  public handleError(error: any): void {

    if (error.status === 409) {
      this.errorMessage = 'Erreur : un mail est déjà associé à ce compte.';
    } else {
      this.errorMessage = 'Erreur : une erreur est survenue lors de la modification des données.';
    }

    this.onError = true;
  }

  logOut(): void {
    this.authService.logout();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}
