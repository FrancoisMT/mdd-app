import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Subscription } from '../models/subscription';
import { Observable } from 'rxjs';
import { Topic } from '../models/topic';

@Injectable({
  providedIn: 'root'
})
export class TopicService {
  public subscriptionPath: string = 'http://localhost:8080/subscription';
  public topicPath: string = 'http://localhost:8080/topic';

  constructor(private httpClient: HttpClient) { }

  getUserTopics(token?: string): Observable<Subscription[]> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.httpClient.get<Subscription[]>(`${this.subscriptionPath}/user/all`, { headers: headers });
  }

  unsubscribe(id?: number, token?: string) : Observable<Object> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.httpClient.delete(`${this.subscriptionPath}/unsubscribe/${id}`, { headers: headers });
  }

  getAllTopics(token?: string) :  Observable<Topic[]> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.httpClient.get<Topic[]>(`${this.topicPath}/list`, { headers: headers });
  }

  subscribeToTopic(id: number, token: string) : Observable<Object> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.httpClient.post(`${this.subscriptionPath}/subscribe/${id}`, {}, { headers: headers });
  }

}
