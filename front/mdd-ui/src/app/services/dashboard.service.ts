import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Post, PostDetail, PostRequest, PostResponse } from '../models/post';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private path: string = 'http://localhost:8080/posts';


  constructor(private httpClient: HttpClient) { }

  getPosts(token: string) : Observable<Post[]> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.httpClient.get<Post[]>(`${this.path}/all`, { headers: headers });
  }

  getPost(id: number, token: string): Observable<PostDetail> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.httpClient.get<PostDetail>(`${this.path}/${id}`, { headers: headers });
  }

  createPost(request: PostRequest, token: string): Observable<PostResponse> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.httpClient.post<PostResponse>(`${this.path}/create`, request, { headers: headers });
  }

}
