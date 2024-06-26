import { Component, OnInit } from '@angular/core';
import { LoginResponse } from '../../models/auth/login-response';
import { AuthService } from '../../services/auth.service';
import { DashboardService } from '../../services/dashboard.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PostDetail } from '../../models/post';

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
  
  constructor(  
    private authService: AuthService,
    private service: DashboardService,
    private route: ActivatedRoute,
    private router: Router
    ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    const postId = this.route.snapshot.paramMap.get('id');
    if (postId) {
      this.id = +postId;
      this.loadPostDetails(this.id, this.currentUser.token)
    }
  }

  loadPostDetails(id: number, token: string) {
    this.service.getPost(this.id, this.currentUser.token).subscribe({
      next: (response) => {
        this.post = response;
        console.log(this.post);
      },
      error: (error) => {
        this.isLoading = false;
        this.onError = true;
        this.errorMessage = "Erreur : une erreur est survenue lors du chargement de l'article";
      },
    });
  }

  back() {
    this.router.navigate(['/dashboard']);
  }

}
