import { Component, OnDestroy, OnInit } from '@angular/core';
import { LoginResponse } from '../../models/auth/login-response';
import { AuthService } from '../../services/auth.service';
import { DashboardService } from '../../services/dashboard.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PostDetail } from '../../models/post';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrl: './post-detail.component.css'
})
export class PostDetailComponent implements OnInit, OnDestroy {
  currentUser!: LoginResponse;
  onError: boolean = false;
  isLoading: boolean = false;
  errorMessage: string = "";
  id!: number;
  post!: PostDetail;
  comments: any[] = [];
  commentForm!: FormGroup;
  subscriptions: Subscription = new Subscription();

  constructor(
    private authService: AuthService,
    private service: DashboardService,
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    const postId = this.route.snapshot.paramMap.get('id');
    if (postId) {
      this.id = +postId;
      this.initForm();
      this.loadPostDetails(this.id, this.currentUser?.token)
    }
  }

  loadPostDetails(id: number, token: string) {
    const postDetailSub : Subscription = this.service.getPost(this.id, this.currentUser?.token).subscribe({
      next: (response) => {
        this.post = response;
        this.comments = this.post.comments;
      },
      error: (error) => {
        this.isLoading = false;
        this.onError = true;
        this.errorMessage = "Erreur : une erreur est survenue lors du chargement de l'article";
      },
    });

    this.subscriptions.add(postDetailSub);
  }

  initForm() {
    this.commentForm = this.fb.group({
      content: ['', [Validators.required, Validators.maxLength(2500)]],
    });
  }

  onSubmit() {
    if (this.commentForm.valid) {
      this.isLoading = true;
      const createCommentSub: Subscription = this.service.addComment(this.id, this.commentForm.value,this.currentUser.token).subscribe({
        next: (response) => {
          this.loadPostDetails(this.id, this.currentUser.token);
          this.initForm();
          this.isLoading = false;
        },
        error: (error) => {
          this.isLoading = false;
          this.onError = true;
          this.errorMessage = "Erreur : une erreur est survenue lors de l'ajout du commentaire";
          this.isLoading = false;
        },
      });

      this.subscriptions.add(createCommentSub);
    }
  }

  back() {
    this.router.navigate(['/dashboard']);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}
