import { Component, DestroyRef, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Movie, RecommendationFilter } from '../../model/movie.model';
import { MovieService } from '../../service/movie.service';

@Component({
  selector: 'app-recommendations-page',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './recommendations-page.component.html',
  styleUrl: './recommendations-page.component.scss'
})
export class RecommendationsPageComponent {
  private readonly destroyRef = inject(DestroyRef);

  recommendations: Movie[] = [];
  readonly filter: RecommendationFilter = {
    genre: '',
    year: new Date().getFullYear()
  };
  shareMessage = '';
  isLoading = true;

  constructor(private readonly movieService: MovieService) {
    this.movieService.recommendedMovies$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(recommendations => {
        this.recommendations = recommendations;
        this.isLoading = false;
      });

    this.updateRecommendations();
  }

  updateRecommendations(): void {
    this.isLoading = true;
    this.movieService.getRecommendations();
  }

  share(movie: Movie): void {
    this.movieService.shareRecommendation(movie);
    this.shareMessage = `Sharing ${movie.title}. A link copy fallback will be used when needed.`;
  }

}
