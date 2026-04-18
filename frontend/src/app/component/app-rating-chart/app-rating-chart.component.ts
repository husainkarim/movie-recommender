import { Component, Input } from '@angular/core';
import { Rate } from '../../model/movie.model';

@Component({
  selector: 'app-rating-chart',
  standalone: true,
  imports: [],
  templateUrl: './app-rating-chart.component.html',
  styleUrl: './app-rating-chart.component.scss'
})
export class AppRatingChartComponent {
  @Input({ required: true }) ratings: Rate[] = [];

  get totalVotes(): number {
    if (!this.ratings) {
      return 0;
    }
    return this.ratings.length;
  }

  get voteSummary(): Array<{ rating: number; count: number; percentage: number }> {
    const counts = new Map<number, number>();
    if (!this.ratings) {
      return [];
    }
    for (const vote of this.ratings) {
      counts.set(vote.rating, (counts.get(vote.rating) ?? 0) + 1);
    }

    return [5, 4, 3, 2, 1].map((rating) => {
      const count = counts.get(rating) ?? 0;
      return {
        rating,
        count,
        percentage: this.totalVotes ? (count / this.totalVotes) * 100 : 0
      };
    });
  }

}
