import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatePostComponent } from './create-post.component';
import { HttpClientModule } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';

describe('CreatePostComponent', () => {
  let component: CreatePostComponent;
  let fixture: ComponentFixture<CreatePostComponent>;
  let authServiceMock: any;

  beforeEach(async () => {
    authServiceMock = {
      getCurrentUser: jest.fn().mockReturnValue({ token: 'fake-token' })
    };

    await TestBed.configureTestingModule({
      declarations: [CreatePostComponent],
      imports: [
        HttpClientModule
      ],
      providers: [
        { provide: AuthService, useValue: authServiceMock }
      ]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CreatePostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
