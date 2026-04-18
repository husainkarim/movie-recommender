import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private readonly baseUrl = 'http://localhost:8080/api';
  constructor(
    private readonly http: HttpClient,
    private readonly authService: AuthService
  ) {}

  encodedId(id: string): string {
    return encodeURIComponent(id);
  }
  // user-service API calls
  // register
  register(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/users/auth/register`, data);
  }
  // login
  login(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/users/auth/login`, data);
  }
  // profile
  getProfile(id: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/users/profile/${this.encodedId(id)}`, { headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${this.authService.getToken()}` } });
  }
  // add to watchlist
  addToWatchlist(request: any): Observable<any> {
    console.log(this.authService.getToken());
    return this.http.post(`${this.baseUrl}/users/watchlist`, request, { headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${this.authService.getToken()}` } });
  }
  // remove from watchlist
  removeFromWatchlist(request: any): Observable<any> {
    return this.http.delete(`${this.baseUrl}/users/watchlist`, { body: request, headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${this.authService.getToken()}` } });
  }
  // get watchlist
  getWatchlist(id: string): Observable<any> {
    console.log(this.authService.getToken());
    return this.http.get(`${this.baseUrl}/users/watchlist/${this.encodedId(id)}`, { headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${this.authService.getToken()}` } });
  }

  // movie-service API calls
  // get movies list
  getMovies(): Observable<any> {
    return this.http.get(`${this.baseUrl}/movies`, { headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${this.authService.getToken()}` } });
  }
  // get movie details
  getMovieDetails(id: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/movies/${this.encodedId(id)}`, { headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${this.authService.getToken()}` } });
  }
  // add movie (admin only)
  addMovie(movie: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/movies`, movie, { headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${this.authService.getToken()}` } });
  }
  // get person details
  getPersonDetails(id: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/persons/${this.encodedId(id)}`, { headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${this.authService.getToken()}` } });
  }

  // rating-service API calls
  // submit rating
  submitRating(request: any): Observable<any> {
    console.log(this.authService.getToken());
    return this.http.post(`${this.baseUrl}/ratings`, request, { headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${this.authService.getToken()}` } });
  }
  // remove rating
  removeRating(request: any): Observable<any> {
    return this.http.delete(`${this.baseUrl}/ratings`, { body: request, headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${this.authService.getToken()}` } });
  }

  // recommendation-service API calls
  // get collaborative recommendations
  getCollaborativeRecommendations(userId: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/recommendations/collaborative/${this.encodedId(userId)}`, { headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${this.authService.getToken()}` } });
  }
  // get content-based recommendations
  getContentBasedRecommendations(userId: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/recommendations/content/${this.encodedId(userId)}`, { headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${this.authService.getToken()}` } });
  }
  // get similar recommendations
  getSimilarRecommendations(movieId: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/recommendations/similar/${this.encodedId(movieId)}`, { headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${this.authService.getToken()}` } });
  }
}
