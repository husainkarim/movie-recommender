import { Component, DestroyRef, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Movie, WatchList } from '../../model/movie.model';
import { MovieService } from '../../service/movie.service';

@Component({
  selector: 'app-watchlist-page',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './watchlist-page.component.html',
  styleUrl: './watchlist-page.component.scss'
})
export class WatchlistPageComponent {
  private readonly destroyRef = inject(DestroyRef);

  watchlist: Movie[] = [];
  shareMessage = 'Your saved movies are ready.';

  constructor(private readonly movieService: MovieService) {
    this.movieService.watchlist$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(watchlist => {
        this.watchlist = watchlist.map((wl: WatchList) => wl.movie);
      });

    this.movieService.loadWatchlist();
  }

  removeFromWatchlist(movieId: string): void {
    const targetMovie = this.watchlist.find(movie => movie.id === movieId);
    this.movieService.toggleWatchlist(movieId);
    this.shareMessage = targetMovie
      ? `${targetMovie.title} removed from watchlist.`
      : 'Movie removed from watchlist.';
  }

  share(movie: Movie): void {
    this.movieService.shareRecommendation(movie);
    this.shareMessage = `Sharing ${movie.title}. Link copy fallback is enabled.`;
  }

}
