import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { DashboardService } from '../../services/dashboard.service'; 
import { PostDetailComponent } from './post-detail.component';
import { LoginResponse } from '../../models/auth/login-response'; 
import { PostDetail } from '../../models/post'; 
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('PostDetailComponent', () => {
  let component: PostDetailComponent;
  let fixture: ComponentFixture<PostDetailComponent>;
  let authService: AuthService;
  let dashboardService: DashboardService;
  let route: ActivatedRoute;
  let router: Router;
  let fb: FormBuilder;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PostDetailComponent],
      imports: [HttpClientTestingModule, ReactiveFormsModule],
      providers: [
        AuthService,
        DashboardService,
        FormBuilder,
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: jest.fn().mockReturnValue('1') } } } },
        { provide: Router, useValue: { navigate: jest.fn() } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(PostDetailComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    dashboardService = TestBed.inject(DashboardService);
    route = TestBed.inject(ActivatedRoute);
    router = TestBed.inject(Router);
    fb = TestBed.inject(FormBuilder);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should call getCurrentUser and loadPostDetails if id is present', () => {
      const mockUser: LoginResponse = { 
        id: 1,
        mail: "test@test.fr",
        username: "fake-user",
        token: 'fake-token'
      };
      const mockPost: PostDetail = {
        id: 1,
        title: "post1",
        description: "description du post 1",
        date: "2023-01-01",
        topic: { id: 1, title: "Topic1", description: "Description du topic 1" },
        user: mockUser,
        comments: []
      };

      jest.spyOn(authService, 'getCurrentUser').mockReturnValue(mockUser);
      jest.spyOn(dashboardService, 'getPost').mockReturnValue(of(mockPost));
      jest.spyOn(component, 'initForm');

      component.ngOnInit();

      expect(authService.getCurrentUser).toHaveBeenCalled();
      expect(dashboardService.getPost).toHaveBeenCalledWith(1, 'fake-token');
      expect(component.post).toEqual(mockPost);
      expect(component.initForm).toHaveBeenCalled();
    });

    it('should handle error when getPost fails', () => {
      const mockUser: LoginResponse = { 
        id: 1,
        mail: "test@test.fr",
        username: "fake-user",
        token: 'fake-token'
      };

      jest.spyOn(authService, 'getCurrentUser').mockReturnValue(mockUser);
      jest.spyOn(dashboardService, 'getPost').mockReturnValue(throwError(() => new Error('Test error')));

      component.ngOnInit();

      expect(authService.getCurrentUser).toHaveBeenCalled();
      expect(dashboardService.getPost).toHaveBeenCalledWith(1, 'fake-token');
      expect(component.isLoading).toBe(false);
      expect(component.onError).toBe(true);
      expect(component.errorMessage).toBe("Erreur : une erreur est survenue lors du chargement de l'article");
    });
  });

  describe('onSubmit', () => {
    it('should add a comment successfully', () => {
      const mockUser: LoginResponse = { 
        id: 1,
        mail: "test@test.fr",
        username: "fake-user",
        token: 'fake-token'
      };
      const mockResponse = {};

      jest.spyOn(authService, 'getCurrentUser').mockReturnValue(mockUser);
      jest.spyOn(dashboardService, 'addComment')
      jest.spyOn(component, 'loadPostDetails');
      jest.spyOn(component, 'initForm');

      component.ngOnInit();
      component.commentForm.setValue({ content: 'test comment' });

      component.onSubmit();

      expect(dashboardService.addComment).toHaveBeenCalledWith(1, { content: 'test comment' }, 'fake-token');
      expect(component.loadPostDetails).toHaveBeenCalled();
      expect(component.initForm).toHaveBeenCalled();
      expect(component.isLoading).toBe(true);
    });

    it('should handle error when addComment fails', () => {
      const mockUser: LoginResponse = { 
        id: 1,
        mail: "test@test.fr",
        username: "fake-user",
        token: 'fake-token'
      };

      jest.spyOn(authService, 'getCurrentUser').mockReturnValue(mockUser);
      jest.spyOn(dashboardService, 'addComment').mockReturnValue(throwError(() => new Error('Test error')));

      component.ngOnInit();
      component.commentForm.setValue({ content: 'test comment' });

      component.onSubmit();

      expect(dashboardService.addComment).toHaveBeenCalledWith(1, { content: 'test comment' }, 'fake-token');
      expect(component.isLoading).toBe(false);
      expect(component.onError).toBe(true);
      expect(component.errorMessage).toBe("Erreur : une erreur est survenue lors de l'ajout du commentaire");
    });
  });

  describe('back', () => {
    it('should navigate to dashboard', () => {
      component.back();
      expect(router.navigate).toHaveBeenCalledWith(['/dashboard']);
    });
  });
});
