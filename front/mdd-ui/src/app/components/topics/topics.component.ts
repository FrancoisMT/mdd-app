import { Component, OnDestroy, OnInit } from '@angular/core';
import { TopicService } from '../../services/topic.service';
import { AuthService } from '../../services/auth.service';
import { LoginResponse } from '../../models/auth/login-response';
import { Topic } from '../../models/topic';
import { forkJoin, Subscription } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-topics',
  templateUrl: './topics.component.html',
  styleUrl: './topics.component.css'
})
export class TopicsComponent implements OnInit, OnDestroy {
  currentUser!: LoginResponse;
  onError: boolean = false;
  isLoading: boolean = false;
  errorMessage: string = "";
  allTopics: Topic[] = [];
  userTopics: Topic[] = [];
  subscriptions: Subscription = new Subscription();


  constructor(
    private authService: AuthService,
    private topicService: TopicService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    if (this.currentUser.token) {
      this.loadTopicsData();
    }
  }

  loadTopicsData() {
    this.isLoading = true;
    const topicsSubscription: Subscription = forkJoin({
      allTopics: this.topicService.getAllTopics(this.currentUser.token),
      userTopics: this.topicService.getUserTopics(this.currentUser.token)
    }).subscribe({
      next: (results) => {
        this.allTopics = results.allTopics;
        this.userTopics = results.userTopics.map(subscription => subscription.topic);
        this.isLoading = false;
      },
      error: (error) => {
        this.isLoading = false;
        this.onError = true;
        this.errorMessage = "Erreur : une erreur est survenue lors de la récupération des thèmes et abonnements";
      }
    });

    this.subscriptions.add(topicsSubscription);
  }

  isSubscribed(topicId: number): boolean {
    return this.userTopics.some(topic => topic.id === topicId);
  }

  subscribe(topic: Topic) {
    this.isLoading = true;

    const topicSubsribeSubscription: Subscription = this.topicService.subscribeToTopic(topic.id, this.currentUser.token).subscribe({
      next: (response) => {
        this.loadTopicsData();
        this.snackBar.open('Vous êtes maintenant abonné à ' + topic.title + '.', 'Fermer', {
          duration: 5000,
          verticalPosition: 'top',
        });
      },
      error: (error) => {
        this.isLoading = false;
        this.onError = true;
        this.errorMessage = "Erreur : une erreur est survenue lors de l'abonnement au thème";
      },
    });

    this.subscriptions.add(topicSubsribeSubscription);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}
