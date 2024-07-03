import { TestBed } from '@angular/core/testing';

import { DashboardService } from './dashboard.service';
import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Post, PostDetail, PostRequest, PostResponse, UserInfo } from '../models/post';
import { Topic } from '../models/topic';
import { CommentRequest } from '../models/comment';


describe('DashboardService', () => {
  let service: DashboardService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        HttpClientTestingModule
      ],
      providers: [
        DashboardService
      ]
    });
    service = TestBed.inject(DashboardService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should send a GET request to the get all posts endpoint', () => {
    const token = "abcd1234";
    const user: UserInfo = {
      id: 1,
      username: "testuser"
    };
    const topic: Topic = {
      id: 1,
      title: "Topic1",
      description: "Description du topic 1"
    };
    const apiResponse: Post[] = [
      {
        id: 1,
        title: "post1",
        description: "description du post 1",
        date: "date1",
        topic: topic,
        user: user,
        comments: []
      },
      {
        id: 2,
        title: "post2",
        description: "description du post 2",
        date: "date2",
        topic: topic,
        user: user,
        comments: []
      }
    ];

    service.getPosts(token).subscribe(response => {
      expect(response).toEqual(apiResponse);
    });

    const req = httpMock.expectOne(`${service.path}/all`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${token}`);

    req.flush(apiResponse);

  });

  it('should send a GET request to the get post endpoint', () => {
    const id = 1;
    const token = "testToken1234";
    const user: UserInfo = {
      id: 1,
      username: "testuser"
    };
    const topic: Topic = {
      id: 1,
      title: "Topic1",
      description: "Description du topic 1"
    };
    const apiResponse: PostDetail = {
      id: 1,
      title: "post1",
      description: "description du post 1",
      date: "date1",
      topic: topic,
      user: user,
      comments: []
    };

    service.getPost(id, token).subscribe(response => {
      expect(response).toEqual(apiResponse);
    });

    const req = httpMock.expectOne(`${service.path}/${id}`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${token}`);

    req.flush(apiResponse);
  })

  it('should send a POST request to the create post endpoint', () => {
    const token = "abcd1234";
    const request: PostRequest = {
      title: "new Post",
      description: "New Post Description",
      topicId: 1
    };
    const apiResponse: PostResponse = {
      id: 1,
      title: "post1",
      description: "description du post 1",
      userId: 1,
      topicId: 1
    }

    service.createPost(request, token).subscribe(response => {
      expect(response).toEqual(apiResponse);
    });

    const req = httpMock.expectOne(`${service.path}/create`);
    expect(req.request.method).toBe('POST');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${token}`);

    req.flush(apiResponse);
  })

  it('should send a POST request to the create comment endpoint', () => {
    const token = "abcd1234";
    const id = 1;
    const request: CommentRequest = {
      content: "test content"
    }

    service.addComment(id, request, token).subscribe();

    const req = httpMock.expectOne(`${service.commentsPath}/${id}/create`);
    expect(req.request.method).toBe('POST');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${token}`);

    req.flush(null);
  })

});
