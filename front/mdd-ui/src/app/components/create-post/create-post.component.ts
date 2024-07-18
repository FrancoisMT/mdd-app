import { Component, OnDestroy, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { TopicService } from '../../services/topic.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DashboardService } from '../../services/dashboard.service';
import { LoginResponse } from '../../models/auth/login-response';
import { Topic } from '../../models/topic';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { PostResponse } from '../../models/post';
import { MessageResponse } from '../../models/message-response';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrl: './create-post.component.css'
})
export class CreatePostComponent implements OnInit, OnDestroy {
  currentUser!: LoginResponse;
  onError: boolean = false;
  isLoading: boolean = false;
  errorMessage: string = "";
  topics: Topic[] = [];
  postForm!:FormGroup;
  subscriptions: Subscription = new Subscription();

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    if (this.currentUser.token) {
      this.initForm();
      this.loadTopics();
    }
  }

  constructor(
    private authService: AuthService,
    private topicService: TopicService,
    private dashboardService: DashboardService,
    private snackBar: MatSnackBar,
    private router: Router,
    private fb: FormBuilder
  ) {}

  initForm(): void {
    this.postForm = this.fb.group({
      topicId: ['', Validators.required],
      title: ['', [Validators.required, Validators.maxLength(200)]],
      description: ['', [Validators.required, Validators.maxLength(2500)]]
    });
  }

  loadTopics(): void {
    if (this.currentUser.token) {
      const topicsSubscription: Subscription = this.topicService.getAllTopics(this.currentUser.token).subscribe({
        next: (response: Topic[]) => {
          this.topics = response;
        },
        error: (error: any) => {
          this.isLoading = false;
          this.onError = true;
          this.errorMessage = "Erreur : une erreur est survenue lors de la récupération des données";
        },
      });

      this.subscriptions.add(topicsSubscription);
    }
  }

  onSubmit(): void {
    if (this.postForm.valid) {
      this.isLoading = true;
      const postSubscription: Subscription = this.dashboardService.createPost(this.postForm.value, this.currentUser.token).subscribe({
        next: (response: MessageResponse) => {
          this.isLoading = false;
          this.snackBar.open(response.message, 'Fermer', {
            duration: 5000, 
            verticalPosition: 'top', 
          });

          this.router.navigate(["/dashboard"]);
        },
        error: (error: any) => {
          this.isLoading = false;
          this.onError = true;
          this.errorMessage = "Une erreur est survenue au moment de la publication de l'article."
        },
      });

      this.subscriptions.add(postSubscription);
    }
  }

  back(): void {
    this.router.navigate(['/dashboard']);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}
