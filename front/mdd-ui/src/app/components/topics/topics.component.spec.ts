import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopicsComponent } from './topics.component';
import { HttpClientModule } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';

describe('TopicsComponent', () => {
  let component: TopicsComponent;
  let fixture: ComponentFixture<TopicsComponent>;
  let authServiceMock: any;

  beforeEach(async () => {
    authServiceMock = {
      getCurrentUser: jest.fn().mockReturnValue({ token: 'fake-token' })
    };

    await TestBed.configureTestingModule({
      declarations: [TopicsComponent],
      imports: [
        HttpClientModule
      ],
      providers: [
        { provide: AuthService, useValue: authServiceMock }
      ]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TopicsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
