import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Subscription } from '../models/subscription';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TopicService {
  private subscriptionPath: string = 'http://localhost:8080/subscription';

  constructor(private httpClient: HttpClient) { }

  getUserTopics(token?: string): Observable<Subscription[]> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.httpClient.get<Subscription[]>(`${this.subscriptionPath}/user/all`, { headers: headers });
  }

}
