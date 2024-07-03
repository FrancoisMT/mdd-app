import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service'; 
import { DashboardService } from '../../services/dashboard.service';
import { DashboardComponent } from './dashboard.component';
import { LoginResponse } from '../../models/auth/login-response';
import { Post } from '../../models/post'; 
import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Router } from '@angular/router';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let authService: AuthService;
  let dashboardService: DashboardService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DashboardComponent],
      imports: [
        HttpClientModule,
        HttpClientTestingModule
      ],
      providers: [
        { provide: AuthService, useValue: { getCurrentUser: jest.fn() } },
        { provide: DashboardService, useValue: { getPosts: jest.fn() } },
        { provide: Router, useValue: { navigate: jest.fn() } }
      ]
    }).compileComponents();
    
    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    dashboardService = TestBed.inject(DashboardService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {

    it('should call getCurrentUser and loadUserPosts if token is present', () => {
      const mockUser: LoginResponse = { 
        id: 1,
        mail: "test@test.fr",
        username: "fake-user",
        token: 'fake-token' 
      };
      const mockTopic = {
        id: 1,
        title: "Topic1",
        description: "Description du topic 1"
      };
      const mockPosts: Post[] = [
        {
          id: 1,
          title: "post1",
          description: "description du post 1",
          date: "2023-01-01",
          topic: mockTopic,
          user: mockUser,
          comments: []
        },
        {
          id: 2,
          title: "post2",
          description: "description du post 2",
          date: "2023-01-02",
          topic: mockTopic,
          user: mockUser,
          comments: []
        }
      ];

      jest.spyOn(authService, 'getCurrentUser').mockReturnValueOnce(mockUser);
      jest.spyOn(dashboardService, 'getPosts').mockReturnValueOnce(of(mockPosts));

      component.ngOnInit();

      expect(authService.getCurrentUser).toHaveBeenCalled();
      expect(dashboardService.getPosts).toHaveBeenCalledWith('fake-token');
      expect(component.posts).toEqual(mockPosts);
    });

    it('should not loadUserPosts if token is not present', () => {
      const mockUser: LoginResponse = { 
        id: 1,
        mail: "test@test.fr",
        username: "fake-user",
        token: ''  // No token provided
      };

      jest.spyOn(authService, 'getCurrentUser').mockReturnValue(mockUser);
      const getPostsSpy = jest.spyOn(dashboardService, 'getPosts');

      component.ngOnInit();

      expect(authService.getCurrentUser).toHaveBeenCalled();
      expect(getPostsSpy).not.toHaveBeenCalled();
    });

    it('should handle error when getPosts fails', () => {
      const mockUser: LoginResponse = { 
        id: 1,
        mail: "test@test.fr",
        username: "fake-user",
        token: "fake-token"  // No token provided
      };

      jest.spyOn(authService, 'getCurrentUser').mockReturnValue(mockUser);
      jest.spyOn(dashboardService, 'getPosts').mockReturnValue(throwError(() => new Error('Test error')));

      component.ngOnInit();

      expect(authService.getCurrentUser).toHaveBeenCalled();
      expect(dashboardService.getPosts).toHaveBeenCalledWith('fake-token');
      expect(component.isLoading).toBe(false);
      expect(component.onError).toBe(true);
      expect(component.errorMessage).toBe("Erreur : une erreur est survenue lors de la récupération de votre fil d'actualité");
    });

  });
});
