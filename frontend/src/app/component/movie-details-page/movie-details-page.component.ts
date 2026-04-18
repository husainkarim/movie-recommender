import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Movie } from '../../model/movie.model';
import { MovieService } from '../../service/movie.service';
import { ApiService } from '../../service/api.service';
import { AuthService } from '../../service/auth.service';
import { AppRatingChartComponent } from '../app-rating-chart/app-rating-chart.component';

@Component({
  selector: 'app-movie-details-page',
  standalone: true,
  imports: [RouterLink, AppRatingChartComponent],
  templateUrl: './movie-details-page.component.html',
  styleUrl: './movie-details-page.component.scss'
})
export class MovieDetailsPageComponent implements OnInit {
  movie?: Movie;
  ratingOptions = [1, 2, 3, 4, 5];
  selectedRating = 0;
  shareMessage = '';
  isLoading = false;
  infoMessage = '';

  constructor(
    private readonly route: ActivatedRoute,
    private readonly movieService: MovieService,
    private readonly apiService: ApiService,
    private readonly authService: AuthService
  ) {
  }

  ngOnInit(): void {
    const movieId = this.route.snapshot.paramMap.get('id') || '';
    this.isLoading = true;
    this.apiService.getMovieDetails(movieId).subscribe({
      next: (response) => {
        console.log(response.message);
        this.movie = response.movie;
        console.log('Loaded movie details:', this.movie?.ratings);
        this.selectedRating = this.movie ? this.movieService.getUserRating(this.movie) : 0;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Failed to load movie details:', error.message);
        this.infoMessage = 'Could not load this movie right now.';
        this.isLoading = false;
      }
    });
  }

  getGenres(movie: Movie): string {
    return movie.genres.map((g) => g.name).join(' • ') || '';
  }

  getActors(movie: Movie): string {
    return movie.actors.map((a) => a.name).join(', ') || '';
  }

  toggleWatchlist(): void {
    if (!this.movie) {
      return;
    }

    this.movieService.toggleWatchlist(this.movie.id);
  }

  isInWatchlist(): boolean {
    if (!this.movie) {
      return false;
    }

    return this.movieService.isInWatchlist(this.movie.id);
  }

  shareMovie(): void {
    if (!this.movie) {
      return;
    }

    this.movieService.shareRecommendation(this.movie);
    this.shareMessage = `Sharing ${this.movie.title}. Link copy fallback is available.`;
  }

  setRating(rating: number): void {
    if (!this.movie) {
      return;
    }
    let data = {
      movieId: this.movie.id,
      rating: rating,
      userId: this.authService.getUser().id
    }
    if (this.selectedRating === rating) {
      // If the same rating is selected again, remove the rating
      this.apiService.removeRating(data).subscribe({
        next: (response) => {
          console.log(response.message);
          this.selectedRating = 0;
          this.infoMessage = 'Your rating was removed.';
        },
        error: (error) => {
          console.error('Failed to remove rating:', error.message);
        }
      });
    } else {
      this.apiService.submitRating(data).subscribe({
        next: (response) => {
          console.log(response.message);
          this.selectedRating = rating;
          this.infoMessage = `Saved your ${rating}/5 rating.`;
        },
        error: (error) => {
          console.error('Failed to submit rating:', error.message);
        }
      });
    }
  }

}
