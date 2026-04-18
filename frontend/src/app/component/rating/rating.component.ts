import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Movie } from '../../model/movie.model';
import { MovieService } from '../../service/movie.service';
import { ApiService } from '../../service/api.service';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-rating',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './rating.component.html',
  styleUrl: './rating.component.scss'
})
export class RatingComponent implements OnInit {
  readonly ratingOptions = [1, 2, 3, 4, 5];
  movies: Movie[] = [];

  constructor(
    private readonly movieService: MovieService,
    private readonly apiService: ApiService,
    private readonly authService: AuthService
  ) { }

  ngOnInit(): void {
    this.loadMovies();
  }

  loadMovies(): void {
    this.apiService.getMovies().subscribe({
      next: (response) => {
        console.log(response.message);
        this.movies = response.movies;
      },
      error: (error) => {
        console.error('Failed to load movies:', error.message);
      }
    });
  }

  getGenres(movie: Movie): string {
    return movie.genres.map((g) => g.name).join(' • ') || '';
  }

  getUserRating(movie: Movie): number {
    return this.movieService.getUserRating(movie);
  }

  setRating(movieId: string, rating: number): void {
    let movie = this.movies.find(m => m.id === movieId);
    if (!movie) {
      console.error('Movie not found for rating:', movieId);
      return;
    }
    let selectedRating = this.getUserRating(movie);
    let data = {
      movieId: movieId,
      rating: rating,
      userId: this.authService.getUser().id
    }

    if (selectedRating === rating) {
      // If the same rating is selected again, remove the rating
      this.apiService.removeRating(data).subscribe({
        next: (response) => {
          console.log(response.message);
          selectedRating = 0;
        },
        error: (error) => {
          console.error('Failed to remove rating:', error.message);
        }
      });
    } else {
      this.apiService.submitRating(data).subscribe({
        next: (response) => {
          console.log(response.message);
          selectedRating = rating;
        },
        error: (error) => {
          console.error('Failed to submit rating:', error.message);
        }
      });
    }
    this.loadMovies(); // Refresh movies to update ratings
  }

}
