import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
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
  recommendations: Movie[] = [];
  readonly filter: RecommendationFilter = {
    genre: '',
    year: new Date().getFullYear()
  };
  shareMessage = '';

  constructor(private readonly movieService: MovieService) {
    this.updateRecommendations();
  }

  updateRecommendations(): void {
    this.recommendations = this.movieService.getRecommendations();
  }

  share(movie: Movie): void {
    this.movieService.shareRecommendation(movie);
  }

}
