import { Injectable } from '@angular/core';
import { Movie, MovieSearchCriteria } from '../model/movie.model';
import { ApiService } from './api.service';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class MovieService {
  Watchlist: Movie[] = [];
  geners: string[] = [];
  years: number[] = [];
  recommendedMovies: Movie[] = [];

  constructor(
    private readonly apiService: ApiService,
    private readonly authService: AuthService
  ) {
    this.loadWatchlist();
  }

  loadWatchlist(): void {
        this.apiService.getWatchlist(this.authService.getUser().id).subscribe({
      next: (response) => {
        console.log(response.message);
        this.Watchlist = response.watchlist;
      },
      error: (err) => {
        console.error('Failed to load watchlist:', err);
      }
    });
  }

  filterMovies(criteria: MovieSearchCriteria, movies: Movie[]): Movie[] {
    return movies.filter(movie => {
      const matchesTitle = criteria.title ? movie.title.toLowerCase().includes(criteria.title.toLowerCase()) : true;
      const matchesGenre = criteria.genre ? movie.genres.some(g => g.name.toLowerCase() === criteria.genre.toLowerCase()) : true;
      const matchesYear = criteria.year ? movie.released === criteria.year : true;
      return matchesTitle && matchesGenre && matchesYear;
    });
  }

  toggleWatchlist(movieId: number): void {
    if (this.isInWatchlist(movieId)) {
      this.removeFromWatchlist(movieId);
    } else {
      this.addToWatchlist(movieId);
    }
    this.loadWatchlist();
  }

  addToWatchlist(movieId: number): void {
    let data = { userId: this.authService.getUser().id, movieId: movieId };
    this.apiService.addToWatchlist(data).subscribe({
      next: (response) => {
        console.log(response.message);
      },
      error: (err) => {
        console.error('Failed to toggle watchlist:', err);
      }
    });
  }

  removeFromWatchlist(movieId: number): void {
    let data = { userId: this.authService.getUser().id, movieId: movieId };
    this.apiService.removeFromWatchlist(data).subscribe({
      next: (response) => {
        console.log(response.message);
      },
      error: (err) => {
        console.error('Failed to remove from watchlist:', err);
      }
    });
  }

  isInWatchlist(movieId: number): boolean {
    return this.Watchlist.some(movie => movie.id === movieId);
  }

  getGenres(list: string[]): void {
    this.geners = list;
  }

  getYears(list: number[]): void {
    this.years = list;
  }

  getAvailableGenres(): string[] {
    return this.geners;
  }

  getAvailableYears(): number[] {
    // Implement logic to fetch available release years from the API or return a static list
    return this.years;
  }

  getRecommendations(): Movie[] {
    this.apiService.getContentBasedRecommendations(this.authService.getUser().id).subscribe({
      next: (response) => {
        console.log(response.message);
        this.recommendedMovies = response.recommendations;
      },
      error: (err) => {
        console.error('Failed to fetch recommendations:', err);
      }
    });
    return this.recommendedMovies;
  }

  shareRecommendation(movieId: number): string {
    // Implement logic to share movie recommendation

    return `Check out this movie with ID: ${movieId}!`;
  }

  getUserRating(movie: Movie): number {
    return movie.rated.find(r => r.user.id === this.authService.getUser().id)?.rating || 0;
  }
}
