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
  ) { }

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

  toggleWatchlist(movieId: string): void {
    if (this.isInWatchlist(movieId)) {
      this.removeFromWatchlist(movieId);
    } else {
      this.addToWatchlist(movieId);
    }
  }

  addToWatchlist(movieId: string): void {
    let data = { userId: this.authService.getUser().id, movieId: movieId };
    this.apiService.addToWatchlist(data).subscribe({
      next: (response) => {
        console.log(response.message);
        this.loadWatchlist();
      },
      error: (err) => {
        console.error('Failed to toggle watchlist:', err);
      }
    });
  }

  removeFromWatchlist(movieId: string): void {
    let data = { userId: this.authService.getUser().id, movieId: movieId };
    this.apiService.removeFromWatchlist(data).subscribe({
      next: (response) => {
        console.log(response.message);
        this.loadWatchlist();
      },
      error: (err) => {
        console.error('Failed to remove from watchlist:', err);
      }
    });
  }

  isInWatchlist(movieId: string): boolean {
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

  shareRecommendation(movie: Movie): void {
    const shareUrl = `${globalThis.location.origin}/movies/${movie.id}`;
    const shareData = {
      title: movie.title,
      text: `Check out "${movie.title}" on Neo4flix!`,
      url: shareUrl
    };

    // Check if the browser supports native sharing (mobile/Safari/Chrome)
    if (navigator.share) {
      navigator.share(shareData)
        .then(() => console.log('Movie shared successfully'))
        .catch((error) => console.error('Error sharing:', error));
    } else {
      // Fallback for Desktop: Copy to Clipboard
      this.copyToClipboard(shareUrl);
    }
  }

  private copyToClipboard(url: string): void {
    navigator.clipboard.writeText(url).then(() => {
      // You might want to use a MatSnackBar or a Toast here instead of alert
      alert('Share link copied to clipboard!');
    }).catch(err => {
      console.error('Could not copy text: ', err);
    });
  }

  getUserRating(movie: Movie): number {
    // rated is not define or empty return 0
    if (!movie.rated || movie.rated.length === 0) {
      return 0;
    }
    return movie.rated.find(r => r.user.id === this.authService.getUser().id)?.rating || 0;
  }
}
