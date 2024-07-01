import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { TopicService } from '../../services/topic.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DashboardService } from '../../services/dashboard.service';
import { LoginResponse } from '../../models/auth/login-response';
import { Topic } from '../../models/topic';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrl: './create-post.component.css'
})
export class CreatePostComponent implements OnInit {
  public currentUser!: LoginResponse;
  public onError: boolean = false;
  public isLoading: boolean = false;
  public errorMessage: string = "";
  public topics: Topic[] = [];
  public postForm!:FormGroup;

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    this.initForm();
    this.loadTopics();
  }

  constructor(
    private authService: AuthService,
    private topicService: TopicService,
    private dashboardService: DashboardService,
    private snackBar: MatSnackBar,
    private router: Router,
    private fb: FormBuilder
  ) {}

  initForm() {
    this.postForm = this.fb.group({
      topicId: ['', Validators.required],
      title: ['', [Validators.required, Validators.maxLength(200)]],
      description: ['', [Validators.required, Validators.maxLength(2500)]]
    });
  }

  loadTopics() {
    this.topicService.getAllTopics(this.currentUser.token).subscribe({
      next: (response) => {
        this.topics = response;
      },
      error: (error) => {
        this.isLoading = false;
        this.onError = true;
        this.errorMessage = "Erreur : une erreur est survenue lors de la récupération des données";
      },
    });
  }

  onSubmit() {
    if (this.postForm.valid) {
      this.isLoading = true;
      this.dashboardService.createPost(this.postForm.value, this.currentUser.token).subscribe({
        next: (response) => {
          this.isLoading = false;
          this.snackBar.open('Article publié avec succès !', 'Fermer', {
            duration: 5000, 
            verticalPosition: 'top', 
          });

          this.router.navigate(["/dashboard"]);
        },
        error: (error) => {
          this.isLoading = false;
          this.onError = true;
          this.errorMessage = "Une erreur est survenue au moment de la publication de l'article."
        },
      });
    }
  }

  back() {
    this.router.navigate(['/dashboard']);
  }


}
