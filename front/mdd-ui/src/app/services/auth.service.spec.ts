import { TestBed } from '@angular/core/testing';

import { AuthService } from './auth.service';
import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LoginRequest } from '../models/auth/login-request';
import { LoginResponse } from '../models/auth/login-response';
import { SignupRequest } from '../models/auth/signup-request';


describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        HttpClientTestingModule
      ],
      providers: [
        AuthService
      ]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);

  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should send a POST request to the login endpoint', () => {
    const request: LoginRequest = {
      email: "test@test.fr",
      password: "testPwd"
    }

    const apiResponse: LoginResponse = {
      id:1,
      mail:"test@test.fr",
      username:"test",
      token:"abcd"
    }

    service.login(request).subscribe(response => {
      expect(response).toEqual(apiResponse);
    });

    const req = httpMock.expectOne(`${service.pathService}/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(request);

    req.flush(apiResponse);

  });

  it('should send a POST request to the register endpoint', () => {
    const request: SignupRequest = {
      email:"test@test.fr",
      password: "testPwd",
      username:"test",
    }

    service.register(request).subscribe();

    const req = httpMock.expectOne(`${service.pathService}/register`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(request);

    req.flush(null);

  })

  it('should send a PUT request to the update endpoint with the correct headers', () => {
    const request: SignupRequest = {
      email: "update@test.fr",
      password: "updatePwd",
      username: "updateUser"
    };

    const token = "abcd1234";

    const apiResponse: LoginResponse = {
      id: 1,
      mail: "update@test.fr",
      username: "updateUser",
      token: "abcd1234"
    };

    service.updateCredentials(request, token).subscribe(response => {
      expect(response).toEqual(apiResponse);
    });

    const req = httpMock.expectOne(`${service.pathService}/update`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(request);
    expect(req.request.headers.get('Content-Type')).toBe('application/json; charset=utf-8');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${token}`);

    req.flush(apiResponse);

  })

});
