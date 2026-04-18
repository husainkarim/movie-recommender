import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Movie, MovieSearchCriteria } from '../../model/movie.model';
import { MovieService } from '../../service/movie.service';
import { SearchBarComponent } from '../search-bar/search-bar.component';
import { ApiService } from '../../service/api.service';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [SearchBarComponent, RouterLink],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.scss'
})
export class HomePageComponent implements OnInit {
  movies: Movie[] = [];
  filteredMovies: Movie[] = [];
  genres: string[] = [];
  years: number[] = [];
  shareMessage = '';

  constructor(
    private readonly apiService: ApiService,
    private readonly movieService: MovieService
  ) {}

  ngOnInit(): void {
    this.apiService.getMovies().subscribe({
      next: (response) => {
        console.log(response.message);
        this.movies = response.movies;
        console.log(this.movies);
        this.genres = response.genres.map((g: any) => g.name).sort();
        this.movieService.getGenres(this.genres);
        this.years = response.years;
        this.movieService.getYears(this.years);
        this.filteredMovies = [...this.movies];
      }
    });
  }

  getGenres(movie: Movie): string {
    return movie.genres.map((g) => g.name).join(' • ') || '';
  }

  onSearchChanged(criteria: MovieSearchCriteria): void {
    this.filteredMovies = this.movieService.filterMovies(criteria, this.movies);
  }

  toggleWatchlist(movieId: string): void {
    this.movieService.toggleWatchlist(movieId);
  }

  isInWatchlist(movieId: string): boolean {
    return this.movieService.isInWatchlist(movieId);
  }

  shareMovie(movie: Movie): void {
    this.movieService.shareRecommendation(movie);
  }

}
