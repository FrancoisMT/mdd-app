import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service'; 
import { TopicService } from '../../services/topic.service'; 
import { ProfilComponent } from './profil.component';
import { LoginResponse } from '../../models/auth/login-response';
import { Subscription } from '../../models/subscription';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Topic } from '../../models/topic';

describe('ProfilComponent', () => {
  let component: ProfilComponent;
  let fixture: ComponentFixture<ProfilComponent>;
  let authService: AuthService;
  let topicService: TopicService;
  let snackBar: MatSnackBar;
  let fb: FormBuilder;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProfilComponent],
      imports: [HttpClientTestingModule, ReactiveFormsModule],
      providers: [
        AuthService,
        TopicService,
        FormBuilder,
        { provide: MatSnackBar, useValue: { open: jest.fn() } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ProfilComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    topicService = TestBed.inject(TopicService);
    snackBar = TestBed.inject(MatSnackBar);
    fb = TestBed.inject(FormBuilder);

    const mockUser: LoginResponse = { 
      id: 1,
      mail: "test@test.fr",
      username: "fake-user",
      token: 'fake-token'
    };    
    jest.spyOn(authService, 'getCurrentUser').mockReturnValue(mockUser);

    fixture.detectChanges();

    component.currentUser = mockUser;  // Ajouté pour s'assurer que le currentUser est initialisé
    component.initForm(mockUser);  // Initialisation du formulaire
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should call getCurrentUser and initForm if mail and username are present', () => {
      jest.spyOn(component, 'initForm');
      jest.spyOn(component, 'loadUserSubscription');

      component.ngOnInit();

      expect(authService.getCurrentUser).toHaveBeenCalled();
      expect(component.initForm).toHaveBeenCalledWith(component.currentUser);
      expect(component.loadUserSubscription).toHaveBeenCalled();
    });
  });

  describe('onSubmit', () => {
    it('should handle error when updateCredentials fails', () => {

      jest.spyOn(authService, 'updateCredentials').mockReturnValue(throwError(() => new Error('Test error')));
      jest.spyOn(component, 'handleError');

      component.ngOnInit();
      component.userForm.setValue({ email: 'updated@test.com', username: 'updateduser', password: 'Valid1@Password' });

      component.onSubmit();

      expect(authService.updateCredentials).toHaveBeenCalledWith({ email: 'updated@test.com', username: 'updateduser', password: 'Valid1@Password' }, 'fake-token');
      expect(component.isLoading).toBe(false);
      expect(component.handleError).toHaveBeenCalled();
    });
  });

  describe('loadUserSubscription', () => {
    it('should load user subscriptions successfully', () => {
     
      const mockTopic: Topic = {
        id: 1,
        title: "Topic1",
        description: "Description du topic 1"
      };   
      const mockSubscriptions: Subscription[] = [{ id: 1, topic: mockTopic}];

      jest.spyOn(topicService, 'getUserTopics').mockReturnValue(of(mockSubscriptions));

      component.ngOnInit();

      expect(topicService.getUserTopics).toHaveBeenCalledWith('fake-token');
      expect(component.userTopics).toEqual(mockSubscriptions);
      expect(component.isLoading).toBe(false);
    });

    it('should handle error when getUserTopics fails', () => {

      jest.spyOn(topicService, 'getUserTopics').mockReturnValue(throwError(() => new Error('Test error')));

      component.ngOnInit();

      expect(topicService.getUserTopics).toHaveBeenCalledWith('fake-token');
      expect(component.isLoading).toBe(false);
      expect(component.onError).toBe(true);
      expect(component.errorMessage).toBe("Erreur : une erreur est survenue lors de la récupération des abonnements");
    });
  });

  describe('unsubscribeTopic', () => {
    it('should unsubscribe from topic successfully', () => {
    
      const mockTopic: Topic = {
        id: 1,
        title: "Topic1",
        description: "Description du topic 1"
      };
      const mockSubscription: Subscription = { id: 1, topic: mockTopic };

      jest.spyOn(topicService, 'unsubscribe').mockReturnValue(of({}));
      jest.spyOn(snackBar, 'open');
      jest.spyOn(component, 'loadUserSubscription');

      component.ngOnInit();
      component.unsubscribeTopic(mockSubscription);

      expect(topicService.unsubscribe).toHaveBeenCalledWith(1, 'fake-token');
      expect(snackBar.open).toHaveBeenCalledWith('Vous vous êtes désabonné de ce topic', 'Fermer', { duration: 3000, verticalPosition: 'top' });
      expect(component.isLoading).toBe(false);
      expect(component.loadUserSubscription).toHaveBeenCalled();
    });

    it('should handle error when unsubscribe fails', () => {
     
      const mockTopic: Topic = {
        id: 1,
        title: "Topic1",
        description: "Description du topic 1"
      };
      const mockSubscription: Subscription = { id: 1, topic: mockTopic};

      jest.spyOn(topicService, 'unsubscribe').mockReturnValue(throwError(() => new Error('Test error')));

      component.ngOnInit();
      component.unsubscribeTopic(mockSubscription);

      expect(topicService.unsubscribe).toHaveBeenCalledWith(1, 'fake-token');
      expect(component.isLoading).toBe(false);
      expect(component.onError).toBe(true);
      expect(component.errorMessage).toBe("Erreur : une erreur est survenue lors du désabonnement");
    });
  });

  describe('handleError', () => {
    it('should set appropriate error message', () => {
      const error409 = { status: 409 };
      const errorOther = { status: 500 };

      component.handleError(error409);
      expect(component.errorMessage).toBe('Erreur : un mail est déjà associé à ce compte.');
      expect(component.onError).toBe(true);

      component.handleError(errorOther);
      expect(component.errorMessage).toBe('Erreur : une erreur est survenue lors de la modification des données.');
      expect(component.onError).toBe(true);
    });
  });
});
``
