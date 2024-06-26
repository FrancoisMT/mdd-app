import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { filter } from 'rxjs';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  isLoggedIn: boolean = false;
  activeItem: string = 'dashboard';
  isMenuOpen: boolean = false;

  constructor(private authService: AuthService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) { }

  ngOnInit() {
    this.authService.isLoggedIn().subscribe((loggedIn) => {
      this.isLoggedIn = loggedIn;
    });

    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        // Récupérer l'url active pour déterminer l'élément actif
        const path = this.activatedRoute.snapshot.firstChild?.routeConfig?.path;
        if (path === 'dashboard') {
          this.activeItem = 'dashboard';
        } else if (path === 'profil') {
          this.activeItem = 'profil';
        } else if (path === 'topics') {
          this.activeItem = 'topics';
        } else {
          this.activeItem = 'dashboard';
        }
      });

  }

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  goToDashboard() {
    this.activeItem = 'dashboard';
    this.router.navigate(['dashboard']);
    this.closeMenu();
  }

  goToTopics() {
    this.activeItem = 'topics';
    this.router.navigate(['topics']);
    this.closeMenu();
  }

  goToProfile() {
    this.activeItem = 'profil';
    this.router.navigate(['profil']);
    this.closeMenu();
  }

  closeMenu() {
    this.isMenuOpen = false;
  }

}
