import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginRequest } from '../models/auth/login-request';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { LoginResponse } from '../models/auth/login-response';
import { SignupRequest } from '../models/auth/signup-request';
import { Router } from '@angular/router';
import { SignupResponse } from '../models/auth/signup-response';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private loggedIn = new BehaviorSubject<boolean>(false);
  public pathService = 'http://localhost:8080/auth';

  constructor(private httpClient: HttpClient, private router: Router) {
    let currentUser = localStorage.getItem('currentUser');

    if (currentUser) {
      this.loggedIn.next(true);
    }

  }

  public login(loginRequest: LoginRequest): Observable<LoginResponse> {
    return this.httpClient.post<LoginResponse>(`${this.pathService}/login`, loginRequest).pipe(
      tap((response: LoginResponse) => {
        this.loggedIn.next(true);
      })
    );
  }

  public register(request: SignupRequest): Observable<SignupResponse> {
    return this.httpClient.post<SignupResponse>(`${this.pathService}/register`, request);
  }

  public updateCredentials(request: SignupRequest, token?: string): Observable<LoginResponse> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json; charset=utf-8',
      'Authorization': `Bearer ${token}`
    });

    return this.httpClient.put<LoginResponse>(`${this.pathService}/update`, request, { headers: headers});
  }

  isLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }

  logout(): void {
    this.loggedIn.next(false);
    localStorage.removeItem('currentUser');
    this.router.navigate(["/home"]);
  }

  getCurrentUser(): LoginResponse {
    let currentUser = localStorage.getItem('currentUser');
    return currentUser ? JSON.parse(currentUser) : null;
  }

  getToken(): string | null {
    let currentUser = this.getCurrentUser();
    return currentUser ? currentUser.token : null;
  }

}
