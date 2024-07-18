import { Component, OnDestroy, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { LoginResponse } from '../../models/auth/login-response';
import { DashboardService } from '../../services/dashboard.service';
import { Post } from '../../models/post';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit, OnDestroy {
  currentUser!: LoginResponse;
  onError: boolean = false;
  isLoading: boolean = false;
  errorMessage: string = "";
  posts: Post[] = [];
  ascendingOrder: boolean = true;
  postSubscription: Subscription = new Subscription();

  constructor(
    private authService: AuthService,
    private service: DashboardService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    if (this.currentUser?.token) {
      this.loadUserPosts();
    }
  }

  loadUserPosts(): void{
    this.postSubscription = this.service.getPosts(this.currentUser.token).subscribe({
      next: (response: Post[]) => {
        this.posts = response.sort((a, b) => {
          const dateA = new Date(a.date);
          const dateB = new Date(b.date);
          return dateB.getTime() - dateA.getTime();
        });
      },
      error: (error: any) => {
        this.isLoading = false;
        this.onError = true;
        this.errorMessage = "Erreur : une erreur est survenue lors de la récupération de votre fil d'actualité";
      },
    });
  }

  create(): void {
    this.router.navigate(['/create']);
  }

  reverseOrder(): void {
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

  onClickDetail(id: number) {
    this.router.navigate(['detail', id]);
  }

  ngOnDestroy(): void {
    this.postSubscription.unsubscribe();
  }

}
