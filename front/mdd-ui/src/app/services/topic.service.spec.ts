import { TestBed } from '@angular/core/testing';

import { TopicService } from './topic.service';
import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Topic } from '../models/topic';
import { Subscription } from '../models/subscription';

describe('TopicService', () => {
  let service: TopicService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        HttpClientTestingModule
      ],
      providers: [
        TopicService
      ]
    });
    service = TestBed.inject(TopicService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should send a GET request to the get user topics endpoint', () => {
    const token = "abcd1234";
    const topic: Topic = {
      id: 1,
      title: "Topic1",
      description: "Description du topic 1"
    };
    const apiResponse: Subscription[] = [
      {
        id:1,
        topic: topic
      }, 
      {
        id:2,
        topic: topic
      }
    ];

    service.getUserTopics(token).subscribe(response => {
      expect(response).toEqual(apiResponse);
    });

    const req = httpMock.expectOne(`${service.subscriptionPath}/user/all`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${token}`);

    req.flush(apiResponse);
  });

  it('should send a DELETE request to the unsubscribe endpoint', () => {
    const token = "abcd1234";
    const id = 1;

    service.unsubscribe(id, token).subscribe();

    const req = httpMock.expectOne(`${service.subscriptionPath}/unsubscribe/${id}`);
    expect(req.request.method).toBe('DELETE');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${token}`);

    req.flush(null);
  });

  it('should send a GET request to the get topics list endpoint', () => {
    const token = "abcd1234";
    const apiResponse : Topic[] = [
      {
        id: 1,
        title: "Topic 1",
        description: "Description du topic 1"
      },
      {
        id: 2,
        title: "Topic 2",
        description: "Description du topic 2"
      }
    ];

    service.getAllTopics(token).subscribe(response => {
      expect(response).toEqual(apiResponse);
    });

    const req = httpMock.expectOne(`${service.topicPath}/list`);
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${token}`);

    req.flush(apiResponse);
  });

  it('should send a POST request to the subscribe topic endpoint', () => {
    const token = "abcd1234";
    const id = 1;

    service.subscribeToTopic(id, token).subscribe();

    const req = httpMock.expectOne(`${service.subscriptionPath}/subscribe/${id}`);
    expect(req.request.method).toBe('POST');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${token}`);

    req.flush(null);
  })


});
