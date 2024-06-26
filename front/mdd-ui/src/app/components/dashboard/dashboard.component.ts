import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { LoginResponse } from '../../models/auth/login-response';
import { DashboardService } from '../../services/dashboard.service';
import { Post } from '../../models/post';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  public currentUser!: LoginResponse;
  public onError: boolean = false;
  public isLoading: boolean = false;
  public errorMessage: string = "";
  public posts: Post[] = [];
  public ascendingOrder: boolean = true;

  constructor(
    private authService: AuthService,
    private service: DashboardService,
    private router: Router
  ) { }

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
    this.loadUserPosts();
  }

  loadUserPosts() {
    this.service.getPosts(this.currentUser.token).subscribe({
      next: (response) => {
        this.posts = response.sort((a, b) => {
          const dateA = new Date(a.date);
          const dateB = new Date(b.date);
          return dateB.getTime() - dateA.getTime();
        });
      },
      error: (error) => {
        this.isLoading = false;
        this.onError = true;
        this.errorMessage = "Erreur : une erreur est survenue lors de la récupération de votre fil d'actualité";
      },
    });
  }

  create() {
    this.router.navigate(['/create']);
  }

  reverseOrder() {
    this.ascendingOrder = !this.ascendingOrder;
    
    if (this.ascendingOrder) {
      this.posts.sort((a, b) => {
        const dateA = new Date(a.date);
        const dateB = new Date(b.date);
        return dateB.getTime() - dateA.getTime();
      });
    } else {
      this.posts.sort((a, b) => {
        const dateA = new Date(a.date);
        const dateB = new Date(b.date);
        return dateA.getTime() - dateB.getTime();
      });
    }
  }

  onClickDetail(id:number) {
    this.router.navigate(['detail', id]);
  }

}
