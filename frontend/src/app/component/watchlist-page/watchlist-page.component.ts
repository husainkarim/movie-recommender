import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Movie } from '../../model/movie.model';
import { MovieService } from '../../service/movie.service';

@Component({
  selector: 'app-watchlist-page',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './watchlist-page.component.html',
  styleUrl: './watchlist-page.component.scss'
})
export class WatchlistPageComponent {
  watchlist: Movie[] = [];
  shareMessage = '';

  constructor(private readonly movieService: MovieService) {
    this.refreshWatchlist();
  }

  removeFromWatchlist(movieId: number): void {
    this.movieService.toggleWatchlist(movieId);
    this.refreshWatchlist();
  }

  share(movieId: number): void {
    this.shareMessage = this.movieService.shareRecommendation(movieId);
  }

  private refreshWatchlist(): void {
    this.watchlist = this.movieService.Watchlist;
  }

}
