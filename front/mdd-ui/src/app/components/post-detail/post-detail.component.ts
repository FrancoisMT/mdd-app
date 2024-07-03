import { Component, OnInit } from '@angular/core';
import { LoginResponse } from '../../models/auth/login-response';
import { AuthService } from '../../services/auth.service';
import { DashboardService } from '../../services/dashboard.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PostDetail } from '../../models/post';
import { Comment } from '../../models/comment';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrl: './post-detail.component.css'
})
export class PostDetailComponent implements OnInit {
  public currentUser!: LoginResponse;
  public onError: boolean = false;
  public isLoading: boolean = false;
  public errorMessage: string = "";
  public id!: number;
  public post!: PostDetail;
  public comments: any[] = [];
  public commentForm!: FormGroup;

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
    this.service.getPost(this.id, this.currentUser?.token).subscribe({
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
  }

  initForm() {
    this.commentForm = this.fb.group({
      content: ['', [Validators.required, Validators.maxLength(2500)]],
    });
  }

  onSubmit() {
    if (this.commentForm.valid) {
      this.isLoading = true;
      this.service.addComment(this.id, this.commentForm.value,this.currentUser.token).subscribe({
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
    }
  }

  back() {
    this.router.navigate(['/dashboard']);
  }

}
